package com.corpogas.corpoapp.Entities.Virtuales;

import java.io.Serializable;

public class FajillaTotal implements Serializable {

    public long TipoFajilla;
    public String Descripcion;
    public double Cantidad;
    public double Total;

    public long getTipoFajilla() {
        return TipoFajilla;
    }

    public void setTipoFajilla(long tipoFajilla) {
        TipoFajilla = tipoFajilla;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public double getCantidad() {
        return Cantidad;
    }

    public void setCantidad(double cantidad) {
        Cantidad = cantidad;
    }

    public double getTotal() {
        return Total;
    }

    public void setTotal(double total) {
        Total = total;
    }
}
