package com.corpogas.corpoapp.Configuracion;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;


import com.corpogas.corpoapp.Entities.Cortes.CierreValePapel;
import com.corpogas.corpoapp.Entregas.Entities.PaperVoucherType;

import java.util.ArrayList;
import java.util.List;

public class CorteDB extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "CorteDelTurno.db";

    public CorteDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TBL_LECTURASAPI);
        db.execSQL(TBL_LECTURASMECANICAS);
        db.execSQL(TBL_INVENTARIOPRODUCTOS);
        db.execSQL(TBL_FAJILLAS);
        db.execSQL(TBL_PICOS);
        db.execSQL(TBL_FORMASPAGO);
        db.execSQL(TBL_TIPOVALEPAPELES);
        db.execSQL(TBL_CIERREVALEPAPEL);
        db.execSQL(TBL_PROCESO_CORTE) ;
        db.execSQL(TBL_USUARIO_RECIBE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

//<-------------------------------------------------------PARAMETROS DE LA TABLA------------------------------------------------>
    public static class DatosLecturas implements BaseColumns {
        public static final String nombreTabla = "LecturasApi";
        public static final String mangueraId = "MangueraId";
        public static final String cierreId = "CierreId";
        public static final String productoId = "ProductoId";
        public static final String valorInicial = "ValorInicial";
        public static final String valorFinal = "ValorFinal";
        public static final String valorInicalElectronica = "ValorInicalElectronica";
        public static final String valorFinalElectronica = "ValorFinalElectronica";
        public static final String diferenciaImporteDespacho = "DiferenciaImporteDespacho";
        public static final String diferenciaLecturasElectronicasMecanincas = "DiferenciaLecturasElectronicasMecanincas";
        public static final String diferenciaLecturasElectronicasDespachos = "DiferenciaLecturasElectronicasDespachos";
        public static final String numeroManguera = "NumeroManguera";
        public static final String shortDescription = "ShortDescription";
    }

    public static class DatosLecturasMecanicas implements BaseColumns{
        public static final String nombreTabla = "LecturasMecanicas";
        public static final String numeroManguera = "NumeroManguera";
        public static final String combustible = "Combustible";
        public static final String lecturaFinalMecanica = "LecturaFinalMecanica";
    }

    public static class DatosInventarioProductos implements BaseColumns{
        public static final String nombreTabla = "InventarioProductos";
        public static final String productoId = "ProductoId";
        public static final String precio = "Precio";
        public static final String cantidad = "Cantidad";
        public static final String codigoBarras = "CodigoBarras";
        public static final String productoDescripcionCorta = "ProductoDescripcionCorta";
        public static final String cantidadRecibida = "CantidadRecibida";
        public static final String cantidadEntregada = "CantidadEntregada";
        public static final String productoVendido = "ProductoVendido";
        public static final String id = "Id";
        public static final String existencias = "Existencias";
    }

    public static class DatosFajillas implements BaseColumns{
        public static final String nombreTabla = "Fajillas";
        public static final String fajillasBilletes = "FajillasBilletes";
        public static final String fajillasMonedas = "FajillasMonedas";
        public static final String tipoFajilla = "TipoFajilla";

    }

    public static class DatosPicos implements BaseColumns{

        public static final String nombreTabla = "Picos";
        public static final String cierreId = "CierreId";
        public static final String folioFinal = "FolioFinal";
        public static final String folioInicial = "FolioInicial";
        public static final String sucursalId = "SucursalId";
        public static final String picosMonedas = "PicosMonedas";
        public static final String tipoFajilla = "TipoFajilla";
        public static final String denominacion = "Denominacion";
        public static final String sumaBilletes = "sumaBilletes";

    }

    public static class DatosFormasPago implements BaseColumns{
        public static final String nombreTabla = "FormasPago";
        public static final String formaPagoId = "FormaPagoId";
        public static final String cantidad = "Cantidad";
        public static final String numeroTickets = "NumeroTickets";
        public static final String shortDescription = "ShortDescription";
        public static final String longDescription = "LongDescription";
    }

    public static class DatosTipoValesPapeles implements BaseColumns{
        public static final String nombreTabla = "TipoValesPapel";
        public static final String imagen = "Imagen";
        public static final String descripcion = "Descripcion";
        public static final String id = "Id";
    }

    public static class DatosCierreValePapel implements BaseColumns{
        public static final String nombreTabla = "CierreValePapel";
        public static final String sucursalId = "SucursalId";
        public static final String estacionId = "EstacionId";
        public static final String cierreId = "CierreId";
        public static final String tipoValePapelId = "TipoValePapelId";
        public static final String cantidad = "Cantidad";
        public static final String denominacion = "Denominacion";
        public static final String importe = "Importe";
        public static final String nombreVale = "NombreVale";
    }

    public static class DatosProcesoCorte implements BaseColumns{
        public static final String nombreTabla = "ProcesoCorte";
        public static final String procesoId = "ProcesoId";
        public static final String descripcion = "Descripcion";
        public static final String estatus = "Estatus";
        public static final String islaId = "IslaId";
    }

    public static class DatosUsuarioRecibe implements BaseColumns{
        public static final String nombreTabla = "UsuarioEntrega";
        public static final String password = "Password";
        public static final String numeroEmpleado = "NumeroEmpleado";
        public static final String nombreCompleto = "NombreCompleto";
        public static final String idRoll = "idRoll";
    }



//<------------------------------------------------------------------CREACION DE TABLA---------------------------------------------------------------------->

    private static final String TBL_LECTURASAPI = "CREATE TABLE " + DatosLecturas.nombreTabla+ "("+
            DatosLecturas._ID + " INTEGER PRIMARY KEY," +
            DatosLecturas.mangueraId + " INTEGER," +
            DatosLecturas.cierreId + " INTEGER," +
            DatosLecturas.productoId + " INTEGER," +
            DatosLecturas.valorInicial + " REAL," +
            DatosLecturas.valorFinal + " REAL," +
            DatosLecturas.valorInicalElectronica + " REAL," +
            DatosLecturas.valorFinalElectronica + " REAL," +
            DatosLecturas.diferenciaImporteDespacho + " REAL," +
            DatosLecturas.diferenciaLecturasElectronicasMecanincas + " REAL," +
            DatosLecturas.diferenciaLecturasElectronicasDespachos + " REAL," +
            DatosLecturas.numeroManguera + " INTEGER," +
            DatosLecturas.shortDescription + " TEXT)";

    public static final String SQL_DELETE_TBL_LECTURASAPI =
            "DROP TABLE IF EXISTS " + DatosLecturas.nombreTabla;

    private static final String TBL_LECTURASMECANICAS = "CREATE TABLE " + DatosLecturasMecanicas.nombreTabla+ "("+
            DatosLecturasMecanicas._ID + " INTEGER PRIMARY KEY," +
            DatosLecturasMecanicas.numeroManguera + " INTEGER," +
            DatosLecturasMecanicas.combustible + " TEXT," +
            DatosLecturasMecanicas.lecturaFinalMecanica + " REAL)";

    public static final String SQL_DELETE_TBL_LECTURASMECANICAS=
            "DROP TABLE IF EXISTS " + DatosLecturasMecanicas.nombreTabla;

    private static final String TBL_INVENTARIOPRODUCTOS = "CREATE TABLE " + DatosInventarioProductos.nombreTabla+ "("+
            DatosInventarioProductos._ID + " INTEGER PRIMARY KEY," +
            DatosInventarioProductos.productoId + " INTEGER," +
            DatosInventarioProductos.precio + " REAL," +
            DatosInventarioProductos.cantidad + " INTEGER," +
            DatosInventarioProductos.codigoBarras + " TEXT," +
            DatosInventarioProductos.productoDescripcionCorta + " TEXT," +
            DatosInventarioProductos.cantidadRecibida + " INTEGER," +
            DatosInventarioProductos.cantidadEntregada + " INTEGER," +
            DatosInventarioProductos.productoVendido + " INTEGER," +
            DatosInventarioProductos.id + " INTEGER," +
            DatosInventarioProductos.existencias + " INTEGER)";

    public static final String SQL_DELETE_TBL_INVENTARIOPRODUCTOS=
            "DROP TABLE IF EXISTS " + DatosInventarioProductos.nombreTabla;

    public  static final String TBL_FAJILLAS = "CREATE TABLE " + DatosFajillas.nombreTabla+ "("+
            DatosFajillas._ID + " INTEGER PRIMARY KEY," +
            DatosFajillas.fajillasBilletes + " INTEGER," +
            DatosFajillas.fajillasMonedas + " INTEGER," +
            DatosFajillas.tipoFajilla + " INTEGER)";

    public static final String SQL_DELETE_TBL_FAJILLAS =
            "DROP TABLE IF EXISTS" + DatosFajillas.nombreTabla;

    private static final String TBL_PICOS = "CREATE TABLE " + DatosPicos.nombreTabla+ "("+
            DatosPicos._ID + " INTEGER PRIMARY KEY," +
            DatosPicos.cierreId + " INTEGER," +
            DatosPicos.folioFinal + " INTEGER," +
            DatosPicos.folioInicial + " INTEGER," +
            DatosPicos.sucursalId + " INTEGER," +
            DatosPicos.picosMonedas + " REAL," +
            DatosPicos.tipoFajilla + " INTEGET," +
            DatosPicos.denominacion + " INTEGER," +
            DatosPicos.sumaBilletes + " INTEGER)";

    public static final String SQL_DELETE_TBL_PICOS=
            "DROP TABLE IF EXISTS " + DatosPicos.nombreTabla;

    private static final String TBL_FORMASPAGO = "CREATE TABLE " + DatosFormasPago.nombreTabla+ "("+
            DatosFormasPago._ID + " INTEGER PRIMARY KEY," +
            DatosFormasPago.formaPagoId + " INTEGER," +
            DatosFormasPago.cantidad + " REAL," +
            DatosFormasPago.numeroTickets + " INTEGER," +
            DatosFormasPago.shortDescription + " TEXT," +
            DatosFormasPago.longDescription + " INTEGER)";

    public static final String SQL_DELETE_TBL_FORMASPAGO=
            "DROP TABLE IF EXISTS " + DatosFormasPago.nombreTabla;

    private static final String TBL_TIPOVALEPAPELES = "CREATE TABLE " + DatosTipoValesPapeles.nombreTabla+ "("+
            DatosTipoValesPapeles._ID + " INTEGER PRIMARY KEY," +
            DatosTipoValesPapeles.imagen + " TEXT," +
            DatosTipoValesPapeles.descripcion + " TEXT," +
            DatosTipoValesPapeles.id + " INTEGER)";

    public static final String SQL_DELETE_TBL_TIPOVALEPAPELES =
            "DROP TABLE IF EXISTS " + DatosTipoValesPapeles.nombreTabla;

    private static final String TBL_CIERREVALEPAPEL = "CREATE TABLE " + DatosCierreValePapel.nombreTabla+ "("+
            DatosCierreValePapel._ID + " INTEGER PRIMARY KEY," +
            DatosCierreValePapel.sucursalId + " INTEGER," +
            DatosCierreValePapel.estacionId + " INTEGER," +
            DatosCierreValePapel.cierreId + " INTEGER," +
            DatosCierreValePapel.tipoValePapelId + " INTEGER," +
            DatosCierreValePapel.cantidad + " REAL," +
            DatosCierreValePapel.denominacion + " REAL," +
            DatosCierreValePapel.importe + " REAL," +
            DatosCierreValePapel.nombreVale + " TEXT)";

    public static final String SQL_DELETE_TBL_CIERREVALEPAPEL=
            "DROP TABLE IF EXISTS " + DatosCierreValePapel.nombreTabla;

    private static final String TBL_PROCESO_CORTE = "CREATE TABLE " + DatosProcesoCorte.nombreTabla+ "("+
            DatosProcesoCorte._ID + " INTEGER PRIMARY KEY," +
            DatosProcesoCorte.procesoId + " INTEGER," +
            DatosProcesoCorte.descripcion + " TEXT," +
            DatosProcesoCorte.estatus + " INTEGER," +
            DatosProcesoCorte.islaId + " INTEGER)";

    public static final String SQL_DELETE_TBL_PROCESO_CORTE=
            "DROP TABLE IF EXISTS " + DatosProcesoCorte.nombreTabla;

    private static final String TBL_USUARIO_RECIBE = "CREATE TABLE " + DatosUsuarioRecibe.nombreTabla+ "("+
            DatosUsuarioRecibe._ID + " INTEGER PRIMARY KEY," +
            DatosUsuarioRecibe.password + " TEXT," +
            DatosUsuarioRecibe.numeroEmpleado + " TEXT," +
            DatosUsuarioRecibe.nombreCompleto + " TEXT," +
            DatosUsuarioRecibe.idRoll + " TEXT)";

    public static final String SQL_DELETE_TBL_USUARIO_RECIBE=
            "DROP TABLE IF EXISTS " + DatosUsuarioRecibe.nombreTabla;

//    <----------------------------------------------------------------------INSERTS------------------------------------------------------------------->

    public void InsertarLecturasApi(int mangueraId,long cierreId,long productoId,double valorInicial,double valorFinal,double valorInicalElectronica,
                                    double valorFinalElectronica,double diferenciaImporteDespacho,double diferenciaLecturasElectronicasMecanincas,
                                    double diferenciaLecturasElectronicasDespachos,long numeroManguera,String shortDescription){
        SQLiteDatabase base = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatosLecturas.mangueraId, mangueraId);
        values.put(DatosLecturas.cierreId, cierreId);
        values.put(DatosLecturas.productoId, productoId);
        values.put(DatosLecturas.valorInicial, valorInicial);
        values.put(DatosLecturas.valorFinal, valorFinal);
        values.put(DatosLecturas.valorInicalElectronica, valorInicalElectronica);
        values.put(DatosLecturas.valorFinalElectronica, valorFinalElectronica);
        values.put(DatosLecturas.diferenciaImporteDespacho, diferenciaImporteDespacho);
        values.put(DatosLecturas.diferenciaLecturasElectronicasMecanincas, diferenciaLecturasElectronicasMecanincas);
        values.put(DatosLecturas.diferenciaLecturasElectronicasDespachos, diferenciaLecturasElectronicasDespachos);
        values.put(DatosLecturas.numeroManguera, numeroManguera);
        values.put(DatosLecturas.shortDescription, shortDescription);

        long newRowId = base.insert(DatosLecturas.nombreTabla,null,values);
    }

    public void InsertarLecturasMecanicas(long numeroManguera,String combustible,double lecturaFinalMecanica){
        SQLiteDatabase base = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatosLecturasMecanicas.numeroManguera, numeroManguera);
        values.put(DatosLecturasMecanicas.combustible, combustible);
        values.put(DatosLecturasMecanicas.lecturaFinalMecanica, lecturaFinalMecanica);

        long newRowId = base.insert(DatosLecturasMecanicas.nombreTabla,null,values);
    }

    public void InsertarInventarioProductos(long productoId,double precio,long cantidad,String codigoBarras,String productoDescripcionCorta,
                                            long cantidadRecibida,long cantidadEntregada,long productoVendido,long id,long existencias){
        SQLiteDatabase base = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatosInventarioProductos.productoId, productoId);
        values.put(DatosInventarioProductos.precio, precio);
        values.put(DatosInventarioProductos.cantidad, cantidad);
        values.put(DatosInventarioProductos.codigoBarras, codigoBarras);
        values.put(DatosInventarioProductos.productoDescripcionCorta, productoDescripcionCorta);
        values.put(DatosInventarioProductos.cantidadRecibida, cantidadRecibida);
        values.put(DatosInventarioProductos.cantidadEntregada, cantidadEntregada);
        values.put(DatosInventarioProductos.productoVendido, productoVendido);
        values.put(DatosInventarioProductos.id, id);
        values.put(DatosInventarioProductos.existencias, existencias);

        long newRowId = base.insert(DatosInventarioProductos.nombreTabla,null,values);
    }

    public void InsertarFajillas(int fajillasBilletes, int fajillasMonedas, long tipoFajilla){
        SQLiteDatabase base = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatosFajillas.fajillasBilletes, fajillasBilletes);
        values.put(DatosFajillas.fajillasMonedas, fajillasMonedas);
        values.put(DatosFajillas.tipoFajilla, tipoFajilla);

        long newRowId = base.insert(DatosFajillas.nombreTabla,null,values);
    }

    public void InsertarPicos(long cierreId, long folioFinal, long folioInicial, long sucursalId, double picosMonedas, long tipoFajilla, int denominacion, int sumaBilletes){
        SQLiteDatabase base = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatosPicos.cierreId, cierreId);
        values.put(DatosPicos.folioFinal, folioFinal);
        values.put(DatosPicos.folioInicial, folioInicial);
        values.put(DatosPicos.sucursalId, sucursalId);
        values.put(DatosPicos.picosMonedas, picosMonedas);
        values.put(DatosPicos.tipoFajilla, tipoFajilla);
        values.put(DatosPicos.denominacion, denominacion);
        values.put(DatosPicos.sumaBilletes, sumaBilletes);

        long newRowId = base.insert(DatosPicos.nombreTabla,null,values);
    }

    public void InsertarFormasPago(long formaPagoId,double cantidad,long numeroTickets,String shortDescription,String longDescription){
        SQLiteDatabase base = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatosFormasPago.formaPagoId, formaPagoId);
        values.put(DatosFormasPago.cantidad, cantidad);
        values.put(DatosFormasPago.numeroTickets, numeroTickets);
        values.put(DatosFormasPago.shortDescription, shortDescription);
        values.put(DatosFormasPago.longDescription, longDescription);

        long newRowId = base.insert(DatosFormasPago.nombreTabla,null,values);
    }

    public void InsertarTipoValePapeles(int imagen,String descripcion,long id){
        SQLiteDatabase base = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatosTipoValesPapeles.imagen,imagen);
        values.put(DatosTipoValesPapeles.descripcion, descripcion);
        values.put(DatosTipoValesPapeles.id, id);

        long newRowId = base.insert(DatosTipoValesPapeles.nombreTabla,null,values);
    }

    public void InsertarCierreValePapel(long sucursalId,long estacionId,long cierreId,long tipoValePapelId,double cantidad,double denominacion,
                                        double importe, String nombreVale){
        SQLiteDatabase base = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatosCierreValePapel.sucursalId, sucursalId);
        values.put(DatosCierreValePapel.estacionId, estacionId);
        values.put(DatosCierreValePapel.cierreId, cierreId);
        values.put(DatosCierreValePapel.tipoValePapelId, tipoValePapelId);
        values.put(DatosCierreValePapel.cantidad, cantidad);
        values.put(DatosCierreValePapel.denominacion, denominacion);
        values.put(DatosCierreValePapel.importe, importe);
        values.put(DatosCierreValePapel.nombreVale, nombreVale);

        long newRowId = base.insert(DatosCierreValePapel.nombreTabla,null,values);
    }


    public void InsertarProcesoCorte(int procesoId,String descripcion,int estatus, long islaId){
        SQLiteDatabase base = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatosProcesoCorte.procesoId, procesoId);
        values.put(DatosProcesoCorte.descripcion, descripcion);
        values.put(DatosProcesoCorte.estatus, estatus);
        values.put(DatosProcesoCorte.islaId, islaId);

        long newRowId = base.insert(DatosProcesoCorte.nombreTabla,null,values);
    }



    public void InsertarUsuarioRecibe(String password,String numeroEmpleado,String nombreCompleto,String idRoll){
        SQLiteDatabase base = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatosUsuarioRecibe.password, password);
        values.put(DatosUsuarioRecibe.numeroEmpleado, numeroEmpleado);
        values.put(DatosUsuarioRecibe.nombreCompleto, nombreCompleto);
        values.put(DatosUsuarioRecibe.idRoll, idRoll);

        long newRowId = base.insert(DatosUsuarioRecibe.nombreTabla,null,values);
    }

    //<----------------------------------------------------------------------SELECTS -------------------------------------------------------->

