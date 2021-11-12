package com.corpogas.corpoapp.VentaCombustible;

import static androidx.recyclerview.widget.ItemTouchHelper.DOWN;
import static androidx.recyclerview.widget.ItemTouchHelper.UP;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
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
import com.corpogas.corpoapp.Adapters.RVAdapter;
import com.corpogas.corpoapp.Conexion;
import com.corpogas.corpoapp.Configuracion.SQLiteBD;
import com.corpogas.corpoapp.Entities.Classes.RecyclerViewHeaders;
import com.corpogas.corpoapp.LecturaTarjetas.MonederosElectronicos;
import com.corpogas.corpoapp.Menu_Principal;
import com.corpogas.corpoapp.Modales.Modales;
import com.corpogas.corpoapp.Puntada.PuntadaQr;
import com.corpogas.corpoapp.R;
import com.corpogas.corpoapp.ValesPapel.ValesPapel;
import com.corpogas.corpoapp.VentaPagoTarjeta;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FormasPagoReordenado extends AppCompatActivity {
    RecyclerView rcvFormasPagoReordenado;

    SQLiteBD data;

    String formapago, nombrepago, numticket; //numticket, numeroTicket = "";
    Bundle args = new Bundle();
    String EstacionId, sucursalId, ipEstacion, EstacionJarreo;
    Double MontoCanasta;

    JSONObject FormasPagoObjecto;
    JSONArray FormasPagoArreglo;
    Integer TipoTransacionImprimir;

    String fechaTicket;
    String posiciondecargaid, sucursalnumeroempleado, claveProducto, numeroTarjeta, nipCliente;

    Double totalPesos, totalDolares, tipoCambio, descuento;
    String puntadaId, provieneDe = "1";
    String numpago1,  idoperativa;
    List<RecyclerViewHeaders> lrcvFormaPago;

    //Declaramos la lista de titulo
    List<String> maintitle;
    //Creamos la lista para los subtitulos
    List<String> subtitle;
    //CReamos una nueva list de tipo Integer con la cual cargaremos a una imagen
    List<Integer> imgid;
    List<String> numerotickets;
    List<String> IdFormaPago;
    List<String> NumeroInternoFormaPago;
    List<String> AcumulaPuntosArreglo;
    String posicioncarga;
    String idusuario;
    String ClaveDespachador;
    String formaPagoIdentificador;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formas_pago_reordenado);

        init();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvFormasPagoReordenado.setLayoutManager(linearLayoutManager);
        rcvFormasPagoReordenado.setHasFixedSize(true);
        obtenerformasdepago();
    }

    private void init(){
        data = new SQLiteBD(getApplicationContext());
        this.setTitle(data.getNombreEstacion() + " ( EST.:" + data.getNumeroEstacion() + ")");
        SQLiteBD db = new SQLiteBD(getApplicationContext());
        EstacionId = data.getIdEstacion();
        sucursalId = data.getIdSucursal();
        ipEstacion = data.getIpEstacion();

        EstacionJarreo = getIntent().getStringExtra("estacionjarreo");
        MontoCanasta = getIntent().getDoubleExtra("montoenCanasta", 0);
        posiciondecargaid = getIntent().getStringExtra("posicionCarga");
        sucursalnumeroempleado = data.getNumeroEmpleado();//getIntent().getStringExtra("numeroEmpleado");

        numeroTarjeta = getIntent().getStringExtra("numeroTarjeta");
        descuento = getIntent().getDoubleExtra("descuento",0);
        nipCliente = getIntent().getStringExtra("nipCliente");

//        posicioncarga = getIntent().getStringExtra("posicioncarga");
        idusuario = data.getUsuarioId(); //getIntent().getStringExtra("IdUsuario");
        ClaveDespachador = getIntent().getStringExtra("clavedespachador");
        idoperativa = getIntent().getStringExtra("IdOperativa");

        rcvFormasPagoReordenado = (RecyclerView) findViewById(R.id.rcvFormasPagoReordenado);

    }

    //funcion para obtener formas de pago
    public void obtenerformasdepago() {
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
    }

    private void formadepago(String response) {
        lrcvFormaPago = new ArrayList<>();
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

        //Lo asignamos a un nuevo ArrayList
        numerotickets = new ArrayList<String>();

        //Lo asignamos a un nuevo ArrayList
        IdFormaPago = new ArrayList<String>();

        //Lo asignamos a un nuevo ArrayList
        NumeroInternoFormaPago = new ArrayList<String>();

        AcumulaPuntosArreglo = new ArrayList<String>();



        try {
            //JSONObject jsonObject = new JSONObject(response);
            //String formapago = jsonObject.getString("SucursalFormapagos");
            Boolean colocarformapago;
            JSONArray nodo = new JSONArray(response);
            String numero_pago;
            String internonumero="";
            String nombre_pago = "";
            for (int i = 0; i <= nodo.length() ; i++) {
                if (i == nodo.length()){
                    numerotickets.add("0");
                    maintitle.add("VARIAS FORMAS DE PAGO");
                    subtitle.add("Forma de Pago:" + 0);
                    IdFormaPago.add("0");
                    colocarformapago = true;
                    numero_pago = "0";
                    internonumero ="0";
                    NumeroInternoFormaPago.add("0");
                    lrcvFormaPago.add(new RecyclerViewHeaders("VARIAS FORMAS PAGO", "Forma de Pago:" + 0, R.drawable.variasformaspago));
                }else{
                    colocarformapago = false;
                    JSONObject nodo1 = nodo.getJSONObject(i);
                    String formap = nodo1.getString("PaymentMethod"); //FormaPago
                    JSONObject  numerointernoobject = new JSONObject(formap);
                    internonumero = numerointernoobject.getString("Id"); //NumeroInterno

                    numero_pago = numerointernoobject.getString("Id"); //FormaPagoId
                    //String formapago1 = nodo1.getString("PaymentMethod");
                    //JSONObject nodo2 = new JSONObject(formapago1);
                    nombre_pago = numerointernoobject.getString("LongDescription"); //DescripcionLarga
                    String numero_ticket = numerointernoobject.getString("PrintsAllowed");
                    String visible = numerointernoobject.getString("IsFrontVisible"); //VisibleTarjetero
                    String acumulaPuntos = numerointernoobject.getString("AccumulatePoints");
                    if (visible == "true") {
                        if (numero_pago.equals("14") | numero_pago.equals("0")){
                            //Nodesplega MErcadoPAgo
                        }else{
                            numerotickets.add(numero_ticket);
                            maintitle.add(nombre_pago);
                            subtitle.add("Forma de Pago:" + numero_pago);
                            IdFormaPago.add(numero_pago);
                            NumeroInternoFormaPago.add(internonumero);
                            colocarformapago = true;
                            AcumulaPuntosArreglo.add(acumulaPuntos);
                        }
                    }else{
                        if (EstacionJarreo.equals("true")){
                            if (numero_pago.equals("91")){ //JArreo
                                numerotickets.add(numero_ticket);
                                maintitle.add(nombre_pago);
                                subtitle.add("Forma de Pago:" + numero_pago); //numero_pago
                                IdFormaPago.add(numero_pago);
                                NumeroInternoFormaPago.add(internonumero);
                                colocarformapago = true;
                            }
                        }
                    }
                }
                if (colocarformapago.equals(true)){
                    int idpago = Integer.parseInt(numero_pago); //numero_pago
                    switch (idpago) {
                        case 0:
                            imgid.add(R.drawable.variasformaspago);
                            break;
                        case 1: //Efectivo
                            imgid.add(R.drawable.billete);
                            lrcvFormaPago.add(new RecyclerViewHeaders(nombre_pago, "Forma de Pago:" + numero_pago, R.drawable.billete));
                            break;
                        case 2: //Vales
                            imgid.add(R.drawable.vale);
                            lrcvFormaPago.add(new RecyclerViewHeaders(nombre_pago, "Forma de Pago:" + numero_pago, R.drawable.vale));
                            break;
                        case 3: //AMEX
                            imgid.add(R.drawable.american);
                            lrcvFormaPago.add(new RecyclerViewHeaders(nombre_pago, "Forma de Pago:" + numero_pago, R.drawable.american));
                            break;
                        case 5: //VISA/MC
                            imgid.add(R.drawable.visa);
                            lrcvFormaPago.add(new RecyclerViewHeaders(nombre_pago, "Forma de Pago:" + numero_pago, R.drawable.visa));
                            break;
                        case 6: // VALE/ELEC
                            imgid.add(R.drawable.valeelectronico);
                            lrcvFormaPago.add(new RecyclerViewHeaders(nombre_pago, "Forma de Pago:" + numero_pago, R.drawable.valeelectronico));
                            break;
                        case 7: //CREDITO ES
                            imgid.add(R.drawable.gas);
                            lrcvFormaPago.add(new RecyclerViewHeaders(nombre_pago, "Forma de Pago:" + numero_pago, R.drawable.gas));
                            break;
                        case 8: //CREDITO ES
                            imgid.add(R.drawable.gas);
                            lrcvFormaPago.add(new RecyclerViewHeaders(nombre_pago, "Forma de Pago:" + numero_pago, R.drawable.gas));
                            break;
                        case 9: //CREDITO ES
                            imgid.add(R.drawable.gas);
                            lrcvFormaPago.add(new RecyclerViewHeaders(nombre_pago, "Forma de Pago:" + numero_pago, R.drawable.gas));
                            break;
                        case 10: //CORPOMOBILE
                            imgid.add(R.drawable.gas);
                            lrcvFormaPago.add(new RecyclerViewHeaders(nombre_pago, "Forma de Pago:" + numero_pago, R.drawable.gas));
                            break;
                        case 11: //TANQUELLENO
                            imgid.add(R.drawable.tanquelleno);
                            lrcvFormaPago.add(new RecyclerViewHeaders(nombre_pago, "Forma de Pago:" + numero_pago, R.drawable.tanquelleno));
                            break;
                        case 12: //REDENCION PUNTADA
                            imgid.add(R.drawable.tanquelleno);
                            break;
                        case 13: //GASCARD
                            imgid.add(R.drawable.gascard);
                            lrcvFormaPago.add(new RecyclerViewHeaders(nombre_pago, "Forma de Pago:" + numero_pago, R.drawable.gascard));
                            break;
                        case 14: //MERCADOPAGO
                            imgid.add(R.drawable.mercadopago);
                            lrcvFormaPago.add(new RecyclerViewHeaders(nombre_pago, "Forma de Pago:" + numero_pago, R.drawable.mercadopago));
                            break;
                        case 90://Consigna
                            imgid.add(R.drawable.gas);
                            lrcvFormaPago.add(new RecyclerViewHeaders(nombre_pago, "Forma de Pago:" + numero_pago, R.drawable.gas));
                            break;
                        case 91: //JARREO
                            imgid.add(R.drawable.jarreo);
                            lrcvFormaPago.add(new RecyclerViewHeaders(nombre_pago, "Forma de Pago:" + numero_pago, R.drawable.jarreo));
                            break;
                        case 92://AUTOJARREO
                            imgid.add(R.drawable.jarreo);
                            lrcvFormaPago.add(new RecyclerViewHeaders(nombre_pago, "Forma de Pago:" + numero_pago, R.drawable.jarreo));
                            break;
                        case 16://USD
                            imgid.add(R.drawable.usd);
                            lrcvFormaPago.add(new RecyclerViewHeaders(nombre_pago, "Forma de Pago:" + numero_pago, R.drawable.usd));
                            break;

                        //case 10:
                        //    imgid.add(R.drawable.monedero);
                        //    break;
                        default:
                            imgid.add(R.drawable.gas);
                            lrcvFormaPago.add(new RecyclerViewHeaders(nombre_pago, "Forma de Pago:" + numero_pago, R.drawable.gas));
                    }
                }
                initializeAdapter();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initializeAdapter(){
        RVAdapter adapter = new RVAdapter(lrcvFormaPago);
        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String titulo;
//                String acumularPuntada= "false";
////                int pos = position + 1;
//                formapago = NumeroInternoFormaPago.get(v);
//                if (Integer.parseInt(formapago)>0){
//                    acumularPuntada = AcumulaPuntosArreglo.get(v);
//                }
//                String formapagoid = IdFormaPago.get(v);//String.valueOf(pos);
//                nombrepago = maintitle.get(v);
//                String numticket = numerotickets.get(v);
//                int numpago = Integer.parseInt(NumeroInternoFormaPago.get(v)); //IdFormaPago
//                numpago1 = IdFormaPago.get(v);
//                idoperativa = getIntent().getStringExtra("IdOperativa");


                formaPagoIdentificador = lrcvFormaPago.get(rcvFormasPagoReordenado.getChildAdapterPosition(v)).getSubtitulo();
                formaPagoIdentificador =formaPagoIdentificador.substring(formaPagoIdentificador.indexOf(":") + 1);

                switch (formaPagoIdentificador){
                    case "0": //Diferentes Formas de Pago
                        data.getWritableDatabase().delete("PagoTarjeta", null, null);
                        data.close();
                        data.InsertarDatosPagoTarjeta("1",posiciondecargaid, formaPagoIdentificador, Double.toString(MontoCanasta), "0", provieneDe, "0", numeroTarjeta, Double.toString(descuento), nipCliente, Double.toString(MontoCanasta));
                        Intent intentDiferente = new Intent(getApplicationContext(), DiferentesFormasPago.class);
                        intentDiferente.putExtra("Enviadodesde", "formaspago");
                        intentDiferente.putExtra("idusuario", idusuario);
                        intentDiferente.putExtra("posicioncarga", posiciondecargaid);
                        intentDiferente.putExtra("claveusuario", ClaveDespachador);
                        intentDiferente.putExtra("idoperativa", idoperativa);
                        intentDiferente.putExtra("formapagoid", formaPagoIdentificador);
                        intentDiferente.putExtra("montoencanasta", MontoCanasta);
                        intentDiferente.putExtra("numeroempleadosucursal", sucursalnumeroempleado);
                        intentDiferente.putExtra("numeroTarjeta", numeroTarjeta);
                        intentDiferente.putExtra("descuento", descuento);
                        intentDiferente.putExtra("nipCliente", nipCliente);
                        startActivity(intentDiferente);
                        finish();
                        break;

                    case "1":
                        if (numeroTarjeta.length() > 0){
//                                if (acumularPuntada.equals("true")) {
//                                    predeterminarPuntadaAcumular(Integer.parseInt(formapago));
//
//                                }else{
                            MuestraFormaEfectivo();
//                                }
                        }else{
                            if (idoperativa.equals("20")){
                                MuestraFormaEfectivo();
                            }else{
                                titulo = "PUNTADA";
                                String mensajes = "¿Desea Acumular la venta a su Tarjeta Puntada?";
                                Modales modalesEfectivo = new Modales(FormasPagoReordenado.this);
                                View viewLecturas = modalesEfectivo.MostrarDialogoAlerta(FormasPagoReordenado.this, mensajes,  "SI", "NO");
                                viewLecturas.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        MuestraFormaEfectivoPuntada();
                                    }
                                });

                                viewLecturas.findViewById(R.id.buttonNo).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        modalesEfectivo.alertDialog.dismiss();
                                        MuestraFormaEfectivo();
                                    }
                                });

                            }
                        }
                        break;
                    case "2":
