package com.corpogas.corpoapp.Configuracion;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import com.corpogas.corpoapp.Entities.Cortes.CierreFajilla;
import com.corpogas.corpoapp.Entities.Estaciones.Empleado;
import com.corpogas.corpoapp.Entities.Estaciones.RecepcionFajilla;
import com.corpogas.corpoapp.Entities.FormasPago.FormasPagoCataglogo;
import com.corpogas.corpoapp.Entities.Sucursales.PriceBankRoll;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SQLiteBD extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "ConfiguracionEstacion.db";


    public SQLiteBD(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TBL_CONFIGURACION_ESTACION);
        db.execSQL(TBL_ENCABEZADO_ESTACION);
        db.execSQL(TBL_DATOS_TARJETERO);
        db.execSQL(TBL_ACTUALIZADOR_APP);
        db.execSQL(TBL_EMPLEADO);
        db.execSQL(TBL_PRECIO_FAJILLAS);
        db.execSQL(TBL_FAJILLAS);
        db.execSQL(TBL_PICOS);
        db.execSQL(TBL_PAGOTARJETA);
        db.execSQL(TBL_PAGOTARJETA_DIFERENTESFORMASPAGO);
        db.execSQL(TBL_MAXIMO_EFECTIVO_SUCURSAL);
        db.execSQL(TBL_FORMASPAGO);
        db.execSQL(TBL_TARJETAQRPUNTADAYENA);
        db.execSQL(TBL_TOKEN);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_CONFIGURACION_ESTACION);
        db.execSQL(SQL_DELETE_ENCABEZADO);
        db.execSQL(SQL_DELETE_DATOS_TARJETERO);
        db.execSQL(SQL_DELETE_ACTUALIZADOR_APP);
        db.execSQL(SQL_DELETE_TBL_EMPLEADO);
        db.execSQL(SQL_DELETE_TBL_PICOS);
        db.execSQL(SQL_DELETE_TBL_PRECIO_FAJILLAS);
        db.execSQL(SQL_DELETE_PAGOTARJETA);
        db.execSQL(SQL_DELETE_TOKEN);
        onCreate(db);

    }


    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }



    public boolean checkDataBase(String Database_path) {
        SQLiteDatabase checkDB = null;
        try {
            checkDB = SQLiteDatabase.openDatabase(Database_path, null, SQLiteDatabase.OPEN_READONLY);
            checkDB.close();
        } catch (SQLiteException e) {
            Log.e("Error", "No existe la base de datos ");
        }
        return checkDB != null;
    }

    public void InsertarDatosPagoTarjeta(String id, String poscioncarga, String formapagoid,String monto, String puntada, String provienede, String correctoid, String numerotarjeta, String descuento, String nipcliente, String montoTotal, String responsepagotarjeta){
        SQLiteDatabase base = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatosPagoTarjetas.Id, id);
        values.put(DatosPagoTarjetas.posicionCarga, poscioncarga);
        values.put(DatosPagoTarjetas.formaPagoId, formapagoid);
        values.put(DatosPagoTarjetas.monto, monto);
        values.put(DatosPagoTarjetas.puntadaid, puntada);
        values.put(DatosPagoTarjetas.provieneid, provienede);
        values.put(DatosPagoTarjetas.correctoid, correctoid);
        values.put(DatosPagoTarjetas.numerotarjeta, numerotarjeta);
        values.put(DatosPagoTarjetas.descuento, descuento);
        values.put(DatosPagoTarjetas.nipcliente, nipcliente);
        values.put(DatosPagoTarjetas.montototal, montoTotal);
        values.put(DatosPagoTarjetas.responsepagotarjeta, responsepagotarjeta);

        long newRowId = base.insert(DatosPagoTarjetas.nombreTabla, null, values);
    }


    public void InsertarDatosPagoTarjetaDFP(String iddfp, String montototal, String formapagoiddfp,String response, String montoparcial, String cobrado, String posicioncarga, String numinternoposicioncarga){
        SQLiteDatabase base = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatosPagoTarjetasDiferentesFormasPago.IdDFP, iddfp);
        values.put(DatosPagoTarjetasDiferentesFormasPago.montoTotal, montototal);
        values.put(DatosPagoTarjetasDiferentesFormasPago.formaPagoIdDFP, formapagoiddfp);
        values.put(DatosPagoTarjetasDiferentesFormasPago.response, response);
        values.put(DatosPagoTarjetasDiferentesFormasPago.montoparcial, montoparcial);
        values.put(DatosPagoTarjetasDiferentesFormasPago.cobrado, cobrado);
        values.put(DatosPagoTarjetasDiferentesFormasPago.posicioncarga, posicioncarga);
        values.put(DatosPagoTarjetasDiferentesFormasPago.numinternoposicioncarga, numinternoposicioncarga);
        long newRowId = base.insert(DatosPagoTarjetasDiferentesFormasPago.nombreTabla, null, values);
    }




