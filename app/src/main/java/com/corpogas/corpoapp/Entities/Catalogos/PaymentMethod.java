package com.corpogas.corpoapp.Entities.Catalogos;

public class PaymentMethod {

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
}
