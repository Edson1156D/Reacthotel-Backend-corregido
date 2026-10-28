package com.hotel.repository;

import com.hotel.entity.Mantenimiento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MantenimientoRepository extends JpaRepository<Mantenimiento, Long> {

    boolean existsByUsuario_Id(Long usuarioId);

    boolean existsByHabitacionId(Long habitacionId);

    boolean existsByHabitacionIdAndResueltoFalse(Long habitacionId);
}
