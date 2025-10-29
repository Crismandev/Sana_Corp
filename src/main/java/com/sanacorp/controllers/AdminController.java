package com.sanacorp.controllers;

import com.sanacorp.services.CitaService;
import com.sanacorp.services.ConsultorioService;
import com.sanacorp.services.EspecialidadService;
import com.sanacorp.services.MedicoService;
import com.sanacorp.services.PacienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controlador del módulo Administrador
 * Maneja las funcionalidades administrativas del sistema hospitalario
 * 
 * @author Sana Corp Development Team
 * @version 1.0
 */
@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMINISTRADOR')")
public class AdminController {

    @Autowired
    private CitaService citaService;
    
    @Autowired
    private MedicoService medicoService;
    
    @Autowired
    private PacienteService pacienteService;
    
    @Autowired
    private EspecialidadService especialidadService;
    
    @Autowired
    private ConsultorioService consultorioService;

    /**
     * Dashboard principal del administrador
     * Muestra estadísticas generales y accesos rápidos
     * 
     * @param model Modelo para pasar datos a la vista
     * @return Vista del dashboard
     */
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        try {
            // Obtener estadísticas generales usando los métodos disponibles
            long totalMedicos = medicoService.listarTodos().size();
            long totalPacientes = pacienteService.getAllPacientes().size();
            long totalEspecialidades = especialidadService.listarTodas().size();
            long totalConsultorios = consultorioService.getAllConsultoriosActivos().size();
            long totalCitas = citaService.getCitasDelDia().size();
            long citasHoy = citaService.getCitasPorFecha(java.time.LocalDate.now()).size();
            
            // Agregar estadísticas al modelo
            model.addAttribute("totalMedicos", totalMedicos);
            model.addAttribute("totalPacientes", totalPacientes);
            model.addAttribute("totalEspecialidades", totalEspecialidades);
            model.addAttribute("totalConsultorios", totalConsultorios);
            model.addAttribute("totalCitas", totalCitas);
            model.addAttribute("citasHoy", citasHoy);
            
        } catch (Exception e) {
            // En caso de error, mostrar valores por defecto
            model.addAttribute("totalMedicos", 0L);
            model.addAttribute("totalPacientes", 0L);
            model.addAttribute("totalEspecialidades", 0L);
            model.addAttribute("totalConsultorios", 0L);
            model.addAttribute("totalCitas", 0L);
            model.addAttribute("citasHoy", 0L);
        }
        
        return "admin/dashboard";
    }

    /**
     * Gestión de médicos
     */
    @GetMapping("/medicos")
    public String medicos(Model model) {
        model.addAttribute("medicos", medicoService.listarTodos());
        return "admin/medicos";
    }

    /**
     * Gestión de especialidades
     */
    @GetMapping("/especialidades")
    public String especialidades(Model model) {
        model.addAttribute("especialidades", especialidadService.listarTodas());
        return "admin/especialidades";
    }

    /**
     * Gestión de consultorios
     */
    @GetMapping("/consultorios")
    public String consultorios(Model model) {
        model.addAttribute("consultorios", consultorioService.getAllConsultoriosActivos());
        return "admin/consultorios";
    }

    /**
     * Reportes y estadísticas
     */
    @GetMapping("/reportes")
    public String reportes(Model model) {
        // Aquí se pueden agregar más estadísticas y reportes
        return "admin/reportes";
    }

    /**
     * Configuración del sistema
     */
    @GetMapping("/configuracion")
    public String configuracion() {
        return "admin/configuracion";
    }
}