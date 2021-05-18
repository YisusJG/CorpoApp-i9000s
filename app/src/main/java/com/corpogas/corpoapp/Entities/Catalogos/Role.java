package com.corpogas.corpoapp.Entities.Catalogos;

import com.corpogas.corpoapp.Entities.Sucursales.BranchAccess;

import java.io.Serializable;
import java.util.List;

public class Role implements Serializable {

    public String Name;
    public String Description;

    /// <summary>
    /// Listado de Accesos por cada Rol
    /// </summary>
    public List<BranchAccess> BranchAccesses;

    public String getName() { return Name; }

    public void setName(String name) { Name = name; }

    public String getDescription() { return Description; }

    public void setDescription(String description) { Description = description; }

    public List<BranchAccess> getBranchAccesses() { return BranchAccesses; }

    public void setBranchAccesses(List<BranchAccess> branchAccesses) { BranchAccesses = branchAccesses; }
}
