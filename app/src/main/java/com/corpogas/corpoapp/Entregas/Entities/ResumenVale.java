package com.corpogas.corpoapp.Entregas.Entities;

public class ResumenVale {
    public String NombreEmpleado;
    public PaperVoucherType TipoValePapel;
    public int Cantidad;
    public String FechaUltimaValePapel;

    public String getNombreEmpleado() {
        return NombreEmpleado;
    }

    public void setNombreEmpleado(String nombreEmpleado) {
        NombreEmpleado = nombreEmpleado;
    }

    public PaperVoucherType getTipoValePapel() {
        return TipoValePapel;
    }

    public void setTipoValePapel(PaperVoucherType tipoValePapel) {
        TipoValePapel = tipoValePapel;
    }

    public int getCantidad() {
        return Cantidad;
    }

    public void setCantidad(int cantidad) {
        Cantidad = cantidad;
    }

    public String getFechaUltimaValePapel() {
        return FechaUltimaValePapel;
    }

    public void setFechaUltimaValePapel(String fechaUltimaValePapel) {
        FechaUltimaValePapel = fechaUltimaValePapel;
    }
}
