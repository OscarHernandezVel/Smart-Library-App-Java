package com.smartlibrary.repository;

import com.smartlibrary.model.Libro;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/** Realización JPA/H2 de {@link LibroRepository}. */
@Repository
public class LibroRepositoryJpa implements LibroRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Optional<Libro> buscarPorId(Long id) {
        return Optional.ofNullable(em.find(Libro.class, id));
    }

    @Override
    public Libro guardar(Libro libro) {
        if (libro.getId() == null) {
            em.persist(libro);
            return libro;
        }
        return em.merge(libro);
    }
}