//    public static final String nombreTabla = "PagoTarjetaDiferentesFormasPago";
//    public static final String IdDFP = "IDFP";
//    public static final String montoTotal = "MontoTotal";
//    public static final String formaPagoIdDFP = "FormaPagoIdDFP";
//    public static final String montoparcial = "MontoParcial";


    public void InsertarDatosEstacion(String idempresa, String sucursalid,String siic,String correo, String empresaid, String ipestacion, String nombreestacion, String numerofranquicia, String numerointerno, String tipo){
        SQLiteDatabase base = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Datosempresa.IDEMPRESA, idempresa);
        values.put(Datosempresa.SUCURSALID, sucursalid);
        values.put(Datosempresa.SIIC, siic);
        values.put(Datosempresa.CORREO, correo);
        values.put(Datosempresa.EMPRESAID, empresaid);
        values.put(Datosempresa.IPESTACION, ipestacion);
        values.put(Datosempresa.NOMBREESTACION, nombreestacion);
        values.put(Datosempresa.NUMEROFRANQUICIA, numerofranquicia);
        values.put(Datosempresa.NUMEROINTERNO, numerointerno);
        values.put(Datosempresa.TIPO, tipo);

        long newRowId = base.insert(Datosempresa.NOMBRE_TABLA, null, values);
    }


    public String getposcioncarga(){
        SQLiteDatabase base = getReadableDatabase();
        Cursor cursor = base.rawQuery("SELECT posicionCarga FROM pagotarjeta", null);
        cursor.moveToFirst();
        String tipo = cursor.getString(0);
        return tipo;
    }

    public String getformapagoid(){
        SQLiteDatabase base = getReadableDatabase();
        Cursor cursor = base.rawQuery("SELECT formaPagoId FROM pagotarjeta", null);
        cursor.moveToFirst();
        String tipo = cursor.getString(0);
        return tipo;
    }

    public String getmonto(){
        SQLiteDatabase base = getReadableDatabase();
        Cursor cursor = base.rawQuery("SELECT monto FROM pagotarjeta", null);
        cursor.moveToFirst();
        String tipo = cursor.getString(0);
        return tipo;
    }

    public String getResponsePagoTarjeta(){
        SQLiteDatabase base = getReadableDatabase();
        Cursor cursor = base.rawQuery("SELECT responsepagotarjeta FROM pagotarjeta", null);
        cursor.moveToFirst();
        String responsepagotarjeta = cursor.getString(0);
        return responsepagotarjeta;
    }




    public String getmontototaldfp(){
        SQLiteDatabase base = getReadableDatabase();
        Cursor cursor = base.rawQuery("SELECT montototal FROM pagotarjeta", null);
        cursor.moveToFirst();
        String tipo = cursor.getString(0);
        return tipo;
    }

    public String getbanderapuntada(){
        SQLiteDatabase base = getReadableDatabase();
        Cursor cursor = base.rawQuery("SELECT puntadaid FROM pagotarjeta", null);
        cursor.moveToFirst();
        String tipo = cursor.getString(0);
        return tipo;
    }

    public String getlugarformapagodiferentes(){
        SQLiteDatabase base = getReadableDatabase();
        Cursor cursor = base.rawQuery("SELECT provieneid FROM pagotarjeta", null);
        cursor.moveToFirst();
        String tipo = cursor.getString(0);
        return tipo;
    }

    public String getCorrectoIncorrecto()
    {
        SQLiteDatabase base = getReadableDatabase();
        Cursor cursor = base.rawQuery("SELECT correctoid FROM pagotarjeta", null);
        cursor.moveToFirst();
        String tipo = cursor.getString(0);
        return tipo;
    }


    public String getNumeroTarjetaIni()
    {
        SQLiteDatabase base = getReadableDatabase();
        Cursor cursor = base.rawQuery("SELECT numerotarjeta FROM pagotarjeta", null);
        cursor.moveToFirst();
        String tipo = cursor.getString(0);
        return tipo;
    }

    public String getDescuentoIni()
    {
        SQLiteDatabase base = getReadableDatabase();
        Cursor cursor = base.rawQuery("SELECT descuento FROM pagotarjeta", null);
        cursor.moveToFirst();
        String tipo = cursor.getString(0);
        return tipo;
    }

    public String getNipIni()
    {
        SQLiteDatabase base = getReadableDatabase();
        Cursor cursor = base.rawQuery("SELECT nipcliente FROM pagotarjeta", null);
        cursor.moveToFirst();
        String tipo = cursor.getString(0);
        return tipo;
    }



    public String getempresaid(){
        SQLiteDatabase base = getReadableDatabase();
        Cursor cursor = base.rawQuery("SELECT empresaid FROM configuracionestacion", null);
        cursor.moveToFirst();
        String tipo = cursor.getString(0);
        return tipo;
    }

    public String getIdEstacion(){
        SQLiteDatabase base = getReadableDatabase();
        Cursor cursor = base.rawQuery("SELECT idempresa FROM configuracionestacion", null);
        cursor.moveToFirst();
        String idempresa = cursor.getString(0);
        return idempresa;
    }
    public String getNumeroEstacion(){
        SQLiteDatabase base = getReadableDatabase();
        Cursor cursor = base.rawQuery("SELECT numerofranquicia FROM configuracionestacion", null);
        cursor.moveToFirst();
        String idempresa = cursor.getString(0);
        return idempresa;
    }
    public String getSIIC(){
        SQLiteDatabase base = getReadableDatabase();
        Cursor cursor = base.rawQuery("SELECT siic FROM configuracionestacion", null);
        cursor.moveToFirst();
        String tipo = cursor.getString(0);
        return tipo;
    }
    public String getNombreEstacion (){
        SQLiteDatabase base = getReadableDatabase();
        Cursor cursor = base.rawQuery("SELECT nombreestacion FROM configuracionestacion", null);
        cursor.moveToFirst();
        String nombreestacion = cursor.getString(0);
        return nombreestacion;
    }
    public String getIpEstacion(){
        SQLiteDatabase base = getReadableDatabase();
        Cursor cursor = base.rawQuery("SELECT ipestacion FROM configuracionestacion", null);
        cursor.moveToFirst();
        String ipestacion = cursor.getString(0);
        return ipestacion;
    }
    public String getTipoEstacion(){
        SQLiteDatabase base = getReadableDatabase();
        Cursor cursor = base.rawQuery("SELECT tipo FROM configuracionestacion", null);
        cursor.moveToFirst();
        String tipo = cursor.getString(0);
        return tipo;
    }
    public String getIdSucursal(){
        SQLiteDatabase base = getReadableDatabase();
        Cursor cursor = base.rawQuery("SELECT sucursalid FROM configuracionestacion", null);
        cursor.moveToFirst();
        String idempresa = cursor.getString(0);
        return idempresa;
    }

    public String getNumeroFranquicia(){
        SQLiteDatabase base = getReadableDatabase();
        Cursor cursor = base.rawQuery("SELECT numerofranquicia FROM configuracionestacion", null);
        cursor.moveToFirst();
        String nummerofranquicia = cursor.getString(0);
        return nummerofranquicia;
    }

    public void execSQL(String sqlDeleteConfiguracionEstacion) {
    }


    public static class DatosFormasPago implements BaseColumns{
        public static final String NOMBRE_TABLA = "formasPago";
        public static final String IDFORMAPAGO = "idformapago";
        public static final String NOMBREPAGO = "nombrepago";
        public static final String VISIBLEINVISIBLE = "visible";
        public static final String ACUMULAPUNTOS = "acumula";
    }
    private static final String TBL_FORMASPAGO =
        "CREATE TABLE " + DatosFormasPago.NOMBRE_TABLA + " (" +
                          DatosFormasPago.IDFORMAPAGO + " INTEGER PRIMARY KEY," +
                          DatosFormasPago.NOMBREPAGO + " TEXT," +
                          DatosFormasPago.VISIBLEINVISIBLE + " BOOLEAN," +
                          DatosFormasPago.ACUMULAPUNTOS + " BOOLEAN)";

    public void InsertarDatosFormasPago(Integer idformapago, String nombrepago, Boolean visible, Boolean acumulapuntos){
        SQLiteDatabase base = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatosFormasPago.IDFORMAPAGO, idformapago);
        values.put(DatosFormasPago.NOMBREPAGO, nombrepago);
        values.put(DatosFormasPago.VISIBLEINVISIBLE, visible);
        values.put(DatosFormasPago.ACUMULAPUNTOS, acumulapuntos);

        long newRowId = base.insert(DatosFormasPago.NOMBRE_TABLA, null, values);
    }

    public int getFormaPagoPuntadaYena(Integer identificador){
        SQLiteDatabase base = getReadableDatabase(); //
        Cursor mCount= base.rawQuery("SELECT  count(*) FROM formaspago  WHERE idformapago = " + identificador, null);
        mCount.moveToFirst();
        int count= mCount.getInt(0);
        mCount.close();
        return count;

    }

    public List<FormasPagoCataglogo> getFormasPago(){
        SQLiteDatabase base = getReadableDatabase();
        Cursor cursor = base.rawQuery("SELECT * FROM FormasPago", null);
        List<FormasPagoCataglogo> lFormasPago = new ArrayList<>();
        if (cursor.moveToFirst()){
            do{
                lFormasPago.add(new FormasPagoCataglogo(cursor.getInt(1), cursor.getString(2), cursor.getInt(3), cursor.getInt(4)));
            }while (cursor.moveToNext());
        }
        return lFormasPago;
    }

    public static class DatosTarjetaPuntadaYena implements  BaseColumns{
        public static final String NOMBRE_TABLA = "tarjetaqr";
        public static final String IDPOSICIONCARGA = "idposicioncarga";
        public static final String NUMEROPOSICIONCARGA = "numeroposicioncarga";
        public static final String NUMEROTARJETA = "numerotarjeta";
        public static final String NIPTARJETAPUNTADAYENA = "nip";
        public static final String DESCUENTOTARJETA = "descuento";

    }

    private static final String TBL_TARJETAQRPUNTADAYENA =
            "CREATE TABLE " + DatosTarjetaPuntadaYena.NOMBRE_TABLA + " (" +
                    DatosTarjetaPuntadaYena.IDPOSICIONCARGA + " INTEGER PRIMARY KEY," +
                    DatosTarjetaPuntadaYena.NUMEROPOSICIONCARGA + " TEXT," +
                    DatosTarjetaPuntadaYena.NUMEROTARJETA + " TEXT," +
                    DatosTarjetaPuntadaYena.NIPTARJETAPUNTADAYENA + " TEXT," +
                    DatosTarjetaPuntadaYena.DESCUENTOTARJETA + " REAL)";

    public void InsertarDatosTarjetaPuntadaYena(Integer idposicionCarga, String numeroposicioncarga, String numerotarjeta, String niptarjeta, Double descuentotarjeta){
        SQLiteDatabase base = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatosTarjetaPuntadaYena.IDPOSICIONCARGA, idposicionCarga);
        values.put(DatosTarjetaPuntadaYena.NUMEROPOSICIONCARGA, numeroposicioncarga);
        values.put(DatosTarjetaPuntadaYena.NUMEROTARJETA, numerotarjeta);
        values.put(DatosTarjetaPuntadaYena.NIPTARJETAPUNTADAYENA, niptarjeta);
        values.put(DatosTarjetaPuntadaYena.DESCUENTOTARJETA, descuentotarjeta);

        long newRowId = base.insert(DatosFormasPago.NOMBRE_TABLA, null, values);
    }


    public String getNumeroPosicionCargaTarjetaPuntadaYena(String posicioncargaseleccionada){
        SQLiteDatabase base = getReadableDatabase();
        Cursor cursor = base.rawQuery("SELECT numeroposicioncarga FROM tarjetaqr WHERE idposicioncarga = " + posicioncargaseleccionada, null);
        cursor.moveToFirst();
        String numeroposicioncargaqr = cursor.getString(0);
        return numeroposicioncargaqr;
    }

    public String getNumeroTarjetaPuntadaYena(String posicioncargaseleccionada){
        SQLiteDatabase base = getReadableDatabase();
        Cursor cursor = base.rawQuery("SELECT numerotarjeta FROM tarjetaqr WHERE idposicioncarga = " + posicioncargaseleccionada, null);
        cursor.moveToFirst();
        String numerotarjetaqr = cursor.getString(0);
        return numerotarjetaqr;
    }

    public String getNipPuntadaYena(String posicioncargaseleccionada){
        SQLiteDatabase base = getReadableDatabase();
        Cursor cursor = base.rawQuery("SELECT niptarjeta FROM tarjetaqr WHERE idposicioncarga = " + posicioncargaseleccionada, null);
        cursor.moveToFirst();
        String numeroposicioncargaqr = cursor.getString(0);
        return numeroposicioncargaqr;
    }

    public Integer getDescuentoPuntadaYena(String posicioncargaseleccionada){
        SQLiteDatabase base = getReadableDatabase();
        Cursor cursor = base.rawQuery("SELECT descuentotarjeta FROM tarjetaqr WHERE idposicioncarga = " + posicioncargaseleccionada, null);
        cursor.moveToFirst();
        Integer numeroposicioncargaqr = cursor.getInt(0);
        return numeroposicioncargaqr;
    }


    public static class Datosempresa implements BaseColumns {
        public static final String NOMBRE_TABLA = "configuracionestacion";
        public static final String IDEMPRESA = "idempresa";
        public static final String SUCURSALID = "sucursalid";
        public static final String SIIC = "siic";
        public static final String CORREO = "correo";
        public static final String EMPRESAID = "empresaid";
        public static final String IPESTACION = "ipestacion";
        public static final String NOMBREESTACION = "nombreestacion";
        public static final String NUMEROFRANQUICIA = "numerofranquicia";
        public static final String NUMEROINTERNO = "numerointerno";
        public static final String TIPO = "tipo";
    }
    private static final String TBL_CONFIGURACION_ESTACION =
            "CREATE TABLE " + Datosempresa.NOMBRE_TABLA + " (" +
                    Datosempresa._ID + " INTEGER PRIMARY KEY," +
                    Datosempresa.IDEMPRESA + " TEXT," +
                    Datosempresa.SUCURSALID + " TEXT," +
                    Datosempresa.SIIC + " TEXT," +
                    Datosempresa.CORREO + " TEXT," +
                    Datosempresa.EMPRESAID + " TEXT," +
                    Datosempresa.IPESTACION + " TEXT," +
                    Datosempresa.NOMBREESTACION + " TEXT," +
                    Datosempresa.NUMEROFRANQUICIA + " TEXT," +
                    Datosempresa.NUMEROINTERNO + " TEXT," +
                    Datosempresa.TIPO + " TEXT)";

    public static final String SQL_DELETE_CONFIGURACION_ESTACION =
            "DROP TABLE IF EXISTS " + Datosempresa.NOMBRE_TABLA;

    public static class Datosencabezado implements BaseColumns{
        public static final String NOMBRE_TABLA_ENCABEZADO = "tblencabezado";
        public static final String EMPRESA_ID = "empresaid";
        public static final String RAZON_SOCIAL = "razonsocial";
        public static final String REGIMEN_FISCAL = "regimenfiscal";
        public static final String CALLE = "calle";
        public static final String NUMERO_EXTERIOR = "numeroexterior";
        public static final String NUMERO_INTERIOR = "numerointerior";
        public static final String COLONIA = "colonia";
        public static final String LOCALIDAD = "localidad";
        public static final String MUNICIPIO = "municipio";
        public static final String ESTADO = "estado";
        public static final String PAIS = "pais";
        public static final String CP = "cp";
        public static final String RFC = "rfc";
    }

    private static final String TBL_ENCABEZADO_ESTACION =
            "CREATE TABLE " + Datosencabezado.NOMBRE_TABLA_ENCABEZADO + " ("+
                    Datosencabezado._ID + " TEXT PRIMARY KEY," +
                    Datosencabezado.EMPRESA_ID + " TEXT," +
                    Datosencabezado.RAZON_SOCIAL + " TEXT," +
                    Datosencabezado.REGIMEN_FISCAL + " TEXT," +
                    Datosencabezado.CALLE + " TEXT," +
                    Datosencabezado.NUMERO_EXTERIOR + " TEXT," +
                    Datosencabezado.NUMERO_INTERIOR + " TEXT," +
                    Datosencabezado.COLONIA + " TEXT," +
                    Datosencabezado.LOCALIDAD + " TEXT," +
                    Datosencabezado.MUNICIPIO + " TEXT," +
                    Datosencabezado.ESTADO + " TEXT," +
                    Datosencabezado.PAIS + " TEXT," +
                    Datosencabezado.CP + " TEXT," +
                    Datosencabezado.RFC + " TEXT)";

    public static final String SQL_DELETE_ENCABEZADO =
            "DROP TABLE IF EXISTS " + Datosencabezado.NOMBRE_TABLA_ENCABEZADO;



    public void InsertarDatosEncabezado(String empresaid, String razonsocial, String regimenfiscal, String calle, String numeroexterior, String numerointerior, String colonia, String localidad, String municipio, String estado, String pais, String cp, String rfc){
        SQLiteDatabase base = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Datosencabezado.EMPRESA_ID,empresaid);
        values.put(Datosencabezado.RAZON_SOCIAL,razonsocial);
        values.put(Datosencabezado.REGIMEN_FISCAL, regimenfiscal);
        values.put(Datosencabezado.CALLE, calle);
        values.put(Datosencabezado.NUMERO_EXTERIOR, numeroexterior);
        values.put(Datosencabezado.NUMERO_INTERIOR,numerointerior);
        values.put(Datosencabezado.COLONIA, colonia);
        values.put(Datosencabezado.LOCALIDAD,localidad);
        values.put(Datosencabezado.MUNICIPIO, municipio);
        values.put(Datosencabezado.ESTADO, estado);
        values.put(Datosencabezado.PAIS, pais);
        values.put(Datosencabezado.CP,cp);
        values.put(Datosencabezado.RFC, rfc);

        long newRowId = base.insert(Datosencabezado.NOMBRE_TABLA_ENCABEZADO, null, values);
    }

    public String getRazonSocial(){
        SQLiteDatabase base = getReadableDatabase();
        Cursor cursor = base.rawQuery("SELECT razonsocial FROM tblencabezado", null);
        cursor.moveToFirst();
        String tipo = cursor.getString(0);
        return tipo;
    }
    public String getRegimenFiscal(){
        SQLiteDatabase base = getReadableDatabase();
        Cursor cursor = base.rawQuery("SELECT regimenfiscal FROM tblencabezado", null);
        cursor.moveToFirst();
        String tipo = cursor.getString(0);
        return tipo;
    }
    public String getCalle(){
        SQLiteDatabase base = getReadableDatabase();
        Cursor cursor = base.rawQuery("SELECT calle FROM tblencabezado", null);
        cursor.moveToFirst();
        String tipo = cursor.getString(0);
        return tipo;
    }
    public String getNumeroExterior(){
        SQLiteDatabase base = getReadableDatabase();
        Cursor cursor = base.rawQuery("SELECT numeroexterior FROM tblencabezado", null);
        cursor.moveToFirst();
        String tipo = cursor.getString(0);
        return tipo;
    }
    public String getNumeroInterno(){
        SQLiteDatabase base = getReadableDatabase();
        Cursor cursor = base.rawQuery("SELECT numerointerior FROM tblencabezado", null);
        cursor.moveToFirst();
        String tipo = cursor.getString(0);
        return tipo;
    }
    public String getColonia(){
        SQLiteDatabase base = getReadableDatabase();
        Cursor cursor = base.rawQuery("SELECT colonia FROM tblencabezado", null);
        cursor.moveToFirst();
        String tipo = cursor.getString(0);
        return tipo;
    }
    public String getLocalidad(){
        SQLiteDatabase base = getReadableDatabase();
        Cursor cursor = base.rawQuery("SELECT localidad FROM tblencabezado", null);
        cursor.moveToFirst();
        String tipo = cursor.getString(0);
        return tipo;
    }
    public String getMunicipio(){
        SQLiteDatabase base = getReadableDatabase();
        Cursor cursor = base.rawQuery("SELECT municipio FROM tblencabezado", null);
        cursor.moveToFirst();
        String tipo = cursor.getString(0);
        return tipo;
    }
    public String getEstado(){
        SQLiteDatabase base = getReadableDatabase();
        Cursor cursor = base.rawQuery("SELECT estado FROM tblencabezado", null);
        cursor.moveToFirst();
        String tipo = cursor.getString(0);
        return tipo;
    }
    public String getPais(){
        SQLiteDatabase base = getReadableDatabase();
        Cursor cursor = base.rawQuery("SELECT pais FROM tblencabezado", null);
        cursor.moveToFirst();
        String tipo = cursor.getString(0);
        return tipo;
    }
    public String getCP(){
        SQLiteDatabase base = getReadableDatabase();
        Cursor cursor = base.rawQuery("SELECT cp FROM tblencabezado", null);
        cursor.moveToFirst();
        String tipo = cursor.getString(0);
        return tipo;
    }
    public String getRFC(){
        SQLiteDatabase base = getReadableDatabase();
        Cursor cursor = base.rawQuery("SELECT rfc FROM tblencabezado", null);
        cursor.moveToFirst();
        String tipo = cursor.getString(0);
        return tipo;
    }


    //--------------------------------------TABLA EDICION Y CONFIGURACION DE LO MTODOS DE LA TABLA NUMERO TARJETERO--------------------------
    public static class DatosTarjetero implements BaseColumns{
        public static final String NOMBRE_TABLA_TARJETERO = "tblnumerotarjetero";
        public static final String DIRECCION_MAC = "direccionmac";
        //public static final String PROPIEDAD_CONEXION = "propiedadconexion";
        public static final String LECTOR_HUELA = "lectorhuella";
        public static final String ID_TARJETERO = "idtarjetero";
        public static final String IMPRIME_LOCAL = "imprimelocal";
    }

    private static final String TBL_DATOS_TARJETERO =
            "CREATE TABLE " + DatosTarjetero.NOMBRE_TABLA_TARJETERO + " ("+
                    DatosTarjetero._ID + " TEXT PRIMARY KEY," +
                    DatosTarjetero.DIRECCION_MAC + " TEXT," +
                    //DatosTarjetero.PROPIEDAD_CONEXION + " TEXT, "  +
                    DatosTarjetero.LECTOR_HUELA + " TEXT, "  +
                    DatosTarjetero.ID_TARJETERO + " TEXT, " +
                    DatosTarjetero.IMPRIME_LOCAL + " TEXT )";

    public static final String SQL_DELETE_DATOS_TARJETERO =
            "DROP TABLE IF EXISTS " + DatosTarjetero.NOMBRE_TABLA_TARJETERO;

    public void InsertarDatosNumeroTarjetero(String direccionmac, String lectorhuella, String idtarjetero, String imprimelocal){
        SQLiteDatabase base = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatosTarjetero.DIRECCION_MAC, direccionmac);
