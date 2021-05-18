package com.corpogas.corpoapp.Entities.Tickets;

import java.io.Serializable;

public class DiccionarioParcialidades implements Serializable {

    /// <summary>
    /// Id de la forma de pago
    /// </summary>
    public long Id;
    /// <summary>
    /// Importe de la parcialidad
    /// </summary>
    public double Importe;

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public double getImporte() {
        return Importe;
    }

    public void setImporte(double importe) {
        Importe = importe;
    }
}
