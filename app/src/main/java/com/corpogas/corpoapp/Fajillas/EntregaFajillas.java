package com.corpogas.corpoapp.Fajillas;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.device.scanner.configuration.Triggering;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.corpogas.corpoapp.Conexion;
import com.corpogas.corpoapp.Configuracion.SQLiteBD;
import com.corpogas.corpoapp.Entities.Accesos.AccesoUsuario;
import com.corpogas.corpoapp.Entities.Classes.RespuestaApi;
import com.corpogas.corpoapp.Menu_Principal;
import com.corpogas.corpoapp.Modales.Modales;
import com.corpogas.corpoapp.Productos.ListAdapterProductos;
import com.corpogas.corpoapp.Productos.VentasProductos;
import com.corpogas.corpoapp.R;
import com.corpogas.corpoapp.ScanManagerProvides;
import com.corpogas.corpoapp.ValesPapel.ValesPapel;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class EntregaFajillas extends AppCompatActivity {
    String model;

    ScanManagerProvides scanManagerProvides;
    String result = "";

    long numeroIslas, valorTipoFajilla=1 ;
    Spinner spnFajillas;
    EditText cantidad;
    Button aceptar, btnIncrementar, btnDecrementar, agregarFolio;
    ListView listFolio;
    ArrayList<String> FolioFajilla, Folio, ValorFolio;
    ArrayList<String> tipoFajilla, tipoMorralla, contaStrings;
    String lugarProviene, m_deviceName, ipEstacion, claveUsuario, nombreCompleto,  password, islaId, descripciones ;
    String fajillaSeleccionada, fajillaSeleccionadaDescripcion;
//    String[] opciones2 = new String[2];
    ImageButton imgEscanearFajilla;
    SQLiteBD db;
    Long sucursalId, idUsuario, totalFajillas ;
    ListAdapterProductos adapterP;

//    RespuestaApi<AccesoUsuario> respuestaApiAccesoUsuario;



    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrega_fajillas);
        model = Build.MODEL;

        SQLiteBD data = new SQLiteBD(getApplicationContext());
        this.setTitle(data.getNombreEstacion() + " ( EST.:" + data.getNumeroEstacion() + ")");
        sucursalId = Long.parseLong(data.getIdSucursal());
        ipEstacion = data.getIpEstacion();


        init();
        setVariables();

        if (model.equals("i9000S")) {
            scanManagerProvides = new ScanManagerProvides();
            imgEscanearFajilla.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if (view.getId() == R.id.imgEscanearFajilla) {
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

            cantidad.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
//                    if (!cantidad.getText().toString().equals("")) {
//                        if (cantidad.getText().toString().length() > 6) {
//                            AgregarFajillaaLista(cantidad.getText().toString());
//                        }
//                    }
                }
            });
        } else {
            imgEscanearFajilla.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    IntentIntegrator integrator = new IntentIntegrator(EntregaFajillas.this);
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

    protected void onActivityResult (int requestCode, int resulCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resulCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(getApplicationContext(), "Lectura Cancelada", Toast.LENGTH_SHORT).show();
            } else {
//                    Toast.makeText(getApplicationContext(), result.getContents(), Toast.LENGTH_SHORT).show();
//                    Producto.setText(result.getContents());
                AgregarFajillaaLista(result.getContents());
            }
        } else {
            super.onActivityResult(requestCode, resulCode, data);
        }
    }

