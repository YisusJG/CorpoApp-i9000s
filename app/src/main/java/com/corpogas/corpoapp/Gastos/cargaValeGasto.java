package com.corpogas.corpoapp.Gastos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.corpogas.corpoapp.Conexion;
import com.corpogas.corpoapp.Configuracion.SQLiteBD;
import com.corpogas.corpoapp.Menu_Principal;
import com.corpogas.corpoapp.Modales.Modales;
import com.corpogas.corpoapp.ObtenerClave.ClaveEmpleado;
import com.corpogas.corpoapp.R;
import com.corpogas.corpoapp.Service.PrintBillService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class cargaValeGasto extends AppCompatActivity {
    ListView list;
    TextView txtDescripcion, txtClave, isla, turno, usuario;
    TextView SubTotal, Descripcion;
    String EstacionId, sucursalId, ipEstacion ;
    String idisla, idTurno;
    Bundle args = new Bundle();
    SQLiteBD db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carga_vale_gasto);
        db = new SQLiteBD(getApplicationContext());
        this.setTitle(db.getNombreEstacion()  + " ( EST.:" + db.getNumeroEstacion() + ")");

        EstacionId = db.getIdEstacion();
        sucursalId=db.getIdSucursal();
        ipEstacion = db.getIpEstacion();
        SubTotal =findViewById(R.id.subTot);
        Descripcion = findViewById(R.id.descripcion);

        String islaId = getIntent().getStringExtra("isla");
        String turnoId = getIntent().getStringExtra("turno");

    }


    private void EnviarGastos() {
        if (!Conexion.compruebaConexion(this)) {
            Toast.makeText(getBaseContext(), "Sin conexión a la red ", Toast.LENGTH_SHORT).show();
            Intent intent1 = new Intent(getApplicationContext(), Menu_Principal.class);
            startActivity(intent1);
            finish();
        } else {
            final String turnoId = getIntent().getStringExtra("turno");

            String url = "http://" + ipEstacion + "/CorpogasService/api/Turnos/fechaTrabajo/sucursal/" + sucursalId + "/turno/" + turnoId;
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject resultadorespuesta = new JSONObject(response);
                        String valor = resultadorespuesta.getString("ObjetoRespuesta");
                        GuardarGasto(valor, turnoId);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                }
            });
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(12000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        }
    }


    private void GuardarGasto(String fechatrabajo, String turnoId) {
        if (!Conexion.compruebaConexion(this)) {
            Toast.makeText(getBaseContext(), "Sin conexión a la red ", Toast.LENGTH_SHORT).show();
            Intent intent1 = new Intent(getApplicationContext(), Menu_Principal.class);
            startActivity(intent1);
            finish();
        } else {

            String islaId = getIntent().getStringExtra("isla");
            Date date = new Date();
            String fechaTrabajo = fechatrabajo;

            String empleadoId = getIntent().getStringExtra("empleadoid");
            final String empleado = getIntent().getStringExtra("empleado");

            String URL = "http://" + ipEstacion + "/CorpogasService/api/CajaChicas";
            final JSONObject mjason = new JSONObject();
            RequestQueue queue = Volley.newRequestQueue(this);
            try {
                mjason.put("EstacionId", EstacionId);
                mjason.put("TurnoId", turnoId); //turno.getText().toString());
                mjason.put("TurnoSucursalId", sucursalId); //turno.getText().toString());Sucursal
                mjason.put("IslaId", islaId);
                mjason.put("IslaEstacionId", EstacionId);
                mjason.put("FechaTrabajo", fechaTrabajo);
                mjason.put("Descripcion", Descripcion.getText().toString());
                mjason.put("Importe", SubTotal.getText().toString());
                mjason.put("SucursalEmpleadoId", empleadoId);
                mjason.put("SucursalEmpleadoSucursalId", sucursalId);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JsonObjectRequest request_json = new JsonObjectRequest(Request.Method.POST, URL, mjason, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    //Toast.makeText(getApplicationContext(),"Gasto Cargado Exitosamente",Toast.LENGTH_LONG).show();
                    String titulo = "AVISO";
                    String mensaje = "Gasto Cargado Exitosamente";
                    final Modales modales = new Modales(cargaValeGasto.this);
                    View view1 = modales.MostrarDialogoCorrecto(cargaValeGasto.this, mensaje);
                    view1.findViewById(R.id.buttonAction);
                    view1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            modales.alertDialog.dismiss();
                            String numeroticket = null;
                            try {
                                numeroticket = response.getString("Id");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Intent intentService = new Intent( cargaValeGasto.this, PrintBillService.class);
                            intentService.putExtra("SPRT", "expenses");
                            intentService.putExtra("Subtotal", SubTotal.getText().toString());
                            intentService.putExtra("Iva", "0");
                            intentService.putExtra("Descripcion", Descripcion.getText().toString());
                            intentService.putExtra("NumeroTicket", numeroticket);
                            intentService.putExtra("NombreEmpleado", empleado);
                            intentService.putExtra("DescripcionGasto", "CAJA CHICA");
                            intentService.putExtra("TipoGasto", "CajaChica");
                            startService(intentService);
                            //ObtenerCuerpoTicket(SubTotal.getText().toString(), Descripcion.getText().toString(), numeroticket, empleado);
                        }
                    });
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "Sin Conexión con el servidor", Toast.LENGTH_LONG).show();
                }
            }) {
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<String, String>();
                    return headers;
                }

                protected Response<JSONObject> parseNetwokResponse(NetworkResponse response) {
                    if (response != null) {

                        try {
                            String responseString;
                            JSONObject datos = new JSONObject();
                            responseString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));

                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                    return Response.success(mjason, HttpHeaderParser.parseCacheHeaders(response));
                }
            };
            queue.add(request_json);

        }
    }

    //procedimiento para  cachar el Enter del teclado
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_ENTER:
                calculos();
                return true;
            default:
                return super.onKeyUp(keyCode, event);
        }
    }

    private void calculos() {
        if (Descripcion.length()==0 )       //.length() >0)
        {
            String titulo = "AVISO";
            String mensaje = "digite la descripción";
            Modales modales = new Modales(cargaValeGasto.this);
            View view1 = modales.MostrarDialogoAlertaAceptar(cargaValeGasto.this,mensaje,titulo);
            view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Descripcion.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(Descripcion, InputMethodManager.SHOW_IMPLICIT);

                    modales.alertDialog.dismiss();
                }
            });

            //Toast.makeText(getApplicationContext(), "digite el importe", Toast.LENGTH_LONG).show();
        } else { if (SubTotal.length()==0){
            String titulo = "AVISO";
            String mensaje = "digite el importe";
            Modales modales = new Modales(cargaValeGasto.this);
            View view1 = modales.MostrarDialogoAlertaAceptar(cargaValeGasto.this,mensaje,titulo);
            view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SubTotal.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(Descripcion, InputMethodManager.SHOW_FORCED);
                    modales.alertDialog.dismiss();
                }
            });
        }else {
            EnviarGastos();
        }
        }
    }








    //Metodo para regresar a la actividad principal
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), Menu_Principal.class);
        startActivity(intent);
        finish();
    }


}