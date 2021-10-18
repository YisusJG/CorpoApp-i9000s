package com.corpogas.corpoapp.Entities.Virtuales;

import java.io.Serializable;

public class VentaCombustibleTotal implements Serializable {

    public long NumeroCombustible;
    public String Combustible;
    public double Litros;
    public double Importe;

    public long getNumeroCombustible() {
        return NumeroCombustible;
    }

    public void setNumeroCombustible(long numeroCombustible) {
        NumeroCombustible = numeroCombustible;
    }

    public String getCombustible() {
        return Combustible;
    }

    public void setCombustible(String combustible) {
        Combustible = combustible;
    }

    public double getLitros() {
        return Litros;
    }

    public void setLitros(double litros) {
        Litros = litros;
    }

    public double getImporte() {
        return Importe;
    }

    public void setImporte(double importe) {
        Importe = importe;
    }
}
