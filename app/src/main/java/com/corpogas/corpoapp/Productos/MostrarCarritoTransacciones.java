package com.corpogas.corpoapp.Productos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.corpogas.corpoapp.Adapters.RVAdapter;
import com.corpogas.corpoapp.Adapters.RVAdapterItem;
import com.corpogas.corpoapp.ConexionInternet.ComprobarConexionInternet;
import com.corpogas.corpoapp.Configuracion.SQLiteBD;
import com.corpogas.corpoapp.Entities.Classes.RecyclerViewHeaders;
import com.corpogas.corpoapp.Entities.Classes.RespuestaApi;
import com.corpogas.corpoapp.Entities.Common.ProductoTarjetero;
import com.corpogas.corpoapp.Entities.Sistemas.Conexion;
import com.corpogas.corpoapp.Entities.Ventas.Transaccion;
import com.corpogas.corpoapp.FormasPago.FormasDePago;
import com.corpogas.corpoapp.Menu_Principal;
import com.corpogas.corpoapp.Modales.Modales;
import com.corpogas.corpoapp.MyListAdapter;
import com.corpogas.corpoapp.ObtenerClave.ClaveEmpleado;
import com.corpogas.corpoapp.R;
import com.corpogas.corpoapp.Interfaces.Endpoints.EndPoints;
import com.corpogas.corpoapp.VentaCombustible.FormasPago;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MostrarCarritoTransacciones extends AppCompatActivity {
    ListView list, list2;
    String EstacionId, sucursalId, ipEstacion, posicioncarga, usuarioid,  usuarioclave, cadenaproducto, lugarproviene, cadenaRespuesta, NombreCompleto, EmpleadoNumero;
    List<String> ID;
    List<String> NombreProducto;
    List<String> PrecioProducto;
    List<String> ClaveProducto;
    List<String> ProductosId;
    List<String> TipoProductoId;
    List<String> DescripcionProducto;
    List<String> Cantidad;
    List<String> ImporteProducto;
    TextView txtEtiqueta;
    Double MontoenCanasta=0.00;
    Long numerooperativa, posicioncargaid;

    String[] maintitle ={
            "Vaciar Carrito", "Finalizar Venta", "Imprimir",
            "Agregar + Productos",
    };

    String[] subtitle ={
            "Elimina Productos de la transacción actual", "Finaliza la transacción actual", "Imprime el ticket de la transacción",
            "Agregar productos a la transacción actual",
    };

    Integer[] imgid={
            R.drawable.vaciarproductos, R.drawable.finaliza, R.drawable.imprimir,
            R.drawable.agregarproductos,
    };

    RecyclerView recyclerView;
    ArrayList<MainModel> mainModels;
    MainAdapter mainAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_carrito_transacciones);

        SQLiteBD db = new SQLiteBD(getApplicationContext());
        this.setTitle(db.getNombreEstacion() + " ( EST.:" + db.getNumeroEstacion() + ")");
        EstacionId = db.getIdEstacion();
        sucursalId = db.getIdSucursal();
        ipEstacion = db.getIpEstacion();

        posicioncarga = getIntent().getStringExtra("posicion");
        posicioncargaid = getIntent().getLongExtra("pocioncargaid", 0);
        usuarioid = db.getUsuarioId();//getIntent().getStringExtra("usuario");
        cadenaproducto = getIntent().getStringExtra("cadenaproducto");
        lugarproviene = getIntent().getStringExtra("lugarproviene");
        numerooperativa = getIntent().getLongExtra("numeroOperativa", 0);
        usuarioclave = db.getClave();//getIntent().getStringExtra("clave");
        NombreCompleto = db.getNombreCompleto();//getIntent().getStringExtra("nombrecompleto");
        EmpleadoNumero = db.getNumeroEmpleado();//getIntent().getStringExtra("numeroEmpleado");
        list = findViewById(R.id.list);
        recyclerView = findViewById(R.id.recyclerview);

        ValidaTransaccionActiva();

        //Creando Integer Array
        Integer[] langLogo = {R.drawable.vaciarproductos, R.drawable.finaliza, R.drawable.imprimir};//}; //R.drawable.imprimir,
        String [] langName = {"Vaciar Carrito","Finaliza","Imprimir"};//,"Agregar Productos"}; //"Imprimir",
        //Inicializa ArrayList
        mainModels = new ArrayList<>();
        for (int i = 0; i<langLogo.length; i++){
            MainModel model = new MainModel(langLogo[i], langName[i]);
            mainModels.add(model);
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(
                MostrarCarritoTransacciones.this, LinearLayoutManager.HORIZONTAL, false
        );
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mainAdapter = new MainAdapter(MostrarCarritoTransacciones.this, mainModels, MostrarCarritoTransacciones.this, ipEstacion, usuarioid, usuarioclave, sucursalId, posicioncarga, numerooperativa.toString(), lugarproviene, MontoenCanasta, NombreCompleto, EmpleadoNumero);
        recyclerView.setAdapter(mainAdapter);

        mainAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        MyListAdapter adapter=new MyListAdapter(this, maintitle, subtitle,imgid);
        list2= findViewById(R.id.list2);
        list2.setAdapter(adapter);
        list2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                switch (position){
                    case 0:
                        VaciaCarrito();
                        break;
                    case 1:
                        FinalizaVenta();
                        break;
                    case 2:
                        Imprimir();
                        break;
                    case 3:
                        ContinuarVenta();
                        break;
                    default:
                }
            }
        });
    }

    private void VaciaCarrito() {
//        if (!Conexion.compruebaConexion(this)) {
//            Toast.makeText(getBaseContext(), "Sin conexión a la red ", Toast.LENGTH_SHORT).show();
//            Intent intent1 = new Intent(getApplicationContext(), Menu_Principal.class);
//            startActivity(intent1);
//            finish();
//        } else {
            String titulo = "Vaciar Carrito";
            String mensajes = "¿Estas seguro de VACIAR EL CARRITO?";
            Modales modales = new Modales(MostrarCarritoTransacciones.this);
            View viewLectura = modales.MostrarDialogoAlerta(MostrarCarritoTransacciones.this, mensajes, "SI", "NO");
            viewLectura.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String url = "http://" + ipEstacion + "/CorpogasService/api/ventaProductos/BorraProductos/sucursal/" + sucursalId + "/usuario/" + usuarioid + "/posicionCarga/" + posicioncarga;
                    // Utilizamos el metodo Post para validar la contraseña
                    StringRequest eventoReq = new StringRequest(Request.Method.DELETE, url,
                            new com.android.volley.Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        //Se instancia la respuesta del json
                                        JSONObject validar = new JSONObject(response);
                                        String correcto = validar.getString("Correcto");
                                        String mensaje = validar.getString("Mensaje");
                                        String objetorespuesta = validar.getString("ObjetoRespuesta");

                                        mensaje = "Se vacio el carrito";
                                        final Modales modales = new Modales(MostrarCarritoTransacciones.this);
                                        View view1 = modales.MostrarDialogoCorrecto(MostrarCarritoTransacciones.this, mensaje);
                                        view1.findViewById(R.id.buttonAction).setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                Intent intent = new Intent(getApplicationContext(), Menu_Principal.class);
                                                startActivity(intent);
                                                finish();
                                                modales.alertDialog.dismiss();
                                            }
                                        });
                                    } catch (JSONException e) {
                                        //herramienta  para diagnostico de excepciones
                                        //e.printStackTrace();
                                        String titulo = "AVISO";
                                        String mensaje = "Error en el borrado";
                                        Modales modales = new Modales(MostrarCarritoTransacciones.this);
                                        View view1 = modales.MostrarDialogoAlertaAceptar(MostrarCarritoTransacciones.this, mensaje, titulo);
                                        view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                modales.alertDialog.dismiss();
                                            }
                                        });
                                    }
                                }
                                //funcion para capturar errores
                            }, new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();
                            //VolleyLog.e("Error: ", volleyError.getMessage());
                            String algo = new String(error.networkResponse.data);
                            try {
                                //creamos un json Object del String algo
                                JSONObject errorCaptado = new JSONObject(algo);
                                //Obtenemos el elemento ExceptionMesage del errro enviado
                                String errorMensaje = errorCaptado.getString("ExceptionMessage");
                                try {
                                    String titulo = "AVISO";
                                    String mensaje = "" + errorMensaje;
                                    Modales modales = new Modales(MostrarCarritoTransacciones.this);
                                    View view1 = modales.MostrarDialogoAlertaAceptar(MostrarCarritoTransacciones.this, mensaje, titulo);
                                    view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            modales.alertDialog.dismiss();
                                            Intent intent = new Intent(getApplicationContext(), Menu_Principal.class);
                                            startActivity(intent);
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
                    });
                    // Añade la peticion a la cola
                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    eventoReq.setRetryPolicy(new DefaultRetryPolicy(12000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    requestQueue.add(eventoReq);
                    //-------------------------Aqui termina el volley --------------
                }
            });
            viewLectura.findViewById(R.id.buttonNo).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    modales.alertDialog.dismiss();
                }
            });
