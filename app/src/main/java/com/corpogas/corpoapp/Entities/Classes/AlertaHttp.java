package com.corpogas.corpoapp.Entities.Classes;

public class AlertaHttp {

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
