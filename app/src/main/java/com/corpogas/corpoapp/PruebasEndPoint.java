package com.corpogas.corpoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.corpogas.corpoapp.Configuracion.SQLiteBD;
import com.corpogas.corpoapp.Entities.Accesos.AccesoUsuario;
import com.corpogas.corpoapp.Entities.Catalogos.Bin;
import com.corpogas.corpoapp.Entities.Classes.RespuestaApi;
import com.corpogas.corpoapp.Entities.Common.ProductoTarjetero;
import com.corpogas.corpoapp.Entities.Estaciones.Empleado;
import com.corpogas.corpoapp.Entities.HandHeld.ListaSucursalFormaPago;
import com.corpogas.corpoapp.Entities.Sistemas.ConfiguracionAplicacion;
import com.corpogas.corpoapp.Entities.Sucursales.BranchPaymentMethod;
import com.corpogas.corpoapp.Entities.Sucursales.Update;
import com.corpogas.corpoapp.Entities.Sucursales.UpdateDetail;
import com.corpogas.corpoapp.Entities.Tarjetas.Puntada;
import com.corpogas.corpoapp.Entities.Tickets.DiccionarioParcialidades;
import com.corpogas.corpoapp.Entities.Tickets.Ticket;
import com.corpogas.corpoapp.Entities.Tickets.TicketRequest;
import com.corpogas.corpoapp.Request.Interfaces.EndPoints;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PruebasEndPoint extends AppCompatActivity {

    SQLiteBD data;

    Button btnPeticionBin, btnPeticionAccesoUsuario, btnPeticionAcumulaPuntos, btnPeticionGeneraTicket, btnPeticionFormasPago, btnPeticionEmpleado, btnPeticionProductosProcedencia;

    RespuestaApi<Bin> respuestaApiBin;
    RespuestaApi<AccesoUsuario> accesoUsuario;
    RespuestaApi<Puntada> respuestaPuntada;
    RespuestaApi<Empleado> respuestaApiEmpleado;
    Ticket<TicketRequest> respuestaTicketRequest;
    List<BranchPaymentMethod> respuestaListaSucursalFormasPago;
    RespuestaApi<List<ProductoTarjetero>> respuestaApiProductoTarjetero;




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
                        .baseUrl("http://" + data.getIpEstacion() + "/CorpogasService_Entities/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                EndPoints obtenerAccesoUsuario = retrofit.create(EndPoints.class);
                Call<RespuestaApi<AccesoUsuario>> call = obtenerAccesoUsuario.getAccesoUsuario("497", "1111");
                call.enqueue(new Callback<RespuestaApi<AccesoUsuario>>() {


                    @Override
                    public void onResponse(Call<RespuestaApi<AccesoUsuario>> call, Response<RespuestaApi<AccesoUsuario>> response) {
                        if (!response.isSuccessful()) {
                            return;
                        }
                        accesoUsuario = response.body();
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
                        .baseUrl("http://" + data.getIpEstacion() + "/CorpogasService_Entities/")
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
                Call<RespuestaApi<List<ProductoTarjetero>>> call = productosProcedencia.getProductosProcedencia("497","1");
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
    }
}
