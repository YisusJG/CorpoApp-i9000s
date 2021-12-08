package com.corpogas.corpoapp.VentaCombustible;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.corpogas.corpoapp.Adapters.RVAdapter;
import com.corpogas.corpoapp.Configuracion.SQLiteBD;
import com.corpogas.corpoapp.Entities.Accesos.AccesoUsuario;
import com.corpogas.corpoapp.Entities.Classes.RecyclerViewHeaders;
import com.corpogas.corpoapp.Entities.Classes.RespuestaApi;
import com.corpogas.corpoapp.Entities.Common.ProductoTarjetero;
import com.corpogas.corpoapp.Entities.Ventas.Transaccion;
import com.corpogas.corpoapp.Menu_Principal;
import com.corpogas.corpoapp.Modales.Modales;
import com.corpogas.corpoapp.MyListAdapter;
import com.corpogas.corpoapp.Productos.MostrarCarritoTransacciones;
import com.corpogas.corpoapp.R;
import com.corpogas.corpoapp.Interfaces.Endpoints.EndPoints;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProcesoVenta extends AppCompatActivity {
    String bearerToken;
    RespuestaApi<AccesoUsuario> token;
    RecyclerView rcvProcesoVenta;
    ListView listPosicionesCarga;
    String EstacionId,  ipEstacion, lugarproviene, IdUsuario;
    long posicionCargaId, cargaNumeroInterno,empleadoNumero,sucursalId;
    Boolean banderaposicionCarga;
    SQLiteBD data;
    RespuestaApi<AccesoUsuario> accesoUsuario;
    List<RecyclerViewHeaders> lProcesoVenta;
    RespuestaApi<List<ProductoTarjetero>> respuestaApiProductoTarjetero;
    RespuestaApi<Boolean> respuestaApiAutorizaDespacho;
    RespuestaApi<Boolean> respuestaApiTicketPendienteCobro;
    RespuestaApi<Transaccion> respuestaApiTransaccion;
    ProgressDialog bar;

    //Declaramos la lista de titulo
    List<String> maintitle;
    List<String> maintitle1;
    //Creamos la lista para los subtitulos
    List<String> subtitle;
    //CReamos una nueva list de tipo Integer con la cual cargaremos a una imagen
    List<Integer> imgid;
    List<String> permiteventa;
    List<String> numeroOperativa;

    Button btnCargaTodasPosicionesCarga;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proceso_venta);
        getToken();
        init();
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//        rcvProcesoVenta.setLayoutManager(linearLayoutManager);
//        rcvProcesoVenta.setHasFixedSize(true);

        btnCargaTodasPosicionesCarga = findViewById(R.id.btnCargaTodasPosicionesCarga);
        btnCargaTodasPosicionesCarga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                posicionCargaFinaliza(2);
            }
        });
        posicionCargaFinaliza(1);
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



    private void init() {
        rcvProcesoVenta = (RecyclerView)findViewById(R.id.rcvPosicionCargaTicket);
        data = new SQLiteBD(getApplicationContext());
        this.setTitle(data.getRazonSocial());
        this.setTitle(data.getNombreEstacion() + " ( EST.:" + data.getNumeroEstacion() + ")");
        EstacionId = data.getIdEstacion();
        sucursalId = Long.parseLong(data.getIdSucursal());
        ipEstacion = data.getIpEstacion();
        lugarproviene = getIntent().getStringExtra("lugarproviene");
        IdUsuario = getIntent().getStringExtra("IdUsuario");

        //        numeroEmpleado = getIntent().getStringExtra("numeromepleado");
    }


    public void posicionCargaFinaliza(Integer Identificador){
        bar = new ProgressDialog(ProcesoVenta.this);
        bar.setTitle("Buscando Posiciones de Carga");
        bar.setMessage("Ejecutando... ");
        bar.setIcon(R.drawable.gas);
        bar.setCancelable(false);
        bar.show();

        String url;
        if (Identificador.equals(1)){
            url = "http://" + ipEstacion + "/CorpogasService/api/posicionCargas/GetPosicionCargaEmpleadoId/sucursal/" + sucursalId + "/empleado/" + IdUsuario;
        }else{
            url = "http://" + ipEstacion + "/CorpogasService/api/posicionCargas/GetPosicionCargasEstacion/sucursal/" + sucursalId;
        }
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    //lo assignamos a un nuevo ArrayList
                    maintitle = new ArrayList<String>();

                    //lo assignamos a un nuevo ArrayList
                    maintitle1 = new ArrayList<String>();

                    //Lo asignamos a un nuevo ArrayList
                    subtitle = new ArrayList<String>();

                    //La asignamos a un nuevo elemento de ArrayList
                    imgid = new ArrayList<>();

                    permiteventa = new ArrayList<>();

                    numeroOperativa = new ArrayList<>();

                    String posicionCarga,  disponible, estado , pendientdecobro, descripcionOperativa , numeroPosicionCarga, descripcion;

                    JSONObject jsonObject = new JSONObject(response);
                    String correcto = jsonObject.getString("Correcto");
                    String mensaje = jsonObject.getString("Mensaje");
                    String ObjetoRespuesta = jsonObject.getString("ObjetoRespuesta");

//                    JSONObject jsonObject1 = new JSONObject(ObjetoRespuesta);

                    if (correcto.equals("false")){
                        String titulo = "AVISO";
                        Modales modales = new Modales(ProcesoVenta.this);
                        View view1 = modales.MostrarDialogoAlertaAceptar(ProcesoVenta.this,mensaje,titulo);
                        view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                modales.alertDialog.dismiss();
                                Intent intent1 = new Intent(getApplicationContext(), Menu_Principal.class);
                                startActivity(intent1);
                                finish();
                            }
                        });
                    }else{
                        JSONArray control1 = new JSONArray(ObjetoRespuesta);
                        for (int i = 0; i < control1.length(); i++) {
                            JSONObject posiciones = control1.getJSONObject(i);
                             posicionCarga = posiciones.getString("Posicioncarga");
                             disponible = posiciones.getString("Disponible");
                             estado = posiciones.getString("Estado");
                             pendientdecobro = posiciones.getString("PendienteCobro");
                             descripcionOperativa = posiciones.getString("DescripcionOperativa");
                             numeroPosicionCarga = posiciones.getString("NumeroPosicionCarga");
                             descripcion = posiciones.getString("Descripcion");

                            maintitle.add("PC " + numeroPosicionCarga); //carga
                            maintitle1.add(posicionCarga);
                            subtitle.add("Magna  |  Premium  |  Diesel");
                            permiteventa.add(disponible);
                            numeroOperativa.add(descripcionOperativa);
                            imgid.add(R.drawable.gas);

                        }
                        bar.cancel();
                    }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Adaptador adapterProd = new Adaptador(ProcesoVenta.this, maintitle, subtitle, imgid);
                    listPosicionesCarga = (ListView) findViewById(R.id.listPosicionesCarga);
                    listPosicionesCarga.setAdapter(adapterProd);
                    listPosicionesCarga.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                           String ventapermitida = permiteventa.get(position);
                           String operativa = subtitle.get(position);
                           String numerooperativa = numeroOperativa.get(position);
                           String cargaPosicion =  maintitle1.get(position);
                           if (ventapermitida == "true") {
                                ValidaTransaccionActiva(cargaPosicion, numerooperativa, "false");
                            }else{
    //                            //Mensaje para comunicar que no se puede agregar productos
                                try {
                                    //Toast.makeText(posicionFinaliza.this, "No hay Posiciones de Carga para Finalizar Venta", Toast.LENGTH_SHORT).show();
                                    String titulo = "AVISO";
                                    String mensaje = "Venta no permitida en la Posicion de Carga seleccionada";
                                    Modales modales = new Modales(ProcesoVenta.this);
                                    View view1 = modales.MostrarDialogoAlertaAceptar(ProcesoVenta.this,mensaje,titulo);
                                    view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
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
                        }
                    });
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
                        Modales modales = new Modales(ProcesoVenta.this);
                        View view1 = modales.MostrarDialogoAlertaAceptar(ProcesoVenta.this, mensajes, titulo);
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




    private void ValidaTransaccionActiva(String posicionCarga, String numerooperativa, String Estacionjarreo) {
//        if (!Conexion.compruebaConexion(this)) {
//            Toast.makeText(getBaseContext(), "Sin conexión a la red ", Toast.LENGTH_SHORT).show();
//            Intent intent1 = new Intent(getApplicationContext(), Menu_Principal.class);
//
//            startActivity(intent1);
//            finish();
//        } else {

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
                        Intent intent = new Intent(getApplicationContext(), VentaProductos.class); //VentaCombustibleAceites
                        intent.putExtra("numeroEmpleado", IdUsuario);
                        intent.putExtra("posicionCarga", posicionCarga);
                        intent.putExtra("estacionjarreo", Estacionjarreo);
                        startActivity(intent);
                        finish();
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

                        if (banderaConDatos.equals(true)){
                            Toast.makeText(ProcesoVenta.this, "Posicion Ocupada", Toast.LENGTH_SHORT).show();
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
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            int IdOperativa = Integer.parseInt(numerooperativa);
                            if (IdOperativa == 21){ //TarjetaPuntada Redimir
    //                                        imprimirticket(posicionCarga, "REDIMIR", MontoenCanasta.toString());
                            }else{
                                Intent intent = new Intent(getApplicationContext(), VentaCombustibleAceites.class);
                                intent.putExtra("estacionjarreo", Estacionjarreo);
    //                                        intent.putExtra("clavedespachador", ClaveDespachador);
    //                                        intent.putExtra("numeroempleadosucursal", numeroempleado);
                                switch (IdOperativa) {
                                    case 1: //              Operativa normal
                                    case 3:
                                    case 20://Operativa Puntada P
    //                                                intent.putExtra("posicioncarga", posicionCarga);
    //                                                intent.putExtra("IdOperativa", numerooperativa);
    //                                                intent.putExtra("IdUsuario", IdUsuario);
    //                                                intent.putExtra("nombrecompleto", nombreCompletoempleado);
    //                                                intent.putExtra("estacionjarreo", Estacionjarreo);
    //                                                intent.putExtra("cadenarespuesta", CadenaObjetoRespuesta);
    //                                                intent.putExtra("montocanasta", MontoenCanasta.toString());
    //                                                startActivity(intent);
    //                                                finish();
                                        break;
                                    case 2://                Operativa Autoservicio
                                        break;
                                    case 4://                Operativa Tanque Lleno Arillo
                                        break;
                                    case 5://                Operativa Tanque Lleno Tarjeta
    //                                                imprimirticket(posicionCarga, "TLLENO", MontoenCanasta.toString());
                                        break;
                                    case 6://                Operativa Cliente Estacion E
                                        break;
                                    case 7://                Operativa Yena Y
                                        break;
                                    case 8://                Operativa Yena Ñ
                                        break;
                                    case 10://                Operrativa Puntada Q
                                        break;
                                    case 11://                Operativa Desconocida
                                        break;
                                    case 54://                Operativa Predeterminada
                                        break;
                                    case 55://                Operativa Jarreo
                                        break;
                                    case 31://    Autojarreo
    //                                                Intent intent1 = new Intent(getApplicationContext(), formaPagoEstacionJarreo.class); //
    //                                                intent1.putExtra("posicioncarga",posicionCarga);
    //                                                intent1.putExtra("IdOperativa", numerooperativa);
    //                                                intent1.putExtra("IdUsuario", IdUsuario);
    //                                                intent1.putExtra("nombrecompleto", nombreCompletoempleado);
    //                                                intent1.putExtra("montocanasta", MontoenCanasta.toString());
    //                                                intent1.putExtra("numeroempleadosucursal", numeroempleado);
    //                                                intent1.putExtra("posicioncargarid", posicioncargaid);
    //                                                startActivity(intent1);
    //                                                finish();
                                        break;
                                    case 41:// Mercado Pago
    //                                                imprimirticket(posicionCarga, "MERCADOPAGO", MontoenCanasta.toString());
                                        break;
                                    default://                No se encontro ninguna forma de operativa
                                        break;
                                }
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
                        Modales modales = new Modales(ProcesoVenta.this);
                        View view1 = modales.MostrarDialogoAlertaAceptar(ProcesoVenta.this, mensajes, titulo);
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














//            String url = "http://" + ipEstacion + "/CorpogasService/api/ventaProductos/sucursal/" + sucursalId + "/posicionCargaId/" + posicionCarga;
//            // Utilizamos el metodo Post para validar la contraseña
//            StringRequest eventoReq = new StringRequest(Request.Method.GET, url,
//                    new com.android.volley.Response.Listener<String>() {
//                        @Override
//                        public void onResponse(String response) {
//                            Boolean banderaConDatos;
//                            try {
//                                JSONObject jsonObject = new JSONObject(response);
//                                String Correcto = jsonObject.getString("Correcto");
//                                String Mensaje = jsonObject.getString("Mensaje");
//                                String CadenaObjetoRespuesta = jsonObject.getString("ObjetoRespuesta");
//                                if (Correcto.equals("true")) {
//
//                                    if (CadenaObjetoRespuesta.equals("null")) {
//                                        banderaConDatos = false;
//                                    } else {
//                                        if (CadenaObjetoRespuesta.equals("[]")) {
//                                            banderaConDatos = false;
//                                        } else {
//                                            banderaConDatos = true;
//                                        }
//                                    }
//
//                                    Double MontoenCanasta = 0.00;
//                                    try {
//                                        JSONArray ArregloCadenaRespuesta = new JSONArray(CadenaObjetoRespuesta);
//                                        for (int i = 0; i < ArregloCadenaRespuesta.length(); i++) {
//                                            JSONObject ObjetoCadenaRespuesta = ArregloCadenaRespuesta.getJSONObject(i);
//                                            String ImporteTotal = ObjetoCadenaRespuesta.getString("ImporteTotal");
//
//                                            Double aTotal;
//                                            String fTotal;
//                                            aTotal = Double.parseDouble(ImporteTotal);//Double.parseDouble(Monto) * Double.parseDouble(Precio);
//                                            MontoenCanasta = MontoenCanasta + aTotal;
//                                        }
//                                    } catch (JSONException e) {
//                                        e.printStackTrace();
//                                    }
//
//                                    int IdOperativa = Integer.parseInt(numerooperativa);
//                                    if (IdOperativa == 21){ //TarjetaPuntada Redimir
////                                        imprimirticket(posicionCarga, "REDIMIR", MontoenCanasta.toString());
//                                    }else{
//                                        Intent intent = new Intent(getApplicationContext(), FormasPago.class);
//                                        intent.putExtra("estacionjarreo", Estacionjarreo);
////                                        intent.putExtra("clavedespachador", ClaveDespachador);
////                                        intent.putExtra("numeroempleadosucursal", numeroempleado);
//                                        switch (IdOperativa) {
//                                            case 1: //              Operativa normal
//                                            case 3:
//                                            case 20://Operativa Puntada P
////                                                intent.putExtra("posicioncarga", posicionCarga);
////                                                intent.putExtra("IdOperativa", numerooperativa);
////                                                intent.putExtra("IdUsuario", IdUsuario);
////                                                intent.putExtra("nombrecompleto", nombreCompletoempleado);
////                                                intent.putExtra("estacionjarreo", Estacionjarreo);
////                                                intent.putExtra("cadenarespuesta", CadenaObjetoRespuesta);
////                                                intent.putExtra("montocanasta", MontoenCanasta.toString());
////                                                startActivity(intent);
////                                                finish();
//                                                break;
//                                            case 2://                Operativa Autoservicio
//                                                break;
//                                            case 4://                Operativa Tanque Lleno Arillo
//                                                break;
//                                            case 5://                Operativa Tanque Lleno Tarjeta
////                                                imprimirticket(posicionCarga, "TLLENO", MontoenCanasta.toString());
//                                                break;
//                                            case 6://                Operativa Cliente Estacion E
//                                                break;
//                                            case 7://                Operativa Yena Y
//                                                break;
//                                            case 8://                Operativa Yena Ñ
//                                                break;
//                                            case 10://                Operrativa Puntada Q
//                                                break;
//                                            case 11://                Operativa Desconocida
//                                                break;
//                                            case 54://                Operativa Predeterminada
//                                                break;
//                                            case 55://                Operativa Jarreo
//                                                break;
//                                            case 31://    Autojarreo
////                                                Intent intent1 = new Intent(getApplicationContext(), formaPagoEstacionJarreo.class); //
////                                                intent1.putExtra("posicioncarga",posicionCarga);
////                                                intent1.putExtra("IdOperativa", numerooperativa);
////                                                intent1.putExtra("IdUsuario", IdUsuario);
////                                                intent1.putExtra("nombrecompleto", nombreCompletoempleado);
////                                                intent1.putExtra("montocanasta", MontoenCanasta.toString());
////                                                intent1.putExtra("numeroempleadosucursal", numeroempleado);
////                                                intent1.putExtra("posicioncargarid", posicioncargaid);
////                                                startActivity(intent1);
////                                                finish();
//                                                break;
//                                            case 41:// Mercado Pago
////                                                imprimirticket(posicionCarga, "MERCADOPAGO", MontoenCanasta.toString());
//                                                break;
//                                            default://                No se encontro ninguna forma de operativa
//                                                break;
//                                        }
//                                    }
//                                }else{
//                                    String titulo = "AVISO";
//                                    String mensajes = "Error";
//                                    Modales modales = new Modales(ProcesoVenta.this);
//                                    View view1 = modales.MostrarDialogoError(ProcesoVenta.this,Mensaje);
//                                    view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
//                                        @Override
//                                        public void onClick(View view) {
//                                            modales.alertDialog.dismiss();
//                                            Intent intent = new Intent(getApplicationContext(), Menu_Principal.class);
//                                            startActivity(intent);
//                                            finish();
//                                        }
//                                    });
//                                }
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                        //funcion para capturar errores
//                    }, new com.android.volley.Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();
//                }
//            });
//            // Añade la peticion a la cola
//            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
//            eventoReq.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//            requestQueue.add(eventoReq);

    }


    private void solicitadespacho() {
        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("http://" + data.getIpEstacion() + "/CorpogasService/")
                .baseUrl("http://10.0.1.40/CorpogasService_entities_token/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        EndPoints AutorizaDespacho = retrofit.create(EndPoints.class);
        Call<RespuestaApi<Boolean>> call = AutorizaDespacho.getAutorizaDespacho(posicionCargaId,empleadoNumero, "Bearer " +bearerToken);
        call.enqueue(new Callback<RespuestaApi<Boolean>>() {


            @Override
            public void onResponse(Call<RespuestaApi<Boolean>> call, Response<RespuestaApi<Boolean>> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                respuestaApiAutorizaDespacho = response.body();
                boolean correctoautoriza =  respuestaApiAutorizaDespacho.isCorrecto();
                String mensajeautoriza =   respuestaApiAutorizaDespacho.getMensaje();

                if (correctoautoriza ==true) {
                    enviaMunu();
                } else {
                    String titulo = "AVISO";
                    String mensaje = "La posición de carga se encuentra Ocupada";
                    Modales modales = new Modales(ProcesoVenta.this);
                    View view1 = modales.MostrarDialogoAlertaAceptar(ProcesoVenta.this,mensaje,titulo);
                    view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            modales.alertDialog.dismiss();
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<RespuestaApi<Boolean>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void enviaMunu() {
        String titulo = "AVISO";
        String mensaje = "Listo para Iniciar Despacho";
        final Modales modales = new Modales(ProcesoVenta.this);
        View view1 = modales.MostrarDialogoCorrecto(ProcesoVenta.this,mensaje);
        view1.findViewById(R.id.buttonAction).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modales.alertDialog.dismiss();
                Intent intent1 = new Intent(getApplicationContext(), Menu_Principal.class);
                startActivity(intent1);
                finish();
            }
        });
    }

    private void validaPosicionDisponible(long posicionCargaId) {

        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("http://" + data.getIpEstacion() + "/CorpogasService/")
                .baseUrl("http://10.0.1.40/CorpogasService_entities_token/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        EndPoints TicketPendienteCobro = retrofit.create(EndPoints.class);
        Call<RespuestaApi<Boolean>> call = TicketPendienteCobro.getTicketPendienteCobro(sucursalId, posicionCargaId, "Bearer " +bearerToken);
        call.enqueue(new Callback<RespuestaApi<Boolean>>() {


            @Override
            public void onResponse(Call<RespuestaApi<Boolean>> call, Response<RespuestaApi<Boolean>> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                respuestaApiTicketPendienteCobro = response.body();
                boolean correcto = respuestaApiTicketPendienteCobro.isCorrecto(); //p1.getString("ObjetoRespuesta");

                if (correcto == true) {
                    finalizaventa(posicionCargaId);
                } else {
                    //Despacho en proceso
                    String mensaje = respuestaApiTicketPendienteCobro.getMensaje();   //p1.getString("Mensaje");
                    AlertDialog.Builder builder = new AlertDialog.Builder(ProcesoVenta.this);
                    builder.setTitle("Finaliza Venta");
                    builder.setCancelable(false);
                    builder.setMessage("Despacho en proceso: " + mensaje)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent intente = new Intent(getApplicationContext(), Menu_Principal.class);
                                    startActivity(intente);
                                    finish();
                                }
                            }).show();
                }

            }

            @Override
            public void onFailure(Call<RespuestaApi<Boolean>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void finalizaventa(long posicionCargaId) {
        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("http://" + data.getIpEstacion() + "/CorpogasService/")
                .baseUrl("http://10.0.1.40/CorpogasService_entities_token/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        EndPoints postFinalizaVenta = retrofit.create(EndPoints.class);
        Call<RespuestaApi<Transaccion>> call = postFinalizaVenta.getPostFinalizaVenta(sucursalId,posicionCargaId,empleadoNumero, "Bearer " +bearerToken);
        call.enqueue(new Callback<RespuestaApi<Transaccion>>() {


            @Override
            public void onResponse(Call<RespuestaApi<Transaccion>> call, Response<RespuestaApi<Transaccion>> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                respuestaApiTransaccion = response.body();
                String mensaje = respuestaApiTransaccion.getMensaje();   //respuesta.getString("Mensaje");
//                Transaccion objetoRespuesta = respuestaApiTransaccion.getObjetoRespuesta(); //respuesta.getString("ObjetoRespuesta");
                if(respuestaApiTransaccion.getObjetoRespuesta() == null)
                {
                    String titulo = "AVISO";
                    //String mensaje = "No hay Posiciones de Carga para Finalizar Venta";
                    Modales modales = new Modales(ProcesoVenta.this);
                    View view1 = modales.MostrarDialogoAlertaAceptar(ProcesoVenta.this,mensaje,titulo);
                    view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            modales.alertDialog.dismiss();
                            Intent intent1 = new Intent(getApplicationContext(), Menu_Principal.class);
                            startActivity(intent1);
                            finish();
                        }
                    });

                }else
                {
                    String titulo = "AVISO";
                    String mensajes = "Venta Finalizada";
                    final Modales modales = new Modales(ProcesoVenta.this);
                    View view1 = modales.MostrarDialogoCorrecto(ProcesoVenta.this,mensajes);
                    view1.findViewById(R.id.buttonAction).setOnClickListener(new View.OnClickListener() {
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
            public void onFailure(Call<RespuestaApi<Transaccion>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}