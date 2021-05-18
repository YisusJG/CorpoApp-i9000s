package com.corpogas.corpoapp.Entities.HandHeld;

import com.corpogas.corpoapp.Entities.Sucursales.BranchPaymentMethod;

import java.io.Serializable;
import java.util.List;

public class ListaSucursalFormaPago implements Serializable {

    public List<BranchPaymentMethod> SucursalFormapagos;

    public List<BranchPaymentMethod> getSucursalFormapagos() { return SucursalFormapagos; }

    public void setSucursalFormapagos(List<BranchPaymentMethod> sucursalFormapagos) { SucursalFormapagos = sucursalFormapagos; }
}
