package com.corpogas.corpoapp.SmartPayment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.corpogas.corpoapp.R

class  DetailActivity : AppCompatActivity() {
    var bundle: Bundle? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        bundle = intent.extras

        if (bundle!!.getBoolean("TX_STATUS")) {
            if (bundle!!.getString("TX_JSON_RETURN") == null) {
                closeApp()
            } else {
                findViewById<TextView>(R.id.content).text = bundle!!.getString("TX_JSON_RETURN")
                logger("JSON", bundle!!.getString("TX_JSON_RETURN")!!)
            }
        } else {
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
        startActivity(
            Intent(
                this,
                VentaPagoTarjeta::class.java
            ).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        )
        this.finish()
    }
}