package com.academia.app.repos;

import com.academia.app.entidades.Curso;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CursosRepo extends CrudRepository<Curso, Long> {
    Optional<Curso> findFirstByOrderByIdAsc();
}
