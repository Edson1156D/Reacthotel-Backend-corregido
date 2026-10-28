package com.hotel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hotel.entity.EstadoReserva;

import java.time.LocalDate;

public class ReservaUpdate {

    private Long habitacionId;
    private LocalDate fechaEntrada;
    private LocalDate fechaSalida;
    private String observaciones;
    private EstadoReserva estado;

    private boolean habitacionIdPresente;
    private boolean fechaEntradaPresente;
    private boolean fechaSalidaPresente;
    private boolean observacionesPresente;
    private boolean estadoPresente;

    public Long getHabitacionId() {
        return habitacionId;
    }

    @JsonProperty("habitacion_id")
    public void setHabitacionId(Long habitacionId) {
        this.habitacionId = habitacionId;
        this.habitacionIdPresente = true;
    }

    public LocalDate getFechaEntrada() {
        return fechaEntrada;
    }

    @JsonProperty("fecha_entrada")
    public void setFechaEntrada(LocalDate fechaEntrada) {
        this.fechaEntrada = fechaEntrada;
        this.fechaEntradaPresente = true;
    }

    public LocalDate getFechaSalida() {
        return fechaSalida;
    }

    @JsonProperty("fecha_salida")
    public void setFechaSalida(LocalDate fechaSalida) {
        this.fechaSalida = fechaSalida;
        this.fechaSalidaPresente = true;
    }

    public String getObservaciones() {
        return observaciones;
    }

    @JsonProperty("observaciones")
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
        this.observacionesPresente = true;
    }

    public EstadoReserva getEstado() {
        return estado;
    }

    @JsonProperty("estado")
    public void setEstado(EstadoReserva estado) {
        this.estado = estado;
        this.estadoPresente = true;
    }

    public boolean isHabitacionIdPresente() {
        return habitacionIdPresente;
    }

    public boolean isFechaEntradaPresente() {
        return fechaEntradaPresente;
    }

    public boolean isFechaSalidaPresente() {
        return fechaSalidaPresente;
    }

    public boolean isObservacionesPresente() {
        return observacionesPresente;
    }

    public boolean isEstadoPresente() {
        return estadoPresente;
    }
}
