package com.cibertec.proyecto.controller;

import com.cibertec.proyecto.entity.Cita;
import com.cibertec.proyecto.entity.Medico;
import com.cibertec.proyecto.entity.Paciente;
import com.cibertec.proyecto.entity.Especialidad;
import com.cibertec.proyecto.service.CitaService;
import com.cibertec.proyecto.service.MedicoService;
import com.cibertec.proyecto.service.EspecialidadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/medico")
public class MedicoController {

    @Autowired
    private MedicoService medicoService;

    @Autowired
    private CitaService citaService;

    @Autowired
    private EspecialidadService especialidadService;

    // MI AGENDA - Vista mejorada
    @GetMapping("/agenda")
    public String miAgenda(Model model) {
        // Para pruebas - cambiar por usuario de sesión después
        Integer medicoId = 1; // ID del médico en sesión
        
        Optional<Medico> medico = medicoService.buscarPorId(medicoId);
        if (medico.isPresent()) {
            List<Cita> citas = citaService.buscarPorMedico(medicoId);
            
            model.addAttribute("medico", medico.get());
            model.addAttribute("citas", citas);
            
            // Estadísticas para la agenda
            long citasHoy = citas.stream()
                .filter(c -> c.getEstado().name().equals("Programada"))
                .count();
            model.addAttribute("citasHoy", citasHoy);
            
            return "medico/agenda";
        } else {
            model.addAttribute("error", "No tienes un perfil de médico registrado");
            return "medico/perfil";
        }
    }

    // MIS PACIENTES - Lista de pacientes atendidos
    @GetMapping("/mis-pacientes")
    public String misPacientes(Model model) {
        // Para pruebas - cambiar por usuario de sesión después
        Integer medicoId = 1; // ID del médico en sesión
        
        Optional<Medico> medico = medicoService.buscarPorId(medicoId);
        if (medico.isPresent()) {
            List<Cita> citas = citaService.buscarPorMedico(medicoId);
            
            // Extraer pacientes únicos
            Set<Paciente> pacientesUnicos = citas.stream()
                .map(Cita::getPaciente)
                .collect(Collectors.toSet());
            
            model.addAttribute("medico", medico.get());
            model.addAttribute("pacientes", pacientesUnicos);
            model.addAttribute("totalPacientes", pacientesUnicos.size());
            
            return "medico/mis-pacientes";
        } else {
            model.addAttribute("error", "No tienes un perfil de médico registrado");
            return "medico/perfil";
        }
    }

    // PERFIL DEL MÉDICO - Vista mejorada
    @GetMapping("/perfil")
    public String perfilMedico(Model model) {
        // Para pruebas - cambiar por usuario de sesión después
        Integer usuarioId = 2; // ID del usuario "medico01" en tu BD
        
        Optional<Medico> medico = medicoService.buscarPorUsuarioId(usuarioId);
        
        if (medico.isPresent()) {
            // Obtener estadísticas para el perfil
            List<Cita> citas = citaService.buscarPorMedico(medico.get().getId());
            Set<Paciente> pacientesUnicos = citas.stream()
                .map(Cita::getPaciente)
                .collect(Collectors.toSet());
            
            model.addAttribute("medico", medico.get());
            model.addAttribute("especialidades", especialidadService.listarTodas());
            model.addAttribute("totalCitas", citas.size());
            model.addAttribute("totalPacientes", pacientesUnicos.size());
            
            return "medico/perfil";
        } else {
            model.addAttribute("error", "No se encontró perfil de médico registrado");
            return "medico/perfil";
        }
    }

    // LISTAR TODOS LOS MÉDICOS
    @GetMapping("/lista")
    public String listaMedicos(Model model) {
        model.addAttribute("medicos", medicoService.listarTodos());
        model.addAttribute("especialidades", especialidadService.listarTodas());
        return "medico/lista";
    }

    // BUSCAR MÉDICOS
    @GetMapping("/buscar")
    public String buscarMedicos(@RequestParam(required = false) Integer especialidadId,
                               @RequestParam(required = false) String nombre,
                               Model model) {
        List<Medico> medicos;
        
        if (especialidadId != null) {
            medicos = medicoService.buscarPorEspecialidad(especialidadId);
        } else if (nombre != null && !nombre.trim().isEmpty()) {
            medicos = medicoService.buscarPorNombre(nombre);
        } else {
            medicos = medicoService.listarTodos();
        }
        
        model.addAttribute("medicos", medicos);
        model.addAttribute("especialidades", especialidadService.listarTodas());
        return "medico/buscar";
    }

    // ACTUALIZAR ESPECIALIDAD DEL MÉDICO
    @PostMapping("/actualizar-especialidad")
    public String actualizarEspecialidad(@RequestParam Integer medicoId,
                                        @RequestParam Integer especialidadId,
                                        RedirectAttributes redirectAttributes) {
        try {
            Optional<Medico> medicoOpt = medicoService.buscarPorId(medicoId);
            Optional<Especialidad> especialidadOpt = especialidadService.buscarPorId(especialidadId);
            
            if (medicoOpt.isPresent() && especialidadOpt.isPresent()) {
                Medico medico = medicoOpt.get();
                medico.setEspecialidad(especialidadOpt.get());
                medicoService.guardar(medico);
                
                redirectAttributes.addFlashAttribute("success", "Especialidad actualizada exitosamente");
            } else {
                redirectAttributes.addFlashAttribute("error", "Médico o especialidad no encontrados");
            }
            return "redirect:/medico/lista";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al actualizar especialidad: " + e.getMessage());
            return "redirect:/medico/lista";
        }
    }

    // MÉTODO EXISTENTE (para mantener compatibilidad)
    @GetMapping("/pacientes")
    public String misPacientesOld(Model model) {
        // Para pruebas - cambiar por usuario de sesión después
        Integer usuarioId = 2;
        
        Optional<Medico> medico = medicoService.buscarPorUsuarioId(usuarioId);
        if (medico.isPresent()) {
            model.addAttribute("medico", medico.get());
            model.addAttribute("mensaje", "Funcionalidad de pacientes en desarrollo");
            return "medico/pacientes";
        } else {
            model.addAttribute("error", "No tienes un perfil de médico registrado");
            return "medico/perfil";
        }
    }
}