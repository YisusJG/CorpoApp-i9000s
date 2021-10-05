package com.corpogas.corpoapp.VentaCombustible;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.corpogas.corpoapp.Configuracion.SQLiteBD;
import com.corpogas.corpoapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class VentaProductos extends AppCompatActivity {
    SQLiteBD data;
    String EstacionId,  ipEstacion, lugarproviene, idUsuario, sucursalId, poscicionCarga, estacionJarreo;

    Button btnCombustibleVenta, btnPerifericosVentas, btnIncrementarProducto, btnDecrementarProducto, btnEscanearProducto;
    ListView lstProductos;

    List<String> NombreProducto;
    List<String> PrecioProducto;
    List<String> ClaveProducto;
    List<String> CodigoBarras;
    List<String> ExistenciaProductos;
    List<String> ProductoId, CategoriaId, ID;
    ProgressDialog bar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venta_productos);

        SQLiteBD db = new SQLiteBD(getApplicationContext());
        data = new SQLiteBD(getApplicationContext());
        this.setTitle(data.getNombreEstacion() + " ( EST.:" + data.getNumeroEstacion() + ")");
        EstacionId = data.getIdEstacion();
        sucursalId = data.getIdSucursal();
        ipEstacion = data.getIpEstacion();

        init();

        lstProductos = findViewById(R.id.lstProductos);
        btnCombustibleVenta = findViewById(R.id.btnGasolinas);
        btnCombustibleVenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(VentaProductos.this, "Entro Boton Gas Venta", Toast.LENGTH_SHORT).show();
                ActivaDesactiva (false);
            }
        });
        btnPerifericosVentas = findViewById(R.id.btnAceitesVentas);
        btnPerifericosVentas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivaDesactiva (true);
//                Toast.makeText(VentaProductos.this, "Entro Boton Perifericos Venta", Toast.LENGTH_SHORT).show();
            }
        });
        btnIncrementarProducto = findViewById(R.id.btnAumentar);
        btnIncrementarProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(VentaProductos.this, "Entro Boton Mas Venta", Toast.LENGTH_SHORT).show();

            }
        });
        btnDecrementarProducto = findViewById(R.id.btnDisminuir);
        btnDecrementarProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(VentaProductos.this, "Entro Boton Menos Venta", Toast.LENGTH_SHORT).show();
            }
        });

        btnEscanearProducto = findViewById(R.id.btnEscanearProducto);
        btnEscanearProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(VentaProductos.this, "Entro Boton Escanear Venta", Toast.LENGTH_SHORT).show();

            }
        });
        ActivaDesactiva (false);
