package com.corpogas.corpoapp.VentaCombustible;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.corpogas.corpoapp.Conexion;
import com.corpogas.corpoapp.Configuracion.SQLiteBD;
import com.corpogas.corpoapp.LecturaTarjetas.MonederosElectronicos;
import com.corpogas.corpoapp.Menu_Principal;
import com.corpogas.corpoapp.Modales.Modales;
import com.corpogas.corpoapp.R;
import com.corpogas.corpoapp.VentaPagoTarjeta;

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

public class DiferentesFormasPago extends AppCompatActivity {
    SQLiteBD db;
    String EstacionId, sucursalId, ipEstacion, saldoPuntada, tarjetaNumero, pagoConPuntada;
    ListView list;
    TextView txtMontoTotal, txtMontoFaltante, tvSaldo, tvSaldoPuntada;
    ImageView botonEnviar;
    JSONObject FormasPagoObjecto;
    JSONArray FormasPagoArreglo, ArregloFormaPagoVale;
    Bundle args = new Bundle();
    String CadenaFinal, numeroempleado, numeroTarjeta, nipCliente;
    String BanderaHuella, enviadoDesde, Usuarioid, PosicionCarga, Clavedespachador, OperativaId, NombreCompletoVenta;
    Integer TipoTransacionImprimir;
    String arregloVales;
    Double MontoenCanasta, montoVales, descuento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diferentes_formas_pago);
        db = new SQLiteBD(getApplicationContext());
        this.setTitle(db.getNombreEstacion() + " ( EST.:" + db.getNumeroEstacion() + ")");
        EstacionId = db.getIdEstacion();
        sucursalId = db.getIdSucursal();
        ipEstacion= db.getIpEstacion();
        db.getIdTarjtero();
        db.getCorrectoIncorrecto();

        Usuarioid = db.getUsuarioId();
        Clavedespachador = db.getClave();
        NombreCompletoVenta = getIntent().getStringExtra("NombreCompleto");
        numeroempleado = db.getNumeroEmpleado();
        enviadoDesde = getIntent().getStringExtra("Enviadodesde");
        OperativaId = getIntent().getStringExtra("idoperativa");
        saldoPuntada = getIntent().getStringExtra("saldoPuntada");
        tarjetaNumero = getIntent().getStringExtra("tarjetaNumero");
        pagoConPuntada = getIntent().getStringExtra("pagoconpuntada");

        if (db.getCorrectoIncorrecto().equals("2")){
            MontoenCanasta = Double.parseDouble(db.getmontototaldfp());
            PosicionCarga = db.getposcioncarga();
            numeroTarjeta = getIntent().getStringExtra("numeroTarjeta");
            descuento = getIntent().getDoubleExtra("descuento",0);
            nipCliente = getIntent().getStringExtra("nipCliente");

        }else{
            MontoenCanasta = getIntent().getDoubleExtra("montoencanasta", 0.00);
            PosicionCarga = getIntent().getStringExtra("posicioncarga");
            numeroTarjeta = getIntent().getStringExtra("numeroTarjeta");
            descuento = getIntent().getDoubleExtra("descuento",0);
            nipCliente = getIntent().getStringExtra("nipCliente");
        }

        txtMontoTotal= (TextView) findViewById(R.id.txtMontoTotal);
        txtMontoFaltante= (TextView)findViewById(R.id.txtmontofaltante);
        tvSaldoPuntada= (TextView)findViewById(R.id.tvsaldopuntada);
        tvSaldo=(TextView) findViewById(R.id.tvsaldo2);

//        if (enviadoDesde.equals("valespapel")){
//            montoVales = getIntent().getDoubleExtra("montoencanastavales", 0.00);
//            arregloVales= getIntent().getStringExtra("arregloVales");
//            tvSaldo.setText(montoVales.toString());
//            try {
//                FormasPagoArreglo = new JSONArray(arregloVales);
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }

        obtenerformasdepago();
        DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
        simbolos.setDecimalSeparator('.');
        DecimalFormat df = new DecimalFormat("####.00##",simbolos);
        df.setMaximumFractionDigits(2);
//        MontoenCanasta = MontoenCanasta;
//        tvSaldo.setText(df.format(Double.parseDouble(saldoPuntada)));


