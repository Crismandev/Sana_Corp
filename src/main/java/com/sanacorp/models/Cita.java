package com.sanacorp.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * Entidad que representa las citas médicas
 * Basada en la tabla 'citas' de la base de datos
 */
@Entity
@Table(name = "citas")
public class Cita {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long idCita;
    
    @NotNull(message = "La fecha y hora de la cita es obligatoria")
    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fechaHora;
    
    @Size(max = 255, message = "El motivo no puede exceder 255 caracteres")
    @Column(name = "motivo_consulta")
    private String motivo;
    
    @NotNull(message = "El estado de la cita es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    private EstadoCita estado = EstadoCita.PROGRAMADA;
    
    @Size(max = 500, message = "Las observaciones no pueden exceder 500 caracteres")
    @Column(name = "observaciones", length = 500)
    private String observaciones;
    
    @Column(name = "fecha_registro", nullable = false)
    private LocalDateTime fechaCreacion;
    
    // Relación Many-to-One con Paciente
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paciente_id", referencedColumnName = "id", nullable = false)
    private Paciente paciente;
    
    // Relación Many-to-One con Medico
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medico_id", referencedColumnName = "id", nullable = false)
    private Medico medico;
    
    // Relación Many-to-One con Consultorio
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "consultorio_id", referencedColumnName = "id")
    private Consultorio consultorio;
    
    // Enum para el estado de la cita
    public enum EstadoCita {
        PROGRAMADA("Programada"),
        CONFIRMADA("Confirmada"),
        EN_CURSO("En Curso"),
        COMPLETADA("Completada"),
        CANCELADA("Cancelada"),
        NO_ASISTIO("No Asistió");
        
        private final String descripcion;
        
        EstadoCita(String descripcion) {
            this.descripcion = descripcion;
        }
        
        public String getDescripcion() {
            return descripcion;
        }
    }
    
    // Constructores
    public Cita() {
        this.fechaCreacion = LocalDateTime.now();
    }
    
    public Cita(LocalDateTime fechaHora, String motivo, Paciente paciente, Medico medico, Consultorio consultorio) {
        this();
        this.fechaHora = fechaHora;
        this.motivo = motivo;
        this.paciente = paciente;
        this.medico = medico;
        this.consultorio = consultorio;
    }
    
    // Getters y Setters
    public Long getIdCita() {
        return idCita;
    }
    
    public void setIdCita(Long idCita) {
        this.idCita = idCita;
    }
    
    public LocalDateTime getFechaHora() {
        return fechaHora;
    }
    
    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }
    
    public String getMotivo() {
        return motivo;
    }
    
    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }
    
    public EstadoCita getEstado() {
        return estado;
    }
    
    public void setEstado(EstadoCita estado) {
        this.estado = estado;
    }
    
    public String getObservaciones() {
        return observaciones;
    }
    
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
    
    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }
    
    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
    
    public Paciente getPaciente() {
        return paciente;
    }
    
    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }
    
    public Medico getMedico() {
        return medico;
    }
    
    public void setMedico(Medico medico) {
        this.medico = medico;
    }
    
    public Consultorio getConsultorio() {
        return consultorio;
    }
    
    public void setConsultorio(Consultorio consultorio) {
        this.consultorio = consultorio;
    }
    
    // Métodos de utilidad
    public boolean isProgramada() {
        return estado == EstadoCita.PROGRAMADA;
    }
    
    public boolean isConfirmada() {
        return estado == EstadoCita.CONFIRMADA;
    }
    
    public boolean isCompletada() {
        return estado == EstadoCita.COMPLETADA;
    }
    
    public boolean isCancelada() {
        return estado == EstadoCita.CANCELADA;
    }
    
    public boolean puedeSerCancelada() {
        return estado == EstadoCita.PROGRAMADA || estado == EstadoCita.CONFIRMADA;
    }
    
    public boolean puedeSerCompletada() {
        return estado == EstadoCita.CONFIRMADA || estado == EstadoCita.EN_CURSO;
    }
    
    @Override
    public String toString() {
        return "Cita{" +
                "idCita=" + idCita +
                ", fechaHora=" + fechaHora +
                ", motivo='" + motivo + '\'' +
                ", estado=" + estado +
                ", paciente=" + (paciente != null ? paciente.getNombreCompleto() : "null") +
                ", medico=" + (medico != null ? medico.getNombreCompleto() : "null") +
                '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cita)) return false;
        Cita cita = (Cita) o;
        return idCita != null && idCita.equals(cita.idCita);
    }
    
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}