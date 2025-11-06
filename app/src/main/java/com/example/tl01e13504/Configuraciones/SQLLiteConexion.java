package com.example.tl01e13504.Configuraciones;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.tl01e13504.Contactos;
import java.util.ArrayList;

public class SQLLiteConexion extends SQLiteOpenHelper {

    public SQLLiteConexion(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Transacciones.CreateTableContactos);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Transacciones.TablaContactos);
        onCreate(db);
    }

    // 👉 Método para insertar contacto
    public long insertarContacto(String nombre, String telefono, String nota, String codigoPais, String imagen) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put(Transacciones.nombre, nombre);
        valores.put(Transacciones.telefono, telefono);
        valores.put(Transacciones.nota, nota);
        valores.put(Transacciones.codigoPais, codigoPais);
        valores.put(Transacciones.imagen, imagen);
        long resultado = db.insert(Transacciones.TablaContactos, null, valores);
        db.close();
        return resultado;
    }

    // 👉 Método para obtener contactos
    public ArrayList<Contactos> obtenerContactos() {
        ArrayList<Contactos> lista = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + Transacciones.TablaContactos, null);

        if (cursor.moveToFirst()) {
            do {
                Contactos contacto = new Contactos();
                contacto.setId(cursor.getInt(cursor.getColumnIndexOrThrow(Transacciones.id)));
                contacto.setNombre(cursor.getString(cursor.getColumnIndexOrThrow(Transacciones.nombre)));
                contacto.setTelefono(cursor.getString(cursor.getColumnIndexOrThrow(Transacciones.telefono)));
                contacto.setNota(cursor.getString(cursor.getColumnIndexOrThrow(Transacciones.nota)));
                contacto.setCodigoPais(cursor.getString(cursor.getColumnIndexOrThrow(Transacciones.codigoPais)));
                contacto.setImagen(cursor.getString(cursor.getColumnIndexOrThrow(Transacciones.imagen)));
                lista.add(contacto);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return lista;
    }
}



