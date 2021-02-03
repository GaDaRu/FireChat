package com.example.firechat.modelo;

import java.util.List;

public class Usuario {
    String id, email, nombre, telefono;
    List<Mensaje> mensajesRecibidos;
    List<Mensaje> mensajesLeidos;

    public Usuario() {
    }

    public Usuario(String id, String email, String nombre, String telefono) {
        this.id = id;
        this.email = email;
        this.nombre = nombre;
        this.telefono = telefono;
    }

    public Usuario(String id, String email, String nombre, String telefono, List<Mensaje> mensajesRecibidos, List<Mensaje> mensajesLeidos) {
        this.id = id;
        this.email = email;
        this.nombre = nombre;
        this.telefono = telefono;
        this.mensajesRecibidos = mensajesRecibidos;
        this.mensajesLeidos = mensajesLeidos;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public List<Mensaje> getMensajesRecibidos() {
        return mensajesRecibidos;
    }

    public void setMensajesRecibidos(List<Mensaje> mensajesRecibidos) {
        this.mensajesRecibidos = mensajesRecibidos;
    }

    public List<Mensaje> getMensajesLeidos() {
        return mensajesLeidos;
    }

    public void setMensajesLeidos(List<Mensaje> mensajesLeidos) {
        this.mensajesLeidos = mensajesLeidos;
    }
}
