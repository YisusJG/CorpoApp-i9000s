package com.corpogas.corpoapp.ValesPapel;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.device.ScanManager;
import android.device.scanner.configuration.PropertyID;
import android.device.scanner.configuration.Symbology;
import android.device.scanner.configuration.Triggering;
import android.hardware.Camera;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.corpogas.corpoapp.Configuracion.SQLiteBD;
import com.corpogas.corpoapp.LecturaTarjetas.MonederosElectronicos;
import com.corpogas.corpoapp.Menu_Principal;
import com.corpogas.corpoapp.Modales.Modales;
import com.corpogas.corpoapp.Productos.ListAdapterProductos;
import com.corpogas.corpoapp.Puntada.PuntadaRedimirQr;
import com.corpogas.corpoapp.R;
import com.corpogas.corpoapp.Tickets.PosicionCargaTickets;
import com.corpogas.corpoapp.VentaCombustible.DiferentesFormasPago;
import com.corpogas.corpoapp.VentaCombustible.FormasPago;
import com.corpogas.corpoapp.VentaCombustible.VentaProductos;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ValesPapel extends AppCompatActivity {
    Spinner spnValesPapel;
    Button btnAgregarVale, btnAceptarValesPapel, btnOtrasFormasPago;
    ListView lstValesPapel;
    ImageButton imgEscanearVale, imgEscanearVale2;
    int sumaTiposVale=0, TipoValesPapelId;
    String[] opcionesTipoVale;// = new String[sumaTiposVale];
    Double montoaCobrar;
    String posicionCarga, usuarioid, estacionJarreo, claveProducto,  ValeSeleccionado, ipEstacion, ValePapelId = "2", idoperativa;
    EditText  tvFolioMonto, tvDenominacionMonto;
    TextView tvMontoACargar, tvMontoACargarPendiente;
    List<String> IDMontos;
    List<String> NombreTipoValePapel;
    List<String> MontoVale;
    List<String> FolioValePapel;
    List<String> TipoValesPapel;

    SQLiteBD data;
    String url, fechaTicket;
    JSONObject FormasPagoObjecto, datos;
    JSONArray FormasPagoArreglo;

    private static Map<String, BarcodeHolder> mBarcodeMap = new HashMap<String, BarcodeHolder>();

    private ScanManager mScanManager = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vales_papel);
        data = new SQLiteBD(getApplicationContext());
        this.setTitle(data.getNombreEstacion() + " ( EST.:" + data.getNumeroEstacion() + ")");

        posicionCarga = getIntent().getStringExtra("posicioncarga");
        usuarioid = getIntent().getStringExtra("numeroEmpleado");
        estacionJarreo = getIntent().getStringExtra("estacionjarreo");
        claveProducto = getIntent().getStringExtra("claveProducto");
        montoaCobrar = getIntent().getDoubleExtra("montoencanasta", 0);
        idoperativa = getIntent().getStringExtra("idoperativa");

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
                if (idoperativa.equals("20")){
                    EnviarValesPapel();
                }else{
//                    String titulo = "PUNTADA";
//                    String mensajes = "Desea Acumular la venta a su Tarjeta Puntada?";
//                    Modales modalesPuntada = new Modales(ValesPapel.this);
//                    View viewLectura = modalesPuntada.MostrarDialogoAlerta(ValesPapel.this, mensajes,  "SI", "NO");
//                    viewLectura.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            String banderaHuella = getIntent().getStringExtra( "banderaHuella");
//                            String nombreCompletoVenta = getIntent().getStringExtra("nombrecompleto");
//                            //LeeTarjeta();
//                            Intent intent = new Intent(getApplicationContext(), MonederosElectronicos.class);
//                            //intent.putExtra("device_name", m_deviceName);
//                            intent.putExtra("Enviadodesde", "formaspago");
//                            intent.putExtra("numeroEmpleado", data.getNumeroEmpleado());
//                            intent.putExtra("idoperativa", 1);
//                            intent.putExtra("formapagoid", "2");
//                            intent.putExtra("NombrePago", "Vales");
//                            intent.putExtra("NombreCompleto", data.getNombreCompleto());
//                            intent.putExtra("montoenlacanasta", montoaCobrar);
//                            intent.putExtra("posicioncargaid", posicionCarga);
//                            intent.putExtra("tipoTarjeta", "Puntada");
//                            intent.putExtra("pagoconpuntada", "no");
//
//                            startActivity(intent);
//                            modalesPuntada.alertDialog.dismiss();
//                        }
//                    });
//                    viewLectura.findViewById(R.id.buttonNo).setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
////                                    RespuestaImprimeFinaliza(posicioncarga, idusuario, formapagoid, numticket, nombrepago);
//                            modalesPuntada.alertDialog.dismiss();
////                                    SeleccionaPesosDoalares();
                            EnviarValesPapel();
//                        }
//                    });
//
                }
            }
        });

        btnOtrasFormasPago.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EnviaraDiferentesFormasPago();
            }
        });

        imgEscanearVale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                IntentIntegrator integrator = new IntentIntegrator(ValesPapel.this);
