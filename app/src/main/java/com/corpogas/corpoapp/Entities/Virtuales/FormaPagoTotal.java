package com.corpogas.corpoapp.Entities.Virtuales;

import java.io.Serializable;

public class FormaPagoTotal implements Serializable {

    public long NumeroFormaPago;
    public String FormaPago;
    public double Importe;

    public long getNumeroFormaPago() {
        return NumeroFormaPago;
    }

    public void setNumeroFormaPago(long numeroFormaPago) {
        NumeroFormaPago = numeroFormaPago;
    }

    public String getFormaPago() {
        return FormaPago;
    }

    public void setFormaPago(String formaPago) {
        FormaPago = formaPago;
    }

    public double getImporte() {
        return Importe;
    }

    public void setImporte(double importe) {
        Importe = importe;
    }
}
