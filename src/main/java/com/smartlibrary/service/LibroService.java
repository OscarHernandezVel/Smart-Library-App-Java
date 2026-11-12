package com.smartlibrary.service;

import com.smartlibrary.model.Libro;
import com.smartlibrary.repository.LibroRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

/** Servicio de negocio del dominio Libro: depende solo de {@link LibroRepository}. */
@Service
public class LibroService {

    private final LibroRepository libroRepository;

    public LibroService(LibroRepository libroRepository) {
        this.libroRepository = libroRepository;
    }

    /** @throws NoSuchElementException si el libro no existe */
    @Transactional(readOnly = true)
    public Libro buscarPorId(Long id) {
        return libroRepository.buscarPorId(id)
                .orElseThrow(() -> new NoSuchElementException("No existe el libro " + id));
    }
}
