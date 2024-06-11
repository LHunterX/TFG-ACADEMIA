package com.academia.app.repos;

import com.academia.app.entidades.Asignatura;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AsignaturasRepo extends CrudRepository<Asignatura, Long> {

}
