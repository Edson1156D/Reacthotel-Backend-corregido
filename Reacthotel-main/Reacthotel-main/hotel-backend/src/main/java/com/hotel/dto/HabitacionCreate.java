package com.hotel.dto;

import com.hotel.entity.EstadoHabitacion;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record HabitacionCreate(
        @NotBlank(message = "El número no puede estar vacío")
        String numero,
        Integer piso,
        String descripcion,
        @NotNull(message = "El tipo de habitación es obligatorio")
        Long tipoId,
        EstadoHabitacion estado) {
}
