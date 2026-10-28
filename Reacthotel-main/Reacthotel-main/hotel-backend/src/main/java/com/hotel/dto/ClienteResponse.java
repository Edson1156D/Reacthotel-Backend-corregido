package com.hotel.dto;

import com.hotel.entity.Cliente;

public record ClienteResponse(
        Long id,
        String nombre,
        String apellido,
        String dni,
        String email,
        String telefono,
        String direccion) {

    public static ClienteResponse desde(Cliente cliente) {
        return new ClienteResponse(
                cliente.getId(),
                cliente.getNombre(),
                cliente.getApellido(),
                cliente.getDni(),
                cliente.getEmail(),
                cliente.getTelefono(),
                cliente.getDireccion());
    }
}
