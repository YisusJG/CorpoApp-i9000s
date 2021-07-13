package com.corpogas.corpoapp.Entities.Tickets;

import java.io.Serializable;

public class Ticket implements Serializable {
    /// <summary>
    /// Encabezado del ticket
    /// </summary>
    public TicketCabecero Cabecero;
    /// <summary>
    /// Cuerpo del ticket
    /// </summary>
    public TicketDetalle Detalle;
    /// <summary>
    /// En caso de ocurrir algun error, se muestra en esta propiedad
    /// </summary>
    public TicketResultado Resultado;
    /// <summary>
    /// Pie del ticket
    /// </summary>
    public TicketPie Pie;



    public TicketCabecero getCabecero() {
        return Cabecero;
    }

    public void setCabecero(TicketCabecero cabecero) {
        Cabecero = cabecero;
    }

    public TicketDetalle getDetalle() {
        return Detalle;
    }

    public void setDetalle(TicketDetalle detalle) {
        Detalle = detalle;
    }

    public TicketResultado getResultado() {
        return Resultado;
    }

    public void setResultado(TicketResultado resultado) {
        Resultado = resultado;
    }

    public TicketPie getPie() {
        return Pie;
    }

    public void setPie(TicketPie pie) {
        Pie = pie;
    }
}
