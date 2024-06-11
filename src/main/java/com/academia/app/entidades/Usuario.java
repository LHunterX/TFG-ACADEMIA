package com.academia.app.entidades;

import com.academia.app.enums.Rol;
import jakarta.persistence.*;
import org.thymeleaf.model.IProcessingInstruction;

import java.util.List;

@Entity
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String email;

    private String contraseña;

    private Rol rol;

    @OneToOne(cascade = CascadeType.ALL)
    private Archivo foto;

    public Usuario() {
    }

    public Usuario(String name, String email, String contraseña, Rol rol) {
        this.name = name;
        this.email = email;
        this.contraseña = contraseña;
        this.rol = rol;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public Archivo getFoto() {
        return foto;
    }

    public void setFoto(Archivo foto) {
        this.foto = foto;
    }
}
