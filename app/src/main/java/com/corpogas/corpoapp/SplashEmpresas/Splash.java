package com.corpogas.corpoapp.SplashEmpresas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.corpogas.corpoapp.Configuracion.SQLiteBD;
import com.corpogas.corpoapp.Login.LoginActivity;
import com.corpogas.corpoapp.Menu_Principal;
import com.corpogas.corpoapp.R;

public class Splash extends AppCompatActivity {
    SQLiteBD db;
    private final int DURACION_SPLASH = 2000;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        db = new SQLiteBD(this);
        this.setTitle(db.getNombreEstacion() + " ( EST.:" + db.getNumeroEstacion() + ")");

        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent = new Intent(Splash.this, LoginActivity.class);
                startActivity(intent);
                finish();
            };
        },DURACION_SPLASH);

    }
}