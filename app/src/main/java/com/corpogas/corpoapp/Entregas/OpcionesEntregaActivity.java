package com.corpogas.corpoapp.Entregas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.corpogas.corpoapp.Configuracion.CorteDB;
import com.corpogas.corpoapp.Configuracion.SQLiteBD;
import com.corpogas.corpoapp.Entregas.Entities.PaperVoucherType;
import com.corpogas.corpoapp.Interfaces.Endpoints.EndPoints;
import com.corpogas.corpoapp.Login.EntregaPicos;
import com.corpogas.corpoapp.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OpcionesEntregaActivity extends AppCompatActivity {
    Button btnEntregaVales;
    Button btnEntregaPicos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opciones_entrega);


        btnEntregaVales = findViewById(R.id.btnEntregarVales);
        btnEntregaVales.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), EntregaValesActivity.class);
            startActivity(intent);
        });


        btnEntregaPicos = findViewById(R.id.btnEntregaPicos);
        btnEntregaPicos.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), EntregaPicosActivity.class);
            startActivity(intent);
        });
    }


}