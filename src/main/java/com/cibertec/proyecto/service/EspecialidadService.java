package com.cibertec.proyecto.service;

import com.cibertec.proyecto.entity.Especialidad;
import com.cibertec.proyecto.repository.EspecialidadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class EspecialidadService {
    
    @Autowired
    private EspecialidadRepository especialidadRepository;
    
    public List<Especialidad> listarTodas() {
        return especialidadRepository.findAll();
    }
    
    public Optional<Especialidad> buscarPorId(Integer id) {
        return especialidadRepository.findById(id);
    }
    
    public List<Especialidad> buscarPorNombre(String nombre) {
        return especialidadRepository.findByNombreContaining(nombre);
    }
    
    public Especialidad guardar(Especialidad especialidad) {
        return especialidadRepository.save(especialidad);
    }
    
    public void eliminar(Integer id) {
        especialidadRepository.deleteById(id);
    }
}