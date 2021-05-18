package com.corpogas.corpoapp.Entities.Tarjetas;

import com.corpogas.corpoapp.Entities.Common.ProductoTarjetero;

import java.io.Serializable;
import java.util.List;

public class Puntada implements Serializable {

    public long EmpleadoId;
    public long SucursalId;
    public int RequestID;
    public int PosicionCarga;
    public String Tarjeta;
    public int NuTarjetero;
    public String NIP;
    public List<ProductoTarjetero> Productos;

  public Puntada(long empleadoId, long sucursalId, int requestID, int posicionCarga, String tarjeta, List<ProductoTarjetero> productos){
      EmpleadoId = empleadoId;
      SucursalId = sucursalId;
      RequestID = requestID;
      PosicionCarga = posicionCarga;
      Tarjeta = tarjeta;
      Productos = productos;

  }

    public long getEmpleadoId() {
        return EmpleadoId;
    }

    public void setEmpleadoId(long empleadoId) {
        EmpleadoId = empleadoId;
    }

    public long getSucursalId() {
        return SucursalId;
    }

    public void setSucursalId(long sucursalId) {
        SucursalId = sucursalId;
    }

    public int getRequestID() {
        return RequestID;
    }

    public void setRequestID(int requestID) {
        RequestID = requestID;
    }

    public int getPosicionCarga() {
        return PosicionCarga;
    }

    public void setPosicionCarga(int posicionCarga) {
        PosicionCarga = posicionCarga;
    }

    public String getTarjeta() {
        return Tarjeta;
    }

    public void setTarjeta(String tarjeta) {
        Tarjeta = tarjeta;
    }

    public int getNuTarjetero() {
        return NuTarjetero;
    }

    public void setNuTarjetero(int nuTarjetero) {
        NuTarjetero = nuTarjetero;
    }

    public String getNIP() {
        return NIP;
    }

    public void setNIP(String NIP) {
        this.NIP = NIP;
    }

    public List<ProductoTarjetero> getProductos() {
        return Productos;
    }

    public void setProductos(List<ProductoTarjetero> productos) {
        Productos = productos;
    }
}
