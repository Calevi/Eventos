package com.codingdojo.camilo.servicios;

import java.util.List;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import com.codingdojo.camilo.modelos.Evento;
import com.codingdojo.camilo.modelos.Mensaje;
import com.codingdojo.camilo.modelos.Usuario;
import com.codingdojo.camilo.repositorios.RepositorioEventos;
import com.codingdojo.camilo.repositorios.RepositorioMensajes;
import com.codingdojo.camilo.repositorios.RepositorioUsuarios;

@Service
public class Servicios {

	@Autowired 
	private RepositorioUsuarios ru;
	
	@Autowired
	private RepositorioEventos re;
	
	@Autowired
	private RepositorioMensajes rm;
	
	public Usuario registrar(Usuario nuevoUsuario,BindingResult result) {
		//comparamos contrasenias
		String password = nuevoUsuario.getPassword();
		String confirmacion = nuevoUsuario.getConfirmacion();
		if(!password.equals(confirmacion)) {
			result.rejectValue("confirmacion","Matches","Las password no coinciden");
		}
		
		//Revisamos si el email que recibimos NO exista en mi BD
		String email = nuevoUsuario.getEmail();
		Usuario existeUsuario = ru.findByEmail(email);
		if(existeUsuario!=null) {
			//el correo ya esta registrado
			result.rejectValue("email","Unique", "El correo ingresado ya se encuentra registrado");
		}
		
		//Si existe error, entonces regresamos null
		if(result.hasErrors()) {
			return null;
		} else {
			//Si no existe ningun error, guardamos.
			//Encriptamos contrasenia
			String contra_encriptada = BCrypt.hashpw(password, BCrypt.gensalt());
			nuevoUsuario.setPassword(contra_encriptada);
			return ru.save(nuevoUsuario);
		}
}

	public Usuario login(String email, String password) {
		//Revisamos que el correo que recibimos este en BD
		Usuario usuarioInicioSesion = ru.findByEmail(email);//Regresa el objeto usuario o nulo si no existe
		if(usuarioInicioSesion == null) {
			return null;
		}
		if(BCrypt.checkpw(password, usuarioInicioSesion.getPassword())) {
			return usuarioInicioSesion;
		} else {
			return null;
		}
		
	}
	
	public Evento guardarEvento(Evento nuevoEvento) {
		return re.save(nuevoEvento);
	}
	
	public Usuario encontrarUsuario(Long id) {
		return ru.findById(id).orElse(null);
	}
	public List<Evento> eventosEnMiEstado(String estado){
		return re.findByEstado(estado);
	}
	public List<Evento> eventosOtroEstado(String estado){
		return re.findByEstadoIsNot(estado);
	}
	public Evento encontrarEvento(Long id) {
		return re.findById(id).orElse(null);
	}
	
	public void unirEvento(Long usuarioId, Long eventoId) {
		Usuario miUsuario = encontrarUsuario(usuarioId);
		Evento miEvento = encontrarEvento(eventoId);
		
		miUsuario.getEventosAsistidos().add(miEvento);
		ru.save(miUsuario);
	}
	
	public void quitarEvento(Long usuarioId, Long eventoId) {
		Usuario miUsuario = encontrarUsuario(usuarioId);
		Evento miEvento = encontrarEvento(eventoId);
		
		miUsuario.getEventosAsistidos().remove(miEvento);
		ru.save(miUsuario);
	}
	
	public Mensaje guardarMensaje(Mensaje nuevoMensaje) {
		return rm.save(nuevoMensaje);
	}
}
