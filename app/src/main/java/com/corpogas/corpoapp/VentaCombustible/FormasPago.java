package com.corpogas.corpoapp.VentaCombustible;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
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
import com.corpogas.corpoapp.ValesPapel.ValesPapel;

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

public class FormasPago extends AppCompatActivity {
    private ListView list;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formas_pago);
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

        obtenerformasdepago();
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

        final List<String> AcumulaPuntosArreglo;
        AcumulaPuntosArreglo = new ArrayList<String>();



        try {
            //JSONObject jsonObject = new JSONObject(response);
            //String formapago = jsonObject.getString("SucursalFormapagos");
            Boolean colocarformapago;
            JSONArray nodo = new JSONArray(response);
            String numero_pago;
            String internonumero="";
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
                }else{
                    colocarformapago = false;
                    JSONObject nodo1 = nodo.getJSONObject(i);
                    String formap = nodo1.getString("PaymentMethod"); //FormaPago
                    JSONObject  numerointernoobject = new JSONObject(formap);
                    internonumero = numerointernoobject.getString("Id"); //NumeroInterno

                    numero_pago = numerointernoobject.getString("Id"); //FormaPagoId
                    //String formapago1 = nodo1.getString("PaymentMethod");
                    //JSONObject nodo2 = new JSONObject(formapago1);
                    String nombre_pago = numerointernoobject.getString("LongDescription"); //DescripcionLarga
                    String numero_ticket = numerointernoobject.getString("PrintsAllowed");
                    String visible = numerointernoobject.getString("IsFrontVisible"); //VisibleTarjetero
                    String acumulaPuntos = numerointernoobject.getString("AccumulatePoints");
                    if (visible == "true") {
                        if (numero_pago.equals("14")){
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
                            break;
                        case 2: //Vales
                            imgid.add(R.drawable.vale);
                            break;
                        case 3: //AMEX
                            imgid.add(R.drawable.american);
                            break;
                        case 5: //VISA/MC
                            imgid.add(R.drawable.visa);
                            break;
                        case 6: // VALE/ELEC
                            imgid.add(R.drawable.valeelectronico);
                            break;
                        case 7: //CREDITO ES
                            imgid.add(R.drawable.gas);
                            break;
                        case 8: //CREDITO ES
                            imgid.add(R.drawable.gas);
                            break;
                        case 9: //CREDITO ES
                            imgid.add(R.drawable.gas);
                            break;
                        case 10: //CORPOMOBILE
                            imgid.add(R.drawable.gas);
                            break;
                        case 11: //TANQUELLENO
                            imgid.add(R.drawable.tanquelleno);
                            break;
                        case 12: //REDENCION PUNTADA
                            imgid.add(R.drawable.tanquelleno);
                            break;
                        case 13: //GASCARD
                            imgid.add(R.drawable.gascard);
                            break;
                        case 14: //MERCADOPAGO
                            imgid.add(R.drawable.mercadopago);
                            break;
                        case 90://Consigna
                            imgid.add(R.drawable.gas);
                            break;
                        case 91: //JARREO
                            imgid.add(R.drawable.jarreo);
                            break;
                        case 92://AUTOJARREO
                            imgid.add(R.drawable.jarreo);
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
        list = (ListView) findViewById(R.id.lstFormasPago);
        list.setEnabled(true);
        list.setAdapter(adapter);

        //Optenemos el numero del Item seleccionado que corresponde a al numero de posicion de carga
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                list.setEnabled(false);
                String acumularPuntada= "false";
                int pos = position + 1;
                formapago = NumeroInternoFormaPago.get(position);
                if (Integer.parseInt(formapago)>0){
                    acumularPuntada = AcumulaPuntosArreglo.get(position);
                }
                String formapagoid = IdFormaPago.get(position);//String.valueOf(pos);
                nombrepago = maintitle.get(position);
                String numticket = numerotickets.get(position);
                int numpago = Integer.parseInt(NumeroInternoFormaPago.get(position)); //IdFormaPago
                numpago1 = IdFormaPago.get(position);
                idoperativa = getIntent().getStringExtra("IdOperativa");
                String posicioncarga = getIntent().getStringExtra("posicioncarga");
                String idusuario = data.getUsuarioId(); //getIntent().getStringExtra("IdUsuario");
                String ClaveDespachador = getIntent().getStringExtra("clavedespachador");
//                int operativa = Integer.parseInt(idoperativa);

                switch (Integer.parseInt(formapago)) {
                    case 0: //Diferentes Formas de Pago
                        data.getWritableDatabase().delete("PagoTarjeta", null, null);
                        data.close();
                        data.InsertarDatosPagoTarjeta("1",posiciondecargaid, formapagoid, Double.toString(MontoCanasta), "0", provieneDe, "0", numeroTarjeta, Double.toString(descuento), nipCliente, Double.toString(MontoCanasta));
                        Intent intentDiferente = new Intent(getApplicationContext(), DiferentesFormasPago.class);
                        intentDiferente.putExtra("Enviadodesde", "formaspago");
                        intentDiferente.putExtra("idusuario", idusuario);
                        intentDiferente.putExtra("posicioncarga", posiciondecargaid);
                        intentDiferente.putExtra("claveusuario", ClaveDespachador);
                        intentDiferente.putExtra("idoperativa", idoperativa);
                        intentDiferente.putExtra("formapagoid", numpago);
                        intentDiferente.putExtra("montoencanasta", MontoCanasta);
                        intentDiferente.putExtra("numeroempleadosucursal", sucursalnumeroempleado);
                        intentDiferente.putExtra("numeroTarjeta", numeroTarjeta);
                        intentDiferente.putExtra("descuento", descuento);
                        intentDiferente.putExtra("nipCliente", nipCliente);
                        startActivity(intentDiferente);
                        finish();
                        break;
                    case 6: //VALES ELECTRONICOS
                        if (idoperativa.equals("20")){
                            ImprimeVenta();
                            //IMPRIMIR
                        }else{
                            String mensajes = "¿Desea Acumular la venta a su Tarjeta Puntada?";
                            Modales modalesEfectivo = new Modales(FormasPago.this);
                            View viewLecturas = modalesEfectivo.MostrarDialogoAlerta(FormasPago.this, mensajes,  "SI", "NO");
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
                    case 2:
//                        if (acumularPuntada.equals("true")) {
//                            predeterminarPuntadaAcumular(Integer.parseInt(formapago));
//                        }else{
                            Intent intentVale = new Intent(getApplicationContext(), ValesPapel.class);
                            intentVale.putExtra("Enviadodesde", "formaspago");
                            intentVale.putExtra("posicioncarga", posiciondecargaid);
                            intentVale.putExtra("idoperativa", idoperativa);
                            intentVale.putExtra("formapagoid", numpago);
                            intentVale.putExtra("montoencanasta", MontoCanasta);
                            startActivity(intentVale);
                            finish();
//                        }

                        break;
                    case 13://GAS CARD AMEX
                    case 3: //AMEX
                    case 5: //VISA
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
                                data.InsertarDatosPagoTarjeta("1",posiciondecargaid, formapagoid, Double.toString(MontoCanasta), "0", provieneDe, "0", numeroTarjeta, Double.toString(descuento), nipCliente, Double.toString(MontoCanasta));

                                Intent intentVisa = new Intent(getApplicationContext(), VentaPagoTarjeta.class);
                                intentVisa.putExtra("lugarProviene", "formaspago");
                                intentVisa.putExtra("posicioncarga", posiciondecargaid);
                                intentVisa.putExtra("formapagoid", numpago);
                                intentVisa.putExtra("montoencanasta", "$"+ df.format(MontoCanasta));
                                intentVisa.putExtra("numeroTarjeta", "");
                                startActivity(intentVisa);
                                finish();
//                            }
                        }else{
                            if (idoperativa.equals("20")){
                                data.getWritableDatabase().delete("PagoTarjeta", null, null);
                                data.close();

                                data.InsertarDatosPagoTarjeta("1",posiciondecargaid, formapagoid, Double.toString(MontoCanasta),  "0", provieneDe, "0", numeroTarjeta, Double.toString(descuento), nipCliente, Double.toString(MontoCanasta));
                                Intent intentVisa = new Intent(getApplicationContext(), VentaPagoTarjeta.class);
                                intentVisa.putExtra("Enviadodesde", "formaspago");
                                intentVisa.putExtra("posicioncarga", posiciondecargaid);
                                intentVisa.putExtra("formapagoid", numpago);
                                intentVisa.putExtra("montoencanasta", "$"+ df.format(MontoCanasta));
                                startActivity(intentVisa);
                                finish();
                            }else{
                                String titulo = "PUNTADA";
                                String mensajes = "¿Desea Acumular la venta a su Tarjeta Puntada?";
                                Modales modalesPuntada = new Modales(FormasPago.this);
                                View viewLectura = modalesPuntada.MostrarDialogoAlerta(FormasPago.this, mensajes,  "SI", "NO");
                                viewLectura.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        //LeeTarjeta();
                                        data.getWritableDatabase().delete("PagoTarjeta", null, null);
                                        data.close();
                                        data.InsertarDatosPagoTarjeta("1",posiciondecargaid, formapagoid, Double.toString(MontoCanasta),  "1", provieneDe, "0", numeroTarjeta, Double.toString(descuento), nipCliente, Double.toString(MontoCanasta));

                                        Intent intent = new Intent(getApplicationContext(), MonederosElectronicos.class);
                                        //intent.putExtra("device_name", m_deviceName);
                                        intent.putExtra("banderaHuella", banderaHuella);
                                        intent.putExtra("Enviadodesde", "formaspago");
                                        intent.putExtra("idoperativa", idoperativa);
                                        intent.putExtra("formapagoid", formapago);
                                        intent.putExtra("NombrePago", nombrepago);
                                        intent.putExtra("montoenlacanasta", MontoCanasta);
                                        intent.putExtra("posicioncargaid", posiciondecargaid);
                                        intent.putExtra("tipoTarjeta", "Puntada");
                                        intent.putExtra("pagoconpuntada", "no");

                                        startActivity(intent);
                                        modalesPuntada.alertDialog.dismiss();
                                    }
                                });
                                viewLectura.findViewById(R.id.buttonNo).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        modalesPuntada.alertDialog.dismiss();
                                        data.getWritableDatabase().delete("PagoTarjeta", null, null);
                                        data.close();

                                        data.InsertarDatosPagoTarjeta("1",posiciondecargaid, formapagoid, Double.toString(MontoCanasta),  "0", provieneDe, "0", numeroTarjeta, Double.toString(descuento), nipCliente, Double.toString(MontoCanasta));
                                        Intent intentVisa = new Intent(getApplicationContext(), VentaPagoTarjeta.class);
                                        intentVisa.putExtra("Enviadodesde", "formaspago");
                                        intentVisa.putExtra("posicioncarga", posiciondecargaid);
                                        intentVisa.putExtra("formapagoid", numpago);
                                        intentVisa.putExtra("montoencanasta", "$"+ df.format(MontoCanasta));
                                        startActivity(intentVisa);
                                        finish();
                                    }
                                });
                            }
                        }
                        break;
                    case 14: //MERCADO PAGO
                    case 17: // VALES PROPIOS
                        String titulo = "AVISO";
                        final Modales modales = new Modales(FormasPago.this);
                        View view1 = modales.MostrarDialogoAlertaAceptar(FormasPago.this, "Proximamente", titulo);
                        view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                modales.alertDialog.dismiss();
                                list.setEnabled(true);
                            }
                        });
                        break;
                    case 16://PAGO DOLARES
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
                                Modales modalesEfectivo = new Modales(FormasPago.this);
                                View viewLecturas = modalesEfectivo.MostrarDialogoAlerta(FormasPago.this, mensajes,  "SI", "NO");
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
                    case 1:
