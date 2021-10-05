package com.corpogas.corpoapp.Puntada;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;
import android.widget.Toast;

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
import com.corpogas.corpoapp.Menu_Principal;
import com.corpogas.corpoapp.Modales.Modales;
import com.corpogas.corpoapp.R;
import com.corpogas.corpoapp.Interfaces.Endpoints.EndPoints;
import com.corpogas.corpoapp.TanqueLleno.PosicionCargaTLl;
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

public class PosicionPuntadaRedimir extends AppCompatActivity {
    RecyclerView rcvPosicionCarga;
    String EstacionId;
    Long sucursalId;
    String ipEstacion;
    String lugarproviene;
    String usuario;
    String numerotarjeta;
    String NipCliente;
    String NipClientemd5;
    long posicionCargaId;
    long numeroOperativa;
    long cargaNumeroInterno;
    long usuarioid;
    long empleadoNumero;
    Integer numerotarjetero;
    Boolean banderaposicionCarga;
    SQLiteBD db;
    RespuestaApi<AccesoUsuario> accesoUsuario;
    List<RecyclerViewHeaders> lrcvPosicionCarga;


    ProgressDialog bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posicion_puntada_redimir);
        init();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvPosicionCarga.setLayoutManager(linearLayoutManager);
        rcvPosicionCarga.setHasFixedSize(true);
        PosicionCarga();

    }


    private void init() {
        rcvPosicionCarga = (RecyclerView)findViewById(R.id.rcvPosicionCarga);
        db = new SQLiteBD(this);
        this.setTitle(db.getNombreEstacion() + " ( EST.:" + db.getNumeroEstacion() + ")");
        EstacionId = db.getIdEstacion();
        sucursalId = Long.parseLong(db.getIdSucursal());
        ipEstacion = db.getIpEstacion();
        numerotarjetero = Integer.parseInt(db.getIdTarjtero());

        lugarproviene = getIntent().getStringExtra("lugarproviene");
        usuarioid = getIntent().getLongExtra("IdUsuario",0);
        usuario = getIntent().getStringExtra("ClaveDespachador");
        numerotarjeta = getIntent().getStringExtra("track"); //"6ABE322B"; //
        NipCliente = getIntent().getStringExtra("nip");

    }
    private void PosicionCarga(){
        bar = new ProgressDialog(PosicionPuntadaRedimir.this);
        bar.setTitle("Cargando Posiciones de Carga");
        bar.setMessage("Ejecutando... ");
        bar.setIcon(R.drawable.gas);
        bar.setCancelable(false);
        bar.show();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://" + db.getIpEstacion() + "/CorpogasService/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        EndPoints obtenerAccesoUsuario = retrofit.create(EndPoints.class);
        Call<RespuestaApi<AccesoUsuario>> call = obtenerAccesoUsuario.getAccesoUsuario(sucursalId, usuario);
        call.enqueue(new Callback<RespuestaApi<AccesoUsuario>>() {


            @Override
            public void onResponse(Call<RespuestaApi<AccesoUsuario>> call, Response<RespuestaApi<AccesoUsuario>> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                accesoUsuario = response.body();
                String mensajes =  accesoUsuario.getMensaje();  // jsonObject.getString("Mensaje");
                boolean correcto =  accesoUsuario.isCorrecto();  //jsonObject.getString("Correcto");

                if(accesoUsuario.getObjetoRespuesta() == null)
                {
                    if(correcto == false)
                    {

                        //Toast.makeText(posicionProductos.this, mensaje, Toast.LENGTH_SHORT).show();
                        String titulo = "AVISO";
                        String mensaje = "" + mensajes;
                        Modales modales = new Modales(PosicionPuntadaRedimir.this);
                        View view1 = modales.MostrarDialogoAlertaAceptar(PosicionPuntadaRedimir.this,mensaje,titulo);
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
                    }else
                    {
                        //Toast.makeText(posicionProductos.this, mensaje, Toast.LENGTH_SHORT).show();
                        String titulo = "AVISO";
                        String mensaje = "El usuario no tiene asignadas posiciones de carga. " ;
                        Modales modales = new Modales(PosicionPuntadaRedimir.this);
                        View view1 = modales.MostrarDialogoAlertaAceptar(PosicionPuntadaRedimir.this,mensaje,titulo);
                        view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                modales.alertDialog.dismiss();
                                Intent intent1 = new Intent(getApplicationContext(), Menu_Principal.class);
                                startActivity(intent1);
                                finish();
                            }
                        });

                    }

                }else {
                    lrcvPosicionCarga = new ArrayList<>();
                    banderaposicionCarga= false;
                    empleadoNumero = accesoUsuario.getObjetoRespuesta().getNumeroEmpleado();
                    for(Control control : accesoUsuario.getObjetoRespuesta().getControles())
                    {
                        for( Posicion posicion : control.getPosiciones() )
                        {

                            posicionCargaId = posicion.getPosicionCargaId();
                            long posicionCargaNumeroInterno = posicion.getNumeroInterno();
                            boolean pocioncargadisponible = posicion.isDisponible();
                            boolean pocioncargapendientecobro = posicion.isPendienteCobro();
                            String descripcionoperativa =  posicion.getDescripcionOperativa();
                            String descripcion = posicion.getDescripcion();
                            numeroOperativa = posicion.getOperativa();
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
                                                lrcvPosicionCarga.add(new RecyclerViewHeaders(titulo,subtitulo,R.drawable.gas,posicionCargaId,posicionCargaNumeroInterno));//
                                                banderaposicionCarga = true;
                                            }
                                        }
                                    }
                                }
                            }

                        }

                    }

                    if (banderaposicionCarga.equals(false)){
//                        //Toast.makeText(posicionFinaliza.this, "No hay Posiciones de Carga para Finalizar Venta", Toast.LENGTH_SHORT).show();
                        String titulo = "AVISO";
                        String mensaje="";
                        mensaje = "No hay posiciones de carga disponiles";
                        Modales modales = new Modales(PosicionPuntadaRedimir.this);
                        View view1 = modales.MostrarDialogoAlertaAceptar(PosicionPuntadaRedimir.this,mensaje,titulo);
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
            }

            @Override
            public void onFailure(Call<RespuestaApi<AccesoUsuario>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                bar.cancel();
            }
        });

    }


    private void initializeAdapter() {
        RVAdapter adapter = new RVAdapter(lrcvPosicionCarga);
        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                posicionCargaId=lrcvPosicionCarga.get(rcvPosicionCarga.getChildAdapterPosition(v)).getPosicionCargaId();
                cargaNumeroInterno = lrcvPosicionCarga.get(rcvPosicionCarga.getChildAdapterPosition(v)).getPosicioncarganumerointerno();
                //posicion = numeroposicioncarga;
                //solicitarBalanceTarjeta(numeroposicioncarga);
                switch (lugarproviene){
                    case "ConsultaSaldoPuntada":
                        obtieneSaldoTarjeta(String.valueOf(posicionCargaId));
                        break;
                    case "Registrar":
                        RegistraTarjeta();
                        break;
                    case "Redimir":
                        obtieneSaldoTarjeta(String.valueOf(posicionCargaId));
                        break;
                    default:
                        Toast.makeText(PosicionPuntadaRedimir.this, "No se selecciono ninguna opción válida", Toast.LENGTH_SHORT).show();
                        Intent intente = new Intent(getApplicationContext(), Menu_Principal.class);
                        startActivity(intente);
                        finish();
                        break;
                }
            }
        });

        rcvPosicionCarga.setAdapter(adapter);
    }

    public void obtieneSaldoTarjeta(String PosicionDeCarga) {
        String Posi = PosicionDeCarga;
        bar = new ProgressDialog(PosicionPuntadaRedimir.this);
        bar.setTitle("Conectando con Puntada");
        bar.setMessage("Ejecutando... ");
        bar.setIcon(R.drawable.gas);
        bar.setCancelable(false);
        bar.show();
        String url = "http://" + ipEstacion + "/CorpogasService/api/puntadas/actualizaPuntos/clave/"+usuario;

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
                                        intent.putExtra("pos", Posi);
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
                                                final Modales modales = new Modales(PosicionPuntadaRedimir.this);
                                                View view1 = modales.MostrarDialogoCorrecto(PosicionPuntadaRedimir.this,mensajes);
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
                                    Modales modales = new Modales(PosicionPuntadaRedimir.this);
                                    View view1 = modales.MostrarDialogoAlertaAceptar(PosicionPuntadaRedimir.this,mensajes,titulo);
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
                                    Modales modales = new Modales(PosicionPuntadaRedimir.this);
                                    View view1 = modales.MostrarDialogoAlertaAceptar(PosicionPuntadaRedimir.this,mensajes,titulo);
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
                params.put("EmpleadoId", String.valueOf(usuarioid));
                params.put("SucursalId", sucursalId.toString());
                params.put("RequestID","33");
                params.put("PosicionCarga", String.valueOf(posicionCargaId));
                params.put("Tarjeta", numerotarjeta);
                params.put("NuTarjetero", numerotarjetero.toString());
                params.put("NIP", NipCliente);
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

    private void RegistraTarjeta() {
        if (!Conexion.compruebaConexion(this)) {
            Toast.makeText(getBaseContext(), "Sin conexión a la red ", Toast.LENGTH_SHORT).show();
            Intent intent1 = new Intent(getApplicationContext(), Menu_Principal.class);
            startActivity(intent1);
            finish();
        } else {

            String url = "http://" + ipEstacion + "/CorpogasService/api/puntadas/actualizaPuntos/clave/" + usuario;
            StringRequest eventoReq = new StringRequest(Request.Method.POST, url,
                    new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            String estado = null;
                            String mensaje = null;

                            try {
                                JSONObject respuesta = new JSONObject(response);
                                estado = respuesta.getString("Estado");
                                mensaje = respuesta.getString("Mensaje");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            if (estado.equals("true")) {
                                try {
                                    String titulo = "TARJETA PUNTADA";
                                    String mensajes = "" + mensaje;
                                    Modales modales = new Modales(PosicionPuntadaRedimir.this);
                                    View view1 = modales.MostrarDialogoAlertaAceptar(PosicionPuntadaRedimir.this, mensajes, titulo);
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
                                try {
                                    String titulo = "AVISO";
                                    String mensajes = "" + mensaje;
                                    Modales modales = new Modales(PosicionPuntadaRedimir.this);
                                    View view1 = modales.MostrarDialogoError(PosicionPuntadaRedimir.this, mensajes);
                                    view1.findViewById(R.id.buttonAction).setOnClickListener(new View.OnClickListener() {
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
                    Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    // Posting parameters to login url
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("EmpleadoId", String.valueOf(usuarioid));
                    params.put("SucursalId", sucursalId.toString());
                    params.put("RequestID", "39");
                    params.put("PosicionCarga", String.valueOf(posicionCargaId));
                    params.put("Tarjeta", numerotarjeta);
                    params.put("NIP", NipCliente);
                    // params.put("NuTarjetero", TarjeteroId);
                    String gson = new Gson().toJson(params);
                    return params;
                }
            };

            // Añade la peticion a la cola
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            eventoReq.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(eventoReq);
        }
    }










    @Override
    public void onBackPressed() {
//        Intent intent = new Intent(getApplicationContext(), Munu_Principal.class);
//        startActivity(intent);
//        finish();
        startActivity(new Intent(getBaseContext(), Menu_Principal.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
        finish();
    }

}