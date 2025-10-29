package com.sanacorp.services;

import com.sanacorp.models.Cita;
import com.sanacorp.models.Cita.EstadoCita;
import com.sanacorp.models.Consultorio;
import com.sanacorp.models.Medico;
import com.sanacorp.models.Paciente;
import com.sanacorp.repositories.CitaRepository;
import com.sanacorp.repositories.ConsultorioRepository;
import com.sanacorp.repositories.MedicoRepository;
import com.sanacorp.repositories.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

/**
 * Servicio para la gestión de citas médicas
 * Contiene la lógica de negocio para operaciones relacionadas con citas
 */
@Service
@Transactional
public class CitaService {
    
    @Autowired
    private CitaRepository citaRepository;
    
    @Autowired
    private PacienteRepository pacienteRepository;
    
    @Autowired
    private MedicoRepository medicoRepository;
    
    @Autowired
    private ConsultorioRepository consultorioRepository;
    
    @Autowired
    private HorarioService horarioService;
    
    // Duración estándar de una cita en minutos
    private static final int DURACION_CITA_MINUTOS = 30;
    
    /**
     * Obtiene las citas para una fecha específica
     * @param fecha Fecha de las citas
     * @return Lista de citas en la fecha especificada
     */
    @Transactional(readOnly = true)
    public List<Cita> getCitasPorFecha(LocalDate fecha) {
        return citaRepository.findByFecha(fecha);
    }
    
    /**
     * Obtiene las citas del día actual
     * @return Lista de citas del día actual
     */
    @Transactional(readOnly = true)
    public List<Cita> getCitasDelDia() {
        return citaRepository.findCitasDelDia();
    }
    
    /**
     * Obtiene las citas próximas (siguientes 7 días)
     * @return Lista de citas próximas
     */
    @Transactional(readOnly = true)
    public List<Cita> getCitasProximas() {
        LocalDateTime fechaLimite = LocalDateTime.now().plusDays(7);
        return citaRepository.findCitasProximas(fechaLimite);
    }
    
    /**
     * Obtiene las citas de un paciente
     * @param pacienteId ID del paciente
     * @return Lista de citas del paciente
     */
    @Transactional(readOnly = true)
    public List<Cita> getCitasPorPaciente(Long pacienteId) {
        return citaRepository.findByPacienteId(pacienteId);
    }
    
    /**
     * Obtiene las citas de un médico en una fecha específica
     * @param medicoId ID del médico
     * @param fecha Fecha de las citas
     * @return Lista de citas del médico en la fecha especificada
     */
    @Transactional(readOnly = true)
    public List<Cita> getCitasPorMedicoYFecha(Long medicoId, LocalDate fecha) {
        return citaRepository.findByMedicoIdAndFecha(medicoId, fecha);
    }
    
