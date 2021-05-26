package com.corpogas.corpoapp.Entities.Estaciones;

import com.corpogas.corpoapp.Entities.Sucursales.Branch;

import java.io.Serializable;

public class Estacion implements Serializable {
    /// <summary>
    /// Identificador de la entidad Sucursal
    /// NOTA: Id de la tabla(este es computado)
    /// </summary>
    private long SucursalId;

    /// <summary>
    /// Relacion con la entidad de Sucursal del esquema Sucursales
    /// Llave foranea
    /// </summary>
    private Branch Sucursal;

    /// <summary>
    /// Numero del permiso de la estacion ante la Cre
    /// </summary>
    private String PermisoCre;

    /// <summary>
    /// Numero SIIC de la estacion
    /// NOTA: El Siic solo estara presente para estaciones Pemex
    /// </summary>
    private String Siic;

    /// <summary>
    /// Bandera que indica si la estacion maneja aditivo
    /// </summary>
    private boolean UsaAditivo;

    /// <summary>
    /// Bandera que indica si la estacion tiene tabla de cubicaciones
    /// </summary>
    private boolean TablaCubicaciones;

    /// <summary>
    /// Relación de Estaciones con islas
    /// </summary>
//    private List<Isla> Islas;

    /// <summary>
    /// Credenciales para conexión a distintos servicios
    /// </summary>
//    private List<CredencialConexion> CredencialConexiones;


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

    public String getPermisoCre() {
        return PermisoCre;
    }

    public void setPermisoCre(String permisoCre) {
        PermisoCre = permisoCre;
    }

    public String getSiic() {
        return Siic;
    }

    public void setSiic(String siic) {
        Siic = siic;
    }

    public boolean isUsaAditivo() {
        return UsaAditivo;
    }

    public void setUsaAditivo(boolean usaAditivo) {
        UsaAditivo = usaAditivo;
    }

    public boolean isTablaCubicaciones() {
        return TablaCubicaciones;
    }

    public void setTablaCubicaciones(boolean tablaCubicaciones) { TablaCubicaciones = tablaCubicaciones; }
}
