package com.corpogas.corpoapp.FormasPago;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import android.device.PrinterManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.corpogas.corpoapp.Adapters.RVAdapter;
import com.corpogas.corpoapp.Configuracion.SQLiteBD;
import com.corpogas.corpoapp.Corte.ProcesoCorte;
import com.corpogas.corpoapp.Entities.Classes.RecyclerViewHeaders;
import com.corpogas.corpoapp.Entities.Classes.RespuestaApi;
import com.corpogas.corpoapp.Entities.Sucursales.BranchPaymentMethod;
import com.corpogas.corpoapp.Entities.Tickets.DiccionarioParcialidades;
import com.corpogas.corpoapp.Entities.Tickets.Ticket;
import com.corpogas.corpoapp.Entities.Tickets.TicketRequest;
import com.corpogas.corpoapp.Entities.Ventas.Transaccion;
import com.corpogas.corpoapp.Interfaces.Endpoints.EndPoints;
import com.corpogas.corpoapp.LecturaTarjetas.MonederosElectronicos;
import com.corpogas.corpoapp.Menu_Principal;
import com.corpogas.corpoapp.Modales.Modales;
import com.corpogas.corpoapp.R;

import com.corpogas.corpoapp.Service.PrintBillService;
import com.corpogas.corpoapp.VentaCombustible.ProcesoVenta;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FormasDePago extends AppCompatActivity {
    RecyclerView rcvFormasPago;
    List<RecyclerViewHeaders> lFormasPago;
    SQLiteBD db;
    String ipEstacion;
    double totalCarrito;
    long posicioncarga,usuarioid,sucursalId;
    List<BranchPaymentMethod> respuestaListaSucursalFormasPago;
    RespuestaApi<Transaccion> respuestaApiTransaccion;
    Ticket respuestaTicketRequest;
    private PrinterManager printer = new PrinterManager();
    private final static String STR_PRNT_SALE = "sale";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formas_de_pago);
        init();
        rcvFormasPago = findViewById(R.id.rcvFormasPago);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvFormasPago.setLayoutManager(linearLayoutManager);
        rcvFormasPago.setHasFixedSize(true);
        obtenerformasdepago();


    }


    private void init() {
        db = new SQLiteBD(getApplicationContext());
        rcvFormasPago = findViewById(R.id.rcvFormasPago);

        this.setTitle(db.getRazonSocial());
        this.setTitle(db.getNumeroEstacion() + " ( EST.:" + db.getNumeroEstacion() + ")");
        sucursalId = Long.parseLong(db.getIdSucursal());
        ipEstacion = db.getIpEstacion();
        posicioncarga = getIntent().getLongExtra("posicioncarga",0);
        usuarioid = getIntent().getLongExtra("IdUsuario",0);
        totalCarrito = getIntent().getDoubleExtra("totalCarrito",0);



    }

    private void obtenerformasdepago() {

           Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://"+ipEstacion+"/CorpogasService/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        EndPoints formasPago = retrofit.create(EndPoints.class);
        Call<List<BranchPaymentMethod>> call = formasPago.getFormaPagos(sucursalId);

        call.enqueue(new Callback<List<BranchPaymentMethod>>() {

            @Override
            public void onResponse(Call<List<BranchPaymentMethod>> call, Response<List<BranchPaymentMethod>> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                respuestaListaSucursalFormasPago = response.body();
                initializeDataFormasDePago();
                initializeAdapterFormasDePago();
            }

            @Override
            public void onFailure(Call<List<BranchPaymentMethod>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void initializeDataFormasDePago() {
        lFormasPago = new ArrayList<>();
        int idPago;
        for(BranchPaymentMethod item : respuestaListaSucursalFormasPago)
        {
            idPago =item.getPaymentMethod().getId();
            switch (idPago) {
                case 0:
                    lFormasPago.add(new RecyclerViewHeaders("VARIAS FORMAS DE PAGO",item.getPaymentMethod().getId(),R.drawable.variasformaspago));
                    break;
                case 1: //Efectivo
                    lFormasPago.add(new RecyclerViewHeaders(item.getPaymentMethod().getLongDescription(),item.getPaymentMethod().getId(),R.drawable.billete));
                    break;
                case 2: //Vales
                    lFormasPago.add(new RecyclerViewHeaders(item.getPaymentMethod().getLongDescription(),item.getPaymentMethod().getId(),R.drawable.vale));
                    break;
                case 3: //AMEX
                    lFormasPago.add(new RecyclerViewHeaders(item.getPaymentMethod().getLongDescription(),item.getPaymentMethod().getId(),R.drawable.american));
                    break;
                case 5: //VISA/MC
                    lFormasPago.add(new RecyclerViewHeaders(item.getPaymentMethod().getLongDescription(),item.getPaymentMethod().getId(),R.drawable.visa));
                    break;
                case 6: // VALE/ELEC
                    lFormasPago.add(new RecyclerViewHeaders(item.getPaymentMethod().getLongDescription(),item.getPaymentMethod().getId(),R.drawable.valeelectronico));
                    break;
                case 13: //GASCARD
                    lFormasPago.add(new RecyclerViewHeaders(item.getPaymentMethod().getLongDescription(),item.getPaymentMethod().getId(),R.drawable.gascard));
                    break;
                case 14: //MERCADO PAGO
                    lFormasPago.add(new RecyclerViewHeaders(item.getPaymentMethod().getLongDescription(),item.getPaymentMethod().getId(),R.drawable.gascard));
                    break;
                default:

            }

//            lFormasPago.add(new RecyclerViewHeaders(item.getPaymentMethod().getLongDescription(),"", idPago == 0? R.drawable.variasformaspago
//                    :idPago == 2 ? R.drawable.vale :idPago == 3 ? R.drawable.american :idPago == 5 ? R.drawable.visa
//                    :idPago == 6 ? R.drawable.valeelectronico :idPago == 13 ? R.drawable.gascard :R.drawable.gascard));
        }


    }

    private void initializeAdapterFormasDePago() {
        RVAdapter adapter = new RVAdapter(lFormasPago);
        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String titulo = lFormasPago.get(rcvFormasPago.getChildAdapterPosition(v)).getTitulo();
                Toast.makeText(getApplicationContext(),"Seleccion :" + lFormasPago.get(rcvFormasPago.getChildAdapterPosition(v)).getTitulo(), Toast.LENGTH_SHORT).show();
                long formapagoid = lFormasPago.get(rcvFormasPago.getChildAdapterPosition(v)).getFormaPagoId();
                if(titulo.equals("EFECTIVO"))
                {

                    String mensajes = "Desea Acumular la venta a su Tarjeta Puntada?";
                    Modales modales = new Modales(FormasDePago.this);
                    View viewLectura = modales.MostrarDialogoAlerta(FormasDePago.this, mensajes,  "SI", "NO");
                    viewLectura.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
//                            String banderaHuella = getIntent().getStringExtra( "banderaHuella");
                            String nombreCompletoVenta = getIntent().getStringExtra("nombrecompleto");
                            //LeeTarjeta();
                            Intent intent = new Intent(getApplicationContext(), MonederosElectronicos.class);
                            //intent.putExtra("device_name", m_deviceName);
//                            intent.putExtra("banderaHuella", banderaHuella);
                            intent.putExtra("Enviadodesde", "formaspago");
//                            intent.putExtra("idusuario", idusuario);
//                            intent.putExtra("posicioncarga", posicioncarga);
//                            intent.putExtra("claveusuario", ClaveDespachador);
//                            intent.putExtra("idoperativa", idoperativa);
//                            intent.putExtra("formapagoid", numpago1);
//                            intent.putExtra("NombrePago", nombrepago);
//                            intent.putExtra("NombreCompleto", nombreCompletoVenta);
//                            intent.putExtra("montoenlacanasta", MontoCanasta);
//                            intent.putExtra("posicioncargaid", posiciondecargaid);
                            startActivity(intent);
                            modales.alertDialog.dismiss();
                        }
                    });
                    viewLectura.findViewById(R.id.buttonNo).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            respuestaImprimeFinaliza(posicioncarga, usuarioid, formapagoid);
                            modales.alertDialog.dismiss();
                        }
                    });
                }

            }
        });

        rcvFormasPago.setAdapter(adapter);
    }

    private void respuestaImprimeFinaliza(long posicioncarga, long usuarioid, long formapagoid) {

        String mensajes = "Desea finalizar la venta?";
        Modales modales = new Modales(FormasDePago.this);
        View viewLectura = modales.MostrarDialogoAlerta(FormasDePago.this, mensajes,  "FINALIZAR", "IMPRIMIR");
        viewLectura.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modales.alertDialog.dismiss();
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://" + ipEstacion + "/CorpogasService/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                EndPoints postFinalizaVenta = retrofit.create(EndPoints.class);
                Call<RespuestaApi<Transaccion>> call = postFinalizaVenta.getPostFinalizaVenta(sucursalId,posicioncarga,usuarioid);
                call.enqueue(new Callback<RespuestaApi<Transaccion>>() {


                    @Override
                    public void onResponse(Call<RespuestaApi<Transaccion>> call, Response<RespuestaApi<Transaccion>> response) {
                        if (!response.isSuccessful()) {
                            return;
                        }
                        respuestaApiTransaccion = response.body();
                        String mensaje = respuestaApiTransaccion.getMensaje();   //respuesta.getString("Mensaje");
//                Transaccion objetoRespuesta = respuestaApiTransaccion.getObjetoRespuesta(); //respuesta.getString("ObjetoRespuesta");
                        if(respuestaApiTransaccion.getObjetoRespuesta() == null)
                        {
                            String titulo = "AVISO";
                            //String mensaje = "No hay Posiciones de Carga para Finalizar Venta";
                            Modales modales = new Modales(FormasDePago.this);
                            View view1 = modales.MostrarDialogoAlertaAceptar(FormasDePago.this,mensaje,titulo);
                            view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    modales.alertDialog.dismiss();
                                    Intent intent1 = new Intent(getApplicationContext(), Menu_Principal.class);
                                    startActivity(intent1);
                                    finish();
                                }
                            });

                        }else
                        {
                            String titulo = "AVISO";
                            String mensajes = "Venta Finalizada";
                            final Modales modales = new Modales(FormasDePago.this);
                            View view1 = modales.MostrarDialogoCorrecto(FormasDePago.this,mensajes);
                            view1.findViewById(R.id.buttonAction).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    modales.alertDialog.dismiss();
                                    Intent intent = new Intent(getApplicationContext(), Menu_Principal.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });

                        }

                    }

                    @Override
                    public void onFailure(Call<RespuestaApi<Transaccion>> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        viewLectura.findViewById(R.id.buttonNo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modales.alertDialog.dismiss();
                ObtenerDatosticketnormal(posicioncarga, String.valueOf(usuarioid), formapagoid);
            }
        });
    }

    private void ObtenerDatosticketnormal(long posicioncarga, String usuarioid, long formapagoid) {
        List<DiccionarioParcialidades> parcialidades = new ArrayList<DiccionarioParcialidades>();
        parcialidades.add(new DiccionarioParcialidades(formapagoid,totalCarrito));
        TicketRequest ticketRequest = new TicketRequest(posicioncarga,sucursalId, usuarioid, parcialidades); //
        String json = new Gson().toJson(ticketRequest);

        try {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://" + ipEstacion + "/CorpogasService/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();


            EndPoints generaTicket = retrofit.create(EndPoints.class);
            Call<Ticket> call = generaTicket.getGenerarTicket(ticketRequest);
            call.enqueue(new Callback<Ticket>() {


                @Override
                public void onResponse(Call<Ticket> call, Response<Ticket> response) {
                    if (!response.isSuccessful()) {
                        return;
                    }
                    respuestaTicketRequest = response.body();
                    int ret = printer.prn_getStatus();
                    if (ret == 0) {
                        doprintwork(STR_PRNT_SALE);
//                        doprintwork("Sales un yisus");// print sale

                    } else {
                        doprintwork(STR_PRNT_SALE);
//                    Intent intent = new Intent(PRNT_ACTION);
//                    intent.putExtra("ret", ret);
//                    sendBroadcast(intent);
                    }
//                    Toast.makeText(getApplicationContext(), "Mandar a imprimir", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<Ticket> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                }


            });

        }catch (Exception e)
        {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }

    void doprintwork(String msg) {
        Intent intentService = new Intent(this, PrintBillService.class);
        intentService.putExtra("SPRT", msg);
        intentService.putExtra("ticketEfectivo",respuestaTicketRequest);
        startService(intentService);
    }

}