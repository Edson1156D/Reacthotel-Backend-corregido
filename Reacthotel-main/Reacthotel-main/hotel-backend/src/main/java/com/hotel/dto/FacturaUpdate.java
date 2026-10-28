package com.hotel.dto;

import com.hotel.entity.EstadoFactura;
import jakarta.validation.constraints.NotNull;

public record FacturaUpdate(
        @NotNull(message = "El estado es obligatorio")
        EstadoFactura estado) {
}