//                        if (acumularPuntada.equals("true")) {
//                            predeterminarPuntadaAcumular(Integer.parseInt(formapago));
//                        }else{
                        Intent intentVale = new Intent(getApplicationContext(), ValesPapel.class);
                        intentVale.putExtra("Enviadodesde", "formaspago");
                        intentVale.putExtra("posicioncarga", posiciondecargaid);
                        intentVale.putExtra("idoperativa", idoperativa);
                        intentVale.putExtra("formapagoid", formaPagoIdentificador);
                        intentVale.putExtra("montoencanasta", MontoCanasta);
                        startActivity(intentVale);
                        finish();
//                        }

                        break;
                    case "3": //AMEX
                    case "5": //VISA
                    case "13"://GAS CARD AMEX
                        DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
                        simbolos.setDecimalSeparator('.');
                        DecimalFormat df = new DecimalFormat("####.00##",simbolos);

                        df.setMaximumFractionDigits(2);

                        String banderaHuella = getIntent().getStringExtra( "banderaHuella");
                        String nombreCompletoVenta = getIntent().getStringExtra("nombrecompleto");
                        if (numeroTarjeta.length() > 0){
//                            if (acumularPuntada.equals("true")) {
//                                predeterminarPuntadaAcumular(Integer.parseInt(formapago));
//                            }else{
                            data.getWritableDatabase().delete("PagoTarjeta", null, null);
                            data.close();
                            data.InsertarDatosPagoTarjeta("1",posiciondecargaid, formaPagoIdentificador, Double.toString(MontoCanasta), "0", provieneDe, "0", numeroTarjeta, Double.toString(descuento), nipCliente, Double.toString(MontoCanasta));

                            Intent intentVisa = new Intent(getApplicationContext(), VentaPagoTarjeta.class);
                            intentVisa.putExtra("lugarProviene", "formaspago");
                            intentVisa.putExtra("posicioncarga", posiciondecargaid);
                            intentVisa.putExtra("formapagoid", formaPagoIdentificador);
                            intentVisa.putExtra("montoencanasta", "$"+ df.format(MontoCanasta));
                            intentVisa.putExtra("numeroTarjeta", "");
                            startActivity(intentVisa);
                            finish();
//                            }
                        }else{
                            if (idoperativa.equals("20")){
                                data.getWritableDatabase().delete("PagoTarjeta", null, null);
                                data.close();

                                data.InsertarDatosPagoTarjeta("1",posiciondecargaid, formaPagoIdentificador, Double.toString(MontoCanasta),  "0", provieneDe, "0", numeroTarjeta, Double.toString(descuento), nipCliente, Double.toString(MontoCanasta));
                                Intent intentVisa = new Intent(getApplicationContext(), VentaPagoTarjeta.class);
                                intentVisa.putExtra("Enviadodesde", "formaspago");
                                intentVisa.putExtra("posicioncarga", posiciondecargaid);
                                intentVisa.putExtra("formapagoid", formaPagoIdentificador);
                                intentVisa.putExtra("montoencanasta", "$"+ df.format(MontoCanasta));
                                startActivity(intentVisa);
                                finish();
                            }else{
                                titulo = "PUNTADA";
                                String mensajes = "¿Desea Acumular la venta a su Tarjeta Puntada?";
                                Modales modalesPuntada = new Modales(FormasPagoReordenado.this);
                                View viewLectura = modalesPuntada.MostrarDialogoAlerta(FormasPagoReordenado.this, mensajes,  "SI", "NO");
                                String finalFormaPagoIdentificador = formaPagoIdentificador;
                                viewLectura.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        //LeeTarjeta();
                                        data.getWritableDatabase().delete("PagoTarjeta", null, null);
                                        data.close();
                                        data.InsertarDatosPagoTarjeta("1",posiciondecargaid, finalFormaPagoIdentificador, Double.toString(MontoCanasta),  "1", provieneDe, "0", numeroTarjeta, Double.toString(descuento), nipCliente, Double.toString(MontoCanasta));

                                        Intent intent = new Intent(getApplicationContext(), MonederosElectronicos.class);
                                        //intent.putExtra("device_name", m_deviceName);
                                        intent.putExtra("banderaHuella", banderaHuella);
                                        intent.putExtra("Enviadodesde", "formaspago");
                                        intent.putExtra("idoperativa", idoperativa);
                                        intent.putExtra("formapagoid", finalFormaPagoIdentificador);
                                        intent.putExtra("NombrePago", nombrepago);
                                        intent.putExtra("montoenlacanasta", MontoCanasta);
                                        intent.putExtra("posicioncargaid", posiciondecargaid);
                                        intent.putExtra("tipoTarjeta", "Puntada");
                                        intent.putExtra("pagoconpuntada", "no");

                                        startActivity(intent);
                                        modalesPuntada.alertDialog.dismiss();
                                        finish();
                                    }
                                });
                                String finalFormaPagoIdentificador1 = formaPagoIdentificador;
                                viewLectura.findViewById(R.id.buttonNo).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        modalesPuntada.alertDialog.dismiss();
                                        data.getWritableDatabase().delete("PagoTarjeta", null, null);
                                        data.close();

                                        data.InsertarDatosPagoTarjeta("1",posiciondecargaid, finalFormaPagoIdentificador1, Double.toString(MontoCanasta),  "0", provieneDe, "0", numeroTarjeta, Double.toString(descuento), nipCliente, Double.toString(MontoCanasta));
                                        Intent intentVisa = new Intent(getApplicationContext(), VentaPagoTarjeta.class);
                                        intentVisa.putExtra("Enviadodesde", "formaspago");
                                        intentVisa.putExtra("posicioncarga", posiciondecargaid);
                                        intentVisa.putExtra("formapagoid", finalFormaPagoIdentificador1);
                                        intentVisa.putExtra("montoencanasta", "$"+ df.format(MontoCanasta));
                                        startActivity(intentVisa);
                                        finish();
                                    }
                                });
                            }
                        }
                        break;
                    case "6": //VALES ELECTRONICOS
                        if (idoperativa.equals("20")){
                            ImprimeVenta();
                            //IMPRIMIR
                        }else{
                            String mensajes = "¿Desea Acumular la venta a su Tarjeta Puntada?";
                            Modales modalesEfectivo = new Modales(FormasPagoReordenado.this);
                            View viewLecturas = modalesEfectivo.MostrarDialogoAlerta(FormasPagoReordenado.this, mensajes,  "SI", "NO");
                            viewLecturas.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(getApplicationContext(), MonederosElectronicos.class);
                                    //intent.putExtra("device_name", m_deviceName);
                                    intent.putExtra("banderaHuella", "banderaHuella");
                                    intent.putExtra("Enviadodesde", "formaspago");
                                    intent.putExtra("idoperativa", idoperativa);
                                    intent.putExtra("formapagoid", formapago);
                                    intent.putExtra("NombrePago", nombrepago);
                                    intent.putExtra("montoenlacanasta", MontoCanasta);
                                    intent.putExtra("posicioncargaid", posiciondecargaid);
                                    intent.putExtra("tipoTarjeta", "Puntada");
                                    intent.putExtra("pagoconpuntada", "no");
                                    startActivity(intent);
                                    modalesEfectivo.alertDialog.dismiss();
                                }
                            });

                            viewLecturas.findViewById(R.id.buttonNo).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    modalesEfectivo.alertDialog.dismiss();
                                    ImprimeVenta();
                                }
                            });
                        }
                        break;
                    case "14": //MERCADO PAGO
                    case "17": // VALES PROPIOS
                        titulo = "AVISO";
                        final Modales modales = new Modales(FormasPagoReordenado.this);
                        View view1 = modales.MostrarDialogoAlertaAceptar(FormasPagoReordenado.this, "Proximamente", titulo);
                        view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                modales.alertDialog.dismiss();
                                rcvFormasPagoReordenado.setEnabled(true);
                            }
                        });
                        break;
                    case "16"://PAGO DOLARES
                        if (numeroTarjeta.length() > 0){
//                            if (acumularPuntada.equals("true")) {
//                                predeterminarPuntadaAcumular(Integer.parseInt(formapago));
//
//                            }else{
                            ObtieneMontoDolares(1);
//                            }
                        }else{
                            if (idoperativa.equals("20")){
                                ObtieneMontoDolares(1);
                            }else{
                                titulo = "PUNTADA";
                                String mensajes = "¿Desea Acumular la venta a su Tarjeta Puntada?";
                                Modales modalesEfectivo = new Modales(FormasPagoReordenado.this);
                                View viewLecturas = modalesEfectivo.MostrarDialogoAlerta(FormasPagoReordenado.this, mensajes,  "SI", "NO");
                                viewLecturas.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        ObtieneMontoDolares(2);
                                    }
                                });

                                viewLecturas.findViewById(R.id.buttonNo).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        //                                    RespuestaImprimeFinaliza(posicioncarga, idusuario, formapagoid, numticket, nombrepago);
                                        modalesEfectivo.alertDialog.dismiss();
                                        //                                    SeleccionaPesosDoalares();
                                        ObtieneMontoDolares(1);
                                    }
                                });
                            }
                        }
                        break;

                }
            }
        });
        rcvFormasPagoReordenado.setAdapter(adapter);
        RecyclerView.ItemDecoration divider = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rcvFormasPagoReordenado.addItemDecoration(divider);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(UP | DOWN, 0) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                int position_dragged = viewHolder.getAdapterPosition();
                int position_tarjet = target.getAdapterPosition();

                Collections.swap(lrcvFormaPago, position_dragged, position_tarjet);
                adapter.notifyItemMoved(position_dragged, position_tarjet);
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            }
        });
        itemTouchHelper.attachToRecyclerView(rcvFormasPagoReordenado);
