package com.corpogas.corpoapp.Corte;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.corpogas.corpoapp.Configuracion.SQLiteBD;
import com.corpogas.corpoapp.Corte.Adapters.RVAdapterResumenFormaPagos;
import com.corpogas.corpoapp.Corte.Adapters.RVAdapterResumenGastos;
import com.corpogas.corpoapp.Corte.Adapters.RVAdapterResumenVales;
import com.corpogas.corpoapp.Corte.Entities.ValePapelTotal;
import com.corpogas.corpoapp.Entities.Classes.RespuestaApi;
import com.corpogas.corpoapp.Entities.Virtuales.CierreTicket;
import com.corpogas.corpoapp.Entities.Virtuales.FajillaTotal;
import com.corpogas.corpoapp.Entities.Virtuales.FormaPagoTotal;
import com.corpogas.corpoapp.Entities.Virtuales.GastoTotal;
import com.corpogas.corpoapp.Entities.Virtuales.VentaCombustibleTotal;
import com.corpogas.corpoapp.Interfaces.Endpoints.EndPoints;
import com.corpogas.corpoapp.Login.LoginActivity;
import com.corpogas.corpoapp.Menu_Principal;
import com.corpogas.corpoapp.Modales.Modales;
import com.corpogas.corpoapp.R;

import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ResumenActivity extends AppCompatActivity {

    ConstraintLayout expandableCombustible, expandableFajilla, expandablePico, expandableVale, expandableFormaPago, expandableJarreo, expandableGastos;
    Button arrowBtnCombustible, arrowBtnFajilla, arrowBtnPico, arrowBtnVale, arrowBtnFormaPago, arrowBtnJarreo, arrowBtnGastos, btnAgregarFajillasResumenActivity, btnValidarEntrega;
    CardView cardViewCombustible, cardViewFajilla, cardViewPico, cardViewVale, cardViewFormaPago, cardViewJarreo, cardViewGastos;
    RecyclerView rcvGasopass, rcvEfectivale, rcvAccor, rcvSiVale, rcvFormaPago, rcvGastos;
    ImageButton btnListaProductosResumenActivity;

    SQLiteBD db;
    String ipEstacion, idusuario, titulo, mensaje;
    long sucursalId;
    double importeDiferenciaTotal;
    int banderaConfirmaInventario;

    RespuestaApi<CierreTicket> respuestaApiCierreTicket;
    Modales modales;

    //Variables para mostrar la informacion en la vista
    private TextView tvTotalMagna;
    private TextView tvTotalPremium;
    private TextView tvTotalCombustible;
    private TextView tvTotalProducto;

    private TextView tvTotalFajillas;
    private TextView tvTotalFajillaBillete;
    private TextView tvTotalFajillaMorralla;

    private TextView tvTotalPicos;
    private TextView tvTotalPicoBillete;
    private TextView tvTotalPicoMorralla;

    private TextView tvVales;
    private TextView tvFormaPago;

    private TextView tvTotalJarreo;
    private TextView tvTotalCantidadJarreo;
    private TextView tvTotalCantidadAutojarreo;

    private TextView txvGasopass,txvCantidadGasopass,txvDenominacionGasopass, txvTotalGasopass;
    private TextView txvEfectivale,txvCantidadEfectivale,txvDenominacionEfectivale, txvTotalEfectivale;
    private TextView txvAccor,txvCantidadAccor,txvDenominacionAccor, txvTotalAccor;
    private TextView txvSivale,txvCantidadSivale,txvDenominacionSivale, txvTotalSivale;

    private TextView tvTotalGastos;

    private ImageView imgTotalCombustible;
    private ImageView imgTotalValesPapel, imgTotalFormasPago, imgTotalGastos;
    private ImageView imgTotalProductos;
    private ImageView imgTotalFajillas;
    private ImageView imgTotalPicos;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resumen);

        init();
        getObjetos();
        setVariables();

        onClicks();
        obtenerResumenCorte();

    }

    private void init() {

        rcvGasopass = findViewById(R.id.rcvGasopass);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext().getApplicationContext());
        rcvGasopass.setLayoutManager(linearLayoutManager);
        rcvGasopass.setHasFixedSize(true);

        rcvEfectivale = findViewById(R.id.rcvEfectivale);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getApplicationContext().getApplicationContext());
        rcvEfectivale.setLayoutManager(linearLayoutManager2);
        rcvEfectivale.setHasFixedSize(true);

        rcvAccor = findViewById(R.id.rcvAccor);
        LinearLayoutManager linearLayoutManager3 = new LinearLayoutManager(getApplicationContext().getApplicationContext());
        rcvAccor.setLayoutManager(linearLayoutManager3);
        rcvAccor.setHasFixedSize(true);

        rcvSiVale = findViewById(R.id.rcvSiVale);
        LinearLayoutManager linearLayoutManager4 = new LinearLayoutManager(getApplicationContext().getApplicationContext());
        rcvSiVale.setLayoutManager(linearLayoutManager4);
        rcvSiVale.setHasFixedSize(true);

        rcvFormaPago = findViewById(R.id.rcvFormaPago);
        LinearLayoutManager linearLayoutManager5 = new LinearLayoutManager(getApplicationContext().getApplicationContext());
        rcvFormaPago.setLayoutManager(linearLayoutManager5);
        rcvFormaPago.setHasFixedSize(true);

        rcvGastos = findViewById(R.id.rcvGastos);
        Context context;
        LinearLayoutManager linearLayoutManager6 = new LinearLayoutManager(getApplicationContext().getApplicationContext());
        rcvGastos.setLayoutManager(linearLayoutManager6);
        rcvGastos.setHasFixedSize(true);

        expandableCombustible = findViewById(R.id.expanVCombustibles);
        arrowBtnCombustible = findViewById(R.id.btnexpanCombustibles);
        cardViewCombustible = findViewById(R.id.crvCombustibles);

        expandableFajilla = findViewById(R.id.expanVFajillas);
        arrowBtnFajilla = findViewById(R.id.btnexpanFajillas);
        cardViewFajilla = findViewById(R.id.crvFajillas);

        expandablePico = findViewById(R.id.expanVPicos);
        arrowBtnPico = findViewById(R.id.btnexpanPicos);
        cardViewPico = findViewById(R.id.crvPicos);

        expandableVale = findViewById(R.id.expanVVales);
        arrowBtnVale = findViewById(R.id.btnexpanVales);
        cardViewVale = findViewById(R.id.crvVales);

        expandableFormaPago = findViewById(R.id.expanVFormaPagos);
        arrowBtnFormaPago = findViewById(R.id.btnexpanFormaPago);
        cardViewFormaPago = findViewById(R.id.crvFormasPago);

