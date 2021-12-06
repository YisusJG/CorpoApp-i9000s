package com.corpogas.corpoapp.Yena

import android.annotation.SuppressLint
import android.content.Intent
import android.device.scanner.configuration.Triggering
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.corpogas.corpoapp.Configuracion.SQLiteBD
import com.corpogas.corpoapp.Entities.Classes.RespuestaApi
import com.corpogas.corpoapp.Entities.Estaciones.EndPointYena
import com.corpogas.corpoapp.Entities.Yena.YenaResponse
import com.corpogas.corpoapp.Interfaces.Endpoints.EndPoints
import com.corpogas.corpoapp.Menu_Principal
import com.corpogas.corpoapp.Modales.Modales
import com.corpogas.corpoapp.Puntada.ProductosARedimir
import com.corpogas.corpoapp.R
import com.corpogas.corpoapp.ScanManagerProvides
import com.corpogas.corpoapp.Service.MagReadService
import com.corpogas.corpoapp.VentaCombustible.DiferentesFormasPago
import com.corpogas.corpoapp.VentaCombustible.ImprimePuntada
import com.corpogas.corpoapp.VentaCombustible.IniciaVentas
import com.corpogas.corpoapp.VentaPagoTarjeta
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import kotlin.properties.Delegates

class LecturayEscaneo : AppCompatActivity() {
    var model: String? = null

    private var mReadService: MagReadService? = null
    private lateinit var scanManagerProvides: ScanManagerProvides

    private lateinit var db: SQLiteBD
    private lateinit var lugarProviene: String
    private lateinit var posicionDeCarga: String
    private var posicionCarga by Delegates.notNull<Long>()
    private var numEmpleado by Delegates.notNull<Long>()
    private var sucursalId by Delegates.notNull<Long>()
    private var result = ""
    private val formatoCifras = DecimalFormat("#,##0.00##")

    // data from FormasPagoReordenado
    private lateinit var banderaHuella: String
    private lateinit var numeroEmpleado: String
    private lateinit var idOperativa: String
    private lateinit var formaPagoId: String
    private lateinit var nombrePago: String
    private lateinit var nombreCompletoOperacion: String
    private var montoCanasta by Delegates.notNull<Double>()
    private lateinit var posicionCargaId: String
    private lateinit var tipoTarjeta: String
    private lateinit var pagoConYena: String
    private lateinit var idusuario: String

