package com.corpogas.corpoapp.Puntada;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.device.scanner.configuration.Triggering;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.corpogas.corpoapp.Configuracion.SQLiteBD;
import com.corpogas.corpoapp.LecturaTarjetas.MonederosElectronicos;
import com.corpogas.corpoapp.Menu_Principal;
import com.corpogas.corpoapp.Modales.Modales;
import com.corpogas.corpoapp.Productos.VentasProductos;
import com.corpogas.corpoapp.R;
import com.corpogas.corpoapp.ScanManagerProvides;
import com.corpogas.corpoapp.VentaCombustible.EligePrecioLitros;
import com.corpogas.corpoapp.VentaCombustible.IniciaVentas;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class PuntadaRedimirQr extends AppCompatActivity {
    String model;

    ScanManagerProvides scanManagerProvides;
    String result;

    SQLiteBD db;
    ImageButton btnScanPQR;
    Button btnAceptarrQrPuntada;
    TextView tvDescuento, tvNoTarjetaQr;
    String NIP, lugarProviene;
    String TipoSeleccionado, usuario, posicionCarga, usuarioid, estacionJarreo, claveProducto, precio;
    Long numeroInternoPosicionCarga;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puntada_redimir_qr);
        model = Build.MODEL;
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
                btnAceptarrQrPuntada.setEnabled(false);
//                if (lugarProviene.equals("Redimir")){
//                }else{
//                    db.InsertarDatosTarjetaPuntadaYena(Integer.parseInt(posicionCarga), numeroInternoPosicionCarga.toString(), tvNoTarjetaQr.getText().toString(), NIP, Double.parseDouble(tvDescuento.getText().toString()));
//                }
                if (!tvDescuento.getText().toString().equals("") || !tvNoTarjetaQr.getText().toString().equals("")) {
                    Intent intent = new Intent(getApplicationContext(), PosicionPuntadaRedimir.class);  //ENVIABA A SeccionTarjeta cambio a PosicionPuntadaRedimir
                    intent.putExtra("track", tvNoTarjetaQr.getText().toString());
                    intent.putExtra("nip", NIP);
                    intent.putExtra("lugarproviene", lugarProviene); //"RedimirQR"
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
                            btnAceptarrQrPuntada.setEnabled(true);
                        }
                    });
                }
            }
        });

        if (model.equals("i9000S")) {
            scanManagerProvides = new ScanManagerProvides();
            result = "";
            btnScanPQR.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if (view.getId() == R.id.btnScanPQR) {
                        if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                            if (scanManagerProvides.getTriggerMode() == Triggering.HOST) {
                                scanManagerProvides.stopDecode();
                            }
                        }
                        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                            scanManagerProvides.startDecode();
                        }
                    }
                    return false;
                }
            });
        } else {
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
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() != KeyEvent.KEYCODE_ENTER) { //Not Adding ENTER_KEY to barcode String
            char pressedKey = (char) event.getUnicodeChar();
            result += pressedKey;
        }
        if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {  //Any method handling the data
            if (!result.contains(",")){
//                Toast.makeText(getApplicationContext(), "El código QR no contiene descuento asociado", Toast.LENGTH_SHORT).show();
                Modales modales = new Modales(PuntadaRedimirQr.this);
                View viewLectura = modales.MostrarDialogoError(PuntadaRedimirQr.this, "Tarjeta Incorrecta");
                viewLectura.findViewById(R.id.buttonAction).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        modales.alertDialog.dismiss();
                        startActivity(new Intent(PuntadaRedimirQr.this, Menu_Principal.class));
                    }
                });
            } else {
                int posicionPunto = result.indexOf(",");
                String hastaComa = result.substring(0, posicionPunto);
                int finalChar = result.length();
                String desdeComa = result.substring(posicionPunto + 1, finalChar);
                tvNoTarjetaQr.setText(hastaComa);
                tvDescuento.setText(desdeComa);
                result = "";
            }
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (model.equals("i9000S")) {
            scanManagerProvides.initScan(PuntadaRedimirQr.this);
        }
    }

        protected void onActivityResult (int requestCode, int resulCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resulCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(getApplicationContext(), "Lectura Cancelada", Toast.LENGTH_SHORT).show();
            } else {
                if (!result.getContents().contains(",")){
                    Toast.makeText(getApplicationContext(), "El código QR no contiene descuento asociado", Toast.LENGTH_SHORT).show();
                } else{
                    int posicionPunto = result.getContents().indexOf(",");
                    String hastaComa = result.getContents().substring(0, posicionPunto);
                    int finalChar = result.getContents().length();
                    String desdeComa = result.getContents().substring(posicionPunto + 1, finalChar);
                    tvNoTarjetaQr.setText(hastaComa);
                    tvDescuento.setText(desdeComa);
                }
            }
        } else {
            super.onActivityResult(requestCode, resulCode, data);
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(PuntadaRedimirQr.this, Menu_Principal.class));
    }
}