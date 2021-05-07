package com.corpogas.corpoapp.Entities.Tickets;

import com.corpogas.corpoapp.Entities.Empresas.Company;
import com.corpogas.corpoapp.Entities.Empresas.CompanyAddress;
import com.corpogas.corpoapp.Entities.Empresas.Empresa;
import com.corpogas.corpoapp.Entities.Empresas.EmpresaDomicilio;

import java.io.Serializable;

public class TicketCabecero implements Serializable {
    /// <summary>
    /// Empresa a la que pertenece la estacion de donde se emite el ticket
    /// </summary>
    private Company Empresa;
    /// <summary>
    /// Domicilio de la empresa
    /// </summary>
    private CompanyAddress Domicilio;

    public Company getEmpresa() {
        return Empresa;
    }

    public void setEmpresa(Company empresa) {
        Empresa = empresa;
    }

    public CompanyAddress getDomicilio() {
        return Domicilio;
    }

    public void setDomicilio(CompanyAddress domicilio) {
        Domicilio = domicilio;
    }
}
