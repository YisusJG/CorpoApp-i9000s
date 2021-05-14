package com.corpogas.corpoapp.Entities.Classes;

import java.io.Serializable;

public class ErrorAlertaHttp implements Serializable {

    /// <summary>
    /// Mensaje de error para el usuario
    /// </summary>
    public String MensajeUsuario;
    /// <summary>
    /// Mensaje de error para el sistema
    /// </summary>
    public String MensajeSistema;

    public String getMensajeUsuario() {
        return MensajeUsuario;
    }

    public void setMensajeUsuario(String mensajeUsuario) {
        MensajeUsuario = mensajeUsuario;
    }

    public String getMensajeSistema() {
        return MensajeSistema;
    }

    public void setMensajeSistema(String mensajeSistema) {
        MensajeSistema = mensajeSistema;
    }
}
