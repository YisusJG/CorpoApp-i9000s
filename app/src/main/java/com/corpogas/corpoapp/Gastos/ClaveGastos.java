package com.corpogas.corpoapp.Gastos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Bundle;
import android.os.Handler;
import android.security.identity.UnknownAuthenticationKeyException;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.corpogas.corpoapp.Conexion;
import com.corpogas.corpoapp.Configuracion.SQLiteBD;
import com.corpogas.corpoapp.EnProcesoDeDesarrollo.EnDesarrollo;
import com.corpogas.corpoapp.Entities.Accesos.AccesoUsuario;
import com.corpogas.corpoapp.Entities.Classes.RespuestaApi;
import com.corpogas.corpoapp.Entities.Estaciones.Empleado;
import com.corpogas.corpoapp.Menu_Principal;
import com.corpogas.corpoapp.Modales.Modales;
import com.corpogas.corpoapp.ObtenerClave.ClaveEmpleado;
import com.corpogas.corpoapp.R;
import com.corpogas.corpoapp.Interfaces.Endpoints.EndPoints;
import com.corpogas.corpoapp.TanqueLleno.PosicionCargaTLl;
import com.corpogas.corpoapp.Token.GlobalToken;
import com.corpogas.corpoapp.VentaCombustible.ProcesoVenta;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ClaveGastos extends AppCompatActivity {
    SQLiteBD db;
    //datos para huella
    private static final String TAG = "FingerprintActivity";
    TextView usuario, carga, txtEtiqueta, titulo, numerodispositivo;
    String iduser, lugarProviene, NIPCliente, NIPmd5Cliente;
    //Variables para huella
//    private String m_deviceName = "";
//    private Reader m_reader = null;
//    private int m_DPI = 0;
//    private Reader.CaptureResult cap_result = null;
//    private byte[] bytesCapture;
//    private byte[] bytesCaptureTraducido;
    String EstacionId, sucursalId, ipEstacion, strStore, empleadoIdentificado, numeroempleado, rolid, nombrecompleto, TipoBiometrico = "2", TipoAutenticacion = "4";
    String  claveEmpleadoIdentificado, idisla, idTurno;
    long idRoll,idusuario;
//    final JSONObject datos = new JSONObject();
//    private Engine m_engine = null;
//    private boolean m_reset = false;
//    boolean ejecutar = true;
    //termina variables para huellas


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
    String banderaHuella;

    LottieAnimationView animationView2;
    RespuestaApi<Empleado> respuestaApiEmpleado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clave_gastos);
        init();

        this.setTitle(db.getRazonSocial());
        this.setTitle(db.getNombreEstacion() + " ( EST.:" + db.getNumeroEstacion() + ")");
        numerodispositivo.setVisibility(View.INVISIBLE);

    }


    private void init() {
        db = new SQLiteBD(this);
        pasword = findViewById(R.id.edtclavedespachador);
        txtEtiqueta = findViewById(R.id.txtEtiqueta);
        titulo = findViewById(R.id.titulo);
        numerodispositivo =  (TextView) findViewById(R.id.txtnumerodispositivo);
        lugarProviene =  getIntent().getStringExtra("LugarProviene");
        empleadoIdentificado="0";
        claveEmpleadoIdentificado = "";
        EstacionId = db.getIdEstacion();
        sucursalId = db.getIdSucursal();
        ipEstacion = db.getIpEstacion();

    }
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
        final String pass = pasword.getText().toString();


        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("http://" + data.getIpEstacion() + "/CorpogasService/")
                .baseUrl("http://" + ipEstacion + "/CorpogasService_entities_token/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        EndPoints datosEmpleado = retrofit.create(EndPoints.class);
        Call<RespuestaApi<Empleado>> call = datosEmpleado.getDatosEmpleado(pass, db.getToken());
        call.enqueue(new Callback<RespuestaApi<Empleado>>() {

            @Override
            public void onResponse(Call<RespuestaApi<Empleado>> call, Response<RespuestaApi<Empleado>> response) {
                if (!response.isSuccessful()) {
                    if (response.code() == 401) {
                        GlobalToken.errorToken(ClaveGastos.this);
                    } else {
                        Toast.makeText(ClaveGastos.this, response.message(), Toast.LENGTH_SHORT).show();
                    }
                    return;
                }
                respuestaApiEmpleado = response.body();
                if(respuestaApiEmpleado.getObjetoRespuesta()==null)
                {
                    String titulo = "AVISO";
                    String mensaje = "Clave inexistente";
                    Modales modales = new Modales(ClaveGastos.this);
                    View view1 = modales.MostrarDialogoAlertaAceptar(ClaveGastos.this,mensaje,titulo);
                    view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            modales.alertDialog.dismiss();
                            pasword.setText("");
                        }
                    });
                    return;

                }
                boolean valido = respuestaApiEmpleado.getObjetoRespuesta().isActivo();
                idusuario = respuestaApiEmpleado.getObjetoRespuesta().getId();//respuestaobjeto.getString("Id");
                nombrecompleto = respuestaApiEmpleado.getObjetoRespuesta().getNombreCompleto();       //respuestaobjeto.getString("NombreCompleto");
                numeroempleado = respuestaApiEmpleado.getObjetoRespuesta().getNumeroEmpleado();
                idRoll = respuestaApiEmpleado.getObjetoRespuesta().getRolId();//respuestaobjeto.getString("RolId");

                if (valido == true) {
//                                numerodispositivo.setVisibility(View.VISIBLE);
                    if (idRoll == 3 || idRoll == 1) { // 1 es Jefe de ISla Autorizado por Gerente
                        //Si es valido se asignan valores
                        obtenerIsla("2"); //proviene
                    } else {
                        String titulo = "Contraseña incorrecta";
                        String mensajes = "Debe ser de Jefe de Isla o Gerente";
                        Modales modales = new Modales(ClaveGastos.this);
                        View view1 = modales.MostrarDialogoAlertaAceptar(ClaveGastos.this, mensajes, titulo);
                        view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                modales.alertDialog.dismiss();
//                                password.setText("");
                            }
                        });

                    }
                } else {
                    //Si no es valido se envia mensaje
                    //vm.mostrarVentana("Usuario No Activo");
                    String titulo = "AVISO";
                    String mensajes = "Usuario No Activo";
                    Modales modales = new Modales(ClaveGastos.this);
                    View view1 = modales.MostrarDialogoAlertaAceptar(ClaveGastos.this,mensajes,titulo);
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
            public void onFailure(Call<RespuestaApi<Empleado>> call, Throwable t) {
//                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                String errorMensaje = t.getMessage();

                String titulo = "AVISO";
                String mensaje = "" + errorMensaje;
                Modales modales = new Modales(ClaveGastos.this);
                View view1 = modales.MostrarDialogoAlertaAceptar(ClaveGastos.this, mensaje, titulo);
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
        });
    }

    public void obtenerIsla(final String proviene) {
        if (!Conexion.compruebaConexion(this)) {
            Toast.makeText(getBaseContext(), "Sin conexión a la red ", Toast.LENGTH_SHORT).show();
            Intent intent1 = new Intent(getApplicationContext(), Menu_Principal.class);
            startActivity(intent1);
            finish();
        } else {
            if (claveEmpleadoIdentificado.length() > 0) {
                pasword.setText(claveEmpleadoIdentificado);
            }
            final String pass = pasword.getText().toString();

            String url = "http://" + ipEstacion + "/CorpogasService/api/estacionControles/Sucursal/" + sucursalId + "/ClaveEmpleado/" + pass;
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new com.android.volley.Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject claveusuario = jsonArray.getJSONObject(i);
                            idisla = claveusuario.getString("IslaId");
                            idTurno = claveusuario.getString("TurnoId");
                        }
                        //usuario.setText(idusuario);
                        //Se instancia y se llama a la clase formas de pago
                        Intent intent = new Intent(getApplicationContext(), AutorizacionGastos.class);
                        intent.putExtra("tipoGasto", proviene);
                        intent.putExtra("isla", idisla);
                        intent.putExtra("turno", idTurno);
                        startActivity(intent);
                        finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    String algo = new String(error.networkResponse.data);
                    try {
                        //creamos un json Object del String algo
                        JSONObject errorCaptado = new JSONObject(algo);
                        //Obtenemos el elemento ExceptionMesage del errro enviado
                        String errorMensaje = errorCaptado.getString("ExceptionMessage");
                        try {
                            String titulo = "Gastos";
                            String mensajes = errorMensaje;
                            Modales modales = new Modales(ClaveGastos.this);
                            View view1 = modales.MostrarDialogoAlertaAceptar(ClaveGastos.this, mensajes, titulo);
                            view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    modales.alertDialog.dismiss();
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
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        }
    }




    private  void enviaActividadSiguiente(String pass, long idRoll){
        switch (lugarProviene){
            case "Reimpresion":
                if (idRoll ==1 || idRoll ==3) { // 1 es Gerente, 3 Jefe de ISla Autorizado por Gerente  (idRoll.equals("3") || idRoll.equals("1"))
                    Intent intent = new Intent(getApplicationContext(), Menu_Principal.class);
                    intent.putExtra("IdUsuario", idusuario);
                    intent.putExtra("ClaveDespachador", pass);
                    intent.putExtra("nombrecompleto", nombrecompleto);
                    intent.putExtra("lugarProviene", lugarProviene);
                    startActivity(intent);
                    finish();
                }else{
                    String titulo = "AVISO";
                    String mensajes = "El usuario debe ser Gerente";
                    Modales modales = new Modales(ClaveGastos.this);
                    View view1 = modales.MostrarDialogoAlertaAceptar(ClaveGastos.this,mensajes,titulo);
                    view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            modales.alertDialog.dismiss();
                            pasword.setText("");
                            if (banderaHuella.equals("true")){
//                                InicializaLector();
                            }
                        }
                    });
                }
                break;
            case "Impresion":
                Intent intent = new Intent(getApplicationContext(), Menu_Principal.class);
                intent.putExtra("IdUsuario", idusuario);
                intent.putExtra("ClaveDespachador", pass);
                intent.putExtra("nombrecompleto", nombrecompleto);
                intent.putExtra("lugarProviene", lugarProviene);
                intent.putExtra("numeromepleado", numeroempleado);
                startActivity(intent);
                finish();
                break;
            case "IniciaVenta":
                //Se instancia y se llama a la clase Venta de Productos
                Intent intent1 = new Intent(getApplicationContext(), ProcesoVenta.class); //posicionFinaliza Mikel 22/04/2021
                //Se envian los parametros de posicion y usuario
                intent1.putExtra("lugarproviene", "IniciaVenta");
                intent1.putExtra("IdUsuario", idusuario);
                intent1.putExtra("clave", pass);
