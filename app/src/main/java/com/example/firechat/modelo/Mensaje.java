package com.example.firechat.modelo;

public class Mensaje {
    String texto, hora, emisor, receptor;
    int leido;

    public Mensaje() {
    }

    public Mensaje(String texto, String hora, int leido, String emisor, String receptor) {
        this.texto = texto;
        this.hora = hora;
        this.leido = leido;
        this.emisor = emisor;
        this.receptor = receptor;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public int getLeido() {
        return leido;
    }

    public void setLeido(int leido) {
        this.leido = leido;
    }

    public String getEmisor() {
        return emisor;
    }

    public void setEmisor(String emisor) {
        this.emisor = emisor;
    }

    public String getReceptor() {
        return receptor;
    }

    public void setReceptor(String receptor) {
        this.receptor = receptor;
    }
}
