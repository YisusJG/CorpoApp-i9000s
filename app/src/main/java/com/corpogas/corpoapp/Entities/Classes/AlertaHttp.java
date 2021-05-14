package com.corpogas.corpoapp.Entities.Classes;

import java.io.Serializable;

public class AlertaHttp implements Serializable {

    /// <summary>
    /// Estructura principal den mensaje
    /// </summary>
    public ErrorAlertaHttp Error;

    public ErrorAlertaHttp getError() {
        return Error;
    }

    public void setError(ErrorAlertaHttp error) {
        Error = error;
    }
}
