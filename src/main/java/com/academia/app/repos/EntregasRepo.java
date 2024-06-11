package com.academia.app.repos;

import com.academia.app.entidades.Entrega;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface EntregasRepo extends CrudRepository<Entrega, Long> {

    Entrega findByTareaIdAndAlumnoId(Long tareaId, Long alumnoId);

}
