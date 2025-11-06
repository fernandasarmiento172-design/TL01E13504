package com.example.tl01e13504;

public class Contactos {
    private int id;
    private String nombre;
    private String telefono;
    private String nota;
    private String codigoPais;
    private String imagen;

    public Contactos() {}

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

    // 👇 Esto define cómo se verá en el ListView
    @Override
    public String toString() {
        return nombre + " (" + codigoPais + ") - " + telefono;
    }
}

