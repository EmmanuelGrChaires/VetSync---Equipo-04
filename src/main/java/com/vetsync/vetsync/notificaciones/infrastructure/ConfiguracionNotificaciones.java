/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vetsync.vetsync.notificaciones.infrastructure;

import com.vetsync.vetsync.notificaciones.application.RecordatorioCitaService;
import com.vetsync.vetsync.notificaciones.domain.CitasProximasConsulta;
import com.vetsync.vetsync.notificaciones.domain.NotificacionRepository;
import com.vetsync.vetsync.notificaciones.domain.Notificador;
import java.time.Clock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class ConfiguracionNotificaciones {

    private final RecordatorioCitaService recordatorioService;

    public ConfiguracionNotificaciones(RecordatorioCitaService recordatorioService) {
        this.recordatorioService = recordatorioService;
    }

    @Bean
    public static RecordatorioCitaService recordatorioCitaService(CitasProximasConsulta citasProximas,
                                                                  NotificacionRepository repo,
                                                                  Notificador notificador,
                                                                  Clock reloj) {
        return new RecordatorioCitaService(citasProximas, repo, notificador, reloj);
    }

    @Scheduled(fixedRate = 3600000)
    public void ejecutarRecordatorios() {
        recordatorioService.enviarRecordatorios();
    }
}
