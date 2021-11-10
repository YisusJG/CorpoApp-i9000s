package com.corpogas.corpoapp.Cofre

import android.app.AlertDialog
import android.content.Intent
import android.device.ScanManager
import android.device.scanner.configuration.Triggering
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.InputType
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.corpogas.corpoapp.Cofre.Adapters.RVAdapterCofreFajillas
import com.corpogas.corpoapp.Cofre.Entities.EnviarFajillaCofre
import com.corpogas.corpoapp.Cofre.Entities.RecepcionFajillasNoEnCajaFuerte
import com.corpogas.corpoapp.Cofre.Entities.StatusFajilla
import com.corpogas.corpoapp.Cofre.Entities.TotalFajillaCajaFuerte
import com.corpogas.corpoapp.Configuracion.SQLiteBD
import com.corpogas.corpoapp.Entities.Classes.RespuestaApi
import com.corpogas.corpoapp.Entities.Cortes.CierreValePapel
import com.corpogas.corpoapp.Entregas.Adapters.RVAdapterValesPapel
import com.corpogas.corpoapp.Interfaces.Endpoints.EndPoints
import com.corpogas.corpoapp.Login.LoginActivity
import com.corpogas.corpoapp.Menu_Principal
import com.corpogas.corpoapp.Modales.Modales
import com.corpogas.corpoapp.Puntada.SeccionTarjeta
import com.corpogas.corpoapp.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import com.google.gson.Gson




class CofreActivity : AppCompatActivity() {
    private lateinit var txtScanner: EditText
    private var mScan: ImageView? = null
    lateinit var txtResponsable: TextView
    private var mScanManager: ScanManager? = null
    lateinit var rcvCofreFajillas: RecyclerView
    lateinit var txtCantidadCo: TextView
    lateinit var txtPrecioCo: TextView
    lateinit var txtImporteCo : TextView
    var data: SQLiteBD? = null

    private val DURACION_SPLASH = 1000
    var contadorEventos = 0

    var db: SQLiteBD? = null
    var sucursalId: Long = 0
    var ipEstacion: String? =null
    var numeroEmpleado: String? = null

    var respuestaFajillasCofre: RespuestaApi<RecepcionFajillasNoEnCajaFuerte<TotalFajillaCajaFuerte>>? = null
    var statusRespuesFajilla: RespuestaApi<List<StatusFajilla>>?=null

    internal inner class ButtonListenerPrueba : View.OnClickListener, View.OnTouchListener {
        override fun onClick(v: View) {
            LogD("ButtonListenerPrueba onClick")
        }

        override fun onTouch(v: View, event: MotionEvent): Boolean {
            if (v.id == R.id.btnScanCofre) {
                if (event.action == MotionEvent.ACTION_UP) {
                    LogD("onTouch button Up")
                    //                    mScan.setText(R.string.scan_trigger_start);
                    if (triggerMode == Triggering.HOST) {
                        stopDecode()
                    }
                }
                if (event.action == MotionEvent.ACTION_DOWN) {
                    LogD("onTouch button Down")
                    //                    mScan.setText(R.string.scan_trigger_end);
                    startDecode()
                }
            }
            return false
        }
    }

    private fun initView() {
        data = SQLiteBD(applicationContext)
        this.setTitle(data!!.getNombreEstacion() + " ( EST.:" + data!!.getNumeroEstacion() + ")")

        txtScanner = findViewById(R.id.txtScanResult)
        mScan = findViewById<View>(R.id.btnScanCofre) as ImageView
        txtResponsable = findViewById(R.id.txtResponsable)
        rcvCofreFajillas = findViewById(R.id.rcvCofreFajillas)
        txtCantidadCo = findViewById(R.id.txtCantidadCo)
        txtPrecioCo = findViewById(R.id.txtPrecioCo)
        txtImporteCo = findViewById(R.id.txtImporteCo)

        val listener = ButtonListenerPrueba()
        mScan!!.setOnTouchListener(listener)
        mScan!!.setOnClickListener(listener)

        db = SQLiteBD(applicationContext)
        ipEstacion = db!!.ipEstacion
        sucursalId = db!!.idSucursal.toLong()
        numeroEmpleado = db!!.numeroEmpleado


        val linearLayoutManager = LinearLayoutManager(applicationContext.applicationContext)

        rcvCofreFajillas.layoutManager = linearLayoutManager
        rcvCofreFajillas.setHasFixedSize(true)


    }

