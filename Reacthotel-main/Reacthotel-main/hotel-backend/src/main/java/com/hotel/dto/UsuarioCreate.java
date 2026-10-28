package com.hotel.dto;

import com.hotel.entity.Rol;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UsuarioCreate(
        @NotBlank(message = "El nombre no puede estar vacío")
        String nombre,
        @NotBlank(message = "El email no puede estar vacío")
        @Email(message = "El email no tiene un formato válido")
        String email,
        @NotBlank(message = "La contraseña no puede estar vacía")
        String password,
        Rol rol,
        Boolean activo) {
}
