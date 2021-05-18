package com.corpogas.corpoapp.Entities.Estaciones;

import com.corpogas.corpoapp.Entities.Sucursales.Branch;

import java.io.Serializable;

public class Huella implements Serializable {

    public long SucursalId;
    public Branch Sucursal;
    public long EstacionId;
    public Estacion Estacion;
    public long EmpleadoId;
    public Empleado Empleado;
//    public TipoBiometrico TipoBiometricoId;
    public byte[] HuellaDerecha;
    public byte[] HuellaIzquierda;

    public long getSucursalId() { return SucursalId; }

    public void setSucursalId(long sucursalId) { SucursalId = sucursalId; }

    public Branch getSucursal() { return Sucursal; }

    public void setSucursal(Branch sucursal) { Sucursal = sucursal; }

    public long getEstacionId() { return EstacionId; }

    public void setEstacionId(long estacionId) { EstacionId = estacionId; }

    public Estacion getEstacion() { return Estacion; }

    public void setEstacion(Estacion estacion) { Estacion = estacion; }

    public long getEmpleadoId() { return EmpleadoId; }

    public void setEmpleadoId(long empleadoId) { EmpleadoId = empleadoId; }

    public Empleado getEmpleado() { return Empleado; }

    public void setEmpleado(Empleado empleado) { Empleado = empleado; }

    public byte[] getHuellaDerecha() { return HuellaDerecha; }

    public void setHuellaDerecha(byte[] huellaDerecha) { HuellaDerecha = huellaDerecha; }

    public byte[] getHuellaIzquierda() { return HuellaIzquierda; }

    public void setHuellaIzquierda(byte[] huellaIzquierda) { HuellaIzquierda = huellaIzquierda; }
}
