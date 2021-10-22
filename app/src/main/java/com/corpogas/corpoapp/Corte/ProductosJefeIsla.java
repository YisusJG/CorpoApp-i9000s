package com.corpogas.corpoapp.Corte;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.corpogas.corpoapp.Configuracion.SQLiteBD;
import com.corpogas.corpoapp.Corte.Adapters.ListAdapterProductos;
import com.corpogas.corpoapp.Entities.Accesos.AccesoUsuario;
import com.corpogas.corpoapp.Entities.Classes.RespuestaApi;
import com.corpogas.corpoapp.Entities.Cortes.ProductosFaltantes;
import com.corpogas.corpoapp.Entregas.EntregaPicosActivity;
import com.corpogas.corpoapp.Interfaces.Endpoints.EndPoints;
import com.corpogas.corpoapp.Login.EntregaPicos;
import com.corpogas.corpoapp.Modales.Modales;
import com.corpogas.corpoapp.Productos.VentasProductos;
import com.corpogas.corpoapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProductosJefeIsla extends AppCompatActivity {

    int resultado;
    Button btnAutorizacion, btnVentaProductos, btnRefresh;
    TextView tvEntregado, tvRecibi;
    EditText edtNipAutorizacion;
    SQLiteBD db;
    Long sucursalId ;
    ImageButton btnListaProductosResumenActivity;

    String  idusuario, numerodispositivo, turnoId,  fechaTrabajo, ipEstacion, claveUsuario, titulo, mensaje, nipAutorizacion;
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

    RespuestaApi<AccesoUsuario> respuestaApiAccesoUsuario;
    Modales modales;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productos);

        init();
        setVariables();
        onClicks();
        ObtieneProductosCierre(1);
    }

    private void init() {
        tvRecibi = (TextView) findViewById(R.id.tvRecibi);
        listProductos = (ListView) findViewById(R.id.lisproductos);
        btnAutorizacion = (Button) findViewById(R.id.btnaceptar);
        btnVentaProductos = (Button) findViewById(R.id.btnsiguiente);
        btnRefresh = (Button) findViewById(R.id.btnrefresh);
        btnListaProductosResumenActivity = findViewById(R.id.btnListaProductosResumenActivity);


    }


    private void setVariables() {
        db = new SQLiteBD(getApplicationContext());
        sucursalId = Long.parseLong(db.getIdSucursal());
        ipEstacion = db.getIpEstacion();
        idusuario = db.getNumeroEmpleado();
        claveUsuario = db.getClave();
        modales = new Modales(ProductosJefeIsla.this);

    }

    private void onClicks(){

        btnAutorizacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titulo = "CONFIRMACION";
                mensaje = "Ingresa NIP de confirmación.";
                View viewLectura = modales.MostrarDialogoInsertaDato(ProductosJefeIsla.this, mensaje, titulo);
                edtNipAutorizacion= ((EditText) viewLectura.findViewById(R.id.textInsertarDato));
                edtNipAutorizacion.setInputType(InputType.TYPE_CLASS_NUMBER);
                viewLectura.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        nipAutorizacion = edtNipAutorizacion.getText().toString();
                        if (nipAutorizacion.isEmpty()) {
                            edtNipAutorizacion.setError("Ingresa NIP");
                            return;
                        }else{
                            obtenerObjetoAccesoUsuario();
                        }

                    }
                });

                viewLectura.findViewById(R.id.buttonNo).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        modales.alertDialog.dismiss();
                    }
                });




            }
        });

        btnVentaProductos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductosJefeIsla.this, VentasProductos.class); //claveFinVenta
                intent.putExtra("lugarproviene", "Corte");
                intent.putExtra("NumeroEmpleado", idusuario);
                intent.putExtra("usuario", claveUsuario);
                intent.putExtra("cadenaproducto", "");
