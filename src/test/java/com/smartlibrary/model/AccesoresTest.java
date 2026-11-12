package com.smartlibrary.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;

/** Cada atributo del diagrama tiene su getter y su setter. */
class AccesoresTest {

    @Test
    void estudiante() {
        Estudiante e = new Estudiante(1L, "Ana", "ana@x.edu");
        e.setId(2L);
        e.setNombre("Luis");
        e.setCorreo("luis@x.edu");
        assertEquals(2L, e.getId());
        assertEquals("Luis", e.getNombre());
        assertEquals("luis@x.edu", e.getCorreo());
    }

    @Test
    void libro() {
        Libro l = new Libro(1L, "Clean Code", true);
        l.setId(5L);
        l.setTitulo("Refactoring");
        l.setDisponible(false);
        assertEquals(5L, l.getId());
        assertEquals("Refactoring", l.getTitulo());
        assertFalse(l.isDisponible());
    }

    @Test
    void prestamo() {
        Estudiante e = new Estudiante(1L, "Ana", "ana@x.edu");
        Libro l = new Libro(1L, "Clean Code", true);
        Prestamo p = new Prestamo(LocalDate.of(2026, 11, 12), e, l);
        Estudiante otro = new Estudiante(2L, "Luis", "luis@x.edu");
        Libro otroLibro = new Libro(2L, "Effective Java", true);
        p.setId(9L);
        p.setFechaPrestamo(LocalDate.of(2026, 11, 13));
        p.setFechaDevolucion(LocalDate.of(2026, 11, 20));
        p.setEstudiante(otro);
        p.setLibro(otroLibro);
        assertEquals(9L, p.getId());
        assertEquals(LocalDate.of(2026, 11, 13), p.getFechaPrestamo());
        assertEquals(LocalDate.of(2026, 11, 20), p.getFechaDevolucion());
        assertSame(otro, p.getEstudiante());
        assertSame(otroLibro, p.getLibro());
    }
}
