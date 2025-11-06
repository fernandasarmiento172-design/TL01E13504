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
            // USAR directamtente this.getWritableDatabase()
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
    // En DbContactos.java

    public ArrayList<Contacto> mostrarContactos() {
        SQLiteDatabase db = this.getReadableDatabase();

        ArrayList<Contacto> listaContactos = new ArrayList<>();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("SELECT * FROM " + TABLE_CONTACTOS, null);

            if (cursor.moveToFirst()) {
                do {
                    Contacto contacto = new Contacto();

                    // --- Mapeo CRÍTICO por índice ---
                    contacto.setId(cursor.getInt(0));         // Índice 0: id
                    contacto.setNombre(cursor.getString(1));  // Índice 1: nombre
                    contacto.setTelefono(cursor.getString(2)); // Índice 2: telefono

                    // NOTA está en el índice 3. Como no la usas, pasamos al siguiente.

                    contacto.setCodigoPais(cursor.getString(4)); // Índice 4: codigoPais
                    // ---------------------------------

                    listaContactos.add(contacto);

                } while (cursor.moveToNext());
            }

        } catch (Exception ex) {
            // MUY IMPORTANTE: Imprime el error para ver exactamente qué índice falla
            ex.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                // No cerrar la DB si otros métodos la necesitan.
                // Aunque es buena práctica cerrarla, a veces genera problemas.
                // Para el diagnóstico, déjalo abierto o asegúrate de que esté bien cerrado.
                // db.close();
            }
        }
        return listaContactos;
    }
}