//                intent.putExtra("device_name", m_deviceName);
                startActivity(intent);
            }
        });

        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ObtieneProductosCierre(1);
            }
        });

    }

    private void ObtieneProductosCierre(Integer integer) {
        btnAutorizacion.setEnabled(true);
//            banderaSigue = true;
        String url = "http://" + ipEstacion + "/CorpogasService/api/cierreDetalles/ObtieneVentasPorDespachador/sucursalId/" + sucursalId + "/numeroEmpleado/" + idusuario;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                MostrarProductosCierre(response, integer);
//                    cargaProductosCierre(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //asiganmos a una variable el error para desplegar la descripcion de Tickets no asignados a la terminal
                String algo = new String(error.networkResponse.data);
                try {
                    //creamos un json Object del String algo
                    JSONObject errorCaptado = new JSONObject(algo);
                    //Obtenemos el elemento ExceptionMesage del errro enviado
                    String errorMensaje = errorCaptado.getString("ExceptionMessage");
                    Toast.makeText(getApplicationContext(), errorMensaje, Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(12000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);

    }

    private void MostrarProductosCierre(String Response, Integer opcion){
        VentaProductos = 0;
        String TProductoId;
        Long cantidad;

        maintitle = new ArrayList<String>();
        subtitle = new ArrayList<String>();
        calculo = new ArrayList<String>();
        cantidadEntregada = new ArrayList<String>();

        listID = new ArrayList<Long>();

        listNombreProducto = new ArrayList<String>();
        listprecio = new ArrayList<>();
        listClaveProducto = new ArrayList();
        listCodigobarras = new ArrayList();
        listProductosId = new ArrayList();
        //TipoProductoId = new ArrayList();

        listProductosVendidos = new ArrayList();
        listProductosRecibidos = new ArrayList();

        try {
            JSONObject p1 = new JSONObject(Response);

            //String ni = p1.getString("NumeroInterno");
            String objetorespuesta = p1.getString("ObjetoRespuesta");
            //JSONObject ps = new JSONObject(objetorespuesta);
            //String producto = ps.getString("CierreDetalles");
            JSONArray cierredetalles = new JSONArray(objetorespuesta);

            for (int i = 0; i < cierredetalles.length(); i++) { //cierredetalles.length() //3
                String IdProductos = null;
                JSONObject pA = cierredetalles.getJSONObject(i);
                String productoclave = pA.getString("ProductoDescripcionCorta"); //ProductoDescripcion
                String DescLarga = pA.getString("ProductoDescripcionCorta");//ProductoDescripcion
                TProductoId = pA.getString("CategoriaProductoId");
//                JSONObject categoriaProducto = pA.getJSONObject("CategoriaProducto");
                int numeroInterno = 100;//categoriaProducto.getLong("Id"); //NumeroInterno
                String idArticulo = pA.getString("ProductoId"); //NumeroInterno
                String codigobar = pA.getString("CodigoBarras");
                Long cantidadvendida = pA.getLong("Cantidad");
                Double preciounitario = pA.getDouble("Precio");
                Long cantidadrecibida = pA.getLong("CantidadRecibida");

                if (cantidadvendida!=0){
                    VentaProductos += (preciounitario * cantidadvendida);
                }
                //               IdProductos = pA.getString("RecursoId");
                //String PControl=prod.getString("ProductoControles");
                //JSONArray PC = new JSONArray(PControl);

                //               if (numeroInterno.equals("1") || numeroInterno.equals("5")) {// No considera combustibles
                //               } else {
                //                   maintitle.add("-");
                maintitle.add(String.valueOf(cantidadrecibida - cantidadvendida));
                subtitle.add(DescLarga + " (" + idArticulo + ")");
                calculo.add(String.valueOf(cantidadrecibida));

                listID.add(Long.valueOf(numeroInterno));
                listClaveProducto.add(Long.valueOf(idArticulo));
                //                   ProductosId.add(IdProductos);
                listCodigobarras.add(codigobar);
                listProductosVendidos.add(cantidadvendida);
                listprecio.add(preciounitario);
                listProductosRecibidos.add(cantidadrecibida);
                //TipoProductoId.add(TProductoId);
                //                   if (cantidadvendida > cantidadrecibida) {
                //                       cantidad = Long.valueOf(0);
                //                   } else {
                //                       cantidad = cantidadrecibida - cantidadvendida;
                //                   }
                //                   JSONObject mjasonF = new JSONObject();
                //               }
            }
            if (opcion.equals(2)){
//                dbCorte.InsertarInventarioProductos(100000, VentaProductos, 0, "codigobarras", "DescLarga", 0, 0, 0, 0, 0 );
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ListAdapterProductos adapterP = new ListAdapterProductos(ProductosJefeIsla.this, calculo, subtitle, maintitle);
        listProductos.setTextFilterEnabled(true);
        listProductos.setAdapter(adapterP);

    }

    public void obtenerObjetoAccesoUsuario(){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://"+ ipEstacion  +"/corpogasService/")//http://" + data.getIpEstacion() + "/corpogasService_Entities_token/
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        EndPoints obtenerAccesoUsuario = retrofit.create(EndPoints.class);
        Call<RespuestaApi<AccesoUsuario>> call = obtenerAccesoUsuario.getAccesoUsuario(sucursalId, nipAutorizacion);
        call.enqueue(new Callback<RespuestaApi<AccesoUsuario>>() {

            @Override
            public void onResponse(Call<RespuestaApi<AccesoUsuario>> call, retrofit2.Response<RespuestaApi<AccesoUsuario>> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                respuestaApiAccesoUsuario = response.body();
                usuarioCorrecto();
            }

            @Override
            public void onFailure(Call<RespuestaApi<AccesoUsuario>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    public void usuarioCorrecto(){
        boolean correcto = respuestaApiAccesoUsuario.Correcto;
        if (correcto == true) {
            if (respuestaApiAccesoUsuario.getObjetoRespuesta().getClave().equals(nipAutorizacion) && respuestaApiAccesoUsuario.getObjetoRespuesta().getNumeroInternoRol() == 3) {
                int banderaConfirmaInventario = 1;
                Intent intent = new Intent(getApplicationContext(), ResumenActivity.class); //IslasEstacion.class aqui iba antes
                intent.putExtra("banderaConfirmaInventario", banderaConfirmaInventario);
//                intent.putExtra("accesoUsuario", respuestaApiAccesoUsuario);
                startActivity(intent);
                finish();

            } else {
                edtNipAutorizacion.setError("No eres jefe de isla");
            }

        } else {
            edtNipAutorizacion.setError(respuestaApiAccesoUsuario.Mensaje);
        }

    }


}