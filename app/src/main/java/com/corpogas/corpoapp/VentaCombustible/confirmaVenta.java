package com.corpogas.corpoapp.VentaCombustible;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.corpogas.corpoapp.Conexion;
import com.corpogas.corpoapp.Configuracion.SQLiteBD;
import com.corpogas.corpoapp.Menu_Principal;
import com.corpogas.corpoapp.Modales.Modales;
import com.corpogas.corpoapp.Productos.ListAdapterProductos;
import com.corpogas.corpoapp.Productos.VentasProductos;
import com.corpogas.corpoapp.R;

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

public class confirmaVenta extends AppCompatActivity {
    String usuario, posicion, cadenaproductos, nombreproducto;
    ListView list, list2;
    Double MontoTotal=0.00;
    Button Cobrar, Agregar, Eliminar;
    List<String> ID;
    List<String> NombreProducto;
    List<String> PrecioProducto;
    List<String> ClaveProducto;
    List<String> codigoBarras;
    List<String> ExistenciaProductos;
    List<String> ProductosId;
    List<String> TipoProductoId;
    List<String> DescripcionProducto;
    List<String> Cantidad;
    JSONArray myArray = new JSONArray();
    String EstacionId, sucursalId, ipEstacion, tipoTransaccion, numerodispositivo ;
    TextView txttotal, txtdespachosolicitado, txtproducto;
    String lugarproviene, lugarProviene, numerooperativa ;
    Long NumeroEmpleado, islaId;
    int idSeleccionado;
    JSONObject myJOProductosFaltantes;
    SQLiteBD db;
    ProgressDialog bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirma_venta);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = new SQLiteBD(getApplicationContext());
        EstacionId = db.getIdEstacion();
        sucursalId = db.getIdSucursal();
        ipEstacion= db.getIpEstacion();
        this.setTitle(db.getRazonSocial());
        this.setTitle(db.getNombreEstacion() + " ( EST.:" + db.getNumeroEstacion() + ")");
        tipoTransaccion = "1"; //Transaccion Normal
        numerodispositivo = db.getIdTarjtero(); // "1";
        txttotal=findViewById(R.id.txttotal);
        txtdespachosolicitado=findViewById(R.id.txtdespachosolicitado);
        txtproducto=findViewById(R.id.txtproducto);

        numerooperativa = getIntent().getStringExtra("numeroOperativa");
        posicion = getIntent().getStringExtra("posicion");
        usuario = db.getUsuarioId();//getIntent().getStringExtra("usuario");
        cadenaproductos = getIntent().getStringExtra("cadenaproducto");
        nombreproducto = getIntent().getStringExtra("Descripcion");

        lugarproviene = getIntent().getStringExtra("lugarproviene");
        lugarProviene = getIntent().getStringExtra("lugarProviene");
        islaId = getIntent().getLongExtra("numeroisla",0);
        NumeroEmpleado = Long.parseLong(db.getNumeroEmpleado());// getIntent().getLongExtra("numeroempleado", 0);


        Cobrar = findViewById(R.id.comprar);
        Agregar = findViewById(R.id.btnagregar);
        Eliminar = findViewById(R.id.eliminar);

        if (lugarproviene.equals("SoloProductos")){
            txtdespachosolicitado.setVisibility(View.INVISIBLE);
        }else{
            if (lugarproviene.equals("Corte")){
                txtdespachosolicitado.setVisibility(View.INVISIBLE);

            }
            //                txtdespachosolicitado.setVisibility(View.VISIBLE);

        }

        Cobrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ////AgregarDespacho(posicion, usuarioid);
                if (lugarproviene.equals("Corte")){
                    EnviarProductosCorte();
                }else{
                bar = new ProgressDialog(confirmaVenta.this);
                bar.setTitle("Realizando venta");
                bar.setMessage("Ejecutando... ");
                bar.setIcon(R.drawable.productos);
                bar.setCancelable(false);
                bar.show();

                if (myArray.length() == 0){
                    String titulo = "AVISO";
                    String mensaje = "No se agregó ningún producto";
                    final Modales modales = new Modales(confirmaVenta.this);
                    View view1 = modales.MostrarDialogoCorrecto(confirmaVenta.this,mensaje);
                    view1.findViewById(R.id.buttonAction).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            modales.alertDialog.dismiss();
                            bar.cancel();
                            finish();
                        }
                    });
                }else{
                    EnviarProductos();
                }
                }
            }
        });

        Agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), VentasProductos.class);
                intent.putExtra("posicion", posicion);
                intent.putExtra("usuario", usuario);
                intent.putExtra("cadenaproducto", myArray.toString());
                startActivity(intent);
                finish();

            }
        });

        Eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        despliegadatos();
    }

    private void correcto() throws JSONException {
        String titulo = "AVISO";
        String mensaje = "Venta Realizada";
        final Modales modales = new Modales(confirmaVenta.this);
        View view1 = modales.MostrarDialogoCorrecto(confirmaVenta.this,mensaje);
        view1.findViewById(R.id.buttonAction).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modales.alertDialog.dismiss();
//                Intent intente = new Intent(getApplicationContext(), Menu_Principal.class);
//                startActivity(intente);
                bar.cancel();
                finish();
            }
        });
    }

    private void EnviarProductosCorte() {  //final String posicionCarga, final String Usuarioid, final Integer ProductoIdEntero
            String url = "http://"+ipEstacion+"/CorpogasService/api/ventaProductos/GuardaProducto/sucursal/"+sucursalId+"/numeroEmpleado/"+ db.getNumeroEmpleado();
            RequestQueue queue = Volley.newRequestQueue(this);

            JsonObjectRequest request_json = new JsonObjectRequest(Request.Method.POST, url, myJOProductosFaltantes, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    String estado = null;
                    String mensaje = null;
                    try {
                        estado = response.getString("Correcto");
                        mensaje = response.getString("Mensaje");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (estado.equals("true")){
                        try {
                            final Modales modales = new Modales(confirmaVenta.this);
                            View view1 = modales.MostrarDialogoCorrecto(confirmaVenta.this,"Venta Realizada Correctamente");

                            view1.findViewById(R.id.buttonAction).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    modales.alertDialog.dismiss();
                                    finish();

                                }
                            });
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }else{
                        try {
                            Modales modales = new Modales(confirmaVenta.this);
                            modales.MostrarDialogoError(confirmaVenta.this,mensaje);

                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            }){
                public Map<String,String>getHeaders() throws AuthFailureError {
                    Map<String,String> headers = new HashMap<String, String>();
                    return headers;
                }
                protected  Response<JSONObject> parseNetwokResponse(NetworkResponse response){
                    if (response != null){

                        try {
                            String responseString;
                            JSONObject datos = new JSONObject();
                            responseString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));

                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                    return Response.success(myJOProductosFaltantes, HttpHeaderParser.parseCacheHeaders(response));
                }
            };
            queue.add(request_json);
    }

    private void EnviarProductos() {
        if (!Conexion.compruebaConexion(this)) {
            Toast.makeText(getBaseContext(), "Sin conexión a la red ", Toast.LENGTH_SHORT).show();
            Intent intent1 = new Intent(getApplicationContext(), Menu_Principal.class);
            startActivity(intent1);
            finish();
        } else {
            //RequestQueue queue = Volley.newRequestQueue(this);
            String url = "http://" + ipEstacion + "/CorpogasService/api/ventaProductos/GuardaProductos/sucursal/" + sucursalId + "/origen/" + numerodispositivo + "/usuario/" + usuario + "/posicionCarga/" + posicion;
            RequestQueue queue = Volley.newRequestQueue(this);

            JsonArrayRequest request_json = new JsonArrayRequest(Request.Method.POST, url, myArray,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            //Get Final response
                            //Toast.makeText(confirmaVenta.this, "Venta Realizada", Toast.LENGTH_SHORT).show();
                            try {
                                correcto();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(confirmaVenta.this);
                            builder.setTitle("Venta Productos");
                            builder.setMessage(errorMensaje)
                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
//                                            Intent intente = new Intent(getApplicationContext(), Menu_Principal.class);
//                                            startActivity(intente);
                                            bar.cancel();
                                        }
                                    }).show();
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
                protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {
                    String responseString;
                    JSONArray array = new JSONArray();
                    if (response != null) {

                        try {
                            responseString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                            //transaccionId =
                            JSONObject obj = new JSONObject(responseString);
                            //Si es valido se asignan valores
                            //Intent intent = new Intent(getApplicationContext(), productoFormapago.class);
                            //DAtos enviados a formaPago
                            //intent.putExtra("posicion",posicionCarga);
                            //intent.putExtra("usuario",Usuarioid);
                            ////startActivity(intent);
                            //Toast.makeText(getApplicationContext(), "Venta realizada", Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    //return array;
                    return Response.success(myArray, HttpHeaderParser.parseCacheHeaders(response));
                }
            };
            queue.add(request_json);
        }
    }

    private void despliegadatos(){
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

        try {
            myJOProductosFaltantes = new JSONObject();
            JSONArray response = new JSONArray(cadenaproductos);
            myArray = response;
            for (int i=0; i< response.length(); i++){
                JSONObject producto = response.getJSONObject(i);
                String numerointerno = producto.getString("NumeroInterno");
                String cantidad = producto.getString("Cantidad");
                String precio = producto.getString("Precio");
                String tproductoid = producto.getString("TipoProducto");
                String descripcioncorta = producto.getString("Descripcion");
                String productosid = producto.getString("ProductoId");
                Double monto = Double.parseDouble(precio)*Double.parseDouble(cantidad);
                MontoTotal = MontoTotal +  monto;

                ID.add(descripcioncorta);

                NombreProducto.add("ID: " + numerointerno + "    |     $"+precio);
                PrecioProducto.add(precio);
                ClaveProducto.add(numerointerno);
                //codigoBarras.add(codigobarras);
                //ExistenciaProductos.add(IdProductos);
                ProductosId.add(productosid);
                TipoProductoId.add(tproductoid);
                DescripcionProducto.add(descripcioncorta);
                Cantidad.add(cantidad);

                myJOProductosFaltantes.put("TipoProducto", tproductoid);
                myJOProductosFaltantes.put("ProductoId", productosid);
                myJOProductosFaltantes.put("NumeroInterno", numerointerno);
                myJOProductosFaltantes.put("Descripcion", descripcioncorta);
                myJOProductosFaltantes.put("Cantidad", cantidad);
                myJOProductosFaltantes.put("Precio", precio);

            }
            //txttotal.setText("TOTAL :  "+ UFormat(MontoTotal));

            DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
            simbolos.setDecimalSeparator('.');
            DecimalFormat df = new DecimalFormat("#,###.00##",simbolos);

            df.setMaximumFractionDigits(2);
            txttotal.setText("TOTAL : $" + df.format(MontoTotal));

            //txttotal.setText("TOTAL : " + String.format("%.2f",MontoTotal));

            final ListAdapterProductos adapterP = new ListAdapterProductos(this,  ID ,  NombreProducto);
            list=(ListView)findViewById(R.id.list);
            list.setTextFilterEnabled(true);
            list.setAdapter(adapterP);
//        Agregado  click en la lista
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int identificador, long l) {
                    String titulo = "Estas seguro?";
                    String mensajes = "Deseas eliminar el elemento seleccionado?";
                    Modales modales = new Modales(confirmaVenta.this);
                    View viewLectura = modales.MostrarDialogoAlerta(confirmaVenta.this, mensajes,  "SI", "NO");
                    viewLectura.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
//                            if (ID.size() == 1) {
//                                Toast.makeText(confirmaVenta.this, "No se puede eliminar ya que es el último elemento de la lista", Toast.LENGTH_SHORT).show();
//                            }else{
                                ID.remove(identificador);
                                Cantidad.remove(identificador);
                                adapterP.notifyDataSetChanged();

                                NombreProducto.remove(identificador);
                                PrecioProducto.remove(identificador);
                                ClaveProducto.remove(identificador);
                                //codigoBarras.add(codigobarras);
                                //ExistenciaProductos.add(IdProductos);
                                ProductosId.remove(identificador);
                                TipoProductoId.remove(identificador);
                                DescripcionProducto.remove(identificador);

                                final ListAdapterProductos adapterP = new ListAdapterProductos(confirmaVenta.this, ID, Cantidad);

                                EliminarIdentificador(identificador);
//                            }
                            modales.alertDialog.dismiss();
                        }
                    });
                    viewLectura.findViewById(R.id.buttonNo).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            modales.alertDialog.dismiss();
                        }
                    });



                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void EliminarIdentificador(int identificador){
        myArray = new JSONArray();
        MontoTotal =0.0;

        for (int m = 0; m<ID.size(); m++) {
            JSONObject mjason = new JSONObject();
            try {
                mjason.put("TipoProducto", TipoProductoId.get(m));
                mjason.put("ProductoId", ProductosId.get(m));
                mjason.put("NumeroInterno", ClaveProducto.get(m));
                //mjason.put("Descripcion", descrProducto.toString());
                mjason.put("Cantidad", Cantidad.get(m));
                mjason.put("Precio", PrecioProducto.get(m));
                mjason.put("Descripcion", DescripcionProducto.get(m));
                myArray.put(mjason);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        cadenaproductos = myArray.toString();
        despliegadatos();

    }

    //Metodo para regresar a la actividad principal
    @Override
    public void onBackPressed() {
        if (lugarproviene.equals("Corte")){
            finish();
        }else{
            Intent intent = new Intent(getApplicationContext(), Menu_Principal.class);
            startActivity(intent);
            finish();
        }
    }

}