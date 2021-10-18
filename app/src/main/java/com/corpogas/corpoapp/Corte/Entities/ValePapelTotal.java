package com.corpogas.corpoapp.Corte.Entities;

public class ValePapelTotal {

    public long NumeroTipoVale;
    public String TipoVale ;
    public int Cantidad ;
    public double Denominacion ;
    public double Total ;

    public void setTotal(double total) {
        Total = total;
    }

    public long getNumeroTipoVale() {
        return NumeroTipoVale;
    }

    public void setNumeroTipoVale(long numeroTipoVale) {
        NumeroTipoVale = numeroTipoVale;
    }

    public String getTipoVale() {
        return TipoVale;
    }

    public void setTipoVale(String tipoVale) {
        TipoVale = tipoVale;
    }

    public int getCantidad() {
        return Cantidad;
    }

    public void setCantidad(int cantidad) {
        Cantidad = cantidad;
    }

    public double getDenominacion() {
        return Denominacion;
    }

    public void setDenominacion(double denominacion) {
        Denominacion = denominacion;
    }

    public double getTotal() {
        return Cantidad * Denominacion;
    }
}
