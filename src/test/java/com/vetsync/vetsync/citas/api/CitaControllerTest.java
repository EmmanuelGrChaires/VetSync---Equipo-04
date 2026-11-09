/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vetsync.vetsync.citas.api;

import com.vetsync.vetsync.citas.application.AgendarCitaService;
import com.vetsync.vetsync.citas.domain.Cita;
import com.vetsync.vetsync.citas.domain.CitaInvalidaException;
import com.vetsync.vetsync.citas.domain.Veterinario;
import com.vetsync.vetsync.citas.domain.VeterinarioOcupadoException;
import com.vetsync.vetsync.citas.infrastructure.VeterinarioJpaRepository;
import com.vetsync.vetsync.shared.ManejadorErrores;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class CitaControllerTest {

    private static final String JSON = """
            {"mascotaId":1,"veterinarioId":1,
             "inicio":"2026-11-20T10:00:00","fin":"2026-11-20T10:30:00","motivo":"Vacuna"}
            """;

    @Mock
    private AgendarCitaService service;

    @Mock
    private VeterinarioJpaRepository veterinarios;

    private MockMvc mvc;
    private Veterinario vet;

    @BeforeEach
    void preparar() {
        mvc = MockMvcBuilders.standaloneSetup(new CitaController(service, veterinarios))
                .setControllerAdvice(new ManejadorErrores())
                .build();
        vet = new Veterinario("Dra. Lopez", "General", "lopez@vetsync.com");
    }

    @Test
    void devuelve201CuandoLaCitaSeAgenda() throws Exception {
        LocalDateTime inicio = LocalDateTime.of(2026, 11, 20, 10, 0);
        Cita cita = new Cita(1L, vet, inicio, inicio.plusMinutes(30), "Vacuna");
        when(veterinarios.findById(1L)).thenReturn(Optional.of(vet));
        when(service.agendarCita(eq(1L), any(), any(), any(), any())).thenReturn(cita);

        mvc.perform(post("/api/citas").contentType(MediaType.APPLICATION_JSON).content(JSON))
                .andExpect(status().isCreated());
    }

    @Test
    void devuelve409CuandoElVeterinarioEstaOcupado() throws Exception {
        when(veterinarios.findById(1L)).thenReturn(Optional.of(vet));
        when(service.agendarCita(anyLong(), any(), any(), any(), any()))
                .thenThrow(new VeterinarioOcupadoException("ocupado"));

        mvc.perform(post("/api/citas").contentType(MediaType.APPLICATION_JSON).content(JSON))
                .andExpect(status().isConflict());
    }

    @Test
    void devuelve400CuandoElVeterinarioNoExiste() throws Exception {
        when(veterinarios.findById(1L)).thenReturn(Optional.empty());

        mvc.perform(post("/api/citas").contentType(MediaType.APPLICATION_JSON).content(JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void devuelve400CuandoLaCitaEsInvalida() throws Exception {
        when(veterinarios.findById(1L)).thenReturn(Optional.of(vet));
        when(service.agendarCita(anyLong(), any(), any(), any(), any()))
                .thenThrow(new CitaInvalidaException("invalida"));

        mvc.perform(post("/api/citas").contentType(MediaType.APPLICATION_JSON).content(JSON))
                .andExpect(status().isBadRequest());
    }
}