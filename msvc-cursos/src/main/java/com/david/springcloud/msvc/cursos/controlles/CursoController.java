package com.david.springcloud.msvc.cursos.controlles;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.david.springcloud.msvc.cursos.models.Usuario;
import com.david.springcloud.msvc.cursos.models.entity.Curso;
import com.david.springcloud.msvc.cursos.services.CursosService;

import jakarta.validation.Valid;

@RestController()
public class CursoController {

	@Autowired
	private CursosService cs;

	@GetMapping()
	public List<Curso> listar() {
		return cs.listar();
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> detalle(@PathVariable(name = "id") Long id) {
		Optional<Curso> curOptional = cs.porIdConUsuarios(id); //cs.porId(id);
		return curOptional.isPresent() ? ResponseEntity.ok(curOptional.get()) : ResponseEntity.notFound().build();
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> crear(@Valid @RequestBody Curso curso, BindingResult result) {
		Map<String, String> errores = obtenerErrores(result);
		ResponseEntity<?> res = null;
		;
		if (result.hasErrors()) {
			res = res.status(HttpStatus.BAD_REQUEST).body(errores);
		} else {
			res = res.status(HttpStatus.CREATED).body(cs.guardar(curso));
		}
		return res;
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> editar(@Valid @RequestBody Curso usuario, BindingResult result, @PathVariable Long id) {
		Optional<Curso> curOptional = cs.porId(id);
		Curso cur = null;
		ResponseEntity<?> res = null;
		Map<String, String> errores = obtenerErrores(result);

		if (result.hasErrors()) {
			res = res.status(HttpStatus.BAD_REQUEST).body(errores);
		} else if (curOptional.isPresent()) {
			cur = curOptional.get();
			cur.setNombre(usuario.getNombre());
			res = res = ResponseEntity.status(HttpStatus.CREATED).body(cs.guardar(cur));
		}
		return res;
	}

	@DeleteMapping({ "{id}" })
	public ResponseEntity<?> eliminar(@PathVariable Long id) {
		Optional<Curso> o = cs.porId(id);
		ResponseEntity<?> res = ResponseEntity.status(HttpStatus.NOT_FOUND).build();

		if (o.isPresent()) {
			cs.eliminar(id);
			res = res.noContent().build();
		}

		return res;
	}
	@DeleteMapping("/eliminar-curso-usuario/{id}")
	public ResponseEntity<?> eliminarCursoUsuarioPorId(@PathVariable Long id) {
		cs.eliminarCursoUsuarioPorId(id);
		return ResponseEntity.noContent().build();
	}
	

	@PutMapping("/asignar-usuario/{cursoId}")
	public ResponseEntity<?> asignarUsuario(@RequestBody Usuario usuario, @PathVariable Long cursoId) {
		Optional<Usuario> o = null;
		try {
			o = cs.asignarUsuario(usuario, cursoId);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("mensaje",
					"No existe  el usuario por el id  o error en la comunicación: " + e.getMessage()));
		}
		if (o.isPresent()) {
			return ResponseEntity.status(HttpStatus.CREATED).body(o.get());
		}
		return ResponseEntity.notFound().build();
	}
	
	@PostMapping("/crear-usuario/{cursoId}")
	public ResponseEntity<?> crearUsuario(@RequestBody Usuario usuario, @PathVariable Long cursoId) {
		Optional<Usuario> o = null;
		try {
			o = cs.crearUsuario(usuario, cursoId);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("mensaje",
					"No se pudo crear el usuario o error en la comunicación: " + e.getMessage()));
		}
		if (o.isPresent()) {
			return ResponseEntity.status(HttpStatus.CREATED).body(o.get());
		}
		return ResponseEntity.notFound().build();
	}
	
	@DeleteMapping("/eliminar-usuario/{cursoId}")
	public ResponseEntity<?> eliminarUsuario(@RequestBody Usuario usuario, @PathVariable Long cursoId) {
		Optional<Usuario> o = null;
		try {
			o = cs.eliminarUsuario(usuario, cursoId);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("mensaje",
					"No se pudo eliminar el usuario o error en la comunicación: " + e.getMessage()));
		}
		if (o.isPresent()) {
			return ResponseEntity.status(HttpStatus.CREATED).body(o.get());
		}
		return ResponseEntity.notFound().build();
	}

	private Map<String, String> obtenerErrores(BindingResult result) {
		Map<String, String> errores = new HashMap<>();
		if (result.hasErrors()) {
			result.getFieldErrors().forEach(err -> {
				errores.put(err.getField(), "El campo " + err.getField() + " " + err.getDefaultMessage());
			});
		}
		return errores;
	}
}
