package com.corpogas.corpoapp.Entities.Estaciones;

import java.io.Serializable;

public class TotalFajilla implements Serializable {


    public String Responsable; //NombreAutoriza
    public String TipoFajilla;
    public int Precio;
    public Double ImporteTotal ;
    public int Cantidad;

    public String getNombreAutoriza() {
        return Responsable;
    }

    public void setNombreAutoriza(String responsable) {
        Responsable = responsable;
    }


    public String getTipoFajilla() {
        return TipoFajilla;
    }

    public void setTipoFajilla(String tipoFajilla) {
        TipoFajilla = tipoFajilla;
    }

    public int getPrecio() {
        return Precio;
    }

    public void setPrecio(int precio) {
        Precio = precio;
    }

    public Double getImporteTotal() {
        return ImporteTotal;
    }

    public void setImporteTotal(Double importeTotal) {
        ImporteTotal = importeTotal;
    }

    public int getCantidad() {
        return Cantidad;
    }

    public void setCantidad(int cantidad) {
        Cantidad = cantidad;
    }
}
