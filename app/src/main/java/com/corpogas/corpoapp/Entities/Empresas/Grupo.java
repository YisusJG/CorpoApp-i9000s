package com.corpogas.corpoapp.Entities.Empresas;

import java.io.Serializable;

public class Grupo implements Serializable {
    /// <summary>
    /// Número interno del grupo
    /// Ejemplos: 1,2,3,etc...
    /// </summary>
    private int NumeroInterno;

    /// <summary>
    /// Nombre o descripción del grupo
    /// Ejemplos:
    /// Corpogas
    /// Los Rodriguez
    /// Lodemo
    /// etc...
    /// </summary>
    private String Descripcion;

    public int getNumeroInterno() {
        return NumeroInterno;
    }

    public void setNumeroInterno(int numeroInterno) {
        NumeroInterno = numeroInterno;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }
}
