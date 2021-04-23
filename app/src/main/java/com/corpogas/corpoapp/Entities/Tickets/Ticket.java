package com.corpogas.corpoapp.Entities.Tickets;

public class Ticket {
    /// <summary>
    /// Encabezado del ticket
    /// </summary>
    private TicketCabecero Cabecero;
    /// <summary>
    /// Cuerpo del ticket
    /// </summary>
//    private TicketDetalle Detalle;
    /// <summary>
    /// En caso de ocurrir algun error, se muestra en esta propiedad
    /// </summary>
//    private TicketResultado Resultado;
    /// <summary>
    /// Pie del ticket
    /// </summary>
//    private TicketPie Pie;


    public TicketCabecero getCabecero() {
        return Cabecero;
    }

    public void setCabecero(TicketCabecero cabecero) {
        Cabecero = cabecero;
    }
}
