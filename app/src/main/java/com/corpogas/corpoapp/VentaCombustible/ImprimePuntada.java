package com.corpogas.corpoapp.VentaCombustible;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
import com.android.volley.toolbox.Volley;
import com.corpogas.corpoapp.Conexion;
import com.corpogas.corpoapp.Configuracion.SQLiteBD;
import com.corpogas.corpoapp.Menu_Principal;
import com.corpogas.corpoapp.Modales.Modales;
import com.corpogas.corpoapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.HashMap;
import java.util.Map;

public class ImprimePuntada extends AppCompatActivity {
    SQLiteBD data;
    String EstacionId, sucursalId, ipEstacion;
    String Usuarioid , CargaPosicion, UsuarioClave, FormaPagoId, NombrePago, OperativaId, nombreCompletoVenta,  numeroempleado, lugarProviene;
    Double MontoEnCanasta;
    JSONObject FormasPagoObjecto;
    JSONArray FormasPagoArreglo;
    Integer TipoTransacionImprimir;
    String fechaTicket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imprime_puntada);
        data = new SQLiteBD(getApplicationContext());
        this.setTitle(data.getNombreEstacion() + " ( EST.:" + data.getNumeroEstacion() + ")");
        sucursalId=data.getIdSucursal();
        EstacionId = data.getIdEstacion();
        ipEstacion = data.getIpEstacion();
        numeroempleado = data.getNumeroEmpleado();
        Usuarioid = data.getUsuarioId();
        CargaPosicion = getIntent().getStringExtra("posicioncarga");
        UsuarioClave = data.getClave();
        OperativaId = getIntent().getStringExtra("idoperativa");
        FormaPagoId = getIntent().getStringExtra("idformapago");
        NombrePago = getIntent().getStringExtra("nombrepago");
        nombreCompletoVenta = data.getNombreCompleto();
        MontoEnCanasta= getIntent().getDoubleExtra("montoencanasta", 0);
        lugarProviene = getIntent().getStringExtra("lugarProviene");

        if (lugarProviene.equals("DiferentesFormasPago")){
            EnviaArregloDiferentesFormasPagos();
        }else{
            Moduloimprime();
        }
    }

    private void Moduloimprime(){
        if (!Conexion.compruebaConexion(this)) {
            Toast.makeText(getBaseContext(), "Sin conexión a la red ", Toast.LENGTH_SHORT).show();
            Intent intent1 = new Intent(getApplicationContext(), Menu_Principal.class);
            startActivity(intent1);
            finish();
        } else {
            FormasPagoObjecto = new JSONObject();
            FormasPagoArreglo = new JSONArray();
            try {
                FormasPagoObjecto.put("Id", FormaPagoId);
                FormasPagoObjecto.put("Importe", MontoEnCanasta);
                FormasPagoArreglo.put(FormasPagoObjecto);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JSONObject datos = new JSONObject();
            String url = "http://" + ipEstacion + "/CorpogasService/api/tickets/generar";
            RequestQueue queue = Volley.newRequestQueue(this);
            try {
                datos.put("PosicionCargaId", CargaPosicion);
                datos.put("IdUsuario", numeroempleado);
                datos.put("SucursalId", sucursalId);
                datos.put("IdFormasPago", FormasPagoArreglo);
                datos.put("SucursalId", sucursalId);
                datos.put("ConfiguracionAplicacionId", data.getIdTarjtero());

            } catch (JSONException e) {
                e.printStackTrace();
            }

            JsonObjectRequest request_json = new JsonObjectRequest(Request.Method.POST, url, datos, new Response.Listener<JSONObject>() {
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
                                Modales modales = new Modales(ImprimePuntada.this);
                                View view1 = modales.MostrarDialogoError(ImprimePuntada.this, mensaje);
                                view1.findViewById(R.id.buttonAction).setOnClickListener(new View.OnClickListener() { //buttonYes
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
                            Modales modales = new Modales(ImprimePuntada.this);
                            View view1 = modales.MostrarDialogoCorrecto(ImprimePuntada.this, "Ticket enviado a impresora central");
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
                    Toast.makeText(ImprimePuntada.this, "error", Toast.LENGTH_SHORT).show();

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
                    return Response.success(datos, HttpHeaderParser.parseCacheHeaders(response));
                }
            };
//            queue.add(request_json);
            request_json.setRetryPolicy(new DefaultRetryPolicy(900000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            queue.add(request_json);
        }
    }

    private void EnviaArregloDiferentesFormasPagos() {
        try {
            FormasPagoArreglo = new JSONArray(FormaPagoId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (!Conexion.compruebaConexion(this)) {
            Toast.makeText(getBaseContext(), "Sin conexión a la red ", Toast.LENGTH_SHORT).show();
            Intent intent1 = new Intent(getApplicationContext(), Menu_Principal.class);
            startActivity(intent1);
            finish();
        } else {

            JSONObject datos = new JSONObject();
            String url = "http://" + ipEstacion + "/CorpogasService/api/tickets/generar";
            RequestQueue queue = Volley.newRequestQueue(this);
            try {
                datos.put("PosicionCargaId", CargaPosicion);
                datos.put("IdUsuario", numeroempleado);
                datos.put("SucursalId", sucursalId);
                datos.put("IdFormasPago", FormasPagoArreglo);
                datos.put("SucursalId", sucursalId);
                datos.put("ConfiguracionAplicacionId", data.getIdTarjtero());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JsonObjectRequest request_json = new JsonObjectRequest(Request.Method.POST, url, datos, new Response.Listener<JSONObject>() {
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
                                Modales modales = new Modales(ImprimePuntada.this);
                                View view1 = modales.MostrarDialogoError(ImprimePuntada.this, mensaje);
                                view1.findViewById(R.id.buttonAction).setOnClickListener(new View.OnClickListener() { //buttonYes
                                    @Override
                                    public void onClick(View view) {
                                        data.getWritableDatabase().delete("PagoTarjetaDiferentesFormasPago", null, null);
                                        data.close();
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
                            Modales modales = new Modales(ImprimePuntada.this);
                            View view1 = modales.MostrarDialogoCorrecto(ImprimePuntada.this, "Ticket enviado a impresora central");
                            view1.findViewById(R.id.buttonAction).setOnClickListener(new View.OnClickListener() { //buttonYes
                                @Override
                                public void onClick(View view) {
                                    data.getWritableDatabase().delete("PagoTarjetaDiferentesFormasPago", null, null);
                                    data.close();
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
                    Toast.makeText(ImprimePuntada.this, "error", Toast.LENGTH_SHORT).show();

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
                    return Response.success(datos, HttpHeaderParser.parseCacheHeaders(response));
                }
            };
//            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            request_json.setRetryPolicy(new DefaultRetryPolicy(900000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            queue.add(request_json);

        }
    }


}