package com.example.lab09_10.Data;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.lab09_10.Model.Curso;
import com.example.lab09_10.Model.Estudiante;
import com.example.lab09_10.Model.Usuario;

import java.util.ArrayList;
import java.util.List;

public class DBAdapterSQL extends SQLiteOpenHelper{
    private static final String TAG = "com.example.lab09_10.Data";
    private static DBAdapterSQL mInstance = null;
    static final String DATABASE_NAME = "Lab09102.db";
    static final String DATABASE_TABLE_USER = "Usuario";
    static final String DATABASE_TABLE_ESTUDIANTE = "Estudiante";
    static final String DATABASE_TABLE_CURSO = "Curso";
    static final String DATABASE_TABLE_CURSOSESTUDIANTE= "CursosEstudiante";
    static final int DATABASE_VERSION = 1;

    static final String userTable = "create table "+DATABASE_TABLE_USER+"(id integer primary key autoincrement " +
                                     ", username text not null, password text not null, rol text not null)";
    static final String estudianteTable = "create table "+DATABASE_TABLE_ESTUDIANTE+
                                          "(id integer primary key autoincrement, cedula text not null,  nombre text not null," +
                                            "apellidos text not null, edad integer not null, user integer not null," +
                                            "foreign key(user) references Usuario(id))";
    static final String cursoTable = "create table "+DATABASE_TABLE_CURSO+"(id  integer primary key autoincrement,"+
                                        "descripcion text not null, creditos integer not null)";
    static final String cursosEstudiante = "create table "+DATABASE_TABLE_CURSOSESTUDIANTE+" (idEstudiante integer not null," +
            "idCurso integer not null, foreign key(idEstudiante) references Estudiante(id), foreign key(idCurso) references Curso(id))";
    private Context mCtx;
    SQLiteDatabase db;
    public DBAdapterSQL(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        db  = this.getWritableDatabase();
    }

