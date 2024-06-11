package com.academia.app.entidades;

import jakarta.persistence.*;

@Entity
public class Mensaje {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Usuario emisor;

    @ManyToOne
    private Usuario receptor;

    private String mensaje;

    @ManyToOne
    private Conversacion conversacion;

    public Mensaje() {
    }

    public Mensaje(Usuario emisor, Usuario receptor, String mensaje, Conversacion conversacion) {
        this.emisor = emisor;
        this.receptor = receptor;
        this.mensaje = mensaje;
        this.conversacion = conversacion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getEmisor() {
        return emisor;
    }

    public void setEmisor(Usuario emisor) {
        this.emisor = emisor;
    }

    public Usuario getReceptor() {
        return receptor;
    }

    public void setReceptor(Usuario receptor) {
        this.receptor = receptor;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Conversacion getConversacion() {
        return conversacion;
    }

    public void setConversacion(Conversacion conversacion) {
        this.conversacion = conversacion;
    }
}
