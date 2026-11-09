/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.vetsync.vetsync.citas.application;

import com.vetsync.vetsync.citas.domain.Cita;
import com.vetsync.vetsync.citas.domain.CitaInvalidaException;
import com.vetsync.vetsync.citas.domain.CitaRepository;
import com.vetsync.vetsync.citas.domain.MascotaConsulta;
import com.vetsync.vetsync.citas.domain.Veterinario;
import com.vetsync.vetsync.citas.domain.VeterinarioOcupadoException;
import java.time.Clock;
import java.time.LocalDateTime;

public class AgendarCitaService {

    private final CitaRepository citaRepository;
    private final MascotaConsulta mascotaConsulta;
    private final Clock reloj;

    public AgendarCitaService(CitaRepository citaRepository, MascotaConsulta mascotaConsulta, Clock reloj) {
        this.citaRepository = citaRepository;
        this.mascotaConsulta = mascotaConsulta;
        this.reloj = reloj;
    }

    public Cita agendarCita(Long mascotaId, Veterinario veterinario,
                            LocalDateTime inicio, LocalDateTime fin, String motivo) {
        validarFechas(inicio, fin);

        if (!mascotaConsulta.existe(mascotaId)) {
            throw new CitaInvalidaException("La mascota no esta registrada");
        }

        if (citaRepository.existeTraslape(veterinario.getId(), inicio, fin)) {
            throw new VeterinarioOcupadoException("El veterinario ya tiene una cita en ese horario");
        }

        return citaRepository.guardar(new Cita(mascotaId, veterinario, inicio, fin, motivo));
    }

    private void validarFechas(LocalDateTime inicio, LocalDateTime fin) {
        if (!fin.isAfter(inicio)) {
            throw new CitaInvalidaException("La hora de fin debe ser posterior al inicio");
        }
        if (inicio.isBefore(LocalDateTime.now(reloj))) {
            throw new CitaInvalidaException("No se puede agendar una cita en el pasado");
        }
    }
}