package com.corpogas.corpoapp.TanqueLleno.PlanchadoTarjeta;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.corpogas.corpoapp.Conexion;
import com.corpogas.corpoapp.Configuracion.SQLiteBD;
import com.corpogas.corpoapp.Entities.Accesos.AccesoUsuario;
import com.corpogas.corpoapp.Entities.Classes.RespuestaApi;
import com.corpogas.corpoapp.Entities.Estaciones.BodegaProducto;
import com.corpogas.corpoapp.Entities.Estaciones.Isla;
import com.corpogas.corpoapp.Entities.Sucursales.ProductControl;
import com.corpogas.corpoapp.Interfaces.Endpoints.EndPoints;
import com.corpogas.corpoapp.LecturaTarjetas.MonederosElectronicos;
import com.corpogas.corpoapp.Menu_Principal;
import com.corpogas.corpoapp.Modales.Modales;
import com.corpogas.corpoapp.PruebasEndPoint;
import com.corpogas.corpoapp.R;
import com.corpogas.corpoapp.TanqueLleno.ListAdapterConbustiblesTLl;
import com.corpogas.corpoapp.TanqueLleno.eligeCombustibleTL;
import com.corpogas.corpoapp.Token.GlobalToken;

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
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PlanchadoTanqueLleno extends AppCompatActivity implements View.OnClickListener {
    String numerodispositivo ,ClaveTanqueLleno, EstacionId, ipEstacion, numerointernoSucursal,  usuario, SucursalEmpleadoId, nipCliente, nipMd5Cliente, productoid,  nombrecombustible;
    String mensaje = "", correcto = "";
    EditText tvDenominaNumeroClienteTl, tvMontoTL, tvOdometroMontoTl, tvPlacasTl, tvNumeroFolioTl;
    Button btnAceptarProductoTl;
    ListView lstCombustible;
    List<String> ID;
    List<String> NombreProducto;
    List<Double> PrecioProducto;
    List<Long> ClaveProducto;
    List<String> CodigoBarras;
    List<String> ExistenciaProductos;
    List<Long> ProductoId;
    List<Long> CategoriaId;
    Isla respuestaApiPosicionCargaProductosSucursal;
    Long PosicioCargaNumerico, idProducto, sucursalId, IdCombustible;
    long TipoProducto = 1;
    double precioproducto;
    JSONObject datos = new JSONObject();
    JSONArray myArray = new JSONArray();
    TextView txtleyendaTl;
    boolean pasa, banderaDatos;

    String tk1;
    JSONArray datosTarjeta = new JSONArray();
    SQLiteBD db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planchado_tanque_lleno);
        db = new SQLiteBD(getApplicationContext());
        this.setTitle(db.getNombreEstacion() + " ( EST.:" + db.getNumeroEstacion() + ")");
        EstacionId = db.getIdEstacion();
        sucursalId = Long.parseLong(db.getIdSucursal());
        ipEstacion = db.getIpEstacion();
