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
import com.corpogas.corpoapp.Menu_Principal;
import com.corpogas.corpoapp.Modales.Modales;
import com.corpogas.corpoapp.R;
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

public class FormaPagoTarjetasBancarias extends AppCompatActivity {
    SQLiteBD data;
    String formaPagoId, poscionCarga, total, EstacionId,  ipEstacion,  sucursalId, numeroempleado, provieneeFPoDFP, banderapuntada, numeroTarjeta;
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
                imprimePagoTarjetaBancaria();
            }else{
                data.getWritableDatabase().delete("PagoTarjeta", null, null);
                data.close();
                finish();
            }
//        }else{ //Primero Actualiza a puntada acumular y luego imprime
//            predeterminarPuntadaAcumular();
//        }

        btnimprimeventas = (Button) findViewById(R.id.btnimprimeventas);
        btnimprimeventas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imprimePagoTarjetaBancaria();
            }
        });
    }

    private void imprimePagoTarjetaBancaria(){
        if (!Conexion.compruebaConexion(this)) {
            Toast.makeText(getBaseContext(), "Sin conexión a la red ", Toast.LENGTH_SHORT).show();
            Intent intent1 = new Intent(getApplicationContext(), Menu_Principal.class);
            startActivity(intent1);
            finish();
        } else {
            JSONObject TramaBancariaDetalle = new JSONObject();
            try {
                TramaBancariaDetalle.put("SucursalId", 497);
                TramaBancariaDetalle.put("EstacionId", 251);
                TramaBancariaDetalle.put(        "RESPONSE_CODe", "00");
                TramaBancariaDetalle.put(        "POS_ID", "POSANDROID");
                TramaBancariaDetalle.put(        "Tag_9B", "E800");
                TramaBancariaDetalle.put(       "Tag_9F26", "A76488169348EBAB");
                TramaBancariaDetalle.put(        "CARD_TYPE", 1);
                TramaBancariaDetalle.put(        "MNEMO_NAME", "Mc");
                TramaBancariaDetalle.put(        "TERMINAL_ID", "POSANDROID");
                TramaBancariaDetalle.put(        "APP_LABEL","Debit MasterCard");
                TramaBancariaDetalle.put(        "TAG_50", "Debit MasterCard");
                TramaBancariaDetalle.put(        "Tag_95", "0000008000");
                TramaBancariaDetalle.put(        "CARD_HOLDER_NAME","ERICK/AGUILA MARTINEZ ");
                TramaBancariaDetalle.put(        "HEADER_1", "SmartPaymentServices");
                TramaBancariaDetalle.put(        "AMOUNT", "6666.89");
                TramaBancariaDetalle.put(        "PREFERRED_NAME", "Debit MasterCard");
                TramaBancariaDetalle.put(        "ACCOUNT_NUMBEr", "1234 5986 1250 4521");
                TramaBancariaDetalle.put(        "AiD", "A0000000041010");
                TramaBancariaDetalle.put(        "SIGNATURE_FLAG", 0);
                TramaBancariaDetalle.put(        "ArQc","A76488169348EBAB");
                TramaBancariaDetalle.put(        "Footer_1", "Pagaré negociable únicamente");
                TramaBancariaDetalle.put(        "TXN_NAME", "VENTA EN LINEA");
                TramaBancariaDetalle.put(        "TXN_TIME","162439");
                TramaBancariaDetalle.put(        "FOOTER_3", "nada");
                TramaBancariaDetalle.put(        "FOOTER_2", "con instituciones de crédito");
                TramaBancariaDetalle.put(        "TAG_9F12", "Debit MasterCard");
                TramaBancariaDetalle.put(        "TXN_APPROVAL_CODE", "420557");
                TramaBancariaDetalle.put(        "Tag_9F34", "440302");
                TramaBancariaDetalle.put(        "CARD_NAME", "Mastercard");
                TramaBancariaDetalle.put(        "Header_2", "Av de los Insurgentes Sur 2453");
                TramaBancariaDetalle.put(        "HEADER_3", "Tizapán San Ángel Tel:55509935");
                TramaBancariaDetalle.put(        "HEADER_4", "Álvaro Obregón, 01090 CDMX");
                TramaBancariaDetalle.put(        "TSN","162544");
                TramaBancariaDetalle.put(        "EXPIRATION_DATE", "**");
                TramaBancariaDetalle.put(        "ENTRY_MODE","05");
                TramaBancariaDetalle.put(        "BRAND_NAME:", "l");
                TramaBancariaDetalle.put(        "Tag_5F2A","0484");
                TramaBancariaDetalle.put(        "TXN_DATE", "210909");
                TramaBancariaDetalle.put(        "SG_REFERENCE", "000036578535");
                TramaBancariaDetalle.put(        "TiP",0.8);
                TramaBancariaDetalle.put(        "FOOTER_4", "nada2");
                TramaBancariaDetalle.put(        "TOTAL_AMOUNT", "6666.97");
                TramaBancariaDetalle.put(        "BANK_NAME", " ALQUIMIADIGITAL.MX");
                TramaBancariaDetalle.put("MERCHANT_ID", "7550587");

            } catch (JSONException e) {
                e.printStackTrace();
            }

            JSONObject FormasPagoObjecto = new JSONObject();
            JSONArray FormasPagoArreglo = new JSONArray();
            try {
                FormasPagoObjecto.put("Id", formaPagoId);
                FormasPagoObjecto.put("Importe", total);
                FormasPagoObjecto.put("TramaBancariaDetalle", TramaBancariaDetalle);
                FormasPagoArreglo.put(FormasPagoObjecto);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JSONObject datos = new JSONObject();
            String url = "http://" + ipEstacion + "/CorpogasService/api/tickets/generar";
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
                    Toast.makeText(FormaPagoTarjetasBancarias.this, "error", Toast.LENGTH_SHORT).show();

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
                                imprimePagoTarjetaBancaria();

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