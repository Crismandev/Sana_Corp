package com.sanacorp.repositories;

import com.sanacorp.models.Medico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

/**
 * Repositorio para la entidad Medico
 * Contiene métodos de consulta personalizados para operaciones específicas del módulo Secretario
 */
@Repository
public interface MedicoRepository extends JpaRepository<Medico, Long> {
    
    /**
     * Busca médicos por especialidad
     * @param especialidadId ID de la especialidad
     * @return Lista de médicos de la especialidad especificada
     */
    @Query("SELECT m FROM Medico m JOIN m.especialidad e WHERE e.id = :especialidadId")
    List<Medico> findByEspecialidadId(@Param("especialidadId") Long especialidadId);
    
    /**
     * Busca médicos activos con sus especialidades
     * @return Lista de médicos activos con especialidades cargadas
     */
    @Query("SELECT m FROM Medico m JOIN FETCH m.especialidad e JOIN FETCH m.persona p ORDER BY p.apellido, p.nombre")
    List<Medico> findAllActivosWithEspecialidad();
    
    /**
     * Busca médicos por nombre o apellidos
     * @param texto Texto a buscar en nombre o apellidos
     * @return Lista de médicos que coinciden
     */
    @Query("SELECT m FROM Medico m JOIN FETCH m.persona p JOIN FETCH m.especialidad e WHERE " +
           "(LOWER(p.nombre) LIKE LOWER(CONCAT('%', :texto, '%')) OR " +
           "LOWER(p.apellido) LIKE LOWER(CONCAT('%', :texto, '%'))) " +
           "ORDER BY p.apellido, p.nombre")
    List<Medico> findByNombreContaining(@Param("texto") String texto);
    
    /**
     * Busca médicos disponibles para programar citas
     * @return Lista de médicos disponibles
     */
    @Query("SELECT m FROM Medico m JOIN FETCH m.especialidad e JOIN FETCH m.persona p ORDER BY e.nombre, p.apellido")
    List<Medico> findMedicosDisponibles();
    
    /**
     * Cuenta el número de médicos por especialidad
     * @param especialidadId ID de la especialidad
     * @return Número de médicos en la especialidad
     */
    @Query("SELECT COUNT(m) FROM Medico m WHERE m.especialidad.id = :especialidadId")
    Long countByEspecialidadId(@Param("especialidadId") Long especialidadId);
}