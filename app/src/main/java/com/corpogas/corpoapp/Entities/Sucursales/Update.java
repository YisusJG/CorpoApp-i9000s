package com.corpogas.corpoapp.Entities.Sucursales;

import java.io.Serializable;
import java.util.List;

public class Update implements Serializable {

    public long BranchId;
//    public Branch Branch;
    public String UpdateReason;
    public String AplicationDate;
    public List<UpdateDetail> UpdateDetails;

    public long getBranchId() {
        return BranchId;
    }

    public void setBranchId(long branchId) {
        BranchId = branchId;
    }

    public String getUpdateReason() {
        return UpdateReason;
    }

    public void setUpdateReason(String updateReason) {
        UpdateReason = updateReason;
    }

    public String getAplicationDate() {
        return AplicationDate;
    }

    public void setAplicationDate(String aplicationDate) {
        AplicationDate = aplicationDate;
    }

    public List<UpdateDetail> getUpdateDetails() {
        return UpdateDetails;
    }

    public void setUpdateDetails(List<UpdateDetail> updateDetails) {
        UpdateDetails = updateDetails;
    }
}
