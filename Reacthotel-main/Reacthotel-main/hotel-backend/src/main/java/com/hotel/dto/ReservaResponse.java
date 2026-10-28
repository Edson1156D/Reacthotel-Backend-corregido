package com.hotel.dto;

import com.hotel.entity.EstadoReserva;
import com.hotel.entity.Reserva;

import java.time.LocalDate;

public record ReservaResponse(
        Long id,
        Long clienteId,
        Long habitacionId,
        LocalDate fechaEntrada,
        LocalDate fechaSalida,
        EstadoReserva estado,
        String observaciones) {

    public static ReservaResponse desde(Reserva reserva) {
        return new ReservaResponse(
                reserva.getId(),
                reserva.getCliente().getId(),
                reserva.getHabitacion().getId(),
                reserva.getFechaEntrada(),
                reserva.getFechaSalida(),
                reserva.getEstado(),
                reserva.getObservaciones());
    }
}
