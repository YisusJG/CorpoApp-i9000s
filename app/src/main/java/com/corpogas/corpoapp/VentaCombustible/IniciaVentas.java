package com.corpogas.corpoapp.VentaCombustible;

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
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.corpogas.corpoapp.Adapters.RVAdapter;
import com.corpogas.corpoapp.Conexion;
import com.corpogas.corpoapp.Configuracion.SQLiteBD;
import com.corpogas.corpoapp.Entities.Classes.RecyclerViewHeaders;
import com.corpogas.corpoapp.Entities.Estaciones.Isla;
import com.corpogas.corpoapp.Fajillas.EntregaFajillas;
import com.corpogas.corpoapp.LecturaTarjetas.MonederosElectronicos;
import com.corpogas.corpoapp.Menu_Principal;
import com.corpogas.corpoapp.Modales.Modales;
import com.corpogas.corpoapp.Productos.MostrarCarritoTransacciones;
import com.corpogas.corpoapp.Productos.VentasProductos;
import com.corpogas.corpoapp.Puntada.PuntadaQr;
import com.corpogas.corpoapp.Puntada.PuntadaRedimirQr;
import com.corpogas.corpoapp.Puntada.SeccionTarjeta;
import com.corpogas.corpoapp.R;
import com.corpogas.corpoapp.TanqueLleno.ListAdapterConbustiblesTLl;
import com.corpogas.corpoapp.Tickets.FormaPagoAutojarreo;
import com.corpogas.corpoapp.Tickets.PosicionCargaTickets;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IniciaVentas extends AppCompatActivity {
    SQLiteBD data;
    String EstacionId, ipEstacion, lugarproviene, idUsuario, sucursalId, poscicionCarga, estacionJarreo, posicionCarga, usuarioid, numerodispositivo, correcto, mensaje;
    long numeroInternoPosicionCarga, posicioncargaid;
    JSONArray myArray = new JSONArray();
    TextView tvTituloIniciaVenta, tvPredeterminadoCantidad, tvAutojarreo;
    Spinner spCombustible;
    RecyclerView rvPredeterminado;
    Double descuento, descuentoMagnaYena, descuentoPremiumYena, descuentoDieselYena;
    String claveProducto, descripcioncombustible, precio, numeroTarjeta, claveTarjeta, nipCliente;

    List<RecyclerViewHeaders> lrecyclerViewHeaders;

    Button btnAgregarProducto, btnDespachoLibreVenta, btnCobrarPosicionCarga;

    List<String> NombreProducto;
    List<String> PrecioProducto;
    List<String> ClaveProducto;
    List<String> CodigoBarras;
    List<String> ExistenciaProductos;
    List<String> ProductoId, CategoriaId, ID;
    ProgressDialog bar;



    //    Isla respuestaApiPosicionCargaProductosSucursal;
    long TipoProducto = 1, idProducto, IdCombustible;
    double precioproducto;

    DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
    DecimalFormat df;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicia_ventas);

        init();

        btnDespachoLibreVenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvPredeterminadoCantidad.setText("");
                spCombustible.setEnabled(false);
                cargaOpcionesPredeterminadoSolo();
                initializeAdapter();
                solicitarDespacho();
            }
        });

        btnAgregarProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), VentasProductos.class);
                intent.putExtra("numeroOperativa", usuarioid);
                intent.putExtra("cadenaproducto", "");
                intent.putExtra("lugarproviene", "Venta");
                intent.putExtra("lugarProviene", lugarproviene);
                intent.putExtra("NumeroIsla", data.getIslaId());
                intent.putExtra("NumeroEmpleado", usuarioid);
                intent.putExtra("posicionCarga", poscicionCarga);
                startActivity(intent);

            }
        });

        btnCobrarPosicionCarga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidaTransaccionActiva();
            }
        });



        cargaProductos("1");
    }

    private void ValidaTransaccionActiva() {
        if (!Conexion.compruebaConexion(this)) {
            Toast.makeText(getBaseContext(), "Sin conexión a la red ", Toast.LENGTH_SHORT).show();
            Intent intent1 = new Intent(getApplicationContext(), Menu_Principal.class);
            startActivity(intent1);
            finish();
        } else {
            String url = "http://" + ipEstacion + "/CorpogasService/api/ventaProductos/sucursal/" + sucursalId + "/posicionCargaId/" + poscicionCarga;
            // Utilizamos el metodo Post para validar la contraseña
            StringRequest eventoReq = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
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
                                    if (banderaConDatos.equals(true)) {
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
                                            if (MontoenCanasta.equals(0.00)) {
//                                                Reintenta();
                                                Toast.makeText(IniciaVentas.this, "Monto en CERO", Toast.LENGTH_SHORT).show();
                                            } else {
                                                if (estacionJarreo.equals("true")){
                                                    Intent intent1 = new Intent(getApplicationContext(), FormaPagoAutojarreo.class);
                                                    intent1.putExtra("posicioncarga", Long.parseLong(poscicionCarga));
                                                    intent1.putExtra("posicioncargainterno", numeroInternoPosicionCarga);
                                                    intent1.putExtra("montocanasta", MontoenCanasta.toString());

                                                    startActivity(intent1);
                                                    finish();
                                                }else{
//                                                    posicioncargaEmpleadoSucursal(MontoenCanasta);
                                                    Intent intent = new Intent(getApplicationContext(), FormasPagoReordenado.class);
                                                    intent.putExtra("numeroEmpleado", usuarioid);
                                                    intent.putExtra("posicionCarga", poscicionCarga);
                                                    intent.putExtra("claveProducto", claveProducto);
                                                    intent.putExtra("montoenCanasta", MontoenCanasta);
                                                    intent.putExtra("numeroTarjeta", numeroTarjeta);
                                                    intent.putExtra("claveTarjeta", claveTarjeta);
                                                    intent.putExtra("descuento", descuento);
                                                    intent.putExtra("lugarProviene", lugarproviene);
                                                    intent.putExtra("nipCliente", nipCliente);
                                                    intent.putExtra("IdOperativa", "0");
                                                    intent.putExtra("estacionjarreo", "true");
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            }

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    } else {
                                        String titulo = "AVISO";
                                        String mensajes = "Error";
                                        Modales modales = new Modales(IniciaVentas.this);
                                        View view1 = modales.MostrarDialogoError(IniciaVentas.this, "Aun no concluye el despacho");
                                        view1.findViewById(R.id.buttonAction).setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                modales.alertDialog.dismiss();
//                                            bar.cancel();
                                            }
                                        });
                                    }
                                } else {
                                    String titulo = "AVISO";
                                    String mensajes = "Error";
                                    Modales modales = new Modales(IniciaVentas.this);
                                    View view1 = modales.MostrarDialogoError(IniciaVentas.this, Mensaje);
                                    view1.findViewById(R.id.buttonAction).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            modales.alertDialog.dismiss();
//                                        bar.cancel();
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
                    Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
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
        } else {

            String url = "http://" + ipEstacion + "/CorpogasService/api/despachos/autorizaDespacho/posicionCargaId/" + poscicionCarga + "/usuarioId/" + usuarioid;
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject respuesta = new JSONObject(response);
                        String correctoautoriza = respuesta.getString("Correcto");
                        String mensajeautoriza = respuesta.getString("Mensaje");
                        if (correctoautoriza.equals("true")) {
                            String objetoRespuesta = respuesta.getString("ObjetoRespuesta");
                            String titulo = "AVISO";
                            String mensaje = "Listo para Iniciar Despacho";
                            final Modales modales = new Modales(IniciaVentas.this);
                            View view1 = modales.MostrarDialogoCorrecto(IniciaVentas.this, mensaje);
                            view1.findViewById(R.id.buttonAction).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    modales.alertDialog.dismiss();
                                    tvPredeterminadoCantidad.setText("Despacho Libre");
//                                    Intent intent = new Intent(getApplicationContext(), EligePrecioLitros.class);
//                                    intent.putExtra("numeroEmpleado", idUsuario);
//                                    intent.putExtra("posicionCarga", poscicionCarga);
//                                    intent.putExtra("estacionjarreo", estacionJarreo);
//                                    intent.putExtra("despacholibre", "si");
//                                    intent.putExtra("lugarProviene", "ventas");
//                                    intent.putExtra("nip", "");
//                                    intent.putExtra("numeroTarjeta", "");
//                                    intent.putExtra("descuento", 0);
//                                    intent.putExtra("pocioncarganumerointerno", numeroInternoPosicionCarga);
//
//                                    startActivity(intent);
//                                    finish();
                                }
                            });
                        } else {
                            String titulo = "AVISO";
                            String mensaje = "La posición de carga se encuentra Ocupada";
                            Modales modales = new Modales(IniciaVentas.this);
                            View view1 = modales.MostrarDialogoAlertaAceptar(IniciaVentas.this, mensaje, titulo);
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
        posicioncargaid = getIntent().getLongExtra("pocioncargaid", 0);
        idUsuario = data.getNumeroEmpleado();//getIntent().getStringExtra("numeroEmpleado");
        estacionJarreo = getIntent().getStringExtra("estacionjarreo");
        numeroInternoPosicionCarga = getIntent().getLongExtra("pcnumerointerno", 0);
        numerodispositivo = data.getIdTarjtero();
        lugarproviene = getIntent().getStringExtra("lugarProviene");
        descuento = getIntent().getDoubleExtra("Descuento", 0);

        descuentoMagnaYena = getIntent().getDoubleExtra("descuentoMagna", 0);
        descuentoPremiumYena = getIntent().getDoubleExtra("descuentoPremium", 0);
        descuentoDieselYena = getIntent().getDoubleExtra("descuentoDiesel", 0);


        numeroTarjeta = getIntent().getStringExtra("numeroTarjeta");
        claveTarjeta = getIntent().getStringExtra("claveTarjeta");
        nipCliente = getIntent().getStringExtra("nip");
        tvTituloIniciaVenta = (TextView) findViewById(R.id.tvTituloIniciaVenta);
        tvAutojarreo = (TextView) findViewById(R.id.tvAutojarreo);
        spCombustible = (Spinner) findViewById(R.id.spCombustible);
        rvPredeterminado = (RecyclerView) findViewById(R.id.rvPredeterminado);
        btnAgregarProducto = (Button) findViewById(R.id.btnAgregarProducto);
        btnDespachoLibreVenta = (Button) findViewById(R.id.btnDespachoLibreVenta);
        btnCobrarPosicionCarga = (Button) findViewById(R.id.btnCobrarPosicionCarga);
        tvPredeterminadoCantidad = (TextView) findViewById(R.id.tvPredeterminadoCantidad);
        tvTituloIniciaVenta.setText("PC " + posicioncargaid + ", TIPO DESPACHO");

        if (estacionJarreo.equals("true")){
            tvAutojarreo.setVisibility(View.VISIBLE);
        }else{
            tvAutojarreo.setVisibility(View.INVISIBLE);
        }
        simbolos.setDecimalSeparator('.');
        df = new DecimalFormat("#,###.00##", simbolos);

        if (lugarproviene.equals("puntadaAcumularQr") || lugarproviene.equals("descuentoYena")) {
            btnDespachoLibreVenta.setEnabled(false);
        }


    }


    private void cargaProductos(String TipoProducto) {
        bar = new ProgressDialog(IniciaVentas.this);
        bar.setTitle("Cargando Productos");
        bar.setMessage("Ejecutando... ");
        bar.setIcon(R.drawable.gas);
        bar.setCancelable(false);
        bar.show();


        String url = "http://" + ipEstacion + "/CorpogasService/api/islas/productos/sucursal/" + sucursalId + "/posicionCargaId/" + poscicionCarga;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mostrarProductos(response, TipoProducto);
//                    Toast.makeText(VentaCombustibleAceites.this, "Entro", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                bar.cancel();
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

        List<Integer> imgid;
        imgid = new ArrayList<>();


        NombreProducto.add("0"); // + "    |    " + IdProductos );
        ID.add("Seleccione un combustible");
        PrecioProducto.add("");
        ClaveProducto.add("");
        CodigoBarras.add("");
        ProductoId.add("");
        CategoriaId.add("");


        //ArrayList<singleRow> singlerow = new ArrayList<>();
        try {
            JSONObject p1 = new JSONObject(response);

            //String ni = p1.getString("NumeroInterno");
            String bodega = p1.getString("Bodega");
            JSONObject ps = new JSONObject(bodega);
            String producto = p1.getString("BodegaProductos");
            JSONArray bodegaprod = new JSONArray(producto);

            for (int i = 0; i < bodegaprod.length(); i++) {
                String IdProductos = "";
                JSONObject pA = bodegaprod.getJSONObject(i);
                String productoclave = pA.getString("Producto");
                JSONObject prod = new JSONObject(productoclave);
                String categoriaid = prod.getString("ProductCategoryId");
                if (categoriaid.equals(TipoProducto)) {
                    //NO CARGA LOS COMBUSTIBLES
                    String ExProductos = pA.getString("Existencias");
                    ExistenciaProductos.add(ExProductos);
                    String TProductoId = "2";//prod.getString("TipoSatProductoId");
                    DescLarga = prod.getString("LongDescription");
                    idArticulo = prod.getString("Id");
                    String codigobarras = prod.getString("Barcode");
                    String PControl = prod.getString("ProductControls");

                    JSONArray PC = new JSONArray(PControl);
                    for (int j = 0; j < PC.length(); j++) {
                        JSONObject Control = PC.getJSONObject(j);
                        preciol = Control.getString("Price");
                        IdProductos = Control.getString("ProductId"); //ProductoId
                    }
                    NombreProducto.add("ID: " + idArticulo + "    |     $" + preciol); // + "    |    " + IdProductos );
                    ID.add(DescLarga);
                    PrecioProducto.add(preciol);
                    ClaveProducto.add(idArticulo);
                    CodigoBarras.add(codigobarras);
                    ProductoId.add(IdProductos);
                    CategoriaId.add(categoriaid);

                    if (IdProductos.length() == 0) {
                        IdProductos = "10";
                    }
                    switch (Integer.parseInt(IdProductos)) {
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
        } catch (JSONException e) {
            e.printStackTrace();
            bar.cancel();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(IniciaVentas.this, R.layout.support_simple_spinner_dropdown_item, ID);
        spCombustible.setAdapter(adapter);

        spCombustible.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tvPredeterminadoCantidad.setText("");
                if (position > 0) {
//                    String titulo = "PUNTADA QR";
//                    String mensajes = "¿Descuento con Puntada ( QR )? ";
//                    Modales modalesPuntada = new Modales(IniciaVentas.this);
//                    View viewLectura = modalesPuntada.MostrarDialogoAlerta(IniciaVentas.this, mensajes,  "SI", "NO");
//                    viewLectura.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            String precio = PrecioProducto.get(position);
//                            String claveProducto = ClaveProducto.get(position);
//                            //LeeTarjeta();
//                            Intent intent = new Intent(getApplicationContext(), PuntadaRedimirQr.class);
//                            //Intent intent = new Intent(getApplicationContext(), ScanManagerDemo.class);
//                            intent.putExtra("combustible", ID.get(position));
//                            intent.putExtra("posicionCarga", poscicionCarga);
//                            intent.putExtra("estacionjarreo", estacionJarreo);
//                            intent.putExtra("claveProducto", claveProducto);
//                            intent.putExtra("precioProducto", precio);
//                            intent.putExtra("despacholibre", "no");
//                            intent.putExtra("lugarProviene", "Acumular");
//                            intent.putExtra("pocioncarganumerointerno", numeroInternoPosicionCarga);
//                            startActivity(intent);
//                            modalesPuntada.alertDialog.dismiss();
//                        }
//                    });
//                    viewLectura.findViewById(R.id.buttonNo).setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            modalesPuntada.alertDialog.dismiss();
                    claveProducto = ClaveProducto.get(position);
                    descripcioncombustible = ID.get(position);
                    precio = PrecioProducto.get(position);
                    cargaOpcionesPredeterminado();
                    initializeAdapter();
//                        }
//                    });
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void cargaOpcionesPredeterminado() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvPredeterminado.setLayoutManager(linearLayoutManager);
        rvPredeterminado.setHasFixedSize(true);
        lrecyclerViewHeaders = new ArrayList<>();
        lrecyclerViewHeaders.add(new RecyclerViewHeaders("Litros", "Cantidad en Litros", R.drawable.gas));
        lrecyclerViewHeaders.add(new RecyclerViewHeaders("Pesos", "Cantidad en Pesos", R.drawable.moneda));

    }

    private void cargaOpcionesPredeterminadoSolo() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvPredeterminado.setLayoutManager(linearLayoutManager);
        rvPredeterminado.setHasFixedSize(true);
        lrecyclerViewHeaders = new ArrayList<>();

    }


    private void initializeAdapter() {
        RVAdapter adapter = new RVAdapter(lrecyclerViewHeaders);
        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnDespachoLibreVenta.setEnabled(false);
                switch (lrecyclerViewHeaders.get(rvPredeterminado.getChildAdapterPosition(v)).getTitulo()) {
                    case "Litros":
                        EnviaLitrosPesos("LITROS");
                        break;
                    case "Pesos":
                        EnviaLitrosPesos("PESOS");
                        break;
                    default:
                        break;
                }
            }
        });
        rvPredeterminado.setAdapter(adapter);
    }

    private void EnviaLitrosPesos(String identificador) {
        String titulo = "PREDETERMINADO";
        String mensaje = "Ingresa la cantidad de " + identificador;
        Modales modales = new Modales(IniciaVentas.this);
        View viewLectura = modales.MostrarDialogoInsertaDato(IniciaVentas.this, mensaje, titulo);
        EditText edtProductoCantidad = ((EditText) viewLectura.findViewById(R.id.textInsertarDato));
        edtProductoCantidad.setInputType(InputType.TYPE_CLASS_NUMBER);
        viewLectura.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modales.alertDialog.dismiss();
                String cantidad = edtProductoCantidad.getText().toString();
                if (cantidad.isEmpty()) {
                    edtProductoCantidad.setError("Ingresa la cantidad de " + identificador);
                } else {
                    String CantidadaEnviar = cantidad;
                    EnviarProductosPredeterminado(Double.parseDouble(CantidadaEnviar), identificador);

                }
            }
        });
        viewLectura.findViewById(R.id.buttonNo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modales.alertDialog.dismiss();
            }
        });
    }

    private void EnviarProductosPredeterminado(Double cantidadpredeterminar, String identificador) {
        if (!Conexion.compruebaConexion(this)) {
            Toast.makeText(getBaseContext(), "Sin conexión a la red", Toast.LENGTH_SHORT).show();
            Intent intent1 = new Intent(getApplicationContext(), Menu_Principal.class);
            startActivity(intent1);
            finish();
        } else {

            JSONObject datos = new JSONObject();
            try {
                datos.put("TipoProducto", "1");
                datos.put("ProductoId", claveProducto);
                datos.put("NumeroInterno", claveProducto);
                datos.put("Descripcion", descripcioncombustible);
                datos.put("Cantidad", cantidadpredeterminar);
                datos.put("Precio", precio);
                if (lugarproviene.equals("puntadaAcumularQr")) {
                    datos.put("importedescuento", descuento);
                } else if (lugarproviene.equals("descuentoYena")) {
                    switch (claveProducto) {
                        case "1":
                            datos.put("importedescuento",  descuentoMagnaYena);
                            break;
                        case "2":
                            datos.put("importedescuento", descuentoPremiumYena);
                            break;
                        case "3":
                            datos.put("importedescuento", descuentoDieselYena);
                            break;
                    }
                }

                myArray.put(datos);
                //datos.put("Precio", litros);
                if (identificador.equals("LITROS")) {
                    datos.put("Importe", false);
                } else {
                    datos.put("Importe", true);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


            String url = "http://" + ipEstacion + "/CorpogasService/api/ventaProductos/GuardaProductos/sucursal/" + sucursalId + "/origen/" + numerodispositivo + "/usuario/" + usuarioid + "/posicionCarga/" + poscicionCarga;
            RequestQueue queue = Volley.newRequestQueue(this);
            JsonArrayRequest request_json = new JsonArrayRequest(Request.Method.POST, url, myArray,
                    new com.android.volley.Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            //Get Final response
                            if (correcto.equals("true")) {
                                String titulo = "AVISO";
                                String mensajes = "Listo para Iniciar Despacho";
                                if (identificador.equals("PESOS")) {
                                    tvPredeterminadoCantidad.setText("Predeterminado: $" + df.format(cantidadpredeterminar));
                                } else {
                                    tvPredeterminadoCantidad.setText("Predeterminado: " + df.format(cantidadpredeterminar) + " " + identificador);
                                }
                                spCombustible.setEnabled(false);
                                cargaOpcionesPredeterminadoSolo();
                                initializeAdapter();
                                final Modales modales = new Modales(IniciaVentas.this);

                                View view1 = modales.MostrarDialogoCorrecto(IniciaVentas.this, mensajes);
                                view1.findViewById(R.id.buttonAction).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        modales.alertDialog.dismiss();
                                    }
                                });
                            } else {
                                try {
                                    String titulo = "AVISO";
                                    final Modales modales = new Modales(IniciaVentas.this);
                                    View view1 = modales.MostrarDialogoAlertaAceptar(IniciaVentas.this, mensaje, titulo);
                                    view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            modales.alertDialog.dismiss();
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
                            Modales modales = new Modales(IniciaVentas.this);
                            View view1 = modales.MostrarDialogoAlertaAceptar(IniciaVentas.this, mensajes, titulo);
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
                    return com.android.volley.Response.success(myArray, HttpHeaderParser.parseCacheHeaders(response));
                }
            };
            request_json.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            queue.add(request_json);
        }
    }

    private void posicioncargaEmpleadoSucursal(Double montoacargar){
        bar = new ProgressDialog(IniciaVentas.this);
        bar.setTitle("Cargando Posiciones de Carga");
        bar.setMessage("Ejecutando... ");
        bar.setIcon(R.drawable.gas);
        bar.setCancelable(false);
        bar.show();

        String url;
        url = "http://" + ipEstacion + "/CorpogasService/api/posicionCargas/ObtienePosicionCargaPendienteCobroPorEmpleado/sucursal/" + sucursalId + "/posicionCarga/" + poscicionCarga;
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
                        Modales modales = new Modales(IniciaVentas.this);
                        View view1 = modales.MostrarDialogoAlertaAceptar(IniciaVentas.this,"Problema con la Posicion de Carga",titulo);
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

                        JSONArray control1 = new JSONArray(ObjetoRespuesta);
                        for (int i = 0; i < control1.length(); i++) {
                            JSONObject posiciones = control1.getJSONObject(i);
//                            Long posicionCargaId = posiciones.getLong("Posicioncarga");
//                            long posicionCargaNumeroInterno = posiciones.getLong("NumeroPosicionCarga");
//
//                            boolean pocioncargadisponible = posiciones.getBoolean("Disponible");
//                            estado = posiciones.getString("Estado");
                            boolean pocioncargapendientecobro = posiciones.getBoolean("PendienteCobro");
//                            String descripcionoperativa = posiciones.getString("DescripcionOperativa");
                            String jarreo = posiciones.getString("EstacionJarreo");
                            Long numeroOperativa = posiciones.getLong("Operativa");
                            Boolean banderacarga ;
                            banderacarga = false;
                            if (pocioncargapendientecobro == true){
//                                    String titulo = "PC " + posicionCargaNumeroInterno;
//                                    String subtitulo = "";//
                                //    subtitulo = "Magna  |  Premium  |  Diesel";
//                                    subtitulo =descripcionoperativa;//
                                Intent intent = new Intent(getApplicationContext(), FormasPagoReordenado.class);
                                intent.putExtra("numeroEmpleado", usuarioid);
                                intent.putExtra("posicionCarga", poscicionCarga);
                                intent.putExtra("claveProducto", claveProducto);
                                intent.putExtra("montoenCanasta", montoacargar);
                                intent.putExtra("numeroTarjeta", numeroTarjeta);
                                intent.putExtra("claveTarjeta", claveTarjeta);
                                intent.putExtra("descuento", descuento);
                                intent.putExtra("lugarProviene", lugarproviene);
                                intent.putExtra("nipCliente", nipCliente);
                                intent.putExtra("IdOperativa", "0");
                                intent.putExtra("estacionjarreo", jarreo);
                                startActivity(intent);
                                finish();
                            }
                        }
                    }
                    bar.cancel();
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
                        Modales modales = new Modales(IniciaVentas.this);
                        View view1 = modales.MostrarDialogoAlertaAceptar(IniciaVentas.this, mensajes, titulo);
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



    @Override
    public void onBackPressed() {
        startActivity(new Intent(IniciaVentas.this, Menu_Principal.class));
    }
}