package com.corpogas.corpoapp.Entities.Cortes;

import java.io.Serializable;

public class ValePapelDenominacion implements Serializable {

    /// <summary>
    /// Tipo de Imagen del vale
    /// </summary>
    public int ImagenVale;

    /// <summary>
    /// Tipo de vale asociado a la denominacion
    /// </summary>
    public long TipoValePapelId;

    /// <summary>
    /// Cantidad de vales
    /// </summary>
    public int Cantidad;

    /// <summary>
    /// Importe de la denominacion
    /// </summary>
    public double Importe;

    /// <summary>
    /// Total de vales por su denominacion
    /// </summary>
    public double Total ;

    /// <summary>
    /// Posicion en la cual se le da click al listview
    /// </summary>
    public int Posicion;

    /// <summary>
    /// Nombre del tipo de vale
    /// </summary>
    public String NombreVale;

    /// <summary>
    /// Denominacion de la moneda de la fajilla
    /// </summary>
    public double Denominacion ;

    /// <summary>
    /// //Declaramos los get y set de cada uno de los elementos de esta clase
    /// </summary>


    public int getImagenVale() {
        return ImagenVale;
    }

    public String getNombreVale() {return NombreVale;}

    public void setNombreVale(String nombreVale) {NombreVale = nombreVale;}

    public double getTotal() {
        return Total;
    }

    public void setTotal(double total) {
        Total = total;
    }

    public long getTipoValePapelId() {
        return TipoValePapelId;
    }

    public void setTipoValePapelId(long tipoValePapelId) {
        TipoValePapelId = tipoValePapelId;
    }

    public int getCantidad() {
        return Cantidad;
    }

    public void setCantidad(int cantidad) {
        Cantidad = cantidad;
    }

    public double getImporte() {
        return Importe;
    }

    public void setImporte(double importe) {
        Importe = importe;
    }

    public int getPosicion() {
        return Posicion;
    }

    public void setPosicion(int posicion) {
        this.Posicion = posicion;
    }

    public double getDenominacion() {
        return Denominacion;
    }

    public void setDenominacion(double denominacion) {
        Denominacion = denominacion;
    }
}
