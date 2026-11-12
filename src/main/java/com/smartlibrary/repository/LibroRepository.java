package com.smartlibrary.repository;

import com.smartlibrary.model.Libro;

import java.util.Optional;

/** Contrato de persistencia de libros, expresado en lenguaje del dominio. */
public interface LibroRepository {

    Optional<Libro> buscarPorId(Long id);

    Libro guardar(Libro libro);
}