    /**
     * Registra una nueva cita médica con validaciones
     * @param cita Datos de la cita a registrar
     * @return Cita registrada
     * @throws RuntimeException si hay errores de validación
     */
    @Transactional
    public Cita registrarNuevaCita(Cita cita) {
        // Validar que la fecha no sea en el pasado
        if (cita.getFechaHora().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("No se puede programar una cita en el pasado");
        }
        
        // Validar que el paciente exista
        Optional<Paciente> pacienteOpt = pacienteRepository.findById(cita.getPaciente().getId());
        if (pacienteOpt.isEmpty()) {
            throw new RuntimeException("El paciente seleccionado no existe");
        }
        
        // Validar que el médico exista
        Optional<Medico> medicoOpt = medicoRepository.findById(cita.getMedico().getId());
        if (medicoOpt.isEmpty()) {
            throw new RuntimeException("El médico seleccionado no existe");
        }
        
        // Validar que el consultorio exista
        Optional<Consultorio> consultorioOpt = consultorioRepository.findById(cita.getConsultorio().getId());
        if (consultorioOpt.isEmpty()) {
            throw new RuntimeException("El consultorio seleccionado no existe");
        }
        
        // Validar que la fecha corresponda a un día laborable (lunes a sábado)
        DayOfWeek diaSemana = cita.getFechaHora().getDayOfWeek();
        if (diaSemana == DayOfWeek.SUNDAY) {
            throw new RuntimeException("No se pueden programar citas los domingos");
        }
        
        // Convertir DayOfWeek a Integer (1=Lunes, 7=Domingo)
        int diaSemanaInt = diaSemana.getValue();
        
        // Validar que el médico tenga horario disponible para ese día y hora
        LocalTime horaInicio = cita.getFechaHora().toLocalTime();
        LocalTime horaFin = horaInicio.plusMinutes(DURACION_CITA_MINUTOS);
        
        if (!horarioService.medicoTieneDisponibilidad(cita.getMedico().getId(), diaSemanaInt, horaInicio, horaFin)) {
            throw new RuntimeException("El médico no tiene horario disponible para la fecha y hora seleccionada");
        }
        
        // Validar que no haya conflictos con otras citas del médico
        LocalDateTime fechaInicio = cita.getFechaHora();
        LocalDateTime fechaFin = fechaInicio.plusMinutes(DURACION_CITA_MINUTOS);
        
        List<Cita> citasConflicto = citaRepository.findConflictosHorario(
            cita.getMedico().getId(), fechaInicio, fechaFin);
        
        if (!citasConflicto.isEmpty()) {
            throw new RuntimeException("El médico ya tiene una cita programada en ese horario");
        }
        
        // Establecer estado inicial y fecha de creación
        cita.setEstado(EstadoCita.PROGRAMADA);
        cita.setFechaCreacion(LocalDateTime.now());
        
        // Asignar entidades completas
        cita.setPaciente(pacienteOpt.get());
        cita.setMedico(medicoOpt.get());
        cita.setConsultorio(consultorioOpt.get());
        
        // Guardar la cita
        return citaRepository.save(cita);
    }
    
    /**
     * Cancela una cita médica
     * @param idCita ID de la cita a cancelar
     * @param observaciones Motivo de la cancelación
     * @return Cita cancelada
     * @throws RuntimeException si la cita no existe o no puede ser cancelada
     */
    @Transactional
    public Cita cancelarCita(Long idCita, String observaciones) {
        Optional<Cita> citaOpt = citaRepository.findById(idCita);
        if (citaOpt.isEmpty()) {
            throw new RuntimeException("La cita no existe");
        }
        
        Cita cita = citaOpt.get();
        
        // Validar que la cita pueda ser cancelada
        if (!cita.puedeSerCancelada()) {
            throw new RuntimeException("La cita no puede ser cancelada porque su estado actual es: " + cita.getEstado());
        }
        
        // Actualizar estado y observaciones
        cita.setEstado(EstadoCita.CANCELADA);
        cita.setObservaciones(observaciones);
        
        return citaRepository.save(cita);
    }
    
    /**
     * Marca una cita como completada
     * @param idCita ID de la cita a completar
     * @param observaciones Observaciones de la atención
     * @return Cita completada
     * @throws RuntimeException si la cita no existe o no puede ser completada
     */
    @Transactional
    public Cita completarCita(Long idCita, String observaciones) {
        Optional<Cita> citaOpt = citaRepository.findById(idCita);
        if (citaOpt.isEmpty()) {
            throw new RuntimeException("La cita no existe");
        }
        
        Cita cita = citaOpt.get();
        
        // Validar que la cita pueda ser completada
        if (!cita.puedeSerCompletada()) {
            throw new RuntimeException("La cita no puede ser completada porque su estado actual es: " + cita.getEstado());
        }
        
        // Actualizar estado y observaciones
        cita.setEstado(EstadoCita.COMPLETADA);
        cita.setObservaciones(observaciones);
        
        return citaRepository.save(cita);
    }
    
    /**
     * Confirma una cita programada
     * @param idCita ID de la cita a confirmar
     * @return Cita confirmada
     * @throws RuntimeException si la cita no existe o no está en estado PROGRAMADA
     */
    @Transactional
    public Cita confirmarCita(Long idCita) {
        Optional<Cita> citaOpt = citaRepository.findById(idCita);
        if (citaOpt.isEmpty()) {
            throw new RuntimeException("La cita no existe");
        }
        
        Cita cita = citaOpt.get();
        
        // Validar que la cita esté en estado PROGRAMADA
        if (!cita.isProgramada()) {
            throw new RuntimeException("Solo se pueden confirmar citas en estado PROGRAMADA");
        }
        
        // Actualizar estado
        cita.setEstado(EstadoCita.CONFIRMADA);
        
        return citaRepository.save(cita);
    }
    
