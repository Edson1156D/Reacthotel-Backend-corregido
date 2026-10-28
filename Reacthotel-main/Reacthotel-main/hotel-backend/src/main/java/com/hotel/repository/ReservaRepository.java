package com.hotel.repository;

import com.hotel.entity.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    boolean existsByClienteId(Long clienteId);

    boolean existsByHabitacionId(Long habitacionId);

    List<Reserva> findByHabitacionId(Long habitacionId);
}
