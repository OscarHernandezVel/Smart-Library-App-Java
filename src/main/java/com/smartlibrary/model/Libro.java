package com.smartlibrary.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Entidad de dominio: ejemplar que puede prestarse.
 * Es el experto en su propia disponibilidad (R1 y R3).
 */
@Entity
@Table(name = "libro")
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false)
    private boolean disponible;

    /** Constructor requerido por JPA. */
    protected Libro() {
    }

    public Libro(Long id, String titulo, boolean disponible) {
        this.id = id;
        this.titulo = titulo;
        this.disponible = disponible;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    /**
     * R1: solo un libro disponible puede prestarse.
     * R3: al prestarlo, el libro deja de estar disponible.
     *
     * @throws LibroNoDisponibleException si el libro ya está prestado
     */
    public void marcarPrestado() {
        if (!disponible) {
            throw new LibroNoDisponibleException(id);
        }
        disponible = false;
    }
}
