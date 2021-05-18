package com.corpogas.corpoapp.Entities.Tickets;

import java.io.Serializable;

public class TicketResultado implements Serializable {

    /// <summary>
    /// Codigo de error
    /// </summary>
    public int Error;
    /// <summary>
    /// Description del error
    /// </summary>
    public String Descripcion;

    public int getError() { return Error; }

    public void setError(int error) { Error = error; }

    public String getDescripcion() { return Descripcion; }

    public void setDescripcion(String descripcion) { Descripcion = descripcion; }
}
