package com.corpogas.corpoapp.Configuracion;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.corpogas.corpoapp.Entities.Classes.RespuestaApi;
import com.corpogas.corpoapp.Entities.Estaciones.Estacion;
import com.corpogas.corpoapp.Entities.Sistemas.Conexion;
import com.corpogas.corpoapp.Entities.Sistemas.ConfiguracionAplicacion;
import com.corpogas.corpoapp.Entities.Sucursales.Update;
import com.corpogas.corpoapp.Entities.Sucursales.UpdateDetail;
import com.corpogas.corpoapp.Entities.Tickets.Ticket;
import com.corpogas.corpoapp.Modales.Modales;
import com.corpogas.corpoapp.R;
import com.corpogas.corpoapp.Interfaces.Endpoints.EndPoints;
import com.corpogas.corpoapp.SplashEmpresas.Splash;
import com.corpogas.corpoapp.SplashEmpresas.SplashGulf;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ConfiguracionServidor extends AppCompatActivity{
    EditText edtOct1, edtOct2, edtOct3, edtOct4;
    Button btnenviar;
    String oct1, oct2,oct3,oct4,ip2;
    static String mac;
    Estacion estacion;
    Ticket ticket;
    Conexion conexionApi;
    ConfiguracionAplicacion configuracionAplicacionApi;
    RespuestaApi<Update> applicationUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_configuracion_servidor);
        this.setTitle("Configuracion Inicial Servidor");
        getMacAddress();
        SQLiteBD data = new SQLiteBD(getApplicationContext());
        boolean verdad = data.checkDataBase("/data/data/com.corpogas.corpoapp/databases/ConfiguracionEstacion.db");
        if(verdad == true){
            String tipo = data.getTipoEstacion();
            if (tipo.equals("CORPOGAS")){
                Intent intent = new Intent(getApplicationContext(), Splash.class);
                startActivity(intent);//h
                finish();
            }else{
                if (tipo.equals("GULF")){
                    Intent intent = new Intent(getApplicationContext(),
                            SplashGulf.class);
                    startActivity(intent);
                    finish();
                }else{
                    if (tipo.equals("PEMEX")){
                        Intent intent = new Intent(getApplicationContext(), Splash.class);
                        startActivity(intent);

                        finish();
                    }
                }
            }
        }else{

            btnenviar = findViewById(R.id.btnEnviar);

            btnenviar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    edtOct1 = findViewById(R.id.edtOct1);
                    edtOct2 = findViewById(R.id.edtOct2);
                    edtOct3 = findViewById(R.id.edtOct3);
                    edtOct4 = findViewById(R.id.edtOct4);

                    oct1 = edtOct1.getText().toString();
                    oct2 = edtOct2.getText().toString();
                    oct3 = edtOct3.getText().toString();
                    oct4 = edtOct4.getText().toString();

                    ip2 = oct1 + "." + oct2 + "." + oct3 + "." + oct4;


                    if (oct1.isEmpty()){
                        Toast.makeText(getApplicationContext(),"Ingresa el campo 1",Toast.LENGTH_LONG).show();
                        //happy
                    }else{
                        if (oct2.isEmpty()){
                            Toast.makeText(getApplicationContext(),"Ingresa este campo 2",Toast.LENGTH_LONG).show();
                        }else{
                            if (oct3.isEmpty()){
                                Toast.makeText(getApplicationContext(),"Ingresa este campo 3",Toast.LENGTH_LONG).show();
                            }else{
                                if (oct4.isEmpty()){
                                    Toast.makeText(getApplicationContext(),"Ingresa este campo 4",Toast.LENGTH_LONG).show();
                                }else{
                                    ConectarIP();
                                }
                            }
                        }
                    }
                }
            });
        }



    }

    private void ConectarIP() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://"+ip2+"/CorpogasService/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        EndPoints conectarIp = retrofit.create(EndPoints.class);
        Call<Estacion> call = conectarIp.getEstacionApi(oct1,oct2,oct3,oct4);
        call.timeout().timeout(60, TimeUnit.SECONDS);
        call.enqueue(new Callback<Estacion>() {
            @Override
            public void onResponse(Call<Estacion> call, Response<Estacion> response) {
                if(!response.isSuccessful())
                {
                    String titulo = "AVISO";
                    String mensaje = "La direccion IP que ingresaste no es Valida";
                    final Modales modales = new Modales(ConfiguracionServidor.this);
                    View view1 = modales.MostrarDialogoAlertaAceptar(ConfiguracionServidor.this, mensaje, titulo);
                    view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            modales.alertDialog.dismiss();

                            edtOct1.setText("");
                            edtOct2.setText("");
                            edtOct3.setText("");
                            edtOct4.setText("");

                            edtOct1.requestFocus();


                        }
                    });
//                    mJsonTxtView.setText("Codigo: "+ response.code());
                    return;
                }
                estacion = response.body();
                guardarDatosDBEmpresa();
            }

            @Override
            public void onFailure(Call<Estacion> call, Throwable t) {
                String error ="";
                Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

    }


    public static String getMacAddress() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    mac = String.valueOf(res1.append(Integer.toHexString(b & 0xFF) + ":"));
                }

                if (res1.length() > 0) {
                    mac = String.valueOf(res1.deleteCharAt(res1.length() - 1));

                }

                return res1.toString();
            }
        } catch (Exception ex) {
            Log.e("Error", ex.getMessage());
        }
        return "";
    }

    private void guardarDatosDBEmpresa() {
        try {

            Long id =  estacion.getSucursalId();
            String siic = estacion.getSiic();
            Long sucursalid = estacion.getSucursalId();

            String correo = estacion.getSucursal().getEmail();
            Long empresaid  = estacion.getSucursal().getCompanyId();
            String ip = estacion.getSucursal().getIp();
            String nombre = estacion.getSucursal().getAlias();
            String numerofranquicia = estacion.getSucursal().getFranchiseNumber();
            String numinterno = estacion.getSucursal().getBranchNumber();

            String descripcion =estacion.getSucursal().getCompany().getGroup().getName();

            SQLiteBD data = new SQLiteBD(getApplicationContext());
            data.InsertarDatosEstacion(id.toString(),sucursalid.toString(),siic,correo,empresaid.toString(),ip,nombre,numerofranquicia,numinterno, descripcion);

            guardarDatosEncabezado(sucursalid);
            obtenerNumeroTarjetero(sucursalid);
            getActualizaAPP();

            if (descripcion.equals("CORPOGAS")){
                String titulo = "Inicio de Configuración";
                String mensaje = "Los datos se guardaron correctamente";
                final Modales modales = new Modales(ConfiguracionServidor.this);
                View view1 = modales.MostrarDialogoCorrecto(ConfiguracionServidor.this,mensaje);
                view1.findViewById(R.id.buttonAction).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), Splash.class);
                        startActivity(intent);
                        finish();
                        modales.alertDialog.dismiss();
                    }
                });
            }else{
                if (descripcion.equals("GULF")){
                    String titulo = "Inicio de Configuración";
                    String mensaje = "Los datos se guardaron correctamente";
                    Modales modales = new Modales(ConfiguracionServidor.this);
                    View view1 = modales.MostrarDialogoCorrecto(ConfiguracionServidor.this,mensaje);
                    view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getApplicationContext(), SplashGulf.class);
                            startActivity(intent);
                            finish();
                            modales.alertDialog.dismiss();
                        }
                    });
                }else{
                    if (descripcion.equals("PEMEX")){
                        String titulo = "Inicio de Configuración";
                        String mensaje = "Los datos se guardaron correctamente";
                        Modales modales = new Modales(ConfiguracionServidor.this);
                        View view1 = modales.MostrarDialogoCorrecto(ConfiguracionServidor.this,mensaje);
                        view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(getApplicationContext(), Splash.class);
                                startActivity(intent);
                                finish();
                                modales.alertDialog.dismiss();
                            }
                        });
                    }else {
                        String titulo = "Inicio de Configuración";
                        String mensaje = "Los datos no se guardaron en la base de datos";
                        Modales modales = new Modales(ConfiguracionServidor.this);
                        View view1 = modales.MostrarDialogoError(ConfiguracionServidor.this,mensaje );
                        view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                modales.alertDialog.dismiss();
                            }
                        });
                    }
                }
            }
            edtOct1.setText("");
            edtOct2.setText("");
            edtOct3.setText("");
            edtOct4.setText("");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getActualizaAPP() {
//        String getUrl = "http://10.0.2.11/CorpoCoreService/api/actualizaciones/sucursalId/"+data.getIdSucursal()+"/aplicacionId/3/lastUpdates"; //Produccion
        SQLiteBD data = new SQLiteBD(getApplicationContext());
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://"+ip2+"/CorpogasService/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        EndPoints actualizaApp = retrofit.create(EndPoints.class);
        Call<RespuestaApi<Update>> call = actualizaApp.getActializaApp(data.getIdSucursal());
        call.timeout().timeout(60, TimeUnit.SECONDS);
        call.enqueue(new Callback<RespuestaApi<Update>>() {
            @Override
            public void onResponse(Call<RespuestaApi<Update>> call, Response<RespuestaApi<Update>> response) {
                if(!response.isSuccessful())
                {
//                    mJsonTxtView.setText("Codigo: "+ response.code());
                    return;
                }
                applicationUpdate = response.body();

                if(applicationUpdate.isCorrecto() == true) {

                    for (UpdateDetail item : applicationUpdate.getObjetoRespuesta().getUpdateDetails()) {
                        data.InsertarActualizcionApp(item.getVersion(), item.getFileName(), "I9000S");
                    }
                }else
                {
                    data.InsertarActualizcionApp("","","I9000S");
                }

            }

            @Override
            public void onFailure(Call<RespuestaApi<Update>> call, Throwable t) {
                Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void obtenerNumeroTarjetero(final long sucursalid) {

//        Conexion conexion = new Conexion(sucursalid,7,mac);
        ConfiguracionAplicacion configuracionAplicacion = new ConfiguracionAplicacion(sucursalid,0,3,"",mac,0,false,true,0);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://"+ip2+"/CorpogasService/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        EndPoints obtenNumeroTarjetero = retrofit.create(EndPoints.class);
        Call<ConfiguracionAplicacion> call = obtenNumeroTarjetero.getConexionApi(configuracionAplicacion);
        call.timeout().timeout(60, TimeUnit.SECONDS);
        call.enqueue(new Callback<ConfiguracionAplicacion>() {
            @Override
            public void onResponse(Call<ConfiguracionAplicacion> call, Response<ConfiguracionAplicacion> response) {
                if(!response.isSuccessful()) {
                    return;
                }
                configuracionAplicacionApi = response.body();
//                String direccionmac =  configuracionAplicacionApi.getDireccionMac();//respons.getString("DireccionMac");
//                boolean banderaHuella = configuracionAplicacionApi.isLectorHuella(); //respons.getString("PropiedadConexion");
//                long id = configuracionAplicacionApi.getId();  //respons.getString("Id");
//
//                SQLiteBD data = new SQLiteBD(ConfiguracionServidor.this);
//                data.InsertarDatosNumeroTarjetero(direccionmac,String.valueOf(banderaHuella), String.valueOf(id));

            }

            @Override
            public void onFailure(Call<ConfiguracionAplicacion> call, Throwable t) {
                Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void guardarDatosEncabezado(long sucursalid) {
        //Utilizamos el metodo Get para obtener el encabezado para los tickets
        //hay que cambiar el volo 1 del fina po el numeo de la estacion que se encuentra

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://"+ip2+"/CorpogasService/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        EndPoints guardaDatosEncabezado = retrofit.create(EndPoints.class);
        Call<Ticket> call = guardaDatosEncabezado.getTicketsApi(sucursalid);
        call.timeout().timeout(60, TimeUnit.SECONDS);
        call.enqueue(new Callback<Ticket>() {
            @Override
            public void onResponse(Call<Ticket> call, Response<Ticket> response) {
                if(!response.isSuccessful())
                {
//                    mJsonTxtView.setText("Codigo: "+ response.code());
                    return;
                }
                ticket = response.body();

                String calle =  ticket.getCabecero().getDomicilio().getStreet();   //domicilio1.getString("Calle");
                String cp = ticket.getCabecero().getDomicilio().getZipCode();//domicilio1.getString("CodigoPostal");
                String colonia = ticket.getCabecero().getDomicilio().getColony();//domicilio1.getString("Colonia");
                long empresaid = ticket.getCabecero().getDomicilio().getCompanyId();//domicilio1.getString("EmpresaId");
                String estado = ticket.getCabecero().getDomicilio().getState();//domicilio1.getString("Estado");
                String localidad = ticket.getCabecero().getDomicilio().getLocality();//domicilio1.getString("Localidad");
                String municipio = ticket.getCabecero().getDomicilio().getMunicipality();//domicilio1.getString("Municipio");
                String numeroexterior = ticket.getCabecero().getDomicilio().getStreetNumber();//domicilio1.getString("NumeroExterior");
                String numerointerior = ticket.getCabecero().getDomicilio().getSuiteNumber();//domicilio1.getString("NumeroInterior");
                String pais = ticket.getCabecero().getDomicilio().getCountry();//domicilio1.getString("Pais");

                String razonsocial = ticket.getCabecero().getEmpresa().getCommercialName();  //empresa1.getString("RazonSocial");
                String rfc2 = ticket.getCabecero().getEmpresa().getRfc();//empresa1.getString("Rfc");
                long rfc1 = ticket.getCabecero().getEmpresa().getTaxRegimeId(); //empresa1.getString("TipoRegimenFiscalId");
                String regimenfiscal = null;
                if (rfc1 == 1){
                    regimenfiscal = "REGIMEN GENERAL DE LEY PERSONAS MORALES";
                }

                SQLiteBD data = new SQLiteBD(getApplicationContext());
                data.InsertarDatosEncabezado(String.valueOf(empresaid), razonsocial, regimenfiscal,calle, numeroexterior,numerointerior,colonia,localidad,municipio,estado,pais,cp,rfc2);


            }

            @Override
            public void onFailure(Call<Ticket> call, Throwable t) {
                Toast.makeText(getApplicationContext(),t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    //procedimiento para  cachar el Enter del teclado
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_ENTER:
                edtOct1 = findViewById(R.id.edtOct1);
                edtOct2 = findViewById(R.id.edtOct2);
                edtOct3 = findViewById(R.id.edtOct3);
                edtOct4 = findViewById(R.id.edtOct4);

                oct1 = edtOct1.getText().toString();
                oct2 = edtOct2.getText().toString();
                oct3 = edtOct3.getText().toString();
                oct4 = edtOct4.getText().toString();

//                ip = oct1+"/"+oct2+"/"+oct3+"/"+oct4;

                ip2 = oct1 + "." + oct2 + "." + oct3 + "." + oct4;


                if (oct1.isEmpty()){
                    edtOct1.setError("Ingresa un valor");
                    edtOct1.requestFocus();
                    //happy
                }else{
                    if (oct2.isEmpty()){
                        edtOct2.setError("Ingresa un valor");
                        edtOct2.requestFocus();
                    }else{
                        if (oct3.isEmpty()){
                            edtOct3.setError("Ingresa un valor");
                            edtOct3.requestFocus();
                        }else{
                            if (oct4.isEmpty()){
                                edtOct4.setError("Ingresa un valor");
                                edtOct4.requestFocus();
                            }else{
                                edtOct4.requestFocus();
                                ConectarIP();
                            }
                        }
                    }
                }


                return true;
            default:
                return super.onKeyUp(keyCode, event);
        }
    }

}
