package com.corpogas.corpoapp.Entities.Accesos;

import java.io.Serializable;
import java.util.List;

public class AccesoUsuario implements Serializable {

    /// <summary>
    /// Identificador de la entidad SucursalEmpleado
    /// NOTA: Id de la tabla(este es computado)
    /// </summary> 
    public long SucursalEmpleadoId;

    /// <summary>
    /// Nombres del usuario
    /// Ejemplo: Porfirio Antonio
    /// </summary>
    public String Nombre;

    /// <summary>
    /// Apellido Paterno
    /// Ejemplo: Salinas
    /// </summary>
    public String ApellidoPaterno;

    /// <summary>
    /// Apellido Materno
    /// Ejemplo: Reyes
    /// </summary>
    public String ApellidoMaterno;


    /// <summary>
    /// Propiedad de solo lectura para obtener el nombre completo del usuario
    /// </summary>
    public String NombreCompleto;

    /// <summary>
    /// Clave para accesar a aplicaciones
    /// Ejemplo: ****** 
    /// </summary>
    public String Clave;

    /// <summary>
    /// Identificador de la entidad Rol
    /// NOTA: Id de la tabla(este es computado)
    /// </summary> 
    public long RolId;

    /// <summary>
    /// Numero interno del rol del usuario
    /// </summary>
    public long NumeroInternoRol;

    /// <summary>
    /// Nombre del puesto
    /// Ejemplos:
    /// *Gerente
    /// *Despachador
    /// *Jefe de isla
    /// *Sistema
    /// *etc...
    /// </summary>
    public String DescripcionRol;

    /// <summary>
    /// Bandera que indica si en la estacion se encuentra en jarreo ante una autoridad
    /// </summary>
    public boolean EstacionJarreo;

    /// <summary>
    /// Numero del empleado en el central de empleados
    /// </summary>
    public String NumeroEmpleado;

    /// <summary>
    /// Lista de controles asignados al usuario en el turno anterior.
    /// Dicho listado corresponde a los datos existentes en la tabla EstacionControles
    /// </summary>
    public List<Control> ControlesTurnoAnterior;

    /// <summary>
    /// Lista de controles asignados al usuario.
    /// Dicho listado corresponde a los datos existentes en la tabla EstacionControles
    /// </summary>
    public List<Control> Controles;
}
