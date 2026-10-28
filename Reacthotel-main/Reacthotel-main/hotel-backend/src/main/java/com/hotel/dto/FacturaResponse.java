package com.hotel.dto;

import com.hotel.entity.EstadoFactura;
import com.hotel.entity.Factura;

import java.math.BigDecimal;

public record FacturaResponse(
        Long id,
        Long reservaId,
        BigDecimal total,
        EstadoFactura estado,
        String notas) {

    public static FacturaResponse desde(Factura factura) {
        return new FacturaResponse(
                factura.getId(),
                factura.getReserva().getId(),
                factura.getTotal(),
                factura.getEstado(),
                factura.getNotas());
    }
}
