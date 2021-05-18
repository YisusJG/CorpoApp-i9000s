package com.corpogas.corpoapp.Entities.Common;

import java.io.Serializable;

public class ProductoTarjetero implements Serializable {

    /// <summary>
    /// Tipo del producto
    /// 1 = Combustible
    /// 2 = Aceites
    /// </summary>
    public int TipoProducto;
    /// <summary>
    /// Id del producto
    /// </summary>
    public long ProductoId;
    /// <summary>
    /// Numero interno del producto
    /// </summary>
    public int NumeroInterno;
    /// <summary>
    /// Description de los productos que se van a agregar
    /// Si es combustible se toma la descricion larga, si es un producto se toma la descripcion corta
    /// </summary>
    public String Descripcion;
    /// <summary>
    /// Cantidad de producto (unidades/litros)
    /// </summary>
    public double Cantidad;
    /// <summary>
    /// Price del producto
    /// </summary>
    public double Precio;
    /// <summary>
    /// Importe total del producto
    /// </summary>
    public double ImporteTotal;
    /// <summary>
    /// Bandera que valida si la cantidad enviada son litros o el importe (en caso de qu    e sea combustible)
    /// </summary>
    public boolean Importe;

    public int getTipoProducto() {
        return TipoProducto;
    }

    public void setTipoProducto(int tipoProducto) {
        TipoProducto = tipoProducto;
    }

    public long getProductoId() {
        return ProductoId;
    }

    public void setProductoId(long productoId) {
        ProductoId = productoId;
    }

    public int getNumeroInterno() {
        return NumeroInterno;
    }

    public void setNumeroInterno(int numeroInterno) {
        NumeroInterno = numeroInterno;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public double getCantidad() {
        return Cantidad;
    }

    public void setCantidad(double cantidad) {
        Cantidad = cantidad;
    }

    public double getPrecio() {
        return Precio;
    }

    public void setPrecio(double precio) {
        Precio = precio;
    }

    public double getImporteTotal() {
        return ImporteTotal;
    }

    public void setImporteTotal(double importeTotal) {
        ImporteTotal = importeTotal;
    }

    public boolean isImporte() {
        return Importe;
    }

    public void setImporte(boolean importe) {
        Importe = importe;
    }
}
