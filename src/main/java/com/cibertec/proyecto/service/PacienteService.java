package com.cibertec.proyecto.service;

import com.cibertec.proyecto.entity.Paciente;
import com.cibertec.proyecto.entity.Persona;
import com.cibertec.proyecto.entity.Usuario;
import com.cibertec.proyecto.repository.PacienteRepository;
import com.cibertec.proyecto.repository.PersonaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class PacienteService {
    
    @Autowired
    private PacienteRepository pacienteRepository;
    
    @Autowired
    private PersonaRepository personaRepository;
    
    public List<Paciente> listarTodos() {
        return pacienteRepository.findAll();
    }
    
    public Optional<Paciente> buscarPorId(Integer id) {
        return pacienteRepository.findById(id);
    }
    
    public Optional<Paciente> buscarPorDni(String dni) {
        return pacienteRepository.findByPersonaDni(dni);
    }
    
    public Optional<Paciente> buscarPorUsuarioId(Integer usuarioId) {
        return pacienteRepository.findByPersonaUsuarioId(usuarioId);
    }
    
    public Paciente guardar(Paciente paciente) {
        return pacienteRepository.save(paciente);
    }
    
    // Crear paciente desde usuario
    public Paciente crearPacienteDesdeUsuario(Usuario usuario, String dni, String nombre, String apellido) {
        // Crear persona
        Persona persona = new Persona();
        persona.setUsuario(usuario);
        persona.setDni(dni);
        persona.setNombre(nombre);
        persona.setApellido(apellido);
        persona = personaRepository.save(persona);
        
        // Crear paciente
        Paciente paciente = new Paciente();
        paciente.setPersona(persona);
        return pacienteRepository.save(paciente);
    }
}