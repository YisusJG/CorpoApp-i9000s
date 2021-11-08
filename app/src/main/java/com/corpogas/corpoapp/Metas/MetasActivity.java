package com.corpogas.corpoapp.Metas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.corpogas.corpoapp.Entregas.Adapters.RVAdapterValesPapel;
import com.corpogas.corpoapp.Metas.Adapters.AdapterMetas;
import com.corpogas.corpoapp.Metas.Entities.Metas;
import com.corpogas.corpoapp.R;

import java.util.ArrayList;
import java.util.List;

public class MetasActivity extends AppCompatActivity {
//    private TextView txvTipoProducto,txvDespachos,txvMetas,txvVendidos,txtDiferencias;
    private RecyclerView rcvMetas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_metas);
        init();
        initialData();
    }

    private void init() {
        rcvMetas = findViewById(R.id.rcvMetas);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext().getApplicationContext());
        rcvMetas.setLayoutManager(linearLayoutManager);
        rcvMetas.setHasFixedSize(true);
    }

    private void initialData() {
        List<Metas> lMetas= new ArrayList<Metas>();
        lMetas.add(new Metas(1,"ACEITES",785,50,32,18));
        lMetas.add(new Metas(1,"ADITIVOS",4520,95,45,50));
        lMetas.add(new Metas(1,"OTROS",40,92,93,0));
        lMetas.add(new Metas(1,"COMBUSTIBLES",420,902,222,680));

        initialAdapter(lMetas);
    }

    private void initialAdapter(List<Metas> lMetas) {
        AdapterMetas adapter = new AdapterMetas(lMetas);
        rcvMetas.setAdapter(adapter);

    }
}