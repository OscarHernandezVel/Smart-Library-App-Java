package com.smartlibrary.service;

import com.smartlibrary.model.Estudiante;
import com.smartlibrary.repository.EstudianteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

/** Servicio de negocio del dominio Estudiante: depende solo de {@link EstudianteRepository}. */
@Service
public class EstudianteService {

    private final EstudianteRepository estudianteRepository;

    public EstudianteService(EstudianteRepository estudianteRepository) {
        this.estudianteRepository = estudianteRepository;
    }

    /** @throws NoSuchElementException si el estudiante no existe */
    @Transactional(readOnly = true)
    public Estudiante buscarPorId(Long id) {
        return estudianteRepository.buscarPorId(id)
                .orElseThrow(() -> new NoSuchElementException("No existe el estudiante " + id));
    }
}
