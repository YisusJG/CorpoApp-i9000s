package com.corpogas.corpoapp.Fajillas

import android.content.ComponentName
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import com.corpogas.corpoapp.Configuracion.SQLiteBD
import com.corpogas.corpoapp.JsonData
import com.corpogas.corpoapp.MoneyTextWatcher
import com.corpogas.corpoapp.R
import com.google.gson.GsonBuilder

class CancelarPagoTransaccion : AppCompatActivity() {
    private var edtAmount: EditText? = null
    private var edtTip: EditText? = null
    private var edtReference: EditText? = null
    //    private var edtDate: EditText? = null
    private var checkPrint: CheckBox? = null

    private var amount = ""
    private var tip = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cancelar_pago_transaccion)
        val db = SQLiteBD(this)
        this.title = "${db.nombreEstacion} (EST.: ${db.numeroEstacion})"

        edtAmount = findViewById(R.id.amount)
        edtTip = findViewById(R.id.tip)
        edtReference = findViewById(R.id.reference)
//        edtDate = findViewById(R.id.date)
        checkPrint = findViewById(R.id.print)

        edtAmount!!.addTextChangedListener(MoneyTextWatcher(edtAmount!!))
        edtTip!!.addTextChangedListener(MoneyTextWatcher(edtTip!!))

//        findViewById<Button>(R.id.btnCobrar).setOnClickListener {
//            getEdtValues()
//            doSale(amount, tip)
//        }
//
//        findViewById<Button>(R.id.btnAutentica).setOnClickListener {
//            authenticate()
//        }

        findViewById<Button>(R.id.btnCancel).setOnClickListener {
            getEdtValues()
            doCancel(amount, tip)
        }

//        findViewById<Button>(R.id.btnConsultaTransaccion).setOnClickListener {
////            doQueryTx(edtReference!!.text.toString(), edtDate!!.text.toString())
//        }
    }

    //Function to get values from edittext
    private fun getEdtValues() {
        amount = replaceChars(edtAmount!!.text.toString())
        tip = replaceChars(edtTip!!.text.toString())
    }

    //Function to do the sale process
    private fun doSale(amount: String, tip: String) {
        callSmartComponent(amount, tip, checkPrint!!.isChecked, false, "", "")
    }

    //Function to do the cancel process
    private fun doCancel(amount: String, tip: String) {
        callSmartComponent(
            amount,
            tip,
            checkPrint!!.isChecked,
            true, // Para cancelación siempre activo
            edtReference!!.text.toString(),  //12 Digitos
            ""
        )
        finish()
    }

    //Function to get the TXN by reference and date
    private fun doQueryTx(reference: String, date: String) {
        callSmartComponent(
            "", "",
            print = false,
            cancel = false,
            reference = reference, //12 Digitos
            date = date //"YYYYMMDD"
        )
    }

    //Function before called openApp calls SmartWrapper App
    private fun callSmartComponent(
        amount: String,
        tip: String,
        print: Boolean,
        cancel: Boolean,
        reference: String,
        date: String
    ) {
        Log.d("TEXT", "$amount $tip")

        val intent = Intent()
        intent.setClassName(SMART_PACKAGE, "$SMART_PACKAGE.activities.ContainerActivity")
        intent.putExtra("amount", amount)
        intent.putExtra("cancel", cancel)
        intent.putExtra("reference", reference)
        intent.putExtra("tip", tip)
        intent.putExtra("from", "corpoapp")
        intent.putExtra("json", createJson(print))
        intent.putExtra("date", date)

        startActivity(intent)
    }


    //Function to send APIKEY and get permissions and operations permitted
    private fun authenticate() {
        val intent = Intent()
        intent.putExtra("authentication", "APIKEY")
        intent.putExtra("json", createJson(false))

        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES)
        intent.component =
            ComponentName(SMART_PACKAGE, "$SMART_PACKAGE.corpoapp.broadcast.AuthenticationBroadcast")
        sendBroadcast(intent)
    }

    private fun createJson(print: Boolean): String {
        val jsonData = JsonData()
        jsonData.activityStr = "${this.packageName}.DetailActivity"
        jsonData.packageStr = this.packageName
        jsonData.print = print

        return getJsonString(jsonData)!!
    }

    private fun getJsonString(json: JsonData): String? {
        val gson = GsonBuilder()
            .create()
        return gson.toJson(json)
    }


    private fun replaceChars(original: String): String {
        return original.replace("$", "").replace(",", "")
    }

    companion object {
        private const val SMART_PACKAGE = "com.smart.smart"
    }

}