/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vetsync.vetsync.clientes.api;

import com.vetsync.vetsync.clientes.application.RegistroClienteService;
import com.vetsync.vetsync.clientes.domain.ClienteInvalidoException;
import com.vetsync.vetsync.clientes.domain.Dueno;
import com.vetsync.vetsync.clientes.domain.HistorialMedico;
import com.vetsync.vetsync.clientes.domain.Mascota;
import com.vetsync.vetsync.shared.ManejadorErrores;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ClienteControllerTest {

    @Mock
    private RegistroClienteService service;

    private MockMvc mvc;
    private Dueno dueno;
    private Mascota mascota;

    @BeforeEach
    void preparar() {
        mvc = MockMvcBuilders.standaloneSetup(new ClienteController(service))
                .setControllerAdvice(new ManejadorErrores())
                .build();
        dueno = new Dueno("Ana", "ana@mail.com", "555");
        mascota = new Mascota(dueno, "Firulais", "Perro", null, null);
    }

    @Test
    void registraDueno() throws Exception {
        when(service.registrarDueno(any(), any(), any())).thenReturn(dueno);

        mvc.perform(post("/api/duenos").contentType(MediaType.APPLICATION_JSON)
                .content("{\"nombre\":\"Ana\",\"email\":\"ana@mail.com\",\"telefono\":\"555\"}"))
                .andExpect(status().isCreated());
    }

    @Test
    void rechazaDuenoRepetidoCon400() throws Exception {
        when(service.registrarDueno(any(), any(), any()))
                .thenThrow(new ClienteInvalidoException("repetido"));

        mvc.perform(post("/api/duenos").contentType(MediaType.APPLICATION_JSON)
                .content("{\"nombre\":\"Ana\",\"email\":\"ana@mail.com\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void registraMascota() throws Exception {
        when(service.registrarMascota(anyLong(), any(), any(), any(), any())).thenReturn(mascota);

        mvc.perform(post("/api/duenos/1/mascotas").contentType(MediaType.APPLICATION_JSON)
                .content("{\"nombre\":\"Firulais\",\"especie\":\"Perro\"}"))
                .andExpect(status().isCreated());
    }

    @Test
    void agregaHistorial() throws Exception {
        HistorialMedico h = new HistorialMedico(mascota, null, LocalDate.of(2026, 11, 8), "Otitis", "Gotas");
        when(service.agregarHistorial(anyLong(), any(), any(), any(), any())).thenReturn(h);

        mvc.perform(post("/api/mascotas/1/historial").contentType(MediaType.APPLICATION_JSON)
                .content("{\"fecha\":\"2026-11-08\",\"diagnostico\":\"Otitis\",\"tratamiento\":\"Gotas\"}"))
                .andExpect(status().isCreated());
    }
}