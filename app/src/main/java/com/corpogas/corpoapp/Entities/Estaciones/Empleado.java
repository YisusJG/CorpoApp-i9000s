package com.corpogas.corpoapp.Entities.Estaciones;

import com.corpogas.corpoapp.Entities.Catalogos.Role;
import com.corpogas.corpoapp.Entities.Sucursales.Branch;

import java.io.Serializable;
import java.util.List;

public class Empleado implements Serializable {

    public long SucursalId;
    public Branch Sucursal;
    public long EstacionId;
    public Estacion Estacion;
    public long RolId;
    public Role Rol;
    public String Nombre;
    public String ApellidoPaterno;
    public String ApellidoMaterno;
    public String NombreCompleto;
    public List<Huella> Huellas;
    public long Id;

    /// <summary>
    /// Clave para accesar a aplicaciones
    /// Ejemplo: ****** 
    /// </summary>
    public String Clave;

    /// <summary>
    /// Bandera para ver si esta activo el usuario
    /// </summary>
    public boolean Activo;

    /// <summary>
    /// Correo del usuario
    /// Ejemplo: antonio.reyes@corpogas.com.mx
    /// </summary>
    public String Correo;

    /// <summary>
    /// Numero asociado a cada usuario en el central
    /// Id de las base de datos central de empleados
    /// Ejemplo: 9182
    /// </summary>
    public String NumeroEmpleado;

    public Empleado(long sucursalId, long estacionId, long rolId, String nombre, String apellidoPaterno, String apellidoMaterno, String nombreCompleto, long id,
                    String clave, boolean activo, String correo, String numeroEmpleado){

        this.SucursalId = sucursalId;
        this.EstacionId = estacionId;
        this.RolId = rolId;
        this.Nombre = nombre;
        this.ApellidoPaterno = apellidoPaterno;
        this.ApellidoMaterno = apellidoMaterno;
        this.NombreCompleto = nombreCompleto;
        this.Id = id;
        this.Clave = clave;
        this.Activo = activo;
        this.Correo = correo;
        this.NumeroEmpleado = numeroEmpleado;

    }

    public long getSucursalId() {
        return SucursalId;
    }

    public void setSucursalId(long sucursalId) {
        SucursalId = sucursalId;
    }

    public Branch getSucursal() {
        return Sucursal;
    }

    public void setSucursal(Branch sucursal) {
        Sucursal = sucursal;
    }

    public long getEstacionId() {
        return EstacionId;
    }

    public void setEstacionId(long estacionId) {
        EstacionId = estacionId;
    }

    public com.corpogas.corpoapp.Entities.Estaciones.Estacion getEstacion() {
        return Estacion;
    }

    public void setEstacion(com.corpogas.corpoapp.Entities.Estaciones.Estacion estacion) {
        Estacion = estacion;
    }

    public long getRolId() {
        return RolId;
    }

    public void setRolId(long rolId) {
        RolId = rolId;
    }

    public Role getRol() {
        return Rol;
    }

    public void setRol(Role rol) {
        Rol = rol;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getApellidoPaterno() {
        return ApellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        ApellidoPaterno = apellidoPaterno;
    }

    public String getApellidoMaterno() {
        return ApellidoMaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        ApellidoMaterno = apellidoMaterno;
    }

    public String getNombreCompleto() {
        return NombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        NombreCompleto = nombreCompleto;
    }

    public List<Huella> getHuellas() {
        return Huellas;
    }

    public void setHuellas(List<Huella> huellas) {
        Huellas = huellas;
    }

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public String getClave() {
        return Clave;
    }

    public void setClave(String clave) {
        Clave = clave;
    }

    public boolean isActivo() {
        return Activo;
    }

    public void setActivo(boolean activo) {
        Activo = activo;
    }

    public String getCorreo() {
        return Correo;
    }

    public void setCorreo(String correo) {
        Correo = correo;
    }

    public String getNumeroEmpleado() {
        return NumeroEmpleado;
    }

    public void setNumeroEmpleado(String numeroEmpleado) {
        NumeroEmpleado = numeroEmpleado;
    }
}
