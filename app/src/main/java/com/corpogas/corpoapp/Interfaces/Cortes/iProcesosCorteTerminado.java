package com.corpogas.corpoapp.Interfaces.Cortes;

public interface iProcesosCorteTerminado {

    void procesoFajillasBilletes(Boolean fajillasBilletesCompletado);
    void procesoFajillasMonedas(Boolean fajillasMonedasCompletado, int dineroMorralla);
    void procesoPicos(Boolean picosCompletado);
}
