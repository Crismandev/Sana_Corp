package com.cibertec.proyecto.controller;

import com.cibertec.proyecto.entity.Cita;
import com.cibertec.proyecto.entity.Medico;
import com.cibertec.proyecto.entity.Paciente;
import com.cibertec.proyecto.service.CitaService;
import com.cibertec.proyecto.service.MedicoService;
import com.cibertec.proyecto.service.PacienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/citas")
public class CitaController {

    @Autowired
    private CitaService citaService;

    @Autowired
    private PacienteService pacienteService;

    @Autowired
    private MedicoService medicoService;

    // 1. LISTAR TODAS LAS CITAS (solo admin)
    @GetMapping
    public String listarCitas(Model model) {
        model.addAttribute("citas", citaService.listarTodas());
        return "citas/lista";
    }

    // 2. MIS CITAS (paciente)
    @GetMapping("/mis-citas")
    public String misCitas(Model model) {
        Integer pacienteId = 1; // simula usuario paciente logueado
        model.addAttribute("citas", citaService.buscarPorPaciente(pacienteId));
        return "citas/mis-citas";
    }

    // 3. FORMULARIO NUEVA CITA (opcionalmente con médico ya seleccionado)
    @GetMapping("/nueva")
    public String formularioNuevaCita(@RequestParam(required = false) Integer medicoId, Model model) {

        Cita cita = new Cita();
        model.addAttribute("cita", cita);

        // Paciente de prueba
        Integer pacienteId = 1;
        Optional<Paciente> paciente = pacienteService.buscarPorId(pacienteId);
        paciente.ifPresent(p -> model.addAttribute("paciente", p));

        // Listar médicos (solo activos)
        List<Medico> medicos = medicoService.listarActivos();
        model.addAttribute("medicos", medicos);

        // Si vino desde “Solicitar Cita” con un médico específico
        if (medicoId != null) {
            model.addAttribute("medicoSeleccionadoId", medicoId);
        }

        return "citas/form-nueva";
    }

    // 4. GUARDAR CITA
    @PostMapping("/guardar")
    public String guardarCita(
            @RequestParam Integer pacienteId,
            @RequestParam Integer medicoId,
            @RequestParam String fechaHora,
            @RequestParam String motivo) {

        citaService.programarCita(pacienteId, medicoId, fechaHora, motivo);
        return "redirect:/citas/mis-citas?exito";
    }

    // 5. CANCELAR CITA
    @GetMapping("/cancelar/{id}")
    public String cancelarCita(@PathVariable Integer id) {
        citaService.cancelarCita(id);
        return "redirect:/citas/mis-citas?cancelada";
    }

    // 6. COMPLETAR CITA
    @GetMapping("/completar/{id}")
    public String completarCita(@PathVariable Integer id) {
        citaService.completarCita(id);
        return "redirect:/citas/agenda?completada";
    }
}
