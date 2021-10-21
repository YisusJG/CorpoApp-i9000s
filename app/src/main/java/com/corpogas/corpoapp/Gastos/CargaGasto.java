package com.corpogas.corpoapp.Gastos;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioButton;
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
import com.corpogas.corpoapp.R;
import com.corpogas.corpoapp.Service.PrintBillService;
import com.corpogas.corpoapp.VentaCombustible.EligePrecioLitros;
import com.corpogas.corpoapp.VentaCombustible.VentaProductos;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CargaGasto extends AppCompatActivity {
    //Declaracion de variables
    ListView list;
    TextView txtDescripcion, txtClave;
    String isla,turno,usuario;
    TextView subTotal, iva, total, Descripcion;
    String EstacionId, sucursalId, ipEstacion, NumeroEmpleado, EmpleadoId;
    Boolean ConSinFactura;
    String idisla, idTurno;
    Bundle args = new Bundle();
    SQLiteBD db;
    DecimalFormat df = new DecimalFormat("#.00");
    ProgressDialog bar;
    RadioButton rbConFactura, rbSinFactura;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carga_gasto);
        db = new SQLiteBD(getApplicationContext());
        this.setTitle(db.getNombreEstacion() + " ( EST.:" + db.getNumeroEstacion() + ")");

        EstacionId = db.getIdEstacion();
        sucursalId = db.getIdSucursal();
        ipEstacion = db.getIpEstacion();
        NumeroEmpleado = db.getNumeroEmpleado();
        EmpleadoId = db.getUsuarioId();
        txtDescripcion = findViewById(R.id.txtDescripcion);
        txtClave = findViewById(R.id.txtClave);
        subTotal = findViewById(R.id.subTot);
        iva = findViewById(R.id.iva);
        total = findViewById(R.id.total);
        Descripcion = findViewById(R.id.descripcion);
        rbConFactura = (RadioButton) findViewById(R.id.rbConFactura);
        rbSinFactura = (RadioButton) findViewById(R.id.rbSinFactura);

        cargaTipoGastos();

    }

    private void cargaTipoGastos() {
        if (!Conexion.compruebaConexion(this)) {
            Toast.makeText(getBaseContext(), "Sin conexión a la red ", Toast.LENGTH_SHORT).show();
            Intent intent1 = new Intent(getApplicationContext(), Menu_Principal.class);
            startActivity(intent1);
            finish();
        } else {
            String url = "http://" + ipEstacion + "/CorpogasService/api/ConceptoGastos";
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    CargarGastos(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                }
            });
            RequestQueue requestQueue = Volley.newRequestQueue(this.getApplicationContext());
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(12000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        }
    }


    private void CargarGastos(String response){
        bar = new ProgressDialog(CargaGasto.this);
        bar.setTitle("Cargando Gastos");
        bar.setMessage("Ejecutando... ");
        bar.setIcon(R.drawable.gastos);
        bar.setCancelable(false);
        bar.show();

        //Declaracion de variables
        final List<String> ID;
        ID = new ArrayList<String>();

        final List<String> NombreProducto;
        NombreProducto = new ArrayList<String>();

        final List<String> PrecioProducto;
        PrecioProducto = new ArrayList<>();

        final List<String> ClaveProducto;
        ClaveProducto = new ArrayList();
        //ArrayList<singleRow> singlerow = new ArrayList<>();

        try {
            JSONArray productos = new JSONArray(response);
            for (int i = 0; i <productos.length() ; i++) {
                JSONObject p1 = productos.getJSONObject(i);
                String idtipoPago = p1.getString("Id");
                String DesLarga = p1.getString("DescripcionCorta");
                NombreProducto.add("ID: " + idtipoPago );
                ID.add(DesLarga);
                ClaveProducto.add(idtipoPago);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final ListAdapterProductos adapterP = new ListAdapterProductos(this,  ID, NombreProducto);
        list=(ListView)findViewById(R.id.lstPosicionCarga);
        list.setTextFilterEnabled(true);
        list.setAdapter(adapterP);
//        Agregado  click en la lista
        bar.cancel();
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String  Descripcion = ID.get(i).toString();
                //String precioUnitario = PrecioProducto.get(i).toString();
                String clave= ClaveProducto.get(i).toString();
                txtDescripcion.setText(Descripcion);
                txtClave.setText(clave);
            }
        });
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
        if (txtClave.length() == 0)       //.length() >0)
        {
            String titulo = "AVISO";
            String mensaje = "Seleccione uno de los tipos de gasto";
            Modales modales = new Modales(CargaGasto.this);
            View view1 = modales.MostrarDialogoAlertaAceptar(CargaGasto.this,mensaje,titulo);
            view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    modales.alertDialog.dismiss();
                }
            });
            //Toast.makeText(getApplicationContext(), "Seleccione al menos uno de los tipos de gasto", Toast.LENGTH_LONG).show();
        } else {
            if (Descripcion.length() == 0) {
                String titulo = "AVISO";
                String mensaje = "digite la descripción";
                Modales modales = new Modales(CargaGasto.this);
                View view1 = modales.MostrarDialogoAlertaAceptar(CargaGasto.this,mensaje,titulo);
                view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Descripcion.requestFocus();
                        modales.alertDialog.dismiss();
                    }
                });
                //Toast.makeText(getApplicationContext(), "Dijite una descripción", Toast.LENGTH_LONG).show();
            } else {
                if (subTotal.length() == 0) {
                    String titulo = "AVISO";
                    String mensaje = "digite el Subtotal";
                    Modales modales = new Modales(CargaGasto.this);
                    View view1 = modales.MostrarDialogoAlertaAceptar(CargaGasto.this,mensaje,titulo);
                    view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            subTotal.requestFocus();
                            modales.alertDialog.dismiss();
                        }
                    });

                    //Toast.makeText(getApplicationContext(), "Digite el Subtotal", Toast.LENGTH_LONG).show();
                } else {
                    if(iva.length()==0){
                        String titulo = "AVISO";
                        String mensaje = "digite el IVA";
                        Modales modales = new Modales(CargaGasto.this);
                        View view1 = modales.MostrarDialogoAlertaAceptar(CargaGasto.this,mensaje,titulo);
                        view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                iva.requestFocus();
                                modales.alertDialog.dismiss();
                            }
                        });
                    }else {
                        if(Double.parseDouble(iva.getText().toString())>= Double.parseDouble(subTotal.getText().toString())){
                            String titulo = "AVISO";
                            String mensaje = "El iva no puede ser mayor o igual que el subtotal cargado";
                            Modales modales = new Modales(CargaGasto.this);
                            View view1 = modales.MostrarDialogoAlertaAceptar(CargaGasto.this,mensaje,titulo);
                            view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    iva.requestFocus();
                                    modales.alertDialog.dismiss();
                                }
                            });

                        }else{
                            EnviarGastos();
                        }
                    }
                }
            }
        }
    }

    private void EnviarGastos() {
        if (!Conexion.compruebaConexion(this)) {
            Toast.makeText(getBaseContext(), "Sin conexión a la red ", Toast.LENGTH_SHORT).show();
            Intent intent1 = new Intent(getApplicationContext(), Menu_Principal.class);
            startActivity(intent1);
            finish();
        } else {
            Double totalAceptado;
            totalAceptado = Double.parseDouble(subTotal.getText().toString()) + Double.parseDouble(iva.getText().toString());
            if (totalAceptado>=2000) {
                String titulo = "AVISO";
                final Modales modales = new Modales(CargaGasto.this);
                View view1 = modales.MostrarDialogoCorrecto(CargaGasto.this,"Los gastos no pueden ser mayores a $2,000");
                view1.findViewById(R.id.buttonAction).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        modales.alertDialog.dismiss();
                    }
                });
            }else{


                if (rbConFactura.isChecked()){
                    ConSinFactura = true;
                }
                if (rbSinFactura.isChecked()){
                    ConSinFactura = false;
                }

                final String[] numeroticket = new String[1];

                turno = getIntent().getStringExtra("turno");

                String empleaddoAutoriza = getIntent().getStringExtra("empleadoid");
                final String empleado = getIntent().getStringExtra("empleado");

                String URL = "http://" + ipEstacion + "/CorpogasService/api/Gastos/numeroEmpleado/" + NumeroEmpleado + "/empleadoAutoriza/" + empleaddoAutoriza + "/dispositivoId/" + db.getIdTarjtero();
                final JSONObject mjason = new JSONObject();
                RequestQueue queue = Volley.newRequestQueue(this);
                try {
                    mjason.put("SucursalId", sucursalId);
                    mjason.put("EstacionId", "");
                    mjason.put("TurnoId", turno); //turno.getText().toString());
                    mjason.put("ConceptoGastoId", txtClave.getText().toString());
                    mjason.put("Descripcion", Descripcion.getText().toString());
                    mjason.put("Subtotal", subTotal.getText().toString());
                    mjason.put("Iva", iva.getText().toString());
                    mjason.put("Factura", ConSinFactura);
                    mjason.put("EstatusId", 1);
//                    mjason.put("FechaTrabajo", "2021-10-18 00:00:00.000");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JsonObjectRequest request_json = new JsonObjectRequest(Request.Method.POST, URL, mjason, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            String correcto = response.getString("Correcto");
                            String mensajes = response.getString("Mensaje");
                            if (correcto.equals("true")) {
                                String objetorespuesta = response.getString("ObjetoRespuesta");
                                JSONObject respuestaobjeto = new JSONObject(objetorespuesta);
                                String titulo = "AVISO";
                                String mensaje = "Gasto cargado exitosamente";
                                final Modales modales = new Modales(CargaGasto.this);
                                View view1 = modales.MostrarDialogoCorrecto(CargaGasto.this, mensaje);
                                view1.findViewById(R.id.buttonAction).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        modales.alertDialog.dismiss();
                                        Intent intent = new Intent(getApplicationContext(), Menu_Principal.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                });

                            } else {
                                String titulo = "AVISO";
                                String mensaje = mensajes;
                                final Modales modales = new Modales(CargaGasto.this);
                                View view1 = modales.MostrarDialogoError(CargaGasto.this, mensaje);
                                view1.findViewById(R.id.buttonAction).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        modales.alertDialog.dismiss();
                                    }
                                });

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

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

//        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                request_json.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                queue.add(request_json);
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