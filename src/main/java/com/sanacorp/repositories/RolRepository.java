package com.sanacorp.repositories;

import com.sanacorp.models.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

/**
 * Repositorio para la entidad Rol
 * Contiene métodos de consulta personalizados para operaciones específicas
 */
@Repository
public interface RolRepository extends JpaRepository<Rol, Long> {
    
    /**
     * Busca un rol por su nombre
     * @param nombreRol Nombre del rol
     * @return Optional con el rol encontrado
     */
    @Query("SELECT r FROM Rol r WHERE r.nombre = :nombre")
    Optional<Rol> findByNombre(@Param("nombre") String nombre);
    
    /**
     * Busca roles que contengan el texto especificado en su nombre
     * @param texto Texto a buscar
     * @return Lista de roles que coinciden
     */
    @Query("SELECT r FROM Rol r WHERE r.nombre LIKE CONCAT('%', :texto, '%') OR r.descripcion LIKE CONCAT('%', :texto, '%')")
    List<Rol> findByNombreOrDescripcionContaining(@Param("texto") String texto);
    
    /**
     * Verifica si existe un rol con el nombre dado
     * @param nombre Nombre del rol
     * @return true si existe, false en caso contrario
     */
    boolean existsByNombre(String nombre);
}