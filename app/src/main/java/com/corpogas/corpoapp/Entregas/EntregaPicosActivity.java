package com.corpogas.corpoapp.Entregas;

import android.os.Build;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.corpogas.corpoapp.Configuracion.SQLiteBD;
import com.corpogas.corpoapp.Entities.Classes.RespuestaApi;
import com.corpogas.corpoapp.Entities.Estaciones.Empleado;
import com.corpogas.corpoapp.Entities.Estaciones.RecepcionFajilla;
import com.corpogas.corpoapp.Entities.Estaciones.ResumenFajilla;
import com.corpogas.corpoapp.Entities.Sucursales.PriceBankRoll;
import com.corpogas.corpoapp.Entities.Virtuales.CierreVariables;
import com.corpogas.corpoapp.Interfaces.Endpoints.EndPoints;
import com.corpogas.corpoapp.Login.Adapters.RVAdapterPicos;
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

public class EntregaPicosActivity extends AppCompatActivity {

    RecyclerView rcvPicos;
    EditText editTextMorralla, editText;
    TextView textSumaTotalBilletes;
    Button btnAceptar;
    SQLiteBD dataBase;

    String ipEstacion, titulo, mensaje, cantidadBilletes;
    int  sumaTotal;
    long sucursalId;
    String idusuario;
    double cantiMorralla;
    Modales modales;

    List<RecepcionFajilla> recepcionFajillas = new ArrayList<>();
    Spinner snipperTipoVales;



