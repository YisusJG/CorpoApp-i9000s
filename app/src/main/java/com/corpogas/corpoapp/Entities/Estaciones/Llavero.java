package com.corpogas.corpoapp.Entities.Estaciones;

import com.corpogas.corpoapp.Entities.Sucursales.Branch;

import java.io.Serializable;

public class Llavero implements Serializable {

    public long SucursalId;
    public Branch Sucursal;
    public long EstacionId;
    public Estacion Estacion;
//    public TipoLlavero TipoLlavero;
    public String NumeroLlavero;

    public long getSucursalId() {
        return SucursalId;
    }

    public void setSucursalId(long sucursalId) {
        SucursalId = sucursalId;
    }

    public Branch getSucursal() {
        return Sucursal;
    }

    public void setSucursal(Branch sucursal) {
        Sucursal = sucursal;
    }

    public long getEstacionId() {
        return EstacionId;
    }

    public void setEstacionId(long estacionId) {
        EstacionId = estacionId;
    }

    public com.corpogas.corpoapp.Entities.Estaciones.Estacion getEstacion() {
        return Estacion;
    }

    public void setEstacion(com.corpogas.corpoapp.Entities.Estaciones.Estacion estacion) {
        Estacion = estacion;
    }

    public String getNumeroLlavero() {
        return NumeroLlavero;
    }

    public void setNumeroLlavero(String numeroLlavero) {
        NumeroLlavero = numeroLlavero;
    }
}
