package com.corpogas.corpoapp.Configuracion;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

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
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_CONFIGURACION_ESTACION);
        db.execSQL(SQL_DELETE_ENCABEZADO);
        db.execSQL(SQL_DELETE_DATOS_TARJETERO);
        db.execSQL(SQL_DELETE_ACTUALIZADOR_APP);
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
    public String getNombreEsatcion (){
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

    public void execSQL(String sqlDeleteConfiguracionEstacion) {
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
    }

    private static final String TBL_DATOS_TARJETERO =
            "CREATE TABLE " + DatosTarjetero.NOMBRE_TABLA_TARJETERO + " ("+
                    DatosTarjetero._ID + " TEXT PRIMARY KEY," +
                    DatosTarjetero.DIRECCION_MAC + " TEXT," +
                    //DatosTarjetero.PROPIEDAD_CONEXION + " TEXT, "  +
                    DatosTarjetero.LECTOR_HUELA + " TEXT, "  +
                    DatosTarjetero.ID_TARJETERO + " TEXT )";

    public static final String SQL_DELETE_DATOS_TARJETERO =
            "DROP TABLE IF EXISTS " + DatosTarjetero.NOMBRE_TABLA_TARJETERO;

    public void InsertarDatosNumeroTarjetero(String direccionmac, String lectorhuella, String idtarjetero){
        SQLiteDatabase base = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatosTarjetero.DIRECCION_MAC, direccionmac);
//        values.put(DatosTarjetero.PROPIEDAD_CONEXION, propiedadconexion);
        values.put(DatosTarjetero.LECTOR_HUELA, lectorhuella);
        values.put(DatosTarjetero.ID_TARJETERO, idtarjetero);

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
}


