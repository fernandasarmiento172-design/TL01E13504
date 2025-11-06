package com.example.tl01e13504.Configuraciones;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 3;
    private static final String DATABASE_NOMBRE = "agenda.db";
    public static final String TABLE_CONTACTOS = "t_contactos";

    public DbHelper(Context context) {
        super(context, DATABASE_NOMBRE, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Incluimos todos los campos de tu formulario
        db.execSQL("CREATE TABLE " + TABLE_CONTACTOS + "(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nombre TEXT NOT NULL," +
                "telefono TEXT NOT NULL," +
                "nota TEXT," +
                "codigoPais TEXT," +
                "fotoBase64 TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTOS);
        onCreate(db);
    }
}