    private void init() {
        rcvPicos = findViewById(R.id.rcvPicos);
        editTextMorralla = (EditText) findViewById(R.id.editMorrallaPicos);
        textSumaTotalBilletes = (TextView) findViewById(R.id.textTotalBilletesPicos);
        btnAceptar = (Button) findViewById(R.id.btnAceptarPicos);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getApplicationContext());
        rcvPicos.setLayoutManager(linearLayoutManager);
        rcvPicos.setHasFixedSize(true);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrega_picos);

        init();
        setVariables();
        obtenerCierreVariables();
        SetRecyclerView();
        onClickButton();
    }

    private void obtenerCierreVariables() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://" + ipEstacion + "/corpogasService/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        EndPoints obtenerCierreVariables = retrofit.create(EndPoints.class);
        Call<RespuestaApi<CierreVariables>> call = obtenerCierreVariables.getCierreVariables(sucursalId);
        call.enqueue(new Callback<RespuestaApi<CierreVariables>>() {

            @Override
            public void onResponse(Call<RespuestaApi<CierreVariables>> call, Response<RespuestaApi<CierreVariables>> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                RespuestaApi<CierreVariables> respuestaApiCierreVariables = response.body();

                for (Integer item : respuestaApiCierreVariables.getObjetoRespuesta().Denominaciones) {
                    recepcionFajillas.add(new RecepcionFajilla(sucursalId, 3, 0, item.intValue(), 0));
                }

                for (PriceBankRoll item : respuestaApiCierreVariables.getObjetoRespuesta().PrecioFajillas){

                    if (item.BankRollType == 1){
                        dataBase.InsertarPrecioFajillas(sucursalId,1,item.getPrice());

                    }else{
                        if (item.BankRollType == 2){
                            dataBase.InsertarPrecioFajillas(sucursalId,2,item.getPrice());
                        }
                    }
                }
                SetRecyclerView();
            }

            @Override
            public void onFailure(Call<RespuestaApi<CierreVariables>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void validaClave(String clave) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://" + ipEstacion + "/corpogasService/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        EndPoints validaClave = retrofit.create(EndPoints.class);
        Call<RespuestaApi<Empleado>> call = validaClave.getDatosEmpleado(clave);
        call.enqueue(new Callback<RespuestaApi<Empleado>>() {

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<RespuestaApi<Empleado>> call, Response<RespuestaApi<Empleado>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Ocurrio un error.", Toast.LENGTH_SHORT).show();
                    return;
                }
                RespuestaApi<Empleado> respuestaApi = response.body();

                if (respuestaApi.Correcto){
                    Empleado empleado = respuestaApi.getObjetoRespuesta();

                    if (empleado.getRolId() == 1 || empleado.getRolId() == 3) {

                        recepcionFajillas = recepcionFajillas.stream().filter(x -> x.Cantidad != 0).collect(Collectors.toList());


                        Retrofit retrofit2 = new Retrofit.Builder()
                                .baseUrl("http://" + dataBase.getIpEstacion() + "/CorpogasService/")
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();

                        EndPoints guardaPicoBilletes = retrofit2.create(EndPoints.class);
                        Call<RespuestaApi<List<ResumenFajilla>>> call2 = guardaPicoBilletes.postGuardaFajillas(recepcionFajillas, idusuario, idusuario);
                        call2.timeout().timeout(60, TimeUnit.SECONDS);
                        call2.enqueue(new Callback<RespuestaApi<List<ResumenFajilla>>>() {

                            @Override
                            public void onResponse(Call<RespuestaApi<List<ResumenFajilla>>> call2, Response<RespuestaApi<List<ResumenFajilla>>> response) {
                                if (!response.isSuccessful()) {
                                    return;
                                }
                                RespuestaApi<List<ResumenFajilla>> respuestaApi = response.body();
                                boolean correcto = respuestaApi.Correcto;
                                if (correcto == false) {
                                    titulo = "AVISO";
                                    mensaje = respuestaApi.Mensaje;
                                    View view1 = modales.MostrarDialogoAlertaAceptar(EntregaPicosActivity.this, mensaje, titulo);
                                    view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {  modales.alertDialog.dismiss(); }
                                    });
                                }
                                else{
                                    mensaje = "Sus picos han sido registrados. Total: $" + (String.valueOf(sumaTotal + cantiMorralla));
                                    Toast.makeText(getApplicationContext(), mensaje, Toast.LENGTH_LONG).show();


//                                View view1 = modales.MostrarDialogoCorrecto(EntregaPicosActivity.this, mensaje);
//                                view1.findViewById(R.id.buttonAction).setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
//                                        startActivity(intent);
//                                        modales.alertDialog.dismiss();
//                                    }
//
//                                });

                                    finish();
                                }
                            }

                            @Override
                            public void onFailure(Call<RespuestaApi<List<ResumenFajilla>>> call, Throwable t) {
                                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });


                    }else{
                        Toast.makeText(getApplicationContext(), "Usuario no autorizado.", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(), respuestaApi.Mensaje, Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<RespuestaApi<Empleado>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void SetRecyclerView() {

        RVAdapterPicos adapterPicos = new RVAdapterPicos(recepcionFajillas);
        adapterPicos.setOnClickListener(v -> {
            if (!adapterPicos.isClickable) {
                adapterPicos.isClickable = true;
                int updateIndex = rcvPicos.getChildAdapterPosition(v);
                double denominacion = recepcionFajillas.get(rcvPicos.getChildAdapterPosition(v)).getDenominacion();

                mensaje = "Ingresa Cantidad de Billetes";
                titulo = "Denominación: $" + denominacion;
                View viewLectura = modales.MostrarDialogoInsertaDato(EntregaPicosActivity.this, mensaje, titulo);
                editText = viewLectura.findViewById(R.id.textInsertarDato);
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(3)});

                viewLectura.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onClick(View view) {
                        cantidadBilletes = editText.getText().toString();
                        if (cantidadBilletes.isEmpty()) {
                            editText.setError("Ingresa un valor");

                        } else {
                            int cantiBilletes = Integer.parseInt(cantidadBilletes);
                            int totalBilletes = 0;
                            totalBilletes = (int) (denominacion * Integer.parseInt(cantidadBilletes));

                            recepcionFajillas.set(updateIndex, new RecepcionFajilla(sucursalId, 3, cantiBilletes, denominacion, totalBilletes));

                            adapterPicos.notifyItemChanged(updateIndex);
                            modales.alertDialog.dismiss();;
                            adapterPicos.isClickable = false;
                            setTextSumaTotalBilletes();
                            SetRecyclerView();
                        }
                    }
                });

                viewLectura.findViewById(R.id.buttonNo).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        adapterPicos.isClickable = false;
                        modales.alertDialog.dismiss();
                    }
                });
            }
        });
        rcvPicos.setAdapter(adapterPicos);
    }



    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setVariables(){
        dataBase = new SQLiteBD(getApplicationContext());
        this.setTitle("Entrega de Picos");

        ipEstacion = dataBase.getIpEstacion();
        sucursalId = Long.parseLong(dataBase.getIdSucursal());
        idusuario = dataBase.getNumeroEmpleado();

        modales = new Modales(EntregaPicosActivity.this);

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setTextSumaTotalBilletes() {
        for (RecepcionFajilla item : recepcionFajillas) {
            sumaTotal = (int) recepcionFajillas.stream().filter(x -> x.getTipoFajilla() == 3).mapToDouble(RecepcionFajilla::getImporte).sum();
        }
        textSumaTotalBilletes.setText("TOTAL BILLETES: $" + sumaTotal);
    }

    private void onClickButton() {
        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                //btnAceptar.setEnabled(false);
                agregaPicosMorralla();
                //Toast.makeText(getApplicationContext(),  String.valueOf(recepcionFajillas.stream().count()) , Toast.LENGTH_LONG).show();
                enviaPicos();

            }
        });

    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private void enviaPicos() {

        if (cantiMorralla == 0 && sumaTotal == 0) {

            titulo = "AVISO";
            mensaje = "No haz ingresado ninguna cantidad";
            View view1 = modales.MostrarDialogoAlertaAceptar(EntregaPicosActivity.this, mensaje, titulo);
            view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    btnAceptar.setEnabled(true);
                    modales.alertDialog.dismiss();
                }
            });

        }else{
            titulo = "Clave";
            mensaje = "Ingresa la clave de Gerente o Jefe de Isla";

            View viewLectura = modales.MostrarDialogoInsertaDato(EntregaPicosActivity.this, mensaje, titulo);
            EditText editText = ((EditText) viewLectura.findViewById(R.id.textInsertarDato));
            editText.setInputType(InputType.TYPE_CLASS_NUMBER);
            viewLectura.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String clave = editText.getText().toString();

                    if (clave.isEmpty()){
                        editText.setError("Campo requerido");
                    }else {
                        validaClave(clave);
                        modales.alertDialog.dismiss();

                    }
                }
            });

            viewLectura.findViewById(R.id.buttonNo).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    modales.alertDialog.dismiss();
                }
            });

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void agregaPicosMorralla() {
        String cantidadMorralla = editTextMorralla.getText().toString();

        if (!cantidadMorralla.isEmpty()) {
            cantiMorralla = Double.parseDouble(cantidadMorralla);
            recepcionFajillas.add(new RecepcionFajilla(sucursalId, 4, 1, (int) cantiMorralla, (int) cantiMorralla));
        } else{
                cantiMorralla = 0.0;
        }
    }
}