package com.corpogas.corpoapp.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.corpogas.corpoapp.Configuracion.SQLiteBD;
import com.corpogas.corpoapp.Entities.Classes.RespuestaApi;
import com.corpogas.corpoapp.Entities.Estaciones.Empleado;
import com.corpogas.corpoapp.Entities.Estaciones.EstacionControl;
import com.corpogas.corpoapp.Interfaces.Endpoints.EndPoints;
import com.corpogas.corpoapp.Menu_Principal;
import com.corpogas.corpoapp.R;
import com.corpogas.corpoapp.SplashEmpresas.Splash;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    EditText edtNip;
    TextView txtNumeroDispositivoLogin;
    String ipEstacion, nip, nombreApi, apellidoPaternoApi, apellidoMaternoApi, nombreCompletoApi, claveApi, correoApi, numeroEmpleadoApi, rolDescripcionApi;
    SQLiteBD db;
    RespuestaApi<Empleado> respuestaApiEmpleado;
    long  sucursalIdApi, estacionIdApi, rolIdApi, idApi, idTarjetero, islaId;
    boolean activoApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        db = new SQLiteBD(getApplicationContext());
        ipEstacion = db.getIpEstacion();
        edtNip = (EditText) findViewById(R.id.edtNip);
        idTarjetero = Long.parseLong(db.getIdTarjtero());
        txtNumeroDispositivoLogin = findViewById(R.id.txtNumeroDispositivoLogin);
        txtNumeroDispositivoLogin.setText("No. Dispositivo: " + db.getIdTarjtero());


    }

    public void usuarioCorrecto(){
        boolean correcto = respuestaApiEmpleado.Correcto;
        if (correcto == true){
            sucursalIdApi = respuestaApiEmpleado.getObjetoRespuesta().getSucursalId();
            estacionIdApi = respuestaApiEmpleado.getObjetoRespuesta().getEstacionId();
            rolIdApi = respuestaApiEmpleado.getObjetoRespuesta().getRolId();
            nombreApi = respuestaApiEmpleado.getObjetoRespuesta().getNombre();
            apellidoPaternoApi = respuestaApiEmpleado.getObjetoRespuesta().getApellidoPaterno();
            apellidoMaternoApi = respuestaApiEmpleado.getObjetoRespuesta().getApellidoMaterno();
            nombreCompletoApi = respuestaApiEmpleado.getObjetoRespuesta().getNombreCompleto();
            idApi = respuestaApiEmpleado.getObjetoRespuesta().getId();
            claveApi = respuestaApiEmpleado.getObjetoRespuesta().getClave();
            activoApi = respuestaApiEmpleado.getObjetoRespuesta().isActivo();
            correoApi = respuestaApiEmpleado.getObjetoRespuesta().getCorreo();
            numeroEmpleadoApi = respuestaApiEmpleado.getObjetoRespuesta().getNumeroEmpleado();
            rolDescripcionApi = respuestaApiEmpleado.getObjetoRespuesta().getRol().Description;

            for (EstacionControl item : respuestaApiEmpleado.getObjetoRespuesta().getEstacionControles()){
                islaId = item.IslaId;
            }

            db.InsertarDatosEmpleado(sucursalIdApi, estacionIdApi,rolIdApi, nombreApi, apellidoPaternoApi, apellidoMaternoApi,
                                     nombreCompletoApi, idApi, claveApi, activoApi, correoApi, numeroEmpleadoApi,rolDescripcionApi,islaId);

            Intent intent = new Intent(LoginActivity.this, Menu_Principal.class);
            startActivity(intent);
            finish();
        }else {
            edtNip.setError(respuestaApiEmpleado.Mensaje);
        }

    }

    public void obtenerValidacionNip(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://"+ ipEstacion  +"/corpogasService/")//http://" + data.getIpEstacion() + "/corpogasService_Entities_token/
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        EndPoints obtenerValidacionNip = retrofit.create(EndPoints.class);
        Call<RespuestaApi<Empleado>> call = obtenerValidacionNip.getValidaNip(Integer.parseInt(nip), idTarjetero );
        call.enqueue(new Callback<RespuestaApi<Empleado>>() {

            @Override
            public void onResponse(Call<RespuestaApi<Empleado>> call, Response<RespuestaApi<Empleado>> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                respuestaApiEmpleado = response.body();
                usuarioCorrecto();

            }

            @Override
            public void onFailure(Call<RespuestaApi<Empleado>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void validacionCampoPassword() {
        //Se lee el password del objeto y se asigna a variable
        nip = edtNip.getText().toString();

        //Si no se ingresa nada, envia mensaje de ingresar contraseña
        if (nip.isEmpty()) {
            edtNip.setError("Agrega una clave");
        } else {
            obtenerValidacionNip();
        }
    }

     //Metodo para asignar una accion a la tecla Enter.

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

    //Metodo para regresar a la actividad principal
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();
    }




}