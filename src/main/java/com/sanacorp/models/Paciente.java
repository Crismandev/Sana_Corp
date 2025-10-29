package com.sanacorp.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad que representa los pacientes del hospital
 * Basada en la tabla 'pacientes' de la base de datos
 */
@Entity
@Table(name = "pacientes")
public class Paciente {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    

    
    // Relación One-to-One con Persona
    @OneToOne
    @JoinColumn(name = "id_persona", referencedColumnName = "id", nullable = false)
    private Persona persona;
    
    @Size(max = 50, message = "El seguro médico no puede exceder 50 caracteres")
    @Column(name = "seguro_medico", length = 50)
    private String seguroMedico;
    
    // Relación One-to-Many con Cita
    @OneToMany(mappedBy = "paciente", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Cita> citas = new ArrayList<>();
    
    // Constructores
    public Paciente() {
    }
    
    public Paciente(Persona persona) {
        this.persona = persona;
    }
    
    public Paciente(Persona persona, String seguroMedico) {
        this.persona = persona;
        this.seguroMedico = seguroMedico;
    }
    
    // Getters y Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getSeguroMedico() {
        return seguroMedico;
    }
    
    public void setSeguroMedico(String seguroMedico) {
        this.seguroMedico = seguroMedico;
    }
    
    public Persona getPersona() {
        return persona;
    }
    
    public void setPersona(Persona persona) {
        this.persona = persona;
    }
    
    public List<Cita> getCitas() {
        return citas;
    }
    
    public void setCitas(List<Cita> citas) {
        this.citas = citas;
    }
    
    // Métodos de utilidad
    public void addCita(Cita cita) {
        this.citas.add(cita);
        cita.setPaciente(this);
    }
    
    public void removeCita(Cita cita) {
        this.citas.remove(cita);
        cita.setPaciente(null);
    }
    
    public String getNombreCompleto() {
        return persona != null ? persona.getNombreCompleto() : "";
    }
    
    public String getDni() {
        return persona != null ? persona.getDni() : "";
    }
    
    @Override
    public String toString() {
        return "Paciente{" +
                "id=" + id +
                ", nombreCompleto='" + getNombreCompleto() + '\'' +
                ", dni='" + getDni() + '\'' +
                ", seguroMedico='" + seguroMedico + '\'' +
                '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Paciente)) return false;
        Paciente paciente = (Paciente) o;
        return id != null && id.equals(paciente.id);
    }
    
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}