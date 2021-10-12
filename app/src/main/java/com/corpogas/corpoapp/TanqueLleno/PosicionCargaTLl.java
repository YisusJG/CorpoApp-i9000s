package com.corpogas.corpoapp.TanqueLleno;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.corpogas.corpoapp.Adapters.RVAdapter;
import com.corpogas.corpoapp.Conexion;
import com.corpogas.corpoapp.Configuracion.SQLiteBD;
import com.corpogas.corpoapp.Entities.Accesos.AccesoUsuario;
import com.corpogas.corpoapp.Entities.Accesos.Control;
import com.corpogas.corpoapp.Entities.Accesos.Posicion;
import com.corpogas.corpoapp.Entities.Classes.RecyclerViewHeaders;
import com.corpogas.corpoapp.Entities.Classes.RespuestaApi;
import com.corpogas.corpoapp.Entities.Estaciones.PosicionCarga;
import com.corpogas.corpoapp.Entities.TanqueLleno.RespuestaIniAuto;
import com.corpogas.corpoapp.Entities.Tarjetas.RespuestaTanqueLleno;
import com.corpogas.corpoapp.Menu_Principal;
import com.corpogas.corpoapp.Modales.Modales;
import com.corpogas.corpoapp.R;
import com.corpogas.corpoapp.Interfaces.Endpoints.EndPoints;
import com.corpogas.corpoapp.TanqueLleno.PlanchadoTarjeta.PlanchadoTanqueLleno;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PosicionCargaTLl extends AppCompatActivity {
    RecyclerView rcvPosicionCarga;
    String EstacionId;
    Long sucursalId;
    String ipEstacion;
    String lugarproviene;
    String usuario, numeroempleado;
    String numerotarjeta;
    String NipCliente;
    String NipClientemd5;
    long posicionCargaId;
    long numeroOperativa;
    long cargaNumeroInterno;
    long usuarioid;
    long empleadoNumero;
    Boolean banderaposicionCarga;
    SQLiteBD data;
    RespuestaApi<AccesoUsuario> accesoUsuario;
    List<RecyclerViewHeaders> lrcvPosicionCarga;


    ProgressDialog bar;
    RespuestaTanqueLleno respuestaTanqueLleno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posicion_carga_t_ll);
        init();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvPosicionCarga.setLayoutManager(linearLayoutManager);
        rcvPosicionCarga.setHasFixedSize(true);
        posicionCargaFinaliza();

    }

    private void init() {
        rcvPosicionCarga = (RecyclerView)findViewById(R.id.rcvPosicionCarga);
        data = new SQLiteBD(getApplicationContext());
        this.setTitle(data.getNombreEstacion() + " ( EST.:" + data.getNumeroEstacion() + ")");
        EstacionId = data.getIdEstacion();
        sucursalId = Long.parseLong(data.getIdSucursal());
        ipEstacion = data.getIpEstacion();
        lugarproviene = getIntent().getStringExtra("lugarproviene");
        usuario = data.getClave();
        numerotarjeta = getIntent().getStringExtra("track"); //"6ABE322B"; //
        NipCliente = getIntent().getStringExtra("nipCliente");
        NipClientemd5 = getIntent().getStringExtra("nipMd5Cliente");
        numeroempleado = data.getNumeroEmpleado();

    }

    public void posicionCargaFinaliza(){
        bar = new ProgressDialog(PosicionCargaTLl.this);
        bar.setTitle("Cargando Posiciones de Carga");
        bar.setMessage("Ejecutando... ");
        bar.setIcon(R.drawable.gas);
        bar.setCancelable(false);
        bar.show();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://" + data.getIpEstacion() + "/CorpogasService/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        EndPoints obtenerAccesoUsuario = retrofit.create(EndPoints.class);
        Call<RespuestaApi<AccesoUsuario>> call = obtenerAccesoUsuario.getAccesoUsuarionumeroempleado(sucursalId, numeroempleado);
        call.enqueue(new Callback<RespuestaApi<AccesoUsuario>>() {


            @Override
            public void onResponse(Call<RespuestaApi<AccesoUsuario>> call, Response<RespuestaApi<AccesoUsuario>> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                accesoUsuario = response.body();
                String mensajes =  accesoUsuario.getMensaje();  // jsonObject.getString("Mensaje");
                boolean correcto =  accesoUsuario.isCorrecto();  //jsonObject.getString("Correcto");

                if(accesoUsuario.getObjetoRespuesta() == null)
                {
                    if(correcto == false)
                    {

                        //Toast.makeText(posicionProductos.this, mensaje, Toast.LENGTH_SHORT).show();
                        String titulo = "AVISO";
                        String mensaje = "" + mensajes;
                        Modales modales = new Modales(PosicionCargaTLl.this);
                        View view1 = modales.MostrarDialogoAlertaAceptar(PosicionCargaTLl.this,mensaje,titulo);
                        view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                bar.cancel();
                                modales.alertDialog.dismiss();
                                Intent intent1 = new Intent(getApplicationContext(), Menu_Principal.class);
                                startActivity(intent1);
                                finish();
                            }
                        });
                    }else
                    {
                        //Toast.makeText(posicionProductos.this, mensaje, Toast.LENGTH_SHORT).show();
                        String titulo = "AVISO";
                        String mensaje = "El usuario no tiene asignadas posiciones de carga. " ;
                        Modales modales = new Modales(PosicionCargaTLl.this);
                        View view1 = modales.MostrarDialogoAlertaAceptar(PosicionCargaTLl.this,mensaje,titulo);
                        view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                modales.alertDialog.dismiss();
                                Intent intent1 = new Intent(getApplicationContext(), Menu_Principal.class);
                                startActivity(intent1);
                                finish();
                            }
                        });

                    }

                }else {
                    lrcvPosicionCarga = new ArrayList<>();
                    banderaposicionCarga= false;
                    empleadoNumero = accesoUsuario.getObjetoRespuesta().getNumeroEmpleado();
                    for(Control control : accesoUsuario.getObjetoRespuesta().getControles())
                    {
                        for( Posicion posicion : control.getPosiciones() )
                        {

                            posicionCargaId = posicion.getPosicionCargaId();
                            long posicionCargaNumeroInterno = posicion.getNumeroInterno();
                            boolean pocioncargadisponible = posicion.isDisponible();
                            boolean pocioncargapendientecobro = posicion.isPendienteCobro();
                            String descripcionoperativa =  posicion.getDescripcionOperativa();
                            String descripcion = posicion.getDescripcion();
                            numeroOperativa = posicion.getOperativa();
                            Boolean banderacarga ;
                            if (pocioncargapendientecobro == true){
                                banderacarga = false;
                            }else{
                                banderacarga = true;
                            }
                            if (banderacarga.equals(true)) {
                                if (numeroOperativa == 5){
                                }else {
                                    if (numeroOperativa == 6){

                                    }else{
                                        if (numeroOperativa == 20){

                                        }else{
                                            if (numeroOperativa == 21){

                                            }else{
                                                String titulo = "PC " + posicionCargaNumeroInterno;
                                                String subtitulo = "";//
                                                //    subtitulo = "Magna  |  Premium  |  Diesel";
                                                subtitulo =descripcionoperativa;//
                                                lrcvPosicionCarga.add(new RecyclerViewHeaders(titulo,subtitulo,R.drawable.gas,posicionCargaId,posicionCargaNumeroInterno));//
                                                banderaposicionCarga = true;
                                            }
                                        }
                                    }
                                }
                            }

                        }

                    }

                    if (banderaposicionCarga.equals(false)){
//                        //Toast.makeText(posicionFinaliza.this, "No hay Posiciones de Carga para Finalizar Venta", Toast.LENGTH_SHORT).show();
                        String titulo = "AVISO";
                        String mensaje="";
                        mensaje = "No hay posiciones de carga disponiles";
                        Modales modales = new Modales(PosicionCargaTLl.this);
                        View view1 = modales.MostrarDialogoAlertaAceptar(PosicionCargaTLl.this,mensaje,titulo);
                        view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                modales.alertDialog.dismiss();
                                Intent intent1 = new Intent(getApplicationContext(), Menu_Principal.class);
                                startActivity(intent1);
                                finish();
                            }
                        });
                    }else {
                        initializeAdapter();
                        bar.cancel();
                    }
                }
            }

            @Override
            public void onFailure(Call<RespuestaApi<AccesoUsuario>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                bar.cancel();
            }
        });
    }

    private void initializeAdapter() {
        RVAdapter adapter = new RVAdapter(lrcvPosicionCarga);
        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                posicionCargaId=lrcvPosicionCarga.get(rcvPosicionCarga.getChildAdapterPosition(v)).getPosicionCargaId();
                cargaNumeroInterno = lrcvPosicionCarga.get(rcvPosicionCarga.getChildAdapterPosition(v)).getPosicioncarganumerointerno();
                    //ValidaTransaccionActiva();
                if (lugarproviene.equals("Planchado")){
                    Intent intent1 = new Intent(getApplicationContext(), PlanchadoTanqueLleno.class); //PlanchadoTanqueLleno
                    intent1.putExtra("cargaPosicion", posicionCargaId);
                    startActivity(intent1);
                    finish();
                }else{
                    enviardatos();
                }

            }
        });

        rcvPosicionCarga.setAdapter(adapter);
    }


    private void enviardatos2(){
        //usuario, posicionCargaId,numerotarjeta
//        if (!Conexion.compruebaConexion(this)) {
//            Toast.makeText(getBaseContext(), "Sin conexión a la red ", Toast.LENGTH_SHORT).show();
//            Intent intent1 = new Intent(getApplicationContext(), Menu_Principal.class);
//
//            startActivity(intent1);
//            finish();
//        } else {
//

        RespuestaIniAuto respuestaIniAuto1 = new RespuestaIniAuto(); //= new RespuestaIniAuto(Long.parseLong(data.getIdSucursal()), posicionCargaId , numerotarjeta);
        respuestaIniAuto1.SucursalId = Long.parseLong(data.getIdSucursal());
        respuestaIniAuto1.PosicionCarga = posicionCargaId;
        respuestaIniAuto1.TarjetaCliente = numerotarjeta;

        String nuevo = new Gson().toJson(respuestaIniAuto1);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://" + data.getIpEstacion() + "/CorpogasService/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        EndPoints obtenerIniAuto = retrofit.create(EndPoints.class);
        Call<RespuestaTanqueLleno> call = obtenerIniAuto.getInicializaAuto(usuario, respuestaIniAuto1);
        call.timeout().timeout(120, TimeUnit.SECONDS);
        call.enqueue(new Callback<RespuestaTanqueLleno>() {
            @Override
            public void onResponse(Call<RespuestaTanqueLleno> call, Response<RespuestaTanqueLleno> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                respuestaTanqueLleno = response.body();

                boolean correcto = respuestaTanqueLleno.Correcto;
                Double creditodispoble = respuestaTanqueLleno.CreditoDisponible;
                String numerointernosucursal =  respuestaTanqueLleno.NumeroInternoSucursal;
                boolean odometro = respuestaTanqueLleno.PideOdometro;
                boolean placa = respuestaTanqueLleno.PidePlaca;
                List combustibles = respuestaTanqueLleno.CombustiblesAutorizados;
                final Integer tipocliente = respuestaTanqueLleno.TipoCliente;
                final String sucursalempleados = respuestaTanqueLleno.NumeroEmpleado;

//                combustibles = combustibles.replace("[", "");
//                combustibles = combustibles.replace("]", "");
                final String clave = respuestaTanqueLleno.Clave;
                final Long transaccionId = respuestaTanqueLleno.TransaccionId;
                final Long folio = respuestaTanqueLleno.Folio;
                final String SolicitudEnLitros =  respuestaTanqueLleno.SolicitudEnLitros;
                if (correcto == true) {
                    if (combustibles.size() == 0) {
                        String titulo = "AVISO";
                        String mensajes = "LA TARJETA NO CUENTA CON COMBUSTIBLES ASIGNADOS ";
                        Modales modales = new Modales(PosicionCargaTLl.this);
                        View view1 = modales.MostrarDialogoAlertaAceptar(PosicionCargaTLl.this, mensajes, titulo);
                        view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                modales.alertDialog.dismiss();
                                Intent intent = new Intent(getApplicationContext(), Menu_Principal.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                    } else {
                        if (odometro == true) {
                            try {
                                String titulo = "TANQUE LLENO";
                                String mensaje = "Ingresa el Odometro \n";
                                Modales modales = new Modales(PosicionCargaTLl.this);
                                View viewLectura = modales.MostrarDialogoInsertaDato(PosicionCargaTLl.this, mensaje, titulo);
                                EditText edtProductoCantidad = ((EditText) viewLectura.findViewById(R.id.textInsertarDato));
                                edtProductoCantidad.setInputType(InputType.TYPE_CLASS_NUMBER);
                                //String finalCombustibles = finalCombustibles1;
                                viewLectura.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        modales.alertDialog.dismiss();
                                        String cantidad = edtProductoCantidad.getText().toString();
                                        if (cantidad.isEmpty()) {
                                            edtProductoCantidad.setError("Ingresa el Odometro");
                                        } else {
                                            String odometro = cantidad;
                                            //IngresarPlacas(placa, odometro, numerointernosucursal, sucursalempleados, posicionCargaId, numerotarjeta, clave, tipocliente, transaccionId, folio, SolicitudEnLitros, combustibles);
                                        }
                                    }
                                });
                                viewLectura.findViewById(R.id.buttonNo).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        modales.alertDialog.dismiss();
                                    }
                                });
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            if (placa == true) {
                                try {
                                    String titulo = "TANQUE LLENO";
                                    String mensaje = "Ingresa las Placas \n";
                                    Modales modales = new Modales(PosicionCargaTLl.this);
                                    View viewLectura = modales.MostrarDialogoInsertaDato(PosicionCargaTLl.this, mensaje, titulo);
                                    EditText edtProductoCantidad = ((EditText) viewLectura.findViewById(R.id.textInsertarDato));
                                    edtProductoCantidad.setInputType(InputType.TYPE_CLASS_TEXT);
//                                    String finalCombustibles = combustibles;
                                    viewLectura.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            String cantidad = edtProductoCantidad.getText().toString();
                                            if (cantidad.isEmpty()) {
                                                edtProductoCantidad.setError("Ingresa las Placas");
                                            } else {
                                                String placas = cantidad;
                                                Intent intent = new Intent(getApplicationContext(), eligeLitrosPrecioTL.class); //ProductoTLl
                                                intent.putExtra("placas", placas);
                                                intent.putExtra("odometro", odometro);
                                                intent.putExtra("NumeroInternoEstacion", numerointernosucursal);
                                                intent.putExtra("SucursalEmpleadoId", sucursalempleados);
                                                intent.putExtra("PosicioDeCarga", posicionCargaId);
                                                intent.putExtra("NumeroDeTarjeta", numerotarjeta);
                                                intent.putExtra("ClaveTanqueLleno", clave);
                                                intent.putExtra("Tipocliente", tipocliente);
                                                intent.putExtra("transaccionid", transaccionId);
                                                intent.putExtra("folio", folio);
                                                intent.putExtra("nipCliente", NipCliente);
                                                intent.putExtra("nipMd5Cliente", NipClientemd5);
                                                intent.putExtra("Litros", SolicitudEnLitros);
                                                intent.putExtra("CombustiblesAsociados", String.valueOf(combustibles));
                                                startActivity(intent);
                                                finish();
                                            }
                                        }
                                    });
                                    viewLectura.findViewById(R.id.buttonNo).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            modales.alertDialog.dismiss();
                                        }
                                    });

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                Intent intent = new Intent(getApplicationContext(), eligeLitrosPrecioTL.class); //ProductoTLl
                                intent.putExtra("placas", "");
                                intent.putExtra("odometro", "");
                                intent.putExtra("NumeroInternoEstacion", numerointernosucursal);
                                intent.putExtra("SucursalEmpleadoId", sucursalempleados);
                                intent.putExtra("PosicioDeCarga", posicionCargaId);
                                intent.putExtra("NumeroDeTarjeta", numerotarjeta);
                                intent.putExtra("ClaveTanqueLleno", clave);
                                intent.putExtra("Tipocliente", tipocliente);
                                intent.putExtra("transaccionid", transaccionId);
                                intent.putExtra("folio", folio);
                                intent.putExtra("nipCliente", NipCliente);
                                intent.putExtra("nipMd5Cliente", NipClientemd5);
                                intent.putExtra("Litros", SolicitudEnLitros);
                                intent.putExtra("CombustiblesAsociados", String.valueOf(combustibles));

                                startActivity(intent);
                                finish();
                            }
                        }
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "La contraseña ingresada no es correcta", Toast.LENGTH_LONG).show();
                }
