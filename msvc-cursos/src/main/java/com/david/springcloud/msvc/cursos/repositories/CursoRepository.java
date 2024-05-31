package com.david.springcloud.msvc.cursos.repositories;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.david.springcloud.msvc.cursos.models.entity.Curso;

@Repository
public interface CursoRepository extends CrudRepository<Curso, Long>{
	
	@Modifying
	@Query("delete from CursoUsuario cu where cu.usuarioId = ?1")
	void eliminarCursoUsuarioPorId(Long Id);

}
