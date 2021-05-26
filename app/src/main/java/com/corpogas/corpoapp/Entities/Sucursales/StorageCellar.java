package com.corpogas.corpoapp.Entities.Sucursales;

import java.io.Serializable;

public class StorageCellar implements Serializable {

    public long BranchId;
    public Branch Branch;
    public int StorageCellarNumber;

    public long getBranchId() { return BranchId; }

    public void setBranchId(long branchId) { BranchId = branchId; }

    public Branch getBranch() { return Branch; }

    public void setBranch(Branch branch) { Branch = branch; }

    public int getStorageCellarNumber() { return StorageCellarNumber; }

    public void setStorageCellarNumber(int storageCellarNumber) { StorageCellarNumber = storageCellarNumber; }
}
