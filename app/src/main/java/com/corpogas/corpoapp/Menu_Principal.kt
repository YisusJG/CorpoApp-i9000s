package com.corpogas.corpoapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.corpogas.corpoapp.ConexionInternet.ComprobarConexionInternet;
import com.corpogas.corpoapp.Configuracion.SQLiteBD;
import com.corpogas.corpoapp.Encriptacion.EncriptarMAC;
import com.corpogas.corpoapp.Modales.Modales;

public class Menu_Principal extends AppCompatActivity {
    DrawerLayout drawerLayout;
    ListView list;

    //-------Variables necesarias para ininicalizar el lector de huellas
    private final int GENERAL_ACTIVITY_RESULT = 1;
    private static final String ACTION_USB_PERMISSION = "com.digitalpersona.uareu.dpfpddusbhost.USB_PERMISSION";
    private String m_deviceName = "";
    //    private String m_versionName = "";
//    private final String tag = "UareUSampleJava";
//    Reader m_reader;
    String banderaHuella= "";
    private ImageSwitcher imgFoto;
    ImageView imagen;
    Boolean ImageDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu__principal);
        SQLiteBD data = new SQLiteBD(this);
        this.setTitle(data.getNombreEsatcion());

        drawerLayout = findViewById(R.id.drawer_layout);
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,"open navigation drawer", "close navigation drawer");
//        drawerLayout.addDrawerListener(toggle);
//        toggle.syncState();

//        if (banderaHuella.length()==0){
//            banderaHuella = "false";
//        }else{
//            banderaHuella = getIntent().getStringExtra( "banderaHuella");
//        }
//        //power on
//        if (banderaHuella.equals("true")){
//            PM.powerOn();
//            launchGetReader();
//        }
        EncriptarMAC mac = new EncriptarMAC();
        String mac2 = mac.getMacAddr();
        String macmd5 = mac.getMD5(mac2);

        //carga el color de acuerdo al tipoEstacion
        String primaryDark = "#1C0D02";
        String background = "#FFFFFF";
        String primary= "";
        String tipo = data.getTipoEstacion();
        if (tipo.equals("CORPOGAS")){
            primary = "#003300";
        }else{
            if (tipo.equals("GULF")){
                primary = "#FF9800";
            }else{
                if (tipo.equals("PEMEX")){
                    primary = "#003300";
                }
            }
        }
        cambiaColor(primaryDark, primary, background);
        imagen = (ImageView) findViewById(R.id.imgFoto);
        imagen.setImageResource(R.drawable.gasolinera);
        ImageDisplay = true;

    }

    private void  formaAutenticacion() {
        String mensaje = "Qué forma de Autenticación desea utilizar?";
        Modales modales = new Modales(Menu_Principal.this);
        View viewLectura = modales.MostrarDialogoAlerta(Menu_Principal.this, mensaje,  "Huella", "Clave");
        viewLectura.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modales.alertDialog.dismiss();
                banderaHuella = "true";
//                PM.powerOn();
//                launchGetReader();
            }
        });

        viewLectura.findViewById(R.id.buttonNo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Si no se terclea nada envia mensaje de teclear contraseña
                banderaHuella = "false";
                modales.alertDialog.dismiss();
            }
        });

    }

    public void ClicMenu(View view){
        openDrawer(drawerLayout);
    }

    private void openDrawer(DrawerLayout drawerLayout) {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public  void ClickLogo(View view){
        closeDrawer(drawerLayout);
    }

    public static void closeDrawer(DrawerLayout drawerLayout) {
        //Close drawer Layout Check condition
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    public void ClickHome(View view){
        recreate();
    }

    public void ClickHuella(View view){
        formaAutenticacion();
    }

    public void ClickCerrarSesion(View view){
//        Toast.makeText(this, "Cerrar Sesión", Toast.LENGTH_SHORT).show();
//        recreate();
        if (!ComprobarConexionInternet.compruebaConexion(this)) {
            Toast.makeText(getBaseContext(),"Necesaria conexión a internet ", Toast.LENGTH_SHORT).show();
        }

    }


    public void ClickSalir(View view){
        LogOut(this);
    }

    public void ClickCambiar(View view){
        if (ImageDisplay){
            imagen.setImageResource(R.drawable.corpogas1);
            ImageDisplay = false;
        }else{
            imagen.setImageResource(R.drawable.gasolinera);
            ImageDisplay =true;
        }
    }


    public void ClickDashboard(View view){
//        redirectActivity(this, NuevaClas.class);
    }


    public void cargarImagen(){
//        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//
//        intent.setType("image/");
//        startActivityForResult(intent.createChooser(intent, "Seleccione la Aplicación"), 10);
        imagen.setImageResource(R.drawable.gasolinera);

    }


    public void LogOut(Menu_Principal mainActivity) {
        String mensaje = "Estas seguro de Salir de la aplicación ?";
        Modales modales = new Modales(Menu_Principal.this);
        View viewLectura = modales.MostrarDialogoAlerta(Menu_Principal.this, mensaje,  "SI", "NO");
        viewLectura.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.finishAffinity();
                System.exit(0);
                modales.alertDialog.dismiss();
            }
        });

        viewLectura.findViewById(R.id.buttonNo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Si no se terclea nada envia mensaje de teclear contraseña
                modales.alertDialog.dismiss();
            }
        });
    }



    public void cambiaColor(String primaryDark, String  primary, String background ) { //String primaryDark, String  primary, String background
        getWindow().setStatusBarColor(Color.parseColor(primaryDark));
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(primary)));
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor(background)));
        getWindow().setNavigationBarColor(Color.parseColor(primary));
    }

    //Metodo para regresar a la actividad principal
    @Override
    public void onBackPressed() {
        String titulo = "CORPOApp";
        String mensajes = "No puedes CERRAR la aplicación";
        Modales modales = new Modales(Menu_Principal.this);
        View viewLectura = modales.MostrarDialogoError(Menu_Principal.this, mensajes);
        viewLectura.findViewById(R.id.buttonAction).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //finishAffinity();
                modales.alertDialog.dismiss();
            }
        });
    }

    //-----------------Empieza los metodos para inicializar el componente de huellas---------------------
