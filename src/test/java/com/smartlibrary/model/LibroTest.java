package com.smartlibrary.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LibroTest {

    @Test
    void marcarPrestadoDejaAlLibroNoDisponible() {
        Libro libro = new Libro(1L, "Clean Code", true);

        libro.marcarPrestado();

        assertFalse(libro.isDisponible());
    }

    @Test
    void marcarPrestadoRechazaUnLibroQueYaNoEstaDisponible() {
        Libro libro = new Libro(2L, "Refactoring", false);

        assertThrows(LibroNoDisponibleException.class, libro::marcarPrestado);
        assertFalse(libro.isDisponible());
    }

    @Test
    void unLibroNuevoDisponibleLoSigueEstandoHastaQueSePresta() {
        assertTrue(new Libro(3L, "Effective Java", true).isDisponible());
    }
}
