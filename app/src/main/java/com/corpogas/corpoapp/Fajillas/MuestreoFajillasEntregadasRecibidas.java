package com.corpogas.corpoapp.Fajillas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.corpogas.corpoapp.Adapters.RVAdapter;
import com.corpogas.corpoapp.Adapters.RVAdapterFajillas;
import com.corpogas.corpoapp.Conexion;
import com.corpogas.corpoapp.Configuracion.SQLiteBD;
import com.corpogas.corpoapp.Entities.Classes.RespuestaApi;
import com.corpogas.corpoapp.Entities.Estaciones.RecepcionFajillaEntregada;
import com.corpogas.corpoapp.Interfaces.Endpoints.EndPoints;
import com.corpogas.corpoapp.Menu_Principal;
import com.corpogas.corpoapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MuestreoFajillasEntregadasRecibidas extends AppCompatActivity {
    RecyclerView rcvFajillasEntregadas;
    SQLiteBD data;
    long usuarioid,sucursalId, numeroEmpleado;
    String ipEstacion, m_deviceName, tipoFajillaDescripcion, autorizoFajillas, fechaFajillas, nombreEmpleado, totaldeFajillas, montoTotalFajillas;
    Double  preciodelaFajilla, fajillasMontoTotal =0.0;
    List<RecyclerViewHeadersFajillas> lFajillasEntregadas;
    Boolean banderaFajillas;
    TextView tvNombreFajillas, tvFecha, tvFajillasEntregadas;
    Button btnAceptarFajilla;
    DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
    DecimalFormat df;

    RespuestaApi<RecepcionFajillaEntregada> respuestaApiObtenerFajillasRecepcion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_muestreo_fajillas_entregadas_recibidas);
        init();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvFajillasEntregadas.setLayoutManager(linearLayoutManager);
        rcvFajillasEntregadas.setHasFixedSize(true);

        cargaFajillas();

    }


    private void init() {
        simbolos.setDecimalSeparator('.');
        df = new DecimalFormat("#,###.00##",simbolos);
        df.setMaximumFractionDigits(2);

        rcvFajillasEntregadas = (RecyclerView)findViewById(R.id.rcvFajillasEntregadas);
        data = new SQLiteBD(getApplicationContext());
        this.setTitle(data.getNombreEstacion() + " ( EST.:" + data.getNumeroEstacion() + ")");
        usuarioid = getIntent().getLongExtra("IdUsuario",0);
        ipEstacion = data.getIpEstacion();
        sucursalId = Long.parseLong(data.getIdSucursal());
        tvNombreFajillas = (TextView) findViewById(R.id.tvNombreFajillas);
        tvFecha = (TextView) findViewById(R.id.tvFecha);
        btnAceptarFajilla = (Button) findViewById(R.id.btnAceptarFajilla);
        btnAceptarFajilla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getApplicationContext(), Menu_Principal.class);
                //Se envian los parametros de posicion y usuario
//                intent1.putExtra("device_name", m_deviceName);
                //inicia el activity
                startActivity(intent1);
                finish();
            }
        });

        tvFajillasEntregadas = (TextView) findViewById(R.id.tvFajillasEntregadas);
        numeroEmpleado = Long.parseLong(data.getNumeroEmpleado()); //getIntent().getLongExtra("numeromepleado", 0);
        nombreEmpleado = data.getNombreCompleto();//getIntent().getStringExtra("nombrecompleto");
