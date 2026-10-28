package com.hotel.controller;

import com.hotel.dto.ReservaCreate;
import com.hotel.dto.ReservaResponse;
import com.hotel.dto.ReservaUpdate;
import com.hotel.service.ReservaService;
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
@RequestMapping({"/reservas", "/reservas/"})
@Tag(name = "Reservas")
public class ReservaController {

    private final ReservaService reservaService;

    public ReservaController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    @GetMapping
    @Operation(summary = "Listar reservas",
            description = "Devuelve todas las reservas con cliente y habitación como IDs.")
    public ResponseEntity<List<ReservaResponse>> listar() {
        return ResponseEntity.ok(reservaService.listar());
    }

    @PostMapping
    @Operation(summary = "Crear reserva",
            description = "Crea una reserva con estado inicial PENDIENTE.")
    public ResponseEntity<ReservaResponse> crear(
            @Valid @RequestBody ReservaCreate request,
            UriComponentsBuilder uriBuilder) {
        ReservaResponse response = reservaService.crear(request);
        return ResponseEntity.created(uriBuilder
                .path("/reservas/{id}")
                .buildAndExpand(response.id())
                .toUri()).body(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar reserva",
            description = "Actualiza parcialmente una reserva sin cambiar su cliente.")
    public ResponseEntity<ReservaResponse> actualizar(
            @PathVariable Long id,
            @RequestBody ReservaUpdate request) {
        return ResponseEntity.ok(reservaService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar reserva",
            description = "Elimina una reserva si no tiene facturas asociadas.")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        reservaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