//        numerodispositivo= db.getIdTarjtero();
        numerointernoSucursal = db.getNumeroFranquicia();
        numerodispositivo = db.getIdTarjtero();
        usuario = db.getNumeroEmpleado();  // getUsuarioId();

        init();
        cargaCombustible();
        btnAceptarProductoTl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnAceptarProductoTl.setEnabled(false);
                if(txtleyendaTl.getText() == "PRODUCTO SELECCIONADO"){
                    banderaDatos =true;
                    String mensajeValidacion = "";
                    if (tvDenominaNumeroClienteTl.length() ==0){
                        banderaDatos = false;
                        mensajeValidacion="Digite el No. de Tarjeta";
                    }
                    if (tvDenominaNumeroClienteTl.length()<16){
                        banderaDatos = false;
                        mensajeValidacion="El No. de Tarjeta debe ser de 16 digitos";
                    }
                    if (tvMontoTL.length() ==0){
                        banderaDatos = false;
                        mensajeValidacion="Digite el Monto a Cargar";
                    }
                    if (tvOdometroMontoTl.length() ==0){
                        banderaDatos = false;
                        mensajeValidacion="Digite el Odometro";
                    }
                    if (tvPlacasTl.length() ==0){
                        banderaDatos = false;
                        mensajeValidacion="Digite las Placas";
                    }
                    if (tvNumeroFolioTl.length() ==0){
                        banderaDatos = false;
                        mensajeValidacion="Digite el No. de Autorización";
                    }

                    if (banderaDatos){
                        validaTarjetaPuntada();
//                        EnviarProductos(); MIKEL
                    }else{
                        String titulo = "AVISO";
                        Modales modales = new Modales(PlanchadoTanqueLleno.this);
                        View view1 = modales.MostrarDialogoAlertaAceptar(PlanchadoTanqueLleno.this,mensajeValidacion,titulo);
                        view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                modales.alertDialog.dismiss();
                                btnAceptarProductoTl.setEnabled(true);
                            }
                        });
                    }
                }else{
                    //Toast.makeText(getApplicationContext(),"Ingresa el producto", Toast.LENGTH_LONG).show();
                    String titulo = "AVISO";
                    String mensajes = "Seleccione alguno de los Combustibles"  ;
                    Modales modales = new Modales(PlanchadoTanqueLleno.this);
                    View view1 = modales.MostrarDialogoAlertaAceptar(PlanchadoTanqueLleno.this,mensajes,titulo);
                    view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            modales.alertDialog.dismiss();
                        }
                    });
                }

            }
        });

        tvDenominaNumeroClienteTl.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                tk1 =  tvDenominaNumeroClienteTl.getText().toString()+"00000000";
            }
        });
    }


    private void validaTarjetaPuntada(){

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
                datosTarjeta.put("");
                if (tk1 != null) {
                    datosTarjeta.put(tk1);
                } else {
                    datosTarjeta.put("");
                }
                datosTarjeta.put("");
                jsonObject.put("Pistas", datosTarjeta);
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
                            if (modenerotipo.equals("2") && formaPagoId.equals("11")) { //PUNTADA    modenerotipo.equals("3")
                               EnviarProductos();
                            } else {
                                String titulo = "DEBE PASAR UNA TARJETA PUNTADA";
                                String mensaje = "Tarjeta inválida";
                                Modales modales = new Modales(PlanchadoTanqueLleno.this);
                                View view1 = modales.MostrarDialogoAlertaAceptar(PlanchadoTanqueLleno.this, mensaje, titulo);
                                view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        modales.alertDialog.dismiss();
                                        btnAceptarProductoTl.setEnabled(true);
                                    }
                                });
                            }
                        } else {
                            String titulo = "AVISO";
                            String mensaje = "Tarjeta inválida";
                            Modales modales = new Modales(PlanchadoTanqueLleno.this);
                            View view1 = modales.MostrarDialogoAlertaAceptar(PlanchadoTanqueLleno.this, mensaje, titulo);
                            view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    modales.alertDialog.dismiss();
                                    btnAceptarProductoTl.setEnabled(true);
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
//                    onDestroy();
//                    Intent intente = new Intent(getApplicationContext(), Menu_Principal.class);
//                    startActivity(intente);
                    GlobalToken.errorToken(PlanchadoTanqueLleno.this);
                }
            }) {
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<String, String>();
                    headers.put("Authorization", db.getToken());
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

    private void cargaCombustible(){
        //Declaracion de variables
        ID = new ArrayList<String>();
        NombreProducto = new ArrayList<String>();
        PrecioProducto = new ArrayList<>();
        ClaveProducto = new ArrayList();
        CodigoBarras = new ArrayList();
        ExistenciaProductos = new ArrayList();
        ProductoId = new ArrayList();
        CategoriaId = new ArrayList<>();
        PosicioCargaNumerico = getIntent().getLongExtra("cargaPosicion", 0);
        ClaveTanqueLleno = "";
        List<Integer> imgid;
        imgid = new ArrayList<>();


        if (!Conexion.compruebaConexion(this)) {
            Toast.makeText(getBaseContext(), "Sin conexión a la red ", Toast.LENGTH_SHORT).show();
            Intent intent1 = new Intent(getApplicationContext(), Menu_Principal.class);

            startActivity(intent1);
            finish();
        } else {
            Retrofit retrofit = new Retrofit.Builder()
//                    .baseUrl("http://" + ipEstacion + "/CorpogasService/")
                    .baseUrl("http://10.0.1.40/CorpogasService_entities_token/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            EndPoints PosicionCargaProductosSucursal = retrofit.create(EndPoints.class);
            Call<Isla> call = PosicionCargaProductosSucursal.getPosicionCargaProductosSucursal(sucursalId, PosicioCargaNumerico.toString(), db.getToken());
            call.enqueue(new Callback<Isla>() {


                @Override
                public void onResponse(Call<Isla> call, Response<Isla> response) {
                    if (!response.isSuccessful()) {
                        GlobalToken.errorTokenWithReload(PlanchadoTanqueLleno.this);
                        return;
                    }
                    respuestaApiPosicionCargaProductosSucursal = response.body();

                    for(BodegaProducto item : respuestaApiPosicionCargaProductosSucursal.getBodegaProductos())
                    {
                        long tipoProducto = item.getProducto().getProductCategoryId();
                        if (tipoProducto == TipoProducto){
                            double ExistenciasProductos = item.Existencias;
                            String descripcionProducto = item.getProducto().getLongDescription();
                            String codigoBarras = item.getProducto().Barcode;
                            for(ProductControl item2 : item.getProducto().getProductControls()){
                                precioproducto= item2.Price;
                                idProducto = item2.ProductId;
                            }
                            NombreProducto.add("ID: " + idProducto + "    |     $"+precioproducto); // + "    |    " + IdProductos );
                            ID.add(descripcionProducto);
                            PrecioProducto.add(precioproducto);
                            ClaveProducto.add(idProducto);
                            CodigoBarras.add(codigoBarras);
                            ProductoId.add(idProducto);
                            CategoriaId.add(tipoProducto);

                            switch (idProducto.toString()) {
                                case "1":
                                    imgid.add(R.drawable.magna);
                                    break;
                                case "2":
                                    imgid.add(R.drawable.premium);
                                    break;
                                case "3":
                                    imgid.add(R.drawable.diesel);
                                    break;
                                case "4":
                                    imgid.add(R.drawable.magna);
                                    break;
                                case "5":
                                    imgid.add(R.drawable.premium);
                                    break;
                                case "6":
                                    imgid.add(R.drawable.diesel);
                                    break;
                                case "7":
                                    imgid.add(R.drawable.magna);
                                    break;
                                case "8":
                                    imgid.add(R.drawable.premium);
                                    break;
                                case "9":
                                    imgid.add(R.drawable.diesel);
                                    break;

                                default:
                                    imgid.add(R.drawable.magna);
                            }

                        }
                    }
                    ListAdapterConbustiblesTLl adapterP = new ListAdapterConbustiblesTLl(PlanchadoTanqueLleno.this, ID, NombreProducto, imgid);
                    lstCombustible.setAdapter(adapterP);
//        Agregado  click en la lista
                    lstCombustible.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                            if (tvMontoTL.length() == 0){
                                Toast.makeText(PlanchadoTanqueLleno.this, "Coloque el monto a cargar primero", Toast.LENGTH_SHORT).show();
                            }else{
                                IdCombustible = ClaveProducto.get(position);
                                productoid = String.valueOf(ClaveProducto.get(position));
                                Double Costo = PrecioProducto.get(position);
                                nombrecombustible= ID.get(position);
                                String monto =  tvMontoTL.getText().toString();
                                Double  litros = Double.parseDouble(monto) / Costo;
                                DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
                                simbolos.setDecimalSeparator('.');
                                DecimalFormat df = new DecimalFormat("####.00##",simbolos);
                                df.setMaximumFractionDigits(2);
                                Double pesosD = Double.parseDouble(monto);
                                datos = new JSONObject();
                                try {
                                    datos.put("TipoProducto","1");
                                    datos.put("ProductoId", productoid);
                                    datos.put("NumeroInterno", IdCombustible);
                                    datos.put("Descripcion",nombrecombustible);
                                    datos.put("Cantidad", Double.parseDouble(monto));
                                    datos.put("Precio", Costo);
                                    //datos.put("Precio", litros);
                                    datos.put("Importe", true);
                                    myArray.put(datos);

                                    datos = new JSONObject();
//                                    datos.put("NumeroInternoSucursal", numerointernoSucursal);
                                    datos.put("SucursalId", sucursalId);
                                    datos.put("SucursalEmpleadoId",usuario);
                                    datos.put("PosicionCarga", PosicioCargaNumerico);
                                    datos.put("TarjetaCliente", tvDenominaNumeroClienteTl.getText());
                                    datos.put("Placas", tvPlacasTl.getText());
                                    datos.put("Odometro",tvOdometroMontoTl.getText());
//                                    datos.put( "ClaveTanqueLleno", ClaveTanqueLleno);
//                                    datos.put("Nip",  nipMd5Cliente);
//                                    datos.put("TipoCliente", "1"); //Tipocliente.toString()
                                    datos.put("Productos", myArray);
                                    datos.put("Autorizacion", tvNumeroFolioTl.getText());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                final List<String> main;
                                main = new ArrayList<String>();
                                main.add(ID.get(position));
                                final List<String> sub;
                                sub = new ArrayList<String>();
                                sub.add(NombreProducto.get(position));
                                final List<Integer> img;
                                img = new ArrayList<>();
                                img.add(imgid.get(position));
                                ListAdapterConbustiblesTLl adapterP = new ListAdapterConbustiblesTLl(PlanchadoTanqueLleno.this, main, sub, img);
                                lstCombustible=(ListView)findViewById(R.id.lstCombustibleTl);
                                lstCombustible.setAdapter(adapterP);
                                lstCombustible.setEnabled(false);
                                txtleyendaTl.setText("PRODUCTO SELECCIONADO");
                            }
                        }
                    });
                }

                @Override
                public void onFailure(Call<Isla> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void init() {
        tvDenominaNumeroClienteTl = (EditText) findViewById(R.id.tvDenominaNumeroClienteTl);
        tvMontoTL = (EditText) findViewById(R.id.tvMontoTL);
        tvOdometroMontoTl = (EditText) findViewById(R.id.tvOdometroMontoTl);
        tvPlacasTl = (EditText) findViewById(R.id.tvPlacasTl);
        tvNumeroFolioTl = (EditText) findViewById(R.id.tvNumeroFolioTl);
        btnAceptarProductoTl = (Button) findViewById(R.id.btnAceptarProductoTl);
        lstCombustible = (ListView) findViewById(R.id.lstCombustibleTl);
        txtleyendaTl = (TextView)  findViewById(R.id.txtleyendaTl);
    }


    @Override
    public void onClick(View view) {

    }

    private void EnviarProductos() {
        if (!Conexion.compruebaConexion(this)) {
            Toast.makeText(getBaseContext(), "Sin conexión a la red ", Toast.LENGTH_SHORT).show();
            Intent intent1 = new Intent(getApplicationContext(), Menu_Principal.class);
            startActivity(intent1);
            finish();
        } else {
//            String URL = "http://"+ipEstacion+"/CorpogasService/api/tanqueLleno/EnviarProductosAutorizacion";
            String URL = "http://"+ipEstacion+"/CorpogasService_entities_token/api/tanqueLleno/EnviarProductosAutorizacion";

            RequestQueue queue = Volley.newRequestQueue(this);
            JsonObjectRequest request_json = new JsonObjectRequest(Request.Method.POST, URL, datos, new com.android.volley.Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
//                    pasa = true;

                    try {
                        String validacion = response.getString("Correcto");
                        String transaccion, folios;
                        transaccion = response.getString("TransaccionId");
                        folios = response.getString("Folio");
                        if (validacion == "true") {
//                        agregarcombustible.setVisibility(View.INVISIBLE);
//                        imprimirTicket.setVisibility(View.VISIBLE);

                            String titulo = "TARJETA TANQUELLENO";
                            String mensaje = "La petición se ha completado correctamente";
                            Modales modales = new Modales(PlanchadoTanqueLleno.this);
                            View view1 = modales.MostrarDialogoCorrecto(PlanchadoTanqueLleno.this,mensaje);
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
                            String estado = response.getString("Mensaje");
                            String titulo = "AVISO TANQUELLENO";
                            String mensaje = "" + estado;
                            Modales modales = new Modales(PlanchadoTanqueLleno.this);
                            View view1 = modales.MostrarDialogoAlertaAceptar(PlanchadoTanqueLleno.this,mensaje,titulo);
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
                        e.printStackTrace();
                    }


                }
            }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    GlobalToken.errorToken(PlanchadoTanqueLleno.this);
//                    String error1 = error.networkResponse.data.toString();
////                    Toast.makeText(PlanchadoTanqueLleno.this, "error: "+error1, Toast.LENGTH_SHORT).show();
//                    String titulo = "TARJETA TANQUELLENO";
//                    String mensaje = "Fallo en la conexión con consola";
//                    Modales modales = new Modales(PlanchadoTanqueLleno.this);
//                    View view1 = modales.MostrarDialogoAlertaAceptar(PlanchadoTanqueLleno.this,mensaje,titulo);
//                    view1.findViewById(R.id.buttonAction).setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            modales.alertDialog.dismiss();
//                            pasa = false;                        }
//                    });
                }
            }) {
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<String, String>();
                    headers.put("Authorization", db.getToken());
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
            request_json.setRetryPolicy(new DefaultRetryPolicy(
                    120000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            queue.add(request_json);
        }
    }

    private void EnviarProductosPredeterminado() {
        if (!Conexion.compruebaConexion(this)) {
            Toast.makeText(getBaseContext(), "Sin conexión a la red ", Toast.LENGTH_SHORT).show();
            Intent intent1 = new Intent(getApplicationContext(), Menu_Principal.class);
            startActivity(intent1);
            finish();
        } else {
            String url = "http://" + ipEstacion + "/CorpogasService/api/ventaProductos/GuardaProductos/sucursal/" + sucursalId + "/origen/" + numerodispositivo + "/usuario/" + usuario + "/posicionCarga/" + PosicioCargaNumerico;
            RequestQueue queue = Volley.newRequestQueue(this);

            JsonArrayRequest request_json = new JsonArrayRequest(Request.Method.POST, url, myArray,
                    new com.android.volley.Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            //Get Final response
                            if (correcto.equals("true")) {
                                String titulo = "AVISO";
                                String mensajes = "Listo para Iniciar Despacho";
                                final Modales modales = new Modales(PlanchadoTanqueLleno.this);

                                View view1 = modales.MostrarDialogoCorrecto(PlanchadoTanqueLleno.this, mensajes);
                                view1.findViewById(R.id.buttonAction).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        modales.alertDialog.dismiss();
                                        Intent intent1 = new Intent(getApplicationContext(), Menu_Principal.class);
                                        startActivity(intent1);
                                        finish();
                                    }
                                });
                            } else {
                                try {
                                    String titulo = "AVISO";
                                    final Modales modales = new Modales(PlanchadoTanqueLleno.this);
                                    View view1 = modales.MostrarDialogoAlertaAceptar(PlanchadoTanqueLleno.this, mensaje, titulo);
                                    view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
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

                        }
                    }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    //VolleyLog.e("Error: ", volleyError.getMessage());
                    String algo = new String(volleyError.networkResponse.data);
                    try {
                        //creamos un json Object del String algo
                        JSONObject errorCaptado = new JSONObject(algo);
                        //Obtenemos el elemento ExceptionMesage del errro enviado
                        String errorMensaje = errorCaptado.getString("ExceptionMessage");
                        try {
                            String titulo = "AVISO";
                            String mensajes = "" + errorMensaje;
                            Modales modales = new Modales(PlanchadoTanqueLleno.this);
                            View view1 = modales.MostrarDialogoAlertaAceptar(PlanchadoTanqueLleno.this, mensajes, titulo);
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

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<String, String>();
                    // Add headers
                    return headers;
                }

                //Important part to convert response to JSON Array Again
                @Override
                protected com.android.volley.Response<JSONArray> parseNetworkResponse(NetworkResponse response) {
                    String responseString;
                    JSONArray array = new JSONArray();
                    if (response != null) {

                        try {
                            responseString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                            //transaccionId =

                            JSONObject respuesta = new JSONObject(responseString);
                            correcto = respuesta.getString("Correcto");
                            mensaje = respuesta.getString("Mensaje");
                            //String objetoRespuesta = respuesta.getString("ObjetoRespuesta");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    //return array;

                    return com.android.volley.Response.success(myArray, HttpHeaderParser.parseCacheHeaders(response));
                }
            };


            request_json.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            queue.add(request_json);
        }
    }
}