package com.example.tl01e13504.Configuraciones;

public class Contacto {
    private int id;
    private String nombre;
    private String telefono;
    private String nota;
    private String codigoPais; // Guardaremos el código del país seleccionado
    private String fotoBase64; // Campo para guardar la foto

    public Contacto() {

    }
    public Contacto(int id, String nombre, String telefono, String nota, String codigoPais, String fotoBase64) {
        this.id = id;
        this.nombre = nombre;
        this.telefono = telefono;
        this.nota = nota;
        this.codigoPais = codigoPais;
        this.fotoBase64 = fotoBase64;
    }

    // --- GETTERS Y SETTERS ---

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

    public String getFotoBase64() { return fotoBase64; }
    public void setFotoBase64(String fotoBase64) { this.fotoBase64 = fotoBase64; }
}

