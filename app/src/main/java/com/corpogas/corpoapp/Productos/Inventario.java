package com.corpogas.corpoapp.Productos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.corpogas.corpoapp.Conexion;
import com.corpogas.corpoapp.Configuracion.SQLiteBD;
import com.corpogas.corpoapp.Entities.Accesos.AccesoUsuario;
import com.corpogas.corpoapp.Entities.Classes.RecyclerViewHeaders;
import com.corpogas.corpoapp.Entities.Classes.RespuestaApi;
import com.corpogas.corpoapp.Entities.Estaciones.BodegaProducto;
import com.corpogas.corpoapp.Fajillas.AutorizaFajillas;
import com.corpogas.corpoapp.Fajillas.RecyclerViewHeadersFajillas;
import com.corpogas.corpoapp.Interfaces.Endpoints.EndPoints;
import com.corpogas.corpoapp.Menu_Principal;
import com.corpogas.corpoapp.Metas.Entities.Metas;
import com.corpogas.corpoapp.Metas.MetasActivity;
import com.corpogas.corpoapp.Modales.Modales;
import com.corpogas.corpoapp.Productos.Adapters.AdapterBodegaProducto;
import com.corpogas.corpoapp.Puntada.PosicionPuntadaRedimir;
import com.corpogas.corpoapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Inventario extends AppCompatActivity {

    private RecyclerView rcvInventario;
    String ipEstacion, numeroEmpleado;
    Long sucursalId;
    SQLiteBD db;
    RespuestaApi<List<BodegaProducto>> respuestaApiBodegaProducto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventario);
        init();
        obtenerExistencias();

    }

    private void init(){
        db = new SQLiteBD(getApplicationContext());
        ipEstacion = db.getIpEstacion();
        sucursalId = Long.parseLong(db.getIdSucursal());
        numeroEmpleado =  db.getNumeroEmpleado();

        rcvInventario = findViewById(R.id.rcvInventario);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext().getApplicationContext());
        rcvInventario.setLayoutManager(linearLayoutManager);
        rcvInventario.setHasFixedSize(true);
    }


    private void obtenerExistencias() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://" + ipEstacion + "/CorpogasService/")
//                .baseUrl("http://" + ipEstacion + "/CorpogasService_entities_token/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        EndPoints obtenerExistencias = retrofit.create(EndPoints.class);
        Call<RespuestaApi<List<BodegaProducto>>> call = obtenerExistencias.getExistenciaPorEmpleado(sucursalId,numeroEmpleado);
        call.enqueue(new Callback<RespuestaApi<List<BodegaProducto>>>() {

            @Override
            public void onResponse(Call<RespuestaApi<List<BodegaProducto>>> call, retrofit2.Response<RespuestaApi<List<BodegaProducto>>> response) {
                if (!response.isSuccessful()) {
//                    GlobalToken.errorTokenWithReload(Inventario.this);
                    return;
                }

                respuestaApiBodegaProducto = response.body();
                if (respuestaApiBodegaProducto.Correcto){
                    initialAdapter(respuestaApiBodegaProducto.getObjetoRespuesta());
                }else{
                    String titulo = "AVISO";
                    Modales modales = new Modales(Inventario.this);
                    View view1 = modales.MostrarDialogoAlertaAceptar(Inventario.this,"Ha ocurrido un error",titulo);
                    view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            modales.alertDialog.dismiss();
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<RespuestaApi<List<BodegaProducto>>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initialAdapter(List<BodegaProducto> lBodegaProducto){
        AdapterBodegaProducto adapter = new AdapterBodegaProducto(lBodegaProducto);
        rcvInventario.setAdapter(adapter);
    }
}