//    @Override
//    public boolean dispatchKeyEvent(KeyEvent event) {
//        if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() != KeyEvent.KEYCODE_ENTER) { //Not Adding ENTER_KEY to barcode String
//            char pressedKey = (char) event.getUnicodeChar();
//            result += pressedKey;
//        }
//        if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {  //Any method handling the data
//            cantidad.setText(result);
//            result = "";
//        }
//        return false;
//    }

    @Override
    protected void onResume() {
        super.onResume();
        if (model.equals("i9000S")) {
            scanManagerProvides.initScan(EntregaFajillas.this);
        }
    }

    private void init(){
        db = new SQLiteBD(getApplicationContext());
//        m_deviceName = getIntent().getStringExtra("device_name");
//        islaId = getIntent().getStringExtra("IslaId");
        lugarProviene = getIntent().getStringExtra("lugarProviene");
        listFolio = findViewById(R.id.listFolio);
        FolioFajilla = new ArrayList<String>();
        ValorFolio = new ArrayList<String>();
        Folio = new ArrayList<String>();
        tipoFajilla = new ArrayList<String>();
        tipoMorralla = new ArrayList<String>();
        contaStrings = new ArrayList<String>();

        if (lugarProviene.equals("corteFajillas")){
            idUsuario = Long.valueOf(db.getUsuarioId());
        }else{
//            respuestaApiAccesoUsuario = (RespuestaApi<AccesoUsuario>) getIntent().getSerializableExtra("accesoUsuario");
            nombreCompleto = db.getNombreCompleto();//respuestaApiAccesoUsuario.getObjetoRespuesta().getNombreCompleto();
            idUsuario = Long.parseLong(db.getUsuarioId());//respuestaApiAccesoUsuario.getObjetoRespuesta().getNumeroEmpleado();
            password = db.getClave();//respuestaApiAccesoUsuario.getObjetoRespuesta().getClave();
        }

    }

    private void setVariables(){

        spnFajillas = (Spinner) findViewById(R.id.spFajillas);
        cantidad =  findViewById(R.id.etCantidad);
        aceptar = (Button) findViewById(R.id.btnAceptarFajilla);
        agregarFolio = findViewById(R.id.agregarFolio);
        imgEscanearFajilla = (ImageButton) findViewById(R.id.imgEscanearFajilla);

//        imgEscanearFajilla.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                IntentIntegrator integrator = new IntentIntegrator(EntregaFajillas.this);
//                integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
//                integrator.setPrompt("Lector - CDP");
//                integrator.setCameraId(0);
//                integrator.setBeepEnabled(false);
//                integrator.setBarcodeImageEnabled(true);
//                integrator.initiateScan();
//
//            }
//        });

        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!tipoMorralla.isEmpty()) {
                    if (valorTipoFajilla == 2 ) {
                        Intent intent1 = new Intent(EntregaFajillas.this, AutorizaFajillas.class); //despachdorclave
                        intent1.putExtra("lugarProviene", lugarProviene);
                        intent1.putExtra("TipoFajilla", valorTipoFajilla);
                        intent1.putStringArrayListExtra("TipoFajillas", tipoMorralla);
                        intent1.putExtra("tipoFajillaFM", "Morralla");
                        startActivity(intent1);
                    }
                } else if (!tipoFajilla.isEmpty()) {
                    if (valorTipoFajilla == 1) {
                        Intent intent1 = new Intent(EntregaFajillas.this, AutorizaFajillas.class); //despachdorclave
                        intent1.putExtra("lugarProviene", lugarProviene);
                        intent1.putExtra("TipoFajilla", valorTipoFajilla);
                        intent1.putStringArrayListExtra("Folio", Folio);
                        intent1.putStringArrayListExtra("TipoFajillas", tipoFajilla);
                        intent1.putExtra("tipoFajillaFM", "Fajilla");
                        startActivity(intent1);
                    }
                } else {
                    Toast.makeText(EntregaFajillas.this, "Debe ingresar por lo menos una entrada", Toast.LENGTH_SHORT).show();
                    cantidad.setText("");
                }

