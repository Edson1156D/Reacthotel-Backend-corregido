package com.hotel.controller;

import com.hotel.dto.HabitacionCreate;
import com.hotel.dto.HabitacionResponse;
import com.hotel.dto.HabitacionUpdate;
import com.hotel.service.HabitacionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping({"/habitaciones", "/habitaciones/"})
@Tag(name = "Habitaciones")
public class HabitacionController {

    private final HabitacionService habitacionService;

    public HabitacionController(HabitacionService habitacionService) {
        this.habitacionService = habitacionService;
    }

    @GetMapping
    @Operation(summary = "Listar habitaciones",
            description = "Devuelve todas las habitaciones con el objeto tipo completo.")
    public ResponseEntity<List<HabitacionResponse>> listar() {
        return ResponseEntity.ok(habitacionService.listar());
    }

    @PostMapping
    @Operation(summary = "Crear habitación",
            description = "Crea una habitación. El estado predeterminado es LIBRE.")
    public ResponseEntity<HabitacionResponse> crear(
            @Valid @RequestBody HabitacionCreate request,
            UriComponentsBuilder uriBuilder) {
        HabitacionResponse response = habitacionService.crear(request);
        return ResponseEntity.created(uriBuilder
                .path("/habitaciones/{id}")
                .buildAndExpand(response.id())
                .toUri()).body(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar habitación",
            description = "Actualiza parcialmente una habitación; solo cambia los campos recibidos.")
    public ResponseEntity<HabitacionResponse> actualizar(
            @PathVariable Long id,
            @RequestBody HabitacionUpdate request) {
        return ResponseEntity.ok(habitacionService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar habitación",
            description = "Elimina una habitación si no tiene reservas ni mantenimientos.")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        habitacionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
