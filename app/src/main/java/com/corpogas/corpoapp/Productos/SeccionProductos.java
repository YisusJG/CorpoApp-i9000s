package com.corpogas.corpoapp.Productos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.corpogas.corpoapp.Adapters.RVAdapter;
import com.corpogas.corpoapp.Configuracion.SQLiteBD;
import com.corpogas.corpoapp.Entities.Classes.RecyclerViewHeaders;
import com.corpogas.corpoapp.Menu_Principal;
import com.corpogas.corpoapp.Puntada.PosicionPuntadaRedimir;
import com.corpogas.corpoapp.Puntada.PuntadaQr;
import com.corpogas.corpoapp.Puntada.PuntadaRedimirQr;
import com.corpogas.corpoapp.R;
import com.corpogas.corpoapp.Tickets.PosicionCargaTickets;

import java.util.ArrayList;
import java.util.List;

public class SeccionProductos extends AppCompatActivity {
    RecyclerView recyclerViewSeccionProductos;
    List<RecyclerViewHeaders> lrecyclerViewHeaders;
    Button btnTutorial;
    String NumeroTarjeta;
    SQLiteBD data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seccion_productos);
        data = new SQLiteBD(getApplicationContext());
        this.setTitle(data.getNombreEstacion() + " ( EST.:" + data.getNumeroEstacion() + ")");

        init();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerViewSeccionProductos.setLayoutManager(linearLayoutManager);
        recyclerViewSeccionProductos.setHasFixedSize(true);
        initializeData();
        initializeAdapter();

    }

    private void init() {
        recyclerViewSeccionProductos = findViewById(R.id.rcViewSeccionProductos);
    }

    private void initializeData() {
        lrecyclerViewHeaders = new ArrayList<>();
        lrecyclerViewHeaders.add(new RecyclerViewHeaders("Productos","Agregar Productos",R.drawable.productos));
        lrecyclerViewHeaders.add(new RecyclerViewHeaders("Inventario","Inventario Productos",R.drawable.inventario));
    }
    private void initializeAdapter() {
        RVAdapter adapter = new RVAdapter(lrecyclerViewHeaders);
        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (lrecyclerViewHeaders.get(recyclerViewSeccionProductos.getChildAdapterPosition(v)).getTitulo()) {
                    case "Productos":
                        Intent intentProducto = new Intent(getApplicationContext(), PosicionCargaTickets.class);
                        intentProducto.putExtra("lugarProviene", "OpcionProductos");
                        startActivity(intentProducto);
                        finish();
                       break;
                    case "Inventario":
                        Intent intent = new Intent(getApplicationContext(), Inventario.class);
                        startActivity(intent);
                        finish();
                        break;
                    default:
                        break;
                }
            }
        });
        recyclerViewSeccionProductos.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getBaseContext(), Menu_Principal.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
        finish();
    }

}