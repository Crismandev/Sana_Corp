package com.cibertec.proyecto.repository;

import com.cibertec.proyecto.entity.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PacienteRepository extends JpaRepository<Paciente, Integer> {
    Optional<Paciente> findByPersonaDni(String dni);
    Optional<Paciente> findByPersonaUsuarioId(Integer usuarioId);
}