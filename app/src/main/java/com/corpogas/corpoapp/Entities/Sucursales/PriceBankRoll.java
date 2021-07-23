package com.corpogas.corpoapp.Entities.Sucursales;

import com.corpogas.corpoapp.Entities.TipoFajilla;

import java.io.Serializable;

public class PriceBankRoll implements Serializable {

    public long BranchId;
    public Branch Branch;
    public Long BankRollType;
    public int Price;

    public long getBranchId() {
        return BranchId;
    }

    public void setBranchId(long branchId) {
        BranchId = branchId;
    }

    public com.corpogas.corpoapp.Entities.Sucursales.Branch getBranch() {
        return Branch;
    }

    public void setBranch(com.corpogas.corpoapp.Entities.Sucursales.Branch branch) {
        Branch = branch;
    }

    public Long getBankRollType() {
        return BankRollType;
    }

    public void setBankRollType(Long bankRollType) {
        BankRollType = bankRollType;
    }

    public int getPrice() {
        return Price;
    }

    public void setPrice(int price) {
        Price = price;
    }
}


