package com.corpogas.corpoapp.Fajillas;

import java.io.Serializable;

public class RecyclerViewHeadersFajillas implements Serializable {
    public String tipofajilla;
    public String cantidad;
    public String monto;
    public String Autorizo;
    public String Entradasalida;
//    public int imgAgregarVale;
//    public int imgEditarVale;
//    public int imgEliminarVale;
//    public int imgDetalleVale;

    public RecyclerViewHeadersFajillas(String tipofajilla, String cantidad, String monto, String autorizo, String entradasalida) {
        this.tipofajilla = tipofajilla;
        this.cantidad = cantidad;
        this.monto = monto;
        this.Autorizo = autorizo;
        this.Entradasalida = entradasalida;
    }

    public String getTipoFajilla() {
        return tipofajilla;
    }

    public void setTipoFajilla(String tipoFajilla) {
        tipoFajilla = tipofajilla;
    }

    public String getCantidadFajillas() {
        return cantidad;
    }

    public void setCantidadFajillas(String cantidad) {
        this.cantidad = cantidad;
    }

    public String getMontoFajillas() { return monto;}

    public void setMontoFajillas(String monto) {
        this.monto = monto;
    }

    public String getAutorizo() {
        return Autorizo;
    }

    public void setAutorizo(String autorizo) {
        Autorizo = autorizo;
    }

    public String getEntradasalida(){ return  Entradasalida;}

    public  void setEntradasalida(String entradasalida) { Entradasalida = entradasalida; }

}

