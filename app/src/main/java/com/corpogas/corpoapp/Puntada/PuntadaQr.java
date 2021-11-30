package com.corpogas.corpoapp.Puntada;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.corpogas.corpoapp.Configuracion.SQLiteBD;
import com.corpogas.corpoapp.Menu_Principal;
import com.corpogas.corpoapp.R;

public class PuntadaQr extends AppCompatActivity {
    SQLiteBD data;
    Button btnSalir;
    ImageView imvIos, imvAndroid, imvAndroidopc, imviosOpc;
    TextView tvOpcion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puntada_qr);
        data = new SQLiteBD(getApplicationContext());
        this.setTitle(data.getNombreEstacion() + " ( EST.:" + data.getNumeroEstacion() + ")");

        init();

        btnSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentApp = new Intent(getApplicationContext(), Menu_Principal.class);
                startActivity(intentApp);
                finish();
            }
        });

        imvAndroidopc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvOpcion.setText("Android");
                imvAndroid.setVisibility(View.VISIBLE);
                imvIos.setVisibility(View.INVISIBLE);
            }
        });
        imviosOpc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvOpcion.setText("iOS");
                imvAndroid.setVisibility(View.INVISIBLE);
                imvIos.setVisibility(View.VISIBLE);
            }
        });

    }

    private void init(){
        imvIos = (ImageView) findViewById(R.id.imvIos);
        imvAndroid = (ImageView) findViewById(R.id.imvAndroid);
        imvAndroidopc = (ImageView) findViewById(R.id.imvAndroidopc);
        imviosOpc = (ImageView) findViewById(R.id.imviosOpc);
        tvOpcion = findViewById(R.id.tvOpcion);
        btnSalir = (Button) findViewById(R.id.btnSalir);

        imvAndroid.setVisibility(View.VISIBLE);
        imvIos.setVisibility(View.INVISIBLE);

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getBaseContext(), Menu_Principal.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
        finish();
    }


}