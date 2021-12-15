package com.corpogas.corpoapp.LecturaTarjetas;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.device.scanner.configuration.Triggering;
import android.device.PiccManager;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
import com.corpogas.corpoapp.Entities.Accesos.AccesoUsuario;
import com.corpogas.corpoapp.Entities.Catalogos.Bin;
import com.corpogas.corpoapp.Entities.Classes.RespuestaApi;
import com.corpogas.corpoapp.Menu_Principal;
import com.corpogas.corpoapp.Modales.Modales;
import com.corpogas.corpoapp.Puntada.PosicionPuntadaRedimir;
import com.corpogas.corpoapp.Puntada.ProductosARedimir;
import com.corpogas.corpoapp.Puntada.SeccionTarjeta;
import com.corpogas.corpoapp.R;
import com.corpogas.corpoapp.Interfaces.Endpoints.EndPoints;
import com.corpogas.corpoapp.ScanManagerProvides;
import com.corpogas.corpoapp.Service.MagReadService;
import com.corpogas.corpoapp.TanqueLleno.TanqueLlenoNip;
import com.corpogas.corpoapp.Token.GlobalToken;
import com.corpogas.corpoapp.ValesPapel.ValesPapel;
import com.corpogas.corpoapp.VentaCombustible.DiferentesFormasPago;
import com.corpogas.corpoapp.VentaCombustible.ImprimePuntada;
import com.corpogas.corpoapp.VentaPagoTarjeta;
import com.corpogas.gogasmanagement.Entities.Helpers.CurrencyFormatter;
import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MonederosElectronicos extends AppCompatActivity {
    String model;
    //Variables Escaner
    ScanManagerProvides scanManagerProvides;
    String result;

    ProgressDialog bar;
    private MagReadService mReadService;
    private ToneGenerator tg = null;
    String Enviadodesde, lugarProviene, NIP;
    long idSucursal;
    SQLiteBD data;
    CurrencyFormatter numFormat;
    DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
    DecimalFormat df;
    TextView tvMensajeDesliza;
    Button btnEscanearTarjeta;

    //NFC
    private Button bCheck;
    private PiccManager piccReader;
//    private Handler handler;
    private ExecutorService exec;
    int scanCard = -1;
    int SNLen = -1;
    private static final String TAG = "PiccCheck";
    private static final int MSG_BLOCK_NO_NONE = 0;
    private static final int MSG_BLOCK_NO_ILLEGAL = 1;
    private static final int MSG_AUTHEN_FAIL = 2;
    private static final int MSG_WRITE_SUCCESS = 3;
    private static final int MSG_WRITE_FAIL = 4;
    private static final int MSG_READ_FAIL = 5;
    private static final int MSG_SHOW_BLOCK_DATA = 6;
    private static final int MSG_ACTIVE_FAIL = 7;
    private static final int MSG_APDU_FAIL = 8;
    private static final int MSG_SHOW_APDU = 9;
    private static final int MSG_BLOCK_DATA_NONE = 10;
    private static final int MSG_AUTHEN_BEFORE = 11;
    private static final int MSG_FOUND_UID = 12;


    RespuestaApi<Bin> respuestaApiBin;
    JSONArray datos = new JSONArray();
    String idformaPago, posiciondeCarga, numeroempleadosucursal, PagoPuntada, tipoTarjeta;
    String numeroTarjeta, nipQr;
    Double descuentoCombustible1, descuentoCombustible2, descuentoCombustible3;

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

    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            switch (msg.what) {
                case MSG_BLOCK_NO_NONE:
                    break;
                case MSG_BLOCK_NO_ILLEGAL:
                    break;
                case MSG_AUTHEN_FAIL:
                    break;
                case MSG_AUTHEN_BEFORE:
                    break;
                case MSG_WRITE_SUCCESS:
                    break;
                case MSG_WRITE_FAIL:
                    break;
                case MSG_READ_FAIL:
                    break;
                case MSG_APDU_FAIL:
                    break;
                case MSG_BLOCK_DATA_NONE:
                    break;
                case MSG_SHOW_BLOCK_DATA:
////                        SoundTool.getMySound(MainActivity.this).playMusic("success");
//                        String data = (String) msg.obj;
//                        tvApdu.append("\n" + data);
                    break;
                case MSG_ACTIVE_FAIL:
                    break;
                case MSG_SHOW_APDU:
                    break;
                case MSG_FOUND_UID:
                    String uid = (String) msg.obj;
//                        SoundTool.getMySound(MainActivity.this).playMusic("success");
                    Intent intent = new Intent(getApplicationContext(), TanqueLlenoNip.class);  //seccionTanqueLleno
                    intent.putExtra("track", uid);
                    intent.putExtra("lugarProviene", "NFC");

                    startActivity(intent);
                    finish();


                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };


    String EstacionId, sucursalId, ipEstacion, numeroTarjetero;
    Double montoenlacanasta;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_monederos_electronicos);
        model = Build.MODEL;
        data = new SQLiteBD(getApplicationContext());
        this.setTitle(data.getNombreEstacion() + " ( EST.:" + data.getNumeroEstacion() + ")");
        tvMensajeDesliza = (TextView) findViewById(R.id.tvMensajeDesliza);
