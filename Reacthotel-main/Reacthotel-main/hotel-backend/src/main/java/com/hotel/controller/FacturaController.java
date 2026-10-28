package com.hotel.controller;

import com.hotel.dto.FacturaCreate;
import com.hotel.dto.FacturaResponse;
import com.hotel.dto.FacturaUpdate;
import com.hotel.service.FacturaService;
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
@RequestMapping({"/facturas", "/facturas/"})
@Tag(name = "Facturas")
public class FacturaController {

    private final FacturaService facturaService;

    public FacturaController(FacturaService facturaService) {
        this.facturaService = facturaService;
    }

    @GetMapping
    @Operation(summary = "Listar facturas",
            description = "Devuelve todas las facturas.")
    public ResponseEntity<List<FacturaResponse>> listar() {
        return ResponseEntity.ok(facturaService.listar());
    }

    @PostMapping
    @Operation(summary = "Crear factura",
            description = "Crea una factura pendiente para una reserva confirmada y calcula el total.")
    public ResponseEntity<FacturaResponse> crear(
            @Valid @RequestBody FacturaCreate request,
            UriComponentsBuilder uriBuilder) {
        FacturaResponse response = facturaService.crear(request);
        return ResponseEntity.created(uriBuilder
                .path("/facturas/{id}")
                .buildAndExpand(response.id())
                .toUri()).body(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar factura",
            description = "Cambia únicamente el estado de la factura.")
    public ResponseEntity<FacturaResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody FacturaUpdate request) {
        return ResponseEntity.ok(facturaService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar factura",
            description = "Elimina físicamente una factura.")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        facturaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
