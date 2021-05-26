package com.corpogas.corpoapp.Entities.Ventas;

import com.corpogas.corpoapp.Entities.Catalogos.Product;
import com.corpogas.corpoapp.Entities.Estaciones.Estacion;
import com.corpogas.corpoapp.Entities.Sucursales.Branch;

import java.io.Serializable;

public class VentaProducto implements Serializable {

    public long SucursalId;
    public Branch Sucursal;
    public long EstacionId;
    public Estacion Estacion;
    public long TransaccionId;
    public Transaccion Transaccion;
    public long ProductoId;
    public Product Producto;
    public double Cantidad;
    public double Precio;
    public double Importe;

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

    public long getProductoId() { return ProductoId; }

    public void setProductoId(long productoId) { ProductoId = productoId; }

    public Product getProducto() { return Producto; }

    public void setProducto(Product producto) { Producto = producto; }

    public double getCantidad() { return Cantidad; }

    public void setCantidad(double cantidad) { Cantidad = cantidad; }

    public double getPrecio() { return Precio; }

    public void setPrecio(double precio) { Precio = precio; }

    public double getImporte() { return Importe; }

    public void setImporte(double importe) { Importe = importe; }
}
