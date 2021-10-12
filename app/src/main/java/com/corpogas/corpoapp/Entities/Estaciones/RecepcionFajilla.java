package com.corpogas.corpoapp.Entities.Estaciones;

import com.corpogas.corpoapp.Entities.Sucursales.Branch;
import com.corpogas.corpoapp.Entities.Sucursales.Shift;

import java.io.Serializable;
import java.util.List;

public class RecepcionFajilla implements Serializable {

    public long SucursalId;
    public Branch Sucursal;
    public long EstacionId;
    public Estacion Estacion;
    public long IslaId;
    public Isla Isla;
    public long TurnoId;
    public Shift Turno;
//    public DateTime FechaTrabajo;
    public long EmpleadoEntregaId;
    public Empleado EmpleadoEntrega;
    public long EmpleadoAutorizaId;
    public Empleado EmpleadoAutoriza;
    public long TipoFajilla;
    public int Cantidad;
    public double Denominacion;
    public double Importe;
    public double PicosMonedas;
    public int Total;

    public RecepcionFajilla(long sucursalId, long turnoId, long tipoFajilla, int cantidad, double denominacion, double picosMonedas, int total){
        this.SucursalId = sucursalId;
        this.TurnoId = turnoId;
        this.TipoFajilla = tipoFajilla;
        this.Cantidad = cantidad;
        this.Denominacion = denominacion;
        this.PicosMonedas = picosMonedas;
        this.Total = total;

    }

    public RecepcionFajilla(){

    }

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

    public long getEmpleadoEntregaId() {
        return EmpleadoEntregaId;
    }

    public void setEmpleadoEntregaId(long empleadoEntregaId) {
        EmpleadoEntregaId = empleadoEntregaId;
    }

    public Empleado getEmpleadoEntrega() {
        return EmpleadoEntrega;
    }

    public void setEmpleadoEntrega(Empleado empleadoEntrega) {
        EmpleadoEntrega = empleadoEntrega;
    }

    public long getEmpleadoAutorizaId() {
        return EmpleadoAutorizaId;
    }

    public void setEmpleadoAutorizaId(long empleadoAutorizaId) {
        EmpleadoAutorizaId = empleadoAutorizaId;
    }

    public Empleado getEmpleadoAutoriza() {
        return EmpleadoAutoriza;
    }

    public void setEmpleadoAutoriza(Empleado empleadoAutoriza) {
        EmpleadoAutoriza = empleadoAutoriza;
    }

    public long getTipoFajilla() {
        return TipoFajilla;
    }

    public void setTipoFajilla(long tipoFajilla) {
        TipoFajilla = tipoFajilla;
    }

    public int getCantidad() {
        return Cantidad;
    }

    public void setCantidad(int cantidad) {
        Cantidad = cantidad;
    }

    public double getDenominacion() {
        return Denominacion;
    }

    public void setDenominacion(double denominacion) {
        Denominacion = denominacion;
    }

    public double getImporte() {
        return Importe;
    }

    public void setImporte(double importe) {
        Importe = importe;
    }

    public double getPicosMonedas() {
        return PicosMonedas;
    }

    public void setPicosMonedas(double picosMonedas) {
        PicosMonedas = picosMonedas;
    }

    public int getTotal() {
        return Total;
    }

    public void setTotal(int total) {
        Total = total;
    }
}
