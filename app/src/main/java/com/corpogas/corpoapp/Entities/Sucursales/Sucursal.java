package com.corpogas.corpoapp.Entities.Sucursales;

import com.corpogas.corpoapp.Entities.Empresas.Empresa;

import java.io.Serializable;

public class Sucursal implements Serializable {

    /// <summary>
    /// Identificador de la entidad Empresa
    /// NOTA: Id de la tabla(este es computado)
    /// </summary> 
    private long EmpresaId;

    /// <summary>
    /// Relacion con la entidad de Empresa del esquema Empresas
    /// Llave foranea
    /// </summary>
    private com.corpogas.corpoapp.Entities.Empresas.Empresa Empresa ;

    /// <summary>
    /// Numero interno de control
    /// Ejemplo: 13711 de la estacion de Bolonia
    /// </summary>
    private String NumeroInterno ;

    /// <summary>
    /// Numero de franquicia
    /// Ejemplo: 13711 de la estacion de bolonia
    /// </summary>
    private String NumeroFranquicia ;

    /// <summary>
    /// Ip de la estacion
    /// Ejemplo: 10.2.148.1
    /// </summary>
    private String Ip ;

    /// <summary>
    /// Nombre de la sucursal
    /// Ejemplo: Bolonia
    /// </summary>
    private String Nombre ;

    /// <summary>
    /// Correo de contacto de la sucursal
    /// Ejemplo: es13711@corpogas.com.mx
    /// </summary>
    private String Correo ;

    /// <summary>
    /// IVA regional manejado en la estacion
    /// </summary>
    private double IvaRegional ;

    /// <summary>
    /// IVA local manejado en la estacion
    /// </summary>
    private double IvaLocal ;

    /// <summary>
    /// Coordenada geografica de latitud(y) donde se ubica
    /// Ejemplo: 19.40542 (Bolonia)
    /// </summary>
    private double Latitud ;

    /// <summary>
    /// Bandera que indica si es matriza sino se debe considerar como sucursal
    /// </summary>
    private boolean Matriz ;

    /// <summary>
    /// Coordenada geografica de longitud(x) donde se ubica
    /// Ejemplo: -99.19241 (Bolonia)
    /// </summary>
    private double Longitud ;

    /// <summary>
    /// Fecha en la que inicio operaciones la sucursal
    /// </summary>
    private String InicioOperaciones ;

    /// <summary>
    /// Relación con la tabla formas de pago
    /// </summary>
//    private  List<SucursalFormaPago> EmpresaFormaPagos ;

    /// <summary>
    /// Relación con la tabla EmpresaProductos
    /// </summary>
//    private  List<SucursalProducto> EmpresaProductos ;

    /// <summary>
    /// Relación con la tabla SucursalTipoTransaccion
    /// </summary>
//    private  List<SucursalTipoTransaccion> EmpresaTipoTransacciones ;

    /// <summary>
    /// Relación con la tabla EmpresaEstaciones
    /// </summary>
//    private List<Estacion> EmpresaEstaciones ;

    /// <summary>
    /// Relación con la tabla Turnos
    /// </summary>
//    private  List<Turno> Turnos ;

    /// <summary>
    /// Telefonos asociados a la sucursal
    /// </summary>
//    private  List<SucursalTelefono> SucursalTelefonos ;


    public long getEmpresaId() {
        return EmpresaId;
    }

    public void setEmpresaId(long empresaId) {
        EmpresaId = empresaId;
    }

    public Empresa getEmpresa() {
        return Empresa;
    }

    public void setEmpresa(Empresa empresa) {
        Empresa = empresa;
    }

    public String getNumeroInterno() {
        return NumeroInterno;
    }

    public void setNumeroInterno(String numeroInterno) {
        NumeroInterno = numeroInterno;
    }

    public String getNumeroFranquicia() {
        return NumeroFranquicia;
    }

    public void setNumeroFranquicia(String numeroFranquicia) {
        NumeroFranquicia = numeroFranquicia;
    }

    public String getIp() {
        return Ip;
    }

    public void setIp(String ip) {
        Ip = ip;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getCorreo() {
        return Correo;
    }

    public void setCorreo(String correo) {
        Correo = correo;
    }

    public double getIvaRegional() {
        return IvaRegional;
    }

    public void setIvaRegional(double ivaRegional) {
        IvaRegional = ivaRegional;
    }

    public double getIvaLocal() {
        return IvaLocal;
    }

    public void setIvaLocal(double ivaLocal) {
        IvaLocal = ivaLocal;
    }

    public double getLatitud() {
        return Latitud;
    }

    public void setLatitud(double latitud) {
        Latitud = latitud;
    }

    public boolean isMatriz() {
        return Matriz;
    }

    public void setMatriz(boolean matriz) {
        Matriz = matriz;
    }

    public double getLongitud() {
        return Longitud;
    }

    public void setLongitud(double longitud) {
        Longitud = longitud;
    }

    public String getInicioOperaciones() {
        return InicioOperaciones;
    }

    public void setInicioOperaciones(String inicioOperaciones) {
        InicioOperaciones = inicioOperaciones;
    }
}
