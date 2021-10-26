package com.corpogas.corpoapp.Fajillas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.corpogas.corpoapp.Conexion;
import com.corpogas.corpoapp.Configuracion.SQLiteBD;
import com.corpogas.corpoapp.Entities.Accesos.AccesoUsuario;
import com.corpogas.corpoapp.Entities.Classes.RespuestaApi;
import com.corpogas.corpoapp.Menu_Principal;
import com.corpogas.corpoapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class EntregaFajillas extends AppCompatActivity {
    long numeroIslas, valorTipoFajilla=1 ;
    Spinner spnFajillas;
    EditText cantidad;
    Button aceptar, btnIncrementar, btnDecrementar;
//    String [] opciones = {"Fajillas Billete", "Fajillas Morralla"}; //, "Fajillas  Billetes Pico", "Fajillas Morralla Pico"
    String lugarProviene, m_deviceName, ipEstacion, claveUsuario, nombreCompleto,  password, islaId, descripciones ;

//    String[] opciones2 = new String[2];

    SQLiteBD db;
    Long sucursalId, idUsuario, totalFajillas ;

//    RespuestaApi<AccesoUsuario> respuestaApiAccesoUsuario;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrega_fajillas);
        SQLiteBD data = new SQLiteBD(getApplicationContext());
        this.setTitle(data.getNombreEstacion() + " ( EST.:" + data.getNumeroEstacion() + ")");
        sucursalId = Long.parseLong(data.getIdSucursal());
        ipEstacion = data.getIpEstacion();

        init();
        setVariables();
    }

    private void init(){
        db = new SQLiteBD(getApplicationContext());
//        m_deviceName = getIntent().getStringExtra("device_name");
//        islaId = getIntent().getStringExtra("IslaId");
        lugarProviene = getIntent().getStringExtra("lugarProviene");


        if (lugarProviene.equals("corteFajillas")){
            idUsuario = Long.valueOf(db.getUsuarioId());
        }else{
//            respuestaApiAccesoUsuario = (RespuestaApi<AccesoUsuario>) getIntent().getSerializableExtra("accesoUsuario");
            nombreCompleto = db.getNombreCompleto();//respuestaApiAccesoUsuario.getObjetoRespuesta().getNombreCompleto();
            idUsuario = Long.parseLong(db.getUsuarioId());//respuestaApiAccesoUsuario.getObjetoRespuesta().getNumeroEmpleado();
            password = db.getClave();//respuestaApiAccesoUsuario.getObjetoRespuesta().getClave();
        }

    }

    private void setVariables(){

        spnFajillas = (Spinner) findViewById(R.id.spFajillas);
        cantidad =  findViewById(R.id.etCantidad);
        aceptar = (Button) findViewById(R.id.btnAceptarFajilla);

        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cantidad.length() != 0){
                    if (valorTipoFajilla !=0){
                        totalFajillas = Long.parseLong (String.valueOf(cantidad.getText()));
                        if (totalFajillas >4){
                            Toast.makeText(EntregaFajillas.this, "La Cantidad maxima de fajillas es 4", Toast.LENGTH_SHORT).show();
                            cantidad.setText("");
                        }else{
                            if (totalFajillas == 0){
                                Toast.makeText(EntregaFajillas.this, "La Cantidad cargada no puede ser CERO", Toast.LENGTH_SHORT).show();
                                cantidad.setText("");
                            }else{
                                //ENVIA AUTORIZACION
                                Intent intent1 = new Intent(EntregaFajillas.this, AutorizaFajillas.class); //despachdorclave
                                intent1.putExtra("LugarProviene", lugarProviene);
                                intent1.putExtra("TotalFajillas", totalFajillas);
                                intent1.putExtra("TipoFajilla", valorTipoFajilla);
                                startActivity(intent1);
                                finish();
                            }
                        }
                    }else{
                        Toast.makeText(EntregaFajillas.this, "Seleccione uno de los Tipos de Fajilla", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(EntregaFajillas.this, "Teclee una Cantidad", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnIncrementar = (Button) findViewById(R.id.btnAceptarFajilla);
        btnDecrementar = (Button) findViewById(R.id.btnAceptarFajilla);

        ObtieneFajillas();

    }

    private void ObtieneFajillas(){
        ArrayList<String> comboTipoFajilla = new ArrayList<String>();

        if (!Conexion.compruebaConexion(this)) {
            Toast.makeText(getBaseContext(), "Sin conexión a la red ", Toast.LENGTH_SHORT).show();
            Intent intent1 = new Intent(getApplicationContext(), Menu_Principal.class);

            startActivity(intent1);
            finish();
        } else {
            String url = "http://" + ipEstacion + "/CorpogasService/api/PrecioFajillas/sucursal/" + sucursalId;
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject respuesta = new JSONObject(response);
                        String correcto = respuesta.getString("Correcto");
                        String mensaje = respuesta.getString("Mensaje");

                        if (correcto.equals("true") ) {
                            String objetoRespuesta = respuesta.getString("ObjetoRespuesta");
                            JSONArray tiposFajilla = new JSONArray(objetoRespuesta);


                            for (int i = 0; i <tiposFajilla.length() ; i++){
                                JSONObject respuestaTipoFajilla = tiposFajilla.getJSONObject(i);
                                String describeTipoFajilla = respuestaTipoFajilla.getString("TipoFajilla");
                                String idFajilla = respuestaTipoFajilla.getString("Price");
//                                opciones[i] = describeTipoFajilla + " - $" + idFajilla;
                                comboTipoFajilla.add(describeTipoFajilla + " - $" + idFajilla);
                            }
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(EntregaFajillas.this, R.layout.support_simple_spinner_dropdown_item, comboTipoFajilla);
                            spnFajillas.setAdapter(adapter);
                            spnFajillas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    String fajillaSeleccionada = adapterView.getItemAtPosition(i).toString();
                                    fajillaSeleccionada = fajillaSeleccionada.substring(0, fajillaSeleccionada.indexOf(" -"));
                                    switch (fajillaSeleccionada){
                                        case "BILLETE":
                                            valorTipoFajilla =1;
                                            break;
                                        case "MORRALLA":
                                            valorTipoFajilla =2;
                                            break;
                                        default:
                                            valorTipoFajilla =1;
                                            break;
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {

                                }
                            });

                        }else{
                            Toast.makeText(EntregaFajillas.this, "Sin Tipos de Fahilla", Toast.LENGTH_SHORT).show();
                            Intent intent1 = new Intent(getApplicationContext(), Menu_Principal.class);
                            //Se envian los parametros de posicion y usuario
                            intent1.putExtra("device_name", m_deviceName);
                            //inicia el activity
                            startActivity(intent1);
                            finish();

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                }
            });
            RequestQueue requestQueue = Volley.newRequestQueue(this.getApplicationContext());
            requestQueue.add(stringRequest);
        }
    }


    //Metodo para regresar a la actividad principal
    @Override
    public void onBackPressed() {
        if (lugarProviene.equals("corteFajillas")){
            finish();
        }else{
            //Se instancia y se llama a la clase Venta de Productos
            Intent intent1 = new Intent(getApplicationContext(), Menu_Principal.class);
            //Se envian los parametros de posicion y usuario
            intent1.putExtra("device_name", m_deviceName);
            //inicia el activity
            startActivity(intent1);
            finish();
        }
    }


}