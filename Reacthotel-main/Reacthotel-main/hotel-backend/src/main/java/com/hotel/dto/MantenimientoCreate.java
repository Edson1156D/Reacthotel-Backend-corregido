package com.hotel.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MantenimientoCreate(
        @NotNull(message = "La habitación es obligatoria")
        Long habitacionId,
        Long usuarioId,
        @NotBlank(message = "La descripción es obligatoria")
        String descripcion) {
}