//                if (cantidad.length() != 0){
//                    if (valorTipoFajilla !=0){
//                        totalFajillas = Long.parseLong (String.valueOf(cantidad.getText()));
//                        if (totalFajillas >4){
//                            Toast.makeText(EntregaFajillas.this, "La Cantidad maxima de fajillas es 4", Toast.LENGTH_SHORT).show();
//                            cantidad.setText("");
//                        }else{
//                            if (totalFajillas == 0){
//                                Toast.makeText(EntregaFajillas.this, "La Cantidad cargada no puede ser CERO", Toast.LENGTH_SHORT).show();
//                                cantidad.setText("");
//                            }else{
//                                //ENVIA AUTORIZACION
//                                Intent intent1 = new Intent(EntregaFajillas.this, AutorizaFajillas.class); //despachdorclave
//                                intent1.putExtra("lugarProviene", lugarProviene);
//                                intent1.putExtra("TotalFajillas", totalFajillas);
//                                intent1.putExtra("TipoFajilla", valorTipoFajilla);
//                                startActivity(intent1);
//                                finish();
//                            }
//                        }
//                    }else{
//                        Toast.makeText(EntregaFajillas.this, "Seleccione uno de los Tipos de Fajilla", Toast.LENGTH_SHORT).show();
//                    }
//                }else{
//                    Toast.makeText(EntregaFajillas.this, "Teclee una Cantidad", Toast.LENGTH_SHORT).show();
//                }
            }
        });

//        btnIncrementar = (Button) findViewById(R.id.btnAceptarFajilla);
//        btnDecrementar = (Button) findViewById(R.id.btnAceptarFajilla);

        agregarFolio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AgregarFajillaaLista(cantidad.getText().toString());
            }
        });

        ObtieneFajillas();


    }

