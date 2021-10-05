package com.corpogas.corpoapp.Facturacion.Entities;

import java.io.Serializable;

public class Emisor implements Serializable {

    public String RazonSocial;
    public String RFC;
    public String RegimenFiscal ;
    public DomicilioEmpresaFactura DomicilioEmpresaFactura;

    public String getRazonSocial() {
        return RazonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        RazonSocial = razonSocial;
    }

    public String getRFC() {
        return RFC;
    }

    public void setRFC(String RFC) {
        this.RFC = RFC;
    }

    public String getRegimenFiscal() {
        return RegimenFiscal;
    }

    public void setRegimenFiscal(String regimenFiscal) {
        RegimenFiscal = regimenFiscal;
    }

    public DomicilioEmpresaFactura getDomicilioEmpresaFactura() {
        return DomicilioEmpresaFactura;
    }

    public void setDomicilioEmpresaFactura(DomicilioEmpresaFactura domicilioEmpresaFactura) {
        DomicilioEmpresaFactura = domicilioEmpresaFactura;
    }
}
