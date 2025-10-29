package com.cibertec.proyecto.service;

import com.cibertec.proyecto.entity.Usuario;
import com.cibertec.proyecto.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }
    
    public Optional<Usuario> buscarPorId(Integer id) {
        return usuarioRepository.findById(id);
    }
    
    public Optional<Usuario> buscarPorUsername(String username) {
        return usuarioRepository.findByUsername(username);
    }
    
    public Usuario guardar(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }
    
    public void eliminar(Integer id) {
        usuarioRepository.deleteById(id);
    }
    
    public boolean existeUsername(String username) {
        return usuarioRepository.existsByUsername(username);
    }
    
    public boolean existeEmail(String email) {
        return usuarioRepository.existsByEmail(email);
    }
    
    // Buscar usuarios por rol
    public List<Usuario> buscarPorRol(String rolNombre) {
        return usuarioRepository.findByRolNombre(rolNombre);
    }
}