package com.hotel.dto;

import com.hotel.entity.TipoHabitacion;

import java.math.BigDecimal;

public record TipoHabitacionResponse(
        Long id,
        String nombre,
        String descripcion,
        BigDecimal precioNoche,
        Integer capacidad,
        String imagenUrl) {

    public static TipoHabitacionResponse desde(TipoHabitacion tipoHabitacion) {
        return new TipoHabitacionResponse(
                tipoHabitacion.getId(),
                tipoHabitacion.getNombre(),
                tipoHabitacion.getDescripcion(),
                tipoHabitacion.getPrecioNoche(),
                tipoHabitacion.getCapacidad(),
                tipoHabitacion.getImagenUrl());
    }
}
