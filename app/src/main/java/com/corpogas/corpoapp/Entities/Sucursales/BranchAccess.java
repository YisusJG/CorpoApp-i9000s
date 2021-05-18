package com.corpogas.corpoapp.Entities.Sucursales;

import com.corpogas.corpoapp.Entities.Catalogos.Role;

import java.io.Serializable;

public class BranchAccess implements Serializable {

    public long BranchId;
    public Branch Branch;
    public long RoleId;
    public Role Role;
    public long ApplicationId;
    public Application Application;
    public boolean IsAdministrator;

    public long getBranchId() { return BranchId; }

    public void setBranchId(long branchId) { BranchId = branchId; }

    public Branch getBranch() { return Branch; }

    public void setBranch(Branch branch) { Branch = branch; }

    public long getRoleId() { return RoleId; }

    public void setRoleId(long roleId) { RoleId = roleId; }

    public Role getRole() { return Role; }

    public void setRole(Role role) { Role = role; }

    public long getApplicationId() { return ApplicationId; }

    public void setApplicationId(long applicationId) { ApplicationId = applicationId; }

    public Application getApplication() { return Application; }

    public void setApplication(Application application) { Application = application; }

    public boolean isAdministrator() { return IsAdministrator; }

    public void setAdministrator(boolean administrator) { IsAdministrator = administrator; }
}

