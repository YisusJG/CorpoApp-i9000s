package com.corpogas.corpoapp.Puntada;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.corpogas.corpoapp.Configuracion.SQLiteBD;
import com.corpogas.corpoapp.Menu_Principal;
import com.corpogas.corpoapp.Modales.Modales;
import com.corpogas.corpoapp.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductosARedimir extends AppCompatActivity {
    DecimalFormat formatoCifras;

    Button btnAgregar,btnEnviar, incrementar, decrementar, btnMostrarCombustibles, btnMostrarProductos;
    TextView cantidadProducto, txtSaldo, txtSaldoUtilizado, txtDescripcionProducto, txtproductos, txtcantidad, txtpesos;
    EditText Producto, existenias;
    String cantidad, TipoProductoSeleccionado;
    JSONObject mjason = new JSONObject();
    ListView list;
    EditText litros, pesos;
    Button agregarcombustible, imprimirTicket, enviarProductos, limpiar;
    String IdCombustible, cs,numneroInterno,  descripcion, precio, IdCombus, Costo, NIP;
    double  LitrosCoversion;
    final JSONObject datos = new JSONObject();
    String EstacionId, sucursalId, ipEstacion, numeroTarjetero;

    String folio, transaccion, lugarProviene, textoresultado;
    List<String> ID;
    JSONArray array1 = new JSONArray();
    JSONArray myArrayVer = new JSONArray();

    List<String> NombreProducto;
    List<String> PrecioProducto;
    List<String> ClaveProducto;
    List<String> CodigoBarras;
    List<String> ExistenciaProductos;
    List<String> ProductoId, CategoriaId, tipoprod;
    String tipoproductofinal, productoIdfinal, numeroInternofinal, Descripcionfinal, cantidadfinal, preciofinal, enviadoDesde ;
    EditText productoIdSeleccionado, totalproductosaCargar;
    String TipoCombustible= "1";
    String banderaProducto, banderaCombustible, fechaTicket="",  nombreatendio, posicionCarga;
    Long idUsuario;
    Double descuento;

    //variables para imprimir
    public String tag = "PrintActivity-Robert2";
    final int PRINT_EFECTIVO = 1;
    final int PRINT_VALES = 2;
    final int PRINT_OTRAS_TARJETAS = 3;
    final int PRINT_ACUMULAR = 4;
    final int PRINT_TANQUELLENO = 5;
    final int PRINT_REDIMIR=6;
    final int PRINT_JARREO = 7;
    final int PRINT_AUTOJARREO = 8;
    //    final int PRINT_VALE_ELCTRONICA =3;
    int ret = -1;
    SQLiteBD db;

    ProgressDialog bar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productos_a_redimir);
        db = new SQLiteBD(getApplicationContext());
        this.setTitle(db.getNombreEstacion() + " ( EST.:" + db.getNumeroEstacion() + ")");
        EstacionId = db.getIdEstacion();
        sucursalId = db.getIdSucursal();
        ipEstacion = db.getIpEstacion();
        numeroTarjetero = db.getIdTarjtero();
        lugarProviene = "";
        textoresultado="";
        banderaProducto = "false";
        banderaCombustible = "false";

        formatoCifras = new DecimalFormat("#,###.##");

        posicionCarga = getIntent().getStringExtra("pos");
        String saldos = String.format("$%.2f",Double.parseDouble(getIntent().getStringExtra("saldo")));
        idUsuario = getIntent().getLongExtra("empleadoid", 0);
        nombreatendio = getIntent().getStringExtra("nombreatendio");

        descuento = getIntent().getDoubleExtra("descuentouno", 0.0);
        enviadoDesde = getIntent().getStringExtra("lugarproviene");

 //      enviamos el saldo disponible para poder visualizar

        txtpesos = findViewById(R.id.txtPesos);
        txtcantidad = findViewById(R.id.etiquetacantidad);
        txtSaldo= findViewById(R.id.txtSaldos);
        txtSaldo.setText(saldos);

        txtSaldoUtilizado=findViewById(R.id.txtSaldosUtilizado);
        txtSaldoUtilizado.setText(saldos);

        litros = findViewById(R.id.edtLitros);
        litros.setEnabled(false);
        pesos=findViewById(R.id.edtPesos);
        pesos.setEnabled(false);

        productoIdSeleccionado = findViewById(R.id.edtProductoId);
        txtDescripcionProducto = findViewById(R.id.txtDescripcion);
        totalproductosaCargar = findViewById(R.id.edtCantidadProducto);
        existenias = findViewById(R.id.existencias);
        incrementar = findViewById(R.id.btnIncrementar);
        incrementar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (lugarProviene.equals("Producto")) {
                    Aumentar();
                }else{
                    if (lugarProviene.equals("")){
                        Toast.makeText(ProductosARedimir.this, "Seleccione un Tipo de producto COMBUSTIBLES/ACEITES", Toast.LENGTH_SHORT).show();

                    }else {
                        Toast.makeText(ProductosARedimir.this, "No aplica para combustibles", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        decrementar = findViewById(R.id.btnDecrementar);
        decrementar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (lugarProviene.equals("Producto")) {
                    Decrementar();
                }else{
                    if (lugarProviene.equals("")){
                        Toast.makeText(ProductosARedimir.this, "Seleccione un Tipo de producto COMBUSTIBLES/ACEITES", Toast.LENGTH_SHORT).show();

                    }else {
                        Toast.makeText(ProductosARedimir.this, "No aplica para combustibles", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


        agregarcombustible = findViewById(R.id.btnAgregarProducto);
        agregarcombustible.setOnClickListener(new View.OnClickListener() {
            JSONObject jsonParam = new JSONObject();

            @Override
            public void onClick(View v) {
                if (txtDescripcionProducto.length() == 0) {
                    String titulo = "AVISO";
                    String mensaje = "Seleccione uno de los Productos";
                    Modales modales = new Modales(ProductosARedimir.this);
                    View view1 = modales.MostrarDialogoAlertaAceptar(ProductosARedimir.this,mensaje,titulo);
                    view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            modales.alertDialog.dismiss();
                        }
                    });
                }else{
                    txtproductos = findViewById(R.id.txtproductos);
                    Integer TipoProducto=2;
                    Double SaldoPorUtilizar;
                    SaldoPorUtilizar= Double.parseDouble(txtSaldoUtilizado.getText().toString().replace("$",""));

                    //if (litros.getText().toString().isEmpty()) {
                    //if (pesos.getText().toString().isEmpty()) {
                    if (lugarProviene.equals("Producto")){
                        // Se carga Producto
                        Integer cantidad = Integer.parseInt(totalproductosaCargar.getText(). toString());
                        try {
                            boolean bandera=true;
                            if (array1.length()>0  ) {
                                for (int i = 0; i < array1.length(); i++) {
                                    try {
                                        JSONObject jsonObject = array1.getJSONObject(i);
                                        if (jsonObject.has("ProductoId")) {
                                            String valor = jsonObject.getString("ProductoId");
                                            int res = Integer.parseInt(valor);
                                            if (res == Integer.parseInt(productoIdfinal) && TipoProducto != 1){
                                                bandera=false;
                                                break;
                                            }
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                            if (bandera) {

                                if (SaldoPorUtilizar>= (Double.parseDouble(preciofinal)*cantidad)) {
                                    JSONObject jsonParam = new JSONObject();

                                    jsonParam.put("TipoProducto", tipoproductofinal);
                                    jsonParam.put("ProductoId", numeroInternofinal); //productoIdfinal
                                    jsonParam.put("NumeroInterno", numeroInternofinal);
                                    jsonParam.put("Descripcion", txtDescripcionProducto.getText().toString());
                                    jsonParam.put("Cantidad", totalproductosaCargar.getText().toString());
                                    jsonParam.put("Precio", preciofinal);
                                    array1.put(jsonParam);


                                    JSONObject mjasonver = new JSONObject();
                                    mjasonver.put("TipoProducto:", tipoproductofinal);
                                    mjasonver.put("Producto:", Integer.parseInt(numeroInternofinal));
                                    mjasonver.put("Cantidad:", totalproductosaCargar.getText().toString());
                                    mjasonver.put("Precio:", preciofinal);
                                    myArrayVer.put(mjasonver);

                                    String txtproducto = "Producto: " + numeroInternofinal;  //Integer.parseInt(numeroInternofinal);
                                    String txtcantidad = "Cantidad: " + totalproductosaCargar.getText().toString();
                                    String txtprecio = "Precio: " + String.format("$%.2f", preciofinal);



                                    textoresultado = textoresultado + " " + txtproducto + " " + txtcantidad + " " + txtprecio + "     ";
                                    txtproductos.setText(textoresultado);//myArrayVer.toString());

                                    Double resultado = SaldoPorUtilizar - Double.parseDouble(preciofinal);
                                    txtSaldoUtilizado.setText(String.format(resultado.toString()));
                                    txtDescripcionProducto.setText("");
                                    totalproductosaCargar.setText("1");
                                    banderaProducto = "true";
                                }else{
                                    String titulo = "Producto no agregado ";
                                    String mensaje = "El precio del producto es mayor que el saldo de la tarjeta";
                                    Modales modales = new Modales(ProductosARedimir.this);
                                    View view1 = modales.MostrarDialogoAlertaAceptar(ProductosARedimir.this,mensaje,titulo);
                                    view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            modales.alertDialog.dismiss();
                                            txtDescripcionProducto.setText("");
                                            totalproductosaCargar.setText("1");
                                        }
                                    });
                                }
                            }else{
                                String titulo = "AVISO";
                                String mensaje = "Producto: "+ txtDescripcionProducto.getText().toString() +" cargado anteriormente";
                                Modales modales = new Modales(ProductosARedimir.this);
                                View view1 = modales.MostrarDialogoAlertaAceptar(ProductosARedimir.this,mensaje,titulo);
                                view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        modales.alertDialog.dismiss();
                                        txtDescripcionProducto.setText("");
                                        totalproductosaCargar.setText("1");

                                    }
                                });

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        if (lugarProviene.equals("Combustible")) {
                            if ((pesos.getText().toString().isEmpty()) || (pesos.getText().toString()) == "0.00") {
                                String titulo = "Combustible no agregado ";
                                String mensaje = "Teclee la cantidad en pesos a cargar";
                                Modales modales = new Modales(ProductosARedimir.this);
                                View view1 = modales.MostrarDialogoAlertaAceptar(ProductosARedimir.this, mensaje, titulo);
                                view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        modales.alertDialog.dismiss();
                                    }
                                });
                            } else {
                                boolean bandera=true;
                                TipoProducto=1;
                                double pesospedidos = Double.valueOf(pesos.getText().toString());
                                double litrospedidos = pesospedidos / Double.parseDouble(preciofinal);  //Double.valueOf(precio);
                                String valor = String.valueOf(litrospedidos);
                                String valor1;
                                if (valor.length()>5) {
                                    valor1 = valor.substring(0, 4);
                                }else{
                                    valor1 = "1"; //valor
                                }
                                litros.setText(valor1);

                                try {
                                    if (array1.length() > 0) {
                                        for (int i = 0; i < array1.length(); i++) {
                                            try {
                                                JSONObject jsonObject = array1.getJSONObject(i);
                                                if (jsonObject.has("TipoProducto")  )  {
                                                    String valorobtenido = jsonObject.getString("ProductoId");
                                                    String tipoproductoobtenido = jsonObject.getString("TipoProducto");
                                                    int res = Integer.parseInt(valorobtenido);
                                                    if (tipoproductoobtenido.equals(TipoCombustible)){
                                                        bandera = false;
                                                        break;
                                                    }
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                    if (bandera == true) {

                                        if (SaldoPorUtilizar >= pesospedidos) {
                                            JSONObject jsonParam = new JSONObject();

                                            jsonParam.put("TipoProducto", tipoproductofinal);
                                            jsonParam.put("ProductoId", numeroInternofinal); //numeroInternofinal
                                            jsonParam.put("NumeroInterno", numeroInternofinal); //
                                            jsonParam.put("Descripcion", descripcion);
                                            jsonParam.put("Cantidad", pesospedidos);
                                            //jsonParam.put("Precio", Double.parseDouble(preciofinal));
                                            jsonParam.put("Importe", true);
                                            array1.put(jsonParam);

                                            JSONObject mjasonver = new JSONObject();

                                            mjasonver.put("TipoProducto:", tipoproductofinal);
                                            mjasonver.put("Producto:", Integer.parseInt(productoIdfinal));
                                            mjasonver.put("Cantidad:", pesospedidos);
                                            mjasonver.put("Precio:", valor1);
                                            myArrayVer.put(mjasonver);

                                            String txtproducto = "Producto: " + descripcion; //Integer.parseInt(numeroInternofinal);
                                            String txtcantidad = "Cantidad: $" + pesos.getText().toString();
                                            String txtprecio = "Precio: " + String.format("$%.2f", Double.parseDouble(preciofinal));
                                            textoresultado = textoresultado + " " + txtproducto + " " + txtcantidad + " " + txtprecio + "           ";
                                            txtproductos.setText(textoresultado);//myArrayVer.toString());

                                            Double resultado = SaldoPorUtilizar - pesospedidos;
                                            txtSaldoUtilizado.setText(String.format("$%.2f", Double.parseDouble(resultado.toString())));
                                            pesos.setText("");
                                            litros.setText("");
                                            txtDescripcionProducto.setText("");
                                            banderaCombustible = "true";
                                        } else {
                                            String titulo = "Combustible no agregado ";
                                            String mensaje = "El monto cargado es mayor que el saldo de la tarjeta";
                                            Modales modales = new Modales(ProductosARedimir.this);
                                            View view1 = modales.MostrarDialogoAlertaAceptar(ProductosARedimir.this, mensaje, titulo);
                                            view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    modales.alertDialog.dismiss();
                                                }
                                            });
                                        }
                                    }else{
                                        String titulo = "AVISO";
                                        String mensaje = "No se pueden cargar dos tipos de gasolina diferentes";
                                        Modales modales = new Modales(ProductosARedimir.this);
                                        View view1 = modales.MostrarDialogoAlertaAceptar(ProductosARedimir.this,mensaje,titulo);
                                        view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                modales.alertDialog.dismiss();
                                                txtDescripcionProducto.setText("");
                                                pesos.setText("");
                                                litros.setText("");
                                            }
                                        });
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        } else {
                            if (lugarProviene == ""){
                                Toast.makeText(ProductosARedimir.this, "Seleccione un Tipo de producto COMBUSTIBLES/ACEITES", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            }
        });

//      Los elementos anteriores son los primero que se mostraran en el activity al iniciar
        btnMostrarCombustibles = findViewById(R.id.btnCombustibles);
        btnMostrarCombustibles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                litros = findViewById(R.id.edtLitros);
                //litros.setEnabled(true);
                pesos=findViewById(R.id.edtPesos);
                pesos.setEnabled(true);
                lugarProviene = "Combustible";
                incrementar.setVisibility(View.INVISIBLE);
                decrementar.setVisibility(View.INVISIBLE);
                totalproductosaCargar.setText("1");
                totalproductosaCargar.setVisibility(View.INVISIBLE);
                txtcantidad.setVisibility(View.INVISIBLE);

                txtpesos.setVisibility(View.VISIBLE);
                pesos.setVisibility(View.VISIBLE);
                txtDescripcionProducto.setText("");
                //MostrarCombustibles();
                MostrarProductos("1");

            }
        });
        btnMostrarProductos = findViewById(R.id.btnAceites);
        btnMostrarProductos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                litros.setText("");
                pesos.setText("");
                litros = findViewById(R.id.edtLitros);
                litros.setEnabled(false);
                pesos=findViewById(R.id.edtPesos);
                pesos.setEnabled(false);
                lugarProviene = "Producto";
                incrementar.setVisibility(View.VISIBLE);
                decrementar.setVisibility(View.VISIBLE);
                totalproductosaCargar.setText("1");
                totalproductosaCargar.setVisibility(View.VISIBLE);
                txtcantidad.setVisibility(View.VISIBLE);

                txtpesos.setVisibility(View.INVISIBLE);
                pesos.setVisibility(View.INVISIBLE);
                txtDescripcionProducto.setText("");
                MostrarProductos("2");
            }
        });

        enviarProductos = findViewById(R.id.btnEnviarProducto);
        enviarProductos.setEnabled(true);
        enviarProductos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviarProductos.setEnabled(false);
                if (array1.length()==0  )       //.length() >0)
                {
                    String titulo = "AVISO";
                    String mensaje = "Seleccione al menos uno de los Productos";
                    Modales modales = new Modales(ProductosARedimir.this);
                    View view1 = modales.MostrarDialogoAlertaAceptar(ProductosARedimir.this,mensaje,titulo);
                    view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            enviarProductos.setEnabled(true);
                            modales.alertDialog.dismiss();
                        }
                    });
                } else {
                    String nip = getIntent().getStringExtra("nip");
                    try {
                        if (enviadoDesde.equals("Redencion Yena")) {
                            enviarProductosServerDesdeYena();
                        } else {
                            enviarProductosServer();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void Aumentar() {
        if (txtDescripcionProducto.length() == 0){
            String titulo = "AVISO";
            String mensaje = "Seleccione uno de los Productos";
            Modales modales = new Modales(ProductosARedimir.this);
            View view1 = modales.MostrarDialogoAlertaAceptar(ProductosARedimir.this,mensaje,titulo);
            view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    modales.alertDialog.dismiss();
                }
            });

        }else {
            cantidad = totalproductosaCargar.getText().toString();
            int numero = Integer.parseInt(cantidad);
            int totalexistencia = Integer.parseInt(existenias.getText().toString());
            if (numero < totalexistencia) {
                int total = numero + 1;
                String resultado = String.valueOf(total);
                totalproductosaCargar.setText(resultado);
            } else {
                String titulo = "AVISO";
                String mensaje = "Solo hay "+ existenias.getText().toString() + " en existencia ";
                Modales modales = new Modales(ProductosARedimir.this);
                View view1 = modales.MostrarDialogoAlertaAceptar(ProductosARedimir.this,mensaje,titulo);
                view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        modales.alertDialog.dismiss();
                    }
                });

                //Toast.makeText(getApplicationContext(), "solo hay " + existencias.getText().toString() + " en existencia ", Toast.LENGTH_LONG).show();
            }
        }
    }
    private void Decrementar() {
        if (txtDescripcionProducto.length() == 0){
            String titulo = "AVISO";
            String mensaje = "Seleccione  uno de los Productos";
            Modales modales = new Modales(ProductosARedimir.this);
            View view1 = modales.MostrarDialogoAlertaAceptar(ProductosARedimir.this,mensaje,titulo);
            view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    modales.alertDialog.dismiss();
                }
            });
        }else {
            cantidad = totalproductosaCargar.getText().toString();
            int numero = Integer.parseInt(cantidad);
            if (numero > 1) {
                int total = numero - 1;
                String resultado = String.valueOf(total);
                totalproductosaCargar.setText(resultado);
            } else {
                String titulo = "AVISO";
                String mensaje = "El valor minimo debe ser 1";
                Modales modales = new Modales(ProductosARedimir.this);
                View view1 = modales.MostrarDialogoAlertaAceptar(ProductosARedimir.this,mensaje,titulo);
                view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        modales.alertDialog.dismiss();
                    }
                });
                //Toast.makeText(getApplicationContext(), "el valor minimo debe ser 1", Toast.LENGTH_LONG).show();
            }
        }
    };

    private void MostrarProductos(String TipoProducto) {
        bar = new ProgressDialog(ProductosARedimir.this);
        bar.setTitle("Cargando Productos");
        bar.setMessage("Ejecutando... ");
        bar.setIcon(R.drawable.redimirpuntada);
        bar.setCancelable(false);
        bar.show();

        SQLiteBD data = new SQLiteBD(getApplicationContext());
        String url = "http://" + ipEstacion + "/CorpogasService/api/islas/productos/sucursal/"+sucursalId+"/posicionCargaId/"+posicionCarga.toString();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                PresentarProductos(response, TipoProducto);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString(), Toast.LENGTH_LONG).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this.getApplicationContext());
        requestQueue.add(stringRequest);
    }
    private void PresentarProductos(String response, String TipoProducto) {

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
//                        preciol = String.format("$%.2f", Double.parseDouble(Control.getString("Price")));
                        preciol = Control.getString("Price");
                        IdProductos = Control.getString("Id"); //ProductoId
                    }
                    NombreProducto.add("ID: " + idArticulo + "    |     "+preciol); // + "    |    " + IdProductos );
                    ID.add(DescLarga);
                    PrecioProducto.add(preciol);
                    ClaveProducto.add(idArticulo);
                    CodigoBarras.add(codigobarras);
                    ProductoId.add(IdProductos);
                    CategoriaId.add(categoriaid);
                } else {

                }
            }
            bar.cancel();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final ListAdapterAceitesRedimir adapterP = new ListAdapterAceitesRedimir(this,  ID, NombreProducto);
        list=(ListView)findViewById(R.id.lstPosicionCarga);
        list.setTextFilterEnabled(true);
        list.setAdapter(adapterP);
