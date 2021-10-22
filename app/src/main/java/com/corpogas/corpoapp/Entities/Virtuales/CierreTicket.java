package com.corpogas.corpoapp.Entities.Virtuales;

import com.corpogas.corpoapp.Corte.Entities.ValePapelTotal;

import java.io.Serializable;
import java.util.List;

public class CierreTicket implements Serializable {

    public List<VentaCombustibleTotal> VentaCombustiblesTotal;
    public double ImporteCombustibleTotal;
    public List<VentaProductoTotal> VentaProductosTotal;
    public double ImporteProductoTotal;
    public double VentaTotal;
    public List<FajillaTotal> FajillasTotal;
    public double ImporteFajillaTotal;
    public List<FajillaTotal> PicosTotal;
    public double PicosBilletesTotal;
    public double PicosMorrallasTotal;
    public double ImportePicoTotal;
    public List<ValePapelTotal> ValePapelesTotal;
    public List<ResumenValePapelTotal> ResumenValePapelesTotal;
    public double ImporteValesTotal;
    public List<FormaPagoTotal> FormaPagosTotal;
    public double ImporteFormaPagoTotal;
    public double ImporteTotalJarreos;
    public double ImporteTotalAutoJarreos;
    public List<GastoTotal> GastosTotal;
    public double ImporteGastosTotal;
    public double Total;
    public List<DiferenciaTotal> DiferenciasTotal;
    public double ImporteDiferenciaTotal;
    public String Observaciones;

    public List<VentaCombustibleTotal> getVentaCombustiblesTotal() {
        return VentaCombustiblesTotal;
    }

    public void setVentaCombustiblesTotal(List<VentaCombustibleTotal> ventaCombustiblesTotal) {
        VentaCombustiblesTotal = ventaCombustiblesTotal;
    }

    public double getImporteCombustibleTotal() {
        return ImporteCombustibleTotal;
    }

    public void setImporteCombustibleTotal(double importeCombustibleTotal) {
        ImporteCombustibleTotal = importeCombustibleTotal;
    }

    public List<VentaProductoTotal> getVentaProductosTotal() {
        return VentaProductosTotal;
    }

    public void setVentaProductosTotal(List<VentaProductoTotal> ventaProductosTotal) {
        VentaProductosTotal = ventaProductosTotal;
    }

    public double getImporteProductoTotal() {
        return ImporteProductoTotal;
    }

    public void setImporteProductoTotal(double importeProductoTotal) {
        ImporteProductoTotal = importeProductoTotal;
    }

    public double getVentaTotal() {
        return VentaTotal;
    }

    public void setVentaTotal(double ventaTotal) {
        VentaTotal = ventaTotal;
    }

    public List<FajillaTotal> getFajillasTotal() {
        return FajillasTotal;
    }

    public void setFajillasTotal(List<FajillaTotal> fajillasTotal) {
        FajillasTotal = fajillasTotal;
    }

    public double getImporteFajillaTotal() {
        return ImporteFajillaTotal;
    }

    public void setImporteFajillaTotal(double importeFajillaTotal) {
        ImporteFajillaTotal = importeFajillaTotal;
    }

    public List<FajillaTotal> getPicosTotal() {
        return PicosTotal;
    }

    public void setPicosTotal(List<FajillaTotal> picosTotal) {
        PicosTotal = picosTotal;
    }

    public double getPicosBilletesTotal() {
        return PicosBilletesTotal;
    }

    public void setPicosBilletesTotal(double picosBilletesTotal) {
        PicosBilletesTotal = picosBilletesTotal;
    }

    public double getPicosMorrallasTotal() {
        return PicosMorrallasTotal;
    }

    public void setPicosMorrallasTotal(double picosMorrallasTotal) {
        PicosMorrallasTotal = picosMorrallasTotal;
    }

    public double getImportePicoTotal() {
        return ImportePicoTotal;
    }

    public void setImportePicoTotal(double importePicoTotal) {
        ImportePicoTotal = importePicoTotal;
    }

    public List<ValePapelTotal> getValePapelesTotal() {
        return ValePapelesTotal;
    }

    public void setValePapelesTotal(List<ValePapelTotal> valePapelesTotal) {
        ValePapelesTotal = valePapelesTotal;
    }

    public List<ResumenValePapelTotal> getResumenValePapelesTotal() {
        return ResumenValePapelesTotal;
    }

    public void setResumenValePapelesTotal(List<ResumenValePapelTotal> resumenValePapelesTotal) {
        ResumenValePapelesTotal = resumenValePapelesTotal;
    }

    public double getImporteValesTotal() {
        return ImporteValesTotal;
    }

    public void setImporteValesTotal(double importeValesTotal) {
        ImporteValesTotal = importeValesTotal;
    }

    public List<FormaPagoTotal> getFormaPagosTotal() {
        return FormaPagosTotal;
    }

    public void setFormaPagosTotal(List<FormaPagoTotal> formaPagosTotal) {
        FormaPagosTotal = formaPagosTotal;
    }

    public double getImporteFormaPagoTotal() {
        return ImporteFormaPagoTotal;
    }

    public void setImporteFormaPagoTotal(double importeFormaPagoTotal) {
        ImporteFormaPagoTotal = importeFormaPagoTotal;
    }

    public double getImporteTotalJarreos() {
        return ImporteTotalJarreos;
    }

    public void setImporteTotalJarreos(double importeTotalJarreos) {
        ImporteTotalJarreos = importeTotalJarreos;
    }

    public double getImporteTotalAutoJarreos() {
        return ImporteTotalAutoJarreos;
    }

    public void setImporteTotalAutoJarreos(double importeTotalAutoJarreos) {
        ImporteTotalAutoJarreos = importeTotalAutoJarreos;
    }

    public List<GastoTotal> getGastosTotal() {
        return GastosTotal;
    }

    public void setGastosTotal(List<GastoTotal> gastosTotal) {
        GastosTotal = gastosTotal;
    }

    public double getImporteGastosTotal() {
        return ImporteGastosTotal;
    }

    public void setImporteGastosTotal(double importeGastosTotal) {
        ImporteGastosTotal = importeGastosTotal;
    }

    public double getTotal() {
        return Total;
    }

    public void setTotal(double total) {
        Total = total;
    }

    public List<DiferenciaTotal> getDiferenciasTotal() {
        return DiferenciasTotal;
    }

    public void setDiferenciasTotal(List<DiferenciaTotal> diferenciasTotal) {
        DiferenciasTotal = diferenciasTotal;
    }

    public double getImporteDiferenciaTotal() {
        return ImporteDiferenciaTotal;
    }

    public void setImporteDiferenciaTotal(double importeDiferenciaTotal) {
        ImporteDiferenciaTotal = importeDiferenciaTotal;
    }

    public String getObservaciones() {
        return Observaciones;
    }

    public void setObservaciones(String observaciones) {
        Observaciones = observaciones;
    }
}
