package com.corpogas.corpoapp.Entities.Tickets;

import java.io.Serializable;
import java.util.List;

public class TicketRequest implements Serializable {

    /// <summary>
    /// Posicion de carga de donde se emitira el ticket
    /// </summary>
    public long PosicionCargaId;
    /// <summary>
    /// Id del usuario que realiza la peticion
    /// </summary>
    public String IdUsuario;
    /// <summary>
    /// Listado de las diferentes formas de pago con las que se realizara la transaccion
    /// </summary>
    public List<DiccionarioParcialidades> IdFormasPago;
    ///// <summary>
    ///// Id de la forma de pago asociada a la peticion
    ///// </summary>
    //public Dictionary<long, decimal> IdFormasPago;
    ///// <summary>
    ///// Temporal mientras se generan las multiples parcialidades
    ///// </summary>
    //public long IdFormaPago;
    /// <summary>
    /// Tipo de monedero asociado a la peticion (En caso de que la forma de pago sea monedero)
    /// </summary>
    public int TipoMonedero;
    /// <summary>
    /// Id de la transaccion asociada a la peticion
    /// </summary>
    public long TransaccionId;
    /// <summary>
    /// Sucursal asociada a la estacion
    /// </summary>
    public long SucursalId;
    /// <summary>
    /// Folio para tanque lleno
    /// </summary>
    public long Folio;
    /// <summary>
    /// Clave para la conexion a tanque lleno, esta variable se llena con la respuesta de IniAuto
    /// </summary>
    public String Clave;
    /// <summary>
    /// Bandera que determina si se esta solicitando una reimpresion
    /// </summary>
    public boolean Reimpresion;

    public TicketRequest(long posicionCargaId, String idUsuario, long sucursalId, List<DiccionarioParcialidades> idFormasPago){

        PosicionCargaId = posicionCargaId;
        IdUsuario = idUsuario;
        SucursalId = sucursalId;
        IdFormasPago = idFormasPago;
    }

    public long getPosicionCargaId() {
        return PosicionCargaId;
    }

    public void setPosicionCargaId(long posicionCargaId) {
        PosicionCargaId = posicionCargaId;
    }

    public String getIdUsuario() {
        return IdUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        IdUsuario = idUsuario;
    }

    public List<DiccionarioParcialidades> getIdFormasPago() {
        return IdFormasPago;
    }

    public void setIdFormasPago(List<DiccionarioParcialidades> idFormasPago) {
        IdFormasPago = idFormasPago;
    }

    public int getTipoMonedero() {
        return TipoMonedero;
    }

    public void setTipoMonedero(int tipoMonedero) {
        TipoMonedero = tipoMonedero;
    }

    public long getTransaccionId() {
        return TransaccionId;
    }

    public void setTransaccionId(long transaccionId) {
        TransaccionId = transaccionId;
    }

    public long getSucursalId() {
        return SucursalId;
    }

    public void setSucursalId(long sucursalId) {
        SucursalId = sucursalId;
    }

    public long getFolio() {
        return Folio;
    }

    public void setFolio(long folio) {
        Folio = folio;
    }

    public String getClave() {
        return Clave;
    }

    public void setClave(String clave) {
        Clave = clave;
    }

    public boolean isReimpresion() {
        return Reimpresion;
    }

    public void setReimpresion(boolean reimpresion) {
        Reimpresion = reimpresion;
    }
}
