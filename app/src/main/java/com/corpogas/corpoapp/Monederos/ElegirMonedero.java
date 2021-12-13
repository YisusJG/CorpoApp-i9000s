package com.corpogas.corpoapp.Monederos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.corpogas.corpoapp.ClienteEstacion.ClienteEstacionNip;
import com.corpogas.corpoapp.ClienteEstacion.LecturaTarjeta;
import com.corpogas.corpoapp.LecturaTarjetas.MonederosElectronicos;
import com.corpogas.corpoapp.Menu_Principal;
import com.corpogas.corpoapp.R;
import com.corpogas.corpoapp.TanqueLleno.seccionTanqueLleno;

public class ElegirMonedero extends AppCompatActivity {

    ImageButton imgTanqueLlenoMonederos, imgClienteEstacionMonederos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elegir_monedero);
        init();
        getObjetos();
        setVariables();
        onClicks();

    }

    private void init(){
        imgTanqueLlenoMonederos = (ImageButton) findViewById(R.id.imgTanqueLlenoMonederos);
        imgClienteEstacionMonederos = (ImageButton) findViewById(R.id.imgClienteEstacionMonederos);
    }

    private void getObjetos(){

    }

    private void setVariables(){

    }

    private void onClicks(){
        imgTanqueLlenoMonederos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), seccionTanqueLleno.class);
                startActivity(intent);

            }
        });

        imgClienteEstacionMonederos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ClienteEstacionNip.class);
                startActivity(intent);
            }
        });
    }

}