//        RecyclerView.ItemDecoration divider = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
//        rcvFormasPagoReordenado.addItemDecoration(divider);
//
//        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(UP | DOWN,0) {
//            @Override
//            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder dragged, @NonNull RecyclerView.ViewHolder target) {
//                int position_dragged = dragged.getAdapterPosition();
//                int position_tarjet = target.getAdapterPosition();
//
//                Collections.swap(lrcvFormaPago, position_dragged, position_tarjet);
//                adapter.notifyItemMoved(position_dragged, position_tarjet);
//                return false;
//            }
//
//            @Override
//            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
//
//            }
//        });
//        itemTouchHelper.attachToRecyclerView(rcvFormasPagoReordenado);

    }

    private void MuestraFormaEfectivo(){
        String tituloEfectivo = "Venta";
        Modales modalesEfectivo = new Modales(FormasPagoReordenado.this);
        View viewVenta = modalesEfectivo.MostrarDialogoEfectivo(FormasPagoReordenado.this, String.valueOf(MontoCanasta), tituloEfectivo);
        viewVenta.findViewById(R.id.btnAceptarVales).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modalesEfectivo.alertDialog.dismiss();
                String titulo = "IMPRIMIR";
                String mensajes = "¿Desea finalizar la venta?";
                Modales modalesImprime = new Modales(FormasPagoReordenado.this);
                View viewLectura = modalesImprime.MostrarDialogoAlerta(FormasPagoReordenado.this, mensajes,  "FINALIZAR", "IMPRIMIR");
                viewLectura.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FinalizaVenta();
                        modalesImprime.alertDialog.dismiss();
                        finish();
                    }
                });
                viewLectura.findViewById(R.id.buttonNo).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        modalesImprime.alertDialog.dismiss();
                        ImprimeVenta();
                        finish();
                    }
                });

            }
        });
        viewVenta.findViewById(R.id.btnCancelarVales).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modalesEfectivo.alertDialog.dismiss();
                rcvFormasPagoReordenado.setEnabled(true);
            }
        });
    }

    private void MuestraFormaEfectivoPuntada(){
        String tituloEfectivo = "Venta";
        Modales modalesEfectivo = new Modales(FormasPagoReordenado.this);
        View viewVenta = modalesEfectivo.MostrarDialogoEfectivo(FormasPagoReordenado.this, String.valueOf(MontoCanasta), tituloEfectivo);
        viewVenta.findViewById(R.id.btnAceptarVales).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modalesEfectivo.alertDialog.dismiss();
                String banderaHuella = getIntent().getStringExtra( "banderaHuella");
                String nombreCompletoVenta = getIntent().getStringExtra("nombrecompleto");
                //LeeTarjeta();
                Intent intent = new Intent(getApplicationContext(), MonederosElectronicos.class);
                //intent.putExtra("device_name", m_deviceName);
                intent.putExtra("banderaHuella", banderaHuella);
                intent.putExtra("Enviadodesde", "formaspago");
                intent.putExtra("numeroEmpleado", sucursalnumeroempleado);
                intent.putExtra("idoperativa", idoperativa);
                intent.putExtra("formapagoid", formaPagoIdentificador);
                intent.putExtra("NombrePago", nombrepago);
                intent.putExtra("NombreCompleto", nombreCompletoVenta);
                intent.putExtra("montoenlacanasta", MontoCanasta);
                intent.putExtra("posicioncargaid", posiciondecargaid);
                intent.putExtra("tipoTarjeta", "Puntada");
                intent.putExtra("pagoconpuntada", "no");
                startActivity(intent);
                finish();
            }
        });
        viewVenta.findViewById(R.id.btnCancelarVales).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modalesEfectivo.alertDialog.dismiss();
                rcvFormasPagoReordenado.setEnabled(true);
            }
        });
    }


    private void FinalizaVenta() {
        if (!Conexion.compruebaConexion(this)) {
            Toast.makeText(getBaseContext(), "Sin conexión a la red ", Toast.LENGTH_SHORT).show();
            Intent intent1 = new Intent(getApplicationContext(), Menu_Principal.class);
            startActivity(intent1);
            finish();
        } else {
            //Utilizamos el metodo POST para  finalizar la Venta
            String url = "http://" + ipEstacion + "/CorpogasService/api/Transacciones/finalizaVenta/sucursal/" + sucursalId + "/posicionCarga/" + posiciondecargaid + "/usuario/" + sucursalnumeroempleado ;
            StringRequest eventoReq = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
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
                                        Modales modales = new Modales(FormasPagoReordenado.this);
                                        View view1 = modales.MostrarDialogoAlertaAceptar(FormasPagoReordenado.this, mensaje, titulo);
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

                                }else {
                                    try {
                                        String titulo = "AVISO";
                                        String mensajes = "Venta Finalizada";
                                        final Modales modales = new Modales(FormasPagoReordenado.this);
                                        View view1 = modales.MostrarDialogoCorrecto(FormasPagoReordenado.this,mensajes);
                                        view1.findViewById(R.id.buttonAction).setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                modales.alertDialog.dismiss();
                                                Intent intent = new Intent(getApplicationContext(), Menu_Principal.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        });
                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();
                }
            }){
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
    }

    private  void ImprimeVenta(){
        FormasPagoObjecto = new JSONObject();
        FormasPagoArreglo = new JSONArray();
        try {
            FormasPagoObjecto.put("Id", formapago);
            FormasPagoObjecto.put("Importe", MontoCanasta);
            FormasPagoArreglo.put(FormasPagoObjecto);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONObject datos = new JSONObject();
        String url = "http://" + ipEstacion + "/CorpogasService/api/tickets/generar";
        RequestQueue queue = Volley.newRequestQueue(this);
        try {
            datos.put("PosicionCargaId", posiciondecargaid);
            datos.put("IdUsuario", sucursalnumeroempleado);
            datos.put("SucursalId", sucursalId);
            datos.put("IdFormasPago", FormasPagoArreglo);
            datos.put("SucursalId", sucursalId);
            datos.put("ConfiguracionAplicacionId", data.getIdTarjtero());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request_json = new JsonObjectRequest(Request.Method.POST, url,  datos, new Response.Listener<JSONObject>() {
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
                            Modales modales = new Modales(FormasPagoReordenado.this);
                            View view1 = modales.MostrarDialogoError(FormasPagoReordenado.this, mensaje);
                            view1.findViewById(R.id.buttonAction).setOnClickListener(new View.OnClickListener() {
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
                        Modales modales = new Modales(FormasPagoReordenado.this);
                        View view1 = modales.MostrarDialogoCorrecto(FormasPagoReordenado.this, "Ticket enviado a impresora central");
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
                Toast.makeText(FormasPagoReordenado.this, "error", Toast.LENGTH_SHORT).show();

            }
        }){
            public Map<String,String>getHeaders() throws AuthFailureError {
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

    private void ObtieneMontoDolares(Integer identificador){
        String url = "http://" + ipEstacion + "/CorpogasService/api/transacciones/resumenultimaTransaccion/sucursalId/" + sucursalId + "/posicionCargaId/" + posiciondecargaid + "/formapago/16";
        StringRequest eventoReq = new StringRequest(Request.Method.GET, url,  new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject respuesta = null;
                try {
                    respuesta = new JSONObject(response);
                    String correcto = respuesta.getString("Correcto");
                    if (correcto.equals("true")){
                        String objetoRespuesta = respuesta.getString("ObjetoRespuesta");
                        JSONObject objetoRespuestaObj = new JSONObject(objetoRespuesta);
                        totalPesos  = Double.parseDouble(objetoRespuestaObj.getString("TotalVentaPesos"));
                        totalDolares = Double.parseDouble(objetoRespuestaObj.getString("TotalVentaDivisa"));
                        tipoCambio = Double.parseDouble(objetoRespuestaObj.getString("TasaCambio"));
                        if (identificador ==1){
                            MuestraFormaEfectivoDolares();
                        }else{
                            MuestraFormaEfectivoDolaresPuntada();
                        }
                    }else{
                        Toast.makeText(FormasPagoReordenado.this, "No hay Tipo de cambio", Toast.LENGTH_SHORT).show();
                        rcvFormasPagoReordenado.setEnabled(true);
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

    private void MuestraFormaEfectivoDolares(){
        MontoCanasta = totalDolares;
        String tituloEfectivo = "USD";
        Modales modalesEfectivo = new Modales(FormasPagoReordenado.this);
        View viewVenta = modalesEfectivo.MostrarDialogoEfectivoDolares(FormasPagoReordenado.this, String.valueOf(totalDolares),  String.valueOf(tipoCambio), tituloEfectivo);
        viewVenta.findViewById(R.id.btnAceptarDolares).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modalesEfectivo.alertDialog.dismiss();
                String titulo = "IMPRIMIR";
                String mensajes = "¿Desea finalizar la venta?";
                Modales modalesImprime = new Modales(FormasPagoReordenado.this);
                View viewLectura = modalesImprime.MostrarDialogoAlerta(FormasPagoReordenado.this, mensajes,  "FINALIZAR", "IMPRIMIR");
                viewLectura.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FinalizaVenta();
                        modalesImprime.alertDialog.dismiss();
                    }
                });
                viewLectura.findViewById(R.id.buttonNo).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        modalesImprime.alertDialog.dismiss();
                        ImprimeVenta();
                    }
                });

            }
        });
        viewVenta.findViewById(R.id.btnCancelarDolares).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modalesEfectivo.alertDialog.dismiss();
                rcvFormasPagoReordenado.setEnabled(true);
            }
        });
    }

    private void MuestraFormaEfectivoDolaresPuntada(){
        MontoCanasta = totalDolares;
        String tituloEfectivo = "USD";
        Modales modalesEfectivo = new Modales(FormasPagoReordenado.this);
        View viewVenta = modalesEfectivo.MostrarDialogoEfectivoDolares(FormasPagoReordenado.this, String.valueOf(totalDolares),  String.valueOf(tipoCambio), tituloEfectivo);
        viewVenta.findViewById(R.id.btnAceptarDolares).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modalesEfectivo.alertDialog.dismiss();
                Intent intent = new Intent(getApplicationContext(), MonederosElectronicos.class);
                //intent.putExtra("device_name", m_deviceName);
                intent.putExtra("banderaHuella", "banderaHuella");
                intent.putExtra("Enviadodesde", "formaspago");
                intent.putExtra("numeroEmpleado", sucursalnumeroempleado);
                intent.putExtra("idoperativa", idoperativa);
                intent.putExtra("formapagoid", formaPagoIdentificador);
                intent.putExtra("NombrePago", nombrepago);
                intent.putExtra("NombreCompleto", "nombreCompletoVenta");
                intent.putExtra("montoenlacanasta", MontoCanasta);
                intent.putExtra("posicioncargaid", posiciondecargaid);
                intent.putExtra("tipoTarjeta", "Puntada");
                intent.putExtra("pagoconpuntada", "no");
                startActivity(intent);

            }
        });
        viewVenta.findViewById(R.id.btnCancelarDolares).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modalesEfectivo.alertDialog.dismiss();
                rcvFormasPagoReordenado.setEnabled(true);
            }
        });
    }


}