package com.corpogas.corpoapp.Gastos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
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
import com.corpogas.corpoapp.Conexion;
import com.corpogas.corpoapp.Configuracion.SQLiteBD;
import com.corpogas.corpoapp.Menu_Principal;
import com.corpogas.corpoapp.Modales.Modales;
import com.corpogas.corpoapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AutorizacionGastos extends AppCompatActivity {
    ImageView Autoriza;
    ListView list;
    TextView txtusuario, txtclaveusuario;
    String EstacionId, sucursalId, ipEstacion, islaId ;
    String proviene, turnoId;
    SQLiteBD db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autorizacion_gastos);
            db = new SQLiteBD(getApplicationContext());
            this.setTitle(db.getNombreEstacion() + " ( EST.:" + db.getNumeroEstacion() + ")");
            EstacionId = db.getIdEstacion();
            sucursalId=db.getIdSucursal();
            ipEstacion = db.getIpEstacion();

            txtusuario = findViewById(R.id.txtempleado);
            txtclaveusuario= findViewById(R.id.txtclaveempleado);

            proviene = getIntent().getStringExtra("tipoGasto");
            islaId = getIntent().getStringExtra("isla");
            turnoId = getIntent().getStringExtra("turno");


            CargaEmpleados();
        }

        private void CargaEmpleados() {
            if (!Conexion.compruebaConexion(this)) {
                Toast.makeText(getBaseContext(), "Sin conexión a la red ", Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent(getApplicationContext(), Menu_Principal.class);
                startActivity(intent1);
                finish();
            } else {
                String url = "http://" + ipEstacion + "/CorpogasService/api/Empleados/sucursal/" + sucursalId;
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mostrarEmpleados(response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                    }
                });
                RequestQueue requestQueue = Volley.newRequestQueue(this.getApplicationContext());
                stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                requestQueue.add(stringRequest);
            }
        }


        private void mostrarEmpleados(String response) {
            //Declaracion de variables
            String IdentificadorEmpleado;
            final List<String> ID;
            ID = new ArrayList<String>();

            final List<String> NombreUsuario;
            NombreUsuario = new ArrayList<String>();
            try {
                JSONArray productos = new JSONArray(response);
                for (int i = 0; i <productos.length() ; i++) {

                    JSONObject p1 = productos.getJSONObject(i);
                    IdentificadorEmpleado = p1.getString("RolId");
                    if (IdentificadorEmpleado.equals("1") || IdentificadorEmpleado.equals("3") ) {
                        String idEmpleado = p1.getString("Id"); //Id Mikel cambie el EmpleadoNumero por el numero compuesto que viene desde la central
                        String DesLarga = p1.getString("Nombre") + " " + p1.getString("ApellidoPaterno") + " " + p1.getString("ApellidoMaterno");
                        NombreUsuario.add("" + idEmpleado);
                        ID.add(DesLarga);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            final ListAdapterProductos adapterP = new ListAdapterProductos(this,  ID, NombreUsuario);
            list=(ListView)findViewById(R.id.list);
            list.setTextFilterEnabled(true);
            list.setAdapter(adapterP);
//        Agregado  click en la lista
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    String  Descripcion = ID.get(i).toString();
                    String  clave = NombreUsuario.get(i).toString();
                    txtusuario.setText(Descripcion);
                    txtclaveusuario.setText(clave);

                    //Cargamos el usuario
                    if (txtusuario.length() >0) {
                        if (proviene.equals("1")) {
                            Intent intent = new Intent(getApplicationContext(), CargaGasto.class);
                            intent.putExtra("isla", islaId);
                            intent.putExtra("turno", turnoId);
                            intent.putExtra("empleadoid", txtclaveusuario.getText().toString());
                            intent.putExtra("empleado", txtusuario.getText().toString());
                            startActivity(intent);
                            finish();
                        } else {
                            Intent intent = new Intent(getApplicationContext(), cargaValeGasto.class);
                            intent.putExtra("isla", islaId);
                            intent.putExtra("turno", turnoId);
                            intent.putExtra("empleadoid", txtclaveusuario.getText().toString());
                            intent.putExtra("empleado", txtusuario.getText().toString());
                            startActivity(intent);
                            finish();
                        }
                    }else{
                        String titulo = "AVISO";
                        String mensaje = "Seleccione un empleado de la lista";
                        Modales modales = new Modales(AutorizacionGastos.this);
                        View view1 = modales.MostrarDialogoAlertaAceptar(AutorizacionGastos.this,mensaje,titulo);
                        view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                modales.alertDialog.dismiss();
                                Intent intent1 = new Intent(getApplicationContext(), Menu_Principal.class);
                                startActivity(intent1);
                                finish();
                            }
                        });

                    }

                }
            });
        }
        //Metodo para regresar a la actividad principal
        @Override
        public void onBackPressed() {
            Intent intent = new Intent(getApplicationContext(), Menu_Principal.class);
            startActivity(intent);
            finish();
        }


}