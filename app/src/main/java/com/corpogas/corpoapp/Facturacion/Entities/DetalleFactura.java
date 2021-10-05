package com.corpogas.corpoapp.Facturacion.Entities;

import java.io.Serializable;

public class DetalleFactura implements Serializable {
    public double Cantidad;
    public String ClaveProdutoServicio;
    public String ClaveUnidad;
    public String Unidad;
    public String NoIdentificacion;
    public String Descripcion;
    public double ValorUnitario;
    public String Descuento;
    public double Importe;

    public double getCantidad() {
        return Cantidad;
    }

    public void setCantidad(double cantidad) {
        Cantidad = cantidad;
    }

    public String getClaveProdutoServicio() {
        return ClaveProdutoServicio;
    }

    public void setClaveProdutoServicio(String claveProdutoServicio) {
        ClaveProdutoServicio = claveProdutoServicio;
    }

    public String getClaveUnidad() {
        return ClaveUnidad;
    }

    public void setClaveUnidad(String claveUnidad) {
        ClaveUnidad = claveUnidad;
    }

    public String getUnidad() {
        return Unidad;
    }

    public void setUnidad(String unidad) {
        Unidad = unidad;
    }

    public String getNoIdentificacion() {
        return NoIdentificacion;
    }

    public void setNoIdentificacion(String noIdentificacion) {
        NoIdentificacion = noIdentificacion;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public double getValorUnitario() {
        return ValorUnitario;
    }

    public void setValorUnitario(double valorUnitario) {
        ValorUnitario = valorUnitario;
    }

    public String getDescuento() {
        return Descuento;
    }

    public void setDescuento(String descuento) {
        Descuento = descuento;
    }

    public double getImporte() {
        return Importe;
    }

    public void setImporte(double importe) {
        Importe = importe;
    }
}
