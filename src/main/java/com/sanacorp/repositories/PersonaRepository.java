package com.sanacorp.repositories;

import com.sanacorp.models.Persona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

/**
 * Repositorio para la entidad Persona
 * Contiene métodos de consulta personalizados para operaciones específicas
 */
@Repository
public interface PersonaRepository extends JpaRepository<Persona, Long> {
    
    /**
     * Busca una persona por su DNI
     * @param dni DNI de la persona
     * @return Optional con la persona encontrada
     */
    @Query("SELECT p FROM Persona p WHERE p.dni = :dni")
    Optional<Persona> findByDni(@Param("dni") String dni);
    
    /**
     * Busca personas por nombre o apellidos
     * @param texto Texto a buscar en nombre o apellidos
     * @return Lista de personas que coinciden
     */
    @Query("SELECT p FROM Persona p WHERE LOWER(p.nombre) LIKE LOWER(CONCAT('%', :texto, '%')) OR LOWER(p.apellido) LIKE LOWER(CONCAT('%', :texto, '%'))")
    List<Persona> findByNombreOrApellidos(@Param("texto") String texto);
    
    /**
     * Busca personas por género
     * @param genero Género de la persona (M/F)
     * @return Lista de personas del género especificado
     */
    @Query("SELECT p FROM Persona p WHERE p.genero = :genero")
    List<Persona> findByGenero(@Param("genero") String genero);
    
    /**
     * Verifica si existe una persona con el DNI dado
     * @param dni DNI de la persona
     * @return true si existe, false en caso contrario
     */
    boolean existsByDni(String dni);
}