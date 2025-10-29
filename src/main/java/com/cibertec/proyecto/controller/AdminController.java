package com.cibertec.proyecto.controller;

import com.cibertec.proyecto.entity.Usuario;
import com.cibertec.proyecto.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // PÁGINA PRINCIPAL DEL ADMIN - REDIRIGIR AL DASHBOARD
    @GetMapping({"", "/"})
    public String adminHome() {
        return "redirect:/admin/dashboard";
    }

    // 1. LISTA MEJORADA DE USUARIOS
    @GetMapping("/usuarios")
    public String listaUsuarios(Model model) {
        model.addAttribute("usuarios", usuarioRepository.findAll());
        model.addAttribute("totalUsuarios", usuarioRepository.count());
        
        // Contar usuarios activos/inactivos
        long usuariosActivos = usuarioRepository.findAll().stream()
                .filter(u -> u.getEstado() == 1)
                .count();
        long usuariosInactivos = usuarioRepository.findAll().stream()
                .filter(u -> u.getEstado() == 0)
                .count();
        
        model.addAttribute("usuariosActivos", usuariosActivos);
        model.addAttribute("usuariosInactivos", usuariosInactivos);
        
        return "admin/usuarios";
    }

    // 2. FORMULARIO para nuevo usuario
    @GetMapping("/usuarios/nuevo")
    public String formularioNuevoUsuario(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "admin/form-usuario";
    }

    // 3. GUARDAR usuario nuevo
    @PostMapping("/usuarios/guardar")
    public String guardarUsuario(@ModelAttribute Usuario usuario) {
        usuarioRepository.save(usuario);
        return "redirect:/admin/usuarios?exito";
    }

    // 4. CAMBIAR ESTADO (activar/desactivar)
    @GetMapping("/usuarios/estado/{id}")
    public String cambiarEstado(@PathVariable Integer id) {
        usuarioRepository.findById(id).ifPresent(usuario -> {
            usuario.setEstado(usuario.getEstado() == 1 ? 0 : 1);
            usuarioRepository.save(usuario);
        });
        return "redirect:/admin/usuarios";
    }
    
    // 5. EDITAR USUARIO (formulario)
    @GetMapping("/usuarios/editar/{id}")
    public String formularioEditarUsuario(@PathVariable Integer id, Model model) {
        usuarioRepository.findById(id).ifPresent(usuario -> {
            model.addAttribute("usuario", usuario);
        });
        return "admin/form-usuario-editar";
    }
}