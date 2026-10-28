package com.hotel.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record TipoHabitacionRequest(
        @NotBlank(message = "El nombre no puede estar vacío")
        String nombre,
        String descripcion,
        @NotNull(message = "El precio por noche es obligatorio")
        @DecimalMin(value = "0", message = "El precio por noche no puede ser negativo")
        BigDecimal precioNoche,
        @NotNull(message = "La capacidad es obligatoria")
        @Min(value = 1, message = "La capacidad debe ser mayor o igual a 1")
        Integer capacidad,
        String imagenUrl) {
}
