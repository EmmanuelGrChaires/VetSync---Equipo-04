/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vetsync.vetsync.notificaciones.infrastructure;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class NotificadorCorreoSimuladoTest {

    @Test
    void enviaSinLanzarErrores() {
        assertDoesNotThrow(() -> new NotificadorCorreoSimulado()
                .enviar("a@b.com", "Asunto", "Mensaje"));
    }
}