package com.smartlibrary.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Entidad de dominio: hecho que vincula a un estudiante con un libro.
 * Agregaciones hacia Estudiante y Libro (Prestamo 0..* -> 1 Estudiante, Prestamo 0..* -> 1 Libro):
 * los extremos tienen ciclo de vida propio y un préstamo es un registro histórico.
 * Un préstamo está activo mientras fechaDevolucion sea nula.
 */
@Entity
@Table(name = "prestamo")
public class Prestamo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fecha_prestamo", nullable = false)
    private LocalDate fechaPrestamo;

    /** Multiplicidad [0..1]: nula mientras el préstamo siga activo. */
    @Column(name = "fecha_devolucion")
    private LocalDate fechaDevolucion;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "estudiante_id", nullable = false)
    private Estudiante estudiante;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "libro_id", nullable = false)
    private Libro libro;

    /** Constructor requerido por JPA. */
    protected Prestamo() {
    }

    /** R2: el préstamo registra estudiante, libro y fecha; no puede crearse incompleto. */
    public Prestamo(LocalDate fecha, Estudiante estudiante, Libro libro) {
        this.fechaPrestamo = Objects.requireNonNull(fecha, "La fecha del préstamo es obligatoria");
        this.estudiante = Objects.requireNonNull(estudiante, "El estudiante es obligatorio");
        this.libro = Objects.requireNonNull(libro, "El libro es obligatorio");
    }

    public boolean estaActivo() {
        return fechaDevolucion == null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFechaPrestamo() {
        return fechaPrestamo;
    }

    public void setFechaPrestamo(LocalDate fechaPrestamo) {
        this.fechaPrestamo = fechaPrestamo;
    }

    public LocalDate getFechaDevolucion() {
        return fechaDevolucion;
    }

    public void setFechaDevolucion(LocalDate fechaDevolucion) {
        this.fechaDevolucion = fechaDevolucion;
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }

    public Libro getLibro() {
        return libro;
    }

    public void setLibro(Libro libro) {
        this.libro = libro;
    }
}
