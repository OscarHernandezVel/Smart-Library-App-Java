package com.smartlibrary.repository;

import com.smartlibrary.model.Estudiante;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/** Realización JPA/H2 de {@link EstudianteRepository}. */
@Repository
public class EstudianteRepositoryJpa implements EstudianteRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Optional<Estudiante> buscarPorId(Long id) {
        return Optional.ofNullable(em.find(Estudiante.class, id));
    }
}
