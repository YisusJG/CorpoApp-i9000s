package com.corpogas.corpoapp.Entities.Tickets;

import java.io.Serializable;

public class Ticket<T> implements Serializable {
    /// <summary>
    /// Encabezado del ticket
    /// </summary>
    private TicketCabecero Cabecero;
    /// <summary>
    /// Cuerpo del ticket
    /// </summary>
    private TicketDetalle Detalle;
    /// <summary>
    /// En caso de ocurrir algun error, se muestra en esta propiedad
    /// </summary>
    private TicketResultado Resultado;
    /// <summary>
    /// Pie del ticket
    /// </summary>
    private TicketPie Pie;


    public TicketCabecero getCabecero() {
        return Cabecero;
    }

    public void setCabecero(TicketCabecero cabecero) {
        Cabecero = cabecero;
    }

    public TicketDetalle getDetalle() { return Detalle; }

    public void setDetalle(TicketDetalle detalle) { Detalle = detalle; }

    public TicketResultado getResultado() { return Resultado; }

    public void setResultado(TicketResultado resultado) { Resultado = resultado; }

    public TicketPie getPie() { return Pie; }

    public void setPie(TicketPie pie) { Pie = pie; }
}
