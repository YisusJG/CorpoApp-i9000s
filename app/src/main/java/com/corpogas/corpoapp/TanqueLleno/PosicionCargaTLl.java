package com.corpogas.corpoapp.TanqueLleno;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.corpogas.corpoapp.Adapters.RVAdapter;
import com.corpogas.corpoapp.Conexion;
import com.corpogas.corpoapp.Configuracion.SQLiteBD;
import com.corpogas.corpoapp.Entities.Accesos.AccesoUsuario;
import com.corpogas.corpoapp.Entities.Accesos.Control;
import com.corpogas.corpoapp.Entities.Accesos.Posicion;
import com.corpogas.corpoapp.Entities.Classes.RecyclerViewHeaders;
import com.corpogas.corpoapp.Entities.Classes.RespuestaApi;
import com.corpogas.corpoapp.Entities.Estaciones.PosicionCarga;
import com.corpogas.corpoapp.Entities.TanqueLleno.RespuestaIniAuto;
import com.corpogas.corpoapp.Entities.Tarjetas.RespuestaTanqueLleno;
import com.corpogas.corpoapp.Fajillas.EntregaFajillas;
import com.corpogas.corpoapp.Menu_Principal;
import com.corpogas.corpoapp.Modales.Modales;
import com.corpogas.corpoapp.PruebasEndPoint;
import com.corpogas.corpoapp.Puntada.PosicionPuntadaRedimir;
import com.corpogas.corpoapp.R;
import com.corpogas.corpoapp.Interfaces.Endpoints.EndPoints;
import com.corpogas.corpoapp.TanqueLleno.PlanchadoTarjeta.PlanchadoTanqueLleno;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.corpogas.corpoapp.Token.GlobalToken;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PosicionCargaTLl extends AppCompatActivity {
    RecyclerView rcvPosicionCarga;
    String EstacionId;
    Long sucursalId;
    String ipEstacion;
    String lugarproviene;
    String usuario, numeroempleado;
    String numerotarjeta;
    String NipCliente;
    String NipClientemd5;
    long posicionCargaId;
    long numeroOperativa;
    long cargaNumeroInterno;
    long usuarioid;
    long empleadoNumero;
    Boolean banderaposicionCarga;
    SQLiteBD data;
    RespuestaApi<AccesoUsuario> accesoUsuario;
    List<RecyclerViewHeaders> lrcvPosicionCarga;
    Button btnCargarTodasPCTll;
    FloatingActionButton cargarPosicionesButton;


    ProgressDialog bar;
    RespuestaTanqueLleno respuestaTanqueLleno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posicion_carga_t_ll);
        init();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvPosicionCarga.setLayoutManager(linearLayoutManager);
        rcvPosicionCarga.setHasFixedSize(true);
        posicionCargaFinaliza(1);

        btnCargarTodasPCTll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                posicionCargaFinaliza(2);
            }
        });

        cargarPosicionesButton = findViewById(R.id.cargaTodasPosicionesTl);
        cargarPosicionesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                posicionCargaFinaliza(2);
            }
        });


    }


    private void init() {
        rcvPosicionCarga = (RecyclerView)findViewById(R.id.rcvPosicionCarga);
        data = new SQLiteBD(getApplicationContext());
        this.setTitle(data.getNombreEstacion() + " ( EST.:" + data.getNumeroEstacion() + ")");
        EstacionId = data.getIdEstacion();
        sucursalId = Long.parseLong(data.getIdSucursal());
        ipEstacion = data.getIpEstacion();
        lugarproviene = getIntent().getStringExtra("lugarproviene");
        usuario = data.getClave();
        numerotarjeta = getIntent().getStringExtra("track"); //"6ABE322B"; //
        NipCliente = getIntent().getStringExtra("nipCliente");
        NipClientemd5 = getIntent().getStringExtra("nipMd5Cliente");
        numeroempleado = data.getNumeroEmpleado();
        btnCargarTodasPCTll = (Button) findViewById(R.id.btnCargarTodasPCTll);

    }

    public void posicionCargaFinaliza(Integer Identificador){
        bar = new ProgressDialog(PosicionCargaTLl.this);
        bar.setTitle("Buscando Posiciones de Carga");
        bar.setMessage("Ejecutando... ");
        bar.setIcon(R.drawable.gas);
        bar.setCancelable(false);
        bar.show();

        String url;
        if (Identificador.equals(1)){
//            url = "http://" + ipEstacion + "/CorpogasService/api/posicionCargas/GetPosicionCargaEmpleadoId/sucursal/" + sucursalId + "/empleado/" + numeroempleado;
            url = "http://" + ipEstacion + "/CorpogasService_entities_token/api/posicionCargas/GetPosicionCargaEmpleadoId/sucursal/" + sucursalId + "/empleado/" + numeroempleado;

        }else{
//            url = "http://" + ipEstacion + "/CorpogasService/api/posicionCargas/GetPosicionCargasEstacion/sucursal/" + sucursalId;
            url = "http://" + ipEstacion + "/CorpogasService_entities_token/api/posicionCargas/GetPosicionCargasEstacion/sucursal/" + sucursalId;

        }
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    String posicionCarga,  disponible, estado , pendientdecobro, descripcionOperativa , numeroPosicionCarga, descripcion;
                    JSONObject jsonObject = new JSONObject(response);
                    String correcto = jsonObject.getString("Correcto");
                    String mensaje = jsonObject.getString("Mensaje");
                    String ObjetoRespuesta = jsonObject.getString("ObjetoRespuesta");

//                    JSONObject jsonObject1 = new JSONObject(ObjetoRespuesta);

                    if (correcto.equals("false")){
                        String titulo = "AVISO";
                        Modales modales = new Modales(PosicionCargaTLl.this);
                        View view1 = modales.MostrarDialogoAlertaAceptar(PosicionCargaTLl.this,mensaje,titulo);
                        view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
//                                Intent intent1 = new Intent(getApplicationContext(), Menu_Principal.class);
//                                startActivity(intent1);
//                                finish();
                                bar.cancel();
                                modales.alertDialog.dismiss();
                            }
                        });
                    }else{
                        lrcvPosicionCarga = new ArrayList<>();
                        banderaposicionCarga= false;

                        JSONArray control1 = new JSONArray(ObjetoRespuesta);
                        for (int i = 0; i < control1.length(); i++) {
                            JSONObject posiciones = control1.getJSONObject(i);
                            posicionCargaId = posiciones.getLong("Posicioncarga");
                            long posicionCargaNumeroInterno = posiciones.getLong("NumeroPosicionCarga");

                            boolean pocioncargadisponible = posiciones.getBoolean("Disponible");
                            estado = posiciones.getString("Estado");
                            boolean pocioncargapendientecobro = posiciones.getBoolean("PendienteCobro");
                            String descripcionoperativa = posiciones.getString("DescripcionOperativa");
                            descripcion = posiciones.getString("Descripcion");
                            numeroOperativa = posiciones.getLong("Operativa");
                            Boolean banderacarga ;
                            if (pocioncargapendientecobro == true){
                                banderacarga = false;
                            }else{
                                banderacarga = true;
                            }
                            if (banderacarga.equals(true)) {
                                if (numeroOperativa == 5){
                                }else {
                                    if (numeroOperativa == 6){

                                    }else{
                                        if (numeroOperativa == 20){

                                        }else{
                                            if (numeroOperativa == 21){

                                            }else{
                                                String titulo = "PC " + posicionCargaNumeroInterno;
                                                String subtitulo = "";//
                                                //    subtitulo = "Magna  |  Premium  |  Diesel";
                                                subtitulo =descripcionoperativa;//
                                                lrcvPosicionCarga.add(new RecyclerViewHeaders(titulo,subtitulo,R.drawable.gas,posicionCargaId,posicionCargaNumeroInterno, "false", "false"));//
                                                banderaposicionCarga = true;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        bar.cancel();
                        //AQUI VA EL ADAPTADOR
                        if (banderaposicionCarga.equals(false)){
//                        //Toast.makeText(posicionFinaliza.this, "No hay Posiciones de Carga para Finalizar Venta", Toast.LENGTH_SHORT).show();
                            String titulo = "AVISO";
                            String mensajes="";
                            mensajes = "No hay posiciones de carga disponiles";
                            Modales modales = new Modales(PosicionCargaTLl.this);
                            View view1 = modales.MostrarDialogoAlertaAceptar(PosicionCargaTLl.this,mensajes,titulo);
                            view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    modales.alertDialog.dismiss();
                                    Intent intent1 = new Intent(getApplicationContext(), Menu_Principal.class);
                                    startActivity(intent1);
                                    finish();
                                }
                            });
                        }else {
                            initializeAdapter();
                            bar.cancel();
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse.statusCode == 401) {
                    GlobalToken.errorToken(PosicionCargaTLl.this);
                } else {
                    Toast.makeText(PosicionCargaTLl.this, error.toString(), Toast.LENGTH_SHORT).show();
                }
//                String algo = new String(error.networkResponse.data);
//                try {
//                    //creamos un json Object del String algo
//                    JSONObject errorCaptado = new JSONObject(algo);
//                    //Obtenemos el elemento ExceptionMesage del errro enviado
//                    String errorMensaje = errorCaptado.getString("ExceptionMessage");
//                    try {
//                        String titulo = "Jarreo";
//                        String mensajes = errorMensaje;
//                        Modales modales = new Modales(PosicionCargaTLl.this);
//                        View view1 = modales.MostrarDialogoAlertaAceptar(PosicionCargaTLl.this, mensajes, titulo);
//                        view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                modales.alertDialog.dismiss();
//                            }
//                        });
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", data.getToken());
                return headers;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    private void initializeAdapter() {
        RVAdapter adapter = new RVAdapter(lrcvPosicionCarga);
        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                posicionCargaId=lrcvPosicionCarga.get(rcvPosicionCarga.getChildAdapterPosition(v)).getPosicionCargaId();
                cargaNumeroInterno = lrcvPosicionCarga.get(rcvPosicionCarga.getChildAdapterPosition(v)).getPosicioncarganumerointerno();
                    //ValidaTransaccionActiva();
                if (lugarproviene.equals("Planchado")){
                    Intent intent1 = new Intent(getApplicationContext(), PlanchadoTanqueLleno.class); //PlanchadoTanqueLleno
                    intent1.putExtra("cargaPosicion", posicionCargaId);
                    startActivity(intent1);
                    finish();
                }else{
                    if (lugarproviene.equals("Arillos")){
                        enviaPeticionArillo();
                    }else{
                        enviardatos();
                    }
                }

            }
        });

        rcvPosicionCarga.setAdapter(adapter);
    }

    private void enviaPeticionArillo(){
//        String url = "http://" + ipEstacion + "/CorpogasService/Api/tanqueLleno/Arillo/posicionCargaId/" + posicionCargaId + "/numeroEmpleado/"+numeroempleado;
        String url = "http://" + ipEstacion + "/CorpogasService_entities_token/Api/tanqueLleno/Arillo/posicionCargaId/" + posicionCargaId + "/numeroEmpleado/"+numeroempleado;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject respuesta = new JSONObject(response);
                    String correcto = respuesta.getString("Correcto");
                    String mensaje = respuesta.getString("Mensaje");

                    if (correcto.equals("true") ) {
                        String objetoRespuesta = respuesta.getString("ObjetoRespuesta");
                        String titulo = "TanqueLleno";
                        Modales modales = new Modales(PosicionCargaTLl.this);
                        View view1 = modales.MostrarDialogoCorrecto(PosicionCargaTLl.this, mensaje);
                        view1.findViewById(R.id.buttonAction).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                modales.alertDialog.dismiss();
                                Intent intent = new Intent(getApplicationContext(), Menu_Principal.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                    }else{
                        String titulo = "TanqueLleno";
                        Modales modales = new Modales(PosicionCargaTLl.this);
                        View view1 = modales.MostrarDialogoAlertaAceptar(PosicionCargaTLl.this, mensaje, titulo);
                        view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                modales.alertDialog.dismiss();
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                if (error.networkResponse.statusCode == 401) {
                    GlobalToken.errorToken(PosicionCargaTLl.this);
                } else {
                    Toast.makeText(PosicionCargaTLl.this, error.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", data.getToken());
                return headers;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this.getApplicationContext());
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);


    }


    private void enviardatos2(){
        //usuario, posicionCargaId,numerotarjeta
//        if (!Conexion.compruebaConexion(this)) {
//            Toast.makeText(getBaseContext(), "Sin conexión a la red ", Toast.LENGTH_SHORT).show();
//            Intent intent1 = new Intent(getApplicationContext(), Menu_Principal.class);
//
//            startActivity(intent1);
//            finish();
//        } else {
//

        RespuestaIniAuto respuestaIniAuto1 = new RespuestaIniAuto(); //= new RespuestaIniAuto(Long.parseLong(data.getIdSucursal()), posicionCargaId , numerotarjeta);
        respuestaIniAuto1.SucursalId = Long.parseLong(data.getIdSucursal());
        respuestaIniAuto1.PosicionCarga = posicionCargaId;
        respuestaIniAuto1.TarjetaCliente = numerotarjeta;

        String nuevo = new Gson().toJson(respuestaIniAuto1);


        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("http://" + data.getIpEstacion() + "/CorpogasService/")
                .baseUrl("http://" + ipEstacion + "/CorpogasService_entities_token/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        EndPoints obtenerIniAuto = retrofit.create(EndPoints.class);
        Call<RespuestaTanqueLleno> call = obtenerIniAuto.getInicializaAuto(usuario, respuestaIniAuto1, data.getToken());
        call.timeout().timeout(120, TimeUnit.SECONDS);
        call.enqueue(new Callback<RespuestaTanqueLleno>() {
            @Override
            public void onResponse(Call<RespuestaTanqueLleno> call, Response<RespuestaTanqueLleno> response) {
                if (!response.isSuccessful()) {
                    if (response.code() == 401) {
                        GlobalToken.errorToken(PosicionCargaTLl.this);
                    } else {
                        Toast.makeText(PosicionCargaTLl.this, response.message(), Toast.LENGTH_SHORT).show();
                    }
                    return;
                }
                respuestaTanqueLleno = response.body();

                boolean correcto = respuestaTanqueLleno.Correcto;
                Double creditodispoble = respuestaTanqueLleno.CreditoDisponible;
                String numerointernosucursal =  respuestaTanqueLleno.NumeroInternoSucursal;
                boolean odometro = respuestaTanqueLleno.PideOdometro;
                boolean placa = respuestaTanqueLleno.PidePlaca;
                List combustibles = respuestaTanqueLleno.CombustiblesAutorizados;
                final Integer tipocliente = respuestaTanqueLleno.TipoCliente;
                final String sucursalempleados = respuestaTanqueLleno.NumeroEmpleado;

//                combustibles = combustibles.replace("[", "");
//                combustibles = combustibles.replace("]", "");
                final String clave = respuestaTanqueLleno.Clave;
                final Long transaccionId = respuestaTanqueLleno.TransaccionId;
                final Long folio = respuestaTanqueLleno.Folio;
                final String SolicitudEnLitros =  respuestaTanqueLleno.SolicitudEnLitros;
                if (correcto == true) {
                    if (combustibles.size() == 0) {
                        String titulo = "AVISO";
                        String mensajes = "LA TARJETA NO CUENTA CON COMBUSTIBLES ASIGNADOS ";
                        Modales modales = new Modales(PosicionCargaTLl.this);
                        View view1 = modales.MostrarDialogoAlertaAceptar(PosicionCargaTLl.this, mensajes, titulo);
                        view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                modales.alertDialog.dismiss();
                                Intent intent = new Intent(getApplicationContext(), Menu_Principal.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                    } else {
                        if (odometro == true) {
                            try {
                                String titulo = "TANQUE LLENO";
                                String mensaje = "Ingresa el Odometro \n";
                                Modales modales = new Modales(PosicionCargaTLl.this);
                                View viewLectura = modales.MostrarDialogoInsertaDato(PosicionCargaTLl.this, mensaje, titulo);
                                EditText edtProductoCantidad = ((EditText) viewLectura.findViewById(R.id.textInsertarDato));
                                edtProductoCantidad.setInputType(InputType.TYPE_CLASS_NUMBER);
                                //String finalCombustibles = finalCombustibles1;
                                viewLectura.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        modales.alertDialog.dismiss();
                                        String cantidad = edtProductoCantidad.getText().toString();
                                        if (cantidad.isEmpty()) {
                                            edtProductoCantidad.setError("Ingresa el Odometro");
                                        } else {
                                            String odometro = cantidad;
                                            //IngresarPlacas(placa, odometro, numerointernosucursal, sucursalempleados, posicionCargaId, numerotarjeta, clave, tipocliente, transaccionId, folio, SolicitudEnLitros, combustibles);
                                        }
                                    }
                                });
                                viewLectura.findViewById(R.id.buttonNo).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        modales.alertDialog.dismiss();
                                    }
                                });
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            if (placa == true) {
                                try {
                                    String titulo = "TANQUE LLENO";
                                    String mensaje = "Ingresa las Placas \n";
                                    Modales modales = new Modales(PosicionCargaTLl.this);
                                    View viewLectura = modales.MostrarDialogoInsertaDato(PosicionCargaTLl.this, mensaje, titulo);
                                    EditText edtProductoCantidad = ((EditText) viewLectura.findViewById(R.id.textInsertarDato));
                                    edtProductoCantidad.setInputType(InputType.TYPE_CLASS_TEXT);
//                                    String finalCombustibles = combustibles;
                                    viewLectura.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            String cantidad = edtProductoCantidad.getText().toString();
                                            if (cantidad.isEmpty()) {
                                                edtProductoCantidad.setError("Ingresa las Placas");
                                            } else {
                                                String placas = cantidad;
                                                Intent intent = new Intent(getApplicationContext(), eligeLitrosPrecioTL.class); //ProductoTLl
                                                intent.putExtra("placas", placas);
                                                intent.putExtra("odometro", odometro);
                                                intent.putExtra("NumeroInternoEstacion", numerointernosucursal);
                                                intent.putExtra("SucursalEmpleadoId", sucursalempleados);
                                                intent.putExtra("PosicioDeCarga", posicionCargaId);
                                                intent.putExtra("NumeroDeTarjeta", numerotarjeta);
                                                intent.putExtra("ClaveTanqueLleno", clave);
                                                intent.putExtra("Tipocliente", tipocliente);
                                                intent.putExtra("transaccionid", transaccionId);
                                                intent.putExtra("folio", folio);
                                                intent.putExtra("nipCliente", NipCliente);
                                                intent.putExtra("nipMd5Cliente", NipClientemd5);
                                                intent.putExtra("Litros", SolicitudEnLitros);
                                                intent.putExtra("CombustiblesAsociados", String.valueOf(combustibles));
                                                startActivity(intent);
                                                finish();
                                            }
                                        }
                                    });
                                    viewLectura.findViewById(R.id.buttonNo).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            modales.alertDialog.dismiss();
                                        }
                                    });

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                Intent intent = new Intent(getApplicationContext(), eligeLitrosPrecioTL.class); //ProductoTLl
                                intent.putExtra("placas", "");
                                intent.putExtra("odometro", "");
                                intent.putExtra("NumeroInternoEstacion", numerointernosucursal);
                                intent.putExtra("SucursalEmpleadoId", sucursalempleados);
                                intent.putExtra("PosicioDeCarga", posicionCargaId);
                                intent.putExtra("NumeroDeTarjeta", numerotarjeta);
                                intent.putExtra("ClaveTanqueLleno", clave);
                                intent.putExtra("Tipocliente", tipocliente);
                                intent.putExtra("transaccionid", transaccionId);
                                intent.putExtra("folio", folio);
                                intent.putExtra("nipCliente", NipCliente);
                                intent.putExtra("nipMd5Cliente", NipClientemd5);
                                intent.putExtra("Litros", SolicitudEnLitros);
                                intent.putExtra("CombustiblesAsociados", String.valueOf(combustibles));

                                startActivity(intent);
                                finish();
                            }
                        }
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "La contraseña ingresada no es correcta", Toast.LENGTH_LONG).show();
                }
