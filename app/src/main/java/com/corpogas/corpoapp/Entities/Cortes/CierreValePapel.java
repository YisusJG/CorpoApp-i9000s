package com.corpogas.corpoapp.Entities.Cortes;

import com.corpogas.corpoapp.Entities.Estaciones.Estacion;
import com.corpogas.corpoapp.Entities.Sucursales.Branch;
import com.corpogas.corpoapp.Entregas.Entities.PaperVoucherType;

import java.io.Serializable;

public class CierreValePapel implements Serializable {

    public long SucursalId;
    public Branch Sucursal;
    public long EstacionId;
    public Estacion Estacion;
    public long CierreId;
    public Cierre Cierre;
    public long TipoValePapelId;
    public PaperVoucherType TipoValePapel;
    public double Cantidad;
    public double Denominacion;
    public double Importe;
    public String NombreVale;

    public CierreValePapel(long sucursalId, long estacionId, long cierreId, long tipoValePapelId, double cantidad, double denominacion, double importe, String nombreVale) {
        SucursalId = sucursalId;
        EstacionId = estacionId;
        CierreId = cierreId;
        TipoValePapelId = tipoValePapelId;
        Cantidad = cantidad;
        Denominacion = denominacion;
        Importe = importe;
        NombreVale = nombreVale;
    }

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

    public Estacion getEstacion() {
        return Estacion;
    }

    public void setEstacion(Estacion estacion) { Estacion = estacion; }

    public long getCierreId() {
        return CierreId;
    }

    public void setCierreId(long cierreId) {
        CierreId = cierreId;
    }

    public Cierre getCierre() {
        return Cierre;
    }

    public void setCierre(Cierre cierre) {
        Cierre = cierre;
    }

    public long getTipoValePapelId() {
        return TipoValePapelId;
    }

    public void setTipoValePapelId(long tipoValePapelId) {
        TipoValePapelId = tipoValePapelId;
    }

    public PaperVoucherType getTipoValePapel() {
        return TipoValePapel;
    }

    public void setTipoValePapel(PaperVoucherType tipoValePapel) {
        TipoValePapel = tipoValePapel;
    }

    public double getCantidad() {
        return Cantidad;
    }

    public void setCantidad(double cantidad) {
        Cantidad = cantidad;
    }

    public double getDenominacion() {
        return Denominacion;
    }

    public void setDenominacion(double denominacion) {
        Denominacion = denominacion;
    }

    public double getImporte() {
        return Importe;
    }

    public void setImporte(double importe) {
        Importe = importe;
    }

    public String getNombreVale() {
        return NombreVale;
    }

    public void setNombreVale(String nombreVale) {
        NombreVale = nombreVale;
    }
}
