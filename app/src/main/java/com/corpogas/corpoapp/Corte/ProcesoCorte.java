package com.corpogas.corpoapp.Corte;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.corpogas.corpoapp.Adapters.AdapterRvProcesoCorte;
import com.corpogas.corpoapp.Entities.Cortes.CierreDespachoDetalle;
import com.corpogas.corpoapp.Entities.Cortes.MenuCorte;
import com.corpogas.corpoapp.Entities.Cortes.ProductosFaltantes;
import com.corpogas.corpoapp.Entities.Cortes.ValePapelDenominacion;
import com.corpogas.corpoapp.Interfaces.iDesgloceVales;
import com.corpogas.corpoapp.Interfaces.iDespachoDetalles;
import com.corpogas.corpoapp.Interfaces.iProcesosCorteTerminado;
import com.corpogas.corpoapp.Interfaces.iProductosFaltantes;
import com.corpogas.corpoapp.Menu_Principal;
import com.corpogas.corpoapp.R;

import java.util.ArrayList;

public class ProcesoCorte extends AppCompatActivity implements iDesgloceVales, iProductosFaltantes, iProcesosCorteTerminado, iDespachoDetalles {

    private RecyclerView recyclerView;
    private AdapterRvProcesoCorte adapterRvProcesoCorte;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proceso_corte);

        ArrayList<MenuCorte> item = new ArrayList<>();
        item.add(new MenuCorte(R.drawable.lecturasmecanicas,"Lecturas mecanicas"));
        item.add(new MenuCorte(R.drawable.productos,"Productos"));
        item.add(new MenuCorte(R.drawable.productos_faltantes,"Productos faltantes"));
        item.add(new MenuCorte(R.drawable.fajillas_billetes,"Fajillas billetes"));
        item.add(new MenuCorte(R.drawable.fajillas_monedas,"Fajillas monedas"));
        item.add(new MenuCorte(R.drawable.picos,"Picos"));
        item.add(new MenuCorte(R.drawable.vales_papel,"Vales de papel"));
        item.add(new MenuCorte(R.drawable.vales_papel,"Desgloce vales"));
        item.add(new MenuCorte(R.drawable.formas_pago,"Formas de pago"));
        item.add(new MenuCorte(R.drawable.combustibles,"Combustibles"));
        item.add(new MenuCorte(R.drawable.gran_total,"GranTotal"));

        recyclerView = findViewById(R.id.rvOpcionesCorte);
        adapterRvProcesoCorte = new AdapterRvProcesoCorte(item, ProcesoCorte.this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false));
        recyclerView.setAdapter(adapterRvProcesoCorte);
    }


    @Override
    public void desgloceVales(ArrayList<ValePapelDenominacion> valePapelDenominacion) {
        adapterRvProcesoCorte.RecuperaDatosVales(valePapelDenominacion);

    }

    @Override
    public void productosFaltantes(ArrayList<ProductosFaltantes> productosFaltantes) {
        adapterRvProcesoCorte.ProductosFaltantes(productosFaltantes);

    }

    @Override
    public void procesoFajillasBilletes(Boolean fajillasBilletesCompletado) {
        adapterRvProcesoCorte.FajillaBilletesCompletado(fajillasBilletesCompletado);
    }

    @Override
    public void procesoFajillasMonedas(Boolean fajillasMonedasCompletado, int dineroFajillasMonedas) {
        adapterRvProcesoCorte.FajillaMonedasCompletado(fajillasMonedasCompletado, dineroFajillasMonedas);
    }

    @Override
    public void procesoPicos(Boolean picosCompletado) {
        adapterRvProcesoCorte.PicosCompletado(picosCompletado);
    }

    @Override
    public void despachoDetalles(ArrayList<CierreDespachoDetalle> cierreDespachoDetalles) {
        adapterRvProcesoCorte.RecuperaCierreDespachoDetalle(cierreDespachoDetalles);
    }

    //Metodo para regresar a la actividad principal
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), Menu_Principal.class);

        startActivity(intent);
        // finish();
    }
}