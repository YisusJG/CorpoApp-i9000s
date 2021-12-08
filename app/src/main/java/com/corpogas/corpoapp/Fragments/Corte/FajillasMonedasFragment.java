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
import com.corpogas.corpoapp.Entities.Classes.RespuestaApi;
import com.corpogas.corpoapp.Entities.Cortes.Cierre;
import com.corpogas.corpoapp.Entities.Cortes.CierreFajilla;
import com.corpogas.corpoapp.Entities.Sucursales.PriceBankRoll;
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

    String bearerToken;
    RespuestaApi<AccesoUsuario> token;
    View view;
    EditText fajillasMorralla;
    Button btnValidaFajillas;
    long cierreId, turnoId, idusuario, islaId, sucursalId;
    String titulo, morralla, mensaje;
    int dineroMorralla, fajillaMorralla;

    RespuestaApi<AccesoUsuario> respuestaApiAccesoUsuario;
    RespuestaApi<Cierre> respuestaApiCierreCabero;
    RespuestaApi<List<CierreFajilla>> respuestaApiCierreFajilla;
    Modales modales;

    ArrayList<CierreFajilla> arrayListCierreFajillas = new ArrayList<>();

    SQLiteBD db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_fajillas_monedas, container, false);

        getToken();
        init();
        getObjetos();
        setVariables();
        onClickButton();


        return view;
    }

    private void getToken() {
        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("http://"+ip2+"/CorpogasService/") //anterior
                .baseUrl("http://10.0.1.40/CorpogasService_entities_token/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        EndPoints obtenerToken = retrofit.create(EndPoints.class);
        Call<RespuestaApi<AccesoUsuario>> call = obtenerToken.getAccesoUsuario(497L, "1111");
        call.timeout().timeout(60, TimeUnit.SECONDS);
        call.enqueue(new Callback<RespuestaApi<AccesoUsuario>>() {
            @Override
            public void onResponse(Call<RespuestaApi<AccesoUsuario>> call, Response<RespuestaApi<AccesoUsuario>> response) {
                if (response.isSuccessful()) {
                    token = response.body();
                    assert token != null;
                    bearerToken = token.Mensaje;
                } else {
                    bearerToken = "";
                }
            }

            @Override
            public void onFailure(Call<RespuestaApi<AccesoUsuario>> call, Throwable t) {
                Toast.makeText(requireContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void init() {
        fajillasMorralla = (EditText) view.findViewById(R.id.editTextFajillasMorralla);
        btnValidaFajillas = (Button) view.findViewById(R.id.btnAceptarFajillaMorralla);
    }

    private void getObjetos() {
        respuestaApiCierreCabero = (RespuestaApi<Cierre>) getActivity().getIntent().getSerializableExtra("respuestaApiCierreCabero");
        respuestaApiAccesoUsuario = (RespuestaApi<AccesoUsuario>) getActivity().getIntent().getSerializableExtra("respuestaApiAccesoUsuario");
    }

    private void setVariables() {
        db = new SQLiteBD(getContext());
        turnoId = respuestaApiCierreCabero.getObjetoRespuesta().getTurnoId();
        cierreId = respuestaApiCierreCabero.getObjetoRespuesta().getId();
        idusuario = respuestaApiAccesoUsuario.getObjetoRespuesta().getNumeroEmpleado();
        islaId = Long.parseLong(getActivity().getIntent().getStringExtra("islaId"));
        sucursalId = Long.parseLong(db.getIdSucursal());
        modales = new Modales(getActivity());
        titulo = "AVISO";
        fajillasMorralla.setText("0");

    }

    public void onClickButton(){
        btnValidaFajillas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnValidaFajillas.setEnabled(true);
                boolean validaCampos = validaCampos();
                if (validaCampos == true){
                    dineroMorralla = Integer.parseInt(morralla) * fajillaMorralla;
                    enviarFoliosMorralla();
                }
            }
        });
    }

    private boolean validaCampos(){

        for(PriceBankRoll item : respuestaApiCierreCabero.getObjetoRespuesta().getVariables().getPrecioFajillas())
        {
            if(item.BankRollType == 2)

                fajillaMorralla = item.getPrice();
        }

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

    public void enviarFoliosMorralla(){

//        arrayListCierreFajillas.add(new CierreFajilla(sucursalId,cierreId,2,0,Integer.parseInt(morralla),fajillaMorralla));

        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("http://" + db.getIpEstacion() + "/CorpogasService/")
                .baseUrl("http://10.0.1.40/CorpogasService_entities_token/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        EndPoints guardaFoliosCierreListaFajillas = retrofit.create(EndPoints.class);
        Call<RespuestaApi<List<CierreFajilla>>> call = guardaFoliosCierreListaFajillas.postGuardaFoliosCierreListaFajillas(arrayListCierreFajillas, idusuario, "Bearer " +bearerToken);
        call.timeout().timeout(60, TimeUnit.SECONDS);
        call.enqueue(new Callback<RespuestaApi<List<CierreFajilla>>>() {


            @Override
            public void onResponse(Call<RespuestaApi<List<CierreFajilla>>> call, Response<RespuestaApi<List<CierreFajilla>>> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                respuestaApiCierreFajilla = response.body();
                boolean correcto = respuestaApiCierreFajilla.Correcto;
                if (correcto == true){
                    mensaje = "Sus Folios han sido registrados. Total Morralla: $" + dineroMorralla + " pesos";
                    View view1 = modales.MostrarDialogoCorrecto(getContext(),mensaje);
                    view1.findViewById(R.id.buttonAction).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            btnValidaFajillas.setEnabled(false);
                            modales.alertDialog.dismiss();
                        }
                    });

                }else {
                    String titulo = "AVISO";
                    String mensaje = respuestaApiCierreFajilla.Mensaje;
                    View view1 = modales.MostrarDialogoAlertaAceptar(getContext(), mensaje,titulo);
                    view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            btnValidaFajillas.setEnabled(true);
                            modales.alertDialog.dismiss();

                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<RespuestaApi<List<CierreFajilla>>> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }
}