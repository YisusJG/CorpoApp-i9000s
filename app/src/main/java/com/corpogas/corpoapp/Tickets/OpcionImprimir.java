package com.corpogas.corpoapp.Tickets;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.corpogas.corpoapp.Adapters.RVAdapter;
import com.corpogas.corpoapp.Configuracion.SQLiteBD;
import com.corpogas.corpoapp.Entities.Classes.RecyclerViewHeaders;
import com.corpogas.corpoapp.Fajillas.AutorizaFajillas;
import com.corpogas.corpoapp.LecturaTarjetas.MonederosElectronicos;
import com.corpogas.corpoapp.Menu_Principal;
import com.corpogas.corpoapp.Modales.Modales;
import com.corpogas.corpoapp.Puntada.PuntadaQr;
import com.corpogas.corpoapp.Puntada.PuntadaRedimirQr;
import com.corpogas.corpoapp.Puntada.SeccionTarjeta;
import com.corpogas.corpoapp.R;

import java.util.ArrayList;
import java.util.List;

public class OpcionImprimir extends AppCompatActivity {
    RecyclerView rcViewOpcionImprimir;
    List<RecyclerViewHeaders> lrecyclerViewHeaders;
    Button btnTutorial;
    String NumeroTarjeta;
    SQLiteBD data;
    String PuntadaProceso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opcion_imprimir);
        data = new SQLiteBD(getApplicationContext());
        this.setTitle(data.getNombreEstacion() + " ( EST.:" + data.getNumeroEstacion() + ")");

        init();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcViewOpcionImprimir.setLayoutManager(linearLayoutManager);
        rcViewOpcionImprimir.setHasFixedSize(true);
        initializeData();
        initializeAdapter();
    }

    private void init() {
        rcViewOpcionImprimir = findViewById(R.id.rcViewOpcionImprimir);
    }

    private void initializeData() {
        lrecyclerViewHeaders = new ArrayList<>();
        lrecyclerViewHeaders.add(new RecyclerViewHeaders("Imprimir","Imprime última Transacción",R.drawable.reimprimir));
        lrecyclerViewHeaders.add(new RecyclerViewHeaders("Reimprimir","Reimprime última Transacción",R.drawable.reimprimir));
        lrecyclerViewHeaders.add(new RecyclerViewHeaders("Ticket Pendiente","Imprime ticket enviado a HH",R.drawable.pendientes));

        NumeroTarjeta = getIntent().getStringExtra("track");
    }

    private void initializeAdapter() {
        RVAdapter adapter = new RVAdapter(lrecyclerViewHeaders);
        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (lrecyclerViewHeaders.get(rcViewOpcionImprimir.getChildAdapterPosition(v)).getTitulo()) {
                    case "Imprimir": //Consulta Saldo
                        Intent intentMonedero = new Intent(getApplicationContext(), PosicionCargaTickets.class);
                        intentMonedero.putExtra("lugarproviene", "Imprimir");
                        startActivity(intentMonedero);
                        finish();
                        break;
                    case "Reimprimir"://Redimir
                        Intent intent = new Intent(getApplicationContext(), AutorizaFajillas.class);//PosicionCargaTickets
                        intent.putExtra("lugarProviene", "Reimprimir"); //lugarproviene
                        startActivity(intent);
                        finish();
                        break;
                    case "Ticket Pendiente"://Registrar
                        Intent intentTP = new Intent(getApplicationContext(), ClaveTicketPendiente.class);
                        startActivity(intentTP);
                        finish();
                        break;
                    default:
                        break;
                }
            }
        });

        rcViewOpcionImprimir.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
//        Intent intent = new Intent(getApplicationContext(), Munu_Principal.class);
//        startActivity(intent);
//        finish();
        startActivity(new Intent(getBaseContext(), Menu_Principal.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
        finish();
    }


}