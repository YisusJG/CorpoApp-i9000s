package com.corpogas.corpoapp.Entities.Estaciones;

import com.corpogas.corpoapp.Entities.Sucursales.Branch;

import java.io.Serializable;

public class DiferenciaPermitida implements Serializable {

    public long SucursalId;
    public Branch Sucursal;
    public long EstacionId;
    public Estacion Estacion;
    public double ImporteDespacho;
    public double LecturasElectronicasMecanicas;
    public double LecturasElectronicasDespachos;

    public long getSucursalId() { return SucursalId; }

    public void setSucursalId(long sucursalId) { SucursalId = sucursalId; }

    public Branch getSucursal() { return Sucursal; }

    public void setSucursal(Branch sucursal) { Sucursal = sucursal; }

    public long getEstacionId() { return EstacionId; }

    public void setEstacionId(long estacionId) { EstacionId = estacionId; }

    public Estacion getEstacion() { return Estacion; }

    public void setEstacion(Estacion estacion) { Estacion = estacion; }

    public double getImporteDespacho() { return ImporteDespacho; }

    public void setImporteDespacho(double importeDespacho) { ImporteDespacho = importeDespacho; }

    public double getLecturasElectronicasMecanicas() { return LecturasElectronicasMecanicas; }

    public void setLecturasElectronicasMecanicas(double lecturasElectronicasMecanicas) { LecturasElectronicasMecanicas = lecturasElectronicasMecanicas; }

    public double getLecturasElectronicasDespachos() { return LecturasElectronicasDespachos; }

    public void setLecturasElectronicasDespachos(double lecturasElectronicasDespachos) { LecturasElectronicasDespachos = lecturasElectronicasDespachos; }
}
