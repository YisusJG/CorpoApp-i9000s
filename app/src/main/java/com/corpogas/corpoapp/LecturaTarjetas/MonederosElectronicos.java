package com.corpogas.corpoapp.LecturaTarjetas;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.corpogas.corpoapp.Conexion;
import com.corpogas.corpoapp.Configuracion.SQLiteBD;
import com.corpogas.corpoapp.Entities.Catalogos.Bin;
import com.corpogas.corpoapp.Entities.Classes.RespuestaApi;
import com.corpogas.corpoapp.Menu_Principal;
import com.corpogas.corpoapp.Modales.Modales;
import com.corpogas.corpoapp.Puntada.PosicionPuntadaRedimir;
import com.corpogas.corpoapp.Puntada.ProductosARedimir;
import com.corpogas.corpoapp.Puntada.SeccionTarjeta;
import com.corpogas.corpoapp.R;
import com.corpogas.corpoapp.Interfaces.Endpoints.EndPoints;
import com.corpogas.corpoapp.Service.MagReadService;
import com.corpogas.corpoapp.TanqueLleno.TanqueLlenoNip;
import com.corpogas.corpoapp.VentaCombustible.DiferentesFormasPago;
import com.corpogas.corpoapp.VentaCombustible.ImprimePuntada;
import com.corpogas.corpoapp.VentaPagoTarjeta;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MonederosElectronicos extends AppCompatActivity {

    ProgressDialog bar;
    private MagReadService mReadService;
    private ToneGenerator tg = null;
    String Enviadodesde, lugarProviene, NIP;
    long idSucursal;
    SQLiteBD data;
    RespuestaApi<Bin> respuestaApiBin;
    JSONArray datos = new JSONArray();
    String idformaPago, posiciondeCarga, numeroempleadosucursal, PagoPuntada, tipoTarjeta;

    boolean pasa;

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MagReadService.MESSAGE_READ_MAG:
                    //MyToast.showCrouton(MainActivity.this, "Read the card successed!", Style.CONFIRM);
//                    updateAlert("Read the card successed!", 1);-------Revisar para los tipos de errores
                    String tracks = msg.getData().getString(MagReadService.CARD_TRACK1);
                    ArrayList<String> strInfo = msg.getData().getStringArrayList(MagReadService.CARD_TRACKS);
                    String tk1 = strInfo.get(0);
                    String tk2 = strInfo.get(1);
                    String tk3 = strInfo.get(2);

//                    String[] strInfo = tracks.split("\ntrack");
//                    String track1=strInfo[1];
//                    String track2 = strInfo[2];

                    //mNo.setText("");
                    if(!tracks.equals(""))
                        beep(tk1,tk2,tk3);
//                    mNo.append(tracks);
                    //mNo.append("\n\n");
                    break;
                case MagReadService.MESSAGE_OPEN_MAG:
                    //MyToast.showCrouton(MainActivity.this, "Init Mag Reader faile!", Style.ALERT);
//                    updateAlert("Init Mag Reader failed!", 2);-------Revisar para los tipos de errores
                    break;
                case MagReadService.MESSAGE_CHECK_FAILE:
                    //MyToast.showCrouton(MainActivity.this, "Please Pay by card!", Style.ALERT);
//                    updateAlert("Please Pay by card!", 2);-------Revisar para los tipos de errores
                    break;
                case MagReadService.MESSAGE_CHECK_OK:
                    //MyToast.showCrouton(MainActivity.this, "Pay by card OK!", Style.CONFIRM);
//                    updateAlert("Pay by card successed!", 1);-------Revisar para los tipos de errores
                    break;
            }
        }
    };


    String EstacionId, sucursalId, ipEstacion, numeroTarjetero;
    Double montoenlacanasta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_monederos_electronicos);
        data = new SQLiteBD(getApplicationContext());
        this.setTitle(data.getNombreEstacion() + " ( EST.:" + data.getNumeroEstacion() + ")");

