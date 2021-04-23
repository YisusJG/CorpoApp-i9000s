package com.corpogas.corpoapp.Entities.Tickets;

import com.corpogas.corpoapp.Entities.Empresas.Empresa;
import com.corpogas.corpoapp.Entities.Empresas.EmpresaDomicilio;

import java.io.Serializable;

public class TicketCabecero implements Serializable {
    /// <summary>
    /// Empresa a la que pertenece la estacion de donde se emite el ticket
    /// </summary>
    private Empresa Empresa;
    /// <summary>
    /// Domicilio de la empresa
    /// </summary>
    private EmpresaDomicilio Domicilio;

    public com.corpogas.corpoapp.Entities.Empresas.Empresa getEmpresa() {
        return Empresa;
    }

    public void setEmpresa(com.corpogas.corpoapp.Entities.Empresas.Empresa empresa) {
        Empresa = empresa;
    }

    public EmpresaDomicilio getDomicilio() {
        return Domicilio;
    }

    public void setDomicilio(EmpresaDomicilio domicilio) {
        Domicilio = domicilio;
    }
}
