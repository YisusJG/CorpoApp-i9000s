package com.corpogas.corpoapp.Entities.Virtuales;

import java.io.Serializable;

public class DiferenciaTotal implements Serializable {

    public long NumeroFormaPago;
    public String Descripcion;
    public double Diferencia;
    public int TipoDiferencia;
    public String Tipo;

    public long getNumeroFormaPago() {
        return NumeroFormaPago;
    }

    public void setNumeroFormaPago(long numeroFormaPago) {
        NumeroFormaPago = numeroFormaPago;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public double getDiferencia() {
        return Diferencia;
    }

    public void setDiferencia(double diferencia) {
        Diferencia = diferencia;
    }

    public int getTipoDiferencia() {
        return TipoDiferencia;
    }

    public void setTipoDiferencia(int tipoDiferencia) {
        TipoDiferencia = tipoDiferencia;
    }

    public String getTipo() {
        return Tipo;
    }

    public void setTipo(String tipo) {
        Tipo = tipo;
    }
}
