package com.academia.app.repos;

import com.academia.app.entidades.Usuario;
import com.academia.app.enums.Rol;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface UsuariosRepo extends CrudRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);

    List<Usuario> findByRol(Rol rol);

    List<Usuario> findByRolNot(Rol rol);

    List<Usuario> findByNameContaining(String name);

    List<Usuario> findByRolAndNameContaining(Rol rol, String name);
}
