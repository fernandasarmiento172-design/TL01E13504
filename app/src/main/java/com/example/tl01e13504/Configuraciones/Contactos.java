package com.example.tl01e13504.Configuraciones;

import androidx.annotation.NonNull;

public class Contactos {
    private int id;
    private String nombre;
    private String telefono;
    private String nota;
    private String pais; // Usaremos 'pais' en lugar de 'listapaises' para simplificar
    private String fotoBase64; // La cadena base64 de la foto

    // Constructor vacío (necesario si vas a usar un Cursor para llenar los datos)
    public Contactos() {}

    // Constructor para cuando se necesita un objeto completo
    public Contactos(int id, String nombre, String telefono, String nota, String pais, String fotoBase64) {
        this.id = id;
        this.nombre = nombre;
        this.telefono = telefono;
        this.nota = nota;
        this.pais = pais;
        this.fotoBase64 = fotoBase64;
    }

    // Getters y Setters
    // (Debes generarlos todos: Alt + Insert o Click derecho -> Generate -> Getter and Setter)

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getNota() { return nota; }
    public void setNota(String nota) { this.nota = nota; }

    public String getPais() { return pais; }
    public void setPais(String pais) { this.pais = pais; }

    public String getFotoBase64() { return fotoBase64; }
    public void setFotoBase64(String fotoBase64) { this.fotoBase64 = fotoBase64; }

    // Método ToString para mostrar datos en un ListView simple (Importante para la lista)

    @Override
    public String toString() {
        return "ID: " + id + " | Nombre: " + nombre + " | Teléfono: " + telefono + " (" + pais + ")";
    }
}

