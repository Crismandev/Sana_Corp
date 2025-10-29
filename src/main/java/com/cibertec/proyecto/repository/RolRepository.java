package com.cibertec.proyecto.repository;

import com.cibertec.proyecto.entity.Rol;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolRepository extends JpaRepository<Rol, Integer> {
    Rol findByNombre(String nombre);
}