package com.corpogas.corpoapp.ValesPapel;

import androidx.appcompat.app.AppCompatActivity;

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

import com.corpogas.corpoapp.Productos.ListAdapterProductos;
import com.corpogas.corpoapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ValesPapel extends AppCompatActivity {
    Spinner spnValesPapel;
    Button btnAgregarVale, btnAceptarValesPapel, btnLimpiarPantalla;
    ListView lstValesPapel;
    ImageView imgEscanearVale;
    String[] opcionesTipoVale = new String[2];
    String posicionCarga, usuarioid, estacionJarreo, claveProducto, montoaCobrar, ValeSeleccionado;
    EditText  tvFolioMonto, tvDenominacionMonto;
    TextView tvMontoACargar, tvMontoACargarPendiente;
    List<String> ID;
    List<String> NombreTipoValePapel;
    List<String> MontoVale;
    List<String> FolioValePapel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vales_papel);

        posicionCarga = getIntent().getStringExtra("posicionCarga");
        usuarioid = getIntent().getStringExtra("numeroEmpleado");
        estacionJarreo = getIntent().getStringExtra("estacionjarreo");
        claveProducto = getIntent().getStringExtra("claveProducto");
        montoaCobrar = getIntent().getStringExtra("montoenCanasta");

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
                            //ID.add(DescLarga);
                            MontoVale.add(tvFolioMonto.getText().toString());
                            ListAdapterProductos adapterP = new ListAdapterProductos(this, NombreTipoValePapel, MontoVale );
                            lstValesPapel.setTextFilterEnabled(true);
                            lstValesPapel.setAdapter(adapterP);
                            lstValesPapel.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                                @Override
                                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
//                                    montoPendiente = Double.parseDouble(tvMontoACargarPendiente.getText().toString());
                                    NombreTipoValePapel.remove(i);
//                                    MontoVale.remove(i);

                                    adapterP.notifyDataSetChanged();
                                    return false;
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

        ID = new ArrayList<String>();
        NombreTipoValePapel = new ArrayList<String>();
        MontoVale = new ArrayList<>();
    }

    private void ObtieneTiposVale(){

        tvMontoACargar.setText(montoaCobrar);
        tvMontoACargarPendiente.setText(montoaCobrar);
        opcionesTipoVale[0] = "Efectivale"; //+ " - $" + idFajilla;
        opcionesTipoVale[1] = "Si Vale";

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


    }


    }