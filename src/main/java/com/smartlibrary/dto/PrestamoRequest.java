package com.smartlibrary.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/** Objeto de entrada de POST /api/prestamos. Valida solo el formato (R4). */
public class PrestamoRequest {

    @NotNull
    @Positive
    private Long estudianteId;

    @NotNull
    @Positive
    private Long libroId;

    public PrestamoRequest() {
    }

    public PrestamoRequest(Long estudianteId, Long libroId) {
        this.estudianteId = estudianteId;
        this.libroId = libroId;
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
}
