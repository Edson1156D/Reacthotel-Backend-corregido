package com.hotel.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ClienteRequest(
        @NotBlank(message = "El nombre no puede estar vacío")
        String nombre,
        @NotBlank(message = "El apellido no puede estar vacío")
        String apellido,
        @NotBlank(message = "El DNI no puede estar vacío")
        String dni,
        @Email(message = "El email no tiene un formato válido")
        String email,
        String telefono,
        String direccion) {
}
