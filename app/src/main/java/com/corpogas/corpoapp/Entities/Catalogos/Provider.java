package com.corpogas.corpoapp.Entities.Catalogos;

import com.corpogas.corpoapp.Entities.Empresas.Company;

import java.io.Serializable;

public class Provider implements Serializable {

    public long CompanyId;
    public Company Company;
    public String Description;
    public int CreditDays;
    public String Rfc;

    public long getCompanyId() { return CompanyId; }

    public void setCompanyId(long companyId) { CompanyId = companyId; }

    public Company getCompany() { return Company; }

    public void setCompany(Company company) { Company = company; }

    public String getDescription() { return Description; }

    public void setDescription(String description) { Description = description; }

    public int getCreditDays() { return CreditDays; }

    public void setCreditDays(int creditDays) { CreditDays = creditDays; }

    public String getRfc() { return Rfc; }

    public void setRfc(String rfc) { Rfc = rfc; }
}
