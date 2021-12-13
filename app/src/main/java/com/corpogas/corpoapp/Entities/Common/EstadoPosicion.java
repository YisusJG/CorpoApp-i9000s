package com.corpogas.corpoapp.Entities.Common;

import java.io.Serializable;

public class EstadoPosicion implements Serializable {

    /// <summary>
    /// Numero interno de la posicion de carga
    /// </summary>
    public int Posicioncarga;
    /// <summary>
    /// Bandera que indica si la posicion de carga se encuentra disponible
    /// </summary>
    public boolean Disponible;
    /// <summary>
    /// Indica el numero correspondiente al estado de la posicion de carga
    /// 8-Predeterminado con manguera colgada
    /// 9-Predeterminado con manguera descolgada
    /// </summary>
    public int Estado;
    /// <summary>
    /// Bandera que indica si la posicion de carga ya se encuentra en pendiente de cobro
    /// </summary>
    public boolean PendienteCobro;
    /// <summary>
    /// Operacion que se esta realizando en la posicion de carga (tanque lleno, puntada...)
    /// </summary>
    public int Operativa;
    /// <summary>
    /// Description de la operativa en la que se encuentra la posicion de carga
    /// </summary>
    public String DescripcionOperativa;
    /// <summary>
    /// Description del estado de la posicion de carga
    /// </summary>
    public String Descripcion;
    /// <summary>
    /// Numero de la posicion de carga 
    /// </summary>
    public int NumeroPosicionCarga;

    /// <summary>
    /// Bandera que indica si en la estacion se encuentra en jarreo ante una autoridad
    /// </summary>
    public boolean EstacionJarreo;

    public int getPosicioncarga() {
        return Posicioncarga;
    }

    public void setPosicioncarga(int posicioncarga) {
        Posicioncarga = posicioncarga;
    }

    public boolean isDisponible() {
        return Disponible;
    }

    public void setDisponible(boolean disponible) {
        Disponible = disponible;
    }

    public int getEstado() {
        return Estado;
    }

    public void setEstado(int estado) {
        Estado = estado;
    }

    public boolean isPendienteCobro() {
        return PendienteCobro;
    }

    public void setPendienteCobro(boolean pendienteCobro) {
        PendienteCobro = pendienteCobro;
    }

    public int getOperativa() {
        return Operativa;
    }

    public void setOperativa(int operativa) {
        Operativa = operativa;
    }

    public String getDescripcionOperativa() {
        return DescripcionOperativa;
    }

    public void setDescripcionOperativa(String descripcionOperativa) {
        DescripcionOperativa = descripcionOperativa;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public int getNumeroPosicionCarga() {
        return NumeroPosicionCarga;
    }

    public void setNumeroPosicionCarga(int numeroPosicionCarga) {
        NumeroPosicionCarga = numeroPosicionCarga;
    }

    public boolean isEstacionJarreo() {
        return EstacionJarreo;
    }

    public void setEstacionJarreo(boolean estacionJarreo) {
        EstacionJarreo = estacionJarreo;
    }
}
