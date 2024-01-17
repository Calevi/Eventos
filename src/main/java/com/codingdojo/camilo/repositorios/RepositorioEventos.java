package com.codingdojo.camilo.repositorios;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.codingdojo.camilo.modelos.Evento;

@Repository
public interface RepositorioEventos extends CrudRepository<Evento,Long> {

	//Es como hacer un query que diga SELECT * FROM eventos WHERE estado = <ESTADO RECIBIDO>
	List<Evento> findByEstado(String estado); //lista de eventos de MI estado
	//Es como hacer un query que diga SELECT * FROM eventos WHERE estado != <ESTADO RECIBIDO>
	List<Evento> findByEstadoIsNot(String estado); 
}
