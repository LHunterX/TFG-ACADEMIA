package com.academia.app.repos;

import com.academia.app.entidades.AsignaturaPromocion;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AsignaturasPromocionRepo extends CrudRepository<AsignaturaPromocion, Long> {

    @Query("SELECT ap FROM AsignaturaPromocion ap WHERE ap.profesor.id = ?1 " +
            "AND CURRENT DATE BETWEEN ap.promocion.inicio AND ap.promocion.fin")
    Iterable<AsignaturaPromocion> findAllByProfesorIdActuales(Long profesorId);

    @Query("SELECT ap FROM AsignaturaPromocion ap WHERE ap.profesor.id = ?1 " +
            "AND CURRENT DATE NOT BETWEEN ap.promocion.inicio AND ap.promocion.fin")
    Iterable<AsignaturaPromocion> findAllByProfesorId(Long profesorId);

    @Query("SELECT ap FROM AsignaturaPromocion ap JOIN ap.alumnos a " +
            " WHERE a.id = ?1 AND CURRENT DATE BETWEEN ap.promocion.inicio AND ap.promocion.fin")
    Iterable<AsignaturaPromocion> findAllByAlumnoIdActuales(Long alumnoId);

    @Query("SELECT ap FROM AsignaturaPromocion ap JOIN ap.alumnos a " +
            " WHERE a.id = ?1 AND CURRENT DATE NOT BETWEEN ap.promocion.inicio AND ap.promocion.fin")
    Iterable<AsignaturaPromocion> findAllByAlumnoId(Long alumnoId);

}
