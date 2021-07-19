package com.corpogas.corpoapp.Entities.Cortes;

import com.corpogas.corpoapp.Entities.Catalogos.PaymentMethod;
import com.corpogas.corpoapp.Entities.Estaciones.Estacion;
import com.corpogas.corpoapp.Entities.Sucursales.Branch;

import java.io.Serializable;

public class CierreFormaPago implements Serializable {

    public long SucursalId;
    public Branch Sucursal;
    public long EstacionId;
    public Estacion Estacion;
    public long CierreId;
    public Cierre Cierre;
    public long FormaPagoId;
    public PaymentMethod FormaPago;
    public double Cantidad;
    public int NumeroTickets;

    public long getSucursalId() {
        return SucursalId;
    }

    public void setSucursalId(long sucursalId) {
        SucursalId = sucursalId;
    }

    public Branch getSucursal() {
        return Sucursal;
    }

    public void setSucursal(Branch sucursal) {
        Sucursal = sucursal;
    }

    public long getEstacionId() {
        return EstacionId;
    }

    public void setEstacionId(long estacionId) {
        EstacionId = estacionId;
    }

    public com.corpogas.corpoapp.Entities.Estaciones.Estacion getEstacion() {
        return Estacion;
    }

    public void setEstacion(Estacion estacion) {
        Estacion = estacion;
    }

    public long getCierreId() {
        return CierreId;
    }

    public void setCierreId(long cierreId) {
        CierreId = cierreId;
    }

    public Cierre getCierre() {
        return Cierre;
    }

    public void setCierre(Cierre cierre) {
        Cierre = cierre;
    }

    public long getFormaPagoId() {
        return FormaPagoId;
    }

    public void setFormaPagoId(long formaPagoId) {
        FormaPagoId = formaPagoId;
    }

    public PaymentMethod getFormaPago() {
        return FormaPago;
    }

    public void setFormaPago(PaymentMethod formaPago) {
        FormaPago = formaPago;
    }

    public double getCantidad() {
        return Cantidad;
    }

    public void setCantidad(double cantidad) {
        Cantidad = cantidad;
    }

    public int getNumeroTickets() {
        return NumeroTickets;
    }

    public void setNumeroTickets(int numeroTickets) {
        NumeroTickets = numeroTickets;
    }
}
