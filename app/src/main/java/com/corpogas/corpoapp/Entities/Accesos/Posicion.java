package com.corpogas.corpoapp.Entities.Accesos;

import java.io.Serializable;
import java.util.List;

public class Posicion implements Serializable {

    /// <summary>
    /// Identificador de la entidad PosicionCarga
    /// NOTA: Id de la tabla(este es computado)
    /// </summary> 
    public long PosicionCargaId;

    /// <summary>
    /// Número interno de control de la posicion de carga
    /// Ejemplos: 1,2,3,etc...
    /// </summary>
    public long NumeroInterno;

    /// <summary>
    /// Bandera que indica si la posicion de carga se encuentra disponible
    /// </summary>
    public boolean Disponible;

    /// <summary>
    /// Bandera que indica si la posicion de carga ya se encuentra en pendiente de cobro
    /// </summary>
    public boolean PendienteCobro;

    /// <summary>
    /// Operacion que se esta realizando en la posicion de carga (tanque lleno, puntada...)
    /// Numero interno del tipo de transaccion
    /// </summary>
    public long Operativa;

    /// <summary>
    /// Descripcion de la operativa en la que se encuentra la posicion de carga
    /// </summary>
    public String DescripcionOperativa;

    /// <summary>
    /// Descripcion del estado de la posicion de carga
    /// </summary>
    public String Descripcion;

    /// <summary>
    /// Bandera que indica si la posicion de carga tiene permitido cargar productos
    /// </summary>
    public boolean PermiteProductos;

    /// <summary>
    /// Lista de controles asignados al usuario.
    /// Dicho listado corresponde a los datos existentes en la tabla Islas
    /// </summary>
    public List<MangueraPorPosicion> Mangueras;
}
