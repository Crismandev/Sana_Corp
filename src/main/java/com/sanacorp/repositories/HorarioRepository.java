package com.sanacorp.repositories;

import com.sanacorp.models.Horario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalTime;
import java.util.List;

/**
 * Repositorio para la entidad Horario
 * Contiene métodos de consulta personalizados para operaciones específicas del módulo Secretario
 */
@Repository
public interface HorarioRepository extends JpaRepository<Horario, Long> {
    
    /**
     * Busca horarios por médico
     * @param medicoId ID del médico
     * @return Lista de horarios del médico especificado
     */
    @Query("SELECT h FROM Horario h WHERE h.medico.id = :medicoId ORDER BY h.diaSemana, h.horaInicio")
    List<Horario> findByMedicoId(@Param("medicoId") Long medicoId);
    
    /**
     * Busca horarios por día de la semana
     * @param diaSemana Día de la semana (1=Lunes, 7=Domingo)
     * @return Lista de horarios para el día especificado
     */
    @Query("SELECT h FROM Horario h JOIN FETCH h.medico m JOIN FETCH m.persona p " +
           "WHERE h.diaSemana = :diaSemana " +
           "ORDER BY h.horaInicio")
    List<Horario> findByDiaSemana(@Param("diaSemana") Integer diaSemana);
    
    /**
     * Busca horarios de todos los médicos
     * @return Lista de todos los horarios
     */
    @Query("SELECT h FROM Horario h JOIN FETCH h.medico m JOIN FETCH m.persona p JOIN FETCH m.especialidad e " +
           "ORDER BY h.diaSemana, h.horaInicio")
    List<Horario> findAllActivosWithMedico();
    
    /**
     * Busca horarios por médico y día de la semana
     * @param medicoId ID del médico
     * @param diaSemana Día de la semana
     * @return Lista de horarios del médico en el día especificado
     */
    @Query("SELECT h FROM Horario h WHERE h.medico.id = :medicoId AND h.diaSemana = :diaSemana ORDER BY h.horaInicio")
    List<Horario> findByMedicoIdAndDiaSemana(@Param("medicoId") Long medicoId, @Param("diaSemana") Integer diaSemana);
    
    /**
     * Verifica si un médico tiene horarios disponibles
     * @param medicoId ID del médico
     * @return true si el médico tiene horarios
     */
    @Query("SELECT COUNT(h) > 0 FROM Horario h WHERE h.medico.id = :medicoId")
    boolean existsByMedicoIdAndActivoTrue(@Param("medicoId") Long medicoId);
    
    /**
     * Busca horarios que se superponen con un rango de tiempo específico
     * @param medicoId ID del médico
     * @param diaSemana Día de la semana
     * @param horaInicio Hora de inicio del rango
     * @param horaFin Hora de fin del rango
     * @return Lista de horarios que se superponen
     */
    @Query("SELECT h FROM Horario h WHERE h.medico.id = :medicoId " +
           "AND h.diaSemana = :diaSemana " +
           "AND ((h.horaInicio <= :horaInicio AND h.horaFin > :horaInicio) " +
           "OR (h.horaInicio < :horaFin AND h.horaFin >= :horaFin) " +
           "OR (h.horaInicio >= :horaInicio AND h.horaFin <= :horaFin))")
    List<Horario> findHorariosSuperpuestos(@Param("medicoId") Long medicoId, 
                                         @Param("diaSemana") Integer diaSemana,
                                         @Param("horaInicio") LocalTime horaInicio, 
                                         @Param("horaFin") LocalTime horaFin);
}