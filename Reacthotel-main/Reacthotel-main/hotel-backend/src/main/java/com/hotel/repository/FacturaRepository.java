package com.hotel.repository;

import com.hotel.entity.Factura;
import com.hotel.entity.EstadoFactura;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FacturaRepository extends JpaRepository<Factura, Long> {

    boolean existsByReservaId(Long reservaId);

    boolean existsByReservaIdAndEstadoNot(Long reservaId, EstadoFactura estado);
}
