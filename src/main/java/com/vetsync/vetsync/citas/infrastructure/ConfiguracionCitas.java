/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vetsync.vetsync.citas.infrastructure;

import com.vetsync.vetsync.citas.application.AgendarCitaService;
import com.vetsync.vetsync.citas.domain.CitaRepository;
import com.vetsync.vetsync.citas.domain.MascotaConsulta;
import java.time.Clock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfiguracionCitas {

    @Bean
    public Clock reloj() {
        return Clock.systemDefaultZone();
    }

    @Bean
    public AgendarCitaService agendarCitaService(CitaRepository citaRepository,
                                                 MascotaConsulta mascotaConsulta,
                                                 Clock reloj) {
        return new AgendarCitaService(citaRepository, mascotaConsulta, reloj);
    }
}