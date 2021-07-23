package com.corpogas.corpoapp.Entities.Cortes;

import com.corpogas.corpoapp.Entities.Classes.EntityBase;
import com.corpogas.corpoapp.Entities.Estaciones.Estacion;
import com.corpogas.corpoapp.Entities.Estaciones.Isla;
import com.corpogas.corpoapp.Entities.Sucursales.Branch;
import com.corpogas.corpoapp.Entities.Sucursales.Shift;
import com.corpogas.corpoapp.Entities.Virtuales.CierreVariables;

import java.io.Serializable;
import java.util.List;

public class Cierre implements Serializable {

    public long SucursalId;
    public Branch Sucursal;
    public long EstacionId;
    public Estacion Estacion;
    public long TurnoId;
    public Shift Turno;
    public long IslaId;
    public Isla Isla;
    public int Transacciones;
    public double TotalVenta;
    public double TotalIva;
    public double TotalIeps;
    public boolean Completado;
    public long Id;
//    public DateTime FechaTrabajo;


//    public List<CierreDetalle> CierreDetalles;
//    public List<CierreDespachoDetalle> CierreCombustibleDetalles;
//    public List<CierreFormaPago> CierreFormaPagos;
    public List<CierreCarrete> CierreCarretes;
//    public List<CierreFajilla> CierreFajillas;
//    public List<CierreValePapel> CierreValePapeles;
//    public List<CierreDetallePeriferico> CierreDetallePerifericos;
//    public List<CierreDetalle> CierreDetalleCategoriaProducto;

    public CierreVariables Variables;
//    public GranTotal Totales;


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

    public void setEstacion(com.corpogas.corpoapp.Entities.Estaciones.Estacion estacion) {
        Estacion = estacion;
    }

    public long getTurnoId() {
        return TurnoId;
    }

    public void setTurnoId(long turnoId) {
        TurnoId = turnoId;
    }

    public Shift getTurno() {
        return Turno;
    }

    public void setTurno(Shift turno) {
        Turno = turno;
    }

    public long getIslaId() {
        return IslaId;
    }

    public void setIslaId(long islaId) {
        IslaId = islaId;
    }

    public com.corpogas.corpoapp.Entities.Estaciones.Isla getIsla() {
        return Isla;
    }

    public void setIsla(com.corpogas.corpoapp.Entities.Estaciones.Isla isla) {
        Isla = isla;
    }

    public int getTransacciones() {
        return Transacciones;
    }

    public void setTransacciones(int transacciones) {
        Transacciones = transacciones;
    }

    public double getTotalVenta() {
        return TotalVenta;
    }

    public void setTotalVenta(double totalVenta) {
        TotalVenta = totalVenta;
    }

    public double getTotalIva() {
        return TotalIva;
    }

    public void setTotalIva(double totalIva) {
        TotalIva = totalIva;
    }

    public double getTotalIeps() {
        return TotalIeps;
    }

    public void setTotalIeps(double totalIeps) {
        TotalIeps = totalIeps;
    }

    public boolean isCompletado() {
        return Completado;
    }

    public void setCompletado(boolean completado) {
        Completado = completado;
    }

    public List<CierreCarrete> getCierreCarretes() {
        return CierreCarretes;
    }

    public void setCierreCarretes(List<CierreCarrete> cierreCarretes) {
        CierreCarretes = cierreCarretes;
    }

    public CierreVariables getVariables() {
        return Variables;
    }

    public void setVariables(CierreVariables variables) {
        Variables = variables;
    }

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }
}
