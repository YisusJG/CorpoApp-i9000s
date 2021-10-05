package com.corpogas.corpoapp.Facturacion.Entities;

import java.io.Serializable;

public class PeticionRFC implements Serializable {
    public String rfc;
    public String despachador;
    public String terminal;

    public String getRfc() {
        return rfc;
    }

    public void setRfc(String rfc) {
        this.rfc = rfc;
    }

    public String getDespachador() {
        return despachador;
    }

    public void setDespachador(String despachador) {
        this.despachador = despachador;
    }

    public String getTerminal() {
        return terminal;
    }

    public void setTerminal(String terminal) {
        this.terminal = terminal;
    }

}
