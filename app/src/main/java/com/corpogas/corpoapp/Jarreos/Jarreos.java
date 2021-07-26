package com.corpogas.corpoapp.Jarreos;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.corpogas.corpoapp.Configuracion.SQLiteBD;
import com.corpogas.corpoapp.Menu_Principal;
import com.corpogas.corpoapp.Modales.Modales;
import com.corpogas.corpoapp.R;

import org.json.JSONException;
import org.json.JSONObject;

public class Jarreos extends AppCompatActivity {
    String sucursalId, EstacionId, ipEstacion, numeroEmpleado;
    Switch swjarreo;
    TextView tvEstado;
    ImageView btnJarreo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jarreos);
        SQLiteBD db = new SQLiteBD(getApplicationContext());
        this.setTitle(db.getNombreEstacion() + " ( EST.:" + db.getNumeroEstacion() + ")");
        sucursalId=db.getIdSucursal();
        EstacionId = db.getIdEstacion();
        ipEstacion = db.getIpEstacion();
        swjarreo = findViewById(R.id.swjarreo);
        tvEstado = findViewById(R.id.tvEstado);
        numeroEmpleado = getIntent().getStringExtra("numeroempleado");
        btnJarreo = findViewById(R.id.btnJarreo);
        ObtieneEstatusJarreo();

        btnJarreo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tvEstado.getText().equals("JARREO ACTIVADO")){
                    tvEstado.setText("JARREO DESACTIVADO");
                    ActivarDesactivarJarreo();
                }else{
                    String titulo = "IMPRIMIR";
                    String mensajes = "Estas seguro de Activar el JARREO en toda la Estación?";
                    Modales modales = new Modales(Jarreos.this);
                    View viewLectura = modales.MostrarDialogoAlerta(Jarreos.this, mensajes,  "SI", "NO");
                    viewLectura.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            modales.alertDialog.dismiss();
                            tvEstado.setText("JARREO ACTIVADO");
                            ActivarDesactivarJarreo();
                        }
                    });
                    viewLectura.findViewById(R.id.buttonNo).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            modales.alertDialog.dismiss();
                            Intent intent = new Intent(getApplicationContext(), Menu_Principal.class);
                            startActivity(intent);
                            finish();
                        }
                    });

                }
            }
        });
    }

    private void ActivarDesactivarJarreo(){
        ProgressDialog bar = new ProgressDialog(Jarreos.this);
        bar.setTitle("JARREO");
        bar.setMessage("Esperando Respuesta");
        bar.setIcon(R.drawable.jarreo);
        bar.setCancelable(false);
        bar.show();

        String url = "http://" + ipEstacion + "/CorpogasService/api/jarreos/controlJarreo/sucursal/" + sucursalId + "/numeroEmpleado/" + numeroEmpleado;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String estado = jsonObject.getString("Estado");

                    String mensaje = jsonObject.getString("Mensaje");
                    String saldo = jsonObject.getString("Saldo");
                    String conectado = jsonObject.getString("Conectado");

                    String titulo = "AVISO";
                    Modales modales = new Modales(Jarreos.this);
                    View view1 = modales.MostrarDialogoAlertaAceptar(Jarreos.this, mensaje, titulo);
                    view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            modales.alertDialog.dismiss();
                            Intent intent = new Intent(getApplicationContext(), Menu_Principal.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String algo = new String(error.networkResponse.data);
                try {
                    //creamos un json Object del String algo
                    JSONObject errorCaptado = new JSONObject(algo);
                    //Obtenemos el elemento ExceptionMesage del errro enviado
                    String errorMensaje = errorCaptado.getString("ExceptionMessage");
                    try {
                        String titulo = "Jarreo";
                        String mensajes = errorMensaje;
                        Modales modales = new Modales(Jarreos.this);
                        View view1 = modales.MostrarDialogoAlertaAceptar(Jarreos.this, mensajes, titulo);
                        view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                modales.alertDialog.dismiss();
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);

    }

    private void ObtieneEstatusJarreo(){

        String url = "http://" + ipEstacion + "/CorpogasService/api/jarreos/estado/sucursal/" + sucursalId + "/numeroEmpleado/" + numeroEmpleado;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String estado = jsonObject.getString("Estado");

                    String mensaje = jsonObject.getString("Mensaje");
                    String saldo = jsonObject.getString("Saldo");
                    String conectado = jsonObject.getString("Conectado");
                    swjarreo.setChecked(Boolean.parseBoolean(estado));
                    if (estado.equals("true")){
                        tvEstado.setText("JARREO ACTIVADO");
                    }else{
                        tvEstado.setText("JARREO DESACTIVADO");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String algo = new String(error.networkResponse.data);
                try {
                    //creamos un json Object del String algo
                    JSONObject errorCaptado = new JSONObject(algo);
                    //Obtenemos el elemento ExceptionMesage del errro enviado
                    String errorMensaje = errorCaptado.getString("ExceptionMessage");
                    try {
                        String titulo = "Jarreo";
                        String mensajes = errorMensaje;
                        Modales modales = new Modales(Jarreos.this);
                        View view1 = modales.MostrarDialogoAlertaAceptar(Jarreos.this, mensajes, titulo);
                        view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                modales.alertDialog.dismiss();
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);


    }
}