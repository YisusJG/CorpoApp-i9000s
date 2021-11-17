package com.corpogas.corpoapp.Metas.Entities;

import java.io.Serializable;

public class Metas implements Serializable {

   public String TipoProducto;
   public int Despachos; //Int
   public int Meta; // int
   public int Vendidos; // int
   public int Diferencia; // int

    public Metas(String tipoProducto, int despachos, int meta, int vendidos, int diferencias) {
        TipoProducto = tipoProducto;
        Despachos = despachos;
        Meta = meta;
        Vendidos = vendidos;
        Diferencia = diferencias;
    }

    public String getTipoProducto() {
        return TipoProducto;
    }

    public void setTipoProducto(String tipoProducto) {
        TipoProducto = tipoProducto;
    }

    public int getDespachos() {
        return Despachos;
    }

    public void setDespachos(int despachos) {
        Despachos = despachos;
    }

    public int getMeta() {
        return Meta;
    }

    public void setMeta(int meta) {
        Meta = meta;
    }

    public int getVendidos() {
        return Vendidos;
    }

    public void setVendidos(int vendidos) {
        Vendidos = vendidos;
    }

    public int getDiferencias() {
        return Diferencia;
    }

    public void setDiferencias(int diferencias) {
        Diferencia = diferencias;
    }
}
