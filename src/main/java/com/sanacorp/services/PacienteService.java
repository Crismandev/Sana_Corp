package com.sanacorp.services;

import com.sanacorp.models.Paciente;
import com.sanacorp.models.Persona;
import com.sanacorp.repositories.PacienteRepository;
import com.sanacorp.repositories.PersonaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

/**
 * Servicio para la gestión de pacientes
 * Contiene la lógica de negocio para operaciones relacionadas con pacientes
 */
@Service
@Transactional
public class PacienteService {
    
    @Autowired
    private PacienteRepository pacienteRepository;
    
    @Autowired
    private PersonaRepository personaRepository;
    
    /**
     * Registra un nuevo paciente en el sistema
     * @param paciente Datos del paciente a registrar
     * @return Paciente registrado
     * @throws RuntimeException si ya existe un paciente con el mismo DNI
     */
    @Transactional
    public Paciente registrarNuevoPaciente(Paciente paciente) {
        // Validar que no exista una persona con el mismo DNI
        if (personaRepository.existsByDni(paciente.getPersona().getDni())) {
            throw new RuntimeException("Ya existe una persona registrada con el DNI: " + paciente.getPersona().getDni());
        }
        
        // Guardar la persona primero
        Persona personaGuardada = personaRepository.save(paciente.getPersona());
        
        // Asignar la persona guardada al paciente
        paciente.setPersona(personaGuardada);
        
        // Guardar el paciente
        return pacienteRepository.save(paciente);
    }
    
    /**
     * Busca pacientes por DNI o nombre
     * @param criterio Criterio de búsqueda (DNI o nombre)
     * @return Lista de pacientes que coinciden con el criterio
     */
    @Transactional(readOnly = true)
    public List<Paciente> buscarPacientes(String criterio) {
        if (criterio == null || criterio.trim().isEmpty()) {
            return pacienteRepository.findAll();
        }
        
        criterio = criterio.trim();
        
        // Si el criterio parece ser un DNI (8 dígitos), buscar por DNI
        if (criterio.matches("\\d{8}")) {
            Optional<Paciente> paciente = pacienteRepository.findByPersonaDni(criterio);
            return paciente.map(List::of).orElse(List.of());
        }
        
        // Buscar por nombre o apellidos
        return pacienteRepository.findByNombreContaining(criterio);
    }
    
    /**
     * Obtiene un paciente por su ID
     * @param id ID del paciente
     * @return Optional con el paciente encontrado
     */
    @Transactional(readOnly = true)
    public Optional<Paciente> getPacienteById(Long id) {
        return pacienteRepository.findById(id);
    }
    
    /**
     * Obtiene todos los pacientes
     * @return Lista de todos los pacientes
     */
    @Transactional(readOnly = true)
    public List<Paciente> getAllPacientes() {
        return pacienteRepository.findAll();
    }
    
    /**
     * Actualiza los datos de un paciente existente
     * @param paciente Paciente con los datos actualizados
     * @return Paciente actualizado
     */
    @Transactional
    public Paciente actualizarPaciente(Paciente paciente) {
        // Verificar que el paciente existe
        if (!pacienteRepository.existsById(paciente.getId())) {
            throw new RuntimeException("El paciente con ID " + paciente.getId() + " no existe");
        }
        
        // Actualizar la persona asociada
        if (paciente.getPersona() != null) {
            personaRepository.save(paciente.getPersona());
        }
        
        // Actualizar el paciente
        return pacienteRepository.save(paciente);
    }
    

    
    /**
     * Busca un paciente por DNI
     * @param dni DNI del paciente
     * @return Optional con el paciente encontrado
     */
    @Transactional(readOnly = true)
    public Optional<Paciente> buscarPorDni(String dni) {
        return pacienteRepository.findByPersonaDni(dni);
    }
    
    /**
     * Obtiene el número total de pacientes
     * @return Número de pacientes
     */
    @Transactional(readOnly = true)
    public Long contarPacientes() {
        return pacienteRepository.countPacientes();
    }
}