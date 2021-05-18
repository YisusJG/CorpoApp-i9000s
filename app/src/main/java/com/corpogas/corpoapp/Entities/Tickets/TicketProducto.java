package com.corpogas.corpoapp.Entities.Tickets;

import java.io.Serializable;

public class TicketProducto implements Serializable {

    /// <summary>
    /// Define el tipo al que pertenece el producto
    /// </summary>
    public int TipoProducto;
    /// <summary>
    /// Description corta del producto
    /// </summary>
    public String Descripcion;
    /// <summary>
    /// Cantidad del producto
    /// </summary>
    public double Cantidad;
    /// <summary>
    /// Cantidad a pagar por el producto
    /// </summary>
    public double Importe;
    /// <summary>
    /// Price individual del producto
    /// </summary>
    public double Precio;
    /// <summary>
    /// Iva cobrado del producto
    /// </summary>
    public double Iva;

    public int getTipoProducto() {  return TipoProducto; }

    public void setTipoProducto(int tipoProducto) { TipoProducto = tipoProducto; }

    public String getDescripcion() { return Descripcion; }

    public void setDescripcion(String descripcion) { Descripcion = descripcion; }

    public double getCantidad() { return Cantidad; }

    public void setCantidad(double cantidad) { Cantidad = cantidad; }

    public double getImporte() { return Importe; }

    public void setImporte(double importe) { Importe = importe; }

    public double getPrecio() { return Precio; }

    public void setPrecio(double precio) { Precio = precio; }

    public double getIva() { return Iva; }

    public void setIva(double iva) { Iva = iva; }
}
