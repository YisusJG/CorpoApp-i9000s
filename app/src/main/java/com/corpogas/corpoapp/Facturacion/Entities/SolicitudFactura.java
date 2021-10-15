package com.corpogas.corpoapp.Facturacion.Entities;

import java.io.Serializable;
import java.util.List;

public class SolicitudFactura implements Serializable {
    public List<String> tickets;
    public String rfc;
    public String email;
    public String IdCliente;
    public String IdAlias;
    public String DispositivoId;



    public SolicitudFactura() {

    }

    public List<String> getTickets() {
        return tickets;
    }

    public void setTickets(List<String> tickets) {
        this.tickets = tickets;
    }

    public String getRfc() {
        return rfc;
    }

    public void setRfc(String rfc) {
        this.rfc = rfc;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getDispositivoId() {return DispositivoId; }

    public void  setDispositivoId(String dispositivoId) { DispositivoId = dispositivoId;}

}
