package com.sanacorp.models;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad que representa los médicos del hospital
 * Basada en la tabla 'medicos' de la base de datos
 */
@Entity
@Table(name = "medicos")
public class Medico {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    // Relación One-to-One con Persona
    @OneToOne
    @JoinColumn(name = "id_persona", referencedColumnName = "id", nullable = false)
    private Persona persona;
    
    // Relación Many-to-One con Especialidad
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "especialidad_id", referencedColumnName = "id", nullable = false)
    private Especialidad especialidad;
    
    // Relación One-to-Many con Horario
    @OneToMany(mappedBy = "medico", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Horario> horarios = new ArrayList<>();
    
    // Relación One-to-Many con Cita
    @OneToMany(mappedBy = "medico", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Cita> citas = new ArrayList<>();
    
    // Constructores
    public Medico() {}
    
    public Medico(Persona persona, Especialidad especialidad) {
        this.persona = persona;
        this.especialidad = especialidad;
    }
    
    // Getters y Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Persona getPersona() {
        return persona;
    }
    
    public void setPersona(Persona persona) {
        this.persona = persona;
    }
    
    public Especialidad getEspecialidad() {
        return especialidad;
    }
    
    public void setEspecialidad(Especialidad especialidad) {
        this.especialidad = especialidad;
    }
    
    public List<Horario> getHorarios() {
        return horarios;
    }
    
    public void setHorarios(List<Horario> horarios) {
        this.horarios = horarios;
    }
    
    public List<Cita> getCitas() {
        return citas;
    }
    
    public void setCitas(List<Cita> citas) {
        this.citas = citas;
    }
    
    // Métodos de utilidad
    public void addHorario(Horario horario) {
        this.horarios.add(horario);
        horario.setMedico(this);
    }
    
    public void removeHorario(Horario horario) {
        this.horarios.remove(horario);
        horario.setMedico(null);
    }
    
    public void addCita(Cita cita) {
        this.citas.add(cita);
        cita.setMedico(this);
    }
    
    public void removeCita(Cita cita) {
        this.citas.remove(cita);
        cita.setMedico(null);
    }
    
    public String getNombreCompleto() {
        return persona != null ? persona.getNombreCompleto() : "";
    }
    
    @Override
    public String toString() {
        return "Medico{" +
                "id=" + id +
                ", nombreCompleto='" + getNombreCompleto() + '\'' +
                '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Medico)) return false;
        Medico medico = (Medico) o;
        return id != null && id.equals(medico.id);
    }
    
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}