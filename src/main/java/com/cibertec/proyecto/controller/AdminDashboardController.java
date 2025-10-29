package com.cibertec.proyecto.controller;

import com.cibertec.proyecto.entity.Cita;
import com.cibertec.proyecto.entity.Rol;
import com.cibertec.proyecto.entity.Usuario;
import com.cibertec.proyecto.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminDashboardController {

    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private MedicoService medicoService;
    
    @Autowired
    private PacienteService pacienteService;
    
    @Autowired
    private CitaService citaService;
    
    @Autowired
    private EspecialidadService especialidadService;

    // 🏠 DASHBOARD PRINCIPAL
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        // Obtener estadísticas
        Map<String, Object> estadisticas = obtenerEstadisticas();
        model.addAttribute("estadisticas", estadisticas);
        
        return "admin/dashboard";
    }

    // 📅 GESTIÓN DE CITAS - VISTA ADMIN
    @GetMapping("/citas")
    public String gestionCitas(Model model) {
        // Obtener todas las citas del sistema
        List<Cita> citas = citaService.listarTodas();
        model.addAttribute("citas", citas);
        
        return "admin/citas";
    }

    // 📈 REPORTES DEL SISTEMA
    @GetMapping("/reportes")
    public String reportesSistema(Model model) {
        // Obtener datos para reportes
        Map<String, Object> reportes = obtenerDatosReportes();
        model.addAttribute("reportes", reportes);
        
        return "admin/reportes";
    }

    // 📊 MÉTODO PARA OBTENER ESTADÍSTICAS
    private Map<String, Object> obtenerEstadisticas() {
        Map<String, Object> stats = new HashMap<>();
        
        // Estadísticas básicas
        stats.put("totalUsuarios", usuarioService.listarTodos().size());
        stats.put("totalMedicos", medicoService.listarTodos().size());
        stats.put("totalPacientes", pacienteService.listarTodos().size());
        stats.put("totalCitas", citaService.listarTodas().size());
        stats.put("totalEspecialidades", especialidadService.listarTodas().size());
        
        // Contar citas por estado
        long citasPendientes = citaService.listarTodas().stream()
                .filter(c -> c.getEstado().name().equals("Programada"))
                .count();
        long citasCompletadas = citaService.listarTodas().stream()
                .filter(c -> c.getEstado().name().equals("Completada"))
                .count();
        long citasCanceladas = citaService.listarTodas().stream()
                .filter(c -> c.getEstado().name().equals("Cancelada"))
                .count();
        
        stats.put("citasPendientes", citasPendientes);
        stats.put("citasCompletadas", citasCompletadas);
        stats.put("citasCanceladas", citasCanceladas);
        
        return stats;
    }

    // 📈 MÉTODO PARA OBTENER DATOS DE REPORTES
    private Map<String, Object> obtenerDatosReportes() {
        Map<String, Object> reportes = new HashMap<>();
        
        // Usuarios por rol
        List<Usuario> usuarios = usuarioService.listarTodos();
        Map<String, Long> usuariosPorRol = new HashMap<>();
        
        for (Usuario usuario : usuarios) {
            if (usuario.getRoles() != null) {
                for (Rol rol : usuario.getRoles()) {
                    String nombreRol = rol.getNombre();
                    usuariosPorRol.put(nombreRol, usuariosPorRol.getOrDefault(nombreRol, 0L) + 1);
                }
            }
        }
        reportes.put("usuariosPorRol", usuariosPorRol);
        
        // Citas por médico
        List<Cita> citas = citaService.listarTodas();
        Map<String, Long> citasPorMedico = new HashMap<>();
        
        for (Cita cita : citas) {
            if (cita.getMedico() != null) {
                String nombreMedico = cita.getMedico().getNombreCompleto();
                citasPorMedico.put(nombreMedico, citasPorMedico.getOrDefault(nombreMedico, 0L) + 1);
            }
        }
        reportes.put("citasPorMedico", citasPorMedico);
        
        // Citas por especialidad
        Map<String, Long> citasPorEspecialidad = new HashMap<>();
        for (Cita cita : citas) {
            if (cita.getMedico() != null && cita.getMedico().getEspecialidad() != null) {
                String especialidad = cita.getMedico().getEspecialidad().getNombre();
                citasPorEspecialidad.put(especialidad, citasPorEspecialidad.getOrDefault(especialidad, 0L) + 1);
            }
        }
        reportes.put("citasPorEspecialidad", citasPorEspecialidad);
        
        // Citas por estado (ya lo tenemos)
        reportes.put("citasPendientes", citas.stream().filter(c -> c.getEstado().name().equals("Programada")).count());
        reportes.put("citasCompletadas", citas.stream().filter(c -> c.getEstado().name().equals("Completada")).count());
        reportes.put("citasCanceladas", citas.stream().filter(c -> c.getEstado().name().equals("Cancelada")).count());
        
        return reportes;
    }
}