package com.hotel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;

public class ClienteUpdate {

    private String nombre;
    private String apellido;
    private String dni;

    @Email(message = "El email no tiene un formato válido")
    private String email;

    private String telefono;
    private String direccion;

    private boolean nombrePresente;
    private boolean apellidoPresente;
    private boolean dniPresente;
    private boolean emailPresente;
    private boolean telefonoPresente;
    private boolean direccionPresente;

    public String getNombre() {
        return nombre;
    }

    @JsonProperty("nombre")
    public void setNombre(String nombre) {
        this.nombre = nombre;
        this.nombrePresente = true;
    }

    public String getApellido() {
        return apellido;
    }

    @JsonProperty("apellido")
    public void setApellido(String apellido) {
        this.apellido = apellido;
        this.apellidoPresente = true;
    }

    public String getDni() {
        return dni;
    }

    @JsonProperty("dni")
    public void setDni(String dni) {
        this.dni = dni;
        this.dniPresente = true;
    }

    public String getEmail() {
        return email;
    }

    @JsonProperty("email")
    public void setEmail(String email) {
        this.email = email;
        this.emailPresente = true;
    }

    public String getTelefono() {
        return telefono;
    }

    @JsonProperty("telefono")
    public void setTelefono(String telefono) {
        this.telefono = telefono;
        this.telefonoPresente = true;
    }

    public String getDireccion() {
        return direccion;
    }

    @JsonProperty("direccion")
    public void setDireccion(String direccion) {
        this.direccion = direccion;
        this.direccionPresente = true;
    }

    public boolean isNombrePresente() {
        return nombrePresente;
    }

    public boolean isApellidoPresente() {
        return apellidoPresente;
    }

    public boolean isDniPresente() {
        return dniPresente;
    }

    public boolean isEmailPresente() {
        return emailPresente;
    }

    public boolean isTelefonoPresente() {
        return telefonoPresente;
    }

    public boolean isDireccionPresente() {
        return direccionPresente;
    }
}
