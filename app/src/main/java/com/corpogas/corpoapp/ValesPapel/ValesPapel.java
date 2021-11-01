package com.corpogas.corpoapp.ValesPapel;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.corpogas.corpoapp.Configuracion.SQLiteBD;
import com.corpogas.corpoapp.LecturaTarjetas.MonederosElectronicos;
import com.corpogas.corpoapp.Menu_Principal;
import com.corpogas.corpoapp.Modales.Modales;
import com.corpogas.corpoapp.Productos.ListAdapterProductos;
import com.corpogas.corpoapp.Puntada.PuntadaRedimirQr;
import com.corpogas.corpoapp.R;
import com.corpogas.corpoapp.Tickets.PosicionCargaTickets;
import com.corpogas.corpoapp.VentaCombustible.DiferentesFormasPago;
import com.corpogas.corpoapp.VentaCombustible.FormasPago;
import com.corpogas.corpoapp.VentaCombustible.VentaProductos;
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

public class ValesPapel extends AppCompatActivity {
    Spinner spnValesPapel;
    Button btnAgregarVale, btnAceptarValesPapel, btnOtrasFormasPago;
    ListView lstValesPapel;
    ImageButton imgEscanearVale, imgEscanearVale2;
    int sumaTiposVale=0, TipoValesPapelId;
    String[] opcionesTipoVale;// = new String[sumaTiposVale];
    Double montoaCobrar;
    String posicionCarga, usuarioid, estacionJarreo, claveProducto,  ValeSeleccionado, ipEstacion, ValePapelId = "2", idoperativa;
    EditText  tvFolioMonto, tvDenominacionMonto;
    TextView tvMontoACargar, tvMontoACargarPendiente;
    List<String> IDMontos;
    List<String> NombreTipoValePapel;
    List<String> MontoVale;
    List<String> FolioValePapel;
    List<String> TipoValesPapel;

