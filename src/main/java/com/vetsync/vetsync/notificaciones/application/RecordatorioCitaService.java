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
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

public class RecordatorioCitaService {

    private static final long HORAS_ANTICIPACION = 24;

    private final CitasProximasConsulta citasProximas;
    private final NotificacionRepository notificacionRepository;
    private final Notificador notificador;
    private final Clock reloj;

    public RecordatorioCitaService(CitasProximasConsulta citasProximas,
                                   NotificacionRepository notificacionRepository,
                                   Notificador notificador, Clock reloj) {
        this.citasProximas = citasProximas;
        this.notificacionRepository = notificacionRepository;
        this.notificador = notificador;
        this.reloj = reloj;
    }

    public int enviarRecordatorios() {
        LocalDateTime ahora = LocalDateTime.now(reloj);
        List<CitaProxima> proximas = citasProximas.buscarEntre(ahora, ahora.plusHours(HORAS_ANTICIPACION));

        int enviados = 0;
        for (CitaProxima cita : proximas) {
            if (notificacionRepository.yaNotificada(cita.citaId())) {
                continue;
            }
            String asunto = "Recordatorio de cita para " + cita.nombreMascota();
            String mensaje = "Su cita es el " + cita.inicio();
            notificador.enviar(cita.emailDestinatario(), asunto, mensaje);
            notificacionRepository.guardar(
                    new Notificacion(cita.citaId(), cita.emailDestinatario(), asunto, ahora));
            enviados++;
        }
        return enviados;
    }
}