//    protected void launchGetReader()
//    {
//        Intent i = new Intent(Menu_Principal.this, GetReaderActivity.class);
//        i.putExtra("device_name", m_deviceName);
//        startActivityForResult(i, 1);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (data == null) {
//            //displayReaderNotFound();
//            return;
//        }
//        Globals.ClearLastBitmap();
//        m_deviceName = (String) data.getExtras().get("device_name");
//        if (m_deviceName.length() == 0) {
//        } else {
//            switch (requestCode) {
//                case GENERAL_ACTIVITY_RESULT:
//                    if ((m_deviceName != null) && !m_deviceName.isEmpty()) {
//                        ///Lo quite para probar m_selectedDevice.setText("Device: " + m_deviceName);
//                        try {
//                            Context applContext = getApplicationContext();
//                            m_reader = Globals.getInstance().getReader(m_deviceName, applContext);
//                            {
//                                PendingIntent mPermissionIntent;
//                                mPermissionIntent = PendingIntent.getBroadcast(applContext, 0, new Intent(ACTION_USB_PERMISSION), 0);
//                                IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
//                                applContext.registerReceiver(mUsbReceiver, filter);
//                                if (DPFPDDUsbHost.DPFPDDUsbCheckAndRequestPermissions(applContext, mPermissionIntent, m_deviceName)) {
//                                    CheckDevice();
//                                }
//                            }
//                        } catch (UareUException e1) {
//                            displayReaderNotFound();
//                        } catch (DPFPDDUsbException e) {
//                            displayReaderNotFound();
//                        }
//                    } else {
//                        displayReaderNotFound();
//                    }
//                    break;
//            }
//        }
//    }

