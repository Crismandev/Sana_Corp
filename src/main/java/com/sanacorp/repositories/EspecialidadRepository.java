package com.sanacorp.repositories;

import com.sanacorp.models.Especialidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la entidad Especialidad
 * Contiene métodos de consulta personalizados para operaciones específicas
 */
@Repository
public interface EspecialidadRepository extends JpaRepository<Especialidad, Long> {
    
    /**
     * Busca todas las especialidades ordenadas por nombre
     * @return Lista de especialidades ordenadas por nombre
     */
    @Query("SELECT e FROM Especialidad e ORDER BY e.nombre")
    List<Especialidad> findAllActivas();
    
    /**
     * Busca una especialidad por su nombre
     * @param nombre Nombre de la especialidad
     * @return Optional con la especialidad encontrada
     */
    @Query("SELECT e FROM Especialidad e WHERE e.nombre = :nombre")
    Optional<Especialidad> findByNombre(@Param("nombre") String nombre);
    
    /**
     * Busca especialidades que contengan el texto especificado
     * @param texto Texto a buscar
     * @return Lista de especialidades que coinciden
     */
    @Query("SELECT e FROM Especialidad e WHERE " +
           "(LOWER(e.nombre) LIKE LOWER(CONCAT('%', :texto, '%')) OR " +
           "LOWER(e.descripcion) LIKE LOWER(CONCAT('%', :texto, '%'))) " +
           "ORDER BY e.nombre")
    List<Especialidad> findByNombreOrDescripcionContaining(@Param("texto") String texto);
    
    /**
     * Verifica si existe una especialidad con el nombre dado
     * @param nombre Nombre de la especialidad
     * @return true si existe, false en caso contrario
     */
    boolean existsByNombre(String nombre);
}