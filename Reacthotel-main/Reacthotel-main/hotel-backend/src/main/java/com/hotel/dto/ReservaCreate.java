package com.hotel.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record ReservaCreate(
        @NotNull(message = "El cliente es obligatorio")
        Long clienteId,
        @NotNull(message = "La habitación es obligatoria")
        Long habitacionId,
        @NotNull(message = "La fecha de entrada es obligatoria")
        LocalDate fechaEntrada,
        @NotNull(message = "La fecha de salida es obligatoria")
        LocalDate fechaSalida,
        String observaciones) {
}