//        btnDecrementarProducto.setEnabled(false);
//        btnIncrementarProducto.setEnabled(false);
//        btnEscanearProducto.setEnabled(false);

        cargaProductos("1");

    }

    private void init() {
        data = new SQLiteBD(getApplicationContext());
        this.setTitle(data.getNombreEstacion() + " ( EST.:" + data.getNumeroEstacion() + ")");
        EstacionId = data.getIdEstacion();
        sucursalId = data.getIdSucursal();
        ipEstacion = data.getIpEstacion();
        poscicionCarga = getIntent().getStringExtra("posicionCarga");
        idUsuario = getIntent().getStringExtra("numeroEmpleado");
        estacionJarreo = getIntent().getStringExtra("estacionjarreo");

    }

    private void ActivaDesactiva(Boolean Activa){
        btnDecrementarProducto.setEnabled(Activa);
        btnIncrementarProducto.setEnabled(Activa);
        btnEscanearProducto.setEnabled(Activa);

    }

    private void cargaProductos(String TipoProducto) {
        bar = new ProgressDialog(VentaProductos.this);
        bar.setTitle("Cargando Productos");
        bar.setMessage("Ejecutando... ");
        bar.setIcon(R.drawable.gas);
        bar.setCancelable(false);
        bar.show();


        SQLiteBD data = new SQLiteBD(getApplicationContext());
        String posicion = getIntent().getStringExtra("pos");
        String url = "http://" + ipEstacion + "/CorpogasService/api/islas/productos/sucursal/"+sucursalId+"/posicionCargaId/"+poscicionCarga;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mostrarProductos(response, TipoProducto);
//                    Toast.makeText(VentaCombustibleAceites.this, "Entro", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString(), Toast.LENGTH_LONG).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this.getApplicationContext());
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(stringRequest);
    }

    private void mostrarProductos(String response, String TipoProducto) {

        String preciol = null;
        String DescLarga;
        String idArticulo;

        //Declaracion de variables

        ID = new ArrayList<String>();

        NombreProducto = new ArrayList<String>();
        PrecioProducto = new ArrayList<>();
        ClaveProducto = new ArrayList();
        CodigoBarras = new ArrayList();
        ExistenciaProductos = new ArrayList();
        ProductoId = new ArrayList();
        CategoriaId = new ArrayList<>();

        //ArrayList<singleRow> singlerow = new ArrayList<>();
        try {
            JSONObject p1 = new JSONObject(response);

            //String ni = p1.getString("NumeroInterno");
            String bodega = p1.getString("Bodega");
            JSONObject ps = new JSONObject(bodega);
            String producto = p1.getString("BodegaProductos");
            JSONArray bodegaprod = new JSONArray(producto);

            for (int i = 0; i <bodegaprod.length() ; i++){
                String IdProductos = null;
                JSONObject pA = bodegaprod.getJSONObject(i);
                String productoclave = pA.getString("Producto");
                JSONObject prod = new JSONObject(productoclave);
                String categoriaid = prod.getString("ProductCategoryId");
                if (categoriaid.equals(TipoProducto)){
                    //NO CARGA LOS COMBUSTIBLES
                    String ExProductos=pA.getString("Existencias");
                    ExistenciaProductos.add(ExProductos);
                    String TProductoId="2";//prod.getString("TipoSatProductoId");
                    DescLarga=prod.getString("LongDescription");
                    idArticulo=prod.getString("Id");
                    String codigobarras=prod.getString("Barcode");
                    String PControl=prod.getString("ProductControls");

                    JSONArray PC = new JSONArray(PControl);
                    for (int j = 0; j < PC.length(); j++) {
                        JSONObject Control = PC.getJSONObject(j);
                        preciol = Control.getString("Price");
                        IdProductos = Control.getString("Id"); //ProductoId
                    }
                    NombreProducto.add("ID: " + idArticulo + "    |     $"+preciol); // + "    |    " + IdProductos );
                    ID.add(DescLarga);
                    PrecioProducto.add(preciol);
                    ClaveProducto.add(idArticulo);
                    CodigoBarras.add(codigobarras);
                    ProductoId.add(IdProductos);
                    CategoriaId.add(categoriaid);
                }else{

                }
            }
            bar.cancel();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final ListAdapterAceitesCombustibles adapterP = new ListAdapterAceitesCombustibles(this,  ID, NombreProducto);
        lstProductos=(ListView)findViewById(R.id.lstProductos);
        lstProductos.setTextFilterEnabled(true);
        lstProductos.setAdapter(adapterP);
//        Agregado  click en la lista
        lstProductos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(TipoProducto.equals("1")){
                    String precio = PrecioProducto.get(i);
                    String claveProducto = ClaveProducto.get(i);
                    Intent intent = new Intent(getApplicationContext(), EligePrecioLitros.class);
                    intent.putExtra("numeroEmpleado", idUsuario);
                    intent.putExtra("posicionCarga", poscicionCarga);
                    intent.putExtra("estacionjarreo", estacionJarreo);
                    intent.putExtra("claveProducto", claveProducto);
                    intent.putExtra("precioProducto", precio);

                    startActivity(intent);
                    finish();
                }

            }
        });
    }

}