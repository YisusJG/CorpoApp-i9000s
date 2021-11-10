package com.corpogas.corpoapp.Fajillas;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.corpogas.corpoapp.Conexion;
import com.corpogas.corpoapp.Configuracion.SQLiteBD;
import com.corpogas.corpoapp.Corte.ResumenActivity;
import com.corpogas.corpoapp.Entities.Classes.RespuestaApi;
import com.corpogas.corpoapp.Entities.Cortes.CierreFajilla;
import com.corpogas.corpoapp.Entities.Estaciones.RecepcionFajilla;
import com.corpogas.corpoapp.Entities.Estaciones.RecepcionFajillaEntregada;
import com.corpogas.corpoapp.Entities.Estaciones.ResumenFajilla;
import com.corpogas.corpoapp.Entities.TipoFajilla;
import com.corpogas.corpoapp.Interfaces.Endpoints.EndPoints;
import com.corpogas.corpoapp.Menu_Principal;
import com.corpogas.corpoapp.Modales.Modales;
import com.corpogas.corpoapp.R;
import com.corpogas.corpoapp.Tickets.PosicionCargaTickets;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AutorizaFajillas extends AppCompatActivity {
    //datos para huella
    private static final String TAG = "FingerprintActivity";
    TextView usuario, carga, txtEtiqueta, titulo, numerodispositivo;
    String iduser, lugarProviene, FechaF;
    String EstacionId, sucursalId, ipEstacion, strStore, empleadoIdentificado, numeroempleado, rolid, nombrecompleto, TipoBiometrico = "2", TipoAutenticacion = "4", idusuario, idRoll;

    final JSONObject datos = new JSONObject();
    private boolean m_reset = false;
    boolean ejecutar = true;
    SQLiteBD db;

    Button btnhuella;
    private byte[] isoFeatureTmp;
    //    String empleadoIdentificado;
    private FingerprintManager mFingerprintManager;
    private Handler mHandler;
    TextView textResultado;
    Boolean banderaIdentificado;
    List<String> AEmpleadoId;
    List<byte[]> AHuella;
    EditText pasword;
    String numeroTarjetero ;
    Bundle args = new Bundle();
    String banderaHuella,  claveUsuario;

    LottieAnimationView animationView7;


    String claveUsuarioActual, islaId;
    Long empleadoNumeroActual, cierreId, totalFajillas, valorTipoFajilla, numeroEmpleadoSale;


    JSONObject datosFajillas;
    boolean pasa;

//    List<RecepcionFajilla> recepcionFajilla = new ArrayList<>();

    List<RecepcionFajilla> lRecepcionFajillas = new ArrayList<>();
    RespuestaApi<List<ResumenFajilla>> respuestaApiResumenFajilla;
    String nombreEmpleadoRecibio, fechaUIltimaFajilla;


    int cantidadFajillasBilletes, cantidadFajillasMonedas;

    ArrayList<String> Folio;
    ArrayList<String> tipoFajillas;

    JSONObject datosFajillasFinal;
    JSONArray jsonArrayDatosFajillas;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autoriza_fajillas);

        SQLiteBD data = new SQLiteBD(this);
//        dbCorte = new CorteDB(getApplicationContext());
        this.setTitle(data.getRazonSocial());
        this.setTitle(data.getNombreEstacion() + " ( EST.:" + data.getNumeroEstacion() + ")");
        sucursalId= data.getIdSucursal();
        EstacionId = data.getIdEstacion();
        ipEstacion = data.getIpEstacion();
        numeroTarjetero = data.getIdTarjtero();
        numeroEmpleadoSale = Long.parseLong(data.getNumeroEmpleado());
//        Globals.DefaultImageProcessing = Reader.ImageProcessing.IMG_PROC_DEFAULT;
        banderaHuella = data.getLectorHuella();//getIntent().getStringExtra( "banderaHuella");
