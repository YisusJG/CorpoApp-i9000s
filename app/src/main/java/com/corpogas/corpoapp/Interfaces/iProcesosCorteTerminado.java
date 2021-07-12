package com.corpogas.corpoapp.Interfaces;

public interface iProcesosCorteTerminado {

    void procesoFajillasBilletes(Boolean fajillasBilletesCompletado);
    void procesoFajillasMonedas(Boolean fajillasMonedasCompletado, int dineroMorralla);
    void procesoPicos(Boolean picosCompletado);
}
