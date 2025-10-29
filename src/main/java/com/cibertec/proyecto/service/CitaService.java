package com.cibertec.proyecto.service;

import com.cibertec.proyecto.entity.Cita;
import com.cibertec.proyecto.entity.EstadoCita;
import com.cibertec.proyecto.entity.Paciente;
import com.cibertec.proyecto.entity.Medico;
import com.cibertec.proyecto.repository.CitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CitaService {
    
    @Autowired
    private CitaRepository citaRepository;
    
    @Autowired
    private PacienteService pacienteService;
    
    @Autowired
    private MedicoService medicoService;
    
    // Listar todas las citas
    public List<Cita> listarTodas() {
        return citaRepository.findAll();
    }
    
    // Buscar por ID
    public Optional<Cita> buscarPorId(Integer id) {
        return citaRepository.findById(id);
    }
    
    // Citas de un paciente
    public List<Cita> buscarPorPaciente(Integer pacienteId) {
        return citaRepository.findByPacienteIdOrderByFechaHoraDesc(pacienteId);
    }
    
    // Citas de un médico
    public List<Cita> buscarPorMedico(Integer medicoId) {
        return citaRepository.findByMedicoIdOrderByFechaHoraDesc(medicoId);
    }
    
    // Guardar cita genérica
    public Cita guardar(Cita cita) {
        return citaRepository.save(cita);
    }
    
    // ✅ Programar nueva cita (ya funcional)
    public Cita programarCita(Integer pacienteId, Integer medicoId, String fechaHora, String motivo) {
        Optional<Paciente> paciente = pacienteService.buscarPorId(pacienteId);
        Optional<Medico> medico = medicoService.buscarPorId(medicoId);

        if (paciente.isPresent() && medico.isPresent()) {
            Cita cita = new Cita();
            cita.setPaciente(paciente.get());
            cita.setMedico(medico.get());
            
            // 🔹 Convertir String a Date
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
                cita.setFechaHora(sdf.parse(fechaHora));
            } catch (Exception e) {
                cita.setFechaHora(new Date());
            }

            // 🔹 Campos adicionales
            cita.setMotivo(motivo);
            cita.setEstado(EstadoCita.Programada);     // Estado inicial
            cita.setFechaRegistro(new Date());          // Fecha actual
            
            return citaRepository.save(cita);
        }
        return null;
    }
    
    // Cancelar cita
    public boolean cancelarCita(Integer citaId) {
        Optional<Cita> citaOpt = citaRepository.findById(citaId);
        if (citaOpt.isPresent()) {
            Cita cita = citaOpt.get();
            cita.setEstado(EstadoCita.Cancelada);
            citaRepository.save(cita);
            return true;
        }
        return false;
    }
    
    // Completar cita
    public boolean completarCita(Integer citaId) {
        Optional<Cita> citaOpt = citaRepository.findById(citaId);
        if (citaOpt.isPresent()) {
            Cita cita = citaOpt.get();
            cita.setEstado(EstadoCita.Completada);
            citaRepository.save(cita);
            return true;
        }
        return false;
    }
}
