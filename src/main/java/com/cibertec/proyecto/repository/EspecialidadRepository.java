package com.cibertec.proyecto.repository;

import com.cibertec.proyecto.entity.Especialidad;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EspecialidadRepository extends JpaRepository<Especialidad, Integer> {
    List<Especialidad> findByNombreContaining(String nombre);
}