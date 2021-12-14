package com.corpogas.corpoapp.Entities.Sistemas;

import android.app.Application;

import com.corpogas.corpoapp.Entities.Estaciones.Estacion;
import com.corpogas.corpoapp.Entities.Sucursales.Branch;

import java.io.Serializable;

public class ConfiguracionAplicacion implements Serializable {

    public long SucursalId;
//    public Branch Sucursal;
    public long EstacionId;
//    public Estacion Estacion;
    public long AplicacionId;
//    public Application Aplicacion;
    public String AliasDispositivo;
    public String DireccionMac;
//    public long IslaId;
//    public Isla Isla;
    public long CategoriaCombustibleId;
    public boolean LectorHuella;
    public boolean Maximizado;
    public int NumeroTeriminal;
//    public List<ConfiguracionAplicacionDetalle> ConfiguracionAplicacionDetalles;
    public long Id;
    public boolean Imprimelocal;

    public ConfiguracionAplicacion(long sucursalId, long estacionId, long aplicacionId, String aliasDispositivo, String direccionMac, long categoriaCombustibleId, boolean lectorHuella, boolean maximizado, int numeroTeriminal) {
        SucursalId = sucursalId;
        EstacionId = estacionId;
        AplicacionId = aplicacionId;
        AliasDispositivo = aliasDispositivo;
        DireccionMac = direccionMac;
        CategoriaCombustibleId = categoriaCombustibleId;
        LectorHuella = lectorHuella;
        Maximizado = maximizado;
        NumeroTeriminal = numeroTeriminal;
    }

    public Boolean getImprimeLocal() { return Imprimelocal; }

    public long getSucursalId() {
        return SucursalId;
    }

    public void setSucursalId(long sucursalId) {
        SucursalId = sucursalId;
    }

    public long getEstacionId() {
        return EstacionId;
    }

    public void setEstacionId(long estacionId) {
        EstacionId = estacionId;
    }

    public long getAplicacionId() {
        return AplicacionId;
    }

    public void setAplicacionId(long aplicacionId) {
        AplicacionId = aplicacionId;
    }

    public String getAliasDispositivo() {
        return AliasDispositivo;
    }

    public void setAliasDispositivo(String aliasDispositivo) {
        AliasDispositivo = aliasDispositivo;
    }

    public String getDireccionMac() {
        return DireccionMac;
    }

    public void setDireccionMac(String direccionMac) {
        DireccionMac = direccionMac;
    }

    public long getCategoriaCombustibleId() {
        return CategoriaCombustibleId;
    }

    public void setCategoriaCombustibleId(long categoriaCombustibleId) {
        CategoriaCombustibleId = categoriaCombustibleId;
    }

    public boolean isLectorHuella() {
        return LectorHuella;
    }

    public void setLectorHuella(boolean lectorHuella) {
        LectorHuella = lectorHuella;
    }

    public boolean isMaximizado() {
        return Maximizado;
    }

    public void setMaximizado(boolean maximizado) {
        Maximizado = maximizado;
    }

    public int getNumeroTeriminal() {
        return NumeroTeriminal;
    }

    public void setNumeroTeriminal(int numeroTeriminal) {
        NumeroTeriminal = numeroTeriminal;
    }

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }
}
