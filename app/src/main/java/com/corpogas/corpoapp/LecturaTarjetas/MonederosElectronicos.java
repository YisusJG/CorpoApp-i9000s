package com.corpogas.corpoapp.LecturaTarjetas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.device.PrinterManager;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.corpogas.corpoapp.Configuracion.SQLiteBD;
import com.corpogas.corpoapp.Entities.Catalogos.Bin;
import com.corpogas.corpoapp.Entities.Classes.RespuestaApi;
import com.corpogas.corpoapp.Menu_Principal;
import com.corpogas.corpoapp.Modales.Modales;
import com.corpogas.corpoapp.Puntada.SeccionTarjeta;
import com.corpogas.corpoapp.R;
import com.corpogas.corpoapp.Request.Interfaces.EndPoints;
import com.corpogas.corpoapp.Service.MagReadService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MonederosElectronicos extends AppCompatActivity {


    private MagReadService mReadService;
    private ToneGenerator tg = null;
    String Enviadodesde,idSucursal;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_monederos_electronicos);

//        mNo = (EditText) findViewById(R.id.editText1);
//        mAlertTv = (TextView) findViewById(R.id.textView1);
        Enviadodesde = getIntent().getStringExtra( "Enviadodesde");
        data= new SQLiteBD(getApplicationContext());
        idSucursal = data.getIdSucursal();
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
            String logica = "falta la logica aqui";
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
                .baseUrl("http://" + data.getIpEstacion() + "/CorpogasService_Entities/")
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
                        //Toast.makeText(getApplicationContext(),"A qui va seccion de tarjetas",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), SeccionTarjeta.class);
                        intent.putExtra("track",mesanje);
//                        intent.putExtra("banderaHuella", banderaHuella);
                        startActivity(intent);
                        finish();
                    }else {
                        if (idMonedero == 2 && formaPagoId == 11) { //TANQUE LLENO CENTRO
                            Toast.makeText(getApplicationContext(), "A qui va tanque lleno", Toast.LENGTH_SHORT).show();

//                            Intent intent = new Intent(getApplicationContext(), TanqueLlenoNip.class);  //seccionTanqueLleno
//                            intent.putExtra("track",mesanje);
//                            intent.putExtra("banderaHuella", banderaHuella);
//                            startActivity(intent);

                        }
                        if (idMonedero == 3 && formaPagoId == 11) { //TANQUE LLENO SURESTE
                            Toast.makeText(getApplicationContext(), "A qui va tanque lleno", Toast.LENGTH_SHORT).show();

//                            Intent intent = new Intent(getApplicationContext(), seccionTanqueLleno.class);  //ClaveDespachadorTL
//                            intent.putExtra("track",mesanje);
//                            intent.putExtra("banderaHuella", banderaHuella);
//                            startActivity(intent);
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

}