    private fun initScan() {
        mScanManager = ScanManager()
        var powerOn = mScanManager!!.scannerState
        if (!powerOn) {
            powerOn = mScanManager!!.openScanner()
            if (!powerOn) {
                val builder = AlertDialog.Builder(this)
                builder.setMessage("Scanner cannot be turned on!")
                builder.setPositiveButton("OK") { dialog, which -> dialog.dismiss() }
                val mAlertDialog = builder.create()
                mAlertDialog.show()
            }
        }
    }

    private val triggerMode: Triggering
        private get() = mScanManager!!.triggerMode

    private fun getlockTriggerState(): Boolean {
        return mScanManager!!.triggerLockState
    }

    private fun startDecode() {
        if (!mScanEnable) {
            LogI("startDecode ignore, Scan enable:${mScanEnable}")
            return
        }
        val lockState = getlockTriggerState()
        if (lockState) {
            LogI("startDecode ignore, Scan lockTrigger state:$lockState")
            return
        }
        if (mScanManager != null) {
            mScanManager!!.startDecode()
        }
    }

    private fun stopDecode() {
        if (!mScanEnable) {
            LogI("stopDecode ignore, Scan enable:${mScanEnable}")
            return
        }
        if (mScanManager != null) {
            mScanManager!!.stopDecode()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cofre)
        initView()
    }

    override fun onPause() {
        super.onPause()
        //        registerReceiver(false);
    }

    override fun onResume() {
        super.onResume()
        initScan()
        //        registerReceiver(true);
    }

    override fun onDestroy() {
        super.onDestroy()
    }


    override fun onKeyUp(keyCode: Int, event: KeyEvent): Boolean {
        LogD("onKeyUp, keyCode:$keyCode")
        if (keyCode >= SCAN_KEYCODE[0] && keyCode <= SCAN_KEYCODE[SCAN_KEYCODE.size - 1]) {
            val ver = ""
        }
        Handler().postDelayed({
            contadorEventos++
            if (contadorEventos == 1) {
                var datoScaner = txtScanner.text.toString()
                datoScaner= datoScaner.replaceFirst("\n", "");
                txtScanner.text.clear()
//                Toast.makeText(applicationContext, datoScaner, Toast.LENGTH_SHORT).show()
                ObtenerFajillasCofre(datoScaner)
            }
        }, DURACION_SPLASH.toLong())
        return super.onKeyUp(keyCode, event)
    }

