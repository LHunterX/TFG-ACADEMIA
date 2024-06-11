package com.academia.app.repos;

import com.academia.app.entidades.AsignaturaAlumno;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface AsignaturasAlumnoRepo extends CrudRepository<AsignaturaAlumno, Long> {

    @Query("SELECT aa FROM AsignaturaAlumno aa " +
            "LEFT JOIN AsignaturaPromocion ap ON aa.asignatura.id = ap.id " +
            "LEFT JOIN Promocion p ON p.id = ap.promocion.id " +
            "LEFT JOIN Asignatura a ON ap.asignatura.id = a.id " +
            "WHERE aa.alumno.id = ?1 " +
            "AND CURRENT DATE BETWEEN p.inicio AND p.fin")
    Iterable<AsignaturaAlumno> findByAlumnoIdCursoActual(Long alumnoId);

    @Query("SELECT aa FROM AsignaturaAlumno aa " +
            "LEFT JOIN AsignaturaPromocion ap ON aa.asignatura.id = ap.id " +
            "LEFT JOIN Promocion p ON p.id = ap.promocion.id " +
            "LEFT JOIN Asignatura a ON ap.asignatura.id = a.id " +
            "WHERE aa.alumno.id = ?1 AND p.id = ?2")
    Iterable<AsignaturaAlumno> findByAlumnoYPromocion(Long alumnoId, Long promocionId);

}