//        Agregado  click en la lista
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Double SaldoVigente, Saldo, SaldoUtilizado, precioProductoSeleccionado;
                String SaldoVigenteCadena, SaldoCadena, SaldoUtilizadoCadena;
                SaldoVigenteCadena = txtSaldo.getText().toString().replace("$","");
                SaldoUtilizadoCadena = txtSaldoUtilizado.getText().toString().replace("$","");
                SaldoVigente = Double.parseDouble(SaldoVigenteCadena);
                SaldoUtilizado = Double.parseDouble(SaldoUtilizadoCadena);
                preciofinal = PrecioProducto.get(i).toString();
                precioProductoSeleccionado = Double.parseDouble(preciofinal);

                if (precioProductoSeleccionado <= SaldoVigente){
                    Descripcionfinal = ID.get(i).toString();
                    descripcion = CategoriaId.get(i).toString();
                    numeroInternofinal= ClaveProducto.get(i).toString();
                    String existencia = ExistenciaProductos.get(i).toString();
                    productoIdfinal = ProductoId.get(i);
                    tipoproductofinal = CategoriaId.get(i);

                    productoIdSeleccionado.setText(productoIdfinal);
                    txtDescripcionProducto.setText(Descripcionfinal);
                    existenias.setText(existencia);
                } else {
                    Toast.makeText(ProductosARedimir.this, "Sin Saldo suficiente ", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void enviarProductosServer() {    //JSONArray array2, String NIP
        final SQLiteBD data = new SQLiteBD(getApplicationContext());
        String  NumeroEstacion = EstacionId;
        String PosicionDeCarga = getIntent().getStringExtra("pos");
        String NumeroDeTarjeta = getIntent().getStringExtra("track");
        String clave = getIntent().getStringExtra("clave");
        String nip = getIntent().getStringExtra("nip");
        String empleadoid = getIntent().getStringExtra("empleadoid");
        String numeroempleado = db.getNumeroEmpleado();

        String url = "http://" + ipEstacion + "/CorpogasService/api/puntadas/actualizaPuntos/NumeroEmpleado/"+numeroempleado;
        RequestQueue queue = Volley.newRequestQueue(this);
        try {
            datos.put("RequestID","37");
            datos.put("SucursalId", sucursalId);
            datos.put("PosicionCarga",PosicionDeCarga);
            datos.put("NuTarjetero", numeroTarjetero); // data.getIdTarjtero());
            datos.put("Tarjeta", NumeroDeTarjeta);
            datos.put("NIP", nip);
            if (enviadoDesde.equals("RedimirQR")){
                datos.put("importedescuento", descuento);
            }
            datos.put("Productos", array1);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request_json = new JsonObjectRequest(Request.Method.POST, url, datos, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                String estado = null;
                String mensaje = null;
                try {
                    estado = response.getString("Estado");
                    mensaje = response.getString("Mensaje");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (estado.equals("true")){
                    try {
                        String titulo = "PUNTADA REDENCION";
                        String mensajes = mensaje;
                        Modales modales = new Modales(ProductosARedimir.this);
                        View view1 = modales.MostrarDialogoCorrecto(ProductosARedimir.this,mensaje);
                        view1.findViewById(R.id.buttonAction).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                modales.alertDialog.dismiss();
                                //MODIFICCADO MIKEL 28/05/2021 para q imprima deuna vez SOLO PRODUCTOS
                                if (banderaProducto.equals("true") && banderaCombustible.equals("false")){
                                    // ImprimeTicketPuntadaSoloProductos();
                                }else{
                                    Intent intent = new Intent(ProductosARedimir.this, Menu_Principal.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }else{
                    try {
                        String titulo = "PUNTADA REDENCION";
                        String mensajes = mensaje;
                        Modales modales = new Modales(ProductosARedimir.this);
                        View view1 = modales.MostrarDialogoError(ProductosARedimir.this,mensaje);
                        view1.findViewById(R.id.buttonAction).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                modales.alertDialog.dismiss();
                                Intent intent = new Intent(ProductosARedimir.this, Menu_Principal.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ProductosARedimir.this, "error", Toast.LENGTH_SHORT).show();

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


    private void enviarProductosServerDesdeYena() {    //JSONArray array2, String NIP
        final SQLiteBD data = new SQLiteBD(getApplicationContext());
        String NumeroEstacion = EstacionId;
        String PosicionDeCarga = getIntent().getStringExtra("posicionCarga");
        String NumeroDeTarjeta = getIntent().getStringExtra("track");
        String clave = getIntent().getStringExtra("clave");
        String nip = getIntent().getStringExtra("nip");
        String empleadoid = getIntent().getStringExtra("empleadoid");
        String numeroempleado = db.getNumeroEmpleado();
        String claveTarjeta = getIntent().getStringExtra("track1");
        String numeroTarjeta = getIntent().getStringExtra("track2");

//        String url = "http://" + ipEstacion + "/CorpogasService/api/puntadas/actualizaPuntos/NumeroEmpleado/" + numeroempleado;
        String url = "http://" + ipEstacion + "/CorpogasService/Api/yenas";
        RequestQueue queue = Volley.newRequestQueue(this);
        try {
            datos.put("SucursalId", sucursalId);
            datos.put("NumeroEmpleado", numeroempleado); // data.getIdTarjtero());
            datos.put("NumeroTarjeta", numeroTarjeta);
            datos.put("ClaveTarjeta", claveTarjeta);
            datos.put("PosicionCargaId", PosicionDeCarga);
            datos.put("Productos", array1);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request_json = new JsonObjectRequest(Request.Method.POST, url, datos, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                String estado = null;
                String mensaje = null;
                try {
                    estado = response.getString("Estado");
                    mensaje = response.getString("Mensaje");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (estado.equals("true")) {
                    try {
                        String titulo = "YENA REDENCION";
                        String mensajes = mensaje;
                        Modales modales = new Modales(ProductosARedimir.this);
                        View view1 = modales.MostrarDialogoCorrecto(ProductosARedimir.this, mensaje);
                        view1.findViewById(R.id.buttonAction).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                modales.alertDialog.dismiss();
                                //MODIFICCADO MIKEL 28/05/2021 para q imprima de una vez SOLO PRODUCTOS
                                if (banderaProducto.equals("true") && banderaCombustible.equals("false")) {
                                    // ImprimeTicketPuntadaSoloProductos();
                                } else {
                                    Intent intent = new Intent(ProductosARedimir.this, Menu_Principal.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        String titulo = "YENA REDENCION";
                        String mensajes = mensaje;
                        Modales modales = new Modales(ProductosARedimir.this);
                        View view1 = modales.MostrarDialogoError(ProductosARedimir.this, mensaje);
                        view1.findViewById(R.id.buttonAction).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                modales.alertDialog.dismiss();
                                Intent intent = new Intent(ProductosARedimir.this, Menu_Principal.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ProductosARedimir.this, "error", Toast.LENGTH_SHORT).show();

            }
        }) {
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                return headers;
            }

            protected Response<JSONObject> parseNetwokResponse(NetworkResponse response) {
                if (response != null) {
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


    //Metodo para regresar a la actividad principal
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), Menu_Principal.class);
        startActivity(intent);
        finish();
    }




}