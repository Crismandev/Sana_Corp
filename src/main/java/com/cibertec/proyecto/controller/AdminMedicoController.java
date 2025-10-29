package com.cibertec.proyecto.controller;

import com.cibertec.proyecto.entity.Medico;
import com.cibertec.proyecto.service.MedicoService;
import com.cibertec.proyecto.service.EspecialidadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminMedicoController {

    @Autowired
    private MedicoService medicoService;

    @Autowired
    private EspecialidadService especialidadService;

    // 📋 LISTA DE MÉDICOS - VISTA ADMIN
    @GetMapping("/medicos")
    public String listaMedicos(Model model) {
        List<Medico> medicos = medicoService.listarTodos();
        model.addAttribute("medicos", medicos);
        model.addAttribute("totalMedicos", medicos.size());
        model.addAttribute("especialidades", especialidadService.listarTodas());
        
        return "admin/medicos";
    }

    // 🔍 BUSCAR MÉDICOS POR ESPECIALIDAD
    @GetMapping("/medicos/buscar")
    public String buscarMedicos(@RequestParam(required = false) Integer especialidadId, Model model) {
        List<Medico> medicos;
        
        if (especialidadId != null) {
            medicos = medicoService.buscarPorEspecialidad(especialidadId);
        } else {
            medicos = medicoService.listarTodos();
        }
        
        model.addAttribute("medicos", medicos);
        model.addAttribute("totalMedicos", medicos.size());
        model.addAttribute("especialidades", especialidadService.listarTodas());
        model.addAttribute("especialidadSeleccionada", especialidadId);
        
        return "admin/medicos";
    }
}