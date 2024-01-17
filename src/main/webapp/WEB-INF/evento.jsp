<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Evento</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">
</head>
<body>
	<div class="container">
		<div class="row">
			<!-- Detalles de mi evento -->
			<div class="col-6">
				<h1>${evento.nombre }</h1>
				<p>
					<b>Host:</b>${evento.host.nombre }
				</p>
				<p>
					<b>Fecha:</b>${evento.fecha }
				</p>
				<p>
					<b>Ubicacion:</b>${evento.ubicacion},${evento.estado }
				</p>
				<p>
					<b>Cantidad Asistentes:</b>${evento.asistentes.size() }
					<!-- size() = Tamanio total de mi lista. -->
				</p>
				<table class="table table-striped mt-3">
					<thead>
						<tr>
							<th>Nombre</th>
							<th>Ubicacion</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${evento.asistentes }" var="asistente">
							<tr>
								<td>${asistente.nombre }</td>
								<td>${asistente.ubicacion }</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
				<!-- Muro de mensajes y Form:form -->
				<div class="col-6">
					<h2>Muro de mensajes</h2>
					<div class="mb-3 border">
					 	<c:forEach items="${evento.mensajesEvento }" var="msg">
					 		<p>
					 			${msg.autor.nombre } dice: ${msg.cotenido }
					 		</p>
					 	</c:forEach>
					</div>
					<form:form action="/crearmensaje" method="post" modelAttribute="mensaje">
						<form:label path="cotenido">Agregar comentario:</form:label>
						<form:textarea path="cotenido" class="form-control"/>
						<form:hidden path="autor" value="${usuarioEnSesion.id }"/>
						<form:hidden path="evento" value="${evento.id }"/>
						<input type="submit" class="btn btn-primary mt-3" value="Enviar"/>
					</form:form>
				</div>
			</div>
		</div>
	</div>
</body>
</html>