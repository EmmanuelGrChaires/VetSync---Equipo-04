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
import java.time.LocalDate;

public class RegistroClienteService {

    private final ClienteRepository repository;

    public RegistroClienteService(ClienteRepository repository) {
        this.repository = repository;
    }

    public Dueno registrarDueno(String nombre, String email, String telefono) {
        if (email == null || email.isBlank()) {
            throw new ClienteInvalidoException("El email es obligatorio");
        }
        if (repository.existeEmail(email)) {
            throw new ClienteInvalidoException("Ya existe un dueno con ese email");
        }
        return repository.guardarDueno(new Dueno(nombre, email, telefono));
    }

    public Mascota registrarMascota(Long duenoId, String nombre, String especie,
                                    String raza, LocalDate fechaNacimiento) {
        Dueno dueno = repository.buscarDueno(duenoId)
                .orElseThrow(() -> new ClienteInvalidoException("El dueno no existe"));
        return repository.guardarMascota(new Mascota(dueno, nombre, especie, raza, fechaNacimiento));
    }

    public HistorialMedico agregarHistorial(Long mascotaId, Long citaId, LocalDate fecha,
                                            String diagnostico, String tratamiento) {
        if (diagnostico == null || diagnostico.isBlank()) {
            throw new ClienteInvalidoException("El diagnostico es obligatorio");
        }
        Mascota mascota = repository.buscarMascota(mascotaId)
                .orElseThrow(() -> new ClienteInvalidoException("La mascota no existe"));
        return repository.guardarHistorial(new HistorialMedico(mascota, citaId, fecha, diagnostico, tratamiento));
    }
}