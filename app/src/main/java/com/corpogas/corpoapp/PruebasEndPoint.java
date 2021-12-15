package com.corpogas.corpoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.device.PrinterManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.corpogas.corpoapp.Configuracion.SQLiteBD;
import com.corpogas.corpoapp.Corte.IslasEstacion;
import com.corpogas.corpoapp.Entities.Accesos.AccesoUsuario;
import com.corpogas.corpoapp.Entities.Catalogos.Bin;
import com.corpogas.corpoapp.Entities.Classes.RespuestaApi;
import com.corpogas.corpoapp.Entities.Common.ProductoTarjetero;
import com.corpogas.corpoapp.Entities.Cortes.Cierre;
import com.corpogas.corpoapp.Entities.Cortes.LecturaManguera;
import com.corpogas.corpoapp.Entities.Estaciones.Combustible;
import com.corpogas.corpoapp.Entities.Estaciones.DiferenciaPermitida;
import com.corpogas.corpoapp.Entities.Estaciones.Empleado;
import com.corpogas.corpoapp.Entities.Estaciones.Isla;
import com.corpogas.corpoapp.Entities.Sucursales.BranchPaymentMethod;
import com.corpogas.corpoapp.Entities.Sucursales.PriceBankRoll;
import com.corpogas.corpoapp.Entities.Tarjetas.Puntada;
import com.corpogas.corpoapp.Entities.Tickets.DiccionarioParcialidades;
import com.corpogas.corpoapp.Entities.Tickets.Ticket;
import com.corpogas.corpoapp.Entities.Tickets.TicketRequest;
import com.corpogas.corpoapp.Entities.Ventas.Transaccion;
import com.corpogas.corpoapp.Interfaces.Endpoints.EndPoints;
import com.corpogas.corpoapp.Modales.Modales;
import com.corpogas.corpoapp.Puntada.PosicionPuntadaRedimir;
import com.corpogas.corpoapp.Service.PrintBillService;
import com.corpogas.corpoapp.Token.GlobalToken;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PruebasEndPoint extends AppCompatActivity {

    public final static String PRNT_ACTION = "action.printer.message";
//    private PrinterManager printer = new PrinterManager();
    private final static String STR_PRNT_SALE = "sale";


    SQLiteBD data;

    Button btnPeticionBin, btnPeticionAccesoUsuario, btnPeticionAcumulaPuntos, btnPeticionGeneraTicket, btnPeticionFormasPago,
           btnPeticionEmpleado, btnPeticionProductosProcedencia, btnPeticiongetPostFinalizaVenta,btnPeticionTicketPendienteCobro,
           btnPeticionAutorizaDespacho, btnPeticionPosicionCargaProductosSucursal, btnPeticionCombustiblesPorSucursalId,
           btnPeticionLecturaMangueraPorPosicion, btnPeticionDiferenciaPermitidaPorSucursal, mBtnPrnBill;

    long fajillaMorralla;

    RespuestaApi<Bin> respuestaApiBin;
    RespuestaApi<AccesoUsuario> accesoUsuario;
    RespuestaApi<Puntada> respuestaPuntada;
    RespuestaApi<Empleado> respuestaApiEmpleado;
//    Ticket<TicketRequest> respuestaTicketRequest;
    List<BranchPaymentMethod> respuestaListaSucursalFormasPago;
    RespuestaApi<List<ProductoTarjetero>> respuestaApiProductoTarjetero;
    RespuestaApi<Transaccion> respuestaApiTransaccion;
    RespuestaApi<Boolean> respuestaApiTicketPendienteCobro;
    RespuestaApi<Boolean> respuestaApiAutorizaDespacho;
    Isla respuestaApiPosicionCargaProductosSucursal;
    RespuestaApi<List<Combustible>> respuestaApiCombustiblesPorSucursalId;
    List<LecturaManguera> respuestaApiLecturaManguera;
    RespuestaApi<DiferenciaPermitida> respuestaApiDiferenciaPermitida;
    RespuestaApi<Cierre> respuestaApiCierreCabero;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pruebas_end_point);
        data = new SQLiteBD(getApplicationContext());

        btnPeticionBin = (Button) findViewById(R.id.btnPeticionBin);
        btnPeticionAccesoUsuario = (Button) findViewById(R.id.btnPeticionAccesoUsuario);
        btnPeticionAcumulaPuntos = (Button) findViewById(R.id.btnPeticionAcumulaPuntos);
        btnPeticionGeneraTicket = (Button) findViewById(R.id.btnPeticionGeneraTicket);
        btnPeticionFormasPago = (Button) findViewById(R.id.btnPeticionFormasPago);
        btnPeticionEmpleado = (Button) findViewById(R.id.btnPeticionEmpleado);
        btnPeticionProductosProcedencia = (Button) findViewById(R.id.btnPeticionProductosProcedencia);
        btnPeticiongetPostFinalizaVenta = (Button) findViewById(R.id.btnPeticiongetPostFinalizaVenta);
        btnPeticionTicketPendienteCobro = (Button) findViewById(R.id.btnPeticionTicketPendienteCobro);
        btnPeticionAutorizaDespacho = (Button) findViewById(R.id.btnPeticionAutorizaDespacho);
        btnPeticionPosicionCargaProductosSucursal = (Button) findViewById(R.id.btnPeticionPosicionCargaProductosSucursal);
        btnPeticionCombustiblesPorSucursalId = (Button) findViewById(R.id.btnPeticionCombustiblesPorSucursalId);
        btnPeticionLecturaMangueraPorPosicion = (Button) findViewById(R.id.btnPeticionLecturaMangueraPorPosicion);
        btnPeticionDiferenciaPermitidaPorSucursal = (Button) findViewById(R.id.btnPeticionDiferenciaPermitidaPorSucursal);
        mBtnPrnBill = (Button) findViewById(R.id.btnImprimir);
        btnPeticionBin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> pistas = new ArrayList<String>();
                pistas.add("400000025010000199997000");
                pistas.add("400000025010000199997000");
                pistas.add("");

                Bin bin = new Bin(pistas);
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://" + data.getIpEstacion() + "/CorpogasService_Entities/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                EndPoints obtenNumeroTarjetero = retrofit.create(EndPoints.class);
//                Call<RespuestaApi<Bin>> call = obtenNumeroTarjetero.getBin("497", bin);
//                call.enqueue(new Callback<RespuestaApi<Bin>>() {
//
//                    @Override
//                    public void onResponse(Call<RespuestaApi<Bin>> call, Response<RespuestaApi<Bin>> response) {
//
//                        if (!response.isSuccessful()) {
//                            return;
//                        }
//                        respuestaApiBin = response.body();
//
//                    }
//
//                    @Override
//                    public void onFailure(Call<RespuestaApi<Bin>> call, Throwable t) {
//                        Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
//
//                    }
//                });
            }
        });

        btnPeticionAccesoUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Retrofit retrofit = new Retrofit.Builder()
