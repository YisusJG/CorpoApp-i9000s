package com.corpogas.corpoapp.Yena

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.corpogas.corpoapp.Configuracion.SQLiteBD
import com.corpogas.corpoapp.Puntada.PosicionPuntadaRedimir
import com.corpogas.corpoapp.R

class ApartadosYena : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_apartados_yena)
        val data = SQLiteBD(this)
        this.title = data.nombreEstacion + " (EST: " + data.numeroEstacion + ")"

        val btnConsultaSaldo = findViewById<TextView>(R.id.boton_consultar_saldo)
        val btnAcumulacion = findViewById<TextView>(R.id.boton_acumular)
        val btnRedencion = findViewById<TextView>(R.id.boton_redimir)


        btnConsultaSaldo.setOnClickListener {
            val intent = Intent(this, PosicionPuntadaRedimir::class.java)
            intent.putExtra("lugarproviene", "Consulta Yena")
            startActivity(intent)
        }

        btnAcumulacion.setOnClickListener {
            val intent = Intent(this, PosicionPuntadaRedimir::class.java)
            intent.putExtra("lugarproviene", "Acumulacion Yena")
            startActivity(intent)
        }

        btnRedencion.setOnClickListener {
            val intent = Intent(this, PosicionPuntadaRedimir::class.java)
            intent.putExtra("lugarproviene", "Redencion Yena")
            startActivity(intent)
        }
    }
}