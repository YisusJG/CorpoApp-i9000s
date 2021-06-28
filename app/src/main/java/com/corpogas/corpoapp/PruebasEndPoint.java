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
import com.corpogas.corpoapp.Entities.Accesos.AccesoUsuario;
import com.corpogas.corpoapp.Entities.Catalogos.Bin;
import com.corpogas.corpoapp.Entities.Classes.RespuestaApi;
import com.corpogas.corpoapp.Entities.Common.ProductoTarjetero;
import com.corpogas.corpoapp.Entities.Cortes.LecturaManguera;
import com.corpogas.corpoapp.Entities.Estaciones.Combustible;
import com.corpogas.corpoapp.Entities.Estaciones.DiferenciaPermitida;
import com.corpogas.corpoapp.Entities.Estaciones.Empleado;
import com.corpogas.corpoapp.Entities.Estaciones.Isla;
import com.corpogas.corpoapp.Entities.Sucursales.BranchPaymentMethod;
import com.corpogas.corpoapp.Entities.Tarjetas.Puntada;
import com.corpogas.corpoapp.Entities.Tickets.DiccionarioParcialidades;
import com.corpogas.corpoapp.Entities.Tickets.Ticket;
import com.corpogas.corpoapp.Entities.Tickets.TicketRequest;
import com.corpogas.corpoapp.Entities.Ventas.Transaccion;
import com.corpogas.corpoapp.Request.Interfaces.EndPoints;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PruebasEndPoint extends AppCompatActivity {

    public final static String PRNT_ACTION = "action.printer.message";
    private PrinterManager printer = new PrinterManager();
    private static int _XVALUE = 384;
    private final static String STR_FONT_VALUE_SONG = "simsun";
    int height = 60;
    private static int _YVALUE = 24;
    private final int _YVALUE6 = 24;
    private final static String STR_PRNT_SALE = "sale";


    SQLiteBD data;

    Button btnPeticionBin, btnPeticionAccesoUsuario, btnPeticionAcumulaPuntos, btnPeticionGeneraTicket, btnPeticionFormasPago,
           btnPeticionEmpleado, btnPeticionProductosProcedencia, btnPeticiongetPostFinalizaVenta,btnPeticionTicketPendienteCobro,
           btnPeticionAutorizaDespacho, btnPeticionPosicionCargaProductosSucursal, btnPeticionCombustiblesPorSucursalId,
           btnPeticionLecturaMangueraPorPosicion, btnPeticionDiferenciaPermitidaPorSucursal, mBtnPrnBill;

    RespuestaApi<Bin> respuestaApiBin;
    RespuestaApi<AccesoUsuario> accesoUsuario;
    RespuestaApi<Puntada> respuestaPuntada;
    RespuestaApi<Empleado> respuestaApiEmpleado;
    Ticket<TicketRequest> respuestaTicketRequest;
    List<BranchPaymentMethod> respuestaListaSucursalFormasPago;
    RespuestaApi<List<ProductoTarjetero>> respuestaApiProductoTarjetero;
    RespuestaApi<Transaccion> respuestaApiTransaccion;
    RespuestaApi<Boolean> respuestaApiTicketPendienteCobro;
    RespuestaApi<Boolean> respuestaApiAutorizaDespacho;
    Isla respuestaApiPosicionCargaProductosSucursal;
    RespuestaApi<List<Combustible>> respuestaApiCombustiblesPorSucursalId;
    List<LecturaManguera> respuestaApiLecturaManguera;
    RespuestaApi<DiferenciaPermitida> respuestaApiDiferenciaPermitida;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pruebas_end_point);
        data = new SQLiteBD(getApplicationContext());

    String bearerToken;

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
                Call<RespuestaApi<Bin>> call = obtenNumeroTarjetero.getBin("497", bin);
                call.enqueue(new Callback<RespuestaApi<Bin>>() {

                    @Override
                    public void onResponse(Call<RespuestaApi<Bin>> call, Response<RespuestaApi<Bin>> response) {

                        if (!response.isSuccessful()) {
                            return;
                        }
                        respuestaApiBin = response.body();

                    }

                    @Override
                    public void onFailure(Call<RespuestaApi<Bin>> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });

        btnPeticionAccesoUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://10.0.201.20/corpogasService_Entities_token/")//http://" + data.getIpEstacion() + "/corpogasService_Entities_token/
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                EndPoints obtenerAccesoUsuario = retrofit.create(EndPoints.class);
                Call<RespuestaApi<AccesoUsuario>> call = obtenerAccesoUsuario.getAccesoUsuario("497", "QwBvAG4AcwBvAGwAYQBDAG8AcgBwAG8AZwBhAHMA");
                call.enqueue(new Callback<RespuestaApi<AccesoUsuario>>() {


                    @Override
                    public void onResponse(Call<RespuestaApi<AccesoUsuario>> call, Response<RespuestaApi<AccesoUsuario>> response) {
                        if (!response.isSuccessful()) {
                            return;
                        }
                        accesoUsuario = response.body();
                        //bearerToken = accesoUsuario.getMensaje();
                    }

                    @Override
                    public void onFailure(Call<RespuestaApi<AccesoUsuario>> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
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
                        .baseUrl("http://" + data.getIpEstacion() + "/CorpogasService_Entities/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                EndPoints acumularPuntosPuntada = retrofit.create(EndPoints.class);
                Call<RespuestaApi<Puntada>> call = acumularPuntosPuntada.getActualizaPuntos("2222", puntada);
                call.enqueue(new Callback<RespuestaApi<Puntada>>() {

                    @Override
                    public void onResponse(Call<RespuestaApi<Puntada>> call, Response<RespuestaApi<Puntada>> response) {
                        if (!response.isSuccessful()) {
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

                List<DiccionarioParcialidades> parcialidades = new ArrayList<DiccionarioParcialidades>();

                TicketRequest ticketRequest = new TicketRequest(2, "100049486", 497, parcialidades);

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://" + data.getIpEstacion() + "/CorpogasService_Entities/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                EndPoints generaTicket = retrofit.create(EndPoints.class);
                Call<Ticket<TicketRequest>> call = generaTicket.getGenerarTicket(ticketRequest);
                call.enqueue(new Callback<Ticket<TicketRequest>>() {


                    @Override
                    public void onResponse(Call<Ticket<TicketRequest>> call, Response<Ticket<TicketRequest>> response) {
                        if (!response.isSuccessful()) {
                            return;
                        }
                        respuestaTicketRequest = response.body();

                    }

                    @Override
                    public void onFailure(Call<Ticket<TicketRequest>> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
            }

        });

        btnPeticionFormasPago.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://10.0.201.20/corpogasService_Entities_token/")//http://" + data.getIpEstacion() + "/CorpogasService_Entities/
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                EndPoints formasPago = retrofit.create(EndPoints.class);
                Call<List<BranchPaymentMethod>> call = formasPago.getFormaPagos("497");

                call.enqueue(new Callback<List<BranchPaymentMethod>>() {


                    @Override
                    public void onResponse(Call<List<BranchPaymentMethod>> call, Response<List<BranchPaymentMethod>> response) {
                        if (!response.isSuccessful()) {
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
                        .baseUrl("http://" + data.getIpEstacion() + "/CorpogasService_Entities/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                EndPoints datosEmpleado = retrofit.create(EndPoints.class);
                Call<RespuestaApi<Empleado>> call = datosEmpleado.getDatosEmpleado("1111");
                call.enqueue(new Callback<RespuestaApi<Empleado>>() {

                    @Override
                    public void onResponse(Call<RespuestaApi<Empleado>> call, Response<RespuestaApi<Empleado>> response) {
                        if (!response.isSuccessful()) {
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
                        .baseUrl("http://" + data.getIpEstacion() + "/CorpogasService_Entities/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                EndPoints productosProcedencia = retrofit.create(EndPoints.class);
                Call<RespuestaApi<List<ProductoTarjetero>>> call = productosProcedencia.getProductosProcedencia("497",  1);
                call.enqueue(new Callback<RespuestaApi<List<ProductoTarjetero>>>() {


                    @Override
                    public void onResponse(Call<RespuestaApi<List<ProductoTarjetero>>> call, Response<RespuestaApi<List<ProductoTarjetero>>> response) {
                        if (!response.isSuccessful()) {
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
                        .baseUrl("http://" + data.getIpEstacion() + "/CorpogasService_Entities/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                EndPoints postFinalizaVenta = retrofit.create(EndPoints.class);
                Call<RespuestaApi<Transaccion>> call = postFinalizaVenta.getPostFinalizaVenta("497",1,"104");
                call.enqueue(new Callback<RespuestaApi<Transaccion>>() {


                    @Override
                    public void onResponse(Call<RespuestaApi<Transaccion>> call, Response<RespuestaApi<Transaccion>> response) {
                        if (!response.isSuccessful()) {
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
                        .baseUrl("http://" + data.getIpEstacion() + "/CorpogasService_Entities/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                EndPoints TicketPendienteCobro = retrofit.create(EndPoints.class);
                Call<RespuestaApi<Boolean>> call = TicketPendienteCobro.getTicketPendienteCobro("497",1);
                call.enqueue(new Callback<RespuestaApi<Boolean>>() {


                    @Override
                    public void onResponse(Call<RespuestaApi<Boolean>> call, Response<RespuestaApi<Boolean>> response) {
                        if (!response.isSuccessful()) {
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
                        .baseUrl("http://" + data.getIpEstacion() + "/CorpogasService_Entities/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                EndPoints AutorizaDespacho = retrofit.create(EndPoints.class);
                Call<RespuestaApi<Boolean>> call = AutorizaDespacho.getAutorizaDespacho(1,"100049486");
                call.enqueue(new Callback<RespuestaApi<Boolean>>() {


                    @Override
                    public void onResponse(Call<RespuestaApi<Boolean>> call, Response<RespuestaApi<Boolean>> response) {
                        if (!response.isSuccessful()) {
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
                        .baseUrl("http://" + data.getIpEstacion() + "/CorpogasService_Entities/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                EndPoints PosicionCargaProductosSucursal = retrofit.create(EndPoints.class);
                Call<Isla> call = PosicionCargaProductosSucursal.getPosicionCargaProductosSucursal("497","2211");
                call.enqueue(new Callback<Isla>() {


                    @Override
                    public void onResponse(Call<Isla> call, Response<Isla> response) {
                        if (!response.isSuccessful()) {
                            return;
                        }
                        respuestaApiPosicionCargaProductosSucursal = response.body();
                    }

                    @Override
                    public void onFailure(Call<Isla> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });

        btnPeticionCombustiblesPorSucursalId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://" + data.getIpEstacion() + "/CorpogasService_Entities/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                EndPoints CombustiblesPorSucursalId = retrofit.create(EndPoints.class);
                Call<RespuestaApi<List<Combustible>>> call = CombustiblesPorSucursalId.getCombustiblesPorSucursalId("497");
                call.enqueue(new Callback<RespuestaApi<List<Combustible>>>() {


                    @Override
                    public void onResponse(Call<RespuestaApi<List<Combustible>>> call, Response<RespuestaApi<List<Combustible>>> response) {
                        if (!response.isSuccessful()) {
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
                        .baseUrl("http://" + data.getIpEstacion() + "/CorpogasService_Entities/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                EndPoints LecturaMangueraPorPosicionCargaIdLecturaMecanica = retrofit.create(EndPoints.class);
                Call<List<LecturaManguera>> call = LecturaMangueraPorPosicionCargaIdLecturaMecanica.getLecturaMangueraPorPosicionCargaIdLecturaMecanica(497,768,2211);
                call.enqueue(new Callback<List<LecturaManguera>>() {


                    @Override
                    public void onResponse(Call<List<LecturaManguera>> call, Response<List<LecturaManguera>> response) {
                        if (!response.isSuccessful()) {
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
                        .baseUrl("http://" + data.getIpEstacion() + "/CorpogasService_Entities/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                EndPoints DiferenciaPermitidaPorSucursalId = retrofit.create(EndPoints.class);
                Call<RespuestaApi<DiferenciaPermitida>> call = DiferenciaPermitidaPorSucursalId.getDiferenciaPermitidaPorSucursalId(497);
                call.enqueue(new Callback<RespuestaApi<DiferenciaPermitida>>() {


                    @Override
                    public void onResponse(Call<RespuestaApi<DiferenciaPermitida>> call, Response<RespuestaApi<DiferenciaPermitida>> response) {
                        if (!response.isSuccessful()) {
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

        mBtnPrnBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), ImprimirPruebas.class);//Lecturas
//
//                startActivity(intent);

                String messgae ="Pruebas yisus";
//                mBtnPrnBill.setEnabled(false);
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
            }
        });
    }


    void doprintwork(String msg) {

//        printer.prn_open();
//        printer.prn_setupPage(_XVALUE, -1);
//        printer.prn_clearPage();
//        printer.prn_drawText(("打印机测试"), 70, 50, (STR_FONT_VALUE_SONG), 48 , false, false, 0);
//        height += 50;
//
//        BitmapFactory.Options opts = new BitmapFactory.Options();
//        opts.inPreferredConfig = Bitmap.Config.ARGB_8888;
//        opts.inDensity = getResources().getDisplayMetrics().densityDpi;
//        opts.inTargetDensity = getResources().getDisplayMetrics().densityDpi;
//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.unionpay_logo, opts);
//        printer.prn_drawBitmap(bitmap, 84, height);
//        height += 80;
//        Prn_Str("商户名称：测试商户", _YVALUE6, height);

        Intent intentService = new Intent(this, PrintBillService.class);
        intentService.putExtra("SPRT", msg);
        startService(intentService);
    }
    private int Prn_Str(String msg, int fontSize, int height) {
        return printer.prn_drawText(msg, 0, height, STR_FONT_VALUE_SONG, fontSize, false,
                false, 0);
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