//                        .baseUrl("http://10.0.201.20/corpogasService_Entities_token/")//http://" + data.getIpEstacion() + "/corpogasService_Entities_token/
                        .baseUrl("http://10.0.1.40/CorpogasService_entities_token/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                EndPoints obtenerAccesoUsuario = retrofit.create(EndPoints.class);
//                Call<RespuestaApi<AccesoUsuario>> call = obtenerAccesoUsuario.getAccesoUsuario("497", "QwBvAG4AcwBvAGwAYQBDAG8AcgBwAG8AZwBhAHMA");
//                call.enqueue(new Callback<RespuestaApi<AccesoUsuario>>() {
//
//
//                    @Override
//                    public void onResponse(Call<RespuestaApi<AccesoUsuario>> call, Response<RespuestaApi<AccesoUsuario>> response) {
//                        if (!response.isSuccessful()) {
//                            return;
//                        }
//                        accesoUsuario = response.body();
//                        //bearerToken = accesoUsuario.getMensaje();
//                    }
//
//                    @Override
//                    public void onFailure(Call<RespuestaApi<AccesoUsuario>> call, Throwable t) {
//                        Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
//
//                    }
//                });
            }
        });

        btnPeticionAcumulaPuntos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                List<ProductoTarjetero> productos = new ArrayList<ProductoTarjetero>();
//                productos.add("400000025010000199997000");
//                productos.add("400000025010000199997000");
//                productos.add("");

                Puntada puntada = new Puntada(3, 497, 35, 2212, "4000050226500001", productos);

                Retrofit retrofit = new Retrofit.Builder()
