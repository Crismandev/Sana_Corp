package com.sanacorp.repositories;

import com.sanacorp.models.Consultorio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la entidad Consultorio
 * Contiene métodos de consulta personalizados para operaciones específicas
 */
@Repository
public interface ConsultorioRepository extends JpaRepository<Consultorio, Long> {
    
    /**
     * Obtiene todos los consultorios ordenados por nombre
     * @return Lista de consultorios ordenados por nombre
     */
    @Query("SELECT c FROM Consultorio c ORDER BY c.nombre")
    List<Consultorio> findAllActivos();
    
    /**
     * Busca un consultorio por su nombre
     * @param nombre Nombre del consultorio
     * @return Optional con el consultorio encontrado
     */
    @Query("SELECT c FROM Consultorio c WHERE c.nombre = :nombre")
    Optional<Consultorio> findByNombre(@Param("nombre") String nombre);
    
    /**
     * Busca consultorios por ubicación
     * @param ubicacion Ubicación del consultorio
     * @return Lista de consultorios en la ubicación especificada
     */
    @Query("SELECT c FROM Consultorio c WHERE LOWER(c.ubicacion) LIKE LOWER(CONCAT('%', :ubicacion, '%')) ORDER BY c.nombre")
    List<Consultorio> findByUbicacionContaining(@Param("ubicacion") String ubicacion);
    
    /**
     * Verifica si existe un consultorio con el nombre dado
     * @param nombre Nombre del consultorio
     * @return true si existe, false en caso contrario
     */
    boolean existsByNombre(String nombre);
}