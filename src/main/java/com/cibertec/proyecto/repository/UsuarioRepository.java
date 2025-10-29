package com.cibertec.proyecto.repository;

import com.cibertec.proyecto.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findByUsername(String username);
    Optional<Usuario> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    
    // Buscar por rol
    @Query("SELECT u FROM Usuario u JOIN u.roles r WHERE r.nombre = :rolNombre")
    List<Usuario> findByRolNombre(@Param("rolNombre") String rolNombre);
}