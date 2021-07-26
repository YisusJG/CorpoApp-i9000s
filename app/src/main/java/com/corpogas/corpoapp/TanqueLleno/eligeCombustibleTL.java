package com.corpogas.corpoapp.TanqueLleno;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.corpogas.corpoapp.Conexion;
import com.corpogas.corpoapp.Configuracion.SQLiteBD;
import com.corpogas.corpoapp.Entities.Accesos.AccesoUsuario;
import com.corpogas.corpoapp.Entities.Classes.RespuestaApi;
import com.corpogas.corpoapp.Entities.Estaciones.BodegaProducto;
import com.corpogas.corpoapp.Entities.Estaciones.Isla;
import com.corpogas.corpoapp.Entities.Sucursales.ProductControl;
import com.corpogas.corpoapp.Entities.TanqueLleno.RespuestaIniAuto;
import com.corpogas.corpoapp.Entities.Tickets.TicketFormaPago;
import com.corpogas.corpoapp.Menu_Principal;
import com.corpogas.corpoapp.Modales.Modales;
import com.corpogas.corpoapp.R;
import com.corpogas.corpoapp.Interfaces.Endpoints.EndPoints;

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
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class eligeCombustibleTL extends AppCompatActivity {
    SQLiteBD db;
    ListView list;
    boolean pasa;
    String TipoSeleccionado, usuario, posicion, cantidadtotal, tipoSelecccionado, banderatiposeleccion;
    EditText Cantidad, Pesos;
    TextView EtiquetaCantidad, leyenda;
    Long IdCombustible;
    String cs;
    String numneroInterno;
    String descripcion;
    String precio;
    Long IdCombus;
    String Costo;
    Button Enviar;
    String EstacionId,  ipEstacion,numerodispositivo ;
    Long sucursalId;
    JSONObject datos = new JSONObject();
    JSONArray myArray = new JSONArray();
    String tipoproducto, productoid, numnerointerno, nombrecombustible, cantidad, preciounitario, tipopeticion, combustible, numerointernoSucursal ;
    Boolean bandera;
    String placas ,odometro, NumeroInternoEstacion, SucursalEmpleadoId, NumeroDeTarjeta, ClaveTanqueLleno,  transaccionid, folio, nipCliente, nipMd5Cliente;
    Integer Tipocliente;
    String PosicioDeCarga;
    long TipoProducto = 1;
    JSONArray array1 = new JSONArray();
    JSONArray myArrayVer = new JSONArray();

    List<String> ID;
    List<String> NombreProducto;
    List<Double> PrecioProducto;
    List<Long> ClaveProducto;
    List<String> CodigoBarras;
    List<String> ExistenciaProductos;
    List<Long> ProductoId;
    List<Long> CategoriaId;
    //List<Long> tipoprod;

    Isla respuestaApiPosicionCargaProductosSucursal;

    double precioproducto;
    Long idProducto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elige_combustible_t_l);

        SQLiteBD db = new SQLiteBD(getApplicationContext());
        this.setTitle(db.getNombreEstacion() + " ( EST.:" + db.getNumeroEstacion() + ")");
        EstacionId = db.getIdEstacion();
        sucursalId = Long.parseLong(db.getIdSucursal());
        ipEstacion = db.getIpEstacion();
