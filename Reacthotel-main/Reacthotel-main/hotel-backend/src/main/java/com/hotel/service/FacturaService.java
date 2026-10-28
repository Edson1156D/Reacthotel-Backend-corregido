package com.hotel.service;

import com.hotel.dto.FacturaCreate;
import com.hotel.dto.FacturaResponse;
import com.hotel.dto.FacturaUpdate;
import com.hotel.entity.EstadoFactura;
import com.hotel.entity.EstadoReserva;
import com.hotel.entity.Factura;
import com.hotel.entity.Reserva;
import com.hotel.exception.ConflictoException;
import com.hotel.exception.RecursoNoEncontradoException;
import com.hotel.exception.SolicitudInvalidaException;
import com.hotel.repository.FacturaRepository;
import com.hotel.repository.ReservaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@Transactional
public class FacturaService {

    private final FacturaRepository facturaRepository;
    private final ReservaRepository reservaRepository;

    public FacturaService(
            FacturaRepository facturaRepository,
            ReservaRepository reservaRepository) {
        this.facturaRepository = facturaRepository;
        this.reservaRepository = reservaRepository;
    }

    @Transactional(readOnly = true)
    public List<FacturaResponse> listar() {
        return facturaRepository.findAll().stream()
                .map(FacturaResponse::desde)
                .toList();
    }

    public FacturaResponse crear(FacturaCreate request) {
        Reserva reserva = reservaRepository.findById(request.reservaId())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Reserva no encontrada"));

        if (reserva.getEstado() != EstadoReserva.CONFIRMADA) {
            throw new SolicitudInvalidaException(
                    "Solo se puede facturar una reserva CONFIRMADA");
        }

        if (facturaRepository.existsByReservaIdAndEstadoNot(
                reserva.getId(), EstadoFactura.ANULADA)) {
            throw new ConflictoException(
                    "La reserva ya tiene una factura activa");
        }

        long noches = ChronoUnit.DAYS.between(
                reserva.getFechaEntrada(), reserva.getFechaSalida());
        BigDecimal total = reserva.getHabitacion().getTipo().getPrecioNoche()
                .multiply(BigDecimal.valueOf(noches));

        Factura factura = new Factura();
        factura.setReserva(reserva);
        factura.setTotal(total);
        factura.setEstado(EstadoFactura.PENDIENTE);
        factura.setNotas(request.notas());

        return FacturaResponse.desde(facturaRepository.save(factura));
    }

    public FacturaResponse actualizar(Long id, FacturaUpdate request) {
        Factura factura = buscar(id);
        factura.setEstado(request.estado());
        return FacturaResponse.desde(facturaRepository.save(factura));
    }

    public void eliminar(Long id) {
        facturaRepository.delete(buscar(id));
    }

    private Factura buscar(Long id) {
        return facturaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Factura no encontrada"));
    }
}
