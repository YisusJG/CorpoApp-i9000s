package com.corpogas.corpoapp.Entregas;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Spinner;
import android.widget.Toast;

import com.corpogas.corpoapp.Configuracion.CorteDB;
import com.corpogas.corpoapp.Configuracion.SQLiteBD;
import com.corpogas.corpoapp.Entregas.Entities.PaperVoucherType;
import com.corpogas.corpoapp.Interfaces.Endpoints.EndPoints;
import com.corpogas.corpoapp.R;

import java.util.List;

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
//    private AdapterValesPapel adapterValesPapel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrega_vales);
        init();
        if(paperVoucherType.size() == 0) insertarValesPapelSqlite();
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
//                tipoValesPapel();

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
    }
}