//        mNo = (EditText) findViewById(R.id.editText1);
//        mAlertTv = (TextView) findViewById(R.id.textView1);
        Enviadodesde = getIntent().getStringExtra( "Enviadodesde");
        lugarProviene = getIntent().getStringExtra( "lugarproviene");
        NIP = getIntent().getStringExtra( "nip");
        PagoPuntada = getIntent().getStringExtra("pagoconpuntada");
        tipoTarjeta = getIntent().getStringExtra("tipoTarjeta");
        numeroempleadosucursal = getIntent().getStringExtra("numeroEmpleado");
        posiciondeCarga = getIntent().getStringExtra("posicioncargaid");
        idformaPago = getIntent().getStringExtra("formapagoid");
        montoenlacanasta = getIntent().getDoubleExtra("montoenlacanasta", 0);
        data= new SQLiteBD(getApplicationContext());
        idSucursal = Long.parseLong(data.getIdSucursal());

        EstacionId = data.getIdEstacion();
        ipEstacion = data.getIpEstacion();
        numeroTarjetero = data.getIdTarjtero();
        tg = new ToneGenerator(AudioManager.STREAM_MUSIC, ToneGenerator.MAX_VOLUME);
        mReadService = new MagReadService(this, mHandler);
    }


    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        mReadService.stop();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        mReadService.start();
    }
