package com.corpogas.corpoapp.Entities.Estaciones;

import java.io.Serializable;
import java.util.List;

public class ResumenFajilla implements Serializable {
    public long IslaId;
    public int NumeroIsla;
    public String NombreEmpleado;
    public long TipoFajilla;
    public int Cantidad;
    public double ImporteTotal;
    public String FechaUltimaFajilla;

    public long getIslaId() {
        return IslaId;
    }

    public void setIslaId(long islaId) {
        IslaId = islaId;
    }

    public int getNumeroIsla() {
        return NumeroIsla;
    }

    public void setNumeroIsla(int numeroIsla) {
        NumeroIsla = numeroIsla;
    }

    public String getNombreEmpleado() {
        return NombreEmpleado;
    }

    public void setNombreEmpleado(String nombreEmpleado) {
        NombreEmpleado = nombreEmpleado;
    }

    public long getTipoFajilla() {
        return TipoFajilla;
    }

    public void setTipoFajilla(long tipoFajilla) {
        TipoFajilla = tipoFajilla;
    }

    public int getCantidad() {
        return Cantidad;
    }

    public void setCantidad(int cantidad) {
        Cantidad = cantidad;
    }

    public double getImporteTotal() {
        return ImporteTotal;
    }

    public void setImporteTotal(double importeTotal) {
        ImporteTotal = importeTotal;
    }

    public String getFechaUltimaFajilla() {
        return FechaUltimaFajilla;
    }

    public void setFechaUltimaFajilla(String fechaUltimaFajilla) {
        FechaUltimaFajilla = fechaUltimaFajilla;
    }
}
