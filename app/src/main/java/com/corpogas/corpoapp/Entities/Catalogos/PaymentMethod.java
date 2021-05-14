package com.corpogas.corpoapp.Entities.Catalogos;

import java.io.Serializable;

public class PaymentMethod implements Serializable {

    /// <summary>
    /// Identificador de la entidad SatPaymentMethod
    /// </summary>
    public long SatPaymentMethodId;

    /// <summary>
    /// Relacion con la entidad de SatPaymentMethod
    /// Llave foranea
    /// </summary>
    public SatPaymentMethod SatPaymentMethod;

    /// <summary>
    /// Name corto de la forma de pago que se puede usar en tickets
    /// Ejemplo: 
    /// ->AMEX
    /// </summary>
    public String ShortDescription;

    /// <summary>
    /// Name completo de la forma de pago 
    /// Ejemplo:
    /// ->AMERICAN EXPRESS
    /// </summary>
    public String LongDescription;

    /// <summary>
    /// Numero de tickets que debe generar en el punto de venta cuando se emite el ticket asociado a esta forma de pago
    /// NOTA: Por cada parcialidad, debido a que la segunda copia usualmente es el voucher de la tarjeta
    /// Ejemplo: 
    /// ->1
    /// ->2
    /// ->3
    /// </summary>
    /// </summary>
    public int PrintsAllowed;

    /// <summary>
    /// Bandera que indica si la forma de pago es facturable o no
    /// </summary>
    public boolean IsBillable;

    /// <summary>
    /// Bandera que indica si la forma de pago es de tipo bancario 
    /// </summary>
    public boolean Autorization;

    /// <summary>
    /// Bandera que indica si la forma de pago es visible desde el medio de captura de ventas(tarjetero, hand held, punto de venta, etc)
    /// NOTA1: En la estacion de servicio se utiliza para evitar que los despachadores marquen las transacciones con formas de pago marcadas por el sistema como lo son: jarreo, consigna, monedero,etc
    /// NOTA2: En la estacion de servicio esto ayuda a mostrar un numero reducido de formas de pago, ya que en las tpv Verifone el limite son 8
    /// </summary>
    public boolean IsFrontVisible;

    /// <summary>
    /// Valor usado para mapearlo en la base de datos anterior
    /// </summary>
    public int MappingValue;

    public long getSatPaymentMethodId() {
        return SatPaymentMethodId;
    }

    public void setSatPaymentMethodId(long satPaymentMethodId) {
        SatPaymentMethodId = satPaymentMethodId;
    }

    public com.corpogas.corpoapp.Entities.Catalogos.SatPaymentMethod getSatPaymentMethod() {
        return SatPaymentMethod;
    }

    public void setSatPaymentMethod(com.corpogas.corpoapp.Entities.Catalogos.SatPaymentMethod satPaymentMethod) {
        SatPaymentMethod = satPaymentMethod;
    }

    public String getShortDescription() {
        return ShortDescription;
    }

    public void setShortDescription(String shortDescription) {
        ShortDescription = shortDescription;
    }

    public String getLongDescription() {
        return LongDescription;
    }

    public void setLongDescription(String longDescription) {
        LongDescription = longDescription;
    }

    public int getPrintsAllowed() {
        return PrintsAllowed;
    }

    public void setPrintsAllowed(int printsAllowed) {
        PrintsAllowed = printsAllowed;
    }

    public boolean isBillable() {
        return IsBillable;
    }

    public void setBillable(boolean billable) {
        IsBillable = billable;
    }

    public boolean isAutorization() {
        return Autorization;
    }

    public void setAutorization(boolean autorization) {
        Autorization = autorization;
    }

    public boolean isFrontVisible() {
        return IsFrontVisible;
    }

    public void setFrontVisible(boolean frontVisible) {
        IsFrontVisible = frontVisible;
    }

    public int getMappingValue() {
        return MappingValue;
    }

    public void setMappingValue(int mappingValue) {
        MappingValue = mappingValue;
    }
}
