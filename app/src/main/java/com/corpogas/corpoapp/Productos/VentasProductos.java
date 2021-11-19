    package com.corpogas.corpoapp.Productos;

    import android.annotation.SuppressLint;
    import android.app.AlertDialog;
    import android.app.ProgressDialog;
    import android.content.BroadcastReceiver;
    import android.content.Context;
    import android.content.DialogInterface;
    import android.content.Intent;
    import android.content.IntentFilter;
    import android.device.scanner.configuration.Triggering;
    import android.os.Build;
    import android.os.Bundle;
    import android.text.Editable;
    import android.text.InputType;
    import android.text.TextWatcher;
    import android.util.Log;
    import android.view.KeyEvent;
    import android.view.MotionEvent;
    import android.view.View;
    import android.view.WindowManager;
    import android.widget.AdapterView;
    import android.widget.Button;
    import android.widget.EditText;
    import android.widget.ImageButton;
    import android.widget.ListView;
    import android.widget.ProgressBar;
    import android.widget.TextView;
    import android.widget.Toast;

    import androidx.annotation.Nullable;
    import androidx.appcompat.app.AppCompatActivity;
    import androidx.recyclerview.widget.RecyclerView;

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
    import com.corpogas.corpoapp.Conexion;
    import com.corpogas.corpoapp.Configuracion.SQLiteBD;
    import com.corpogas.corpoapp.LecturaCodigoBarrasQR.ScanManagerDemo;
    import com.corpogas.corpoapp.Menu_Principal;
    import com.corpogas.corpoapp.Modales.Modales;
    import com.corpogas.corpoapp.R;
    import com.corpogas.corpoapp.ScanManagerProvides;
    import com.corpogas.corpoapp.ValesPapel.ValesPapel;
    import com.corpogas.corpoapp.VentaCombustible.confirmaVenta;
    import com.google.zxing.integration.android.IntentIntegrator;
    import com.google.zxing.integration.android.IntentResult;

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

