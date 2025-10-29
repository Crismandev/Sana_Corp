package com.cibertec.proyecto.entity;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "personas")
public class Persona {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @OneToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;
    
    private String dni;
    private String nombre;
    private String apellido;
    
    @Temporal(TemporalType.DATE)
    @Column(name = "fecha_nacimiento")
    private Date fechaNacimiento;
    
    private String genero;
    private String telefono;
    private String direccion;
    private String distrito;
    private String provincia;
    private String departamento;
    private String email;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_registro")
    private Date fechaRegistro;
    
    // Relación con Paciente (si existe)
    @OneToOne(mappedBy = "persona")
    private Paciente paciente;
    
    // Relación con Medico (si existe)
    @OneToOne(mappedBy = "persona")
    private Medico medico;
    
    // Constructor
    public Persona() {
        this.fechaRegistro = new Date();
    }
    
    // Getters y Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    
    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    
    public Date getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(Date fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }
    
    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }
    
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    
    public String getDistrito() { return distrito; }
    public void setDistrito(String distrito) { this.distrito = distrito; }
    
    public String getProvincia() { return provincia; }
    public void setProvincia(String provincia) { this.provincia = provincia; }
    
    public String getDepartamento() { return departamento; }
    public void setDepartamento(String departamento) { this.departamento = departamento; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public Date getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(Date fechaRegistro) { this.fechaRegistro = fechaRegistro; }
    
    public Paciente getPaciente() { return paciente; }
    public void setPaciente(Paciente paciente) { this.paciente = paciente; }
    
    public Medico getMedico() { return medico; }
    public void setMedico(Medico medico) { this.medico = medico; }
    
    // Método helper para nombre completo
    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }
}