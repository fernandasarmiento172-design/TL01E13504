package com.example.tl01e13504.Configuraciones;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;

public class SQLLiteConexion extends SQLiteOpenHelper {

    private Context context;

    public SQLLiteConexion(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
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

    // Método 1: Insertar contacto
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

    // Método 2: Obtener todos los contactos
    public ArrayList<Contactos> obtenerContactos() {
        ArrayList<Contactos> lista = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + Transacciones.TablaContactos, null);
        Toast.makeText(context, "Filas encontradas: " + cursor.getCount(), Toast.LENGTH_SHORT).show();

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

    // Método 3: Eliminar contacto
    public int eliminarContacto(int id) {
        SQLiteDatabase db = getWritableDatabase();

        String selection = Transacciones.id + " = ?";
        String[] selectionArgs = { String.valueOf(id) };

        int filasEliminadas = db.delete(
                Transacciones.TablaContactos,
                selection,
                selectionArgs
        );

        db.close();
        return filasEliminadas;
    }

    // ⭐ MÉTODO 4: ACTUALIZAR CONTACTO (AGREGADO) ⭐
    public int actualizarContacto(int id, String nombre, String telefono, String nota, String codigoPais, String imagen) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues valores = new ContentValues();

        // 1. Nuevos valores
        valores.put(Transacciones.nombre, nombre);
        valores.put(Transacciones.telefono, telefono);
        valores.put(Transacciones.nota, nota);
        valores.put(Transacciones.codigoPais, codigoPais);
        valores.put(Transacciones.imagen, imagen);

        // 2. Define la condición WHERE
        String selection = Transacciones.id + " = ?";
        String[] selectionArgs = { String.valueOf(id) };

        // 3. Ejecutar la actualización (db.update)
        int filasActualizadas = db.update(
                Transacciones.TablaContactos, // Tabla
                valores,                      // Datos
                selection,                    // WHERE
                selectionArgs                 // Argumentos
        );

        db.close();
        return filasActualizadas;
    }
}