//    import devliving.online.mvbarcodereader.MVBarcodeScanner;

    public class VentasProductos extends AppCompatActivity implements View.OnClickListener {
        String model;

        ScanManagerProvides scanManagerProvides;
        String result = "";

        //Declaracion de Variables
        private RecyclerView mRecyclerView;
        private RecyclerView.Adapter mAdapter;
        private RecyclerView.LayoutManager mLayoutManager;

        //Declaracion de objetos
        Button btnAgregar,btnEnviar, incrementar, decrementar, comprar, btnsolicitadespacho;
        TextView  txtDescripcion, NumeroProductos, precio, existencias, productoIdentificador;
        EditText cantidadProducto, Producto, tipoproductoid, empleado, categoria;
        String cantidad;
        JSONObject mjason = new JSONObject();
        JSONArray myArray = new JSONArray();
        JSONArray myArrayVer = new JSONArray();
        JSONArray myArrayPaso = new JSONArray();

        String EstacionId, sucursalId, ipEstacion, tipoTransaccion, numerodispositivo ;
        ListView list;
        Integer ProductosAgregados = 0;
        String posicion, usuario;
        String transaccionId;
        TextView txtproductos;
        String cadenaproducto = "";
        String textoresultado, numerooperativa;
        private ImageButton b_auto, btnbuscar, btnborrartodo, btnScanner;
//        private MVBarcodeScanner.ScanningMode modo_Escaneo;
        private TextView text_cod_escaneado;
        private int CODE_SCAN = 1;

        private BroadcastReceiver mScanRecevier = null;
        public static final int ENCODE_MODE_UTF8 = 1;
        public static final int ENCODE_MODE_GBK = 2;
        public static final int ENCODE_MODE_NONE = 3;

        List<String> ID;
        List<String> NombreProducto;
        List<String> PrecioProducto;
        List<String> ClaveProducto;
        List<String> codigoBarras;
        List<String> ExistenciaProductos;
        List<String> ProductosId;
        List<String> TipoProductoId;
        List<String> DescripcionPr;
        List<String> ACategoria;
        String lugarproviene ;
        Long NumeroEmpleado, islaId;
        Integer Propina = 5;
        ProgressDialog bar;
        SQLiteBD db;
        DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
        DecimalFormat df;



        @SuppressLint({"WrongViewCast", "ClickableViewAccessibility"})
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_ventas_productos);

            // Código para verificar HH
            model = Build.MODEL;

            //instruccion para que aparezca la flecha de regreso
            //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            simbolos.setDecimalSeparator('.');
            df = new DecimalFormat("#,###.00##",simbolos);
            df.setMaximumFractionDigits(2);

            textoresultado="";
            db = new SQLiteBD(getApplicationContext());
            this.setTitle(db.getRazonSocial());
            this.setTitle(db.getNombreEstacion() + " ( EST.:" + db.getNumeroEstacion() + ")");

            EstacionId = db.getIdEstacion();
            sucursalId = db.getIdSucursal();
            ipEstacion= db.getIpEstacion();
            tipoTransaccion = "1"; //Transaccion Normal
            numerodispositivo = db.getIdTarjtero(); //"1";

            numerooperativa = getIntent().getStringExtra("numeroOperativa");
            cadenaproducto = getIntent().getStringExtra("cadenaproducto");
            lugarproviene = getIntent().getStringExtra("lugarproviene");
            islaId = db.getIslaId();
            NumeroEmpleado = Long.valueOf(db.getNumeroEmpleado());//getIntent().getLongExtra("NumeroEmpleado", 0);

            txtproductos=findViewById(R.id.txtproductos);
            btnScanner = findViewById(R.id.btnscanner);

            if (cadenaproducto.length()>0){
                txtproductos.setText(myArrayVer.toString());
            }else{
                txtproductos.setText("");
            }

            empleado=findViewById(R.id.empleado);
            empleado.setText(getIntent().getStringExtra("NumeroEmpleado"));

            posicion = getIntent().getStringExtra("posicionCarga");
            usuario = getIntent().getStringExtra("NumeroEmpleado");


            btnsolicitadespacho = findViewById(R.id.btnsolicitadespacho);
            btnsolicitadespacho.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Autorizadespacho();
                }
            });
            comprar=findViewById(R.id.comprar);
            comprar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //se asignan valores
                    final String posicion;
                    posicion = getIntent().getStringExtra("posicionCarga");
                    final String usuarioid;
                    usuarioid = getIntent().getStringExtra("usuario");

                    if (myArray.length()==0  )       //.length() >0)
                    {
                        String titulo = "AVISO";
                        String mensaje = "Seleccione un Producto";
                        Modales modales = new Modales(VentasProductos.this);
                        View view1 = modales.MostrarDialogoAlertaAceptar(VentasProductos.this,mensaje,titulo);
                        view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                modales.alertDialog.dismiss();
                            }
                        });
                        //Toast.makeText(getApplicationContext(), "Seleccione al menos uno de los Productos", Toast.LENGTH_LONG).show();
                    } else {
                        ////AgregarDespacho(posicion, usuarioid);
                        EnviarProductos(posicion, usuarioid);
                    }
                }
            });
            btnEnviar = findViewById(R.id.btnEnviar);
            btnEnviar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Valida si se ha agregado productos al arreglo
                    if (myArray.length()==0  )       //.length() >0)
                    {
                        String titulo = "AVISO";
                        String mensaje = "Seleccione un Producto";
                        Modales modales = new Modales(VentasProductos.this);
                        View view1 = modales.MostrarDialogoAlertaAceptar(VentasProductos.this,mensaje,titulo);
                        view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                modales.alertDialog.dismiss();
                            }
                        });
                    } else {
                        Intent intent = new Intent(getApplicationContext(), confirmaVenta.class);
                        intent.putExtra("posicion", posicion);
                        intent.putExtra("usuario", usuario);
                        intent.putExtra("cadenaproducto", myArrayPaso.toString());
                        intent.putExtra("lugarproviene", lugarproviene);
//                        intent.putExtra("numeroOpertativa", numerooperativa);
                        intent.putExtra("numeroisla", islaId);
//                        intent.putExtra("numeroempleado", NumeroEmpleado);
                        startActivity(intent);
                        finish();

                    }
                }
            });
            btnAgregar = findViewById(R.id.btnAgregar);
            btnAgregar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //procedimiento para agregar un producto al arreglo
                    AgregarProducto();
                    //CrearJSON();
                }
            });


            incrementar = findViewById(R.id.incrementar);
            incrementar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Aumentar();
                }
            });
            decrementar= findViewById(R.id.decrementar);
            decrementar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Decrementar();
                }
            });
            //procedimiento para inicializar variables
            //CantidadProducto();
            cantidadProducto = findViewById(R.id.cantidadProducto);
            Producto= findViewById(R.id.Producto);
            cantidad = cantidadProducto.toString();
            txtDescripcion = findViewById(R.id.txtDescripcion);
            precio = findViewById(R.id.precio);
            existencias = findViewById(R.id.existencias);
            productoIdentificador = findViewById(R.id.productoIdentificador);
            tipoproductoid = findViewById(R.id.tipoproductoid);
            txtproductos = findViewById(R.id.txtproductos);
            categoria = findViewById(R.id.categoria);
            //procedimiento que despliega la lista de productos
            MostrarProductos();

            btnbuscar = findViewById(R.id.btnbuscar);
            btnbuscar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    buscaCodigoInterno(Producto.getText().toString());


                }
            });

