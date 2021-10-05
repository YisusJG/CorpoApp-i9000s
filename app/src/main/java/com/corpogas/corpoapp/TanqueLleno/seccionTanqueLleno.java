package com.corpogas.corpoapp.TanqueLleno;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.LimitExceededException;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.corpogas.corpoapp.Adapters.RVAdapter;
import com.corpogas.corpoapp.Configuracion.SQLiteBD;
import com.corpogas.corpoapp.Entities.Classes.RecyclerViewHeaders;
import com.corpogas.corpoapp.LecturaTarjetas.MonederosElectronicos;
import com.corpogas.corpoapp.ObtenerClave.ClaveEmpleado;
import com.corpogas.corpoapp.R;
import com.corpogas.corpoapp.VentaCombustible.Ventas;

import java.util.ArrayList;
import java.util.List;

public class seccionTanqueLleno extends AppCompatActivity {

    RecyclerView rcvTanqueLleno;
    List<RecyclerViewHeaders> lTanqueLleno;
    ImageButton btnTutorial;
    SQLiteBD data;
    TextView txtTitulo;
    ImageView imgViewIcono;
    VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seccion_tanque_lleno);
        init();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvTanqueLleno.setLayoutManager(linearLayoutManager);
        rcvTanqueLleno.setHasFixedSize(true);
        initializeData();
        initializeAdapter();
//        onClicks();

    }

    private void init() {
        data = new SQLiteBD(getApplicationContext());
        rcvTanqueLleno = findViewById(R.id.rcvTanqueLleno);
//        btnTutorial = (ImageButton) findViewById(R.id.btnTutorial);
//        txtTitulo = (TextView)findViewById(R.id.textTitle);
//        videoView = findViewById(R.id.miVideo);
//        imgViewIcono =(ImageView) findViewById(R.id.imageIcon);
        this.setTitle(data.getNombreEstacion() + " ( EST.:" + data.getNumeroEstacion() + ")");
    }

    private void initializeData() {
        lTanqueLleno = new ArrayList<>();
        lTanqueLleno.add(new RecyclerViewHeaders("Banda Magnética","Tarjeta TanqueLleno",R.drawable.tanquelleno));
        lTanqueLleno.add(new RecyclerViewHeaders("NFC","Tarjeta NFC",R.drawable.tanquelleno));
    }

    private void initializeAdapter() {
        RVAdapter adapter = new RVAdapter(lTanqueLleno);
        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String titulo;
//                Toast.makeText(getApplicationContext(),"Seleccion :" + lCombustible.get(rcvCombustible.getChildAdapterPosition(v)).getTitulo(), Toast.LENGTH_SHORT).show();
                titulo = lTanqueLleno.get(rcvTanqueLleno.getChildAdapterPosition(v)).getTitulo();
                if (titulo.equals("Banda Magnética")) {
                    Intent intent = new Intent(getApplicationContext(), MonederosElectronicos.class);   //LeeTarjeta
                    intent.putExtra("Enviadodesde", "TanqueLlenoBandaMagnetica");
//                    intent.putExtra("LugarProviene", "TanqueLlenoBandaMagnetica");
//                    intent.putExtra("device_name", m_deviceName);
//                    intent.putExtra("banderaHuella", banderaHuella);

                    startActivity(intent);
                    finish();

                } else if (titulo.equals("NFC")) {
                    Intent intent1 = new Intent(getApplicationContext(), ClaveEmpleado.class); //claveFinVenta
//                    intent1.putExtra("LugarProviene", "FinalizaVenta");
//                    intent1.putExtra("device_name", m_deviceName);
//                    intent1.putExtra("banderaHuella", banderaHuella);
//                    startActivity(intent1);
                    finish();
                }

            }
        });

        rcvTanqueLleno.setAdapter(adapter);
    }

    private void onClicks() {
        btnTutorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (!Conexion.compruebaConexion(ventash.this)) {
////            Toast.makeText(getBaseContext(),"Sin conexión a la red ", Toast.LENGTH_SHORT).show();
//                    Intent intent1 = new Intent(getApplicationContext(), Menu_Principal.class);
//                    startActivity(intent1);
//                    finish();
//                } else {
//                txtTitulo.setText("¡¡ VENTAS !!");
//                imgViewIcono.setImageResource(R.drawable.ic_autoplay);
//                MediaController mediaController = new MediaController(seccionTanqueLleno.this);
//                String path = "http://10.0.2.11/stationUpdates/Video_Tutoriales/ventas.mp4";
//                Uri uri = Uri.parse(path);
//                videoView.setVideoURI(uri);
//                videoView.start();
////        mediaController.setMediaPlayer(videoView);
//                videoView.setMediaController(mediaController);
//        videoView.requestFocus();
//                }
            }
        });

    }



}