package com.example.tl01e13504.Configuraciones;

import java.io.Serializable; // Importación necesaria

public class Contactos implements Serializable { // 👈 IMPLEMENTAR Serializable

    // Es buena práctica incluir este ID
    private static final long serialVersionUID = 1L;

    private int id;
    private String nombre;
    private String telefono;
    private String nota;
    private String codigoPais;
    private String imagen;

    // Constructor vacío
    public Contactos() {}

    // Constructor completo
    public Contactos(int id, String nombre, String telefono, String nota, String codigoPais, String imagen) {
        this.id = id;
        this.nombre = nombre;
        this.telefono = telefono;
        this.nota = nota;
        this.codigoPais = codigoPais;
        this.imagen = imagen;
    }

    // Getters y setters

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getNota() { return nota; }
    public void setNota(String nota) { this.nota = nota; }

    public String getCodigoPais() { return codigoPais; }
    public void setCodigoPais(String codigoPais) { this.codigoPais = codigoPais; }

    public String getImagen() { return imagen; }
    public void setImagen(String imagen) { this.imagen = imagen; }

    // Representación en cadena (útil para debug o listados simples)
    @Override
    public String toString() {
        return nombre + " (" + codigoPais + ") - " + telefono + " | Nota: " + nota;
    }
}