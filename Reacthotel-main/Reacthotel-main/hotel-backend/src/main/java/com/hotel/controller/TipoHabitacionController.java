package com.hotel.controller;

import com.hotel.dto.TipoHabitacionRequest;
import com.hotel.dto.TipoHabitacionResponse;
import com.hotel.dto.TipoHabitacionUpdate;
import com.hotel.service.TipoHabitacionService;
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
@RequestMapping({"/tipos-habitacion", "/tipos-habitacion/"})
@Tag(name = "TiposHabitacion")
public class TipoHabitacionController {

    private final TipoHabitacionService tipoHabitacionService;

    public TipoHabitacionController(TipoHabitacionService tipoHabitacionService) {
        this.tipoHabitacionService = tipoHabitacionService;
    }

    @GetMapping
    @Operation(summary = "Listar tipos de habitación",
            description = "Devuelve todos los tipos de habitación.")
    public ResponseEntity<List<TipoHabitacionResponse>> listar() {
        return ResponseEntity.ok(tipoHabitacionService.listar());
    }

    @PostMapping
    @Operation(summary = "Crear tipo de habitación",
            description = "Crea un tipo de habitación con sus características.")
    public ResponseEntity<TipoHabitacionResponse> crear(
            @Valid @RequestBody TipoHabitacionRequest request,
            UriComponentsBuilder uriBuilder) {
        TipoHabitacionResponse response = tipoHabitacionService.crear(request);
        return ResponseEntity.created(uriBuilder
                .path("/tipos-habitacion/{id}")
                .buildAndExpand(response.id())
                .toUri()).body(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar tipo de habitación",
            description = "Actualiza parcialmente un tipo de habitación.")
    public ResponseEntity<TipoHabitacionResponse> actualizar(
            @PathVariable Long id,
            @RequestBody TipoHabitacionUpdate request) {
        return ResponseEntity.ok(tipoHabitacionService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar tipo de habitación",
            description = "Elimina un tipo de habitación si no tiene habitaciones asociadas.")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        tipoHabitacionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
