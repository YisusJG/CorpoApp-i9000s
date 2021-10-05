package com.corpogas.corpoapp.Facturacion.Entities;

import java.io.Serializable;

public class RespuestaRFC implements Serializable {
    public String RazonSocial;
    public String RFC;
    public String Email;
    public String IdCliente;
    public String IdAlias;
    private boolean expandable = false;

    public boolean isExpandable() {
        return expandable;
    }

    public void setExpandable(boolean expandable) {
        this.expandable = expandable;
    }

    public String getRazonSocial() {
        return RazonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        RazonSocial = razonSocial;
    }

    public String getRFC() {
        return RFC;
    }

    public void setRFC(String RFC) {
        this.RFC = RFC;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getIdCliente() {
        return IdCliente;
    }

    public void setIdCliente(String idCliente) {
        IdCliente = idCliente;
    }

    public String getIdAlias() {
        return IdAlias;
    }

    public void setIdAlias(String idAlias) {
        IdAlias = idAlias;
    }
}
