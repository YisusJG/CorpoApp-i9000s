package com.corpogas.corpoapp.Productos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.corpogas.corpoapp.Conexion;
import com.corpogas.corpoapp.Configuracion.SQLiteBD;
import com.corpogas.corpoapp.Entities.Classes.RecyclerViewHeaders;
import com.corpogas.corpoapp.Fajillas.AutorizaFajillas;
import com.corpogas.corpoapp.Fajillas.RecyclerViewHeadersFajillas;
import com.corpogas.corpoapp.Menu_Principal;
import com.corpogas.corpoapp.Modales.Modales;
import com.corpogas.corpoapp.Puntada.PosicionPuntadaRedimir;
import com.corpogas.corpoapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Inventario extends AppCompatActivity {
    SQLiteBD data;
    Long sucursalId;
    String ipEstacion, numeroEmpleado;
    RecyclerView rcvInventarioProductos;
    ListView lstInventario;
    ProgressDialog bar;
    List<RecyclerViewHeaders> lrcvPosicionCarga;
    List<RecyclerViewHeadersFajillas> lFajillasEntregadas;

    List<String> NombreProducto;
    List<String> PrecioProducto;
    List<String> ClaveProducto;
    List<String> ExistenciaProductos;
    List<String> DescripcionPr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventario);

        init();
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//        rcvInventarioProductos.setLayoutManager(linearLayoutManager);
//        rcvInventarioProductos.setHasFixedSize(true);

        //        initializeAdapter();

        MostrarProductos();
    }



    private void MostrarProductos() {
        if (!Conexion.compruebaConexion(this)) {
            Toast.makeText(getBaseContext(), "Sin conexión a la red ", Toast.LENGTH_SHORT).show();
            Intent intent1 = new Intent(getApplicationContext(), Menu_Principal.class);
            startActivity(intent1);
            finish();
        } else {
            bar = new ProgressDialog(Inventario.this);
            bar.setTitle("Cargando Productos");
            bar.setMessage("Ejecutando... ");
            bar.setIcon(R.drawable.productos);
            bar.setCancelable(false);
            bar.show();
            String url;
            url = "http://" + ipEstacion + "/CorpogasService/api/BodegaProductos/obtieneExistenciaPorEmpleado/sucursalId/" + sucursalId + "/numeroEmpleado/"+numeroEmpleado;
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    //mostarProductor(response);
                    mostrarProductosExistencias(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                    bar.cancel();
                }
            });
            RequestQueue requestQueue = Volley.newRequestQueue(this.getApplicationContext());
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        }
    }

    private void mostrarProductosExistencias(String response){
        //Declaracion de variables
        String descripcionProducto;
        String idArticulo, existencias;

        ExistenciaProductos = new ArrayList();
        DescripcionPr = new ArrayList();
        ClaveProducto = new ArrayList();

        //ArrayList<singleRow> singlerow = new ArrayList<>();
        try {
            JSONObject p1 = new JSONObject(response);

            String correcto = p1.getString("Correcto");
            String mensaje = p1.getString("Mensaje");
            if (correcto.equals("true")){
                String objetoRespuesta = p1.getString("ObjetoRespuesta");
                JSONArray respuesta = new JSONArray(objetoRespuesta);
                for (int i = 0; i <respuesta.length() ; i++){
                    JSONObject respuestaObjecto = respuesta.getJSONObject(i);
                    idArticulo = respuestaObjecto.getString("ProductoId");
                    existencias = respuestaObjecto.getString("Existencias");
                    JSONObject  productos = new JSONObject(respuestaObjecto.getString("Producto"));
                    descripcionProducto = productos.getString("LongDescription");
                    ExistenciaProductos.add(existencias);
                    DescripcionPr.add(descripcionProducto);
                    ClaveProducto.add(" ID: " + idArticulo);
                }
            }else{
                String titulo = "AVISO ";
                String mensajes = "Sin Productos en la Base de Datos" ;
                Modales modales = new Modales(Inventario.this);
                View view1 = modales.MostrarDialogoAlertaAceptar(Inventario.this,mensajes,titulo);
                view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        modales.alertDialog.dismiss();
                        finish();
                    }
                });
            }
            bar.cancel();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final ListAdapterInventarioProductos adapterP = new ListAdapterInventarioProductos(this, ExistenciaProductos, DescripcionPr, ClaveProducto);
        lstInventario.setTextFilterEnabled(true);
        lstInventario.setAdapter(adapterP);

    }

    private void init(){
        data = new SQLiteBD(getApplicationContext());
        this.setTitle(data.getNombreEstacion() + " ( EST.:" + data.getNumeroEstacion() + ")");
        sucursalId = Long.parseLong(data.getIdSucursal());
        ipEstacion = data.getIpEstacion();
        numeroEmpleado = data.getNumeroEmpleado();
        lstInventario = (ListView) findViewById(R.id.lstInventario);
    }



}