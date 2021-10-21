package com.corpogas.corpoapp.Entities.Estaciones;

import java.io.Serializable;
import java.util.List;

public class RecepcionFajillaEntregada implements Serializable {
    public List<TotalFajilla> FajillasEntregadas;
    public List<TotalFajilla> FajillasRecibidas;
    //        public String NombreAutoriza;
    public String FechaEntrega;

    public List<TotalFajilla> getFajillasEntregadas() {
        return FajillasEntregadas;
    }

    public void setFajillasEntregadas(List<TotalFajilla> fajillasEntregadas) {
        FajillasEntregadas = fajillasEntregadas;
    }

    public List<TotalFajilla> getFajillasRecibidas() {
        return FajillasRecibidas;
    }

    public void setFajillasRecibidas(List<TotalFajilla> fajillasRecibidas) {
        FajillasRecibidas = fajillasRecibidas;
    }

    public String getFechaEntrega() {
        return FechaEntrega;
    }

    public void setFechaEntrega(String fechaEntrega) {
        FechaEntrega = fechaEntrega;
    }

//    public String getNombreAutoriza() {
//        return NombreAutoriza;
//    }
//
//    public void setNombreAutoriza(String nombreAutoriza) {
//        NombreAutoriza = nombreAutoriza;
//    }
}