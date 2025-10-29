package com.sanacorp.services;

import com.sanacorp.models.Horario;
import com.sanacorp.repositories.HorarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Servicio para la gestión de horarios médicos
 * Contiene la lógica de negocio para operaciones relacionadas con horarios
 */
@Service
@Transactional
public class HorarioService {
    
    @Autowired
    private HorarioRepository horarioRepository;
    
    /**
     * Obtiene los horarios de un médico
     * @param medicoId ID del médico
     * @return Lista de horarios del médico
     */
    @Transactional(readOnly = true)
    public List<Horario> getHorariosPorMedico(Long medicoId) {
        return horarioRepository.findByMedicoId(medicoId);
    }
    
    /**
     * Obtiene los horarios activos de un médico
     * @param medicoId ID del médico
     * @return Lista de horarios activos del médico
     */
    @Transactional(readOnly = true)
    public List<Horario> getHorariosActivosPorMedico(Long medicoId) {
        return horarioRepository.findByMedicoId(medicoId);
    }
    
    /**
     * Obtiene horarios por día de la semana
     * @param diaSemana Día de la semana (1=Lunes, 7=Domingo)
     * @return Lista de horarios para el día especificado
     */
    public List<Horario> getHorariosPorDia(Integer diaSemana) {
        return horarioRepository.findByDiaSemana(diaSemana);
    }
    
    /**
     * Obtiene el horario de un médico para un día específico
     * @param medicoId ID del médico
     * @param diaSemana Día de la semana (1=Lunes, 7=Domingo)
     * @return Lista de horarios encontrados
     */
    @Transactional(readOnly = true)
    public List<Horario> getHorarioPorMedicoYDia(Long medicoId, Integer diaSemana) {
        return horarioRepository.findByMedicoIdAndDiaSemana(medicoId, diaSemana);
    }
    
    /**
     * Verifica si un médico tiene disponibilidad en un día y horario específico
     * @param medicoId ID del médico
     * @param diaSemana Día de la semana (1=Lunes, 7=Domingo)
     * @param horaInicio Hora de inicio de la cita
     * @param horaFin Hora de fin de la cita
     * @return true si el médico tiene disponibilidad, false en caso contrario
     */
    @Transactional(readOnly = true)
    public boolean medicoTieneDisponibilidad(Long medicoId, Integer diaSemana, LocalTime horaInicio, LocalTime horaFin) {
        List<Horario> horarios = horarioRepository.findByMedicoIdAndDiaSemana(medicoId, diaSemana);
        
        if (horarios.isEmpty()) {
            return false;
        }
        
        // Verificar si algún horario cubre el rango solicitado
        for (Horario horario : horarios) {
            if (!horaInicio.isBefore(horario.getHoraInicio()) && 
                !horaFin.isAfter(horario.getHoraFin())) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Registra un nuevo horario para un médico
     * @param horario Datos del horario a registrar
     * @return Horario registrado
     * @throws RuntimeException si hay errores de validación
     */
    @Transactional
    public Horario registrarHorario(Horario horario) {
        // Validar que la hora de inicio sea anterior a la hora de fin
        if (!horario.getHoraInicio().isBefore(horario.getHoraFin())) {
            throw new RuntimeException("La hora de inicio debe ser anterior a la hora de fin");
        }
        
        // Validar que el día de la semana esté en el rango válido (1-7)
        if (horario.getDiaSemana() < 1 || horario.getDiaSemana() > 7) {
            throw new RuntimeException("El día de la semana debe estar entre 1 (Lunes) y 7 (Domingo)");
        }
        
        // Verificar si hay horarios que se superponen (excluyendo el horario actual)
        List<Horario> horariosExistentes = horarioRepository.findHorariosSuperpuestos(
            horario.getMedico().getId(),
            horario.getDiaSemana(),
            horario.getHoraInicio(),
            horario.getHoraFin()
        );
        
        if (!horariosExistentes.isEmpty()) {
            throw new IllegalArgumentException("Ya existe un horario que se superpone con el horario especificado");
        }
        
        return horarioRepository.save(horario);
    }
    
    /**
     * Actualiza un horario existente
     * @param horario Datos del horario a actualizar
     * @return Horario actualizado
     * @throws RuntimeException si hay errores de validación
     */
    @Transactional
    public Horario actualizarHorario(Horario horario) {
        Optional<Horario> horarioExistente = horarioRepository.findById(horario.getIdHorario());
        if (horarioExistente.isEmpty()) {
            throw new RuntimeException("El horario no existe");
        }
        
        // Validar que la hora de inicio sea anterior a la hora de fin
        if (!horario.getHoraInicio().isBefore(horario.getHoraFin())) {
            throw new RuntimeException("La hora de inicio debe ser anterior a la hora de fin");
        }
        
        // Validar que el día de la semana esté en el rango válido (1-7)
        if (horario.getDiaSemana() < 1 || horario.getDiaSemana() > 7) {
            throw new RuntimeException("El día de la semana debe estar entre 1 (Lunes) y 7 (Domingo)");
        }
        
        // Verificar que no haya solapamiento con otros horarios del mismo médico (excluyendo el actual)
        List<Horario> horariosExistentes = horarioRepository.findHorariosSuperpuestos(
            horario.getMedico().getId(),
            horario.getDiaSemana(),
            horario.getHoraInicio(),
            horario.getHoraFin()
        );
        
        // Filtrar el horario actual de la lista de conflictos
        horariosExistentes = horariosExistentes.stream()
            .filter(h -> !h.getIdHorario().equals(horario.getIdHorario()))
            .toList();
        
        if (!horariosExistentes.isEmpty()) {
            throw new RuntimeException("El horario se solapa con otro horario existente del médico");
        }
        
        return horarioRepository.save(horario);
    }
    

    
    /**
     * Obtiene un horario por su ID
     * @param id ID del horario
     * @return Optional con el horario encontrado
     */
    @Transactional(readOnly = true)
    public Optional<Horario> getHorarioById(Long id) {
        return horarioRepository.findById(id);
    }
    
    /**
     * Obtiene todos los horarios activos con información del médico
     * @return Lista de horarios activos con datos del médico
     */
    public List<Horario> getAllHorariosActivosConMedico() {
        return horarioRepository.findAllActivosWithMedico();
    }
    
    /**
     * Verifica si un médico tiene horarios activos
     * @param medicoId ID del médico
     * @return true si el médico tiene horarios activos
     */
    public boolean tieneHorariosActivos(Long medicoId) {
        return horarioRepository.existsByMedicoIdAndActivoTrue(medicoId);
    }
    
    /**
     * Verifica disponibilidad de un médico en una fecha y hora específica
     * @param medicoId ID del médico
     * @param fechaHora Fecha y hora a verificar
     * @return true si el médico está disponible
     */
    public boolean verificarDisponibilidad(Long medicoId, LocalDateTime fechaHora) {
        // Obtener día de la semana (1=Lunes, 7=Domingo)
        int diaSemana = fechaHora.getDayOfWeek().getValue();
        LocalTime hora = fechaHora.toLocalTime();
        
        // Buscar horarios del médico para ese día
        List<Horario> horariosDelDia = horarioRepository.findByMedicoIdAndDiaSemana(medicoId, diaSemana);
        
        if (horariosDelDia.isEmpty()) {
            return false;
        }
        
        // Verificar si la hora está dentro de algún horario disponible
        for (Horario horario : horariosDelDia) {
            if (hora.isAfter(horario.getHoraInicio().minusMinutes(1)) && 
                hora.isBefore(horario.getHoraFin())) {
                return true;
            }
        }
        
        return false;
    }
}