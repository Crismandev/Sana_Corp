package com.cibertec.proyecto.repository;

import com.cibertec.proyecto.entity.Medico;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface MedicoRepository extends JpaRepository<Medico, Integer> {
    Optional<Medico> findByPersonaUsuarioId(Integer usuarioId);
    List<Medico> findByEspecialidadId(Integer especialidadId);
    List<Medico> findByPersonaNombreContainingOrPersonaApellidoContaining(String nombre, String apellido);
}