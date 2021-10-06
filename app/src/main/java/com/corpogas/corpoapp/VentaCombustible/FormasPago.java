package com.corpogas.corpoapp.VentaCombustible;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.corpogas.corpoapp.Configuracion.SQLiteBD;
import com.corpogas.corpoapp.LecturaTarjetas.MonederosElectronicos;
import com.corpogas.corpoapp.Menu_Principal;
import com.corpogas.corpoapp.Modales.Modales;
import com.corpogas.corpoapp.R;
import com.corpogas.corpoapp.ValesPapel.ValesPapel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    String posiciondecargaid, sucursalnumeroempleado, claveProducto;

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
        sucursalnumeroempleado = getIntent().getStringExtra("numeroEmpleado");
        claveProducto = getIntent().getStringExtra("claveProducto");

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
                    subtitle.add("ID Forma de Pago:" + 0);
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
                    if (visible == "true") {
                        numerotickets.add(numero_ticket);
                        maintitle.add(nombre_pago);
                        subtitle.add("ID Forma de Pago:" + numero_pago);
                        IdFormaPago.add(numero_pago);
                        NumeroInternoFormaPago.add(internonumero);
                        colocarformapago = true;
                    }else{
                        if (EstacionJarreo.equals("true")){
                            if (numero_pago.equals("91")){ //JArreo
                                numerotickets.add(numero_ticket);
                                maintitle.add(nombre_pago);
                                subtitle.add("ID Forma de Pago:" + numero_pago); //numero_pago
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
                int pos = position + 1;
                formapago = NumeroInternoFormaPago.get(position);
                String formapagoid = IdFormaPago.get(position);//String.valueOf(pos);
                nombrepago = maintitle.get(position);
                String numticket = numerotickets.get(position);
                int numpago = Integer.parseInt(NumeroInternoFormaPago.get(position)); //IdFormaPago
                String numpago1 = IdFormaPago.get(position);
                String idoperativa = getIntent().getStringExtra("IdOperativa");
                String posicioncarga = getIntent().getStringExtra("posicioncarga");
                String idusuario = getIntent().getStringExtra("IdUsuario");
                String ClaveDespachador = getIntent().getStringExtra("clavedespachador");
//                int operativa = Integer.parseInt(idoperativa);

                switch (Integer.parseInt(formapago)) {
                    case 20: //                    Puntada Acumular
//                        ObtenerTicketPuntadaAcumular(posicioncarga, idusuario, numpago);
                        break;
                    case 3: // Ticket Nomal
                    case 1:
                        if (numpago == 0) { //DIFERENTES FORMAS DE PAGO
                            Intent intent = new Intent(getApplicationContext(), DiferentesFormasPago.class);//DiferentesFormasPagoPuntada
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
                        }
                        if (numpago == 1) { //2 EFECTIVO
                            String titulo = "PUNTADA";
                            String mensajes = "Desea Acumular la venta a su Tarjeta Puntada?";
                            Modales modales = new Modales(FormasPago.this);
                            View viewLectura = modales.MostrarDialogoAlerta(FormasPago.this, mensajes,  "SI", "NO");
                            viewLectura.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
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
                                    modales.alertDialog.dismiss();
                                }
                            });
                            viewLectura.findViewById(R.id.buttonNo).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
//                                    RespuestaImprimeFinaliza(posicioncarga, idusuario, formapagoid, numticket, nombrepago);
                                    modales.alertDialog.dismiss();
                                    SeleccionaPesosDoalares();
                                }
                            });

                        } else {
                            if (numpago == 2) {//3 VALES
                                Intent intent = new Intent(getApplicationContext(), ValesPapel.class);//DiferentesFormasPagoPuntada
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
                                    String titulo = "PUNTADA";
                                    String mensajes = "Desea Acumular la venta a su Tarjeta Puntada?";
                                    Modales modales = new Modales(FormasPago.this);
                                    View viewLectura = modales.MostrarDialogoAlerta(FormasPago.this, mensajes,  "SI", "NO");
                                    viewLectura.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
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
                                            modales.alertDialog.dismiss();
                                        }
                                    });
                                    viewLectura.findViewById(R.id.buttonNo).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
//                                            ObtenerDatosTicketTarjetas(posicioncarga, idusuario, formapagoid, numticket, "true");
                                            modales.alertDialog.dismiss();

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



    private void SeleccionaPesosDoalares(){
        String titulo = "PAGO EFECTIVO";
        String mensaje = "FORMA DE PAGO EN EFECTIVO";
        Modales modales = new Modales(FormasPago.this);
        View viewLectura = modales.MostrarDialogoAlerta(FormasPago.this, mensaje,  "DOLRES", "PESOS");
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
        String titulo = "RECIBI";
        String mensaje = "Ingresa el monto" ;
        Modales modalesEfectivo = new Modales(FormasPago.this);
        View viewLectura = modalesEfectivo.MostrarDialogoInsertaDato(FormasPago.this, mensaje, titulo);
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
                    Double cambio = Double.parseDouble(cantidad)-MontoCanasta;
                    String tituloEfectivo = "Venta";
                    Modales modales = new Modales(FormasPago.this);
                    View viewVenta = modales.MostrarDialogoEfectivo(FormasPago.this, String.valueOf(MontoCanasta), cantidad, cambio.toString(), tituloEfectivo);
                    viewVenta.findViewById(R.id.btnAceptarVales).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            modales.alertDialog.dismiss();
                            FinalizaVenta();
                        }
                    });
                    viewVenta.findViewById(R.id.btnCancelarVales).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            modales.alertDialog.dismiss();
                            list.setEnabled(true);

                        }
                    });
                }
                modalesEfectivo.alertDialog.dismiss();
            }
        });
    }

    private void FinalizaVenta() {
//        if (!Conexion.compruebaConexion(this)) {
//            Toast.makeText(getBaseContext(), "Sin conexión a la red ", Toast.LENGTH_SHORT).show();
//            Intent intent1 = new Intent(getApplicationContext(), Menu_Principal.class);
//            startActivity(intent1);
//            finish();
//        } else {
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
//    }
}