//                Toast.makeText(PosicionCargaTLl.this, "Odometro: " +odometro  + " Placas: "+placa, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(Call<RespuestaTanqueLleno> call, Throwable t) {
                Toast.makeText(PosicionCargaTLl.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });
     }

    private void enviardatos() {
        if (!Conexion.compruebaConexion(this)) {
            Toast.makeText(getBaseContext(), "Sin conexión a la red ", Toast.LENGTH_SHORT).show();
            Intent intent1 = new Intent(getApplicationContext(), Menu_Principal.class);

            startActivity(intent1);
            finish();
        } else {
            //----------------------Aqui va el Volley Si se tecleo contraseña----------------------------
            //Conexion con la base y ejecuta valida clave
            final SQLiteBD data = new SQLiteBD(getApplicationContext());
            ProgressDialog bar = new ProgressDialog(PosicionCargaTLl.this);
            bar.setTitle("Tanque Lleno");
            bar.setMessage("Esperando Respuesta, Iniciando Autorización");
            bar.setIcon(R.drawable.tanquelleno);
            bar.setCancelable(false);
            bar.show();

            String url = "http://"+ipEstacion+"/CorpogasService/api/tanqueLleno/InicioAutorizacion/clave/" + usuario;
            // Utilizamos el metodo Post para validar la contraseña
            StringRequest eventoReq = new StringRequest(Request.Method.POST,url,
                    new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject datos = new JSONObject(response);
                                String correcto = datos.getString("Correcto");
                                if (correcto.equals("false")){
                                    String mensaje = datos.getString("Mensaje");

                                    String titulo = "AVISO";
                                    String mensajes = "" + mensaje;
                                    Modales modales = new Modales(PosicionCargaTLl.this);
                                    View view1 = modales.MostrarDialogoAlertaAceptar(PosicionCargaTLl.this, mensajes, titulo);
                                    view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            modales.alertDialog.dismiss();
                                            Intent intent = new Intent(getApplicationContext(), Menu_Principal.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    });

                                } else {
                                    String creditodispoble = datos.getString("CreditoDisponible");
                                    final String numerointernosucursal = datos.getString("NumeroInternoSucursal");
                                    final String odometro = datos.getString("PideOdometro");

                                    final boolean placa = datos.getBoolean("PidePlaca");
                                    final String sucursalempleados = datos.getString("NumeroEmpleado");
                                    final Integer tipocliente = datos.getInt("TipoCliente");
                                    String combustibles = datos.getString("CombustiblesAutorizados");
                                    combustibles = combustibles.replace("[", "");
                                    combustibles = combustibles.replace("]", "");
                                    final String clave = datos.getString("Clave");
                                    final Long transaccionId = datos.getLong("TransaccionId");
                                    final Long folio = datos.getLong("Folio");
                                    final String SolicitudEnLitros = datos.getString("SolicitudEnLitros");

                                    if (correcto == "true") {
                                        if (combustibles.length() == 0) {
                                            String titulo = "AVISO";
                                            String mensajes = "LA TARJETA NO CUENTA CON COMBUSTIBLES ASIGNADOS ";
                                            Modales modales = new Modales(PosicionCargaTLl.this);
                                            View view1 = modales.MostrarDialogoAlertaAceptar(PosicionCargaTLl.this, mensajes, titulo);
                                            view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    modales.alertDialog.dismiss();
                                                    Intent intent = new Intent(getApplicationContext(), Menu_Principal.class);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            });
                                        } else {
                                            if (odometro.equals("true")) {
                                                try {
                                                    String titulo = "TANQUE LLENO";
                                                    String mensaje = "Ingresa el Odometro \n";
                                                    Modales modales = new Modales(PosicionCargaTLl.this);
                                                    View viewLectura = modales.MostrarDialogoInsertaDato(PosicionCargaTLl.this, mensaje, titulo);
                                                    EditText edtProductoCantidad = ((EditText) viewLectura.findViewById(R.id.textInsertarDato));
                                                    edtProductoCantidad.setInputType(InputType.TYPE_CLASS_NUMBER);
                                                    String finalCombustibles1 = combustibles;
                                                    viewLectura.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View view) {
                                                            String cantidad = edtProductoCantidad.getText().toString();
                                                            modales.alertDialog.dismiss();
                                                            if (cantidad.isEmpty()) {
                                                                edtProductoCantidad.setError("Ingresa el Odometro");
                                                            } else {
                                                                String odometro = cantidad;
                                                                IngresarPlacas(placa, odometro, numerointernosucursal, sucursalempleados, posicionCargaId, numerotarjeta, clave, tipocliente, transaccionId, folio, SolicitudEnLitros, finalCombustibles1);
                                                            }
                                                        }
                                                    });
                                                    viewLectura.findViewById(R.id.buttonNo).setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View view) {
                                                            modales.alertDialog.dismiss();
                                                        }
                                                    });
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            } else {
                                                if (placa == true) {
                                                    try {
                                                        String titulo = "TANQUE LLENO";
                                                        String mensaje = "Ingresa las Placas \n";
                                                        Modales modales = new Modales(PosicionCargaTLl.this);
                                                        View viewLectura = modales.MostrarDialogoInsertaDato(PosicionCargaTLl.this, mensaje, titulo);
                                                        EditText edtProductoCantidad = ((EditText) viewLectura.findViewById(R.id.textInsertarDato));
                                                        edtProductoCantidad.setInputType(InputType.TYPE_CLASS_TEXT);
                                                        String finalCombustibles = combustibles;
                                                        viewLectura.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View view) {
                                                                String cantidad = edtProductoCantidad.getText().toString();
                                                                if (cantidad.isEmpty()) {
                                                                    edtProductoCantidad.setError("Ingresa las Placas");
                                                                } else {
                                                                    String placas = cantidad;
                                                                    Intent intent = new Intent(getApplicationContext(), eligeLitrosPrecioTL.class); //ProductoTLl
                                                                    intent.putExtra("placas", placas);
                                                                    intent.putExtra("odometro", odometro);
                                                                    intent.putExtra("NumeroInternoEstacion", numerointernosucursal);
                                                                    intent.putExtra("SucursalEmpleadoId", sucursalempleados);
                                                                    intent.putExtra("PosicioDeCarga", posicionCargaId);
                                                                    intent.putExtra("NumeroDeTarjeta", numerotarjeta);
                                                                    intent.putExtra("ClaveTanqueLleno", clave);
                                                                    intent.putExtra("Tipocliente", tipocliente);
                                                                    intent.putExtra("transaccionid", transaccionId);
                                                                    intent.putExtra("folio", folio);
                                                                    intent.putExtra("nipCliente", NipCliente);
                                                                    intent.putExtra("nipMd5Cliente", NipClientemd5);
                                                                    intent.putExtra("Litros", SolicitudEnLitros);
                                                                    intent.putExtra("CombustiblesAsociados", finalCombustibles);
                                                                    startActivity(intent);
                                                                    finish();
                                                                }
                                                            }
                                                        });
                                                        viewLectura.findViewById(R.id.buttonNo).setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View view) {
                                                                modales.alertDialog.dismiss();
                                                            }
                                                        });

                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                } else {
                                                    Intent intent = new Intent(getApplicationContext(), eligeLitrosPrecioTL.class); //ProductoTLl
                                                    intent.putExtra("placas", "");
                                                    intent.putExtra("odometro", "");
                                                    intent.putExtra("NumeroInternoEstacion", numerointernosucursal);
                                                    intent.putExtra("SucursalEmpleadoId", sucursalempleados);
                                                    intent.putExtra("PosicioDeCarga", posicionCargaId);
                                                    intent.putExtra("NumeroDeTarjeta", numerotarjeta);
                                                    intent.putExtra("ClaveTanqueLleno", clave);
                                                    intent.putExtra("Tipocliente", tipocliente);
                                                    intent.putExtra("transaccionid", transaccionId);
                                                    intent.putExtra("folio", folio);
                                                    intent.putExtra("nipCliente", NipCliente);
                                                    intent.putExtra("nipMd5Cliente", NipClientemd5);
                                                    intent.putExtra("Litros", SolicitudEnLitros);
                                                    intent.putExtra("CombustiblesAsociados", combustibles);

                                                    startActivity(intent);
                                                    finish();
                                                }
                                            }
                                        }
                                    } else {
                                        Toast.makeText(getApplicationContext(), "La contraseña ingresada no es correcta", Toast.LENGTH_LONG).show();
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        //funcion para capturar errores
                    }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    bar.cancel();
                    //Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                    String titulo = "AVISO";
                    String mensaje = "Problema de Conexión con TanqueLleno";
                    Modales modales = new Modales(PosicionCargaTLl.this);
                    View view1 = modales.MostrarDialogoError(PosicionCargaTLl.this, mensaje);
                    view1.findViewById(R.id.buttonAction).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getApplicationContext(), Menu_Principal.class);
                            startActivity(intent);
                            finish();
                            modales.alertDialog.dismiss();
                        }
                    });

                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    // Posting parameters to login url
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("SucursalId", data.getIdSucursal()); //EstacionId
                    params.put("PosicionCarga", String.valueOf(posicionCargaId));
                    params.put("TarjetaCliente", numerotarjeta);
                    params.put("NIP", NipClientemd5);
                    return params;
                }
            };

            // Añade la peticion a la cola
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            eventoReq.setRetryPolicy(new DefaultRetryPolicy(15000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(eventoReq);
        }
    }


    private void IngresarPlacas(boolean placa, final String odometro, final String numerointernosucursal,
                                final String sucursalempleados, final Long posi, final String track,
                                final String clave, final Integer tipocliente, final Long transaccionId, final Long folio, final String SolicitudEnLitros, String combustibles) {
        if (placa == true){
            try {
                String titulo = "TANQUE LLENO";
                String mensaje = "Ingresa las Placas \n" ;
                Modales modales = new Modales(PosicionCargaTLl.this);
                View viewLectura = modales.MostrarDialogoInsertaDato(PosicionCargaTLl.this, mensaje, titulo);
                EditText edtProductoCantidad = ((EditText) viewLectura.findViewById(R.id.textInsertarDato));
                edtProductoCantidad.setInputType(InputType.TYPE_CLASS_TEXT);
                viewLectura.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        modales.alertDialog.dismiss();
                        String cantidad = edtProductoCantidad.getText().toString();
                        if (cantidad.isEmpty()){
                            edtProductoCantidad.setError("Ingresa las Placas");
                        }else {
                            String placas = cantidad;
                            Intent intent = new Intent(getApplicationContext(), eligeLitrosPrecioTL.class); //ProductoTLl
                            intent.putExtra("placas",placas);
                            intent.putExtra("odometro",odometro);
                            intent.putExtra("NumeroInternoEstacion", numerointernosucursal);
                            intent.putExtra("SucursalEmpleadoId",sucursalempleados);
                            intent.putExtra("PosicioDeCarga", posi);
                            intent.putExtra("NumeroDeTarjeta",track);
                            intent.putExtra("ClaveTanqueLleno",clave);
                            intent.putExtra("Tipocliente",tipocliente);
                            intent.putExtra("transaccionid",transaccionId);
                            intent.putExtra("folio", folio);
                            intent.putExtra("nipCliente", NipCliente);
                            intent.putExtra("nipMd5Cliente", NipClientemd5);
                            intent.putExtra("Litros", SolicitudEnLitros);
                            intent.putExtra("CombustiblesAsociados", String.valueOf(combustibles));

                            startActivity(intent);
                            finish();
                        }
                    }
                });
                viewLectura.findViewById(R.id.buttonNo).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        modales.alertDialog.dismiss();
                    }
                });
            }catch (Exception e){
                e.printStackTrace();
            }
        } else{
            Intent intent = new Intent(getApplicationContext(), eligeLitrosPrecioTL.class);
            intent.putExtra("placas","");
            intent.putExtra("odometro",odometro);
            intent.putExtra("NumeroInternoEstacion", numerointernosucursal);
            intent.putExtra("SucursalEmpleadoId",sucursalempleados);
            intent.putExtra("PosicioDeCarga", posi);
            intent.putExtra("NumeroDeTarjeta",track);
            intent.putExtra("ClaveTanqueLleno",clave);
            intent.putExtra("Tipocliente",tipocliente);
            intent.putExtra("transaccionid",transaccionId);
            intent.putExtra("folio", folio);
            intent.putExtra("nipCliente", NipCliente);
            intent.putExtra("nipMd5Cliente", NipClientemd5);
            intent.putExtra("Litros", SolicitudEnLitros);
            intent.putExtra("CombustiblesAsociados", String.valueOf(combustibles));

            startActivity(intent);
            finish();
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