package com.corpogas.corpoapp.Entities.Virtuales;

import java.io.Serializable;

public class Arqueo implements Serializable {

    public String NumeroEmpleado;
    public String NombreEmpleado;
//    public List<ProductoCorteParcial> Productos;
    public double ImporteTotalProductos;
//    public List<ProductoCorteParcial> Combustibles;
    public double ImporteTotalCombustibles;
//    public List<FormaPagoCorteParcial> FormasPago;
    public double ImporteTotalFormasPago;
//    public List<FajillaCorteParcial> Fajillas;
    public double ImporteTotalFajillas;
    public double EfectivoPorEntregar;
    public double GastosTotales;

    public String getNumeroEmpleado() {
        return NumeroEmpleado;
    }

    public void setNumeroEmpleado(String numeroEmpleado) {
        NumeroEmpleado = numeroEmpleado;
    }

    public String getNombreEmpleado() {
        return NombreEmpleado;
    }

    public void setNombreEmpleado(String nombreEmpleado) {
        NombreEmpleado = nombreEmpleado;
    }

    public double getImporteTotalProductos() {
        return ImporteTotalProductos;
    }

    public void setImporteTotalProductos(double importeTotalProductos) {
        ImporteTotalProductos = importeTotalProductos;
    }

    public double getImporteTotalCombustibles() {
        return ImporteTotalCombustibles;
    }

    public void setImporteTotalCombustibles(double importeTotalCombustibles) {
        ImporteTotalCombustibles = importeTotalCombustibles;
    }

    public double getImporteTotalFormasPago() {
        return ImporteTotalFormasPago;
    }

    public void setImporteTotalFormasPago(double importeTotalFormasPago) {
        ImporteTotalFormasPago = importeTotalFormasPago;
    }

    public double getImporteTotalFajillas() {
        return ImporteTotalFajillas;
    }

    public void setImporteTotalFajillas(double importeTotalFajillas) {
        ImporteTotalFajillas = importeTotalFajillas;
    }

    public double getEfectivoPorEntregar() {
        return EfectivoPorEntregar;
    }

    public void setEfectivoPorEntregar(double efectivoPorEntregar) {
        EfectivoPorEntregar = efectivoPorEntregar;
    }

    public double getGastosTotales() {
        return GastosTotales;
    }

    public void setGastosTotales(double gastosTotales) {
        GastosTotales = gastosTotales;
    }
}
