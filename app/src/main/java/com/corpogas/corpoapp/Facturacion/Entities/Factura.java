package com.corpogas.corpoapp.Facturacion.Entities;

import java.io.Serializable;
import java.util.List;

public class Factura implements Serializable {

    public Emisor Emisor;
    public Receptor Receptor;
    public String UsoCFDI;
    public String Expedicion;
    public String Serie;
    public String Folio;
    public String UUID;
    public String NoCertificadoSAT;
    public String NoCertificadoEmisor;
    public String FechaCertificacion;
    public String TipoComprobante;
    public String LugarFecha;
    public String FormaPago;
    public String ClaveMoneda;
    public double TipoCambio;
    public List<DetalleFactura> DetalleFactura;
    public double Subtotal;
    public double Iva;
    public double Total;
    public String CantidadConLetra;
    public String MetodoPago;
    public String Tickets;
    public String QRInfo;
    public String SelloDigitalSAT;
    public String SelloDigitalEmisor;
    public String CadenaOriginalSAT;

    public Emisor getEmisor() {
        return Emisor;
    }

    public void setEmisor(Emisor emisor) {
        Emisor = emisor;
    }

    public Receptor getReceptor() {
        return Receptor;
    }

    public void setReceptor(Receptor receptor) {
        Receptor = receptor;
    }

    public String getUsoCFDI() {
        return UsoCFDI;
    }

    public void setUsoCFDI(String usoCFDI) {
        UsoCFDI = usoCFDI;
    }

    public String getExpedicion() {
        return Expedicion;
    }

    public void setExpedicion(String expedicion) {
        Expedicion = expedicion;
    }

    public String getSerie() {
        return Serie;
    }

    public void setSerie(String serie) {
        Serie = serie;
    }

    public String getFolio() {
        return Folio;
    }

    public void setFolio(String folio) {
        Folio = folio;
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public String getNoCertificadoSAT() {
        return NoCertificadoSAT;
    }

    public void setNoCertificadoSAT(String noCertificadoSAT) {
        NoCertificadoSAT = noCertificadoSAT;
    }

    public String getNoCertificadoEmisor() {
        return NoCertificadoEmisor;
    }

    public void setNoCertificadoEmisor(String noCertificadoEmisor) {
        NoCertificadoEmisor = noCertificadoEmisor;
    }

    public String getFechaCertificacion() {
        return FechaCertificacion;
    }

    public void setFechaCertificacion(String fechaCertificacion) {
        FechaCertificacion = fechaCertificacion;
    }

    public String getTipoComprobante() {
        return TipoComprobante;
    }

    public void setTipoComprobante(String tipoComprobante) {
        TipoComprobante = tipoComprobante;
    }

    public String getLugarFecha() {
        return LugarFecha;
    }

    public void setLugarFecha(String lugarFecha) {
        LugarFecha = lugarFecha;
    }

    public String getFormaPago() {
        return FormaPago;
    }

    public void setFormaPago(String formaPago) {
        FormaPago = formaPago;
    }

    public String getClaveMoneda() {
        return ClaveMoneda;
    }

    public void setClaveMoneda(String claveMoneda) {
        ClaveMoneda = claveMoneda;
    }

    public double getTipoCambio() {
        return TipoCambio;
    }

    public void setTipoCambio(double tipoCambio) {
        TipoCambio = tipoCambio;
    }

    public List<DetalleFactura> getDetalleFactura() {
        return DetalleFactura;
    }

    public void setDetalleFactura(List<DetalleFactura> detalleFactura) {
        DetalleFactura = detalleFactura;
    }

    public double getSubtotal() {
        return Subtotal;
    }

    public void setSubtotal(double subtotal) {
        Subtotal = subtotal;
    }

    public double getIva() {
        return Iva;
    }

    public void setIva(double iva) {
        Iva = iva;
    }

    public double getTotal() {
        return Total;
    }

    public void setTotal(double total) {
        Total = total;
    }

    public String getCantidadConLetra() {
        return CantidadConLetra;
    }

    public void setCantidadConLetra(String cantidadConLetra) {
        CantidadConLetra = cantidadConLetra;
    }

    public String getMetodoPago() {
        return MetodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        MetodoPago = metodoPago;
    }

    public String getTickets() {
        return Tickets;
    }

    public void setTickets(String tickets) {
        Tickets = tickets;
    }

    public String getQRInfo() {
        return QRInfo;
    }

    public void setQRInfo(String QRInfo) {
        this.QRInfo = QRInfo;
    }

    public String getSelloDigitalSAT() {
        return SelloDigitalSAT;
    }

    public void setSelloDigitalSAT(String selloDigitalSAT) {
        SelloDigitalSAT = selloDigitalSAT;
    }

    public String getSelloDigitalEmisor() {
        return SelloDigitalEmisor;
    }

    public void setSelloDigitalEmisor(String selloDigitalEmisor) {
        SelloDigitalEmisor = selloDigitalEmisor;
    }

    public String getCadenaOriginalSAT() {
        return CadenaOriginalSAT;
    }

    public void setCadenaOriginalSAT(String cadenaOriginalSAT) {
        CadenaOriginalSAT = cadenaOriginalSAT;
    }
}
