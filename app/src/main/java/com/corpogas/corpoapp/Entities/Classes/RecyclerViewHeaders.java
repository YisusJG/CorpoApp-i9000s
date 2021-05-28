package com.corpogas.corpoapp.Entities.Classes;

import java.io.Serializable;

public class RecyclerViewHeaders implements Serializable {
    String titulo;
    String subtitulo;
    int imagenId;
    long PosicionCargaId;

    public RecyclerViewHeaders(String titulo, String subtitulo, int imagenId) {
        this.titulo = titulo;
        this.subtitulo = subtitulo;
        this.imagenId = imagenId;
    }

    public RecyclerViewHeaders(String titulo, String subtitulo, int imagenId, long posicionCargaId) {
        this.titulo = titulo;
        this.subtitulo = subtitulo;
        this.imagenId = imagenId;
        this.PosicionCargaId = posicionCargaId;
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
}