//        expandableJarreo = findViewById(R.id.expanVJarreo);
//        arrowBtnJarreo = findViewById(R.id.btnexpanJarreo);
//        cardViewJarreo = findViewById(R.id.crvJarreo);

        tvTotalCombustible = findViewById(R.id.tvTotalCombustible);
        tvTotalMagna = findViewById(R.id.tvTotalMagna);
        tvTotalPremium = findViewById(R.id.tvTotalPremium);
        tvTotalProducto = findViewById(R.id.tvTotalProducto);

        tvTotalFajillas = findViewById(R.id.tvTotalFajillas);
        tvTotalFajillaBillete = findViewById(R.id.tvTotalFajillaBillete);
        tvTotalFajillaMorralla = findViewById(R.id.tvTotalFajillaMorralla);

        tvTotalPicos = findViewById(R.id.tvTotalPicos);
        tvTotalPicoBillete = findViewById(R.id.tvTotalPicoBillete);
        tvTotalPicoMorralla = findViewById(R.id.tvTotalPicoMorralla);

        tvVales = findViewById(R.id.tvVales);
        tvFormaPago = findViewById(R.id.tvFormaPago);

//        tvTotalJarreo = findViewById(R.id.tvTotalJarreo);
//        tvTotalCantidadJarreo = findViewById(R.id.tvTotalCantidadJarreo);
//        tvTotalCantidadAutojarreo = findViewById(R.id.tvTotalAutoJarreo);

        txvGasopass = findViewById(R.id.txvGasopass);
        txvCantidadGasopass = findViewById(R.id.txvCantidadGasopass);
        txvDenominacionGasopass = findViewById(R.id.txvDenominacionGasopass);
        txvTotalGasopass = findViewById(R.id.txvTotalGasopass);

        txvEfectivale = findViewById(R.id.txvEfectiVale);
        txvCantidadEfectivale = findViewById(R.id.txvCantidadEfectiVale);
        txvDenominacionEfectivale = findViewById(R.id.txvDenominacionEfectiVale);
        txvTotalEfectivale = findViewById(R.id.txvTotalEfectiVale);

        txvAccor = findViewById(R.id.txvAccor);
        txvCantidadAccor = findViewById(R.id.txvCantidadAccor);
        txvDenominacionAccor = findViewById(R.id.txvDenominacionAccor);
        txvTotalAccor = findViewById(R.id.txvTotalAccor);

        txvSivale = findViewById(R.id.txvSiVale);
        txvCantidadSivale = findViewById(R.id.txvCantidadSiVale);
        txvDenominacionSivale = findViewById(R.id.txvDenominacionSiVale);
        txvTotalSivale = findViewById(R.id.txvTotalSiVale);

        tvTotalGastos = findViewById(R.id.tvTotalGastos);

        expandableGastos = findViewById(R.id.expanVGastos);
        arrowBtnGastos = findViewById(R.id.btnExpanGastos);
        cardViewGastos = findViewById(R.id.crvGastos);

