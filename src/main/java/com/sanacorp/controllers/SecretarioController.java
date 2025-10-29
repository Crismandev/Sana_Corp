package com.sanacorp.controllers;

import com.sanacorp.models.Cita;
import com.sanacorp.models.Cita.EstadoCita;
import com.sanacorp.models.Consultorio;
import com.sanacorp.models.Especialidad;
import com.sanacorp.models.Medico;
import com.sanacorp.models.Paciente;
import com.sanacorp.models.Persona;
import com.sanacorp.services.CitaService;
import com.sanacorp.services.ConsultorioService;
import com.sanacorp.services.EspecialidadService;
import com.sanacorp.services.MedicoService;
import com.sanacorp.services.PacienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

/**
 * Controlador para el módulo de Secretario
 * Maneja todas las operaciones relacionadas con la gestión de pacientes y citas
 */
@Controller
@RequestMapping("/secretario")
public class SecretarioController {
    
    @Autowired
    private PacienteService pacienteService;
    
    @Autowired
    private CitaService citaService;
    
    @Autowired
    private MedicoService medicoService;
    
    @Autowired
    private EspecialidadService especialidadService;
    
    @Autowired
    private ConsultorioService consultorioService;
    
    /**
     * Dashboard principal del secretario
     */
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        try {
            // Obtener estadísticas para el dashboard
            CitaService.CitaEstadisticas estadisticas = citaService.getEstadisticasCitas();
            model.addAttribute("estadisticas", estadisticas);
            
            // Obtener citas del día
            List<Cita> citasHoy = citaService.getCitasDelDia();
            model.addAttribute("citasHoy", citasHoy);
            
            // Obtener citas próximas
            List<Cita> citasProximas = citaService.getCitasProximas();
            model.addAttribute("citasProximas", citasProximas);
            
            // Contar pacientes
            Long totalPacientes = pacienteService.contarPacientes();
            model.addAttribute("totalPacientes", totalPacientes);
            
            return "secretario/dashboard";
        } catch (Exception e) {
            model.addAttribute("error", "Error al cargar el dashboard: " + e.getMessage());
            return "secretario/dashboard";
        }
    }
    
    // ==================== GESTIÓN DE CITAS ====================
    
    /**
     * Lista todas las citas
     */
    @GetMapping("/citas")
    public String listarCitas(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            @RequestParam(required = false) String estado,
            Model model) {
        try {
            List<Cita> citas;
            
            if (fecha != null) {
                citas = citaService.getCitasPorFecha(fecha);
            } else {
                citas = citaService.getCitasDelDia();
            }
            
            // Filtrar por estado si se especifica
            if (estado != null && !estado.isEmpty()) {
                EstadoCita estadoCita = EstadoCita.valueOf(estado);
                citas = citas.stream()
                    .filter(c -> c.getEstado() == estadoCita)
                    .toList();
            }
            
            model.addAttribute("citas", citas);
            model.addAttribute("fechaSeleccionada", fecha != null ? fecha : LocalDate.now());
            model.addAttribute("estadoSeleccionado", estado);
            model.addAttribute("estados", EstadoCita.values());
            
            return "secretario/lista-citas";
        } catch (Exception e) {
            model.addAttribute("error", "Error al cargar las citas: " + e.getMessage());
            return "secretario/lista-citas";
        }
    }
    
    /**
     * Formulario para nueva cita
     */
    @GetMapping("/citas/nueva")
    public String nuevaCitaForm(Model model) {
        try {
            model.addAttribute("cita", new Cita());
            model.addAttribute("pacientes", pacienteService.getAllPacientes());
            model.addAttribute("medicos", medicoService.getAllMedicosActivos());
            model.addAttribute("especialidades", especialidadService.getAllEspecialidadesActivas());
            model.addAttribute("consultorios", consultorioService.getAllConsultoriosActivos());
            
            return "secretario/form-cita";
        } catch (Exception e) {
            model.addAttribute("error", "Error al cargar el formulario: " + e.getMessage());
            return "secretario/lista-citas";
        }
    }
    
    /**
     * Procesar registro de nueva cita
     */
    @PostMapping("/citas/nueva")
    public String registrarCita(
            @Valid @ModelAttribute Cita cita,
            BindingResult result,
            @RequestParam Long pacienteId,
            @RequestParam Long medicoId,
            @RequestParam Long consultorioId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime hora,
            RedirectAttributes redirectAttributes,
            Model model) {
        
        if (result.hasErrors()) {
            model.addAttribute("pacientes", pacienteService.getAllPacientes());
            model.addAttribute("medicos", medicoService.getAllMedicosActivos());
            model.addAttribute("especialidades", especialidadService.getAllEspecialidadesActivas());
            model.addAttribute("consultorios", consultorioService.getAllConsultoriosActivos());
            return "secretario/form-cita";
        }
        
        try {
            // Obtener entidades relacionadas
            Optional<Paciente> paciente = pacienteService.getPacienteById(pacienteId);
            Optional<Medico> medico = medicoService.getMedicoById(medicoId);
            Optional<Consultorio> consultorio = consultorioService.getConsultorioById(consultorioId);
            
            if (paciente.isEmpty() || medico.isEmpty() || consultorio.isEmpty()) {
                model.addAttribute("error", "Error: Paciente, médico o consultorio no encontrado");
                model.addAttribute("pacientes", pacienteService.getAllPacientes());
                model.addAttribute("medicos", medicoService.getAllMedicosActivos());
                model.addAttribute("especialidades", especialidadService.getAllEspecialidadesActivas());
                model.addAttribute("consultorios", consultorioService.getAllConsultoriosActivos());
                return "secretario/form-cita";
            }
            
            // Configurar la cita
            cita.setPaciente(paciente.get());
            cita.setMedico(medico.get());
            cita.setConsultorio(consultorio.get());
            cita.setFechaHora(LocalDateTime.of(fecha, hora));
            
            // Registrar la cita
            citaService.registrarNuevaCita(cita);
            
            redirectAttributes.addFlashAttribute("success", "Cita registrada exitosamente");
            return "redirect:/secretario/citas";
            
        } catch (Exception e) {
            model.addAttribute("error", "Error al registrar la cita: " + e.getMessage());
            model.addAttribute("pacientes", pacienteService.getAllPacientes());
            model.addAttribute("medicos", medicoService.getAllMedicosActivos());
            model.addAttribute("especialidades", especialidadService.getAllEspecialidadesActivas());
            model.addAttribute("consultorios", consultorioService.getAllConsultoriosActivos());
            return "secretario/form-cita";
        }
    }
    
    /**
     * Confirmar una cita
     */
    @PostMapping("/citas/{id}/confirmar")
    public String confirmarCita(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            citaService.confirmarCita(id);
            redirectAttributes.addFlashAttribute("success", "Cita confirmada exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al confirmar la cita: " + e.getMessage());
        }
        return "redirect:/secretario/citas";
    }
    
    /**
     * Cancelar una cita
     */
    @PostMapping("/citas/{id}/cancelar")
    public String cancelarCita(
            @PathVariable Long id,
            @RequestParam String observaciones,
            RedirectAttributes redirectAttributes) {
        try {
            citaService.cancelarCita(id, observaciones);
            redirectAttributes.addFlashAttribute("success", "Cita cancelada exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al cancelar la cita: " + e.getMessage());
        }
        return "redirect:/secretario/citas";
    }
    
    /**
     * Completar una cita
     */
    @PostMapping("/citas/{id}/completar")
    public String completarCita(
            @PathVariable Long id,
            @RequestParam String observaciones,
            RedirectAttributes redirectAttributes) {
        try {
            citaService.completarCita(id, observaciones);
            redirectAttributes.addFlashAttribute("success", "Cita completada exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al completar la cita: " + e.getMessage());
        }
        return "redirect:/secretario/citas";
    }
    
    /**
     * Marcar inasistencia
     */
    @PostMapping("/citas/{id}/inasistencia")
    public String marcarInasistencia(
            @PathVariable Long id,
            @RequestParam String observaciones,
            RedirectAttributes redirectAttributes) {
        try {
            citaService.marcarInasistencia(id, observaciones);
            redirectAttributes.addFlashAttribute("success", "Inasistencia registrada exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al registrar inasistencia: " + e.getMessage());
        }
        return "redirect:/secretario/citas";
    }
    
    // ==================== GESTIÓN DE PACIENTES ====================
    
    /**
     * Lista todos los pacientes
     */
    @GetMapping("/pacientes")
    public String listarPacientes(
            @RequestParam(required = false) String busqueda,
            Model model) {
        try {
            List<Paciente> pacientes;
            
            if (busqueda != null && !busqueda.trim().isEmpty()) {
                pacientes = pacienteService.buscarPacientes(busqueda.trim());
            } else {
                pacientes = pacienteService.getAllPacientes();
            }
            
            model.addAttribute("pacientes", pacientes);
            model.addAttribute("busqueda", busqueda);
            
            return "secretario/lista-pacientes";
        } catch (Exception e) {
            model.addAttribute("error", "Error al cargar los pacientes: " + e.getMessage());
            return "secretario/lista-pacientes";
        }
    }
    
    /**
     * Formulario para nuevo paciente
     */
    @GetMapping("/pacientes/nuevo")
    public String nuevoPacienteForm(Model model) {
        model.addAttribute("paciente", new Paciente());
        model.addAttribute("persona", new Persona());
        return "secretario/form-paciente";
    }
    
    /**
     * Procesar registro de nuevo paciente
     */
    @PostMapping("/pacientes/nuevo")
    public String registrarPaciente(
            @Valid @ModelAttribute("paciente") Paciente paciente,
            BindingResult pacienteResult,
            @Valid @ModelAttribute("persona") Persona persona,
            BindingResult personaResult,
            RedirectAttributes redirectAttributes,
            Model model) {
        
        if (pacienteResult.hasErrors() || personaResult.hasErrors()) {
            return "secretario/form-paciente";
        }
        
        try {
            // Asignar la persona al paciente
            paciente.setPersona(persona);
            
            // Registrar el paciente
            pacienteService.registrarNuevoPaciente(paciente);
            
            redirectAttributes.addFlashAttribute("success", "Paciente registrado exitosamente");
            return "redirect:/secretario/pacientes";
            
        } catch (Exception e) {
            model.addAttribute("error", "Error al registrar el paciente: " + e.getMessage());
            return "secretario/form-paciente";
        }
    }
    
    /**
     * Ver detalles de un paciente
     */
    @GetMapping("/pacientes/{id}")
    public String verPaciente(@PathVariable Long id, Model model) {
        try {
            Optional<Paciente> pacienteOpt = pacienteService.getPacienteById(id);
            if (pacienteOpt.isEmpty()) {
                model.addAttribute("error", "Paciente no encontrado");
                return "redirect:/secretario/pacientes";
            }
            
            Paciente paciente = pacienteOpt.get();
            List<Cita> citasPaciente = citaService.getCitasPorPaciente(id);
            
            model.addAttribute("paciente", paciente);
            model.addAttribute("citas", citasPaciente);
            
            return "secretario/detalle-paciente";
        } catch (Exception e) {
            model.addAttribute("error", "Error al cargar el paciente: " + e.getMessage());
            return "redirect:/secretario/pacientes";
        }
    }
    
    /**
     * Buscar paciente por DNI (AJAX)
     */
    @GetMapping("/pacientes/buscar-dni")
    @ResponseBody
    public Paciente buscarPacientePorDni(@RequestParam String dni) {
        try {
            Optional<Paciente> paciente = pacienteService.buscarPorDni(dni);
            return paciente.orElse(null);
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * Obtener médicos por especialidad (AJAX)
     */
    @GetMapping("/medicos/por-especialidad")
    @ResponseBody
    public List<Medico> getMedicosPorEspecialidad(@RequestParam Long especialidadId) {
        try {
            return medicoService.getMedicosPorEspecialidad(especialidadId);
        } catch (Exception e) {
            return List.of();
        }
    }
    
    /**
     * Verificar disponibilidad de médico (AJAX)
     */
    @GetMapping("/medicos/{id}/disponibilidad")
    @ResponseBody
    public boolean verificarDisponibilidad(
            @PathVariable Long id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime hora) {
        try {
            List<Cita> citasExistentes = citaService.getCitasPorMedicoYFecha(id, fecha);
            LocalTime horaFin = hora.plusMinutes(30); // Duración estándar de 30 minutos
            
            // Verificar si hay conflictos de horario
            return citasExistentes.stream().noneMatch(cita -> {
                LocalTime citaInicio = cita.getFechaHora().toLocalTime();
                LocalTime citaFin = citaInicio.plusMinutes(30);
                
                return (hora.isBefore(citaFin) && horaFin.isAfter(citaInicio));
            });
        } catch (Exception e) {
            return false;
        }
    }
}