package com.cibertec.proyecto.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "pacientes")
public class Paciente {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @OneToOne
    @JoinColumn(name = "id_persona")
    private Persona persona;
    
    @Column(name = "seguro_medico")
    private String seguroMedico;
    
    // Constructor
    public Paciente() {}
    
    // Getters y Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    
    public Persona getPersona() { return persona; }
    public void setPersona(Persona persona) { this.persona = persona; }
    
    public String getSeguroMedico() { return seguroMedico; }
    public void setSeguroMedico(String seguroMedico) { this.seguroMedico = seguroMedico; }
    
    // Método helper para obtener datos de la persona
    public String getNombreCompleto() {
        return persona != null ? persona.getNombreCompleto() : "";
    }
    
    public String getDni() {
        return persona != null ? persona.getDni() : "";
    }
}