//                        if (numpago == 0) { //DIFERENTES FORMAS DE PAGO
//                            Intent intent = new Intent(getApplicationContext(), DiferentesFormasPago.class);
//                            intent.putExtra("Enviadodesde", "formaspago");
//                            intent.putExtra("idusuario", idusuario);
//                            intent.putExtra("posicioncarga", posicioncarga);
//                            intent.putExtra("claveusuario", ClaveDespachador);
//                            intent.putExtra("idoperativa", idoperativa);
//                            intent.putExtra("formapagoid", numpago);
//                            intent.putExtra("montoencanasta", MontoCanasta);
//                            intent.putExtra("numeroempleadosucursal", sucursalnumeroempleado);
//
//                            startActivity(intent);
//                            finish();
//                        }
                        if (numpago == 1) { //2 EFECTIVO
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
                                    Modales modalesEfectivo = new Modales(FormasPago.this);
                                    View viewLecturas = modalesEfectivo.MostrarDialogoAlerta(FormasPago.this, mensajes,  "SI", "NO");
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
                        } else {
                            if (numpago == 2) {//3 VALES
                                Intent intent = new Intent(getApplicationContext(), ValesPapel.class);
                                intent.putExtra("Enviadodesde", "formaspago");
                                intent.putExtra("idusuario", idusuario);
                                intent.putExtra("posicioncarga", posicioncarga);
                                intent.putExtra("claveusuario", ClaveDespachador);
                                intent.putExtra("idoperativa", idoperativa);
                                intent.putExtra("formapagoid", numpago);
                                intent.putExtra("montoencanasta", MontoCanasta);
                                intent.putExtra("numeroempleadosucursal", sucursalnumeroempleado);
                                startActivity(intent);
                                finish();

                            } else {
                                if (numpago == 3 || numpago == 5 || numpago == 13 ) { //numpago == 4 || numpago == 5 || numpago == 6 AMEX, VISA, GASCARD

                                    titulo = "PUNTADA";
                                    String mensaje = "¿Desea Acumular la venta a su Tarjeta Puntada?";
                                    Modales modalesA = new Modales(FormasPago.this);
                                    View viewLecturan = modalesA.MostrarDialogoAlerta(FormasPago.this, mensaje,  "SI", "NO");
                                    viewLecturan.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            String banderaHuella = getIntent().getStringExtra( "banderaHuella");
                                            String nombreCompletoVenta = getIntent().getStringExtra("nombrecompleto");
                                            //LeeTarjeta();
//                                            Intent intent = new Intent(getApplicationContext(), MonederosElectronicos.class);
//                                            //intent.putExtra("device_name", m_deviceName);
//                                            intent.putExtra("banderaHuella", banderaHuella);
//                                            intent.putExtra("Enviadodesde", "formaspago");
//                                            intent.putExtra("idusuario", idusuario);
//                                            intent.putExtra("posicioncarga", posicioncarga);
//                                            intent.putExtra("claveusuario", ClaveDespachador);
//                                            intent.putExtra("idoperativa", idoperativa);
//                                            intent.putExtra("formapagoid", numpago1);
//                                            intent.putExtra("NombrePago", nombrepago);
//                                            intent.putExtra("NombreCompleto", nombreCompletoVenta);
//                                            intent.putExtra("montoenlacanasta", MontoCanasta);
//                                            intent.putExtra("posicioncargaid", posiciondecargaid);
//                                            intent.putExtra("tipoTarjeta", "Puntada");
//                                            intent.putExtra("pagoconpuntada", "no");
//
//                                            startActivity(intent);
                                            modalesA.alertDialog.dismiss();
                                        }
                                    });
                                    viewLecturan.findViewById(R.id.buttonNo).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
