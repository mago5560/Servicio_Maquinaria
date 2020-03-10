package com.pantaleon.serviciomaquinaria.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.pantaleon.serviciomaquinaria.Class.Login;
import com.pantaleon.serviciomaquinaria.Class.Piloto;
import com.pantaleon.serviciomaquinaria.Class.RevisionTecleo;
import com.pantaleon.serviciomaquinaria.Class.Servicio;
import com.pantaleon.serviciomaquinaria.Class.ServicioDetalle;
import com.pantaleon.serviciomaquinaria.Customs.Functions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DbHandler extends SQLiteOpenHelper {

    Functions util =  new Functions();
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "serviciomaquina.db";
    private String query;
    private SQLiteDatabase db;

    private  static final String TBL_USUARIO = "tblusuario";
    private static final String TBL_SERVICIO = "tblservicio";
    private static final String TBL_SERVICIO_DETALLE = "tblserviciodetalle";
    private  static final String TBL_VEHICULO = "tblvehiculo";
    private  static final String TBL_PILOTO = "tblpiloto";

    private static final String TBL_REVISION = "tblrevision";
    private static final String TBL_REVISION_DETALLE = "tblrevisiondetalle";


    public DbHandler(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DB_NAME, factory, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        createMaestros(sqLiteDatabase);
        createTable(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // <editor-fold defaultstate="collapsed" desc="(colocar las tablas modificadas o las nuevas tablas a utilizar en el sistema luego de la implementacion de DBHandler)">
        newCreateTable(db);
        // </editor-fold>
    }

    private void createMaestros(SQLiteDatabase db) {
        db.execSQL(" DROP TABLE IF EXISTS " + TBL_SERVICIO);
        db.execSQL(" DROP TABLE IF EXISTS " + TBL_SERVICIO_DETALLE);
        db.execSQL(" DROP TABLE IF EXISTS "+ TBL_USUARIO);
        db.execSQL(" DROP TABLE IF EXISTS "+ TBL_VEHICULO);
        db.execSQL(" DROP TABLE IF EXISTS "+ TBL_PILOTO);

        // <editor-fold defaultstate="collapsed" desc="(Servicio)">
        query = "";
        query = " CREATE TABLE " + TBL_SERVICIO + " ( ";
        query += " Id INTEGER PRIMARY KEY AUTOINCREMENT ";
        query += " ,IdServicio TEXT ";
        query += " ,Descripcion TEXT ";
        query += " ) ";
        db.execSQL(query);
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="(Servicio Detalle)">
        query = "";
        query = " CREATE TABLE " + TBL_SERVICIO_DETALLE + " ( ";
        query += " Id INTEGER PRIMARY KEY AUTOINCREMENT ";
        query += " ,IdServicio TEXT ";
        query += " ,CodRevision TEXT ";
        query += " ,DescripiconRevision TEXT ";
        query += " ,PunteoSistema TEXT ";
        query += " ,PunteoReal TEXT ";
        query += " ,Ponderacion TEXT ";
        query += " ) ";
        db.execSQL(query);
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="(Usuario)">
        query = "";
        query = " CREATE TABLE " + TBL_USUARIO + " ( ";
        query += " Id INTEGER PRIMARY KEY AUTOINCREMENT ";
        query += " ,Usuario TEXT ";
        query += " ,Contraseña TEXT ";
        query += " ) ";
        db.execSQL(query);
        // </editor-fold>


        // <editor-fold defaultstate="collapsed" desc="(Vehiuclo)">
        query = "";
        query = " CREATE TABLE " + TBL_VEHICULO + " ( ";
        query += " Id INTEGER PRIMARY KEY AUTOINCREMENT ";
        query += " ,NoPlaca TEXT ";
        query += " ,CodProveedor TEXT ";
        query += " ,NombreProveedor TEXT ";
        query += " ,Vehiculo TEXT ";
        query += " ,Propietario TEXT ";
        query += " ,Modelo TEXT ";
        query += " ,TC TEXT ";
        query += " ,CodPiloto TEXT ";
        query += " ,NombrePiloto TEXT ";
        query += " ,TipoEquipo TEXT ";
        query += " ,DescripcionTipoEquipo TEXT ";
        query += " ,EdadPiloto TEXT ";
        query += " ,LicenciaPiloto TEXT ";
        query += " ,FechaVenceLicencia TEXT ";
        query += " ) ";
        db.execSQL(query);
        // </editor-fold>


        // <editor-fold defaultstate="collapsed" desc="(Piloto)">
        query = "";
        query = " CREATE TABLE " + TBL_PILOTO + " ( ";
        query += " Id INTEGER PRIMARY KEY AUTOINCREMENT ";
        query += " ,NombrePiloto TEXT ";
        query += " ,EdadPiloto TEXT ";
        query += " ,LicenciaPiloto TEXT ";
        query += " ,FechaVenceLicencia TEXT ";
        query += " ,CodPiloto TEXT ";
        query += " ) ";
        db.execSQL(query);
        // </editor-fold>

    }

    private void createTable(SQLiteDatabase db) {
        db.execSQL(" DROP TABLE IF EXISTS " + TBL_REVISION);
        db.execSQL(" DROP TABLE IF EXISTS " + TBL_REVISION_DETALLE);

        // <editor-fold defaultstate="collapsed" desc="(RevisionConsulta)">
        query = "";
        query = " CREATE TABLE " + TBL_REVISION + " ( ";
        query += " Id INTEGER PRIMARY KEY AUTOINCREMENT ";
        query += " ,IdVehiculo TEXT ";
        query += " ,NombrePiloto TEXT ";
        query += " ,EdadPiloto TEXT ";
        query += " ,LicenciaPiloto TEXT ";
        query += " ,FechaVenceLicencia TEXT ";
        query += " ,CodPiloto TEXT ";
        query += " ,Fecha TEXT ";
        query += " ,Placa TEXT ";
        query += " ,TC TEXT ";
        query += " ,IdentificadorDB TEXT ";
        query += " ,Transferido INTEGER DEFAULT 0 ";
        query += " ) ";
        db.execSQL(query);
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="(RevisionConsulta Detalle)">
        query = "";
        query = " CREATE TABLE " + TBL_REVISION_DETALLE + " ( ";
        query += " Id INTEGER PRIMARY KEY AUTOINCREMENT ";
        query += " ,IdRevision INTEGER "; //Encabezado
        query += " ,IdServicioDetalle INTEGER"; //Local
        query += " ,IdServicio TEXT "; //Ref WebServer
        query += " ,CodRevision TEXT "; //Ref WebServer
        query += " ,PunteoReal TEXT ";
        query += " ,Ponderacion TEXT ";
        query += " ,Observaciones TEXT ";
        query += " ) ";
        db.execSQL(query);
        // </editor-fold>

    }

    private void newCreateTable(SQLiteDatabase db) {
    }


    // <editor-fold defaultstate="collapsed" desc="Insert">

    public Integer insertVehiculo(Piloto cls) {
        long id = 0;
        ContentValues contentValues = new ContentValues();
        contentValues.put("NoPlaca", cls.getPlaca());
        contentValues.put("CodProveedor", cls.getCodProveedor());
        contentValues.put("NombreProveedor", cls.getNombreProveedor());
        contentValues.put("Vehiculo", cls.getVehiculo());
        contentValues.put("Propietario", cls.getPropietario());
        contentValues.put("TC", cls.getTC());
        contentValues.put("CodPiloto", cls.getCodPiloto());
        contentValues.put("NombrePiloto", cls.getNombrePiloto());
        contentValues.put("TipoEquipo", cls.getTipoEquipo());
        contentValues.put("DescripcionTipoEquipo", cls.getDescripcionTipoEquipo());
        contentValues.put("EdadPiloto", cls.getEdadPiloto());
        contentValues.put("LicenciaPiloto", cls.getLicenciaPiloto());
        contentValues.put("FechaVenceLicencia", cls.getFechaVenceLicencia());

        db = getWritableDatabase();
        id = db.insert(TBL_VEHICULO, null, contentValues);
        db.close();
        return (int) id;
    }

    public Integer insertPiloto(Piloto cls) {
        long id = 0;
        ContentValues contentValues = new ContentValues();
        contentValues.put("NombrePiloto", cls.getNombrePiloto());
        contentValues.put("EdadPiloto", cls.getEdadPiloto());
        contentValues.put("LicenciaPiloto", cls.getLicenciaPiloto());
        contentValues.put("FechaVenceLicencia", cls.getFechaVenceLicencia());
        contentValues.put("CodPiloto", cls.getCodPiloto());
        db = getWritableDatabase();
        id = db.insert(TBL_PILOTO, null, contentValues);
        db.close();
        return (int) id;
    }


    public Integer insertUsuario(Login cls) {
        long id = 0;
        ContentValues contentValues = new ContentValues();
        contentValues.put("Usuario", cls.getUsuario());
        contentValues.put("Contraseña", cls.getContraseña());
        db = getWritableDatabase();
        id = db.insert(TBL_USUARIO, null, contentValues);
        db.close();
        return (int) id;
    }


    public Integer insertServicio(Servicio cls) {
        long id = 0;
        ContentValues contentValues = new ContentValues();
        contentValues.put("IdServicio", cls.getIdServicio());
        contentValues.put("Descripcion", cls.getDescripcion());
        db = getWritableDatabase();
        id = db.insert(TBL_SERVICIO, null, contentValues);
        db.close();
        return (int) id;
    }

    public Integer insertServicioDetalle(ServicioDetalle cls) {
        long id = 0;
        ContentValues contentValues = new ContentValues();
        contentValues.put("IdServicio", cls.getIdServicio());
        contentValues.put("CodRevision", cls.getCodRevision());
        contentValues.put("DescripiconRevision", cls.getDescripiconRevision());
        contentValues.put("PunteoSistema", cls.getPunteoSistema());
        contentValues.put("PunteoReal", cls.getPunteoReal());
        contentValues.put("Ponderacion", cls.getPonderacion());
        db = getWritableDatabase();
        id = db.insert(TBL_SERVICIO_DETALLE, null, contentValues);
        db.close();
        return (int) id;
    }

    public Integer insertRevision(RevisionTecleo cls) {
        long id = 0;
        ContentValues contentValues = new ContentValues();
        contentValues.put("IdVehiculo", cls.getIdVehiculo());
        contentValues.put("NombrePiloto", cls.getNombrePiloto());
        contentValues.put("EdadPiloto", cls.getEdadPiloto());
        contentValues.put("LicenciaPiloto", cls.getLicenciaPiloto());
        contentValues.put("FechaVenceLicencia", cls.getFechaVenceLicencia());
        contentValues.put("CodPiloto", cls.getCodPiloto());
        contentValues.put("Fecha", cls.getFecha());
        contentValues.put("TC", cls.getTC());
        contentValues.put("Placa", cls.getPlaca());
        db = getWritableDatabase();
        id = db.insert(TBL_REVISION, null, contentValues);
        db.close();
        return (int) id;
    }

    public Integer insertRevisionDetalle(ServicioDetalle cls) {
        long id = 0;
        ContentValues contentValues = new ContentValues();
        contentValues.put("IdRevision", cls.getIdRevision());
        contentValues.put("IdServicioDetalle", cls.getId());
        contentValues.put("IdServicio", cls.getIdServicio());
        contentValues.put("CodRevision", cls.getCodRevision());
        contentValues.put("PunteoReal", cls.getPunteoReal());
        contentValues.put("Ponderacion", cls.getPonderacion());
        contentValues.put("Observaciones", cls.getObservaciones());
        db = getWritableDatabase();
        id = db.insert(TBL_REVISION_DETALLE, null, contentValues);
        db.close();
        return (int) id;
    }

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Update">

    public void updateRevisionTransferido(String Transferido, String Id,String IdentificadorDB) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("Transferido", Transferido);
        contentValues.put("IdentificadorDB", IdentificadorDB);
        db = getWritableDatabase();
        db.update(TBL_REVISION, contentValues, "Id=" + Id, null);
        db.close();
    }

    public void updateRevisionDetalle(@NonNull ServicioDetalle cls){
        ContentValues contentValues = new ContentValues();
        contentValues.put("PunteoReal", cls.getPunteoReal());
        contentValues.put("Ponderacion", cls.getPonderacion());
        contentValues.put("Observaciones", cls.getObservaciones());
        db = getWritableDatabase();
        db.update(TBL_REVISION_DETALLE, contentValues,"Id=" + cls.getIdRevisionDetalle(), null);
        db.close();
    }

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Delete">

    public void deleteVehiculo(){
        db = getWritableDatabase();
        db.delete(TBL_VEHICULO,null,null);
        db.close();
    }

    public void deletePiloto(){
        db = getWritableDatabase();
        db.delete(TBL_PILOTO,null,null);
        db.close();
    }


    public void deleteUsuario(){
        db = getWritableDatabase();
        db.delete(TBL_USUARIO,null,null);
        db.close();
    }

    public void deleteServicio(){
        db = getWritableDatabase();
        db.delete(TBL_SERVICIO,null,null);
        db.delete(TBL_SERVICIO_DETALLE,null,null);
        db.close();
    }

    public void deleteRevision(String Id){
        db = getWritableDatabase();
        db.delete(TBL_REVISION, "Id = " + Id, null);
        db.delete(TBL_REVISION_DETALLE, "IdRevision = " + Id, null);
        db.close();
    }

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Select">

    public ArrayList selectVehiculo(String Vehiculo){
        ArrayList<Piloto> arrayList = new ArrayList<>();
        db = getWritableDatabase();
        query = "";
        query += " SELECT Id ";
        query += " ,NoPlaca  ";
        query += " ,CodProveedor  ";
        query += " ,NombreProveedor  ";
        query += " ,Vehiculo  ";
        query += " ,Propietario  ";
        query += " ,Modelo  ";
        query += " ,TC  ";
        query += " ,CodPiloto  ";
        query += " ,NombrePiloto  ";
        query += " ,TipoEquipo  ";
        query += " ,DescripcionTipoEquipo  ";
        query += " ,EdadPiloto  ";
        query += " ,LicenciaPiloto  ";
        query += " ,FechaVenceLicencia  ";
        query += " FROM " + TBL_VEHICULO;
        query += " WHERE 1= 1 ";

        if (!Vehiculo.isEmpty()){
            query += " AND Vehiculo =  '"+ Vehiculo  +"'"  ;
        }
        query += " ORDER BY Id DESC ";

        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        while (!c.isAfterLast()) {

            Piloto cls = new Piloto();
            cls.setId(c.getInt(c.getColumnIndex("Id")));

            cls.setPlaca(c.getString(c.getColumnIndex("NoPlaca")));
            cls.setCodProveedor(c.getString(c.getColumnIndex("CodProveedor")));
            cls.setNombreProveedor(c.getString(c.getColumnIndex("NombreProveedor")));
            cls.setVehiculo(c.getString(c.getColumnIndex("Vehiculo")));
            cls.setPropietario(c.getString(c.getColumnIndex("Propietario")));
            cls.setModelo(c.getString(c.getColumnIndex("Modelo")));
            cls.setTC(c.getString(c.getColumnIndex("TC")));
            cls.setCodPiloto(c.getString(c.getColumnIndex("CodPiloto")));
            cls.setNombrePiloto(c.getString(c.getColumnIndex("NombrePiloto")));
            cls.setTipoEquipo(c.getString(c.getColumnIndex("TipoEquipo")));
            cls.setDescripcionTipoEquipo(c.getString(c.getColumnIndex("DescripcionTipoEquipo")));
            cls.setEdadPiloto(c.getString(c.getColumnIndex("EdadPiloto")));
            cls.setLicenciaPiloto(c.getString(c.getColumnIndex("LicenciaPiloto")));
            cls.setFechaVenceLicencia(c.getString(c.getColumnIndex("FechaVenceLicencia")));

            arrayList.add(cls);
            c.moveToNext();
        }
        db.close();
        return arrayList;
    }

    public ArrayList selectPiloto(String Piloto){
        ArrayList<Piloto> arrayList = new ArrayList<>();
        db = getWritableDatabase();
        query = "";
        query += " SELECT Id ";
        query += " ,NombrePiloto  ";
        query += " ,EdadPiloto  ";
        query += " ,LicenciaPiloto  ";
        query += " ,FechaVenceLicencia  ";
        query += " ,CodPiloto  ";

        query += " FROM " + TBL_PILOTO;
        query += " WHERE 1= 1 ";

        if (!Piloto.isEmpty()){
            query += " AND CodPiloto =  '"+ Piloto  +"'"  ;
        }
        query += " ORDER BY Id DESC ";

        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            Piloto cls = new Piloto();
            cls.setId(c.getInt(c.getColumnIndex("Id")));
            cls.setCodPiloto(c.getString(c.getColumnIndex("CodPiloto")));
            cls.setNombrePiloto(c.getString(c.getColumnIndex("NombrePiloto")));
            cls.setEdadPiloto(c.getString(c.getColumnIndex("EdadPiloto")));
            cls.setLicenciaPiloto(c.getString(c.getColumnIndex("LicenciaPiloto")));
            cls.setFechaVenceLicencia(c.getString(c.getColumnIndex("FechaVenceLicencia")));
            arrayList.add(cls);
            c.moveToNext();
        }
        db.close();
        return arrayList;
    }

    public ArrayList selectServicio(){
        ArrayList<Servicio> arrayList = new ArrayList<>();
        db = getWritableDatabase();
        query = "";

        query += " SELECT a.Id ";
        query += " ,a.IdServicio  ";
        query += " ,a.Descripcion  ";
        query += " ,a.Descripcion  ";
        query += " ,b.TotalRegistros  ";
        query += " FROM " + TBL_SERVICIO + " a";
        query += " INNER JOIN  ( " ;
        query += " SELECT IdServicio, COUNT(1) AS TotalRegistros  FROM "+ TBL_SERVICIO_DETALLE ;
        query += " GROUP BY IdServicio ";
        query += " ) b ON a.IdServicio = b.IdServicio ";
        query += " ORDER BY Id DESC ";

        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        while (!c.isAfterLast()) {

            Servicio cls = new Servicio();
            cls.setId(c.getInt(c.getColumnIndex("Id")));
            cls.setIdServicio(c.getString(c.getColumnIndex("IdServicio")));
            cls.setDescripcion(c.getString(c.getColumnIndex("Descripcion")));
            cls.setTotalRegistros(c.getString(c.getColumnIndex("TotalRegistros")));
            arrayList.add(cls);
            c.moveToNext();
        }
        db.close();
        return arrayList;
    }

    public ArrayList selectServicioDetalle(String IdServicio,String IdEncabezado){
        ArrayList<ServicioDetalle> arrayList = new ArrayList<>();
        db = getWritableDatabase();
        query = "";

        query += " SELECT a.Id ";
        query += " ,IFNULL(b.Id,0) AS IdRevisionDetalle ";
        query += " ,a.IdServicio  ";
        query += " ,a.CodRevision  ";
        query += " ,a.DescripiconRevision  ";
        query += " ,a.PunteoSistema  ";
        query += " ,IFNULL(b.PunteoReal,a.PunteoReal) AS PunteoReal  ";
        query += " ,IFNULL(b.Ponderacion,a.Ponderacion) AS  Ponderacion ";
        query += " ,b.Observaciones ";
        query += " FROM " + TBL_SERVICIO_DETALLE  +" a ";
        query += " LEFT JOIN  (";
        query += " SELECT Id,IdServicioDetalle, PunteoReal, Ponderacion,Observaciones  FROM  " + TBL_REVISION_DETALLE;
        query += " WHERE 1 = 1";
        if(!IdEncabezado.isEmpty()){
            query += " AND IdRevision = " + IdEncabezado;
        }

        query += " ) b ON b.IdServicioDetalle = a.Id " ;
        query += " WHERE 1 = 1 ";

        if(!IdServicio.isEmpty()){
            query += " AND a.IdServicio = " + IdServicio;
        }


        query += " ORDER BY a.Id DESC ";

        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        while (!c.isAfterLast()) {

            ServicioDetalle cls = new ServicioDetalle();
            cls.setId(c.getInt(c.getColumnIndex("Id")));
            cls.setIdRevisionDetalle(c.getInt(c.getColumnIndex("IdRevisionDetalle")));
            cls.setIdServicio(c.getString(c.getColumnIndex("IdServicio")));
            cls.setCodRevision(c.getString(c.getColumnIndex("CodRevision")));
            cls.setDescripiconRevision(c.getString(c.getColumnIndex("DescripiconRevision")));
            cls.setPunteoSistema(c.getString(c.getColumnIndex("PunteoSistema")));
            cls.setPunteoReal(c.getString(c.getColumnIndex("PunteoReal")));
            cls.setPonderacion(c.getString(c.getColumnIndex("Ponderacion")));
            cls.setObservaciones(c.getString(c.getColumnIndex("Observaciones")));
            if(!IdEncabezado.isEmpty()){
                cls.setIdRevision(Integer.valueOf(IdEncabezado));
            }

            arrayList.add(cls);
            c.moveToNext();
        }
        db.close();
        return arrayList;
    }

    public String selectServicioDetalleJSON(String IdEncabezado){
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        int i = 0;
        db = getWritableDatabase();
        query = "";

        query += " SELECT a.Id ";
        query += " ,IFNULL(b.Id,0) AS IdRevisionDetalle ";
        query += " ,a.IdServicio  ";
        query += " ,a.CodRevision  ";
        query += " ,a.DescripiconRevision  ";
        query += " ,a.PunteoSistema  ";
        query += " ,IFNULL(b.PunteoReal,a.PunteoReal) AS PunteoReal  ";
        query += " ,IFNULL(b.Ponderacion,a.Ponderacion) AS  Ponderacion ";
        query += " ,b.Observaciones ";
        query += " FROM  "+ TBL_REVISION_DETALLE  + " b ";
        query += " INNER JOIN  " +  TBL_SERVICIO_DETALLE   +" a ON b.IdServicioDetalle = a.Id";
        query += " WHERE 1 = 1 ";
        if(!IdEncabezado.isEmpty()){
            query += " AND b.IdRevision = " + IdEncabezado;
        }
        query += " ORDER BY a.Id DESC ";

        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            JSONObject contact = new JSONObject();
            try {
                contact.put("c1", c.getInt(c.getColumnIndex("IdServicio")));
                contact.put("c2", c.getInt(c.getColumnIndex("CodRevision")));
                contact.put("c3", c.getString(c.getColumnIndex("PunteoSistema")));
                contact.put("c4", c.getString(c.getColumnIndex("PunteoReal")));
                contact.put("c5", c.getString(c.getColumnIndex("Ponderacion")));
                contact.put("c6", c.getString(c.getColumnIndex("Observaciones")));
                jsonArray.put(i, contact);
                i++;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            c.moveToNext();
        }
        db.close();
        try {
            jsonObject.put("datos", jsonArray);
        } catch (Exception e2) {
        }
        return jsonObject.toString();
    }

    public ArrayList selectRevisionDetalle(String IdEncabezado){
        ArrayList<ServicioDetalle> arrayList = new ArrayList<>();
        db = getWritableDatabase();
        query = "";
        query += " SELECT b.Id ";
        query += " ,a.Id AS IdRevisionDetalle ";
        query += " ,b.IdServicio  ";
        query += " ,b.CodRevision  ";
        query += " ,b.DescripiconRevision  ";
        query += " ,b.PunteoSistema  ";
        query += " ,a.PunteoReal  ";
        query += " ,a.Ponderacion ";
        query += " ,a.Observaciones ";
        query += " FROM " +  TBL_REVISION_DETALLE  +" a ";
        query += " INNER  JOIN  " + TBL_SERVICIO_DETALLE  + " b  ON b.Id = a.IdServicioDetalle ";
        query += " WHERE  a.IdRevision = " + IdEncabezado;

        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        while (!c.isAfterLast()) {

            ServicioDetalle cls = new ServicioDetalle();
            cls.setId(c.getInt(c.getColumnIndex("Id")));
            cls.setIdRevisionDetalle(c.getInt(c.getColumnIndex("IdRevisionDetalle")));
            cls.setIdServicio(c.getString(c.getColumnIndex("IdServicio")));
            cls.setCodRevision(c.getString(c.getColumnIndex("CodRevision")));
            cls.setDescripiconRevision(c.getString(c.getColumnIndex("DescripiconRevision")));
            cls.setPunteoSistema(c.getString(c.getColumnIndex("PunteoSistema")));
            cls.setPunteoReal(c.getString(c.getColumnIndex("PunteoReal")));
            cls.setPonderacion(c.getString(c.getColumnIndex("Ponderacion")));
            cls.setObservaciones(c.getString(c.getColumnIndex("Observaciones")));
            if(!IdEncabezado.isEmpty()){
                cls.setIdRevision(Integer.valueOf(IdEncabezado));
            }
            arrayList.add(cls);
            c.moveToNext();
        }
        db.close();
        return arrayList;
    }

    public ArrayList selectRevision(String Id){
        ArrayList<RevisionTecleo> arrayList = new ArrayList<>();
        db = getWritableDatabase();
        query = "";

        query += " SELECT Id ";
        query += " ,IdVehiculo  ";
        query += " ,NombrePiloto  ";
        query += " ,EdadPiloto  ";
        query += " ,LicenciaPiloto  ";
        query += " ,FechaVenceLicencia  ";
        query += " ,CodPiloto  ";
        query += " ,Fecha  ";
        query += " ,TC  ";
        query += " ,Placa  ";
        query += " ,IFNULL(IdentificadorDB,'0') AS IdentificadorDB ";
        query += " ,Transferido ";
        query += " FROM " + TBL_REVISION;
        query += " WHERE 1 = 1 ";
        if(!Id.isEmpty()){
            query += " AND Id = " + Id;
        }
        query += " ORDER BY Id DESC ";

        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        while (!c.isAfterLast()) {

            RevisionTecleo cls = new RevisionTecleo();
            cls.setId(c.getInt(c.getColumnIndex("Id")));
            cls.setIdVehiculo(c.getString(c.getColumnIndex("IdVehiculo")));
            cls.setNombrePiloto(c.getString(c.getColumnIndex("NombrePiloto")));
            cls.setEdadPiloto(c.getString(c.getColumnIndex("EdadPiloto")));
            cls.setLicenciaPiloto(c.getString(c.getColumnIndex("LicenciaPiloto")));
            cls.setFechaVenceLicencia(c.getString(c.getColumnIndex("FechaVenceLicencia")));
            cls.setCodPiloto(c.getString(c.getColumnIndex("CodPiloto")));
            cls.setFecha(c.getString(c.getColumnIndex("Fecha")));
            cls.setTC(c.getString(c.getColumnIndex("TC")));
            cls.setPlaca(c.getString(c.getColumnIndex("Placa")));
            cls.setIdentificadorDB(c.getString(c.getColumnIndex("IdentificadorDB")));
            cls.setTransferido(c.getInt(c.getColumnIndex("Transferido")));

            arrayList.add(cls);
            c.moveToNext();
        }
        db.close();
        return arrayList;
    }


    // </editor-fold>


    // <editor-fold defaultstate="collapsed" desc="Exists">

    public boolean existUsuario(String Usuario, String Contraseña) {
        boolean exists = true;
        db = getWritableDatabase();
        query = "";
        query = " SELECT Usuario,Contraseña";
        query += " FROM " + TBL_USUARIO;
        query += " WHERE Usuario = '" + Usuario + "'";
        query += " AND Contraseña  =  '" + util.md5(Contraseña) + "'";
        Cursor c = db.rawQuery(query, null);
        if (c.getCount() <= 0) {
            exists = false;
        }
        db.close();
        c.close();
        return exists;
    }
    // </editor-fold>


}
