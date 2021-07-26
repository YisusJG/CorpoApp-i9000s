package com.corpogas.corpoapp.Puntada;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.corpogas.corpoapp.R;

public class TarjetaPuntadaProvisional extends AppCompatActivity {
Button btnTarjeta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tarjeta_puntada_provisional);

        btnTarjeta = findViewById(R.id.btnPuntada);
        btnTarjeta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SeccionTarjeta.class);
                intent.putExtra("track", "4000052500100001");
//                intent.putExtra("device_name", m_deviceName);
                startActivity(intent);
                finish();

            }
        });
    }
}