package com.corpogas.corpoapp.Yena

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.corpogas.corpoapp.Adapters.RVAdapter
import com.corpogas.corpoapp.Configuracion.SQLiteBD
import com.corpogas.corpoapp.Entities.Classes.RecyclerViewHeaders
import com.corpogas.corpoapp.Menu_Principal
import com.corpogas.corpoapp.Puntada.PosicionPuntadaRedimir
import com.corpogas.corpoapp.Puntada.PuntadaQr
import com.corpogas.corpoapp.R
import java.util.ArrayList

class ApartadosYena : AppCompatActivity() {

    private lateinit var recyclerViewSeccionTarjeta: RecyclerView
    private lateinit var PuntadaProceso: String
    private lateinit var lrecyclerViewHeaders: ArrayList<RecyclerViewHeaders>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_apartados_yena)
        val data = SQLiteBD(this)
        this.title = data.nombreEstacion + " (EST: " + data.numeroEstacion + ")"
        initializeRecycler()
        initializeData()
        initializeAdapter()

    }

    private fun initializeRecycler() {
        recyclerViewSeccionTarjeta = findViewById(R.id.recycler_yena)
        val linearLayoutManager = LinearLayoutManager(this)
        recyclerViewSeccionTarjeta.layoutManager = linearLayoutManager
        recyclerViewSeccionTarjeta.setHasFixedSize(true)
    }

    private fun initializeData() {
        lrecyclerViewHeaders = ArrayList<RecyclerViewHeaders>()
        lrecyclerViewHeaders.add(
            RecyclerViewHeaders(
                "Yena Consulta de Saldo",
                "Consultar Saldo",
                R.drawable.yena
            )
        )
        lrecyclerViewHeaders.add(
            RecyclerViewHeaders(
                "Yena Redención",
                "Redimir",
                R.drawable.yena
            )
        )
        lrecyclerViewHeaders.add(
            RecyclerViewHeaders(
                "Yena Descuento",
                "Aplicar Descuento",
                R.drawable.yena
            )
        )
    }

    private fun initializeAdapter() {
        val adapter = RVAdapter(lrecyclerViewHeaders)
        adapter.setOnClickListener { v ->
            when (lrecyclerViewHeaders[recyclerViewSeccionTarjeta.getChildAdapterPosition(v)].titulo) {
                "Yena Consulta de Saldo" -> {
                    PuntadaProceso = "Yena Consulta de Saldo"
                    val intent = Intent(this, PosicionPuntadaRedimir::class.java)
                    intent.putExtra("lugarproviene", "Consulta Yena")
                    startActivity(intent)
                }
                "Yena Redención" -> {
                    PuntadaProceso = "Yena Redención"
                    val intent = Intent(this, PosicionPuntadaRedimir::class.java)
                    intent.putExtra("lugarproviene", "Redencion Yena")
                    startActivity(intent)
                }
                "Yena Descuento" -> {
                    val intent = Intent(this, PosicionPuntadaRedimir::class.java)
                    intent.putExtra("lugarproviene", "Descuento Yena")
                    startActivity(intent)
                }
                else -> {}
            }
        }
        recyclerViewSeccionTarjeta.adapter = adapter
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, Menu_Principal::class.java))
        finish()
    }
}