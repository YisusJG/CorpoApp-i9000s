package com.corpogas.corpoapp.Entities.Estaciones;

import com.corpogas.corpoapp.Entities.Sucursales.Branch;

import java.io.Serializable;

public class PosicionCarga implements Serializable {

    public long SucursalId;
    public Branch Sucursal;
    public long EstacionId;
    public Estacion Estacion;
    public long IslaId;
    public Isla Isla;
    public int NumeroPosicionCarga;
    public String Marca;
    public String Modelo;
    public String NumeroSerie;
    public int NumeroDispensario;
    public int NumeroAntena;
    public int MaximoLitros;
    public double MaximoImporte;
    public boolean Arillos;
    public boolean Llaveros;
    public boolean LectorHuella;
    public boolean AutoServicio;

    public long getSucursalId() { return SucursalId; }

    public void setSucursalId(long sucursalId) { SucursalId = sucursalId; }

    public Branch getSucursal() { return Sucursal; }

    public void setSucursal(Branch sucursal) { Sucursal = sucursal; }

    public long getEstacionId() { return EstacionId; }

    public void setEstacionId(long estacionId) { EstacionId = estacionId; }

    public Estacion getEstacion() { return Estacion; }

    public void setEstacion(Estacion estacion) { Estacion = estacion; }

    public long getIslaId() { return IslaId; }

    public void setIslaId(long islaId) { IslaId = islaId; }

    public Isla getIsla() { return Isla; }

    public void setIsla(Isla isla) { Isla = isla; }

    public int getNumeroPosicionCarga() { return NumeroPosicionCarga; }

    public void setNumeroPosicionCarga(int numeroPosicionCarga) { NumeroPosicionCarga = numeroPosicionCarga; }

    public String getMarca() { return Marca; }

    public void setMarca(String marca) { Marca = marca; }

    public String getModelo() { return Modelo; }

    public void setModelo(String modelo) { Modelo = modelo; }

    public String getNumeroSerie() { return NumeroSerie; }

    public void setNumeroSerie(String numeroSerie) { NumeroSerie = numeroSerie; }

    public int getNumeroDispensario() { return NumeroDispensario; }

    public void setNumeroDispensario(int numeroDispensario) { NumeroDispensario = numeroDispensario; }

    public int getNumeroAntena() { return NumeroAntena; }

    public void setNumeroAntena(int numeroAntena) { NumeroAntena = numeroAntena; }

    public int getMaximoLitros() { return MaximoLitros; }

    public void setMaximoLitros(int maximoLitros) { MaximoLitros = maximoLitros; }

    public double getMaximoImporte() { return MaximoImporte; }

    public void setMaximoImporte(double maximoImporte) { MaximoImporte = maximoImporte; }

    public boolean isArillos() { return Arillos; }

    public void setArillos(boolean arillos) { Arillos = arillos; }

    public boolean isLlaveros() { return Llaveros; }

    public void setLlaveros(boolean llaveros) { Llaveros = llaveros; }

    public boolean isLectorHuella() { return LectorHuella; }

    public void setLectorHuella(boolean lectorHuella) { LectorHuella = lectorHuella; }

    public boolean isAutoServicio() { return AutoServicio; }

    public void setAutoServicio(boolean autoServicio) { AutoServicio = autoServicio; }
}
