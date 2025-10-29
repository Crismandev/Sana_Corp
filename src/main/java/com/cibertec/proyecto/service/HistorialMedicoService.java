package com.cibertec.proyecto.service;

import com.cibertec.proyecto.entity.HistorialMedico;
import com.cibertec.proyecto.repository.HistorialMedicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class HistorialMedicoService {
    
    @Autowired
    private HistorialMedicoRepository historialMedicoRepository;
    
    // Listar todo el historial
    public List<HistorialMedico> listarTodos() {
        return historialMedicoRepository.findAll();
    }
    
    // Buscar por ID
    public Optional<HistorialMedico> buscarPorId(Integer id) {
        return historialMedicoRepository.findById(id);
    }
    
    // Historial de un paciente
    public List<HistorialMedico> buscarPorPaciente(Integer pacienteId) {
        return historialMedicoRepository.findByPacienteId(pacienteId);
    }
    
    // Historial por cita
    public List<HistorialMedico> buscarPorCita(Integer citaId) {
        return historialMedicoRepository.findByCitaId(citaId);
    }
    
    // Guardar historial
    public HistorialMedico guardar(HistorialMedico historialMedico) {
        return historialMedicoRepository.save(historialMedico);
    }
    
    // Crear nuevo registro de historial
    public HistorialMedico crearHistorial(Integer citaId, String notas, String archivos) {
        HistorialMedico historial = new HistorialMedico();
        // Nota: Necesitaríamos inyectar CitaService para buscar la cita
        // Por ahora lo dejamos simple
        historial.setNotas(notas);
        historial.setArchivosAdjuntos(archivos);
        return historialMedicoRepository.save(historial);
    }
}