package com.corpogas.corpoapp.VentaCombustible;

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
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.corpogas.corpoapp.Conexion;
import com.corpogas.corpoapp.Configuracion.SQLiteBD;
import com.corpogas.corpoapp.Menu_Principal;
import com.corpogas.corpoapp.Modales.Modales;
import com.corpogas.corpoapp.MyListAdapter;
import com.corpogas.corpoapp.Productos.VentasProductos;
import com.corpogas.corpoapp.R;
import com.corpogas.corpoapp.TanqueLleno.PlanchadoTarjeta.PlanchadoTanqueLleno;
import com.corpogas.corpoapp.ValesPapel.ValesPapel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EligePrecioLitros extends AppCompatActivity {
    ListView list;
    String[] maintitle = {
            "Litros", "Pesos",
    };

    String[] subtitle = {
            "Cantidad en Litros", "Cantidad en Pesos",
    };

    Integer[] imgid = {
            R.drawable.gas, R.drawable.fajillas_monedas,
    };
    EditText Cantidad;
    TextView EtiquetaCantidad, tvTipoDespachoLP;
    String TipoSeleccionado, usuario, posicionCarga, usuarioid, estacionJarreo, claveProducto, precio, nipCliente;
    Button btnLibre, btnPredeterminado, btnCobrar, btnCombustibleCobrar, btnPerifericosCobrar;
    SQLiteBD data;
    String EstacionId,  ipEstacion, sucursalId, numerodispositivo, despacholibre, combustible, numeroTarjeta,  lugarProviene;
    String mensaje = "", correcto = "";
    JSONArray myArray = new JSONArray();
    ProgressDialog bar;
    Double descuento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elige_precio_litros);

        SQLiteBD db = new SQLiteBD(getApplicationContext());
        data = new SQLiteBD(getApplicationContext());
        this.setTitle(data.getNombreEstacion() + " ( EST.:" + data.getNumeroEstacion() + ")");
        EstacionId = data.getIdEstacion();
        sucursalId = data.getIdSucursal();
        ipEstacion = data.getIpEstacion();
        numerodispositivo = data.getIdTarjtero();

        posicionCarga = getIntent().getStringExtra("posicionCarga");
        usuarioid = data.getNumeroEmpleado();
        estacionJarreo = getIntent().getStringExtra("estacionjarreo");
        claveProducto = getIntent().getStringExtra("claveProducto");
        precio = getIntent().getStringExtra("precioProducto");
        despacholibre = getIntent().getStringExtra("despacholibre");
        combustible = getIntent().getStringExtra("combustible");
        numeroTarjeta = getIntent().getStringExtra("numeroTarjeta");
        descuento = getIntent().getDoubleExtra("descuento", 0);
        lugarProviene = getIntent().getStringExtra("lugarProviene");
        nipCliente = getIntent().getStringExtra("nip");

        tvTipoDespachoLP = (TextView) findViewById(R.id.tvTipoDespachoLP);
        btnPredeterminado = (Button) findViewById(R.id.btnPredeterminadoInicia);
        btnCombustibleCobrar = (Button) findViewById(R.id.btnCombustibleCobrar);
        btnCombustibleCobrar.setEnabled(false);
        Cantidad = findViewById(R.id.CantidadPrecio);
        EtiquetaCantidad = findViewById(R.id.EtiquetaPrecioLista);
        btnLibre = (Button) findViewById(R.id.btnDespachoLibre);
        list= findViewById(R.id.lstOpcionesPrecio);

        btnPerifericosCobrar = findViewById(R.id.btnPerifericosCobrar);
        btnPerifericosCobrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), VentasProductos.class);
                intent.putExtra("numeroOperativa", usuarioid);
                intent.putExtra("cadenaproducto", "");
                intent.putExtra("lugarproviene", "Venta");
                intent.putExtra("NumeroIsla", "1");
                intent.putExtra("NumeroEmpleado", usuarioid);
                intent.putExtra("posicionCarga", posicionCarga);
                startActivity(intent);
//                finish();
            }
        });


        btnLibre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                list.setEnabled(false);
//                Cantidad.setEnabled(false);
//                btnLibre.setEnabled(true);
//                btnCobrar.setEnabled(true);
//                btnPredeterminado.setEnabled(false);
                solicitarDespacho();
//                Toast.makeText(EligeLitrosPrecio.this, "Entro Click boton Libre", Toast.LENGTH_SHORT).show();
            }
        });

        btnPredeterminado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cargaOpciones();
