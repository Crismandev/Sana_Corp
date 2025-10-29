package com.sanacorp.services;

import com.sanacorp.models.Medico;
import com.sanacorp.repositories.MedicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

/**
 * Servicio para la gestión de médicos
 * Contiene la lógica de negocio para operaciones relacionadas con médicos
 */
@Service
@Transactional
public class MedicoService {
    
    @Autowired
    private MedicoRepository medicoRepository;
    
    /**
     * Obtiene todos los médicos activos con sus especialidades
     * @return Lista de médicos activos con especialidades
     */
    @Transactional(readOnly = true)
    public List<Medico> getAllMedicosActivos() {
        return medicoRepository.findAllActivosWithEspecialidad();
    }
    
    /**
     * Obtiene médicos por especialidad
     * @param especialidadId ID de la especialidad
     * @return Lista de médicos de la especialidad especificada
     */
    @Transactional(readOnly = true)
    public List<Medico> getMedicosPorEspecialidad(Long especialidadId) {
        return medicoRepository.findByEspecialidadId(especialidadId);
    }
    
    /**
     * Busca médicos por nombre o apellidos
     * @param texto Texto a buscar
     * @return Lista de médicos que coinciden
     */
    public List<Medico> buscarMedicosPorNombre(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            return getAllMedicosActivos();
        }
        return medicoRepository.findByNombreContaining(texto);
    }
    
    /**
     * Obtiene médicos disponibles para citas
     * @return Lista de médicos disponibles
     */
    public List<Medico> getMedicosDisponiblesParaCita() {
        return medicoRepository.findMedicosDisponibles();
    }
    

    
    /**
     * Obtiene un médico por su ID
     * @param id ID del médico
     * @return Optional con el médico encontrado
     */
    @Transactional(readOnly = true)
    public Optional<Medico> getMedicoById(Long id) {
        return medicoRepository.findById(id);
    }
    
    /**
     * Cuenta el número de médicos por especialidad
     * @param especialidadId ID de la especialidad
     * @return Número de médicos en la especialidad
     */
    @Transactional(readOnly = true)
    public Long contarMedicosPorEspecialidad(Long especialidadId) {
        return medicoRepository.countByEspecialidadId(especialidadId);
    }
    
    /**
     * Lista todos los médicos
     * @return Lista de todos los médicos
     */
    @Transactional(readOnly = true)
    public List<Medico> listarTodos() {
        return medicoRepository.findAll();
    }
    
    /**
     * Registra un nuevo médico
     * @param medico Datos del médico a registrar
     * @return Médico registrado
     */
    @Transactional
    public Medico registrarMedico(Medico medico) {
        return medicoRepository.save(medico);
    }
    
    /**
     * Actualiza los datos de un médico
     * @param medico Datos del médico a actualizar
     * @return Médico actualizado
     * @throws RuntimeException si hay errores de validación
     */
    @Transactional
    public Medico actualizarMedico(Medico medico) {
        Optional<Medico> medicoExistente = medicoRepository.findById(medico.getId());
        if (medicoExistente.isEmpty()) {
            throw new RuntimeException("El médico no existe");
        }
        
        return medicoRepository.save(medico);
    }
    

}