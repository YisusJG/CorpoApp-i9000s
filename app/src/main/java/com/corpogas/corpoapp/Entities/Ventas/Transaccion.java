package com.corpogas.corpoapp.Entities.Ventas;

import com.corpogas.corpoapp.Entities.Catalogos.TransactionType;
import com.corpogas.corpoapp.Entities.Estaciones.Estacion;
import com.corpogas.corpoapp.Entities.Estaciones.PosicionCarga;
import com.corpogas.corpoapp.Entities.Sucursales.Branch;
import com.corpogas.corpoapp.Entities.Sucursales.Shift;

import java.io.Serializable;
import java.util.List;

public class Transaccion implements Serializable {

    public long SucursalId;
    public Branch Sucursal;
    public long EstacionId;
    public Estacion Estacion;
    public long TurnoId;
    public Shift Turno;
    public long TipoTransaccionId;
    public TransactionType TipoTransaccion;
    public long PosicionCargaId;
    public PosicionCarga PosicionCarga;
    public int FacturaFormaPagoNumeroInterno;
    public int FacturaTipoFacturaNumeroInterno;
//    public DateTime FechaTrabajo;
    public long NumeroFolio;
    public long NumeroFactura;
    public long NumeroSerie;
    public boolean Impreso;
    public boolean Facturado;
    public long EmpleadoVentaId;
    public long EmpleadoImpresionId;
    public boolean Completado;
    public Despacho Despacho;
    public List<VentaProducto> VentaProductos;

    public long getSucursalId() { return SucursalId; }

    public void setSucursalId(long sucursalId) { SucursalId = sucursalId; }

    public Branch getSucursal() { return Sucursal; }

    public void setSucursal(Branch sucursal) { Sucursal = sucursal; }

    public long getEstacionId() { return EstacionId; }

    public void setEstacionId(long estacionId) { EstacionId = estacionId; }

    public Estacion getEstacion() { return Estacion; }

    public void setEstacion(Estacion estacion) { Estacion = estacion; }

    public long getTurnoId() { return TurnoId; }

    public void setTurnoId(long turnoId) { TurnoId = turnoId; }

    public Shift getTurno() { return Turno; }

    public void setTurno(Shift turno) { Turno = turno; }

    public long getTipoTransaccionId() { return TipoTransaccionId; }

    public void setTipoTransaccionId(long tipoTransaccionId) { TipoTransaccionId = tipoTransaccionId; }

    public TransactionType getTipoTransaccion() { return TipoTransaccion; }

    public void setTipoTransaccion(TransactionType tipoTransaccion) { TipoTransaccion = tipoTransaccion; }

    public long getPosicionCargaId() { return PosicionCargaId; }

    public void setPosicionCargaId(long posicionCargaId) { PosicionCargaId = posicionCargaId; }

    public PosicionCarga getPosicionCarga() { return PosicionCarga; }

    public void setPosicionCarga(PosicionCarga posicionCarga) { PosicionCarga = posicionCarga; }

    public int getFacturaFormaPagoNumeroInterno() { return FacturaFormaPagoNumeroInterno; }

    public void setFacturaFormaPagoNumeroInterno(int facturaFormaPagoNumeroInterno) { FacturaFormaPagoNumeroInterno = facturaFormaPagoNumeroInterno; }

    public int getFacturaTipoFacturaNumeroInterno() { return FacturaTipoFacturaNumeroInterno; }

    public void setFacturaTipoFacturaNumeroInterno(int facturaTipoFacturaNumeroInterno) { FacturaTipoFacturaNumeroInterno = facturaTipoFacturaNumeroInterno; }

    public long getNumeroFolio() { return NumeroFolio; }

    public void setNumeroFolio(long numeroFolio) { NumeroFolio = numeroFolio; }

    public long getNumeroFactura() { return NumeroFactura; }

    public void setNumeroFactura(long numeroFactura) { NumeroFactura = numeroFactura; }

    public long getNumeroSerie() { return NumeroSerie; }

    public void setNumeroSerie(long numeroSerie) { NumeroSerie = numeroSerie; }

    public boolean isImpreso() { return Impreso; }

    public void setImpreso(boolean impreso) { Impreso = impreso; }

    public boolean isFacturado() { return Facturado; }

    public void setFacturado(boolean facturado) { Facturado = facturado; }

    public long getEmpleadoVentaId() { return EmpleadoVentaId; }

    public void setEmpleadoVentaId(long empleadoVentaId) { EmpleadoVentaId = empleadoVentaId; }

    public long getEmpleadoImpresionId() { return EmpleadoImpresionId; }

    public void setEmpleadoImpresionId(long empleadoImpresionId) { EmpleadoImpresionId = empleadoImpresionId; }

    public boolean isCompletado() { return Completado; }

    public void setCompletado(boolean completado) { Completado = completado; }

    public Despacho getDespacho() { return Despacho; }

    public void setDespacho(Despacho despacho) { Despacho = despacho; }

    public List<VentaProducto> getVentaProductos() { return VentaProductos; }

    public void setVentaProductos(List<VentaProducto> ventaProductos) { VentaProductos = ventaProductos; }
}