//        values.put(DatosTarjetero.PROPIEDAD_CONEXION, propiedadconexion);
        values.put(DatosTarjetero.LECTOR_HUELA, lectorhuella);
        values.put(DatosTarjetero.ID_TARJETERO, idtarjetero);
        values.put(DatosTarjetero.IMPRIME_LOCAL, imprimelocal);

        long newRowId = base.insert(DatosTarjetero.NOMBRE_TABLA_TARJETERO,null,values);
    }

    public String getDireccionMac(){
        SQLiteDatabase base = getReadableDatabase();
        Cursor cursor = base.rawQuery("SELECT tblnumero tarjetero FROM direccionmac", null);
        cursor.moveToFirst();
        String dato = cursor.getString(0);
        return dato;
    }
    public String getPropiedadConexion(){
        SQLiteDatabase base = getReadableDatabase();
        Cursor cursor = base.rawQuery("SELECT tblnumerotarjetero FROM propiedadconexion", null);
        cursor.moveToFirst();
        String dato = cursor.getString(0);
        return dato;
    }

    public String getIdTarjtero(){
        SQLiteDatabase base = getReadableDatabase();
        Cursor cursor = base.rawQuery("SELECT idtarjetero FROM tblnumerotarjetero", null);
        cursor.moveToFirst();
        String dato = cursor.getString(0);
        return dato;
    }

    public String getLectorHuella(){
        SQLiteDatabase base = getReadableDatabase();
        Cursor cursor = base.rawQuery("SELECT lectorhuella FROM tblnumerotarjetero", null);
        cursor.moveToFirst();
        String dato = cursor.getString(0);
        return dato;
    }

    public String getImprimeLocal(){
        SQLiteDatabase base = getReadableDatabase();
        Cursor cursor = base.rawQuery("SELECT imprimelocal FROM tblnumerotarjetero", null);
        cursor.moveToFirst();
        String dato = cursor.getString(0);
        return dato;
    }



