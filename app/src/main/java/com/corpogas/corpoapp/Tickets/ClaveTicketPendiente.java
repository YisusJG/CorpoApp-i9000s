package com.corpogas.corpoapp.Tickets;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.fingerprint.FingerprintManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.service.wallpaper.WallpaperService;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
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
import com.google.zxing.BarcodeFormat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ClaveTicketPendiente extends AppCompatActivity {
    EditText contrasena;
    String EstacionId, sucursalId, ipEstacion, numeroTarjetero ;
    Button btnhuella;
    ImageButton btnTutorial,btnTutorial2,btnTutorial3;
    private byte[] isoFeatureTmp;
    SQLiteBD data;
    List<String> AEmpleadoId;
    List<byte[]> AHuella;
    String empleadoIdentificado;
    private Handler mHandler;
    TextView textResultado, numerodispositivo, titulo,txtTitulo, usuario, tvtarjetero;
    Boolean banderaIdentificado;
    EditText pasword;
    Bundle args = new Bundle();
    //Variables para huella

    private byte[] bytesCapture;
    private byte[] bytesCaptureTraducido;
    String strStore, numeroempleado, rolid, nombrecompleto, TipoBiometrico = "2", TipoAutenticacion = "4";
    final JSONObject datos = new JSONObject();
    private WallpaperService.Engine m_engine = null;
    boolean ejecutar = true;
    //termina variables para huellas
    String banderaHuella;



    private int RESULT_CODE = 0;

    SharedPreferences sp;

    private boolean m_bThreadFinished = true;

    //termina aqui
    String fechaTicket;

    LottieAnimationView animationView2;

    ImageView imgViewIcono;
    SQLiteBD db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clave_ticket_pendiente);

        mHandler = new Handler(Looper.getMainLooper());
        textResultado = findViewById(R.id.textresultado);
//        btnTutorial = (ImageButton)findViewById(R.id.btnTutorial);
//        btnTutorial2 = (ImageButton) findViewById(R.id.btnTutorial2);
//        btnTutorial3 = (ImageButton) findViewById(R.id.btnTutorial3);
        txtTitulo = (TextView)findViewById(R.id.textTitle);
        VideoView videoView = findViewById(R.id.miVideo);
        imgViewIcono =(ImageView) findViewById(R.id.imageIcon);
        //btnhuella.setOnClickListener(claveUPendientes.this);

        //lee valores usuario y carga
        usuario= findViewById(R.id.usuario);
        data = new SQLiteBD(getApplicationContext());

        db = new SQLiteBD(getApplicationContext());
        this.setTitle(db.getRazonSocial());
        this.setTitle(db.getNombreEstacion() + " ( EST.:" + db.getNumeroEstacion() + ")");
        sucursalId=db.getIdSucursal();
        EstacionId = db.getIdEstacion();
        ipEstacion = db.getIpEstacion();
        numeroTarjetero = db.getIdTarjtero();
        banderaHuella = db.getLectorHuella(); //getIntent().getStringExtra( "banderaHuella");
        pasword= (EditText) findViewById(R.id.pasword);
        tvtarjetero =  (TextView) findViewById(R.id.tvtarjetero);
        tvtarjetero.setText("Número de Dispositivo: " + numeroTarjetero);
        animationView2 = findViewById(R.id.animationView4);

        titulo = findViewById(R.id.tituloPendiente);

        //Inicializacion y carga de huella
            titulo.setText("Ingresa tu Contraseña de despachador");
