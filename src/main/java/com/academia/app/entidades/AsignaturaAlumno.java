package com.academia.app.entidades;

import jakarta.persistence.*;

@Entity
public class AsignaturaAlumno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private AsignaturaPromocion asignatura;

    @ManyToOne(cascade = CascadeType.ALL)
    private Usuario alumno;

    public AsignaturaAlumno() {
    }

    public AsignaturaAlumno(AsignaturaPromocion asignatura, Usuario alumno) {
        this.asignatura = asignatura;
        this.alumno = alumno;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AsignaturaPromocion getAsignatura() {
        return asignatura;
    }

    public void setAsignatura(AsignaturaPromocion asignatura) {
        this.asignatura = asignatura;
    }

    public Usuario getAlumno() {
        return alumno;
    }

    public void setAlumno(Usuario alumno) {
        this.alumno = alumno;
    }

}
