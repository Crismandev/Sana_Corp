package com.sanacorp.security;

import com.sanacorp.models.Usuario;
import com.sanacorp.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Servicio personalizado para cargar detalles del usuario
 * Implementa UserDetailsService de Spring Security
 * 
 * @author Sana Corp Development Team
 * @version 1.0
 */
@Service
@Transactional
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * Carga un usuario por su nombre de usuario
     * 
     * @param username Nombre de usuario
     * @return UserDetails con la información del usuario
     * @throws UsernameNotFoundException Si el usuario no existe
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        // Verificar si el usuario está activo
        if (usuario.getEstado() != 1) {
            throw new UsernameNotFoundException("Usuario inactivo: " + username);
        }

        return User.builder()
            .username(usuario.getUsername())
            .password(usuario.getPassword())
            .disabled(usuario.getEstado() != 1)
            .authorities(mapRolesToAuthorities(usuario))
            .build();
    }

    /**
     * Convierte los roles del usuario en autoridades de Spring Security
     * 
     * @param usuario Usuario con roles
     * @return Colección de autoridades
     */
    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Usuario usuario) {
        return usuario.getRoles().stream()
            .map(rol -> new SimpleGrantedAuthority("ROLE_" + rol.getNombre().toUpperCase()))
            .collect(Collectors.toList());
    }

    /**
     * Obtiene un usuario por su nombre de usuario
     * 
     * @param username Nombre de usuario
     * @return Usuario encontrado
     * @throws UsernameNotFoundException Si el usuario no existe
     */
    public Usuario findByUsername(String username) throws UsernameNotFoundException {
        return usuarioRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));
    }
}