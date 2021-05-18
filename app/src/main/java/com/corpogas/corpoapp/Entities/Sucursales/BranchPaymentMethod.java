package com.corpogas.corpoapp.Entities.Sucursales;

import com.corpogas.corpoapp.Entities.Catalogos.PaymentMethod;


import java.io.Serializable;

public class BranchPaymentMethod<T> implements Serializable {

    public long BranchId;
    public Branch Branch;
    public long PaymentMethodId;
    public PaymentMethod PaymentMethod;

    public long getBranchId() { return BranchId; }

    public void setBranchId(long branchId) { BranchId = branchId; }

    public Branch getBranch() { return Branch; }

    public void setBranch(Branch branch) { Branch = branch; }

    public long getPaymentMethodId() { return PaymentMethodId; }

    public void setPaymentMethodId(long paymentMethodId) { PaymentMethodId = paymentMethodId; }

    public PaymentMethod getPaymentMethod() { return PaymentMethod; }

    public void setPaymentMethod(PaymentMethod paymentMethod) { PaymentMethod = paymentMethod; }
}
