package com.corpogas.corpoapp.Entregas.Entities;

public class ValePapel {
    public long TipoValePapelId;
    public String TipoVale;
    public int Cantidad;
    public Double Denominacion;

    public ValePapel(long tipoValePapelId, String tipoVale, int cantidad, Double denominacion) {
        TipoValePapelId = tipoValePapelId;
        TipoVale = tipoVale;
        Cantidad = cantidad;
        Denominacion = denominacion;
    }

    public long getTipoValePapelId() {
        return TipoValePapelId;
    }

    public void setTipoValePapelId(long tipoValePapelId) {
        TipoValePapelId = tipoValePapelId;
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
