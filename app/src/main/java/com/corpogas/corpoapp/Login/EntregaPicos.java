package com.corpogas.corpoapp.Login;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.corpogas.corpoapp.Configuracion.SQLiteBD;
import com.corpogas.corpoapp.Entities.Classes.RespuestaApi;

import com.corpogas.corpoapp.Entities.Cortes.CierreFajilla;
import com.corpogas.corpoapp.Entities.Estaciones.RecepcionFajilla;
import com.corpogas.corpoapp.Entities.Estaciones.ResumenFajilla;
import com.corpogas.corpoapp.Entities.Sucursales.PriceBankRoll;
import com.corpogas.corpoapp.Entities.Virtuales.CierreVariables;
import com.corpogas.corpoapp.Interfaces.Endpoints.EndPoints;
import com.corpogas.corpoapp.Login.Adapters.RVAdapterPicos;
import com.corpogas.corpoapp.Modales.Modales;
import com.corpogas.corpoapp.R;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EntregaPicos extends AppCompatActivity {

    RecyclerView rcvPicos;
    EditText editTextMorralla, editText;
    TextView textSumaTotalBilletes;
    Button btnAceptar;
    SQLiteBD db;

    String ipEstacion, titulo, mensaje, cantidadBilletes, cantidadMorralla;
    int fajillaBilletes, sumaTotal, fajillaMorralla;
    long sucursalId, idusuario;
    double cantiMorralla;
    Modales modales;
    List<RecepcionFajilla> lPicos, lCierreMorralla;
    RespuestaApi<CierreVariables> respuestaApiCierreVariables;
    RespuestaApi<List<ResumenFajilla>> respuestaApiPicosBilletes;
//    RespuestaApi<CierreFajilla> respuestaApiPicosMorralla;
    RespuestaApi<List<ResumenFajilla>> respuestaApiPicosMorralla;

    CierreFajilla cierreFajilla;
    RecepcionFajilla recepcionFajilla;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrega_picos);

        init();
        getObjetos();
        setVariables();
        if (lPicos.size() == 0) obtenerCierreVariables();
        SetRecyclerView();
        onClickButton();
