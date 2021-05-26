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

    public long getMangueraId() { return MangueraId; }

    public void setMangueraId(long mangueraId) { MangueraId = mangueraId; }

    public long getNumeroInterno() { return NumeroInterno; }

    public void setNumeroInterno(long numeroInterno) { NumeroInterno = numeroInterno; }

    public long getEstacionCombustibleId() { return EstacionCombustibleId; }

    public void setEstacionCombustibleId(long estacionCombustibleId) { EstacionCombustibleId = estacionCombustibleId; }

    public long getNumeroInternoEstacionCombustible() { return NumeroInternoEstacionCombustible; }

    public void setNumeroInternoEstacionCombustible(long numeroInternoEstacionCombustible) { NumeroInternoEstacionCombustible = numeroInternoEstacionCombustible; }

    public String getDescripcionCombustible() { return DescripcionCombustible; }

    public void setDescripcionCombustible(String descripcionCombustible) { DescripcionCombustible = descripcionCombustible; }
}
