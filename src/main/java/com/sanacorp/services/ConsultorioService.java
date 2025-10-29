package com.sanacorp.services;

import com.sanacorp.models.Consultorio;
import com.sanacorp.repositories.ConsultorioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

/**
 * Servicio para la gestión de consultorios
 * Contiene la lógica de negocio para operaciones relacionadas con consultorios
 */
@Service
@Transactional
public class ConsultorioService {
    
    @Autowired
    private ConsultorioRepository consultorioRepository;
    
    /**
     * Obtiene todos los consultorios activos
     * @return Lista de consultorios activos
     */
    @Transactional(readOnly = true)
    public List<Consultorio> getAllConsultoriosActivos() {
        return consultorioRepository.findAllActivos();
    }
    
    /**
     * Obtiene un consultorio por su nombre
     * @param nombre Nombre del consultorio
     * @return Optional con el consultorio encontrado
     */
    @Transactional(readOnly = true)
    public Optional<Consultorio> getConsultorioPorNombre(String nombre) {
        return consultorioRepository.findByNombre(nombre);
    }
    
    /**
     * Busca consultorios por ubicación
     * @param ubicacion Ubicación del consultorio
     * @return Lista de consultorios en la ubicación especificada
     */
    @Transactional(readOnly = true)
    public List<Consultorio> getConsultoriosPorUbicacion(String ubicacion) {
        return consultorioRepository.findByUbicacionContaining(ubicacion);
    }
    
    /**
     * Obtiene un consultorio por su ID
     * @param id ID del consultorio
     * @return Optional con el consultorio encontrado
     */
    @Transactional(readOnly = true)
    public Optional<Consultorio> getConsultorioById(Long id) {
        return consultorioRepository.findById(id);
    }
    
    /**
     * Registra un nuevo consultorio
     * @param consultorio Consultorio a registrar
     * @return Consultorio registrado
     * @throws RuntimeException si ya existe un consultorio con el mismo nombre
     */
    @Transactional
    public Consultorio registrarConsultorio(Consultorio consultorio) {
        // Validar que no exista otro consultorio con el mismo nombre
        if (consultorioRepository.existsByNombre(consultorio.getNombre())) {
            throw new RuntimeException("Ya existe un consultorio con el nombre: " + consultorio.getNombre());
        }
        
        return consultorioRepository.save(consultorio);
    }
    
    /**
     * Actualiza los datos de un consultorio
     * @param consultorio Datos del consultorio a actualizar
     * @return Consultorio actualizado
     * @throws RuntimeException si hay errores de validación
     */
    @Transactional
    public Consultorio actualizarConsultorio(Consultorio consultorio) {
        Optional<Consultorio> consultorioExistente = consultorioRepository.findById(consultorio.getId());
        if (consultorioExistente.isEmpty()) {
            throw new RuntimeException("El consultorio no existe");
        }
        
        // Validar que no exista otro consultorio con el mismo nombre (excluyendo el actual)
        Optional<Consultorio> consultorioConMismoNombre = consultorioRepository.findByNombre(consultorio.getNombre());
        if (consultorioConMismoNombre.isPresent() && 
            !consultorioConMismoNombre.get().getId().equals(consultorio.getId())) {
            throw new RuntimeException("Ya existe otro consultorio con el nombre: " + consultorio.getNombre());
        }
        
        return consultorioRepository.save(consultorio);
    }
    

}