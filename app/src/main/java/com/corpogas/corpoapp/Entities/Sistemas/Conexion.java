package com.corpogas.corpoapp.Entities.Sistemas;

import com.corpogas.corpoapp.Entities.Sucursales.Branch;

import java.io.Serializable;

public class Conexion implements Serializable {
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
    /// Identificador de la entidad TipoDispositivo
    /// NOTA: Id de la tabla(este es computado)
    /// </summary>
    private long TipoConexionId;

    /// <summary>
    /// Relacion con la entidad de TipoDispositivo del esquema Catalogos
    /// Llave foranea
    /// </summary>
//    private TipoConexion TipoConexion;

    /// <summary>
    /// Ip de la conexion remota
    /// </summary>
    private String Ip;

    /// <summary>
    /// Puerto de conexion con el dispositivo
    /// </summary>
    private int Puerto;

    /// <summary>
    ///  Numeral que identifica a las conexiones del mismo tipo
    ///  Ej: Tarjetero "1" y tarjetero "3"
    /// </summary>
    private int Consecutivo;

    /// <summary>
    /// Mac de la conexion remota
    /// NOTA: Esto es necesario para la autentificacion de los tarjeteros
    /// </summary>
    private String DireccionMac;
    /// <summary>
    /// Campo concatenado de la dirección mac e IP que se determina al guardar o configurar la HH
    /// </summary>

    private String PropiedadConexion;
    /// <summary>
    /// Determina el tipo Url para configurar la HH  proporcionando la Audiencia y el issuer
    /// </summary>
    private String Url;
    /// <summary>
    /// Id que se genera en c# con entity
    /// </summary>
    private long Id;


    public Conexion(long sucursalId, long tipoConexionId, String direccionMac) {
        SucursalId = sucursalId;
        TipoConexionId = tipoConexionId;
        DireccionMac = direccionMac;
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

    public long getTipoConexionId() {
        return TipoConexionId;
    }

    public void setTipoConexionId(long tipoConexionId) {
        TipoConexionId = tipoConexionId;
    }

    public String getIp() {
        return Ip;
    }

    public void setIp(String ip) {
        Ip = ip;
    }

    public int getPuerto() {
        return Puerto;
    }

    public void setPuerto(int puerto) {
        Puerto = puerto;
    }

    public int getConsecutivo() {
        return Consecutivo;
    }

    public void setConsecutivo(int consecutivo) {
        Consecutivo = consecutivo;
    }

    public String getDireccionMac() {
        return DireccionMac;
    }

    public void setDireccionMac(String direccionMac) {
        DireccionMac = direccionMac;
    }

    public String getPropiedadConexion() {
        return PropiedadConexion;
    }

    public void setPropiedadConexion(String propiedadConexion) {
        PropiedadConexion = propiedadConexion;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }
}
