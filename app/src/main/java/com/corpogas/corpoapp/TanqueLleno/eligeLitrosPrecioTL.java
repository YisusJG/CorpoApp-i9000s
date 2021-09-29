package com.corpogas.corpoapp.TanqueLleno;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.corpogas.corpoapp.Configuracion.SQLiteBD;
import com.corpogas.corpoapp.Menu_Principal;
import com.corpogas.corpoapp.Modales.Modales;
import com.corpogas.corpoapp.R;

public class eligeLitrosPrecioTL extends AppCompatActivity {
    ListView list;

    String[] maintitlePesos = {
            "Pesos",
    };

    String[] subtitlePesos = {
            "Cantidad en Pesos",
    };

    Integer[] imgidPesos = {
            R.drawable.moneda,
    };

    String[] maintitleLitros = {
            "Litros",
    };

    String[] subtitleLitros = {
            "Cantidad en Litros",
    };

    Integer[] imgidLitros = {
            R.drawable.gas,
    };
    SQLiteBD db;


    EditText Cantidad;
    TextView EtiquetaCantidad;
    String TipoSeleccionado, usuario, posicion, usuarioid;
    String placas ,odometro, NumeroInternoEstacion, SucursalEmpleadoId, NumeroDeTarjeta, ClaveTanqueLleno,  transaccionid, folio, nipCliente, nipMd5Cliente;
    long PosicioDeCarga;
    String banderaLitros, combustibles;
    Integer Tipocliente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elige_litros_precio_t_l);
        db = new SQLiteBD(getApplicationContext());
        this.setTitle(db.getNombreEstacion() + " ( EST.:" + db.getNumeroEstacion() + ")");

        init();
        cargaOpciones();
    }

    private void init(){
        Cantidad = findViewById(R.id.cantidad);
        EtiquetaCantidad = findViewById(R.id.lblcantidad);

        placas = getIntent().getStringExtra("placas");
        odometro = getIntent().getStringExtra("odometro");
        NumeroInternoEstacion = getIntent().getStringExtra("NumeroInternoEstacion");
        SucursalEmpleadoId = getIntent().getStringExtra("SucursalEmpleadoId");
        PosicioDeCarga = getIntent().getLongExtra("PosicioDeCarga",0);
        NumeroDeTarjeta = getIntent().getStringExtra("NumeroDeTarjeta");
        ClaveTanqueLleno = getIntent().getStringExtra("ClaveTanqueLleno");
        Tipocliente = getIntent().getIntExtra("Tipocliente", 0);
        transaccionid = getIntent().getStringExtra("transaccionid");
        folio = getIntent().getStringExtra("folio");
        nipCliente = getIntent().getStringExtra("nipCliente");
        nipMd5Cliente = getIntent().getStringExtra("nipMd5Cliente");
        banderaLitros = getIntent().getStringExtra("Litros");
        combustibles = getIntent().getStringExtra("CombustiblesAsociados");
    }

    private void cargaOpciones(){

        EtiquetaCantidad.setVisibility(View.VISIBLE);
        Cantidad.setVisibility(View.VISIBLE);
        if (banderaLitros == "LITROS") {
            EtiquetaCantidad.setText("Litros");
            TipoSeleccionado = "L";

            MyListAdapter adapter=new MyListAdapter(eligeLitrosPrecioTL.this, maintitleLitros, subtitleLitros,imgidLitros);
            list= findViewById(R.id.lstPosicionCarga);
            list.setAdapter(adapter);

        }else{
            EtiquetaCantidad.setText("Pesos");
            TipoSeleccionado = "P";

            MyListAdapter adapter=new MyListAdapter(eligeLitrosPrecioTL.this, maintitlePesos, subtitlePesos,imgidPesos);
            list= findViewById(R.id.lstPosicionCarga);
            list.setAdapter(adapter);

        }

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                Cantidad.setVisibility(View.VISIBLE);
                Cantidad.setText("");
                EtiquetaCantidad.setVisibility(View.VISIBLE);
                if (position == 0) {
                    if (banderaLitros == "LITROS") {
                        EtiquetaCantidad.setText("Litros");
                        TipoSeleccionado = "L";
                    }else{
                        EtiquetaCantidad.setText("Pesos");
                        TipoSeleccionado = "P";
                    }
                } else if (position == 1) {
                    EtiquetaCantidad.setText("Pesos");
                    TipoSeleccionado = "P";
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

    //procedimiento para  cachar el Enter del teclado
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_ENTER:
                calculos();
                return true;
            default:
                return super.onKeyUp(keyCode, event);
        }
    }

    private void calculos() {
        //Se lee el password del objeto y se asigna a variable
        usuario = getIntent().getStringExtra("clave");
        posicion = getIntent().getStringExtra("PosicioDeCarga");
        usuarioid = getIntent().getStringExtra("usuarioid");
        String cantidadlitrospesos;

        cantidadlitrospesos = Cantidad.getText().toString();
        if (cantidadlitrospesos.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Ingresa la Cantidad", Toast.LENGTH_SHORT).show();
        } else {
            if (cantidadlitrospesos.equals("0")){
                Toast.makeText(getApplicationContext(), "Ingresa una Cantidad mayor a 0", Toast.LENGTH_SHORT).show();
            }else{

                String mensaje;
                String titulo = "No podrás cambiar los datos posteriormente";
                if (TipoSeleccionado == "L") {
                    mensaje = "Estás seguro de que deseas cargar : " + Cantidad.getText().toString() + " litros";
                }else{
                    mensaje = "Estás seguro de que deseas cargar : " + Cantidad.getText().toString() + " pesos";
                }
                Modales modales = new Modales(eligeLitrosPrecioTL.this);
                View viewLectura = modales.MostrarDialogoAlerta(eligeLitrosPrecioTL.this, mensaje,  "Ok", "Cancelar");
                viewLectura.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        modales.alertDialog.dismiss();
                        Intent intent = new Intent(getApplicationContext(), eligeCombustibleTL.class);
                        intent.putExtra("respuesta", PosicioDeCarga);
                        intent.putExtra("usuario", SucursalEmpleadoId);
                        intent.putExtra("Cantidad", Cantidad.getText().toString());
                        intent.putExtra("TipoSeleccionado", TipoSeleccionado);

                        intent.putExtra("placas",placas);
                        intent.putExtra("odometro",odometro);
                        intent.putExtra("NumeroInternoEstacion", NumeroInternoEstacion);
                        intent.putExtra("SucursalEmpleadoId", SucursalEmpleadoId);
                        intent.putExtra("cargaPosicion", PosicioDeCarga);
                        intent.putExtra("NumeroDeTarjeta", NumeroDeTarjeta);
                        intent.putExtra("ClaveTanqueLleno", ClaveTanqueLleno);
                        intent.putExtra("Tipocliente", Tipocliente);
                        intent.putExtra("transaccionid", transaccionid);
                        intent.putExtra("folio", folio);
                        intent.putExtra("nipCliente", nipCliente);
                        intent.putExtra("nipMd5Cliente", nipMd5Cliente);
                        intent.putExtra("CombustiblesAsociados", combustibles);
                        startActivity(intent);
                        finish();
                    }
                });

                viewLectura.findViewById(R.id.buttonNo).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        modales.alertDialog.dismiss();
                    }
                });
            }
        }
    }

}