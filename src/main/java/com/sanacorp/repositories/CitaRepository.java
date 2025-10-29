package com.sanacorp.repositories;

import com.sanacorp.models.Cita;
import com.sanacorp.models.Cita.EstadoCita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Repositorio para la entidad Cita
 * Contiene métodos de consulta personalizados para operaciones específicas del módulo Secretario
 */
@Repository
public interface CitaRepository extends JpaRepository<Cita, Long> {
    
    /**
     * Busca citas por fecha específica
     * @param fecha Fecha de las citas
     * @return Lista de citas en la fecha especificada
     */
    @Query("SELECT c FROM Cita c JOIN FETCH c.paciente p JOIN FETCH p.persona per " +
           "JOIN FETCH c.medico m JOIN FETCH m.persona mper JOIN FETCH m.especialidad e " +
           "JOIN FETCH c.consultorio con " +
           "WHERE CAST(c.fechaHora AS date) = :fecha " +
           "ORDER BY c.fechaHora")
    List<Cita> findByFecha(@Param("fecha") LocalDate fecha);
    
    /**
     * Busca citas por médico en una fecha específica
     * @param medicoId ID del médico
     * @param fecha Fecha de las citas
     * @return Lista de citas del médico en la fecha especificada
     */
    @Query("SELECT c FROM Cita c WHERE c.medico.id = :medicoId AND CAST(c.fechaHora AS date) = :fecha ORDER BY c.fechaHora")
    List<Cita> findByMedicoIdAndFecha(@Param("medicoId") Long medicoId, @Param("fecha") LocalDate fecha);
    
    /**
     * Busca citas por paciente
     * @param pacienteId ID del paciente
     * @return Lista de citas del paciente ordenadas por fecha descendente
     */
    @Query("SELECT c FROM Cita c JOIN FETCH c.medico m JOIN FETCH m.persona mper JOIN FETCH m.especialidad e " +
           "JOIN FETCH c.consultorio con " +
           "WHERE c.paciente.id = :pacienteId " +
           "ORDER BY c.fechaHora DESC")
    List<Cita> findByPacienteId(@Param("pacienteId") Long pacienteId);
    
    /**
     * Busca citas por estado
     * @param estado Estado de la cita
     * @return Lista de citas con el estado especificado
     */
    @Query("SELECT c FROM Cita c JOIN FETCH c.paciente p JOIN FETCH p.persona per " +
           "JOIN FETCH c.medico m JOIN FETCH m.persona mper " +
           "WHERE c.estado = :estado " +
           "ORDER BY c.fechaHora")
    List<Cita> findByEstado(@Param("estado") EstadoCita estado);
    
    /**
     * Busca citas del día actual
     * @return Lista de citas del día actual
     */
    @Query("SELECT c FROM Cita c JOIN FETCH c.paciente p JOIN FETCH p.persona per " +
           "JOIN FETCH c.medico m JOIN FETCH m.persona mper JOIN FETCH m.especialidad e " +
           "JOIN FETCH c.consultorio con " +
           "WHERE CAST(c.fechaHora AS date) = CURRENT_DATE " +
           "ORDER BY c.fechaHora")
    List<Cita> findCitasDelDia();
    
    /**
     * Busca citas próximas (siguientes 7 días)
     * @return Lista de citas próximas
     */
    @Query("SELECT c FROM Cita c JOIN FETCH c.paciente p JOIN FETCH p.persona per " +
           "JOIN FETCH c.medico m JOIN FETCH m.persona mper " +
           "WHERE c.fechaHora BETWEEN CURRENT_TIMESTAMP AND :fechaLimite " +
           "AND c.estado IN ('PROGRAMADA', 'CONFIRMADA') " +
           "ORDER BY c.fechaHora")
    List<Cita> findCitasProximas(@Param("fechaLimite") LocalDateTime fechaLimite);
    
    /**
     * Verifica si existe conflicto de horario para un médico
     * @param medicoId ID del médico
     * @param fechaHora Fecha y hora de la cita
     * @param duracionMinutos Duración estimada en minutos
     * @return Lista de citas que podrían generar conflicto
     */
    @Query("SELECT c FROM Cita c WHERE c.medico.id = :medicoId " +
           "AND c.estado IN ('PROGRAMADA', 'CONFIRMADA', 'EN_CURSO') " +
           "AND c.fechaHora BETWEEN :fechaInicio AND :fechaFin")
    List<Cita> findConflictosHorario(@Param("medicoId") Long medicoId, 
                                   @Param("fechaInicio") LocalDateTime fechaInicio, 
                                   @Param("fechaFin") LocalDateTime fechaFin);
    
    /**
     * Cuenta citas por estado en una fecha específica
     * @param estado Estado de la cita
     * @param fecha Fecha de las citas
     * @return Número de citas con el estado en la fecha especificada
     */
    @Query("SELECT COUNT(c) FROM Cita c WHERE c.estado = :estado AND CAST(c.fechaHora AS date) = :fecha")
    Long countByEstadoAndFecha(@Param("estado") EstadoCita estado, @Param("fecha") LocalDate fecha);
    
    /**
     * Busca citas recientes (últimos 30 días) ordenadas por fecha
     * @return Lista de citas recientes
     */
    @Query("SELECT c FROM Cita c JOIN FETCH c.paciente p JOIN FETCH p.persona per " +
           "JOIN FETCH c.medico m JOIN FETCH m.persona mper " +
           "WHERE c.fechaCreacion >= :fechaLimite " +
           "ORDER BY c.fechaCreacion DESC")
    List<Cita> findCitasRecientes(@Param("fechaLimite") LocalDateTime fechaLimite);
}