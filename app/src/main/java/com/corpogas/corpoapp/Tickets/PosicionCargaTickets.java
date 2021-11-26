package com.corpogas.corpoapp.Tickets;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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
import com.corpogas.corpoapp.Adapters.RVAdapter;
import com.corpogas.corpoapp.Conexion;
import com.corpogas.corpoapp.Configuracion.SQLiteBD;
import com.corpogas.corpoapp.Entities.Accesos.AccesoUsuario;
import com.corpogas.corpoapp.Entities.Accesos.Control;
import com.corpogas.corpoapp.Entities.Accesos.Posicion;
import com.corpogas.corpoapp.Entities.Classes.RecyclerViewHeaders;
import com.corpogas.corpoapp.Entities.Classes.RespuestaApi;
import com.corpogas.corpoapp.Entities.Common.ProductoTarjetero;
import com.corpogas.corpoapp.Entities.Ventas.Transaccion;
import com.corpogas.corpoapp.Interfaces.Endpoints.EndPoints;
import com.corpogas.corpoapp.Menu_Principal;
import com.corpogas.corpoapp.Modales.Modales;
import com.corpogas.corpoapp.Productos.MostrarCarritoTransacciones;
import com.corpogas.corpoapp.Puntada.PosicionPuntadaRedimir;
import com.corpogas.corpoapp.R;
import com.corpogas.corpoapp.VentaCombustible.DiferentesFormasPago;
import com.corpogas.corpoapp.VentaCombustible.FormasPago;
import com.corpogas.corpoapp.VentaCombustible.FormasPagoReordenado;
import com.corpogas.corpoapp.VentaCombustible.ProcesoVenta;

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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PosicionCargaTickets extends AppCompatActivity {
    RecyclerView rcvPosicionCargaTicket;
    String EstacionId,  ipEstacion, numeroTarjetero, lugarproviene, usuario, clave, operativa, jarreo, numeroEmpleadoReimprime;
    long posicionCargaId;
    long numeroOperativa;
    long cargaNumeroInterno;
    long usuarioid;
    long numeroInternoPosicionCarga;
    long sucursalId ;
    Integer posicionEmpleado = 1;
    Integer posicionSucursal = 2;
    Integer posicionReimpresion = 3;
    Boolean banderaposicionCarga, pendientecobro;
    SQLiteBD data;
    RespuestaApi<AccesoUsuario> accesoUsuario;
    List<RecyclerViewHeaders> lProcesoVenta;
    RespuestaApi<List<ProductoTarjetero>> respuestaApiProductoTarjetero;
    RespuestaApi<Boolean> respuestaApiAutorizaDespacho;
    RespuestaApi<Boolean> respuestaApiTicketPendienteCobro;
    RespuestaApi<Transaccion> respuestaApiTransaccion;
    ProgressDialog bar;
    Button btnCargarTodasPosiciones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posicion_carga_tickets);
        init();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvPosicionCargaTicket.setLayoutManager(linearLayoutManager);
        rcvPosicionCargaTicket.setHasFixedSize(true);

        if (lugarproviene.equals("Imprimir")){
            //posicionesCarga();
            todasposiciones(posicionEmpleado);
        }else{
            if (lugarproviene.equals("Reimprimir")){
                todasposiciones(posicionReimpresion);
            }else{
                todasposiciones(posicionSucursal);
            }
        }
    }



    private void todasposiciones(Integer Identificador){
            bar = new ProgressDialog(PosicionCargaTickets.this);
            bar.setTitle("Cargando Posiciones de Carga");
            bar.setMessage("Ejecutando... ");
            bar.setIcon(R.drawable.gas);
            bar.setCancelable(false);
            bar.show();

            String url;
            if (Identificador.equals(posicionEmpleado)){
                url = "http://" + ipEstacion + "/CorpogasService/api/posicionCargas/GetPosicionCargaPendienteCobroPorEmpleado/sucursal/" + sucursalId + "/empleado/"+usuario;
            }else{
                url = "http://" + ipEstacion + "/CorpogasService/api/posicionCargas/GetPosicionCargasPendienteCobro/sucursal/" + sucursalId;
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
                            Modales modales = new Modales(PosicionCargaTickets.this);
                            View view1 = modales.MostrarDialogoAlertaAceptar(PosicionCargaTickets.this,mensaje,titulo);
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
                            lProcesoVenta = new ArrayList<>();
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
                                jarreo = posiciones.getString("EstacionJarreo");
                                numeroOperativa = posiciones.getLong("Operativa");
                                Boolean banderacarga ;
                                banderacarga = false;
                                if (lugarproviene.equals("Reimprimir")){
                                    String titulo = "PC " + posicionCargaNumeroInterno;
                                    String subtitulo = "";//
                                    //    subtitulo = "Magna  |  Premium  |  Diesel";
                                    subtitulo =descripcionoperativa;//
                                    lProcesoVenta.add(new RecyclerViewHeaders(titulo,subtitulo,R.drawable.gas,posicionCargaId,numeroOperativa, jarreo));//
                                    banderaposicionCarga = true;
                                }else{
                                    if (pocioncargapendientecobro == true){
                                        String titulo = "PC " + posicionCargaNumeroInterno;
                                        String subtitulo = "";//
                                        //    subtitulo = "Magna  |  Premium  |  Diesel";
                                        subtitulo =descripcionoperativa;//
                                        lProcesoVenta.add(new RecyclerViewHeaders(titulo,subtitulo,R.drawable.gas,posicionCargaId,numeroOperativa, jarreo));//
                                        banderaposicionCarga = true;
                                    }else{
                                        banderacarga = false;
                                    }
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
                                                    lProcesoVenta.add(new RecyclerViewHeaders(titulo,subtitulo,R.drawable.gas,posicionCargaId,numeroOperativa, jarreo));//
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
                                Modales modales = new Modales(PosicionCargaTickets.this);
                                View view1 = modales.MostrarDialogoAlertaAceptar(PosicionCargaTickets.this,mensajes,titulo);
                                view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        modales.alertDialog.dismiss();
                                        Intent intent1 = new Intent(getApplicationContext(), Menu_Principal.class);
                                        startActivity(intent1);
                                        finish();
                                        bar.cancel();
//                                        initializeAdapter();
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
                    bar.cancel();
                    String algo = new String(error.networkResponse.data);
                    try {
                        //creamos un json Object del String algo
                        JSONObject errorCaptado = new JSONObject(algo);
                        //Obtenemos el elemento ExceptionMesage del errro enviado
                        String errorMensaje = errorCaptado.getString("ExceptionMessage");
                        try {
                            String titulo = "Jarreo";
                            String mensajes = errorMensaje;
                            Modales modales = new Modales(PosicionCargaTickets.this);
                            View view1 = modales.MostrarDialogoAlertaAceptar(PosicionCargaTickets.this, mensajes, titulo);
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




    private void posicionesCarga() {
        bar = new ProgressDialog(PosicionCargaTickets.this);
        bar.setTitle("Buscando Posiciones de Carga");
        bar.setMessage("Ejecutando... ");
        bar.setIcon(R.drawable.gas);
        bar.setCancelable(false);
        bar.show();

        String url;
        url = "http://" + ipEstacion + "/CorpogasService/api/accesoUsuarios/sucursal/" + sucursalId + "/clave/"+ data.getClave();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    String ObjetoRespuesta = jsonObject.getString("ObjetoRespuesta");
                    JSONObject estacionjarreo = new JSONObject(ObjetoRespuesta);
                    String jarreo = estacionjarreo.getString("EstacionJarreo");
                    if (ObjetoRespuesta.equals("null")) {
                        String mensaje = jsonObject.getString("Mensaje");
                        String titulo = "AVISO";
                        Modales modales = new Modales(PosicionCargaTickets.this);
                        View view1 = modales.MostrarDialogoAlertaAceptar(PosicionCargaTickets.this,mensaje,titulo);
                        view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                modales.alertDialog.dismiss();
                                Intent intent = new Intent(getApplicationContext(), Menu_Principal.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                    }else{
                        lProcesoVenta = new ArrayList<>();
                        banderaposicionCarga= false;
                        boolean banderaEntro = false;
                        JSONObject jsonObject1 = new JSONObject(ObjetoRespuesta);
                        String control = jsonObject1.getString("Controles");

                        JSONArray control1 = new JSONArray(control);
                        for (int i = 0; i < control1.length(); i++) {
                            JSONObject posiciones = control1.getJSONObject(i);
                            String posi = posiciones.getString("Posiciones");

                            JSONArray mangue = new JSONArray(posi);
                            for (int j = 0; j < mangue.length(); j++) {
                                JSONObject res = mangue.getJSONObject(j);
                                posicionCargaId = res.getLong("PosicionCargaId");
                                long posicionCargaNumeroInterno = res.getLong("NumeroInterno");

                                boolean pocioncargadisponible = res.getBoolean("Disponible");
//                                String estado = res.getString("Estado");
                                boolean pocioncargapendientecobro = res.getBoolean("PendienteCobro");
                                String descripcionoperativa = res.getString("DescripcionOperativa");
                                String descripcion = res.getString("Descripcion");
                                numeroOperativa = res.getLong("Operativa");
                                Boolean banderacarga ;
                                if (pocioncargapendientecobro==true) {
                                    String titulo = "PC " + posicionCargaNumeroInterno;
                                    String subtitulo = "";//
                                    subtitulo =descripcionoperativa;//
                                    lProcesoVenta.add(new RecyclerViewHeaders(titulo,subtitulo,R.drawable.gas,posicionCargaId,numeroOperativa, jarreo));//
                                    banderaposicionCarga = true;
                                }
                            }
                        }
                        bar.cancel();
                        //AQUI VA EL ADAPTADOR
                        if (banderaposicionCarga.equals(false)){
//                        //Toast.makeText(posicionFinaliza.this, "No hay Posiciones de Carga para Finalizar Venta", Toast.LENGTH_SHORT).show();
                            String titulo = "AVISO";
                            String mensajes="";
                            mensajes = "No hay posiciones de carga Pendiente de Cobro";
                            Modales modales = new Modales(PosicionCargaTickets.this);
                            View view1 = modales.MostrarDialogoAlertaAceptar(PosicionCargaTickets.this,mensajes,titulo);
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
                    Toast.makeText(PosicionCargaTickets.this, e.toString(), Toast.LENGTH_SHORT).show();
                    bar.cancel();
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
                        Modales modales = new Modales(PosicionCargaTickets.this);
                        View view1 = modales.MostrarDialogoAlertaAceptar(PosicionCargaTickets.this, mensajes, titulo);
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
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(90000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }




    private void initializeAdapter() {
        RVAdapter adapter = new RVAdapter(lProcesoVenta);
        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                posicionCargaId=lProcesoVenta.get(rcvPosicionCargaTicket.getChildAdapterPosition(v)).getPosicionCargaId();
                cargaNumeroInterno = lProcesoVenta.get(rcvPosicionCargaTicket.getChildAdapterPosition(v)).getPosicioncarganumerointerno();
                operativa = lProcesoVenta.get(rcvPosicionCargaTicket.getChildAdapterPosition(v)).getSubtitulo();
                jarreo = lProcesoVenta.get(rcvPosicionCargaTicket.getChildAdapterPosition(v)).getJarreo();
                String obtieneposicion = lProcesoVenta.get(rcvPosicionCargaTicket.getChildAdapterPosition(v)).getTitulo();
                numeroInternoPosicionCarga = Long.parseLong(obtieneposicion.substring(obtieneposicion.indexOf(" ")+1, obtieneposicion.length()));
                if (lugarproviene.equals("Imprimir")){
                    //Valido si hay una transaccion viva en la base de datos de la HH que haya cargado para pago mixto
                    if (data.getresponseFPDCount(posicionCargaId)>0 ){
                        String response="";
                        response = data.getresponseFPD(Integer.parseInt(data.getformapagoid()));
                        if (response.length()>0){
                            Intent intentDiferente = new Intent(getApplicationContext(), DiferentesFormasPago.class);
                            intentDiferente.putExtra("Enviadodesde", "posicioncarga");
                            startActivity(intentDiferente);
                            finish();
                        }else{
                            ValidaTransaccionActiva();
                        }
                    }else{
                        if (data.getFormaPagoFPDCobrado()>0){
                            String PCarga = "";
                            PCarga = data.getNumeroInternoPCDFP();//     getPosicionCargaDFPCobrado();
                            String titulo = "Transaccion Ocupada";
                            String mensajes = "Cierre la venta que ya tiene cargado un pago con tarjeta en la posición: "+ PCarga;
                            Modales modales = new Modales(PosicionCargaTickets.this);
                            View view1 = modales.MostrarDialogoAlertaAceptar(PosicionCargaTickets.this, mensajes, titulo);
                            view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    modales.alertDialog.dismiss();
                                }
                            });
                        }else{
                            ValidaTransaccionActiva();
                        }
                    }
                }else{
                    imprimirticket(Long.toString(posicionCargaId), "1", "0" );
                }

            }
        });

        rcvPosicionCargaTicket.setAdapter(adapter);
    }


    private void ValidaTransaccionActiva() {
        if (!Conexion.compruebaConexion(this)) {
            Toast.makeText(getBaseContext(), "Sin conexión a la red ", Toast.LENGTH_SHORT).show();
            Intent intent1 = new Intent(getApplicationContext(), Menu_Principal.class);

            startActivity(intent1);
            finish();
        } else {
            String url = "http://" + ipEstacion + "/CorpogasService/api/ventaProductos/sucursal/" + sucursalId + "/posicionCargaId/" + posicionCargaId;
            // Utilizamos el metodo Post para validar la contraseña
            StringRequest eventoReq = new StringRequest(Request.Method.GET, url,
                    new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Boolean banderaConDatos;
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String Correcto = jsonObject.getString("Correcto");
                                String Mensaje = jsonObject.getString("Mensaje");
                                String CadenaObjetoRespuesta = jsonObject.getString("ObjetoRespuesta");
                                if (Correcto.equals("true")) {

                                    if (CadenaObjetoRespuesta.equals("null")) {
                                        banderaConDatos = false;
                                    } else {
                                        if (CadenaObjetoRespuesta.equals("[]")) {
                                            banderaConDatos = false;
                                        } else {
                                            banderaConDatos = true;
                                        }
                                    }

                                    Double MontoenCanasta = 0.00;
                                    try {
                                        JSONArray ArregloCadenaRespuesta = new JSONArray(CadenaObjetoRespuesta);
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

                                    if (MontoenCanasta.equals(0.0)){
                                        String titulo = "Transaccion Ocupada";
                                        String mensajes = "Problemas con la posicion de carga: "+ numeroInternoPosicionCarga;
                                        Modales modales = new Modales(PosicionCargaTickets.this);
                                        View view1 = modales.MostrarDialogoAlertaAceptar(PosicionCargaTickets.this, mensajes, titulo);
                                        view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                modales.alertDialog.dismiss();
                                            }
                                        });
                                    }else{

                                    String IdOperativa = String.valueOf(cargaNumeroInterno);
                                    if (IdOperativa.equals("21")){ //TarjetaPuntada Redimir
                                        imprimirticket(String.valueOf(posicionCargaId), "REDIMIR", MontoenCanasta.toString());
                                    }else{
                                        Intent intent = new Intent(getApplicationContext(), FormasPagoReordenado.class); // FormasPago
                                        intent.putExtra("estacionjarreo", jarreo);
                                        intent.putExtra("clavedespachador", "ClaveDespachador");
                                        intent.putExtra("numeroempleadosucursal", "numeroempleado");
                                        intent.putExtra("lugarProviene", lugarproviene);
                                        intent.putExtra("numeroTarjeta", "");
                                        intent.putExtra("descuento", 0);
                                        intent.putExtra("nipCliente", "");

                                        switch (IdOperativa) {
                                            case "1": //              Operativa normal
                                            case "3":
                                            case "20"://Operativa Puntada P
                                                intent.putExtra("posicionCarga", Long.toString(posicionCargaId));  //Long.toString(longNumber)
                                                intent.putExtra("IdOperativa", IdOperativa);
                                                intent.putExtra("IdUsuario", "IdUsuario");
                                                intent.putExtra("nombrecompleto", "nombreCompletoempleado");
                                                intent.putExtra("cadenarespuesta", CadenaObjetoRespuesta);
                                                intent.putExtra("montoenCanasta", MontoenCanasta);
                                                intent.putExtra("idposicionCarga", cargaNumeroInterno);
                                                startActivity(intent);
                                                finish();
                                                break;
                                            case "2"://                Operativa Autoservicio
                                                break;
                                            case "4"://                Operativa Tanque Lleno Arillo
                                                break;
                                            case "5"://                Operativa Tanque Lleno Tarjeta
                                                imprimirticket(String.valueOf(posicionCargaId), "TLLENO", MontoenCanasta.toString());
                                                break;
                                            case "6"://                Operativa TANQUELLENO ARILLOS
                                                imprimirticket(String.valueOf(posicionCargaId), "TLLENOARILLOS", MontoenCanasta.toString());
                                                break;
                                            case "7"://                Operativa Yena Y
                                                break;
                                            case "8"://                Operativa Yena Ñ
                                                break;
                                            case "10"://                Operrativa Puntada Q
                                                break;
                                            case "11"://                Operativa Desconocida
                                                break;
                                            case "16"://                Yena Redencion
                                                imprimirticket(String.valueOf(posicionCargaId), "YENAREDENCION", MontoenCanasta.toString());
                                            case "54"://                Operativa Predeterminada
                                                break;
                                            case "55"://                Operativa Jarreo
                                                break;
                                            case "31"://    Autojarreo
//                                                imprimirticket(String.valueOf(posicionCargaId), "AUTOJARREO", MontoenCanasta.toString());
                                                Intent intent1 = new Intent(getApplicationContext(), FormaPagoAutojarreo.class);
                                                intent1.putExtra("posicioncarga", posicionCargaId);
                                                intent1.putExtra("posicioncargainterno", numeroInternoPosicionCarga);
                                                intent1.putExtra("montocanasta", MontoenCanasta.toString());

                                                startActivity(intent1);
                                                finish();
                                                break;
                                            case "41":// Mercado Pago
                                                imprimirticket(String.valueOf(posicionCargaId), "MERCADOPAGO", MontoenCanasta.toString());
                                                break;
                                            default://                No se encontro ninguna forma de operativa
                                                String titulo = "AVISO";
                                                String mensaje = "Directiva no Identificada";
                                                Modales modales = new Modales(PosicionCargaTickets.this);
                                                View view1 = modales.MostrarDialogoError(PosicionCargaTickets.this, mensaje);
                                                view1.findViewById(R.id.buttonAction).setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        Intent intent = new Intent(getApplicationContext(), Menu_Principal.class);
                                                        startActivity(intent);
                                                        finish();
                                                        modales.alertDialog.dismiss();
                                                    }
                                                });
                                                break;
                                        }
                                    }
                                    }
                                }else{
                                    String titulo = "AVISO";
                                    String mensajes = "Error";
                                    Modales modales = new Modales(PosicionCargaTickets.this);
                                    View view1 = modales.MostrarDialogoError(PosicionCargaTickets.this,Mensaje);
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
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        //funcion para capturar errores
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();
                    bar.cancel();
                }
            });
            // Añade la peticion a la cola
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            eventoReq.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(eventoReq);

        }
    }

    private void imprimirticket(String carga, String TipoTarjeta, String MontoCanasta) {
        JSONObject FormasPagoObjecto = new JSONObject();
        JSONArray FormasPagoArreglo = new JSONArray();
        String valor;
        if (TipoTarjeta == "AUTOJARREO"){
            valor = "92";
        }else{
            if (TipoTarjeta == "YENAREDENCION"){
                valor = "16";
            }else{
                if (TipoTarjeta == "TLLENO"  | TipoTarjeta == "TLLENOARILLOS") {
                    valor = "11";
                }else{
                    if (TipoTarjeta == "MERCADOPAGO") {
                        valor = "14";
                    }else{ //Puntada Redimir
                        valor = "12";
                    }
                }
            }
        }

        if (lugarproviene.equals("Reimprimir")){
        }else{
            try {
                FormasPagoObjecto.put("Id", valor);
                FormasPagoObjecto.put("Importe", Double.parseDouble(MontoCanasta));
                FormasPagoArreglo.put(FormasPagoObjecto);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        JSONObject datos = new JSONObject();
        String url = "http://" + ipEstacion + "/CorpogasService/api/tickets/generar";
        RequestQueue queue = Volley.newRequestQueue(this);
        try {
            datos.put("PosicionCargaId", carga);
            if (lugarproviene.equals("Reimprimir")){
                datos.put("IdUsuario", numeroEmpleadoReimprime); // getIntent().getStringExtra("numeroEmpleado")
            }else{
                datos.put("IdUsuario", data.getNumeroEmpleado());
            }
            datos.put("SucursalId", sucursalId);
            datos.put("IdFormasPago", FormasPagoArreglo);
            datos.put("ConfiguracionAplicacionId", data.getIdTarjtero());

        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest request_json = new JsonObjectRequest(Request.Method.POST, url,  datos, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                String detalle = null;
                try {
                    detalle = response.getString("Detalle");
                    if (detalle.equals("null")) {
                        String estado1 = response.getString("Resultado");
                        JSONObject descripcion = new JSONObject(estado1);
                        String estado = descripcion.getString("Descripcion");
                        try {
                            String titulo = "AVISO";
                            String mensaje = estado;
                            Modales modales = new Modales(PosicionCargaTickets.this);
                            View view1 = modales.MostrarDialogoError(PosicionCargaTickets.this, mensaje);
                            view1.findViewById(R.id.buttonAction).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(getApplicationContext(), Menu_Principal.class);
                                    startActivity(intent);
                                    finish();
                                    modales.alertDialog.dismiss();
                                }
                            });
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    } else {
                        String titulo = "AVISO";
                        Modales modales = new Modales(PosicionCargaTickets.this);
                        View view1 = modales.MostrarDialogoCorrecto(PosicionCargaTickets.this, "Ticket enviado a impresora central");
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

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PosicionCargaTickets.this, "error", Toast.LENGTH_SHORT).show();

            }
        }){
            public Map<String,String> getHeaders() throws AuthFailureError {
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


    private void init() {
        rcvPosicionCargaTicket = (RecyclerView)findViewById(R.id.rcvPosicionCargaTicket);
        data = new SQLiteBD(getApplicationContext());
        this.setTitle(data.getNombreEstacion() + " ( EST.:" + data.getNumeroEstacion() + ")");
        EstacionId = data.getIdEstacion();
        sucursalId = Long.parseLong(data.getIdSucursal());
        ipEstacion = data.getIpEstacion();
        lugarproviene = getIntent().getStringExtra("lugarProviene");
        usuarioid = getIntent().getLongExtra("IdUsuario",0);
        usuario = data.getNumeroEmpleado(); //getIntent().getStringExtra("clave");
        numeroEmpleadoReimprime = getIntent().getStringExtra("numeroEmpleado");
        btnCargarTodasPosiciones = (Button) findViewById(R.id.btnCargarTodasPosiciones);
        btnCargarTodasPosiciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                todasposiciones(2);
            }
        });

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getBaseContext(), Menu_Principal.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
        finish();
    }


}