//            animationView2.setAnimation("tickets.json");
            pasword.setVisibility(View.VISIBLE);


        if (!Conexion.compruebaConexion(this)) {
//            Toast.makeText(getBaseContext(),"Sin conexión a la red ", Toast.LENGTH_SHORT).show();
            Intent intent1 = new Intent(getApplicationContext(), Menu_Principal.class);
            startActivity(intent1);
            finish();
        }


    }


    private void  validaClave(){
        if (!Conexion.compruebaConexion(this)) {
            Toast.makeText(getBaseContext(), "Sin conexión a la red ", Toast.LENGTH_SHORT).show();
            Intent intent1 = new Intent(getApplicationContext(), Menu_Principal.class);
            startActivity(intent1);
            finish();
        } else {
            final String pass = pasword.getText().toString();
            //----------------------Aqui va el Volley Si se tecleo contraseña----------------------------

            //Conexion con la base y ejecuta valida clave
//            String url = "http://"+ipEstacion+"/CorpogasService/api/Empleados/clave/"+pass;
            String url = "http://"+ipEstacion+"/CorpogasService_entities_token/api/Empleados/clave/"+pass;
            // Utilizamos el metodo Post para validar la contraseña
            StringRequest eventoReq = new StringRequest(Request.Method.GET,url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                if (response.equals("null")){
                                    Toast.makeText(getApplicationContext(),"Clave inexistente ",Toast.LENGTH_SHORT).show();
                                }else {
                                    //Se instancia la respuesta del json
                                    JSONObject validar = new JSONObject(response);
                                    String correcto = validar.getString("Correcto");
                                    String mensaje = validar.getString("Mensaje");
                                    String objetorespuesta = validar.getString("ObjetoRespuesta");
                                    JSONObject respuestaobjeto = new JSONObject(objetorespuesta);
                                    String valido = respuestaobjeto.getString("Activo");
                                    nombrecompleto = respuestaobjeto.getString("NombreCompleto");
                                    String idusuario = respuestaobjeto.getString("Id");
                                    numeroempleado = respuestaobjeto.getString("NumeroEmpleado");
                                    if (valido == "true") {
                                        //Si es valido se asignan valores
                                        usuario.setText(numeroempleado);
                                        TicketPendiente();
                                    } else {
                                        //Si no es valido se envia mensaje
                                        try {
                                            String titulo = "AVISO";
                                            String mensajes = "Usuario No Validado" ;
                                            Modales modales = new Modales(ClaveTicketPendiente.this);
                                            View view1 = modales.MostrarDialogoAlertaAceptar(ClaveTicketPendiente.this,mensajes,titulo);
                                            view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    modales.alertDialog.dismiss();
                                                    pasword.setText("");                                                        }
                                            });
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            } catch (JSONException e) {
                                //herramienta  para diagnostico de excepciones
                                Toast.makeText(getApplicationContext(),"Clave inexistente ",Toast.LENGTH_SHORT).show();
                            }
                        }
                        //funcion para capturar errores
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    GlobalToken.errorToken(ClaveTicketPendiente.this);
//                    //Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();
//                    //VolleyLog.e("Error: ", volleyError.getMessage());
//                    String algo = new String(error.networkResponse.data) ;
//                    try {
//                        //creamos un json Object del String algo
//                        JSONObject errorCaptado = new JSONObject(algo);
//                        //Obtenemos el elemento ExceptionMesage del errro enviado
//                        String errorMensaje = errorCaptado.getString("ExceptionMessage");
//                        try {
//                            String titulo = "AVISO";
//                            String mensajes = "" + errorMensaje ;
//                            Modales modales = new Modales(ClaveTicketPendiente.this);
//                            View view1 = modales.MostrarDialogoAlertaAceptar(ClaveTicketPendiente.this,mensajes,titulo);
//                            view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View view) {
//                                    modales.alertDialog.dismiss();
//                                    Intent intente = new Intent(getApplicationContext(), Menu_Principal.class);
//                                    startActivity(intente);
//                                    finish();
//                                }
//                            });
//                        }catch (Exception e){
//                            e.printStackTrace();
//                        }
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<>();
                    headers.put("Authorization", db.getToken());
                    return headers;
                }
            };

            // Añade la peticion a la cola
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            eventoReq.setRetryPolicy(new DefaultRetryPolicy(12000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(eventoReq);
            //-------------------------Aqui termina el volley --------------
        }
    }

    private void TicketPendiente(){
        if (!Conexion.compruebaConexion(this)) {
            Toast.makeText(getBaseContext(), "Sin conexión a la red ", Toast.LENGTH_SHORT).show();
            Intent intent1 = new Intent(getApplicationContext(), Menu_Principal.class);
            startActivity(intent1);
            finish();
        } else {
            EditText pasword = (EditText) findViewById(R.id.pasword);
            //final String numeroTarjetero = pasword.getText().toString();

            final String numerorecibo = "";
            final String numerotransaccion = "";
            final String numerorastreo="";
            final String posicion="";
            final String despachador="";
            final String vendedor="";
            final String subtotal="";
            final String iva="";
            final String totaltotalTexto="";

            //Conexion con la base y ejecuta consulta para saber si tiene tickets Pendientes
//            String url = "http://"+ipEstacion+"/CorpogasService/api/tickets/pendiente/sucursalId/"+sucursalId+"/numeroDispositivo/"+numeroTarjetero+"/usuarioId/"+data.getNumeroEmpleado();
            String url = "http://"+ipEstacion+"/CorpogasService_entities_token/api/tickets/pendiente/sucursalId/"+sucursalId+"/numeroDispositivo/"+numeroTarjetero+"/usuarioId/"+data.getNumeroEmpleado();

            // Utilizamos el metodo Post para validar la contraseña
            StringRequest eventoReq = new StringRequest(Request.Method.POST,url,  //POST
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                if (response.equals("null")){
                                    try {
                                        String titulo = "AVISO";
                                        String mensajes = "El tarjetero No tiene ningún ticKet pendiente";
                                        Modales modales = new Modales(ClaveTicketPendiente.this);
                                        View view1 = modales.MostrarDialogoAlertaAceptar(ClaveTicketPendiente.this,mensajes,titulo);
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
                                }else {
                                    //Se instancia la respuesta del json
                                    JSONObject validar = new JSONObject(response);
                                    //Se asigna el resultado a String
                                    String valido = validar.getString("Resultado");
                                    if (valido=="null") { //==null
                                        String detalle = validar.getString("Detalle");
                                        //Si el detalle es null es que ya se imprimiio
                                        //validar detalle con un if
                                        if (detalle=="null") { //.equals("null")
                                            //JSONObject mensaj = new JSONObject(valido);
                                            //String mensajes = mensaj.getString("Descripcion");
                                            //Toast.makeText(getApplicationContext(), mensajes, Toast.LENGTH_SHORT).show();
                                            try {
                                                String titulo = "AVISO";
                                                String mensajes = "Sin datos para imprimir";
                                                Modales modales = new Modales(ClaveTicketPendiente.this);
                                                View view1 = modales.MostrarDialogoAlertaAceptar(ClaveTicketPendiente.this,mensajes,titulo);
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

                                        } else {
                                            JSONObject detalleRespuesta = new JSONObject(detalle);
                                            String titulo = "AVISO";
                                            Modales modales = new Modales(ClaveTicketPendiente.this);
                                            View view1 = modales.MostrarDialogoCorrecto(ClaveTicketPendiente.this, "Ticket Impreso en Impresora Central");
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
                                    } else {
                                        JSONObject algo = new JSONObject(valido);
                                        String desc = algo.getString("Descripcion");
                                        String errorenviado = algo.getString("Error");
                                        Toast.makeText(getApplicationContext(), errorenviado.toString(), Toast.LENGTH_SHORT).show();
                                        try {
                                            String titulo = "AVISO";
                                            String mensajes = "" + desc;
                                            Modales modales = new Modales(ClaveTicketPendiente.this);
                                            View view1 = modales.MostrarDialogoAlertaAceptar(ClaveTicketPendiente.this,mensajes,titulo);
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
                            } catch (JSONException e) {
                                //herramienta  para diagnostico de excepciones
                                e.printStackTrace();
                            }
                        }
                        //funcion para capturar errores
                    }, new Response.ErrorListener() {
                String PruebaError;
                @Override
                public void onErrorResponse(VolleyError error) {
                    GlobalToken.errorToken(ClaveTicketPendiente.this);
                    //asiganmos a una variable el error para desplegar la descripcion de Tickets no asignados a la terminal
//                    String algo = new String(error.networkResponse.data) ;
//                    try {
//                        //creamos un json Object del String algo
//                        JSONObject errorCaptado = new JSONObject(algo);
//                        //Obtenemos el elemento ExceptionMesage del errro enviado
//                        String errorMensaje = errorCaptado.getString("ExceptionMessage");
//                        try {
//                            String titulo = "AVISO";
//                            String mensaje = " "+ errorMensaje;
//                            Modales modales = new Modales(ClaveTicketPendiente.this);
//                            View view1 = modales.MostrarDialogoAlertaAceptar(ClaveTicketPendiente.this,mensaje,titulo);
//                            view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View view) {
//                                    modales.alertDialog.dismiss();
//                                    Intent intente = new Intent(getApplicationContext(), Menu_Principal.class);
//                                    startActivity(intente);
//                                    finish();
//                                }
//                            });
//                        }catch (Exception e){
//                            e.printStackTrace();
//                        }
//                        //MostrarDialogoSimple(errorMensaje);
//                        //Toast.makeText(getApplicationContext(),errorMensaje,Toast.LENGTH_SHORT).show();
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<>();
                    headers.put("Authorization", db.getToken());
                    return headers;
                }
            };
            // Añade la peticion a la cola
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            eventoReq.setRetryPolicy(new DefaultRetryPolicy(12000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(eventoReq);
            //-------------------------Aqui termina el volley --------------
        }
    }


    //Metodo para regresar a la actividad principal
    @Override
    public void onBackPressed() {
            //Se instancia y se llama a la clase Venta de Productos
            Intent intent1 = new Intent(getApplicationContext(), Menu_Principal.class);
            //inicia el activity
            startActivity(intent1);
            finish();
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



    private void Salir() {
        //Se instancia y se llama a la clase Venta de Productos
        Intent intent1 = new Intent(getApplicationContext(), Menu_Principal.class);
        //Se envian los parametros de posicion y usuario
        //inicia el activity
        startActivity(intent1);
        finish();
        try {
        } catch (Exception e) {

        }
    }



    private void enviarPrincipal() {
        Intent i = new Intent(getApplicationContext(), Menu_Principal.class);
        startActivity(i);
        finishAffinity();
    }



}