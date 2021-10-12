package com.corpogas.corpoapp.Puntada;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.corpogas.corpoapp.Configuracion.SQLiteBD;
import com.corpogas.corpoapp.R;

public class PuntadaRedimirQr extends AppCompatActivity {
    SQLiteBD db;
    Button btnAceptarrQrPuntada;
    EditText tvDescuento, tvNoTarjetaQr;
    String NIP;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puntada_redimir_qr);
        db = new SQLiteBD(this);
        this.setTitle(db.getNombreEstacion() + " ( EST.:" + db.getNumeroEstacion() + ")");

        NIP = getIntent().getStringExtra("nip");
        tvDescuento = (EditText) findViewById(R.id.tvDescuento);
        tvNoTarjetaQr = (EditText) findViewById(R.id.tvNoTarjetaQr);
        btnAceptarrQrPuntada = (Button) findViewById(R.id.btnAceptarrQrPuntada);
        btnAceptarrQrPuntada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PosicionPuntadaRedimir.class); //ENVIABA A SeccionTarjeta cambio a PosicionPuntadaRedimir
                intent.putExtra("track",tvNoTarjetaQr.getText().toString());
                intent.putExtra("nip", NIP);
                intent.putExtra("lugarproviene", "RedimirQR");
                intent.putExtra("descuento", tvDescuento.getText().toString());
                startActivity(intent);
                finish();
            }
        });


    }



}