//                integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
//                integrator.setPrompt("Lector - CDP");
//                integrator.setCameraId(0);
//                integrator.setBeepEnabled(false);
//                integrator.setBarcodeImageEnabled(true);
//                integrator.initiateScan();
            }
        });

        imgEscanearVale2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator integrator = new IntentIntegrator(ValesPapel.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                integrator.setPrompt("Lector - CDP");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(false);
                integrator.setBarcodeImageEnabled(true);
                integrator.initiateScan();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        initScan();
    }


    private void initScan() {
        mScanManager = new ScanManager();
        boolean powerOn = mScanManager.getScannerState();
        if (!powerOn) {
            powerOn = mScanManager.openScanner();
            if (!powerOn) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Scanner cannot be turned on!");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog mAlertDialog = builder.create();
                mAlertDialog.show();
            }
        }
        initBarcodeParameters();
    }


    private void initBarcodeParameters() {
        mBarcodeMap.clear();
        BarcodeHolder holder = new BarcodeHolder();
        // Symbology.AZTEC
        holder.mBarcodeEnable = new CheckBoxPreference(this);
        holder.mParaIds = new int[]{PropertyID.AZTEC_ENABLE};
        holder.mParaKeys = new String[]{"AZTEC_ENABLE"};
        mBarcodeMap.put(Symbology.AZTEC + "", holder);
        // Symbology.CHINESE25
        holder = new BarcodeHolder();
        holder.mBarcodeEnable = new CheckBoxPreference(this);
        holder.mParaIds = new int[]{PropertyID.C25_ENABLE};
        holder.mParaKeys = new String[]{"C25_ENABLE"};
        mBarcodeMap.put(Symbology.CHINESE25 + "", holder);
        // Symbology.CODABAR
        holder = new BarcodeHolder();
        holder.mBarcodeEnable = new CheckBoxPreference(this);
        holder.mBarcodeLength1 = new EditTextPreference(this);
        holder.mBarcodeLength2 = new EditTextPreference(this);
        holder.mBarcodeNOTIS = new CheckBoxPreference(this);
        holder.mBarcodeCLSI = new CheckBoxPreference(this);
        holder.mParaIds = new int[]{PropertyID.CODABAR_ENABLE, PropertyID.CODABAR_LENGTH1, PropertyID.CODABAR_LENGTH2, PropertyID.CODABAR_NOTIS, PropertyID.CODABAR_CLSI};
        holder.mParaKeys = new String[]{"CODABAR_ENABLE", "CODABAR_LENGTH1", "CODABAR_LENGTH2", "CODABAR_NOTIS", "CODABAR_CLSI"};
        mBarcodeMap.put(Symbology.CODABAR + "", holder);
        // Symbology.CODE11
        holder = new BarcodeHolder();
        holder.mBarcodeEnable = new CheckBoxPreference(this);
        holder.mBarcodeLength1 = new EditTextPreference(this);
        holder.mBarcodeLength2 = new EditTextPreference(this);
        holder.mBarcodeCheckDigit = new ListPreference(this);
        holder.mParaIds = new int[]{PropertyID.CODE11_ENABLE, PropertyID.CODE11_LENGTH1, PropertyID.CODE11_LENGTH2, PropertyID.CODE11_SEND_CHECK};
        holder.mParaKeys = new String[]{"CODE11_ENABLE", "CODE11_LENGTH1", "CODE11_LENGTH2", "CODE11_SEND_CHECK"};
        mBarcodeMap.put(Symbology.CODE11 + "", holder);
        // Symbology.CODE32
        holder = new BarcodeHolder();
        holder.mBarcodeEnable = new CheckBoxPreference(this);
        holder.mParaIds = new int[]{PropertyID.CODE32_ENABLE};
        holder.mParaKeys = new String[]{"CODE32_ENABLE"};
        mBarcodeMap.put(Symbology.CODE32 + "", holder);
        // Symbology.CODE39
        holder = new BarcodeHolder();
        holder.mBarcodeEnable = new CheckBoxPreference(this);
        holder.mBarcodeLength1 = new EditTextPreference(this);
        holder.mBarcodeLength2 = new EditTextPreference(this);
        holder.mBarcodeChecksum = new CheckBoxPreference(this);
        holder.mBarcodeSendCheck = new CheckBoxPreference(this);
        holder.mBarcodeFullASCII = new CheckBoxPreference(this);
        holder.mParaIds = new int[]{PropertyID.CODE39_ENABLE, PropertyID.CODE39_LENGTH1, PropertyID.CODE39_LENGTH2, PropertyID.CODE39_ENABLE_CHECK, PropertyID.CODE39_SEND_CHECK, PropertyID.CODE39_FULL_ASCII};
        holder.mParaKeys = new String[]{"CODE39_ENABLE", "CODE39_LENGTH1", "CODE39_LENGTH2", "CODE39_ENABLE_CHECK", "CODE39_SEND_CHECK", "CODE39_FULL_ASCII"};
        mBarcodeMap.put(Symbology.CODE39 + "", holder);
        // Symbology.CODE93
        holder = new BarcodeHolder();
        holder.mBarcodeEnable = new CheckBoxPreference(this);
        holder.mBarcodeLength1 = new EditTextPreference(this);
        holder.mBarcodeLength2 = new EditTextPreference(this);
        holder.mParaIds = new int[]{PropertyID.CODE93_ENABLE, PropertyID.CODE93_LENGTH1, PropertyID.CODE93_LENGTH2};
        holder.mParaKeys = new String[]{"CODE93_ENABLE", "CODE93_LENGTH1", "CODE93_LENGTH2"};
        mBarcodeMap.put(Symbology.CODE93 + "", holder);
        // Symbology.CODE128
        holder = new BarcodeHolder();
        holder.mBarcodeEnable = new CheckBoxPreference(this);
        holder.mBarcodeLength1 = new EditTextPreference(this);
        holder.mBarcodeLength2 = new EditTextPreference(this);
        holder.mBarcodeISBT = new CheckBoxPreference(this);
        holder.mParaIds = new int[]{PropertyID.CODE128_ENABLE, PropertyID.CODE128_LENGTH1, PropertyID.CODE128_LENGTH2, PropertyID.CODE128_CHECK_ISBT_TABLE};
        holder.mParaKeys = new String[]{"CODE128_ENABLE", "CODE128_LENGTH1", "CODE128_LENGTH2", "CODE128_CHECK_ISBT_TABLE"};
        mBarcodeMap.put(Symbology.CODE128 + "", holder);
        // Symbology.COMPOSITE_CC_AB
        holder = new BarcodeHolder();
        holder.mBarcodeEnable = new CheckBoxPreference(this);
        holder.mParaIds = new int[]{PropertyID.COMPOSITE_CC_AB_ENABLE};
        holder.mParaKeys = new String[]{"COMPOSITE_CC_AB_ENABLE"};
        mBarcodeMap.put(Symbology.COMPOSITE_CC_AB + "", holder);
        // Symbology.COMPOSITE_CC_C
        holder = new BarcodeHolder();
        holder.mBarcodeEnable = new CheckBoxPreference(this);
        holder.mParaIds = new int[]{PropertyID.COMPOSITE_CC_C_ENABLE};
        holder.mParaKeys = new String[]{"COMPOSITE_CC_C_ENABLE"};
        mBarcodeMap.put(Symbology.COMPOSITE_CC_C + "", holder);
        // Symbology.DATAMATRIX
        holder = new BarcodeHolder();
        holder.mBarcodeEnable = new CheckBoxPreference(this);
        holder.mParaIds = new int[]{PropertyID.DATAMATRIX_ENABLE};
        holder.mParaKeys = new String[]{"DATAMATRIX_ENABLE"};
        mBarcodeMap.put(Symbology.DATAMATRIX + "", holder);
        // Symbology.DISCRETE25
        holder = new BarcodeHolder();
        holder.mBarcodeEnable = new CheckBoxPreference(this);
        holder.mParaIds = new int[]{PropertyID.D25_ENABLE};
        holder.mParaKeys = new String[]{"D25_ENABLE"};
        mBarcodeMap.put(Symbology.DISCRETE25 + "", holder);
        // Symbology.EAN8
        holder = new BarcodeHolder();
        holder.mBarcodeEnable = new CheckBoxPreference(this);
        holder.mParaIds = new int[]{PropertyID.EAN8_ENABLE};
        holder.mParaKeys = new String[]{"EAN8_ENABLE"};
        mBarcodeMap.put(Symbology.EAN8 + "", holder);
        // Symbology.EAN13
        holder = new BarcodeHolder();
        holder.mBarcodeEnable = new CheckBoxPreference(this);
        holder.mBarcodeBookland = new CheckBoxPreference(this);
        holder.mParaIds = new int[]{PropertyID.EAN13_ENABLE, PropertyID.EAN13_BOOKLANDEAN};
        holder.mParaKeys = new String[]{"EAN13_ENABLE", "EAN13_BOOKLANDEAN"};
        mBarcodeMap.put(Symbology.EAN13 + "", holder);
        // Symbology.GS1_14
        holder = new BarcodeHolder();
        holder.mBarcodeEnable = new CheckBoxPreference(this);
        holder.mParaIds = new int[]{PropertyID.GS1_14_ENABLE};
        holder.mParaKeys = new String[]{"GS1_14_ENABLE"};
        mBarcodeMap.put(Symbology.GS1_14 + "", holder);
        // Symbology.GS1_128
        holder = new BarcodeHolder();
        holder.mBarcodeEnable = new CheckBoxPreference(this);
        holder.mParaIds = new int[]{PropertyID.CODE128_GS1_ENABLE};
        holder.mParaKeys = new String[]{"CODE128_GS1_ENABLE"};
        mBarcodeMap.put(Symbology.GS1_128 + "", holder);
        // Symbology.GS1_EXP
        holder = new BarcodeHolder();
        holder.mBarcodeEnable = new CheckBoxPreference(this);
        holder.mBarcodeLength1 = new EditTextPreference(this);
        holder.mBarcodeLength2 = new EditTextPreference(this);
        holder.mParaIds = new int[]{PropertyID.GS1_EXP_ENABLE, PropertyID.GS1_EXP_LENGTH1, PropertyID.GS1_EXP_LENGTH2};
        holder.mParaKeys = new String[]{"GS1_EXP_ENABLE", "GS1_EXP_LENGTH1", "GS1_EXP_LENGTH2"};
        mBarcodeMap.put(Symbology.GS1_EXP + "", holder);
        // Symbology.GS1_LIMIT
        holder = new BarcodeHolder();
        holder.mBarcodeEnable = new CheckBoxPreference(this);
        holder.mParaIds = new int[]{PropertyID.GS1_LIMIT_ENABLE};
        holder.mParaKeys = new String[]{"GS1_LIMIT_ENABLE"};
        mBarcodeMap.put(Symbology.GS1_LIMIT + "", holder);
        // Symbology.INTERLEAVED25
        holder = new BarcodeHolder();
        holder.mBarcodeEnable = new CheckBoxPreference(this);
        holder.mBarcodeLength1 = new EditTextPreference(this);
        holder.mBarcodeLength2 = new EditTextPreference(this);
        holder.mBarcodeChecksum = new CheckBoxPreference(this);
        holder.mBarcodeSendCheck = new CheckBoxPreference(this);
        holder.mParaIds = new int[]{PropertyID.I25_ENABLE, PropertyID.I25_LENGTH1, PropertyID.I25_LENGTH2, PropertyID.I25_ENABLE_CHECK, PropertyID.I25_SEND_CHECK};
        holder.mParaKeys = new String[]{"I25_ENABLE", "I25_LENGTH1", "I25_LENGTH2", "I25_ENABLE_CHECK", "I25_SEND_CHECK"};
        mBarcodeMap.put(Symbology.INTERLEAVED25 + "", holder);
        // Symbology.MATRIX25
        holder = new BarcodeHolder();
        holder.mBarcodeEnable = new CheckBoxPreference(this);
        holder.mParaIds = new int[]{PropertyID.M25_ENABLE};
        holder.mParaKeys = new String[]{"M25_ENABLE"};
        mBarcodeMap.put(Symbology.MATRIX25 + "", holder);
        // Symbology.MAXICODE
        holder = new BarcodeHolder();
        holder.mBarcodeEnable = new CheckBoxPreference(this);
        holder.mParaIds = new int[]{PropertyID.MAXICODE_ENABLE};
        holder.mParaKeys = new String[]{"MAXICODE_ENABLE"};
        mBarcodeMap.put(Symbology.MAXICODE + "", holder);
        // Symbology.MICROPDF417
        holder = new BarcodeHolder();
        holder.mBarcodeEnable = new CheckBoxPreference(this);
        holder.mParaIds = new int[]{PropertyID.MICROPDF417_ENABLE};
        holder.mParaKeys = new String[]{"MICROPDF417_ENABLE"};
        mBarcodeMap.put(Symbology.MICROPDF417 + "", holder);
        // Symbology.MSI
        holder = new BarcodeHolder();
        holder.mBarcodeEnable = new CheckBoxPreference(this);
        holder.mBarcodeLength1 = new EditTextPreference(this);
        holder.mBarcodeLength2 = new EditTextPreference(this);
        holder.mBarcodeSecondChecksum = new CheckBoxPreference(this);
        holder.mBarcodeSendCheck = new CheckBoxPreference(this);
        holder.mBarcodeSecondChecksumMode = new CheckBoxPreference(this);
        holder.mParaIds = new int[]{PropertyID.MSI_ENABLE, PropertyID.MSI_LENGTH1, PropertyID.MSI_LENGTH2, PropertyID.MSI_REQUIRE_2_CHECK, PropertyID.MSI_SEND_CHECK, PropertyID.MSI_CHECK_2_MOD_11};
        holder.mParaKeys = new String[]{"MSI_ENABLE", "MSI_LENGTH1", "MSI_LENGTH2", "MSI_REQUIRE_2_CHECK", "MSI_SEND_CHECK", "MSI_CHECK_2_MOD_11"};
        mBarcodeMap.put(Symbology.MSI + "", holder);
        // Symbology.PDF417
        holder = new BarcodeHolder();
        holder.mBarcodeEnable = new CheckBoxPreference(this);
        holder.mParaIds = new int[]{PropertyID.PDF417_ENABLE};
        holder.mParaKeys = new String[]{"PDF417_ENABLE"};
        mBarcodeMap.put(Symbology.PDF417 + "", holder);
        // Symbology.QRCODE
        holder = new BarcodeHolder();
        holder.mBarcodeEnable = new CheckBoxPreference(this);
        holder.mParaIds = new int[]{PropertyID.QRCODE_ENABLE};
        holder.mParaKeys = new String[]{"QRCODE_ENABLE"};
        mBarcodeMap.put(Symbology.QRCODE + "", holder);
        // Symbology.TRIOPTIC
        holder = new BarcodeHolder();
        holder.mBarcodeEnable = new CheckBoxPreference(this);
        holder.mParaIds = new int[]{PropertyID.TRIOPTIC_ENABLE};
        holder.mParaKeys = new String[]{"TRIOPTIC_ENABLE"};
        mBarcodeMap.put(Symbology.TRIOPTIC + "", holder);
        // Symbology.UPCA
        holder = new BarcodeHolder();
        holder.mBarcodeEnable = new CheckBoxPreference(this);
        holder.mBarcodeChecksum = new CheckBoxPreference(this);
        holder.mBarcodeSystemDigit = new CheckBoxPreference(this);
        holder.mBarcodeConvertEAN13 = new CheckBoxPreference(this);
        holder.mParaIds = new int[]{PropertyID.UPCA_ENABLE, PropertyID.UPCA_SEND_CHECK, PropertyID.UPCA_SEND_SYS, PropertyID.UPCA_TO_EAN13};
        holder.mParaKeys = new String[]{"UPCA_ENABLE", "UPCA_SEND_CHECK", "UPCA_SEND_SYS", "UPCA_TO_EAN13"};
        mBarcodeMap.put(Symbology.UPCA + "", holder);
        // Symbology.UPCE
        holder = new BarcodeHolder();
        holder.mBarcodeEnable = new CheckBoxPreference(this);
        holder.mBarcodeChecksum = new CheckBoxPreference(this);
        holder.mBarcodeSystemDigit = new CheckBoxPreference(this);
        holder.mBarcodeConvertUPCA = new CheckBoxPreference(this);
        holder.mParaIds = new int[]{PropertyID.UPCE_ENABLE, PropertyID.UPCE_SEND_CHECK, PropertyID.UPCE_SEND_SYS, PropertyID.UPCE_TO_UPCA};
        holder.mParaKeys = new String[]{"UPCE_ENABLE", "UPCE_SEND_CHECK", "UPCE_SEND_SYS", "UPCE_TO_UPCA"};
        mBarcodeMap.put(Symbology.UPCE + "", holder);
        // Symbology.UPCE1
        holder = new BarcodeHolder();
        holder.mBarcodeEnable = new CheckBoxPreference(this);
        holder.mParaIds = new int[]{PropertyID.UPCE1_ENABLE};
        holder.mParaKeys = new String[]{"UPCE1_ENABLE"};
        mBarcodeMap.put(Symbology.UPCE1 + "", holder);
    }

    static class BarcodeHolder {
        CheckBoxPreference mBarcodeEnable = null;
        EditTextPreference mBarcodeLength1 = null;
        EditTextPreference mBarcodeLength2 = null;

        CheckBoxPreference mBarcodeNOTIS = null;
        CheckBoxPreference mBarcodeCLSI = null;

        CheckBoxPreference mBarcodeISBT = null;
        CheckBoxPreference mBarcodeChecksum = null;
        CheckBoxPreference mBarcodeSendCheck = null;
        CheckBoxPreference mBarcodeFullASCII = null;
        ListPreference mBarcodeCheckDigit = null;
        CheckBoxPreference mBarcodeBookland = null;
        CheckBoxPreference mBarcodeSecondChecksum = null;
        CheckBoxPreference mBarcodeSecondChecksumMode = null;
        ListPreference mBarcodePostalCode = null;
        CheckBoxPreference mBarcodeSystemDigit = null;
        CheckBoxPreference mBarcodeConvertEAN13 = null;
        CheckBoxPreference mBarcodeConvertUPCA = null;
        CheckBoxPreference mBarcodeEanble25DigitExtensions = null;
        CheckBoxPreference mBarcodeDPM = null;
        int[] mParaIds = null;
        String[] mParaKeys = null;
    }


    protected void onActivityResult (int requestCode, int resulCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resulCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(getApplicationContext(), "Lectura Cancelada", Toast.LENGTH_SHORT).show();
            } else {
//                if (result.getContents().length() >= 17) {
                    tvFolioMonto.setText(result.getContents());
//                } else {
//                    int resultInt = Integer.parseInt(result.getContents());
//                    String residuo = result.getContents().substring(result.getContents().length() - 2);
////                    int conversion = resultInt/100;
////                    String resultConversion = Integer.toString(conversion);
//                    tvDenominacionMonto.setText(resultConversion);
//                    tvDenominacionMonto.setText(result.getContents());
//                }
            }
        } else {
            super.onActivityResult(requestCode, resulCode, data);
        }
    }

    private  void EnviaraDiferentesFormasPago(){
        if (tvMontoACargarPendiente.getText().toString().equals("0.00")){
            String titulo = "AVISO";
            Modales modales = new Modales(ValesPapel.this);
            View view1 = modales.MostrarDialogoError(ValesPapel.this, "Ya se ha completado el total de la venta");
            view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    modales.alertDialog.dismiss();
                }
            });
        }else{
            JSONObject folioDatos;// = new JSONObject();
            JSONObject datosVale = null; //= new JSONObject();
            JSONArray datosGlobal = new JSONArray();
            datos = new JSONObject();
            Double montoVales=0.00;
            FormasPagoArreglo = new JSONArray();

            for(int m=0; m < NombreTipoValePapel.size(); m++){
                try {
                    folioDatos = new JSONObject();
                    datosVale = new JSONObject();
                    folioDatos.put("Folio", FolioValePapel.get(m));
                    folioDatos.put("SucursalId", data.getIdSucursal());
                    folioDatos.put("PosicionCargaId", posicionCarga);
                    folioDatos.put("TipoValePapelId", TipoValesPapelId);
                    datosVale.put("Id", ValePapelId);
                    datosVale.put ("Importe", IDMontos.get(m));
                    montoVales = montoVales + Double.parseDouble(IDMontos.get(m));
                    datosVale.put("DetalleParcialidad", folioDatos);
                    datosGlobal.put(datosVale);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            Intent intentDiferente = new Intent(getApplicationContext(), DiferentesFormasPago.class);//DiferentesFormasPagoPuntada
            intentDiferente.putExtra("Enviadodesde", "valespapel");
            intentDiferente.putExtra("posicioncarga", posicionCarga);
            intentDiferente.putExtra("idoperativa", 5);
            intentDiferente.putExtra("montoencanasta", montoaCobrar- montoVales);
            intentDiferente.putExtra("arregloVales", datosGlobal.toString());
            intentDiferente.putExtra("montoencanastavales", montoVales);

            startActivity(intentDiferente);
            finish();

        }
    }

    private void AgregarValePapelaLista(){
        Double montoPendiente;

        //Validamos campos a agregar <> de vacio
        if (tvFolioMonto.length() ==0){
//            Toast.makeText(ValesPapel.this, "Digite un Folio", Toast.LENGTH_SHORT).show();
            String titulo = "AVISO";
            Modales modales = new Modales(ValesPapel.this);
            View view1 = modales.MostrarDialogoAlertaAceptar(ValesPapel.this,"Digite un folio",titulo);
            view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    modales.alertDialog.dismiss();
                }
            });

        }else{
            if (tvDenominacionMonto.length() == 0){
//                Toast.makeText(ValesPapel.this, "Digite un Monto", Toast.LENGTH_SHORT).show();
                String titulo = "AVISO";
                Modales modales = new Modales(ValesPapel.this);
                View view1 = modales.MostrarDialogoAlertaAceptar(ValesPapel.this,"Digite un monto",titulo);
                view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        modales.alertDialog.dismiss();
                    }
                });

            }else{
                Double montofaltante = Double.parseDouble(tvMontoACargarPendiente.getText().toString());
                if (montofaltante<=0){
//                    Toast.makeText(ValesPapel.this, "Monto Completado", Toast.LENGTH_SHORT).show();
                    String titulo = "AVISO";
                    Modales modales = new Modales(ValesPapel.this);
                    View view1 = modales.MostrarDialogoAlertaAceptar(ValesPapel.this,"Monto Completado",titulo);
                    view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            modales.alertDialog.dismiss();
                        }
                    });

                }else{
                    montoPendiente = Double.parseDouble(tvMontoACargarPendiente.getText().toString());

//                    if (Double.parseDouble(tvDenominacionMonto.getText().toString()) > montoPendiente){
//                        Toast.makeText(ValesPapel.this, "El monto de los vales no puede ser mayor que el monto Total", Toast.LENGTH_SHORT).show();
//                    }else{

                        boolean bandera=false;
                        for(Integer m = 0; m<FolioValePapel.size(); m++){
                            String codigo = FolioValePapel.get(m);
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
                            MontoVale.add(tvDenominacionMonto.getText().toString());
                            FolioValePapel.add(tvFolioMonto.getText().toString());
                            ListAdapterProductos adapterP = new ListAdapterProductos(this, NombreTipoValePapel, FolioValePapel);
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
//                                    Toast.makeText(ValesPapel.this, "asda", Toast.LENGTH_SHORT).show();
                                    String titulo = "¿Estas seguro?";
                                    String mensajes = "¿Deseas eliminar el elemento seleccionado?";
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
                                            FolioValePapel.remove(identificador);
                                            IDMontos.remove(identificador);

                                            ListAdapterProductos adapterP = new ListAdapterProductos(ValesPapel.this, NombreTipoValePapel, FolioValePapel );
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
                                String titulo = "AVISO";
                                String mensaje = "El folio No. "+ tvFolioMonto.getText().toString() +" ya fue agregado";
                                Modales modales = new Modales(ValesPapel.this);
                                View view1 = modales.MostrarDialogoAlertaAceptar(ValesPapel.this,mensaje,titulo);
                                view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        modales.alertDialog.dismiss();
                                    }
                                });
