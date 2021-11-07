package com.corpogas.corpoapp.Metas.Entities;

import java.io.Serializable;

public class Metas implements Serializable {
    int id;
    String tipoProducto;
    int despachos;
    int meta;
    int vendidos;
    int diferencias;

    public Metas(int id, String tipoProducto, int despachos, int meta, int vendidos, int diferencias) {
        this.id = id;
        this.tipoProducto = tipoProducto;
        this.despachos = despachos;
        this.meta = meta;
        this.vendidos = vendidos;
        this.diferencias = diferencias;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTipoProducto() {
        return tipoProducto;
    }

    public void setTipoProducto(String tipoProducto) {
        this.tipoProducto = tipoProducto;
    }

    public int getDespachos() {
        return despachos;
    }

    public void setDespachos(int despachos) {
        this.despachos = despachos;
    }

    public int getMeta() {
        return meta;
    }

    public void setMeta(int meta) {
        this.meta = meta;
    }

    public int getVendidos() {
        return vendidos;
    }

    public void setVendidos(int vendidos) {
        this.vendidos = vendidos;
    }

    public int getDiferencias() {
        return diferencias;
    }

    public void setDiferencias(int diferencias) {
        this.diferencias = diferencias;
    }
}
