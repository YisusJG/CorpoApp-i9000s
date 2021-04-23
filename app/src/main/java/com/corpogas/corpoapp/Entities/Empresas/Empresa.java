package com.corpogas.corpoapp.Entities.Empresas;

import java.io.Serializable;


public class Empresa implements Serializable {
    /// <summary>
    /// Identificador de la entidad Grupo
    /// NOTA: Id de la tabla(este es computado)
    /// </summary> 
    private long GrupoId;

    /// <summary>
    /// Relacion con la entidad de Rol del esquema Grupo
    /// Llave foranea
    /// </summary>
    private Grupo Grupo;

    /// <summary>
    /// Identificador de la entidad TipoRegimenFiscal
    /// NOTA: Id de la tabla(este es computado)
    /// </summary> 
    private long TipoRegimenFiscalId;

    /// <summary>
    /// Relacion con la entidad de TipoRegimenFiscal del esquema Catalogos
    /// Llave foranea
    /// </summary>
//    private TipoRegimenFiscal TipoRegimenFiscal;

    /// <summary>
    /// Coordenada geografica de latitud(y) donde se ubica
    /// Ejemplo: 19.40542 (Bolonia)
    /// </summary>
    private double Latitud;

    /// <summary>
    /// Coordenada geografica de longitud(x) donde se ubica
    /// Ejemplo: -99.19241 (Bolonia)
    /// </summary>
    private double Longitud;

    /// <summary>
    /// Rfc de la sucursal
    /// Ejemplo: 
    /// </summary>
    private String Rfc;

    /// <summary>
    /// Nombre de la Razón social
    /// Ejemplo: Estacion de servicio Bolonia SA de CV
    /// </summary>
    private String RazonSocial;

    /// <summary>
    /// Correo de contacto de la empresa
    /// Ejemplo: es13711@corpogas.com.mx
    /// </summary>
    private String Correo;


    /// <summary>
    /// Domicilio vigente de la empresa
    /// </summary>
//    private List<EmpresaDomicilio> EmpresaDomicilios;


    public long getGrupoId() {
        return GrupoId;
    }

    public void setGrupoId(long grupoId) {
        GrupoId = grupoId;
    }

    public com.corpogas.corpoapp.Entities.Empresas.Grupo getGrupo() {
        return Grupo;
    }

    public void setGrupo(com.corpogas.corpoapp.Entities.Empresas.Grupo grupo) {
        Grupo = grupo;
    }

    public long getTipoRegimenFiscalId() {
        return TipoRegimenFiscalId;
    }

    public void setTipoRegimenFiscalId(long tipoRegimenFiscalId) {
        TipoRegimenFiscalId = tipoRegimenFiscalId;
    }

    public double getLatitud() {
        return Latitud;
    }

    public void setLatitud(double latitud) {
        Latitud = latitud;
    }

    public double getLongitud() {
        return Longitud;
    }

    public void setLongitud(double longitud) {
        Longitud = longitud;
    }

    public String getRfc() {
        return Rfc;
    }

    public void setRfc(String rfc) {
        Rfc = rfc;
    }

    public String getRazonSocial() {
        return RazonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        RazonSocial = razonSocial;
    }

    public String getCorreo() {
        return Correo;
    }

    public void setCorreo(String correo) {
        Correo = correo;
    }
}