    SQLiteBD data;
    String url, fechaTicket;
    JSONObject FormasPagoObjecto, datos;
    JSONArray FormasPagoArreglo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vales_papel);
        data = new SQLiteBD(getApplicationContext());
        this.setTitle(data.getNombreEstacion() + " ( EST.:" + data.getNumeroEstacion() + ")");

        posicionCarga = getIntent().getStringExtra("posicioncarga");
        usuarioid = getIntent().getStringExtra("numeroEmpleado");
        estacionJarreo = getIntent().getStringExtra("estacionjarreo");
        claveProducto = getIntent().getStringExtra("claveProducto");
        montoaCobrar = getIntent().getDoubleExtra("montoencanasta", 0);
        idoperativa = getIntent().getStringExtra("idoperativa");

        init();
        ObtieneTiposVale();

        btnAgregarVale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AgregarValePapelaLista();
            }
        });

        btnAceptarValesPapel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (idoperativa.equals("20")){
                    EnviarValesPapel();
                }else{
//                    String titulo = "PUNTADA";
//                    String mensajes = "Desea Acumular la venta a su Tarjeta Puntada?";
//                    Modales modalesPuntada = new Modales(ValesPapel.this);
//                    View viewLectura = modalesPuntada.MostrarDialogoAlerta(ValesPapel.this, mensajes,  "SI", "NO");
//                    viewLectura.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            String banderaHuella = getIntent().getStringExtra( "banderaHuella");
//                            String nombreCompletoVenta = getIntent().getStringExtra("nombrecompleto");
//                            //LeeTarjeta();
//                            Intent intent = new Intent(getApplicationContext(), MonederosElectronicos.class);
//                            //intent.putExtra("device_name", m_deviceName);
//                            intent.putExtra("Enviadodesde", "formaspago");
//                            intent.putExtra("numeroEmpleado", data.getNumeroEmpleado());
//                            intent.putExtra("idoperativa", 1);
//                            intent.putExtra("formapagoid", "2");
//                            intent.putExtra("NombrePago", "Vales");
//                            intent.putExtra("NombreCompleto", data.getNombreCompleto());
//                            intent.putExtra("montoenlacanasta", montoaCobrar);
//                            intent.putExtra("posicioncargaid", posicionCarga);
//                            intent.putExtra("tipoTarjeta", "Puntada");
//                            intent.putExtra("pagoconpuntada", "no");
//
//                            startActivity(intent);
//                            modalesPuntada.alertDialog.dismiss();
//                        }
//                    });
//                    viewLectura.findViewById(R.id.buttonNo).setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
////                                    RespuestaImprimeFinaliza(posicioncarga, idusuario, formapagoid, numticket, nombrepago);
//                            modalesPuntada.alertDialog.dismiss();
////                                    SeleccionaPesosDoalares();
                            EnviarValesPapel();
//                        }
//                    });
//
                }
            }
        });

        btnOtrasFormasPago.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EnviaraDiferentesFormasPago();
            }
        });

        imgEscanearVale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator integrator = new IntentIntegrator(ValesPapel.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                integrator.setPrompt("Lector - CDP");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(false);
                integrator.setBarcodeImageEnabled(true);
                integrator.initiateScan();
            }
        });

        imgEscanearVale2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator integrator = new IntentIntegrator(ValesPapel.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                integrator.setPrompt("Lector - CDP");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(false);
                integrator.setBarcodeImageEnabled(true);
                integrator.initiateScan();
            }
        });

    }

    protected void onActivityResult (int requestCode, int resulCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resulCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(getApplicationContext(), "Lectura Cancelada", Toast.LENGTH_SHORT).show();
            } else {
//                if (result.getContents().length() >= 17) {
                    tvFolioMonto.setText(result.getContents());
//                } else {
//                    int resultInt = Integer.parseInt(result.getContents());
//                    String residuo = result.getContents().substring(result.getContents().length() - 2);
////                    int conversion = resultInt/100;
////                    String resultConversion = Integer.toString(conversion);
//                    tvDenominacionMonto.setText(resultConversion);
//                    tvDenominacionMonto.setText(result.getContents());
//                }
            }
        } else {
            super.onActivityResult(requestCode, resulCode, data);
        }
    }

    private  void EnviaraDiferentesFormasPago(){
        if (tvMontoACargarPendiente.getText().toString().equals("0.00")){
            String titulo = "AVISO";
            Modales modales = new Modales(ValesPapel.this);
            View view1 = modales.MostrarDialogoError(ValesPapel.this, "Ya se ha completado el total de la venta");
            view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    modales.alertDialog.dismiss();
                }
            });
        }else{
            JSONObject folioDatos;// = new JSONObject();
            JSONObject datosVale = null; //= new JSONObject();
            JSONArray datosGlobal = new JSONArray();
            datos = new JSONObject();
            Double montoVales=0.00;
            FormasPagoArreglo = new JSONArray();

            for(int m=0; m < NombreTipoValePapel.size(); m++){
                try {
                    folioDatos = new JSONObject();
                    datosVale = new JSONObject();
                    folioDatos.put("Folio", FolioValePapel.get(m));
                    folioDatos.put("SucursalId", data.getIdSucursal());
                    folioDatos.put("PosicionCargaId", posicionCarga);
                    folioDatos.put("TipoValePapelId", TipoValesPapelId);
                    datosVale.put("Id", ValePapelId);
                    datosVale.put ("Importe", IDMontos.get(m));
                    montoVales = montoVales + Double.parseDouble(IDMontos.get(m));
                    datosVale.put("DetalleParcialidad", folioDatos);
                    datosGlobal.put(datosVale);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            Intent intentDiferente = new Intent(getApplicationContext(), DiferentesFormasPago.class);//DiferentesFormasPagoPuntada
            intentDiferente.putExtra("Enviadodesde", "valespapel");
            intentDiferente.putExtra("posicioncarga", posicionCarga);
            intentDiferente.putExtra("idoperativa", 5);
            intentDiferente.putExtra("montoencanasta", montoaCobrar- montoVales);
            intentDiferente.putExtra("arregloVales", datosGlobal.toString());
            intentDiferente.putExtra("montoencanastavales", montoVales);

            startActivity(intentDiferente);
            finish();

        }
    }

    private void AgregarValePapelaLista(){
        Double montoPendiente;

        //Validamos campos a agregar <> de vacio
        if (tvFolioMonto.length() ==0){
//            Toast.makeText(ValesPapel.this, "Digite un Folio", Toast.LENGTH_SHORT).show();
            String titulo = "AVISO";
            Modales modales = new Modales(ValesPapel.this);
            View view1 = modales.MostrarDialogoAlertaAceptar(ValesPapel.this,"Digite un folio",titulo);
            view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    modales.alertDialog.dismiss();
                }
            });

        }else{
            if (tvDenominacionMonto.length() == 0){
//                Toast.makeText(ValesPapel.this, "Digite un Monto", Toast.LENGTH_SHORT).show();
                String titulo = "AVISO";
                Modales modales = new Modales(ValesPapel.this);
                View view1 = modales.MostrarDialogoAlertaAceptar(ValesPapel.this,"Digite un monto",titulo);
                view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        modales.alertDialog.dismiss();
                    }
                });

            }else{
                Double montofaltante = Double.parseDouble(tvMontoACargarPendiente.getText().toString());
                if (montofaltante<=0){
//                    Toast.makeText(ValesPapel.this, "Monto Completado", Toast.LENGTH_SHORT).show();
                    String titulo = "AVISO";
                    Modales modales = new Modales(ValesPapel.this);
                    View view1 = modales.MostrarDialogoAlertaAceptar(ValesPapel.this,"Monto Completado",titulo);
                    view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            modales.alertDialog.dismiss();
                        }
                    });

                }else{
                    montoPendiente = Double.parseDouble(tvMontoACargarPendiente.getText().toString());

//                    if (Double.parseDouble(tvDenominacionMonto.getText().toString()) > montoPendiente){
//                        Toast.makeText(ValesPapel.this, "El monto de los vales no puede ser mayor que el monto Total", Toast.LENGTH_SHORT).show();
//                    }else{

                        boolean bandera=false;
                        for(Integer m = 0; m<FolioValePapel.size(); m++){
                            String codigo = FolioValePapel.get(m);
                            if (codigo.equals(tvFolioMonto.getText().toString())){
                                bandera=true;
                                break;
                            }else{
                                bandera=false;
                            }
                        }
                        if (bandera == false){
                            Double resultado = montoPendiente - Double.parseDouble(tvDenominacionMonto.getText().toString());
                            tvMontoACargarPendiente.setText(resultado.toString());
                            NombreTipoValePapel.add(ValeSeleccionado + "    |     $"+tvDenominacionMonto.getText().toString()); // + "    |    " + IdProductos );
                            IDMontos.add(tvDenominacionMonto.getText().toString());
                            MontoVale.add(tvDenominacionMonto.getText().toString());
                            FolioValePapel.add(tvFolioMonto.getText().toString());
                            ListAdapterProductos adapterP = new ListAdapterProductos(this, NombreTipoValePapel, FolioValePapel);
                            lstValesPapel.setTextFilterEnabled(true);
                            lstValesPapel.setAdapter(adapterP);
//                            lstValesPapel.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//                                @Override
//                                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
////                                    montoPendiente = Double.parseDouble(tvMontoACargarPendiente.getText().toString());
//                                    NombreTipoValePapel.remove(i);
////                                    MontoVale.remove(i);
//
//                                    adapterP.notifyDataSetChanged();
//                                    return false;
//                                }
//                            });
                            lstValesPapel.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int identificador, long l) {
//                                    Toast.makeText(ValesPapel.this, "asda", Toast.LENGTH_SHORT).show();
                                    String titulo = "Estas seguro?";
                                    String mensajes = "Deseas eliminar el elemento seleccionado?";
                                    Modales modalesA = new Modales(ValesPapel.this);
                                    View viewLectura = modalesA.MostrarDialogoAlerta(ValesPapel.this, mensajes,  "SI", "NO");
                                    viewLectura.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
//                                            ID.remove(identificador);
                                            Double total = Double.parseDouble(IDMontos.get(identificador)) + Double.parseDouble(tvMontoACargarPendiente.getText().toString());
                                            tvMontoACargarPendiente.setText(total.toString());

                                            MontoVale.remove(identificador);
                                            NombreTipoValePapel.remove(identificador);
                                            FolioValePapel.remove(identificador);
                                            IDMontos.remove(identificador);

                                            ListAdapterProductos adapterP = new ListAdapterProductos(ValesPapel.this, NombreTipoValePapel, FolioValePapel );
                                            lstValesPapel.setAdapter(adapterP);
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
                            tvDenominacionMonto.setText("");
                            tvFolioMonto.setText("");
                        }else{
                                String titulo = "AVISO";
                                String mensaje = "El folio No. "+ tvFolioMonto.getText().toString() +" ya fue agregado";
                                Modales modales = new Modales(ValesPapel.this);
                                View view1 = modales.MostrarDialogoAlertaAceptar(ValesPapel.this,mensaje,titulo);
                                view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        modales.alertDialog.dismiss();
                                    }
                                });
