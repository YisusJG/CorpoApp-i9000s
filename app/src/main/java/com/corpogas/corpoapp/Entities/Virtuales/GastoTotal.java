package com.corpogas.corpoapp.Entities.Virtuales;

import java.io.Serializable;

public class GastoTotal implements Serializable {

    public long ConceptoGastoId;
    public String Concepto;
    public double Importe;

    public long getConceptoGastoId() {
        return ConceptoGastoId;
    }

    public void setConceptoGastoId(long conceptoGastoId) {
        ConceptoGastoId = conceptoGastoId;
    }

    public String getConcepto() {
        return Concepto;
    }

    public void setConcepto(String concepto) {
        Concepto = concepto;
    }

    public double getImporte() {
        return Importe;
    }

    public void setImporte(double importe) {
        Importe = importe;
    }
}
