package com.corpogas.corpoapp.Facturacion;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.corpogas.corpoapp.Configuracion.SQLiteBD;
import com.corpogas.corpoapp.Entities.Classes.RespuestaApi;
import com.corpogas.corpoapp.Facturacion.Adapters.FacturacionAdapter;
import com.corpogas.corpoapp.Facturacion.Entities.PeticionRFC;
import com.corpogas.corpoapp.Facturacion.Entities.RespuestaRFC;
import com.corpogas.corpoapp.Interfaces.Endpoints.EndPoints;
import com.corpogas.corpoapp.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ClienteFacturas extends Activity {
    RespuestaApi<List<RespuestaRFC>> respuestaApiRfc;
    String ipEstacion, RFC, nombrefacturo, idUsuario;
    PeticionRFC peticionRFC;
    List<RespuestaRFC> lstRfc;
    RecyclerView recyclerViewFacturacion;
    public ProgressDialog bar;

    Type respuestaPeticionRFC;
    SQLiteBD db;

    private TextInputLayout txtObtenRFC;
//    Button obtenToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente_facturas);
        txtObtenRFC =(TextInputLayout)findViewById(R.id.filledRFC);
        db = new SQLiteBD(getApplicationContext());
        recyclerViewFacturacion = findViewById(R.id.recyclerViewFactura);
        nombrefacturo=getIntent().getStringExtra("nombrecompleto");
        idUsuario = db.getNumeroEmpleado();
        ipEstacion = db.getIpEstacion();

    }

    public void onClickObtenCliente(View v) {
        bar = new ProgressDialog(ClienteFacturas.this);
        bar.setTitle("Facturación");
        bar.setMessage("Ejecutando... ");
        bar.setIcon(R.drawable.fact);
        bar.setCancelable(false);
        bar.show();
        if(!validateRFC()){
            return;
        }
        ObtenRfcCliente();

    }

    private boolean validateRFC(){
        RFC = txtObtenRFC.getEditText().getText().toString().trim();

        if(RFC.isEmpty())
        {
            txtObtenRFC.setError("Favor de ingresar un RFC");
            bar.cancel();
            return false;

        }else if(RFC.length()>13 || RFC.length()<12)
        {
            txtObtenRFC.setError("El RFC debe de ser de 12 o 13 caracteres");
            bar.cancel();
            return false;
        }

        else
        {
            txtObtenRFC.setError(null);

//            txtObtenRFC.setErrorEnabled(false);
            return true;
        }
    }

    public void ObtenRfcCliente(){
        peticionRFC = new PeticionRFC();
        peticionRFC.rfc = RFC;
        peticionRFC.despachador = idUsuario;
        peticionRFC.terminal = db.getIdTarjtero();

        String json = new Gson().toJson(peticionRFC);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://"+ ipEstacion  +"/corpogasService/")//http://" + data.getIpEstacion() + "/corpogasService_Entities_token/
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        EndPoints obtenerRfcCliente = retrofit.create(EndPoints.class);
        Call<RespuestaApi<List<RespuestaRFC>>> call = obtenerRfcCliente.postObtenerRfcs(peticionRFC);
        call.enqueue(new Callback<RespuestaApi<List<RespuestaRFC>>>() {

            @Override
            public void onResponse(Call<RespuestaApi<List<RespuestaRFC>>> call, Response<RespuestaApi<List<RespuestaRFC>>> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                respuestaApiRfc = response.body();

                    if(!validaRFCExistente()){
                        return;
                    }
                    setRecyclerView();

            }

            @Override
            public void onFailure(Call<RespuestaApi<List<RespuestaRFC>>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validaRFCExistente(){

        if(respuestaApiRfc.Correcto == true) {
            if (respuestaApiRfc.getMensaje() != null) {
                bar.cancel();
                String validaRFC = respuestaApiRfc.getAlertaHttp().getError().getMensajeSistema();
                txtObtenRFC.setError(validaRFC);
                return false;
            } else {
                txtObtenRFC.setError(null);
//            txtObtenRFC.setErrorEnabled(false);
                return true;
            }
        }else
        {
            bar.cancel();
            txtObtenRFC.setError(respuestaApiRfc.getAlertaHttp().getError().getMensajeSistema());
            return false;
        }

    }

    private void setRecyclerView() {
        lstRfc = (List<RespuestaRFC>) respuestaApiRfc.getObjetoRespuesta();
        FacturacionAdapter facturacionAdapter = new FacturacionAdapter(lstRfc,getApplicationContext(),ClienteFacturas.this,db,idUsuario);
        recyclerViewFacturacion.setAdapter(facturacionAdapter);
        recyclerViewFacturacion.setHasFixedSize(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //disable scan
        Intent intentDisScan = new Intent("ACTION_BAR_SCANCFG");
        intentDisScan.putExtra("EXTRA_SCAN_POWER", 0);
        ClienteFacturas.this.sendBroadcast(intentDisScan);

    }

    @Override
    protected void onResume() {
        super.onResume();

        disableFunctionLaunch(true);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        //enable scan
        Intent intentEnableScan = new Intent("ACTION_BAR_SCANCFG");
        intentEnableScan.putExtra("EXTRA_SCAN_POWER", 1);
        ClienteFacturas.this.sendBroadcast(intentEnableScan);
    }

    @Override
    protected void onPause() {

        disableFunctionLaunch(false);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        super.onPause();
    }

    // disable the power key when the device is boot from alarm but not ipo boot
    private static final String DISABLE_FUNCTION_LAUNCH_ACTION = "android.intent.action.DISABLE_FUNCTION_LAUNCH";
    private void disableFunctionLaunch(boolean state) {
        Intent disablePowerKeyIntent = new Intent(DISABLE_FUNCTION_LAUNCH_ACTION);
        if (state) {
            disablePowerKeyIntent.putExtra("state", true);
        } else {
            disablePowerKeyIntent.putExtra("state", false);
        }
        sendBroadcast(disablePowerKeyIntent);
    }
    public void ObtenProgressDialog(String mensaje)
    {
        bar = new ProgressDialog(ClienteFacturas.this);
        bar.setTitle("Facturación");

        bar.setMessage(mensaje);
        bar.setIcon(R.drawable.fact);
        bar.setCancelable(false);
        bar.show();
    }
}