//-------------------------------actualizar el apk------------------------------------------------

    public static class DatosActualizacionApp implements BaseColumns{
        public static final String NOMBRE_TABLA_ACTUALIZADOR_APP = "ApplicationUpdate";
        public static final String version = "version";
        public static final String filName = "fileName";
        public static final String deviceModel = "deviceModel";
    }

    private static final String TBL_ACTUALIZADOR_APP =
            "CREATE TABLE " + DatosActualizacionApp.NOMBRE_TABLA_ACTUALIZADOR_APP + " ("+
                    DatosActualizacionApp._ID + " TEXT PRIMARY KEY," +
                    DatosActualizacionApp.version + " TEXT," +
                    DatosActualizacionApp.filName + " TEXT, "  +
                    DatosActualizacionApp.deviceModel + " TEXT )";

    public static final String SQL_DELETE_ACTUALIZADOR_APP =
            "DROP TABLE IF EXISTS " + DatosActualizacionApp.NOMBRE_TABLA_ACTUALIZADOR_APP;




    public void InsertarActualizcionApp(String version,String fileName, String deviceModel){
        SQLiteDatabase base = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatosActualizacionApp.version, version);
        values.put(DatosActualizacionApp.filName, fileName);
        values.put(DatosActualizacionApp.deviceModel, deviceModel);

        long newRowId = base.insert(DatosActualizacionApp.NOMBRE_TABLA_ACTUALIZADOR_APP,null,values);
    }

    public String getVersionApk(){
        SQLiteDatabase base = getReadableDatabase();
        Cursor cursor = base.rawQuery("SELECT version FROM ApplicationUpdate", null);
        cursor.moveToFirst();
        String tipo = cursor.getString(0);
        return tipo;
    }

    public String getFileNameApk(){
        SQLiteDatabase base = getReadableDatabase();
        Cursor cursor = base.rawQuery("SELECT fileName FROM ApplicationUpdate", null);
        cursor.moveToFirst();
        String tipo = cursor.getString(0);
        return tipo;
    }

    public String getDeviceModelApk(){
        SQLiteDatabase base = getReadableDatabase();
        Cursor cursor = base.rawQuery("SELECT deviceModel FROM ApplicationUpdate", null);
        cursor.moveToFirst();
        String tipo = cursor.getString(0);
        return tipo;
    }

