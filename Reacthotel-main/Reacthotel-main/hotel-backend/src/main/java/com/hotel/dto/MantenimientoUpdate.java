package com.hotel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public class MantenimientoUpdate {

    private String descripcion;
    private Long usuarioId;
    private Boolean resuelto;
    private Instant fechaFin;

    private boolean descripcionPresente;
    private boolean usuarioIdPresente;
    private boolean resueltoPresente;
    private boolean fechaFinPresente;

    public String getDescripcion() {
        return descripcion;
    }

    @JsonProperty("descripcion")
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
        this.descripcionPresente = true;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    @JsonProperty("usuario_id")
    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
        this.usuarioIdPresente = true;
    }

    public Boolean getResuelto() {
        return resuelto;
    }

    @JsonProperty("resuelto")
    public void setResuelto(Boolean resuelto) {
        this.resuelto = resuelto;
        this.resueltoPresente = true;
    }

    public Instant getFechaFin() {
        return fechaFin;
    }

    @JsonProperty("fecha_fin")
    public void setFechaFin(Instant fechaFin) {
        this.fechaFin = fechaFin;
        this.fechaFinPresente = true;
    }

    public boolean isDescripcionPresente() {
        return descripcionPresente;
    }

    public boolean isUsuarioIdPresente() {
        return usuarioIdPresente;
    }

    public boolean isResueltoPresente() {
        return resueltoPresente;
    }

    public boolean isFechaFinPresente() {
        return fechaFinPresente;
    }
}
