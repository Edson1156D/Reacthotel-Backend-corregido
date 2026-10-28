package com.hotel.service;

import com.hotel.dto.ReservaCreate;
import com.hotel.dto.ReservaResponse;
import com.hotel.dto.ReservaUpdate;
import com.hotel.entity.Cliente;
import com.hotel.entity.EstadoReserva;
import com.hotel.entity.Habitacion;
import com.hotel.entity.Reserva;
import com.hotel.exception.ConflictoException;
import com.hotel.exception.RecursoNoEncontradoException;
import com.hotel.exception.SolicitudInvalidaException;
import com.hotel.repository.ClienteRepository;
import com.hotel.repository.FacturaRepository;
import com.hotel.repository.HabitacionRepository;
import com.hotel.repository.ReservaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final ClienteRepository clienteRepository;
    private final HabitacionRepository habitacionRepository;
    private final FacturaRepository facturaRepository;

    public ReservaService(
            ReservaRepository reservaRepository,
            ClienteRepository clienteRepository,
            HabitacionRepository habitacionRepository,
            FacturaRepository facturaRepository) {
        this.reservaRepository = reservaRepository;
        this.clienteRepository = clienteRepository;
        this.habitacionRepository = habitacionRepository;
        this.facturaRepository = facturaRepository;
    }

    @Transactional(readOnly = true)
    public List<ReservaResponse> listar() {
        return reservaRepository.findAll().stream()
                .map(ReservaResponse::desde)
                .toList();
    }

    public ReservaResponse crear(ReservaCreate request) {
        validarFechas(request.fechaEntrada(), request.fechaSalida());

        Reserva reserva = new Reserva();
        reserva.setCliente(buscarCliente(request.clienteId()));
        reserva.setHabitacion(buscarHabitacion(request.habitacionId()));
        reserva.setFechaEntrada(request.fechaEntrada());
        reserva.setFechaSalida(request.fechaSalida());
        reserva.setObservaciones(request.observaciones());
        reserva.setEstado(EstadoReserva.PENDIENTE);

        validarSolapamiento(reserva, null);
        return ReservaResponse.desde(reservaRepository.save(reserva));
    }

    public ReservaResponse actualizar(Long id, ReservaUpdate request) {
        Reserva reserva = buscar(id);

        if (request.isHabitacionIdPresente()) {
            reserva.setHabitacion(buscarHabitacionRequerida(request.getHabitacionId()));
        }
        if (request.isFechaEntradaPresente()) {
            reserva.setFechaEntrada(request.getFechaEntrada());
        }
        if (request.isFechaSalidaPresente()) {
            reserva.setFechaSalida(request.getFechaSalida());
        }
        if (request.isObservacionesPresente()) {
            reserva.setObservaciones(request.getObservaciones());
        }
        if (request.isEstadoPresente()) {
            if (request.getEstado() == null) {
                throw new SolicitudInvalidaException("El estado no puede ser nulo");
            }
            reserva.setEstado(request.getEstado());
        }

        if (request.isHabitacionIdPresente()
                || request.isFechaEntradaPresente()
                || request.isFechaSalidaPresente()
                || request.isEstadoPresente()) {
            validarFechas(reserva.getFechaEntrada(), reserva.getFechaSalida());
            validarSolapamiento(reserva, reserva.getId());
        }

        return ReservaResponse.desde(reservaRepository.save(reserva));
    }

    public void eliminar(Long id) {
        Reserva reserva = buscar(id);
        if (facturaRepository.existsByReservaId(id)) {
            throw new ConflictoException(
                    "No se puede eliminar: la reserva tiene facturas asociadas");
        }
        reservaRepository.delete(reserva);
    }

    private Reserva buscar(Long id) {
        return reservaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Reserva no encontrada"));
    }

    private Cliente buscarCliente(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Cliente no encontrado"));
    }

    private Habitacion buscarHabitacion(Long id) {
        return habitacionRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Habitación no encontrada"));
    }

    private Habitacion buscarHabitacionRequerida(Long id) {
        if (id == null) {
            throw new SolicitudInvalidaException("La habitación no puede ser nula");
        }
        return buscarHabitacion(id);
    }

    private void validarFechas(LocalDate fechaEntrada, LocalDate fechaSalida) {
        if (fechaEntrada == null || fechaSalida == null) {
            throw new SolicitudInvalidaException(
                    "Las fechas de entrada y salida son obligatorias");
        }
        if (!fechaSalida.isAfter(fechaEntrada)) {
            throw new SolicitudInvalidaException(
                    "La fecha de salida debe ser posterior a la fecha de entrada");
        }
    }

    private void validarSolapamiento(Reserva reserva, Long idExcluir) {
        for (Reserva existente : reservaRepository.findByHabitacionId(
                reserva.getHabitacion().getId())) {
            if (idExcluir != null && existente.getId().equals(idExcluir)) {
                continue;
            }
            if (existente.getEstado() == EstadoReserva.CANCELADA
                    || existente.getEstado() == EstadoReserva.COMPLETADA) {
                continue;
            }
            boolean seSolapan = existente.getFechaEntrada().isBefore(reserva.getFechaSalida())
                    && existente.getFechaSalida().isAfter(reserva.getFechaEntrada());
            if (seSolapan) {
                throw new ConflictoException(
                        "La habitación ya está reservada en esas fechas (Ref #"
                                + existente.getId() + ")");
            }
        }
    }
}
