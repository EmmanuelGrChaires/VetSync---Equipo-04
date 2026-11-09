/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vetsync.vetsync.citas.application;

import java.time.Clock;
import java.time.ZoneId;
import com.vetsync.vetsync.citas.domain.Cita;
import com.vetsync.vetsync.citas.domain.CitaInvalidaException;
import com.vetsync.vetsync.citas.domain.CitaRepository;
import com.vetsync.vetsync.citas.domain.MascotaConsulta;
import com.vetsync.vetsync.citas.domain.Veterinario;
import com.vetsync.vetsync.citas.domain.VeterinarioOcupadoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AgendarCitaServiceTest {

    private static final ZoneId ZONA = ZoneId.systemDefault();
    private static final LocalDateTime AHORA = LocalDateTime.of(2026, 11, 10, 9, 0);

    @Mock
    private CitaRepository citaRepository;

    @Mock
    private MascotaConsulta mascotaConsulta;

    private AgendarCitaService service;
    private Veterinario veterinario;
    private LocalDateTime inicio;
    private LocalDateTime fin;

    @BeforeEach
    void preparar() {
        Clock relojFijo = Clock.fixed(AHORA.atZone(ZONA).toInstant(), ZONA);
        service = new AgendarCitaService(citaRepository, mascotaConsulta, relojFijo);
        veterinario = new Veterinario("Dra. Lopez", "General", "lopez@vetsync.com");
        inicio = AHORA.plusDays(1).withHour(10);
        fin = inicio.plusMinutes(30);
    }

    @Test
    void agendaCitaCuandoTodoEsValido() {
        // Given
        when(mascotaConsulta.existe(1L)).thenReturn(true);
        when(citaRepository.existeTraslape(any(), any(), any())).thenReturn(false);
        when(citaRepository.guardar(any(Cita.class))).thenAnswer(inv -> inv.getArgument(0));

        // When
        Cita cita = service.agendarCita(1L, veterinario, inicio, fin, "Vacuna");

        // Then
        assertNotNull(cita);
        assertEquals(1L, cita.getMascotaId());
        assertEquals(inicio, cita.getInicio());
        verify(citaRepository).guardar(any(Cita.class));
    }

    @Test
    void rechazaCitaCuandoVeterinarioEstaOcupado() {
        // Given
        when(mascotaConsulta.existe(1L)).thenReturn(true);
        when(citaRepository.existeTraslape(any(), any(), any())).thenReturn(true);

        // When + Then
        assertThrows(VeterinarioOcupadoException.class,
                () -> service.agendarCita(1L, veterinario, inicio, fin, "Revision"));
        verify(citaRepository, never()).guardar(any(Cita.class));
    }

    @Test
    void rechazaCitaEnElPasado() {
        // Given
        LocalDateTime inicioPasado = AHORA.minusDays(1);
        LocalDateTime finPasado = inicioPasado.plusMinutes(30);

        // When + Then
        assertThrows(CitaInvalidaException.class,
                () -> service.agendarCita(1L, veterinario, inicioPasado, finPasado, "Revision"));
        verify(citaRepository, never()).guardar(any(Cita.class));
    }

    @Test
    void rechazaCitaCuandoMascotaNoEstaRegistrada() {
        // Given
        when(mascotaConsulta.existe(99L)).thenReturn(false);

        // When + Then
        assertThrows(CitaInvalidaException.class,
                () -> service.agendarCita(99L, veterinario, inicio, fin, "Revision"));
        verify(citaRepository, never()).guardar(any(Cita.class));
    }

    @Test
    void rechazaCitaCuandoFinEsAnteriorAlInicio() {
        // Given
        LocalDateTime finInvalido = inicio.minusMinutes(10);

        // When + Then
        assertThrows(CitaInvalidaException.class,
                () -> service.agendarCita(1L, veterinario, inicio, finInvalido, "Revision"));
        verify(citaRepository, never()).guardar(any(Cita.class));
        verify(mascotaConsulta, never()).existe(anyLong());
    }
}