package com.corpogas.corpoapp.Entities.Estaciones;

import com.corpogas.corpoapp.Entities.Sucursales.Branch;
import com.corpogas.corpoapp.Entities.Sucursales.StorageCellar;

import java.io.Serializable;
import java.util.List;

public class Isla implements Serializable {

    public long SucursalId;
    public Branch Sucursal;
    public long EstacionId;
    public Estacion Estacion;
    public int NumeroIsla;
    public long BodegaId;
    public StorageCellar Bodega;
    public List<BodegaProducto> BodegaProductos;

    public long getSucursalId() { return SucursalId; }

    public void setSucursalId(long sucursalId) { SucursalId = sucursalId; }

    public Branch getSucursal() { return Sucursal; }

    public void setSucursal(Branch sucursal) { Sucursal = sucursal; }

    public long getEstacionId() { return EstacionId; }

    public void setEstacionId(long estacionId) { EstacionId = estacionId; }

    public Estacion getEstacion() { return Estacion; }

    public void setEstacion(Estacion estacion) { Estacion = estacion; }

    public int getNumeroIsla() { return NumeroIsla; }

    public void setNumeroIsla(int numeroIsla) { NumeroIsla = numeroIsla; }

    public long getBodegaId() { return BodegaId; }

    public void setBodegaId(long bodegaId) { BodegaId = bodegaId; }

    public StorageCellar getBodega() { return Bodega; }

    public void setBodega(StorageCellar bodega) { Bodega = bodega; }

    public List<BodegaProducto> getBodegaProductos() { return BodegaProductos; }

    public void setBodegaProductos(List<BodegaProducto> bodegaProductos) { BodegaProductos = bodegaProductos; }
}
