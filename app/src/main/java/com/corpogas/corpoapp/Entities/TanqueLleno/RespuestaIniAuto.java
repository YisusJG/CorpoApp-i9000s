package com.corpogas.corpoapp.Entities.TanqueLleno;

import com.corpogas.corpoapp.Entities.Common.ProductoTarjetero;
import com.corpogas.corpoapp.Entities.Estaciones.Estacion;
import com.corpogas.corpoapp.Entities.Estaciones.PosicionCarga;
import com.corpogas.corpoapp.Entities.Sucursales.Branch;

import java.io.Serializable;
import java.util.List;

public class RespuestaIniAuto implements Serializable {
    public String TarjetaCliente;
    public long PosicionCarga;
    public String EstacionCarga;
    public long Origen;
    public int Socket;
    public String EstacionOrigen;
    public String NombreCliente;
    public String Rfc;
    public int TipoCliente;
    public String NombreTarjeta;
    public boolean Placas;
    public boolean Odometro;
    public boolean ImprimeSaldo;
    public boolean ImprimeSaldoTarjeta;
    public boolean AutorizaProductos;
    public String SolicitudEnLitros;
    public double CantidadAut;
    public double CreditoDisponible;
    public List<Integer> CombustiblesAutorizados;
    public String NumeroPlaca;
    public String Nip;
    public int CodigoError;
    public long SucursalId;

//    public RespuestaIniAuto(long sucursalId, long posicionCarga, String tarjetaCliente){
//        SucursalId = sucursalId;
//        PosicionCarga = posicionCarga;
//        TarjetaCliente = tarjetaCliente;
//    }




    public String getTarjetaCliente() { return TarjetaCliente; }
    public void setTarjetaCliente(String tarjetacliente) { TarjetaCliente = tarjetacliente; }

    public long  getPosicionCarga() { return PosicionCarga; }
    public void setPosicionCarga(int posicioncarga) { PosicionCarga = posicioncarga; }

    public String getEstacionCarga() { return EstacionCarga; }
    public void setEstacionCarga(String estacioncarga) { EstacionCarga = estacioncarga; }

    public long getOrigen() { return Origen; }
    public void setOrigen(long origen) { Origen = origen; }

    public int getSocket(){ return Socket; }
    public void setSocket(int socket) { Socket = socket; }

    public String getEstacionOrigen() { return EstacionOrigen; }
    public void setEstacionOrigen(String estacionorigen) { EstacionOrigen = estacionorigen; }

    public String getNombreCliente() { return NombreCliente; }
    public void setNombreCliente(String nombrecliente) { NombreCliente = nombrecliente; }

    public String getRfc() { return Rfc; }
    public void setRfc(String rfc) { Rfc = rfc; }

    public int getTipoCliente(){ return TipoCliente; }
    public void setTipoCliente(int tipocliente) { TipoCliente = tipocliente; }

    public String getNombreTarjeta() { return NombreTarjeta; }
    public void setNombreTarjeta(String nombretarjeta) { NombreTarjeta = nombretarjeta; }

    public boolean getPlacas(){ return Placas; }
    public void setPlacas(boolean placas) { Placas = placas; }

    public boolean getOdometro(){ return Odometro; }
    public void setOdometro(boolean odometro) { Odometro = odometro; }

    public boolean getImprimeSaldo(){ return ImprimeSaldo; }
    public void setImprimeSaldo(boolean imprimesaldo) { ImprimeSaldo = imprimesaldo; }

    public boolean getImprimeSaldoTarjeta(){ return ImprimeSaldoTarjeta; }
    public void setImprimeSaldoTarjeta(boolean imprimesaldotarjeta) { ImprimeSaldoTarjeta = imprimesaldotarjeta; }


    public boolean getAutorizaProductos(){ return AutorizaProductos; }
    public void setAutorizaProductos(boolean autorizaproductos) { AutorizaProductos = autorizaproductos; }


    public String getSolicitudEnLitros() { return SolicitudEnLitros; }
    public void setSolicitudEnLitros(String solicitudenlitros) { SolicitudEnLitros = solicitudenlitros; }

    public double getCantidadAut() { return CantidadAut; }
    public void setCantidadAut(double cantidadaut){ CantidadAut = cantidadaut; }

    public double getCreditoDisponible() { return CreditoDisponible; }
    public void setCreditoDisponible(double creditodisponible){ CreditoDisponible = creditodisponible; }

    public List<Integer> getCombustiblesAutorizados() { return CombustiblesAutorizados; }

    public String getNumeroPlaca() { return NumeroPlaca; }
    public void setNumeroPlaca(String numeroplaca) { NumeroPlaca = numeroplaca; }

    public String getNip() { return Nip; }
    public void setNip(String nip) { Nip = nip; }

    public int getCodigoError() { return CodigoError; }
    public void setCodigoError(int codigoerror) { CodigoError = codigoerror; }

}