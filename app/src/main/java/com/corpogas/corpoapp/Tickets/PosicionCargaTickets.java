package com.corpogas.corpoapp.Tickets;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.corpogas.corpoapp.Adapters.RVAdapter;
import com.corpogas.corpoapp.Configuracion.SQLiteBD;
import com.corpogas.corpoapp.Entities.Accesos.AccesoUsuario;
import com.corpogas.corpoapp.Entities.Accesos.Control;
import com.corpogas.corpoapp.Entities.Accesos.Posicion;
import com.corpogas.corpoapp.Entities.Classes.RecyclerViewHeaders;
import com.corpogas.corpoapp.Entities.Classes.RespuestaApi;
import com.corpogas.corpoapp.Entities.Common.ProductoTarjetero;
import com.corpogas.corpoapp.Entities.Ventas.Transaccion;
import com.corpogas.corpoapp.Interfaces.Endpoints.EndPoints;
import com.corpogas.corpoapp.Menu_Principal;
import com.corpogas.corpoapp.Modales.Modales;
import com.corpogas.corpoapp.Productos.MostrarCarritoTransacciones;
import com.corpogas.corpoapp.R;
import com.corpogas.corpoapp.VentaCombustible.ProcesoVenta;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PosicionCargaTickets extends AppCompatActivity {
    RecyclerView rcvPosicionCargaTicket;
    String EstacionId,  ipEstacion, numeroTarjetero, lugarproviene, usuario, clave;
    long posicionCargaId,numeroOperativa,cargaNumeroInterno,usuarioid,empleadoNumero,sucursalId;
    Boolean banderaposicionCarga, pendientecobro;
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
        setContentView(R.layout.activity_posicion_carga_tickets);
        init();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvPosicionCargaTicket.setLayoutManager(linearLayoutManager);
        rcvPosicionCargaTicket.setHasFixedSize(true);
        posicionesCarga();

    }

    private void posicionesCarga() {
        bar = new ProgressDialog(PosicionCargaTickets.this);
        bar.setTitle("Buscando Posiciones de Carga");
        bar.setMessage("Ejecutando... ");
        bar.setIcon(R.drawable.gas);
        bar.setCancelable(false);
        bar.show();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://" + data.getIpEstacion() + "/CorpogasService/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        EndPoints obtenerAccesoUsuario = retrofit.create(EndPoints.class);
        Call<RespuestaApi<AccesoUsuario>> call = obtenerAccesoUsuario.getAccesoUsuario(sucursalId, usuario);
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
                        Modales modales = new Modales(PosicionCargaTickets.this);
                        View view1 = modales.MostrarDialogoAlertaAceptar(PosicionCargaTickets.this,mensaje,titulo);
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
                        Modales modales = new Modales(PosicionCargaTickets.this);
                        View view1 = modales.MostrarDialogoAlertaAceptar(PosicionCargaTickets.this,mensaje,titulo);
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
                            long posicionCargaNumeroInterno = posicion.getNumeroInterno();
                            boolean pocioncargadisponible = posicion.isDisponible();
                            boolean pocioncargapendientecobro = posicion.isPendienteCobro();
                            String descripcionoperativa =  posicion.getDescripcionOperativa();
                            String descripcion = posicion.getDescripcion();
                            numeroOperativa = posicion.getOperativa();
                            pendientecobro = posicion.PendienteCobro;   //getString("PendienteCobro");

                            Boolean banderacarga ;

                            if (pendientecobro.equals("true")) {
                                String titulo = "PC " + posicionCargaNumeroInterno;
                                String subtitulo = "";//
                                subtitulo =descripcionoperativa;//
                                lProcesoVenta.add(new RecyclerViewHeaders(titulo,subtitulo,R.drawable.gas,posicionCargaId,posicionCargaNumeroInterno));//
                                banderaposicionCarga = true;
                            }

                        }

                    }

                    if (banderaposicionCarga.equals(false)){
                        //Toast.makeText(posicionFinaliza.this, "No hay Posiciones de Carga para Finalizar Venta", Toast.LENGTH_SHORT).show();
                        String titulo = "AVISO";
                        String mensaje="";
                        mensaje = "No hay posiciones de carga disponibles";

                        Modales modales = new Modales(PosicionCargaTickets.this);
                        View view1 = modales.MostrarDialogoAlertaAceptar(PosicionCargaTickets.this,mensaje,titulo);
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

            }
        });

    }




    private void initializeAdapter() {
        RVAdapter adapter = new RVAdapter(lProcesoVenta);
        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                posicionCargaId=lProcesoVenta.get(rcvPosicionCargaTicket.getChildAdapterPosition(v)).getPosicionCargaId();
                cargaNumeroInterno = lProcesoVenta.get(rcvPosicionCargaTicket.getChildAdapterPosition(v)).getPosicioncarganumerointerno();
            }
        });

        rcvPosicionCargaTicket.setAdapter(adapter);
    }

    private void init() {
        rcvPosicionCargaTicket = (RecyclerView)findViewById(R.id.rcvPosicionCargaTicket);
        data = new SQLiteBD(getApplicationContext());
        this.setTitle(data.getNombreEstacion() + " ( EST.:" + data.getNumeroEstacion() + ")");
        EstacionId = data.getIdEstacion();
        sucursalId = Long.parseLong(data.getIdSucursal());
        ipEstacion = data.getIpEstacion();
        lugarproviene = getIntent().getStringExtra("lugarproviene");
        usuarioid = getIntent().getLongExtra("IdUsuario",0);
        usuario = getIntent().getStringExtra("clave");

    }
}