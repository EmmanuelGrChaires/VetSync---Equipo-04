/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vetsync.vetsync.citas.infrastructure;

import com.vetsync.vetsync.citas.domain.Cita;
import com.vetsync.vetsync.citas.domain.CitaRepository;
import java.time.LocalDateTime;
import org.springframework.stereotype.Repository;

@Repository
public class CitaRepositoryJpa implements CitaRepository {

    private final CitaJpaRepository jpa;

    public CitaRepositoryJpa(CitaJpaRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public Cita guardar(Cita cita) {
        return jpa.save(cita);
    }

    @Override
    public boolean existeTraslape(Long veterinarioId, LocalDateTime inicio, LocalDateTime fin) {
        return jpa.existeTraslape(veterinarioId, inicio, fin);
    }
}