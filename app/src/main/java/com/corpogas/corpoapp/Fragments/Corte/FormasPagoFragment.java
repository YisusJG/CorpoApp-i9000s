package com.corpogas.corpoapp.Fragments.Corte;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.corpogas.corpoapp.Adapters.Corte.ListAdapterFormasPago;
import com.corpogas.corpoapp.Configuracion.SQLiteBD;
import com.corpogas.corpoapp.Entities.Accesos.AccesoUsuario;
import com.corpogas.corpoapp.Entities.Classes.RespuestaApi;
import com.corpogas.corpoapp.Entities.Cortes.CierreFormaPago;
import com.corpogas.corpoapp.Interfaces.Endpoints.EndPoints;
import com.corpogas.corpoapp.R;
import com.corpogas.corpoapp.Token.GlobalToken;

import org.json.JSONArray;
import org.json.JSONException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FormasPagoFragment extends Fragment {
    View view;
    SQLiteBD db;
    TextView totalPicos, subTotalOficina;
//    Button btnAceptar;
    long sucursalId, idusuario, islaId;
    String ipEstacion, resultadoTotalFormasPago;
    double sumaTotalFormas;
    JSONArray contSubtotalOficina = new JSONArray();

    RespuestaApi<AccesoUsuario> respuestaApiAccesoUsuario;
    RespuestaApi<List<CierreFormaPago>> respuestaApiCierreFormaPago;

    List<String> maintitle;
    List<String> subtitle;
    List<String> total;
    ListView list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_formas_pago, container, false);

        init();
        getObjetos();
        setVariables();
        obtenerFormaPagosUltimoTurno();

        return view;
    }


    private void init(){

        totalPicos = (TextView)view.findViewById(R.id.textTotalPicosFormasPago);
        subTotalOficina = (TextView) view.findViewById(R.id.textSubtotalOficinaFormasPago);
//        btnAceptar = view.findViewById(R.id.btnAceptarFormasPago);

    }

    private void getObjetos(){

    respuestaApiAccesoUsuario = (RespuestaApi<AccesoUsuario>) getActivity().getIntent().getSerializableExtra("respuestaApiAccesoUsuario");

    }

    private void setVariables(){
        db = new SQLiteBD(getContext());
        sucursalId = Long.parseLong(db.getIdSucursal());
        ipEstacion = db.getIpEstacion();
        idusuario = respuestaApiAccesoUsuario.getObjetoRespuesta().getNumeroEmpleado();
        islaId = Long.parseLong(getActivity().getIntent().getStringExtra("islaId"));
    }


    public void obtenerFormaPagosUltimoTurno(){

        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("http://"+ ipEstacion  +"/corpogasService/")//http://" + data.getIpEstacion() + "/corpogasService_Entities_token/
                .baseUrl("http://10.0.1.40/CorpogasService_entities_token/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        EndPoints obtenerFormaPagosUltimoTurno = retrofit.create(EndPoints.class);
        Call<RespuestaApi<List<CierreFormaPago>>> call = obtenerFormaPagosUltimoTurno.getFormaPagosUltimoTurno(sucursalId, islaId, db.getToken());
        call.enqueue(new Callback<RespuestaApi<List<CierreFormaPago>>>() {


            @Override
            public void onResponse(Call<RespuestaApi<List<CierreFormaPago>>> call, Response<RespuestaApi<List<CierreFormaPago>>> response) {
                if (!response.isSuccessful()) {
                    GlobalToken.errorTokenWithReload(requireActivity());
                    return;
                }
                respuestaApiCierreFormaPago = response.body();
                mostrarFormasPago();
                BigDecimal bd = new BigDecimal(sumaTotalFormas);
                // Se limite el resultado a 4 decimales y se redondea
                resultadoTotalFormasPago = String.valueOf(bd.setScale(2, RoundingMode.HALF_UP));

                subTotalOficina.setText("SUBTOTAL OFICINA: $ "+ resultadoTotalFormasPago);
                contSubtotalOficina = new JSONArray();

            }

            @Override
            public void onFailure(Call<RespuestaApi<List<CierreFormaPago>>> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void mostrarFormasPago(){
        if(respuestaApiCierreFormaPago.Correcto == true)
        {
            maintitle = new ArrayList<String>();
            subtitle = new ArrayList<String>();
            total = new ArrayList<String>();
            String descripcionCorta = "";
            int numeroTickets = 0;
            double cantidad = 0;
            for (CierreFormaPago item: respuestaApiCierreFormaPago.getObjetoRespuesta())
            {
                descripcionCorta = item.getFormaPago().getShortDescription();
                numeroTickets = item.getNumeroTickets();
                cantidad = item.getCantidad();

                maintitle.add(String.valueOf(numeroTickets));
                subtitle.add(descripcionCorta);
                total.add(String.valueOf(cantidad));
                try {
                    contSubtotalOficina.put(cantidad);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            final ListAdapterFormasPago adapter = new ListAdapterFormasPago((Activity) getContext(), maintitle, subtitle, total);
            list = (ListView) view.findViewById(R.id.listFormasPago);
            list.setAdapter(adapter);

            sumaTotalFormas = calcularTotalFormasPago();

        }
    }

    // Se crea el metodo calcularTotalImporte para sumar los valores que tiene el arreglo importe
    public double calcularTotalFormasPago() {
        double sumamax = 0;
        for (int i = 0; i < contSubtotalOficina.length(); i++) {
            double suma = 0;
            try {
                suma = contSubtotalOficina.getDouble(i);
                sumamax += suma;
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return sumamax;
    }
}