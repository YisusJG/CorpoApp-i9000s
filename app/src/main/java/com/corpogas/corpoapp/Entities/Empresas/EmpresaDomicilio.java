package com.corpogas.corpoapp.Entities.Empresas;

import java.io.Serializable;

public class EmpresaDomicilio implements Serializable {
    /// <summary>
    /// Identificador de la entidad Empresa
    /// NOTA: Id de la tabla(este es computado)
    /// </summary> 
    private long EmpresaId;

    /// <summary>
    /// Relacion con la entidad de Empresa del esquema Empresas
    /// Llave foranea
    /// </summary>
    private Empresa Empresa;

    /// <summary>
    /// Identificador de la entidad TipoDomicilio
    /// NOTA: Id de la tabla(este es computado)
    /// </summary> 
    private long TipoDomicilioId;

    /// <summary>
    /// Identificador de la llave compuesta de la entidad TipoDomicilio
    /// NOTA: Id de la tabla(este NO es computado)
    /// </summary>
//    private TipoDomicilio TipoDomicilio;

    /// <summary>
    /// Calle de la estacion
    /// Ejemplo: Boulevard Adolfo Lopez Mateos
    /// </summary>
    private String Calle;

    /// <summary>
    /// Numero exterior
    /// Ejemplo: 112
    /// </summary>
    private String NumeroExterior;

    /// <summary>
    /// Numero interior
    /// Ejemplo: 112
    /// </summary>
    private String NumeroInterior;

    /// <summary>
    /// Colonia
    /// Ejemplo: Observatorio
    /// </summary>
    private String Colonia;

    /// <summary>
    /// Localidad
    /// Ejemplo: ¿¿??
    /// </summary>
    private String Localidad;

    /// <summary>
    /// Referencia de la estacion
    /// Ejemplo:
    /// </summary>
    private String Referencia;

    /// <summary>
    /// Municipio en el que se encuentra la empresa
    /// Ejemplo: Miguel Hidalgo
    /// </summary>
    private String Municipio;

    /// <summary>
    /// Estado donde se ubica la empresa
    /// Ejemplo: Ciudad de mexico
    /// </summary>
    private String Estado;

    /// <summary>
    /// Pais donde se ubica la empresa
    /// Ejemplo: Mexico
    /// </summary>
    private String Pais;

    /// <summary>
    /// Codigo Postal de la empresa
    /// Ejemplo: 11860
    /// </summary>
    private String CodigoPostal;


    public long getEmpresaId() {
        return EmpresaId;
    }

    public void setEmpresaId(long empresaId) {
        EmpresaId = empresaId;
    }

    public com.corpogas.corpoapp.Entities.Empresas.Empresa getEmpresa() {
        return Empresa;
    }

    public void setEmpresa(com.corpogas.corpoapp.Entities.Empresas.Empresa empresa) {
        Empresa = empresa;
    }

    public long getTipoDomicilioId() {
        return TipoDomicilioId;
    }

    public void setTipoDomicilioId(long tipoDomicilioId) {
        TipoDomicilioId = tipoDomicilioId;
    }

    public String getCalle() {
        return Calle;
    }

    public void setCalle(String calle) {
        Calle = calle;
    }

    public String getNumeroExterior() {
        return NumeroExterior;
    }

    public void setNumeroExterior(String numeroExterior) {
        NumeroExterior = numeroExterior;
    }

    public String getNumeroInterior() {
        return NumeroInterior;
    }

    public void setNumeroInterior(String numeroInterior) {
        NumeroInterior = numeroInterior;
    }

    public String getColonia() {
        return Colonia;
    }

    public void setColonia(String colonia) {
        Colonia = colonia;
    }

    public String getLocalidad() {
        return Localidad;
    }

    public void setLocalidad(String localidad) {
        Localidad = localidad;
    }

    public String getReferencia() {
        return Referencia;
    }

    public void setReferencia(String referencia) {
        Referencia = referencia;
    }

    public String getMunicipio() {
        return Municipio;
    }

    public void setMunicipio(String municipio) {
        Municipio = municipio;
    }

    public String getEstado() {
        return Estado;
    }

    public void setEstado(String estado) {
        Estado = estado;
    }

    public String getPais() {
        return Pais;
    }

    public void setPais(String pais) {
        Pais = pais;
    }

    public String getCodigoPostal() {
        return CodigoPostal;
    }

    public void setCodigoPostal(String codigoPostal) {
        CodigoPostal = codigoPostal;
    }
}
