package com.smartlibrary.service;

import com.smartlibrary.model.Estudiante;
import com.smartlibrary.model.Libro;
import com.smartlibrary.model.Prestamo;
import com.smartlibrary.repository.PrestamoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Coordina los casos de uso «Registrar préstamo» y «Consultar préstamos».
 * Depende de EstudianteService, LibroService y PrestamoRepository (no accede a los otros repositorios).
 * Define la frontera transaccional; las reglas que dependen de una sola entidad
 * viven en esa entidad (Libro.marcarPrestado, constructor de Prestamo).
 */
@Service
public class PrestamoService {

    private final EstudianteService estudianteService;
    private final LibroService libroService;
    private final PrestamoRepository prestamoRepository;

    public PrestamoService(EstudianteService estudianteService,
                           LibroService libroService,
                           PrestamoRepository prestamoRepository) {
        this.estudianteService = estudianteService;
        this.libroService = libroService;
        this.prestamoRepository = prestamoRepository;
    }

    /**
     * Una sola transacción (R4): si falla cualquier paso, no se guarda el préstamo
     * ni se cambia la disponibilidad del libro.
     *
     * @throws NoSuchElementException   si el estudiante o el libro no existen
     * @throws com.smartlibrary.model.LibroNoDisponibleException si el libro no está disponible (R1)
     */
    @Transactional
    public Prestamo registrarPrestamo(Long estudianteId, Long libroId) {
        Estudiante estudiante = estudianteService.buscarPorId(estudianteId);
        Libro libro = libroService.buscarPorId(libroId);

        // El libro es una entidad gestionada: el cambio de disponibilidad se confirma
        // junto con el préstamo al cerrar la transacción.
        libro.marcarPrestado();                                              // R1 y R3
        Prestamo prestamo = new Prestamo(LocalDate.now(), estudiante, libro); // R2

        return prestamoRepository.guardar(prestamo);
    }

    /**
     * Consulta de préstamos: por estudiante (con o sin filtro de activos) o, sin estudiante,
     * todos los préstamos activos.
     *
     * @throws IllegalArgumentException si no se indica estudianteId ni soloActivos
     */
    @Transactional(readOnly = true)
    public List<Prestamo> consultarPrestamos(Long estudianteId, boolean soloActivos) {
        if (estudianteId == null && !soloActivos) {
            throw new IllegalArgumentException("Indique estudianteId o soloActivos=true");
        }
        if (estudianteId == null) {
            return prestamoRepository.buscarActivos();
        }
        List<Prestamo> prestamos = prestamoRepository.buscarPorEstudiante(estudianteId);
        if (!soloActivos) {
            return prestamos;
        }
        return prestamos.stream().filter(Prestamo::estaActivo).toList();
    }
}
