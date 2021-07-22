package com.corpogas.corpoapp.Corte;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.corpogas.corpoapp.Configuracion.SQLiteBD;
import com.corpogas.corpoapp.Entities.Accesos.AccesoUsuario;
import com.corpogas.corpoapp.Entities.Accesos.Control;
import com.corpogas.corpoapp.Entities.Classes.RespuestaApi;
import com.corpogas.corpoapp.Entities.Cortes.Cierre;
import com.corpogas.corpoapp.Entities.Cortes.CierreCarrete;
import com.corpogas.corpoapp.Entities.Cortes.CierreDespachoDetalle;
import com.corpogas.corpoapp.Modales.Modales;
import com.corpogas.corpoapp.R;
import com.corpogas.corpoapp.Interfaces.Endpoints.EndPoints;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class IslasEstacion extends AppCompatActivity {

    String islaCorte, password, nombreCompleto;
    TextView textNombre;
    Spinner islasEstacion;
    Button btnAceptar;
    ArrayList arrayIslas = new ArrayList<String>();
    ArrayAdapter<CharSequence> adaptadorIslas;
    RespuestaApi<Cierre> respuestaApiCierreCabero;
    RespuestaApi<List<CierreCarrete>> respuestaApiCierreCarretes;
    RespuestaApi<AccesoUsuario> respuestaApiAccesoUsuario;
//    RespuestaApi<List<CierreDetalle>> cierreDetalles;
    RespuestaApi<List<CierreDespachoDetalle>> respuestaApiCierreDespachoDetalle;
    Type respuestaCierreCombustiblesDetalles;
    Type respuestaCierreDetalles;
    Type tipóRespuesta, respuestaCierreCarrete;
    long idusuario, numeroIslas, islaId, sucursalId;

    String ipEstacion, EstacionId;
    SQLiteBD db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_islas_estacion);

        // Traemos los Metodos de la clase SQliteBD
        db = new SQLiteBD(getApplicationContext());
        this.setTitle(db.getNombreEstacion() + " ( EST.:" + db.getNumeroEstacion() + ")");

        sucursalId = Long.parseLong(db.getIdSucursal());
        ipEstacion = db.getIpEstacion();

        // Obtenemos la clase Serializada de accesoUsuario obtenida en la actividad anterior

        respuestaApiAccesoUsuario = (RespuestaApi<AccesoUsuario>) getIntent().getSerializableExtra("accesoUsuario");
        nombreCompleto = respuestaApiAccesoUsuario.getObjetoRespuesta().getNombreCompleto();

        // Asignacion de variables con el Layout de la Clase

        textNombre = (TextView) findViewById(R.id.textNombreIslasEstacion);
        islasEstacion = (Spinner) findViewById(R.id.spinnerIslasEstacion);
        btnAceptar = (Button) findViewById(R.id.btnAceptarIslasEstacion);

        textNombre.setText(nombreCompleto);

        idusuario = respuestaApiAccesoUsuario.getObjetoRespuesta().getNumeroEmpleado();
        password = respuestaApiAccesoUsuario.getObjetoRespuesta().getClave();


        islasJefeIsla();
    }

    public void islasJefeIsla() {

        for (Control item : respuestaApiAccesoUsuario.getObjetoRespuesta().getControles()) {
            numeroIslas = item.NumeroInternoIsla;
            islaId = item.IslaId;
            arrayIslas.add(numeroIslas);
        }

        ArrayList<String> comboIslas = new ArrayList<>();
        comboIslas.add(" Isla: ");
        for (int i = 0; i < arrayIslas.size(); i++) {
            comboIslas.add(String.valueOf(arrayIslas.get(i)));


        }
        adaptadorIslas = new ArrayAdapter(getApplicationContext(), R.layout.custom_spinner, comboIslas);
        islasEstacion.setAdapter(adaptadorIslas);

        islasEstacion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    islaCorte = parent.getItemAtPosition(position).toString();
//                    ObtenCierre();
//                    ObtenerCierreDetalles();
                    obtenerLecturasMecanicas();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public void obtenerLecturasMecanicas(){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://"+ ipEstacion  +"/corpogasService/")//http://" + data.getIpEstacion() + "/corpogasService_Entities_token/
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        EndPoints obtenerLecturasMecanicas = retrofit.create(EndPoints.class);
        Call<RespuestaApi<List<CierreCarrete>>> call = obtenerLecturasMecanicas.getLecturaInicialMecanica(sucursalId, islaId);
        call.enqueue(new Callback<RespuestaApi<List<CierreCarrete>>>() {

            @Override
            public void onResponse(Call<RespuestaApi<List<CierreCarrete>>> call, Response<RespuestaApi<List<CierreCarrete>>> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                respuestaApiCierreCarretes = response.body();
                if (respuestaApiCierreCarretes == null) {
                    String mensaje = "La comunicacon con la API es incorrecta. Objeto(cierreCarretes)";
                    Modales modales = new Modales(IslasEstacion.this);
                    modales.MostrarDialogoError(IslasEstacion.this, mensaje);
                    btnAceptar.setEnabled(true);
                } else {

                    boolean Correcto = respuestaApiCierreCarretes.Correcto;
                    if (Correcto == true) {
                        ObtenerCierreId();

                    } else {
                        String mensaje = respuestaApiCierreCarretes.Mensaje;
                        Modales modales = new Modales(IslasEstacion.this);
                        modales.MostrarDialogoError(IslasEstacion.this, mensaje);
                        btnAceptar.setEnabled(true);
                    }
                }
            }

            @Override
            public void onFailure(Call<RespuestaApi<List<CierreCarrete>>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });

    }

    public void ObtenerCierreId(){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://"+ ipEstacion  +"/corpogasService/")//http://" + data.getIpEstacion() + "/corpogasService_Entities_token/
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        EndPoints obtenerCierreId = retrofit.create(EndPoints.class);
        Call<RespuestaApi<Cierre>> call = obtenerCierreId.getCrearCierre(sucursalId, islaId, idusuario);
        call.enqueue(new Callback<RespuestaApi<Cierre>>() {


            @Override
            public void onResponse(Call<RespuestaApi<Cierre>> call, Response<RespuestaApi<Cierre>> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                respuestaApiCierreCabero = response.body();
                boolean Correcto = respuestaApiCierreCabero.Correcto;
                if (Correcto == true) { // true
                    btnAceptar.setEnabled(true);
                    obtenerDespachoDetalles();


//                            respuetas =  "Se recupero el cierre";

                } else {
                    btnAceptar.setEnabled(true);
                    String mensaje = respuestaApiCierreCabero.Mensaje;
                    Modales modales = new Modales(IslasEstacion.this);
                    modales.MostrarDialogoError(IslasEstacion.this, mensaje);

                }
            }

            @Override
            public void onFailure(Call<RespuestaApi<Cierre>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });

    }

    public void obtenerDespachoDetalles(){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://"+ ipEstacion  +"/corpogasService/")//http://" + data.getIpEstacion() + "/corpogasService_Entities_token/
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        EndPoints obtenerDespachoDetalles = retrofit.create(EndPoints.class);
        Call<RespuestaApi<List<CierreDespachoDetalle>>> call = obtenerDespachoDetalles.getDespachoDetalle(sucursalId, islaId);
        call.enqueue(new Callback<RespuestaApi<List<CierreDespachoDetalle>>>() {


            @Override
            public void onResponse(Call<RespuestaApi<List<CierreDespachoDetalle>>> call, Response<RespuestaApi<List<CierreDespachoDetalle>>> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                respuestaApiCierreDespachoDetalle = response.body();
                Intent intent = new Intent(getApplicationContext(), ProcesoCorte.class);//Lecturas
                intent.putExtra("islaId", String.valueOf(islaId));
                intent.putExtra("respuestaApiCierreCabero", respuestaApiCierreCabero);
                intent.putExtra("cierreCarretes", respuestaApiCierreCarretes);
                intent.putExtra("respuestaApiAccesoUsuario", respuestaApiAccesoUsuario);
//                    intent.putExtra("cierreDetalles",cierreDetalles);
                intent.putExtra("cierreCombustibleDetalles",respuestaApiCierreDespachoDetalle);
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<RespuestaApi<List<CierreDespachoDetalle>>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }
}