package com.corpogas.corpoapp.Entities.Virtuales;

import com.corpogas.corpoapp.Entities.Estaciones.DiferenciaPermitida;
import com.corpogas.corpoapp.Entities.Sucursales.PriceBankRoll;

import java.io.Serializable;
import java.util.List;

public class CierreVariables implements Serializable {

    public DiferenciaPermitida DiferenciaPermitida;
    public List<PriceBankRoll> PrecioFajillas;
    public List<Integer> Denominaciones;

    public DiferenciaPermitida getDiferenciaPermitida() {
        return DiferenciaPermitida;
    }

    public void setDiferenciaPermitida(DiferenciaPermitida diferenciaPermitida) {
        DiferenciaPermitida = diferenciaPermitida;
    }

    public List<PriceBankRoll> getPrecioFajillas() {
        return PrecioFajillas;
    }

    public void setPrecioFajillas(List<PriceBankRoll> precioFajillas) {
        PrecioFajillas = precioFajillas;
    }

    public List<Integer> getDenominaciones() { return Denominaciones; }

    public void setDenominaciones(List<Integer> denominaciones) { Denominaciones = denominaciones; }
}
