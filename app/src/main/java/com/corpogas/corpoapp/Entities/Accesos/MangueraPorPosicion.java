package com.corpogas.corpoapp.Entities.Accesos;

import java.io.Serializable;

public class MangueraPorPosicion implements Serializable {

    /// <summary>
    /// Identificador de la entidad PosicionCarga
    /// NOTA: Id de la tabla(este es computado)
    /// </summary> 
    public long MangueraId;

    /// <summary>
    /// Número interno de control de la manguera
    /// Ejemplos: 1,2,3,etc...
    /// </summary>
    public long NumeroInterno;

    /// <summary>
    /// Identificador de la entidad EstacionCombustible
    /// </summary>
    public long EstacionCombustibleId;

    /// <summary>
    /// Numero interno del combustible en la estacion
    /// </summary>
    public long NumeroInternoEstacionCombustible;

    /// <summary>
    /// Nombre del combustible
    /// </summary>
    public String DescripcionCombustible;
}
