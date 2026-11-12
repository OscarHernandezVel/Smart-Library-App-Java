package com.smartlibrary.repository;

import com.smartlibrary.model.Prestamo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

/** Realización JPA/H2 de {@link PrestamoRepository}. */
@Repository
public class PrestamoRepositoryJpa implements PrestamoRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Prestamo guardar(Prestamo prestamo) {
        if (prestamo.getId() == null) {
            em.persist(prestamo);
            return prestamo;
        }
        return em.merge(prestamo);
    }

    @Override
    public List<Prestamo> buscarPorEstudiante(Long estudianteId) {
        return em.createQuery(
                        "select p from Prestamo p "
                                + "join fetch p.estudiante "
                                + "join fetch p.libro "
                                + "where p.estudiante.id = :estudianteId "
                                + "order by p.fechaPrestamo desc, p.id desc",
                        Prestamo.class)
                .setParameter("estudianteId", estudianteId)
                .getResultList();
    }

    @Override
    public List<Prestamo> buscarActivos() {
        return em.createQuery(
                        "select p from Prestamo p "
                                + "join fetch p.estudiante "
                                + "join fetch p.libro "
                                + "where p.fechaDevolucion is null "
                                + "order by p.id",
                        Prestamo.class)
                .getResultList();
    }
}
