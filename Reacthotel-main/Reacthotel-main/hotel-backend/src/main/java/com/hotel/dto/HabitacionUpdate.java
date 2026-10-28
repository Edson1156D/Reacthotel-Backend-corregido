package com.hotel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hotel.entity.EstadoHabitacion;

public class HabitacionUpdate {

    private String numero;
    private Integer piso;
    private String descripcion;
    private Long tipoId;
    private EstadoHabitacion estado;

    private boolean numeroPresente;
    private boolean pisoPresente;
    private boolean descripcionPresente;
    private boolean tipoIdPresente;
    private boolean estadoPresente;

    public String getNumero() {
        return numero;
    }

    @JsonProperty("numero")
    public void setNumero(String numero) {
        this.numero = numero;
        this.numeroPresente = true;
    }

    public Integer getPiso() {
        return piso;
    }

    @JsonProperty("piso")
    public void setPiso(Integer piso) {
        this.piso = piso;
        this.pisoPresente = true;
    }

    public String getDescripcion() {
        return descripcion;
    }

    @JsonProperty("descripcion")
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
        this.descripcionPresente = true;
    }

    public Long getTipoId() {
        return tipoId;
    }

    @JsonProperty("tipo_id")
    public void setTipoId(Long tipoId) {
        this.tipoId = tipoId;
        this.tipoIdPresente = true;
    }

    public EstadoHabitacion getEstado() {
        return estado;
    }

    @JsonProperty("estado")
    public void setEstado(EstadoHabitacion estado) {
        this.estado = estado;
        this.estadoPresente = true;
    }

    public boolean isNumeroPresente() {
        return numeroPresente;
    }

    public boolean isPisoPresente() {
        return pisoPresente;
    }

    public boolean isDescripcionPresente() {
        return descripcionPresente;
    }

    public boolean isTipoIdPresente() {
        return tipoIdPresente;
    }

    public boolean isEstadoPresente() {
        return estadoPresente;
    }
}
