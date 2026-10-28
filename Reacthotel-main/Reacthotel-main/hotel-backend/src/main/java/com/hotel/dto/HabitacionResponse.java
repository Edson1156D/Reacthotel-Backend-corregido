package com.hotel.dto;

import com.hotel.entity.Habitacion;
import com.hotel.entity.EstadoHabitacion;

public record HabitacionResponse(
        Long id,
        String numero,
        Integer piso,
        String descripcion,
        EstadoHabitacion estado,
        TipoHabitacionResponse tipo) {

    public static HabitacionResponse desde(Habitacion habitacion) {
        return new HabitacionResponse(
                habitacion.getId(),
                habitacion.getNumero(),
                habitacion.getPiso(),
                habitacion.getDescripcion(),
                habitacion.getEstado(),
                TipoHabitacionResponse.desde(habitacion.getTipo()));
    }
}
