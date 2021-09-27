package com.corpogas.corpoapp.VentaCombustible;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.corpogas.corpoapp.Configuracion.SQLiteBD;
import com.corpogas.corpoapp.Menu_Principal;
import com.corpogas.corpoapp.Modales.Modales;
import com.corpogas.corpoapp.MyListAdapter;
import com.corpogas.corpoapp.R;


public class eligeLitrosPrecio extends AppCompatActivity {
    ListView list;
    String[] maintitle = {
            "Litros", "Pesos",
    };

    String[] subtitle = {
            "Cantidad en Litros", "Cantidad en Pesos",
    };

    Integer[] imgid = {
            R.drawable.gas, R.drawable.ventas,
    };
    EditText Cantidad;
    TextView EtiquetaCantidad;
    String TipoSeleccionado, usuario, posicion, usuarioid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elige_litros_precio);

        SQLiteBD db = new SQLiteBD(getApplicationContext());
        this.setTitle(db.getNombreEstacion());

        Cantidad = findViewById(R.id.cantidad);
        EtiquetaCantidad = findViewById(R.id.lblcantidad);

        cargaOpciones();
        //TipoSeleccionado = "L";
    }

    private void cargaOpciones(){

        MyListAdapter adapter=new MyListAdapter(eligeLitrosPrecio.this, maintitle, subtitle,imgid);
        list= findViewById(R.id.list);
        list.setAdapter(adapter);


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                // TODO Auto-generated method stub
                Cantidad.setVisibility(View.VISIBLE);
                Cantidad.setText("");
                EtiquetaCantidad.setVisibility(View.VISIBLE);
                if (position == 0) {
                    EtiquetaCantidad.setText("Litros");
                    TipoSeleccionado = "L";
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
        posicion = getIntent().getStringExtra("carga");
        usuarioid = getIntent().getStringExtra("usuarioid");
        String cantidad;

        cantidad = Cantidad.getText().toString();
        if (cantidad.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Ingresa la Cantidad", Toast.LENGTH_SHORT).show();
        } else {
            if (cantidad.equals("0")){
                Toast.makeText(getApplicationContext(), "Ingresa una Cantidad mayor a 0", Toast.LENGTH_SHORT).show();
            }else{
                String mensaje;
                String titulo = "No podrás cambiar los datos posteriormente";
                if (TipoSeleccionado == "L") {
                    mensaje = "Estás seguro de que deseas cargar : " + cantidad + " LITROS";
                }else{
                    mensaje = "Estás seguro de que deseas cargar : " + cantidad + " PESOS";
                }
                Modales modales = new Modales(eligeLitrosPrecio.this);
                View viewLectura = modales.MostrarDialogoAlerta(eligeLitrosPrecio.this, mensaje,  "Ok", "Cancelar");
                viewLectura.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        modales.alertDialog.dismiss();
                        //Intent intent = new Intent(getApplicationContext(), eligeCombustible.class);
                        //intent.putExtra("posicion", posicion);
                        //intent.putExtra("usuario", usuarioid);
                        //intent.putExtra("Cantidad", cantidad);
                        //intent.putExtra("TipoSeleccionado", TipoSeleccionado);
                        //startActivity(intent);
                        //finish();
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
