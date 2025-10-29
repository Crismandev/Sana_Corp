package com.cibertec.proyecto.entity;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "historial_medico")
public class HistorialMedico {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @ManyToOne
    @JoinColumn(name = "cita_id")
    private Cita cita;
    
    @Column(columnDefinition = "TEXT")
    private String notas;
    
    private String archivosAdjuntos;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_registro")
    private Date fechaRegistro;
    
    // Constructor
    public HistorialMedico() {
        this.fechaRegistro = new Date();
    }
    
    // Getters y Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    
    public Cita getCita() { return cita; }
    public void setCita(Cita cita) { this.cita = cita; }
    
    public String getNotas() { return notas; }
    public void setNotas(String notas) { this.notas = notas; }
    
    public String getArchivosAdjuntos() { return archivosAdjuntos; }
    public void setArchivosAdjuntos(String archivosAdjuntos) { this.archivosAdjuntos = archivosAdjuntos; }
    
    public Date getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(Date fechaRegistro) { this.fechaRegistro = fechaRegistro; }
    
    // Métodos helper
    public String getNombreMedico() {
        return cita != null && cita.getMedico() != null ? cita.getMedico().getNombreCompleto() : "";
    }
    
    public String getEspecialidadMedico() {
        return cita != null && cita.getMedico() != null ? cita.getMedico().getEspecialidadNombre() : "";
    }
    
    public Date getFechaCita() {
        return cita != null ? cita.getFechaHora() : null;
    }
    
    public String getMotivoCita() {
        return cita != null ? cita.getMotivo() : "";
    }
}