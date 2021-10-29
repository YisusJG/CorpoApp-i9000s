package com.corpogas.corpoapp.Puntada;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.corpogas.corpoapp.Configuracion.SQLiteBD;
import com.corpogas.corpoapp.LecturaTarjetas.MonederosElectronicos;
import com.corpogas.corpoapp.Modales.Modales;
import com.corpogas.corpoapp.Productos.VentasProductos;
import com.corpogas.corpoapp.R;
import com.corpogas.corpoapp.VentaCombustible.EligePrecioLitros;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class PuntadaRedimirQr extends AppCompatActivity {
    SQLiteBD db;
    ImageButton btnScanPQR;
    Button btnAceptarrQrPuntada;
    TextView tvDescuento, tvNoTarjetaQr;
    String NIP, lugarProviene;
    String TipoSeleccionado, usuario, posicionCarga, usuarioid, estacionJarreo, claveProducto, precio;
    Long numeroInternoPosicionCarga;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puntada_redimir_qr);
        db = new SQLiteBD(this);
        this.setTitle(db.getNombreEstacion() + " ( EST.:" + db.getNumeroEstacion() + ")");

        NIP = getIntent().getStringExtra("nip");
        lugarProviene = getIntent().getStringExtra("lugarProviene");

        posicionCarga = getIntent().getStringExtra("posicionCarga");
        numeroInternoPosicionCarga = getIntent().getLongExtra("pocioncarganumerointerno", 0);
        usuarioid = db.getNumeroEmpleado();
        estacionJarreo = getIntent().getStringExtra("estacionjarreo");
        claveProducto = getIntent().getStringExtra("claveProducto");
        precio = getIntent().getStringExtra("precioProducto");

        btnScanPQR = (ImageButton) findViewById(R.id.btnScanPQR);
        tvDescuento = (TextView) findViewById(R.id.tvDescuento);
        tvNoTarjetaQr = (TextView) findViewById(R.id.tvNoTarjetaQr);
        btnAceptarrQrPuntada = (Button) findViewById(R.id.btnAceptarrQrPuntada);
        btnAceptarrQrPuntada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (lugarProviene.equals("Redimir")){
                    if (!tvDescuento.getText().toString().equals("") || !tvNoTarjetaQr.getText().toString().equals("")) {
                        Intent intent = new Intent(getApplicationContext(), PosicionPuntadaRedimir.class);  //ENVIABA A SeccionTarjeta cambio a PosicionPuntadaRedimir
                        intent.putExtra("track", tvNoTarjetaQr.getText().toString());
                        intent.putExtra("nip", NIP);
                        intent.putExtra("lugarproviene", "RedimirQR");
                        intent.putExtra("descuento", tvDescuento.getText().toString());
                        startActivity(intent);
                        finish();
                    } else {
                        String titulo = "PUNTADA";
                        String mensaje = "Tienes que escanear el QR de tu tarjeta Puntada" ;
                        Modales modales = new Modales(PuntadaRedimirQr.this);
                        View viewLectura = modales.MostrarDialogoAlertaAceptar(PuntadaRedimirQr.this, mensaje, titulo);
                        viewLectura.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                modales.alertDialog.dismiss();
                            }
                        });
                    }
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
                                intent.putExtra("pocioncarganumerointerno", numeroInternoPosicionCarga);
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

        btnScanPQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator integrator = new IntentIntegrator(PuntadaRedimirQr.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                integrator.setPrompt("Lector - CDP");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(true);
                integrator.setBarcodeImageEnabled(true);
                integrator.initiateScan();
            }
        });

    }

    protected void onActivityResult (int requestCode, int resulCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resulCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(getApplicationContext(), "Lectura Cancelada", Toast.LENGTH_SHORT).show();
            } else {
//                Toast.makeText(getApplicationContext(), result.getContents(), Toast.LENGTH_SHORT).show();
                int posicionPunto = result.getContents().indexOf(",");
                String hastaComa = result.getContents().substring(0, posicionPunto);
                int finalChar = result.getContents().length();
                String desdeComa = result.getContents().substring(posicionPunto + 1, finalChar);
                tvNoTarjetaQr.setText(hastaComa);
                tvDescuento.setText(desdeComa);
            }
        } else {
            super.onActivityResult(requestCode, resulCode, data);
        }
    }

}