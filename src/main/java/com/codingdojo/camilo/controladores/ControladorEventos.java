package com.codingdojo.camilo.controladores;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.codingdojo.camilo.modelos.Estado;
import com.codingdojo.camilo.modelos.Evento;
import com.codingdojo.camilo.modelos.Mensaje;
import com.codingdojo.camilo.modelos.Usuario;
import com.codingdojo.camilo.servicios.Servicios;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class ControladorEventos {

	@Autowired
	private Servicios servicios;
	
	@GetMapping("/dashboard")
	public String dashboard(HttpSession session,
							Model model,
							@ModelAttribute("nuevoEvento")Evento nuevoEvento) {
		//revisamos que el usuario haya iniciado sesion
		Usuario usuarioTemporal = (Usuario)session.getAttribute("usuarioEnSesion");
		if(usuarioTemporal == null) {
			return "redirect:/";
		}
		//Servicio, una funcion que me regrese la lista de eventos en mi estado
		String miEstado = usuarioTemporal.getEstado(); //obtenemos el estado del usuario en sesion
		
		List<Evento> eventosMiEstado = servicios.eventosEnMiEstado(miEstado);
		model.addAttribute("eventosMiEstado", eventosMiEstado);
		
		List<Evento> eventosOtroEstado = servicios.eventosOtroEstado(miEstado);
		model.addAttribute("eventosOtroEstado", eventosOtroEstado);
		
		Usuario miUsuario = servicios.encontrarUsuario(usuarioTemporal.getId());
		model.addAttribute("usuario",miUsuario);
		
		//Enviamos la lista de estados disponible
		model.addAttribute("estados", Estado.Estados);
		
		return "dashboard.jsp";
	}
	
	@PostMapping("/crear")
	public String crear(HttpSession session,
					   @Valid @ModelAttribute("nuevoEvento")Evento nuevoEvento,
					   BindingResult result,
					   Model model) {
		//REVISAMOS SESION
		Usuario usuarioTemporal = (Usuario)session.getAttribute("usuarioEnSesion");
		if(usuarioTemporal == null) {
			return "redirect:/";
		}
		//REVISAMOS SESION DENUEVO
		if(result.hasErrors()) {
			model.addAttribute("estados", Estado.Estados);
			return "dashboard.jsp";
		}else {
			servicios.guardarEvento(nuevoEvento);
			return "redirect:/dashboard";
		}
	}
	
	@GetMapping("/unir/{id}")
	public String unir(@PathVariable("id")Long eventoId,
					   HttpSession session) {
		Usuario usuarioTemporal = (Usuario)session.getAttribute("usuarioEnSesion");
		if(usuarioTemporal == null) {
			return "redirect:/";
		}
		
		servicios.unirEvento(usuarioTemporal.getId(), eventoId);
		return "redirect:/dashboard";
	}
	
	@GetMapping("/quitar/{id}")
	public String quitar(@PathVariable("id") Long eventoId,
						 HttpSession session) {
		Usuario usuarioTemporal = (Usuario)session.getAttribute("usuarioEnSesion");
		if(usuarioTemporal == null) {
			return "redirect:/";
		}
		servicios.quitarEvento(usuarioTemporal.getId(), eventoId);
		return "redirect:/dashboard";
	}
	
	@GetMapping("/evento/{id}")
	public String evento(@PathVariable("id")Long eventoId,
					 	 HttpSession session,
					 	 Model model, 
					 	 @ModelAttribute("mensaje")Mensaje mensaje) {
		Usuario usuarioTemporal = (Usuario)session.getAttribute("usuarioEnSesion");
		if(usuarioTemporal == null) {
			return "redirect:/";
		}
		
		Evento evento = servicios.encontrarEvento(eventoId);
		model.addAttribute("evento",evento);
		
		return "evento.jsp";
	}
	
	@PostMapping("/crearmensaje")
	public String crearmensaje(@Valid @ModelAttribute("mensaje")Mensaje mensaje,
									  BindingResult result,
									  HttpSession session,
									  Model model) {
		Usuario usuarioTemporal = (Usuario)session.getAttribute("usuarioEnSesion");
		if(usuarioTemporal == null) {
			return "redirect:/";
		}
		if(result.hasErrors()){
			model.addAttribute("evento",mensaje.getEvento());
			return "evento.jsp";
		}else {
			servicios.guardarMensaje(mensaje);
			return "redirect:/evento/"+mensaje.getEvento().getId();
		}
	}
}
