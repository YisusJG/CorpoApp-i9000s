package com.corpogas.corpoapp.Entities.Sucursales;

import com.corpogas.corpoapp.Entities.Empresas.Company;

import java.io.Serializable;

public class Branch implements Serializable {

    public long CompanyId;
    public Company Company;
    public long ZoneId;
//    public Zone Zone;
    public long BusinessTypeId;
//    public BusinessType BusinessType;
    public String BranchNumber;
    public String FranchiseNumber;
    public String Ip;
    public String Name;
    public String Alias;
    public String Email;
    public double RegionalTax;
    public double LocalTax;
    public double Latitude;
    public double Longitude;
    public String OpeningDate;
    public boolean IsMatrix;

    //public long OldId;
    //public String ExternalId;
    public String ExternalCode;

    //Campos del domicilio
    public String ZipCode;
    public String Street;
    public String SuiteNumber;
    public String StreetNumber;
    public String Colony;
    public String Locality;
    public String Reference;
    public String County;
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

    public long getZoneId() {
        return ZoneId;
    }

    public void setZoneId(long zoneId) {
        ZoneId = zoneId;
    }

    public long getBusinessTypeId() {
        return BusinessTypeId;
    }

    public void setBusinessTypeId(long businessTypeId) {
        BusinessTypeId = businessTypeId;
    }

    public String getBranchNumber() {
        return BranchNumber;
    }

    public void setBranchNumber(String branchNumber) {
        BranchNumber = branchNumber;
    }

    public String getFranchiseNumber() {
        return FranchiseNumber;
    }

    public void setFranchiseNumber(String franchiseNumber) {
        FranchiseNumber = franchiseNumber;
    }

    public String getIp() {
        return Ip;
    }

    public void setIp(String ip) {
        Ip = ip;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getAlias() {
        return Alias;
    }

    public void setAlias(String alias) {
        Alias = alias;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public double getRegionalTax() {
        return RegionalTax;
    }

    public void setRegionalTax(double regionalTax) {
        RegionalTax = regionalTax;
    }

    public double getLocalTax() {
        return LocalTax;
    }

    public void setLocalTax(double localTax) {
        LocalTax = localTax;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }

    public String getOpeningDate() {
        return OpeningDate;
    }

    public void setOpeningDate(String openingDate) {
        OpeningDate = openingDate;
    }

    public boolean isMatrix() {
        return IsMatrix;
    }

    public void setMatrix(boolean matrix) {
        IsMatrix = matrix;
    }

    public String getExternalCode() {
        return ExternalCode;
    }

    public void setExternalCode(String externalCode) {
        ExternalCode = externalCode;
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

    public String getSuiteNumber() {
        return SuiteNumber;
    }

    public void setSuiteNumber(String suiteNumber) {
        SuiteNumber = suiteNumber;
    }

    public String getStreetNumber() {
        return StreetNumber;
    }

    public void setStreetNumber(String streetNumber) {
        StreetNumber = streetNumber;
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

    public String getCounty() {
        return County;
    }

    public void setCounty(String county) {
        County = county;
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
