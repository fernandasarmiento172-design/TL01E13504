package com.example.tl01e13504.Configuraciones;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;

public class DbContactos extends DbHelper {

    Context context;

    public DbContactos(Context context) {
        super(context);
        this.context = context;
    }

    // --- MÉTODO PARA INSERTAR CONTACTO ---
    public long insertarContacto(String nombre, String telefono, String nota, String codigoPais, String fotoBase64) {
        long id = 0;
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("nombre", nombre);
            values.put("telefono", telefono);
            values.put("nota", nota);
            values.put("codigoPais", codigoPais);
            values.put("fotoBase64", fotoBase64);

            id = db.insert(TABLE_CONTACTOS, null, values);
            db.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return id;
    }

    // --- MÉTODO PARA LEER TODOS LOS CONTACTOS ---
    public ArrayList<Contacto> mostrarContactos() {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<Contacto> listaContactos = new ArrayList<>();
        Cursor cursor = null;

        cursor = db.rawQuery("SELECT * FROM " + TABLE_CONTACTOS + " ORDER BY nombre ASC", null);

        if (cursor.moveToFirst()) {
            do {
                Contacto contacto = new Contacto();
                contacto.setId(cursor.getInt(0));
                contacto.setNombre(cursor.getString(1));
                contacto.setTelefono(cursor.getString(2));
                contacto.setNota(cursor.getString(3));
                contacto.setCodigoPais(cursor.getString(4));
                contacto.setFotoBase64(cursor.getString(5));
                listaContactos.add(contacto);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return listaContactos;
    }
}