//        SetRecyclerView();
//        SetEditTextMorralla();
    }

    private void init() {
        rcvPicos = findViewById(R.id.rcvPicos);
        editTextMorralla = (EditText) findViewById(R.id.editMorrallaPicos);
        textSumaTotalBilletes = (TextView) findViewById(R.id.textTotalBilletesPicos);
        btnAceptar = (Button) findViewById(R.id.btnAceptarPicos);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getApplicationContext());
        rcvPicos.setLayoutManager(linearLayoutManager);
        rcvPicos.setHasFixedSize(true);
    }

    private void getObjetos(){

    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setVariables(){
        db = new SQLiteBD(getApplicationContext());
        this.setTitle("Entrega de Picos");

        ipEstacion = db.getIpEstacion();
        sucursalId = Long.parseLong(db.getIdSucursal());
        idusuario = Long.parseLong(db.getNumeroEmpleado());
        modales = new Modales(EntregaPicos.this);
        lPicos = db.getPicos().stream().filter(x -> x.getTipoFajilla() == 3).collect(Collectors.toList());
        setTextSumaTotalBilletes();


    }

    private void obtenerCierreVariables() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://" + ipEstacion + "/corpogasService/")//http://" + data.getIpEstacion() + "/corpogasService_Entities_token/
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
                respuestaApiCierreVariables = response.body();


                for (Integer item : respuestaApiCierreVariables.getObjetoRespuesta().Denominaciones) {

                    db.InsertarPicos(sucursalId,1, 3, 0, item.intValue(), 0, 0);
                }

                for (PriceBankRoll item : respuestaApiCierreVariables.getObjetoRespuesta().PrecioFajillas){

                    if (item.BankRollType == 1){
                        db.InsertarPrecioFajillas(sucursalId,1,item.getPrice());

                    }else{
                        if (item.BankRollType == 2){
                            db.InsertarPrecioFajillas(sucursalId,2,item.getPrice());
                        }
                    }

                }

                lPicos = db.getPicos();

                SetRecyclerView();

            }

            @Override
            public void onFailure(Call<RespuestaApi<CierreVariables>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void SetRecyclerView() {

        RVAdapterPicos adapterPicos = new RVAdapterPicos(lPicos);
        adapterPicos.setOnClickListener(v -> {
            if (!adapterPicos.isClickable) {
                adapterPicos.isClickable = true;
                int updateIndex = rcvPicos.getChildAdapterPosition(v);
                double denominacion = lPicos.get(rcvPicos.getChildAdapterPosition(v)).getDenominacion();
                int prueba = lPicos.get(rcvPicos.getChildAdapterPosition(v)).getCantidad();

                mensaje = "Ingresa Cantidad de Billetes";
                titulo = "Denominación: $" + denominacion;
                View viewLectura = modales.MostrarDialogoInsertaDato(EntregaPicos.this, mensaje, titulo);
                editText = viewLectura.findViewById(R.id.textInsertarDato);
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(3)});

                viewLectura.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onClick(View view) {

                        obtenerValorFajillas();
                        cantidadBilletes = editText.getText().toString();
                        if (cantidadBilletes.isEmpty()) {
                            editText.setError("Ingresa un valor");

                        } else {
                            int cantiBilletes = Integer.parseInt(cantidadBilletes);
                            int totalBilletes = 0;
                            int totalFajilla = 0;
                            totalBilletes = (int) (denominacion * Integer.parseInt(cantidadBilletes));
                            totalFajilla = sumaTotal + totalBilletes;
                            if (totalFajilla == fajillaBilletes) {
                                editText.setError("Tus cantidades pueden ser una fajilla");
                                return;
                            }
                            if ((totalFajilla > fajillaBilletes) && (prueba == 0)) {
                                editText.setError("Tus cantidades superan el valor de una Fajilla");
                                return;
                            }

                            if (totalBilletes > fajillaBilletes) {
                                editText.setError("No puedes superar el valor de una fajilla");
                            } else {
                                if (totalBilletes == fajillaBilletes) {
                                    editText.setError("Tu cantidad puede ser una fajilla");
                                } else {

                                    lPicos.set(updateIndex, new RecepcionFajilla(sucursalId, 0, 3, cantiBilletes, denominacion,0, totalBilletes));
                                    db.updatePicos(cantiBilletes, totalBilletes, denominacion, 3);
                                    adapterPicos.notifyItemChanged(updateIndex);
                                    modales.alertDialog.dismiss();
// // // // // // // //                       lCierreValesPapel.stream().filter(x -> x.getTipoValePapelId() == item.getTipoValePapelId()).mapToDouble(CierreValePapel::getCantidad).sum();
                                    lPicos = db.getPicos().stream().filter(x -> x.getTipoFajilla() == 3).collect(Collectors.toList());
                                    adapterPicos.isClickable = false;
                                    setTextSumaTotalBilletes();
                                    SetRecyclerView();

                                }
                            }
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

    private void obtenerValorFajillas() {

                fajillaBilletes = db.getPrecioFajillaBillete();
                fajillaMorralla = db.getPrecioFajillaMoneda();

    }

    private void insertarPicosMorrala() {
        db.InsertarPicos(sucursalId,1, 4,1, (int) cantiMorralla, 0, 0);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void enviarPicosBilletes() {

        if (sumaTotal == 0) {
            obtenerPicosMorralla();
        } else {

//            Retrofit retrofit = new Retrofit.Builder()
//                    .baseUrl("http://" + db.getIpEstacion() + "/CorpogasService/")
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .build();
//
//            lPicos = lPicos.stream().filter(x -> x.Cantidad != 0).collect(Collectors.toList());
//            String json = new Gson().toJson(lPicos);
//
//            EndPoints guardaPicoBilletes = retrofit.create(EndPoints.class);
//            Call<RespuestaApi<List<ResumenFajilla>>> call = guardaPicoBilletes.postGuardaFajillas(lPicos, db.getNumeroEmpleado(), db.getNumeroEmpleado());
//            call.timeout().timeout(60, TimeUnit.SECONDS);
//            call.enqueue(new Callback<RespuestaApi<List<ResumenFajilla>>>() {
//
//                @Override
//                public void onResponse(Call<RespuestaApi<List<ResumenFajilla>>> call, Response<RespuestaApi<List<ResumenFajilla>>> response) {
//                    if (!response.isSuccessful()) {
//                        return;
//                    }
//                    respuestaApiPicosBilletes = response.body();
//                    boolean correcto = respuestaApiPicosBilletes.Correcto;
//                    if (correcto == true) {
//                        obtenerPicosMorralla();
//                    } else {
//                        titulo = "AVISO";
//                        mensaje = respuestaApiPicosBilletes.Mensaje;
//                        View view1 = modales.MostrarDialogoAlertaAceptar(EntregaPicos.this, mensaje, titulo);
//                        view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                modales.alertDialog.dismiss();
//
//                            }
//                        });
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<RespuestaApi<List<ResumenFajilla>>> call, Throwable t) {
//                    Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
//                }
//
//            });

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void obtenerPicosMorralla() {

        obtenerValorFajillas();
        cantidadMorralla = editTextMorralla.getText().toString();

        if (cantidadMorralla.isEmpty()) {
            btnAceptar.setEnabled(true);
            editTextMorralla.setError("Ingresa un valor");
            editTextMorralla.requestFocus();

        } else {
            if (Double.parseDouble(cantidadMorralla) > fajillaMorralla) {
                editTextMorralla.setError("No puedes superar el valor de una fajilla");

            } else {
                if (Double.parseDouble(cantidadMorralla) == fajillaMorralla) {
                    editTextMorralla.setError("Tu cantidad puede ser una fajilla");

                } else {

                    cantiMorralla = Double.parseDouble(cantidadMorralla);

                    lCierreMorralla = lPicos.stream().filter(x -> x.getTipoFajilla() == 4).collect(Collectors.toList());
                    if (lCierreMorralla.size() == 0) insertarPicosMorrala();
                    db.updatePicos(cantiMorralla, 4);
                    recepcionFajilla = new RecepcionFajilla();
                    lCierreMorralla = db.getPicos().stream().filter(x -> x.getTipoFajilla() == 4).collect(Collectors.toList());
                    for (RecepcionFajilla item : lCierreMorralla) {
                        recepcionFajilla.SucursalId = sucursalId;
                        recepcionFajilla.TurnoId = 1;
                        recepcionFajilla.TipoFajilla = item.getTipoFajilla();
                        recepcionFajilla.Cantidad = item.getCantidad();
                        recepcionFajilla.Denominacion = item.getDenominacion();

                    }

                    enviarPicosMorralla();
                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setTextSumaTotalBilletes() {
        for (RecepcionFajilla item : lPicos) {
            sumaTotal = lPicos.stream().filter(x -> x.getTipoFajilla() == 3).mapToInt(RecepcionFajilla::getTotal).sum();
        }
        textSumaTotalBilletes.setText("TOTAL BILLETES: $" + sumaTotal);
    }

    private void onClickButton() {
        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                btnAceptar.setEnabled(false);
                enviarPicosBilletes();

            }
        });

    }

    private void enviarPicosMorralla() {

        if ((Double.parseDouble(cantidadMorralla) == 0) && (sumaTotal == 0)) {

            titulo = "AVISO";
            mensaje = "No haz ingresado ninguna cantidad. Contina con tu corte.";
            View view1 = modales.MostrarDialogoAlertaAceptar(EntregaPicos.this, mensaje, titulo);
            view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    btnAceptar.setEnabled(true);
                    modales.alertDialog.dismiss();

                }
            });
        } else {

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://" + db.getIpEstacion() + "/CorpogasService/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

             String json = new Gson().toJson(recepcionFajilla);

            EndPoints postGuardaPicosMorralla = retrofit.create(EndPoints.class);
            Call<RespuestaApi<List<ResumenFajilla>>> call = postGuardaPicosMorralla.postGuardaFajillas(recepcionFajilla, db.getNumeroEmpleado(), db.getNumeroEmpleado());
            call.timeout().timeout(60, TimeUnit.SECONDS);
            call.enqueue(new Callback<RespuestaApi<List<ResumenFajilla>>>() {

                @Override
                public void onResponse(Call<RespuestaApi<List<ResumenFajilla>>> call, Response<RespuestaApi<List<ResumenFajilla>>> response) {
                    if (!response.isSuccessful()) {
                        return;
                    }
                    respuestaApiPicosMorralla = response.body();
                    boolean correcto = respuestaApiPicosMorralla.Correcto;
                    if (correcto == true) {

                        if (sumaTotal == 0) {
                            mensaje = "Sus Picos han sido registrados. Total: $" + (cantidadMorralla) + " pesos";
                            View view1 = modales.MostrarDialogoCorrecto(EntregaPicos.this, mensaje);
                            view1.findViewById(R.id.buttonAction).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    btnAceptar.setEnabled(true);
                                    modales.alertDialog.dismiss();
                                }
                            });

                        } else {

                            double total = Double.parseDouble(cantidadMorralla) + Double.parseDouble(String.valueOf(sumaTotal));
                            mensaje = "Sus Picos han sido registrados. Total: $" + (total) + " pesos";
                            View view1 = modales.MostrarDialogoCorrecto(EntregaPicos.this, mensaje);
                            view1.findViewById(R.id.buttonAction).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    btnAceptar.setEnabled(true);
                                    modales.alertDialog.dismiss();
                                }
                            });
                        }
                    } else {

                        titulo = "AVISO";
                        mensaje = respuestaApiPicosMorralla.Mensaje;
                        View view1 = modales.MostrarDialogoAlertaAceptar(EntregaPicos.this, mensaje, titulo);
                        view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                btnAceptar.setEnabled(true);
                                modales.alertDialog.dismiss();

                            }
                        });
                    }

                }

                @Override
                public void onFailure(Call<RespuestaApi<List<ResumenFajilla>>> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();

                }

            });
        }

    }


}