//    public String getActualizaAppId(){
//        SQLiteDatabase base = getReadableDatabase();
//        Cursor cursor = base.rawQuery("SELECT _id FROM ApplicationUpdate", null);
//        cursor.moveToFirst();
//        String tipo = cursor.getString(0);
//        return tipo;
//    }


    public boolean updateVersionAPP(String version,String fileName, String deviceModel){
        SQLiteDatabase base = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatosActualizacionApp.version,version);
        contentValues.put(DatosActualizacionApp.filName,fileName);
//        contentValues.put(DatosActualizacionApp.deviceModel,deviceModel);
        int cantidad = base.update(DatosActualizacionApp.NOMBRE_TABLA_ACTUALIZADOR_APP, contentValues, DatosActualizacionApp.deviceModel + " = ? " ,
                new String[]{deviceModel});
        if(cantidad!=0)
        {
            return  true;
        }else{
            return false;
        }
    }
//   -------------------------------TEMINAN LOS METODOS DE LA TABLA DEL NUMERO DEL TAJETERO ----------------------------------------------

    //<-------------------------------------------------------PARAMETROS DE LA TABLA EMPLEADO------------------------------------------------>

    public static class TokenAsignado implements BaseColumns{
        public static final String nombreTabla = "TokenAsignado";
        public static final String token = "Token";
    }

    //<------------------------------------------------------------------CREACION DE TABLA TOKENASIGNADO---------------------------------------------------------------------->

    private static final String TBL_TOKEN = "CREATE TABLE " + TokenAsignado.nombreTabla+ "("+
        TokenAsignado._ID + " INTEGER PRIMARY KEY," +
        TokenAsignado.token + " TEXT)";

    public static final String SQL_DELETE_TOKEN =
            "DROP TABLE IF EXISTS " + TokenAsignado.nombreTabla;

    //    <----------------------------------------------------------------------INSERT DE TABLA TOKENASIGNADO------------------------------------------------------------------->

    public void InsertarToken(String token){
        SQLiteDatabase base = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TokenAsignado.token, token);

        long newRowId = base.insert(TokenAsignado.nombreTabla, null, values);
    }

    //<----------------------------------------------------------------------SELECTS DE TABLA TOKENASIGNADO -------------------------------------------------------->

    public String getToken(){
        SQLiteDatabase base = getReadableDatabase();
        Cursor cursor = base.rawQuery("SELECT Token FROM TokenAsignado", null);
        cursor.moveToFirst();
        String token = cursor.getString(0);
        return token;
    }

    //<----------------------------------------------------------------------UPDATE DE TABLA TOKENASIGNADO -------------------------------------------------------->


    public boolean updateToken(String tokenObtenido){
        SQLiteDatabase base = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TokenAsignado.token,tokenObtenido);

        int datos = base.update(TokenAsignado.nombreTabla, contentValues, TokenAsignado._ID + " = ? ", new String[]{String.valueOf("1")});
        if (datos!=0){
            return true;
        }else{
            return false;
        }
    }


    //<-------------------------------------------------------PARAMETROS DE LA TABLA EMPLEADO------------------------------------------------>
    public static class DatosEmpleado implements BaseColumns {
        public static final String nombreTabla = "DatosEmpleado";
        public static final String sucursalId = "SucursalId";
        public static final String estacionId = "EstacionId";
        public static final String rolId = "RolId";
        public static final String nombre = "Nombre";
        public static final String apellidoPaterno = "ApellidoPaterno";
        public static final String apellidoMaterno = "ApellidoMaterno";
        public static final String nombreCompleto = "NombreCompleto";
        public static final String id = "id";
        public static final String clave = "Clave";
        public static final String activo = "Activo";
        public static final String correo = "Correo";
        public static final String numeroEmpleado = "NumeroEmpleado";
        public static final String rolDescripcion = "RolDescripcion";
        public static final String islaId = "IslaId";

    }
    //<------------------------------------------------------------------CREACION DE TABLA EMPLEADO---------------------------------------------------------------------->

    private static final String TBL_EMPLEADO = "CREATE TABLE " + DatosEmpleado.nombreTabla+ "("+
            DatosEmpleado._ID + " INTEGER PRIMARY KEY," +
            DatosEmpleado.sucursalId + " REAL," +
            DatosEmpleado.estacionId + " REAL," +
            DatosEmpleado.rolId + " REAL," +
            DatosEmpleado.nombre + " TEXT," +
            DatosEmpleado.apellidoPaterno + " TEXT," +
            DatosEmpleado.apellidoMaterno + " TEXT," +
            DatosEmpleado.nombreCompleto + " TEXT," +
            DatosEmpleado.id + " INTEGER," +
            DatosEmpleado.clave + " TEXT," +
            DatosEmpleado.activo + " INTEGER," +
            DatosEmpleado.correo + " TEXT," +
            DatosEmpleado.numeroEmpleado + " TEXT," +
            DatosEmpleado.rolDescripcion + " TEXT," +
            DatosEmpleado.islaId + " TEXT)";

    public static final String SQL_DELETE_TBL_EMPLEADO =
            "DROP TABLE IF EXISTS " + DatosEmpleado.nombreTabla;


        //    <----------------------------------------------------------------------INSERT DE TABLA EMPLEADO------------------------------------------------------------------->

    public void InsertarDatosEmpleado(long sucursalId, long estacionId, long rolId, String nombre, String apellidoPaterno, String apellidoMaterno, String nombreCompleto,
                                      long id, String clave, boolean activo, String correo, String numeroEmpleado, String rolDescripcion, long islaId){
        SQLiteDatabase base = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatosEmpleado.sucursalId, sucursalId);
        values.put(DatosEmpleado.estacionId, estacionId);
        values.put(DatosEmpleado.rolId, rolId);
        values.put(DatosEmpleado.nombre, nombre);
        values.put(DatosEmpleado.apellidoPaterno, apellidoPaterno);
        values.put(DatosEmpleado.apellidoMaterno, apellidoMaterno);
        values.put(DatosEmpleado.nombreCompleto, nombreCompleto);
        values.put(DatosEmpleado.id, id);
        values.put(DatosEmpleado.clave, clave);
        values.put(DatosEmpleado.activo, activo);
        values.put(DatosEmpleado.correo, correo);
        values.put(DatosEmpleado.numeroEmpleado, numeroEmpleado);
        values.put(DatosEmpleado.rolDescripcion, rolDescripcion);
        values.put(DatosEmpleado.islaId, islaId);

        long newRowId = base.insert(DatosEmpleado.nombreTabla, null, values);
    }
