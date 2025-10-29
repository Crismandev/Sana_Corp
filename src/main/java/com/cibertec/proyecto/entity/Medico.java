package com.cibertec.proyecto.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "medicos")
public class Medico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "id_persona")
    private Persona persona;

    @ManyToOne
    @JoinColumn(name = "especialidad_id")
    private Especialidad especialidad;

    // Constructor
    public Medico() {}

    // Getters y Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Persona getPersona() { return persona; }
    public void setPersona(Persona persona) { this.persona = persona; }

    public Especialidad getEspecialidad() { return especialidad; }
    public void setEspecialidad(Especialidad especialidad) { this.especialidad = especialidad; }

    // ========== MÉTODOS DE APOYO PARA VISTA ==========

    public String getNombreCompleto() {
        return persona != null ? persona.getNombreCompleto() : "";
    }

    public String getEspecialidadNombre() {
        return especialidad != null ? especialidad.getNombre() : "";
    }

    public String getEmail() {
        if (persona != null) {
            if (persona.getEmail() != null && !persona.getEmail().isEmpty()) {
                return persona.getEmail();
            }
            if (persona.getUsuario() != null && persona.getUsuario().getEmail() != null) {
                return persona.getUsuario().getEmail();
            }
        }
        return "Sin correo registrado";
    }

    public String getTelefono() {
        if (persona != null && persona.getTelefono() != null && !persona.getTelefono().isEmpty()) {
            return persona.getTelefono();
        }
        return "No registrado";
    }

    public boolean isActivo() {
        return persona != null && persona.getUsuario() != null && persona.getUsuario().getEstado() == 1;
    }
}