//                intent1.putExtra("numeromepleado", numeroempleado);
                //inicia el activity
                startActivity(intent1);
                finish();
                break;
            case "FinalizaVenta":
                Intent intent2 = new Intent(getApplicationContext(), ProcesoVenta.class);//ProcesoVenta
                //Se envian los parametros de posicion y usuario
                intent2.putExtra("lugarproviene", "FinalizaVenta");
                intent2.putExtra("IdUsuario", idusuario);
                intent2.putExtra("clave", pass);
                //inicia el activity
                startActivity(intent2);
                finish();
                break;
            case "Predeterminado":
                Intent intentp = new Intent(getApplicationContext(), Menu_Principal.class);
                //Se envian los parametros de posicion y usuario
                intentp.putExtra("lugarproviene", "Predeterminado");
                intentp.putExtra("usuario", idusuario);
                intentp.putExtra("clave", pass);
                //inicia el activity
                startActivity(intentp);
                finish();
                break;
            case "Productos":
                //Se instancia y se llama a la clase Venta de Productos
                Intent intentproducto = new Intent(getApplicationContext(), Menu_Principal.class); //formaPago
                //Se envian los parametros de posicion y usuario
                intentproducto.putExtra("IdUsuario", idusuario);
                intentproducto.putExtra("clave",pass);
                intentproducto.putExtra("nombrecompleto", nombrecompleto);
                //inicia el activity
                startActivity(intentproducto);
                finish();
            case "Acumular":
                String track = getIntent().getStringExtra("NumeroTarjeta");
                Intent intent3 = new Intent(getApplicationContext(), Menu_Principal.class);
                intent3.putExtra("IdUsuario", idusuario);
                intent3.putExtra("ClaveDespachador", pass);
                intent3.putExtra("nombrecompleto", nombrecompleto);
                intent3.putExtra("track", track);
                intent3.putExtra("NipCliente", NIPCliente);
                startActivity(intent3);
                finish();
                break;
            case "Redimir":
                String track1 = getIntent().getStringExtra("NumeroTarjeta");
                String nip = getIntent().getStringExtra("Nip");
                Intent intent4 = new Intent(getApplicationContext(), Menu_Principal.class);
                intent4.putExtra("IdUsuario", idusuario);
                intent4.putExtra("ClaveDespachador", pass);
                intent4.putExtra("nombrecompleto", nombrecompleto);
                intent4.putExtra("track", track1);
                intent4.putExtra("nip", nip);
                startActivity(intent4);
                finish();
                break;
            case "Registrar":
                String track2 = getIntent().getStringExtra("NumeroTarjeta");
                String nip2 = getIntent().getStringExtra("Nip");
                Intent intente = new Intent(getApplicationContext(), EnDesarrollo.class);
                intente.putExtra("IdUsuario", idusuario);
                intente.putExtra("ClaveDespachador", pass);
                intente.putExtra("nombrecompleto", nombrecompleto);
                intente.putExtra("track", track2);
                intente.putExtra("nip", nip2);
                startActivity(intente);
                finish();
                break;
            case "Tanque":
                NIPCliente = getIntent().getStringExtra("nipTanqueLleno");
                NIPmd5Cliente = getIntent().getStringExtra("nipTanqueLlenoMd5");
                String track4 = getIntent().getStringExtra("track");

                Intent intent5 = new Intent(getApplicationContext(), PosicionCargaTLl.class);
                intent5.putExtra("IdUsuario", idusuario);
                intent5.putExtra("ClaveDespachador", pass);
                intent5.putExtra("track", track4);
                intent5.putExtra("pass", pass);
                intent5.putExtra("nipCliente", NIPCliente);
                intent5.putExtra("nipMd5Cliente", NIPmd5Cliente);
                startActivity(intent5);
                finish();
                pasword.setText("");
            case "TicketPendiente":
//                usuario.setText(idusuario);
//                TicketPendiente();
                break;
            default:
        }

    }
}