//        tvTotalJarreo = findViewById(R.id.tvTotalJarreo);

        imgTotalCombustible = findViewById(R.id.imgTotalCombustibles);
        imgTotalFormasPago = findViewById(R.id.imgTotalFormasPago);
        imgTotalGastos = findViewById(R.id.imgTotalGastos);
        imgTotalValesPapel = findViewById(R.id.imgTotalValesPapel);
        imgTotalProductos = findViewById(R.id.imgTotalProductos);
        imgTotalFajillas = findViewById(R.id.imgTotalFajillas);
        imgTotalPicos = findViewById(R.id.imgTotalPicos);

        btnAgregarFajillasResumenActivity = findViewById(R.id.btnAgregarFajillasResumenActivity);
        btnListaProductosResumenActivity = findViewById(R.id.btnListaProductosResumenActivity);
        btnValidarEntrega = findViewById(R.id.btnValidarEntrega);

//        imgTotalPicos = findViewById(R.id.imgTotalPicos);
//        imgTotalFajillas = findViewById(R.id.imgTotalFajillas);
//        imgTotalValesPapel = findViewById(R.id.imgTotalValesPapel);


        //lGasopass = lGasopass.stream().filter(x-> x.getNumeroTipoVale() == 1).collect(Collectors.toList());


//        imgTotalPicos.setVisibility(INVISIBLE);
//        imgTotalFajillas.setVisibility(INVISIBLE);
//        imgTotalValesPapel.setVisibility(INVISIBLE);
        
    }

    private void getObjetos() {
    }

    private void setVariables() {
        db = new SQLiteBD(getApplicationContext());
        modales = new Modales(ResumenActivity.this);
        ipEstacion = db.getIpEstacion();
        sucursalId = Long.parseLong(db.getIdSucursal());
        idusuario = db.getNumeroEmpleado();
        banderaConfirmaInventario = getIntent().getIntExtra("banderaConfirmaInventario",0);
        if (banderaConfirmaInventario == 1){
            btnListaProductosResumenActivity.setVisibility(View.INVISIBLE);
            btnListaProductosResumenActivity.setEnabled(false);
            return;
        }
    }

    private void onClicks(){

        arrowBtnCombustible.setOnClickListener(v -> {
            if (expandableCombustible.getVisibility() == View.GONE) {
                TransitionManager.beginDelayedTransition(cardViewCombustible, new AutoTransition());
                expandableCombustible.setVisibility(View.VISIBLE);
                arrowBtnCombustible.setBackgroundResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
            } else {
                TransitionManager.beginDelayedTransition(cardViewCombustible, new AutoTransition());
                expandableCombustible.setVisibility(View.GONE);
                arrowBtnCombustible.setBackgroundResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
            }
        });

        arrowBtnFajilla.setOnClickListener(v -> {
            if (expandableFajilla.getVisibility() == View.GONE) {
                TransitionManager.beginDelayedTransition(cardViewFajilla, new AutoTransition());
                expandableFajilla.setVisibility(View.VISIBLE);
                arrowBtnFajilla.setBackgroundResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
            } else {
                TransitionManager.beginDelayedTransition(cardViewFajilla, new AutoTransition());
                expandableFajilla.setVisibility(View.GONE);
                arrowBtnFajilla.setBackgroundResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
            }
        });

        arrowBtnPico.setOnClickListener(v -> {
            if (expandablePico.getVisibility() == View.GONE) {
                TransitionManager.beginDelayedTransition(cardViewPico, new AutoTransition());
                expandablePico.setVisibility(View.VISIBLE);
                arrowBtnPico.setBackgroundResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
            } else {
                TransitionManager.beginDelayedTransition(cardViewPico, new AutoTransition());
                expandablePico.setVisibility(View.GONE);
                arrowBtnPico.setBackgroundResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
            }
        });

        arrowBtnVale.setOnClickListener(v -> {
            if (expandableVale.getVisibility() == View.GONE) {
                TransitionManager.beginDelayedTransition(cardViewVale, new AutoTransition());
                expandableVale.setVisibility(View.VISIBLE);
                arrowBtnVale.setBackgroundResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
            } else {
                TransitionManager.beginDelayedTransition(cardViewVale, new AutoTransition());
                expandableVale.setVisibility(View.GONE);
                arrowBtnVale.setBackgroundResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
            }
        });

        arrowBtnFormaPago.setOnClickListener(v -> {
            if (expandableFormaPago.getVisibility() == View.GONE) {
                TransitionManager.beginDelayedTransition(cardViewFormaPago, new AutoTransition());
                expandableFormaPago.setVisibility(View.VISIBLE);
                arrowBtnFormaPago.setBackgroundResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
            } else {
                TransitionManager.beginDelayedTransition(cardViewFormaPago, new AutoTransition());
                expandableFormaPago.setVisibility(View.GONE);
                arrowBtnFormaPago.setBackgroundResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
            }
        });

        arrowBtnGastos.setOnClickListener(v -> {
            if (expandableGastos.getVisibility() == View.GONE) {
                TransitionManager.beginDelayedTransition(cardViewGastos, new AutoTransition());
                expandableGastos.setVisibility(View.VISIBLE);
                arrowBtnGastos.setBackgroundResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
            } else {
                TransitionManager.beginDelayedTransition(cardViewGastos, new AutoTransition());
                expandableGastos.setVisibility(View.GONE);
                arrowBtnGastos.setBackgroundResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
            }
        });

//        arrowBtnJarreo.setOnClickListener(v -> {
//            if (expandableJarreo.getVisibility() == View.GONE) {
//                TransitionManager.beginDelayedTransition(cardViewJarreo, new AutoTransition());
//                expandableJarreo.setVisibility(View.VISIBLE);
//                arrowBtnJarreo.setBackgroundResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
//            } else {
//                TransitionManager.beginDelayedTransition(cardViewJarreo, new AutoTransition());
//                expandableJarreo.setVisibility(View.GONE);
//                arrowBtnJarreo.setBackgroundResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
//            }
//        });

        btnAgregarFajillasResumenActivity.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        });

        btnListaProductosResumenActivity.setOnClickListener(view -> {

            if (db.getRol() == 3) {
                Intent intent = new Intent(getApplicationContext(), ProductosJefeIsla.class);
                startActivity(intent);

            } else if (db.getRol() == 4) {
                Intent intent = new Intent(getApplicationContext(), ProductosVendedor.class);
                startActivity(intent);

            } else {
                titulo = "AVISO";
                mensaje = "No eres Jefe de Isla o vendedor";
                View view1 = modales.MostrarDialogoAlertaAceptar(ResumenActivity.this, mensaje, titulo);
                view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        modales.alertDialog.dismiss();
                    }
                });
            }
        });

        btnValidarEntrega.setOnClickListener(view -> {
            importeDiferenciaTotal = respuestaApiCierreTicket.getObjetoRespuesta().getImporteDiferenciaTotal();
            if (importeDiferenciaTotal == 0.0){

                titulo = "CORRECTO";
                mensaje = "Sin diferencias. Puedes continuar.";
                View view1 = modales.MostrarDialogoCorrecto(ResumenActivity.this, titulo, mensaje, "ACEPTAR");
                view1.findViewById(R.id.buttonAction).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        db.getWritableDatabase().delete("DatosEmpleado",null,null);
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                        modales.alertDialog.dismiss();

                    }
                });

            }else{
                titulo = "AVISO";
                mensaje = "Existe una diferencia en tu efectivo. Verifica con tu superior.";
                View view1 = modales.MostrarDialogoAlertaAceptar(ResumenActivity.this, mensaje, titulo);
                view1.findViewById(R.id.buttonAction).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        modales.alertDialog.dismiss();

                    }
                });
            }

        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setTotalVales () {
        List<ValePapelTotal> lGasopass, lEfectivale, lAccor, lSiVale;
        lGasopass = respuestaApiCierreTicket.getObjetoRespuesta().getValePapelesTotal();
        lGasopass = lGasopass.stream().filter(x -> x.getNumeroTipoVale() == 1).collect(Collectors.toList());
        if (lGasopass.size() > 0) SetRecyclerView(lGasopass, 1);

        lEfectivale = respuestaApiCierreTicket.getObjetoRespuesta().getValePapelesTotal();
        lEfectivale = lEfectivale.stream().filter(x -> x.getNumeroTipoVale() == 2).collect(Collectors.toList());
        if (lEfectivale.size() > 0) SetRecyclerView(lEfectivale, 2);

        lAccor = respuestaApiCierreTicket.getObjetoRespuesta().getValePapelesTotal();
        lAccor = lAccor.stream().filter(x -> x.getNumeroTipoVale() == 3).collect(Collectors.toList());
        if (lAccor.size() > 0) SetRecyclerView(lAccor, 3);

        lSiVale = respuestaApiCierreTicket.getObjetoRespuesta().getValePapelesTotal();
        lSiVale = lSiVale.stream().filter(x -> x.getNumeroTipoVale() == 4).collect(Collectors.toList());
        if (lSiVale.size() > 0) SetRecyclerView(lSiVale, 4);
    }

    private void SetRecyclerView (List< ValePapelTotal > valePapelTotalList, int tipoValeId){
        if (tipoValeId == 1) {
            txvGasopass.setVisibility(View.VISIBLE);
            txvCantidadGasopass.setVisibility(View.VISIBLE);
            txvDenominacionGasopass.setVisibility(View.VISIBLE);
            txvTotalGasopass.setVisibility(View.VISIBLE);
            rcvGasopass.setVisibility(View.VISIBLE);
            RVAdapterResumenVales rvAdapterResumenVales = new RVAdapterResumenVales(valePapelTotalList);
            rcvGasopass.setAdapter(rvAdapterResumenVales);
            rcvGasopass.setHasFixedSize(true);
        }
        if (tipoValeId == 2) {
            txvEfectivale.setVisibility(View.VISIBLE);
            txvCantidadEfectivale.setVisibility(View.VISIBLE);
            txvDenominacionEfectivale.setVisibility(View.VISIBLE);
            txvTotalEfectivale.setVisibility(View.VISIBLE);
            rcvEfectivale.setVisibility(View.VISIBLE);
            RVAdapterResumenVales rvAdapterResumenVales = new RVAdapterResumenVales(valePapelTotalList);
            rcvEfectivale.setAdapter(rvAdapterResumenVales);
            rcvEfectivale.setHasFixedSize(true);
        }
        if (tipoValeId == 3) {
            txvAccor.setVisibility(View.VISIBLE);
            txvCantidadAccor.setVisibility(View.VISIBLE);
            txvDenominacionAccor.setVisibility(View.VISIBLE);
            txvTotalAccor.setVisibility(View.VISIBLE);
            rcvAccor.setVisibility(View.VISIBLE);
            RVAdapterResumenVales rvAdapterResumenVales = new RVAdapterResumenVales(valePapelTotalList);
            rcvAccor.setAdapter(rvAdapterResumenVales);
            rcvAccor.setHasFixedSize(true);
        }
        if (tipoValeId == 4) {
            txvSivale.setVisibility(View.VISIBLE);
            txvCantidadSivale.setVisibility(View.VISIBLE);
            txvDenominacionSivale.setVisibility(View.VISIBLE);
            txvTotalSivale.setVisibility(View.VISIBLE);
            rcvSiVale.setVisibility(View.VISIBLE);
            RVAdapterResumenVales rvAdapterResumenVales = new RVAdapterResumenVales(valePapelTotalList);
            rcvSiVale.setAdapter(rvAdapterResumenVales);
            rcvSiVale.setHasFixedSize(true);
        }


    }

    private void obtenerResumenCorte(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://" + ipEstacion + "/corpogasService/")//http://" + data.getIpEstacion() + "/corpogasService_Entities_token/
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        EndPoints obtenerResumenCorte = retrofit.create(EndPoints.class);
        Call<RespuestaApi<CierreTicket>> call = obtenerResumenCorte.getCierreTicket(sucursalId, idusuario);
        call.enqueue(new Callback<RespuestaApi<CierreTicket>>() {

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<RespuestaApi<CierreTicket>> call, Response<RespuestaApi<CierreTicket>> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                respuestaApiCierreTicket = response.body();
                if (respuestaApiCierreTicket.Correcto == true ){

//                obtenerDiferencias();
                    inicializaControles();
//                SetRecyclerView();
                    setTotalVales();
                    setTotalFormasPago();
                    setTotalGastos();
                    // ImprimirTicketCierre();
                }else{
                    titulo = "AVISO";
                    mensaje = respuestaApiCierreTicket.Mensaje;
                    View view1 = modales.MostrarDialogoAlertaAceptar(ResumenActivity.this, mensaje, titulo);
                    view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getApplicationContext(), Menu_Principal.class);
                            startActivity(intent);
                            modales.alertDialog.dismiss();
                            finish();

                        }
                    });


                }


            }

            @Override
            public void onFailure(Call<RespuestaApi<CierreTicket>> call, Throwable t) {
                Toast.makeText(getApplicationContext().getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void inicializaControles(){

        tvTotalCombustible.setText("$ " + respuestaApiCierreTicket.getObjetoRespuesta().ImporteCombustibleTotal);
        tvTotalProducto.setText("$ " + respuestaApiCierreTicket.getObjetoRespuesta().ImporteProductoTotal);
        tvTotalFajillas.setText("$ " + respuestaApiCierreTicket.getObjetoRespuesta().getImporteFajillaTotal());
        tvTotalPicos.setText("$ " + respuestaApiCierreTicket.getObjetoRespuesta().getImportePicoTotal());//nos falta el total de picos
        tvVales.setText(("$ " + respuestaApiCierreTicket.getObjetoRespuesta().getImporteValesTotal()));
        tvFormaPago.setText("$ " + respuestaApiCierreTicket.getObjetoRespuesta().getImporteFormaPagoTotal());
        tvTotalGastos.setText("$ " + respuestaApiCierreTicket.getObjetoRespuesta().ImporteGastosTotal);
        tvTotalPicoBillete.setText("$ " + respuestaApiCierreTicket.getObjetoRespuesta().getPicosBilletesTotal());
        tvTotalPicoMorralla.setText("$ " + respuestaApiCierreTicket.getObjetoRespuesta().getPicosMorrallasTotal());
//        tvTotalCantidadJarreo.setText("$ " + respuestaApiCierreTicket.getObjetoRespuesta().getImporteTotalJarreos());
//        tvTotalCantidadAutojarreo.setText("$ " + respuestaApiCierreTicket.getObjetoRespuesta().getImporteTotalAutoJarreos());
//        double totalJarreos = respuestaApiCierreTicket.getObjetoRespuesta().getImporteTotalJarreos() + respuestaApiCierreTicket.getObjetoRespuesta().getImporteTotalAutoJarreos();

//        tvTotalJarreo.setText("$ "+ totalJarreos);

        for (VentaCombustibleTotal item : respuestaApiCierreTicket.getObjetoRespuesta().getVentaCombustiblesTotal()) {
            switch ((int) item.getNumeroCombustible()) {
                case 1:
                    tvTotalMagna.setText("$ " + item.Importe);
                    break;
                case 2:
                    tvTotalPremium.setText("$ " + item.Importe);
                    break;
            }
        }

        for (FajillaTotal item : respuestaApiCierreTicket.getObjetoRespuesta().getFajillasTotal()) {
            switch ((int) item.TipoFajilla) {
                case 1:
                    tvTotalFajillaBillete.setText("$ " + item.Total);
                    break;
                case 2:
                    tvTotalFajillaMorralla.setText("$ " + item.Total);
                    break;
            }
        }

    }

    private void setTotalFormasPago () {
        List<FormaPagoTotal> lFormaPagoTotal;
        lFormaPagoTotal = respuestaApiCierreTicket.getObjetoRespuesta().FormaPagosTotal;
        RVAdapterResumenFormaPagos rvAdapterResumenFormaPagos = new RVAdapterResumenFormaPagos(lFormaPagoTotal);
        rcvFormaPago.setAdapter(rvAdapterResumenFormaPagos);
        rcvFormaPago.setHasFixedSize(true);
    }

    private void setTotalGastos(){
        List<GastoTotal> lGastosTotal;
        lGastosTotal = respuestaApiCierreTicket.getObjetoRespuesta().GastosTotal;
        RVAdapterResumenGastos rvAdapterResumenGastos = new RVAdapterResumenGastos(lGastosTotal);
        rcvGastos.setAdapter(rvAdapterResumenGastos);
        rcvGastos.setHasFixedSize(true);

    }

    //Metodo para regresar a la actividad principal
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), Menu_Principal.class);
        startActivity(intent);
        finish();
    }


}