package com.cibertec.proyecto.controller;

import com.cibertec.proyecto.entity.HistorialMedico;
import com.cibertec.proyecto.service.HistorialMedicoService;
import com.cibertec.proyecto.service.PacienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;

@Controller
@RequestMapping("/historial")
public class HistorialMedicoController {
    
    @Autowired
    private HistorialMedicoService historialMedicoService;
    
    @Autowired
    private PacienteService pacienteService;
    
    // MI HISTORIAL (paciente)
    @GetMapping("/mi-historial")
    public String miHistorial(Model model) {
        // Para pruebas - cambiar por usuario de sesión después
        Integer pacienteId = 1; // ID del paciente en sesión
        
        List<HistorialMedico> historial = historialMedicoService.buscarPorPaciente(pacienteId);
        model.addAttribute("historial", historial);
        model.addAttribute("paciente", pacienteService.buscarPorId(pacienteId).orElse(null));
        
        return "historial/mi-historial";
    }
    
    // HISTORIAL COMPLETO (admin/médicos)
    @GetMapping
    public String listarHistorial(Model model) {
        model.addAttribute("historial", historialMedicoService.listarTodos());
        return "historial/lista";
    }
}