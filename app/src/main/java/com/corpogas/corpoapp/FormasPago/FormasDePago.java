package com.corpogas.corpoapp.FormasPago;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.corpogas.corpoapp.Adapters.RVAdapter;
import com.corpogas.corpoapp.Configuracion.SQLiteBD;
import com.corpogas.corpoapp.Entities.Classes.RecyclerViewHeaders;
import com.corpogas.corpoapp.R;

import java.util.ArrayList;
import java.util.List;

public class FormasDePago extends AppCompatActivity {
    RecyclerView rcvFormasPago;
    List<RecyclerViewHeaders> lFormasPago;
    SQLiteBD db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formas_de_pago);
        init();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvFormasPago.setLayoutManager(linearLayoutManager);
        rcvFormasPago.setHasFixedSize(true);
        initializeDataFormasDePago();
        initializeAdapterFormasDePago();

    }

    private void init() {
        db = new SQLiteBD(getApplicationContext());
        this.setTitle(db.getRazonSocial());
        this.setTitle(db.getNombreEsatcion() + " ( EST.:" + db.getNumeroEstacion() + ")");
        rcvFormasPago = findViewById(R.id.rcvFormasPago);
    }

    private void initializeDataFormasDePago() {
        lFormasPago = new ArrayList<>();
        lFormasPago.add(new RecyclerViewHeaders("VALES","",R.drawable.vale));
        lFormasPago.add(new RecyclerViewHeaders("AMERICAN EXPRES","",R.drawable.american));
        lFormasPago.add(new RecyclerViewHeaders("VISA MASTERCARD","",R.drawable.visa));
        lFormasPago.add(new RecyclerViewHeaders("VALE ELECTRONICO","",R.drawable.valeelectronico));
        lFormasPago.add(new RecyclerViewHeaders("GAS CARD AMEX","",R.drawable.gascard));
        lFormasPago.add(new RecyclerViewHeaders("MERCADO PAGO","",R.drawable.gascard));
        lFormasPago.add(new RecyclerViewHeaders("VARIAS FORMAS DE PAGO","",R.drawable.variasformaspago));
    }

    private void initializeAdapterFormasDePago() {
        RVAdapter adapter = new RVAdapter(lFormasPago);
//        adapter.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String titulo;
//                Toast.makeText(getApplicationContext(),"Seleccion :" + lProductosCarrito.get(rcvProductosCarrito.getChildAdapterPosition(v)).getTitulo(), Toast.LENGTH_SHORT).show();
//            }
//        });

        rcvFormasPago.setAdapter(adapter);
    }


}