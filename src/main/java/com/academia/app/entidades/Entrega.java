package com.academia.app.entidades;

import jakarta.persistence.*;

@Entity
public class Entrega {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    private Tarea tarea;

    @ManyToOne
    private Usuario alumno;

    private Double nota;

    @ManyToOne(cascade = CascadeType.ALL)
    private Archivo archivo;

    public Entrega() {
    }

    public Entrega(Tarea tarea, Usuario alumno, Double nota, Archivo archivo) {
        this.tarea = tarea;
        this.alumno = alumno;
        this.nota = nota;
        this.archivo = archivo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Tarea getTarea() {
        return tarea;
    }

    public void setTarea(Tarea tarea) {
        this.tarea = tarea;
    }

    public Usuario getAlumno() {
        return alumno;
    }

    public void setAlumno(Usuario alumno) {
        this.alumno = alumno;
    }

    public Double getNota() {
        return nota;
    }

    public void setNota(Double nota) {
        this.nota = nota;
    }

    public Archivo getArchivo() {
        return archivo;
    }

    public void setArchivo(Archivo archivo) {
        this.archivo = archivo;
    }
}
