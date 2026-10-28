package com.hotel.service;

import com.hotel.dto.TipoHabitacionRequest;
import com.hotel.dto.TipoHabitacionResponse;
import com.hotel.dto.TipoHabitacionUpdate;
import com.hotel.entity.TipoHabitacion;
import com.hotel.exception.ConflictoException;
import com.hotel.exception.RecursoNoEncontradoException;
import com.hotel.exception.SolicitudInvalidaException;
import com.hotel.repository.HabitacionRepository;
import com.hotel.repository.TipoHabitacionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class TipoHabitacionService {

    private final TipoHabitacionRepository tipoHabitacionRepository;
    private final HabitacionRepository habitacionRepository;

    public TipoHabitacionService(
            TipoHabitacionRepository tipoHabitacionRepository,
            HabitacionRepository habitacionRepository) {
        this.tipoHabitacionRepository = tipoHabitacionRepository;
        this.habitacionRepository = habitacionRepository;
    }

    @Transactional(readOnly = true)
    public List<TipoHabitacionResponse> listar() {
        return tipoHabitacionRepository.findAll().stream()
                .map(TipoHabitacionResponse::desde)
                .toList();
    }

    public TipoHabitacionResponse crear(TipoHabitacionRequest request) {
        TipoHabitacion tipoHabitacion = new TipoHabitacion();
        tipoHabitacion.setNombre(validarNombre(request.nombre()));
        tipoHabitacion.setDescripcion(request.descripcion());
        tipoHabitacion.setPrecioNoche(validarPrecio(request.precioNoche()));
        tipoHabitacion.setCapacidad(validarCapacidad(request.capacidad()));
        tipoHabitacion.setImagenUrl(request.imagenUrl());
        return TipoHabitacionResponse.desde(tipoHabitacionRepository.save(tipoHabitacion));
    }

    public TipoHabitacionResponse actualizar(Long id, TipoHabitacionUpdate request) {
        TipoHabitacion tipoHabitacion = buscar(id);

        if (request.isNombrePresente()) {
            tipoHabitacion.setNombre(validarNombre(request.getNombre()));
        }
        if (request.isDescripcionPresente()) {
            tipoHabitacion.setDescripcion(request.getDescripcion());
        }
        if (request.isPrecioNochePresente()) {
            tipoHabitacion.setPrecioNoche(validarPrecio(request.getPrecioNoche()));
        }
        if (request.isCapacidadPresente()) {
            tipoHabitacion.setCapacidad(validarCapacidad(request.getCapacidad()));
        }
        if (request.isImagenUrlPresente()) {
            tipoHabitacion.setImagenUrl(request.getImagenUrl());
        }

        return TipoHabitacionResponse.desde(tipoHabitacionRepository.save(tipoHabitacion));
    }

    public void eliminar(Long id) {
        TipoHabitacion tipoHabitacion = buscar(id);
        if (habitacionRepository.existsByTipoId(tipoHabitacion.getId())) {
            throw new ConflictoException(
                    "No se puede eliminar: existen habitaciones de este tipo");
        }
        tipoHabitacionRepository.delete(tipoHabitacion);
    }

    private TipoHabitacion buscar(Long id) {
        return tipoHabitacionRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Tipo de habitación no encontrado"));
    }

    private String validarNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new SolicitudInvalidaException("El nombre no puede estar vacío");
        }
        return nombre;
    }

    private BigDecimal validarPrecio(BigDecimal precio) {
        if (precio == null) {
            throw new SolicitudInvalidaException("El precio por noche es obligatorio");
        }
        if (precio.signum() < 0) {
            throw new SolicitudInvalidaException("El precio por noche no puede ser negativo");
        }
        return precio;
    }

    private Integer validarCapacidad(Integer capacidad) {
        if (capacidad == null || capacidad < 1) {
            throw new SolicitudInvalidaException("La capacidad debe ser mayor o igual a 1");
        }
        return capacidad;
    }
}