//                                            ObtenerDatosTicketTarjetas(posicioncarga, idusuario, formapagoid, numticket, "true");
                                            modalesA.alertDialog.dismiss();

                                        }
                                    });

                                }else{
                                    if (numpago == 91) {//11 JARREO
//                                        imprimirticket(posicioncarga, "jarreo", idusuario);

                                    }else{
                                        if (numpago == 6) {//7 VALE ELECTRONICO
//                                            ObtenerDatosTicketTarjetas(posicioncarga, idusuario, formapagoid, numticket, "false");
                                        }else{
                                            if (numpago == 14) {
//                                                String nombreCompletoVenta = getIntent().getStringExtra("nombrecompleto");
//
//                                                Intent intent = new Intent(getApplicationContext(), MostrarQR.class);
//                                                intent.putExtra("poscicionCarga", posicioncarga);
//                                                intent.putExtra("montoenlacanasta", MontoCanasta);
//                                                intent.putExtra("numeromepleado", sucursalnumeroempleado);
//                                                intent.putExtra("NombreCompleto", nombreCompletoVenta);
//
//                                                startActivity(intent);
//                                                finish();

                                            }
                                        }
                                    }
                                }
                            }
                        }
                        break;

                    case 41:
//                        Intent intent = new Intent(getApplicationContext(), MostrarQR.class);
//                        intent.putExtra("poscicionCarga", posicioncarga);
//                        startActivity(intent);
//                        finish();
                        break;
                    default:
                        throw new IllegalStateException("Valor inesperado: " ); //+ operativa
                }

            }
        });
    }

    private void predeterminarPuntadaAcumular(Integer numpago){
            if (!Conexion.compruebaConexion(this)) {
                Toast.makeText(getBaseContext(), "Sin conexión a la red ", Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent(getApplicationContext(), Menu_Principal.class);

                startActivity(intent1);
                finish();
            } else {
                JSONObject datos = new JSONObject();

                String idoperativa = getIntent().getStringExtra("idoperativa");
                JSONArray myArray = new JSONArray();
                SQLiteBD data = new SQLiteBD(getApplicationContext());
                String url = "http://" + data.getIpEstacion() + "/CorpogasService/api/puntadas/actualizaPuntos/numeroEmpleado/" + sucursalnumeroempleado;
                RequestQueue queue = Volley.newRequestQueue(this);
                try {
                    datos.put("NuTarjetero", data.getIdTarjtero() );
                    datos.put("SucursalId", sucursalId);
                    datos.put("RequestID", 35); // Esto es para cuando se termina de realizar el despacho, es pasa la tarjeta puntada y se acumula
                    datos.put("PosicionCarga", posiciondecargaid); //posicionCarga
                    datos.put("Tarjeta", numeroTarjeta);
                    //datos.put("NIP", NipCliente);
                    datos.put("Productos", myArray);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JsonObjectRequest request_json = new JsonObjectRequest(Request.Method.POST, url, datos, new com.android.volley.Response.Listener<JSONObject>() {
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


                            switch (numpago) {
                                case 1:
                                    MuestraFormaEfectivo();
                                    break;
                                case 2:
                                    Intent intentVale = new Intent(getApplicationContext(), ValesPapel.class);
                                    intentVale.putExtra("Enviadodesde", "formaspago");
                                    intentVale.putExtra("posicioncarga", posiciondecargaid);
                                    intentVale.putExtra("idoperativa", idoperativa);
                                    intentVale.putExtra("formapagoid", numpago);
                                    intentVale.putExtra("montoencanasta", MontoCanasta);
                                    startActivity(intentVale);
                                    finish();
                                    break;
                                case 13://VALES ELECTRONICOS
                                case 3: //AMEX
                                case 5: //VISA
                                    DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
                                    simbolos.setDecimalSeparator('.');
                                    DecimalFormat df = new DecimalFormat("####.00##",simbolos);

                                    df.setMaximumFractionDigits(2);
                                    data.getWritableDatabase().delete("PagoTarjeta", null, null);
                                    data.close();
                                    data.InsertarDatosPagoTarjeta("1",posiciondecargaid, Integer.toString(numpago), Double.toString(MontoCanasta),  "1", provieneDe, "0", numeroTarjeta, Double.toString(descuento), nipCliente, Double.toString(MontoCanasta));
                                    Intent intentVisa = new Intent(getApplicationContext(), VentaPagoTarjeta.class);
                                    intentVisa.putExtra("Enviadodesde", "formaspago");
                                    intentVisa.putExtra("posicioncarga", posiciondecargaid);
                                    intentVisa.putExtra("formapagoid", numpago);
                                    intentVisa.putExtra("montoencanasta", "$"+ df.format(MontoCanasta));
                                    intentVisa.putExtra("numeroTarjeta", "");
                                    startActivity(intentVisa);
                                    finish();

                                    break;
                                default:
                            }


//                                    Double MontoenCarrito = getIntent().getDoubleExtra("montoenlacanasta", 0);
//                                    Intent intente = new Intent(getApplicationContext(), ImprimePuntada.class);
//                                    intente.putExtra("posicioncarga", posiciondecargaid);
//                                    intente.putExtra("idoperativa", idoperativa);
//                                    intente.putExtra("idformapago", numpago);
//                                    intente.putExtra("nombrepago", nombrepago);
//                                    intente.putExtra("montoencanasta", MontoenCarrito);
//                                    startActivity(intente);
//                                    finish();
                        } else {
                            try {
                                String titulo = "AVISO";
                                String mensajes = "" + mensaje;
                                Modales modales = new Modales(FormasPago.this);
                                View view1 = modales.MostrarDialogoAlertaAceptar(FormasPago.this, mensajes, titulo);
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
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {


                    }
                }) {
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> headers = new HashMap<String, String>();
                        return headers;
                    }

                    protected com.android.volley.Response<JSONObject> parseNetwokResponse(NetworkResponse response) {
                        if (response != null) {

                            try {
                                String responseString;
                                JSONObject datos = new JSONObject();
                                responseString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));

                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                        }
                        return com.android.volley.Response.success(datos, HttpHeaderParser.parseCacheHeaders(response));
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                request_json.setRetryPolicy(new DefaultRetryPolicy(500000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                queue.add(request_json);

            }
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
                                Toast.makeText(FormasPago.this, "No hay Tipo de cambio", Toast.LENGTH_SHORT).show();
                                list.setEnabled(true);
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


    private void ObtieneMontoDolaresPuntada(){
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


                        //Manda a Puntada
                        Intent intent = new Intent(getApplicationContext(), MonederosElectronicos.class);
                        //intent.putExtra("device_name", m_deviceName);
                        intent.putExtra("banderaHuella", "false");
                        intent.putExtra("Enviadodesde", "formaspago");
                        intent.putExtra("numeroEmpleado", sucursalnumeroempleado);
                        intent.putExtra("idoperativa", idoperativa);
                        intent.putExtra("formapagoid", numpago1);
                        intent.putExtra("NombrePago", nombrepago);
                        intent.putExtra("NombreCompleto", "nombreCompletoVenta");
                        intent.putExtra("montoenlacanasta", MontoCanasta);
                        intent.putExtra("posicioncargaid", posiciondecargaid);
                        intent.putExtra("tipoTarjeta", "Puntada");
                        intent.putExtra("pagoconpuntada", "no");
                        startActivity(intent);

                    }else{
                        Toast.makeText(FormasPago.this, "No hay Tipo de cambio", Toast.LENGTH_SHORT).show();
                        list.setEnabled(true);
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



    private void SeleccionaPesosDoalares(){
        String titulo = "PAGO EFECTIVO";
        String mensaje = "FORMA DE PAGO EN EFECTIVO";
        Modales modales = new Modales(FormasPago.this);
        View viewLectura = modales.MostrarDialogoAlerta(FormasPago.this, mensaje,  "DOLARES", "PESOS");
        viewLectura.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ObtieneTipoCambbio();
                modales.alertDialog.dismiss();
            }
        });

        viewLectura.findViewById(R.id.buttonNo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MuestraFormaEfectivo();
                modales.alertDialog.dismiss();
            }
        });
    }

    private void ObtieneTipoCambbio(){
        MuestraFormaEfectivo();
    }


    private void MuestraFormaEfectivo(){
        String tituloEfectivo = "Venta";
        Modales modalesEfectivo = new Modales(FormasPago.this);
        View viewVenta = modalesEfectivo.MostrarDialogoEfectivo(FormasPago.this, String.valueOf(MontoCanasta), tituloEfectivo);
        viewVenta.findViewById(R.id.btnAceptarVales).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modalesEfectivo.alertDialog.dismiss();
                String titulo = "IMPRIMIR";
                String mensajes = "¿Desea finalizar la venta?";
                Modales modalesImprime = new Modales(FormasPago.this);
                View viewLectura = modalesImprime.MostrarDialogoAlerta(FormasPago.this, mensajes,  "FINALIZAR", "IMPRIMIR");
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
        viewVenta.findViewById(R.id.btnCancelarVales).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modalesEfectivo.alertDialog.dismiss();
                list.setEnabled(true);
            }
        });
    }

    private void MuestraFormaEfectivoPuntada(){
        String tituloEfectivo = "Venta";
        Modales modalesEfectivo = new Modales(FormasPago.this);
        View viewVenta = modalesEfectivo.MostrarDialogoEfectivo(FormasPago.this, String.valueOf(MontoCanasta), tituloEfectivo);
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
                intent.putExtra("formapagoid", numpago1);
                intent.putExtra("NombrePago", nombrepago);
                intent.putExtra("NombreCompleto", nombreCompletoVenta);
                intent.putExtra("montoenlacanasta", MontoCanasta);
                intent.putExtra("posicioncargaid", posiciondecargaid);
                intent.putExtra("tipoTarjeta", "Puntada");
                intent.putExtra("pagoconpuntada", "no");
                startActivity(intent);
            }
        });
        viewVenta.findViewById(R.id.btnCancelarVales).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modalesEfectivo.alertDialog.dismiss();
                list.setEnabled(true);
            }
        });
    }


    private void MuestraFormaEfectivoDolares(){
        MontoCanasta = totalDolares;
        String tituloEfectivo = "USD";
        Modales modalesEfectivo = new Modales(FormasPago.this);
        View viewVenta = modalesEfectivo.MostrarDialogoEfectivoDolares(FormasPago.this, String.valueOf(totalDolares),  String.valueOf(tipoCambio), tituloEfectivo);
        viewVenta.findViewById(R.id.btnAceptarDolares).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modalesEfectivo.alertDialog.dismiss();
                String titulo = "IMPRIMIR";
                String mensajes = "¿Desea finalizar la venta?";
                Modales modalesImprime = new Modales(FormasPago.this);
                View viewLectura = modalesImprime.MostrarDialogoAlerta(FormasPago.this, mensajes,  "FINALIZAR", "IMPRIMIR");
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
                list.setEnabled(true);
            }
        });
    }

    private void MuestraFormaEfectivoDolaresPuntada(){
        MontoCanasta = totalDolares;
        String tituloEfectivo = "USD";
        Modales modalesEfectivo = new Modales(FormasPago.this);
        View viewVenta = modalesEfectivo.MostrarDialogoEfectivoDolares(FormasPago.this, String.valueOf(totalDolares),  String.valueOf(tipoCambio), tituloEfectivo);
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
                intent.putExtra("formapagoid", numpago1);
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
                list.setEnabled(true);
            }
        });
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
                            Modales modales = new Modales(FormasPago.this);
                            View view1 = modales.MostrarDialogoError(FormasPago.this, mensaje);
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
                        Modales modales = new Modales(FormasPago.this);
                        View view1 = modales.MostrarDialogoCorrecto(FormasPago.this, "Ticket enviado a impresora central");
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
                Toast.makeText(FormasPago.this, "error", Toast.LENGTH_SHORT).show();

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
                                        Modales modales = new Modales(FormasPago.this);
                                        View view1 = modales.MostrarDialogoAlertaAceptar(FormasPago.this, mensaje, titulo);
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
                                        final Modales modales = new Modales(FormasPago.this);
                                        View view1 = modales.MostrarDialogoCorrecto(FormasPago.this,mensajes);
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
}