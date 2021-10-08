package com.corpogas.corpoapp.Entities.Cortes;

import com.corpogas.corpoapp.Entities.Estaciones.Estacion;
import com.corpogas.corpoapp.Entities.Sucursales.Branch;
import com.corpogas.corpoapp.Entities.TipoFajilla;

import java.io.Serializable;

public class CierreFajilla implements Serializable {

    public long SucursalId;
    public Branch Sucursal;
    public long EstacionId;
    public Estacion Estacion;
    public long CierreId;
    public Cierre Cierre;
    public long TipoFajilla; //TipoFajilla TipoFajilla;
    public int FolioInicial;
    public int FolioFinal;
    public double Denominacion;
    public double PicosMonedas;
    public int Total;
    public int FajillasBilletes;
    public int FajillasMonedas;

    public CierreFajilla(int folioFinal, int folioInicial, long sucursalId, double picosMonedas,  long tipoFajilla, double denominacion, int total) {
        SucursalId = sucursalId;
        TipoFajilla = tipoFajilla;
        FolioInicial = folioInicial;
        FolioFinal = folioFinal;
        Denominacion = denominacion;
        PicosMonedas = picosMonedas;
        Total = total;

    }

    public CierreFajilla(int fajillasBilletes, int fajillasMonedas, long tipoFajilla) {
        FajillasBilletes = fajillasBilletes;
        FajillasMonedas = fajillasMonedas;
        TipoFajilla = tipoFajilla;

    }

    public CierreFajilla(){

    }

    public long getSucursalId() { return SucursalId; }

    public void setSucursalId(long sucursalId) { SucursalId = sucursalId; }

    public Branch getSucursal() { return Sucursal; }

    public void setSucursal(Branch sucursal) { Sucursal = sucursal; }

    public long getEstacionId() { return EstacionId; }

    public void setEstacionId(long estacionId) { EstacionId = estacionId; }

    public Estacion getEstacion() { return Estacion; }

    public void setEstacion(Estacion estacion) { Estacion = estacion; }

    public long getCierreId() { return CierreId; }

    public void setCierreId(long cierreId) { CierreId = cierreId; }

    public Cierre getCierre() { return Cierre; }

    public void setCierre(Cierre cierre) { Cierre = cierre; }

//    public TipoFajilla getTipoFajilla() { return TipoFajilla; }
//
//    public void setTipoFajilla(TipoFajilla tipoFajilla) { TipoFajilla = tipoFajilla; }


    public long getTipoFajilla() {
        return TipoFajilla;
    }

    public void setTipoFajilla(long tipoFajilla) {
        TipoFajilla = tipoFajilla;
    }

    public int getFolioInicial() { return FolioInicial; }

    public void setFolioInicial(int folioInicial) { FolioInicial = folioInicial; }

    public int getFolioFinal() { return FolioFinal; }

    public void setFolioFinal(int folioFinal) { FolioFinal = folioFinal; }

    public double getDenominacion() { return Denominacion; }

    public void setDenominacion(double denominacion) { Denominacion = denominacion; }

    public double getPicosMonedas() {
        return PicosMonedas;
    }

    public void setPicosMonedas(double picosMonedas) {
        PicosMonedas = picosMonedas;
    }

    public int getTotal() {
        return Total;
    }

    public void setTotal(int total) {
        Total = total;
    }

    public int getFajillasBilletes() { return FajillasBilletes; }

    public void setFajillasBilletes(int fajillasBilletes) { FajillasBilletes = fajillasBilletes; }

    public int getFajillasMonedas() { return FajillasMonedas; }

    public void setFajillasMonedas(int fajillasMonedas) { FajillasMonedas = fajillasMonedas; }
}
