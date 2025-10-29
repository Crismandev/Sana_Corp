package com.cibertec.proyecto.repository;

import com.cibertec.proyecto.entity.Cita;
import com.cibertec.proyecto.entity.EstadoCita;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CitaRepository extends JpaRepository<Cita, Integer> {
    
    // Citas de un paciente
    List<Cita> findByPacienteIdOrderByFechaHoraDesc(Integer pacienteId);
    
    // Citas de un médico
    List<Cita> findByMedicoIdOrderByFechaHoraDesc(Integer medicoId);
    
    // Citas por estado
    List<Cita> findByEstadoOrderByFechaHoraDesc(EstadoCita estado);
}