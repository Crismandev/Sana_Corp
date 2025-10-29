package com.sanacorp.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;

/**
 * Manejador personalizado de éxito de autenticación
 * Redirige a los usuarios según su rol después del login exitoso
 * 
 * @author Sana Corp Development Team
 * @version 1.0
 */
@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    /**
     * Maneja el éxito de autenticación redirigiendo según el rol del usuario
     * 
     * @param request Petición HTTP
     * @param response Respuesta HTTP
     * @param authentication Información de autenticación
     * @throws IOException Si hay error de E/S
     * @throws ServletException Si hay error del servlet
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, 
                                      HttpServletResponse response, 
                                      Authentication authentication) throws IOException, ServletException {
        
        String targetUrl = determineTargetUrl(authentication);
        
        if (response.isCommitted()) {
            return;
        }
        
        redirectStrategy.sendRedirect(request, response, targetUrl);
    }

    /**
     * Determina la URL de destino según los roles del usuario
     * 
     * @param authentication Información de autenticación
     * @return URL de destino
     */
    protected String determineTargetUrl(Authentication authentication) {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        
        for (GrantedAuthority grantedAuthority : authorities) {
            String authority = grantedAuthority.getAuthority();
            
            switch (authority) {
                case "ROLE_ADMINISTRADOR":
                    return "/admin/dashboard";
                case "ROLE_SECRETARIO":
                    return "/secretario/dashboard";
                case "ROLE_MEDICO":
                    return "/medico/dashboard";
                default:
                    break;
            }
        }
        
        // URL por defecto si no se encuentra un rol específico
        return "/dashboard";
    }
}