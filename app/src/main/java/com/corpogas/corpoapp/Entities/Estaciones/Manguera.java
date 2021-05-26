package com.corpogas.corpoapp.Entities.Estaciones;

import com.corpogas.corpoapp.Entities.Catalogos.Product;
import com.corpogas.corpoapp.Entities.Sucursales.Branch;

import java.io.Serializable;

public class Manguera implements Serializable {

    public long SucursalId;
    public Branch Sucursal;
    public long EstacionId;
    public Estacion Estacion;
    public long PosicionCargaId;
    public PosicionCarga PosicionCarga;
    public long ProductoId;
    public Product Producto;
    public int NumeroManguera;
    public int SecuencialAlvic;
    public String Nid;

    public long getSucursalId() { return SucursalId; }

    public void setSucursalId(long sucursalId) { SucursalId = sucursalId; }

    public Branch getSucursal() { return Sucursal; }

    public void setSucursal(Branch sucursal) { Sucursal = sucursal; }

    public long getEstacionId() { return EstacionId; }

    public void setEstacionId(long estacionId) { EstacionId = estacionId; }

    public com.corpogas.corpoapp.Entities.Estaciones.Estacion getEstacion() { return Estacion; }

    public void setEstacion(com.corpogas.corpoapp.Entities.Estaciones.Estacion estacion) { Estacion = estacion; }

    public long getPosicionCargaId() { return PosicionCargaId; }

    public void setPosicionCargaId(long posicionCargaId) { PosicionCargaId = posicionCargaId; }

    public PosicionCarga getPosicionCarga() { return PosicionCarga; }

    public void setPosicionCarga(PosicionCarga posicionCarga) { PosicionCarga = posicionCarga; }

    public long getProductoId() { return ProductoId; }

    public void setProductoId(long productoId) { ProductoId = productoId; }

    public Product getProducto() { return Producto; }

    public void setProducto(Product producto) { Producto = producto; }

    public int getNumeroManguera() { return NumeroManguera; }

    public void setNumeroManguera(int numeroManguera) { NumeroManguera = numeroManguera; }

    public int getSecuencialAlvic() { return SecuencialAlvic; }

    public void setSecuencialAlvic(int secuencialAlvic) { SecuencialAlvic = secuencialAlvic; }

    public String getNid() { return Nid; }

    public void setNid(String nid) { Nid = nid; }
}