//            btnScanner.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    IntentIntegrator integrator = new IntentIntegrator(VentasProductos.this);
//                    integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
//                    integrator.setPrompt("Lector - CDP");
//                    integrator.setCameraId(0);
//                    integrator.setBeepEnabled(true);
//                    integrator.setBarcodeImageEnabled(true);
//                    integrator.initiateScan();
//                }
//            });

            btnborrartodo = findViewById(R.id.btnborrartodo);
            btnborrartodo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    myArray= new JSONArray();
                    myArrayPaso =new JSONArray();
                    myArrayVer = new JSONArray();
                    Producto.setText("");
                    txtDescripcion.setText("");
                    cantidadProducto.setText("1");
                    precio.setText("");
                    existencias.setText("");
                    productoIdentificador.setText("");
                    txtproductos.setText("");
                    textoresultado="";
                    String titulo = "AVISO";
                    String mensaje = "Se vació la canasta de Productos";
                    Modales modales = new Modales(VentasProductos.this);
                    View view1 = modales.MostrarDialogoAlertaAceptar(VentasProductos.this,mensaje,titulo);
                    view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            modales.alertDialog.dismiss();
                        }
                    });
                    //Toast.makeText(VentasProductos.this, "Se vacio la canasta de productos", Toast.LENGTH_SHORT).show();
                }
            });

            if (model.equals("i9000S")) {
                scanManagerProvides = new ScanManagerProvides();

                btnScanner.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        if (view.getId() == R.id.btnscanner) {
                            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                                if (scanManagerProvides.getTriggerMode() == Triggering.HOST) {
                                    scanManagerProvides.stopDecode();
                                }
                            }
                            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                                scanManagerProvides.startDecode();
                            }
                        }
                        return false;
                    }
                });

                Producto.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        if (!Producto.getText().toString().equals("")) {
                            if (Producto.getText().toString().length() > 5) {
                                buscarCodigoBarra(Producto.getText().toString());
                            }
                        }
                    }
                });
            } else {
                btnScanner.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        IntentIntegrator integrator = new IntentIntegrator(VentasProductos.this);
                        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                        integrator.setPrompt("Lector - CDP");
                        integrator.setCameraId(0);
                        integrator.setBeepEnabled(true);
                        integrator.setBarcodeImageEnabled(true);
                        integrator.initiateScan();
                    }
                });
            }
        }

        //        @Override
