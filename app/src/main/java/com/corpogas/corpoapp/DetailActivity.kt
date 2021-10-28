package com.corpogas.corpoapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.corpogas.corpoapp.Configuracion.SQLiteBD
import com.corpogas.corpoapp.VentaCombustible.DiferentesFormasPago
import com.corpogas.corpoapp.VentaCombustible.FormaPagoTarjetasBancarias

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
                identificador = 1
                closeApp()
            } else {
                identificador = 2
                findViewById<TextView>(R.id.content).text = bundle!!.getString("TX_JSON_RETURN")
                logger("JSON", bundle!!.getString("TX_JSON_RETURN")!!)
            }
        } else {
            identificador = 2
            findViewById<TextView>(R.id.content).text = bundle!!.getString("TX_ERROR")
        }



        findViewById<Button>(R.id.btnGoHome).setOnClickListener {
            identificador = 2
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
            db!!.updatePagoTarjetaCorrecto(1)

            startActivity(
                Intent(
                    this,
                    FormaPagoTarjetasBancarias::class.java // Menu_Principal

                ).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            )
            this.finish()
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