//                Toast.makeText(EligePrecioLitros.this, "Se habilito el predeterminado", Toast.LENGTH_SHORT).show();
                list.setEnabled(true);
                Cantidad.setEnabled(true);
                btnLibre.setEnabled(false);
                btnPredeterminado.setEnabled(true);
            }
        });

        btnCobrar = (Button) findViewById(R.id.btnCobrarDespacho);
        btnCobrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ValidaTransaccionActiva();
            }
        });

        list.setEnabled(false);
        if (despacholibre.equals("si")){
            btnPredeterminado.setEnabled(false);
        }

        tvTipoDespachoLP.setText("PC: " + posicionCarga  + "  TIPO DESPACHO ");//+ ", " + combustible

        if (lugarProviene.equals("puntadaAcumularQr")){
            btnLibre.setEnabled(false);
        }

    }


    private void EnviarProductosPredeterminado() {
        if (!Conexion.compruebaConexion(this)) {
            Toast.makeText(getBaseContext(), "Sin conexión a la red ", Toast.LENGTH_SHORT).show();
            Intent intent1 = new Intent(getApplicationContext(), Menu_Principal.class);
            startActivity(intent1);
            finish();
        } else {

            JSONObject datos = new JSONObject();
            try {
                datos.put("TipoProducto","1");
                datos.put("ProductoId", claveProducto);
                datos.put("NumeroInterno", claveProducto);
                datos.put("Descripcion",combustible);
                datos.put("Cantidad", Double.parseDouble(Cantidad.getText().toString()));
                datos.put("Precio", precio);
                //datos.put("Precio", litros);
                if (TipoSeleccionado.equals("L")){
                    datos.put("Importe", false);
                }else{
                    datos.put("Importe", true);
                }
                if (lugarProviene.equals("puntadaAcumularQr")){
                    datos.put("importedescuento", descuento);
                }

                myArray.put(datos);
            } catch (JSONException e) {
                e.printStackTrace();
            }



            String url = "http://" + ipEstacion + "/CorpogasService/api/ventaProductos/GuardaProductos/sucursal/" + sucursalId + "/origen/" + numerodispositivo + "/usuario/" + usuarioid + "/posicionCarga/" + posicionCarga;
            RequestQueue queue = Volley.newRequestQueue(this);
            JsonArrayRequest request_json = new JsonArrayRequest(Request.Method.POST, url, myArray,
                    new com.android.volley.Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            //Get Final response
                            if (correcto.equals("true")) {
                                String titulo = "AVISO";
                                String mensajes = "Listo para Iniciar Despacho";
                                final Modales modales = new Modales(EligePrecioLitros.this);

                                View view1 = modales.MostrarDialogoCorrecto(EligePrecioLitros.this, mensajes);
                                view1.findViewById(R.id.buttonAction).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        modales.alertDialog.dismiss();
//                                        Intent intent1 = new Intent(getApplicationContext(), Menu_Principal.class);
//                                        startActivity(intent1);
//                                        finish();
                                    }
                                });
                            } else {
                                try {
                                    String titulo = "AVISO";
                                    final Modales modales = new Modales(EligePrecioLitros.this);
                                    View view1 = modales.MostrarDialogoAlertaAceptar(EligePrecioLitros.this, mensaje, titulo);
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
                            Modales modales = new Modales(EligePrecioLitros.this);
                            View view1 = modales.MostrarDialogoAlertaAceptar(EligePrecioLitros.this, mensajes, titulo);
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





    private void ValidaTransaccionActiva() {
        if (!Conexion.compruebaConexion(this)) {
            Toast.makeText(getBaseContext(), "Sin conexión a la red ", Toast.LENGTH_SHORT).show();
            Intent intent1 = new Intent(getApplicationContext(), Menu_Principal.class);
            startActivity(intent1);
            finish();
        } else {
        String url = "http://" + ipEstacion + "/CorpogasService/api/ventaProductos/sucursal/" + sucursalId + "/posicionCargaId/" + posicionCarga;
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
                                if (banderaConDatos.equals(true)){
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
                                            Reintenta();
//                                        Toast.makeText(EligePrecioLitros.this, "Monto en CERO", Toast.LENGTH_SHORT).show();
                                        }else{
                                            Intent intent = new Intent(getApplicationContext(), FormasPago.class);
                                            intent.putExtra("numeroEmpleado", usuarioid);
                                            intent.putExtra("posicionCarga", posicionCarga);
                                            intent.putExtra("estacionjarreo", estacionJarreo);
                                            intent.putExtra("claveProducto", claveProducto);
                                            intent.putExtra("montoenCanasta", MontoenCanasta);
                                            intent.putExtra("numeroTarjeta", numeroTarjeta);
                                            intent.putExtra("descuento", descuento);
                                            intent.putExtra("nipCliente", nipCliente);
                                            startActivity(intent);
                                            finish();
                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }else{
                                    String titulo = "AVISO";
                                    String mensajes = "Error";
                                    Modales modales = new Modales(EligePrecioLitros.this);
                                    View view1 = modales.MostrarDialogoError(EligePrecioLitros.this,"Aun no concluye el despacho");
                                    view1.findViewById(R.id.buttonAction).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            modales.alertDialog.dismiss();
//                                            bar.cancel();
                                        }
                                    });
                                }
                            }else{
                                String titulo = "AVISO";
                                String mensajes = "Error";
                                Modales modales = new Modales(EligePrecioLitros.this);
                                View view1 = modales.MostrarDialogoError(EligePrecioLitros.this,Mensaje);
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
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();
            }
        });
        // Añade la peticion a la cola
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        eventoReq.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(eventoReq);

        }
    }

    private void Reintenta(){
        bar = new ProgressDialog(EligePrecioLitros.this);
        bar.setTitle("Cargando Productos");
        bar.setMessage("Ejecutando... ");
        bar.setIcon(R.drawable.gas);
        bar.setCancelable(false);
        bar.show();
        ValidaTransaccionActiva();
    }

    private void solicitarDespacho() {
        if (!Conexion.compruebaConexion(this)) {
            Toast.makeText(getBaseContext(), "Sin conexión a la red ", Toast.LENGTH_SHORT).show();
            Intent intent1 = new Intent(getApplicationContext(), Menu_Principal.class);
            startActivity(intent1);
            finish();
        }else {

        String url = "http://" + ipEstacion + "/CorpogasService/api/despachos/autorizaDespacho/posicionCargaId/" + posicionCarga + "/usuarioId/" + usuarioid; //usuarioid
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject respuesta = new JSONObject(response);
                    String correctoautoriza = respuesta.getString("Correcto");
                    String mensajeautoriza = respuesta.getString("Mensaje");
                    String objetoRespuesta = respuesta.getString("ObjetoRespuesta");
                    if (correctoautoriza.equals("true")) {
                        btnCobrar.setEnabled(true);
                        btnPredeterminado.setEnabled(false);
                        enviaMunu();
                    } else {
                        String titulo = "AVISO";
                        String mensaje = "La posición de carga se encuentra Ocupada";
                        Modales modales = new Modales(EligePrecioLitros.this);
                        View view1 = modales.MostrarDialogoAlertaAceptar(EligePrecioLitros.this,mensaje,titulo);
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

    private void enviaMunu() {
        String titulo = "AVISO";
        String mensaje = "Listo para Iniciar Despacho";
        final Modales modales = new Modales(EligePrecioLitros.this);
        View view1 = modales.MostrarDialogoCorrecto(EligePrecioLitros.this,mensaje);
        view1.findViewById(R.id.buttonAction).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modales.alertDialog.dismiss();
//                Intent intent1 = new Intent(getApplicationContext(), Menu_Principal.class);
//                startActivity(intent1);
//                finish();
            }
        });

    }


    private void cargaOpciones(){

        MyListAdapter adapter=new MyListAdapter(EligePrecioLitros.this, maintitle, subtitle,imgid);
        list.setAdapter(adapter);


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                // TODO Auto-generated method stub
                Cantidad.setVisibility(View.VISIBLE);
                Cantidad.setText("");
                EtiquetaCantidad.setVisibility(View.VISIBLE);
                if (position == 0) {
                    EtiquetaCantidad.setText("Litros");
                    TipoSeleccionado = "L";
                } else if (position == 1) {
                    EtiquetaCantidad.setText("Pesos");
                    TipoSeleccionado = "P";
                }
            }
        });

    }


    //Metodo para regresar a la actividad principal
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), Menu_Principal.class);
        startActivity(intent);
        finish();
    }

    //procedimiento para  cachar el Enter del teclado
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_ENTER:
                calculos();
                return true;
            default:
                return super.onKeyUp(keyCode, event);
        }
    }

    private void calculos() {
        //Se lee el password del objeto y se asigna a variable
        String cantidadNueva;

        cantidadNueva = Cantidad.getText().toString();
        if (cantidadNueva.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Ingresa la Cantidad", Toast.LENGTH_SHORT).show();
        } else {
            if (cantidadNueva.equals("0")){
                Toast.makeText(getApplicationContext(), "Ingresa una Cantidad mayor a 0", Toast.LENGTH_SHORT).show();
            }else{
                String mensaje;
                String titulo = "No podrás cambiar los datos posteriormente";
                if (TipoSeleccionado == "L") {
                    mensaje = "Estás seguro de que deseas cargar : " + cantidadNueva + " LITROS";
                }else{
                    mensaje = "Estás seguro de que deseas cargar : " + cantidadNueva + " PESOS";
                }
                Cantidad.setEnabled(false);
                Modales modales = new Modales(EligePrecioLitros.this);
                View viewLectura = modales.MostrarDialogoAlerta(EligePrecioLitros.this, mensaje,  "Ok", "Cancelar");
                viewLectura.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        modales.alertDialog.dismiss();
                        btnCobrar.setEnabled(true);
                        EnviarProductosPredeterminado();                    }
                });
                viewLectura.findViewById(R.id.buttonNo).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Cantidad.setText("");
                        modales.alertDialog.dismiss();
                    }
                });
            }
        }
    }


}