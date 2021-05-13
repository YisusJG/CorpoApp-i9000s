package com.corpogas.corpoapp.Entities.Classes;

import java.io.Serializable;

public class RespuestaApi<T> implements Serializable {

    /// <summary>
    /// bandera para validar si la respuesta de la api fue correcta
    /// </summary>
    public boolean Correcto;
    /// <summary>
    /// Mensaje en caso de algun error
    /// </summary>
    public String Mensaje;
    /// <summary>
    /// Objeto que sera enviado por la Api
    /// </summary>
    private final T ObjetoRespuesta;
    /// <summary>
    /// Objeto que contiene un posible error en el consumo de una URI
    /// </summary>
    public AlertaHttp AlertaHttp;


    public RespuestaApi(T objetoRespuesta) {
        ObjetoRespuesta = objetoRespuesta;
    }

    public boolean isCorrecto() {
        return Correcto;
    }

    public void setCorrecto(boolean correcto) {
        Correcto = correcto;
    }

    public String getMensaje() {
        return Mensaje;
    }

    public void setMensaje(String mensaje) {
        Mensaje = mensaje;
    }

    public T getObjetoRespuesta() {
        return ObjetoRespuesta;
    }

    public com.corpogas.corpoapp.Entities.Classes.AlertaHttp getAlertaHttp() {
        return AlertaHttp;
    }

    public void setAlertaHttp(com.corpogas.corpoapp.Entities.Classes.AlertaHttp alertaHttp) {
        AlertaHttp = alertaHttp;
    }
}
