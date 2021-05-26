package com.corpogas.corpoapp.Entities.Sucursales;

import java.io.Serializable;

public class Shift implements Serializable {

    public long BranchId;
    public Branch Branch;
    public int ShiftNumber;
//    public TimeSpan StartTime;
//    public TimeSpan EndTime;


    public long getBranchId() { return BranchId; }

    public void setBranchId(long branchId) { BranchId = branchId; }

    public Branch getBranch() { return Branch; }

    public void setBranch(Branch branch) { Branch = branch; }

    public int getShiftNumber() { return ShiftNumber; }

    public void setShiftNumber(int shiftNumber) { ShiftNumber = shiftNumber; }
}
