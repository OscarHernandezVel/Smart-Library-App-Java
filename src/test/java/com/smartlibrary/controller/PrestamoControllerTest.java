package com.smartlibrary.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Prueba de extremo a extremo (HTTP -> Controller -> Service -> Repository -> H2).
 * Datos de data.sql: estudiantes 1..3; libros 1 y 2 disponibles, libro 3 ya prestado.
 * Cada prueba se revierte al terminar (@Transactional).
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class PrestamoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private static String cuerpo(String estudianteId, String libroId) {
        return "{\"estudianteId\":" + estudianteId + ",\"libroId\":" + libroId + "}";
    }

    @Test
    void camino1_registraElPrestamoCuandoElLibroEstaDisponible() throws Exception {
        mockMvc.perform(post("/api/prestamos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cuerpo("1", "1")))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.estudianteId").value(1))
                .andExpect(jsonPath("$.libroId").value(1))
                .andExpect(jsonPath("$.fechaPrestamo").exists());
    }

    @Test
    void unSegundoPrestamoDelMismoLibroEsRechazado() throws Exception {
        mockMvc.perform(post("/api/prestamos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cuerpo("1", "2")))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/prestamos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cuerpo("2", "2")))
                .andExpect(status().isConflict());
    }

    @Test
    void camino2_rechazaConConflictoCuandoElLibroNoEstaDisponible() throws Exception {
        mockMvc.perform(post("/api/prestamos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cuerpo("1", "3")))
                .andExpect(status().isConflict());

        // R4: no se creó ningún préstamo
        mockMvc.perform(get("/api/prestamos").param("soloActivos", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void estudianteOLibroInexistenteDevuelve404() throws Exception {
        mockMvc.perform(post("/api/prestamos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cuerpo("1", "999")))
                .andExpect(status().isNotFound());

        mockMvc.perform(post("/api/prestamos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cuerpo("999", "1")))
                .andExpect(status().isNotFound());
    }

    @Test
    void solicitudInvalidaDevuelve400() throws Exception {
        mockMvc.perform(post("/api/prestamos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cuerpo("null", "1")))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post("/api/prestamos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void consultaLosPrestamosDeUnEstudiante() throws Exception {
        mockMvc.perform(post("/api/prestamos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cuerpo("1", "1")))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/prestamos").param("estudianteId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].libroId").value(1));

        mockMvc.perform(get("/api/prestamos").param("estudianteId", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void consultaSinCriteriosDevuelve400() throws Exception {
        mockMvc.perform(get("/api/prestamos"))
                .andExpect(status().isBadRequest());
    }
}
