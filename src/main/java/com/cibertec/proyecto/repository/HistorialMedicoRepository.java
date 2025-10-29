package com.cibertec.proyecto.repository;

import com.cibertec.proyecto.entity.HistorialMedico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface HistorialMedicoRepository extends JpaRepository<HistorialMedico, Integer> {
    
    // Historial de un paciente específico
    @Query("SELECT hm FROM HistorialMedico hm WHERE hm.cita.paciente.id = :pacienteId ORDER BY hm.fechaRegistro DESC")
    List<HistorialMedico> findByPacienteId(@Param("pacienteId") Integer pacienteId);
    
    // Historial por cita
    List<HistorialMedico> findByCitaId(Integer citaId);
}