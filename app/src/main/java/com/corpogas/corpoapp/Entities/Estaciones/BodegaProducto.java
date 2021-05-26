package com.corpogas.corpoapp.Entities.Estaciones;

import com.corpogas.corpoapp.Entities.Catalogos.Product;
import com.corpogas.corpoapp.Entities.Sucursales.Branch;
import com.corpogas.corpoapp.Entities.Sucursales.StorageCellar;

import java.io.Serializable;

public class BodegaProducto implements Serializable {

    public long SucursalId;
    public Branch Sucursal;
    public long EstacionId;
    public Estacion Estacion;
    public long BodegaId;
    public StorageCellar Bodega;
    public long ProductoId;
    public Product Producto;
    public double Existencias;

    public long getSucursalId() { return SucursalId; }

    public void setSucursalId(long sucursalId) { SucursalId = sucursalId; }

    public Branch getSucursal() { return Sucursal; }

    public void setSucursal(Branch sucursal) { Sucursal = sucursal; }

    public long getEstacionId() { return EstacionId; }

    public void setEstacionId(long estacionId) { EstacionId = estacionId; }

    public Estacion getEstacion() { return Estacion; }

    public void setEstacion(Estacion estacion) { Estacion = estacion; }

    public long getBodegaId() { return BodegaId; }

    public void setBodegaId(long bodegaId) { BodegaId = bodegaId; }

    public StorageCellar getBodega() { return Bodega; }

    public void setBodega(StorageCellar bodega) { Bodega = bodega; }

    public long getProductoId() { return ProductoId; }

    public void setProductoId(long productoId) { ProductoId = productoId; }

    public Product getProducto() { return Producto; }

    public void setProducto(Product producto) { Producto = producto; }

    public double getExistencias() { return Existencias; }

    public void setExistencias(double existencias) { Existencias = existencias; }
}
