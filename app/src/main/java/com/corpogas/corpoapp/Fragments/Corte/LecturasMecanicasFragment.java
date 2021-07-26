package com.corpogas.corpoapp.Fragments.Corte;

import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.corpogas.corpoapp.Configuracion.SQLiteBD;
import com.corpogas.corpoapp.Entities.Accesos.AccesoUsuario;
import com.corpogas.corpoapp.Entities.Classes.RespuestaApi;
import com.corpogas.corpoapp.Entities.Cortes.Cierre;
import com.corpogas.corpoapp.Entities.Cortes.CierreCarrete;
import com.corpogas.corpoapp.Modales.Modales;
import com.corpogas.corpoapp.R;

import java.util.ArrayList;
import java.util.List;

public class LecturasMecanicasFragment extends Fragment {
    View view;
    TextView textIsla, textTurno;
    ListView list;
    Button btnEnviarMecanicas;

    long cierreId, turnoId, idusuario, islaId, sucursalId;
    double lecturasElectronicasMecanicas, lecturasElectronicasDespachos;
    List<Double> mecanicasInicales = new ArrayList<>();

    RespuestaApi<Cierre> respuestaApiCierreCabero;
    RespuestaApi<AccesoUsuario> respuestaApiAccesoUsuario;
    RespuestaApi<List<CierreCarrete>> respuestaApiCierreCarretes;
    Modales modales;

    SQLiteBD db;


    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_lecturas_mecanicas, container, false);

        init();
        getObjetos();
        setVariables();
        onClickButton();

        textTurno.setText("Turno: " + turnoId);
        textIsla.setText("Isla: " + islaId);

        obtenerMecanicaInical();

        return view;
    }

    private void init() {
        textIsla = (TextView) view.findViewById(R.id.textIslaLecturasMecanicas);
        textTurno = (TextView) view.findViewById(R.id.textTurnoLecturasMecanicas);
        btnEnviarMecanicas = (Button) view.findViewById(R.id.btnAceptarLecturasMecanicas);

    }
    private void getObjetos() {
        respuestaApiCierreCabero = (RespuestaApi<Cierre>) getActivity().getIntent().getSerializableExtra("respuestaApiCierreCabero");
        respuestaApiAccesoUsuario = (RespuestaApi<AccesoUsuario>) getActivity().getIntent().getSerializableExtra("respuestaApiAccesoUsuario");
        respuestaApiCierreCarretes = (RespuestaApi<List<CierreCarrete>>) getActivity().getIntent().getSerializableExtra("respuestaApiCierreCarretes");
    }
    private void setVariables() {
        db = new SQLiteBD(getContext());
        turnoId = respuestaApiCierreCabero.getObjetoRespuesta().getTurnoId();
        cierreId = respuestaApiCierreCabero.getObjetoRespuesta().getId();
        idusuario = respuestaApiAccesoUsuario.getObjetoRespuesta().getNumeroEmpleado();
        lecturasElectronicasMecanicas = respuestaApiCierreCabero.getObjetoRespuesta().getVariables().getDiferenciaPermitida().LecturasElectronicasMecanicas;
        lecturasElectronicasDespachos = respuestaApiCierreCabero.getObjetoRespuesta().getVariables().getDiferenciaPermitida().LecturasElectronicasMecanicas;
        islaId = Long.parseLong(getActivity().getIntent().getStringExtra("islaId"));
        sucursalId = Long.parseLong(db.getIdSucursal());
        modales = new Modales(getActivity());
    }
    private void onClickButton() {
    }

    private void obtenerMecanicaInical(){
        for (CierreCarrete item: respuestaApiCierreCarretes.getObjetoRespuesta() ){
            mecanicasInicales.add(item.getValorInicial());

        }

    }

}