//    private void updateAlert(String mesg, int type) {
//        if(type == 2)
//            mAlertTv.setBackgroundColor(Color.RED);
//        else
//            mAlertTv.setBackgroundColor(Color.GREEN);
//        mAlertTv.setText(mesg);
//
//    }
    private void beep(String tk1, String tk2, String tk3) {
        if (tg != null)
            tg.startTone(ToneGenerator.TONE_CDMA_NETWORK_CALLWAITING);
        if (Enviadodesde.equals("formaspago")  | Enviadodesde.equals("CarritoTransacciones")){
            CompararTarjetasPuntada(tk1, tk2, tk3);
//            CompararTarjetas(tk1, tk2, tk3);
        } else {
            CompararTarjetas(tk1, tk2, tk3);
        }
    }

    private void CompararTarjetas(String tk1, String tk2, String tk3) {

        List<String> pistas = new ArrayList<String>();
        pistas.add(tk1);
        pistas.add(tk2);
        pistas.add(tk3);

        Bin bin = new Bin(pistas);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://" + data.getIpEstacion() + "/CorpogasService/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        EndPoints obtenNumeroTarjetero = retrofit.create(EndPoints.class);
        Call<RespuestaApi<Bin>> call = obtenNumeroTarjetero.getBin(idSucursal, bin);
        call.enqueue(new Callback<RespuestaApi<Bin>>() {

            @Override
            public void onResponse(Call<RespuestaApi<Bin>> call, Response<RespuestaApi<Bin>> response) {

                if (!response.isSuccessful()) {
                    return;
                }
                respuestaApiBin = response.body();
                boolean correcto = respuestaApiBin.Correcto;
                if(correcto == true)
                {
                    String mesanje = respuestaApiBin.Mensaje;
                    long formaPagoId = respuestaApiBin.getObjetoRespuesta().getTipoMonedero().PaymentMethodId;    //tiopoMonedero.getString("PaymentMethodId"); //TipoMonederoId
                    long idMonedero = respuestaApiBin.getObjetoRespuesta().getTipoMonedero().Id;//tiopoMonedero.getString("Id");
                    if ( idMonedero == 1 &&  formaPagoId ==12 ){ //PUNTADA
                        if (tipoTarjeta.equals("TanqueLleno")){
                            String titulo = "AVISO";
                            String mensaje = "Tarjeta inválida, NO ES TARJETA TANQUELLENO";
                            Modales modales = new Modales(MonederosElectronicos.this);
                            View view1 = modales.MostrarDialogoAlertaAceptar(MonederosElectronicos.this,mensaje,titulo);
                            view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
//                                    onDestroy();
                                    modales.alertDialog.dismiss();
                                    Intent intent = new Intent(MonederosElectronicos.this, Menu_Principal.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                        }else{
                            Intent intent = new Intent(getApplicationContext(), PosicionPuntadaRedimir.class); //ENVIABA A SeccionTarjeta cambio a PosicionPuntadaRedimir
                            intent.putExtra("track",mesanje);
                            intent.putExtra("nip", NIP);
                            intent.putExtra("lugarproviene", lugarProviene);
                            intent.putExtra("descuento", "0");
                            startActivity(intent);
                            finish();
                        }

//                        if (PagoPuntada.equals("si")){
//                            EnviaProcesoPuntadaRedimir(mesanje);
//                        }else{
//                            EnviaProcesoPuntadaAcumular(mesanje);
//                        }


//                        //Toast.makeText(getApplicationContext(),"A qui va seccion de tarjetas",Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(getApplicationContext(), SeccionTarjeta.class);
//                        intent.putExtra("track",mesanje);
////                        intent.putExtra("banderaHuella", banderaHuella);
//                        startActivity(intent);
//                        finish();
                    }else {
                        if (idMonedero == 2 && formaPagoId == 11) { //TANQUE LLENO CENTRO
//                            Toast.makeText(getApplicationContext(), "A qui va tanque lleno", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), TanqueLlenoNip.class);  //seccionTanqueLleno
                            intent.putExtra("track",mesanje);
                            intent.putExtra("lugarProviene", Enviadodesde);
                            startActivity(intent);
                            finish();
                        }
                        if (idMonedero == 3 && formaPagoId == 11) { //TANQUE LLENO SURESTE
                            Toast.makeText(getApplicationContext(), "A qui va tanque lleno", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(getApplicationContext(), TanqueLlenoNip.class);  //seccionTanqueLleno
                            intent.putExtra("lugarProviene", Enviadodesde);
                            intent.putExtra("track",mesanje);
                            startActivity(intent);
                            finish();
                        }

                    }
                }
                else{
                    String titulo = "AVISO";
                    String mensaje = "Tarjeta inválida";
                    Modales modales = new Modales(MonederosElectronicos.this);
                    View view1 = modales.MostrarDialogoAlertaAceptar(MonederosElectronicos.this,mensaje,titulo);
                    view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
//                            onDestroy();
                            modales.alertDialog.dismiss();
                            Intent intent = new Intent(MonederosElectronicos.this, Menu_Principal.class);
                            startActivity(intent);
                            finish();
                        }
                    });

                }

            }

            @Override
            public void onFailure(Call<RespuestaApi<Bin>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void EnviaProcesoPuntadaRedimir(String NumeroTarjeta) {
        if (!Conexion.compruebaConexion(this)) {
            Toast.makeText(getBaseContext(), "Sin conexión a la red ", Toast.LENGTH_SHORT).show();
            Intent intent1 = new Intent(getApplicationContext(), Menu_Principal.class);
            startActivity(intent1);
            finish();
        } else {
            obtieneSaldoTarjeta(NumeroTarjeta);
        }
    }

    public void obtieneSaldoTarjeta(String  numerotarjeta) {
        bar = new ProgressDialog(MonederosElectronicos.this);
        bar.setTitle("Conectando con Puntada");
        bar.setMessage("Ejecutando... ");
        bar.setIcon(R.drawable.gas);
        bar.setCancelable(false);
        bar.show();

        final SQLiteBD data = new SQLiteBD(getApplicationContext());
        String nombreCompletoVenta = getIntent().getStringExtra("nombrecompleto");
        String idusuario = getIntent().getStringExtra("idusuario");
        String posicioncarga = getIntent().getStringExtra("posicioncarga");
        String clave = getIntent().getStringExtra("claveusuario");
        String nombrepago = getIntent().getStringExtra("NombrePago");
        String idoperativa = getIntent().getStringExtra("idoperativa");
        String numpago = getIntent().getStringExtra("formapagoid");
        String sucursalnumeroempleado = getIntent().getStringExtra("numeroempleadosucursal");
        String MontoenCarrito = getIntent().getStringExtra("montoenlacanasta");
        String nip = getIntent().getStringExtra("Nip");

        String url = "http://" + ipEstacion + "/CorpogasService/api/puntadas/actualizaPuntos/clave/"+clave;
        StringRequest eventoReq = new StringRequest(Request.Method.POST,url,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            String Proviene = getIntent().getStringExtra("lugarproviene");
                            JSONObject resultado = new JSONObject(response);
                            String estado = resultado.getString("Estado");
                            String mensaje = resultado.getString("Mensaje");
                            final String saldo = resultado.getString("Saldo");
                            if (estado == "true"){ //    mensaje.equals("null")
                                if (Double.parseDouble(saldo) > 0) {
                                    String track =     getIntent().getStringExtra("track");
                                    try {

                                        //LeeTarjeta();
                                        Intent intent = new Intent(getApplicationContext(), Menu_Principal.class); //DiferentesFormasPago
                                        intent.putExtra("Enviadodesde", "formaspago");
                                        intent.putExtra("idusuario", idusuario);
                                        intent.putExtra("posicioncarga", posicioncarga);
                                        intent.putExtra("claveusuario", clave);
                                        intent.putExtra("idoperativa", idoperativa);
                                        intent.putExtra("formapagoid", numpago);
                                        intent.putExtra("NombreCompleto", nombreCompletoVenta);
                                        intent.putExtra("montoencanasta", MontoenCarrito);
                                        intent.putExtra("numeroempleadosucursal", sucursalnumeroempleado);
                                        intent.putExtra("saldoPuntada", saldo);
                                        intent.putExtra("tarjetaNumero", numerotarjeta);
                                        intent.putExtra("pagoconpuntada", PagoPuntada);
                                        startActivity(intent);
                                        finish();

                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }
                                }else{
                                    String titulo = "SALDO INSUFICIENTE";
                                }
                            }else{
                                try{
                                    String titulo = "AVISO";
                                    String mensajes;
                                    if (mensaje.equals("null")){
                                        mensajes = "Sin conexón con la consola";
                                    }else {
                                        mensajes = mensaje;
                                    }
                                    Modales modales = new Modales(MonederosElectronicos.this);
                                    View view1 = modales.MostrarDialogoAlertaAceptar(MonederosElectronicos.this,mensajes,titulo);
                                    view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            bar.cancel();
                                            modales.alertDialog.dismiss();
                                            Intent intent1 = new Intent(getApplicationContext(), Menu_Principal.class);
                                            startActivity(intent1);
                                            finish();
                                        }
                                    });
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                //Obtenemos los parmetros a enviar
                String sproducto = "[]";
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("EmpleadoId", idusuario);
                params.put("SucursalId", data.getIdSucursal());
                params.put("RequestID","33");
                params.put("PosicionCarga", posicioncarga);
                params.put("Tarjeta", numerotarjeta);
                params.put("NuTarjetero", numeroTarjetero);
                params.put("NIP", nip);
                params.put("Productos", sproducto);

                String gson = new Gson().toJson(params);

                return params;
            }
        };
        // Añade la peticion a la cola
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        eventoReq.setRetryPolicy(new DefaultRetryPolicy(12000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(eventoReq);
    }


    private void CompararTarjetasPuntada(final String tk1, final String tk2, final String tk3) {
        if (!Conexion.compruebaConexion(this)) {
            Toast.makeText(getBaseContext(), "Sin conexión a la red ", Toast.LENGTH_SHORT).show();
            Intent intent1 = new Intent(getApplicationContext(), Menu_Principal.class);

            startActivity(intent1);
            finish();
        } else {
            SQLiteBD data = new SQLiteBD(getApplicationContext());
            String URL = "http://" + data.getIpEstacion() + "/CorpogasService/api/bines/obtieneBinTarjeta/sucursalId/" + data.getIdSucursal();
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            final JSONObject jsonObject = new JSONObject();

            try {
                datos.put(tk1);
                datos.put(tk2);
                datos.put(tk3);
                jsonObject.put("Pistas", datos);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JsonObjectRequest request_json = new JsonObjectRequest(Request.Method.POST, URL, jsonObject, new com.android.volley.Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        String correcto = response.getString("Correcto");
                        if (correcto.equals("true")) {
                            String mesanje = response.getString("Mensaje");
                            String objetorespuesta = response.getString("ObjetoRespuesta");
                            JSONObject jsonObjectoRespuesta = new JSONObject(objetorespuesta);
                            String moned = jsonObjectoRespuesta.getString("TipoMonedero");
                            JSONObject numerointerno = new JSONObject(moned);
                            String formaPagoId = numerointerno.getString("PaymentMethodId");
                            String modenerotipo = numerointerno.getString("Id");

                            if (modenerotipo.equals("1") && formaPagoId.equals("12")) { //PUNTADA    modenerotipo.equals("3")
//                            if (modenerotipo.equals("1")) {
                                if (PagoPuntada.equals("si")){
                                    EnviaProcesoPuntadaRedimir(mesanje);
                                }else{
                                    EnviaProcesoPuntadaAcumular(mesanje);
                                }
                            } else {
                                String titulo = "DEBE PASAR UNA TARJETA PUNTADA";
                                String mensaje = "Tarjeta inválida";
                                Modales modales = new Modales(MonederosElectronicos.this);
                                View view1 = modales.MostrarDialogoAlertaAceptar(MonederosElectronicos.this, mensaje, titulo);
                                view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        onDestroy();
                                        modales.alertDialog.dismiss();
                                        Intent intent = new Intent(MonederosElectronicos.this, Menu_Principal.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                            }
                        } else {
                            String titulo = "AVISO";
                            String mensaje = "Tarjeta invalida";
                            Modales modales = new Modales(MonederosElectronicos.this);
                            View view1 = modales.MostrarDialogoAlertaAceptar(MonederosElectronicos.this, mensaje, titulo);
                            view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    onDestroy();
                                    modales.alertDialog.dismiss();
                                    Intent intent = new Intent(MonederosElectronicos.this, Menu_Principal.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                        }

                    } catch (JSONException e) {
                        onDestroy();
                        Intent intente = new Intent(getApplicationContext(), Menu_Principal.class);
                        startActivity(intente);
                    }
                }

            }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    onDestroy();
                    Intent intente = new Intent(getApplicationContext(), Menu_Principal.class);
                    startActivity(intente);
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
                    return com.android.volley.Response.success(jsonObject, HttpHeaderParser.parseCacheHeaders(response));
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            request_json.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            queue.add(request_json);
        }
    }


    private void EnviaProcesoPuntadaAcumularNew(String NumeroDeTarjeta) {
        String url = "http://" + ipEstacion + "/CorpogasService/api/puntadas/actualizaPuntos/numeroEmpleado/"+numeroempleadosucursal;

        StringRequest eventoReq = new StringRequest(Request.Method.POST,url,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            String Proviene = getIntent().getStringExtra("lugarproviene");
                            JSONObject resultado = new JSONObject(response);
                            String estado = resultado.getString("Estado");
                            String mensaje = resultado.getString("Mensaje");
                            final String saldo = resultado.getString("Saldo");
                            if (estado == "true"){ //    mensaje.equals("null")
                                if (Double.parseDouble(saldo) > 0) {
                                    String track =     getIntent().getStringExtra("track"); //"4000052500200001";
                                    Long Idusuario = getIntent().getLongExtra("IdUsuario",0);
                                    String Claveusuario = getIntent().getStringExtra("ClaveDespachador");
                                    String NipTarjeta = getIntent().getStringExtra("nip");

                                    switch (Proviene) {
                                        case "Redimir": //Consulta Saldo
                                            //String carga = getIntent().getStringExtra("pos");
                                            Intent intent = new Intent(getApplicationContext(), ProductosARedimir.class);
//                                            intent.putExtra("pos", Posi);
                                            intent.putExtra("saldo", saldo);
                                            intent.putExtra("clave", Claveusuario);
                                            intent.putExtra("empleadoid", Idusuario);
                                            intent.putExtra("track", track);
                                            intent.putExtra("nip", NipTarjeta);
//                                        intent.putExtra("nombreatendio", nombreatendio);
                                            startActivity(intent);
                                            finish();
                                            break;
                                        case "ConsultaSaldoPuntada"://Redimir
                                            try {
                                                String titulo = "AVISO";
                                                String mensajes = "Tarjeta No. " + track + " con Saldo: " + saldo;
                                                final Modales modales = new Modales(MonederosElectronicos.this);
                                                View view1 = modales.MostrarDialogoCorrecto(MonederosElectronicos.this,mensajes);
                                                view1.findViewById(R.id.buttonAction).setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        modales.alertDialog.dismiss();
                                                        Intent intent = new Intent(getApplicationContext(), Menu_Principal.class);
                                                        startActivity(intent);
                                                        finish();
                                                    }
                                                });
                                            }catch (Exception e){
                                                e.printStackTrace();
                                            }
                                            break;
                                        case "Registrar":

                                            break;
                                        default:
                                            break;
                                    }
                                }else{
                                    String titulo = "SALDO INSUFICIENTE";
                                    String mensajes = "El Saldo en la tarjeta es de: $"+ saldo;
                                    Modales modales = new Modales(MonederosElectronicos.this);
                                    View view1 = modales.MostrarDialogoAlertaAceptar(MonederosElectronicos.this,mensajes,titulo);
                                    view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            bar.cancel();
                                            modales.alertDialog.dismiss();
                                            Intent intent1 = new Intent(getApplicationContext(), Menu_Principal.class);
                                            startActivity(intent1);
                                            finish();
                                        }
                                    });

                                }
                            }else{
                                try{
                                    String titulo = "AVISO";
                                    String mensajes;
                                    if (mensaje.equals("null")){
                                        mensajes = "Sin conexón con la consola";
                                    }else {
                                        mensajes = mensaje;
                                    }
                                    Modales modales = new Modales(MonederosElectronicos.this);
                                    View view1 = modales.MostrarDialogoAlertaAceptar(MonederosElectronicos.this,mensajes,titulo);
                                    view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            bar.cancel();
                                            modales.alertDialog.dismiss();
//                                            Intent intent1 = new Intent(getApplicationContext(), Menu_Principal.class);
//                                            startActivity(intent1);
//                                            finish();
                                        }
                                    });
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                //Obtenemos los parmetros a enviar
                String sproducto = "[]";
                //JSONArray sproducto = new JSONArray()
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();

                params.put("NuTarjetero", numeroTarjetero);
                params.put("SucursalId", data.getIdSucursal());
                params.put("RequestID","35");
                params.put("PosicionCarga", String.valueOf(posiciondeCarga));
                params.put("Tarjeta", NumeroDeTarjeta);
                params.put("Productos", sproducto);
