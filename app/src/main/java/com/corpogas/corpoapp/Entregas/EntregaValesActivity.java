package com.corpogas.corpoapp.Entregas;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.corpogas.corpoapp.Configuracion.CorteDB;
import com.corpogas.corpoapp.Configuracion.SQLiteBD;
import com.corpogas.corpoapp.Entities.Accesos.AccesoUsuario;
import com.corpogas.corpoapp.Entities.Classes.RespuestaApi;
import com.corpogas.corpoapp.Entities.Cortes.CierreValePapel;
import com.corpogas.corpoapp.Entregas.Adapters.AdapterValesPapel;
import com.corpogas.corpoapp.Entregas.Adapters.RVAdapterValesPapel;
import com.corpogas.corpoapp.Entregas.Entities.PaperVoucherType;
import com.corpogas.corpoapp.Interfaces.Endpoints.EndPoints;
import com.corpogas.corpoapp.Modales.Modales;
import com.corpogas.corpoapp.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EntregaValesActivity extends AppCompatActivity {

    List<PaperVoucherType> paperVoucherType;
    String ipEstacion,tituloValePapel;
    long idValePapel,sucursalId,estacionId,cierreId,islaId,usuarioId;
    CorteDB dbCorte;
    SQLiteBD db;
    Spinner snipperTipoVales;
    ImageView imgDetalleVales;
    private AdapterValesPapel adapterValesPapel;
    TextView txtTituloCantidad, txtTituloDenominacion, txtTituloTotal,txtTituloDesgloceVales,valesImporteTxt;
    RecyclerView rcvValesPapel,recyclerView;
    List<CierreValePapel> lCierreValesPapel;
    RespuestaApi<AccesoUsuario> respuestaApiAccesoUsuario;
//    boolean mostrarDegloceVales;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrega_vales);
        init();
        if(paperVoucherType.size() == 0) insertarValesPapelSqlite();
        tipoValesPapel();
        onclicks();
    }


    private void insertarValesPapelSqlite() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://" + ipEstacion + "/CorpogasService/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        EndPoints obtenValesPapel = retrofit.create(EndPoints.class);
        Call<List<PaperVoucherType>> call = obtenValesPapel.getTipoValePapel();
        call.enqueue(new Callback<List<PaperVoucherType>>() {
            @Override
            public void onResponse(Call<List<PaperVoucherType>> call, Response<List<PaperVoucherType>> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                paperVoucherType = response.body();
                dbCorte.InsertarTipoValePapeles(0,"SELECCIONE",0);

                for(PaperVoucherType item: paperVoucherType) {
                    dbCorte.InsertarTipoValePapeles(item.getId() == 1 ? R.drawable.gasopass : item.getId() == 2 ?
                                    R.drawable.efectivale : item.getId() == 3 ? R.drawable.accor : R.drawable.sivale,
                            item.getDescription(), item.getId());
                    dbCorte.InsertarCierreValePapel(sucursalId, estacionId, cierreId, item.getId(), 0, 2, 0, item.getDescription());
                    dbCorte.InsertarCierreValePapel(sucursalId, estacionId, cierreId, item.getId(), 0, 5, 0, item.getDescription());
                    dbCorte.InsertarCierreValePapel(sucursalId, estacionId, cierreId, item.getId(), 0, 10, 0, item.getDescription());
                    dbCorte.InsertarCierreValePapel(sucursalId, estacionId, cierreId, item.getId(), 0, 20, 0, item.getDescription());
                    dbCorte.InsertarCierreValePapel(sucursalId, estacionId, cierreId, item.getId(), 0, 50, 0, item.getDescription());
                    dbCorte.InsertarCierreValePapel(sucursalId, estacionId, cierreId, item.getId(), 0, 100, 0, item.getDescription());
                    dbCorte.InsertarCierreValePapel(sucursalId, estacionId, cierreId, item.getId(), 0, 200, 0, item.getDescription());
                    dbCorte.InsertarCierreValePapel(sucursalId, estacionId, cierreId, item.getId(), 0, 500, 0, item.getDescription());
                    dbCorte.InsertarCierreValePapel(sucursalId, estacionId, cierreId, item.getId(), 0, 1000, 0, item.getDescription());
                }
                paperVoucherType = dbCorte.getTipoValePapeles();
                tipoValesPapel();

            }

            @Override
            public void onFailure(Call<List<PaperVoucherType>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void init() {
        db = new SQLiteBD(getApplicationContext());
        dbCorte = new CorteDB(getApplicationContext());
        paperVoucherType = dbCorte.getTipoValePapeles();
        ipEstacion = db.getIpEstacion();
        sucursalId = Long.parseLong(db.getIdSucursal());
        estacionId = Long.parseLong(db.getIdEstacion());
        cierreId =   getIntent().getLongExtra("cierreId",0);
        rcvValesPapel = findViewById(R.id.rcvValesPapel);
        snipperTipoVales = findViewById(R.id.idSpinnerFragment);
        txtTituloCantidad = findViewById(R.id.txtTituloCantidad);
        txtTituloDenominacion = findViewById(R.id.txtTituloDenominacion);
        txtTituloTotal = findViewById(R.id.txtTituloTotal);
        txtTituloDesgloceVales = findViewById(R.id.txtTituloDesgloceVales);
        imgDetalleVales = findViewById(R.id.imgDetalleVale);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext().getApplicationContext());

        rcvValesPapel.setLayoutManager(linearLayoutManager);
        rcvValesPapel.setHasFixedSize(true);
//        mostrarDegloceVales = true;
    }

    private void tipoValesPapel() {
        adapterValesPapel = new AdapterValesPapel(getApplicationContext(),paperVoucherType);
        snipperTipoVales.setAdapter(adapterValesPapel);

        snipperTipoVales.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                PaperVoucherType clickedTitulos = (PaperVoucherType) parent.getItemAtPosition(position);
                idValePapel = clickedTitulos.getId();
                tituloValePapel = clickedTitulos.getDescription();
                if(idValePapel != 0) {
                    txtTituloCantidad.setVisibility(view.VISIBLE);
                    txtTituloDenominacion.setVisibility(view.VISIBLE);
                    txtTituloTotal.setVisibility(view.VISIBLE);
                    txtTituloDesgloceVales.setVisibility(view.VISIBLE);
                    imgDetalleVales.setVisibility(view.VISIBLE);

                    initializeAdapterFormasDePago(dbCorte.getIdValePapel(idValePapel));

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void initializeAdapterFormasDePago(List<CierreValePapel>lValesPapel) {
        RVAdapterValesPapel adapter = new RVAdapterValesPapel(lValesPapel);


        adapter.setOnClickListener(v -> {
            if(!adapter.isClickable) {
                adapter.isClickable = true;
                int updateIndex = rcvValesPapel.getChildAdapterPosition(v);
                double denominacion = lValesPapel.get(rcvValesPapel.getChildAdapterPosition(v)).getDenominacion();
                double cantidadValeRecuperado = lValesPapel.get(rcvValesPapel.getChildAdapterPosition(v)).getCantidad();


                String mensaje = "Agregar o corregir cantidad: ";
                final Modales modales = new Modales(EntregaValesActivity.this);
                View viewLectura = modales.MostrarDialogoInsertaDato(EntregaValesActivity.this, mensaje, tituloValePapel);
                final EditText edtCantidadVale = ((EditText) viewLectura.findViewById(R.id.textInsertarDato));
                edtCantidadVale.setInputType(InputType.TYPE_CLASS_NUMBER);
                viewLectura.findViewById(R.id.buttonYes).setOnClickListener(view -> {

                    String cantidadVale = edtCantidadVale.getText().toString();
                    if (cantidadVale.equals("")) {
                        edtCantidadVale.setError("Ingresaste un valor");
                    } else {
                        edtCantidadVale.setText(String.valueOf((long) cantidadValeRecuperado));
                        long totalVale = (long) denominacion * Long.parseLong(cantidadVale);
                        lValesPapel.set(updateIndex, new CierreValePapel(sucursalId, estacionId, cierreId, idValePapel, Long.parseLong(cantidadVale), (long) denominacion, totalVale, tituloValePapel));
                        dbCorte.updateCierreValePapel(idValePapel, denominacion, Double.parseDouble(cantidadVale), totalVale);
                        adapter.notifyItemChanged(updateIndex);
                        adapter.isClickable = false;
                        modales.alertDialog.dismiss();
                    }
                });

                viewLectura.findViewById(R.id.buttonNo).setOnClickListener(view ->
                {
                    adapter.isClickable = false;
                    modales.alertDialog.dismiss();
                });
            }
        });

        rcvValesPapel.setAdapter(adapter);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void onclicks() {
        imgDetalleVales.setOnClickListener(v -> {
                lCierreValesPapel = new ArrayList<>();
                lCierreValesPapel = dbCorte.getAllCierreValePapel();

            String titulo = "CONFIRMACION";
            String mensaje = "Ingresa NIP de confirmación.";
            Modales modales = new Modales(EntregaValesActivity.this);
            View viewLectura = modales.MostrarDialogoInsertaDato(EntregaValesActivity.this, mensaje, titulo);
            EditText edtNipAutorizacion= ((EditText) viewLectura.findViewById(R.id.textInsertarDato));
            edtNipAutorizacion.setInputType(InputType.TYPE_CLASS_NUMBER);
            viewLectura.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String nipAutorizacion = edtNipAutorizacion.getText().toString();
                    if (nipAutorizacion.isEmpty()){
                        edtNipAutorizacion.setError("Ingresa NIP");
                        return;
                    }else{
                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl("http://"+ ipEstacion  +"/corpogasService/")//http://" + data.getIpEstacion() + "/corpogasService_Entities_token/
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();

                        EndPoints obtenerAccesoUsuario = retrofit.create(EndPoints.class);
                        Call<RespuestaApi<AccesoUsuario>> call = obtenerAccesoUsuario.getAccesoUsuario(sucursalId, nipAutorizacion);
                        call.enqueue(new Callback<RespuestaApi<AccesoUsuario>>() {

                            @RequiresApi(api = Build.VERSION_CODES.N)
                            @Override
                            public void onResponse(Call<RespuestaApi<AccesoUsuario>> call, Response<RespuestaApi<AccesoUsuario>> response) {
                                if (!response.isSuccessful()) {
                                    return;
                                }
                                respuestaApiAccesoUsuario = response.body();
                                if(respuestaApiAccesoUsuario.Correcto){
                                    if(respuestaApiAccesoUsuario.getObjetoRespuesta().getNumeroInternoRol() == 3
                                    ||(respuestaApiAccesoUsuario.getObjetoRespuesta().getNumeroInternoRol() == 1)){
                                        Retrofit retrofit = new Retrofit.Builder()
                                                .baseUrl("http://"+ ipEstacion  +"/corpogasService/")//http://" + data.getIpEstacion() + "/corpogasService_Entities_token/
                                                .addConverterFactory(GsonConverterFactory.create())
                                                .build();

                                        EndPoints enviarVales = retrofit.create(EndPoints.class);
                                    }else{
                                        edtNipAutorizacion.setError("Solo el jefe de isla o gerente pueden confirmar");
                                    }

                                }else{
                                    edtNipAutorizacion.setError(respuestaApiAccesoUsuario.getMensaje());
                                }


                            }

                            @Override
                            public void onFailure(Call<RespuestaApi<AccesoUsuario>> call, Throwable t) {
                                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        });
                    }
//                    modales.alertDialog.dismiss();
                }
            });
            viewLectura.findViewById(R.id.buttonNo).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    modales.alertDialog.dismiss();
                }
            });

        });
    }
}