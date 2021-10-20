package com.corpogas.corpoapp.Entregas.Entities;

public class ValePapel {
    public long NumeroTipoVale;
    public String TipoVale;
    public int Cantidad;
    public Double Denominacion;

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

    public Double getDenominacion() {
        return Denominacion;
    }

    public void setDenominacion(Double denominacion) {
        Denominacion = denominacion;
    }
}