//    public List<LecturasApi> getLecturasApi(){
//        SQLiteDatabase base = getReadableDatabase();
//        Cursor cursor = base.rawQuery("SELECT * FROM LecturasApi", null);
//        List<LecturasApi> lLecturasApi = new ArrayList<>();
//        if(cursor.moveToFirst()) {
//            do {
//                lLecturasApi.add(new LecturasApi(cursor.getInt(1), cursor.getLong(2), cursor.getLong(3),
//                        cursor.getDouble(4), cursor.getDouble(5), cursor.getDouble(6), cursor.getDouble(7),
//                        cursor.getDouble(8), cursor.getDouble(9), cursor.getDouble(10), cursor.getLong(11),
//                        cursor.getString(12)));
//            }while (cursor.moveToNext());
//        }
//
//        return lLecturasApi;
//    }
//
//    public List<LecturasMecanicasCorte> getLecturasMecanicasCorte()
//    {
//        SQLiteDatabase base = getReadableDatabase();
//        Cursor cursor = base.rawQuery("SELECT * FROM LecturasMecanicas", null);
//        List<LecturasMecanicasCorte> lLecturasMecanicasCorte = new ArrayList<>();
//        if(cursor.moveToFirst()){
//            do{
//                lLecturasMecanicasCorte.add(new LecturasMecanicasCorte(cursor.getLong(1),cursor.getString(2),
//                        cursor.getDouble(3)));
//            }while (cursor.moveToNext());
//        }
//
//        return lLecturasMecanicasCorte;
//
//    }
//
//    public List<InventarioProductos> getInventarioProductos(){
//        SQLiteDatabase base = getReadableDatabase();
//        Cursor cursor = base.rawQuery("SELECT * FROM InventarioProductos", null);
//
//        List<InventarioProductos> lInventarioProductos = new ArrayList<>();
//        if(cursor.moveToFirst()){
//            do{
//                lInventarioProductos.add(new InventarioProductos(cursor.getLong(1),cursor.getDouble(2),
//                        cursor.getLong(3),cursor.getString(4),cursor.getString(5),cursor.getLong(6),
//                        cursor.getLong(7),cursor.getLong(8),cursor.getLong(9),cursor.getLong(10)));
//            }while (cursor.moveToNext());
//        }
//
//        return lInventarioProductos;
//    }
//
//    public List<CierreFajilla> getFajillas(){
//        SQLiteDatabase base = getReadableDatabase();
//        Cursor cursor = base.rawQuery("SELECT * FROM Fajillas", null);
//        List<CierreFajilla> lFajillas = new ArrayList<>();
//        if (cursor.moveToFirst()){
//            do{
//                lFajillas.add(new CierreFajilla(cursor.getInt(1), cursor.getInt(2), cursor.getLong(3)));
//            }while (cursor.moveToNext());
//        }
//        return lFajillas;
//    }
//
//    public List<CierreFajilla> getPicos(){
//        SQLiteDatabase base = getReadableDatabase();
//        Cursor cursor = base.rawQuery("SELECT * FROM Picos", null);
//        List<CierreFajilla> lCierreFajilla = new ArrayList<>();
//        if(cursor.moveToFirst()){
//            do {
//                lCierreFajilla.add(new CierreFajilla(cursor.getLong(1),cursor.getInt(2),cursor.getInt(3),cursor.getLong(4),cursor.getDouble(5),cursor.getLong(6),cursor.getDouble(7), cursor.getInt(8)));
//            }while (cursor.moveToNext());
//        }
//
//        return lCierreFajilla;
//    }
//
//    public List<FormasPago> getFormasPago(){
//        SQLiteDatabase base = getReadableDatabase();
//        Cursor cursor = base.rawQuery("SELECT * FROM FormasPago", null);
//        List<FormasPago> lFormaPago = new ArrayList<>();
//        if(cursor.moveToFirst()){
//            do {
//                lFormaPago.add(new FormasPago(cursor.getLong(1),cursor.getDouble(2),cursor.getLong(3),
//                        cursor.getString(4),cursor.getString(5)));
//            }while (cursor.moveToNext());
//        }
//
//        return lFormaPago;
//    }

    public List<PaperVoucherType> getTipoValePapeles(){
        SQLiteDatabase base = getReadableDatabase();
        Cursor cursor = base.rawQuery("SELECT * FROM TipoValesPapel", null);
        List<PaperVoucherType> lTipoValePapeles = new ArrayList<>();
        if(cursor.moveToFirst()){
            do {
                lTipoValePapeles.add(new PaperVoucherType(cursor.getInt(1), cursor.getString(2),cursor.getLong(3)));
            }while (cursor.moveToNext());
        }

        return lTipoValePapeles;
    }

    public List<CierreValePapel> getAllCierreValePapel(){
        SQLiteDatabase base = getReadableDatabase();
        Cursor cursor = base.rawQuery("SELECT * FROM CierreValePapel", null);
        List<CierreValePapel> lCierreValePapel = new ArrayList<>();
        if(cursor.moveToFirst()){
            do {
                lCierreValePapel.add(new CierreValePapel(cursor.getLong(1),cursor.getLong(2),cursor.getLong(3),cursor.getLong(4),
                        cursor.getDouble(5),cursor.getDouble(6),cursor.getDouble(7),cursor.getString(8)));
            }while (cursor.moveToNext());
        }

        return lCierreValePapel;
    }
