package com.corpogas.corpoapp.VentaCombustible;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.corpogas.corpoapp.Entities.Accesos.AccesoUsuario;
import com.corpogas.corpoapp.Entities.Classes.RespuestaApi;
import com.corpogas.corpoapp.Interfaces.Endpoints.EndPoints;
import com.corpogas.corpoapp.Menu_Principal;
import com.corpogas.corpoapp.Modales.Modales;
import com.corpogas.corpoapp.PruebasEndPoint;
import com.corpogas.corpoapp.R;
import com.corpogas.corpoapp.Token.GlobalToken;
import com.corpogas.corpoapp.ValesPapel.ValesPapel;
import com.corpogas.corpoapp.VentaPagoTarjeta;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FormaPagoTarjetasBancarias extends AppCompatActivity {
    SQLiteBD data;
    String formaPagoId, poscionCarga, total, EstacionId,  ipEstacion,  sucursalId, numeroempleado, provieneeFPoDFP, banderapuntada, numeroTarjeta, responseSmart;
    String FormaPago = "1", FormaPagoMixta = "2";
    EditText amount;
    Button btnimprimeventas;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forma_pago_tarjetas_bancarias);
        data = new SQLiteBD(getApplicationContext());



        formaPagoId = data.getformapagoid(); //intent.getStringExtra("formapagoid")
        poscionCarga = data.getposcioncarga(); //intent.getStringExtra("posicioncarga")
        total = data.getmonto(); //intent.getStringExtra("montoencanasta")
        provieneeFPoDFP = data.getlugarformapagodiferentes();
        banderapuntada = data.getbanderapuntada();
        EstacionId = data.getIdEstacion();
        ipEstacion = data.getIpEstacion();
        sucursalId = data.getIdSucursal();
        numeroempleado = data.getNumeroEmpleado();
        numeroTarjeta = getIntent().getStringExtra("numeroTarjeta");
        amount = (EditText) findViewById(R.id.amountventa);
        amount.setText("$"+total);
//        if (banderapuntada.equals("0")) { //no actuiza a puntada acumular
            if (provieneeFPoDFP.equals("1")){
                responseSmart = data.getresponseFormaPago();
                imprimePagoTarjetaBancaria(1);
            }else{
//                data.updateDiferentesFormasPago("asdasdasdasd", "1", formaPagoId);
                responseSmart = data.getresponseFPD(Integer.parseInt(formaPagoId));

                finish();
            }
