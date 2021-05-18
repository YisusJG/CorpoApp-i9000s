package com.corpogas.corpoapp.Entities.Accesos;

import java.io.Serializable;
import java.util.List;

public class Control implements Serializable {

    /// <summary>
    /// Identificador de la entidad Llavero
    /// NOTA: Id de la tabla(este es computado)
    /// </summary> 
    public long LlaveroId;

    /// <summary>
    /// Numero interno del llavero en base hexadecimal
    /// Ejemplos:
    /// 01C02088
    /// 01C02065
    /// 000A0000
    /// </summary>
    public String NumeroInternoLlavero;

    /// <summary>
    /// Identificador de la entidad Turno
    /// NOTA: Id de la tabla(este es computado)
    /// </summary> 
    public long TurnoId;

    /// <summary>
    /// Número interno de control del turno en la sucursal
    /// Ejemplos: 1,2,3,etc...
    /// </summary>
    public long NumeroInternoTurno;

    /// <summary>
    /// Identificador de la entidad Isla
    /// NOTA: Id de la tabla(este es computado)
    /// </summary> 
    public long IslaId;

    /// <summary>
    /// Número interno de control de la isla en la sucursal
    /// Ejemplos: 1,2,3,etc...
    /// </summary>
    public long NumeroInternoIsla;

    /// <summary>
    /// Lista de controles asignados al usuario.
    /// Dicho listado corresponde a los datos existentes en la tabla Islas
    /// </summary>
    public List<Posicion> Posiciones;
}
