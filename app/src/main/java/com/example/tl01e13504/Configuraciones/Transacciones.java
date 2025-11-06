package com.example.tl01e13504.Configuraciones;

public class Transacciones {

    public static final String DBNAME = "TL01E13504";
    public static final int DBVERSION = 2;

    public static final String TableContactos= "contacto";

    public static final String id = "id";
    public static final String nombre = "nombre";
    public static final String telefono = "telefono";
    public static final String nota = "nota";
    public static final String listapaises = "listapaises";
    public static final String foto = "foto";

    // DDL
    public static final String CREATETABLECONTACTOS =
            "CREATE TABLE " + TableContactos + " (" +
                    id + " INTEGER PRIMARY KEY AUTOINCREMENT, " + // Se agregó la PK
                    nombre + " TEXT NOT NULL, " + // Se añadió el espacio
                    telefono + " TEXT NOT NULL, " + // Se añadió el espacio
                    nota + " TEXT, " + // Se cambió INTEGER por TEXT si se usará como una cadena
                    listapaises + " TEXT, " + // Se añadió el espacio
                    foto + " TEXT) "; // Se añadió el espacio y cerró paréntesis

    public static final String DROPTABLECONTACTOS = "DROP TABLE IF EXISTS " + TableContactos;

    // DML - select, insert, update, delete
    public static final String SELECTTABLECONTACTOS = "SELECT * FROM " + TableContactos; // Corregido de + a *

}