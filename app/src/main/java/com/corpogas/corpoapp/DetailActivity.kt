package com.corpogas.corpoapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.android.volley.*
import com.android.volley.toolbox.HttpHeaderParser
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.corpogas.corpoapp.Configuracion.SQLiteBD
import com.corpogas.corpoapp.Modales.Modales
import com.corpogas.corpoapp.VentaCombustible.DiferentesFormasPago
import com.corpogas.corpoapp.VentaCombustible.FormaPagoTarjetasBancarias
import com.corpogas.corpoapp.VentaCombustible.GenerarTicketVisaAmex
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.UnsupportedEncodingException
import java.lang.Exception
import java.util.HashMap

class  DetailActivity : AppCompatActivity() {
    var bundle: Bundle? = null
    var identificador: Int = 1
    var db: SQLiteBD? = null
    var provieneeFPoDFP: String = "1"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        bundle = intent.extras
        db = SQLiteBD(applicationContext)
        this.setTitle(db!!.getNombreEstacion() + " ( EST.:" + db!!.getNumeroEstacion() + ")")

        provieneeFPoDFP = db!!.getlugarformapagodiferentes()

        if (bundle!!.getBoolean("TX_STATUS")) {
            if (bundle!!.getString("TX_JSON_RETURN") == null) {
                identificador = 2
                closeApp()
            } else {
                identificador = 1
                findViewById<TextView>(R.id.content).text = bundle!!.getString("TX_JSON_RETURN")
                if (provieneeFPoDFP.equals("1")){
                    db!!.updatePagoTarjetaResponse(bundle!!.getString("TX_JSON_RETURN").toString().replace("/"," "))
                    closeApp()
                }else{
                    db!!.updateDiferentesFormasPago(bundle!!.getString("TX_JSON_RETURN").toString().replace("/"," "),"1",db!!.getformapagoid())
                    closeApp()
                }
                logger("JSON", bundle!!.getString("TX_JSON_RETURN")!!)
            }
        } else {
            identificador = 2
            findViewById<TextView>(R.id.content).text = bundle!!.getString("TX_ERROR")
        }
        findViewById<Button>(R.id.btnGoHome).setOnClickListener {
            closeApp()
        }
    }


    fun logger(TAG: String?, message: String) {
        val maxLogSize = 2000
        for (i in 0..message.length / maxLogSize) {
            val start = i * maxLogSize
            var end = (i + 1) * maxLogSize
            end = if (end > message.length) message.length else end
            Log.d(TAG, message.substring(start, end))
        }
    }

    private fun closeApp() {
        if (identificador == 1){
            if (provieneeFPoDFP == "1") {
                db!!.updatePagoTarjetaCorrecto(1)
                startActivity(
                    Intent(
                        this,
                        FormaPagoTarjetasBancarias::class.java // Menu_Principal
                    ).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                )
                this.finish()
            }else{
                this.finish()
            }
        }else{
            db!!.updatePagoTarjetaCorrecto(2)

            if (provieneeFPoDFP == "1") {
                finish()
            }else{
                if (provieneeFPoDFP == "2") {
                    db!!.updateDiferentesFormasPago("ERrror de conexion", "0", db!!.getformapagoid())
                    finish()
                    startActivity(
                        Intent(
                            this,
                            DiferentesFormasPago::class.java
                        ).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    )
                    this.finish()

                }else{
                    finish()
                    startActivity(
                        Intent(
                            this,
                            Menu_Principal::class.java
                        ).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    )
                    this.finish()
                }
            }
        }
    }
}