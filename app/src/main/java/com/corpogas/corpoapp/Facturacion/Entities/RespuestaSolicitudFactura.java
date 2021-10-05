package com.corpogas.corpoapp.Facturacion.Entities;

import java.io.Serializable;

public class RespuestaSolicitudFactura implements Serializable {

    public RespuestaSolicitud datos;

    public RespuestaSolicitud getDatos() {
        return datos;
    }

    public void setDatos(RespuestaSolicitud datos) {
        this.datos = datos;
    }
}
