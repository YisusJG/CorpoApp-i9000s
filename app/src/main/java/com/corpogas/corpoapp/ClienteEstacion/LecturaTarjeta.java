package com.corpogas.corpoapp.ClienteEstacion;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Toast;

import com.corpogas.corpoapp.Configuracion.SQLiteBD;
import com.corpogas.corpoapp.Corte.ResumenActivity;
import com.corpogas.corpoapp.Entities.Accesos.AccesoUsuario;
import com.corpogas.corpoapp.Entities.Catalogos.Bin;
import com.corpogas.corpoapp.Entities.Classes.RespuestaApi;
import com.corpogas.corpoapp.Interfaces.Endpoints.EndPoints;
import com.corpogas.corpoapp.LecturaTarjetas.MonederosElectronicos;
import com.corpogas.corpoapp.Menu_Principal;
import com.corpogas.corpoapp.Modales.Modales;
import com.corpogas.corpoapp.Puntada.PosicionPuntadaRedimir;
import com.corpogas.corpoapp.R;
import com.corpogas.corpoapp.Service.MagReadService;
import com.corpogas.corpoapp.TanqueLleno.TanqueLlenoNip;

import java.sql.SQLData;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LecturaTarjeta extends AppCompatActivity {

    long sucursalId;
    SQLiteBD db;
    String ipEstacion, titulo, mensaje;

    private MagReadService mReadService;
    private ToneGenerator tg = null;

    String bearerToken;
    RespuestaApi<AccesoUsuario> token;

    RespuestaApi<Bin> respuestaApiBin;

    String model;

    Modales modales;

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
                    if (!tracks.equals(""))
                        beep(tk1, tk2, tk3);
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lectura_tarjeta);
        init();
        getObjetos();
        setVariables();
        getToken();
        onClicks();

        {
            mReadService = new MagReadService(this, mHandler);
            tg = new ToneGenerator(AudioManager.STREAM_MUSIC, ToneGenerator.MAX_VOLUME);
        }
    }

    private void init(){

    }

    private void getObjetos(){

    }

    private void setVariables(){
        db = new SQLiteBD(getApplicationContext());
        modales = new Modales(LecturaTarjeta.this);
        sucursalId = Long.parseLong(db.getIdSucursal());
        ipEstacion = db.getIpEstacion();
        model = Build.MODEL;

    }

    private void onClicks(){

    }

    private void beep(String tk1, String tk2, String tk3) {
        if (tg != null)
            tg.startTone(ToneGenerator.TONE_CDMA_NETWORK_CALLWAITING);
        {
            CompararTarjetas(tk1, tk2, tk3);
        }
    }

    private void getToken() {
        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("http://"+ip2+"/CorpogasService/") //anterior
                .baseUrl("http://10.0.1.40/CorpogasService_entities_token/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        EndPoints obtenerToken = retrofit.create(EndPoints.class);
        Call<RespuestaApi<AccesoUsuario>> call = obtenerToken.getAccesoUsuario(497L, "1111");
        call.timeout().timeout(60, TimeUnit.SECONDS);
        call.enqueue(new Callback<RespuestaApi<AccesoUsuario>>() {
            @Override
            public void onResponse(Call<RespuestaApi<AccesoUsuario>> call, Response<RespuestaApi<AccesoUsuario>> response) {
                if (response.isSuccessful()) {
                    token = response.body();
                    assert token != null;
                    bearerToken = token.Mensaje;
                } else {
                    bearerToken = "";
                }
            }

            @Override
            public void onFailure(Call<RespuestaApi<AccesoUsuario>> call, Throwable t) {
                Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void CompararTarjetas(String tk1, String tk2, String tk3) {

        List<String> pistas = new ArrayList<String>();
        pistas.add(tk1);
        pistas.add(tk2);
        pistas.add(tk3);

        Bin bin = new Bin(pistas);
        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("http://" + data.getIpEstacion() + "/CorpogasService/")
                .baseUrl("http://10.0.1.40/CorpogasService_entities_token/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        EndPoints obtenNumeroTarjetero = retrofit.create(EndPoints.class);
        Call<RespuestaApi<Bin>> call = obtenNumeroTarjetero.getBin(sucursalId, bin, "Bearer " +bearerToken);
        call.enqueue(new Callback<RespuestaApi<Bin>>() {

            @Override
            public void onResponse(Call<RespuestaApi<Bin>> call, Response<RespuestaApi<Bin>> response) {

                if (!response.isSuccessful()) {
                    return;
                }
                respuestaApiBin = response.body();

                boolean correcto = respuestaApiBin.Correcto;
                mensaje = respuestaApiBin.Mensaje;
                if (correcto) {

                    long formaPagoId = respuestaApiBin.getObjetoRespuesta().getTipoMonedero().PaymentMethodId;    //tiopoMonedero.getString("PaymentMethodId"); //TipoMonederoId
                    long idMonedero = respuestaApiBin.getObjetoRespuesta().getTipoMonedero().Id;//tiopoMonedero.getString("Id");
                    String prueba = "";

                }else{
                    titulo = "AVISO";
                    modales = new Modales(LecturaTarjeta.this);
                    View view1 = modales.MostrarDialogoAlertaAceptar(LecturaTarjeta.this, mensaje, titulo);
                    view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            modales.alertDialog.dismiss();

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

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        if (model.equals("i9000S")) {
            mReadService.stop();
        }
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        if (model.equals("i9000S")) {
            mReadService.start();
        }
    }
}