//                String gson = new Gson().toJson(params);
                return params;
            }
        };
        // Añade la peticion a la cola
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        eventoReq.setRetryPolicy(new DefaultRetryPolicy(500000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(eventoReq);

    }




    private void EnviaProcesoPuntadaAcumular(String NumeroDeTarjeta) {
        if (!Conexion.compruebaConexion(this)) {
            Toast.makeText(getBaseContext(), "Sin conexión a la red ", Toast.LENGTH_SHORT).show();
            Intent intent1 = new Intent(getApplicationContext(), Menu_Principal.class);

            startActivity(intent1);
            finish();
        } else {
            JSONObject datos = new JSONObject();

            String idusuario = getIntent().getStringExtra("idusuario");
            String nombrepago = getIntent().getStringExtra("NombrePago");
            String idoperativa = getIntent().getStringExtra("idoperativa");
            String numpago = getIntent().getStringExtra("formapagoid");
            String nombreCompleto = getIntent().getStringExtra("NombreCompleto");

            JSONArray myArray = new JSONArray();
            SQLiteBD data = new SQLiteBD(getApplicationContext());
            String url = "http://" + data.getIpEstacion() + "/CorpogasService/api/puntadas/actualizaPuntos/numeroEmpleado/" + numeroempleadosucursal;
            RequestQueue queue = Volley.newRequestQueue(this);
            try {
                datos.put("NuTarjetero", numeroTarjetero);
                datos.put("SucursalId", idSucursal);
                datos.put("RequestID", 35); // Esto es para cuando se termina de realizar el despacho, es pasa la tarjeta puntada y se acumula
                datos.put("PosicionCarga", posiciondeCarga); //posicionCarga
                datos.put("Tarjeta", NumeroDeTarjeta);
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
                        if (Enviadodesde.equals("formaspago")) {
                            if (tipoTarjeta.equals("Puntadaformapago")){
                                Double MontoenCarrito = getIntent().getDoubleExtra("montoenlacanasta", 0);
                                //Enviamos a la pantalla de captura de diferentes formas de pago   MontoenCanasta
                                String banderaHuella = getIntent().getStringExtra( "banderaHuella");
                                String nombreCompletoVenta = getIntent().getStringExtra("nombrecompleto");
                                //LeeTarjeta();
                                Intent intent = new Intent(getApplicationContext(), DiferentesFormasPago.class);
                                intent.putExtra("banderaHuella", banderaHuella);
                                intent.putExtra("Enviadodesde", "Monedero");
                                intent.putExtra("idusuario", idusuario);
                                intent.putExtra("posicioncarga", posiciondeCarga);
                                intent.putExtra("claveusuario", numeroempleadosucursal);
                                intent.putExtra("idoperativa", idoperativa);
                                intent.putExtra("formapagoid", numpago);
                                intent.putExtra("NombreCompleto", nombreCompleto);
                                intent.putExtra("montoencanasta", MontoenCarrito);
                                intent.putExtra("numeroempleadosucursal", numeroempleadosucursal);
                                intent.putExtra("saldoPuntada", "0");
                                intent.putExtra("pagoconpuntada", PagoPuntada);
                                startActivity(intent);
                                finish();
                            }else{
                                if (idformaPago.equals("3") || idformaPago.equals(5) || idformaPago.equals(13)){
                                    DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
                                    simbolos.setDecimalSeparator('.');
                                    DecimalFormat df = new DecimalFormat("####.00##",simbolos);

                                    df.setMaximumFractionDigits(2);
                                    data.getWritableDatabase().delete("PagoTarjeta", null, null);
                                    data.close();
                                    data.InsertarDatosPagoTarjeta("1",posiciondeCarga, idformaPago, Double.toString(montoenlacanasta), "0", "1", "0", "", "", "", Double.toString(montoenlacanasta));
                                    Intent intentVisa = new Intent(getApplicationContext(), VentaPagoTarjeta.class);//DiferentesFormasPagoPuntada
                                    intentVisa.putExtra("lugarProviene", "formaspago");
                                    intentVisa.putExtra("posicioncarga", posiciondeCarga);
                                    intentVisa.putExtra("formapagoid", numpago);
                                    intentVisa.putExtra("montoencanasta", "$"+ df.format(montoenlacanasta));
                                    intentVisa.putExtra("numeroTarjeta", "");
                                    startActivity(intentVisa);
                                    finish();
                                }else{
                                    Double MontoenCarrito = getIntent().getDoubleExtra("montoenlacanasta", 0);
                                    Intent intente = new Intent(getApplicationContext(), ImprimePuntada.class);
                                    intente.putExtra("posicioncarga", posiciondeCarga);
                                    intente.putExtra("idoperativa", idoperativa);
                                    intente.putExtra("idformapago", numpago);
                                    intente.putExtra("nombrepago", nombrepago);
                                    intente.putExtra("montoencanasta", MontoenCarrito);
                                    startActivity(intente);
                                    finish();
                                }
                            }
                        } else {
                            String MontoenCarrito = getIntent().getStringExtra("montoenlacanasta");

                            //Enviamos a la pantalla de captura de diferentes formas de pago   MontoenCanasta
                            String banderaHuella = getIntent().getStringExtra( "banderaHuella");
                            String nombreCompletoVenta = getIntent().getStringExtra("nombrecompleto");
                            //LeeTarjeta();
                            Intent intent = new Intent(getApplicationContext(), DiferentesFormasPago.class);
                            intent.putExtra("banderaHuella", banderaHuella);
                            intent.putExtra("Enviadodesde", "Monedero");
                            intent.putExtra("idusuario", idusuario);
                            intent.putExtra("posicioncarga", posiciondeCarga);
                            intent.putExtra("claveusuario", numeroempleadosucursal);
                            intent.putExtra("idoperativa", idoperativa);
                            intent.putExtra("formapagoid", numpago);
                            intent.putExtra("NombreCompleto", nombreCompleto);
                            intent.putExtra("montoencanasta", MontoenCarrito);
                            intent.putExtra("numeroempleadosucursal", numeroempleadosucursal);
                            intent.putExtra("saldoPuntada", "0");
                            intent.putExtra("pagoconpuntada", PagoPuntada);
                            startActivity(intent);
                            finish();

                        }
                    } else {
                        try {
                            String titulo = "AVISO";
                            String mensajes = "" + mensaje;
                            Modales modales = new Modales(MonederosElectronicos.this);
                            View view1 = modales.MostrarDialogoAlertaAceptar(MonederosElectronicos.this, mensajes, titulo);
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