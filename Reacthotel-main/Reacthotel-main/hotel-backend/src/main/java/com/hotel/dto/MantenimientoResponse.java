package com.hotel.dto;

import com.hotel.entity.Mantenimiento;

import java.time.Instant;

public record MantenimientoResponse(
        Long id,
        Long habitacionId,
        Long usuarioId,
        String descripcion,
        boolean resuelto,
        Instant fechaFin) {

    public static MantenimientoResponse desde(Mantenimiento mantenimiento) {
        return new MantenimientoResponse(
                mantenimiento.getId(),
                mantenimiento.getHabitacion().getId(),
                mantenimiento.getUsuario() == null
                        ? null
                        : mantenimiento.getUsuario().getId(),
                mantenimiento.getDescripcion(),
                mantenimiento.isResuelto(),
                mantenimiento.getFechaFin());
    }
}
