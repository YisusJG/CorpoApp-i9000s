package com.corpogas.corpoapp.Corte;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.corpogas.corpoapp.Conexion;
import com.corpogas.corpoapp.Configuracion.SQLiteBD;
import com.corpogas.corpoapp.Entities.Accesos.AccesoUsuario;
import com.corpogas.corpoapp.Entities.Classes.RespuestaApi;
import com.corpogas.corpoapp.Menu_Principal;
import com.corpogas.corpoapp.R;
import com.corpogas.corpoapp.Request.Interfaces.EndPoints;
import com.google.zxing.Reader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Clave extends AppCompatActivity {

    // Declaracion de Variables

    String pass, ipEstacion, sucursalId;
    TextView numerodispositivo;
    EditText password;
    SQLiteBD data;
    RespuestaApi<AccesoUsuario> respuestaApiAccesoUsuario;
    LottieAnimationView animationView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clave);
        data = new SQLiteBD(getApplicationContext());
//        this.setTitle(data.getNombreEstacion() + " ( EST.:" + data.getNumeroEstacion() + ")");

        sucursalId = "497"; //data.getIdSucursal();
        ipEstacion = "10.0.1.40"; //data.getIpEstacion();
        numerodispositivo =  findViewById(R.id.textnumerodispositivoCorteClave);
        numerodispositivo.setText("");
        numerodispositivo.setVisibility(View.INVISIBLE);

        // Asignacion de variables con el Layout de la Clase

        password = findViewById(R.id.passwordCorteClave);

        // Traemos los Metodos de la clase SQliteBD

        data = new SQLiteBD(getApplicationContext());

        animationView2 = findViewById(R.id.animationCorteClave);

    }

    public void usuarioCorrecto(){
        boolean correcto = respuestaApiAccesoUsuario.Correcto;
        if (correcto == true) {
            if (respuestaApiAccesoUsuario.getObjetoRespuesta().getClave().equals(pass) && respuestaApiAccesoUsuario.getObjetoRespuesta().getNumeroInternoRol() == 3) {
                Intent intent = new Intent(getApplicationContext(), IslasEstacion.class); //IslasEstacion.class aqui iba antes
                intent.putExtra("accesoUsuario", respuestaApiAccesoUsuario);
                startActivity(intent);
                finish();
            } else {
                password.setError("No eres jefe de isla");
            }

        } else {
            password.setError(respuestaApiAccesoUsuario.Mensaje);
        }

    }

    // Metodo para obtener la respuesta de accesoUsuarios

    public void obtenerObjetoAccesoUsuario(){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://"+ ipEstacion  +"/corpogasService/")//http://" + data.getIpEstacion() + "/corpogasService_Entities_token/
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        EndPoints obtenerAccesoUsuario = retrofit.create(EndPoints.class);
        Call<RespuestaApi<AccesoUsuario>> call = obtenerAccesoUsuario.getAccesoUsuario(sucursalId, pass);
        call.enqueue(new Callback<RespuestaApi<AccesoUsuario>>() {

            @Override
            public void onResponse(Call<RespuestaApi<AccesoUsuario>> call, Response<RespuestaApi<AccesoUsuario>> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                respuestaApiAccesoUsuario = response.body();
                usuarioCorrecto();
            }

            @Override
            public void onFailure(Call<RespuestaApi<AccesoUsuario>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    // Metodo para asignar una accion a la tecla Enter.

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_ENTER:
                validacionCampoPassword();
                return true;
            default:
                return super.onKeyUp(keyCode, event);
        }
    }

    //Metodo para validar el campo Password

    private void validacionCampoPassword() {
        //Se lee el password del objeto y se asigna a variable
        pass = password.getText().toString();

        //Si no se ingresa nada, envia mensaje de ingresar contraseña
        if (pass.isEmpty()) {
            password.setError("Ingresa tu contraseña");
        } else {
            obtenerObjetoAccesoUsuario();
        }
    }

    //Metodo para regresar a la actividad principal
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), Menu_Principal.class);
        startActivity(intent);
        finish();
    }

}