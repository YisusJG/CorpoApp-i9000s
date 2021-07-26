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


public class FajillasBilletesFragment extends Fragment {

   View view;
   EditText fajillasBilletes;
   Button btnValidaFajillas;

    long cierreId, turnoId, idusuario, islaId, sucursalId;
    int dineroBilletes, fajillaBilletes;
    Modales modales;
    String titulo, billetes, mensaje;

    RespuestaApi<AccesoUsuario> respuestaApiAccesoUsuario;
    RespuestaApi<Cierre> respuestaApiCierreCabero;
    RespuestaApi<List<CierreFajilla>> respuestaApiCierreFajilla;
    ArrayList<CierreFajilla> arrayListCierreFajillas = new ArrayList<>();

    SQLiteBD db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_fajillas_billetes, container, false);

        init();
        getObjetos();
        setVariables();
        onClickButton();

        return view;
    }

    private void init() {
        fajillasBilletes = (EditText) view.findViewById(R.id.editTextFajillasBilletes);
        btnValidaFajillas = (Button) view.findViewById(R.id.btnAceptarFajillasBilletes);

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
        fajillasBilletes.setText("0");
    }

    public void onClickButton(){
        btnValidaFajillas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnValidaFajillas.setEnabled(true);
                boolean validaCampos = validaCampos();
                if (validaCampos == true){
                    dineroBilletes = Integer.parseInt(billetes) * fajillaBilletes;
                    enviarFoliosBilletes();
                }
            }
        });
    }

    private boolean validaCampos(){

        for(PriceBankRoll item : respuestaApiCierreCabero.getObjetoRespuesta().getVariables().getPrecioFajillas())
        {
            if(item.BankRollType == 1)

                fajillaBilletes = item.getPrice();
        }

        billetes = fajillasBilletes.getText().toString();

        if (billetes.isEmpty()) {

            mensaje = "Ingresa tu Numero de Fajillas de Billetes.";
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

    public void enviarFoliosBilletes(){

        arrayListCierreFajillas.add(new CierreFajilla(sucursalId,cierreId,1,0,Integer.parseInt(billetes),fajillaBilletes));

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://" + db.getIpEstacion() + "/CorpogasService/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        EndPoints guardaFoliosCierreListaFajillas = retrofit.create(EndPoints.class);
        Call<RespuestaApi<List<CierreFajilla>>> call = guardaFoliosCierreListaFajillas.postGuardaFoliosCierreListaFajillas(arrayListCierreFajillas, idusuario);
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
                    mensaje = "Sus Folios han sido registrados. Total Billetes: $" + dineroBilletes + " pesos";
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