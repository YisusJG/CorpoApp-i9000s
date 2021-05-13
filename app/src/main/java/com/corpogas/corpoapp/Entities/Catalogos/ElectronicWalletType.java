package com.corpogas.corpoapp.Entities.Catalogos;

public class ElectronicWalletType {

    /// <summary>
    /// Identificador de la entidad TipoConexion
    /// </summary>

 //    public TipoConexion ConnectionTypeId; CHECAR

    /// <summary>
    /// Identificador de la entidad TipoConexion
    /// </summary>
    public long PaymentMethodId;

    /// <summary>
    /// Relacion con la entidad de TipoConexion
    /// Llave foranea
    /// </summary>
    public PaymentMethod PaymentMethod;

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
}
