package com.corpogas.corpoapp.Entities.Tickets;

import java.io.Serializable;
import java.util.List;

public class TicketPie implements Serializable {

    /// <summary>
    /// Listado de mensajes del pie del ticket
    /// </summary>
    public List<String> Mensaje;
    public double Puntos;
    public String NombreTarjeta;
    public String NumeroTarjeta;
    public double Saldo;
    public String Placas;
    public long Odometro;

    public List<String> getMensaje() { return Mensaje; }

    public void setMensaje(List<String> mensaje) { Mensaje = mensaje; }

    public double getPuntos() { return Puntos; }

    public void setPuntos(double puntos) { Puntos = puntos; }

    public String getNombreTarjeta() { return NombreTarjeta; }

    public void setNombreTarjeta(String nombreTarjeta) { NombreTarjeta = nombreTarjeta; }

    public String getNumeroTarjeta() { return NumeroTarjeta; }

    public void setNumeroTarjeta(String numeroTarjeta) { NumeroTarjeta = numeroTarjeta; }

    public double getSaldo() { return Saldo; }

    public void setSaldo(double saldo) { Saldo = saldo; }

    public String getPlacas() { return Placas; }

    public void setPlacas(String placas) { Placas = placas; }

    public long getOdometro() { return Odometro; }

    public void setOdometro(long odometro) { Odometro = odometro; }
}