//                        .baseUrl("http://" + data.getIpEstacion() + "/CorpogasService_Entities/")
                        .baseUrl("http://" +  data.getIpEstacion() + "/CorpogasService_entities_token/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                EndPoints acumularPuntosPuntada = retrofit.create(EndPoints.class);
                Call<RespuestaApi<Puntada>> call = acumularPuntosPuntada.getActualizaPuntos("2222", puntada, data.getToken());
                call.enqueue(new Callback<RespuestaApi<Puntada>>() {

                    @Override
                    public void onResponse(Call<RespuestaApi<Puntada>> call, Response<RespuestaApi<Puntada>> response) {
                        if (!response.isSuccessful()) {
                            if (response.code() == 401) {
                                GlobalToken.errorToken(PruebasEndPoint.this);
                            } else {
                                Toast.makeText(PruebasEndPoint.this, response.message(), Toast.LENGTH_SHORT).show();
                            }
                            return;
                        }
                        respuestaPuntada = response.body();

                    }

                    @Override
                    public void onFailure(Call<RespuestaApi<Puntada>> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

            }
        });

        btnPeticionGeneraTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                List<DiccionarioParcialidades> parcialidades = new ArrayList<DiccionarioParcialidades>();
//
//                TicketRequest ticketRequest = new TicketRequest(2, 497,100049486, parcialidades);
//                String json = new Gson().toJson(ticketRequest);
//
//                Retrofit retrofit = new Retrofit.Builder()
//                        .baseUrl("http://" + data.getIpEstacion() + "/CorpogasService/")
//                        .addConverterFactory(GsonConverterFactory.create())
//                        .build();
//
//                EndPoints generaTicket = retrofit.create(EndPoints.class);
//                Call<Ticket<TicketRequest>> call = generaTicket.getGenerarTicket(ticketRequest);
//                call.enqueue(new Callback<Ticket<TicketRequest>>() {
//
//
//                    @Override
//                    public void onResponse(Call<Ticket<TicketRequest>> call, Response<Ticket<TicketRequest>> response) {
//                        if (!response.isSuccessful()) {
//                            return;
//                        }
//                        respuestaTicketRequest = response.body();
//
//                    }
//
//                    @Override
//                    public void onFailure(Call<Ticket<TicketRequest>> call, Throwable t) {
//                        Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
//
//                    }
//                });
            }

        });

        btnPeticionFormasPago.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("http://" + db.getIpEstacion() + "/CorpogasService/")
                        .baseUrl("http://" + data.getIpEstacion() + "/CorpogasService_entities_token/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                EndPoints formasPago = retrofit.create(EndPoints.class);
                Call<List<BranchPaymentMethod>> call = formasPago.getFormaPagos(Long.parseLong(data.getIdSucursal()), data.getToken());

                call.enqueue(new Callback<List<BranchPaymentMethod>>() {


                    @Override
                    public void onResponse(Call<List<BranchPaymentMethod>> call, Response<List<BranchPaymentMethod>> response) {
                        if (!response.isSuccessful()) {
                            if (response.code() == 401) {
                                GlobalToken.errorToken(PruebasEndPoint.this);
                            } else {
                                Toast.makeText(PruebasEndPoint.this, response.message(), Toast.LENGTH_SHORT).show();
                            }
                            return;
                        }
                        respuestaListaSucursalFormasPago = response.body();

                    }

                    @Override
                    public void onFailure(Call<List<BranchPaymentMethod>> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        btnPeticionEmpleado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Retrofit retrofit = new Retrofit.Builder()
//                        .baseUrl("http://" + data.getIpEstacion() + "/CorpogasService_Entities/")
                        .baseUrl("http://" + data.getIpEstacion() + "/CorpogasService_entities_token/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                EndPoints datosEmpleado = retrofit.create(EndPoints.class);
                Call<RespuestaApi<Empleado>> call = datosEmpleado.getDatosEmpleado("1111", data.getToken());
                call.enqueue(new Callback<RespuestaApi<Empleado>>() {

                    @Override
                    public void onResponse(Call<RespuestaApi<Empleado>> call, Response<RespuestaApi<Empleado>> response) {
                        if (!response.isSuccessful()) {
                            if (response.code() == 401) {
                                GlobalToken.errorToken(PruebasEndPoint.this);
                            } else {
                                Toast.makeText(PruebasEndPoint.this, response.message(), Toast.LENGTH_SHORT).show();
                            }
                            return;
                        }
                        respuestaApiEmpleado = response.body();
                    }

                    @Override
                    public void onFailure(Call<RespuestaApi<Empleado>> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        btnPeticionProductosProcedencia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Retrofit retrofit = new Retrofit.Builder()
//                        .baseUrl("http://" + data.getIpEstacion() + "/CorpogasService_Entities/")
                        .baseUrl("http://" + data.getIpEstacion() + "/CorpogasService_entities_token/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                EndPoints productosProcedencia = retrofit.create(EndPoints.class);
                Call<RespuestaApi<List<ProductoTarjetero>>> call = productosProcedencia.getProductosProcedencia(497,  1, data.getToken());
                call.enqueue(new Callback<RespuestaApi<List<ProductoTarjetero>>>() {


                    @Override
                    public void onResponse(Call<RespuestaApi<List<ProductoTarjetero>>> call, Response<RespuestaApi<List<ProductoTarjetero>>> response) {
                        if (!response.isSuccessful()) {
                            if (response.code() == 401) {
                                GlobalToken.errorToken(PruebasEndPoint.this);
                            } else {
                                Toast.makeText(PruebasEndPoint.this, response.message(), Toast.LENGTH_SHORT).show();
                            }
                            return;
                        }
                        respuestaApiProductoTarjetero = response.body();

                    }

                    @Override
                    public void onFailure(Call<RespuestaApi<List<ProductoTarjetero>>> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

        });

        btnPeticiongetPostFinalizaVenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Retrofit retrofit = new Retrofit.Builder()
//                        .baseUrl("http://" + data.getIpEstacion() + "/CorpogasService_Entities/")
                        .baseUrl("http://" + data.getIpEstacion() + "/CorpogasService_entities_token/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                EndPoints postFinalizaVenta = retrofit.create(EndPoints.class);
                Call<RespuestaApi<Transaccion>> call = postFinalizaVenta.getPostFinalizaVenta(497,1,104, data.getToken());
                call.enqueue(new Callback<RespuestaApi<Transaccion>>() {


                    @Override
                    public void onResponse(Call<RespuestaApi<Transaccion>> call, Response<RespuestaApi<Transaccion>> response) {
                        if (!response.isSuccessful()) {
                            if (response.code() == 401) {
                                GlobalToken.errorToken(PruebasEndPoint.this);
                            } else {
                                Toast.makeText(PruebasEndPoint.this, response.message(), Toast.LENGTH_SHORT).show();
                            }
                            return;
                        }
                        respuestaApiTransaccion = response.body();
                    }

                    @Override
                    public void onFailure(Call<RespuestaApi<Transaccion>> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        btnPeticionTicketPendienteCobro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Retrofit retrofit = new Retrofit.Builder()
//                        .baseUrl("http://" + data.getIpEstacion() + "/CorpogasService_Entities/")
                        .baseUrl("http://" + data.getIpEstacion() + "/CorpogasService_entities_token/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                EndPoints TicketPendienteCobro = retrofit.create(EndPoints.class);
                Call<RespuestaApi<Boolean>> call = TicketPendienteCobro.getTicketPendienteCobro(497,1, data.getToken());
                call.enqueue(new Callback<RespuestaApi<Boolean>>() {


                    @Override
                    public void onResponse(Call<RespuestaApi<Boolean>> call, Response<RespuestaApi<Boolean>> response) {
                        if (!response.isSuccessful()) {
                            if (response.code() == 401) {
                                GlobalToken.errorToken(PruebasEndPoint.this);
                            } else {
                                Toast.makeText(PruebasEndPoint.this, response.message(), Toast.LENGTH_SHORT).show();
                            }
                            return;
                        }
                        respuestaApiTicketPendienteCobro = response.body();
                    }

                    @Override
                    public void onFailure(Call<RespuestaApi<Boolean>> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        btnPeticionAutorizaDespacho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Retrofit retrofit = new Retrofit.Builder()
//                        .baseUrl("http://" + data.getIpEstacion() + "/CorpogasService_Entities/")
                        .baseUrl("http://" + data.getIpEstacion() + "/CorpogasService_entities_token/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                EndPoints AutorizaDespacho = retrofit.create(EndPoints.class);
                Call<RespuestaApi<Boolean>> call = AutorizaDespacho.getAutorizaDespacho(1,100049486,
                        data.getToken()
                );
                call.enqueue(new Callback<RespuestaApi<Boolean>>() {


                    @Override
                    public void onResponse(Call<RespuestaApi<Boolean>> call, Response<RespuestaApi<Boolean>> response) {
                        if (!response.isSuccessful()) {
                            if (response.code() == 401) {
                                GlobalToken.errorToken(PruebasEndPoint.this);
                            } else {
                                Toast.makeText(PruebasEndPoint.this, response.message(), Toast.LENGTH_SHORT).show();
                            }
                            return;
                        }
                        respuestaApiAutorizaDespacho = response.body();
                    }

                    @Override
                    public void onFailure(Call<RespuestaApi<Boolean>> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        btnPeticionPosicionCargaProductosSucursal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Retrofit retrofit = new Retrofit.Builder()
//                        .baseUrl("http://" + data.getIpEstacion() + "/CorpogasService_Entities/")
                        .baseUrl("http://10.0.1.40/CorpogasService_entities_token/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                EndPoints PosicionCargaProductosSucursal = retrofit.create(EndPoints.class);
//                Call<Isla> call = PosicionCargaProductosSucursal.getPosicionCargaProductosSucursal("497",2211);
//                call.enqueue(new Callback<Isla>() {


////                    @Override
////                    public void onResponse(Call<Isla> call, Response<Isla> response) {
////                        if (!response.isSuccessful()) {
////                            return;
////                        }
////                        respuestaApiPosicionCargaProductosSucursal = response.body();
////                    }
////
////                    @Override
////                    public void onFailure(Call<Isla> call, Throwable t) {
////                        Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
////                    }
//                });


            }
        });

        btnPeticionCombustiblesPorSucursalId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Retrofit retrofit = new Retrofit.Builder()
//                        .baseUrl("http://" + data.getIpEstacion() + "/CorpogasService_Entities/")
                        .baseUrl("http://" + data.getIpEstacion() + "/CorpogasService_entities_token/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                EndPoints CombustiblesPorSucursalId = retrofit.create(EndPoints.class);
                Call<RespuestaApi<List<Combustible>>> call = CombustiblesPorSucursalId.getCombustiblesPorSucursalId(497,
                        data.getToken()
                );
                call.enqueue(new Callback<RespuestaApi<List<Combustible>>>() {


                    @Override
                    public void onResponse(Call<RespuestaApi<List<Combustible>>> call, Response<RespuestaApi<List<Combustible>>> response) {
                        if (!response.isSuccessful()) {
                            if (response.code() == 401) {
                                GlobalToken.errorToken(PruebasEndPoint.this);
                            } else {
                                Toast.makeText(PruebasEndPoint.this, response.message(), Toast.LENGTH_SHORT).show();
                            }
                            return;
                        }
                        respuestaApiCombustiblesPorSucursalId = response.body();
                    }

                    @Override
                    public void onFailure(Call<RespuestaApi<List<Combustible>>> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        btnPeticionLecturaMangueraPorPosicion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Retrofit retrofit = new Retrofit.Builder()
//                        .baseUrl("http://" + data.getIpEstacion() + "/CorpogasService_Entities/")
                        .baseUrl("http://" + data.getIpEstacion() + "/CorpogasService_entities_token/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                EndPoints LecturaMangueraPorPosicionCargaIdLecturaMecanica = retrofit.create(EndPoints.class);
                Call<List<LecturaManguera>> call = LecturaMangueraPorPosicionCargaIdLecturaMecanica.getLecturaMangueraPorPosicionCargaIdLecturaMecanica(497,768,2211,
                        data.getToken()
                );
                call.enqueue(new Callback<List<LecturaManguera>>() {


                    @Override
                    public void onResponse(Call<List<LecturaManguera>> call, Response<List<LecturaManguera>> response) {
                        if (!response.isSuccessful()) {
                            if (response.code() == 401) {
                                GlobalToken.errorToken(PruebasEndPoint.this);
                            } else {
                                Toast.makeText(PruebasEndPoint.this, response.message(), Toast.LENGTH_SHORT).show();
                            }
                            return;
                        }
                        respuestaApiLecturaManguera = response.body();
                    }

                    @Override
                    public void onFailure(Call<List<LecturaManguera>> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        btnPeticionDiferenciaPermitidaPorSucursal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Retrofit retrofit = new Retrofit.Builder()
//                        .baseUrl("http://" + data.getIpEstacion() + "/CorpogasService_Entities/")
                        .baseUrl("http://" + data.getIpEstacion() + "/CorpogasService_entities_token/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                EndPoints DiferenciaPermitidaPorSucursalId = retrofit.create(EndPoints.class);
                Call<RespuestaApi<DiferenciaPermitida>> call = DiferenciaPermitidaPorSucursalId.getDiferenciaPermitidaPorSucursalId(497,
                        data.getToken()
                );
                call.enqueue(new Callback<RespuestaApi<DiferenciaPermitida>>() {


                    @Override
                    public void onResponse(Call<RespuestaApi<DiferenciaPermitida>> call, Response<RespuestaApi<DiferenciaPermitida>> response) {
                        if (!response.isSuccessful()) {
                            if (response.code() == 401) {
                                GlobalToken.errorToken(PruebasEndPoint.this);
                            } else {
                                Toast.makeText(PruebasEndPoint.this, response.message(), Toast.LENGTH_SHORT).show();
                            }
                            return;
                        }
                        respuestaApiDiferenciaPermitida = response.body();
                    }

                    @Override
                    public void onFailure(Call<RespuestaApi<DiferenciaPermitida>> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

//        mBtnPrnBill.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                ObtenerCierreId();
////                Intent intent = new Intent(getApplicationContext(), ImprimirPruebas.class);//Lecturas
////
////                startActivity(intent);
//
//
////                mBtnPrnBill.setEnabled(false);
////                int ret = printer.prn_getStatus();
////                if (ret == 0) {
////                    doprintwork(STR_PRNT_SALE);
//////                        doprintwork("Sales un yisus");// print sale
////
////                } else {
////                    doprintwork(STR_PRNT_SALE);
//////                    Intent intent = new Intent(PRNT_ACTION);
//////                    intent.putExtra("ret", ret);
//////                    sendBroadcast(intent);
//////                }
////            }
//        });
    }


    public void ObtenerCierreId(){

        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("http://"+ data.getIpEstacion()  +"/corpogasService/")//http://" + data.getIpEstacion() + "/corpogasService_Entities_token/
                .baseUrl("http://" + data.getIpEstacion() + "/CorpogasService_entities_token/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        EndPoints obtenerCierreId = retrofit.create(EndPoints.class);
        Call<RespuestaApi<Cierre>> call = obtenerCierreId.getCrearCierre(497, 76, 10002026, data.getToken());
        call.enqueue(new Callback<RespuestaApi<Cierre>>() {


            @Override
            public void onResponse(Call<RespuestaApi<Cierre>> call, Response<RespuestaApi<Cierre>> response) {
                if (!response.isSuccessful()) {
                    if (response.code() == 401) {
                        GlobalToken.errorToken(PruebasEndPoint.this);
                    } else {
                        Toast.makeText(PruebasEndPoint.this, response.message(), Toast.LENGTH_SHORT).show();
                    }
                    return;
                }
                respuestaApiCierreCabero = response.body();
                long id = respuestaApiCierreCabero.getObjetoRespuesta().getId();
//                respuestaApiCierreCabero.getObjetoRespuesta().getVariables().getPrecioFajillas();
                for(PriceBankRoll item : respuestaApiCierreCabero.getObjetoRespuesta().getVariables().getPrecioFajillas())
                {
                    if(item.BankRollType == 2)

                        fajillaMorralla = item.getPrice();
//                    long bankRollType = item.BankRollType;
//                    long bankRollType2 = item.BankRollType.longValue();
//                    String nada = "";
                }
            }

            @Override
            public void onFailure(Call<RespuestaApi<Cierre>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });

    }


    void doprintwork(String msg) {
        Intent intentService = new Intent(this, PrintBillService.class);
        intentService.putExtra("SPRT", msg);
        startService(intentService);
    }


    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

//    @Override
//    protected void onPause() {
//        // TODO Auto-generated method stub
//        super.onPause();
//        mReadService.stop();
//    }
//
//    @Override
//    protected void onResume() {
//        // TODO Auto-generated method stub
//        super.onResume();
//        mReadService.start();
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuItem version = menu.add(0, 1, 0, R.string.menu_about).setIcon(android.R.drawable.ic_menu_info_details);;
        version.setShowAsAction(1);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        PackageManager pk = getPackageManager();
        PackageInfo pi;
        try {
            pi = pk.getPackageInfo(getPackageName(), 0);
            Toast.makeText(this, "V" +pi.versionName , Toast.LENGTH_SHORT).show();
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return super.onOptionsItemSelected(item);
    }
}
