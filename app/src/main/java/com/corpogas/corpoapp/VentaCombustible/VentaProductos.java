package com.corpogas.corpoapp.VentaCombustible;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
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
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.corpogas.corpoapp.Conexion;
import com.corpogas.corpoapp.Configuracion.SQLiteBD;
import com.corpogas.corpoapp.Entities.Estaciones.BodegaProducto;
import com.corpogas.corpoapp.Entities.Estaciones.Isla;
import com.corpogas.corpoapp.Entities.Sucursales.ProductControl;
import com.corpogas.corpoapp.Interfaces.Endpoints.EndPoints;
import com.corpogas.corpoapp.LecturaCodigoBarrasQR.ScanManagerDemo;
import com.corpogas.corpoapp.LecturaTarjetas.MonederosElectronicos;
import com.corpogas.corpoapp.Menu_Principal;
import com.corpogas.corpoapp.Modales.Modales;
import com.corpogas.corpoapp.Productos.MostrarCarritoTransacciones;
import com.corpogas.corpoapp.Productos.VentasProductos;
import com.corpogas.corpoapp.Puntada.PosicionPuntadaRedimir;
import com.corpogas.corpoapp.Puntada.PuntadaRedimirQr;
import com.corpogas.corpoapp.R;
import com.corpogas.corpoapp.VentaPagoTarjeta;
import com.corpogas.corpoapp.TanqueLleno.ListAdapterConbustiblesTLl;
import com.corpogas.corpoapp.TanqueLleno.PlanchadoTarjeta.PlanchadoTanqueLleno;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class VentaProductos extends AppCompatActivity {
    SQLiteBD data;
    String EstacionId,  ipEstacion, lugarproviene, idUsuario, sucursalId, poscicionCarga, estacionJarreo, posicionCarga, usuarioid;
    long numeroInternoPosicionCarga;

    Button btnCombustibleVenta, btnPerifericosVentas, btnCobrarPeriferico, btnDecrementarProducto, btnEscanearProducto, btnLecturaQR;
    ListView lstProductos;

    List<String> NombreProducto;
    List<String> PrecioProducto;
    List<String> ClaveProducto;
    List<String> CodigoBarras;
    List<String> ExistenciaProductos;
    List<String> ProductoId, CategoriaId, ID;
    ProgressDialog bar;


    Isla respuestaApiPosicionCargaProductosSucursal;
    long TipoProducto = 1, idProducto, IdCombustible;
    double precioproducto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venta_productos);

        SQLiteBD db = new SQLiteBD(getApplicationContext());
        data = new SQLiteBD(getApplicationContext());
        this.setTitle(data.getNombreEstacion() + " ( EST.:" + data.getNumeroEstacion() + ")");
        EstacionId = data.getIdEstacion();
        sucursalId = data.getIdSucursal();
        ipEstacion = data.getIpEstacion();

        init();

        lstProductos = findViewById(R.id.lstProductos);
        btnCombustibleVenta = findViewById(R.id.btnGasolinas);
        btnCombustibleVenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(VentaProductos.this, "Entro Boton Gas Venta", Toast.LENGTH_SHORT).show();
//                ActivaDesactiva (false);
            }
        });

        btnCobrarPeriferico = (Button) findViewById(R.id.btnCobrarPeriferico);
        btnCobrarPeriferico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidaTransaccionActiva();
            }
        });

        btnPerifericosVentas = findViewById(R.id.btnAceitesVentas);
        btnPerifericosVentas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivaDesactiva (true);
                btnCobrarPeriferico.setEnabled(true);
                Intent intent = new Intent(getApplicationContext(), VentasProductos.class);
                intent.putExtra("numeroOperativa", usuarioid);
                intent.putExtra("cadenaproducto", "");
                intent.putExtra("lugarproviene", "Venta");
                intent.putExtra("NumeroIsla", data.getIslaId());
                intent.putExtra("NumeroEmpleado", usuarioid);
                intent.putExtra("posicionCarga", poscicionCarga);
                startActivity(intent);

//                Toast.makeText(VentaProductos.this, "Entro Boton Perifericos Venta", Toast.LENGTH_SHORT).show();
            }
        });

        btnEscanearProducto = findViewById(R.id.btnEscanearProducto);
        btnEscanearProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(VentaProductos.this, "Entro Boton Escanear Venta", Toast.LENGTH_SHORT).show();
                solicitarDespacho();
            }
        });
        ActivaDesactiva (false);
