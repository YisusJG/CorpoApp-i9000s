package com.corpogas.corpoapp.VentaCombustible;

import androidx.appcompat.app.AppCompatActivity;

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

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.corpogas.corpoapp.Configuracion.SQLiteBD;
import com.corpogas.corpoapp.Menu_Principal;
import com.corpogas.corpoapp.Modales.Modales;
import com.corpogas.corpoapp.MyListAdapter;
import com.corpogas.corpoapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    TextView EtiquetaCantidad;
    String TipoSeleccionado, usuario, posicionCarga, usuarioid, estacionJarreo, claveProducto, precio;


    Button btnLibre, btnPredeterminado, btnCobrar;
    SQLiteBD data;
    String EstacionId,  ipEstacion, sucursalId;

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

        posicionCarga = getIntent().getStringExtra("posicionCarga");
        usuarioid = getIntent().getStringExtra("numeroEmpleado");
        estacionJarreo = getIntent().getStringExtra("estacionjarreo");
        claveProducto = getIntent().getStringExtra("claveProducto");
        precio = getIntent().getStringExtra("precioProducto");

        Cantidad = findViewById(R.id.CantidadPrecio);
        EtiquetaCantidad = findViewById(R.id.EtiquetaPrecioLista);
        btnLibre = (Button) findViewById(R.id.btnDespachoLibre);
        list= findViewById(R.id.lstOpcionesPrecio);


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

        btnPredeterminado = (Button) findViewById(R.id.btnPredeterminadoInicia);
        btnPredeterminado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(EligePrecioLitros.this, "Se habilito el predeterminado", Toast.LENGTH_SHORT).show();
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

        cargaOpciones();

    }

    private void ValidaTransaccionActiva() {
//        if (!Conexion.compruebaConexion(this)) {
//            Toast.makeText(getBaseContext(), "Sin conexión a la red ", Toast.LENGTH_SHORT).show();
//            Intent intent1 = new Intent(getApplicationContext(), Menu_Principal.class);
//
//            startActivity(intent1);
//            finish();
//        } else {
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
                                    Intent intent = new Intent(getApplicationContext(), FormasPago.class);
                                    intent.putExtra("numeroEmpleado", usuarioid);
                                    intent.putExtra("posicionCarga", posicionCarga);
                                    intent.putExtra("estacionjarreo", estacionJarreo);
                                    intent.putExtra("claveProducto", claveProducto);
                                    intent.putExtra("montoenCanasta", MontoenCanasta);
                                    startActivity(intent);
                                    finish();

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }else{
                                String titulo = "AVISO";
                                String mensajes = "Error";
                                Modales modales = new Modales(EligePrecioLitros.this);
                                View view1 = modales.MostrarDialogoError(EligePrecioLitros.this,Mensaje);
                                view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        modales.alertDialog.dismiss();
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

//        }
    }


    private void solicitarDespacho() {
//        if (!Conexion.compruebaConexion(this)) {
//            Toast.makeText(getBaseContext(), "Sin conexión a la red ", Toast.LENGTH_SHORT).show();
//            Intent intent1 = new Intent(getApplicationContext(), Menu_Principal.class);
//            startActivity(intent1);
//            finish();
//        }else {

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
//        }
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
        String cantidad;

        cantidad = Cantidad.getText().toString();
        if (cantidad.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Ingresa la Cantidad", Toast.LENGTH_SHORT).show();
        } else {
            if (cantidad.equals("0")){
                Toast.makeText(getApplicationContext(), "Ingresa una Cantidad mayor a 0", Toast.LENGTH_SHORT).show();
            }else{
                String mensaje;
                String titulo = "No podrás cambiar los datos posteriormente";
                if (TipoSeleccionado == "L") {
                    mensaje = "Estás seguro de que deseas cargar : " + cantidad + " LITROS";
                }else{
                    mensaje = "Estás seguro de que deseas cargar : " + cantidad + " PESOS";
                }
                Modales modales = new Modales(EligePrecioLitros.this);
                View viewLectura = modales.MostrarDialogoAlerta(EligePrecioLitros.this, mensaje,  "Ok", "Cancelar");
                viewLectura.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        modales.alertDialog.dismiss();
                        btnCobrar.setEnabled(true);
                        //Intent intent = new Intent(getApplicationContext(), eligeCombustible.class);
                        //intent.putExtra("posicion", posicion);
                        //intent.putExtra("usuario", usuarioid);
                        //intent.putExtra("Cantidad", cantidad);
                        //intent.putExtra("TipoSeleccionado", TipoSeleccionado);
                        //startActivity(intent);
                        //finish();
                    }
                });

                viewLectura.findViewById(R.id.buttonNo).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        modales.alertDialog.dismiss();
                    }
                });
            }
        }
    }


}