//        }
    }


    private void FinalizaVenta() {
//        if (!Conexion.compruebaConexion(this)) {
//            Toast.makeText(getBaseContext(), "Sin conexión a la red ", Toast.LENGTH_SHORT).show();
//            Intent intent1 = new Intent(getApplicationContext(), Menu_Principal.class);
//            startActivity(intent1);
//            finish();
//        } else {
            String titulo = "FINALIZAR VENTA";
            String mensajes = "¿Estas seguro de FINALIZAR LA VENTA?";
            Modales modales = new Modales(MostrarCarritoTransacciones.this);
            View viewLectura = modales.MostrarDialogoAlerta(MostrarCarritoTransacciones.this, mensajes, "SI", "NO");
            viewLectura.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Utilizamos el metodo POST para  finalizar la Venta
                    String url = "http://" + ipEstacion + "/CorpogasService/api/Transacciones/finalizaVenta/sucursal/" + sucursalId + "/posicionCarga/" + posicioncarga + "/usuario/" + usuarioid;
                    StringRequest eventoReq = new StringRequest(Request.Method.POST, url,
                            new com.android.volley.Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONObject respuesta = new JSONObject(response);
                                        String correcto = respuesta.getString("Correcto");
                                        String mensaje = respuesta.getString("Mensaje");
                                        String objetoRespuesta = respuesta.getString("ObjetoRespuesta");
                                        if (objetoRespuesta.equals("null")) {
                                            try {
                                                String titulo = "AVISO";
                                                //String mensaje = "No hay Posiciones de Carga para Finalizar Venta";
                                                Modales modales = new Modales(MostrarCarritoTransacciones.this);
                                                View view1 = modales.MostrarDialogoAlertaAceptar(MostrarCarritoTransacciones.this, mensaje, titulo);
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

                                        } else {
                                            try {
                                                String titulo = "AVISO";
                                                String mensajes = "Venta Finalizada";
                                                final Modales modales = new Modales(MostrarCarritoTransacciones.this);
                                                View view1 = modales.MostrarDialogoCorrecto(MostrarCarritoTransacciones.this, mensajes);
                                                view1.findViewById(R.id.buttonAction).setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        modales.alertDialog.dismiss();
                                                        Intent intent = new Intent(getApplicationContext(), Menu_Principal.class);
                                                        startActivity(intent);
                                                        finish();
                                                    }
                                                });
                                            } catch (Exception e) {
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
                            Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() {
                            // Colocar parametros para ingresar la  url
                            Map<String, String> params = new HashMap<String, String>();
                            return params;
                        }
                    };
                    // Añade la peticion a la cola
                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    eventoReq.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    requestQueue.add(eventoReq);
                }
            });
            viewLectura.findViewById(R.id.buttonNo).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    modales.alertDialog.dismiss();
                }
            });
