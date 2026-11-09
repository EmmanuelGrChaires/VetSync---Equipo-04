/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vetsync.vetsync.citas.infrastructure;

import com.vetsync.vetsync.citas.domain.Cita;
import com.vetsync.vetsync.citas.domain.Veterinario;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class CitaRepositoryJpaTest {

    @Autowired
    private CitaJpaRepository citas;

    @Autowired
    private VeterinarioJpaRepository veterinarios;

    private final LocalDateTime inicio = LocalDateTime.of(2026, 11, 20, 10, 0);

    @Test
    void detectaTraslapeParcial() {
        Veterinario vet = veterinarios.save(new Veterinario("Dra. Lopez", "General", "l@vet.com"));
        citas.save(new Cita(1L, vet, inicio, inicio.plusMinutes(30), "Vacuna"));

        assertTrue(citas.existeTraslape(vet.getId(), inicio.plusMinutes(15), inicio.plusMinutes(45)));
    }

    @Test
    void noHayTraslapeSiUnaTerminaCuandoEmpiezaLaOtra() {
        Veterinario vet = veterinarios.save(new Veterinario("Dra. Lopez", "General", "l@vet.com"));
        citas.save(new Cita(1L, vet, inicio, inicio.plusMinutes(30), "Vacuna"));

        assertFalse(citas.existeTraslape(vet.getId(), inicio.plusMinutes(30), inicio.plusMinutes(60)));
    }
}