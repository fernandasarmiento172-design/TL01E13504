package com.example.tl01e13504.Configuraciones;

public class Transacciones {
    // Nombre de la base de datos y versión
    public static final String DBNAME = "DBContactos";
    public static final int DBVERSION = 1;

    // Nombre de la tabla
    public static final String TablaContactos = "contactos";

    // Campos
    public static final String id = "id";
    public static final String nombre = "nombre";
    public static final String telefono = "telefono";
    public static final String nota = "nota";
    public static final String codigoPais = "codigoPais";
    public static final String imagen = "imagen";

    // Sentencia para crear tabla
    public static final String CreateTableContactos =
            "CREATE TABLE " + TablaContactos + " (" +
                    id + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    nombre + " TEXT, " +
                    telefono + " TEXT, " +
                    nota + " TEXT, " +
                    codigoPais + " TEXT, " +
                    imagen + " TEXT)";
}
