package com.corpogas.corpoapp.TanqueLleno;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.corpogas.corpoapp.LecturaTarjetas.MonederosElectronicos;
import com.corpogas.corpoapp.R;

public class LeeTarjeta extends AppCompatActivity {
    Button btnLeeTarjeta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lee_tarjeta);

        btnLeeTarjeta = findViewById(R.id.btnLeeTarjeta);
        btnLeeTarjeta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TanqueLlenoNip.class);
                intent.putExtra("track", "3999990020015649");
                startActivity(intent);
                finish();
            }
        });
    }
}