package com.academia.app.repos;

import com.academia.app.entidades.Tarea;
import org.springframework.data.repository.CrudRepository;

public interface TareasRepo extends CrudRepository<Tarea, Long> {
}
