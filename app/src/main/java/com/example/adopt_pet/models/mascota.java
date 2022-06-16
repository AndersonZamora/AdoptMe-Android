package com.example.adopt_pet.models;

public class mascota extends Model{

    String nombre;
    String raza;
    String edad;
    String contacto;
    String descripcion;
    String estado;
    String fechaPublication;
    String tipo;
    String foto;
    String path;
    boolean state;
    String  adopcion;

    public mascota() {
        // Constructor requerido
    }

    public String getAdopcion() {
        return adopcion;
    }

    public void setAdopcion(String adopcion) {
        this.adopcion = adopcion;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRaza() {
        return raza;
    }

    public void setRaza(String raza) {
        this.raza = raza;
    }

    public String getEdad() {
        return edad;
    }

    public void setEdad(String edad) {
        this.edad = edad;
    }

    public String getContacto() {
        return contacto;
    }

    public void setContacto(String contacto) {
        this.contacto = contacto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getFechaPublication() {
        return fechaPublication;
    }

    public void setFechaPublication(String fechaPublication) {
        this.fechaPublication = fechaPublication;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
