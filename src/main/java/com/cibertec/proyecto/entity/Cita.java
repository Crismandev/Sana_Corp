package com.cibertec.proyecto.entity;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "citas")
public class Cita {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @ManyToOne
    @JoinColumn(name = "paciente_id")
    private Paciente paciente;
    
    @ManyToOne
    @JoinColumn(name = "medico_id")
    private Medico medico;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_hora")
    private Date fechaHora;
    
    private String motivo;
    
    @Enumerated(EnumType.STRING)
    private EstadoCita estado;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_registro")
    private Date fechaRegistro;
    
    // Constructor
    public Cita() {
        this.fechaRegistro = new Date();
        this.estado = EstadoCita.Programada;  // Ahora con P mayúscula
    }
    
    // Getters y Setters (mantener igual)
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    
    public Paciente getPaciente() { return paciente; }
    public void setPaciente(Paciente paciente) { this.paciente = paciente; }
    
    public Medico getMedico() { return medico; }
    public void setMedico(Medico medico) { this.medico = medico; }
    
    public Date getFechaHora() { return fechaHora; }
    public void setFechaHora(Date fechaHora) { this.fechaHora = fechaHora; }
    
    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
    
    public EstadoCita getEstado() { return estado; }
    public void setEstado(EstadoCita estado) { this.estado = estado; }
    
    public Date getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(Date fechaRegistro) { this.fechaRegistro = fechaRegistro; }
    
    // Métodos helper
    public String getNombrePaciente() {
        return paciente != null ? paciente.getNombreCompleto() : "";
    }
    
    public String getNombreMedico() {
        return medico != null ? medico.getNombreCompleto() : "";
    }
    
    public String getEspecialidadMedico() {
        return medico != null ? medico.getEspecialidadNombre() : "";
    }
}