package com.corpogas.corpoapp.Cofre.Entities;

import java.io.Serializable;

public class TotalFajillaCajaFuerte implements Serializable {
    public long Id;
    public long Precio;
    public long ImporteTotal;
    public int Cantidad;
    public long Folio;

    public TotalFajillaCajaFuerte(long id, long precio, long importeTotal, int cantidad, long folio) {
        Id = id;
        Precio = precio;
        ImporteTotal = importeTotal;
        Cantidad = cantidad;
        Folio = folio;
    }
}
