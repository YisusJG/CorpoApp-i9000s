package com.corpogas.corpoapp.Entities.Yena

data class YenaResponse(
    val Activa: Boolean,
    val Bloqueada: Boolean,
    val Descuentos: List<Descuento>,
    val Id: Int,
    val InsertDate: String,
    val IsDeleted: Boolean,
    val SaldoActualDinero: Double,
    val SaldoActualPuntos: Double,
    val SaldoPrevioDinero: Double,
    val SaldoPrevioPuntos: Double,
    val UpdateDate: Any
)