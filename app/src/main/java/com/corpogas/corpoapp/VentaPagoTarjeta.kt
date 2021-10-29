package com.corpogas.corpoapp

import android.content.ComponentName
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.android.volley.*
import com.android.volley.toolbox.HttpHeaderParser
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.corpogas.corpoapp.Configuracion.SQLiteBD
import com.corpogas.corpoapp.Modales.Modales
import com.google.gson.GsonBuilder
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.UnsupportedEncodingException
import java.lang.Exception
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.HashMap

class  VentaPagoTarjeta : AppCompatActivity() {
    private var edtAmount: TextView? = null
    private var edtTip: EditText? = null
    private var checkPrint: CheckBox? = null


    private var amount = ""
    private var tip = "0.00"
    val totalACobrar = 0.00
    private var poscionCarga = ""
    private var lugarproviene = ""
    var data: SQLiteBD? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_venta_pago_tarjeta)
        data = SQLiteBD(applicationContext)
        this.setTitle(data!!.getNombreEstacion() + " ( EST.:" + data!!.getNumeroEstacion() + ")")

        val formaPagoId = data!!.getformapagoid() //intent.getStringExtra("formapagoid")
        val lugarproviene = intent.getStringExtra("lugarProviene")
        val poscionCarga = data!!.getposcioncarga() //intent.getStringExtra("posicioncarga")
        var total:String = data!!.getmonto() //intent.getStringExtra("montoencanasta")


        //                                    tarjetaNumero = getIntent().getStringExtra("tarjetaNumero");
        val simbolos = DecimalFormatSymbols()
        simbolos.decimalSeparator = '.'
        val df = DecimalFormat("###0.00##", simbolos)
        //                                    String cantidadformato = cantidad;
        //                                    cantidadformato = df.format(Double.parseDouble(cantidadformato));
        //                                    cantidad = cantidadformato;
        //                                    String cantidadformato = cantidad;
        //                                    cantidadformato = df.format(Double.parseDouble(cantidadformato));
        //                                    cantidad = cantidadformato;
        total = "$"+ df.format(total.toDouble())


        edtAmount = findViewById(R.id.amount)
        edtTip = findViewById(R.id.tip)
        checkPrint = findViewById(R.id.print)
        edtAmount!!.setText(total);

//        edtAmount!!.addTextChangedListener(MoneyTextWatcher(edtAmount!!))
        edtTip!!.addTextChangedListener(MoneyTextWatcher(edtTip!!))


//        if (total!!.equals(0.00)){
//            imprimir()
//        }

        findViewById<Button>(R.id.do_sale).setOnClickListener {
            getEdtValues()
            doSale(amount, tip)
        }

        findViewById<Button>(R.id.authenticate).setOnClickListener {
            authenticate()
        }

//        findViewById<Button>(R.id.btnimprime).setOnClickListener {
//            imprimir();
//        }

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
        intent.putExtra("from", "corpoapp")
        intent.putExtra("json", createJson(print))
        intent.putExtra("date", date.replace("/", ""))

        startActivity(intent)
        finish()
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
                "$SMART_PACKAGE.corpoapp.broadcast.AuthenticationBroadcast"
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