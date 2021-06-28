package com.corpogas.corpoapp.Entities.Estaciones;

import com.corpogas.corpoapp.Entities.Sucursales.Branch;
import com.corpogas.corpoapp.Entities.Sucursales.Shift;

import java.io.Serializable;

public class EstacionControl implements Serializable {

    public long SucursalId;
    public Branch Sucursal;
    public long EstacionId;
    public Estacion Estacion;
    public long LlaveroId;
    public Llavero Llavero;
    public long IslaId;
    public Isla Isla;
    public long TurnoId;
    public Shift Turno;
    public long EmpleadoId;
    public Empleado Empleado;

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

    public long getLlaveroId() {
        return LlaveroId;
    }

    public void setLlaveroId(long llaveroId) {
        LlaveroId = llaveroId;
    }

    public com.corpogas.corpoapp.Entities.Estaciones.Llavero getLlavero() {
        return Llavero;
    }

    public void setLlavero(com.corpogas.corpoapp.Entities.Estaciones.Llavero llavero) {
        Llavero = llavero;
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

    public long getEmpleadoId() {
        return EmpleadoId;
    }

    public void setEmpleadoId(long empleadoId) {
        EmpleadoId = empleadoId;
    }

    public com.corpogas.corpoapp.Entities.Estaciones.Empleado getEmpleado() {
        return Empleado;
    }

    public void setEmpleado(com.corpogas.corpoapp.Entities.Estaciones.Empleado empleado) {
        Empleado = empleado;
    }
}