//        if (saldoPuntada.equals("0")){
//            tvSaldoPuntada.setVisibility(View.INVISIBLE);
//            tvSaldo.setVisibility(View.INVISIBLE);
//
//        }else{
//            tvSaldoPuntada.setVisibility(View.VISIBLE);
//            tvSaldo.setVisibility(View.VISIBLE);
//        }
        txtMontoTotal.setText(MontoenCanasta.toString());
        txtMontoFaltante.setText(MontoenCanasta.toString());
        botonEnviar = findViewById(R.id.imprimir);
        botonEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Double montoPendiente;
                montoPendiente = Double.parseDouble(txtMontoFaltante.getText().toString());
                if (montoPendiente>0){
                    botonEnviar.setClickable(true);
                    try {
                        String titulo = "AVISO";
                        String mensaje = "Se debe cubrir el 100% del monto total";
                        Modales modales = new Modales(DiferentesFormasPago.this);
                        View view1 = modales.MostrarDialogoAlertaAceptar(DiferentesFormasPago.this,mensaje,titulo);
                        view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                modales.alertDialog.dismiss();
                            }
                        });

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }else{
                    botonEnviar.setClickable(false);

//                    String titulo = "PUNTADA";
//                    String mensajes = "Desea Acumular la venta a su Tarjeta Puntada?";
//                    Modales modalesEfectivo = new Modales(DiferentesFormasPago.this);
//                    View viewLecturas = modalesEfectivo.MostrarDialogoAlerta(DiferentesFormasPago.this, mensajes,  "SI", "NO");
//                    viewLecturas.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            String banderaHuella = getIntent().getStringExtra( "banderaHuella");
//                            String nombreCompletoVenta = getIntent().getStringExtra("nombrecompleto");
//                            //LeeTarjeta();
//                            Intent intent = new Intent(getApplicationContext(), MonederosElectronicos.class);
//                            //intent.putExtra("device_name", m_deviceName);
//                            intent.putExtra("banderaHuella", banderaHuella);
//                            intent.putExtra("Enviadodesde", "formaspago");
//                            intent.putExtra("numeroEmpleado", numeroempleado);
//                            intent.putExtra("idoperativa", "1");
//                            intent.putExtra("formapagoid", numpago1);
//                            intent.putExtra("NombrePago", nombrepago);
//                            intent.putExtra("NombreCompleto", nombreCompletoVenta);
//                            intent.putExtra("montoenlacanasta", MontoCanasta);
//                            intent.putExtra("posicioncargaid", posiciondecargaid);
//                            intent.putExtra("tipoTarjeta", "Puntada");
//                            intent.putExtra("pagoconpuntada", "no");
//
//                            startActivity(intent);
//                            modalesEfectivo.alertDialog.dismiss();
//                        }
//
//                    });
//
//                    viewLecturas.findViewById(R.id.buttonNo).setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
////                                    RespuestaImprimeFinaliza(posicioncarga, idusuario, formapagoid, numticket, nombrepago);
//                            modalesEfectivo.alertDialog.dismiss();
////                                    SeleccionaPesosDoalares();
                            EnviaArregloDiferentesFormasPagos();                        }
