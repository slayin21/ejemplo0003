package com.example.demo;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Equipo;
import com.example.demo.model.Jugador;
import com.example.demo.model.Liga;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name="DemoRestService", description="API de la Liga")
public class DemoRestService {
	@Autowired
	private Liga liga;
	
	@Operation(summary = "Equipos de la liga", description = "Lista de equipos de la liga", 
			tags = {"DemoRestService"})
	@GetMapping("/equipos")
	public @ResponseBody ResponseEntity<Collection<Equipo>> getEquipos() {
		Collection<Equipo> equipos = liga.getEquipos().values();
		return ResponseEntity.ok(equipos);
	}
	
	@Operation(summary = "Info del equipo", description = "Información de Equipo", 
			tags = {"DemoRestService"})
	@GetMapping("/equipos/{id}")
	public @ResponseBody ResponseEntity<Equipo> getEquipo(
			@Parameter(description = "id del equipo", required = true, example = "E1", in = ParameterIn.PATH)
				@PathVariable(value = "id") String id
			) {
		Equipo equipo = liga.getEquipos().get(id);
		if (equipo == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(equipo);
	}

	@Operation(summary = "Actualiza equipo", description = "Actualiza la información un equipo existente", tags = {"DemoRestService"})
	@PutMapping("/equipos/{id}")
	public @ResponseBody ResponseEntity<Equipo> updateEquipo(
			@Parameter(description = "id del equipo", required = true, example = "E1", in = ParameterIn.PATH)
				@PathVariable(value = "id") String id,
				@RequestBody Equipo equipo
			) {
		if (liga.getEquipos().get(id) == null) {
			return ResponseEntity.notFound().build();
		}
		Equipo eq = liga.getEquipos().put(id, equipo);
		return ResponseEntity.ok(eq); // devuelve la versión antigua del equipo
	}
	
	@Operation(summary = "Añadir jugador", description = "Añadir un jugador existente a un equipo", tags = {"DemoRestService"})
	@PutMapping("/equipos/{id}/{jugadorId}")
	public @ResponseBody ResponseEntity addJugadorAEquipo(
			@Parameter(description = "id del equipo", required = true, example = "E1", in = ParameterIn.PATH)
				@PathVariable(value = "id") String equipoId,
			@Parameter(description = "id del jugador", required = true, example = "J01", in = ParameterIn.PATH)
				@PathVariable(value = "jugadorId") String jugadorId
			) {
		Equipo eq = liga.getEquipos().get(equipoId);
		if (eq == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El equipo " + equipoId + " no existe");
		}		
		Jugador jg = liga.getJugadores().get(jugadorId);
		if (jg == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El jugador " + jugadorId + " no existe");
		}
		if (eq.getJugadores() != null && eq.getJugadores().containsKey(jg.getId())) {
			return ResponseEntity.badRequest().body("El jugador " + jugadorId + " ya está en el equipo " + equipoId);
		}
		eq.addJugador(jg);
		return ResponseEntity.ok(eq);
	}

	@Operation(summary = "Crear equipo", description = "Crear un nuevo equipo", tags = {"DemoRestService"})
	@PostMapping("/equipos")
	public @ResponseBody ResponseEntity addEquipo(
			@Parameter(description = "Id del equipo", required = true, example = "E1", in = ParameterIn.QUERY) 
				@RequestParam(name = "id") String id,
			@Parameter(description = "Nombre del equipo", required = true, example = "Rojo", 
					in = ParameterIn.QUERY)
				@RequestParam(name = "nombre") String nombre
			) {
		if (liga.getEquipos().get(id) == null) {
			Equipo eq = new Equipo(id, nombre);
			liga.getEquipos().put(eq.getId(), eq);
		} else {
			return ResponseEntity.badRequest().body("El equipo " + id + " ya existe");
		}
		Equipo equipoCreado = liga.getEquipos().get(id);
		if (equipoCreado == null) {
			return ResponseEntity.internalServerError().body("No se ha podido crear el equipo " + id);
		} else {
			return ResponseEntity.ok(equipoCreado);
		}
	}
	
	@Operation(summary = "Suprimir equipo", description = "Suprimir un equipo", tags = {"DemoRestService"})
	@DeleteMapping("/equipos/{id}")
	public @ResponseBody ResponseEntity<Equipo> delEquipo(
			@Parameter(description = "id del equipo", required = true, example = "E1", in = ParameterIn.PATH)
			@PathVariable(value = "id") String id
			) {
		Equipo borrado = liga.getEquipos().remove(id);
		if (borrado == null) {
			return ResponseEntity.notFound().build();
		} else {
			return ResponseEntity.ok(borrado);
		}
	}
}