//        public boolean dispatchKeyEvent(KeyEvent event) {
//            if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() != KeyEvent.KEYCODE_ENTER) { //Not Adding ENTER_KEY to barcode String
//                char pressedKey = (char) event.getUnicodeChar();
//                result += pressedKey;
//            }
//            if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {  //Any method handling the data
//                buscarCodigoBarra(result);
//                result = "";
//            }
//            return false;
//        }

        protected void onActivityResult (int requestCode, int resulCode, Intent data) {
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resulCode, data);
            if (result != null) {
                if (result.getContents() == null) {
                    Toast.makeText(getApplicationContext(), "Lectura Cancelada", Toast.LENGTH_SHORT).show();
                } else {
//                    Toast.makeText(getApplicationContext(), result.getContents(), Toast.LENGTH_SHORT).show();
//                    Producto.setText(result.getContents());
                    buscarCodigoBarra(result.getContents());
                }
            } else {
                super.onActivityResult(requestCode, resulCode, data);
            }
        }

        private void Autorizadespacho() {
//            if (!Conexion.compruebaConexion(this)) {
//                Toast.makeText(getBaseContext(), "Sin conexión a la red ", Toast.LENGTH_SHORT).show();
//                Intent intent1 = new Intent(getApplicationContext(), Menu_Principal.class);
//                startActivity(intent1);
//                finish();
//            } else {
                final String posicion;
                posicion = getIntent().getStringExtra("posicion");
                final String usuarioid;
                usuarioid = getIntent().getStringExtra("usuario");


                String url = "http://" + ipEstacion + "/CorpogasService/api/despachos/autorizaDespacho/posicionCargaId/" + posicion + "/usuarioId/" + usuarioid;
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject respuesta = new JSONObject(response);
                            String correctoautoriza = respuesta.getString("Correcto");
                            String mensajeautoriza = respuesta.getString("Mensaje");
                            String objetoRespuesta = respuesta.getString("ObjetoRespuesta");
                            if (correctoautoriza.equals("true")) {
                                Toast.makeText(getApplicationContext(), mensajeautoriza, Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getApplicationContext(), mensajeautoriza, Toast.LENGTH_LONG).show();
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

//            }
        }

//        private void UI() {
//            b_auto = findViewById(R.id.btnscanner);
//            b_auto.setOnClickListener(this);
//            mScanRecevier = new BroadcastReceiver() {
//                public void onReceive(Context context, Intent intent) {
//                    Log.e("Scan", "scan receive.......");
//
//                    String scanResult = "";
//                    int length = intent.getIntExtra("EXTRA_SCAN_LENGTH", 0);
//                    int encodeType = intent.getIntExtra("EXTRA_SCAN_ENCODE_MODE", 1);
//
//                    if (encodeType == ENCODE_MODE_NONE) {
//                        byte[] data = intent.getByteArrayExtra("EXTRA_SCAN_DATA");
//                        try {
//                            scanResult = new String(data, 0, length, "iso-8859-1");//Encode charSet
//                        } catch (UnsupportedEncodingException e) {
//                            e.printStackTrace();
//                        }
//
//                    } else {
//                        scanResult = intent.getStringExtra("EXTRA_SCAN_DATA");
//                        Toast.makeText(context, "Codigo: " + scanResult, Toast.LENGTH_SHORT).show();
//                        buscarCodigoBarra(scanResult);
//                    }
////                final String  scanResultData=intent.getStringExtra("EXTRA_SCAN_DATA");
////                    tvMsg.setText("Scan Bar Code ：" + scanResult);
//                }
//            };
//
//            IntentFilter filter = new IntentFilter("ACTION_BAR_SCAN");
//            VentasProductos.this.registerReceiver(mScanRecevier, filter);
//
//        }

//        @Override
//        protected void onDestroy() {
//            super.onDestroy();
//
//            //disable scan
//            Intent intentDisScan = new Intent("ACTION_BAR_SCANCFG");
//            intentDisScan.putExtra("EXTRA_SCAN_POWER", 0);
//            VentasProductos.this.sendBroadcast(intentDisScan);
//
//        }

        @Override
        protected void onResume() {
            super.onResume();

            disableFunctionLaunch(true);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

            //enable scan
            Intent intentEnableScan = new Intent("ACTION_BAR_SCANCFG");
            intentEnableScan.putExtra("EXTRA_SCAN_POWER", 1);
            VentasProductos.this.sendBroadcast(intentEnableScan);
            if (model.equals("i9000S")) {
                scanManagerProvides.initScan(VentasProductos.this);
            }
        }

        @Override
        protected void onPause() {

            disableFunctionLaunch(false);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

            super.onPause();
        }

        @Override
        public void onClick(View view) {

        }

        //        @Override
//        public void onClick(View view) {
//            switch (view.getId()){
//                case R.id.btnscanner:
////                    modo_Escaneo = MVBarcodeScanner.ScanningMode.SINGLE_AUTO;
//                    Intent startIntent = new Intent("ACTION_BAR_TRIGSCAN");
//                    startIntent.putExtra("timeout", 60);// Units per second,and Maximum 9
////                    tvMsg.setText("Start Scan...");
//
//                    VentasProductos.this.sendBroadcast(startIntent);
//                    break;
//            }
////            new MVBarcodeScanner.Builder().setScanningMode(modo_Escaneo).setFormats(Barcode.ALL_FORMATS)
////                    .build()
////                    .launchScanner(this, CODE_SCAN);
//        }
//        @Override
//        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//            super.onActivityResult(requestCode, resultCode, data);
//
//            if (requestCode == CODE_SCAN) {
//                if (resultCode == RESULT_OK && data != null
//                        && data.getExtras() != null) {
//
//                    if (data.getExtras().containsKey(MVBarcodeScanner.BarcodeObject)) {
//                        Barcode mBarcode = data.getParcelableExtra(MVBarcodeScanner.BarcodeObject);
////                    Producto.setText(mBarcode.rawValue);
//                        buscarCodigoBarra(mBarcode.rawValue);
//                    } else if (data.getExtras().containsKey(MVBarcodeScanner.BarcodeObjects)) {
//                        List<Barcode> mBarcodes = data.getParcelableArrayListExtra(MVBarcodeScanner.BarcodeObjects);
//                        StringBuilder s = new StringBuilder();
//                        for (Barcode b:mBarcodes){
//                            s.append(b.rawValue + "\n");
//                        }
////                    Producto.setText(s.toString());
//                        buscarCodigoBarra(s.toString());
//                    }
//                }
//            }
//        }

        private void buscarCodigoBarra(String rawValue) {
            int indicecodigo = codigoBarras.indexOf(rawValue);
            if (indicecodigo > 0 ) {
                String  Descripcion = ID.get(indicecodigo);
                String precioUnitario = PrecioProducto.get(indicecodigo);
                String paso= ClaveProducto.get(indicecodigo);
                String existencia = ExistenciaProductos.get(indicecodigo);
                String idproduc = ProductosId.get(indicecodigo);
                String categoriaid = ACategoria.get(indicecodigo);

                Producto.setText(paso);
                txtDescripcion.setText(Descripcion);
                precio.setText(precioUnitario);
                existencias.setText(existencia);
                productoIdentificador.setText(idproduc);
                tipoproductoid.setText(idproduc);
                categoria.setText(categoriaid);
            }else{
                Toast.makeText(getApplicationContext(), "Producto no encontrado en la lista", Toast.LENGTH_LONG).show();
                Producto.setText("");
                txtDescripcion.setText("");
                precio.setText("");
                //existencias.setText(existencia);
                productoIdentificador.setText("");
                cantidadProducto.setText("1");
                tipoproductoid.setText("");
            }
        }

        private void buscaCodigoInterno(String valor){
            int indicecodigo = ClaveProducto.indexOf(valor);
            if (indicecodigo >=0 ) {
                String Descripcion = ID.get(indicecodigo);
                String precioUnitario = PrecioProducto.get(indicecodigo);
                //String paso= ClaveProducto.get(indicecodigo);
                String existencia = ExistenciaProductos.get(indicecodigo);
                String idproduc = ProductosId.get(indicecodigo);
                String tipop = TipoProductoId.get(indicecodigo);
                //String codigo = codigoBarras.get(indicecodigo);

                //Producto.setText(paso);
                txtDescripcion.setText(Descripcion);
                precio.setText(precioUnitario);
                existencias.setText(existencia);
                productoIdentificador.setText(idproduc);
                tipoproductoid.setText(tipop);
            } else {
                Toast.makeText(getApplicationContext(), "Producto no encontrado en la lista", Toast.LENGTH_LONG).show();
                Producto.setText("");
                txtDescripcion.setText("");
                precio.setText("");
                //existencias.setText(existencia);
                productoIdentificador.setText("");
                cantidadProducto.setText("1");
            }
        }


        private void finalizaventa(final String posicion, final String idUsuario){
//            if (!Conexion.compruebaConexion(this)) {
//                Toast.makeText(getBaseContext(),"Sin conexión a la red ", Toast.LENGTH_SHORT).show();
//                Intent intent1 = new Intent(getApplicationContext(), Menu_Principal.class);
//                startActivity(intent1);
//                finish();
//            }else {
                //Utilizamos el metodo POST para  finalizar la Venta
                String url = "http://" + ipEstacion + "/CorpogasService/api/Transacciones/finalizaVenta/sucursal/" + sucursalId + "/posicionCarga/" + posicion + "/usuario/" + idUsuario;
                StringRequest eventoReq = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                //Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(getApplicationContext(), Menu_Principal.class);
                                startActivity(intent);
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }, new Response.ErrorListener() {
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
//            }
        }
        private void CantidadProducto() {
            cantidadProducto = findViewById(R.id.cantidadProducto);
            Producto= findViewById(R.id.Producto);
            cantidad = cantidadProducto.toString();
            txtDescripcion = findViewById(R.id.txtDescripcion);
            precio = findViewById(R.id.precio);
            existencias = findViewById(R.id.existencias);
            productoIdentificador = findViewById(R.id.productoIdentificador);
            tipoproductoid = findViewById(R.id.tipoproductoid);
            txtproductos = findViewById(R.id.txtproductos);
            categoria = findViewById(R.id.categoria);
        }



        private void Aumentar() {
            if (txtDescripcion.length() == 0){
                String titulo = "AVISO";
                String mensaje = "Seleccione un Producto";
                Modales modales = new Modales(VentasProductos.this);
                View view1 = modales.MostrarDialogoAlertaAceptar(VentasProductos.this,mensaje,titulo);
                view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        modales.alertDialog.dismiss();
                    }
                });

            }else {
                if (Integer.parseInt(categoria.getText().toString()) == Propina){
                    String titulo = "AVISO";
                    String mensaje = "La Catidad para el producto "+ txtDescripcion.getText()+", No puede ser mayor a 1";
                    Modales modales = new Modales(VentasProductos.this);
                    View view1 = modales.MostrarDialogoAlertaAceptar(VentasProductos.this,mensaje,titulo);
                    view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            modales.alertDialog.dismiss();
                        }
                    });

                }else{
                    cantidad = cantidadProducto.getText().toString();
                    int numero = Integer.parseInt(cantidad);
                    int totalexistencia = Integer.parseInt(existencias.getText().toString());
                    if (numero < totalexistencia) {
                        int total = numero + 1;
                        String resultado = String.valueOf(total);
                        cantidadProducto.setText(resultado);
                    } else {
                        String titulo = "AVISO";
                        String mensaje = "Solo hay "+ existencias.getText().toString() + " en existencia ";
                        Modales modales = new Modales(VentasProductos.this);
                        View view1 = modales.MostrarDialogoAlertaAceptar(VentasProductos.this,mensaje,titulo);
                        view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                            modales.alertDialog.dismiss();
                        }
                        });
                    }
                }

            }
        }
        private void Decrementar() {
            if (txtDescripcion.length() == 0){
                String titulo = "AVISO";
                String mensaje = "Seleccione  uno de los Productos";
                Modales modales = new Modales(VentasProductos.this);
                View view1 = modales.MostrarDialogoAlertaAceptar(VentasProductos.this,mensaje,titulo);
                view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        modales.alertDialog.dismiss();
                    }
                });
            }else {
                cantidad = cantidadProducto.getText().toString();
                int numero = Integer.parseInt(cantidad);
                if (numero > 1) {
                    int total = numero - 1;
                    String resultado = String.valueOf(total);
                    cantidadProducto.setText(resultado);
                } else {
                    String titulo = "AVISO";
                    String mensaje = "El valor minimo debe ser 1";
                    Modales modales = new Modales(VentasProductos.this);
                    View view1 = modales.MostrarDialogoAlertaAceptar(VentasProductos.this,mensaje,titulo);
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

        private void AgregarProducto(){
            String resultado  = "";
            //EditText cantidadProducto = (EditText)getActivity().findViewById();
            String  TipoProductoId;
            String  ProductoId;
            String  numInterno;
            String  descrProducto;
            int TotalProducto;
            int ProductoIdEntero;

            //if (ProductoId.isEmpty())
            if (txtDescripcion.length() == 0)
            {
                String titulo = "AVISO";
                String mensaje = "Seleccione un Producto";
                Modales modales = new Modales(VentasProductos.this);
                View view1 = modales.MostrarDialogoAlertaAceptar(VentasProductos.this,mensaje,titulo);
                view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        modales.alertDialog.dismiss();
                    }
                });
            }
            else{
                TotalProducto = Integer.parseInt(cantidadProducto.getText().toString());
                String PrecioMonto = precio.getText().toString();
                Double precioUnitario =  Double.valueOf(PrecioMonto);
                //ProductoId = Producto.getText().toString();
                ProductoId = productoIdentificador.getText().toString();
                ProductoIdEntero = Integer.parseInt(ProductoId);
                numInterno = Producto.getText().toString();
                descrProducto = txtDescripcion.getText().toString();
                TipoProductoId = tipoproductoid.getText().toString();

                try {
                    boolean bandera=true;
                    if (myArray.length()>0  ) {
                        for (int i = 0; i < myArray.length(); i++) {
                            try {
                                JSONObject jsonObject = myArray.getJSONObject(i);
                                if (jsonObject.has("ProductoId")) { //
                                    String valor = jsonObject.getString("ProductoId");
                                    int res = Integer.parseInt(valor);
                                    if (res==ProductoIdEntero){
                                        bandera=false;
                                        break;
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    if (bandera==true) {
                        JSONObject mjason = new JSONObject();
                        JSONObject mjasonver = new JSONObject();
                        JSONObject mjasonpaso = new JSONObject();

                        mjason.put("TipoProducto", Integer.parseInt(TipoProductoId));
                        mjason.put("ProductoId", numInterno); //ProductoIdEntero
                        mjason.put("NumeroInterno", Integer.parseInt(numInterno));
                        //mjason.put("Descripcion", descrProducto.toString());
                        mjason.put("Cantidad", TotalProducto);
                        mjason.put("Precio", precioUnitario);
                        //mjason.put("Descripcion", descrProducto);
                        myArray.put(mjason);

                        mjasonpaso.put("TipoProducto", Integer.parseInt(TipoProductoId));
                        mjasonpaso.put("ProductoId", numInterno); //ProductoIdEntero
                        mjasonpaso.put("NumeroInterno", Integer.parseInt(numInterno));
                        //mjasonpaso.put("Descripcion", descrProducto.toString());
                        mjasonpaso.put("Cantidad", TotalProducto);
                        mjasonpaso.put("Precio", precioUnitario);
                        mjasonpaso.put("Descripcion", descrProducto);

                        myArrayPaso.put(mjasonpaso);

                        mjasonver.put("Producto:", Integer.parseInt(numInterno));
                        mjasonver.put("Cantidad:", TotalProducto);
                        mjasonver.put("Precio:", precioUnitario);

                        myArrayVer.put(mjasonver);
                        String txtproducto= "Producto: "+Integer.parseInt(numInterno);
                        String txtcantidad= "Cantidad: "+TotalProducto;
                        String txtprecio= "Precio: $"+ df.format(precioUnitario);


                        textoresultado = textoresultado + " " + txtproducto + " " + txtcantidad + " " +txtprecio + "     ";
                        txtproductos.setText(textoresultado);//myArrayVer.toString());
                        ProductosAgregados = +ProductosAgregados;
                    }else{
                        String titulo = "AVISO";
                        String mensaje = "Producto: "+ ProductoId+" cargado anteriormente";
                        Modales modales = new Modales(VentasProductos.this);
                        View view1 = modales.MostrarDialogoAlertaAceptar(VentasProductos.this,mensaje,titulo);
                        view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                modales.alertDialog.dismiss();
                            }
                        });

                        //Toast.makeText(getApplicationContext(), "Producto: "+ ProductoId+" cargado anteriormente"  , Toast.LENGTH_LONG).show();
                    }
                    Producto.setText("");
                    txtDescripcion.setText("");
                    cantidadProducto.setText("1");
                    precio.setText("");
                    existencias.setText("");
                    productoIdentificador.setText("");


                } catch (JSONException error) {
                }
            }
        }

        private void MostrarProductos() {
            if (!Conexion.compruebaConexion(this)) {
                Toast.makeText(getBaseContext(), "Sin conexión a la red ", Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent(getApplicationContext(), Menu_Principal.class);
                startActivity(intent1);
                finish();
            } else {
                bar = new ProgressDialog(VentasProductos.this);
                bar.setTitle("Cargando Productos");
                bar.setMessage("Ejecutando... ");
                bar.setIcon(R.drawable.productos);
                bar.setCancelable(false);
                bar.show();
                String url;
                if (lugarproviene.equals("Corte")){
                    url = "http://" + ipEstacion + "/CorpogasService/api/islas/productos/sucursal/" + sucursalId + "/islaId/" + islaId; //+ islaId;
                }else{
                    url = "http://" + ipEstacion + "/CorpogasService/api/islas/productos/sucursal/" + sucursalId + "/posicionCargaId/"+ posicion;
                }
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //mostarProductor(response);
                        mostrarProductosExistencias(response);
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
        }

        private void mostrarProductosExistencias(String response){
            //Declaracion de variables
            String preciol = null;
            String DescLarga;
            String idArticulo;
            String TProductoId;

            ID = new ArrayList<String>();

            NombreProducto = new ArrayList<String>();
            PrecioProducto = new ArrayList<>();
            ClaveProducto = new ArrayList();
            codigoBarras = new ArrayList();
            ExistenciaProductos = new ArrayList();
            ProductosId = new ArrayList();
            TipoProductoId = new ArrayList();
            DescripcionPr = new ArrayList();;
            ACategoria = new ArrayList();


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
                    if (categoriaid.equals("1")){
                        //NO CARGA LOS COMBUSTIBLES
                    }else{
                        String ExProductos=pA.getString("Existencias");
                        ExistenciaProductos.add(ExProductos);
                        TProductoId="2";//prod.getString("TipoSatProductoId");
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
                        ProductosId.add(IdProductos);
                        codigoBarras.add(codigobarras);
                        TipoProductoId.add(TProductoId);
                        DescripcionPr.add(DescLarga);
                        ACategoria.add(categoriaid);
                    }
                }
                bar.cancel();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            final ListAdapterProductos adapterP = new ListAdapterProductos(this,  ID, NombreProducto);
            list=(ListView)findViewById(R.id.list);
            list.setTextFilterEnabled(true);
            list.setAdapter(adapterP);
//        Agregado  click en la lista
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    if (PrecioProducto.get(i)==null){
                        String titulo = "AVISO";
                        String mensaje = "El producto seleccionado no tiene Precio" ;
                        Modales modales = new Modales(VentasProductos.this);
                        View view1 = modales.MostrarDialogoAlertaAceptar(VentasProductos.this,mensaje,titulo);
                        view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                precio.setText("");
                                Producto.setText("");
                                txtDescripcion.setText("");
                                existencias.setText("");
                                productoIdentificador.setText("");
                                tipoproductoid.setText("");
                                categoria.setText("");
                               modales.alertDialog.dismiss();
                            }
                        });
                    }else{
                        String  Descripcion = ID.get(i).toString();
                        String precioUnitario = PrecioProducto.get(i).toString();
                        String paso= ClaveProducto.get(i).toString();
                        String existencia = ExistenciaProductos.get(i).toString();
                        String IProd = ProductosId.get(i).toString();
                        String TProd = TipoProductoId.get(i).toString();
                        String  idcategoria = ACategoria.get(i).toString();

                        Producto.setText(paso);
                        txtDescripcion.setText(Descripcion);
                        existencias.setText(existencia);
                        productoIdentificador.setText(paso); //IProd
                        tipoproductoid.setText(TProd);
                        categoria.setText(idcategoria);

                        if (Integer.parseInt(idcategoria) == Propina){
                            cantidadProducto.setText("1");
                            String titulo = "PRODUCTOS";
                            String mensaje = "Ingresa la Cantidad de " + txtDescripcion.getText() ;
                            Modales modales = new Modales(VentasProductos.this);
                            View viewLectura = modales.MostrarDialogoInsertaDato(VentasProductos.this, mensaje, titulo);
                            EditText edtProductoCantidad = ((EditText) viewLectura.findViewById(R.id.textInsertarDato));
                            edtProductoCantidad.setInputType(InputType.TYPE_CLASS_NUMBER);
                            viewLectura.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    String cantidad = edtProductoCantidad.getText().toString();
                                    if (cantidad.isEmpty()){
                                        edtProductoCantidad.setError("Ingresa la Cantidad");
                                    }else {
                                        precio.setText(cantidad);
                                    }
                                    modales.alertDialog.dismiss();
                                }
                            });
                            viewLectura.findViewById(R.id.buttonNo).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    //Toast.makeText(VentasProductos.this, "Ingresa la Cantidad", Toast.LENGTH_SHORT).show();
                                    precio.setText("");
                                    Producto.setText("");
                                    txtDescripcion.setText("");
                                    existencias.setText("");
                                    productoIdentificador.setText("");
                                    tipoproductoid.setText("");
                                    categoria.setText("");
                                    modales.alertDialog.dismiss();
                                }
                            });
                        }else{
                            precio.setText(precioUnitario);
                        }
                    }
                }
            });
        }

        private void mostarProductor(String response) {
            //Declaracion de variables
            final List<String> ID;
            ID = new ArrayList<String>();

            final List<String> NombreProducto;
            NombreProducto = new ArrayList<String>();

            final List<String> PrecioProducto;
            PrecioProducto = new ArrayList<>();

            final List<String> ClaveProducto;
            ClaveProducto = new ArrayList();
            //ArrayList<singleRow> singlerow = new ArrayList<>();

            try {
                JSONArray productos = new JSONArray(response);
                for (int i = 0; i <productos.length() ; i++) {
                    JSONObject p1 = productos.getJSONObject(i);
                    String idArticulo = p1.getString("IdArticulo");
                    String DesLarga = p1.getString("DescLarga");
                    String precio = p1.getString("Precio");
                    NombreProducto.add("ID: " + idArticulo + "    |     $"+precio);
                    ID.add(DesLarga);
                    PrecioProducto.add(precio);
                    ClaveProducto.add(idArticulo);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            final ListAdapterProductos adapterP = new ListAdapterProductos(this,  ID, NombreProducto);
            list=(ListView)findViewById(R.id.list);
            list.setTextFilterEnabled(true);
            list.setAdapter(adapterP);
            Producto.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    //adapterP.getFilter().filter(charSequence);
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
//        Agregado  click en la lista
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    String  Descripcion = ID.get(i).toString();
                    String precioUnitario = PrecioProducto.get(i).toString();
                    String paso= ClaveProducto.get(i).toString();
                    Producto.setText(paso);
                    txtDescripcion.setText(Descripcion);
                    precio.setText(precioUnitario);
                }
            });
        }

        private void enviarAFormasPago(final String posicionCarga, final String UsuarioVenta){

        }

        private void EnviarProductos(final String posicionCarga, final String Usuarioid) {
//            if (!Conexion.compruebaConexion(this)) {
//                Toast.makeText(getBaseContext(), "Sin conexión a la red ", Toast.LENGTH_SHORT).show();
//                Intent intent1 = new Intent(getApplicationContext(), Menu_Principal.class);
//                startActivity(intent1);
//                finish();
//            } else {
                //RequestQueue queue = Volley.newRequestQueue(this);
                String url = "http://" + ipEstacion + "/CorpogasService/api/ventaProductos/GuardaProductos/sucursal/" + sucursalId + "/origen/" + numerodispositivo + "/usuario/" + Usuarioid + "/posicionCarga/" + posicionCarga;
                RequestQueue queue = Volley.newRequestQueue(this);

                JsonArrayRequest request_json = new JsonArrayRequest(Request.Method.POST, url, myArray,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                //Get Final response
//                                Intent intent = new Intent(getApplicationContext(), FPaga.class);
//                                intent.putExtra("posicion", posicionCarga);
//                                intent.putExtra("usuario", Usuarioid);
//                                startActivity(intent);
//                                finish();
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
                                AlertDialog.Builder builder = new AlertDialog.Builder(VentasProductos.this);
                                builder.setTitle("Venta Productos");
                                builder.setCancelable(false);
                                builder.setMessage(errorMensaje)
                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                Intent intente = new Intent(getApplicationContext(), Menu_Principal.class);
                                                startActivity(intente);
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
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                request_json.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                requestQueue.add(request_json);

//            }
        }

        private void AgregarDespacho(final String posicionCarga, final String Usuarioid) {
//            if (!Conexion.compruebaConexion(this)) {
//                Toast.makeText(getBaseContext(), "Sin conexión a la red ", Toast.LENGTH_SHORT).show();
//                Intent intent1 = new Intent(getApplicationContext(), Menu_Principal.class);
//                startActivity(intent1);
//                finish();
//            } else {
                String url = "http://" + ipEstacion + "/CorpogasService/api/despachos/sucursal/" + sucursalId + "/utlima/posicionCarga/" + posicionCarga;
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject respuesta = null;
                        try {
                            respuesta = new JSONObject(response);
                            String correcto = respuesta.getString("Correcto");
                            if (correcto.equals("false")) {//Aqui debe ser true o false
                            } else {
                                String objetoRespuesta = respuesta.getString("ObjetoRespuesta");
                                JSONObject oRespuesta = new JSONObject(objetoRespuesta);
                                Integer ProductoIdEntero = Integer.parseInt(oRespuesta.getString("CombustibleId"));
                                Double TotalProducto = Double.parseDouble(oRespuesta.getString("Litros"));
                                Double Precio = Double.parseDouble(oRespuesta.getString("Precio"));
                                //Double PrecioUnitario = TotalProducto * Precio;
                                //String PrecioUnitario =  PrecioU.toString();
                                Double PrecioUnitario = Double.parseDouble(oRespuesta.getString("Importe"));

                                String combustible = oRespuesta.getString("Combustible");
                                JSONObject combustibleF = new JSONObject(combustible);

                                int tProdId = 1; //Integer.parseInt(combustible.getString("TipoSatCombustibleId"));
                                String numInterno = combustibleF.getString("CodigoFranquicia");
                                //String descrProducto = combustible.getString("DescripcionLarga");
                                JSONObject mjason = new JSONObject();

                                mjason.put("TipoProducto", tProdId);
                                mjason.put("ProductoId", ProductoIdEntero);
                                mjason.put("NumeroInterno", Integer.parseInt(numInterno));
                                //mjason.put("Descripcion", descrProducto);
                                mjason.put("Cantidad", TotalProducto);
                                mjason.put("Precio", PrecioUnitario);
                                myArray.put(mjason);
                            }
                            EnviarProductos(posicionCarga, Usuarioid);
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

//            }
        }

        //Metodo para regresar a la actividad principal
        @Override
        public void onBackPressed() {
//            if (lugarproviene.equals("Corte")){
                finish();
//            }else{
//                Intent intent = new Intent(getApplicationContext(), Menu_Principal.class);
//                startActivity(intent);
//                finish();
//            }
        }

        // disable the power key when the device is boot from alarm but not ipo boot
        private static final String DISABLE_FUNCTION_LAUNCH_ACTION = "android.intent.action.DISABLE_FUNCTION_LAUNCH";
        private void disableFunctionLaunch(boolean state) {
            Intent disablePowerKeyIntent = new Intent(DISABLE_FUNCTION_LAUNCH_ACTION);
            if (state) {
                disablePowerKeyIntent.putExtra("state", true);
            } else {
                disablePowerKeyIntent.putExtra("state", false);
            }
            sendBroadcast(disablePowerKeyIntent);
        }

    }