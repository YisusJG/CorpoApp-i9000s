package com.corpogas.corpoapp.SmartPayment

import android.content.ComponentName
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import com.corpogas.corpoapp.R
import com.google.gson.GsonBuilder

class  VentaPagoTarjeta : AppCompatActivity() {
    private var edtAmount: EditText? = null
    private var edtTip: EditText? = null
    private var checkPrint: CheckBox? = null


    private var amount = ""
    private var tip = "0.00"
    val totalACobrar = 0.00
    private var poscionCarga = ""
    private var enviadoDesde = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_venta_pago_tarjeta)

        val formaPagoId = intent.getStringExtra("formapagoid")
        val enviadoDesde = intent.getStringExtra("Enviadodesde")
        val poscionCarga = intent.getStringExtra("posicioncarga")
        val total = intent.getStringExtra("montoencanasta")

        edtAmount = findViewById(R.id.amount)
        edtTip = findViewById(R.id.tip)
        checkPrint = findViewById(R.id.print)
        edtAmount!!.setText(total);

        edtAmount!!.addTextChangedListener(MoneyTextWatcher(edtAmount!!))
        edtTip!!.addTextChangedListener(MoneyTextWatcher(edtTip!!))


        findViewById<Button>(R.id.do_sale).setOnClickListener {
            getEdtValues()
            doSale(amount, tip)
        }

        findViewById<Button>(R.id.authenticate).setOnClickListener {
            authenticate()
        }


//        edtAmount!!.setText()
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
        intent.putExtra("from", this.resources.getString(R.string.app_name))
        intent.putExtra("json", createJson(print))
        intent.putExtra("date", date.replace("/", ""))

        startActivity(intent)
    }


    //Function to send APIKEY and get permissions and operations permitted
    private fun authenticate() {
        val intent = Intent()
        intent.putExtra("authentication", "APIKEY")
        intent.putExtra("json", createJson(false))

        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES)
        intent.component =
            ComponentName(
                SMART_PACKAGE,
                "$SMART_PACKAGE.thirdparty.broadcast.AuthenticationBroadcast"
            )
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