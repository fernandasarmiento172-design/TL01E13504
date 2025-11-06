package com.example.tl01e13504.Configuraciones;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.tl01e13504.Configuraciones.Contactos; // Ajusta el paquete si es diferente

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
public class SQLLiteConexion extends SQLiteOpenHelper
{
    public SQLLiteConexion(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);

    }



    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL(Transacciones.CREATETABLECONTACTOS);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL(Transacciones.DROPTABLECONTACTOS); // Usar la instancia provista
        onCreate(sqLiteDatabase);
    }
    public long insertarContacto(String nombre, String telefono, String nota, String pais, String fotoBase64) {
        SQLiteDatabase db = this.getWritableDatabase(); // 1. Abrir conexión

        ContentValues valores = new ContentValues(); // 2. Preparar datos
        valores.put(Transacciones.nombre, nombre);
        valores.put(Transacciones.telefono, telefono);
        valores.put(Transacciones.nota, nota);
        valores.put(Transacciones.listapaises, pais);
        valores.put(Transacciones.foto, fotoBase64);

        long id = db.insert(Transacciones.TableContactos, null, valores); // 3. Ejecutar INSERT

        db.close(); // 4. Cerrar conexión
        return id; // 5. Devolver el resultado
    }
    public List<Contactos> obtenerContactos() {
        List<Contactos> listaContactos = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            // Ejecutar la consulta SELECT * FROM contacto
            cursor = db.rawQuery(Transacciones.SELECTTABLECONTACTOS, null);

            if (cursor != null && cursor.moveToFirst()) {

                // Obtener índices de las columnas
                int idIndex = cursor.getColumnIndex(Transacciones.id);
                int nombreIndex = cursor.getColumnIndex(Transacciones.nombre);
                int telefonoIndex = cursor.getColumnIndex(Transacciones.telefono);
                int notaIndex = cursor.getColumnIndex(Transacciones.nota);
                int paisIndex = cursor.getColumnIndex(Transacciones.listapaises);

                do {
                    Contactos contacto = new Contactos();

                    // Asignar los valores del cursor al objeto Contactos
                    if (idIndex != -1) contacto.setId(cursor.getInt(idIndex));
                    if (nombreIndex != -1) contacto.setNombre(cursor.getString(nombreIndex));
                    if (telefonoIndex != -1) contacto.setTelefono(cursor.getString(telefonoIndex));
                    if (notaIndex != -1) contacto.setNota(cursor.getString(notaIndex));
                    if (paisIndex != -1) contacto.setPais(cursor.getString(paisIndex));

                    listaContactos.add(contacto);
                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
            // Imprime el error en Logcat para diagnóstico
            e.printStackTrace();
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }
        return listaContactos;
    }

}



