package com.cibertec.proyecto.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "usuarios")
public class Usuario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "username")
    private String username;
    
    @Column(name = "email")
    private String email;
    
    @Column(name = "password_hash")
    private String passwordHash;
    
    @Column(name = "estado")
    private Integer estado = 1;
    
    @OneToOne(mappedBy = "usuario")
    private Persona persona;
    
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "usuario_roles",
        joinColumns = @JoinColumn(name = "id_usuario"),
        inverseJoinColumns = @JoinColumn(name = "id_rol")
    )
    private List<Rol> roles;
    
    // Constructor vacío
    public Usuario() {}
    
    // Getters y Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    
    public Integer getEstado() { return estado; }
    public void setEstado(Integer estado) { this.estado = estado; }
    
    public Persona getPersona() { return persona; }
    public void setPersona(Persona persona) { this.persona = persona; }
    
    public List<Rol> getRoles() { return roles; }
    public void setRoles(List<Rol> roles) { this.roles = roles; }
    
    // Método helper para verificar rol
    public boolean hasRole(String roleName) {
        if (roles == null) return false;
        return roles.stream().anyMatch(rol -> rol.getNombre().equals(roleName));
    }
    
    // Método para obtener rol principal
    public String getRolPrincipal() {
        if (roles == null || roles.isEmpty()) return null;
        
        // Prioridad de roles
        if (hasRole("Administrador")) return "Administrador";
        if (hasRole("Médico")) return "Médico"; 
        if (hasRole("Secretario")) return "Secretario";
        if (hasRole("Cliente")) return "Cliente";
        
        return roles.get(0).getNombre();
    }
}