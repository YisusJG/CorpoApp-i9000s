package com.corpogas.corpoapp.VentaCombustible;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.corpogas.corpoapp.Adapters.RVAdapter;
import com.corpogas.corpoapp.Configuracion.SQLiteBD;
import com.corpogas.corpoapp.Entities.Classes.RecyclerViewHeaders;
import com.corpogas.corpoapp.Menu_Principal;
import com.corpogas.corpoapp.ObtenerClave.ClaveEmpleado;
import com.corpogas.corpoapp.R;

import java.util.ArrayList;
import java.util.List;

public class Ventas extends AppCompatActivity {

    RecyclerView rcvCombustible;
    List<RecyclerViewHeaders> lCombustible;
    ImageView btnTutorial;
    SQLiteBD data;
    TextView txtTitulo;
    ImageView imgViewIcono;
    VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ventas);
        init();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvCombustible.setLayoutManager(linearLayoutManager);
        rcvCombustible.setHasFixedSize(true);
        initializeData();
        initializeAdapter();
        onClicks();
    }



    private void init() {
        data = new SQLiteBD(getApplicationContext());
        rcvCombustible = findViewById(R.id.rcvVentasCombustible);
        btnTutorial = (ImageView) findViewById(R.id.btnTutorial);
        txtTitulo = (TextView)findViewById(R.id.textTitle);
        videoView = findViewById(R.id.miVideo);
        imgViewIcono =(ImageView) findViewById(R.id.imageIcon);
        this.setTitle(data.getNombreEstacion() + " ( EST.:" + data.getNumeroEstacion() + ")");
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
                txtTitulo.setText("¡¡ VENTAS !!");
                imgViewIcono.setImageResource(R.drawable.ic_autoplay);
                MediaController mediaController = new MediaController(Ventas.this);
                String path = "http://10.0.2.11/stationUpdates/Video_Tutoriales/ventas.mp4";
                Uri uri = Uri.parse(path);
                videoView.setVideoURI(uri);
                videoView.start();
//        mediaController.setMediaPlayer(videoView);
                videoView.setMediaController(mediaController);
//        videoView.requestFocus();
//                }
            }
        });

    }


    private void initializeData() {
        lCombustible = new ArrayList<>();
        lCombustible.add(new RecyclerViewHeaders("Inicia Venta","Inicia Proceso de Venta",R.drawable.gas));
        lCombustible.add(new RecyclerViewHeaders("Finaliza Venta","Finaliza Proceso de Venta",R.drawable.gas));
    }


    private void initializeAdapter() {
        RVAdapter adapter = new RVAdapter(lCombustible);
        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String titulo;
//                Toast.makeText(getApplicationContext(),"Seleccion :" + lCombustible.get(rcvCombustible.getChildAdapterPosition(v)).getTitulo(), Toast.LENGTH_SHORT).show();
                titulo = lCombustible.get(rcvCombustible.getChildAdapterPosition(v)).getTitulo();
                if (titulo.equals("Inicia Venta")) {
                    Intent intent = new Intent(getApplicationContext(), ClaveEmpleado.class);   //claveIniciaVenta
                    intent.putExtra("LugarProviene", "IniciaVenta");
//                    intent.putExtra("device_name", m_deviceName);
//                    intent.putExtra("banderaHuella", banderaHuella);

                    startActivity(intent);
                    finish();

                } else if (titulo.equals("Finaliza Venta")) {
                    Intent intent1 = new Intent(getApplicationContext(), ClaveEmpleado.class); //claveFinVenta
                    intent1.putExtra("LugarProviene", "FinalizaVenta");
//                    intent1.putExtra("device_name", m_deviceName);
//                    intent1.putExtra("banderaHuella", banderaHuella);
                    startActivity(intent1);
                    finish();
                }

            }
        });

        rcvCombustible.setAdapter(adapter);
    }




    //Metodo para regresar a la actividad principal
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), Menu_Principal.class);
        startActivity(intent);
        finish();
    }
}