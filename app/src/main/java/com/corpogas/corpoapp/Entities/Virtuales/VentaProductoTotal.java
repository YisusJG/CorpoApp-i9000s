package com.corpogas.corpoapp.Entities.Virtuales;

import java.io.Serializable;

public class VentaProductoTotal implements Serializable {

    public long NumeroProducto;
    public String Producto;
    public int Cantidad;
    public double Importe;

    public long getNumeroProducto() {
        return NumeroProducto;
    }

    public void setNumeroProducto(long numeroProducto) {
        NumeroProducto = numeroProducto;
    }

    public String getProducto() {
        return Producto;
    }

    public void setProducto(String producto) {
        Producto = producto;
    }

    public int getCantidad() {
        return Cantidad;
    }

    public void setCantidad(int cantidad) {
        Cantidad = cantidad;
    }

    public double getImporte() {
        return Importe;
    }

    public void setImporte(double importe) {
        Importe = importe;
    }
}
