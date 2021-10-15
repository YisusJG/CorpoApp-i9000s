package com.corpogas.corpoapp.Fajillas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.corpogas.corpoapp.Conexion;
import com.corpogas.corpoapp.Configuracion.SQLiteBD;
import com.corpogas.corpoapp.Menu_Principal;
import com.corpogas.corpoapp.Modales.Modales;
import com.corpogas.corpoapp.MyListAdapter;
import com.corpogas.corpoapp.R;

public class OpcionFajillas extends AppCompatActivity {
    //    public static final int REQUEST_CODE = 1;
    String banderaHuella;
    Button IniciaVenta, FinalizaVenta;
    ImageButton btnTutorial,btnTutorial2,btnTutorial3;
    ListView list;
    String[] maintitle ={
//            "Inicia Venta", "Inicia Predeterminado", "Finaliza Venta",
            "Fajillas", "Entrega de Fajillas"
    };

    String[] subtitle ={
//            "Inicia Proceso de Venta", "Inicia Predeterminado", "Finaliza Proceso de Venta",
            "Entrega de Fajillas", "Fajillas Entregadas/Recibidas"
    };

    Integer[] imgid={
            R.drawable.gas, R.drawable.gas, R.drawable.fajillas_billetes, R.drawable.fajillas_picos,
    };

    String m_deviceName;
    TextView txtTitulo;
    ImageView imgViewIcono;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opcion_fajillas);

        SQLiteBD data = new SQLiteBD(getApplicationContext());
//        btnTutorial = (ImageButton) findViewById(R.id.btnTutorial);
//        btnTutorial2 = (ImageButton) findViewById(R.id.btnTutorial2);
//        btnTutorial3 = (ImageButton) findViewById(R.id.btnTutorial3);
        txtTitulo = (TextView)findViewById(R.id.textTitle);
        VideoView videoView = findViewById(R.id.miVideo);
        imgViewIcono =(ImageView) findViewById(R.id.imageIcon);
        btnTutorial2.setVisibility(View.INVISIBLE);
        btnTutorial3.setVisibility(View.INVISIBLE);

        this.setTitle(data.getNombreEstacion() + " ( EST.:" + data.getNumeroEstacion() + ")");
        m_deviceName = getIntent().getStringExtra("device_name");
        banderaHuella = getIntent().getStringExtra( "banderaHuella");
        MyListAdapter adapter=new MyListAdapter(OpcionFajillas.this, maintitle, subtitle,imgid);
        list= findViewById(R.id.listOpcionFajillas);
        list.setAdapter(adapter);

        btnTutorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (!Conexion.compruebaConexion(OpcionFajillas.this)) {
////            Toast.makeText(getBaseContext(),"Sin conexión a la red ", Toast.LENGTH_SHORT).show();
//                    Intent intent1 = new Intent(getApplicationContext(), Menu_Principal.class);
//                    startActivity(intent1);
//                    finish();
//                } else {
//                    txtTitulo.setText("¡¡ VENTAS !!");
//                    imgViewIcono.setImageResource(R.drawable.ic_autoplay);
//                    MediaController mediaController = new MediaController(OpcionFajillas.this);
//                    String path = "http://10.0.2.11/stationUpdates/Video_Tutoriales/ventas.mp4";
//                    Uri uri = Uri.parse(path);
//                    videoView.setVideoURI(uri);
//                    videoView.start();
////        mediaController.setMediaPlayer(videoView);
//                    videoView.setMediaController(mediaController);
////        videoView.requestFocus();
//                }
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                // TODO Auto-generated method stub
                if (position == 0) { //ENTREGA FAJILLAS
                    Intent intent = new Intent(getApplicationContext(), EntregaFajillas.class);   //claveIniciaVenta
                    intent.putExtra("LugarProviene", "IniciaVenta");
                    intent.putExtra("device_name", m_deviceName);

                    startActivity(intent);
                    finish();
                } else if (position == 1) { //CONSULTA FAJILLAS
                    Intent intent1 = new Intent(getApplicationContext(), EntregaFajillas.class); //claveFinVenta
                    intent1.putExtra("LugarProviene", "FinalizaVenta");
                    intent1.putExtra("device_name", m_deviceName);
                    startActivity(intent1);
                    finish();
                } //else if (position == 2) {
            }
        });
        if (!Conexion.compruebaConexion(this)) {
            //SalirAplicacion(this);
            Toast.makeText(getBaseContext(),"Sin conexión a la red ", Toast.LENGTH_SHORT).show();
            Intent intent1 = new Intent(getApplicationContext(), Menu_Principal.class);
            startActivity(intent1);
            finish();
        }
    }

    private void  SalirAplicacion(OpcionFajillas mainActivity ) {
        String titulo = "AVISO";
        String mensaje = "Problema de Conexión con la Red";
        Modales modales = new Modales(OpcionFajillas.this);
        View view1 = modales.MostrarDialogoError(OpcionFajillas.this, mensaje);
        view1.findViewById(R.id.buttonAction).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.exit(0);
                mainActivity.finishAffinity();
                finish();
                modales.alertDialog.dismiss();
            }
        });
    }


    public void cambiaColor(String primaryDark, String  primary, String background ) { //String primaryDark, String  primary, String background
        getWindow().setStatusBarColor(Color.parseColor(primaryDark));
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(primary)));
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor(background)));
        getWindow().setNavigationBarColor(Color.parseColor(primary));
    }


    private void validaSeleccion(){
//        IniciaVenta= findViewById(R.id.btniniciaventa);
//        FinalizaVenta=findViewById(R.id.btnfinalizaventa);

        IniciaVenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Menu_Principal.class);
                intent.putExtra("lugarproviene", "1");
                startActivity(intent);
                finish();
            }
        });

        FinalizaVenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(getApplicationContext(), Menu_Principal.class);
                intent1.putExtra("lugarproviene", "2");
                startActivity(intent1);
                finish();
            }
        });

    }


    //Metodo para regresar a la actividad principal
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), Menu_Principal.class);
        startActivity(intent);
        finish();
    }
}