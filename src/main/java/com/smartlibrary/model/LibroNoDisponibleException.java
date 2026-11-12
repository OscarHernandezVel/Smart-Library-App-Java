package com.smartlibrary.model;

/** Excepción de dominio: se intentó prestar un libro que no está disponible (R1). */
public class LibroNoDisponibleException extends RuntimeException {

    public LibroNoDisponibleException(Long libroId) {
        super("El libro " + libroId + " no está disponible");
    }
}
