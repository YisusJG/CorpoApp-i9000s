package com.corpogas.corpoapp.ClienteEstacion;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.corpogas.corpoapp.Adapters.RVAdapter;
import com.corpogas.corpoapp.Configuracion.SQLiteBD;
import com.corpogas.corpoapp.Entities.Accesos.AccesoUsuario;
import com.corpogas.corpoapp.Entities.Classes.RecyclerViewHeaders;
import com.corpogas.corpoapp.Entities.Classes.RespuestaApi;
import com.corpogas.corpoapp.Entities.Common.EstadoPosicion;
import com.corpogas.corpoapp.Interfaces.Endpoints.EndPoints;
import com.corpogas.corpoapp.R;
import com.corpogas.corpoapp.TanqueLleno.PlanchadoTarjeta.PlanchadoTanqueLleno;

import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PosicionCargaCE extends AppCompatActivity {

    SQLiteBD db;
    String ipEstacion, numeroEmpledo;
    Long sucursalId;

    RespuestaApi<List<EstadoPosicion>> respuestaApiEstadoPosicion;

    RecyclerView rcvPosicionCargaCE;
    List<RecyclerViewHeaders> lrcvPosicionCargaCE;

    long posicionCargaId, cargaNumeroInterno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posicion_carga_ce);

        init();
        getObjetos();
        setVariables();
        onClicks();
        obtenerPosicionCargaEmpleadoId();
        obtenerPosicionCargasEstacion();
    }

    private void init(){
        rcvPosicionCargaCE = (RecyclerView)findViewById(R.id.rcvPosicionCargaCE);

    }
    private void getObjetos(){

    }
    private void setVariables(){
        db = new SQLiteBD(getApplicationContext());
        this.setTitle(db.getNombreEstacion() + " ( EST.:" + db.getNumeroEstacion() + ")");
        ipEstacion = db.getIpEstacion();
        sucursalId = Long.parseLong(db.getIdSucursal());
        numeroEmpledo = db.getNumeroEmpleado();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvPosicionCargaCE.setLayoutManager(linearLayoutManager);
        rcvPosicionCargaCE.setHasFixedSize(true);

    }
    private void onClicks(){

    }

    private void obtenerPosicionCargaEmpleadoId(){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://"+ipEstacion+"/CorpogasService/") //anterior
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        EndPoints obtenerPosicionCargaEmpleadoId = retrofit.create(EndPoints.class);
        Call<RespuestaApi<List<EstadoPosicion>>> call = obtenerPosicionCargaEmpleadoId.obtenerPosicionCargaEmpleadoId(sucursalId, numeroEmpledo);
        call.timeout().timeout(60, TimeUnit.SECONDS);
        call.enqueue(new Callback<RespuestaApi<List<EstadoPosicion>>>() {

            @Override
            public void onResponse(Call<RespuestaApi<List<EstadoPosicion>>> call, Response<RespuestaApi<List<EstadoPosicion>>> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                respuestaApiEstadoPosicion = response.body();
            }

            @Override
            public void onFailure(Call<RespuestaApi<List<EstadoPosicion>>> call, Throwable t) {
                Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void obtenerPosicionCargasEstacion(){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://"+ipEstacion+"/CorpogasService/") //anterior
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        EndPoints obtenerPosicionCargasEstacion = retrofit.create(EndPoints.class);
        Call<RespuestaApi<List<EstadoPosicion>>> call = obtenerPosicionCargasEstacion.obtenerPosicionCargasEstacion(sucursalId);
        call.timeout().timeout(60, TimeUnit.SECONDS);
        call.enqueue(new Callback<RespuestaApi<List<EstadoPosicion>>>() {

            @Override
            public void onResponse(Call<RespuestaApi<List<EstadoPosicion>>> call, Response<RespuestaApi<List<EstadoPosicion>>> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                respuestaApiEstadoPosicion = response.body();
            }

            @Override
            public void onFailure(Call<RespuestaApi<List<EstadoPosicion>>> call, Throwable t) {
                Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void initializeAdapter() {
        RVAdapter adapter = new RVAdapter(lrcvPosicionCargaCE);
        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                posicionCargaId=lrcvPosicionCargaCE.get(rcvPosicionCargaCE.getChildAdapterPosition(v)).getPosicionCargaId();
                cargaNumeroInterno = lrcvPosicionCargaCE.get(rcvPosicionCargaCE.getChildAdapterPosition(v)).getPosicioncarganumerointerno();
                //ValidaTransaccionActiva();
//                if (lugarproviene.equals("Planchado")){
//                    Intent intent1 = new Intent(getApplicationContext(), PlanchadoTanqueLleno.class); //PlanchadoTanqueLleno
//                    intent1.putExtra("cargaPosicion", posicionCargaId);
//                    startActivity(intent1);
//                    finish();
//                }else{
//                    if (lugarproviene.equals("Arillos")){
//                        enviaPeticionArillo();
//                    }else{
//                        enviardatos();
//                    }
//                }

            }
        });

        rcvPosicionCargaCE.setAdapter(adapter);
    }

}