package com.sanacorp.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sanacorp.models.Especialidad;
import com.sanacorp.repositories.EspecialidadRepository;

/**
 * Servicio para la gestión de especialidades médicas
 * Contiene la lógica de negocio para operaciones relacionadas con especialidades
 */
@Service
@Transactional
public class EspecialidadService {
    
    @Autowired
    private EspecialidadRepository especialidadRepository;
    
    /**
     * Obtiene todas las especialidades activas
     * @return Lista de especialidades activas
     */
    @Transactional(readOnly = true)
    public List<Especialidad> getAllEspecialidadesActivas() {
        return especialidadRepository.findAllActivas();
    }
    
    /**
     * Busca especialidades por nombre
     * @param nombre Nombre de la especialidad
     * @return Optional con la especialidad encontrada
     */
    @Transactional(readOnly = true)
    public Optional<Especialidad> getEspecialidadPorNombre(String nombre) {
        return especialidadRepository.findByNombre(nombre);
    }
    
    /**
     * Busca especialidades por nombre o descripción
     * @param texto Texto a buscar
     * @return Lista de especialidades que coinciden
     */
    @Transactional(readOnly = true)
    public List<Especialidad> buscarEspecialidades(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            return getAllEspecialidadesActivas();
        }
        return especialidadRepository.findByNombreOrDescripcionContaining(texto);
    }
    
    /**
     * Obtiene una especialidad por su ID
     * @param id ID de la especialidad
     * @return Optional con la especialidad encontrada
     */
    @Transactional(readOnly = true)
    public Optional<Especialidad> getEspecialidadById(Long id) {
        return especialidadRepository.findById(id);
    }
    
    /**
     * Registra una nueva especialidad
     * @param especialidad Datos de la especialidad a registrar
     * @return Especialidad registrada
     * @throws RuntimeException si ya existe una especialidad con el mismo nombre
     */
    @Transactional
    public Especialidad registrarEspecialidad(Especialidad especialidad) {
        // Verificar si ya existe una especialidad con el mismo nombre
        if (especialidadRepository.findByNombre(especialidad.getNombre()).isPresent()) {
            throw new IllegalArgumentException("Ya existe una especialidad con el nombre: " + especialidad.getNombre());
        }
        
        return especialidadRepository.save(especialidad);
    }
    
    /**
     * Actualiza los datos de una especialidad
     * @param especialidad Datos de la especialidad a actualizar
     * @return Especialidad actualizada
     * @throws RuntimeException si hay errores de validación
     */
    @Transactional
    public Especialidad actualizarEspecialidad(Especialidad especialidad) {
        Optional<Especialidad> especialidadExistente = especialidadRepository.findById(especialidad.getId());
        if (especialidadExistente.isEmpty()) {
            throw new RuntimeException("La especialidad no existe");
        }
        
        // Validar que no exista otra especialidad con el mismo nombre (excluyendo la actual)
        Optional<Especialidad> especialidadConMismoNombre = especialidadRepository.findByNombre(especialidad.getNombre());
        if (especialidadConMismoNombre.isPresent() && 
            !especialidadConMismoNombre.get().getId().equals(especialidad.getId())) {
            throw new RuntimeException("Ya existe otra especialidad con el nombre: " + especialidad.getNombre());
        }
        
        return especialidadRepository.save(especialidad);
    }
    
    /**
     * Obtiene todas las especialidades
     * @return Lista de todas las especialidades
     */
    @Transactional(readOnly = true)
    public List<Especialidad> listarTodas() {
        return especialidadRepository.findAll();
    }
}