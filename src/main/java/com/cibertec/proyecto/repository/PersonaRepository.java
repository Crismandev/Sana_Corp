package com.cibertec.proyecto.repository;

import com.cibertec.proyecto.entity.Persona;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PersonaRepository extends JpaRepository<Persona, Integer> {
    Optional<Persona> findByDni(String dni);
    Optional<Persona> findByUsuarioId(Integer usuarioId);
}