//        btnhuella  =  findViewById(R.id.btnhuella);
        // btnhuella.setOnClickListener(despachdorclave.this);
        mHandler = new Handler(Looper.getMainLooper());
        animationView7 = findViewById(R.id.animationView3);
//        fragmentFajillas = new FajillasFragment();
        //lee valores usuario y carga
//        usuario= findViewById(R.id.usuario);
//        carga = findViewById(R.id.carga);


        lugarProviene =  getIntent().getStringExtra("lugarProviene");
//        Button siguinte = findViewById(R.id.btnsiguientetic);
//        siguinte.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                idUsuario();
//            }
//        });

        pasword = findViewById(R.id.edtclavedespachador);
        txtEtiqueta = findViewById(R.id.txtEtiqueta);
        titulo = findViewById(R.id.titulo);
        numerodispositivo =  (TextView) findViewById(R.id.txtnumerodispositivo);

        numerodispositivo.setText("");
        numerodispositivo.setVisibility(View.INVISIBLE);
//        lFajillas = dbCorte.getFajillas();
//        ObtenerValorFajillasBilletes();
//        ObtenerValorFajillasMonedas();


        switch (lugarProviene) {
            case "corteFajillas":
            case "CorteProductos":
                empleadoNumeroActual =  getIntent().getLongExtra("numeroEmpleadoJI", 0);
                claveUsuarioActual =  getIntent().getStringExtra("ClaveUsuarioActual");
                cierreId =  getIntent().getLongExtra("cierreid", 0);

                if (lugarProviene.equals("corteFajillas")){
                    txtEtiqueta.setText("CONFIRMA Recepción de Fajilla");
                }else{
                    txtEtiqueta.setText("CONFIRMA Inventario de Productos");
                }
                if (banderaHuella.equals("true")){
                    titulo.setText("Ingresa tu Huella de Jefe de Isla");
                    animationView7.setAnimation("fingerprint.json");
                }else{
                    titulo.setText("Ingresa tu Contraseña de Jefe de Isla");
                }
                break;
            case "CancelarFajillas":
//                empleadoNumeroActual =  Long.parseLong(data.getNumeroEmpleado()); //getIntent().getLongExtra("numeroEmpleadoJI", 0);
                txtEtiqueta.setText("Cancelar Pago");
//                if (banderaHuella.equals("true")){
//                    titulo.setText("Ingresa tu Huella de Jefe de Isla");
                animationView7.setAnimation("confirmation.json");
//                }else{
                titulo.setText("Ingresa tu Contraseña de Jefe de Isla");
//                }
                break;
            case "EntregaFajillas":
                empleadoNumeroActual =  Long.parseLong(data.getNumeroEmpleado()); //getIntent().getLongExtra("numeroEmpleadoJI", 0);
                txtEtiqueta.setText("CONFIRMA Entrega Fajillas");
//                if (banderaHuella.equals("true")){
//                    titulo.setText("Ingresa tu Huella de Jefe de Isla");
                    animationView7.setAnimation("confirmation.json");
//                }else{
                    titulo.setText("Ingresa tu Contraseña de Jefe de Isla");
//                }
                break;


            default:
        }
//        //Inicializacion y carga de huella
//        if (banderaHuella.equals("true")){
//            InicializaLector();
//            pasword.setVisibility(View.INVISIBLE);
//        }else{
            pasword.setVisibility(View.VISIBLE);
//        }


        if (!Conexion.compruebaConexion(this)) {
            Toast.makeText(getBaseContext(),"Sin conexión a la red ", Toast.LENGTH_SHORT).show();
            Intent intent1 = new Intent(getApplicationContext(), Menu_Principal.class);
            startActivity(intent1);
            finish();
        }
    }

