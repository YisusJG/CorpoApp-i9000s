package com.corpogas.corpoapp.Puntada;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.corpogas.corpoapp.Configuracion.SQLiteBD;
import com.corpogas.corpoapp.Menu_Principal;
import com.corpogas.corpoapp.R;

public class PuntadaQr extends AppCompatActivity {
    SQLiteBD data;
    Button btnSalir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puntada_qr);
        data = new SQLiteBD(getApplicationContext());
        this.setTitle(data.getNombreEstacion() + " ( EST.:" + data.getNumeroEstacion() + ")");

        btnSalir = (Button) findViewById(R.id.btnSalir);
        btnSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentApp = new Intent(getApplicationContext(), Menu_Principal.class);
                startActivity(intentApp);
                finish();
            }
        });
    }
}