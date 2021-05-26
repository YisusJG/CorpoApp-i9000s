package com.corpogas.corpoapp.VentaCombustible;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.corpogas.corpoapp.Configuracion.SQLiteBD;
import com.corpogas.corpoapp.R;

public class ProcesoVenta extends AppCompatActivity {
    RecyclerView rcvProcesoVenta;
    String carga, usuarioid, EmpleadoNumero;
    String EstacionId, sucursalId, ipEstacion, numeroTarjetero, lugarproviene, usuario, posicion, clave;
    Boolean banderaposicionCarga;
    SQLiteBD db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proceso_venta);
        init();
//        posicionCargaFinaliza();
    }

    private void init() {
        rcvProcesoVenta = (RecyclerView)findViewById(R.id.rcvProcesoVenta);
        db = new SQLiteBD(getApplicationContext());
        this.setTitle(db.getRazonSocial());
        this.setTitle(db.getNombreEsatcion() + " ( EST.:" + db.getNumeroEstacion() + ")");
        EstacionId = db.getIdEstacion();
        sucursalId = db.getIdSucursal();
        ipEstacion = db.getIpEstacion();
        lugarproviene = getIntent().getStringExtra("lugarproviene");
        usuarioid = getIntent().getStringExtra("usuario");
        usuario = getIntent().getStringExtra("clave");
    }
}