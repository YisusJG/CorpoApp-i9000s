package com.corpogas.corpoapp.Entities.Catalogos;

public class Bin {

    public long Id;

    /// <summary>
    /// Identificador de la entidad TipoMonedero
    /// NOTA: Id de la tabla(este es computado)
    /// </summary> 
    public long TipoMonederoId;

    /// <summary>
    /// Relacion con la entidad de TipoMonedero del esquema catalogos
    /// Llave foranea
    /// </summary>
    public ElectronicWalletType TipoMonedero;

    /// <summary>
    /// Mascara de la tarjeta de tal manera que
    /// los asteriscos representan cualquier valor numerico
    /// y los otros valores deben de aparecer en el orden especificado
    /// ademas recordando que la suma total de caracteres debe ser igual a la
    /// especificada en la mascara
    /// de tal manera que 
    /// Ejemplos: 
    /// 399999********** TanqueLleno centro
    /// 399996********** TanqueLleno Sureste
    /// 400000********** Puntada
    /// 3XXXXX********** Donde:
    ///             XXXXX = # Estacion Cliente estacion
    /// </summary>
    public String Mascara;

    /// <summary>
    /// Indicador que indica la pista donde se encuentra el bin esperado
    /// </summary>
    public int Pista;

    /// <summary>
    /// Bandera que indica si el registro es de un numero de tarjeta
    /// </summary>
    public boolean NumeroTarjeta;
}
