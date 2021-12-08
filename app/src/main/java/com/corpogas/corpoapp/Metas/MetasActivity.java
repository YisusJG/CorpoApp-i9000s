package com.corpogas.corpoapp.Metas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.corpogas.corpoapp.Configuracion.SQLiteBD;
import com.corpogas.corpoapp.Entities.Accesos.AccesoUsuario;
import com.corpogas.corpoapp.Entities.Classes.RespuestaApi;
import com.corpogas.corpoapp.Entregas.Adapters.RVAdapterValesPapel;
import com.corpogas.corpoapp.Entregas.Entities.ResumenVale;
import com.corpogas.corpoapp.Entregas.EntregaValesActivity;
import com.corpogas.corpoapp.Interfaces.Endpoints.EndPoints;
import com.corpogas.corpoapp.Metas.Adapters.AdapterMetas;
import com.corpogas.corpoapp.Metas.Entities.Metas;
import com.corpogas.corpoapp.Modales.Modales;
import com.corpogas.corpoapp.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MetasActivity extends AppCompatActivity {
//    private TextView txvTipoProducto,txvDespachos,txvMetas,txvVendidos,txtDiferencias;
    String bearerToken;
    RespuestaApi<AccesoUsuario> token;

    private RecyclerView rcvMetas;
    String ipEstacion,numeroEmpleado;
    Long sucursalId;
    SQLiteBD db;
    RespuestaApi<List<Metas>> metasApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_metas);
        getToken();
        init();
//        initialData();
    }

    private void getToken() {
        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("http://"+ip2+"/CorpogasService/") //anterior
                .baseUrl("http://10.0.1.40/CorpogasService_entities_token/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        EndPoints obtenerToken = retrofit.create(EndPoints.class);
        Call<RespuestaApi<AccesoUsuario>> call = obtenerToken.getAccesoUsuario(497L, "1111");
        call.timeout().timeout(60, TimeUnit.SECONDS);
        call.enqueue(new Callback<RespuestaApi<AccesoUsuario>>() {
            @Override
            public void onResponse(Call<RespuestaApi<AccesoUsuario>> call, Response<RespuestaApi<AccesoUsuario>> response) {
                if (response.isSuccessful()) {
                    token = response.body();
                    assert token != null;
                    bearerToken = token.Mensaje;
                    initialData();
                } else {
                    bearerToken = "";
                }
            }

            @Override
            public void onFailure(Call<RespuestaApi<AccesoUsuario>> call, Throwable t) {
                Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void init() {
        db = new SQLiteBD(getApplicationContext());
        ipEstacion = db.getIpEstacion();
        sucursalId = Long.parseLong(db.getIdSucursal());
        numeroEmpleado = db.getNumeroEmpleado();
        rcvMetas = findViewById(R.id.rcvMetas);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext().getApplicationContext());
        rcvMetas.setLayoutManager(linearLayoutManager);
        rcvMetas.setHasFixedSize(true);
    }

    private void initialData() {

        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("http://" + ipEstacion + "/CorpogasService/")
                .baseUrl("http://10.0.1.40/CorpogasService_entities_token/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        EndPoints metas = retrofit.create(EndPoints.class);
        Call<RespuestaApi<List<Metas>>> call = metas.getMetas(sucursalId,numeroEmpleado, "Bearer " +bearerToken);
        call.enqueue(new Callback<RespuestaApi<List<Metas>>>() {
            @Override
            public void onResponse(Call<RespuestaApi<List<Metas>>> call, Response<RespuestaApi<List<Metas>>> response) {
                if (!response.isSuccessful()) {
                    return;
                }

                metasApi = response.body();

                if(metasApi.Correcto)
                {
                    initialAdapter(metasApi.getObjetoRespuesta());

                }else
                {
                    String titulo = "AVISO";
                    Modales modales = new Modales(MetasActivity.this);
                    View view1 = modales.MostrarDialogoAlertaAceptar(MetasActivity.this,"Ah ocurrido un error",titulo);
                    view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            modales.alertDialog.dismiss();
                        }
                    });
                }

            }

            @Override
            public void onFailure(Call<RespuestaApi<List<Metas>>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });



//        List<Metas> lMetas= new ArrayList<Metas>();
//        lMetas.add(new Metas("ACEITES", 785L,50.0,32.0,18.0));
//        lMetas.add(new Metas("ADITIVOS",45L,95.0,45.0,50.0));
//        lMetas.add(new Metas("OTROS",40L,92.0,9.0,1.0));
//        lMetas.add(new Metas("COMBUSTIBLES",4L,902.0,222.0,680.0));

//        initialAdapter(lMetas);
    }

    private void initialAdapter(List<Metas> lMetas) {
        AdapterMetas adapter = new AdapterMetas(lMetas);
        rcvMetas.setAdapter(adapter);

    }
}