//<----------------------------------------------------------------------SELECTS DE TABLA EMPLEADO -------------------------------------------------------->

        public List<Empleado> getDatosEmpleado(){
        SQLiteDatabase base =getReadableDatabase();
        Cursor cursor = base.rawQuery("SELECT * FROM DatosEmpleado", null);
        List<Empleado> lEmpleado = new ArrayList<>();
        if (cursor.moveToFirst()){
            do{
                lEmpleado.add(new Empleado(cursor.getLong(1),cursor.getLong(2),cursor.getLong(3),cursor.getString(4),cursor.getString(5),
                              cursor.getString(6),cursor.getString(7),cursor.getInt(8),cursor.getString(9),cursor.getInt(10) != 0,cursor.getString(11),
                              cursor.getString(12)));

            }while(cursor.moveToNext());

        }
        return lEmpleado;
    }

    public String getNumeroEmpleado(){
        SQLiteDatabase base = getReadableDatabase();
        Cursor cursor = base.rawQuery("SELECT NumeroEmpleado FROM DatosEmpleado", null);
        cursor.moveToFirst();
        String tipo = cursor.getString(0);
        return tipo;
    }

    public String getClave(){
        SQLiteDatabase base = getReadableDatabase();
        Cursor cursor = base.rawQuery("SELECT Clave FROM DatosEmpleado", null);
        cursor.moveToFirst();
        String tipo = cursor.getString(0);
        return tipo;
    }

    public String getUsuarioId(){
        SQLiteDatabase base = getReadableDatabase();
        Cursor cursor = base.rawQuery("SELECT id FROM DatosEmpleado", null);
        cursor.moveToFirst();
        String tipo = cursor.getString(0);
        return tipo;
    }

    public String getNombreCompleto(){
        SQLiteDatabase base = getReadableDatabase();
        Cursor cursor = base.rawQuery("SELECT NombreCompleto FROM DatosEmpleado", null);
        cursor.moveToFirst();
        String nombreCompleto = cursor.getString(0);
        return nombreCompleto;
    }

    public String getNombre(){
        SQLiteDatabase base = getReadableDatabase();
        Cursor cursor = base.rawQuery("SELECT Nombre FROM DatosEmpleado", null);
        cursor.moveToFirst();
        String nombre = cursor.getString(0);
        return nombre;
    }

    public Long getRol(){
        SQLiteDatabase base = getReadableDatabase();
        Cursor cursor = base.rawQuery("SELECT RolId FROM DatosEmpleado", null);
        cursor.moveToFirst();
        Long rol = cursor.getLong(0);
        return rol;
    }

    public String getRolDescripcion(){
        SQLiteDatabase base = getReadableDatabase();
        Cursor cursor = base.rawQuery("SELECT RolDescripcion FROM DatosEmpleado", null);
        cursor.moveToFirst();
        String tipo = cursor.getString(0);
        return tipo;
    }

    public Long getIslaId(){
        SQLiteDatabase base = getReadableDatabase();
        Cursor cursor = base.rawQuery("SELECT IslaId FROM DatosEmpleado", null);
        cursor.moveToFirst();
        Long islaId = cursor.getLong(0);
        return islaId;
    }

    //  ----------------------------------------------------------------------------------------------------------------------------------------------------  //

           //<-------------------------------------------------------PARAMETROS DE LA TABLA FAJILLAS------------------------------------------------>

    public static class DatosFajillas implements BaseColumns{
        public static final String nombreTabla = "Fajillas";
        public static final String fajillasBilletes = "FajillasBilletes";
        public static final String fajillasMonedas = "FajillasMonedas";
        public static final String tipoFajilla = "TipoFajilla";

    }

    //<------------------------------------------------------------------CREACION DE TABLA FAJILLAS---------------------------------------------------------------------->

    public  static final String TBL_FAJILLAS = "CREATE TABLE " + DatosFajillas.nombreTabla+ "("+
            DatosFajillas._ID + " INTEGER PRIMARY KEY," +
            DatosFajillas.fajillasBilletes + " INTEGER," +
            DatosFajillas.fajillasMonedas + " INTEGER," +
            DatosFajillas.tipoFajilla + " INTEGER)";

    public static final String SQL_DELETE_TBL_FAJILLAS =
            "DROP TABLE IF EXISTS" + DatosFajillas.nombreTabla;

    //    <----------------------------------------------------------------------INSERT DE TABLA FAJILLAS------------------------------------------------------------------->

    public void InsertarFajillas(int fajillasBilletes, int fajillasMonedas, long tipoFajilla){
        SQLiteDatabase base = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatosFajillas.fajillasBilletes, fajillasBilletes);
        values.put(DatosFajillas.fajillasMonedas, fajillasMonedas);
        values.put(DatosFajillas.tipoFajilla, tipoFajilla);

        long newRowId = base.insert(DatosFajillas.nombreTabla,null,values);
    }

    //<----------------------------------------------------------------------SELECTS DE TABLA FAJILLAS -------------------------------------------------------->

    public List<CierreFajilla> getFajillas(){
        SQLiteDatabase base = getReadableDatabase();
        Cursor cursor = base.rawQuery("SELECT * FROM Fajillas", null);
        List<CierreFajilla> lFajillas = new ArrayList<>();
        if (cursor.moveToFirst()){
            do{
                lFajillas.add(new CierreFajilla(cursor.getInt(1), cursor.getInt(2), cursor.getLong(3)));
            }while (cursor.moveToNext());
        }
        return lFajillas;
    }

    //<----------------------------------------------------------------------UPDATE DE TABLA FAJILLAS -------------------------------------------------------->

    public boolean updateFajillas(int fajillasBilletes, int fajillasMonedas, long tipoFajilla){
        SQLiteDatabase base = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatosFajillas.fajillasBilletes,fajillasBilletes);
        contentValues.put(DatosFajillas.fajillasMonedas,fajillasMonedas);

        int datos = base.update(DatosFajillas.nombreTabla, contentValues, DatosFajillas.tipoFajilla + " = ? ",
                new String[]{String.valueOf(tipoFajilla)});
        if (datos!=0){
            return true;
        }else{
            return false;
        }
    }

    //  ----------------------------------------------------------------------------------------------------------------------------------------------------  //

    public boolean updateDiferentesFormasPago(String responseobtenido, String  cobrado, String formapagoid){
        SQLiteDatabase base = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatosPagoTarjetasDiferentesFormasPago.response,responseobtenido);
        contentValues.put(DatosPagoTarjetasDiferentesFormasPago.cobrado,cobrado);

        int datos = base.update(DatosPagoTarjetasDiferentesFormasPago.nombreTabla, contentValues, DatosPagoTarjetasDiferentesFormasPago.IdDFP + " = ? ",
                new String[]{formapagoid});
        if (datos!=0){
            return true;
        }else{
            return false;
        }
    }


    public boolean updateDiferentesFormasPagoMod(String responseobtenido, String  cobrado, String formapagoid){
        SQLiteDatabase base = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatosPagoTarjetasDiferentesFormasPago.response,responseobtenido);
        contentValues.put(DatosPagoTarjetasDiferentesFormasPago.cobrado,cobrado);

        int datos = base.update(DatosPagoTarjetasDiferentesFormasPago.nombreTabla, contentValues, DatosPagoTarjetasDiferentesFormasPago.IdDFP + " = ? ",
                new String[]{formapagoid});
        if (datos!=0){
            return true;
        }else{
            return false;
        }
    }




    public static class DatosPagoTarjetas implements BaseColumns{
       public static final String nombreTabla = "PagoTarjeta";
       public static final String Id = "ID";
       public static final String posicionCarga = "PosicionCarga";
       public static final String formaPagoId = "FormaPagoId";
       public static final String monto = "Monto";
       public static final String puntadaid = "PuntadaId";
       public static final String provieneid = "Provieneid";
       public static final String correctoid = "Correctoid";
       public static final String numerotarjeta = "NumeroTarjeta";
       public static final String descuento = "Descuento";
       public static final String nipcliente = "NipCliente";
       public static final String responsepagotarjeta = "ResponsePagoTarjeta";
       public static final String montototal = "MontoTotal";

    }


    public static class DatosPagoTarjetasDiferentesFormasPago implements  BaseColumns{
        public static final String nombreTabla = "PagoTarjetaDiferentesFormasPago";
        public static final String IdDFP = "IDFP";
        public static final String montoTotal = "MontoTotal";
        public static final String formaPagoIdDFP = "FormaPagoIdDFP";
        public static final String response = "Response";
        public static final String montoparcial = "MontoParcial";
        public static final String cobrado = "Cobrado";
        public static final String posicioncarga = "PosicionCarga";
        public static final String numinternoposicioncarga = "NumInternoPosicionCarga";
    }

    public static final String SQL_DELETE_PAGOTARJETA=
            "DROP TABLE " + DatosPagoTarjetas.nombreTabla;


    //<-------------------------------------------------------PARAMETROS DE LA TABLA PICOS------------------------------------------------>

    public static class DatosPicos implements BaseColumns{

        public static final String nombreTabla = "Picos";
        public static final String sucursalId = "SucursalId";
        public static final String turnoId = "TurnoId";
        public static final String tipoFajilla = "TipoFajilla";
        public static final String cantidad = "Cantidad";
        public static final String denominacion = "Denominacion";
        public static final String picosMonedas = "PicosMonedas";
        public static final String sumaBilletes = "sumaBilletes";

    }

    private static final String TBL_PAGOTARJETA = "CREATE TABLE " + DatosPagoTarjetas.nombreTabla+ "("+
            DatosPagoTarjetas.Id + " INTEGER PRIMARY KEY," +
            DatosPagoTarjetas.posicionCarga + " INTEGER,"+
            DatosPagoTarjetas.formaPagoId + " INTEGER,"+
            DatosPagoTarjetas.monto+ " REAL," +
            DatosPagoTarjetas.puntadaid+ " INTEGER,"+
            DatosPagoTarjetas.provieneid + " INTEGER,"+
            DatosPagoTarjetas.correctoid + " INTEGER,"+
            DatosPagoTarjetas.numerotarjeta + " STRING,"+
            DatosPagoTarjetas.descuento + " REAL,"+
            DatosPagoTarjetas.nipcliente + " INTEGER," +
            DatosPagoTarjetas.responsepagotarjeta + " STRING," +
            DatosPagoTarjetas.montototal + " REAL)";


    private static final String TBL_PAGOTARJETA_DIFERENTESFORMASPAGO = "CREATE TABLE " + DatosPagoTarjetasDiferentesFormasPago.nombreTabla+ "("+
            DatosPagoTarjetasDiferentesFormasPago.IdDFP + " INTEGER PRIMARY KEY," +
            DatosPagoTarjetasDiferentesFormasPago.montoTotal + " REAL," +
            DatosPagoTarjetasDiferentesFormasPago.formaPagoIdDFP + " INTEGER," +
            DatosPagoTarjetasDiferentesFormasPago.response + " STRING," +
            DatosPagoTarjetasDiferentesFormasPago.montoparcial + " REAL," +
            DatosPagoTarjetasDiferentesFormasPago.cobrado + " INTEGER," +
            DatosPagoTarjetasDiferentesFormasPago.posicioncarga + " STRING,"+
            DatosPagoTarjetasDiferentesFormasPago.numinternoposicioncarga  + " STRING)";

    //<------------------------------------------------------------------CREACION DE TABLA PICOS---------------------------------------------------------------------->

    private static final String TBL_PICOS = "CREATE TABLE " + DatosPicos.nombreTabla+ "("+
            DatosPicos._ID + " INTEGER PRIMARY KEY," +
            DatosPicos.sucursalId + " INTEGER," +
            DatosPicos.turnoId + " INTEGER," +
            DatosPicos.tipoFajilla + " INTEGER," +
            DatosPicos.cantidad + " INTEGER," +
            DatosPicos.denominacion + " INTEGER," +
            DatosPicos.picosMonedas + " REAL," +
            DatosPicos.sumaBilletes + " INTEGER)";

    public static final String SQL_DELETE_TBL_PICOS=
            "DROP TABLE " + DatosPicos.nombreTabla;

    //    <----------------------------------------------------------------------INSERT DE TABLA PICOS------------------------------------------------------------------->

    public void InsertarPicos(long sucursalId, long turnoId, long tipoFajilla, long cantidad, int denominacion, double picosMonedas, int sumaBilletes){
        SQLiteDatabase base = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatosPicos.sucursalId, sucursalId);
        values.put(DatosPicos.turnoId, turnoId);
        values.put(DatosPicos.tipoFajilla, tipoFajilla);
        values.put(DatosPicos.cantidad, cantidad);
        values.put(DatosPicos.denominacion, denominacion);
        values.put(DatosPicos.picosMonedas, picosMonedas);
        values.put(DatosPicos.sumaBilletes, sumaBilletes);

        long newRowId = base.insert(DatosPicos.nombreTabla,null,values);
    }

    //<----------------------------------------------------------------------SELECTS DE TABLA PICOS -------------------------------------------------------->

    public List<RecepcionFajilla> getPicos(){
        SQLiteDatabase base = getReadableDatabase();
        Cursor cursor = base.rawQuery("SELECT * FROM Picos", null);
        List<RecepcionFajilla> lCierreFajilla = new ArrayList<>();
        if(cursor.moveToFirst()){
            do {
                lCierreFajilla.add(new RecepcionFajilla(cursor.getLong(1),cursor.getLong(2),cursor.getLong(3),cursor.getInt(4), cursor.getDouble(5), cursor.getDouble(6), cursor.getInt(7)));
            }while (cursor.moveToNext());
        }

        return lCierreFajilla;
    }

    //<----------------------------------------------------------------------UPDATE DE TABLA PICOS -------------------------------------------------------->

    public boolean  updatePicos(int cantidad,int sumaBilletes,double denominacion,long tipoFajilla)
    {
        SQLiteDatabase base = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatosPicos.cantidad,cantidad);
        contentValues.put(DatosPicos.sumaBilletes,sumaBilletes);
        int datos = base.update(DatosPicos.nombreTabla, contentValues, DatosPicos.denominacion + " = ? "+
                        "and "+DatosPicos.tipoFajilla+ " = ? ",
                new String[]{String.valueOf(denominacion),String.valueOf(tipoFajilla)});
        if(datos!=0)
        {
            return  true;
        }else{
            return false;
        }
    }

    public boolean  updatePicos(double denominacion,int tipoFajilla)
    {
        SQLiteDatabase base = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatosPicos.denominacion,denominacion);
        int datos = base.update(DatosPicos.nombreTabla, contentValues, DatosPicos.tipoFajilla + " = ? ",
                new String[]{String.valueOf(tipoFajilla)});
        if(datos!=0)
        {
            return  true;
        }else{
            return false;
        }
    }

    //  ----------------------------------------------------------------------------------------------------------------------------------------------------  //

    public boolean  updatePagoTarjetaCorrecto(int correctoIncorrecto)
    {
        SQLiteDatabase base = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatosPagoTarjetas.correctoid, correctoIncorrecto);
        int datos = base.update(DatosPagoTarjetas.nombreTabla, contentValues, DatosPagoTarjetas.Id + " = ? ",
                new String[]{String.valueOf("1")});
        if(datos!=0)
        {
            return  true;
        }else{
            return false;
        }
    }

    public boolean  updatePagoTarjetaResponse(String response)
    {
        SQLiteDatabase base = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatosPagoTarjetas.responsepagotarjeta,response);
        int datos = base.update(DatosPagoTarjetas.nombreTabla, contentValues, DatosPagoTarjetas.Id + " = ? ",
                new String[]{String.valueOf("1")});
        if(datos!=0)
        {
            return  true;
        }else{
            return false;
        }
    }

    //<-------------------------------------------------------PARAMETROS DE LA TABLA PRECIOFAJILLAS------------------------------------------------>

    public static class PrecioFajillas implements BaseColumns {

        public static final String nombreTabla = "PrecioFajillas";
        public static final String sucursalId = "SucursalId";
        public static final String bankRollType = "BankRollType";
        public static final String price = "Price";
    }

    //<------------------------------------------------------------------CREACION DE TABLA PRECIOFAJILLAS---------------------------------------------------------------------->

    private static final String TBL_PRECIO_FAJILLAS = "CREATE TABLE " + PrecioFajillas.nombreTabla+ "("+
            PrecioFajillas._ID + " INTEGER PRIMARY KEY," +
            PrecioFajillas.sucursalId + " INTEGER," +
            PrecioFajillas.bankRollType + " INTEGER," +
            PrecioFajillas.price + " INTEGER)";

    public static final String SQL_DELETE_TBL_PRECIO_FAJILLAS=
            "DROP TABLE IF EXISTS " + PrecioFajillas.nombreTabla;

    //    <----------------------------------------------------------------------INSERT DE TABLA PRECIOFAJILLAS------------------------------------------------------------------->

    public void InsertarPrecioFajillas(long sucursalId, long bankRollType, int price){
        SQLiteDatabase base = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PrecioFajillas.sucursalId, sucursalId);
        values.put(PrecioFajillas.bankRollType, bankRollType);
        values.put(PrecioFajillas.price, price);

        long newRowId = base.insert(PrecioFajillas.nombreTabla,null,values);
    }

    //<----------------------------------------------------------------------SELECTS DE TABLA PICOS -------------------------------------------------------->

    public List<PriceBankRoll> getPrecioFajillas(){
        SQLiteDatabase base = getReadableDatabase();
        Cursor cursor = base.rawQuery("SELECT * FROM PrecioFajillas", null);
        List<PriceBankRoll> lPrecioFajillas = new ArrayList<>();
        if(cursor.moveToFirst()){
            do {
                lPrecioFajillas.add(new PriceBankRoll(cursor.getLong(1),cursor.getLong(2),cursor.getInt(3)));
            }while (cursor.moveToNext());
        }

        return lPrecioFajillas;
    }

    public Integer getPrecioFajillaBillete(){
        SQLiteDatabase base = getReadableDatabase();
        Cursor cursor = base.rawQuery("SELECT Price FROM PrecioFajillas WHERE BankRollType = 1", null);
        cursor.moveToFirst();
        Integer precioFajillaBillete = cursor.getInt(0);
        return precioFajillaBillete;
    }

    public Integer getPrecioFajillaMoneda(){
        SQLiteDatabase base = getReadableDatabase();
        Cursor cursor = base.rawQuery("SELECT Price FROM PrecioFajillas WHERE BankRollType = 2", null);
        cursor.moveToFirst();
        Integer precioFajillaMoneda = cursor.getInt(0);
        return precioFajillaMoneda;
    }

    public int getFormaPagoFPD(int tipopago){
        SQLiteDatabase base = getReadableDatabase();
        Cursor mCount= base.rawQuery("SELECT  count(*) FROM PagoTarjetaDiferentesFormasPago WHERE Cobrado = 1 AND FormaPagoIdDFP =  '" + tipopago +"'", null);
        mCount.moveToFirst();
        int count= mCount.getInt(0);
        mCount.close();
        return count;

    }

    public int getFormaPagoFPDCobrado(){
        SQLiteDatabase base = getReadableDatabase();
        Cursor mCount= base.rawQuery("SELECT  count(*) FROM PagoTarjetaDiferentesFormasPago WHERE Cobrado = 1", null);
        mCount.moveToFirst();
        int count= mCount.getInt(0);
        mCount.close();
        return count;

    }

    public String getPosicionCargaDFPCobrado(){
        SQLiteDatabase base = getReadableDatabase();
        Cursor cursor= base.rawQuery("SELECT  posicioncarga FROM PagoTarjetaDiferentesFormasPago WHERE Cobrado = 1", null);
        cursor.moveToFirst();
        String posicarga = cursor.getString(0);
        return posicarga;

    }

    public String getNumeroInternoPCDFP(){
        SQLiteDatabase base = getReadableDatabase();
        Cursor cursor= base.rawQuery("SELECT  numinternoposicioncarga FROM PagoTarjetaDiferentesFormasPago WHERE Cobrado = 1", null);
        cursor.moveToFirst();
        String posicarga = cursor.getString(0);
        return posicarga;

    }

    public String getNumeroInternoPCDFPError(){
        SQLiteDatabase base = getReadableDatabase();
        Cursor cursor= base.rawQuery("SELECT  numinternoposicioncarga FROM PagoTarjetaDiferentesFormasPago WHERE Cobrado <> 1", null);
        cursor.moveToFirst();
        String posicarga = cursor.getString(0);
        return posicarga;
    }





    public int getFormaPagoMixto(){
        SQLiteDatabase base = getReadableDatabase();
        Cursor mCount= base.rawQuery("SELECT  count(*) FROM PagoTarjetaDiferentesFormasPago", null);
        mCount.moveToFirst();
        int count= mCount.getInt(0);
        mCount.close();
        return count;

    }

    public Double getMontoTotalFPDgeneral(){
        SQLiteDatabase base = getReadableDatabase();
        Cursor cursor = base.rawQuery("SELECT montoTotal FROM PagoTarjetaDiferentesFormasPago", null);
        cursor.moveToFirst();
        Double montoparcial = cursor.getDouble(0);
        return montoparcial;

    }


    public Double getMontoTotalFPD(int tipopago){
        SQLiteDatabase base = getReadableDatabase();
        Cursor cursor = base.rawQuery("SELECT montoTotal FROM PagoTarjetaDiferentesFormasPago WHERE FormaPagoIdDFP =  '" + tipopago +"'", null);
        cursor.moveToFirst();
        Double montoparcial = cursor.getDouble(0);
        return montoparcial;

    }


    public Double getMontoFPD(int tipopago){
        SQLiteDatabase base = getReadableDatabase();
        Cursor cursor = base.rawQuery("SELECT MontoParcial FROM PagoTarjetaDiferentesFormasPago WHERE FormaPagoIdDFP =  '" + tipopago +"'", null);
        cursor.moveToFirst();
        Double montoparcial = cursor.getDouble(0);
        return montoparcial;

    }
    public String getresponseFPD(int tipopago){
        SQLiteDatabase base = getReadableDatabase();
        Cursor cursor = base.rawQuery("SELECT Response FROM  PagoTarjetaDiferentesFormasPago WHERE FormaPagoIdDFP =  '" + tipopago +"'", null);
        cursor.moveToFirst();
        String response = cursor.getString(0);
        return response;

    }

    public int getresponseFPDCount(long posicionCarga){
        SQLiteDatabase base = getReadableDatabase();
        Cursor cursor = base.rawQuery("SELECT count(*)  FROM  PagoTarjetaDiferentesFormasPago WHERE posicioncarga =  '" + posicionCarga +"'", null);
        cursor.moveToFirst();
        int count= cursor.getInt(0);
        cursor.close();
        return count;

    }

    public Integer getEstatusCobradoFPD(int tipopago){
        SQLiteDatabase base = getReadableDatabase();
        Cursor cursor = base.rawQuery("SELECT Cobrado FROM  PagoTarjetaDiferentesFormasPago WHERE FormaPagoIdDFP =  '" + tipopago +"'", null);
        cursor.moveToFirst();
        Integer cobrado = cursor.getInt(0);
        return cobrado;

    }

    public String getresponseFormaPago(){
        SQLiteDatabase base = getReadableDatabase();
        Cursor cursor = base.rawQuery("SELECT responsepagotarjeta FROM  PagoTarjeta WHERE Id =  '1'", null);
        cursor.moveToFirst();
        String response = cursor.getString(0);
        return response;

    }



    //  ----------------------------------------------------------------------------------------------------------------------------------------------------  //

    //<-------------------------------------------------------PARAMETROS DE LA TABLA RECEPCION FAJILLAS Y PICOS------------------------------------------------>

    //<-------------------------------------------------------PARAMETROS DE LA TABLA MAXIMO DINERO EN EFECTIVO------------------------------------------------>

    public static class MaximoEfectivoSucursal implements BaseColumns{
        public static final String nombreTabla = "MaximoEfectivoSucursal";
        public static final String maximoEfectivo = "MaximoEfectivo";
    }

    //<------------------------------------------------------------------CREACION DE TABLA EMPLEADO---------------------------------------------------------------------->

    private static final String TBL_MAXIMO_EFECTIVO_SUCURSAL = "CREATE TABLE " + MaximoEfectivoSucursal.nombreTabla+ "("+
            MaximoEfectivoSucursal._ID + " INTEGER PRIMARY KEY," +
            MaximoEfectivoSucursal.maximoEfectivo + " REAL)";

    //    <----------------------------------------------------------------------INSERT DE TABLA EMPLEADO------------------------------------------------------------------->

    public void InsertarMaximoEfectivo(double maximoEfectivo){
        SQLiteDatabase base = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MaximoEfectivoSucursal.maximoEfectivo, maximoEfectivo);

        long newRowId = base.insert(MaximoEfectivoSucursal.nombreTabla, null, values);

    }

    //<----------------------------------------------------------------------SELECTS DE TABLA MAXIMO EFECTIVO SUCURSAL -------------------------------------------------------->

    public Double getMaximoEfectivo(){
        SQLiteDatabase base = getReadableDatabase();
        Cursor cursor = base.rawQuery("SELECT MaximoEfectivo FROM MaximoEfectivoSucursal", null);
        cursor.moveToFirst();
        Double valorMaximoEfectivo = cursor.getDouble(0);
        return valorMaximoEfectivo;
    }


}


