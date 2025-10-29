package com.sanacorp.config;

import com.sanacorp.models.Rol;
import com.sanacorp.models.Usuario;
import com.sanacorp.repositories.RolRepository;
import com.sanacorp.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Inicializador de datos para crear usuarios y roles por defecto
 */
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private RolRepository rolRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Verificar si ya existen datos
        if (usuarioRepository.count() == 0) {
            System.out.println("Inicializando datos de prueba...");
            
            // Crear rol de administrador si no existe
            Rol rolAdmin = rolRepository.findByNombre("Administrador")
                .orElseGet(() -> {
                    Rol nuevoRol = new Rol();
                    nuevoRol.setNombre("Administrador");
                    nuevoRol.setDescripcion("Control total del sistema");
                    return rolRepository.save(nuevoRol);
                });
            
            // Crear usuario administrador de prueba
            Usuario adminUser = new Usuario();
            adminUser.setUsername("admin01");
            adminUser.setPassword(passwordEncoder.encode("password123"));
            adminUser.setEmail("admin@sanacorp.com");
            adminUser.setEstado(1);
            adminUser.setFechaRegistro(LocalDateTime.now());
            adminUser.addRol(rolAdmin);
            
            usuarioRepository.save(adminUser);
            
            System.out.println("Usuario de prueba creado:");
            System.out.println("Username: admin01");
            System.out.println("Password: password123");
            System.out.println("Rol: " + rolAdmin.getNombre());
        }
    }
}