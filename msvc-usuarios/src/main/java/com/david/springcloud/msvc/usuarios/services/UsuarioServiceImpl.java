package com.david.springcloud.msvc.usuarios.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.david.springcloud.msvc.usuarios.clients.CursoClienteRest;
import com.david.springcloud.msvc.usuarios.models.entity.Usuario;
import com.david.springcloud.msvc.usuarios.repositories.UsuarioRepository;

@Service
public class UsuarioServiceImpl implements UsuarioService {
	
	/***
	 * NOTAS:
	 * 1. Todos los metodos del services deben tener el @Transactional, le agrega el begin, el rollback, el comit.
	 */
	
	@Autowired
	private UsuarioRepository re;
	
	@Autowired
	private CursoClienteRest client;

	
	@Override
	@Transactional(readOnly = true)
	public List<Usuario> listar() {
		return (List<Usuario>) re.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<Usuario> porId(Long id) {
		return re.findById(id);
	}

	@Override
	@Transactional
	public Usuario guardar(Usuario usuario) {
		return re.save(usuario);
	}

	@Override
	@Transactional
	public void eliminar(Long id) {
		re.deleteById(id);
		client.eliminarCursoUsuarioPorId(id);
		
	}

	@Override
	public Optional<Usuario> porEmail(String email) {
		return re.findByEmail(email);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Usuario> listarPorIds(Iterable<Long> ids) {
		return (List<Usuario>) re.findAllById(ids);
	}

}