//
//
    public List<CierreValePapel> getIdValePapel(long tipoValePapelId){
        SQLiteDatabase base = getReadableDatabase();
        Cursor cursor = base.rawQuery("SELECT * FROM CierreValePapel WHERE tipoValePapelId = "+tipoValePapelId+"", null);
        List<CierreValePapel> lCierreValePapel = new ArrayList<>();
        if(cursor.moveToFirst()){
            do {
                lCierreValePapel.add(new CierreValePapel(cursor.getLong(1),cursor.getLong(2),cursor.getLong(3),cursor.getLong(4),
                        cursor.getDouble(5),cursor.getDouble(6),cursor.getDouble(7),cursor.getString(8)));
            }while (cursor.moveToNext());
        }

        return lCierreValePapel;
    }
//
//    public List<ProcesoCorteMenu> getProcesoCorte(){
//
//        SQLiteDatabase base = getReadableDatabase();
//        Cursor cursor = base.rawQuery("SELECT * FROM ProcesoCorte", null);
//        List<ProcesoCorteMenu> lProcesoCorte = new ArrayList<>();
//        if(cursor.moveToFirst()){
//            do {
//                lProcesoCorte.add(new ProcesoCorteMenu(cursor.getInt(1),cursor.getString(2),cursor.getInt(3),cursor.getLong(4)));
//            }while (cursor.moveToNext());
//        }
//
//        return lProcesoCorte;
//    }
//
//    public List<UsuarioRecibe> getUsuarioRecibe(){
//
//        SQLiteDatabase base = getReadableDatabase();
//        Cursor cursor = base.rawQuery("SELECT * FROM UsuarioEntrega", null);
//        List<UsuarioRecibe> lUsuarioRecibe = new ArrayList<>();
//        if(cursor.moveToFirst()){
//            do {
//                lUsuarioRecibe.add(new UsuarioRecibe(cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4)));
//            }while (cursor.moveToNext());
//        }
//
//        return lUsuarioRecibe;
//    }