//                            Toast.makeText(ValesPapel.this, "El folio No. "+ tvFolioMonto.getText().toString() +" ya fue agregado", Toast.LENGTH_SHORT).show();
                        }
//                    }
                }
            }
        }
    }

    private void EnviarValesPapel() {
        if (Double.parseDouble(tvMontoACargarPendiente.getText().toString())> 0 ){
            String titulo = "AVISO";
            String mensaje = "Se debe cubrir el 100% del monto total";
            Modales modales = new Modales(ValesPapel.this);
            View view1 = modales.MostrarDialogoAlertaAceptar(ValesPapel.this,mensaje,titulo);
            view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    modales.alertDialog.dismiss();
                }
            });

        }else{

            JSONObject folioDatos;// = new JSONObject();
            JSONObject datosVale = null; //= new JSONObject();
            JSONArray datosGlobal = new JSONArray();
            datos = new JSONObject();
            FormasPagoArreglo = new JSONArray();

            for(int m=0; m < NombreTipoValePapel.size(); m++){
                try {
                    folioDatos = new JSONObject();
                    datosVale = new JSONObject();
                    folioDatos.put("Folio", FolioValePapel.get(m));
                    folioDatos.put("SucursalId", data.getIdSucursal());
                    folioDatos.put("PosicionCargaId", posicionCarga);
                    folioDatos.put("TipoValePapelId", TipoValesPapelId);
                    datosVale.put("Id", ValePapelId);
                    datosVale.put ("Importe", IDMontos.get(m));
                    datosVale.put("DetalleParcialidad", folioDatos);
                    datosGlobal.put(datosVale);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
    //        FormasPagoArreglo.put(datosVale);
            JSONObject datos = new JSONObject();
            String url = "http://" + ipEstacion + "/CorpogasService/api/tickets/generar";
            RequestQueue queue = Volley.newRequestQueue(this);
            try {
                datos.put("PosicionCargaId", posicionCarga);
                datos.put("IdUsuario",  data.getUsuarioId());
                datos.put("SucursalId", data.getIdSucursal());
                datos.put("IdFormasPago", datosGlobal);
                datos.put("SucursalId", data.getIdSucursal());
                datos.put("ConfiguracionAplicacionId", data.getIdTarjtero());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JsonObjectRequest request_json = new JsonObjectRequest(Request.Method.POST, url, datos, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    String detalle = null;
                    try {
                        detalle = response.getString("Detalle");
                        if (detalle.equals("null")) {
                            String estado1 = response.getString("Resultado");
                            JSONObject descripcion = new JSONObject(estado1);
                            String estado = descripcion.getString("Descripcion");
                            try {
                                String titulo = "AVISO";
                                String mensaje = estado;
                                Modales modales = new Modales(ValesPapel.this);
                                View view1 = modales.MostrarDialogoError(ValesPapel.this, mensaje);
                                view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(getApplicationContext(), Menu_Principal.class);
                                        startActivity(intent);
                                        finish();
                                        modales.alertDialog.dismiss();
                                    }
                                });
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        } else {
                            String titulo = "AVISO";
                            Modales modales = new Modales(ValesPapel.this);
                            View view1 = modales.MostrarDialogoCorrecto(ValesPapel.this, "Ticket Impreso en Impresora Central");
                            view1.findViewById(R.id.buttonAction).setOnClickListener(new View.OnClickListener() { //buttonYes
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(getApplicationContext(), Menu_Principal.class);
                                    startActivity(intent);
                                    finish();
                                    modales.alertDialog.dismiss();
                                }
                            });
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(ValesPapel.this, "error", Toast.LENGTH_SHORT).show();

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

    }

    private void LimpiarPantallaValesPapel(){

    }

    private void init() {
        spnValesPapel = (Spinner) findViewById(R.id.spTipoValePapel);
        btnAgregarVale = (Button) findViewById(R.id.btnAgregarVale);
        btnAceptarValesPapel = (Button) findViewById(R.id.btnAceptarValesPapel);
        btnOtrasFormasPago = (Button) findViewById(R.id.btnOtrasFormasPago);
        lstValesPapel = (ListView) findViewById(R.id.lstValesPapel);
        imgEscanearVale = (ImageButton) findViewById(R.id.imgEscanearVale);
        imgEscanearVale2 = (ImageButton) findViewById(R.id.imgEscanearVale2);
        tvMontoACargar = (TextView) findViewById(R.id.tvMontoACargar);
        tvMontoACargarPendiente = (TextView) findViewById(R.id.tvMontoACargarPendiente);
        tvFolioMonto = (EditText) findViewById(R.id.tvFolioMonto);
        tvDenominacionMonto = (EditText) findViewById(R.id.tvDenominacionMonto);
        data = new SQLiteBD(getApplicationContext());
        ipEstacion = data.getIpEstacion();

        IDMontos = new ArrayList<String>();
        NombreTipoValePapel = new ArrayList<String>();
        MontoVale = new ArrayList<String>();
        FolioValePapel = new ArrayList<String>();
        TipoValesPapel = new ArrayList<String>();
    }

    private void ObtieneTiposVale(){
//        bar = new ProgressDialog(VentaProductos.this);
//        bar.setTitle("Cargando Productos");
//        bar.setMessage("Ejecutando... ");
//        bar.setIcon(R.drawable.gas);
//        bar.setCancelable(false);
//        bar.show();

        url = "http://" + ipEstacion + "/CorpogasService/api/TipoValePapeles";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray respuesta = new JSONArray(response);
                    int suma=0;
                    for (int i =0; i< respuesta.length(); i++){
                        suma = suma +1;
                    }
                    opcionesTipoVale = new String[suma];
                    for (int i =0; i< respuesta.length(); i++){
                        JSONObject tipoValePapel = respuesta.getJSONObject(i);
                        String IdentificadorVale = tipoValePapel.getString("Id");
                        String DescripcionVale = tipoValePapel.getString("Description");
                        String EliminadoVale = tipoValePapel.getString("IsDeleted");

                        if(EliminadoVale.equals("false")){
                            opcionesTipoVale[i] = DescripcionVale;
                            TipoValesPapel.add(IdentificadorVale);
                        }
                    }
                    tvMontoACargar.setText(montoaCobrar.toString());
                    tvMontoACargarPendiente.setText(montoaCobrar.toString());
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(ValesPapel.this, R.layout.support_simple_spinner_dropdown_item, opcionesTipoVale);
                    spnValesPapel.setAdapter(adapter);
                    spnValesPapel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            ValeSeleccionado = adapterView.getItemAtPosition(i).toString();
                            TipoValesPapelId = Integer.parseInt(TipoValesPapel.get(i));
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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


}