package com.smartlibrary.controller;

import com.smartlibrary.dto.PrestamoRequest;
import com.smartlibrary.dto.PrestamoResponse;
import com.smartlibrary.model.LibroNoDisponibleException;
import com.smartlibrary.service.PrestamoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Entrada y salida HTTP. No contiene reglas de negocio ni accede a repositorios:
 * valida el formato, delega en un único método del servicio y elige el código de respuesta.
 */
@RestController
@RequestMapping("/api/prestamos")
public class PrestamoController {

    private final PrestamoService prestamoService;

    public PrestamoController(PrestamoService prestamoService) {
        this.prestamoService = prestamoService;
    }

    /**
     * 201 Created: préstamo registrado.
     * 400 Bad Request: solicitud inválida (la validación de formato corre antes de entrar aquí).
     * 404 Not Found: el estudiante o el libro no existen.
     * 409 Conflict: el libro no está disponible (R1).
     */
    @PostMapping
    public ResponseEntity<PrestamoResponse> registrar(@Valid @RequestBody PrestamoRequest request) {
        try {
            PrestamoResponse response = PrestamoResponse.desde(
                    prestamoService.registrarPrestamo(request.getEstudianteId(), request.getLibroId()));
            return ResponseEntity.created(URI.create("/api/prestamos/" + response.getId())).body(response);
        } catch (LibroNoDisponibleException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /** Consulta por estudiante (estudianteId) y/o solo préstamos activos (soloActivos=true). */
    @GetMapping
    public List<PrestamoResponse> consultar(
            @RequestParam(required = false) Long estudianteId,
            @RequestParam(defaultValue = "false") boolean soloActivos) {
        try {
            return prestamoService.consultarPrestamos(estudianteId, soloActivos).stream()
                    .map(PrestamoResponse::desde)
                    .toList();
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
