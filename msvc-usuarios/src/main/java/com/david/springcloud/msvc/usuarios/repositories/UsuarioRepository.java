package com.david.springcloud.msvc.usuarios.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.david.springcloud.msvc.usuarios.models.entity.Usuario;

@Repository
public interface UsuarioRepository extends CrudRepository<Usuario, Long>{
	Optional<Usuario> findByEmail(String email);
}