//        }
    }

    private void Imprimir(){
        String titulo = "IMPRIMIR";
        String mensajes = "¿Estas seguro de IMPRIMIR venta?";
        Modales modales = new Modales(MostrarCarritoTransacciones.this);
        View viewLectura = modales.MostrarDialogoAlerta(MostrarCarritoTransacciones.this, mensajes,  "SI", "NO");
        viewLectura.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), FormasPago.class);
                //intent.putExtra("estacionjarreo", Estacionjarreo);
                intent.putExtra("clavedespachador", usuarioclave);
                intent.putExtra("posicionCarga",posicioncarga);
                intent.putExtra("IdOperativa", numerooperativa);
                intent.putExtra("IdUsuario", usuarioid);
                intent.putExtra("nombrecompleto", NombreCompleto);
                intent.putExtra("estacionjarreo", "");
                intent.putExtra("montoenCanasta", MontoenCanasta);
                intent.putExtra("clavedespachador", usuarioclave);
                intent.putExtra("numeroempleadosucursal", EmpleadoNumero);
                startActivity(intent);
                finish();

            }
        });
        viewLectura.findViewById(R.id.buttonNo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modales.alertDialog.dismiss();
            }
        });
    }


    private void ContinuarVenta() {
//        String mensajes;
//        String titulo = "CONTINUAR VENTA";
//
//        if (lugarproviene.equals("SoloProductos")) {
//            mensajes = "Estas seguro de AGREGAR  más productos?";
//        }
//        else{
//            mensajes = "Estas seguro de AGREGAR  un Combustible?";
//        }
//        Modales modales = new Modales(MostrarCarritoTransacciones.this);
//        View viewLectura = modales.MostrarDialogoAlerta(MostrarCarritoTransacciones.this, mensajes,  "SI", "NO");
//        viewLectura.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //Envia a Ventas Productos
//                Intent intente = new Intent(getApplicationContext(), VentasProductos.class);
//                if (lugarproviene.equals("SoloProductos")) {
//                    //se envia el id seleccionado a la clase Usuario Producto
//                    intente.putExtra("posicion", posicioncarga);
//                    intente.putExtra("usuario", usuarioid);
//                    intente.putExtra("cadenaproducto", "");
//                    intente.putExtra("lugarproviene", "SoloProductos");
//                    intente.putExtra("numeroOperativa", numerooperativa);
//                    startActivity(intente);
//                    //Finaliza activity
//                    finish();
//                }else{
//                    if (lugarproviene.equals("Predeterminado")) {
//                        //Se llama la clase para la clave del usuario
//                        Intent intent = new Intent(getApplicationContext(), eligeLitrosPrecio.class);
//                        //se envia el id seleccionado a la clase Usuario Producto
//                        intent.putExtra("carga", posicioncarga);
//                        intent.putExtra("clave", usuarioclave);
//                        intent.putExtra("usuarioid", usuarioid);
//                        //Ejecuta la clase del Usuario producto
//                        startActivity(intent);
//                        //Finaliza activity
//                        finish();
//
//                    }else{
//                        solicitadespacho();
//                    }
//                }
//                modales.alertDialog.dismiss();
//            }
//        });
//        viewLectura.findViewById(R.id.buttonNo).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                modales.alertDialog.dismiss();
//            }
//        });
    }

    private void solicitadespacho() {
//        if (!Conexion.(this)) {
//            Toast.makeText(getBaseContext(), "Sin conexión a la red ", Toast.LENGTH_SHORT).show();
//            Intent intent1 = new Intent(getApplicationContext(), Menu_Principal.class);
//            startActivity(intent1);
//            finish();
//        } else {
            String url = "http://" + ipEstacion + "/CorpogasService/api/despachos/autorizaDespacho/posicionCargaId/" + posicioncarga + "/usuarioId/" + EmpleadoNumero; //usuarioid

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new com.android.volley.Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject respuesta = new JSONObject(response);
                        String correctoautoriza = respuesta.getString("Correcto");
                        String mensajeautoriza = respuesta.getString("Mensaje");
                        String objetoRespuesta = respuesta.getString("ObjetoRespuesta");
                        if (correctoautoriza.equals("true")) {
                            enviaMunu();
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
//        }
    }

    private void ValidaTransaccionActiva() {
        MontoenCanasta = 0.00;
        txtEtiqueta = findViewById(R.id.txtEtiqueta);

        //Declaramos la lista de titulo
        ID = new ArrayList<String>();

        NombreProducto = new ArrayList<String>();
        PrecioProducto = new ArrayList<>();
        ClaveProducto = new ArrayList();
        //codigoBarras = new ArrayList();
        //ExistenciaProductos = new ArrayList();
        ProductosId = new ArrayList();
        TipoProductoId = new ArrayList();
        DescripcionProducto = new ArrayList();
        Cantidad = new ArrayList();
        ImporteProducto = new ArrayList();

        String TipoProducto, ProductoId, NumeroInterno, Descripcion, Monto, Precio, Importe, ImporteTotal;
        DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
        simbolos.setDecimalSeparator('.');
        DecimalFormat df = new DecimalFormat("#,###.00##",simbolos);
        df.setMaximumFractionDigits(2);

        cadenaRespuesta = getIntent().getStringExtra("cadenarespuesta");
        try {
            JSONArray ArregloCadenaRespuesta = new JSONArray(cadenaRespuesta);
            for (int i = 0; i < ArregloCadenaRespuesta.length(); i++) {
                JSONObject ObjetoCadenaRespuesta = ArregloCadenaRespuesta.getJSONObject(i);
                TipoProducto = ObjetoCadenaRespuesta.getString("TipoProducto");
                ProductoId= ObjetoCadenaRespuesta.getString("ProductoId");
                NumeroInterno= ObjetoCadenaRespuesta.getString("NumeroInterno");
                Descripcion= ObjetoCadenaRespuesta.getString("Descripcion");
                Monto= ObjetoCadenaRespuesta.getString("Cantidad");
                Precio= ObjetoCadenaRespuesta.getString("Precio");
                Importe = ObjetoCadenaRespuesta.getString("Importe");
                ImporteTotal = ObjetoCadenaRespuesta.getString("ImporteTotal");

                Double aTotal;
                String fTotal;
                aTotal = Double.parseDouble(ImporteTotal);//Double.parseDouble(Monto) * Double.parseDouble(Precio);
                MontoenCanasta = MontoenCanasta + aTotal;
                fTotal= df.format(aTotal);
                String CantidadT;
                if (TipoProducto.equals("1")){
                    CantidadT = df.format(Double.parseDouble(Monto)) + " LITROS";

                }else{
                    CantidadT = Monto;
                }
                TipoProductoId.add(TipoProducto);
                ProductosId.add(ProductoId);
                ClaveProducto.add(NumeroInterno);
                NombreProducto.add("Cantidad: "+ CantidadT +"; $ "+ fTotal.toString()); //
                DescripcionProducto.add(Descripcion);
                Cantidad.add(Monto);
                PrecioProducto.add(Precio);
                ImporteProducto.add(Importe);
            }
            txtEtiqueta.setText("Productos en el carrito: $ "+df.format(MontoenCanasta)+"  PC: " + posicioncargaid); //posicioncarga
            final ListAdapterProductos adapterP = new ListAdapterProductos(this,  DescripcionProducto ,   NombreProducto);
            list=(ListView)findViewById(R.id.list);

            list.setTextFilterEnabled(true);
            list.setAdapter(adapterP);
            //list.setBackgroundColor(getResources().getColor(R.color.light_blue));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void enviaMunu() {
        String titulo = "AVISO";
        String mensaje = "Listo para Iniciar Despacho";
        final Modales modales = new Modales(MostrarCarritoTransacciones.this);
        View view1 = modales.MostrarDialogoCorrecto(MostrarCarritoTransacciones.this,mensaje);
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
}
