package com.hotel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class TipoHabitacionUpdate {

    private String nombre;
    private String descripcion;
    private BigDecimal precioNoche;
    private Integer capacidad;
    private String imagenUrl;

    private boolean nombrePresente;
    private boolean descripcionPresente;
    private boolean precioNochePresente;
    private boolean capacidadPresente;
    private boolean imagenUrlPresente;

    public String getNombre() {
        return nombre;
    }

    @JsonProperty("nombre")
    public void setNombre(String nombre) {
        this.nombre = nombre;
        this.nombrePresente = true;
    }

    public String getDescripcion() {
        return descripcion;
    }

    @JsonProperty("descripcion")
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
        this.descripcionPresente = true;
    }

    public BigDecimal getPrecioNoche() {
        return precioNoche;
    }

    @JsonProperty("precio_noche")
    public void setPrecioNoche(BigDecimal precioNoche) {
        this.precioNoche = precioNoche;
        this.precioNochePresente = true;
    }

    public Integer getCapacidad() {
        return capacidad;
    }

    @JsonProperty("capacidad")
    public void setCapacidad(Integer capacidad) {
        this.capacidad = capacidad;
        this.capacidadPresente = true;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    @JsonProperty("imagen_url")
    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
        this.imagenUrlPresente = true;
    }

    public boolean isNombrePresente() {
        return nombrePresente;
    }

    public boolean isDescripcionPresente() {
        return descripcionPresente;
    }

    public boolean isPrecioNochePresente() {
        return precioNochePresente;
    }

    public boolean isCapacidadPresente() {
        return capacidadPresente;
    }

    public boolean isImagenUrlPresente() {
        return imagenUrlPresente;
    }
}
