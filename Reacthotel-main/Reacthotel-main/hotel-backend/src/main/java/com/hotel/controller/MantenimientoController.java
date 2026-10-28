package com.hotel.controller;

import com.hotel.dto.MantenimientoCreate;
import com.hotel.dto.MantenimientoResponse;
import com.hotel.dto.MantenimientoUpdate;
import com.hotel.service.MantenimientoService;
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
@RequestMapping({"/mantenimientos", "/mantenimientos/"})
@Tag(name = "Mantenimientos")
public class MantenimientoController {

    private final MantenimientoService mantenimientoService;

    public MantenimientoController(MantenimientoService mantenimientoService) {
        this.mantenimientoService = mantenimientoService;
    }

    @GetMapping
    @Operation(summary = "Listar mantenimientos",
            description = "Devuelve todos los mantenimientos registrados.")
    public ResponseEntity<List<MantenimientoResponse>> listar() {
        return ResponseEntity.ok(mantenimientoService.listar());
    }

    @PostMapping
    @Operation(summary = "Crear mantenimiento",
            description = "Crea un mantenimiento pendiente para una habitación.")
    public ResponseEntity<MantenimientoResponse> crear(
            @Valid @RequestBody MantenimientoCreate request,
            UriComponentsBuilder uriBuilder) {
        MantenimientoResponse response = mantenimientoService.crear(request);
        return ResponseEntity.created(uriBuilder
                .path("/mantenimientos/{id}")
                .buildAndExpand(response.id())
                .toUri()).body(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar mantenimiento",
            description = "Actualiza parcialmente un mantenimiento sin cambiar su habitación.")
    public ResponseEntity<MantenimientoResponse> actualizar(
            @PathVariable Long id,
            @RequestBody MantenimientoUpdate request) {
        return ResponseEntity.ok(mantenimientoService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar mantenimiento",
            description = "Elimina un mantenimiento del registro.")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        mantenimientoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
