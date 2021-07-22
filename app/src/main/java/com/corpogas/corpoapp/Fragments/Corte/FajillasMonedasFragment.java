package com.corpogas.corpoapp.Fragments.Corte;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.corpogas.corpoapp.Configuracion.SQLiteBD;
import com.corpogas.corpoapp.Entities.Accesos.AccesoUsuario;
import com.corpogas.corpoapp.Entities.Catalogos.Bin;
import com.corpogas.corpoapp.Entities.Classes.RespuestaApi;
import com.corpogas.corpoapp.Entities.Cortes.Cierre;
import com.corpogas.corpoapp.Entities.Cortes.CierreFajilla;
import com.corpogas.corpoapp.Entities.Sucursales.PriceBankRoll;
import com.corpogas.corpoapp.Entities.Virtuales.CierreVariables;
import com.corpogas.corpoapp.Interfaces.Endpoints.EndPoints;
import com.corpogas.corpoapp.Modales.Modales;
import com.corpogas.corpoapp.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FajillasMonedasFragment extends Fragment {

    View view;
    EditText fajillasMorralla;
    Button btnValidaFajillas;
    long cierreId, turnoId, idusuario, islaId, sucursalId ;
    String titulo, morralla, mensaje;

    RespuestaApi<AccesoUsuario> respuestaApiAccesoUsuario;
    RespuestaApi<Cierre> respuestaApiCierreCabero;
    Modales modales;

    CierreFajilla cierreFajilla = null;

    SQLiteBD db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_fajillas_monedas, container, false);

        init();
        getObjetos();
        setVariables();


        return view;
    }

    private void init() {
        fajillasMorralla = (EditText) view.findViewById(R.id.editTextFajillasMorralla);
        btnValidaFajillas = (Button) view.findViewById(R.id.btnAceptarFajillaBilletes);
    }

    private void getObjetos() {
        respuestaApiCierreCabero = (RespuestaApi<Cierre>) getActivity().getIntent().getSerializableExtra("respuestaApiCierreCabero");
        respuestaApiAccesoUsuario = (RespuestaApi<AccesoUsuario>) getActivity().getIntent().getSerializableExtra("respuestaApiAccesoUsuario");
    }

    private void setVariables() {
        turnoId = respuestaApiCierreCabero.getObjetoRespuesta().getTurnoId();
        cierreId = respuestaApiCierreCabero.getObjetoRespuesta().getId();
        idusuario = respuestaApiAccesoUsuario.getObjetoRespuesta().getNumeroEmpleado();
        sucursalId = Long.parseLong(db.getIdSucursal());
        islaId = Long.parseLong(getActivity().getIntent().getStringExtra("islaId"));
        db = new SQLiteBD(getContext());
        modales = new Modales(getActivity());
        titulo = "AVISO";
        fajillasMorralla.setText("0");


    }



    private boolean validaCampos(){

//        for(PriceBankRoll item : respuestaApiCierreCabero.getObjetoRespuesta().Variables.getPrecioFajillas()){
//
//            item.BankRollType.getMORRALLA();
//
//        }

        morralla = fajillasMorralla.getText().toString();

        if (morralla.isEmpty()) {

            mensaje = "Ingresa tu Numero de Fajillas de Morralla.";
            View view1 = modales.MostrarDialogoAlertaAceptar(getContext(), mensaje, titulo);
            view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    modales.alertDialog.dismiss();
                    btnValidaFajillas.setEnabled(true);

                }
            });
            return false;
        }
    return true;
    }

    public void obtenerObjetoCierreFajillas(long tipoFajillaId, int folioInical, int folioFinal, double denominacion){
        cierreFajilla = new CierreFajilla(sucursalId,cierreId,tipoFajillaId,folioInical,folioFinal,denominacion);

    }

    public void enviarFoliosMorralla(){

        List<String> pistas = new ArrayList<String>();
        pistas.add("400000025010000199997000");
        pistas.add("400000025010000199997000");
        pistas.add("");

        Bin bin = new Bin(pistas);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://" + db.getIpEstacion() + "/CorpogasService/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        EndPoints obtenNumeroTarjetero = retrofit.create(EndPoints.class);
        Call<RespuestaApi<Bin>> call = obtenNumeroTarjetero.getBin(497, bin);
        call.timeout().timeout(60, TimeUnit.SECONDS);
        call.enqueue(new Callback<RespuestaApi<Bin>>() {

            @Override
            public void onResponse(Call<RespuestaApi<Bin>> call, Response<RespuestaApi<Bin>> response) {

                if (!response.isSuccessful()) {
                    return;
                }
//                respuestaApiBin = response.body();

            }

            @Override
            public void onFailure(Call<RespuestaApi<Bin>> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }
}