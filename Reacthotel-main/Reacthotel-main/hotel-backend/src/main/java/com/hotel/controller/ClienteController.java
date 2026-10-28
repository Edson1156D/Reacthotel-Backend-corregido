package com.hotel.controller;

import com.hotel.dto.ClienteRequest;
import com.hotel.dto.ClienteResponse;
import com.hotel.dto.ClienteUpdate;
import com.hotel.service.ClienteService;
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
@RequestMapping({"/clientes", "/clientes/"})
@Tag(name = "Clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    @Operation(summary = "Listar clientes", description = "Devuelve todos los clientes.")
    public ResponseEntity<List<ClienteResponse>> listar() {
        return ResponseEntity.ok(clienteService.listar());
    }

    @PostMapping
    @Operation(summary = "Crear cliente", description = "Crea un cliente con sus datos de contacto.")
    public ResponseEntity<ClienteResponse> crear(
            @Valid @RequestBody ClienteRequest request,
            UriComponentsBuilder uriBuilder) {
        ClienteResponse response = clienteService.crear(request);
        return ResponseEntity.created(uriBuilder
                .path("/clientes/{id}")
                .buildAndExpand(response.id())
                .toUri()).body(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar cliente", description = "Actualiza parcialmente los datos del cliente.")
    public ResponseEntity<ClienteResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ClienteUpdate request) {
        return ResponseEntity.ok(clienteService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar cliente", description = "Elimina un cliente sin reservas asociadas.")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        clienteService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