//        }else{ //Primero Actualiza a puntada acumular y luego imprime
//            predeterminarPuntadaAcumular();
//        }

        btnimprimeventas = (Button) findViewById(R.id.btnimprimeventas);
        btnimprimeventas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imprimePagoTarjetaBancaria(2);
            }
        });
    }


    private void imprimePagoTarjetaBancaria(Integer identificador){
        if (!Conexion.compruebaConexion(this)) {
            Toast.makeText(getBaseContext(), "Sin conexión a la red ", Toast.LENGTH_SHORT).show();
            Intent intent1 = new Intent(getApplicationContext(), Menu_Principal.class);
            startActivity(intent1);
            finish();
        } else {
            JSONObject TramaBancariaDetalle = new JSONObject();
//            try {
//                TramaBancariaDetalle.put("SucursalId", 497);
//                TramaBancariaDetalle.put("EstacionId", 251);
//                TramaBancariaDetalle.put("TRAMA", responseSmart);

                if (provieneeFPoDFP.equals("1")){
                }else{
//                    data.updateDiferentesFormasPago(TramaBancariaDetalle.toString(), "1" , formaPagoId );
                    data.updateDiferentesFormasPago(TramaBancariaDetalle.toString(), "1" , formaPagoId );
                }

//            } catch (JSONException e) {
//                e.printStackTrace();
//            }

            JSONObject FormasPagoObjecto = new JSONObject();
            JSONArray FormasPagoArreglo = new JSONArray();
            try {
                FormasPagoObjecto.put("Id", formaPagoId);
                FormasPagoObjecto.put("Importe", total);
                FormasPagoObjecto.put("Trama", responseSmart); //TramaBancariaDetalle
                FormasPagoArreglo.put(FormasPagoObjecto);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JSONObject datos = new JSONObject();
//            String url = "http://" + ipEstacion + "/CorpogasService/api/tickets/generar";
            String url = "http://" + ipEstacion + "/CorpogasService_entities_token/api/tickets/generar";

            RequestQueue queue = Volley.newRequestQueue(this);
            try {
                datos.put("PosicionCargaId", poscionCarga);
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
                                Modales modales = new Modales(FormaPagoTarjetasBancarias.this);
                                View view1 = modales.MostrarDialogoError(FormaPagoTarjetasBancarias.this, mensaje);
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
                            Modales modales = new Modales(FormaPagoTarjetasBancarias.this);
                            View view1 = modales.MostrarDialogoCorrecto(FormaPagoTarjetasBancarias.this, "Ticket Impreso en Impresora Central");
                            view1.findViewById(R.id.buttonAction).setOnClickListener(new View.OnClickListener() { //buttonYes
                                @Override
                                public void onClick(View view) {
                                    data.getWritableDatabase().delete("PagoTarjeta", null, null);
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
                    if (identificador == 1) {
                        if (error.networkResponse.statusCode == 401) {
                            GlobalToken.errorTokenWithReload(FormaPagoTarjetasBancarias.this);
                        } else {
                            Toast.makeText(FormaPagoTarjetasBancarias.this, error.toString(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        if (error.networkResponse.statusCode == 401) {
                            GlobalToken.errorToken(FormaPagoTarjetasBancarias.this);
                        } else {
                            Toast.makeText(FormaPagoTarjetasBancarias.this, error.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }

                }
            }) {
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<String, String>();
                    headers.put("Authorization", data.getToken());
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
            queue.add(request_json);
        }

    }

    private void predeterminarPuntadaAcumular(){
        if (!Conexion.compruebaConexion(this)) {
            Toast.makeText(getBaseContext(), "Sin conexión a la red ", Toast.LENGTH_SHORT).show();
            Intent intent1 = new Intent(getApplicationContext(), Menu_Principal.class);

            startActivity(intent1);
            finish();
        } else {
            JSONObject datos = new JSONObject();

            String idoperativa = getIntent().getStringExtra("idoperativa");
            JSONArray myArray = new JSONArray();
            SQLiteBD data = new SQLiteBD(getApplicationContext());
            String url = "http://" + data.getIpEstacion() + "/CorpogasService/api/puntadas/actualizaPuntos/numeroEmpleado/" + numeroempleado;
            RequestQueue queue = Volley.newRequestQueue(this);
            try {
                datos.put("NuTarjetero", data.getIdTarjtero() );
                datos.put("SucursalId", sucursalId);
                datos.put("RequestID", 35); // Esto es para cuando se termina de realizar el despacho, es pasa la tarjeta puntada y se acumula
                datos.put("PosicionCarga", poscionCarga); //posicionCarga
                datos.put("Tarjeta", numeroTarjeta);
                //datos.put("NIP", NipCliente);
                datos.put("Productos", myArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JsonObjectRequest request_json = new JsonObjectRequest(Request.Method.POST, url, datos, new com.android.volley.Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    String estado = null;
                    String mensaje = null;
                    try {
                        estado = response.getString("Estado");
                        mensaje = response.getString("Mensaje");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (estado.equals("true")) {


                        switch (formaPagoId) {
                            case "13"://VALES ELECTRONICOS
                            case "3": //AMEX
                            case "5": //VISA
                                imprimePagoTarjetaBancaria(2);

                                break;
                            default:
                        }
                    } else {
                        try {
                            String titulo = "AVISO";
                            String mensajes = "" + mensaje;
                            Modales modales = new Modales(FormaPagoTarjetasBancarias.this);
                            View view1 = modales.MostrarDialogoAlertaAceptar(FormaPagoTarjetasBancarias.this, mensajes, titulo);
                            view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    modales.alertDialog.dismiss();
                                    Intent intente = new Intent(getApplicationContext(), Menu_Principal.class);
                                    startActivity(intente);
                                    finish();
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();

                        }
                    }
                }
            }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {


                }
            }) {
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<String, String>();
                    return headers;
                }

                protected com.android.volley.Response<JSONObject> parseNetwokResponse(NetworkResponse response) {
                    if (response != null) {

                        try {
                            String responseString;
                            JSONObject datos = new JSONObject();
                            responseString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));

                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                    return com.android.volley.Response.success(datos, HttpHeaderParser.parseCacheHeaders(response));
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            request_json.setRetryPolicy(new DefaultRetryPolicy(500000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            queue.add(request_json);

        }
    }


}