//        btnDecrementarProducto.setEnabled(false);
//        btnIncrementarProducto.setEnabled(false);
        btnEscanearProducto.setEnabled(true);

//        cargaProductos("1");
        cargaCombustible();

    }

    private void ValidaTransaccionActiva() {
        if (!Conexion.compruebaConexion(this)) {
            Toast.makeText(getBaseContext(), "Sin conexión a la red ", Toast.LENGTH_SHORT).show();
            Intent intent1 = new Intent(getApplicationContext(), Menu_Principal.class);
            startActivity(intent1);
            finish();
        } else {

            String url = "http://" + ipEstacion + "/CorpogasService/api/ventaProductos/sucursal/"+sucursalId+"/posicionCargaId/" + poscicionCarga;

            // Utilizamos el metodo Post para validar la contraseña
            StringRequest eventoReq = new StringRequest(Request.Method.GET,url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Boolean banderaConDatos;
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String Correcto = jsonObject.getString("Correcto");
                                //String Mensaje = jsonObject.getString("Mensaje");
                                String CadenaObjetoRespuesta = jsonObject.getString("ObjetoRespuesta");
                                if (CadenaObjetoRespuesta.equals("null")){
                                    banderaConDatos = false;
                                }else{
                                    if (CadenaObjetoRespuesta.equals("[]")){
                                        banderaConDatos = false;
                                    }else{
                                        banderaConDatos=true;
                                    }
                                }
                                if (Correcto.equals("true")){
                                    if (banderaConDatos.equals(false)){
                                        String titulo = "AVISO";
                                        String mensajes = "No hay productos a cobrar";
                                        Modales modales = new Modales(VentaProductos.this);
                                        View view1 = modales.MostrarDialogoAlertaAceptar(VentaProductos.this,mensajes,titulo);
                                        view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                bar.cancel();
                                                modales.alertDialog.dismiss();
                                                btnCobrarPeriferico.setEnabled(false);
                                            }
                                        });
                                    }else{
                                        //Envia a Mostrar CArrito TRansacciones
                                        Intent intente = new Intent(getApplicationContext(), MostrarCarritoTransacciones.class);
                                        //se envia el id seleccionado a la clase Usuario Producto
                                        intente.putExtra("posicion", poscicionCarga);
                                        intente.putExtra("usuario", usuarioid);
                                        intente.putExtra("cadenaproducto", "");
                                        intente.putExtra("lugarproviene", "Despacho");
                                        intente.putExtra("numeroOperativa", "1");
                                        intente.putExtra("cadenarespuesta", CadenaObjetoRespuesta);
                                        //Ejecuta la clase del Usuario producto
                                        startActivity(intente);
                                        //Finaliza activity
                                        finish();

                                    }
                                }else {
                                    String titulo = "AVISO";
                                    String mensajes = "No hay productos a cobrar";
                                    Modales modales = new Modales(VentaProductos.this);
                                    View view1 = modales.MostrarDialogoAlertaAceptar(VentaProductos.this,mensajes,titulo);
                                    view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            bar.cancel();
                                            modales.alertDialog.dismiss();
                                            btnCobrarPeriferico.setEnabled(false);
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
                }
            });
            // Añade la peticion a la cola
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            eventoReq.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(eventoReq);

        }
    }



    private void solicitarDespacho() {
        if (!Conexion.compruebaConexion(this)) {
            Toast.makeText(getBaseContext(), "Sin conexión a la red ", Toast.LENGTH_SHORT).show();
            Intent intent1 = new Intent(getApplicationContext(), Menu_Principal.class);
            startActivity(intent1);
            finish();
        }else {

            String url = "http://" + ipEstacion + "/CorpogasService/api/despachos/autorizaDespacho/posicionCargaId/" + poscicionCarga + "/usuarioId/" + usuarioid;
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject respuesta = new JSONObject(response);
                        String correctoautoriza = respuesta.getString("Correcto");
                        String mensajeautoriza = respuesta.getString("Mensaje");
                        String objetoRespuesta = respuesta.getString("ObjetoRespuesta");
                        if (correctoautoriza.equals("true")) {
                            String titulo = "AVISO";
                            String mensaje = "Listo para Iniciar Despacho";
                            final Modales modales = new Modales(VentaProductos.this);
                            View view1 = modales.MostrarDialogoCorrecto(VentaProductos.this,mensaje);
                            view1.findViewById(R.id.buttonAction).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    modales.alertDialog.dismiss();
                                    Intent intent = new Intent(getApplicationContext(), EligePrecioLitros.class);
                                    intent.putExtra("numeroEmpleado", idUsuario);
                                    intent.putExtra("posicionCarga", poscicionCarga);
                                    intent.putExtra("estacionjarreo", estacionJarreo);
                                    intent.putExtra("despacholibre", "si");
                                    intent.putExtra("lugarProviene", "ventas");
                                    intent.putExtra("nip", "");
                                    intent.putExtra("numeroTarjeta", "");
                                    intent.putExtra("descuento", 0);
                                    intent.putExtra("pocioncarganumerointerno", numeroInternoPosicionCarga);

                                    startActivity(intent);
                                    finish();
                                }
                            });
                        } else {
                            String titulo = "AVISO";
                            String mensaje = "La posición de carga se encuentra Ocupada";
                            Modales modales = new Modales(VentaProductos.this);
                            View view1 = modales.MostrarDialogoAlertaAceptar(VentaProductos.this,mensaje,titulo);
                            view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    modales.alertDialog.dismiss();
                                }
                            });
                            //Toast.makeText(getApplicationContext(), "Posiciín Ocupada",mensajeautoriza, Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                }
            });
            RequestQueue requestQueue = Volley.newRequestQueue(this.getApplicationContext());
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        }
    }



    private void init() {
        data = new SQLiteBD(getApplicationContext());
        this.setTitle(data.getNombreEstacion() + " ( EST.:" + data.getNumeroEstacion() + ")");
        EstacionId = data.getIdEstacion();
        sucursalId = data.getIdSucursal();
        ipEstacion = data.getIpEstacion();
        usuarioid = data.getNumeroEmpleado();
        poscicionCarga = getIntent().getStringExtra("posicionCarga");
        idUsuario = data.getNumeroEmpleado();//getIntent().getStringExtra("numeroEmpleado");
        estacionJarreo = getIntent().getStringExtra("estacionjarreo");
        numeroInternoPosicionCarga = getIntent().getLongExtra("pcnumerointerno",0);

    }

    private void ActivaDesactiva(Boolean Activa){
//        btnDecrementarProducto.setEnabled(Activa);
//        btnIncrementarProducto.setEnabled(Activa);
//        btnEscanearProducto.setEnabled(Activa);

    }



    private void cargaCombustible(){
        bar = new ProgressDialog(VentaProductos.this);
        bar.setTitle("Cargando Productos");
        bar.setMessage("Ejecutando... ");
        bar.setIcon(R.drawable.gas);
        bar.setCancelable(false);
        bar.show();

        //Declaracion de variables
        ID = new ArrayList<String>();
        NombreProducto = new ArrayList<String>();
        PrecioProducto = new ArrayList<>();
        ClaveProducto = new ArrayList();
        CodigoBarras = new ArrayList();
        ExistenciaProductos = new ArrayList();
        ProductoId = new ArrayList();
        CategoriaId = new ArrayList<>();
//        ClaveTanqueLleno = "";
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
            Call<Isla> call = PosicionCargaProductosSucursal.getPosicionCargaProductosSucursal(Long.parseLong(sucursalId), poscicionCarga.toString());
            call.enqueue(new Callback<Isla>() {


                @Override
                public void onResponse(Call<Isla> call, retrofit2.Response<Isla> response) {
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
                            PrecioProducto.add(String.valueOf(precioproducto));
                            ClaveProducto.add(String.valueOf(idProducto));
                            CodigoBarras.add(codigoBarras);
                            ProductoId.add(String.valueOf(idProducto));
                            CategoriaId.add(String.valueOf(tipoProducto));

                            switch ((int) idProducto) {
                                case 1:
                                    imgid.add(R.drawable.magna);
                                    break;
                                case 2:
                                    imgid.add(R.drawable.premium);
                                    break;
                                case 3:
                                    imgid.add(R.drawable.diesel);
                                    break;
                                case 4:
                                    imgid.add(R.drawable.magna);
                                    break;
                                case 5:
                                    imgid.add(R.drawable.premium);
                                    break;
                                case 6:
                                    imgid.add(R.drawable.diesel);
                                    break;
                                case 7:
                                    imgid.add(R.drawable.magna);
                                    break;
                                case 8:
                                    imgid.add(R.drawable.premium);
                                    break;
                                case 9:
                                    imgid.add(R.drawable.diesel);
                                    break;

                                default:
                                    imgid.add(R.drawable.magna);
                            }

                        }
                    }
                    bar.cancel();
                    ListAdapterConbustiblesTLl adapterP = new ListAdapterConbustiblesTLl(VentaProductos.this, ID, NombreProducto, imgid);
                    lstProductos.setAdapter(adapterP);
//        Agregado  click en la lista
                    lstProductos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            String titulo = "PUNTADA QR";
                            String mensajes = "Desea Acumular la venta a su Tarjeta Puntada ?";
                            Modales modalesPuntada = new Modales(VentaProductos.this);
                            View viewLectura = modalesPuntada.MostrarDialogoAlerta(VentaProductos.this, mensajes,  "SI", "NO");
                            viewLectura.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    String precio = PrecioProducto.get(i);
                                    String claveProducto = ClaveProducto.get(i);
                                    //LeeTarjeta();
                                    Intent intent = new Intent(getApplicationContext(), PuntadaRedimirQr.class);
                                    //Intent intent = new Intent(getApplicationContext(), ScanManagerDemo.class);
                                    intent.putExtra("combustible", ID.get(i));
                                    intent.putExtra("posicionCarga", poscicionCarga);
                                    intent.putExtra("estacionjarreo", estacionJarreo);
                                    intent.putExtra("claveProducto", claveProducto);
                                    intent.putExtra("precioProducto", precio);
                                    intent.putExtra("despacholibre", "no");
                                    intent.putExtra("lugarProviene", "Acumular");
                                    startActivity(intent);
                                    modalesPuntada.alertDialog.dismiss();
                                }
                            });
                            viewLectura.findViewById(R.id.buttonNo).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    modalesPuntada.alertDialog.dismiss();
                                    String nip="-";
                                    String tarjetanumero = "-";
