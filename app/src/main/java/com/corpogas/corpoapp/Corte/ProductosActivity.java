package com.corpogas.corpoapp.Corte;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.corpogas.corpoapp.Configuracion.SQLiteBD;
import com.corpogas.corpoapp.Entities.Cortes.ProductosFaltantes;
import com.corpogas.corpoapp.R;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class ProductosActivity extends AppCompatActivity {

    int resultado;
    Button btnAutorizacion, btnVentaProductos, btnRefresh;
    TextView tvEntregado, tvRecibi;
    SQLiteBD db;
    Long sucursalId ;

    String  idusuario, numerodispositivo, turnoId,  fechaTrabajo, ipEstacion, claveUsuario;
    Long numeroEmpleado, islaId, cierreId;
    ListView listProductos;

    List<String> maintitle, subtitle,  cantidadEntregada, cantidadVendidos, total;
    ListView mList;

    JSONArray ArrayResultante = new JSONArray();
    List<String> calculo;
    List<String> listNombreProducto;
    List<Double> listprecio;
    List<Long> listID, listClaveProducto;
    List<String> listCodigobarras;
    List<Long> listProductosId;
    List<Long> listProductosVendidos;
    List<Long> listProductosRecibidos;
    List<Long> listBanderaProductoVendido;
    List<Long> listExistencias;
    List<String> listProductosEntregados;
    List<String>   listDescripciones;

    String m_deviceName;

    boolean banderaValidaBotonVentasFaltantes;
    double VentaProductos = 0;
    long cantidadAceites = 0;
    ArrayList<ProductosFaltantes> ArrayventasFaltantes = new ArrayList<ProductosFaltantes>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productos);

        init();
        setVariables();
    }

    private void init() {
        tvRecibi = (TextView) findViewById(R.id.tvRecibi);
        listProductos = (ListView) findViewById(R.id.lisproductos);
        btnAutorizacion = (Button) findViewById(R.id.btnaceptar);
        btnVentaProductos = (Button) findViewById(R.id.btnsiguiente);
        btnRefresh = (Button) findViewById(R.id.btnrefresh);
    }

    private void setVariables() {
        db = new SQLiteBD(getApplicationContext());
        sucursalId = Long.parseLong(db.getIdSucursal());
        ipEstacion = db.getIpEstacion();
    }


}