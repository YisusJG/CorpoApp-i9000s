package com.corpogas.corpoapp.Facturacion.Entities;

import java.io.Serializable;

public class RespuestaSolicitud implements Serializable {
    public String codigo ;
    public String mensaje;
    public Cfdi Cfdi;

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Cfdi getCfdi() {
        return Cfdi;
    }

    public void setCfdi(Cfdi cfdi) {
        Cfdi = cfdi;
    }
}
