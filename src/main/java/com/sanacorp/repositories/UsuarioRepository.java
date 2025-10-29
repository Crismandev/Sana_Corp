package com.sanacorp.repositories;

import com.sanacorp.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

/**
 * Repositorio para la entidad Usuario
 * Contiene métodos de consulta personalizados para operaciones específicas
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
    /**
     * Busca un usuario por su nombre de usuario
     * @param username Nombre de usuario
     * @return Optional con el usuario encontrado
     */
    @Query("SELECT u FROM Usuario u LEFT JOIN FETCH u.roles WHERE u.username = :username")
    Optional<Usuario> findByUsername(@Param("username") String username);
    
    /**
     * Busca un usuario por su email
     * @param email Email del usuario
     * @return Optional con el usuario encontrado
     */
    @Query("SELECT u FROM Usuario u WHERE u.email = :email")
    Optional<Usuario> findByEmail(@Param("email") String email);
    
    /**
     * Busca usuarios activos
     * @return Lista de usuarios activos
     */
    @Query("SELECT u FROM Usuario u WHERE u.estado = 1")
    List<Usuario> findByEstadoActivo();
    
    /**
     * Busca usuarios por rol específico
     * @param nombreRol Nombre del rol
     * @return Lista de usuarios con el rol especificado
     */
    @Query("SELECT u FROM Usuario u JOIN u.roles r WHERE r.nombre = :nombre AND u.estado = 1")
    List<Usuario> findByRolNombre(@Param("nombre") String nombre);
    
    /**
     * Verifica si existe un usuario con el username dado
     * @param username Nombre de usuario
     * @return true si existe, false en caso contrario
     */
    boolean existsByUsername(String username);
    
    /**
     * Verifica si existe un usuario con el email dado
     * @param email Email del usuario
     * @return true si existe, false en caso contrario
     */
    boolean existsByEmail(String email);
}