package com.hotel.service;

import com.hotel.dto.MantenimientoCreate;
import com.hotel.dto.MantenimientoResponse;
import com.hotel.dto.MantenimientoUpdate;
import com.hotel.entity.Habitacion;
import com.hotel.entity.Mantenimiento;
import com.hotel.entity.Rol;
import com.hotel.entity.Usuario;
import com.hotel.exception.ConflictoException;
import com.hotel.exception.RecursoNoEncontradoException;
import com.hotel.exception.SolicitudInvalidaException;
import com.hotel.repository.HabitacionRepository;
import com.hotel.repository.MantenimientoRepository;
import com.hotel.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MantenimientoService {

    private final MantenimientoRepository mantenimientoRepository;
    private final HabitacionRepository habitacionRepository;
    private final UsuarioRepository usuarioRepository;

    public MantenimientoService(
            MantenimientoRepository mantenimientoRepository,
            HabitacionRepository habitacionRepository,
            UsuarioRepository usuarioRepository) {
        this.mantenimientoRepository = mantenimientoRepository;
        this.habitacionRepository = habitacionRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional(readOnly = true)
    public List<MantenimientoResponse> listar() {
        return mantenimientoRepository.findAll().stream()
                .map(MantenimientoResponse::desde)
                .toList();
    }

    public MantenimientoResponse crear(MantenimientoCreate request) {
        Habitacion habitacion = buscarHabitacion(request.habitacionId());
        if (mantenimientoRepository.existsByHabitacionIdAndResueltoFalse(habitacion.getId())) {
            throw new ConflictoException(
                    "La habitación ya tiene un mantenimiento pendiente");
        }

        Mantenimiento mantenimiento = new Mantenimiento();
        mantenimiento.setHabitacion(habitacion);
        mantenimiento.setUsuario(buscarUsuarioMantenimiento(request.usuarioId()));
        mantenimiento.setDescripcion(request.descripcion());
        mantenimiento.setResuelto(false);
        mantenimiento.setFechaFin(null);
        return MantenimientoResponse.desde(mantenimientoRepository.save(mantenimiento));
    }

    public MantenimientoResponse actualizar(Long id, MantenimientoUpdate request) {
        Mantenimiento mantenimiento = buscar(id);

        if (request.isDescripcionPresente()) {
            if (request.getDescripcion() == null
                    || request.getDescripcion().isBlank()) {
                throw new SolicitudInvalidaException(
                        "La descripción no puede estar vacía");
            }
            mantenimiento.setDescripcion(request.getDescripcion());
        }
        if (request.isUsuarioIdPresente()) {
            mantenimiento.setUsuario(buscarUsuarioMantenimiento(request.getUsuarioId()));
        }
        if (request.isResueltoPresente()) {
            if (request.getResuelto() == null) {
                throw new SolicitudInvalidaException(
                        "El campo resuelto no puede ser nulo");
            }
            mantenimiento.setResuelto(request.getResuelto());
        }
        if (request.isFechaFinPresente()) {
            mantenimiento.setFechaFin(request.getFechaFin());
        }

        return MantenimientoResponse.desde(mantenimientoRepository.save(mantenimiento));
    }

    public void eliminar(Long id) {
        mantenimientoRepository.delete(buscar(id));
    }

    private Mantenimiento buscar(Long id) {
        return mantenimientoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Mantenimiento no encontrado"));
    }

    private Habitacion buscarHabitacion(Long id) {
        return habitacionRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Habitación no encontrada"));
    }

    private Usuario buscarUsuarioMantenimiento(Long id) {
        if (id == null) {
            return null;
        }
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new SolicitudInvalidaException(
                        "El usuario indicado no existe"));
        if (usuario.getRol() != Rol.MANTENIMIENTO) {
            throw new SolicitudInvalidaException(
                    "El usuario debe tener el rol MANTENIMIENTO");
        }
        return usuario;
    }
}
