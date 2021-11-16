package com.corpogas.corpoapp.Entities.Estaciones

import com.corpogas.corpoapp.Entities.Common.ProductoTarjetero

class EndPointYena(val SucursalId: Long, val NumeroEmpleado: Long, val NumeroTarjeta: String, var ClaveTarjeta: String? = null,
                   var PosicionCargaId: Long? = null, var Productos: List<ProductoTarjetero>? = null)