//                    });
//                }
            }
        });

    }

    private void EnviaArregloDiferentesFormasPagos() {
        if (!Conexion.compruebaConexion(this)) {
            Toast.makeText(getBaseContext(), "Sin conexión a la red ", Toast.LENGTH_SHORT).show();
            Intent intent1 = new Intent(getApplicationContext(), Menu_Principal.class);
            startActivity(intent1);
            finish();
        } else {
            JSONObject datos = new JSONObject();
            String url = "http://" + ipEstacion + "/CorpogasService/api/tickets/generar";
            RequestQueue queue = Volley.newRequestQueue(this);
            try {

                datos.put("PosicionCargaId", PosicionCarga);
                datos.put("IdUsuario", numeroempleado);
                datos.put("SucursalId", sucursalId);
                datos.put("IdFormasPago", FormasPagoArreglo);
                datos.put("SucursalId", sucursalId);
                datos.put("ConfiguracionAplicacionId", db.getIdTarjtero());
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
                                Modales modales = new Modales(DiferentesFormasPago.this);
                                View view1 = modales.MostrarDialogoError(DiferentesFormasPago.this, mensaje);
                                view1.findViewById(R.id.buttonAction).setOnClickListener(new View.OnClickListener() { //buttonYes
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
                            Modales modales = new Modales(DiferentesFormasPago.this);
                            View view1 = modales.MostrarDialogoCorrecto(DiferentesFormasPago.this, "Ticket Impreso en Impresora Central");
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
                    Toast.makeText(DiferentesFormasPago.this, "error", Toast.LENGTH_SHORT).show();

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
    }


    //funcion para obtener formas de pago
    public void obtenerformasdepago() {
//        if (!Conexion.compruebaConexion(this)) {
//            Toast.makeText(getBaseContext(), "Sin conexión a la red ", Toast.LENGTH_SHORT).show();
//            Intent intent1 = new Intent(getApplicationContext(), Menu_Principal.class);
//            startActivity(intent1);
//            finish();
//        } else {
        String url = "http://" + ipEstacion + "/CorpogasService/api/sucursalformapagos/sucursal/" + sucursalId;
        StringRequest eventoReq = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        formadepago(response);
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
//        }
    }

    private void formadepago(String response) {

        //Declaramos la lista de titulo
        final List<String> maintitle;
        //lo assignamos a un nuevo ArrayList
        maintitle = new ArrayList<String>();

        //Creamos la lista para los subtitulos
        List<String> subtitle;
        //Lo asignamos a un nuevo ArrayList
        subtitle = new ArrayList<String>();

        //CReamos una nueva list de tipo Integer con la cual cargaremos a una imagen
        List<Integer> imgid;
        //La asignamos a un nuevo elemento de ArrayList
        imgid = new ArrayList<>();

        final List<String> numerotickets;
        //Lo asignamos a un nuevo ArrayList
        numerotickets = new ArrayList<String>();

        final List<String> IdFormaPago;
        //Lo asignamos a un nuevo ArrayList
        IdFormaPago = new ArrayList<String>();

        final List<String> NumeroInternoFormaPago;
        //Lo asignamos a un nuevo ArrayList
        NumeroInternoFormaPago = new ArrayList<String>();


        try {
            //JSONObject jsonObject = new JSONObject(response);
            //String formapago = jsonObject.getString("SucursalFormapagos");
            Boolean colocarformapago;
            JSONArray nodo = new JSONArray(response);
            String numero_pago;
            String internonumero="";
            for (int i = 0; i <= nodo.length() ; i++) {
                colocarformapago = false;
                JSONObject nodo1 = nodo.getJSONObject(i);
                String formap = nodo1.getString("PaymentMethod");
                JSONObject  numerointernoobject = new JSONObject(formap);
//                    String formapago1 = nodo1.getString("FormaPago");
//                    JSONObject nodo2 = new JSONObject(formapago1);
                internonumero = numerointernoobject.getString("Id"); //FormaPagoId
                numero_pago = numerointernoobject.getString("Id");
                String nombre_pago = numerointernoobject.getString("LongDescription");

                String numero_ticket = numerointernoobject.getString("PrintsAllowed");
                String visible = numerointernoobject.getString("IsFrontVisible");
                String facturable = numerointernoobject.getString("IsBillable");

//                if (facturable == "true") {
                if (visible == "true") {
                    if (numero_pago.equals("2") || numero_pago.equals("6") || numero_pago.equals("14") || numero_pago.equals("16") || numero_pago.equals("17")) {//2=VALES, 6=VALE ELECTRONICO, 14=MERCADO PAGO,  16 = PAGO DOLARES, 17= VALES PROPIOS
                    }else{
//                        if (enviadoDesde.equals("valespapel") && numero_pago.equals("2")){
//                        }else{
                            numerotickets.add(numero_ticket);
                            maintitle.add(nombre_pago);
                            if (db.getCorrectoIncorrecto().equals("2")){
                                subtitle.add("$"+ db.getMontoFPD(Integer.parseInt(numero_pago)) );
                            }else{
                                subtitle.add("$0.00" );
                            }
                            IdFormaPago.add(numero_pago);
                            colocarformapago = true;
//                        }
                    }
                }
//                }else{
//                    if (numero_pago.equals("0")){
////                        if (pagoConPuntada.equals("si")){
////                            if (saldoPuntada.equals("0")){
////                            }else{
////                                numerotickets.add("1");
////                                maintitle.add("Puntada Redención");
////                                subtitle.add("$0.00" );
////                                IdFormaPago.add("12"); //numero_pago
////                                colocarformapago = true;
////                            }
////                        }
//                    }
//                }

                if (colocarformapago.equals(true)){
                    int idpago = Integer.parseInt(internonumero); //numero_pago
                    switch (idpago) {
                        case 0:
                            imgid.add(R.drawable.redimirpuntada);
                            break;
                        case 1://2:
                            imgid.add(R.drawable.billete);
                            break;
                        case 2://3
                            imgid.add(R.drawable.vale);
                            break;
                        case 3://4
                            imgid.add(R.drawable.american);
                            break;
                        case 13://5
                            imgid.add(R.drawable.gascard);
                            break;
                        case 5://6
                            imgid.add(R.drawable.visa);
                            break;
                        case 6://7
                            imgid.add(R.drawable.valeelectronico);
                            break;
                        case 7://8
                            imgid.add(R.drawable.gas);
                            break;
                        case 10://9
                            imgid.add(R.drawable.gas);
                            break;
                        case 90:
                            imgid.add(R.drawable.gas);//Consigna
                            break;
                        case 91:
                            imgid.add(R.drawable.jarreo);
                            break;
                        case 92:
                            imgid.add(R.drawable.jarreo); //Autojarreo
                            break;
                        case 16://USD
                            imgid.add(R.drawable.usd);
                            break;
                        //case 10:
                        //    imgid.add(R.drawable.monedero);
                        //    break;
                        default:
                            imgid.add(R.drawable.gas);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Invocamos a la clase de listadapter para crear la vista sobre el layout
        Adaptador adapter = new Adaptador(this, maintitle, subtitle, imgid);
        list = (ListView) findViewById(R.id.lstPosicionCarga);
        list.setAdapter(adapter);
        //Optenemos el numero del Item seleccionado que corresponde a al numero de posicion de carga
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String total;
                String formaPagoSeleccionada;

                formaPagoSeleccionada = IdFormaPago.get(position);
                // TODO Auto-generated method stub
                if (db.getEstatusCobradoFPD(Integer.parseInt(formaPagoSeleccionada) )== 0){
                    try {
                        String titulo = "Parcialidades";
                        String mensaje = "Ingresa el monto" ;
                        Modales modales = new Modales(DiferentesFormasPago.this);
                        View viewLectura = modales.MostrarDialogoInsertaDato(DiferentesFormasPago.this, mensaje, titulo);
                        EditText edtProductoCantidad = ((EditText) viewLectura.findViewById(R.id.textInsertarDato));
                        edtProductoCantidad.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                        viewLectura.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Boolean banderaSigue = true;
                                String cantidad = edtProductoCantidad.getText().toString();
                                if (cantidad.isEmpty()){
                                    edtProductoCantidad.setError("Ingresa el monto");
                                }else {
                                    String montoSeleccionado;
                                    String montosinpesos;
                                    montosinpesos  = subtitle.get(position);
                                    montoSeleccionado =montosinpesos.replace("$", "");
    //                                if (position == 0 && pagoConPuntada.equals("si"))  {
    //                                    if (Double.parseDouble(cantidad) > Double.parseDouble(saldoPuntada) ){
    //                                        banderaSigue = false;
    //                                        String titulo = "AVISO";
    //                                        String mensaje = "La cantidad no puede ser mayor que el Saldo de la Tarjeta Puntada";
    //                                        Modales modales = new Modales(DiferentesFormasPago.this);
    //                                        View view1 = modales.MostrarDialogoAlertaAceptar(DiferentesFormasPago.this,mensaje,titulo);
    //                                        view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
    //                                            @Override
    //                                            public void onClick(View view) {
    //                                                modales.alertDialog.dismiss();
    //                                            }
    //                                        });
    //                                    }else{
    //                                        if (Double.parseDouble(cantidad) > Double.parseDouble(txtMontoFaltante.getText().toString())){
    //                                            try {
    //                                                banderaSigue = false;
    //                                                String titulo = "AVISO";
    //                                                String mensaje = "La cantidad capturada no puede ser mayor que la cantidad Pendiente";
    //                                                Modales modales = new Modales(DiferentesFormasPago.this);
    //                                                View view1 = modales.MostrarDialogoAlertaAceptar(DiferentesFormasPago.this,mensaje,titulo);
    //                                                view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
    //                                                    @Override
    //                                                    public void onClick(View view) {
    //                                                        modales.alertDialog.dismiss();
    //                                                    }
    //                                                });
    //
    //                                            }catch (Exception e){
    //                                                e.printStackTrace();
    //                                            }
    //                                        }
    //                                    }
    //                                }else{
                                    if (Double.parseDouble(cantidad) > Double.parseDouble(txtMontoFaltante.getText().toString())){
                                        if (montoSeleccionado.equals("0.00")){
                                            try {
                                                banderaSigue = false;
                                                String titulo = "AVISO";
                                                String mensaje = "La cantidad capturada no puede ser mayor que la cantidad Pendiente";
                                                Modales modales = new Modales(DiferentesFormasPago.this);
                                                View view1 = modales.MostrarDialogoAlertaAceptar(DiferentesFormasPago.this,mensaje,titulo);
                                                view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        modales.alertDialog.dismiss();
                                                    }
                                                });

                                            }catch (Exception e){
                                                e.printStackTrace();
                                            }
                                        }else{
                                            //                                        if (Double.parseDouble(cantidad) > Double.parseDouble(montoSeleccionado) ){
                                            //                                            try {
                                            //                                                banderaSigue = false;
                                            //                                                String titulo = "AVISO";
                                            //                                                String mensaje = "La cantidad no puede ser mayor que la que se había cargado";
                                            //                                                Modales modales = new Modales(DiferentesFormasPago.this);
                                            //                                                View view1 = modales.MostrarDialogoAlertaAceptar(DiferentesFormasPago.this,mensaje,titulo);
                                            //                                                view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                                            //                                                    @Override
                                            //                                                    public void onClick(View view) {
                                            //                                                        modales.alertDialog.dismiss();
                                            //                                                    }
                                            //                                                });
                                            //                                                banderaSigue = false;
                                            //                                            }catch (Exception e){
                                            //                                                e.printStackTrace();
                                            //                                            }
                                            //                                        }
                                        }
                                    }else{
                                        if (montoSeleccionado.equals("0.00")){
                                            if (Double.parseDouble(txtMontoFaltante.getText().toString())==0.00){
                                                try {
                                                    banderaSigue = false;
                                                    String titulo = "AVISO";
                                                    String mensaje = "Ya se completó el monto Total, con las diferentes formas de pago";
                                                    Modales modales = new Modales(DiferentesFormasPago.this);
                                                    View view1 = modales.MostrarDialogoAlertaAceptar(DiferentesFormasPago.this,mensaje,titulo);
                                                    view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View view) {
                                                            modales.alertDialog.dismiss();
                                                        }
                                                    });
                                                    banderaSigue = false;
                                                }catch (Exception e){
                                                    e.printStackTrace();
                                                }
                                            }
                                        }else{
                                            if (Double.parseDouble(cantidad) > Double.parseDouble(montoSeleccionado) ){
                                                try {
                                                    banderaSigue = false;
                                                    String titulo = "AVISO";
                                                    String mensaje = "La cantidad no puede ser mayor que la que se había cargado";
                                                    Modales modales = new Modales(DiferentesFormasPago.this);
                                                    View view1 = modales.MostrarDialogoAlertaAceptar(DiferentesFormasPago.this,mensaje,titulo);
                                                    view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View view) {
                                                            modales.alertDialog.dismiss();
                                                        }
                                                    });
                                                    banderaSigue = false;
                                                }catch (Exception e){
                                                    e.printStackTrace();
                                                }
                                            }
                                        }
    //                                    }
                                    }

                                    if (banderaSigue.equals(true)){
    //                                    tarjetaNumero = getIntent().getStringExtra("tarjetaNumero");

                                        DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
                                        simbolos.setDecimalSeparator('.');
                                        DecimalFormat df = new DecimalFormat("###0.00##",simbolos);
    //                                    String cantidadformato = cantidad;
    //                                    cantidadformato = df.format(Double.parseDouble(cantidadformato));
    //                                    cantidad = cantidadformato;
                                        cantidad= df.format(Double.parseDouble(cantidad));
                                        cantidad = "$"+cantidad;
                                        subtitle.set(position, cantidad);
                                        Adaptador adapter = new Adaptador(DiferentesFormasPago.this, maintitle, subtitle, imgid);
                                        list = (ListView) findViewById(R.id.lstPosicionCarga);
                                        list.setAdapter(adapter);
                                        Double cantidadObtenida;
                                        cantidadObtenida = 0.00;
                                        CadenaFinal="";
    //                                    if (enviadoDesde.equals("valespapel")){
    //                                        try {
    //                                            FormasPagoArreglo = new JSONArray(arregloVales);
    //                                        } catch (JSONException e) {
    //                                            e.printStackTrace();
    //                                        }
    //                                    }else{
                                            FormasPagoArreglo = new JSONArray();
    //                                    }
                                        JSONObject TramaBancariaDetalle = new JSONObject();
                                        try {
                                            TramaBancariaDetalle.put("SucursalId", 497);
                                            TramaBancariaDetalle.put("EstacionId", 251);
                                            TramaBancariaDetalle.put(        "RESPONSE_CODe", "00");
                                            TramaBancariaDetalle.put(        "POS_ID", "POSANDROID");
                                            TramaBancariaDetalle.put(        "Tag_9B", "E800");
                                            TramaBancariaDetalle.put(       "Tag_9F26", "A76488169348EBAB");
                                            TramaBancariaDetalle.put(        "CARD_TYPE", 1);
                                            TramaBancariaDetalle.put(        "MNEMO_NAME", "Mc");
                                            TramaBancariaDetalle.put(        "TERMINAL_ID", "POSANDROID");
                                            TramaBancariaDetalle.put(        "APP_LABEL","Debit MasterCard");
                                            TramaBancariaDetalle.put(        "TAG_50", "Debit MasterCard");
                                            TramaBancariaDetalle.put(        "Tag_95", "0000008000");
                                            TramaBancariaDetalle.put(        "CARD_HOLDER_NAME","ERICK/AGUILA MARTINEZ ");
                                            TramaBancariaDetalle.put(        "HEADER_1", "SmartPaymentServices");
                                            TramaBancariaDetalle.put(        "AMOUNT", "6666.89");
                                            TramaBancariaDetalle.put(        "PREFERRED_NAME", "Debit MasterCard");
                                            TramaBancariaDetalle.put(        "ACCOUNT_NUMBEr", "1234 5986 1250 4521");
                                            TramaBancariaDetalle.put(        "AiD", "A0000000041010");
                                            TramaBancariaDetalle.put(        "SIGNATURE_FLAG", 0);
                                            TramaBancariaDetalle.put(        "ArQc","A76488169348EBAB");
                                            TramaBancariaDetalle.put(        "Footer_1", "Pagaré negociable únicamente");
                                            TramaBancariaDetalle.put(        "TXN_NAME", "VENTA EN LINEA");
                                            TramaBancariaDetalle.put(        "TXN_TIME","162439");
                                            TramaBancariaDetalle.put(        "FOOTER_3", "nada");
                                            TramaBancariaDetalle.put(        "FOOTER_2", "con instituciones de crédito");
                                            TramaBancariaDetalle.put(        "TAG_9F12", "Debit MasterCard");
                                            TramaBancariaDetalle.put(        "TXN_APPROVAL_CODE", "420557");
                                            TramaBancariaDetalle.put(        "Tag_9F34", "440302");
                                            TramaBancariaDetalle.put(        "CARD_NAME", "Mastercard");
                                            TramaBancariaDetalle.put(        "Header_2", "Av de los Insurgentes Sur 2453");
                                            TramaBancariaDetalle.put(        "HEADER_3", "Tizapán San Ángel Tel:55509935");
                                            TramaBancariaDetalle.put(        "HEADER_4", "Álvaro Obregón, 01090 CDMX");
                                            TramaBancariaDetalle.put(        "TSN","162544");
                                            TramaBancariaDetalle.put(        "EXPIRATION_DATE", "**");
                                            TramaBancariaDetalle.put(        "ENTRY_MODE","05");
                                            TramaBancariaDetalle.put(        "BRAND_NAME:", "l");
                                            TramaBancariaDetalle.put(        "Tag_5F2A","0484");
                                            TramaBancariaDetalle.put(        "TXN_DATE", "210909");
                                            TramaBancariaDetalle.put(        "SG_REFERENCE", "000036578535");
                                            TramaBancariaDetalle.put(        "TiP",0.8);
                                            TramaBancariaDetalle.put(        "FOOTER_4", "nada2");
                                            TramaBancariaDetalle.put(        "TOTAL_AMOUNT", "6666.97");
                                            TramaBancariaDetalle.put(        "BANK_NAME", " ALQUIMIADIGITAL.MX");
                                            TramaBancariaDetalle.put("MERCHANT_ID", "7550587");

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                        String  FormaPagoId;
                                        String cadenaArreglo="";
                                        for (int g= 0; g< list.getCount(); g++)
                                        {
                                            FormasPagoObjecto = new JSONObject();
                                            String valorObtenido;
                                            valorObtenido = subtitle.get(g);
                                            valorObtenido = valorObtenido.replace("$", "");
                                            cantidadObtenida = cantidadObtenida + Double.parseDouble(valorObtenido);
                                            if (Double.parseDouble(valorObtenido)==0.00){
                                            }else{
                                                try {
                                                    FormasPagoObjecto.put("Id", IdFormaPago.get(g));
                                                    FormasPagoObjecto.put("Importe", Double.parseDouble(valorObtenido));
                                                    if (IdFormaPago.get(g).equals("12")){ //Solo para Puntada Redimir
                                                        FormasPagoObjecto.put("NumeroTarjeta", tarjetaNumero);
                                                    }
    //                                                if (IdFormaPago.get(g).equals("3")  ){
    //                                                    FormasPagoObjecto.put("TramaBancariaDetalle", TramaBancariaDetalle);
    //                                                }
    //                                                if (IdFormaPago.get(g).equals("5") ){
    //                                                    FormasPagoObjecto.put("TramaBancariaDetalle", TramaBancariaDetalle);
    //                                                }
    //                                                if (IdFormaPago.get(g).equals("13") ){
                                                   if (IdFormaPago.get(g).equals("3") || IdFormaPago.get(g).equals("5") || IdFormaPago.get(g).equals("13")){
                                                        FormasPagoObjecto.put("TramaBancariaDetalle", TramaBancariaDetalle);
                                                    }

                                                    FormasPagoArreglo.put(FormasPagoObjecto);
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }

                                        Double MontoTotal = Double.parseDouble(txtMontoTotal.getText().toString());
                                        Double TotalFaltante = MontoTotal - cantidadObtenida;
                                        txtMontoFaltante.setText(df.format(TotalFaltante).toString());

                                        //AQUI VA ENVIAR A PAGOBANCARIO
                                        if (formaPagoSeleccionada.equals("3") || formaPagoSeleccionada.equals("5") || formaPagoSeleccionada.equals("13")){
                                            db.getWritableDatabase().delete("PagoTarjeta", null, null);
                                            db.close();
                                            db.InsertarDatosPagoTarjeta("1", PosicionCarga, formaPagoSeleccionada, cantidad.replace("$",""), "0", "2", "0", numeroTarjeta, Double.toString(descuento), nipCliente, Double.toString(MontoTotal));

                                            db.InsertarDatosPagoTarjetaDFP(formaPagoSeleccionada, Double.toString(MontoTotal), formaPagoSeleccionada, "", cantidad, "0");
                                            Intent intentVisa = new Intent(getApplicationContext(), VentaPagoTarjeta.class);//DiferentesFormasPagoPuntada
                                            intentVisa.putExtra("lugarProviene", "diferentesformaspago");
                                            intentVisa.putExtra("posicioncarga", PosicionCarga);
                                            intentVisa.putExtra("formapagoid", formaPagoSeleccionada);
                                            intentVisa.putExtra("montoencanasta", cantidad);
                                            intentVisa.putExtra("numeroTarjeta", "");
                                            startActivity(intentVisa);
                                        }
                                    }
                                    modales.alertDialog.dismiss();
                                }
                            }
                        });
                        viewLectura.findViewById(R.id.buttonNo).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                modales.alertDialog.dismiss();
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else{
                    String titulo = "AVISO";
                    String mensaje = "Ya se ha realizado el pago con la forma de pago seleccionada";
                    Modales modales = new Modales(DiferentesFormasPago.this);
                    View view1 = modales.MostrarDialogoAlertaAceptar(DiferentesFormasPago.this,mensaje,titulo);
                    view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            modales.alertDialog.dismiss();
                        }
                    });

                }

            }
        });
    }
}