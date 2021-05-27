package com.corpogas.corpoapp.VentaCombustible;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.corpogas.corpoapp.Adapters.RVAdapter;
import com.corpogas.corpoapp.Configuracion.SQLiteBD;
import com.corpogas.corpoapp.EnProcesoDeDesarrollo.EnDesarrollo;
import com.corpogas.corpoapp.Entities.Accesos.AccesoUsuario;
import com.corpogas.corpoapp.Entities.Accesos.Control;
import com.corpogas.corpoapp.Entities.Accesos.Posicion;
import com.corpogas.corpoapp.Entities.Classes.RecyclerViewHeaders;
import com.corpogas.corpoapp.Entities.Classes.RespuestaApi;
import com.corpogas.corpoapp.Entities.Common.ProductoTarjetero;
import com.corpogas.corpoapp.Entities.Ventas.Transaccion;
import com.corpogas.corpoapp.Menu_Principal;
import com.corpogas.corpoapp.Modales.Modales;
import com.corpogas.corpoapp.ObtenerClave.ClaveEmpleado;
import com.corpogas.corpoapp.Productos.MostrarCarritoTransacciones;
import com.corpogas.corpoapp.R;
import com.corpogas.corpoapp.Request.Interfaces.EndPoints;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProcesoVenta extends AppCompatActivity {
    RecyclerView rcvProcesoVenta;
    String  usuarioid, empleadoNumero;
    long posicionCargaId,numeroOperativa;
    String EstacionId, sucursalId, ipEstacion, numeroTarjetero, lugarproviene, usuario, clave;
    Boolean banderaposicionCarga;
    SQLiteBD data;
    RespuestaApi<AccesoUsuario> accesoUsuario;
    List<RecyclerViewHeaders> lProcesoVenta;
    RespuestaApi<List<ProductoTarjetero>> respuestaApiProductoTarjetero;
    RespuestaApi<Boolean> respuestaApiAutorizaDespacho;
    RespuestaApi<Boolean> respuestaApiTicketPendienteCobro;
    RespuestaApi<Transaccion> respuestaApiTransaccion;
    ProgressDialog bar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proceso_venta);
        init();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvProcesoVenta.setLayoutManager(linearLayoutManager);
        rcvProcesoVenta.setHasFixedSize(true);
        posicionCargaFinaliza();
    }

    private void init() {
        rcvProcesoVenta = (RecyclerView)findViewById(R.id.rcvProcesoVenta);
        data = new SQLiteBD(getApplicationContext());
        this.setTitle(data.getRazonSocial());
        this.setTitle(data.getNombreEsatcion() + " ( EST.:" + data.getNumeroEstacion() + ")");
        EstacionId = data.getIdEstacion();
        sucursalId = data.getIdSucursal();
        ipEstacion = data.getIpEstacion();
        lugarproviene = getIntent().getStringExtra("lugarproviene");
        usuarioid = getIntent().getStringExtra("usuario");
        usuario = getIntent().getStringExtra("clave");
    }


    public void posicionCargaFinaliza(){
        bar = new ProgressDialog(ProcesoVenta.this);
        bar.setTitle("Cargando Posiciones de Carga");
        bar.setMessage("Ejecutando... ");
        bar.setIcon(R.drawable.gas);
        bar.setCancelable(false);
        bar.show();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://" + data.getIpEstacion() + "/CorpogasService_Entities/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        EndPoints obtenerAccesoUsuario = retrofit.create(EndPoints.class);
        Call<RespuestaApi<AccesoUsuario>> call = obtenerAccesoUsuario.getAccesoUsuario(data.getIdSucursal(), usuario);
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
                        Modales modales = new Modales(ProcesoVenta.this);
                        View view1 = modales.MostrarDialogoAlertaAceptar(ProcesoVenta.this,mensaje,titulo);
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
                        Modales modales = new Modales(ProcesoVenta.this);
                        View view1 = modales.MostrarDialogoAlertaAceptar(ProcesoVenta.this,mensaje,titulo);
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

                }else
                {
                    lProcesoVenta = new ArrayList<>();
                    banderaposicionCarga= false;
                    empleadoNumero = accesoUsuario.getObjetoRespuesta().getNumeroEmpleado();
                    for(Control control : accesoUsuario.getObjetoRespuesta().getControles())
                    {
                        for( Posicion posicion : control.getPosiciones() )
                        {

                            posicionCargaId = posicion.getPosicionCargaId();
                            long posicioncarganumerointerno = posicion.getNumeroInterno();
                            boolean pocioncargadisponible = posicion.isDisponible();
                            boolean pocioncargapendientecobro = posicion.isPendienteCobro();
                            String descripcionoperativa =  posicion.getDescripcionOperativa();
                            String descripcion = posicion.getDescripcion();
                            numeroOperativa = posicion.getOperativa();
                            Boolean banderacarga ;
                            if (lugarproviene.equals("IniciaVenta")){//Inicia despacho
                                if (pocioncargapendientecobro == true){
                                    banderacarga = false;
                                }else{
                                    banderacarga = true;
                                }
                            }else{//Finaliza despacho
                                if (pocioncargapendientecobro == true){
                                    banderacarga = true;
                                }else{
                                    banderacarga= false;
                                }
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
                                                String titulo = "PC " + posicioncarganumerointerno;
                                                String subtitulo = "";//
                                                if (lugarproviene.equals("1")) {
                                                    subtitulo = "Magna  |  Premium  |  Diesel";
//
                                                } else {
                                                    subtitulo =descripcionoperativa;//
                                                }
                                                lProcesoVenta.add(new RecyclerViewHeaders(titulo,subtitulo,R.drawable.gas,posicionCargaId));//
                                                banderaposicionCarga = true;
                                            }
                                        }
                                    }
                                }
                            }

                        }

                    }

                    if (banderaposicionCarga.equals(false)){
                        //Toast.makeText(posicionFinaliza.this, "No hay Posiciones de Carga para Finalizar Venta", Toast.LENGTH_SHORT).show();
                        String titulo = "AVISO";
                        String mensaje="";
                        switch(lugarproviene) {
                            case "IniciaVenta":
                                mensaje = "No hay posiciones de carga disponiles";
                                break;
                            case "FinalizaVenta":
                                mensaje = "No hay posiciones de carga disponibles para Finalizar la venta";
                                break;
                            case "Predeterminado":
                                mensaje = "No hay posiciones de carga disponibles";
                                break;
                            default:
                        }
                        Modales modales = new Modales(ProcesoVenta.this);
                        View view1 = modales.MostrarDialogoAlertaAceptar(ProcesoVenta.this,mensaje,titulo);
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
                    }
                }
            }

            @Override
            public void onFailure(Call<RespuestaApi<AccesoUsuario>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void initializeAdapter() {
        RVAdapter adapter = new RVAdapter(lProcesoVenta);
        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                posicionCargaId=lProcesoVenta.get(rcvProcesoVenta.getChildAdapterPosition(v)).getPosicionCargaId();

                if (lugarproviene.equals("IniciaVenta")) {
                    ValidaTransaccionActiva(posicionCargaId,numeroOperativa);
                } else {
                    validaPosicionDisponible(posicionCargaId);
                }
//                Toast.makeText(getApplicationContext(),"Seleccion :" + lProcesoVenta.get(rcvProcesoVenta.getChildAdapterPosition(v)).getTitulo(), Toast.LENGTH_SHORT).show();

            }
        });

        rcvProcesoVenta.setAdapter(adapter);
    }

    private void ValidaTransaccionActiva(long posicionCargaId, long numeroOperativa){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://" + data.getIpEstacion() + "/CorpogasService_Entities/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        EndPoints productosProcedencia = retrofit.create(EndPoints.class);
        Call<RespuestaApi<List<ProductoTarjetero>>> call = productosProcedencia.getProductosProcedencia(sucursalId,posicionCargaId);
        call.enqueue(new Callback<RespuestaApi<List<ProductoTarjetero>>>() {


            @Override
            public void onResponse(Call<RespuestaApi<List<ProductoTarjetero>>> call, Response<RespuestaApi<List<ProductoTarjetero>>> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                respuestaApiProductoTarjetero = response.body();
                Boolean banderaConDatos;
                boolean Correcto = respuestaApiProductoTarjetero.isCorrecto(); //jsonObject.getString("Correcto");
                if (respuestaApiProductoTarjetero.getObjetoRespuesta() == null){
                    banderaConDatos = false;
                }else{
                    if (respuestaApiProductoTarjetero.getObjetoRespuesta().equals("[]")){
                        banderaConDatos = false;
                    }else{
                        banderaConDatos=true;
                    }
                }

                if (Correcto == true ){
                    if (banderaConDatos.equals(false)){
                        //Envia a Despacho
                        solicitadespacho();
                    }else{
                        //Envia a Mostrar CArrito TRansacciones
                        Intent intente = new Intent(getApplicationContext(), EnDesarrollo.class);//MostrarCarritoTransacciones
                        //se envia el id seleccionado a la clase Usuario Producto
                        intente.putExtra("posicion", posicionCargaId);
                        intente.putExtra("usuario", usuarioid);
                        intente.putExtra("cadenaproducto", "");
                        intente.putExtra("lugarproviene", "Despacho");
                        intente.putExtra("numeroOperativa", numeroOperativa);
                        intente.putExtra("cadenarespuesta", respuestaApiProductoTarjetero);
                        intente.putExtra("clave", usuario);
                        intente.putExtra("nombrecompleto", "Nombrecompleto");
                        //Ejecuta la clase del Usuario producto
                        startActivity(intente);
                        //Finaliza activity
                        finish();

                    }
                }else
                {
                    solicitadespacho();
                }

            }

            @Override
            public void onFailure(Call<RespuestaApi<List<ProductoTarjetero>>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void solicitadespacho() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://" + data.getIpEstacion() + "/CorpogasService_Entities/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        EndPoints AutorizaDespacho = retrofit.create(EndPoints.class);
        Call<RespuestaApi<Boolean>> call = AutorizaDespacho.getAutorizaDespacho(posicionCargaId,empleadoNumero);
        call.enqueue(new Callback<RespuestaApi<Boolean>>() {


            @Override
            public void onResponse(Call<RespuestaApi<Boolean>> call, Response<RespuestaApi<Boolean>> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                respuestaApiAutorizaDespacho = response.body();
                boolean correctoautoriza =  respuestaApiAutorizaDespacho.isCorrecto();
                String mensajeautoriza =   respuestaApiAutorizaDespacho.getMensaje();

                if (correctoautoriza ==true) {
                    enviaMunu();
                } else {
                    String titulo = "AVISO";
                    String mensaje = "La posición de carga se encuentra Ocupada";
                    Modales modales = new Modales(ProcesoVenta.this);
                    View view1 = modales.MostrarDialogoAlertaAceptar(ProcesoVenta.this,mensaje,titulo);
                    view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            modales.alertDialog.dismiss();
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<RespuestaApi<Boolean>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void enviaMunu() {
        String titulo = "AVISO";
        String mensaje = "Listo para Iniciar Despacho";
        final Modales modales = new Modales(ProcesoVenta.this);
        View view1 = modales.MostrarDialogoCorrecto(ProcesoVenta.this,mensaje);
        view1.findViewById(R.id.buttonAction).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modales.alertDialog.dismiss();
                Intent intent1 = new Intent(getApplicationContext(), Menu_Principal.class);
                startActivity(intent1);
                finish();
            }
        });
    }

    private void validaPosicionDisponible(long posicionCargaId) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://" + data.getIpEstacion() + "/CorpogasService_Entities/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        EndPoints TicketPendienteCobro = retrofit.create(EndPoints.class);
        Call<RespuestaApi<Boolean>> call = TicketPendienteCobro.getTicketPendienteCobro(sucursalId, posicionCargaId);
        call.enqueue(new Callback<RespuestaApi<Boolean>>() {


            @Override
            public void onResponse(Call<RespuestaApi<Boolean>> call, Response<RespuestaApi<Boolean>> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                respuestaApiTicketPendienteCobro = response.body();
                boolean correcto = respuestaApiTicketPendienteCobro.isCorrecto(); //p1.getString("ObjetoRespuesta");

                if (correcto == true) {
                    finalizaventa(posicionCargaId);
                } else {
                    //Despacho en proceso
                    String mensaje = respuestaApiTicketPendienteCobro.getMensaje();   //p1.getString("Mensaje");
                    AlertDialog.Builder builder = new AlertDialog.Builder(ProcesoVenta.this);
                    builder.setTitle("Finaliza Venta");
                    builder.setCancelable(false);
                    builder.setMessage("Despacho en proceso: " + mensaje)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent intente = new Intent(getApplicationContext(), Menu_Principal.class);
                                    startActivity(intente);
                                    finish();
                                }
                            }).show();
                }

            }

            @Override
            public void onFailure(Call<RespuestaApi<Boolean>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void finalizaventa(long posicionCargaId) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://" + data.getIpEstacion() + "/CorpogasService_Entities/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        EndPoints postFinalizaVenta = retrofit.create(EndPoints.class);
        Call<RespuestaApi<Transaccion>> call = postFinalizaVenta.getPostFinalizaVenta(sucursalId,posicionCargaId,empleadoNumero);
        call.enqueue(new Callback<RespuestaApi<Transaccion>>() {


            @Override
            public void onResponse(Call<RespuestaApi<Transaccion>> call, Response<RespuestaApi<Transaccion>> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                respuestaApiTransaccion = response.body();
                String mensaje = respuestaApiTransaccion.getMensaje();   //respuesta.getString("Mensaje");
//                Transaccion objetoRespuesta = respuestaApiTransaccion.getObjetoRespuesta(); //respuesta.getString("ObjetoRespuesta");
                if(respuestaApiTransaccion.getObjetoRespuesta() == null)
                {
                    String titulo = "AVISO";
                    //String mensaje = "No hay Posiciones de Carga para Finalizar Venta";
                    Modales modales = new Modales(ProcesoVenta.this);
                    View view1 = modales.MostrarDialogoAlertaAceptar(ProcesoVenta.this,mensaje,titulo);
                    view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            modales.alertDialog.dismiss();
                            Intent intent1 = new Intent(getApplicationContext(), Menu_Principal.class);
                            startActivity(intent1);
                            finish();
                        }
                    });

                }else
                {
                    String titulo = "AVISO";
                    String mensajes = "Venta Finalizada";
                    final Modales modales = new Modales(ProcesoVenta.this);
                    View view1 = modales.MostrarDialogoCorrecto(ProcesoVenta.this,mensajes);
                    view1.findViewById(R.id.buttonAction).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            modales.alertDialog.dismiss();
                            Intent intent = new Intent(getApplicationContext(), Menu_Principal.class);
                            startActivity(intent);
                            finish();
                        }
                    });

                }

            }

            @Override
            public void onFailure(Call<RespuestaApi<Transaccion>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}