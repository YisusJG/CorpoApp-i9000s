package com.corpogas.corpoapp.Entities.Tickets;

import com.corpogas.corpoapp.Entities.Catalogos.TransactionType;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class TicketDetalle implements Serializable {

    /// <summary>
    /// Id del despacho en caso de que se haya comprado combustible, en caso contrario es cero
    /// </summary>
    public long NoRecibo;
    /// <summary>
    /// Id de transaccion del ticket (para tanque lleno y puntada es el numero de folio)
    /// </summary>
    public long NoTransaccion;
    /// <summary>
    /// Numero de rastreo para facturacion
    /// </summary>
    public String NoRastreo;
    /// <summary>
    /// Posicion de carga de donde se realizo el despacho
    /// </summary>
    public long PosCarga;
    /// <summary>
    /// Empleado que realizo el despacho
    /// </summary>
    public long Desp;
    /// <summary>
    /// Empleado que realizo la venta
    /// </summary>
    public long Vend;
    /// <summary>
    /// Listado de productos vendidos
    /// </summary>
    public List<TicketProducto> Productos;
    /// <summary>
    /// Subtotal de la venta antes del iva
    /// </summary>
    public double Subtotal;
    /// <summary>
    /// Cantidad de iva cobrado
    /// </summary>
    public double IVA;
    /// <summary>
    /// Total de la venta (subtotal e iva)
    /// </summary>
    public double Total;
    /// <summary>
    /// Total de la venta en texto
    /// </summary>
    public String TotalTexto;
    /// <summary>
    /// Numero de cliente
    /// </summary>
    public String Clave;
    /// <summary>
    /// RFC del cliente
    /// </summary>
    public String Rfc;
    ///// <summary>
    ///// Metodo con el que se realizo el pago
    ///// </summary>
    //public string FormaPago;
    /// <summary>
    /// Bandera que indica si el ticket es una reimpresion
    /// </summary>
    public boolean Reimpresion;
    ///// <summary>
    ///// Objeto que contiene la forma de pago con la que se realizo la transaccion
    ///// </summary>
    //public FormaPago FormaPagoTicket;
    public List<TicketFormaPago> TicketFormaPagos;
    /// <summary>
    /// Objeton que contiene el tipo de transaccion con la que se realizo la transaccion
    /// </summary>
    public TransactionType TipoTransaccion;
    /// <summary>
    /// Fecha en la que se genera el ticket
    /// </summary>
    public String Fecha;
    /// <summary>
    /// Bandera que valida si se debe de imprimir el saldo (para tanque lleno)
    /// </summary>
    public boolean ImprimeSaldo;
    /// <summary>
    /// Id del empleado que genera la venta
    /// </summary>
    public long EmpleadoVentaId;
    /// <summary>
    /// Id del empleado que imprime el ticket
    /// </summary>
    public long EmpleadoImpresionId;
    /// <summary>
    /// Id del empleadoq ue reimprime el ticket
    /// </summary>
    public long EmpleadoReimpresionId;
    /// <summary>
    /// Nombre del empleado que imprime el ticket
    /// </summary>
    public String NombreEmpleadoImpresion;

    public long getNoRecibo() {
        return NoRecibo;
    }

    public void setNoRecibo(long noRecibo) {
        NoRecibo = noRecibo;
    }

    public long getNoTransaccion() {
        return NoTransaccion;
    }

    public void setNoTransaccion(long noTransaccion) {
        NoTransaccion = noTransaccion;
    }

    public String getNoRastreo() {
        return NoRastreo;
    }

    public void setNoRastreo(String noRastreo) {
        NoRastreo = noRastreo;
    }

    public long getPosCarga() {
        return PosCarga;
    }

    public void setPosCarga(long posCarga) {
        PosCarga = posCarga;
    }

    public long getDesp() {
        return Desp;
    }

    public void setDesp(long desp) {
        Desp = desp;
    }

    public long getVend() {
        return Vend;
    }

    public void setVend(long vend) {
        Vend = vend;
    }

    public List<TicketProducto> getProductos() {
        return Productos;
    }

    public void setProductos(List<TicketProducto> productos) {
        Productos = productos;
    }

    public double getSubtotal() {
        return Subtotal;
    }

    public void setSubtotal(double subtotal) {
        Subtotal = subtotal;
    }

    public double getIVA() {
        return IVA;
    }

    public void setIVA(double IVA) {
        this.IVA = IVA;
    }

    public double getTotal() {
        return Total;
    }

    public void setTotal(double total) {
        Total = total;
    }

    public String getTotalTexto() {
        return TotalTexto;
    }

    public void setTotalTexto(String totalTexto) {
        TotalTexto = totalTexto;
    }

    public String getClave() {
        return Clave;
    }

    public void setClave(String clave) {
        Clave = clave;
    }

    public String getRfc() {
        return Rfc;
    }

    public void setRfc(String rfc) {
        Rfc = rfc;
    }

    public boolean isReimpresion() {
        return Reimpresion;
    }

    public void setReimpresion(boolean reimpresion) {
        Reimpresion = reimpresion;
    }

    public List<TicketFormaPago> getTicketFormaPagos() {
        return TicketFormaPagos;
    }

    public void setTicketFormaPagos(List<TicketFormaPago> ticketFormaPagos) {
        TicketFormaPagos = ticketFormaPagos;
    }

    public TransactionType getTipoTransaccion() {
        return TipoTransaccion;
    }

    public void setTipoTransaccion(TransactionType tipoTransaccion) {
        TipoTransaccion = tipoTransaccion;
    }

    public String getFecha() {
        return Fecha;
    }

    public void setFecha(String fecha) {
        Fecha = fecha;
    }

    public boolean isImprimeSaldo() {
        return ImprimeSaldo;
    }

    public void setImprimeSaldo(boolean imprimeSaldo) {
        ImprimeSaldo = imprimeSaldo;
    }

    public long getEmpleadoVentaId() {
        return EmpleadoVentaId;
    }

    public void setEmpleadoVentaId(long empleadoVentaId) {
        EmpleadoVentaId = empleadoVentaId;
    }

    public long getEmpleadoImpresionId() {
        return EmpleadoImpresionId;
    }

    public void setEmpleadoImpresionId(long empleadoImpresionId) {
        EmpleadoImpresionId = empleadoImpresionId;
    }

    public long getEmpleadoReimpresionId() {
        return EmpleadoReimpresionId;
    }

    public void setEmpleadoReimpresionId(long empleadoReimpresionId) {
        EmpleadoReimpresionId = empleadoReimpresionId;
    }

    public String getNombreEmpleadoImpresion() {
        return NombreEmpleadoImpresion;
    }

    public void setNombreEmpleadoImpresion(String nombreEmpleadoImpresion) {
        NombreEmpleadoImpresion = nombreEmpleadoImpresion;
    }
}
