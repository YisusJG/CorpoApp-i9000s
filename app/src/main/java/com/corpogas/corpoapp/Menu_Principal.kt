package com.corpogas.corpoapp

import android.Manifest
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.ImageSwitcher
import android.widget.ImageView
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.corpogas.corpoapp.ConexionInternet.ComprobarConexionInternet
import com.corpogas.corpoapp.Configuracion.SQLiteBD
import com.corpogas.corpoapp.Encriptacion.EncriptarMAC
import com.corpogas.corpoapp.Entities.Sucursales.Update
import com.corpogas.corpoapp.LecturaTarjetas.MonederosElectronicos
import com.corpogas.corpoapp.Menu_Principal
import com.corpogas.corpoapp.Modales.Modales
import com.corpogas.corpoapp.Request.Interfaces.EndPoints
import com.google.android.material.snackbar.Snackbar
import com.szzcs.corpoapp.ActualizadorAPP.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Menu_Principal : AppCompatActivity() {
    var drawerLayout: DrawerLayout? = null
    lateinit var bar: ProgressDialog
    var list: ListView? = null
    lateinit var downloadController:DownloadController

    //-------Variables necesarias para ininicalizar el lector de huellas
    private val GENERAL_ACTIVITY_RESULT = 1
    private val m_deviceName = ""

    //    private String m_versionName = "";
    //    private final String tag = "UareUSampleJava";
    //    Reader m_reader;
    var banderaHuella = ""
    private val imgFoto: ImageSwitcher? = null
    var imagen: ImageView? = null
    var ImageDisplay: Boolean? = null
    var applicationUpdate: Update? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu__principal)
        val data = SQLiteBD(this)
        this.title = data.nombreEsatcion
        drawerLayout = findViewById(R.id.drawer_layout)
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
        val mac = EncriptarMAC()
        val mac2 = mac.macAddr
        val macmd5 = mac.getMD5(mac2)

        //carga el color de acuerdo al tipoEstacion
        val primaryDark = "#1C0D02"
        val background = "#FFFFFF"
        var primary = ""
        val tipo = data.tipoEstacion
        if (tipo == "CORPOGAS") {
            primary = "#003300"
        } else {
            if (tipo == "GULF") {
                primary = "#FF9800"
            } else {
                if (tipo == "PEMEX") {
                    primary = "#003300"
                }
            }
        }
        cambiaColor(primaryDark, primary, background)
        imagen = findViewById<View>(R.id.imgFoto) as ImageView
        imagen!!.setImageResource(R.drawable.gasolinera)
        ImageDisplay = true
        BuscarActualizacion
    }


    private val BuscarActualizacion: Unit
        private get() {
            val data = SQLiteBD(applicationContext)
            val retrofit = Retrofit.Builder()
                    .baseUrl("http://"+data.ipEstacion+"/CorpogasService_Entities/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            val actualizaApp = retrofit.create(EndPoints::class.java)
            val call = actualizaApp.getActializaApp(data.idSucursal)
            call.enqueue(object : Callback<Update?> {
                override fun onResponse(call: Call<Update?>, response: Response<Update?>) {
                    var version = ""
                    var fileName = ""
                    var deviceModel =""
                    if (!response.isSuccessful) {
//                    mJsonTxtView.setText("Codigo: "+ response.code());
                        return
                    }
                    applicationUpdate = response.body()
                    for (item in applicationUpdate!!.UpdateDetails) {
                        version = item.getVersion()
                        fileName = item.getFileName()
                        deviceModel = item.getDeviceModel()
                    }
//                    var actualizaDatosApp = data!!.updateVersionAPP("1.0","CorpoApp.apk",deviceModel)  //descomentar si deseas actualizar la tabla para hacer la prueba de actualizacion
                    if (data!!.versionApk != version && data!!.fileNameApk != fileName) {
                        val apkUrl = "http://sso.corpogas.com.mx/StationUpdates/I9000S/"+fileName
                        downloadController = DownloadController(this@Menu_Principal, apkUrl, fileName)


                        val titulo = "CORPOGAS"
                        val mensajes = "Hay una actualizacion disponible"

                        val modales = Modales(this@Menu_Principal)
                        val viewActualizarApp = modales.MostrarDialogoCorrecto(this@Menu_Principal, titulo, mensajes, "ACTUALIZAR")
                        viewActualizarApp.findViewById<View>(R.id.buttonAction).setOnClickListener {
                            modales.alertDialog.dismiss()
                            bar = ProgressDialog(this@Menu_Principal)
                            bar.setTitle("CORPOAPP")
                            bar.setMessage("ACTUALIZANDO APLICACION... ")
                            bar.setIcon(R.drawable.logocorpoapp)
                            bar.setCancelable(false)
                            bar.show()

                            var actualizaDatosApp = data!!.updateVersionAPP(version,fileName,deviceModel)

                            checkStoragePermission()

                        }
                    } else {
                        val mensaje = "No hay actualizacions disponibles"
                    }
                }

                override fun onFailure(call: Call<Update?>, t: Throwable) {
                    Toast.makeText(applicationContext, t.message, Toast.LENGTH_SHORT).show()
                }
            })
        }

    private fun formaAutenticacion() {
        val mensaje = "Qué forma de Autenticación desea utilizar?"
        val modales = Modales(this@Menu_Principal)
        val viewLectura = modales.MostrarDialogoAlerta(this@Menu_Principal, mensaje, "Huella", "Clave")
        viewLectura.findViewById<View>(R.id.buttonYes).setOnClickListener {
            modales.alertDialog.dismiss()
            banderaHuella = "true"
            //                PM.powerOn();
//                launchGetReader();
        }
        viewLectura.findViewById<View>(R.id.buttonNo).setOnClickListener { //Si no se terclea nada envia mensaje de teclear contraseña
            banderaHuella = "false"
            modales.alertDialog.dismiss()
        }
    }

    fun ClicMenu(view: View?) {
        openDrawer(drawerLayout)
    }

    private fun openDrawer(drawerLayout: DrawerLayout?) {
        drawerLayout!!.openDrawer(GravityCompat.START)
    }

    fun ClickLogo(view: View?) {
        closeDrawer(drawerLayout)
    }

    fun ClickHome(view: View?) {
        recreate()
    }

    fun ClickHuella(view: View?) {
        formaAutenticacion()
    }

    fun ClickCerrarSesion(view: View?) {
//        Toast.makeText(this, "Cerrar Sesión", Toast.LENGTH_SHORT).show();
//        recreate();
        if (!ComprobarConexionInternet.compruebaConexion(this)) {
            Toast.makeText(baseContext, "Necesaria conexión a internet ", Toast.LENGTH_SHORT).show()
        }
    }

    fun ClickSalir(view: View?) {
        LogOut(this)
    }

    fun ClickCambiar(view: View?) {
        ImageDisplay = if (ImageDisplay!!) {
            imagen!!.setImageResource(R.drawable.corpogas1)
            false
        } else {
            imagen!!.setImageResource(R.drawable.gasolinera)
            true
        }
    }

    fun ClickDashboard(view: View?) {
//        redirectActivity(this, NuevaClas.class);
    }

    fun cargarImagen() {
//        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//
//        intent.setType("image/");
//        startActivityForResult(intent.createChooser(intent, "Seleccione la Aplicación"), 10);
        imagen!!.setImageResource(R.drawable.gasolinera)
    }

    fun LogOut(mainActivity: Menu_Principal) {
        val mensaje = "Estas seguro de Salir de la aplicación ?"
        val modales = Modales(this@Menu_Principal)
        val viewLectura = modales.MostrarDialogoAlerta(this@Menu_Principal, mensaje, "SI", "NO")
        viewLectura.findViewById<View>(R.id.buttonYes).setOnClickListener {
            mainActivity.finishAffinity()
            System.exit(0)
            modales.alertDialog.dismiss()
        }
        viewLectura.findViewById<View>(R.id.buttonNo).setOnClickListener { //Si no se terclea nada envia mensaje de teclear contraseña
            modales.alertDialog.dismiss()
        }
    }

    fun cambiaColor(primaryDark: String?, primary: String?, background: String?) { //String primaryDark, String  primary, String background
        window.statusBarColor = Color.parseColor(primaryDark)
        supportActionBar!!.setBackgroundDrawable(ColorDrawable(Color.parseColor(primary)))
        window.setBackgroundDrawable(ColorDrawable(Color.parseColor(background)))
        window.navigationBarColor = Color.parseColor(primary)
    }

    //Metodo para regresar a la actividad principal
    override fun onBackPressed() {
        val titulo = "CORPOApp"
        val mensajes = "No puedes CERRAR la aplicación"
        val modales = Modales(this@Menu_Principal)
        val viewLectura = modales.MostrarDialogoError(this@Menu_Principal, mensajes)
        viewLectura.findViewById<View>(R.id.buttonAction).setOnClickListener { //finishAffinity();
            modales.alertDialog.dismiss()
        }
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
    override fun onPause() {
        super.onPause()
        closeDrawer(drawerLayout)
    }

    fun onClickMenuPrincipal(v: View) {
        var intent: Intent
        when (v.id) {
            R.id.btnImgVentas -> {
            }
            R.id.btnImgTickets -> {
            }
            R.id.btnImgMonederos -> {
                intent = Intent(applicationContext, MonederosElectronicos::class.java)
                intent.putExtra("Enviadodesde", "menuprincipal")
                startActivity(intent)

            }
            R.id.btnImgProductos -> {
            }
            R.id.btnImgCortes -> {
            }
            R.id.btnImgPendientes -> {
            }
            R.id.btnImgGastos -> {
            }
            R.id.btnImgReimpresiones -> {
            }
            R.id.btnImgFacturacion -> {
                intent = Intent(applicationContext, PruebasEndPoint::class.java)
                startActivity(intent)
            }
        }
    }

    companion object {
        private const val PERMISSION_REQUEST_STORAGE = 0
        private const val ACTION_USB_PERMISSION = "com.digitalpersona.uareu.dpfpddusbhost.USB_PERMISSION"
        fun closeDrawer(drawerLayout: DrawerLayout?) {
            //Close drawer Layout Check condition
            if (drawerLayout!!.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START)
            }
        }
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_STORAGE) {
            // Request for camera permission.
            if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // start downloading
                downloadController.enqueueDownload()
            } else {
                // Permission request was denied.
                drawerLayout!!.showSnackbar(R.string.storage_permission_denied, Snackbar.LENGTH_SHORT)
            }
        }
    }


    private fun checkStoragePermission() {
        // Check if the storage permission has been granted
        if (checkSelfPermissionCompat(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED
        ) {
            // start downloading
            downloadController.enqueueDownload()
        } else {
            // Permission is missing and must be requested.
            requestStoragePermission()
        }
    }


    private fun requestStoragePermission() {

        if (shouldShowRequestPermissionRationaleCompat(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            drawerLayout!!.showSnackbar(
                    R.string.storage_access_required,
                    Snackbar.LENGTH_INDEFINITE, R.string.ok
            ) {
                requestPermissionsCompat(
                        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        PERMISSION_REQUEST_STORAGE
                )
            }

        } else {
            requestPermissionsCompat(
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    PERMISSION_REQUEST_STORAGE
            )
        }
    }
}