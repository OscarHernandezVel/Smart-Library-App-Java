package com.smartlibrary.repository;

import com.smartlibrary.model.Estudiante;

import java.util.Optional;

/** Contrato de persistencia de estudiantes, expresado en lenguaje del dominio. */
public interface EstudianteRepository {

    Optional<Estudiante> buscarPorId(Long id);
}
