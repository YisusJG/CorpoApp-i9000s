package com.corpogas.corpoapp.Puntada;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.corpogas.corpoapp.Adapters.RVAdapter;
import com.corpogas.corpoapp.Configuracion.SQLiteBD;
import com.corpogas.corpoapp.Entities.Classes.RecyclerViewHeaders;
import com.corpogas.corpoapp.LecturaTarjetas.MonederosElectronicos;
import com.corpogas.corpoapp.Menu_Principal;
import com.corpogas.corpoapp.Modales.Modales;
import com.corpogas.corpoapp.ObtenerClave.ClaveEmpleado;
import com.corpogas.corpoapp.R;

import java.util.ArrayList;
import java.util.List;

public class SeccionTarjeta extends AppCompatActivity {
    RecyclerView recyclerViewSeccionTarjeta;
    List<RecyclerViewHeaders> lrecyclerViewHeaders;
    Button btnTutorial;
    String NumeroTarjeta;
    SQLiteBD data;
    String PuntadaProceso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seccion_tarjeta);
        data = new SQLiteBD(getApplicationContext());
        this.setTitle(data.getNombreEstacion() + " ( EST.:" + data.getNumeroEstacion() + ")");

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
        lrecyclerViewHeaders.add(new RecyclerViewHeaders("Puntada Redimir QR","Lee QR para Pago con Puntos",R.drawable.redimirpuntada));

        //        lrecyclerViewHeaders.add(new RecyclerViewHeaders("Puntada Registrar","Registrar Tarjeta Puntada",R.drawable.registrarpuntada));
        lrecyclerViewHeaders.add(new RecyclerViewHeaders("Puntada Consulta Saldo","Saldo Tarjeta",R.drawable.registrarpuntada));
        lrecyclerViewHeaders.add(new RecyclerViewHeaders("Puntada App","Descarga App de Puntada",R.drawable.registrarpuntada));

        NumeroTarjeta = getIntent().getStringExtra("track");
    }

    private void initializeAdapter() {
        RVAdapter adapter = new RVAdapter(lrecyclerViewHeaders);
        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (lrecyclerViewHeaders.get(recyclerViewSeccionTarjeta.getChildAdapterPosition(v)).getTitulo()) {
                    case "Puntada Consulta Saldo": //Consulta Saldo
                        PuntadaProceso = "ConsultaSaldoPuntada";
                        RedencionConsultaPuntada("ConsultaSaldoPuntada" );
                        break;
                    case "Puntada Redimir"://Redimir
                        PuntadaProceso = "Redimir";
                        RedencionConsultaPuntada("Redimir");
                        break;
                    case "Puntada Registrar"://Registrar
                        break;
                    case "Puntada App":
                        Intent intentApp = new Intent(getApplicationContext(), PuntadaQr.class);
                        startActivity(intentApp);
                        finish();
                        break;
                    case "Puntada Redimir QR":
                        PuntadaProceso = "RedimirQr";
                        RedencionConsultaPuntada("RedimirQr");
                        break;
                    default:
                        break;
                }
            }
        });

        recyclerViewSeccionTarjeta.setAdapter(adapter);
    }
    private void RedencionConsultaPuntada(String ProcesoARealizar){
        String proceso = ProcesoARealizar;
        try {
            String titulo = "PUNTADA";
            String mensaje = "Ingresa el NIP de la tarjeta Puntada" ;
            Modales modales = new Modales(SeccionTarjeta.this);
            View viewLectura = modales.MostrarDialogoInsertaDato(SeccionTarjeta.this, mensaje, titulo);
            EditText edtProductoCantidad = ((EditText) viewLectura.findViewById(R.id.textInsertarDato));
            edtProductoCantidad.setInputType(InputType.TYPE_CLASS_NUMBER);
            viewLectura.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String cantidad = edtProductoCantidad.getText().toString();
                    if (cantidad.isEmpty()){
                        edtProductoCantidad.setError("Ingresa el NIP de la tarjeta Puntada");
                    }else {
                        String NIPCliente = cantidad;
//                        Intent intent4 = new Intent(getApplicationContext(), PosicionPuntadaRedimir.class);
//                        intent4.putExtra("track", NumeroTarjeta);
//                        intent4.putExtra("nip", NIPCliente);
//                        intent4.putExtra("lugarproviene", PuntadaProceso);
//                        startActivity(intent4);
//                        finish();
                        if (PuntadaProceso.equals("RedimirQr")){
                            Intent intentQr = new Intent(getApplicationContext(), PuntadaRedimirQr.class);
                            intentQr.putExtra("nip", NIPCliente);
                            intentQr.putExtra("lugarProviene", "Redimir");

                            startActivity(intentQr);
                            finish();
                        }else{
                            Intent intentMonedero = new Intent(getApplicationContext(), MonederosElectronicos.class);
                            intentMonedero.putExtra("Enviadodesde", "menuprincipal");
                            intentMonedero.putExtra("tipoTarjeta", "Puntada");
                            intentMonedero.putExtra("lugarproviene", PuntadaProceso);
                            intentMonedero.putExtra("nip", NIPCliente);
                            startActivity(intentMonedero);
                        }
                    }
                }
            });
            viewLectura.findViewById(R.id.buttonNo).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    modales.alertDialog.dismiss();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

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