    /**
     * Marca una cita como "No Asistió"
     * @param idCita ID de la cita
     * @param observaciones Observaciones sobre la inasistencia
     * @return Cita actualizada
     * @throws RuntimeException si la cita no existe o no está en estado PROGRAMADA o CONFIRMADA
     */
    @Transactional
    public Cita marcarInasistencia(Long idCita, String observaciones) {
        Optional<Cita> citaOpt = citaRepository.findById(idCita);
        if (citaOpt.isEmpty()) {
            throw new RuntimeException("La cita no existe");
        }
        
        Cita cita = citaOpt.get();
        
        // Validar que la cita esté en estado PROGRAMADA o CONFIRMADA
        if (!cita.isProgramada() && !cita.isConfirmada()) {
            throw new RuntimeException("Solo se pueden marcar como inasistencia las citas en estado PROGRAMADA o CONFIRMADA");
        }
        
        // Validar que la fecha de la cita sea hoy o en el pasado
        if (cita.getFechaHora().toLocalDate().isAfter(LocalDate.now())) {
            throw new RuntimeException("No se puede marcar inasistencia para citas futuras");
        }
        
        // Actualizar estado y observaciones
        cita.setEstado(EstadoCita.NO_ASISTIO);
        cita.setObservaciones(observaciones);
        
        return citaRepository.save(cita);
    }
    
    /**
     * Obtiene una cita por su ID
     * @param id ID de la cita
     * @return Optional con la cita encontrada
     */
    @Transactional(readOnly = true)
    public Optional<Cita> getCitaById(Long id) {
        return citaRepository.findById(id);
    }
    
    /**
     * Obtiene estadísticas de citas para el dashboard
     * @return Mapa con estadísticas de citas
     */
    @Transactional(readOnly = true)
    public CitaEstadisticas getEstadisticasCitas() {
        LocalDate hoy = LocalDate.now();
        
        CitaEstadisticas estadisticas = new CitaEstadisticas();
        estadisticas.setCitasHoy(citaRepository.countByEstadoAndFecha(EstadoCita.PROGRAMADA, hoy) + 
                                citaRepository.countByEstadoAndFecha(EstadoCita.CONFIRMADA, hoy));
        estadisticas.setCitasCompletadasHoy(citaRepository.countByEstadoAndFecha(EstadoCita.COMPLETADA, hoy));
        estadisticas.setCitasCanceladasHoy(citaRepository.countByEstadoAndFecha(EstadoCita.CANCELADA, hoy));
        estadisticas.setCitasInasistenciasHoy(citaRepository.countByEstadoAndFecha(EstadoCita.NO_ASISTIO, hoy));
        
        return estadisticas;
    }
    
    /**
     * Clase interna para estadísticas de citas
     */
    public static class CitaEstadisticas {
        private Long citasHoy;
        private Long citasCompletadasHoy;
        private Long citasCanceladasHoy;
        private Long citasInasistenciasHoy;
        
        public Long getCitasHoy() {
            return citasHoy;
        }
        
        public void setCitasHoy(Long citasHoy) {
            this.citasHoy = citasHoy;
        }
        
        public Long getCitasCompletadasHoy() {
            return citasCompletadasHoy;
        }
        
        public void setCitasCompletadasHoy(Long citasCompletadasHoy) {
            this.citasCompletadasHoy = citasCompletadasHoy;
        }
        
        public Long getCitasCanceladasHoy() {
            return citasCanceladasHoy;
        }
        
        public void setCitasCanceladasHoy(Long citasCanceladasHoy) {
            this.citasCanceladasHoy = citasCanceladasHoy;
        }
        
        public Long getCitasInasistenciasHoy() {
            return citasInasistenciasHoy;
        }
        
        public void setCitasInasistenciasHoy(Long citasInasistenciasHoy) {
            this.citasInasistenciasHoy = citasInasistenciasHoy;
        }
    }
}