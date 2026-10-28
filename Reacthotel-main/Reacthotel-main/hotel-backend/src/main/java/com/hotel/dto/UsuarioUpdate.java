package com.hotel.dto;

import com.hotel.entity.Rol;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

public record UsuarioUpdate(
        @Pattern(regexp = ".*\\S.*", message = "El nombre no puede estar vacío")
        String nombre,
        @Email(message = "El email no tiene un formato válido")
        String email,
        Rol rol,
        Boolean activo) {
}
