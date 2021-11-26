package com.corpogas.corpoapp.Tickets;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.corpogas.corpoapp.Configuracion.SQLiteBD;
import com.corpogas.corpoapp.Menu_Principal;
import com.corpogas.corpoapp.Modales.Modales;
import com.corpogas.corpoapp.R;
import com.corpogas.corpoapp.TanqueLleno.MyListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class FormaPagoAutojarreo extends AppCompatActivity {
    Button listFormaPagoA;
    String formadePago = "Autojarreo", formadePagoId = "92", EstacionId, sucursalId, ipEstacion;
    SQLiteBD data;
    ListView list;
    String[] maintitle ={
            "Autojarreo",
    };

//    String[] subtitle ={
//            "Posición de Carga en Autojarreo",
//    };

    Integer[] imgid={
            R.drawable.autojarreo,
    };

    JSONObject FormasPagoObjecto ;
    JSONArray FormasPagoArreglo;
    String MontoCanasta;
    Long posicioncargaInterno, posicioncargaid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forma_pago_autojarreo);
        data = new SQLiteBD(this);
        this.setTitle(data.getNombreEstacion() + " ( EST.:" + data.getNumeroEstacion() + ")");
        EstacionId = data.getIdEstacion();
        sucursalId = data.getIdSucursal();
        ipEstacion = data.getIpEstacion();
        posicioncargaid = getIntent().getLongExtra("posicioncarga",0);
        MontoCanasta = getIntent().getStringExtra("montocanasta");
        posicioncargaInterno = getIntent().getLongExtra("posicioncargainterno",0);
        String[] subtitle ={
                "Posición de Carga: "+ posicioncargaInterno,
        };

        MyListAdapter adapter=new MyListAdapter(FormaPagoAutojarreo.this, maintitle, subtitle,imgid);
        list= findViewById(R.id.listFormaPagoA);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                ImprimeTiket();
            }
        });
    }

    private void ImprimeTiket() {
        JSONObject FormasPagoObjecto = new JSONObject();
        JSONArray FormasPagoArreglo = new JSONArray();
        String valor;

        try {
            FormasPagoObjecto.put("Id", formadePagoId);
            FormasPagoObjecto.put("Importe", Double.parseDouble(MontoCanasta));
            FormasPagoArreglo.put(FormasPagoObjecto);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONObject datos = new JSONObject();
        String url = "http://" + ipEstacion + "/CorpogasService/api/tickets/generar";
        RequestQueue queue = Volley.newRequestQueue(this);
        try {
            datos.put("PosicionCargaId", posicioncargaid);
            datos.put("IdUsuario", data.getNumeroEmpleado());
            datos.put("SucursalId", sucursalId);
            datos.put("IdFormasPago", FormasPagoArreglo);
            datos.put("ConfiguracionAplicacionId", data.getIdTarjtero());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request_json = new JsonObjectRequest(Request.Method.POST, url,  datos, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                String detalle = null;
                try {
                    detalle = response.getString("Detalle");
                    if (detalle.equals("null")) {
                        String estado1 = response.getString("Resultado");
                        JSONObject descripcion = new JSONObject(estado1);
                        String estado = descripcion.getString("Descripcion");
                        try {
                            String titulo = "AVISO";
                            String mensaje = estado;
                            Modales modales = new Modales(FormaPagoAutojarreo.this);
                            View view1 = modales.MostrarDialogoError(FormaPagoAutojarreo.this, mensaje);
                            view1.findViewById(R.id.buttonAction).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(getApplicationContext(), Menu_Principal.class);
                                    startActivity(intent);
                                    finish();
                                    modales.alertDialog.dismiss();
                                }
                            });
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    } else {
                        String titulo = "AVISO";
                        Modales modales = new Modales(FormaPagoAutojarreo.this);
                        View view1 = modales.MostrarDialogoCorrecto(FormaPagoAutojarreo.this, "Ticket enviado a impresora central");
                        view1.findViewById(R.id.buttonAction).setOnClickListener(new View.OnClickListener() { //buttonYes
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(getApplicationContext(), Menu_Principal.class);
                                startActivity(intent);
                                finish();
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
                Toast.makeText(FormaPagoAutojarreo.this, "error", Toast.LENGTH_SHORT).show();

            }
        }){
            public Map<String,String> getHeaders() throws AuthFailureError {
                Map<String,String> headers = new HashMap<String, String>();
                return headers;
            }
            protected Response<JSONObject> parseNetwokResponse(NetworkResponse response){
                if (response != null){
                    try {
                        String responseString;
                        JSONObject datos = new JSONObject();
                        responseString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));

                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
                return Response.success(datos, HttpHeaderParser.parseCacheHeaders(response));
            }
        };
        queue.add(request_json);
    }

    //Metodo para regresar a la actividad principal
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), Menu_Principal.class);
        startActivity(intent);
        finish();
    }


}