    private fun ObtenerFajillasCofre(datoScaner: String){
        val retrofit = Retrofit.Builder()
            .baseUrl("http://$ipEstacion/CorpogasService/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val getFajillasCofre= retrofit.create(EndPoints::class.java)
        val call = getFajillasCofre.getFajillasCofre(sucursalId,numeroEmpleado,datoScaner)
        call.enqueue(object : Callback<RespuestaApi<RecepcionFajillasNoEnCajaFuerte<TotalFajillaCajaFuerte>>>{
            override fun onResponse(
                call: Call<RespuestaApi<RecepcionFajillasNoEnCajaFuerte<TotalFajillaCajaFuerte>>>,
                response: Response<RespuestaApi<RecepcionFajillasNoEnCajaFuerte<TotalFajillaCajaFuerte>>>
            ) {

                if (!response.isSuccessful) {
//                    mJsonTxtView.setText("Codigo: "+ response.code());
                    return
                }else
                {

                    respuestaFajillasCofre = response.body()
                    if(respuestaFajillasCofre?.Correcto == true){
                        mScan!!.visibility = View.INVISIBLE
                        txtScanner.visibility = View.INVISIBLE
                        txtResponsable.visibility = View.VISIBLE
                        txtCantidadCo.visibility = View.VISIBLE
                        txtPrecioCo.visibility = View.VISIBLE
                        txtImporteCo.visibility = View.VISIBLE
                        rcvCofreFajillas.visibility = View.VISIBLE
                        mScan!!.visibility = View.INVISIBLE
                        txtResponsable.text = respuestaFajillasCofre!!.objetoRespuesta.Responsable

                        var lCofreFajillas = respuestaFajillasCofre!!.objetoRespuesta.FajillasNoEntregadas
                        initializeAdapterFormasDePago(lCofreFajillas)

                    }else
                    {
                        val titulo = "AVISO"
                        val mensaje = respuestaFajillasCofre?.Mensaje!!
                        val modales = Modales(this@CofreActivity)
                        val view1 = modales.MostrarDialogoAlertaAceptar(this@CofreActivity, mensaje, titulo)
                        view1.findViewById<View>(R.id.buttonYes).setOnClickListener {
                            contadorEventos = 0
                            modales.alertDialog.dismiss()
                        }
                    }
                }


            }

            override fun onFailure(
                call: Call<RespuestaApi<RecepcionFajillasNoEnCajaFuerte<TotalFajillaCajaFuerte>>>,
                t: Throwable
            ) {
                Toast.makeText(applicationContext, t.message, Toast.LENGTH_SHORT).show()
            }
        })



    }

    private fun initializeAdapterFormasDePago(fajillasNoEntregadas: MutableList<TotalFajillaCajaFuerte>) {
        val adapter = RVAdapterCofreFajillas(fajillasNoEntregadas)

        adapter.setOnClickListener {
            var lCofreFajillas: ArrayList<EnviarFajillaCofre> = arrayListOf()
            val deleteIndex: Int = rcvCofreFajillas.getChildAdapterPosition(it)
            var idRecepcionFajilla: Long =  fajillasNoEntregadas[rcvCofreFajillas.getChildAdapterPosition(it)].Id

            var titulo = "ENTREGA"
            var mensaje = "¿Enviar fajilla al cofre?"
            val modales = Modales(this@CofreActivity)
            val viewLectura =modales.MostrarDialogoCofre(this@CofreActivity, mensaje, titulo)
            viewLectura.findViewById<View>(R.id.buttonYes).setOnClickListener {
                modales.alertDialog.dismiss()
               lCofreFajillas.add(EnviarFajillaCofre(idRecepcionFajilla))

                val retrofit = Retrofit.Builder()
                    .baseUrl("http://$ipEstacion/CorpogasService/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

//                val gson = Gson()
//                val json = gson.toJson(lCofreFajillas)
//                var vernumem= numeroEmpleado

                val getFajillasCofre= retrofit.create(EndPoints::class.java)
                val call = getFajillasCofre.postGuardaFajillasCofre(lCofreFajillas ,numeroEmpleado)
                call.enqueue(object : Callback<RespuestaApi<List<StatusFajilla>>>{
                    override fun onResponse(
                        call: Call<RespuestaApi<List<StatusFajilla>>>,
                        response: Response<RespuestaApi<List<StatusFajilla>>>
                    ) {
                        if (!response.isSuccessful) {//
                            return
                        }else
                        {
                            statusRespuesFajilla = response.body()
                            if(statusRespuesFajilla?.isCorrecto == true)
                            {
                                titulo = "CORRECTO"
                                mensaje = "Fajilla guardada correctamente"
                                val view1 = modales.MostrarDialogoCorrecto(
                                    this@CofreActivity,
                                    titulo,
                                    mensaje,
                                    "ACEPTAR"
                                )
                                view1.findViewById<View>(R.id.buttonAction).setOnClickListener {
                                    fajillasNoEntregadas.removeAt(deleteIndex)
                                    adapter.notifyItemRemoved(deleteIndex)

                                    if(fajillasNoEntregadas.size ==0){
                                        var intent = Intent(applicationContext, Menu_Principal::class.java)  //MonederosElectronicos SeccionTarjeta
                                        startActivity(intent)
                                        finish()
                                    }else
                                    {
                                        modales.alertDialog.dismiss()
                                    }

                                }

                            }else{
                                val titulo = "AVISO"
                                val mensaje = statusRespuesFajilla?.Mensaje!!
                                val modales = Modales(this@CofreActivity)
                                val view1 = modales.MostrarDialogoAlertaAceptar(this@CofreActivity, mensaje, titulo)
                                view1.findViewById<View>(R.id.buttonYes).setOnClickListener {
                                    contadorEventos = 0
                                    modales.alertDialog.dismiss()
                                }
                            }
                        }
                    }

                    override fun onFailure(call: Call<RespuestaApi<List<StatusFajilla>>>, t: Throwable) {
                        Toast.makeText(applicationContext, t.message, Toast.LENGTH_SHORT).show()
                    }
                })


//                modales.alertDialog.dismiss()
            }
            viewLectura.findViewById<View>(R.id.buttonNo)
                .setOnClickListener {

                    modales.alertDialog.dismiss()
                }
        }

        rcvCofreFajillas.adapter = adapter

    }




    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        LogD("onKeyDown, keyCode:$keyCode")
        if (keyCode >= SCAN_KEYCODE[0] && keyCode <= SCAN_KEYCODE[SCAN_KEYCODE.size - 1]) {
            val ver = ""
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun LogD(msg: String) {
        if (DEBUG) {
            Log.d(TAG, msg)
        }
    }

    private fun LogI(msg: String) {
        Log.i(TAG, msg)
    }

    companion object {
        private const val TAG = "ScanManagerDemo"
        private const val DEBUG = true
        private const val mScanEnable = true
        private val SCAN_KEYCODE = intArrayOf(520, 521, 522, 523)
    }
}
