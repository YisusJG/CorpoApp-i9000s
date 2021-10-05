package com.corpogas.corpoapp.Facturacion.Entities;

import java.io.Serializable;

public class DomicilioEmpresaFactura implements Serializable {
    public String Calle;
    public String NumeroExterior;
    public String NumeroInterior;
    public String Colonia;
    public String Localidad;
    public String Municipio;
    public String EntidadFederativa;
    public String Pais;
    public String CodigoPostal;

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

    public String getMunicipio() {
        return Municipio;
    }

    public void setMunicipio(String municipio) {
        Municipio = municipio;
    }

    public String getEntidadFederativa() {
        return EntidadFederativa;
    }

    public void setEntidadFederativa(String entidadFederativa) {
        EntidadFederativa = entidadFederativa;
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
