package com.corpogas.corpoapp.Puntada;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
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
import com.corpogas.corpoapp.Productos.MostrarCarritoTransacciones;
import com.corpogas.corpoapp.R;
import com.corpogas.corpoapp.Interfaces.Endpoints.EndPoints;
import com.corpogas.corpoapp.TanqueLleno.PosicionCargaTLl;
import com.corpogas.corpoapp.VentaCombustible.Adaptador;
import com.corpogas.corpoapp.VentaCombustible.IniciaVentas;
import com.corpogas.corpoapp.VentaCombustible.ProcesoVenta;
import com.corpogas.corpoapp.VentaCombustible.VentaCombustibleAceites;
import com.corpogas.corpoapp.VentaCombustible.VentaProductos;
import com.corpogas.corpoapp.Yena.LecturayEscaneo;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

public class PosicionPuntadaRedimir extends AppCompatActivity {
    RecyclerView rcvPosicionCarga;
    String EstacionId;
    Long sucursalId;
    String ipEstacion;
    String lugarproviene;
    String usuario;
    String numerotarjeta;
    String NipCliente;
    String NipClientemd5, autoJarreo;
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
    Button btnCargarTodasPC;
    String descuento;
    Double descuentoPorLitro;
    ProgressDialog bar;

    DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
    DecimalFormat df;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posicion_puntada_redimir);
        init();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvPosicionCarga.setLayoutManager(linearLayoutManager);
        rcvPosicionCarga.setHasFixedSize(true);
        PosicionCarga(1);
        btnCargarTodasPC = (Button) findViewById(R.id.btnCargarTodasPC);
        btnCargarTodasPC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PosicionCarga(2);
            }
        });
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
        usuarioid = Long.parseLong(db.getUsuarioId()); //getIntent().getLongExtra("IdUsuario",0);
        usuario = db.getNumeroEmpleado();//getIntent().getStringExtra("ClaveDespachador");   getClave()
        numerotarjeta = getIntent().getStringExtra("track"); //"6ABE322B"; //
        NipCliente = getIntent().getStringExtra("nip");

        simbolos.setDecimalSeparator('.');
        df = new DecimalFormat("###,###.00",simbolos);
        df.setMaximumFractionDigits(2);


    }
    public void PosicionCarga(Integer Identificador){
        bar = new ProgressDialog(PosicionPuntadaRedimir.this);
        bar.setTitle("Buscando Posiciones de Carga");
        bar.setMessage("Ejecutando... ");
        bar.setIcon(R.drawable.gas);
        bar.setCancelable(false);
        bar.show();

        String url;
        if (Identificador.equals(1)){
            url = "http://" + ipEstacion + "/CorpogasService/api/posicionCargas/GetPosicionCargaEmpleadoId/sucursal/" + sucursalId + "/empleado/" + usuario;
        }else{
            url = "http://" + ipEstacion + "/CorpogasService/api/posicionCargas/GetPosicionCargasEstacion/sucursal/" + sucursalId;
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
                        Modales modales = new Modales(PosicionPuntadaRedimir.this);
                        View view1 = modales.MostrarDialogoAlertaAceptar(PosicionPuntadaRedimir.this,mensaje,titulo);
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
                            String jarreo;
                            if (numeroOperativa == 31){
                                jarreo = "true";
                            }else{
                                jarreo = "false";
                            }

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
                                                lrcvPosicionCarga.add(new RecyclerViewHeaders(titulo,subtitulo,R.drawable.gas,posicionCargaId,posicionCargaNumeroInterno, jarreo));//
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
                            Modales modales = new Modales(PosicionPuntadaRedimir.this);
                            View view1 = modales.MostrarDialogoAlertaAceptar(PosicionPuntadaRedimir.this,mensajes,titulo);
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
                String algo = new String(error.networkResponse.data);
                try {
                    //creamos un json Object del String algo
                    JSONObject errorCaptado = new JSONObject(algo);
                    //Obtenemos el elemento ExceptionMesage del errro enviado
                    String errorMensaje = errorCaptado.getString("ExceptionMessage");
                    try {
                        String titulo = "Jarreo";
                        String mensajes = errorMensaje;
                        Modales modales = new Modales(PosicionPuntadaRedimir.this);
                        View view1 = modales.MostrarDialogoAlertaAceptar(PosicionPuntadaRedimir.this, mensajes, titulo);
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


    private void PosicionCargaRetro(){
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
        Call<RespuestaApi<AccesoUsuario>> call = obtenerAccesoUsuario.getAccesoUsuarionumeroempleado(sucursalId, usuario); //getAccesoUsuario
        call.enqueue(new Callback<RespuestaApi<AccesoUsuario>>() {

            @Override
            public void onResponse(Call<RespuestaApi<AccesoUsuario>> call, Response<RespuestaApi<AccesoUsuario>> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                accesoUsuario = response.body();
                String mensajes =  accesoUsuario.getMensaje();  // jsonObject.getString("Mensaje");
                boolean correcto =  accesoUsuario.isCorrecto();  //jsonObject.getString("Correcto");

                if(accesoUsuario.getObjetoRespuesta() == null) {
                    if(correcto == false) {

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
                    }else {
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
                            String jarreo;
                            numeroOperativa = posicion.getOperativa();
                            if (numeroOperativa == 31){
                                jarreo = "true";
                            }else{
                                jarreo = "false";
                            }
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
                                                lrcvPosicionCarga.add(new RecyclerViewHeaders(titulo,subtitulo,R.drawable.gas,posicionCargaId,posicionCargaNumeroInterno, jarreo));//
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
                autoJarreo = lrcvPosicionCarga.get(rcvPosicionCarga.getChildAdapterPosition(v)).getJarreo();
                //posicion = numeroposicioncarga;
                //solicitarBalanceTarjeta(numeroposicioncarga);
                switch (lugarproviene){
                    case "ConsultaSaldoPuntada":
                        obtieneSaldoTarjeta(String.valueOf(posicionCargaId));
                        break;

                    case "Consulta Yena":

                    case "Acumulacion Yena":
                        Intent intent = new Intent(PosicionPuntadaRedimir.this, LecturayEscaneo.class);
                        intent.putExtra("lugarProviene", lugarproviene);
                        intent.putExtra("posicionCarga", posicionCargaId);
                        intent.putExtra("numeroEmpleado", empleadoNumero);
                        startActivity(intent);
                        break;

                    case "Redencion Yena":
                        Intent intent2 = new Intent(PosicionPuntadaRedimir.this, LecturayEscaneo.class);
                        intent2.putExtra("lugarProviene", lugarproviene);
                        intent2.putExtra("pocioncargaid", posicionCargaId);
                        intent2.putExtra("numeroEmpleado", empleadoNumero);
                        startActivity(intent2);
                        break;

                    case "Descuento Yena":
                        Intent intentDesc = new Intent(getApplicationContext(), LecturayEscaneo.class);
                        //Intent intent = new Intent(getApplicationContext(), ScanManagerDemo.class);
                        intentDesc.putExtra("combustible", "");
                        intentDesc.putExtra("posicionCarga", posicionCargaId);
                        intentDesc.putExtra("estacionjarreo", "estacionJarreo");
                        intentDesc.putExtra("claveProducto", "");
                        intentDesc.putExtra("precioProducto", "");
                        intentDesc.putExtra("despacholibre", "no");
                        intentDesc.putExtra("lugarProviene", "descuentoYena");
                        intentDesc.putExtra("pocioncarganumerointerno", cargaNumeroInterno);
                        startActivity(intentDesc);
                        break;


                    case "Registrar":
                        RegistraTarjeta();
                        break;
                    case "RedimirQR":

                    case "Redimir":
                        obtieneSaldoTarjeta(String.valueOf(posicionCargaId));
                        break;
                    case "Ventas":
                        ValidaTransaccionActiva(String.valueOf(posicionCargaId), String.valueOf(numeroOperativa), autoJarreo, false);
                        break;
                    case "DescuentoQr":
                        descuento = getIntent().getStringExtra("descuento");
                        descuentoPorLitro = Double.parseDouble(descuento);
                        ValidaTransaccionActiva(String.valueOf(posicionCargaId), String.valueOf(numeroOperativa), autoJarreo, true);
                        break;

                    case "Imprimir":
                    case "Reimprimir":
                    case "Ticket Pendiente":
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



    private void ValidaTransaccionActiva(String posicionCarga, String numerooperativa, String Estacionjarreo, Boolean banderaDescuento) {
        if (!Conexion.compruebaConexion(this)) {
            Toast.makeText(getBaseContext(), "Sin conexión a la red ", Toast.LENGTH_SHORT).show();
            Intent intent1 = new Intent(getApplicationContext(), Menu_Principal.class);

            startActivity(intent1);
            finish();
        } else {

            String url = "http://" + ipEstacion + "/CorpogasService/api/ventaProductos/sucursal/" + sucursalId + "/posicionCargaId/" + posicionCarga;
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new com.android.volley.Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        String disponible, estado , pendientdecobro, descripcionOperativa , numeroPosicionCarga, descripcion;

                        JSONObject jsonObject = new JSONObject(response);
                        String correcto = jsonObject.getString("Correcto");
                        String mensaje = jsonObject.getString("Mensaje");
                        String ObjetoRespuesta = jsonObject.getString("ObjetoRespuesta");

//                    JSONObject jsonObject1 = new JSONObject(ObjetoRespuesta);

                        if (correcto.equals("false")){
//                            if (autoJarreo.equals("true")){
//                                enviarSiguientePantallaVentas(posicionCarga);
//                            }else{
//                                confirmaDescuentoPuntadaQr(posicionCarga);
//                            }
                            enviarSiguientePantallaVentas(posicionCarga, banderaDescuento);
                        }else{
                            Boolean banderaConDatos;
                            if (ObjetoRespuesta.equals("null")) {
                                banderaConDatos = false;
                            } else {
                                if (ObjetoRespuesta.equals("[]")) {
                                    banderaConDatos = false;
                                } else {
                                    banderaConDatos = true;
                                }
                            }

                            if (banderaConDatos.equals(false)){
//                                if (autoJarreo.equals("true")){
//                                    enviarSiguientePantallaVentas(posicionCarga);
//                                }else{
//                                    confirmaDescuentoPuntadaQr(posicionCarga);
//                                }
                                enviarSiguientePantallaVentas(posicionCarga, banderaDescuento);
                            } else {

                                Double MontoenCanasta = 0.00;
                                try {
                                    JSONArray ArregloCadenaRespuesta = new JSONArray(ObjetoRespuesta);
                                    for (int i = 0; i < ArregloCadenaRespuesta.length(); i++) {
                                        JSONObject ObjetoCadenaRespuesta = ArregloCadenaRespuesta.getJSONObject(i);
                                        String ImporteTotal = ObjetoCadenaRespuesta.getString("ImporteTotal");

                                        Double aTotal;
                                        String fTotal;
                                        aTotal = Double.parseDouble(ImporteTotal);//Double.parseDouble(Monto) * Double.parseDouble(Precio);
                                        MontoenCanasta = MontoenCanasta + aTotal;
                                    }
                                    Intent intente = new Intent(getApplicationContext(), MostrarCarritoTransacciones.class);
                                    //se envia el id seleccionado a la clase Usuario Producto
                                    intente.putExtra("posicion", posicionCarga);
                                    intente.putExtra("usuario", usuarioid);
                                    intente.putExtra("cadenaproducto", "");
                                    intente.putExtra("lugarproviene", "Despacho");
                                    intente.putExtra("numeroOperativa", numeroOperativa);
                                    intente.putExtra("cadenarespuesta", ObjetoRespuesta);
                                    intente.putExtra("pocioncargaid", cargaNumeroInterno);
                                    //Ejecuta la clase del Usuario producto
                                    startActivity(intente);
                                    //Finaliza activity
                                    finish();

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                        }
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
                            String titulo = "Posiciones de Carga";
                            String mensajes = errorMensaje;
                            Modales modales = new Modales(PosicionPuntadaRedimir.this);
                            View view1 = modales.MostrarDialogoAlertaAceptar(PosicionPuntadaRedimir.this, mensajes, titulo);
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

    private void enviarSiguientePantallaVentas(String posicionCarga, Boolean banderaDescuento){
        Intent intent = new Intent(getApplicationContext(), IniciaVentas.class); //VentaProductos
        intent.putExtra("numeroEmpleado", empleadoNumero);
        intent.putExtra("posicionCarga", posicionCarga);
        intent.putExtra("estacionjarreo", autoJarreo);
        intent.putExtra("pcnumerointerno", cargaNumeroInterno);
        intent.putExtra("pocioncargaid", cargaNumeroInterno);
        intent.putExtra("descuento", descuentoPorLitro);
        intent.putExtra("lugarProviene", "ventas");
        intent.putExtra("numeroTarjeta", "");
        intent.putExtra("nip", NipCliente);
        intent.putExtra("banderaDescuento", banderaDescuento);

        startActivity(intent);
        finish();

    }


    private void confirmaDescuentoPuntadaQr(String posicionCarga){
//        String titulo = "PUNTADA QR";
//        String mensajes = "¿Desea aplicar descuento?";
//        Modales modalesPuntada = new Modales(PosicionPuntadaRedimir.this);
//        View viewLectura = modalesPuntada.MostrarDialogoAlertaParaRedencion(PosicionPuntadaRedimir.this, mensajes);
//        viewLectura.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), LecturayEscaneo.class);
//                //Intent intent = new Intent(getApplicationContext(), ScanManagerDemo.class);
//                intent.putExtra("combustible", "");
//                intent.putExtra("posicionCarga", posicionCarga);
//                intent.putExtra("estacionjarreo", "estacionJarreo");
//                intent.putExtra("claveProducto", "");
//                intent.putExtra("precioProducto", "");
//                intent.putExtra("despacholibre", "no");
//                intent.putExtra("lugarProviene", "descuentoYena");
//                intent.putExtra("pocioncarganumerointerno", cargaNumeroInterno);
//                startActivity(intent);
//                finish();
//                modalesPuntada.alertDialog.dismiss();
//            }
//        });
//        viewLectura.findViewById(R.id.buttonNo).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////              String precio = PrecioProducto.get(position);
////              String claveProducto = ClaveProducto.get(position);
////              LeeTarjeta();
//                Intent intent = new Intent(getApplicationContext(), PuntadaRedimirQr.class);
//                //Intent intent = new Intent(getApplicationContext(), ScanManagerDemo.class);
//                intent.putExtra("combustible", "");
//                intent.putExtra("posicionCarga", posicionCarga);
//                intent.putExtra("estacionjarreo", "estacionJarreo");
//                intent.putExtra("claveProducto", "");
//                intent.putExtra("precioProducto", "");
//                intent.putExtra("despacholibre", "no");
//                intent.putExtra("lugarProviene", "Acumular");
//                intent.putExtra("pocioncarganumerointerno", cargaNumeroInterno);
//                startActivity(intent);
//                finish();
//                modalesPuntada.alertDialog.dismiss();
//            }
//        });
//        viewLectura.findViewById(R.id.buttonPuntada).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                modalesPuntada.alertDialog.dismiss();
//                enviarSiguientePantallaVentas(posicionCarga, banderaDescuento);
//            }
//        });
    }

    public void obtieneSaldoTarjeta(String PosicionDeCarga) {
        String Posi = PosicionDeCarga;
        bar = new ProgressDialog(PosicionPuntadaRedimir.this);
        bar.setTitle("Conectando con Puntada");
        bar.setMessage("Ejecutando... ");
        bar.setIcon(R.drawable.redimirpuntada);
        bar.setCancelable(false);
        bar.show();
        String url = "http://" + ipEstacion + "/CorpogasService/api/puntadas/actualizaPuntos/numeroEmpleado/"+usuario;

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
                                        case "RedimirQR":
                                        case "Redimir": //Consulta Saldo
                                            //String carga = getIntent().getStringExtra("pos");
                                            try {
                                                enviarProductosARedimir(Posi, saldo, Claveusuario, Idusuario, track, NipTarjeta );
                                            }catch (Exception e){
                                                e.printStackTrace();
                                            }
                                            break;
                                        case "ConsultaSaldoPuntada"://Redimir
                                            try {
                                                simbolos.setDecimalSeparator('.');
                                                df = new DecimalFormat("$#,###.00##",simbolos);
                                                df.setMaximumFractionDigits(2);
                                                String titulo = "AVISO";
                                                String mensajes = "Tarjeta No. " + track + " con Saldo: " + df.format(Double.parseDouble(saldo));
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
                                    bar.cancel();
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
                                    bar.cancel();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                bar.cancel();
//                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();
                String titulo = "AVISO";
                String mensajes;
                mensajes = "Sin conexón con la consola";
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
        }){
            @Override
            protected Map<String, String> getParams() {
                //Obtenemos los parmetros a enviar
                String sproducto = "[]";
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("RequestID","33");
                params.put("SucursalId", sucursalId.toString());
                params.put("PosicionCarga", String.valueOf(posicionCargaId));
                params.put("NuTarjetero", numerotarjetero.toString());
                params.put("Tarjeta", numerotarjeta);
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


    private void enviarProductosARedimir(String Posi, String saldo, String Claveusuario, Long Idusuario, String track, String NipTarjeta){

        Intent intent = new Intent(getApplicationContext(), ProductosARedimir.class);
        intent.putExtra("pos", Posi);
        intent.putExtra("saldo", saldo);
        intent.putExtra("clave", db.getClave()); //Claveusuario
        intent.putExtra("empleadoid", db.getUsuarioId()); //Idusuario)
        intent.putExtra("track", track);
        intent.putExtra("nip", NipTarjeta);
        intent.putExtra("descuentouno", descuentoPorLitro);
        intent.putExtra("lugarproviene", lugarproviene);
        startActivity(intent);
        finish();


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