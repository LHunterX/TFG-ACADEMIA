package com.academia.app.entidades;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class AsignaturaPromocion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    private Asignatura asignatura;

    @ManyToOne(cascade = CascadeType.ALL)
    private Promocion promocion;

    @ManyToOne(cascade = CascadeType.ALL)
    private Usuario profesor;

    @OneToMany(mappedBy = "asignatura", cascade = CascadeType.ALL)
    private List<AsignaturaAlumno> alumnos;

    @OneToMany(mappedBy = "asignatura", cascade = CascadeType.ALL)
    private List<Tarea> tareas;

    public AsignaturaPromocion() {
    }

    public AsignaturaPromocion(Long id) {
        this.id = id;
    }

    public AsignaturaPromocion(Asignatura asignatura, Promocion promocion, Usuario profesor, List<AsignaturaAlumno> alumnos) {
        this.asignatura = asignatura;
        this.promocion = promocion;
        this.profesor = profesor;
        this.alumnos = alumnos;
        this.tareas = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Asignatura getAsignatura() {
        return asignatura;
    }

    public void setAsignatura(Asignatura asignatura) {
        this.asignatura = asignatura;
    }

    public Promocion getPromocion() {
        return promocion;
    }

    public void setPromocion(Promocion promocion) {
        this.promocion = promocion;
    }

    public Usuario getProfesor() {
        return profesor;
    }

    public void setProfesor(Usuario profesor) {
        this.profesor = profesor;
    }

    public List<AsignaturaAlumno> getAlumnos() {
        return alumnos;
    }

    public void setAlumnos(List<AsignaturaAlumno> alumnos) {
        this.alumnos = alumnos;
    }

    public List<Tarea> getTareas() {
        return tareas;
    }

    public void setTareas(List<Tarea> tareas) {
        this.tareas = tareas;
    }
}
