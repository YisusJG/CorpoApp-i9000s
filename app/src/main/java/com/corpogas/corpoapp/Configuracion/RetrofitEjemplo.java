package com.corpogas.corpoapp.Configuracion;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;


import com.corpogas.corpoapp.R;

import com.corpogas.corpoapp.Entities.Estaciones.Estacion;
import com.corpogas.corpoapp.Request.Interfaces.EndPoints;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RetrofitEjemplo extends AppCompatActivity {

    private TextView mJsonTxtView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofit);
        mJsonTxtView =(TextView)findViewById(R.id.jsonText);
        getEstacion();
    }

    private void getEstacion(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.201.20/CorpogasService/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        EndPoints iEstacionesIp = retrofit.create(EndPoints.class);
        Call<Estacion> call = iEstacionesIp.getEstacioApi("44","4","5","45");
        call.enqueue(new Callback<Estacion>() {
            @Override
            public void onResponse(Call<Estacion> call, Response<Estacion> response) {
                if(!response.isSuccessful())
                {
                    mJsonTxtView.setText("Codigo: "+ response.code());
                    return;
                }
                Estacion estacion = response.body();
                Long sucursalId = estacion.getSucursalId();
                mJsonTxtView.append(sucursalId.toString());
            }

            @Override
            public void onFailure(Call<Estacion> call, Throwable t) {
                mJsonTxtView.setText(t.getMessage());
            }
        });
    }
}