//    protected void onActivityResult (int requestCode, int resulCode, Intent data) {
//        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resulCode, data);
//        if (result != null) {
//            if (result.getContents() == null) {
//                Toast.makeText(getApplicationContext(), "Lectura Cancelada", Toast.LENGTH_SHORT).show();
//            } else {
//                cantidad.setText(result.getContents());
//            }
//        } else {
//            super.onActivityResult(requestCode, resulCode, data);
//        }
//    }


    private void ObtieneFajillas(){
        ArrayList<String> comboTipoFajilla = new ArrayList<String>();

        if (!Conexion.compruebaConexion(this)) {
            Toast.makeText(getBaseContext(), "Sin conexión a la red ", Toast.LENGTH_SHORT).show();
            Intent intent1 = new Intent(getApplicationContext(), Menu_Principal.class);

            startActivity(intent1);
            finish();
        } else {
            String url = "http://" + ipEstacion + "/CorpogasService/api/PrecioFajillas/sucursal/" + sucursalId;
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject respuesta = new JSONObject(response);
                        String correcto = respuesta.getString("Correcto");
                        String mensaje = respuesta.getString("Mensaje");

                        if (correcto.equals("true") ) {
                            String objetoRespuesta = respuesta.getString("ObjetoRespuesta");
                            JSONArray tiposFajilla = new JSONArray(objetoRespuesta);


                            for (int i = 0; i <tiposFajilla.length() ; i++){
                                JSONObject respuestaTipoFajilla = tiposFajilla.getJSONObject(i);
                                String describeTipoFajilla = respuestaTipoFajilla.getString("TipoFajilla");
                                String idFajilla = respuestaTipoFajilla.getString("Price");
//                                opciones[i] = describeTipoFajilla + " - $" + idFajilla;
                                comboTipoFajilla.add(describeTipoFajilla + " - $" + idFajilla);
                            }
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(EntregaFajillas.this, R.layout.support_simple_spinner_dropdown_item, comboTipoFajilla);
                            spnFajillas.setAdapter(adapter);
                            spnFajillas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    fajillaSeleccionadaDescripcion = adapterView.getItemAtPosition(i).toString();
                                    fajillaSeleccionada = fajillaSeleccionadaDescripcion.substring(0, fajillaSeleccionadaDescripcion.indexOf(" -"));
                                    switch (fajillaSeleccionada){
                                        case "BILLETE":
                                            valorTipoFajilla = 1;
                                            findViewById(R.id.tvTitulo2).setVisibility(View.VISIBLE);
                                            findViewById(R.id.etCantidad).setVisibility(View.VISIBLE);
                                            findViewById(R.id.imgEscanearFajilla).setVisibility(View.VISIBLE);
                                            agregarFolio.setText("Agregar Folio");
                                            break;
                                        case "MORRALLA":
                                            valorTipoFajilla = 2;
                                            findViewById(R.id.tvTitulo2).setVisibility(View.GONE);
                                            findViewById(R.id.etCantidad).setVisibility(View.GONE);
                                            findViewById(R.id.imgEscanearFajilla).setVisibility(View.GONE);
                                            agregarFolio.setText("Agregar Morralla");
                                            break;
                                        default:
                                            valorTipoFajilla =1;
                                            break;
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {

                                }
                            });

                        }else{
                            Toast.makeText(EntregaFajillas.this, "Sin Tipos de Fahilla", Toast.LENGTH_SHORT).show();
                            Intent intent1 = new Intent(getApplicationContext(), Menu_Principal.class);
                            //Se envian los parametros de posicion y usuario
                            intent1.putExtra("device_name", m_deviceName);
                            //inicia el activity
                            startActivity(intent1);
                            finish();

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
            requestQueue.add(stringRequest);
        }
    }


    //Metodo para regresar a la actividad principal
    @Override
    public void onBackPressed() {
        if (lugarProviene.equals("corteFajillas")){
            finish();
        }else{
            //Se instancia y se llama a la clase Venta de Productos
            Intent intent1 = new Intent(getApplicationContext(), Menu_Principal.class);
            //Se envian los parametros de posicion y usuario
            intent1.putExtra("device_name", m_deviceName);
            //inicia el activity
            startActivity(intent1);
            finish();
        }
    }


    private void AgregarFajillaaLista(String cantidad) {
        spnFajillas = (Spinner) findViewById(R.id.spFajillas);
//        if (cantidad.length() == 0) {
            if (fajillaSeleccionada.equals("MORRALLA")) {
                if (valorTipoFajilla == 2) {
                    ValorFolio.add("Tipo " + fajillaSeleccionadaDescripcion);
                    tipoMorralla.add("2");
                    int conta = (int) tipoMorralla.size();
                    contaStrings.add(String.valueOf(conta));
                    adapterP = new ListAdapterProductos(this, contaStrings, ValorFolio);
                    listFolio.setTextFilterEnabled(true);
                    listFolio.setAdapter(adapterP);
                    listFolio.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int identificador, long l) {
                            String titulo = "¿Estás seguro?";
                            String mensajes = "¿Deseas eliminar el elemento seleccionado?";
                            Modales modalesA = new Modales(EntregaFajillas.this);
                            View viewLectura = modalesA.MostrarDialogoAlerta(EntregaFajillas.this, mensajes, "SI", "NO");
                            viewLectura.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    ValorFolio.remove(identificador);
                                    contaStrings.remove(identificador);

                                    ListAdapterProductos adapterP = new ListAdapterProductos(EntregaFajillas.this, contaStrings, ValorFolio);
                                    listFolio.setAdapter(adapterP);
                                    adapterP.notifyDataSetChanged();
                                    modalesA.alertDialog.dismiss();
                                }
                            });
                            viewLectura.findViewById(R.id.buttonNo).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    modalesA.alertDialog.dismiss();
                                }
                            });

                        }
                    });

                }
            } else {
                boolean bandera = false;
                for (Integer m = 0; m < Folio.size(); m++) {
                    String codigo = Folio.get(m);
                    if (codigo.equals(cantidad)) {
                        bandera = true;
                        break;
                    } else {
                        bandera = false;
                    }
                }
                if (!bandera) {
                    if (valorTipoFajilla == 1) {
                        ValorFolio.add("Tipo Fajilla " + fajillaSeleccionadaDescripcion);
                        tipoFajilla.add("1");
                    }
//                else if (valorTipoFajilla == 2) {
//                    ValorFolio.add("Tipo Fajilla "+fajillaSeleccionadaDescripcion);
//                    tipoFajilla.add("2");
//                }
                    Folio.add(cantidad);
                    adapterP = new ListAdapterProductos(this, Folio, ValorFolio);
                    listFolio.setTextFilterEnabled(true);
                    listFolio.setAdapter(adapterP);
                    listFolio.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int identificador, long l) {
                            String titulo = "Estas seguro?";
                            String mensajes = "Deseas eliminar el elemento seleccionado?";
                            Modales modalesA = new Modales(EntregaFajillas.this);
                            View viewLectura = modalesA.MostrarDialogoAlerta(EntregaFajillas.this, mensajes, "SI", "NO");
                            viewLectura.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    ValorFolio.remove(identificador);
                                    Folio.remove(identificador);

                                    ListAdapterProductos adapterP = new ListAdapterProductos(EntregaFajillas.this, Folio, ValorFolio);
                                    listFolio.setAdapter(adapterP);
                                    adapterP.notifyDataSetChanged();
                                    modalesA.alertDialog.dismiss();
                                }
                            });
                            viewLectura.findViewById(R.id.buttonNo).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    modalesA.alertDialog.dismiss();
                                }
                            });

                        }
                    });
                } else {
                    String titulo = "AVISO";
                    String mensaje = "El folio No. " + cantidad + " ya fue agregado";
                    Modales modales = new Modales(EntregaFajillas.this);
                    View view1 = modales.MostrarDialogoAlertaAceptar(EntregaFajillas.this, mensaje, titulo);
                    view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            modales.alertDialog.dismiss();
                        }
                    });
                }
            }