//        m_deviceName = getIntent().getStringExtra("device_name");

    }

    private void cargaFajillas(){ //Retro
        lFajillasEntregadas = new ArrayList<>();
        banderaFajillas= false;
        if (!Conexion.compruebaConexion(this)) {
            Toast.makeText(getBaseContext(), "Sin conexión a la red ", Toast.LENGTH_SHORT).show();
            Intent intent1 = new Intent(getApplicationContext(), Menu_Principal.class);

            startActivity(intent1);
            finish();
        } else {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://" + ipEstacion + "/corpogasService/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            EndPoints obtenerFajillas = retrofit.create(EndPoints.class);
            Call<RespuestaApi<RecepcionFajillaEntregada>> call = obtenerFajillas.getFajillas(sucursalId, numeroEmpleado);
            call.enqueue(new Callback<RespuestaApi<RecepcionFajillaEntregada>>() {
                @Override
                public void onResponse(Call<RespuestaApi<RecepcionFajillaEntregada>> call, retrofit2.Response<RespuestaApi<RecepcionFajillaEntregada>> response) {

                    if (!response.isSuccessful()) {
                        return;
                    }
                    respuestaApiObtenerFajillasRecepcion = response.body();

                    Boolean correcto = respuestaApiObtenerFajillasRecepcion.isCorrecto();
                    String mensaje = respuestaApiObtenerFajillasRecepcion.Mensaje;
                    if (correcto.equals(true) ) {
                        fechaFajillas = respuestaApiObtenerFajillasRecepcion.getObjetoRespuesta().getFechaEntrega();
                        for (int i = 0; i < respuestaApiObtenerFajillasRecepcion.getObjetoRespuesta().getFajillasEntregadas().size(); i++){
                            tipoFajillaDescripcion = respuestaApiObtenerFajillasRecepcion.getObjetoRespuesta().getFajillasEntregadas().get(i).getTipoFajilla();
                            montoTotalFajillas = String.valueOf(df.format(respuestaApiObtenerFajillasRecepcion.getObjetoRespuesta().getFajillasEntregadas().get(i).getImporteTotal()));
                            totaldeFajillas = String.valueOf(respuestaApiObtenerFajillasRecepcion.getObjetoRespuesta().getFajillasEntregadas().get(i).getCantidad());
                            autorizoFajillas = " A: " + respuestaApiObtenerFajillasRecepcion.getObjetoRespuesta().getFajillasEntregadas().get(i).getNombreAutoriza();

                            preciodelaFajilla = Double.parseDouble(String.valueOf(respuestaApiObtenerFajillasRecepcion.getObjetoRespuesta().getFajillasEntregadas().get(i).getPrecio()));
                            fajillasMontoTotal = fajillasMontoTotal + respuestaApiObtenerFajillasRecepcion.getObjetoRespuesta().getFajillasEntregadas().get(i).getImporteTotal();
                            lFajillasEntregadas.add(new RecyclerViewHeadersFajillas(tipoFajillaDescripcion, totaldeFajillas, "$"+montoTotalFajillas, autorizoFajillas, "ENTREGADA"));
                            banderaFajillas = true;
                        }

                        for (int i = 0; i < respuestaApiObtenerFajillasRecepcion.getObjetoRespuesta().getFajillasRecibidas().size(); i++){
                            tipoFajillaDescripcion = respuestaApiObtenerFajillasRecepcion.getObjetoRespuesta().getFajillasRecibidas().get(i).getTipoFajilla();
                            montoTotalFajillas = String.valueOf(df.format(respuestaApiObtenerFajillasRecepcion.getObjetoRespuesta().getFajillasRecibidas().get(i).getImporteTotal()));
                            totaldeFajillas = String.valueOf(respuestaApiObtenerFajillasRecepcion.getObjetoRespuesta().getFajillasRecibidas().get(i).getCantidad());
                            preciodelaFajilla = Double.parseDouble(String.valueOf(respuestaApiObtenerFajillasRecepcion.getObjetoRespuesta().getFajillasRecibidas().get(i).getPrecio()));
//                            autorizoFajillas = " A: " + respuestaApiObtenerFajillasRecepcion.getObjetoRespuesta().getFajillasRecibidas().get(i).getNombreAutoriza();
                            lFajillasEntregadas.add(new RecyclerViewHeadersFajillas(tipoFajillaDescripcion, totaldeFajillas, "$"+montoTotalFajillas, "", "RECIBIDA"));
                            banderaFajillas = true;
                        }
                        if (banderaFajillas==false){
                            Toast.makeText(MuestreoFajillasEntregadasRecibidas.this, "Sin datos para mostrar", Toast.LENGTH_SHORT).show();
                            Intent intent1 = new Intent(getApplicationContext(), Menu_Principal.class);
                            //Se envian los parametros de posicion y usuario
//                            intent1.putExtra("device_name", m_deviceName);
                            //inicia el activity
                            startActivity(intent1);
                            finish();
                        }else {
                            RVAdapterFajillas adapter = new RVAdapterFajillas(lFajillasEntregadas);
                            rcvFajillasEntregadas.setAdapter(adapter);
                            tvNombreFajillas.setText(nombreEmpleado);
                            tvFecha.setText("Fecha entrega: " + fechaFajillas);
                            tvFajillasEntregadas.setText("Total Fajillas Entregadas: $" + df.format(fajillasMontoTotal).toString());
                        }
                    }else{
                        Toast.makeText(MuestreoFajillasEntregadasRecibidas.this, "No hay Fajillas para mostrar", Toast.LENGTH_SHORT).show();
                        Intent intent1 = new Intent(getApplicationContext(), Menu_Principal.class);
                        //Se envian los parametros de posicion y usuario
                        intent1.putExtra("device_name", m_deviceName);
                        //inicia el activity
                        startActivity(intent1);
                        finish();
                    }
                }
                @Override
                public void onFailure(Call<RespuestaApi<RecepcionFajillaEntregada>> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private void cargaFajillasVoley(){//Voley
        lFajillasEntregadas = new ArrayList<>();
        banderaFajillas= false;
        if (!Conexion.compruebaConexion(this)) {
            Toast.makeText(getBaseContext(), "Sin conexión a la red ", Toast.LENGTH_SHORT).show();
            Intent intent1 = new Intent(getApplicationContext(), Menu_Principal.class);

            startActivity(intent1);
            finish();
        } else {
            String url;
            url = "http://" + ipEstacion + "/CorpogasService/api/recepcionFajillas/getFajillas/sucursalId/" + sucursalId + "/numeroEmpleado/" + numeroEmpleado;
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject respuesta = new JSONObject(response);
                        Boolean correcto = respuesta.getBoolean("Correcto");
                        String mensaje = respuesta.getString("Mensaje");
                        if (correcto.equals(true)){
                            String objetoRespuesta = respuesta.getString("ObjetoRespuesta");
                            JSONObject respuestaObjeto = new JSONObject(objetoRespuesta);
                            String fajillasEntregadas = respuestaObjeto.getString("FajillasEntregadas");
                            try {
                                JSONArray fajillasEntregasArreglo = new JSONArray(fajillasEntregadas);
                                for (int i = 0; i <fajillasEntregasArreglo.length() ; i++){
                                    JSONObject fajillasEntregadasObject = fajillasEntregasArreglo.getJSONObject(i);
                                    tipoFajillaDescripcion = fajillasEntregadasObject.getString("TipoFajilla");
//                                          Precio = fajillasEntregadasObject.getString("Precio");
                                    montoTotalFajillas = fajillasEntregadasObject.getString("ImporteTotal");
                                    totaldeFajillas = fajillasEntregadasObject.getString("Cantidad");
                                    autorizoFajillas = " A: " + fajillasEntregadasObject.getString("NombreAutoriza");
                                    fajillasMontoTotal = fajillasMontoTotal + Double.parseDouble(montoTotalFajillas);
                                    lFajillasEntregadas.add(new RecyclerViewHeadersFajillas(tipoFajillaDescripcion, totaldeFajillas, "$"+montoTotalFajillas, autorizoFajillas, "ENTREGADA"));
                                    banderaFajillas = true;
                                }
                                String fajillasRecibidas = respuestaObjeto.getString("FajillasRecibidas");
                                JSONArray fajillasRecibidasArreglo = new JSONArray(fajillasRecibidas);
                                for (int i = 0; i <fajillasRecibidasArreglo.length() ; i++){
                                    JSONObject fajillasRecibidasObject = fajillasRecibidasArreglo.getJSONObject(i);
                                    tipoFajillaDescripcion = fajillasRecibidasObject.getString("TipoFajilla");
//                                             Precio = fajillasEntregadasObject.getString("Precio");
                                    montoTotalFajillas = fajillasRecibidasObject.getString("ImporteTotal");
                                    totaldeFajillas = fajillasRecibidasObject.getString("Cantidad");
//                                    fajillasMontoTotal = fajillasMontoTotal + Double.parseDouble(montoTotalFajillas);
                                    lFajillasEntregadas.add(new RecyclerViewHeadersFajillas(tipoFajillaDescripcion, totaldeFajillas, "$"+montoTotalFajillas, "", "RECIBIDA"));
                                    banderaFajillas = true;
                                }
                                fechaFajillas = respuestaObjeto.getString("FechaEntrega");
                                if (banderaFajillas==false){
                                    Toast.makeText(MuestreoFajillasEntregadasRecibidas.this, "No hay Fajillas para mostrar", Toast.LENGTH_SHORT).show();
                                    Intent intent1 = new Intent(getApplicationContext(), Menu_Principal.class);
                                    //Se envian los parametros de posicion y usuario
//                                    intent1.putExtra("device_name", m_deviceName);
                                    //inicia el activity
                                    startActivity(intent1);
                                    finish();
                                }else {
                                    DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
                                    simbolos.setDecimalSeparator('.');
                                    DecimalFormat df = new DecimalFormat("#,###.00##",simbolos);
                                    df.setMaximumFractionDigits(2);
                                    RVAdapterFajillas adapter = new RVAdapterFajillas(lFajillasEntregadas);
                                    rcvFajillasEntregadas.setAdapter(adapter);
                                    tvNombreFajillas.setText(nombreEmpleado);
                                    tvFecha.setText("Fecha entrega: " + fechaFajillas);
                                    tvFajillasEntregadas.setText("Monto Fajillas Entregadas: $" + df.format(fajillasMontoTotal).toString());
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }else {
                            Toast.makeText(MuestreoFajillasEntregadasRecibidas.this, "No hay Fajillas para mostrar", Toast.LENGTH_SHORT).show();
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
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        }
    }



}