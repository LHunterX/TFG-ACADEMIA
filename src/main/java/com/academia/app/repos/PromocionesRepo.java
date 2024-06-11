package com.academia.app.repos;

import com.academia.app.entidades.Curso;
import com.academia.app.entidades.Promocion;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.lang.annotation.Native;
import java.util.Optional;

public interface PromocionesRepo extends CrudRepository<Promocion, Long> {

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "DELETE FROM promocion WHERE id = ?1")
    public void removeById(Long id);

    @Query("SELECT p FROM Promocion p " +
            "LEFT JOIN AsignaturaPromocion ap ON ap.promocion.id = p.id " +
            "LEFT JOIN ap.alumnos aa " +
            "WHERE aa.id = ?1 AND CURRENT_DATE NOT BETWEEN p.inicio AND p.fin")
    Iterable<Promocion> findByAlumnoId(Long alumnoId);

}
