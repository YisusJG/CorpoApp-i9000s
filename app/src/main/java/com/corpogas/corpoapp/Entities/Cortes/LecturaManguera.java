package com.corpogas.corpoapp.Entities.Cortes;

import com.corpogas.corpoapp.Entities.Catalogos.Product;
import com.corpogas.corpoapp.Entities.Estaciones.Estacion;
import com.corpogas.corpoapp.Entities.Estaciones.Manguera;
import com.corpogas.corpoapp.Entities.Sucursales.Branch;
import com.corpogas.corpoapp.Entities.Sucursales.Shift;

import java.io.Serializable;

public class LecturaManguera implements Serializable {

    public long SucursalId;
    public Branch Sucursal;
    public long EstacionId;
    public Estacion Estacion;
    public long MangueraId;
    public Manguera Manguera;
    public long ProductoId;
    public Product Producto;
    public long TurnoId;
    public Shift Turno;
//    public TipoLectura TipoLectura;
    public double Precio;
    public double ValorInicial;
    public double ValorFinal;
//    public DateTime FechaTrabajo;
//    public DateTime FechaTurno;
    public int TurnoAuxiliar;
    public boolean CorteTurno;

    public long getSucursalId() { return SucursalId; }

    public void setSucursalId(long sucursalId) { SucursalId = sucursalId; }

    public Branch getSucursal() { return Sucursal; }

    public void setSucursal(Branch sucursal) { Sucursal = sucursal; }

    public long getEstacionId() { return EstacionId; }

    public void setEstacionId(long estacionId) { EstacionId = estacionId; }

    public Estacion getEstacion() {
        return Estacion;
    }

    public void setEstacion(Estacion estacion) {
        Estacion = estacion;
    }

    public long getMangueraId() {
        return MangueraId;
    }

    public void setMangueraId(long mangueraId) {
        MangueraId = mangueraId;
    }

    public Manguera getManguera() {
        return Manguera;
    }

    public void setManguera(Manguera manguera) {
        Manguera = manguera;
    }

    public long getProductoId() {
        return ProductoId;
    }

    public void setProductoId(long productoId) {
        ProductoId = productoId;
    }

    public Product getProducto() {
        return Producto;
    }

    public void setProducto(Product producto) {
        Producto = producto;
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

    public double getPrecio() {
        return Precio;
    }

    public void setPrecio(double precio) {
        Precio = precio;
    }

    public double getValorInicial() {
        return ValorInicial;
    }

    public void setValorInicial(double valorInicial) {
        ValorInicial = valorInicial;
    }

    public double getValorFinal() {
        return ValorFinal;
    }

    public void setValorFinal(double valorFinal) {
        ValorFinal = valorFinal;
    }

    public int getTurnoAuxiliar() {
        return TurnoAuxiliar;
    }

    public void setTurnoAuxiliar(int turnoAuxiliar) {
        TurnoAuxiliar = turnoAuxiliar;
    }

    public boolean isCorteTurno() {
        return CorteTurno;
    }

    public void setCorteTurno(boolean corteTurno) {
        CorteTurno = corteTurno;
    }
}
