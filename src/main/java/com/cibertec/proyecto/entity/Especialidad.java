package com.cibertec.proyecto.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "especialidades")
public class Especialidad {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    private String nombre;
    private String descripcion;
    
    // Constructor
    public Especialidad() {}
    
    // Getters y Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
}