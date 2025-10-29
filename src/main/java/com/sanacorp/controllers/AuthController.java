package com.sanacorp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controlador de autenticación
 * Maneja las páginas de login y logout
 * 
 * @author Sana Corp Development Team
 * @version 1.0
 */
@Controller
public class AuthController {

    /**
     * Muestra la página de login
     * 
     * @param error Parámetro que indica si hubo error en el login
     * @param logout Parámetro que indica si se hizo logout
     * @param expired Parámetro que indica si la sesión expiró
     * @param model Modelo para pasar datos a la vista
     * @return Nombre de la vista de login
     */
    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error,
                       @RequestParam(value = "logout", required = false) String logout,
                       @RequestParam(value = "expired", required = false) String expired,
                       Model model) {
        
        if (error != null) {
            model.addAttribute("error", "Usuario o contraseña incorrectos");
        }
        
        if (logout != null) {
            model.addAttribute("message", "Has cerrado sesión correctamente");
        }
        
        if (expired != null) {
            model.addAttribute("error", "Tu sesión ha expirado. Por favor, inicia sesión nuevamente");
        }
        
        return "login";
    }

    /**
     * Redirige a la página principal después del login exitoso
     * Esta ruta es manejada por CustomAuthenticationSuccessHandler
     */
    @GetMapping("/")
    public String home() {
        return "redirect:/login";
    }
}