package com.corpogas.corpoapp.Entities.Tickets;

import com.corpogas.corpoapp.Entities.Catalogos.PaymentMethod;

import java.io.Serializable;

public class TicketFormaPago implements Serializable {

    public double Importe;
    public PaymentMethod FormaPago;

    public double getImporte() { return Importe; }

    public void setImporte(double importe) { Importe = importe; }

    public PaymentMethod getFormaPago() { return FormaPago; }

    public void setFormaPago(PaymentMethod formaPago) { FormaPago = formaPago; }
}
