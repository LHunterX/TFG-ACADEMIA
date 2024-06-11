package com.academia.app.repos;

import com.academia.app.entidades.Archivo;
import org.springframework.data.repository.CrudRepository;

public interface ArchivosRepo extends CrudRepository<Archivo, Long> {
}
