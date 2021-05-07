package com.corpogas.corpoapp.Entities.Empresas;

import java.io.Serializable;

public class Company implements Serializable {
    public long TaxRegimeId;
//    public TaxRegime TaxRegime;
    public long GroupId;
    public Group Group;
    public String Rfc;
    public String CommercialName;

    /// <summary>
    /// Domicilio vigente de la empresa
    /// </summary>
//    public List<CompanyAddress> CompanyAdresses;


    public long getTaxRegimeId() {
        return TaxRegimeId;
    }

    public void setTaxRegimeId(long taxRegimeId) {
        TaxRegimeId = taxRegimeId;
    }

    public long getGroupId() {
        return GroupId;
    }

    public void setGroupId(long groupId) {
        GroupId = groupId;
    }

    public com.corpogas.corpoapp.Entities.Empresas.Group getGroup() {
        return Group;
    }

    public void setGroup(com.corpogas.corpoapp.Entities.Empresas.Group group) {
        Group = group;
    }

    public String getRfc() {
        return Rfc;
    }

    public void setRfc(String rfc) {
        Rfc = rfc;
    }

    public String getCommercialName() {
        return CommercialName;
    }

    public void setCommercialName(String commercialName) {
        CommercialName = commercialName;
    }
}
