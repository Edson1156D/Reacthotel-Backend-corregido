package com.hotel.service;

import com.hotel.dto.HabitacionCreate;
import com.hotel.dto.HabitacionResponse;
import com.hotel.dto.HabitacionUpdate;
import com.hotel.entity.EstadoHabitacion;
import com.hotel.entity.Habitacion;
import com.hotel.entity.TipoHabitacion;
import com.hotel.exception.ConflictoException;
import com.hotel.exception.RecursoNoEncontradoException;
import com.hotel.exception.SolicitudInvalidaException;
import com.hotel.repository.HabitacionRepository;
import com.hotel.repository.MantenimientoRepository;
import com.hotel.repository.ReservaRepository;
import com.hotel.repository.TipoHabitacionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class HabitacionService {

    private final HabitacionRepository habitacionRepository;
    private final TipoHabitacionRepository tipoHabitacionRepository;
    private final ReservaRepository reservaRepository;
    private final MantenimientoRepository mantenimientoRepository;

    public HabitacionService(
            HabitacionRepository habitacionRepository,
            TipoHabitacionRepository tipoHabitacionRepository,
            ReservaRepository reservaRepository,
            MantenimientoRepository mantenimientoRepository) {
        this.habitacionRepository = habitacionRepository;
        this.tipoHabitacionRepository = tipoHabitacionRepository;
        this.reservaRepository = reservaRepository;
        this.mantenimientoRepository = mantenimientoRepository;
    }

    @Transactional(readOnly = true)
    public List<HabitacionResponse> listar() {
        return habitacionRepository.findAll().stream()
                .map(HabitacionResponse::desde)
                .toList();
    }

    public HabitacionResponse crear(HabitacionCreate request) {
        Habitacion habitacion = new Habitacion();
        habitacion.setNumero(request.numero());
        habitacion.setPiso(request.piso());
        habitacion.setDescripcion(request.descripcion());
        habitacion.setTipo(buscarTipo(request.tipoId()));
        habitacion.setEstado(request.estado() == null ? EstadoHabitacion.LIBRE : request.estado());
        return HabitacionResponse.desde(habitacionRepository.save(habitacion));
    }

    public HabitacionResponse actualizar(Long id, HabitacionUpdate request) {
        Habitacion habitacion = buscar(id);

        if (request.isNumeroPresente()) {
            habitacion.setNumero(validarNumero(request.getNumero()));
        }
        if (request.isPisoPresente()) {
            habitacion.setPiso(request.getPiso());
        }
        if (request.isDescripcionPresente()) {
            habitacion.setDescripcion(request.getDescripcion());
        }
        if (request.isTipoIdPresente()) {
            habitacion.setTipo(buscarTipoRequerido(request.getTipoId()));
        }
        if (request.isEstadoPresente()) {
            if (request.getEstado() == null) {
                throw new SolicitudInvalidaException("El estado no puede ser nulo");
            }
            habitacion.setEstado(request.getEstado());
        }

        return HabitacionResponse.desde(habitacionRepository.save(habitacion));
    }

    public void eliminar(Long id) {
        Habitacion habitacion = buscar(id);
        if (reservaRepository.existsByHabitacionId(id)) {
            throw new ConflictoException(
                    "No se puede eliminar: la habitación tiene reservas asociadas");
        }
        if (mantenimientoRepository.existsByHabitacionId(id)) {
            throw new ConflictoException(
                    "No se puede eliminar: la habitación tiene mantenimientos asociados");
        }
        habitacionRepository.delete(habitacion);
    }

    private Habitacion buscar(Long id) {
        return habitacionRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Habitación no encontrada"));
    }

    private TipoHabitacion buscarTipo(Long tipoId) {
        return tipoHabitacionRepository.findById(tipoId)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Tipo de habitación no encontrado"));
    }

    private TipoHabitacion buscarTipoRequerido(Long tipoId) {
        if (tipoId == null) {
            throw new SolicitudInvalidaException("El tipo de habitación no puede ser nulo");
        }
        return buscarTipo(tipoId);
    }

    private String validarNumero(String numero) {
        if (numero == null || numero.isBlank()) {
            throw new SolicitudInvalidaException("El número no puede estar vacío");
        }
        return numero;
    }
}
