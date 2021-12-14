package com.corpogas.corpoapp.Entities.Classes;

import java.io.Serializable;

public class RecyclerViewHeaders implements Serializable {
    String titulo;
    String subtitulo;
    int imagenId;
    long PosicionCargaId;
    long posicioncarganumerointerno;
    long formaPagoId;
    String jarreo;
    String ventaproductos;
    String autojarreo;
    String acumulapuntos;

    public RecyclerViewHeaders(String titulo, String subtitulo, int imagenId) {
        this.titulo = titulo;
        this.subtitulo = subtitulo;
        this.imagenId = imagenId;
    }

    public RecyclerViewHeaders(String titulo, String subtitulo, int imagenId, String ventaproductos) {
        this.titulo = titulo;
        this.subtitulo = subtitulo;
        this.imagenId = imagenId;
        this.ventaproductos = ventaproductos;
    }

    public RecyclerViewHeaders(String titulo, String subtitulo, int imagenId, String ventaproductos, String acumulapuntos) {
        this.titulo = titulo;
        this.subtitulo = subtitulo;
        this.imagenId = imagenId;
        this.ventaproductos = ventaproductos;
        this.acumulapuntos = acumulapuntos;
    }


    public RecyclerViewHeaders(String titulo, String subtitulo, int imagenId, long posicionCargaId,long posicioncarganumerointerno, String jarreo, String autojarreo) {
        this.titulo = titulo;
        this.subtitulo = subtitulo;
        this.imagenId = imagenId;
        this.PosicionCargaId = posicionCargaId;
        this.posicioncarganumerointerno = posicioncarganumerointerno;
        this.jarreo = jarreo;
        this.autojarreo = autojarreo;
    }

    public RecyclerViewHeaders(String titulo, long formaPagoId, int imagenId) {
        this.titulo = titulo;
        this.formaPagoId = formaPagoId;
        this.imagenId = imagenId;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getSubtitulo() {
        return subtitulo;
    }

    public void setSubtitulo(String subtitulo) {
        this.subtitulo = subtitulo;
    }

    public int getImagenId() {
        return imagenId;
    }

    public void setImagenId(int imagenId) {
        this.imagenId = imagenId;
    }

    public long getPosicionCargaId() {
        return PosicionCargaId;
    }

    public void setPosicionCargaId(long posicionCargaId) {
        PosicionCargaId = posicionCargaId;
    }

    public long getPosicioncarganumerointerno() {
        return posicioncarganumerointerno;
    }

    public String getJarreo() {return  jarreo; }

    public String getAutojarreo() {return autojarreo;}

    public String getVentaproductos() {return ventaproductos;}

    public String getAcumulapuntos() {return acumulapuntos;}

    public void setPosicioncarganumerointerno(long posicioncarganumerointerno) {
        this.posicioncarganumerointerno = posicioncarganumerointerno;
    }

    public long getFormaPagoId() {
        return formaPagoId;
    }

    public void setFormaPagoId(long formaPagoId) {
        this.formaPagoId = formaPagoId;
    }
}
