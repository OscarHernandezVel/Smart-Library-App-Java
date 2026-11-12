package com.smartlibrary.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PrestamoTest {

    private final Estudiante estudiante = new Estudiante(1L, "Ana Gómez", "ana@smartlibrary.edu");
    private final Libro libro = new Libro(1L, "Clean Code", true);

    @Test
    void registraEstudianteLibroYFechaYNaceActivo() {
        LocalDate fecha = LocalDate.of(2026, 11, 12);

        Prestamo prestamo = new Prestamo(fecha, estudiante, libro);

        assertSame(estudiante, prestamo.getEstudiante());
        assertSame(libro, prestamo.getLibro());
        assertEquals(fecha, prestamo.getFechaPrestamo());
        assertTrue(prestamo.estaActivo());
    }

    @Test
    void noPuedeCrearseIncompleto() {
        LocalDate fecha = LocalDate.of(2026, 11, 12);

        assertThrows(NullPointerException.class, () -> new Prestamo(null, estudiante, libro));
        assertThrows(NullPointerException.class, () -> new Prestamo(fecha, null, libro));
        assertThrows(NullPointerException.class, () -> new Prestamo(fecha, estudiante, null));
    }
}
