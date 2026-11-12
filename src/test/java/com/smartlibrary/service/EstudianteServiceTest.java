package com.smartlibrary.service;

import com.smartlibrary.model.Estudiante;
import com.smartlibrary.repository.EstudianteRepository;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EstudianteServiceTest {

    private final Estudiante ana = new Estudiante(1L, "Ana", "ana@x.edu");
    private final EstudianteRepository repositorio =
            id -> id.equals(1L) ? Optional.of(ana) : Optional.empty();
    private final EstudianteService servicio = new EstudianteService(repositorio);

    @Test
    void devuelveElEstudianteExistente() {
        assertSame(ana, servicio.buscarPorId(1L));
    }

    @Test
    void lanzaNoSuchElementSiNoExiste() {
        assertThrows(NoSuchElementException.class, () -> servicio.buscarPorId(99L));
    }
}
