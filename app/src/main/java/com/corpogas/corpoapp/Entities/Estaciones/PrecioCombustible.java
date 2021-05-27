package com.corpogas.corpoapp.Entities.Estaciones;

import com.corpogas.corpoapp.Entities.Catalogos.Product;
import com.corpogas.corpoapp.Entities.Sucursales.Branch;

import java.io.Serializable;

public class PrecioCombustible implements Serializable {

    public long SucursalId;
    public Branch Sucursal;
    public long EstacionId;
    public Estacion Estacion;
    public long ProductoId;
    public Product Producto;
    public double Importe;
//    public DateTime FechaInicio;
//    public DateTime FechaFin;
    public boolean Aplicado;

    public long getSucursalId() { return SucursalId; }

    public void setSucursalId(long sucursalId) { SucursalId = sucursalId; }

    public Branch getSucursal() { return Sucursal; }

    public void setSucursal(Branch sucursal) { Sucursal = sucursal; }

    public long getEstacionId() { return EstacionId; }

    public void setEstacionId(long estacionId) { EstacionId = estacionId; }

    public Estacion getEstacion() { return Estacion; }

    public void setEstacion(Estacion estacion) { Estacion = estacion; }

    public long getProductoId() { return ProductoId; }

    public void setProductoId(long productoId) {  ProductoId = productoId; }

    public Product getProducto() { return Producto; }

    public void setProducto(Product producto) { Producto = producto; }

    public double getImporte() { return Importe; }

    public void setImporte(double importe) { Importe = importe; }

    public boolean isAplicado() { return Aplicado; }

    public void setAplicado(boolean aplicado) { Aplicado = aplicado; }
}
