package com.smartlibrary.service;

import com.smartlibrary.model.Libro;
import com.smartlibrary.repository.LibroRepository;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LibroServiceTest {

    private final Libro libro = new Libro(1L, "Clean Code", true);
    private final LibroRepository repositorio = new LibroRepository() {
        @Override
        public Optional<Libro> buscarPorId(Long id) {
            return id.equals(1L) ? Optional.of(libro) : Optional.empty();
        }

        @Override
        public Libro guardar(Libro l) {
            return l;
        }
    };
    private final LibroService servicio = new LibroService(repositorio);

    @Test
    void devuelveElLibroExistente() {
        assertSame(libro, servicio.buscarPorId(1L));
    }

    @Test
    void lanzaNoSuchElementSiNoExiste() {
        assertThrows(NoSuchElementException.class, () -> servicio.buscarPorId(99L));
    }
}
