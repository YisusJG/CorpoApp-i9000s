package com.corpogas.corpoapp.TanqueLleno;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.corpogas.corpoapp.Configuracion.SQLiteBD;
import com.corpogas.corpoapp.ObtenerClave.ClaveEmpleado;
import com.corpogas.corpoapp.R;

import java.math.BigInteger;

public class TanqueLlenoNip extends AppCompatActivity {
    SQLiteBD data;
    EditText Nippasword;
    String NIPCliente, NIPmd5Cliente;
    String track2;
    String banderaHuella, m_deviceName;

    ImageButton btnTutorial,btnTutorial2,btnTutorial3;
    TextView txtTitulo;
    ImageView imgViewIcono;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tanque_lleno_nip);

        init();

    }

    private void init() {
        data = new SQLiteBD(getApplicationContext());
//        btnTutorial = (ImageButton) findViewById(R.id.btnTutorial);
//        txtTitulo = (TextView)findViewById(R.id.textTitle);
//        videoView = findViewById(R.id.miVideo);
//        imgViewIcono =(ImageView) findViewById(R.id.imageIcon);
        this.setTitle(data.getNombreEstacion() + " ( EST.:" + data.getNumeroEstacion() + ")");
//        txtTitulo = (TextView)findViewById(R.id.textTitle);
//        VideoView videoView = findViewById(R.id.miVideo);
        imgViewIcono =(ImageView) findViewById(R.id.imageView);
//        btnTutorial2.setVisibility(View.INVISIBLE);
//        btnTutorial3.setVisibility(View.INVISIBLE);
        Nippasword = findViewById(R.id.nippasword);
        track2 =  getIntent().getStringExtra("track");
    }

    //procedimiento para  cachar el Enter del teclado
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_ENTER:
                calculos();
                return true;
            default:
                return super.onKeyUp(keyCode, event);
        }
    }

    private void calculos() {
        //Se lee el password del objeto y se asigna a variable
        String pass;

        pass = Nippasword.getText().toString();
        if (pass.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Ingrese el NIP de la tarjeta TanqueLleno", Toast.LENGTH_SHORT).show();
        } else {
            NIPCliente = pass;
            ObtieneNipMD5();
//            Intent intent = new Intent(getApplicationContext(), ClaveEmpleado.class); //ClaveDespachadorTL
//            //bundle.putString("track",track2);
//            intent.putExtra("track",track2);
//            intent.putExtra("nipTanqueLleno", NIPCliente);
//            intent.putExtra("nipTanqueLlenoMd5", NIPmd5Cliente);
//            intent.putExtra("LugarProviene", "Tanque");
//            startActivity(intent);
//            finish();
            Intent intent5 = new Intent(getApplicationContext(), PosicionCargaTLl.class);
//            intent5.putExtra("IdUsuario", idusuario);
            intent5.putExtra("track", track2);
            intent5.putExtra("nipCliente", NIPCliente);
            intent5.putExtra("nipMd5Cliente", NIPmd5Cliente);
            intent5.putExtra("pagoconpuntada", "no");
            startActivity(intent5);
            finish();

        }
    }
    private void ObtieneNipMD5(){

        byte [] md5Input = NIPCliente.getBytes();
        BigInteger md5Data = null;

        try{
            md5Data = new BigInteger(1, md5.encryptMD5(md5Input));
        } catch (Exception e) {
            e.printStackTrace();
        }
        NIPmd5Cliente = md5Data.toString(16);

    }


}