//                            Toast.makeText(ValesPapel.this, "El folio No. "+ tvFolioMonto.getText().toString() +" ya fue agregado", Toast.LENGTH_SHORT).show();
                        }
//                    }
                }
            }
        }
    }

    private void EnviarValesPapel() {
        if (Double.parseDouble(tvMontoACargarPendiente.getText().toString())> 0 ){
            String titulo = "AVISO";
            String mensaje = "Se debe cubrir el 100% del monto total";
            Modales modales = new Modales(ValesPapel.this);
            View view1 = modales.MostrarDialogoAlertaAceptar(ValesPapel.this,mensaje,titulo);
            view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    modales.alertDialog.dismiss();
                }
            });

        }else{

            JSONObject folioDatos;// = new JSONObject();
            JSONObject datosVale = null; //= new JSONObject();
            JSONArray datosGlobal = new JSONArray();
            datos = new JSONObject();
            FormasPagoArreglo = new JSONArray();

            for(int m=0; m < NombreTipoValePapel.size(); m++){
                try {
                    folioDatos = new JSONObject();
                    datosVale = new JSONObject();
                    folioDatos.put("Folio", FolioValePapel.get(m));
                    folioDatos.put("SucursalId", data.getIdSucursal());
                    folioDatos.put("PosicionCargaId", posicionCarga);
                    folioDatos.put("TipoValePapelId", TipoValesPapelId);
                    datosVale.put("Id", ValePapelId);
                    datosVale.put ("Importe", IDMontos.get(m));
                    datosVale.put("DetalleParcialidad", folioDatos);
                    datosGlobal.put(datosVale);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
    //        FormasPagoArreglo.put(datosVale);
            JSONObject datos = new JSONObject();
            String url = "http://" + ipEstacion + "/CorpogasService/api/tickets/generar";
            RequestQueue queue = Volley.newRequestQueue(this);
            try {
                datos.put("PosicionCargaId", posicionCarga);
                datos.put("IdUsuario",  data.getUsuarioId());
                datos.put("SucursalId", data.getIdSucursal());
                datos.put("IdFormasPago", datosGlobal);
                datos.put("SucursalId", data.getIdSucursal());
                datos.put("ConfiguracionAplicacionId", data.getIdTarjtero());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JsonObjectRequest request_json = new JsonObjectRequest(Request.Method.POST, url, datos, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    String detalle = null;
                    try {
                        detalle = response.getString("Detalle");
                        if (detalle.equals("null")) {
                            String estado1 = response.getString("Resultado");
                            JSONObject descripcion = new JSONObject(estado1);
                            String estado = descripcion.getString("Descripcion");
                            try {
                                String titulo = "AVISO";
                                String mensaje = estado;
                                Modales modales = new Modales(ValesPapel.this);
                                View view1 = modales.MostrarDialogoError(ValesPapel.this, mensaje);
                                view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(getApplicationContext(), Menu_Principal.class);
                                        startActivity(intent);
                                        finish();
                                        modales.alertDialog.dismiss();
                                    }
                                });
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        } else {
                            String titulo = "AVISO";
                            Modales modales = new Modales(ValesPapel.this);
                            View view1 = modales.MostrarDialogoCorrecto(ValesPapel.this, "Ticket Impreso en Impresora Central");
                            view1.findViewById(R.id.buttonAction).setOnClickListener(new View.OnClickListener() { //buttonYes
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(getApplicationContext(), Menu_Principal.class);
                                    startActivity(intent);
                                    finish();
                                    modales.alertDialog.dismiss();
                                }
                            });
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(ValesPapel.this, "error", Toast.LENGTH_SHORT).show();

                }
            }){
                public Map<String,String> getHeaders() throws AuthFailureError {
                    Map<String,String> headers = new HashMap<String, String>();
                    return headers;
                }
                protected Response<JSONObject> parseNetwokResponse(NetworkResponse response){
                    if (response != null){
                        try {
                            String responseString;
                            JSONObject datos = new JSONObject();
                            responseString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));

                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                    return Response.success(datos, HttpHeaderParser.parseCacheHeaders(response));
                }
            };
            queue.add(request_json);
        }

    }

    private void LimpiarPantallaValesPapel(){

    }

    private void init() {
        spnValesPapel = (Spinner) findViewById(R.id.spTipoValePapel);
        btnAgregarVale = (Button) findViewById(R.id.btnAgregarVale);
        btnAceptarValesPapel = (Button) findViewById(R.id.btnAceptarValesPapel);
        btnOtrasFormasPago = (Button) findViewById(R.id.btnOtrasFormasPago);
        lstValesPapel = (ListView) findViewById(R.id.lstValesPapel);
        imgEscanearVale = (ImageButton) findViewById(R.id.imgEscanearVale);
        imgEscanearVale2 = (ImageButton) findViewById(R.id.imgEscanearVale2);
        tvMontoACargar = (TextView) findViewById(R.id.tvMontoACargar);
        tvMontoACargarPendiente = (TextView) findViewById(R.id.tvMontoACargarPendiente);
        tvFolioMonto = (EditText) findViewById(R.id.tvFolioMonto);
        tvDenominacionMonto = (EditText) findViewById(R.id.tvDenominacionMonto);
        data = new SQLiteBD(getApplicationContext());
        ipEstacion = data.getIpEstacion();

        IDMontos = new ArrayList<String>();
        NombreTipoValePapel = new ArrayList<String>();
        MontoVale = new ArrayList<String>();
        FolioValePapel = new ArrayList<String>();
        TipoValesPapel = new ArrayList<String>();
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
                            TipoValesPapel.add(IdentificadorVale);
                        }
                    }
                    tvMontoACargar.setText(montoaCobrar.toString());
                    tvMontoACargarPendiente.setText(montoaCobrar.toString());
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(ValesPapel.this, R.layout.support_simple_spinner_dropdown_item, opcionesTipoVale);
                    spnValesPapel.setAdapter(adapter);
                    spnValesPapel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            ValeSeleccionado = adapterView.getItemAtPosition(i).toString();
                            TipoValesPapelId = Integer.parseInt(TipoValesPapel.get(i));
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