//    @RequiresApi(api = Build.VERSION_CODES.N)
//    private void ObtenerValorFajillasBilletes(){
//        lFajillas = dbCorte.getFajillas().stream().filter(x -> x.TipoFajilla == 1).collect(Collectors.toList());
//        for (CierreFajilla item : lFajillas) {
//            cantidadFajillasBilletes = item.getFajillasBilletes();
//        }
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.N)
//    private void ObtenerValorFajillasMonedas() {
//        lFajillas = dbCorte.getFajillas().stream().filter(x -> x.TipoFajilla == 2).collect(Collectors.toList());
//        for (CierreFajilla item : lFajillas) {
//            cantidadFajillasMonedas = item.getFajillasMonedas();
//
//        }
//    }



    private void Salir() {
        if (banderaHuella.equals("false")){
            //Se instancia y se llama a la clase Venta de Productos
            Intent intent1 = new Intent(getApplicationContext(), Menu_Principal.class);
            //inicia el activity
            startActivity(intent1);
            finish();
        }else{
            //final String pass = pasword.getText().toString();
            try {
                switch (lugarProviene) {
                    case "corteFajillas":
                    case "CorteProductos":
                        finish();
                        break;
                    case "EntregaFajillas":
                        //Se instancia y se llama a la clase Venta de Productos
                        Intent intent1 = new Intent(getApplicationContext(), Menu_Principal.class);
                        //Se envian los parametros de posicion y usuario
//                        intent1.putExtra("device_name", m_deviceName);
                        //inicia el activity
                        startActivity(intent1);
                        finish();
                        break;
                }

            } catch (Exception e) {

            }
        }
    }