//    public static final String nombreTabla = "UsuarioEntrega";
//    public static final String password = "Password";
//    public static final String numeroEmpleado = "NumeroEmpleado";
//    public static final String nombreCompleto = "NombreCompleto";
//    public static final String idRoll = "idRoll";





    //<----------------------------------------------------------------------UPDATES-------------------------------------------------------->

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

    public boolean  updatePicos(long folioFinal,int sumaBilletes,double denominacion,int tipoFajilla)
    {
        SQLiteDatabase base = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatosPicos.folioFinal,folioFinal);
        contentValues.put(DatosPicos.sumaBilletes,sumaBilletes);
        int datos = base.update(DatosPicos.nombreTabla, contentValues, DatosPicos.denominacion + " = ? "+
                        "and "+ DatosPicos.tipoFajilla+ " = ? ",
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

    public boolean updateInventarioProductos(long productoId, long cantidadEntregada,long productoVendido){
        SQLiteDatabase base = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatosInventarioProductos.cantidadEntregada,cantidadEntregada);
        contentValues.put(DatosInventarioProductos.productoVendido,productoVendido);
        int datos = base.update(DatosInventarioProductos.nombreTabla, contentValues, DatosInventarioProductos.productoId + " = ? " ,
                new String[]{String.valueOf(productoId)});
        if(datos!=0)
        {
            return  true;
        }else{
            return false;
        }
    }

    public boolean updateCierreValePapel(long tipoValePapelId,double denominacion, double cantidad,double importe){
        SQLiteDatabase base = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatosCierreValePapel.cantidad,cantidad);
        contentValues.put(DatosCierreValePapel.importe,importe);
        int datos = base.update(DatosCierreValePapel.nombreTabla, contentValues, DatosCierreValePapel.tipoValePapelId + " = ? "+
                        "and "+ DatosCierreValePapel.denominacion+ " = ? ",
                new String[]{String.valueOf(tipoValePapelId), String.valueOf(denominacion)});
        if(datos!=0)
        {
            return  true;
        }else{
            return false;
        }
    }

    public boolean updateProcesoCorte(int procesoId, int estatus)
    {
        SQLiteDatabase base = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatosProcesoCorte.estatus,estatus);
        int datos = base.update(DatosProcesoCorte.nombreTabla, contentValues, DatosProcesoCorte.procesoId + " = ? " ,
                new String[]{String.valueOf(procesoId)});
        if(datos!=0)
        {
            return  true;
        }else{
            return false;
        }
    }

    //    <----------------------------------------------------------------------METODOS ADICIONALES-------------------------------------------------------->
    public boolean existeDBCorte(String Database_path) {
        SQLiteDatabase checkDB = null;
        try {
            checkDB = SQLiteDatabase.openDatabase(Database_path, null, SQLiteDatabase.OPEN_READONLY);
            checkDB.close();
        } catch (SQLiteException e) {
            Log.e("Error", "No existe la base de datos ");
        }
        return checkDB != null;
    }

}
