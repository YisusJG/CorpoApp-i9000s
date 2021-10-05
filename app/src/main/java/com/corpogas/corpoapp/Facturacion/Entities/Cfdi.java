package com.corpogas.corpoapp.Facturacion.Entities;

import java.io.Serializable;

public class Cfdi implements Serializable {
    public Factura Factura;

    public Factura getFactura() {
        return Factura;
    }

    public void setFactura(Factura factura) {
        Factura = factura;
    }
}
