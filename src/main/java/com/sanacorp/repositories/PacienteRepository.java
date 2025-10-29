package com.sanacorp.repositories;

import com.sanacorp.models.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

/**
 * Repositorio para la entidad Paciente
 * Contiene métodos de consulta personalizados para operaciones específicas del módulo Secretario
 */
@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    
    /**
     * Busca pacientes por DNI
     * @param dni DNI del paciente
     * @return Optional con el paciente encontrado
     */
    @Query("SELECT p FROM Paciente p JOIN p.persona per WHERE per.dni = :dni")
    Optional<Paciente> findByPersonaDni(@Param("dni") String dni);
    
    /**
     * Busca pacientes por nombre o apellidos
     * @param texto Texto a buscar en nombre o apellidos
     * @return Lista de pacientes que coinciden
     */
    @Query("SELECT p FROM Paciente p JOIN p.persona per WHERE " +
           "(LOWER(per.nombre) LIKE LOWER(CONCAT('%', :texto, '%')) OR " +
           "LOWER(per.apellido) LIKE LOWER(CONCAT('%', :texto, '%')))")
    List<Paciente> findByNombreContaining(@Param("texto") String texto);
    
    /**
     * Busca pacientes por DNI o nombre (método combinado para el buscador)
     * @param dni DNI del paciente
     * @param nombre Texto a buscar en nombre o apellidos
     * @return Lista de pacientes que coinciden
     */
    @Query("SELECT p FROM Paciente p JOIN p.persona per WHERE " +
           "(per.dni = :dni OR " +
           "LOWER(per.nombre) LIKE LOWER(CONCAT('%', :nombre, '%')) OR " +
           "LOWER(per.apellido) LIKE LOWER(CONCAT('%', :nombre, '%')))")
    List<Paciente> findByDniOrNombre(@Param("dni") String dni, @Param("nombre") String nombre);
    
    /**
     * Cuenta el número total de pacientes
     * @return Número de pacientes
     */
    @Query("SELECT COUNT(p) FROM Paciente p")
    Long countPacientes();
}