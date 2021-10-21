package com.corpogas.corpoapp.Entities.Catalogos;

import java.io.Serializable;
import java.util.List;

public class Bin implements Serializable {

    public List<String> Pistas;

    public Bin(List<String> pistas) {
        Pistas = pistas;
    }

    public long ElectronicWalletTypeId;

    /// <summary>
    /// Relacion con la entidad de TipoMonedero del esquema catalogos
    /// Llave foranea
    /// </summary>
    public volatile ElectronicWalletType ElectronicWalletType;

    public volatile ElectronicWalletType TipoMonedero;


    /// <summary>
    /// Mascara de la tarjeta de tal manera que
    /// los asteriscos representan cualquier valor numerico
    /// y los otros valores deben de aparecer en el orden especificado
    /// ademas recordando que la suma total de caracteres debe ser igual a la
    /// especificada en la mascara
    /// de tal manera que
    /// Ejemplos:
    /// 399999********** TanqueLleno centro
    /// 399996********** TanqueLleno Sureste
    /// 400000********** Puntada
    /// 3XXXXX********** Donde:
    ///             XXXXX = # Estacion Cliente estacion
    /// </summary>
    public String Mask;

    /// <summary>
    /// Indicador que indica la pista donde se encuentra el bin esperado
    /// </summary>
    public int TrackNumber;

    /// <summary>
    /// Bandera que indica si el registro es de un numero de tarjeta
    /// </summary>
    public Boolean CardNumber;

    public long getElectronicWalletTypeId() {
        return ElectronicWalletTypeId;
    }

    public void setElectronicWalletTypeId(long electronicWalletTypeId) {
        ElectronicWalletTypeId = electronicWalletTypeId;
    }

    public com.corpogas.corpoapp.Entities.Catalogos.ElectronicWalletType getElectronicWalletType() {
        return ElectronicWalletType;
    }

    public void setElectronicWalletType(com.corpogas.corpoapp.Entities.Catalogos.ElectronicWalletType electronicWalletType) {
        ElectronicWalletType = electronicWalletType;
    }

    public com.corpogas.corpoapp.Entities.Catalogos.ElectronicWalletType getTipoMonedero() {
        return TipoMonedero;
    }

    public void setTipoMonedero(com.corpogas.corpoapp.Entities.Catalogos.ElectronicWalletType tipoMonedero) {
        TipoMonedero = tipoMonedero;
    }

    public String getMask() {
        return Mask;
    }

    public void setMask(String mask) {
        Mask = mask;
    }

    public int getTrackNumber() {
        return TrackNumber;
    }

    public void setTrackNumber(int trackNumber) {
        TrackNumber = trackNumber;
    }

    public Boolean getCardNumber() {
        return CardNumber;
    }

    public void setCardNumber(Boolean cardNumber) {
        CardNumber = cardNumber;
    }
}
