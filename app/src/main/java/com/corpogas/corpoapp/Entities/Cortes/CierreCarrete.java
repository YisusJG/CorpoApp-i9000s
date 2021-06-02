package com.corpogas.corpoapp.Entities.Cortes;

import com.corpogas.corpoapp.Entities.Catalogos.Product;
import com.corpogas.corpoapp.Entities.Estaciones.Manguera;

import java.io.Serializable;

public class CierreCarrete implements Serializable {

    public long EstacionId;
//    public Estacion Estacion;

    /// <summary>
    /// Identificador de la entidad Branch
    /// NOTA: Id de la tabla(este es computado)
    /// </summary>
    public long SucursalId;

    /// <summary>
    /// Identificador de la llave compuesta de la entidad Branch
    /// NOTA: Id de la tabla(este NO es computado)
    /// </summary>
//    public Branch Sucursal;

    public long CierreId;

    //ESTO ERA LLAVE FORANEA
    /// <summary>
    /// Identificador de la entidad Branch
    /// NOTA: Id de la tabla(este es computado)
    /// </summary>
    //public long CierreSucursalId;


    public Cierre Cierre;

    /// <summary>
    /// Id de la manguera
    /// </summary>
    public long MangueraId;

    //NOTA: ESTO SE COMENTO XQ ESTE CAMPO ERA DE LA LLAVE FORANEA
    /// <summary>
    /// Estacion de la manguera
    /// </summary>
    //public long MangueraEstacionId;

    /// <summary>
    /// Relacion con el catalogo de mangueras
    /// </summary>
    public Manguera Manguera;

    /// <summary>
    /// Identificador de la entidad Combustible
    /// NOTA: Id de la tabla(este es computado)
    /// </summary>

    public long ProductoId;

    /// <summary>
    /// Relacion con la entidad de Combustible del esquema Catalogos
    /// Llave foranea
    /// </summary>
    public Product Producto;


    /// <summary>
    /// Valor inicial del totalizador(desde la ultima lectura)
    /// </summary>
    public double ValorInicial;

    /// <summary>
    /// Valor final del totalizador obtenido del ALVIC
    /// </summary>
    public double ValorFinal;


    public double ValorInicialElectronica;

    public double ValorFinalElectronica;


    public double DiferenciaImporteDespacho;


    /// <summary>
    /// Diferencia permitida(en litros) entre las lecturas electronicas y las lecturas mecanicas
    /// </summary>
    public double DiferenciaLecturasElectronicasMecanicas;

    /// <summary>
    /// Diferencia permitida(en litros) entre las lecturas electronicas y la sumatoria de los despachos
    /// </summary>
    public double DiferenciaLecturasElectronicasDespachos;

    public long getEstacionId() { return EstacionId; }

    public void setEstacionId(long estacionId) { EstacionId = estacionId; }

    public long getSucursalId() { return SucursalId; }

    public void setSucursalId(long sucursalId) { SucursalId = sucursalId; }

    public long getCierreId() { return CierreId; }

    public void setCierreId(long cierreId) { CierreId = cierreId; }

    public Cierre getCierre() { return Cierre; }

    public void setCierre(Cierre cierre) { Cierre = cierre; }

    public long getMangueraId() { return MangueraId; }

    public void setMangueraId(long mangueraId) { MangueraId = mangueraId; }

    public Manguera getManguera() { return Manguera; }

    public void setManguera(Manguera manguera) { Manguera = manguera; }

    public long getProductoId() { return ProductoId; }

    public void setProductoId(long productoId) { ProductoId = productoId; }

    public Product getProducto() { return Producto; }

    public void setProducto(Product producto) { Producto = producto; }

    public double getValorInicial() { return ValorInicial; }

    public void setValorInicial(double valorInicial) { ValorInicial = valorInicial; }

    public double getValorFinal() { return ValorFinal; }

    public void setValorFinal(double valorFinal) { ValorFinal = valorFinal; }

    public double getValorInicialElectronica() { return ValorInicialElectronica; }

    public void setValorInicialElectronica(double valorInicialElectronica) { ValorInicialElectronica = valorInicialElectronica; }

    public double getValorFinalElectronica() { return ValorFinalElectronica; }

    public void setValorFinalElectronica(double valorFinalElectronica) { ValorFinalElectronica = valorFinalElectronica; }

    public double getDiferenciaImporteDespacho() { return DiferenciaImporteDespacho; }

    public void setDiferenciaImporteDespacho(double diferenciaImporteDespacho) { DiferenciaImporteDespacho = diferenciaImporteDespacho; }

    public double getDiferenciaLecturasElectronicasMecanicas() { return DiferenciaLecturasElectronicasMecanicas; }

    public void setDiferenciaLecturasElectronicasMecanicas(double diferenciaLecturasElectronicasMecanicas) { DiferenciaLecturasElectronicasMecanicas = diferenciaLecturasElectronicasMecanicas; }

    public double getDiferenciaLecturasElectronicasDespachos() { return DiferenciaLecturasElectronicasDespachos; }

    public void setDiferenciaLecturasElectronicasDespachos(double diferenciaLecturasElectronicasDespachos) { DiferenciaLecturasElectronicasDespachos = diferenciaLecturasElectronicasDespachos; }
}
