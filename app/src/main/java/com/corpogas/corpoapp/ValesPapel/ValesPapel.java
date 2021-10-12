package com.corpogas.corpoapp.ValesPapel;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
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
import com.corpogas.corpoapp.Modales.Modales;
import com.corpogas.corpoapp.Productos.ListAdapterProductos;
import com.corpogas.corpoapp.R;
import com.corpogas.corpoapp.VentaCombustible.VentaProductos;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ValesPapel extends AppCompatActivity {
    Spinner spnValesPapel;
    Button btnAgregarVale, btnAceptarValesPapel, btnLimpiarPantalla;
    ListView lstValesPapel;
    ImageView imgEscanearVale;

    int sumaTiposVale=0;
    String[] opcionesTipoVale;// = new String[sumaTiposVale];

    String posicionCarga, usuarioid, estacionJarreo, claveProducto, montoaCobrar, ValeSeleccionado, ipEstacion;
    EditText  tvFolioMonto, tvDenominacionMonto;
    TextView tvMontoACargar, tvMontoACargarPendiente;
    List<String> IDMontos;
    List<String> NombreTipoValePapel;
    List<String> MontoVale;
    List<String> FolioValePapel;
    SQLiteBD data;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vales_papel);
        data = new SQLiteBD(getApplicationContext());
        this.setTitle(data.getNombreEstacion() + " ( EST.:" + data.getNumeroEstacion() + ")");

        posicionCarga = getIntent().getStringExtra("posicionCarga");
        usuarioid = getIntent().getStringExtra("numeroEmpleado");
        estacionJarreo = getIntent().getStringExtra("estacionjarreo");
        claveProducto = getIntent().getStringExtra("claveProducto");
        montoaCobrar = String.valueOf(getIntent().getDoubleExtra("montoencanasta", 0));

        init();
        ObtieneTiposVale();

        btnAgregarVale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AgregarValePapelaLista();
            }
        });

        btnAceptarValesPapel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EnviarValesPapel();
            }
        });

        imgEscanearVale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    private void AgregarValePapelaLista(){
        Double montoPendiente;

        //Validamos campos a agregar <> de vacio
        if (tvFolioMonto.length() ==0){
            Toast.makeText(ValesPapel.this, "Digite un Folio", Toast.LENGTH_SHORT).show();
        }else{
            if (tvDenominacionMonto.length() == 0){
                Toast.makeText(ValesPapel.this, "Digite un Monto", Toast.LENGTH_SHORT).show();
            }else{
                if (tvMontoACargarPendiente.equals(0)){
                    Toast.makeText(ValesPapel.this, "Monto Completado", Toast.LENGTH_SHORT).show();
                }else{
                    montoPendiente = Double.parseDouble(tvMontoACargarPendiente.getText().toString());

                    if (Double.parseDouble(tvDenominacionMonto.getText().toString()) > montoPendiente){
                        Toast.makeText(ValesPapel.this, "El monto de los vales no puede ser mayor que el monto Total", Toast.LENGTH_SHORT).show();
                    }else{

                        boolean bandera=false;
                        for(Integer m = 0; m<MontoVale.size(); m++){
                            String codigo = MontoVale.get(m);
                            if (codigo.equals(tvFolioMonto.getText().toString())){
                                bandera=true;
                                break;
                            }else{
                                bandera=false;
                            }
                        }
                        if (bandera == false){
                            Double resultado = montoPendiente - Double.parseDouble(tvDenominacionMonto.getText().toString());
                            tvMontoACargarPendiente.setText(resultado.toString());
                            NombreTipoValePapel.add(ValeSeleccionado + "    |     $"+tvDenominacionMonto.getText().toString()); // + "    |    " + IdProductos );
                            IDMontos.add(tvDenominacionMonto.getText().toString());
                            MontoVale.add(tvFolioMonto.getText().toString());
                            ListAdapterProductos adapterP = new ListAdapterProductos(this, NombreTipoValePapel, MontoVale );
                            lstValesPapel.setTextFilterEnabled(true);
                            lstValesPapel.setAdapter(adapterP);
//                            lstValesPapel.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//                                @Override
//                                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
////                                    montoPendiente = Double.parseDouble(tvMontoACargarPendiente.getText().toString());
//                                    NombreTipoValePapel.remove(i);
////                                    MontoVale.remove(i);
//
//                                    adapterP.notifyDataSetChanged();
//                                    return false;
//                                }
//                            });
                            lstValesPapel.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int identificador, long l) {
                                    Toast.makeText(ValesPapel.this, "asda", Toast.LENGTH_SHORT).show();
                                    String titulo = "Estas seguro?";
                                    String mensajes = "Deseas eliminar el elemento seleccionado?";
                                    Modales modalesA = new Modales(ValesPapel.this);
                                    View viewLectura = modalesA.MostrarDialogoAlerta(ValesPapel.this, mensajes,  "SI", "NO");
                                    viewLectura.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
//                                            ID.remove(identificador);
                                            Double total = Double.parseDouble(IDMontos.get(identificador)) + Double.parseDouble(tvMontoACargarPendiente.getText().toString());
                                            tvMontoACargarPendiente.setText(total.toString());

                                            MontoVale.remove(identificador);
                                            NombreTipoValePapel.remove(identificador);

                                            ListAdapterProductos adapterP = new ListAdapterProductos(ValesPapel.this, NombreTipoValePapel, MontoVale );
                                            lstValesPapel.setAdapter(adapterP);
                                            adapterP.notifyDataSetChanged();
                                            modalesA.alertDialog.dismiss();
                                        }
                                    });
                                    viewLectura.findViewById(R.id.buttonNo).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            modalesA.alertDialog.dismiss();
                                        }
                                    });

                                }
                            });
                            tvDenominacionMonto.setText("");
                            tvFolioMonto.setText("");
                        }else{
                            Toast.makeText(ValesPapel.this, "El folio No. "+ tvFolioMonto.getText().toString() +" ya fue agregado", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        }
    }

    private void EnviarValesPapel(){

    }

    private void LimpiarPantallaValesPapel(){

    }

    private void init() {
        spnValesPapel = (Spinner) findViewById(R.id.spTipoValePapel);
        btnAgregarVale = (Button) findViewById(R.id.btnAgregarVale);
        btnAceptarValesPapel = (Button) findViewById(R.id.btnAceptarValesPapel);
        lstValesPapel = (ListView) findViewById(R.id.lstValesPapel);
        imgEscanearVale = (ImageView) findViewById(R.id.imgEscanearVale);
        tvMontoACargar = (TextView) findViewById(R.id.tvMontoACargar);
        tvMontoACargarPendiente = (TextView) findViewById(R.id.tvMontoACargarPendiente);
        tvFolioMonto = (EditText) findViewById(R.id.tvFolioMonto);
        tvDenominacionMonto = (EditText) findViewById(R.id.tvDenominacionMonto);
        data = new SQLiteBD(getApplicationContext());
        ipEstacion = data.getIpEstacion();

        IDMontos = new ArrayList<String>();
        NombreTipoValePapel = new ArrayList<String>();
        MontoVale = new ArrayList<>();
    }

    private void ObtieneTiposVale(){
//        bar = new ProgressDialog(VentaProductos.this);
//        bar.setTitle("Cargando Productos");
//        bar.setMessage("Ejecutando... ");
//        bar.setIcon(R.drawable.gas);
//        bar.setCancelable(false);
//        bar.show();

        url = "http://" + ipEstacion + "/CorpogasService/api/TipoValePapeles";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray respuesta = new JSONArray(response);
                    int suma=0;
                    for (int i =0; i< respuesta.length(); i++){
                        suma = suma +1;
                    }
                    opcionesTipoVale = new String[suma];
                    for (int i =0; i< respuesta.length(); i++){
                        JSONObject tipoValePapel = respuesta.getJSONObject(i);
                        String IdentificadorVale = tipoValePapel.getString("Id");
                        String DescripcionVale = tipoValePapel.getString("Description");
                        String EliminadoVale = tipoValePapel.getString("IsDeleted");

                        if(EliminadoVale.equals("false")){
                            opcionesTipoVale[i] = DescripcionVale;
                        }
                    }
                    tvMontoACargar.setText(montoaCobrar);
                    tvMontoACargarPendiente.setText(montoaCobrar);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(ValesPapel.this, R.layout.support_simple_spinner_dropdown_item, opcionesTipoVale);
                    spnValesPapel.setAdapter(adapter);
                    spnValesPapel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            ValeSeleccionado = adapterView.getItemAtPosition(i).toString();
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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


    }