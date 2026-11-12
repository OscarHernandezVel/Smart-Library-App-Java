package com.smartlibrary.repository;

import com.smartlibrary.model.Prestamo;

import java.util.List;

/** Contrato de persistencia de préstamos, expresado en lenguaje del dominio. */
public interface PrestamoRepository {

    Prestamo guardar(Prestamo prestamo);

    List<Prestamo> buscarPorEstudiante(Long estudianteId);

    List<Prestamo> buscarActivos();
}