//        numerodispositivo= db.getIdTarjtero();
        numerointernoSucursal = db.getNumeroFranquicia();

        Cantidad = findViewById(R.id.cantidad);
        EtiquetaCantidad = findViewById(R.id.lblcantidad);
        Pesos = findViewById(R.id.pesos);
        leyenda=findViewById(R.id.txtleyenda);
        usuario = getIntent().getStringExtra("usuario");
        posicion = getIntent().getStringExtra("posicion");
        cantidadtotal = getIntent().getStringExtra("Cantidad");
        tipoSelecccionado = getIntent().getStringExtra("TipoSeleccionado");

        placas = getIntent().getStringExtra("placas");
        odometro = getIntent().getStringExtra("odometro");
        NumeroInternoEstacion = getIntent().getStringExtra("NumeroInternoEstacion");
        SucursalEmpleadoId = getIntent().getStringExtra("SucursalEmpleadoId");
        PosicioDeCarga = getIntent().getStringExtra("PosicioDeCarga");
        NumeroDeTarjeta = getIntent().getStringExtra("NumeroDeTarjeta");
        ClaveTanqueLleno = getIntent().getStringExtra("ClaveTanqueLleno");
        Tipocliente = getIntent().getIntExtra("Tipocliente", 0);
        transaccionid = getIntent().getStringExtra("transaccionid");
        folio = getIntent().getStringExtra("folio");
        nipCliente = getIntent().getStringExtra("nipCliente");
        nipMd5Cliente = getIntent().getStringExtra("nipMd5Cliente");
        combustible = getIntent().getStringExtra("CombustiblesAsociados");

        if (tipoSelecccionado.equals("L")){
            Cantidad.setText(cantidadtotal);
            Pesos.setEnabled(false);
            Cantidad.setEnabled(false);
            banderatiposeleccion = "false";
            bandera = false;
            //EtiquetaCantidad.setText("Litros");
        }else{if (tipoSelecccionado.equals("P")) {
            Pesos.setText(cantidadtotal);
            //EtiquetaCantidad.setText("Pesos");
            Cantidad.setEnabled(false);
            Pesos.setEnabled(false);
            banderatiposeleccion = "false";
            bandera = true;
        }
        }
        cargaCombustible();
        Enviar = findViewById(R.id.comprar);
        Enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Enviar.setEnabled(false);
                if (myArray!= null && myArray.length()!=0){
                    ProgressDialog bar = new ProgressDialog(eligeCombustibleTL.this);
                    bar.setTitle("Tanque Lleno");
                    bar.setMessage("Esperando Respuesta");
                    bar.setIcon(R.drawable.tanquelleno);
                    bar.setCancelable(false);
                    bar.show();
                    EnviarProductos();
                }else{
                    Enviar.setEnabled(true);
                    Toast.makeText(getApplicationContext(),"Selecciona un combustible", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void EnviarProductos() {
        if (!Conexion.compruebaConexion(this)) {
            Toast.makeText(getBaseContext(), "Sin conexión a la red ", Toast.LENGTH_SHORT).show();
            Intent intent1 = new Intent(getApplicationContext(), Menu_Principal.class);

            startActivity(intent1);
            finish();
        } else {
            String URL = "http://"+ipEstacion+"/CorpogasService/api/tanqueLleno/EnviarProductos";
            RequestQueue queue = Volley.newRequestQueue(this);
            try {
                datos = new JSONObject();
                datos.put("NumeroInternoSucursal", numerointernoSucursal);
//            datos.put("EstacionId", EstacionId);
                datos.put("SucursalId", sucursalId);
                datos.put("SucursalEmpleadoId",SucursalEmpleadoId);
                datos.put("PosicionCarga", PosicioDeCarga);
                datos.put("TarjetaCliente", NumeroDeTarjeta);
                datos.put("Placas", placas);
                datos.put("Odometro",odometro);
                datos.put( "ClaveTanqueLleno", ClaveTanqueLleno);
                datos.put("Nip",  nipMd5Cliente);
                datos.put("TipoCliente", Tipocliente.toString());
                datos.put("Productos", myArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JsonObjectRequest request_json = new JsonObjectRequest(Request.Method.POST, URL, datos, new com.android.volley.Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    pasa = true;

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
                            Modales modales = new Modales(eligeCombustibleTL.this);
                            View view1 = modales.MostrarDialogoCorrecto(eligeCombustibleTL.this,mensaje);
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
                            Modales modales = new Modales(eligeCombustibleTL.this);
                            View view1 = modales.MostrarDialogoAlertaAceptar(eligeCombustibleTL.this,mensaje,titulo);
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
                    String error1 = error.networkResponse.data.toString();
                    Toast.makeText(eligeCombustibleTL.this, "error: "+error1, Toast.LENGTH_SHORT).show();
                    pasa = false;
                }
            }) {
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<String, String>();
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
        List<Integer> imgid;
        imgid = new ArrayList<>();


        if (!Conexion.compruebaConexion(this)) {
            Toast.makeText(getBaseContext(), "Sin conexión a la red ", Toast.LENGTH_SHORT).show();
            Intent intent1 = new Intent(getApplicationContext(), Menu_Principal.class);

            startActivity(intent1);
            finish();
        } else {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://" + ipEstacion + "/CorpogasService/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            EndPoints PosicionCargaProductosSucursal = retrofit.create(EndPoints.class);
            Call<Isla> call = PosicionCargaProductosSucursal.getPosicionCargaProductosSucursal(sucursalId,PosicioDeCarga);
            call.enqueue(new Callback<Isla>() {


                @Override
                public void onResponse(Call<Isla> call, Response<Isla> response) {
                    if (!response.isSuccessful()) {
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
                    ListAdapterConbustiblesTLl adapterP = new ListAdapterConbustiblesTLl(eligeCombustibleTL.this, ID, NombreProducto, imgid);
                    list=(ListView)findViewById(R.id.list);
                    list.setAdapter(adapterP);
//        Agregado  click en la lista
                    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                            IdCombustible = ClaveProducto.get(position);
                            productoid = String.valueOf(ClaveProducto.get(position));

                            Double Costo = PrecioProducto.get(position);

                            nombrecombustible= ID.get(position);
                            cantidadtotal= Cantidad.getText().toString();


                            if (tipoSelecccionado.equals("L")){
                                String litros =  Cantidad.getText().toString();
                                Double Monto = Costo * Double.parseDouble(litros);
                                DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
                                simbolos.setDecimalSeparator('.');
                                DecimalFormat df = new DecimalFormat("####.00##",simbolos);

                                df.setMaximumFractionDigits(2);
                                Pesos.setText(df.format(Monto));
                                Double litrosD = Double.parseDouble(litros);
                                //Cantidad.setText(df.format(litrosD));

                                datos = new JSONObject();
                                try {
                                    datos.put("TipoProducto","1");
                                    datos.put("ProductoId", productoid);
                                    datos.put("NumeroInterno", IdCombustible);
                                    datos.put("Descripcion",nombrecombustible);
                                    datos.put("Cantidad", Double.parseDouble(litros));
                                    //datos.put("Precio", Monto);
                                    datos.put("Importe", false);
                                    myArray.put(datos);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                            }else{
                                String monto =  Pesos.getText().toString();
                                Double  litros = Double.parseDouble(monto) / Costo;
                                DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
                                simbolos.setDecimalSeparator('.');
                                DecimalFormat df = new DecimalFormat("####.00##",simbolos);

                                df.setMaximumFractionDigits(2);
                                Cantidad.setText(df.format(litros));

                                Double pesosD = Double.parseDouble(monto);
                                Pesos.setText(df.format(pesosD));

                                datos = new JSONObject();
                                try {
                                    datos.put("TipoProducto","1");
                                    datos.put("ProductoId", productoid);
                                    datos.put("NumeroInterno", IdCombustible);
                                    datos.put("Descripcion",nombrecombustible);
                                    datos.put("Cantidad", Double.parseDouble(monto));
                                    //datos.put("Precio", litros);
                                    datos.put("Importe", true);
                                    myArray.put(datos);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
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

                            ListAdapterConbustiblesTLl adapterP = new ListAdapterConbustiblesTLl(eligeCombustibleTL.this, main, sub, img);
                            list=(ListView)findViewById(R.id.list);
                            list.setAdapter(adapterP);
                            list.setEnabled(false);

                            leyenda.setText("PRODUCTO SELECCIONADO");

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

    private String obtieneDosDecimales(float valor){
        DecimalFormat format = new DecimalFormat();
        format.setMaximumFractionDigits(2); //Define 2 decimales.
        return format.format(valor);
    }


    //Metodo para regresar a la actividad principal
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), Menu_Principal.class);
        startActivity(intent);
        finish();
    }

}