package com.david.springcloud.msvc.usuarios.controllers;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.david.springcloud.msvc.usuarios.models.entity.Usuario;
import com.david.springcloud.msvc.usuarios.services.UsuarioService;

import jakarta.validation.Valid;


@RestController()
public class UsuarioController {
	
	@Autowired
	private UsuarioService us;
	
	@GetMapping()
	public List<Usuario> listar(){
		return us.listar();
	}
	
	@GetMapping("/{id}")
	public  ResponseEntity<?> detalle(@PathVariable(name="id") Long id) {
		Optional<Usuario> usuOptional = us.porId(id);
		return usuOptional.isPresent() ? ResponseEntity.ok(usuOptional.get()) : ResponseEntity.notFound().build();
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> crear(@Valid @RequestBody Usuario usuario, BindingResult result) {
		Map<String, String> errores = obtenerErrores(result);
		ResponseEntity<?> res = null;
		if (us.porEmail(usuario.getEmail()).isPresent()) {
			return res.badRequest().body(Collections.singletonMap("mensaje", "Ya existe un usuario con ese correo"));
		} else if (result.hasErrors()) {
			res = res.status(HttpStatus.BAD_REQUEST).body(errores);
		}else {
			res = res.status(HttpStatus.CREATED).body(us.guardar(usuario));
		}
		return  res;
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<?> editar (@Valid @RequestBody Usuario usuario, BindingResult result, @PathVariable Long id){
		Optional<Usuario> usuOptional = us.porId(id);
		Usuario usu = null;
		ResponseEntity<?> res = null;
		Map<String, String> errores = obtenerErrores(result);
		if (result.hasErrors()) {
			res = res.status(HttpStatus.BAD_REQUEST).body(errores);
		} else if (usuOptional.isPresent()) {
			usu = usuOptional.get();
			usu.setNombre(usuario.getNombre());
			usu.setEmail(usuario.getEmail());
			usu.setPassword(usuario.getPassword());
			res = ResponseEntity.status(HttpStatus.CREATED).body(us.guardar(usu));
		}
		return res;
	}
	
	@DeleteMapping({"{id}"})
	public ResponseEntity<?> eliminar(@PathVariable Long id){
		Optional<Usuario> o = us.porId(id);
		ResponseEntity<?> res = ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		
		if (o.isPresent()) {
			us.eliminar(id);
			res = res.noContent().build();
		}
		
		return res;
	}
	
	@GetMapping("/usuarios-por-curso")
	public ResponseEntity<?> obtenerAlumnosPorCurso(@RequestParam List<Long> ids){
		return ResponseEntity.ok(us.listarPorIds(ids));
	}
	
	private Map<String, String> obtenerErrores(BindingResult result){
		Map<String, String> errores = new HashMap<>(); 
		if (result.hasErrors()) {
			result.getFieldErrors().forEach(err -> {
				errores.put(err.getField(), "El campo " + err.getField() + " " + err.getDefaultMessage());
			});
		}
		return errores;
	}
}
