package com.corpogas.corpoapp.Entities.Cortes;

import com.corpogas.corpoapp.Entities.Estaciones.Combustible;

import java.io.Serializable;

public class CierreDespachoDetalle implements Serializable {

    public long CierreId;

    /// <summary>
    /// Identificador de la entidad Sucursal
    /// NOTA: Id de la tabla(este es computado)
    /// </summary>
    public long CierreSucursalId;

//    public Cierre Cierre;


    /// <summary>
    /// Identificador de la entidad Sucursal
    /// NOTA: Id de la tabla(este es computado)
    /// </summary>
    public long SucursalId;

    /// <summary>
    /// Relacion con la entidad de Sucursal del esquema Sucursales
    /// Llave foranea
    /// </summary>
//    public Sucursal Sucursal;

    /// <summary>
    /// Identificador de la entidad Combustible
    /// NOTA: Id de la tabla(este es computado)
    /// </summary>

    /// <summary>
    /// Id de la manguera
    /// </summary>
    public long MangueraId;

    /// <summary>
    /// Estacion de la manguera
    /// </summary>
    public long MangueraEstacionId;

    /// <summary>
    /// Relacion con el catalogo de mangueras
    /// </summary>
//    public Manguera Manguera;

    public long CombustibleId;

    /// <summary>
    /// Relacion con la entidad de Combustible del esquema Catalogos
    /// Llave foranea
    /// </summary>
    public Combustible Combustible;

    /*
    public long ProductoId { get; set; }
    public virtual Producto Producto { get; set; }
    */
    public double Precio;

    public double Cantidad;


    public double Total;

    public double Iva;

    public double Ieps;

    public long getCierreId() { return CierreId; }

    public void setCierreId(long cierreId) { CierreId = cierreId; }

    public long getCierreSucursalId() { return CierreSucursalId; }

    public void setCierreSucursalId(long cierreSucursalId) { CierreSucursalId = cierreSucursalId; }

    public long getSucursalId() { return SucursalId; }

    public void setSucursalId(long sucursalId) { SucursalId = sucursalId; }

    public long getMangueraId() { return MangueraId; }

    public void setMangueraId(long mangueraId) { MangueraId = mangueraId; }

    public long getMangueraEstacionId() { return MangueraEstacionId; }

    public void setMangueraEstacionId(long mangueraEstacionId) { MangueraEstacionId = mangueraEstacionId; }

    public long getCombustibleId() { return CombustibleId; }

    public void setCombustibleId(long combustibleId) { CombustibleId = combustibleId; }

    public Combustible getCombustible() { return Combustible; }

    public void setCombustible(Combustible combustible) { Combustible = combustible; }

    public double getPrecio() { return Precio; }

    public void setPrecio(double precio) { Precio = precio; }

    public double getCantidad() { return Cantidad; }

    public void setCantidad(double cantidad) { Cantidad = cantidad; }

    public double getTotal() { return Total; }

    public void setTotal(double total) { Total = total; }

    public double getIva() { return Iva; }

    public void setIva(double iva) { Iva = iva; }

    public double getIeps() { return Ieps; }

    public void setIeps(double ieps) { Ieps = ieps; }
}
