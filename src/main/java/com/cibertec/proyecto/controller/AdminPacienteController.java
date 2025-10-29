package com.cibertec.proyecto.controller;

import com.cibertec.proyecto.entity.Paciente;
import com.cibertec.proyecto.service.PacienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminPacienteController {

    @Autowired
    private PacienteService pacienteService;

    // 📋 LISTA COMPLETA DE PACIENTES - CON MANEJO DE ERRORES
    @GetMapping("/pacientes")
    public String listaPacientes(Model model) {
        try {
            List<Paciente> pacientes = pacienteService.listarTodos();
            model.addAttribute("pacientes", pacientes);
            model.addAttribute("totalPacientes", pacientes.size());
            
            // Estadísticas
            long conSeguro = pacientes.stream()
                    .filter(p -> p.getSeguroMedico() != null && !p.getSeguroMedico().isEmpty())
                    .count();
            long sinSeguro = pacientes.size() - conSeguro;
            
            model.addAttribute("pacientesConSeguro", conSeguro);
            model.addAttribute("pacientesSinSeguro", sinSeguro);
            
            return "admin/pacientes";
        } catch (Exception e) {
            model.addAttribute("error", "Error al cargar los pacientes: " + e.getMessage());
            return "admin/pacientes";
        }
    }

    // 👁️ VER DETALLES DE PACIENTE - MEJORADO
    @GetMapping("/pacientes/detalles/{id}")
    public String verDetallesPaciente(@PathVariable Integer id, Model model) {
        try {
            Optional<Paciente> pacienteOpt = pacienteService.buscarPorId(id);
            if (pacienteOpt.isPresent()) {
                model.addAttribute("paciente", pacienteOpt.get());
                return "admin/detalles-paciente";
            } else {
                model.addAttribute("error", "Paciente no encontrado");
                return "redirect:/admin/pacientes";
            }
        } catch (Exception e) {
            model.addAttribute("error", "Error al cargar detalles: " + e.getMessage());
            return "redirect:/admin/pacientes";
        }
    }
}