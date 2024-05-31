package com.david.springcloud.msvc.cursos.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.david.springcloud.msvc.cursos.clients.UsuarioClientRest;
import com.david.springcloud.msvc.cursos.models.Usuario;
import com.david.springcloud.msvc.cursos.models.entity.Curso;
import com.david.springcloud.msvc.cursos.models.entity.CursoUsuario;
import com.david.springcloud.msvc.cursos.repositories.CursoRepository;

@Service
public class CursoServiceImpl implements CursosService {

	@Autowired
	private CursoRepository cr;

	@Autowired
	private UsuarioClientRest ucr;

	@Override
	@Transactional(readOnly = true)
	public List<Curso> listar() {
		return (List<Curso>) cr.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<Curso> porId(Long id) {
		return cr.findById(id);
	}

	@Override
	@Transactional
	public Curso guardar(Curso curso) {
		return cr.save(curso);
	}

	@Override
	@Transactional
	public void eliminar(Long id) {
		cr.deleteById(id);

	}

	@Override
	@Transactional
	public Optional<Usuario> asignarUsuario(Usuario usuario, Long cursoId) {
		Optional<Curso> o = cr.findById(cursoId);

		if (o.isPresent()) {
			Usuario usuarioMsvc = ucr.detalle(usuario.getId());
			Curso curso = o.get();
			CursoUsuario cursoUsuario = new CursoUsuario();
			cursoUsuario.setUsuarioId(usuarioMsvc.getId());
			curso.addCursoUsuario(cursoUsuario);
			cr.save(curso);
			return Optional.of(usuarioMsvc);
		}

		return Optional.empty();
	}

	@Override
	@Transactional
	public Optional<Usuario> crearUsuario(Usuario usuario, Long cursoId) {
		Optional<Curso> o = cr.findById(cursoId);

		if (o.isPresent()) {
			Usuario usuarioNuevoMsvc = ucr.crear(usuario);
			Curso curso = o.get();
			CursoUsuario cursoUsuario = new CursoUsuario();
			cursoUsuario.setUsuarioId(usuarioNuevoMsvc.getId());
			curso.addCursoUsuario(cursoUsuario);
			cr.save(curso);
			return Optional.of(usuarioNuevoMsvc);
		}

		return Optional.empty();
	}

	@Override
	@Transactional
	public Optional<Usuario> eliminarUsuario(Usuario usuario, Long cursoId) {
		Optional<Curso> o = cr.findById(cursoId);

		if (o.isPresent()) {
			Usuario usuarioMsvc = ucr.detalle(usuario.getId());
			Curso curso = o.get();
			CursoUsuario cursoUsuario = new CursoUsuario();
			cursoUsuario.setUsuarioId(usuarioMsvc.getId());
			
			curso.removeCursoUsuario(cursoUsuario);
			cr.save(curso);
			return Optional.of(usuarioMsvc);
		}

		return Optional.empty();
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<Curso> porIdConUsuarios(Long id) {
		Optional<Curso> o = cr.findById(id);
		if (o.isPresent()) {
			Curso curso = o.get();
			if (!curso.getCursoUsuarios().isEmpty()) {
				List<Long> ids = curso.getCursoUsuarios().stream().map(CursoUsuario::getUsuarioId).toList();
				List<Usuario> usuarios = ucr.obtenerAlumnosPorCurso(ids);
				curso.setUsuarios(usuarios);
			}
			return Optional.of(curso);
		}
		return Optional.empty();
	}

	@Transactional
	@Override
	public void eliminarCursoUsuarioPorId(Long id) {
		cr.eliminarCursoUsuarioPorId(id);
	}

}
