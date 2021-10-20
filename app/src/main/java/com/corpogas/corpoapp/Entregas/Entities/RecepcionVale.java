package com.corpogas.corpoapp.Entregas.Entities;

import java.util.List;

public class RecepcionVale {
    public long SucursalId;
//    public virtual Branch Sucursal;
//    public long EstacionId;
//    public Estacion Estacion;
    public long PosicionCargaId;
//    public virtual PosicionCarga Isla;
//    public long TurnoId;
//    public virtual Shift Turno;
//    public DateTime FechaTrabajo;
//    public long EmpleadoEntregaId;
//    public virtual Empleado EmpleadoEntrega;
//    public long EmpleadoAutorizaId;
//    public virtual Empleado EmpleadoAutoriza;
//    public long TipoValePapelId;
//    public virtual PaperVoucherType TipoValePapel;
//    public int Cantidad;
//    public decimal Denominacion;
//    public virtual PaperVoucherDenomination DenominacionValePapel;
    public List<ValePapel> ValesPapelRecepcion;

    public long getSucursalId() {
        return SucursalId;
    }

    public void setSucursalId(long sucursalId) {
        SucursalId = sucursalId;
    }

    public long getPosicionCargaId() {
        return PosicionCargaId;
    }

    public void setPosicionCargaId(long posicionCargaId) {
        PosicionCargaId = posicionCargaId;
    }

    public List<ValePapel> getValesPapelRecepcion() {
        return ValesPapelRecepcion;
    }

    public void setValesPapelRecepcion(List<ValePapel> valesPapelRecepcion) {
        ValesPapelRecepcion = valesPapelRecepcion;
    }
}
