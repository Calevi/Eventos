package com.codingdojo.camilo.repositorios;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.codingdojo.camilo.modelos.Mensaje;

@Repository
public interface RepositorioMensajes extends CrudRepository<Mensaje,Long>  {

}