    // data from PosicionPuntadaRedimir
    private lateinit var posCarga: String
    private lateinit var estacionJarreo: String
    private var posicionCargaNumInterno by Delegates.notNull<Long>()


    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lecturay_escaneo)
        model = Build.MODEL
        val data = SQLiteBD(this)
        val btnEscanear = findViewById<Button>(R.id.button_escanear)
        this.title = data.nombreEstacion + " (EST: " + data.numeroEstacion + ")"

        Init()

        if (lugarProviene == "Acumulacion Yena" || lugarProviene == "Redencion Yena" || lugarProviene == "descuentoYena") {
            btnEscanear.visibility = View.GONE
            findViewById<TextView>(R.id.text_lectura_y_escaneo).text = "Desliza la Tarjeta"
        }

        if (model == "i9000S") {
            mReadService = MagReadService(this, mHandler)
            scanManagerProvides = ScanManagerProvides()
            btnEscanear.setOnTouchListener { view, motionEvent ->
                if (view.id == R.id.button_escanear) {
                    if (motionEvent.action == MotionEvent.ACTION_UP) {
                        btnEscanear.text = "Escanear"
                        if (scanManagerProvides.triggerMode == Triggering.HOST) {
                            scanManagerProvides.stopDecode()
                        }
                    }
                    if (motionEvent.action == MotionEvent.ACTION_DOWN) {
                        btnEscanear.text = "Escaneando"
                        scanManagerProvides.startDecode()
                    }
                }
                false
            }
        } else {
            findViewById<TextView>(R.id.text_lectura_y_escaneo).text = "Escanear QR o Código de barras"
            btnEscanear.setOnClickListener(View.OnClickListener {
                val integrator = IntentIntegrator(this)
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES)
                integrator.setPrompt("Lector - CDP")
                integrator.setCameraId(0)
                integrator.setBeepEnabled(true)
                integrator.setBarcodeImageEnabled(true)
                integrator.initiateScan()
            })
        }
    }

    private fun Init() {
        idusuario = intent.getStringExtra("idusuario").toString()
        // data from FormasPagoReordenada.java
        banderaHuella = intent.getStringExtra("banderaHuella").toString();
        //        enviadoDesde = intent.getStringExtra("Enviadodesde").toString();
        numeroEmpleado = intent.getStringExtra("numeroEmpleadoA").toString();
        idOperativa = intent.getStringExtra("idoperativa").toString();
        formaPagoId = intent.getStringExtra("formapagoid").toString();
        nombrePago = intent.getStringExtra("NombrePago").toString();
        nombreCompletoOperacion = intent.getStringExtra("NombreCompleto").toString();
        montoCanasta = intent.getDoubleExtra("montoenlacanasta", 0.0);
        posicionCargaId = intent.getStringExtra("posicioncargaid").toString();
        tipoTarjeta = intent.getStringExtra("tipoTarjeta").toString();
        pagoConYena = intent.getStringExtra("pagoconyena").toString();

        lugarProviene = intent.getStringExtra("lugarProviene").toString()
        posicionCarga = intent.getLongExtra("pocioncargaid", 0)
        posicionDeCarga = intent.getLongExtra("pocioncargaid", 0).toString()
        db = SQLiteBD(this)
        sucursalId = db.idSucursal.toLong()
        numEmpleado = intent.getLongExtra("numeroEmpleado", 0)

        posCarga = intent.getStringExtra("posicionCarga").toString()
        estacionJarreo = intent.getStringExtra("estacionjarreo").toString()
        posicionCargaNumInterno = intent.getLongExtra("pocioncarganumerointerno", 0)
    }

    override fun onPause() {
        if (model == "i9000S") {
            mReadService!!.stop()
        }
        super.onPause()
    }

    override fun onResume() {
        if (model == "i9000S") {
            mReadService!!.start()
            scanManagerProvides.initScan(this)
        }
        super.onResume()
    }

    override fun onActivityResult(requestCode: Int, resulCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resulCode, data)
        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(applicationContext, "Lectura Cancelada", Toast.LENGTH_SHORT).show()
            } else {
                if (lugarProviene == "Consulta Yena") {
                    getSaldo(result.contents)
                }
//                else if (lugarProviene == "descuentoYena") {
//                    getSaldoparaAcumular(result.contents)
//                }
            }
        } else {
            super.onActivityResult(requestCode, resulCode, data)
        }
    }

    override fun dispatchKeyEvent(event: KeyEvent?): Boolean {
        if (event!!.action == KeyEvent.ACTION_DOWN && event.keyCode != KeyEvent.KEYCODE_ENTER) { //Not Adding ENTER_KEY to barcode String
            val pressedKey = event.unicodeChar.toChar()
            result += pressedKey
        }
        if (event.action == KeyEvent.ACTION_DOWN && event.keyCode == KeyEvent.KEYCODE_ENTER) {  //Any method handling the data
            if (lugarProviene == "Consulta Yena") {
                getSaldo(result)
            }
//            else if (lugarProviene == "descuentoYena") {
//                getSaldoparaAcumular(result)
//            }
            result = ""
        }
        return super.dispatchKeyEvent(event);
    }

    private val mHandler: Handler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                MagReadService.MESSAGE_READ_MAG -> {
                    val tracks = msg.data.getString(MagReadService.CARD_TRACK1)
                    val strInfo = msg.data.getStringArrayList(MagReadService.CARD_TRACKS)
                    val tk1 = strInfo!![0]
                    val tk2 = strInfo[1]
                    val tk3 = strInfo[2]
                    if (tracks != "") {
                        beep(tk1, tk2, tk3)
                    }
                }
                MagReadService.MESSAGE_OPEN_MAG -> {

                }
                MagReadService.MESSAGE_CHECK_FAILE -> {

                }
                MagReadService.MESSAGE_CHECK_OK -> {

                }
            }
        }
    }

    private fun beep(tk1: String, tk2: String, tk3: String) {
        val tg = ToneGenerator(AudioManager.STREAM_MUSIC, ToneGenerator.MAX_VOLUME)
        tg.startTone(ToneGenerator.TONE_CDMA_NETWORK_CALLWAITING)
        when (lugarProviene) {
            "Consulta Yena" -> {
                getSaldo(tk2)
            }
            "Acumulacion Yena" -> {
                sendToAcumulacion(tk1, tk2, tk3)
            }
            "Redencion Yena" -> {
                sendToRedencion(tk1, tk2, tk3)
            }
            "descuentoYena" -> {
                getSaldoparaDesc(tk2, tk1)
            }
        }
    }

    private fun sendToRedencion(claveTarjeta: String, numeroTarjeta: String, tk3: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://" + db.ipEstacion + "/CorpogasService/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val yenaSaldo = retrofit.create(EndPoints::class.java)
        val call = yenaSaldo.postConsultaYena(EndPointYena(sucursalId, numEmpleado, numeroTarjeta))
        call.enqueue(object : Callback<RespuestaApi<YenaResponse>> {
            override fun onResponse(
                call: Call<RespuestaApi<YenaResponse>>,
                response: Response<RespuestaApi<YenaResponse>>
            ) {
                if (!response.isSuccessful) {
                    return
                } else {
                    val yenaResponse = response.body()
                    if (yenaResponse!!.isCorrecto) {
                        val objeto = yenaResponse.objetoRespuesta
                        val saldo = objeto.SaldoActualDinero.toString()

                        val intent = Intent(this@LecturayEscaneo, ProductosARedimir::class.java)
                        intent.putExtra("lugarproviene", lugarProviene)
                        intent.putExtra("posicionCarga", posicionCarga)
                        intent.putExtra("pos", posicionDeCarga)
                        intent.putExtra("saldo", saldo)
                        intent.putExtra("track1", claveTarjeta)
                        intent.putExtra("track2", numeroTarjeta)
                        intent.putExtra("track3", tk3)
                        startActivity(intent)
                    } else {
                        val titulo = "AVISO"
                        val mensaje = yenaResponse.mensaje
                        val modales = Modales(this@LecturayEscaneo)
                        val viewLectura = modales.MostrarDialogoAlertaAceptar(
                            this@LecturayEscaneo,
                            mensaje,
                            titulo
                        )
                        viewLectura.findViewById<View>(R.id.buttonYes).setOnClickListener {
                            modales.alertDialog.dismiss()
                            startActivity(Intent(this@LecturayEscaneo, ApartadosYena::class.java))
                        }
                    }
                }
            }

            override fun onFailure(call: Call<RespuestaApi<YenaResponse>>, t: Throwable) {
                Toast.makeText(this@LecturayEscaneo, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun sendToAcumulacion(claveTarjeta: String, numeroTarjeta: String, tk3: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://" + db.ipEstacion + "/CorpogasService/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val yenaSaldo = retrofit.create(EndPoints::class.java)
        val call = yenaSaldo.postAcumulaPuntos(EndPointYena(sucursalId, numeroEmpleado.toLong(), numeroTarjeta, claveTarjeta, posicionCargaId.toLong()))
        call.enqueue(object : Callback<RespuestaApi<Boolean>> {
            override fun onResponse(call: Call<RespuestaApi<Boolean>>, response: Response<RespuestaApi<Boolean>>) {
                if (!response.isSuccessful) {
                    return
                } else {
                    val yenaResponse = response.body()
                    if (yenaResponse!!.isCorrecto) {
                        val mensaje = yenaResponse.mensaje
                        if (yenaResponse.objetoRespuesta == true) {
                            if (lugarProviene == "Acumulacion Yena") {
                                if (tipoTarjeta == "Puntadaformapago") {
                                    val intent = Intent(this@LecturayEscaneo, DiferentesFormasPago::class.java)
                                    intent.putExtra("banderaHuella", banderaHuella)
                                    intent.putExtra("Enviadodesde", "Monedero")
//                                  intent.putExtra("idusuario", idusuario)
                                    intent.putExtra("posicioncarga", posicionCargaId)
                                    intent.putExtra("claveusuario", numeroEmpleado)
                                    intent.putExtra("idoperativa", idOperativa)
                                    intent.putExtra("formapagoid", formaPagoId)
                                    intent.putExtra("NombreCompleto", nombreCompletoOperacion)
                                    intent.putExtra("montoencanasta", montoCanasta)
                                    intent.putExtra("saldoYena", "0")
                                    intent.putExtra("pagoconpuntada", pagoConYena)
                                    startActivity(intent)
                                } else if (formaPagoId == "3" || formaPagoId == "5" || formaPagoId == "13") {
                                    val simbolos = DecimalFormatSymbols()
                                    simbolos.decimalSeparator = '.'
                                    val df = DecimalFormat("#,###.00", simbolos)
                                    df.maximumFractionDigits = 2
                                    db.writableDatabase.delete("PagoTarjeta", null, null)
                                    db.close()
                                    db.InsertarDatosPagoTarjeta(
                                        "1",
                                        posicionCargaId,
                                        formaPagoId,
                                        montoCanasta.toString(),
                                        "0",
                                        "1",
                                        "0",
                                        "",
                                        "",
                                        "",
                                        montoCanasta.toString(), ""
                                    )
                                    val intentVisa = Intent(
                                        this@LecturayEscaneo,
                                        VentaPagoTarjeta::class.java
                                    ) //DiferentesFormasPagoPuntada
                                    intentVisa.putExtra("lugarProviene", "formaspago")
                                    intentVisa.putExtra("posicioncarga", posicionCargaId)
                                    intentVisa.putExtra("formapagoid", formaPagoId)
//                                intentVisa.putExtra("montoencanasta", "$" + df.format(montoCanasta))
                                    intentVisa.putExtra("montoencanasta", montoCanasta)
                                    intentVisa.putExtra("numeroTarjeta", "")
                                    startActivity(intentVisa)
                                } else {
                                    val intente =
                                        Intent(this@LecturayEscaneo, ImprimePuntada::class.java)
                                    intente.putExtra("posicioncarga", posicionCargaId)
                                    intente.putExtra("idoperativa", idOperativa)
                                    intente.putExtra("idformapago", formaPagoId)
                                    intente.putExtra("nombrepago", nombrePago)
                                    intente.putExtra("montoencanasta", montoCanasta)
                                    intente.putExtra("lugarProviene", "FormasPago")
                                    intente.putExtra("lugarProviene2", "Acumulacion Yena");
                                    startActivity(intente)
                                }
                            } else {
                                val intente = Intent(this@LecturayEscaneo, ImprimePuntada::class.java)
                                intente.putExtra("posicioncarga", posicionCargaId)
                                intente.putExtra("idoperativa", idOperativa)
                                intente.putExtra("idformapago", formaPagoId)
                                intente.putExtra("nombrepago", nombrePago)
                                intente.putExtra("montoencanasta", 0.0)
                                intente.putExtra("lugarProviene", "DiferentesFormasPago")
                                intente.putExtra("lugarProviene2", "Acumulacion Yena");
                                startActivity(intente)
                            }
                        } else {
                            try {
                                val titulo = "AVISO"
                                val modales = Modales(this@LecturayEscaneo)
                                val view1 = modales.MostrarDialogoAlertaAceptar(
                                    this@LecturayEscaneo,
                                    mensaje,
                                    titulo
                                )
                                view1.findViewById<View>(R.id.buttonYes).setOnClickListener {
                                    modales.alertDialog.dismiss()
                                    val intente = Intent(this@LecturayEscaneo, Menu_Principal::class.java)
                                    startActivity(intente)
                                }
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }
                }
            }

            override fun onFailure(call: Call<RespuestaApi<Boolean>>, t: Throwable) {
                Toast.makeText(this@LecturayEscaneo, t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun getSaldo(numeroTarjeta: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://" + db.ipEstacion + "/CorpogasService/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val yenaSaldo = retrofit.create(EndPoints::class.java)
        val call = yenaSaldo.postConsultaYena(EndPointYena(sucursalId, numEmpleado, numeroTarjeta))
        call.enqueue(object : Callback<RespuestaApi<YenaResponse>> {
            override fun onResponse(
                call: Call<RespuestaApi<YenaResponse>>,
                response: Response<RespuestaApi<YenaResponse>>
            ) {
                if (!response.isSuccessful) {
                    return
                } else {
                    val yenaResponse = response.body()
                    if (yenaResponse!!.isCorrecto) {
                        val objeto = yenaResponse.objetoRespuesta
                        val titulo = "Detalle Tarjeta Yena"
                        val mensaje = objeto.SaldoActualDinero
                        val listaDescuento = objeto.Descuentos
                        val list = mutableListOf<Double>()
                        for (lista in listaDescuento) {
                            list.add(lista.ImporteDescuento)
                        }
                        if (list.isNotEmpty()) {
                            val descuentos =
                                "Descuento Magna: $ ${formatoCifras.format(list[0])} \n Descuento Premium: $ ${
                                    formatoCifras.format(list[1])
                                } \n Descuento Diesel: $ ${formatoCifras.format(list[2])}"
                            val modales = Modales(this@LecturayEscaneo)
                            val viewLectura = modales.MostrarDialogoCorrectoYena(
                                this@LecturayEscaneo,
                                titulo,
                                "Saldo Disponible: $ ${formatoCifras.format(mensaje)}",
                                descuentos,
                                "Aceptar"
                            )
                            viewLectura.findViewById<View>(R.id.buttonAction).setOnClickListener {
                                modales.alertDialog.dismiss()
                                startActivity(
                                    Intent(
                                        this@LecturayEscaneo,
                                        ApartadosYena::class.java
                                    )
                                )
                            }
                        } else {
                            val descuentos = ""
                            val modales = Modales(this@LecturayEscaneo)
                            val viewLectura = modales.MostrarDialogoCorrectoYena(
                                this@LecturayEscaneo,
                                titulo,
                                "Saldo Disponible: $ ${formatoCifras.format(mensaje)}",
                                descuentos,
                                "Aceptar"
                            )
                            viewLectura.findViewById<View>(R.id.buttonAction).setOnClickListener {
                                modales.alertDialog.dismiss()
                                startActivity(
                                    Intent(
                                        this@LecturayEscaneo,
                                        ApartadosYena::class.java
                                    )
                                )
                            }
                        }
                    } else {
                        val titulo = "AVISO"
                        val mensaje = yenaResponse.mensaje

                        val modales = Modales(this@LecturayEscaneo)
                        val viewLectura = modales.MostrarDialogoAlertaAceptar(
                            this@LecturayEscaneo,
                            mensaje,
                            titulo
                        )
                        viewLectura.findViewById<View>(R.id.buttonYes).setOnClickListener {
                            modales.alertDialog.dismiss()
                            startActivity(Intent(this@LecturayEscaneo, ApartadosYena::class.java))
                        }
                    }
                }
            }

            override fun onFailure(call: Call<RespuestaApi<YenaResponse>>, t: Throwable) {
                Toast.makeText(this@LecturayEscaneo, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getSaldoparaDesc(numeroTarjeta: String, claveTarjeta: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://" + db.ipEstacion + "/CorpogasService/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val yenaSaldo = retrofit.create(EndPoints::class.java)
        val call = yenaSaldo.postConsultaYena(EndPointYena(sucursalId, numEmpleado, numeroTarjeta))
        call.enqueue(object : Callback<RespuestaApi<YenaResponse>> {
            override fun onResponse(
                call: Call<RespuestaApi<YenaResponse>>,
                response: Response<RespuestaApi<YenaResponse>>
            ) {
                if (!response.isSuccessful) {
                    return
                } else {
                    val yenaResponse = response.body()
                    if (yenaResponse!!.isCorrecto) {
                        val objeto = yenaResponse.objetoRespuesta
                        val titulo = "Detalle Tarjeta Yena"
                        val mensaje = objeto.SaldoActualDinero
                        val listaDescuento = objeto.Descuentos
                        val list = mutableListOf<Double>()
                        for (lista in listaDescuento) {
                            list.add(lista.ImporteDescuento)
                        }
                        if (list.isNotEmpty()) {
                            val descuentos =
                                "Descuento Magna: $ ${formatoCifras.format(list[0])} \n Descuento Premium: $ ${
                                    formatoCifras.format(list[1])
                                } \n Descuento Diesel: $ ${formatoCifras.format(list[2])}"
                            val modales = Modales(this@LecturayEscaneo)
                            val viewLectura = modales.MostrarDialogoCorrectoYena(
                                this@LecturayEscaneo,
                                titulo,
                                "Saldo Disponible: $ ${formatoCifras.format(mensaje)}",
                                descuentos,
                                "Aceptar"
                            )
                            viewLectura.findViewById<View>(R.id.buttonAction).setOnClickListener {
                                modales.alertDialog.dismiss()
                                val intent = Intent(this@LecturayEscaneo, IniciaVentas::class.java)
                                intent.putExtra("combustible", "claveProducto")
                                intent.putExtra("posicionCarga", posCarga)
                                intent.putExtra("estacionjarreo", estacionJarreo)
                                intent.putExtra("claveProducto", "claveProducto")
                                intent.putExtra("precioProducto", "precio")
                                intent.putExtra("despacholibre", "no")
                                intent.putExtra("numeroTarjeta", numeroTarjeta)
                                intent.putExtra("claveTarjeta", claveTarjeta)
                                intent.putExtra("descuentoMagna", list[0])
                                intent.putExtra("descuentoPremium", list[1])
                                intent.putExtra("descuentoDiesel", list[2])
                                intent.putExtra("lugarProviene", lugarProviene)
                                intent.putExtra("pocioncargaid", posicionCargaNumInterno)
                                startActivity(intent)
                            }
                        } else {
                            val descuentos = ""
                            val modales = Modales(this@LecturayEscaneo)
                            val viewLectura = modales.MostrarDialogoCorrectoYena(
                                this@LecturayEscaneo,
                                titulo,
                                "Saldo Disponible: $ ${formatoCifras.format(mensaje)}",
                                descuentos,
                                "Aceptar"
                            )
                            viewLectura.findViewById<View>(R.id.buttonAction).setOnClickListener {
                                modales.alertDialog.dismiss()
                                val intent = Intent(applicationContext, IniciaVentas::class.java)
                                intent.putExtra("combustible", "claveProducto")
                                intent.putExtra("posicionCarga", posCarga)
                                intent.putExtra("estacionjarreo", estacionJarreo)
                                intent.putExtra("claveProducto", "claveProducto")
                                intent.putExtra("precioProducto", "precio")
                                intent.putExtra("despacholibre", "no")
                                intent.putExtra("numeroTarjeta", numeroTarjeta)
//                                intent.putExtra("descuentoMagna", list[0])
//                                intent.putExtra("descuentoPremium", list[1])
//                                intent.putExtra("descuentoDiesel", list[2])
                                intent.putExtra("lugarProviene", lugarProviene)
                                intent.putExtra("pocioncargaid", posicionCargaNumInterno)
                                startActivity(intent)
                            }
                        }
                    } else {
                        val titulo = "AVISO"
                        val mensaje = yenaResponse.mensaje
                        val modales = Modales(this@LecturayEscaneo)
                        val viewLectura = modales.MostrarDialogoAlertaAceptar(
                            this@LecturayEscaneo,
                            mensaje,
                            titulo
                        )
                        viewLectura.findViewById<View>(R.id.buttonYes).setOnClickListener {
                            modales.alertDialog.dismiss()
                            startActivity(Intent(this@LecturayEscaneo, ApartadosYena::class.java))
                        }
                    }
                }
            }

            override fun onFailure(call: Call<RespuestaApi<YenaResponse>>, t: Throwable) {
                Toast.makeText(this@LecturayEscaneo, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onBackPressed() {
        startActivity(Intent(this, Menu_Principal::class.java))
        finish()
    }
}