//                                    if(TipoProducto.equals("1")){
                                        String precio = PrecioProducto.get(i);
                                        String claveProducto = ClaveProducto.get(i);
                                        Intent intent = new Intent(getApplicationContext(), EligePrecioLitros.class);
                                        intent.putExtra("combustible", ID.get(i));
                                        intent.putExtra("posicionCarga", poscicionCarga);
                                        intent.putExtra("estacionjarreo", estacionJarreo);
                                        intent.putExtra("claveProducto", claveProducto);
                                        intent.putExtra("precioProducto", precio);
                                        intent.putExtra("despacholibre", "no");
                                        intent.putExtra("lugarProviene", "ventas");
                                        intent.putExtra("nip", "");
                                        intent.putExtra("numeroTarjeta", "");
                                        intent.putExtra("descuento", 0);
                                        intent.putExtra("pocioncarganumerointerno", numeroInternoPosicionCarga);
                                        startActivity(intent);
                                        finish();
//                                    }
                                }
                            });
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






    private void cargaProductos(String TipoProducto) {
        bar = new ProgressDialog(VentaProductos.this);
        bar.setTitle("Cargando Productos");
        bar.setMessage("Ejecutando... ");
        bar.setIcon(R.drawable.gas);
        bar.setCancelable(false);
        bar.show();


        SQLiteBD data = new SQLiteBD(getApplicationContext());
        String posicion = getIntent().getStringExtra("pos");
        String url = "http://" + ipEstacion + "/CorpogasService/api/islas/productos/sucursal/"+sucursalId+"/posicionCargaId/"+poscicionCarga;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mostrarProductos(response, TipoProducto);
//                    Toast.makeText(VentaCombustibleAceites.this, "Entro", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString(), Toast.LENGTH_LONG).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this.getApplicationContext());
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(stringRequest);
    }

    private void mostrarProductos(String response, String TipoProducto) {

        String preciol = null;
        String DescLarga;
        String idArticulo;

        //Declaracion de variables

        ID = new ArrayList<String>();

        NombreProducto = new ArrayList<String>();
        PrecioProducto = new ArrayList<>();
        ClaveProducto = new ArrayList();
        CodigoBarras = new ArrayList();
        ExistenciaProductos = new ArrayList();
        ProductoId = new ArrayList();
        CategoriaId = new ArrayList<>();

        //ArrayList<singleRow> singlerow = new ArrayList<>();
        try {
            JSONObject p1 = new JSONObject(response);

            //String ni = p1.getString("NumeroInterno");
            String bodega = p1.getString("Bodega");
            JSONObject ps = new JSONObject(bodega);
            String producto = p1.getString("BodegaProductos");
            JSONArray bodegaprod = new JSONArray(producto);

            for (int i = 0; i <bodegaprod.length() ; i++){
                String IdProductos = null;
                JSONObject pA = bodegaprod.getJSONObject(i);
                String productoclave = pA.getString("Producto");
                JSONObject prod = new JSONObject(productoclave);
                String categoriaid = prod.getString("ProductCategoryId");
                if (categoriaid.equals(TipoProducto)){
                    //NO CARGA LOS COMBUSTIBLES
                    String ExProductos=pA.getString("Existencias");
                    ExistenciaProductos.add(ExProductos);
                    String TProductoId="2";//prod.getString("TipoSatProductoId");
                    DescLarga=prod.getString("LongDescription");
                    idArticulo=prod.getString("Id");
                    String codigobarras=prod.getString("Barcode");
                    String PControl=prod.getString("ProductControls");

                    JSONArray PC = new JSONArray(PControl);
                    for (int j = 0; j < PC.length(); j++) {
                        JSONObject Control = PC.getJSONObject(j);
                        preciol = Control.getString("Price");
                        IdProductos = Control.getString("Id"); //ProductoId
                    }
                    NombreProducto.add("ID: " + idArticulo + "    |     $"+preciol); // + "    |    " + IdProductos );
                    ID.add(DescLarga);
                    PrecioProducto.add(preciol);
                    ClaveProducto.add(idArticulo);
                    CodigoBarras.add(codigobarras);
                    ProductoId.add(IdProductos);
                    CategoriaId.add(categoriaid);
                }else{

                }
            }
            bar.cancel();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final ListAdapterAceitesCombustibles adapterP = new ListAdapterAceitesCombustibles(this,  ID, NombreProducto);
        lstProductos=(ListView)findViewById(R.id.lstProductos);
        lstProductos.setTextFilterEnabled(true);
        lstProductos.setAdapter(adapterP);
//        Agregado  click en la lista
        lstProductos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String titulo = "PUNTADA QR";
                String mensajes = "Desea Acumular la venta a su Tarjeta Puntada ?";
                Modales modalesPuntada = new Modales(VentaProductos.this);
                View viewLectura = modalesPuntada.MostrarDialogoAlerta(VentaProductos.this, mensajes,  "SI", "NO");
                viewLectura.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String precio = PrecioProducto.get(i);
                        String claveProducto = ClaveProducto.get(i);
                        //LeeTarjeta();
                        Intent intent = new Intent(getApplicationContext(), PuntadaRedimirQr.class);
                        //Intent intent = new Intent(getApplicationContext(), ScanManagerDemo.class);
                        intent.putExtra("combustible", ID.get(i));
                        intent.putExtra("posicionCarga", poscicionCarga);
                        intent.putExtra("estacionjarreo", estacionJarreo);
                        intent.putExtra("claveProducto", claveProducto);
                        intent.putExtra("precioProducto", precio);
                        intent.putExtra("despacholibre", "no");
                        intent.putExtra("lugarProviene", "Acumular");
                        startActivity(intent);
                        modalesPuntada.alertDialog.dismiss();
                    }
                });
                viewLectura.findViewById(R.id.buttonNo).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        modalesPuntada.alertDialog.dismiss();
                        if(TipoProducto.equals("1")){
                            String precio = PrecioProducto.get(i);
                            String claveProducto = ClaveProducto.get(i);
                            Intent intent = new Intent(getApplicationContext(), EligePrecioLitros.class);
                            intent.putExtra("combustible", ID.get(i));
                            intent.putExtra("posicionCarga", poscicionCarga);
                            intent.putExtra("estacionjarreo", estacionJarreo);
                            intent.putExtra("claveProducto", claveProducto);
                            intent.putExtra("precioProducto", precio);
                            intent.putExtra("despacholibre", "no");
                            intent.putExtra("nip", "");
                            intent.putExtra("numeroTarjeta", "");
                            intent.putExtra("descuento", 0);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
            }
        });
    }

}