//        }
    }


//    private void AgregarFajillaaLista2(String cantidad) {
//        spnFajillas = (Spinner) findViewById(R.id.spFajillas);
////        if (cantidad.length() == 0) {
//        if (fajillaSeleccionada.equals("MORRALLA")) {
//            if (valorTipoFajilla == 2) {
//                ValorFolio.add("Tipo " + fajillaSeleccionadaDescripcion);
//                tipoFajilla.add("2");
//                int conta = (int) tipoFajilla.size();
//                Folio.add(String.valueOf(conta));
//            }
//        } else {
//            boolean bandera = false;
//            for (Integer m = 0; m < Folio.size(); m++) {
//                String codigo = Folio.get(m);
//                if (codigo.equals(cantidad)) {
//                    bandera = true;
//                    break;
//                } else {
//                    bandera = false;
//                }
//            }
//            if (!bandera) {
//                if (valorTipoFajilla == 1) {
//                    ValorFolio.add("Tipo " + fajillaSeleccionadaDescripcion);
//                    tipoFajilla.add("1");
//                }
////                else if (valorTipoFajilla == 2) {
////                    ValorFolio.add("Tipo Fajilla "+fajillaSeleccionadaDescripcion);
////                    tipoFajilla.add("2");
////                }
//                Folio.add(cantidad);
//
//            } else {
//                String titulo = "AVISO";
//                String mensaje = "El folio No. " + cantidad + " ya fue agregado";
//                Modales modales = new Modales(EntregaFajillas.this);
//                View view1 = modales.MostrarDialogoAlertaAceptar(EntregaFajillas.this, mensaje, titulo);
//                view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        modales.alertDialog.dismiss();
//                    }
//                });
//            }
//        }
//        adapterP = new ListAdapterProductos(this, Folio, ValorFolio);
//        listFolio.setTextFilterEnabled(true);
//        listFolio.setAdapter(adapterP);
//        listFolio.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int identificador, long l) {
//                String titulo = "Estas seguro?";
//                String mensajes = "Deseas eliminar el elemento seleccionado?";
//                Modales modalesA = new Modales(EntregaFajillas.this);
//                View viewLectura = modalesA.MostrarDialogoAlerta(EntregaFajillas.this, mensajes, "SI", "NO");
//                viewLectura.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        ValorFolio.remove(identificador);
//                        Folio.remove(identificador);
//
//                        ListAdapterProductos adapterP = new ListAdapterProductos(EntregaFajillas.this, Folio, ValorFolio);
//                        listFolio.setAdapter(adapterP);
//                        adapterP.notifyDataSetChanged();
//                        modalesA.alertDialog.dismiss();
//                    }
//                });
//                viewLectura.findViewById(R.id.buttonNo).setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        modalesA.alertDialog.dismiss();
//                    }
//                });
//            }
//        });
//    }
}