    public static DBAdapterSQL getInstance(Context ctx) {
        if (mInstance == null) {
            mInstance = new DBAdapterSQL(ctx.getApplicationContext());
        }
        return mInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String ejercicio = "admin";
        String dia ="admin";
        String tiempo = "admin";
        String orden = "INSERT INTO Usuario(usuario,password,rol) VALUES ('" +  ejercicio +"','"+dia+"','"+tiempo+"');";
        try {
            db.execSQL(userTable);
            db.execSQL(estudianteTable);
            db.execSQL(cursoTable);
            db.execSQL(cursosEstudiante);
            db.execSQL(orden);
           // db.execSQL(orden);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+DATABASE_TABLE_USER);
        onCreate(db);
    }
    public DBAdapterSQL open() throws SQLException
    {
        this.getWritableDatabase();
        return this;
    }
    public void close()
    {
       db.close();
    }
    public void insertarUsuario(Usuario user){
        db.beginTransaction();
        db  = this.getWritableDatabase();
        try{
            String orden = "INSERT INTO " + DATABASE_TABLE_USER + " (username,password,rol) VALUES ('" +  user.getUsuario() +"','"+user.getPassword()+"','"+user.getRol()+"');";
            db.execSQL(orden);
            db.setTransactionSuccessful();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        finally {
            db.endTransaction();
        }
    }
    public Usuario getUsuario(String username, String password){
       //db.beginTransaction();
        Usuario user = new Usuario();
        db  = this.getWritableDatabase();
        Cursor c = null;
        try{
                String[] args = new String[]{username, password};
            c = db.rawQuery("select * from usuario where username =? and password = ?", args);
            if(c.moveToFirst()){
                user.setId(c.getInt(0));
                user.setUsuario(c.getString(1));
                user.setPassword(c.getString(2));
                user.setRol(c.getString(3));
                return user;
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        finally {
            if (c != null) {
                c.close();
            }
        }
        return null;
    }
    public boolean insertarEstudiante(Estudiante est){
        db.beginTransaction();
        db  = this.getWritableDatabase();
        try{
            String orden = "INSERT INTO " + DATABASE_TABLE_ESTUDIANTE + " (cedula,nombre,apellidos, edad, user) VALUES ('" +  est.getCedula()
                    +"','"+est.getNombre()+"','"+est.getApellidos()+"','"+est.getEdad()+"','"+est.getUser()+"');";
            db.execSQL(orden);
            db.setTransactionSuccessful();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        finally {
            db.endTransaction();
        }
        return false;
    }
    public List<Estudiante> listEstudiantes(){
        List<Estudiante> list = new ArrayList<>();
         db.beginTransaction();
        db  = this.getWritableDatabase();
        Estudiante estudiante;
        try{
            String orden = "select * from estudiante";
            Cursor c = db.rawQuery(orden, null);
            while(c.moveToNext()){
                estudiante = new Estudiante();
                estudiante.setId(c.getInt(0));
                estudiante.setCedula(c.getString(1));
                estudiante.setNombre(c.getString(2));
                estudiante.setApellidos(c.getString(3));
                estudiante.setEdad(c.getInt(4));
                estudiante.setUser(c.getInt(5));
                list.add(estudiante);
            }
            c.close();
            return list;
        }catch (Exception ex){
            ex.printStackTrace();
        }
        finally {
            db.endTransaction();
        }
        return null;
    }
    public void updateEstudiante(Estudiante est){
        db.beginTransaction();
        db  = this.getWritableDatabase();
        try{
            String orden = "update estudiante set cedula ='"+est.getCedula()+"',nombre = '"+est.getNombre()+"', apellidos='"+est.getApellidos()+
                    "', edad="+est.getEdad()+" where id = "+est.getId();
            db.execSQL(orden);
            db.setTransactionSuccessful();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        finally {
            db.endTransaction();
        }
    }

    public boolean deleteUsuario(int id){
        db.beginTransaction();
        db  = this.getWritableDatabase();
        try{
            String orden = "Delete from usuario where id="+ id + ";";
            db.execSQL(orden);
            db.setTransactionSuccessful();
            return true;
        }catch (Exception ex){
            ex.printStackTrace();
            return false;
        }
        finally {
            db.endTransaction();
        }
    }
    public boolean deleteEstudiante(int id){
        db.beginTransaction();
        db  = this.getWritableDatabase();
        try{
            String orden = "Delete from estudiante where id="+ id + ";";
            db.execSQL(orden);
            db.setTransactionSuccessful();
            return true;
        }catch (Exception ex){
            ex.printStackTrace();
            return false;
        }
        finally {
            db.endTransaction();
        }
    }
    public void insertarCurso(Curso c){
        db.beginTransaction();
        db  = this.getWritableDatabase();
        try{
            String orden = "INSERT INTO " + DATABASE_TABLE_CURSO + " (descripcion,creditos) VALUES ('" +  c.getDescripcion()
                    +"','"+c.getCreditos()+"');";
            db.execSQL(orden);
            db.setTransactionSuccessful();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        finally {
            db.endTransaction();
            //db.close();
        }
    }
    public List<Curso> listCurso(){
        List<Curso> list = new ArrayList<>();
        db.beginTransaction();
        db  = this.getWritableDatabase();
        Curso c;
        try{
            String orden = "select * from curso";
            Cursor cursor = db.rawQuery(orden, null);
            while(cursor.moveToNext()){
                c = new Curso();
                c.setId(cursor.getInt(0));
                c.setDescripcion(cursor.getString(1));
                c.setCreditos(cursor.getInt(2));
                list.add(c);
            }
            cursor.close();
            return list;
        }catch (Exception ex){
            ex.printStackTrace();
        }
        finally {
            db.endTransaction();
        }
        return null;
    }
    public void updateCurso(Curso c){
        db.beginTransaction();
        db  = this.getWritableDatabase();
        try{
            String orden = "update curso set descripcion ='"+c.getDescripcion()+"',creditos="+c.getCreditos()+" where id = "+c.getId();
            db.execSQL(orden);
            db.setTransactionSuccessful();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        finally {
            db.endTransaction();
        }
    }
    public boolean deleteCurso(int id){
        db.beginTransaction();
        db  = this.getWritableDatabase();
        try{
            String orden = "Delete from curso where id="+ id + ";";
            db.execSQL(orden);
            db.setTransactionSuccessful();
            return true;
        }catch (Exception ex){
            ex.printStackTrace();
            return false;
        }
        finally {
            db.endTransaction();
            //db.close();
        }
    }
    public int getCountUsuario(){
        Cursor c;
        db  = this.getWritableDatabase();
        int n;
        try{
           String orden = "SELECT * FROM usuario ORDER BY id DESC LIMIT 1;";
           c = db.rawQuery(orden, null);
            if(c.moveToFirst()){
                n= c.getInt(0);
                c.close();
                return n;
            }

        }catch (Exception ex){
            ex.printStackTrace();
            return 0;
        }
        return 0;
    }
    public Estudiante getEstudiante(int user){
        Cursor c;
        Estudiante estudiante;
        db  = this.getWritableDatabase();
        try{
            String orden = "select * from estudiante where user = "+ user;
            c = db.rawQuery(orden, null);
            if(c.moveToFirst()){
                estudiante = new Estudiante();
                estudiante.setId(c.getInt(0));
                estudiante.setCedula(c.getString(1));
                estudiante.setNombre(c.getString(2));
                estudiante.setApellidos(c.getString(3));
                estudiante.setEdad(c.getInt(4));
                estudiante.setUser(c.getInt(5));
                c.close();
                return estudiante;
            }
        }catch (Exception ex){
            ex.printStackTrace();
            return null;
        }
        return  null;
    }
    public List<Curso> cursosEstudiante(int estudiante){
        List<Curso> cursos = new ArrayList<>();
        Cursor c;
        Curso curso;
        db  = this.getWritableDatabase();
        try{
            String orden = "select * from curso  c inner join cursosEstudiante b on  c.id == b.idCurso where b.idEstudiante =" + estudiante;
            c = db.rawQuery(orden, null);
            while(c.moveToNext()){
                curso = new Curso();
                curso.setId(c.getInt(0));
                curso.setDescripcion(c.getString(1));
                curso.setCreditos(c.getInt(2));
                cursos.add(curso);
            }
            c.close();
            return cursos;

        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            //c.close();
        }
        return cursos;
    }
    public boolean matricularCursos(List<Integer> cursos, int estudiante){
        db.beginTransaction();
        db  = this.getWritableDatabase();
        try{
            for(int i =0; i<cursos.size(); i++){
                String orden = "insert into cursosEstudiante(idEstudiante, idCurso) values("+estudiante+","+cursos.get(i)+");";
                db.execSQL(orden);

            }
            db.setTransactionSuccessful();
            return true;
        }catch (Exception e){
            return false;

        }
        finally {
            db.endTransaction();
        }
    }
    public boolean eliminarCursosEstudiante(int estudiante){
        db.beginTransaction();
        db  = this.getWritableDatabase();
        try{
            String orden = "delete from cursosEstudiante where idEstudiante=" + estudiante;
            db.execSQL(orden);
            db.setTransactionSuccessful();
            return true;
        }catch (Exception ex){
            ex.printStackTrace();
            return false;
        }finally {
            db.endTransaction();
        }
    }
}
