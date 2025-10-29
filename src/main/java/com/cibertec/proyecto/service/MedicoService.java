package com.cibertec.proyecto.service;

import com.cibertec.proyecto.entity.Medico;
import com.cibertec.proyecto.entity.Persona;
import com.cibertec.proyecto.entity.Usuario;
import com.cibertec.proyecto.repository.MedicoRepository;
import com.cibertec.proyecto.repository.PersonaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MedicoService {

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private PersonaRepository personaRepository;

    public List<Medico> listarTodos() {
        return medicoRepository.findAll();
    }

    // ✅ Solo los médicos con usuario activo
    public List<Medico> listarActivos() {
        return medicoRepository.findAll().stream()
                .filter(m -> m.getPersona() != null
                        && m.getPersona().getUsuario() != null
                        && m.getPersona().getUsuario().getEstado() == 1)
                .collect(Collectors.toList());
    }

    public Optional<Medico> buscarPorId(Integer id) {
        return medicoRepository.findById(id);
    }

    public Optional<Medico> buscarPorUsuarioId(Integer usuarioId) {
        return medicoRepository.findByPersonaUsuarioId(usuarioId);
    }

    public List<Medico> buscarPorEspecialidad(Integer especialidadId) {
        return medicoRepository.findByEspecialidadId(especialidadId);
    }

    public List<Medico> buscarPorNombre(String nombre) {
        return medicoRepository.findByPersonaNombreContainingOrPersonaApellidoContaining(nombre, nombre);
    }

    public Medico guardar(Medico medico) {
        return medicoRepository.save(medico);
    }

    // Crear médico desde usuario
    public Medico crearMedicoDesdeUsuario(Usuario usuario, String dni, String nombre, String apellido) {
        Persona persona = new Persona();
        persona.setUsuario(usuario);
        persona.setDni(dni);
        persona.setNombre(nombre);
        persona.setApellido(apellido);
        persona = personaRepository.save(persona);

        Medico medico = new Medico();
        medico.setPersona(persona);
        return medicoRepository.save(medico);
    }
}
