/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vetsync.vetsync.notificaciones.infrastructure;

import com.vetsync.vetsync.notificaciones.domain.Notificador;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class NotificadorCorreoSimulado implements Notificador {

    private static final Logger LOG = LoggerFactory.getLogger(NotificadorCorreoSimulado.class);

    @Override
    public void enviar(String destinatario, String asunto, String mensaje) {
        LOG.info("[CORREO SIMULADO] Para: {} | Asunto: {} | Mensaje: {}", destinatario, asunto, mensaje);
    }
}