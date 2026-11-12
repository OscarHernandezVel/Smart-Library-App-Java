package com.smartlibrary.dto;

import com.smartlibrary.model.Prestamo;

import java.time.LocalDate;

/** Objeto de salida: evita exponer las entidades de dominio en la API. */
public class PrestamoResponse {

    private Long id;
    private Long estudianteId;
    private Long libroId;
    private LocalDate fechaPrestamo;

    public PrestamoResponse() {
    }

    public PrestamoResponse(Long id, Long estudianteId, Long libroId, LocalDate fechaPrestamo) {
        this.id = id;
        this.estudianteId = estudianteId;
        this.libroId = libroId;
        this.fechaPrestamo = fechaPrestamo;
    }

    public static PrestamoResponse desde(Prestamo prestamo) {
        return new PrestamoResponse(
                prestamo.getId(),
                prestamo.getEstudiante().getId(),
                prestamo.getLibro().getId(),
                prestamo.getFechaPrestamo());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEstudianteId() {
        return estudianteId;
    }

    public void setEstudianteId(Long estudianteId) {
        this.estudianteId = estudianteId;
    }

    public Long getLibroId() {
        return libroId;
    }

    public void setLibroId(Long libroId) {
        this.libroId = libroId;
    }

    public LocalDate getFechaPrestamo() {
        return fechaPrestamo;
    }

    public void setFechaPrestamo(LocalDate fechaPrestamo) {
        this.fechaPrestamo = fechaPrestamo;
    }
}
