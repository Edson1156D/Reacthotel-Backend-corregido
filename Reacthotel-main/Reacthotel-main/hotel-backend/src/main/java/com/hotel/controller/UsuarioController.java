package com.hotel.controller;

import com.hotel.dto.UsuarioCreate;
import com.hotel.dto.UsuarioResponse;
import com.hotel.dto.UsuarioUpdate;
import com.hotel.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping({"/usuarios", "/usuarios/"})
@Tag(name = "Usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    @Operation(summary = "Listar usuarios", description = "Devuelve todos los usuarios sin sus contraseñas.")
    public ResponseEntity<List<UsuarioResponse>> listar() {
        return ResponseEntity.ok(usuarioService.listar());
    }

    @PostMapping
    @Operation(summary = "Crear usuario", description = "Crea un usuario con contraseña protegida mediante BCrypt.")
    public ResponseEntity<UsuarioResponse> crear(
            @Valid @RequestBody UsuarioCreate request,
            UriComponentsBuilder uriBuilder) {
        UsuarioResponse response = usuarioService.crear(request);
        return ResponseEntity.created(uriBuilder
                .path("/usuarios/{id}")
                .buildAndExpand(response.id())
                .toUri()).body(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar usuario", description = "Actualiza parcialmente un usuario, sin modificar su contraseña.")
    public ResponseEntity<UsuarioResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody UsuarioUpdate request) {
        return ResponseEntity.ok(usuarioService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar usuario", description = "Elimina un usuario sin mantenimientos asignados.")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        usuarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
