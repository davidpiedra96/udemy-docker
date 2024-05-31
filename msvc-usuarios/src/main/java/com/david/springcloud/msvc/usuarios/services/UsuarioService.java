package com.david.springcloud.msvc.usuarios.services;

import java.util.List;
import java.util.Optional;

import com.david.springcloud.msvc.usuarios.models.entity.Usuario;

public interface UsuarioService {
	List<Usuario> listar();
	
	Optional<Usuario> porId(Long id);
	
	Usuario guardar(Usuario usuario);
	
	void eliminar(Long id);
	
	Optional<Usuario> porEmail (String email);
	
	List<Usuario> listarPorIds(Iterable<Long> ids);
	
}
