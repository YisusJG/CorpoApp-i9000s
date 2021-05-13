package com.corpogas.corpoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.corpogas.corpoapp.Configuracion.SQLiteBD;
import com.corpogas.corpoapp.Entities.Catalogos.Bin;
import com.corpogas.corpoapp.Entities.Classes.RespuestaApi;
import com.corpogas.corpoapp.Entities.Sistemas.ConfiguracionAplicacion;
import com.corpogas.corpoapp.Request.Interfaces.EndPoints;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PruebasEndPoint extends AppCompatActivity {

    SQLiteBD data;

    Button btnPeticionBin;

    RespuestaApi<Bin> respuestaApiBin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pruebas_end_point);
        data = new SQLiteBD(getApplicationContext());


        btnPeticionBin = (Button) findViewById(R.id.btnPeticionBin);
        btnPeticionBin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONArray datos = new JSONArray();
                final JSONObject jsonObject = new JSONObject();
                datos.put("400000025010000199997000");
                datos.put("400000025010000199997000");
                datos.put("");
                try {
                    jsonObject.put("Pistas",datos);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //        Conexion conexion = new Conexion(sucursalid,7,mac);
//                Bin bin = new Bin("Pistas",);
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://"+data.getIpEstacion()+"/CorpogasService_Entities/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                EndPoints obtenNumeroTarjetero = retrofit.create(EndPoints.class);
                Call<RespuestaApi<Bin>> call = obtenNumeroTarjetero.getBin("497",jsonObject);
                call.enqueue(new Callback<RespuestaApi<Bin>>() {

                    @Override
                    public void onResponse(Call<RespuestaApi<Bin>> call, Response<RespuestaApi<Bin>> response) {

                        if(!response.isSuccessful()) {
                            return;
                        }
                        respuestaApiBin = response.body();

                    }

                    @Override
                    public void onFailure(Call<RespuestaApi<Bin>> call, Throwable t) {
                        Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
    }




}