package com.david.springcloud.msvc.cursos.models.entity;

import java.util.ArrayList;
import java.util.List;

import com.david.springcloud.msvc.cursos.models.Usuario;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotEmpty;

@Entity
@Table(name = "cursos")
public class Curso {

	// Atributos
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotEmpty
	@Column(name = "nombre")
	private String nombre;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "curso_id")
	private List<CursoUsuario> cursoUsuarios;

	// Indica que el atributo no esta mapeada a una tabla en la persistencia
	@Transient
	private List<Usuario> usuarios;

	// Constructor
	public Curso() {
		cursoUsuarios = new ArrayList<>();
		usuarios = new ArrayList<>();
	}

	// Getters y Setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public List<CursoUsuario> getCursoUsuarios() {
		return cursoUsuarios;
	}

	public void setCursoUsuarios(List<CursoUsuario> cursoUsuarios) {
		this.cursoUsuarios = cursoUsuarios;
	}

	public List<Usuario> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

	// Métodos
	/**
	 * Agrega un nuevo usuario al curso
	 * 
	 * @param cs
	 */
	public void addCursoUsuario(CursoUsuario cs) {
		cursoUsuarios.add(cs);
	}

	public void removeCursoUsuario(CursoUsuario cs) {
		cursoUsuarios.remove(cs);
	}

}
