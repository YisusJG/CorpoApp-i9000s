package com.corpogas.corpoapp.Entities.Estaciones;

import com.corpogas.corpoapp.Entities.Catalogos.Product;
import com.corpogas.corpoapp.Entities.Sucursales.Branch;

import java.io.Serializable;
import java.util.List;

public class Combustible implements Serializable {

    public long SucursalId;
    public Branch Sucursal;
    public long EstacionId;
    public Estacion Estacion;
    public long ProductoId;
    public Product Producto;
    public int CombustibleCvi;
    public double LitrosConsigna;
    public String CodigoFranquicia;
    public int Octanaje;
    public boolean Etanol;
    public String Color;
    public List<PrecioCombustible> PreciosCombustibles;

    public long getSucursalId() { return SucursalId; }

    public void setSucursalId(long sucursalId) { SucursalId = sucursalId; }

    public Branch getSucursal() { return Sucursal; }

    public void setSucursal(Branch sucursal) { Sucursal = sucursal; }

    public long getEstacionId() {  return EstacionId; }

    public void setEstacionId(long estacionId) { EstacionId = estacionId; }

    public Estacion getEstacion() { return Estacion; }

    public void setEstacion(Estacion estacion) { Estacion = estacion; }

    public long getProductoId() { return ProductoId; }

    public void setProductoId(long productoId) { ProductoId = productoId; }

    public Product getProducto() { return Producto; }

    public void setProducto(Product producto) { Producto = producto; }

    public int getCombustibleCvi() { return CombustibleCvi; }

    public void setCombustibleCvi(int combustibleCvi) { CombustibleCvi = combustibleCvi; }

    public double getLitrosConsigna() { return LitrosConsigna; }

    public void setLitrosConsigna(double litrosConsigna) { LitrosConsigna = litrosConsigna; }

    public String getCodigoFranquicia() { return CodigoFranquicia; }

    public void setCodigoFranquicia(String codigoFranquicia) { CodigoFranquicia = codigoFranquicia; }

    public int getOctanaje() { return Octanaje; }

    public void setOctanaje(int octanaje) { Octanaje = octanaje; }

    public boolean isEtanol() { return Etanol; }

    public void setEtanol(boolean etanol) { Etanol = etanol; }

    public String getColor() { return Color; }

    public void setColor(String color) { Color = color; }

    public List<PrecioCombustible> getPreciosCombustibles() { return PreciosCombustibles; }

    public void setPreciosCombustibles(List<PrecioCombustible> preciosCombustibles) { PreciosCombustibles = preciosCombustibles; }
}
