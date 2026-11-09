/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vetsync.vetsync.clientes.application;

import com.vetsync.vetsync.clientes.domain.ClienteInvalidoException;
import com.vetsync.vetsync.clientes.domain.ClienteRepository;
import com.vetsync.vetsync.clientes.domain.Dueno;
import com.vetsync.vetsync.clientes.domain.HistorialMedico;
import com.vetsync.vetsync.clientes.domain.Mascota;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegistroClienteServiceTest {

    @Mock
    private ClienteRepository repository;

    private RegistroClienteService service;

    @BeforeEach
    void preparar() {
        service = new RegistroClienteService(repository);
    }

    @Test
    void registraDuenoConEmailNuevo() {
        when(repository.existeEmail("ana@mail.com")).thenReturn(false);
        when(repository.guardarDueno(any(Dueno.class))).thenAnswer(inv -> inv.getArgument(0));

        Dueno dueno = service.registrarDueno("Ana", "ana@mail.com", "555");

        assertNotNull(dueno);
        assertEquals("ana@mail.com", dueno.getEmail());
    }

    @Test
    void rechazaDuenoConEmailRepetido() {
        when(repository.existeEmail("ana@mail.com")).thenReturn(true);

        assertThrows(ClienteInvalidoException.class,
                () -> service.registrarDueno("Ana", "ana@mail.com", "555"));
        verify(repository, never()).guardarDueno(any(Dueno.class));
    }

    @Test
    void rechazaDuenoSinEmail() {
        assertThrows(ClienteInvalidoException.class,
                () -> service.registrarDueno("Ana", " ", "555"));
        verify(repository, never()).guardarDueno(any(Dueno.class));
    }

    @Test
    void registraMascotaParaDuenoExistente() {
        Dueno dueno = new Dueno("Ana", "ana@mail.com", "555");
        when(repository.buscarDueno(1L)).thenReturn(Optional.of(dueno));
        when(repository.guardarMascota(any(Mascota.class))).thenAnswer(inv -> inv.getArgument(0));

        Mascota mascota = service.registrarMascota(1L, "Firulais", "Perro", "Mestizo", LocalDate.of(2020, 5, 1));

        assertNotNull(mascota);
        assertEquals("Firulais", mascota.getNombre());
    }

    @Test
    void rechazaMascotaDeDuenoInexistente() {
        when(repository.buscarDueno(99L)).thenReturn(Optional.empty());

        assertThrows(ClienteInvalidoException.class,
                () -> service.registrarMascota(99L, "Firulais", "Perro", null, null));
        verify(repository, never()).guardarMascota(any(Mascota.class));
    }

    @Test
    void agregaHistorialAMascotaExistente() {
        Mascota mascota = new Mascota(new Dueno("Ana", "ana@mail.com", "555"), "Firulais", "Perro", null, null);
        when(repository.buscarMascota(1L)).thenReturn(Optional.of(mascota));
        when(repository.guardarHistorial(any(HistorialMedico.class))).thenAnswer(inv -> inv.getArgument(0));

        HistorialMedico h = service.agregarHistorial(1L, null, LocalDate.of(2026, 11, 8), "Otitis", "Gotas");

        assertNotNull(h);
        assertEquals("Otitis", h.getDiagnostico());
    }

    @Test
    void rechazaHistorialSinDiagnostico() {
        assertThrows(ClienteInvalidoException.class,
                () -> service.agregarHistorial(1L, null, LocalDate.of(2026, 11, 8), "", "Gotas"));
        verify(repository, never()).guardarHistorial(any(HistorialMedico.class));
    }
}