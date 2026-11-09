/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vetsync.vetsync.notificaciones.infrastructure;

import com.vetsync.vetsync.notificaciones.domain.Notificacion;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NotificacionRepositoryJpaTest {

    @Mock
    private NotificacionJpaRepository jpa;

    @InjectMocks
    private NotificacionRepositoryJpa repo;

    @Test
    void delegaYaNotificada() {
        when(jpa.existsByCitaId(1L)).thenReturn(true);
        assertTrue(repo.yaNotificada(1L));
    }

    @Test
    void delegaGuardar() {
        Notificacion n = new Notificacion(1L, "a@b.com", "Asunto", LocalDateTime.now());
        repo.guardar(n);
        verify(jpa).save(n);
    }
}