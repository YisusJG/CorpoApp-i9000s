package com.corpogas.corpoapp.Puntada;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.corpogas.corpoapp.Configuracion.SQLiteBD;
import com.corpogas.corpoapp.LecturaTarjetas.MonederosElectronicos;
import com.corpogas.corpoapp.Modales.Modales;
import com.corpogas.corpoapp.R;
import com.corpogas.corpoapp.VentaCombustible.EligePrecioLitros;

public class PuntadaRedimirQr extends AppCompatActivity {
    SQLiteBD db;
    Button btnAceptarrQrPuntada;
    EditText tvDescuento, tvNoTarjetaQr;
    String NIP, lugarProviene;
    String TipoSeleccionado, usuario, posicionCarga, usuarioid, estacionJarreo, claveProducto, precio;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puntada_redimir_qr);
        db = new SQLiteBD(this);
        this.setTitle(db.getNombreEstacion() + " ( EST.:" + db.getNumeroEstacion() + ")");

        NIP = getIntent().getStringExtra("nip");
        lugarProviene = getIntent().getStringExtra("lugarProviene");


        posicionCarga = getIntent().getStringExtra("posicionCarga");
        usuarioid = db.getNumeroEmpleado();
        estacionJarreo = getIntent().getStringExtra("estacionjarreo");
        claveProducto = getIntent().getStringExtra("claveProducto");
        precio = getIntent().getStringExtra("precioProducto");

        tvDescuento = (EditText) findViewById(R.id.tvDescuento);
        tvNoTarjetaQr = (EditText) findViewById(R.id.tvNoTarjetaQr);
        btnAceptarrQrPuntada = (Button) findViewById(R.id.btnAceptarrQrPuntada);
        btnAceptarrQrPuntada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (lugarProviene.equals("Redimir")){
                    Intent intent = new Intent(getApplicationContext(), PosicionPuntadaRedimir.class); //ENVIABA A SeccionTarjeta cambio a PosicionPuntadaRedimir
                    intent.putExtra("track",tvNoTarjetaQr.getText().toString());
                    intent.putExtra("nip", NIP);
                    intent.putExtra("lugarproviene", "RedimirQR");
                    intent.putExtra("descuento", tvDescuento.getText().toString());
                    startActivity(intent);
                    finish();
                }else{
                    String titulo = "PUNTADA";
                    String mensaje = "Ingresa el NIP de la tarjeta Puntada" ;
                    Modales modales = new Modales(PuntadaRedimirQr.this);
                    View viewLectura = modales.MostrarDialogoInsertaDato(PuntadaRedimirQr.this, mensaje, titulo);
                    EditText edtProductoCantidad = ((EditText) viewLectura.findViewById(R.id.textInsertarDato));
                    edtProductoCantidad.setInputType(InputType.TYPE_CLASS_NUMBER);
                    viewLectura.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String cantidad = edtProductoCantidad.getText().toString();
                            if (cantidad.isEmpty()){
                                edtProductoCantidad.setError("Ingresa el NIP de la tarjeta Puntada");
                            }else {
                                String NIPCliente = cantidad;
                                Intent intent = new Intent(getApplicationContext(), EligePrecioLitros.class);
                                intent.putExtra("combustible", claveProducto);
                                intent.putExtra("posicionCarga", posicionCarga);
                                intent.putExtra("estacionjarreo", estacionJarreo);
                                intent.putExtra("claveProducto", claveProducto);
                                intent.putExtra("precioProducto", precio);
                                intent.putExtra("despacholibre", "no");
                                intent.putExtra("nip", NIPCliente);
                                intent.putExtra("numeroTarjeta", tvNoTarjetaQr.getText().toString());
                                intent.putExtra("descuento", tvDescuento.getText().toString());
                                intent.putExtra("lugarProviene", "puntadaAcumularQr");

                                startActivity(intent);
                                finish();

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
            }
        });


    }



}