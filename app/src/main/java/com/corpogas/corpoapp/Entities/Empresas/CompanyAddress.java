package com.corpogas.corpoapp.Entities.Empresas;

import java.io.Serializable;

public class CompanyAddress implements Serializable {
    public long CompanyId;
    public Company Company;
//    public TipoDomicilios AddressTypeId;
    public String ZipCode;
    public String Street;
    public String StreetNumber;
    public String SuiteNumber;
    public String Colony;
    public String Locality;
    public String Reference;
    public String Municipality;
    public String State;
    public String Country;

    public long getCompanyId() {
        return CompanyId;
    }

    public void setCompanyId(long companyId) {
        CompanyId = companyId;
    }

    public com.corpogas.corpoapp.Entities.Empresas.Company getCompany() {
        return Company;
    }

    public void setCompany(com.corpogas.corpoapp.Entities.Empresas.Company company) {
        Company = company;
    }

    public String getZipCode() {
        return ZipCode;
    }

    public void setZipCode(String zipCode) {
        ZipCode = zipCode;
    }

    public String getStreet() {
        return Street;
    }

    public void setStreet(String street) {
        Street = street;
    }

    public String getStreetNumber() {
        return StreetNumber;
    }

    public void setStreetNumber(String streetNumber) {
        StreetNumber = streetNumber;
    }

    public String getSuiteNumber() {
        return SuiteNumber;
    }

    public void setSuiteNumber(String suiteNumber) {
        SuiteNumber = suiteNumber;
    }

    public String getColony() {
        return Colony;
    }

    public void setColony(String colony) {
        Colony = colony;
    }

    public String getLocality() {
        return Locality;
    }

    public void setLocality(String locality) {
        Locality = locality;
    }

    public String getReference() {
        return Reference;
    }

    public void setReference(String reference) {
        Reference = reference;
    }

    public String getMunicipality() {
        return Municipality;
    }

    public void setMunicipality(String municipality) {
        Municipality = municipality;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }
}

