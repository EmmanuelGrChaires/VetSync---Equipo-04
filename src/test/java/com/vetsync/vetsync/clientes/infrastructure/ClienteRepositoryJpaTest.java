/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vetsync.vetsync.clientes.infrastructure;

import com.vetsync.vetsync.clientes.domain.Dueno;
import com.vetsync.vetsync.clientes.domain.HistorialMedico;
import com.vetsync.vetsync.clientes.domain.Mascota;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClienteRepositoryJpaTest {

    @Mock
    private DuenoJpaRepository duenos;
    @Mock
    private MascotaJpaRepository mascotas;
    @Mock
    private HistorialJpaRepository historiales;

    @InjectMocks
    private ClienteRepositoryJpa repo;

    private final Dueno dueno = new Dueno("Ana", "ana@mail.com", "555");
    private final Mascota mascota = new Mascota(dueno, "Firulais", "Perro", null, null);

    @Test
    void delegaExisteEmail() {
        when(duenos.existsByEmail("ana@mail.com")).thenReturn(true);
        assertTrue(repo.existeEmail("ana@mail.com"));
    }

    @Test
    void delegaGuardadoYBusqueda() {
        when(duenos.save(dueno)).thenReturn(dueno);
        when(duenos.findById(1L)).thenReturn(Optional.of(dueno));
        when(mascotas.save(mascota)).thenReturn(mascota);
        when(mascotas.findById(1L)).thenReturn(Optional.of(mascota));
        HistorialMedico h = new HistorialMedico(mascota, null, LocalDate.of(2026, 11, 8), "Otitis", "Gotas");
        when(historiales.save(h)).thenReturn(h);

        assertEquals(dueno, repo.guardarDueno(dueno));
        assertEquals(dueno, repo.buscarDueno(1L).orElseThrow());
        assertEquals(mascota, repo.guardarMascota(mascota));
        assertEquals(mascota, repo.buscarMascota(1L).orElseThrow());
        assertEquals(h, repo.guardarHistorial(h));
    }
}