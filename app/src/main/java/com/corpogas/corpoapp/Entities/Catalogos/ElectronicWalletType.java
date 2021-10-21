package com.corpogas.corpoapp.Entities.Catalogos;

import java.io.Serializable;

public class ElectronicWalletType implements Serializable {


    /// <summary>
    /// Identificador de la entidad TipoConexion
    /// </summary>
    public long PaymentMethodId;

    /// <summary>
    /// Relacion con la entidad de TipoConexion
    /// Llave foranea
    /// </summary>
    public volatile PaymentMethod PaymentMethod;

    /// <summary>
    /// Description del monedero, de como sera conocido en la sucursal
    /// Ejemplo:
    /// ->TANQUE LLENO CENTRO
    /// ->TANQUE LLENO SURESTE
    /// ->PUNTADA
    /// ->YENA
    /// ->GASOMATIC
    /// ->ETC..
    /// </summary>
    public String Description;

    /// <summary>
    /// Identificador de entidad de cobro para la gestion de control de saldos
    /// ->99999 (TANQUE LLENO CENTRO )
    /// ->99996 (TANQUE LLENO SURESTE)
    /// ->99997 (PUNTADA)
    /// </summary>
    public String OriginStation;

    public long Id;

    public long getPaymentMethodId() {
        return PaymentMethodId;
    }

    public void setPaymentMethodId(long paymentMethodId) {
        PaymentMethodId = paymentMethodId;
    }

    public com.corpogas.corpoapp.Entities.Catalogos.PaymentMethod getPaymentMethod() {
        return PaymentMethod;
    }

    public void setPaymentMethod(com.corpogas.corpoapp.Entities.Catalogos.PaymentMethod paymentMethod) {
        PaymentMethod = paymentMethod;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getOriginStation() {
        return OriginStation;
    }

    public void setOriginStation(String originStation) {
        OriginStation = originStation;
    }

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }
}
