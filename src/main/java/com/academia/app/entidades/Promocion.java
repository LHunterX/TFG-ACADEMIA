package com.academia.app.entidades;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Entity
public class Promocion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne()
    private Curso curso;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date inicio;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date fin;

    @OneToMany(mappedBy = "promocion", cascade = CascadeType.ALL)
    private List<AsignaturaPromocion> asignaturas;

    public Promocion() {
    }

    public Promocion(Curso curso, Date inicio, Date fin) {
        this.curso = curso;
        this.inicio = inicio;
        this.fin = fin;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    public Date getInicio() {
        return inicio;
    }

    public void setInicio(Date inicio) {
        this.inicio = inicio;
    }

    public Date getFin() {
        return fin;
    }

    public void setFin(Date fin) {
        this.fin = fin;
    }

    public List<AsignaturaPromocion> getAsignaturas() {
        return asignaturas;
    }

    public void setAsignaturas(List<AsignaturaPromocion> asignaturas) {
        this.asignaturas = asignaturas;
    }
}
