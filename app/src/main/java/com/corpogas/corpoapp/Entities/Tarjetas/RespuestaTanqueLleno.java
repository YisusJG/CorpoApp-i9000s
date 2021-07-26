package com.corpogas.corpoapp.Entities.Tarjetas;

import java.io.Serializable;
import java.util.List;

public class RespuestaTanqueLleno implements Serializable {

    /// <summary>
    /// Determina si la respuesta de tanque lleno fue correcta
    /// </summary>
    public boolean Correcto;
    /// <summary>
    /// En caso de algun error envia el mensaje
    /// </summary>
    public String Mensaje;
    /// <summary>
    /// Cantidad autorizada para la tarjeta
    /// </summary>
    public double CantidadAutorizada;
    /// <summary>
    /// Credito disponible del cliente
    /// </summary>
    public double CreditoDisponible;
    /// <summary>
    /// Determina si se tiene que solicitar el numero de las placas del automovil
    /// </summary>
    public boolean PidePlaca;
    /// <summary>
    /// Determina si se tiene que solicitar el odometro
    /// </summary>
    public boolean PideOdometro;
    /// <summary>
    /// Combustibles que se tiene permiso cargar por parte de tanque lleno (numero interno del combustible)
    /// </summary>
    public List<Integer> CombustiblesAutorizados;
    /// <summary>
    /// Bandera que indica si la tarjeta tiene autorizado comprar productos
    /// </summary>
    public boolean AutorizaProductos;
    /// <summary>
    /// Usuario para la conexion a tanque lleno, esta variable se llena con la respuesta de iniAuto
    /// </summary>
    public String Usuario;
    /// <summary>
    /// Nip para la conexio a tanque lleno, esta variable se llena con la respuesta de IniAuto
    /// </summary>
    public String Clave;
    /// <summary>
    /// Tipo de cliente para tanque lleno
    /// </summary>
    public int TipoCliente;
    /// <summary>
    /// Id del empleado que realiza la transaccion
    /// </summary>
    public long SucursalEmpleadoId;
    /// <summary>
    /// Numero interno de la sucursal
    /// </summary>
    public String NumeroInternoSucursal;
    /// <summary>
    /// Numero de la transaccion
    /// </summary>
    public long TransaccionId;
    /// <summary>
    /// Folio asociado a la transacion (soo para tanque lleno)
    /// </summary>
    public long Folio;
    /// <summary>
    /// Numero del cliente
    /// </summary>
    public long NumeroCliente;
    /// <summary>
    /// Odometro
    /// </summary>
    public long Odometro;
    /// <summary>
    /// 
    /// </summary>
    public int TiempoOti;
    /// <summary>
    /// Bandera que valida si se debe de imprimir el saldo en el ticket
    /// </summary>
    public boolean ImprimeSaldo;

    public String SolicitudEnLitros;

    /// Id del empleado que realiza la transaccion
    /// </summary>
    public String NumeroEmpleado;

    public boolean isCorrecto() { return Correcto; }

    public void setCorrecto(boolean correcto) { Correcto = correcto; }

    public String getSolicitudEnLitros() { return SolicitudEnLitros; }

    public String getNumeroEmpleado(){ return NumeroEmpleado; }

    public String getMensaje() { return Mensaje; }

    public void setMensaje(String mensaje) { Mensaje = mensaje; }

    public double getCantidadAutorizada() { return CantidadAutorizada; }

    public void setCantidadAutorizada(double cantidadAutorizada) { CantidadAutorizada = cantidadAutorizada; }

    public double getCreditoDisponible() { return CreditoDisponible; }

    public void setCreditoDisponible(double creditoDisponible) { CreditoDisponible = creditoDisponible; }

    public boolean isPidePlaca() { return PidePlaca; }

    public void setPidePlaca(boolean pidePlaca) { PidePlaca = pidePlaca; }

    public boolean isPideOdometro() { return PideOdometro; }

    public void setPideOdometro(boolean pideOdometro) { PideOdometro = pideOdometro; }

    public List<Integer> getCombustiblesAutorizados() { return CombustiblesAutorizados; }

    public void setCombustiblesAutorizados(List<Integer> combustiblesAutorizados) { CombustiblesAutorizados = combustiblesAutorizados; }

    public boolean isAutorizaProductos() { return AutorizaProductos; }

    public void setAutorizaProductos(boolean autorizaProductos) { AutorizaProductos = autorizaProductos; }

    public String getUsuario() { return Usuario; }

    public void setUsuario(String usuario) { Usuario = usuario; }

    public String getClave() { return Clave; }

    public void setClave(String clave) { Clave = clave; }

    public int getTipoCliente() { return TipoCliente; }

    public void setTipoCliente(int tipoCliente) { TipoCliente = tipoCliente; }

    public long getSucursalEmpleadoId() { return SucursalEmpleadoId; }

    public void setSucursalEmpleadoId(long sucursalEmpleadoId) { SucursalEmpleadoId = sucursalEmpleadoId; }

    public String getNumeroInternoSucursal() { return NumeroInternoSucursal; }

    public void setNumeroInternoSucursal(String numeroInternoSucursal) { NumeroInternoSucursal = numeroInternoSucursal; }

    public long getTransaccionId() { return TransaccionId; }

    public void setTransaccionId(long transaccionId) { TransaccionId = transaccionId; }

    public long getFolio() { return Folio; }

    public void setFolio(long folio) { Folio = folio; }

    public long getNumeroCliente() { return NumeroCliente; }

    public void setNumeroCliente(long numeroCliente) { NumeroCliente = numeroCliente; }

    public long getOdometro() { return Odometro; }

    public void setOdometro(long odometro) { Odometro = odometro; }

    public int getTiempoOti() { return TiempoOti; }

    public void setTiempoOti(int tiempoOti) { TiempoOti = tiempoOti; }

    public boolean isImprimeSaldo() { return ImprimeSaldo; }

    public void setImprimeSaldo(boolean imprimeSaldo) { ImprimeSaldo = imprimeSaldo; }
}