//        mNo = (EditText) findViewById(R.id.editText1);
//        mAlertTv = (TextView) findViewById(R.id.textView1);
        bCheck = (Button) findViewById(R.id.picc_check);
//        bCheck.setVisibility(View.GONE);
        Enviadodesde = getIntent().getStringExtra("Enviadodesde");
        lugarProviene = getIntent().getStringExtra("lugarproviene");
        NIP = getIntent().getStringExtra("nip");
        PagoPuntada = getIntent().getStringExtra("pagoconpuntada");
        tipoTarjeta = getIntent().getStringExtra("tipoTarjeta");
        numeroempleadosucursal = data.getNumeroEmpleado(); //getIntent().getStringExtra("numeroEmpleado");
        posiciondeCarga = getIntent().getStringExtra("posicioncargaid");
        idformaPago = getIntent().getStringExtra("formapagoid");
        montoenlacanasta = getIntent().getDoubleExtra("montoenlacanasta", 0);
        idSucursal = Long.parseLong(data.getIdSucursal());

        EstacionId = data.getIdEstacion();
        ipEstacion = data.getIpEstacion();
        numeroTarjetero = data.getIdTarjtero();

        model = Build.MODEL;{
            mReadService = new MagReadService(this, mHandler);
            tg = new ToneGenerator(AudioManager.STREAM_MUSIC, ToneGenerator.MAX_VOLUME);
        }

        simbolos.setDecimalSeparator('.');
        df = new DecimalFormat("###,###.00", simbolos);
        df.setMaximumFractionDigits(2);
        tvMensajeDesliza.setText("Desliza la tarjeta " + tipoTarjeta);
        btnEscanearTarjeta = findViewById(R.id.btnEscanearTarjeta);

        if (tipoTarjeta.equals("TanqueLleno")) {
            bCheck.setVisibility(View.VISIBLE);
            btnEscanearTarjeta.setVisibility(View.INVISIBLE);
        } else {
            bCheck.setVisibility(View.INVISIBLE);
            btnEscanearTarjeta.setVisibility(View.VISIBLE);
        }

        if (model.equals("i9000S")) {
            //PAra inicializar el escaner se debe inicilizar en el metodo OnResume
            scanManagerProvides = new ScanManagerProvides();
            result = "";
            btnEscanearTarjeta.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if (view.getId() == R.id.btnEscanearTarjeta) {
                        if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                            if (scanManagerProvides.getTriggerMode() == Triggering.HOST) {
                                scanManagerProvides.stopDecode();
                            }
                        }
                        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                            scanManagerProvides.startDecode();
                        }
                    }
                    return false;
                }
            });
        }

        //NFC
        bCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exec.execute(new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        byte CardType[] = new byte[2];
                        byte Atq[] = new byte[14];
                        char SAK = 1;
                        byte sak[] = new byte[1];
                        sak[0] = (byte) SAK;
                        byte SN[] = new byte[10];
                        scanCard = piccReader.request(CardType, Atq);
                        if (scanCard > 0) {
                            SNLen = piccReader.antisel(SN, sak);
                            Log.d(TAG, "SNLen = " + SNLen);
                            Message msg = handler.obtainMessage(MSG_FOUND_UID);
                            msg.obj = bytesToHexString(SN, SNLen);
                            handler.sendMessage(msg);
                        }
                    }
                }, "picc check"));
            }
        });


        piccReader = new PiccManager();
        exec = Executors.newSingleThreadExecutor();
        AbrirConexion();
    }

    //NFC
    private void AbrirConexion(){
        exec.execute(new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                int ret = piccReader.open();
                if (ret == 0) {
                } else {
                    return;
                }
            }
        }, "picc open"));

    }
    public static String bytesToHexString(byte[] src, int len) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        if (len <= 0) {
            return "";
        }
        for (int i = 0; i < len; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }


    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() != KeyEvent.KEYCODE_ENTER) { //Not Adding ENTER_KEY to barcode String
            char pressedKey = (char) event.getUnicodeChar();
            result += pressedKey;
        }
        if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {  //Any method handling the data
//            Toast.makeText(MonederosElectronicos.this, result, Toast.LENGTH_SHORT).show();
            ObtieneDatosQrPuntada();
        }
        return super.dispatchKeyEvent(event);
    }


    protected void onActivityResult (int requestCode, int resulCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resulCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(getApplicationContext(), "Lectura Cancelada", Toast.LENGTH_SHORT).show();
            } else {
//                if (!result.getContents().contains(",")){
//                    Toast.makeText(getApplicationContext(), "El código QR no contiene descuento asociado", Toast.LENGTH_SHORT).show();
//                } else{
                    ObtieneDatosQrPuntada();
//                }
            }
        } else {
            super.onActivityResult(requestCode, resulCode, data);
        }
    }

    private void ObtieneDatosQrPuntada(){
        //Se envia resultado de Qr a endpoint para recuperar los datos de la tarjeta, y los descuentos por combustible

        numeroTarjeta = "4000052500200001";
        descuentoCombustible1 = 0.5;
        descuentoCombustible2 = 0.4;
        descuentoCombustible3 = 0.6;
        NIP = "";
        if (PagoPuntada.equals("si")) {
            Intent intent = new Intent(getApplicationContext(), PosicionPuntadaRedimir.class); //ENVIABA A SeccionTarjeta cambio a PosicionPuntadaRedimir
            intent.putExtra("track", numeroTarjeta);
            intent.putExtra("nip", NIP);
            intent.putExtra("lugarproviene", lugarProviene);
            intent.putExtra("descuento1", descuentoCombustible1);
            intent.putExtra("descuento2", descuentoCombustible2);
            intent.putExtra("descuento3", descuentoCombustible3);
            startActivity(intent);
            finish();
        } else {
            EnviaProcesoPuntadaAcumular(numeroTarjeta);
        }
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
            scanManagerProvides.initScan(MonederosElectronicos.this);
        }
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
        if (Enviadodesde.equals("formaspago") | Enviadodesde.equals("CarritoTransacciones") | Enviadodesde.equals("diferentesformaspago")) {
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
//                .baseUrl("http://" + data.getIpEstacion() + "/CorpogasService/")
                .baseUrl("http://" + data.getIpEstacion() + "/CorpogasService_entities_token/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        EndPoints obtenNumeroTarjetero = retrofit.create(EndPoints.class);
        Call<RespuestaApi<Bin>> call = obtenNumeroTarjetero.getBin(idSucursal, bin, data.getToken());
        call.enqueue(new Callback<RespuestaApi<Bin>>() {

            @Override
            public void onResponse(Call<RespuestaApi<Bin>> call, Response<RespuestaApi<Bin>> response) {

                if (!response.isSuccessful()) {
                    if (response.code() == 401) {
                        GlobalToken.errorToken(MonederosElectronicos.this);
                    } else {
                        Toast.makeText(MonederosElectronicos.this, response.message(), Toast.LENGTH_SHORT).show();
                    }
                    return;
                }
                respuestaApiBin = response.body();

                boolean correcto = respuestaApiBin.Correcto;
                if (correcto) {
                    String mesanje = respuestaApiBin.Mensaje;
                    long formaPagoId = respuestaApiBin.getObjetoRespuesta().getTipoMonedero().PaymentMethodId;    //tiopoMonedero.getString("PaymentMethodId"); //TipoMonederoId
                    long idMonedero = respuestaApiBin.getObjetoRespuesta().getTipoMonedero().Id;//tiopoMonedero.getString("Id");

                    if (idMonedero == 1 && formaPagoId == 12) { //PUNTADA
                        if (tipoTarjeta.equals("TanqueLleno")) {
                            String titulo = "AVISO";
                            String mensaje = "Tarjeta inválida, NO ES TARJETA TANQUELLENO";
                            Modales modales = new Modales(MonederosElectronicos.this);
                            View view1 = modales.MostrarDialogoAlertaAceptar(MonederosElectronicos.this, mensaje, titulo);
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
                        } else {
                            String titulo = "PUNTADA";
                            String mensaje = "Ingresa el NIP de la tarjeta Puntada" ;
                            Modales modales = new Modales(MonederosElectronicos.this);
                            View viewLectura = modales.MostrarDialogoInsertaDato(MonederosElectronicos.this, mensaje, titulo);
                            EditText edtProductoCantidad = ((EditText) viewLectura.findViewById(R.id.textInsertarDato));
                            edtProductoCantidad.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
                            viewLectura.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    String cantidad = edtProductoCantidad.getText().toString();
                                    if (cantidad.isEmpty()){
                                        edtProductoCantidad.setError("Ingresa el NIP de la tarjeta Puntada");
                                    }else {
                                        String NIP = cantidad;
                                        Intent intent = new Intent(getApplicationContext(), PosicionPuntadaRedimir.class); //ENVIABA A SeccionTarjeta cambio a PosicionPuntadaRedimir
                                        intent.putExtra("track", mesanje);
                                        intent.putExtra("nip", NIP);
                                        intent.putExtra("lugarproviene", lugarProviene);
                                        intent.putExtra("descuento", "0");
                                        intent.putExtra("descuento1", 0.0);
                                        intent.putExtra("descuento2", 0.0);
                                        intent.putExtra("descuento3", 0.0);
                                        startActivity(intent);
                                        finish();

                                    }
                                }
                            });
                            viewLectura.findViewById(R.id.buttonNo).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    modales.alertDialog.dismiss();
                                    Intent intent = new Intent(getApplicationContext(), Menu_Principal.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });
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
                    } else {
                        if (!tipoTarjeta.equals("TanqueLleno")) {
                            String titulo = "AVISO";
                            String mensaje = "Tarjeta inválida, NO ES TARJETA PUNTADA";
                            Modales modales = new Modales(MonederosElectronicos.this);
                            View view1 = modales.MostrarDialogoAlertaAceptar(MonederosElectronicos.this, mensaje, titulo);
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
                        } else {
                            if (idMonedero == 2 && formaPagoId == 11) { //TANQUE LLENO CENTRO
    //                            Toast.makeText(getApplicationContext(), "A qui va tanque lleno", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), TanqueLlenoNip.class);  //seccionTanqueLleno
                                intent.putExtra("track", mesanje);
                                intent.putExtra("lugarProviene", Enviadodesde);
                                startActivity(intent);
                                finish();
                            }
                            if (idMonedero == 3 && formaPagoId == 11) { //TANQUE LLENO SURESTE
                                Toast.makeText(getApplicationContext(), "Aqui va tanque lleno Sureste", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(getApplicationContext(), TanqueLlenoNip.class);  //seccionTanqueLleno
                                intent.putExtra("lugarProviene", Enviadodesde);
                                intent.putExtra("track", mesanje);
                                startActivity(intent);
                                finish();
                            }
                        }
                    }
                } else {
                    String titulo = "AVISO";
                    String mensaje = "Tarjeta inválida";
                    Modales modales = new Modales(MonederosElectronicos.this);
                    View view1 = modales.MostrarDialogoAlertaAceptar(MonederosElectronicos.this, mensaje, titulo);
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

    public void obtieneSaldoTarjeta(String numerotarjeta) {
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

//        String url = "http://" + ipEstacion + "/CorpogasService/api/puntadas/actualizaPuntos/clave/" + clave;
        String url = "http://" + ipEstacion + "/CorpogasService_entities_token/api/puntadas/actualizaPuntos/clave/" + clave;

        StringRequest eventoReq = new StringRequest(Request.Method.POST, url,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            String Proviene = getIntent().getStringExtra("lugarproviene");
                            JSONObject resultado = new JSONObject(response);
                            String estado = resultado.getString("Estado");
                            String mensaje = resultado.getString("Mensaje");
                            final String saldo = resultado.getString("Saldo");
                            if (estado == "true") { //    mensaje.equals("null")
                                if (Double.parseDouble(saldo) > 0) {
                                    String track = getIntent().getStringExtra("track");
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

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    String titulo = "SALDO INSUFICIENTE";
                                }
                            } else {
                                try {
                                    String titulo = "AVISO";
                                    String mensajes;
                                    if (mensaje.equals("null")) {
                                        mensajes = "Sin conexón con la consola";
                                    } else {
                                        mensajes = mensaje;
                                    }
                                    Modales modales = new Modales(MonederosElectronicos.this);
                                    View view1 = modales.MostrarDialogoAlertaAceptar(MonederosElectronicos.this, mensajes, titulo);
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
                                } catch (Exception e) {
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
//                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                if (error.networkResponse.statusCode == 401) {
                    GlobalToken.errorToken(MonederosElectronicos.this);
                } else {
                    Toast.makeText(MonederosElectronicos.this, error.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                //Obtenemos los parmetros a enviar
                String sproducto = "[]";
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("EmpleadoId", idusuario);
                params.put("SucursalId", data.getIdSucursal());
                params.put("RequestID", "33");
                params.put("PosicionCarga", posicioncarga);
                params.put("Tarjeta", numerotarjeta);
                params.put("NuTarjetero", numeroTarjetero);
                params.put("NIP", nip);
                params.put("Productos", sproducto);
                params.put("Authorization", data.getToken());

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
        bar = new ProgressDialog(MonederosElectronicos.this);
        bar.setTitle("Conectando con Puntada");
        bar.setMessage("Ejecutando... ");
        bar.setIcon(R.drawable.registrarpuntada);
        bar.setCancelable(false);
        bar.show();

        if (!Conexion.compruebaConexion(this)) {
            Toast.makeText(getBaseContext(), "Sin conexión a la red ", Toast.LENGTH_SHORT).show();
            Intent intent1 = new Intent(getApplicationContext(), Menu_Principal.class);

            startActivity(intent1);
            finish();
        } else {
            SQLiteBD data = new SQLiteBD(getApplicationContext());
//            String URL = "http://" + data.getIpEstacion() + "/CorpogasService/api/bines/obtieneBinTarjeta/sucursalId/" + data.getIdSucursal();
            String URL = "http://" + data.getIpEstacion() + "/CorpogasService_entities_token/api/bines/obtieneBinTarjeta/sucursalId/" + data.getIdSucursal();

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
                                if (PagoPuntada.equals("si")) {
                                    EnviaProcesoPuntadaRedimir(mesanje);
                                } else {
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
//                    Intent intente = new Intent(getApplicationContext(), Menu_Principal.class);
//                    startActivity(intente);
                    if (error.networkResponse.statusCode == 401) {
                        GlobalToken.errorToken(MonederosElectronicos.this);
                    } else {
                        Toast.makeText(MonederosElectronicos.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            }) {
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<String, String>();
                    headers.put("Authorization", data.getToken());
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
//        String url = "http://" + ipEstacion + "/CorpogasService/api/puntadas/actualizaPuntos/numeroEmpleado/" + numeroempleadosucursal;
        String url = "http://" + ipEstacion + "/CorpogasService_entities_token/api/puntadas/actualizaPuntos/numeroEmpleado/" + numeroempleadosucursal;


        StringRequest eventoReq = new StringRequest(Request.Method.POST, url,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            String Proviene = getIntent().getStringExtra("lugarproviene");
                            JSONObject resultado = new JSONObject(response);
                            String estado = resultado.getString("Estado");
                            String mensaje = resultado.getString("Mensaje");
                            final String saldo = resultado.getString("Saldo");
                            if (estado == "true") { //    mensaje.equals("null")
                                if (Double.parseDouble(saldo) > 0) {
                                    String track = getIntent().getStringExtra("track"); //"4000052500200001";
                                    Long Idusuario = getIntent().getLongExtra("IdUsuario", 0);
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
                                                simbolos.setDecimalSeparator('.');
                                                df = new DecimalFormat("$#,###.00##", simbolos);
                                                df.setMaximumFractionDigits(2);

                                                String titulo = "AVISO";
                                                String mensajes = "Tarjeta No. " + track + " con Saldo: " + df.format(saldo);
                                                final Modales modales = new Modales(MonederosElectronicos.this);
                                                View view1 = modales.MostrarDialogoCorrecto(MonederosElectronicos.this, mensajes);
                                                view1.findViewById(R.id.buttonAction).setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        modales.alertDialog.dismiss();
                                                        Intent intent = new Intent(getApplicationContext(), Menu_Principal.class);
                                                        startActivity(intent);
                                                        finish();
                                                    }
                                                });
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                            break;
                                        case "Registrar":

                                            break;
                                        default:
                                            break;
                                    }
                                } else {
                                    String titulo = "SALDO INSUFICIENTE";
                                    String mensajes = "El Saldo en la tarjeta es de: $" + saldo;
                                    Modales modales = new Modales(MonederosElectronicos.this);
                                    View view1 = modales.MostrarDialogoAlertaAceptar(MonederosElectronicos.this, mensajes, titulo);
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
                            } else {
                                try {
                                    String titulo = "AVISO";
                                    String mensajes;
                                    if (mensaje.equals("null")) {
                                        mensajes = "Sin conexón con la consola";
                                    } else {
                                        mensajes = mensaje;
                                    }
                                    Modales modales = new Modales(MonederosElectronicos.this);
                                    View view1 = modales.MostrarDialogoAlertaAceptar(MonederosElectronicos.this, mensajes, titulo);
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
                                } catch (Exception e) {
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
//                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                if (error.networkResponse.statusCode == 401) {
                    GlobalToken.errorToken(MonederosElectronicos.this);
                } else {
                    Toast.makeText(MonederosElectronicos.this, error.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                //Obtenemos los parmetros a enviar
                String sproducto = "[]";
                //JSONArray sproducto = new JSONArray()
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();

                params.put("NuTarjetero", numeroTarjetero);
                params.put("SucursalId", data.getIdSucursal());
                params.put("RequestID", "35");
                params.put("PosicionCarga", String.valueOf(posiciondeCarga));
                params.put("Tarjeta", NumeroDeTarjeta);
                params.put("Productos", sproducto);
                params.put("Authorization", data.getToken());
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
            bar = new ProgressDialog(MonederosElectronicos.this);
            bar.setTitle("Acumulando Puntada");
            bar.setMessage("Ejecutando... ");
            bar.setIcon(R.drawable.registrarpuntada);
            bar.setCancelable(false);
            bar.show();

            JSONObject datos = new JSONObject();

            String idusuario = getIntent().getStringExtra("idusuario");
            String nombrepago = getIntent().getStringExtra("NombrePago");
            String idoperativa = getIntent().getStringExtra("idoperativa");
            String numpago = getIntent().getStringExtra("formapagoid");
            String nombreCompleto = getIntent().getStringExtra("NombreCompleto");

            JSONArray myArray = new JSONArray();
            SQLiteBD data = new SQLiteBD(getApplicationContext());
//            String url = "http://" + data.getIpEstacion() + "/CorpogasService/api/puntadas/actualizaPuntos/numeroEmpleado/" + numeroempleadosucursal;
            String url = "http://" + data.getIpEstacion() + "/CorpogasService_entities_token/api/puntadas/actualizaPuntos/numeroEmpleado/" + numeroempleadosucursal;
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
                        bar.cancel();
                    }
                    if (estado.equals("true")) {
                        if (Enviadodesde.equals("formaspago")) {
                            if (tipoTarjeta.equals("Puntadaformapago")) {
                                Double MontoenCarrito = getIntent().getDoubleExtra("montoenlacanasta", 0);
                                //Enviamos a la pantalla de captura de diferentes formas de pago   MontoenCanasta
                                String banderaHuella = getIntent().getStringExtra("banderaHuella");
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
                            } else {
                                if (idformaPago.equals("3") || idformaPago.equals("5") || idformaPago.equals("13")) {
                                    DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
                                    simbolos.setDecimalSeparator('.');
                                    DecimalFormat df = new DecimalFormat("####.00##", simbolos);

                                    df.setMaximumFractionDigits(2);
                                    data.getWritableDatabase().delete("PagoTarjeta", null, null);
                                    data.close();
                                    data.InsertarDatosPagoTarjeta("1", posiciondeCarga, idformaPago, Double.toString(montoenlacanasta), "0", "1", "0", "", "", "", Double.toString(montoenlacanasta),"");
                                    Intent intentVisa = new Intent(getApplicationContext(), VentaPagoTarjeta.class);//DiferentesFormasPagoPuntada
                                    intentVisa.putExtra("lugarProviene", "formaspago");
                                    intentVisa.putExtra("posicioncarga", posiciondeCarga);
                                    intentVisa.putExtra("formapagoid", numpago);
                                    intentVisa.putExtra("montoencanasta", "$" + df.format(montoenlacanasta));
                                    intentVisa.putExtra("numeroTarjeta", "");
                                    startActivity(intentVisa);
                                    finish();
                                } else {
                                    if (idformaPago.equals("2")){
                                        Intent intentVale = new Intent(getApplicationContext(), ValesPapel.class);
                                        intentVale.putExtra("Enviadodesde", "formaspago");
                                        intentVale.putExtra("posicioncarga", posiciondeCarga);
                                        intentVale.putExtra("idoperativa", idoperativa);
                                        intentVale.putExtra("formapagoid", "2");
                                        intentVale.putExtra("montoencanasta", montoenlacanasta);
                                        startActivity(intentVale);
                                        finish();
                                    }else{
                                        Double MontoenCarrito = getIntent().getDoubleExtra("montoenlacanasta", 0);
                                        Intent intente = new Intent(getApplicationContext(), ImprimePuntada.class);
                                        intente.putExtra("posicioncarga", posiciondeCarga);
                                        intente.putExtra("idoperativa", idoperativa);
                                        intente.putExtra("idformapago", numpago);
                                        intente.putExtra("nombrepago", nombrepago);
                                        intente.putExtra("montoencanasta", MontoenCarrito);
                                        intente.putExtra("lugarProviene", "FormasPago");
                                        intente.putExtra("lugarProviene2", "");
                                        startActivity(intente);
                                        finish();
                                    }
                                }
                            }
                        } else {
                            Double MontoenCarrito = 0.0;
                            Intent intente = new Intent(getApplicationContext(), ImprimePuntada.class);
                            intente.putExtra("posicioncarga", posiciondeCarga);
                            intente.putExtra("idoperativa", idoperativa);
                            intente.putExtra("idformapago", numpago);
                            intente.putExtra("nombrepago", nombrepago);
                            intente.putExtra("montoencanasta", MontoenCarrito);
                            intente.putExtra("lugarProviene", "DiferentesFormasPago");
                            intente.putExtra("lugarProviene2", "");
                            startActivity(intente);
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
                            bar.cancel();
                        }
                    }
                }
            }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
//                    Toast.makeText(MonederosElectronicos.this, error.toString(), Toast.LENGTH_SHORT).show();
                    if (error.networkResponse.statusCode == 401) {
                        GlobalToken.errorToken(MonederosElectronicos.this);
                    } else {
                        Toast.makeText(MonederosElectronicos.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            }) {
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<String, String>();
                    headers.put("Authorization", data.getToken());
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
            request_json.setRetryPolicy(new DefaultRetryPolicy(900000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            queue.add(request_json);

        }
    }
}