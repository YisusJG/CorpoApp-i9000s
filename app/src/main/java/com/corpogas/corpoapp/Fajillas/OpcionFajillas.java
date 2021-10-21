package com.corpogas.corpoapp.Fajillas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.corpogas.corpoapp.Adapters.RVAdapter;
import com.corpogas.corpoapp.Conexion;
import com.corpogas.corpoapp.Configuracion.SQLiteBD;
import com.corpogas.corpoapp.Entities.Classes.RecyclerViewHeaders;
import com.corpogas.corpoapp.R;

import java.util.ArrayList;
import java.util.List;

public class OpcionFajillas extends AppCompatActivity {
    RecyclerView rcvOpcionFajillas;
    List<RecyclerViewHeaders> lOpcionFajillas;
    //    ImageButton btnTutorial;
    SQLiteBD data;
    TextView txtTitulo;
    ImageView imgViewIcono;
    VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opcion_fajillas);
        init();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvOpcionFajillas.setLayoutManager(linearLayoutManager);
        rcvOpcionFajillas.setHasFixedSize(true);
        initializeData();
        initializeAdapter();
//        onClicks();
    }

    private void initializeAdapter() {
        RVAdapter adapter = new RVAdapter(lOpcionFajillas);
        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String titulo;
//                Toast.makeText(getApplicationContext(),"Seleccion :" + lCombustible.get(rcvCombustible.getChildAdapterPosition(v)).getTitulo(), Toast.LENGTH_SHORT).show();
                titulo = lOpcionFajillas.get(rcvOpcionFajillas.getChildAdapterPosition(v)).getTitulo();

                if (titulo.equals("Entrega Fajillas")) {
                    Intent intent = new Intent(getApplicationContext(), EntregaFajillas.class);   //LeeTarjeta
                    intent.putExtra("lugarProviene", "EntregaFajillas");
                    startActivity(intent);
                    finish();
                } else if (titulo.equals("Consulta Fajillas")) {
                    Intent intent1 = new Intent(getApplicationContext(), MuestreoFajillasEntregadasRecibidas.class);
                    startActivity(intent1);
                    finish();
                }
            }
        });
        rcvOpcionFajillas.setAdapter(adapter);
    }

    private void initializeData() {
        lOpcionFajillas = new ArrayList<>();
        lOpcionFajillas.add(new RecyclerViewHeaders("Entrega Fajillas","Entrega de Fajillas",R.drawable.fajillas_billetes));
        lOpcionFajillas.add(new RecyclerViewHeaders("Consulta Fajillas","Fajillas Entregadas/Recibidas",R.drawable.fajillas_picos));
    }

    private void init() {
        data = new SQLiteBD(getApplicationContext());
        rcvOpcionFajillas = findViewById(R.id.rcvOpcionFajillas);
        this.setTitle(data.getNombreEstacion() + " ( EST.:" + data.getNumeroEstacion() + ")");

    }

}