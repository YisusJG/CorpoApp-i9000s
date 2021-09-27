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

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.corpogas.corpoapp.Conexion;
import com.corpogas.corpoapp.Configuracion.SQLiteBD;
import com.corpogas.corpoapp.Entities.Catalogos.Bin;
import com.corpogas.corpoapp.Entities.Classes.RespuestaApi;
import com.corpogas.corpoapp.Menu_Principal;
import com.corpogas.corpoapp.Modales.Modales;
import com.corpogas.corpoapp.Puntada.SeccionTarjeta;
import com.corpogas.corpoapp.R;
import com.corpogas.corpoapp.Interfaces.Endpoints.EndPoints;
import com.corpogas.corpoapp.Service.MagReadService;
import com.corpogas.corpoapp.TanqueLleno.TanqueLlenoNip;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

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
    String Enviadodesde;
    long idSucursal;
    SQLiteBD data;
    RespuestaApi<Bin> respuestaApiBin;


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


    String EstacionId, sucursalId, ipEstacion, numeroTarjetero, PagoPuntada, tipoTarjeta;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_monederos_electronicos);

//        mNo = (EditText) findViewById(R.id.editText1);
//        mAlertTv = (TextView) findViewById(R.id.textView1);
        Enviadodesde = getIntent().getStringExtra( "Enviadodesde");
        PagoPuntada = getIntent().getStringExtra("pagoconpuntada");
        tipoTarjeta = getIntent().getStringExtra("tipoTarjeta");
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
//            CompararTarjetasPuntada(tk1, tk2, tk3);
            CompararTarjetas(tk1, tk2, tk3);
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
                    long idMonedero = respuestaApiBin.getObjetoRespuesta().getTipoMonederoId();//tiopoMonedero.getString("Id");
                    if ( idMonedero == 1 &&  formaPagoId ==12 ){ //PUNTADA
                        if (tipoTarjeta.equals("TanqueLleno")){
                            String titulo = "AVISO";
                            String mensaje = "Tarjeta inválida, NO ES TARJETA PUNTADA";
                            Modales modales = new Modales(MonederosElectronicos.this);
                            View view1 = modales.MostrarDialogoAlertaAceptar(MonederosElectronicos.this,mensaje,titulo);
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
                        }else{
                            Intent intent = new Intent(getApplicationContext(), SeccionTarjeta.class);
                            intent.putExtra("track",mesanje);
                            startActivity(intent);
                            finish();
                        }

//                        if (PagoPuntada.equals("si")){
//                            EnviaProcesoPuntadaRedimir(mesanje);
//                        }else{
//                            EnviaProcesoPuntadaAcumular(mesanje);
//                        }


                        //Toast.makeText(getApplicationContext(),"A qui va seccion de tarjetas",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), SeccionTarjeta.class);
                        intent.putExtra("track",mesanje);
//                        intent.putExtra("banderaHuella", banderaHuella);
                        startActivity(intent);
                        finish();
                    }else {
                        if (idMonedero == 2 && formaPagoId == 11) { //TANQUE LLENO CENTRO
//                            Toast.makeText(getApplicationContext(), "A qui va tanque lleno", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), TanqueLlenoNip.class);  //seccionTanqueLleno
                            intent.putExtra("track",mesanje);
                            startActivity(intent);

                        }
                        if (idMonedero == 3 && formaPagoId == 11) { //TANQUE LLENO SURESTE
                            Toast.makeText(getApplicationContext(), "A qui va tanque lleno", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(getApplicationContext(), TanqueLlenoNip.class);  //seccionTanqueLleno
                            intent.putExtra("track",mesanje);
                            startActivity(intent);
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
                            onDestroy();
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




}