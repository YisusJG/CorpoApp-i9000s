package com.corpogas.corpoapp.Entities.Catalogos;

import com.corpogas.corpoapp.Entities.Sucursales.ProductControl;

import java.io.Serializable;
import java.util.List;

public class Product implements Serializable {

    /// <summary>
    /// Identificador de la entidad SatTypeProduct
    /// </summary>
    public long SatTypeProductId;

    /// <summary>
    /// Relacion con la entidad de SatTypeProduct
    /// Llave foranea
    /// </summary>
    public SatTypeProduct SatTypeProduct;

    /// <summary>
    /// Identificador de la entidad Provider
    /// </summary>
    public long ProviderId;

    /// <summary>
    /// Relacion con la entidad de Provider
    /// Llave foranea
    /// </summary>
    public Provider Provider;

    /// <summary>
    /// Identificador de la entidad ProductCategory
    /// </summary>
    public long ProductCategoryId;

    /// <summary>
    /// Relacion con la entidad de ProductCategory
    /// Llave foranea
    /// </summary>
    public ProductCategory ProductCategory;

    /// <summary>
    /// Identificador de la entidad MeasureUnit
    /// </summary>
    public long MeasureUnitId;

    /// <summary>
    /// Relacion con la entidad de MeasureUnit
    /// Llave foranea
    /// </summary>
    public MeasureUnit MeasureUnit;

    /// <summary>
    /// Numero que identifica al producto en la base del proveedor, este es asignado por el corporativo
    /// Ejemplo: 
    /// ->A21
    /// ->A22
    /// ->A23
    /// </summary>
    public String ProviderCode;

    /// <summary>
    /// Codigo que identifica al producto en la base del SAT
    /// Ejemplo:
    /// ->SA7
    /// </summary>
    public String SatCode;

    /// <summary>
    /// Numero EAN que identifica al producto en su codigo de barras, este es asignado por el corporativo
    /// Ejemplo: 
    /// ->137158567000
    /// </summary>
    public String Barcode;


    /// <summary>
    /// Image asociada al producto
    /// NOTA: Usado para mostrar los productos en los puntos de venta
    /// </summary>
    public byte[] Image;

    /// <summary>
    /// Name resumido del producto 
    /// NOTA: Es usado para los documentos en formato ticket con una longitud de caracteres reducida
    /// Ejemplo:
    /// ->TOPO
    /// ->COOL
    /// </summary>
    public String ShortDescription;

    /// <summary>
    /// Name completo del producto
    /// NOTA: Es usado para los documentos en formato oficio en donde la longitud de caracteres no es un problema
    /// Ejemplo:
    /// ->TOP OIL
    /// ->COOLANT
    /// </summary>
    public String LongDescription;

    /// <summary>
    /// Costos e impuestos de los productos
    /// </summary>
    public List<ProductControl> ProductControls;

    public long getSatTypeProductId() { return SatTypeProductId; }

    public void setSatTypeProductId(long satTypeProductId) { SatTypeProductId = satTypeProductId; }

    public com.corpogas.corpoapp.Entities.Catalogos.SatTypeProduct getSatTypeProduct() { return SatTypeProduct; }

    public void setSatTypeProduct(com.corpogas.corpoapp.Entities.Catalogos.SatTypeProduct satTypeProduct) { SatTypeProduct = satTypeProduct; }

    public long getProviderId() { return ProviderId; }

    public void setProviderId(long providerId) { ProviderId = providerId; }

    public com.corpogas.corpoapp.Entities.Catalogos.Provider getProvider() { return Provider; }

    public void setProvider(com.corpogas.corpoapp.Entities.Catalogos.Provider provider) { Provider = provider; }

    public long getProductCategoryId() { return ProductCategoryId; }

    public void setProductCategoryId(long productCategoryId) { ProductCategoryId = productCategoryId; }

    public com.corpogas.corpoapp.Entities.Catalogos.ProductCategory getProductCategory() { return ProductCategory; }

    public void setProductCategory(com.corpogas.corpoapp.Entities.Catalogos.ProductCategory productCategory) { ProductCategory = productCategory; }

    public long getMeasureUnitId() { return MeasureUnitId; }

    public void setMeasureUnitId(long measureUnitId) { MeasureUnitId = measureUnitId; }

    public com.corpogas.corpoapp.Entities.Catalogos.MeasureUnit getMeasureUnit() { return MeasureUnit; }

    public void setMeasureUnit(com.corpogas.corpoapp.Entities.Catalogos.MeasureUnit measureUnit) { MeasureUnit = measureUnit; }

    public String getProviderCode() { return ProviderCode; }

    public void setProviderCode(String providerCode) { ProviderCode = providerCode; }

    public String getSatCode() { return SatCode; }

    public void setSatCode(String satCode) { SatCode = satCode; }

    public String getBarcode() { return Barcode; }

    public void setBarcode(String barcode) { Barcode = barcode; }

    public byte[] getImage() { return Image; }

    public void setImage(byte[] image) { Image = image; }

    public String getShortDescription() { return ShortDescription; }

    public void setShortDescription(String shortDescription) { ShortDescription = shortDescription; }

    public String getLongDescription() { return LongDescription; }

    public void setLongDescription(String longDescription) { LongDescription = longDescription; }

    public List<ProductControl> getProductControls() { return ProductControls; }

    public void setProductControls(List<ProductControl> productControls) { ProductControls = productControls; }
}
