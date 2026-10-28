package com.hotel.dto;

import jakarta.validation.constraints.NotNull;

public record FacturaCreate(
        @NotNull(message = "La reserva es obligatoria")
        Long reservaId,
        String notas) {
}