//    private void displayReaderNotFound()
//    {
////        m_selectedDevice.setText("Device: (No Reader Selected)");
//        //setButtonsEnabled(false);
//        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
//        alertDialogBuilder.setTitle("Lector no encontrado");
//        alertDialogBuilder.setMessage("Favor de instalar el plugin").setCancelable(false).setPositiveButton("Ok",
//                new DialogInterface.OnClickListener()
//                {
//                    public void onClick(DialogInterface dialog,int id) {}
//                });
//
//        AlertDialog alertDialog = alertDialogBuilder.create();
//        if(!isFinishing()) {
//            alertDialog.show();
//        }
//    }
//
//    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver()
//    {
//        public void onReceive(Context context, Intent intent)
//        {
//            String action = intent.getAction();
//            if (ACTION_USB_PERMISSION.equals(action))
//            {
//                synchronized (this)
//                {
//                    UsbDevice device = (UsbDevice)intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
//                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false))
//                    {
//                        if(device != null)
//                        {
//                            //call method to set up device communication
//                            CheckDevice();
//                        }
//                    }
//                    else
//                    {
//                        //setButtonsEnabled(false);
//                    }
//                }
//            }
//        }
//    };
//
//    protected void CheckDevice()
//    {
//        try
//        {
//            m_reader.Open(Reader.Priority.EXCLUSIVE);
//            Reader.Capabilities cap = m_reader.GetCapabilities();
//            //setButtonsEnabled(true);
//            //setButtonsEnabled_Capture(cap.can_capture);
//            //setButtonsEnabled_Stream(cap.can_stream);
//            m_reader.Close();
//        }
//        catch (UareUException e1)
//        {
//            displayReaderNotFound();
//        }
//    }
//
//
//
//    public void redirectActivity(Activity activity, Class aClass) {
//        Intent intent = new Intent(activity, aClass);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        activity.startActivity(intent);
//    }

    @Override
    protected  void onPause(){
        super.onPause();
        closeDrawer(drawerLayout);
    }


    public void onClickMenuPrincipal(View v) {
        Intent intent;
        switch (v.getId()) {


            case R.id.btnImgVentas:
//                intent = new Intent(getApplicationContext(), ventash.class);
//                intent.putExtra("device_name", m_deviceName);
//                intent.putExtra("banderaHuella", banderaHuella);
//                startActivity(intent);
                break;
            case R.id.btnImgTickets:
//                intent = new Intent(getApplicationContext(), despachdorclave.class); //despachdorclave
//                intent.putExtra("LugarProviene", "Impresion");
//                intent.putExtra("device_name", m_deviceName);
//                intent.putExtra("banderaHuella", banderaHuella);
//                startActivity(intent);

                break;
            case R.id.btnImgMonederos:
//                intent = new Intent(getApplicationContext(), MonederosElectronicos.class);
//                intent.putExtra("device_name", m_deviceName);
//                intent.putExtra("banderaHuella", banderaHuella);
//                intent.putExtra("Enviadodesde", "menuprincipal");
//                startActivity(intent);
                //finish();
//                    CardReaderTypeEnum cardType = MAG_CARD; // aqui va la seccion de tarjeta
//                    CardFragment cf = new CardFragment();
//                    Bundle bundle = new Bundle();
//                    bundle.putSerializable("card_type", cardType);
//                    cf.setArguments(bundle);
//                    getFragmentManager().beginTransaction().replace(R.id.menu, cf).commit();
                break;
            case R.id.btnImgProductos:
//                intent = new Intent(getApplicationContext(), claveProducto.class); //claveProducto
//                intent.putExtra("LugarProviene", "Productos");
//                intent.putExtra("device_name", m_deviceName);
//                intent.putExtra("banderaHuella", banderaHuella);
//                startActivity(intent);
                //finish();
                break;
            case R.id.btnImgCortes:
//                intent = new Intent(getApplicationContext(), Clave.class);
//                intent.putExtra("LugarProviene", "Cortes");
//                intent.putExtra("device_name", m_deviceName);
//                intent.putExtra("banderaHuella", banderaHuella);
//                startActivity(intent);
                //finish();
                break;
            case R.id.btnImgPendientes:
//                intent = new Intent(getApplicationContext(), claveUPendientes.class); //claveUPendientes
//                intent.putExtra("LugarProviene", "TicketPendiente");
//                intent.putExtra("device_name", m_deviceName);
//                intent.putExtra("banderaHuella", banderaHuella);
//                startActivity(intent);
                //finish();
                break;
            case R.id.btnImgGastos:
//                intent = new Intent(getApplicationContext(), claveGastos.class);
//                intent.putExtra("LugarProviene", "Gastos");
//                intent.putExtra("device_name", m_deviceName);
//                intent.putExtra("banderaHuella", banderaHuella);
//                startActivity(intent);
                //finish();
                break;
            case R.id.btnImgReimpresiones:
//                intent = new Intent(getApplicationContext(), despachdorclave.class);
//                intent.putExtra("LugarProviene", "Reimpresion");
//                intent.putExtra("device_name", m_deviceName);
//                intent.putExtra("banderaHuella", banderaHuella);
//                startActivity(intent);
                //finish();
                break;
            case R.id.btnImgFacturacion:
//                intent = new Intent(getApplicationContext(), ClaveDespachadorAcumular.class); //ClaveDespachadorAcumular
//                intent.putExtra("LugarProviene", "Facturas");
//                intent.putExtra("device_name", m_deviceName);
//                intent.putExtra("banderaHuella", banderaHuella);
//                startActivity(intent);
                //finish();
                break;

        }
    }
}