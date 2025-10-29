package com.cibertec.proyecto.controller;

import com.cibertec.proyecto.entity.Paciente;
import com.cibertec.proyecto.entity.Medico;
import com.cibertec.proyecto.service.EspecialidadService;
import com.cibertec.proyecto.service.MedicoService;
import com.cibertec.proyecto.service.PacienteService;
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

@Controller
@RequestMapping("/paciente")
public class PacienteController {

    @Autowired
    private PacienteService pacienteService;

    @Autowired
    private MedicoService medicoService;

    @Autowired
    private EspecialidadService especialidadService;

    // 1. PERFIL DEL PACIENTE - Vista
    @GetMapping("/perfil")
    public String perfilPaciente(Model model) {
        Integer usuarioId = 4; // Temporal hasta integrar login
        Optional<Paciente> paciente = pacienteService.buscarPorUsuarioId(usuarioId);

        if (paciente.isPresent()) {
            if (paciente.get().getPersona() == null) {
                model.addAttribute("error", "Datos de persona no encontrados");
                return "paciente/registro";
            }
            model.addAttribute("paciente", paciente.get());
            return "paciente/perfil";
        } else {
            model.addAttribute("error", "No tienes un perfil de paciente registrado");
            return "paciente/registro";
        }
    }

    // 2. FORMULARIO DE REGISTRO
    @GetMapping("/registro")
    public String formularioRegistro(Model model) {
        model.addAttribute("paciente", new Paciente());
        return "paciente/registro";
    }

    // 3. REGISTRAR PACIENTE
    @GetMapping("/registrar")
    public String registrarPaciente() {
        return "redirect:/paciente/perfil";
    }

    // 4. LISTAR TODOS LOS PACIENTES (Admin)
    @GetMapping("/lista")
    public String listaPacientes(Model model) {
        model.addAttribute("pacientes", pacienteService.listarTodos());
        return "paciente/lista";
    }

    // 5. ACTUALIZAR PERFIL
    @GetMapping("/actualizar")
    public String actualizarPerfil() {
        return "redirect:/paciente/perfil";
    }

    // 6. BUSCAR MÉDICOS (vista principal)
    @GetMapping("/buscar-medicos")
    public String buscarMedicos(
            @RequestParam(required = false) Integer especialidadId,
            @RequestParam(required = false) String nombre,
            Model model) {

        Integer usuarioId = 4; // paciente de prueba
        Optional<Paciente> paciente = pacienteService.buscarPorUsuarioId(usuarioId);

        if (paciente.isPresent()) {
            model.addAttribute("paciente", paciente.get());

            List<Medico> medicos;
            if (especialidadId != null) {
                medicos = medicoService.buscarPorEspecialidad(especialidadId);
                model.addAttribute("especialidadSeleccionada", especialidadId);
            } else if (nombre != null && !nombre.trim().isEmpty()) {
                medicos = medicoService.buscarPorNombre(nombre);
                model.addAttribute("nombreBuscado", nombre);
            } else {
                medicos = medicoService.listarTodos();
            }

            model.addAttribute("medicos", medicos);
            model.addAttribute("especialidades", especialidadService.listarTodas());
            return "paciente/buscar-medicos";
        } else {
            model.addAttribute("error", "No tienes un perfil de paciente registrado");
            return "paciente/perfil";
        }
    }

    // 7. POST (redirige con filtros)
    @PostMapping("/buscar-medicos")
    public String procesarBusquedaMedicos(
            @RequestParam(required = false) Integer especialidadId,
            @RequestParam(required = false) String nombre,
            RedirectAttributes redirectAttributes) {

        StringBuilder parametros = new StringBuilder();
        if (especialidadId != null) {
            parametros.append("especialidadId=").append(especialidadId);
        }
        if (nombre != null && !nombre.trim().isEmpty()) {
            if (parametros.length() > 0) parametros.append("&");
            parametros.append("nombre=").append(nombre);
        }

        return "redirect:/paciente/buscar-medicos?" + parametros.toString();
    }

    // 8. MIS CITAS
    @GetMapping("/citas")
    public String misCitas(Model model) {
        Integer usuarioId = 4;
        Optional<Paciente> paciente = pacienteService.buscarPorUsuarioId(usuarioId);

        if (paciente.isPresent()) {
            model.addAttribute("paciente", paciente.get());
            model.addAttribute("mensaje", "Funcionalidad de citas en desarrollo");
            return "paciente/citas";
        } else {
            model.addAttribute("error", "No tienes un perfil de paciente registrado");
            return "paciente/perfil";
        }
    }

    // 9. EDITAR PERFIL
    @GetMapping("/editar")
    public String editarPerfil(Model model) {
        Integer usuarioId = 4;
        Optional<Paciente> paciente = pacienteService.buscarPorUsuarioId(usuarioId);

        if (paciente.isPresent()) {
            model.addAttribute("paciente", paciente.get());
            return "paciente/editar-perfil";
        } else {
            model.addAttribute("error", "No tienes un perfil de paciente registrado");
            return "paciente/perfil";
        }
    }

    // 10. HISTORIAL
    @GetMapping("/historial")
    public String miHistorial(Model model) {
        Integer usuarioId = 4;
        Optional<Paciente> paciente = pacienteService.buscarPorUsuarioId(usuarioId);

        if (paciente.isPresent()) {
            model.addAttribute("paciente", paciente.get());
            model.addAttribute("mensaje", "Funcionalidad de historial en desarrollo");
            return "paciente/historial";
        } else {
            model.addAttribute("error", "No tienes un perfil de paciente registrado");
            return "paciente/perfil";
        }
    }
}
