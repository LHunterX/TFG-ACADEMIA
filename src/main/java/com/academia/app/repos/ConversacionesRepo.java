package com.academia.app.repos;

import com.academia.app.entidades.Asignatura;
import com.academia.app.entidades.Conversacion;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ConversacionesRepo extends CrudRepository<Conversacion, Long> {

    @Query("SELECT c FROM Conversacion c WHERE c.usuario1.id = ?1 OR c.usuario2.id = ?1")
    Iterable<Conversacion> findAllByUsuario(Long longs);

}
