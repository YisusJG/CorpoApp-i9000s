package com.corpogas.corpoapp.Puntada;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.corpogas.corpoapp.Adapters.RVAdapter;
import com.corpogas.corpoapp.Entities.Classes.RecyclerViewHeaders;
import com.corpogas.corpoapp.R;

import java.util.ArrayList;
import java.util.List;

public class SeccionTarjeta extends AppCompatActivity {
    RecyclerView recyclerViewSeccionTarjeta;
    List<RecyclerViewHeaders> lrecyclerViewHeaders;
    Button btnTutorial;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seccion_tarjeta);
        init();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerViewSeccionTarjeta.setLayoutManager(linearLayoutManager);
        recyclerViewSeccionTarjeta.setHasFixedSize(true);
        initializeData();
        initializeAdapter();
    }

    private void init() {
        recyclerViewSeccionTarjeta = findViewById(R.id.rcViewSeccionTarjeta);
    }

    private void initializeData() {
        lrecyclerViewHeaders = new ArrayList<>();
        lrecyclerViewHeaders.add(new RecyclerViewHeaders("Puntada Redimir","Paga con Puntos",R.drawable.redimirpuntada));
        lrecyclerViewHeaders.add(new RecyclerViewHeaders("Puntada Registrar","Registrar Tarjeta Puntada",R.drawable.registrarpuntada));
    }

    private void initializeAdapter() {
        RVAdapter adapter = new RVAdapter(lrecyclerViewHeaders);
        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Seleccion :" + lrecyclerViewHeaders.get(recyclerViewSeccionTarjeta.getChildAdapterPosition(v)).getTitulo(), Toast.LENGTH_SHORT).show();
            }
        });

        recyclerViewSeccionTarjeta.setAdapter(adapter);
    }
}