package com.hotel.repository;

import com.hotel.entity.Habitacion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HabitacionRepository extends JpaRepository<Habitacion, Long> {

    boolean existsByTipoId(Long tipoId);
}