//    protected void CheckDevice(){
//        try
//        {
//            m_reader.Open(Reader.Priority.EXCLUSIVE);
//            Reader.Capabilities cap = m_reader.GetCapabilities();
//            m_reader.Close();
//        }
//        catch (UareUException e1)
//        {
//            e1.printStackTrace();
//        }
//    }


    //Metodo para regresar a la actividad principal
    @Override
    public void onBackPressed() {
        if (banderaHuella.equals("true")){
            if (lugarProviene.equals("CorteProductos") || lugarProviene.equals("corteFajillas") || lugarProviene.equals("CancelarFajillas")) {
                Toast.makeText(this, "No se permite salir hasta confirmar con usuario", Toast.LENGTH_SHORT).show();
            }else{
                Thread.interrupted();
                ejecutar=false;
                numerodispositivo.setText("Coloca cualquier dedo en el lector");
                numerodispositivo.setVisibility(View.VISIBLE);

                //Se instancia y se llama a la clase Venta de Productos
                Intent intent1 = new Intent(getApplicationContext(), Menu_Principal.class);
                //inicia el activity
                startActivity(intent1);
                finish();
            }
        }else{
            if (lugarProviene.equals("CorteProductos") || lugarProviene.equals("corteFajillas") || lugarProviene.equals("CancelarFajillas")) {
                Toast.makeText(this, "No se permite salir hasta confirmar con usuario", Toast.LENGTH_SHORT).show();
            }else{
                //Se instancia y se llama a la clase Venta de Productos
                Intent intent1 = new Intent(getApplicationContext(), Menu_Principal.class);
                //inicia el activity
                startActivity(intent1);
                finish();
            }
        }
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
        //Se lee el password del objeto y se asigna a variable
        String pass;

        pass = pasword.getText().toString();
        if (pass.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Ingresa la contraseña", Toast.LENGTH_SHORT).show();
        } else {
            validaClave();
        }
    }

    private void  validaClave(){
        if (!Conexion.compruebaConexion(this)) {
            Toast.makeText(getBaseContext(),"Sin conexión a la red ", Toast.LENGTH_SHORT).show();
            Intent intent1 = new Intent(getApplicationContext(), Menu_Principal.class);
//            findViewById(R.id.PruebaFondoVentas).setBackgroundColor(Color.BLACK);
            startActivity(intent1);
            finish();
        }else {

            final String pass = pasword.getText().toString();

            //Conexion con la base y ejecuta valida clave
            String url = "http://" + ipEstacion + "/CorpogasService/api/Empleados/clave/" + pass;
            // Utilizamos el metodo Post para validar la contraseña
            StringRequest eventoReq = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                //Se instancia la respuesta del json
                                JSONObject validar = new JSONObject(response);
                                String correcto = validar.getString("Correcto");
                                String mensaje = validar.getString("Mensaje");
                                String objetorespuesta = validar.getString("ObjetoRespuesta");

                                JSONObject respuestaobjeto = new JSONObject(objetorespuesta);
                                String valido = respuestaobjeto.getString("Activo");
                                idusuario = respuestaobjeto.getString("NumeroEmpleado"); //""Id"
                                nombrecompleto = respuestaobjeto.getString("NombreCompleto");
                                numeroempleado = respuestaobjeto.getString("NumeroEmpleado");
                                idRoll = respuestaobjeto.getString("RolId");
                                if (valido == "true") {
                                    ejecutar=false;
//                                numerodispositivo.setText("Coloca cualquier dedo en el lector");
//                                numerodispositivo.setVisibility(View.VISIBLE);
                                    if (lugarProviene.equals("Reimprimir")){
                                        if (idRoll.equals("3") || idRoll.equals("1")){
                                            Intent intent = new Intent(getApplicationContext(), PosicionCargaTickets.class);
                                            intent.putExtra("lugarproviene", "Reimprimir"); //
                                            intent.putExtra("numeroEmpleado", numeroempleado);

                                            startActivity(intent);
                                            finish();
                                        }else{
                                            String titulo = "AVISO";
                                            String mensajes = "Usuario No es un Jefe de Isla o Geremte";
                                            Modales modales = new Modales(AutorizaFajillas.this);
                                            View view1 = modales.MostrarDialogoAlertaAceptar(AutorizaFajillas.this, mensajes, titulo);
                                            view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    modales.alertDialog.dismiss();
                                                    pasword.setText("");
                                                }
                                            });
                                        }
                                    }

                                    if (lugarProviene.equals("CancelarFajillas")){
                                        if (idRoll.equals("3") || idRoll.equals("1")){
                                            Intent intent = new Intent(getApplicationContext(), CancelarPagoTransaccion.class);
//                                            intent.putExtra("lugarproviene", "CancelarFajillas"); //
                                            startActivity(intent);
                                            finish();
                                        }else{
                                            String titulo = "AVISO";
                                            String mensajes = "Usuario No es un Jefe de Isla o Geremte";
                                            Modales modales = new Modales(AutorizaFajillas.this);
                                            View view1 = modales.MostrarDialogoAlertaAceptar(AutorizaFajillas.this, mensajes, titulo);
                                            view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    modales.alertDialog.dismiss();
                                                    pasword.setText("");
                                                }
                                            });
                                        }
                                    }

                                    if (lugarProviene.equals("Corte") || lugarProviene.equals("corteFajillas") || lugarProviene.equals("entregaFajillas")){ //|| lugarProviene.equals("EntregaFajillas")
                                        if (lugarProviene.equals("corteFajillas")){
                                            if (idRoll.equals("3")){
                                                enviaActividadSiguiente(idusuario, pass, idRoll);
                                            }else{
                                                String titulo = "AVISO";
                                                String mensajes = "Usuario No es un Jefe de Isla";
                                                Modales modales = new Modales(AutorizaFajillas.this);
                                                View view1 = modales.MostrarDialogoAlertaAceptar(AutorizaFajillas.this, mensajes, titulo);
                                                view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        modales.alertDialog.dismiss();
                                                        pasword.setText("");
                                                    }
                                                });
                                            }
                                        }else{
                                            if (empleadoNumeroActual.equals(Long.parseLong(idusuario))){
                                                String titulo = "Debe validar otro Usuario";
                                                String mensajes = "Debe Validar el Jefe de Isla Entrante";
                                                Modales modales = new Modales(AutorizaFajillas.this);
                                                View view1 = modales.MostrarDialogoAlertaAceptar(AutorizaFajillas.this, mensajes, titulo);
                                                view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        modales.alertDialog.dismiss();
                                                        pasword.setText("");
                                                    }
                                                });
                                            }else{
                                                if (idRoll.equals("3") || idRoll.equals("1")){
                                                    enviaActividadSiguiente(idusuario, pass, idRoll);
                                                }else{
                                                    String titulo = "AVISO";
                                                    String mensajes = "Usuario No es un Jefe de Isla o Geremte";
                                                    Modales modales = new Modales(AutorizaFajillas.this);
                                                    View view1 = modales.MostrarDialogoAlertaAceptar(AutorizaFajillas.this, mensajes, titulo);
                                                    view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View view) {
                                                            modales.alertDialog.dismiss();
                                                            pasword.setText("");
                                                        }
                                                    });
                                                }
                                            }
                                        }
                                    }else{
                                        if (idRoll.equals("3") || idRoll.equals("1")){
                                            enviaActividadSiguiente(idusuario, pass, idRoll);
                                        }else{
                                            String titulo = "AVISO";
                                            String mensajes = "Usuario No es un Jefe de Isla o Geremte";
                                            Modales modales = new Modales(AutorizaFajillas.this);
                                            View view1 = modales.MostrarDialogoAlertaAceptar(AutorizaFajillas.this, mensajes, titulo);
                                            view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    modales.alertDialog.dismiss();
                                                    pasword.setText("");
                                                }
                                            });
                                        }
                                    }
                                } else {
                                    //Si no es valido se envia mensaje
                                    //vm.mostrarVentana("Usuario No Activo");
                                    String titulo = "AVISO";
                                    String mensajes = "Usuario No Activo";
                                    Modales modales = new Modales(AutorizaFajillas.this);
                                    View view1 = modales.MostrarDialogoAlertaAceptar(AutorizaFajillas.this, mensajes, titulo);
                                    view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            modales.alertDialog.dismiss();
                                            Intent intent = new Intent(getApplicationContext(), Menu_Principal.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    });
                                }
                            } catch (JSONException e) {
                                //herramienta  para diagnostico de excepciones
                                //e.printStackTrace();
                                String titulo = "AVISO";
                                String mensaje = "Clave inexistente";
                                Modales modales = new Modales(AutorizaFajillas.this);
                                View view1 = modales.MostrarDialogoAlertaAceptar(AutorizaFajillas.this, mensaje, titulo);
                                view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        modales.alertDialog.dismiss();
                                        pasword.setText("");
                                    }
                                });
                            }
                        }
                        //funcion para capturar errores
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();
                    //VolleyLog.e("Error: ", volleyError.getMessage());
                    String algo = new String(error.networkResponse.data);
                    try {
                        //creamos un json Object del String algo
                        JSONObject errorCaptado = new JSONObject(algo);
                        //Obtenemos el elemento ExceptionMesage del errro enviado
                        String errorMensaje = errorCaptado.getString("ExceptionMessage");
                        try {
                            String titulo = "AVISO";
                            String mensaje = "" + errorMensaje;
                            Modales modales = new Modales(AutorizaFajillas.this);
                            View view1 = modales.MostrarDialogoAlertaAceptar(AutorizaFajillas.this, mensaje, titulo);
                            view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
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

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });

            // Añade la peticion a la cola
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            eventoReq.setRetryPolicy(new DefaultRetryPolicy(12000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(eventoReq);
        }
        //-------------------------Aqui termina el volley --------------
    }

    private  void enviaActividadSiguiente(String idusuario, String pass, String idRoll){
        switch (lugarProviene){
            case "CorteProductos":
                GuardaAutorizacionCorteProducto(idusuario);
                break;
            case "EntregaFajillas":
                EnviaFajilla();
                break;
            case "corteFajillas":
                EnviaFajilla();
                break;
            default:
        }

    }

    private void EnviaFajilla(){
        if (!Conexion.compruebaConexion(this)) {
            Toast.makeText(getBaseContext(), "Sin conexión a la red ", Toast.LENGTH_SHORT).show();
            Intent intent1 = new Intent(getApplicationContext(), Menu_Principal.class);

            startActivity(intent1);
            finish();
        } else {
//            totalFajillas = getIntent().getLongExtra("TotalFajillas", 0);
            valorTipoFajilla = getIntent().getLongExtra("TipoFajilla", 0);
            Folio = getIntent().getStringArrayListExtra("Folio");
            tipoFajillas = getIntent().getStringArrayListExtra("TipoFajillas");


            datosFajillas = new JSONObject();
            jsonArrayDatosFajillas = new JSONArray();


            for (int i = 0; i < Folio.size(); i++) {
                    lRecepcionFajillas.add(new RecepcionFajilla(Long.parseLong(sucursalId), Long.parseLong(tipoFajillas.get(i)), Folio.get(i), 1));
            }
            // Esta linea nos da el resultado de lo que estoy enviando para checar en el postman como confirmacion
            //String json = new Gson().toJson(lRecepcionFajillas);

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://"+ ipEstacion  +"/corpogasService/")//http://" + data.getIpEstacion() + "/corpogasService_Entities_token/
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            EndPoints recibirFajillas = retrofit.create(EndPoints.class);
            if (lugarProviene.equals("corteFajillas")){
                Call<RespuestaApi<List<ResumenFajilla>>> call = recibirFajillas.postGuardaFajillasCorte(lRecepcionFajillas, numeroEmpleadoSale.toString(), idusuario);
                call.enqueue(new Callback<RespuestaApi<List<ResumenFajilla>>>() {
                    @Override
                    public void onResponse(Call<RespuestaApi<List<ResumenFajilla>>> call, retrofit2.Response<RespuestaApi<List<ResumenFajilla>>> response) {
                        if (!response.isSuccessful()) {
                            return;
                        }
                        respuestaApiResumenFajilla = response.body();

                        String validacion = String.valueOf(respuestaApiResumenFajilla.Correcto);
                        if (validacion.equals("true")) {
                            nombreEmpleadoRecibio = respuestaApiResumenFajilla.getObjetoRespuesta().get(0).NombreEmpleado;
                            fechaUIltimaFajilla = respuestaApiResumenFajilla.getObjetoRespuesta().get(0).FechaUltimaFajilla;

                            String titulo = "ENTREGA DE FAJILLAS";
                            String mensaje = "Fajilla Recibida por: \n" + nombreEmpleadoRecibio + " \n" + fechaUIltimaFajilla;
                            Modales modales = new Modales(AutorizaFajillas.this);
                            View view1 = modales.MostrarDialogoCorrecto(AutorizaFajillas.this, mensaje);
                            view1.findViewById(R.id.buttonAction).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    modales.alertDialog.dismiss();
                                    if (lugarProviene.equals("corteFajillas")){
                                        Intent intent = new Intent(getApplicationContext(), ResumenActivity.class);
                                        startActivity(intent);
                                    }else{
                                        Intent intent = new Intent(getApplicationContext(), Menu_Principal.class);
                                        startActivity(intent);
                                    }
                                    finish();
                                }
                            });
                        }else{
                            String estado = respuestaApiResumenFajilla.Mensaje;
                            String titulo = "AVISO ";
                            String mensaje = "" + estado;
                            Modales modales = new Modales(AutorizaFajillas.this);
                            View view1 = modales.MostrarDialogoAlertaAceptar(AutorizaFajillas.this,mensaje,titulo);
                            view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    modales.alertDialog.dismiss();
                                    Intent intent = new Intent(getApplicationContext(), Menu_Principal.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                        }
                    }

                    @Override
                    public void onFailure(Call<RespuestaApi<List<ResumenFajilla>>> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                });

            }else{
                Call<RespuestaApi<List<ResumenFajilla>>> call = recibirFajillas.postGuardaFajillas(lRecepcionFajillas, numeroEmpleadoSale.toString(), idusuario);
                call.enqueue(new Callback<RespuestaApi<List<ResumenFajilla>>>() {
                    @Override
                    public void onResponse(Call<RespuestaApi<List<ResumenFajilla>>> call, retrofit2.Response<RespuestaApi<List<ResumenFajilla>>> response) {
                        if (!response.isSuccessful()) {
                            return;
                        }
                        respuestaApiResumenFajilla = response.body();

                        String validacion = String.valueOf(respuestaApiResumenFajilla.Correcto);
                        if (validacion.equals("true")) {
                            nombreEmpleadoRecibio = respuestaApiResumenFajilla.getObjetoRespuesta().get(0).NombreEmpleado;
                            fechaUIltimaFajilla = respuestaApiResumenFajilla.getObjetoRespuesta().get(0).FechaUltimaFajilla;

                            String titulo = "ENTREGA DE FAJILLAS";
                            String mensaje = "Fajilla Recibida por: \n" + nombreEmpleadoRecibio + " \n" + fechaUIltimaFajilla;
                            Modales modales = new Modales(AutorizaFajillas.this);
                            View view1 = modales.MostrarDialogoCorrecto(AutorizaFajillas.this, mensaje);
                            view1.findViewById(R.id.buttonAction).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    modales.alertDialog.dismiss();
                                    if (lugarProviene.equals("corteFajillas")){
                                        Intent intent = new Intent(getApplicationContext(), ResumenActivity.class);
                                        startActivity(intent);
                                    }else{
                                        Intent intent = new Intent(getApplicationContext(), Menu_Principal.class);
                                        startActivity(intent);
                                    }
                                    finish();
                                }
                            });
                        }else{
                            String estado = respuestaApiResumenFajilla.Mensaje;
                            String titulo = "AVISO ";
                            String mensaje = "" + estado;
                            Modales modales = new Modales(AutorizaFajillas.this);
                            View view1 = modales.MostrarDialogoAlertaAceptar(AutorizaFajillas.this,mensaje,titulo);
                            view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    modales.alertDialog.dismiss();
                                    Intent intent = new Intent(getApplicationContext(), Menu_Principal.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                        }
                    }

                    @Override
                    public void onFailure(Call<RespuestaApi<List<ResumenFajilla>>> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                });
            }
        }
    }



    private void GuardaAutorizacionCorteProducto(String numeroEmpleadoAutoriza){
        JSONObject datos = new JSONObject();
        String url = "http://" + ipEstacion + "/CorpogasService/api/completadoEstadoCierre/guardaAutorizacionProductos/sucursalId/" + sucursalId + "/cierreId/" + cierreId + "/entregaEmpleado/" + numeroempleado + "/autorizaEmpleado/" + numeroEmpleadoAutoriza;
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest request_json = new JsonObjectRequest(Request.Method.POST, url, datos, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                String detalle = null;
                String mensaje;
                try {
                    String respuesta = response.getString("Correcto");
                    if (respuesta.equals("false")) {
                        mensaje = response.getString("Mensaje");
                        Toast.makeText(AutorizaFajillas.this, mensaje, Toast.LENGTH_SHORT).show();
                    }else{
                        UsuarioAceptado(respuesta);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AutorizaFajillas.this, "error", Toast.LENGTH_SHORT).show();

            }
        }){
            public Map<String,String>getHeaders() throws AuthFailureError {
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


    private void UsuarioAceptado(String resultado){

        String titulo ;
        String mensajes ;
        if (resultado.equals("false")) {
            titulo = "Error";
            mensajes = " ";

        }else{
            titulo = "CONFIRMADO POR";
            mensajes = "INVENTARIO DE PRODUCTOS CONFIRMADO POR: \n" + nombrecompleto;
        }

        Modales modales = new Modales(AutorizaFajillas.this);
        View view1 = modales.MostrarDialogoCorrecto(AutorizaFajillas.this, mensajes);
        view1.findViewById(R.id.buttonAction).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modales.alertDialog.dismiss();
                finish();
            }
        });
    }
}