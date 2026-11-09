/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vetsync.vetsync.notificaciones.application;

import com.vetsync.vetsync.notificaciones.domain.CitaProxima;
import com.vetsync.vetsync.notificaciones.domain.CitasProximasConsulta;
import com.vetsync.vetsync.notificaciones.domain.Notificacion;
import com.vetsync.vetsync.notificaciones.domain.NotificacionRepository;
import com.vetsync.vetsync.notificaciones.domain.Notificador;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RecordatorioCitaServiceTest {

    private static final ZoneId ZONA = ZoneId.systemDefault();
    private static final LocalDateTime AHORA = LocalDateTime.of(2026, 11, 10, 9, 0);

    @Mock
    private CitasProximasConsulta citasProximas;

    @Mock
    private NotificacionRepository notificacionRepository;

    @Mock
    private Notificador notificador;

    private RecordatorioCitaService service;

    @BeforeEach
    void preparar() {
        Clock relojFijo = Clock.fixed(AHORA.atZone(ZONA).toInstant(), ZONA);
        service = new RecordatorioCitaService(citasProximas, notificacionRepository, notificador, relojFijo);
    }

    private CitaProxima cita(long id) {
        return new CitaProxima(id, "dueno" + id + "@mail.com", "Firulais", AHORA.plusHours(5));
    }

    @Test
    void enviaRecordatorioParaCitaProximaNoNotificada() {
        when(citasProximas.buscarEntre(AHORA, AHORA.plusHours(24))).thenReturn(List.of(cita(1L)));
        when(notificacionRepository.yaNotificada(1L)).thenReturn(false);

        int enviados = service.enviarRecordatorios();

        assertEquals(1, enviados);
        verify(notificador).enviar(any(), any(), any());
        verify(notificacionRepository).guardar(any(Notificacion.class));
    }

    @Test
    void noEnviaSiLaCitaYaFueNotificada() {
        when(citasProximas.buscarEntre(AHORA, AHORA.plusHours(24))).thenReturn(List.of(cita(1L)));
        when(notificacionRepository.yaNotificada(1L)).thenReturn(true);

        int enviados = service.enviarRecordatorios();

        assertEquals(0, enviados);
        verify(notificador, never()).enviar(anyString(), anyString(), anyString());
        verify(notificacionRepository, never()).guardar(any(Notificacion.class));
    }

    @Test
    void noHaceNadaSiNoHayCitasProximas() {
        when(citasProximas.buscarEntre(AHORA, AHORA.plusHours(24))).thenReturn(List.of());

        int enviados = service.enviarRecordatorios();

        assertEquals(0, enviados);
        verify(notificador, never()).enviar(anyString(), anyString(), anyString());
    }

    @Test
    void enviaSoloALasNoNotificadasCuandoHayVarias() {
        when(citasProximas.buscarEntre(AHORA, AHORA.plusHours(24))).thenReturn(List.of(cita(1L), cita(2L)));
        when(notificacionRepository.yaNotificada(1L)).thenReturn(true);
        when(notificacionRepository.yaNotificada(2L)).thenReturn(false);

        int enviados = service.enviarRecordatorios();

        assertEquals(1, enviados);
        verify(notificador).enviar(any(), any(), any());
    }
}