//                Toast.makeText(PosicionCargaTLl.this, "Odometro: " +odometro  + " Placas: "+placa, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(Call<RespuestaTanqueLleno> call, Throwable t) {
                Toast.makeText(PosicionCargaTLl.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });
     }

    private void enviardatos() {
        if (!Conexion.compruebaConexion(this)) {
            Toast.makeText(getBaseContext(), "Sin conexión a la red ", Toast.LENGTH_SHORT).show();
            Intent intent1 = new Intent(getApplicationContext(), Menu_Principal.class);

            startActivity(intent1);
            finish();
        } else {
            //----------------------Aqui va el Volley Si se tecleo contraseña----------------------------
            //Conexion con la base y ejecuta valida clave
            final SQLiteBD data = new SQLiteBD(getApplicationContext());
            ProgressDialog bar = new ProgressDialog(PosicionCargaTLl.this);
            bar.setTitle("Tanque Lleno");
            bar.setMessage("Esperando Respuesta, Iniciando Autorización");
            bar.setIcon(R.drawable.tanquelleno);
            bar.setCancelable(false);
            bar.show();

//            String url = "http://"+ipEstacion+"/CorpogasService/api/tanqueLleno/InicioAutorizacion/clave/" + usuario;
            String url = "http://"+ipEstacion+"/CorpogasService_entities_token/api/tanqueLleno/InicioAutorizacion/clave/" + usuario;

            // Utilizamos el metodo Post para validar la contraseña
            StringRequest eventoReq = new StringRequest(Request.Method.POST,url,
                    new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject datos = new JSONObject(response);
                                String correcto = datos.getString("Correcto");
                                if (correcto.equals("false")){
                                    String mensaje = datos.getString("Mensaje");

                                    String titulo = "AVISO";
                                    String mensajes = "" + mensaje;
                                    Modales modales = new Modales(PosicionCargaTLl.this);
                                    View view1 = modales.MostrarDialogoAlertaAceptar(PosicionCargaTLl.this, mensajes, titulo);
                                    view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            modales.alertDialog.dismiss();
                                            Intent intent = new Intent(getApplicationContext(), Menu_Principal.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    });

                                } else {
                                    String creditodispoble = datos.getString("CreditoDisponible");
                                    final String numerointernosucursal = datos.getString("NumeroInternoSucursal");
                                    final String odometro = datos.getString("PideOdometro");

                                    final boolean placa = datos.getBoolean("PidePlaca");
                                    final String sucursalempleados = datos.getString("NumeroEmpleado");
                                    final Integer tipocliente = datos.getInt("TipoCliente");
                                    String combustibles = datos.getString("CombustiblesAutorizados");
                                    combustibles = combustibles.replace("[", "");
                                    combustibles = combustibles.replace("]", "");
                                    final String clave = datos.getString("Clave");
                                    final Long transaccionId = datos.getLong("TransaccionId");
                                    final Long folio = datos.getLong("Folio");
                                    final String SolicitudEnLitros = datos.getString("SolicitudEnLitros");

                                    if (correcto == "true") {
                                        if (combustibles.length() == 0) {
                                            String titulo = "AVISO";
                                            String mensajes = "LA TARJETA NO CUENTA CON COMBUSTIBLES ASIGNADOS ";
                                            Modales modales = new Modales(PosicionCargaTLl.this);
                                            View view1 = modales.MostrarDialogoAlertaAceptar(PosicionCargaTLl.this, mensajes, titulo);
                                            view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    modales.alertDialog.dismiss();
                                                    Intent intent = new Intent(getApplicationContext(), Menu_Principal.class);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            });
                                        } else {
                                            if (odometro.equals("true")) {
                                                try {
                                                    String titulo = "TANQUE LLENO";
                                                    String mensaje = "Ingresa el Odometro \n";
                                                    Modales modales = new Modales(PosicionCargaTLl.this);
                                                    View viewLectura = modales.MostrarDialogoInsertaDato(PosicionCargaTLl.this, mensaje, titulo);
                                                    EditText edtProductoCantidad = ((EditText) viewLectura.findViewById(R.id.textInsertarDato));
                                                    edtProductoCantidad.setInputType(InputType.TYPE_CLASS_NUMBER);
                                                    String finalCombustibles1 = combustibles;
                                                    viewLectura.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View view) {
                                                            String cantidad = edtProductoCantidad.getText().toString();
                                                            modales.alertDialog.dismiss();
                                                            if (cantidad.isEmpty()) {
                                                                edtProductoCantidad.setError("Ingresa el Odometro");
                                                            } else {
                                                                String odometro = cantidad;
                                                                IngresarPlacas(placa, odometro, numerointernosucursal, sucursalempleados, posicionCargaId, numerotarjeta, clave, tipocliente, transaccionId, folio, SolicitudEnLitros, finalCombustibles1);
                                                            }
                                                        }
                                                    });
                                                    viewLectura.findViewById(R.id.buttonNo).setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View view) {
                                                            modales.alertDialog.dismiss();
                                                        }
                                                    });
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            } else {
                                                if (placa == true) {
                                                    try {
                                                        String titulo = "TANQUE LLENO";
                                                        String mensaje = "Ingresa las Placas \n";
                                                        Modales modales = new Modales(PosicionCargaTLl.this);
                                                        View viewLectura = modales.MostrarDialogoInsertaDato(PosicionCargaTLl.this, mensaje, titulo);
                                                        EditText edtProductoCantidad = ((EditText) viewLectura.findViewById(R.id.textInsertarDato));
                                                        edtProductoCantidad.setInputType(InputType.TYPE_CLASS_TEXT);
                                                        String finalCombustibles = combustibles;
                                                        viewLectura.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View view) {
                                                                String cantidad = edtProductoCantidad.getText().toString();
                                                                if (cantidad.isEmpty()) {
                                                                    edtProductoCantidad.setError("Ingresa las Placas");
                                                                } else {
                                                                    String placas = cantidad;
                                                                    Intent intent = new Intent(getApplicationContext(), eligeLitrosPrecioTL.class); //ProductoTLl
                                                                    intent.putExtra("placas", placas);
                                                                    intent.putExtra("odometro", odometro);
                                                                    intent.putExtra("NumeroInternoEstacion", numerointernosucursal);
                                                                    intent.putExtra("SucursalEmpleadoId", sucursalempleados);
                                                                    intent.putExtra("PosicioDeCarga", posicionCargaId);
                                                                    intent.putExtra("NumeroDeTarjeta", numerotarjeta);
                                                                    intent.putExtra("ClaveTanqueLleno", clave);
                                                                    intent.putExtra("Tipocliente", tipocliente);
                                                                    intent.putExtra("transaccionid", transaccionId);
                                                                    intent.putExtra("folio", folio);
                                                                    intent.putExtra("nipCliente", NipCliente);
                                                                    intent.putExtra("nipMd5Cliente", NipClientemd5);
                                                                    intent.putExtra("Litros", SolicitudEnLitros);
                                                                    intent.putExtra("CombustiblesAsociados", finalCombustibles);
                                                                    startActivity(intent);
                                                                    finish();
                                                                }
                                                            }
                                                        });
                                                        viewLectura.findViewById(R.id.buttonNo).setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View view) {
                                                                modales.alertDialog.dismiss();
                                                            }
                                                        });

                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                } else {
                                                    Intent intent = new Intent(getApplicationContext(), eligeLitrosPrecioTL.class); //ProductoTLl
                                                    intent.putExtra("placas", "");
                                                    intent.putExtra("odometro", "");
                                                    intent.putExtra("NumeroInternoEstacion", numerointernosucursal);
                                                    intent.putExtra("SucursalEmpleadoId", sucursalempleados);
                                                    intent.putExtra("PosicioDeCarga", posicionCargaId);
                                                    intent.putExtra("NumeroDeTarjeta", numerotarjeta);
                                                    intent.putExtra("ClaveTanqueLleno", clave);
                                                    intent.putExtra("Tipocliente", tipocliente);
                                                    intent.putExtra("transaccionid", transaccionId);
                                                    intent.putExtra("folio", folio);
                                                    intent.putExtra("nipCliente", NipCliente);
                                                    intent.putExtra("nipMd5Cliente", NipClientemd5);
                                                    intent.putExtra("Litros", SolicitudEnLitros);
                                                    intent.putExtra("CombustiblesAsociados", combustibles);

                                                    startActivity(intent);
                                                    finish();
                                                }
                                            }
                                        }
                                    } else {
                                        Toast.makeText(getApplicationContext(), "La contraseña ingresada no es correcta", Toast.LENGTH_LONG).show();
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        //funcion para capturar errores
                    }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error.networkResponse.statusCode == 401) {
                        GlobalToken.errorToken(PosicionCargaTLl.this);
                    } else {
                        Toast.makeText(PosicionCargaTLl.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
//                    bar.cancel();
//                    //Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
//                    String titulo = "AVISO";
//                    String mensaje = "Problema de Conexión con TanqueLleno";
//                    Modales modales = new Modales(PosicionCargaTLl.this);
//                    View view1 = modales.MostrarDialogoError(PosicionCargaTLl.this, mensaje);
//                    view1.findViewById(R.id.buttonAction).setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            Intent intent = new Intent(getApplicationContext(), Menu_Principal.class);
//                            startActivity(intent);
//                            finish();
//                            modales.alertDialog.dismiss();
//                        }
//                    });

                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    // Posting parameters to login url
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("SucursalId", data.getIdSucursal()); //EstacionId
                    params.put("PosicionCarga", String.valueOf(posicionCargaId));
                    params.put("TarjetaCliente", numerotarjeta);
                    params.put("NIP", NipClientemd5);
                    params.put("Authorization", data.getToken());
                    return params;
                }
            };

            // Añade la peticion a la cola
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            eventoReq.setRetryPolicy(new DefaultRetryPolicy(15000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(eventoReq);
        }
    }


    private void IngresarPlacas(boolean placa, final String odometro, final String numerointernosucursal,
                                final String sucursalempleados, final Long posi, final String track,
                                final String clave, final Integer tipocliente, final Long transaccionId, final Long folio, final String SolicitudEnLitros, String combustibles) {
        if (placa == true){
            try {
                String titulo = "TANQUE LLENO";
                String mensaje = "Ingresa las Placas \n" ;
                Modales modales = new Modales(PosicionCargaTLl.this);
                View viewLectura = modales.MostrarDialogoInsertaDato(PosicionCargaTLl.this, mensaje, titulo);
                EditText edtProductoCantidad = ((EditText) viewLectura.findViewById(R.id.textInsertarDato));
                edtProductoCantidad.setInputType(InputType.TYPE_CLASS_TEXT);
                viewLectura.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        modales.alertDialog.dismiss();
                        String cantidad = edtProductoCantidad.getText().toString();
                        if (cantidad.isEmpty()){
                            edtProductoCantidad.setError("Ingresa las Placas");
                        }else {
                            String placas = cantidad;
                            Intent intent = new Intent(getApplicationContext(), eligeLitrosPrecioTL.class); //ProductoTLl
                            intent.putExtra("placas",placas);
                            intent.putExtra("odometro",odometro);
                            intent.putExtra("NumeroInternoEstacion", numerointernosucursal);
                            intent.putExtra("SucursalEmpleadoId",sucursalempleados);
                            intent.putExtra("PosicioDeCarga", posi);
                            intent.putExtra("NumeroDeTarjeta",track);
                            intent.putExtra("ClaveTanqueLleno",clave);
                            intent.putExtra("Tipocliente",tipocliente);
                            intent.putExtra("transaccionid",transaccionId);
                            intent.putExtra("folio", folio);
                            intent.putExtra("nipCliente", NipCliente);
                            intent.putExtra("nipMd5Cliente", NipClientemd5);
                            intent.putExtra("Litros", SolicitudEnLitros);
                            intent.putExtra("CombustiblesAsociados", String.valueOf(combustibles));

                            startActivity(intent);
                            finish();
                        }
                    }
                });
                viewLectura.findViewById(R.id.buttonNo).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        modales.alertDialog.dismiss();
                    }
                });
            }catch (Exception e){
                e.printStackTrace();
            }
        } else{
            Intent intent = new Intent(getApplicationContext(), eligeLitrosPrecioTL.class);
            intent.putExtra("placas","");
            intent.putExtra("odometro",odometro);
            intent.putExtra("NumeroInternoEstacion", numerointernosucursal);
            intent.putExtra("SucursalEmpleadoId",sucursalempleados);
            intent.putExtra("PosicioDeCarga", posi);
            intent.putExtra("NumeroDeTarjeta",track);
            intent.putExtra("ClaveTanqueLleno",clave);
            intent.putExtra("Tipocliente",tipocliente);
            intent.putExtra("transaccionid",transaccionId);
            intent.putExtra("folio", folio);
            intent.putExtra("nipCliente", NipCliente);
            intent.putExtra("nipMd5Cliente", NipClientemd5);
            intent.putExtra("Litros", SolicitudEnLitros);
            intent.putExtra("CombustiblesAsociados", String.valueOf(combustibles));

            startActivity(intent);
            finish();
        }
    }

    //Metodo para regresar a la actividad principal
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), Menu_Principal.class);
        startActivity(intent);
        finish();
    }


}