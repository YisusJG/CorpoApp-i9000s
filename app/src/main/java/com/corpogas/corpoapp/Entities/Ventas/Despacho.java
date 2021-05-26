package com.corpogas.corpoapp.Entities.Ventas;

import com.corpogas.corpoapp.Entities.Catalogos.Product;
import com.corpogas.corpoapp.Entities.Estaciones.Estacion;
import com.corpogas.corpoapp.Entities.Estaciones.Manguera;
import com.corpogas.corpoapp.Entities.Sucursales.Branch;

import java.io.Serializable;

public class Despacho implements Serializable {

    public long SucursalId;
    public Branch Sucursal;
    public long EstacionId;
    public Estacion Estacion;
    public long TransaccionId;
    public Transaccion Transaccion;
    //public long PosicionCargaId;
    //public virtual PosicionCarga PosicionCarga;
    public long MangueraId;
    public Manguera Manguera;
    public long ProductoId;
    public Product Producto;
    public long ConsecutivoManguera;
    public long ConsecutivoPosicionCarga;
    public long ConsecutivoCvi;
//    public DateTime FechaInicial;
//    public DateTime FechaFinal;
//    public DateTime FechaCVI;
    public double Litros;
    public double Precio;
    public double Importe;
    public double IepsPorLitro;
    public double Iva;
    public double Velocidad;
    public String Placas;
    public boolean Reset;

    public long getSucursalId() { return SucursalId; }

    public void setSucursalId(long sucursalId) { SucursalId = sucursalId; }

    public Branch getSucursal() { return Sucursal; }

    public void setSucursal(Branch sucursal) { Sucursal = sucursal; }

    public long getEstacionId() { return EstacionId; }

    public void setEstacionId(long estacionId) { EstacionId = estacionId; }

    public com.corpogas.corpoapp.Entities.Estaciones.Estacion getEstacion() { return Estacion; }

    public void setEstacion(com.corpogas.corpoapp.Entities.Estaciones.Estacion estacion) { Estacion = estacion; }

    public long getTransaccionId() { return TransaccionId; }

    public void setTransaccionId(long transaccionId) { TransaccionId = transaccionId; }

    public com.corpogas.corpoapp.Entities.Ventas.Transaccion getTransaccion() { return Transaccion; }

    public void setTransaccion(com.corpogas.corpoapp.Entities.Ventas.Transaccion transaccion) { Transaccion = transaccion; }

    public long getMangueraId() { return MangueraId; }

    public void setMangueraId(long mangueraId) { MangueraId = mangueraId; }

    public com.corpogas.corpoapp.Entities.Estaciones.Manguera getManguera() { return Manguera; }

    public void setManguera(com.corpogas.corpoapp.Entities.Estaciones.Manguera manguera) { Manguera = manguera; }

    public long getProductoId() { return ProductoId; }

    public void setProductoId(long productoId) { ProductoId = productoId; }

    public Product getProducto() { return Producto; }

    public void setProducto(Product producto) { Producto = producto; }

    public long getConsecutivoManguera() { return ConsecutivoManguera; }

    public void setConsecutivoManguera(long consecutivoManguera) { ConsecutivoManguera = consecutivoManguera; }

    public long getConsecutivoPosicionCarga() { return ConsecutivoPosicionCarga; }

    public void setConsecutivoPosicionCarga(long consecutivoPosicionCarga) { ConsecutivoPosicionCarga = consecutivoPosicionCarga; }

    public long getConsecutivoCvi() { return ConsecutivoCvi; }

    public void setConsecutivoCvi(long consecutivoCvi) { ConsecutivoCvi = consecutivoCvi; }

    public double getLitros() { return Litros; }

    public void setLitros(double litros) { Litros = litros; }

    public double getPrecio() { return Precio; }

    public void setPrecio(double precio) { Precio = precio; }

    public double getImporte() { return Importe; }

    public void setImporte(double importe) { Importe = importe; }

    public double getIepsPorLitro() { return IepsPorLitro; }

    public void setIepsPorLitro(double iepsPorLitro) { IepsPorLitro = iepsPorLitro; }

    public double getIva() { return Iva; }

    public void setIva(double iva) { Iva = iva; }

    public double getVelocidad() { return Velocidad; }

    public void setVelocidad(double velocidad) { Velocidad = velocidad; }

    public String getPlacas() { return Placas; }

    public void setPlacas(String placas) { Placas = placas; }

    public boolean isReset() { return Reset; }

    public void setReset(boolean reset) { Reset = reset; }
}
