/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vetsync.vetsync.clientes.infrastructure;

import com.vetsync.vetsync.clientes.domain.ClienteRepository;
import com.vetsync.vetsync.clientes.domain.Dueno;
import com.vetsync.vetsync.clientes.domain.HistorialMedico;
import com.vetsync.vetsync.clientes.domain.Mascota;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class ClienteRepositoryJpa implements ClienteRepository {

    private final DuenoJpaRepository duenos;
    private final MascotaJpaRepository mascotas;
    private final HistorialJpaRepository historiales;

    public ClienteRepositoryJpa(DuenoJpaRepository duenos, MascotaJpaRepository mascotas,
                                HistorialJpaRepository historiales) {
        this.duenos = duenos;
        this.mascotas = mascotas;
        this.historiales = historiales;
    }

    @Override
    public boolean existeEmail(String email) {
        return duenos.existsByEmail(email);
    }

    @Override
    public Dueno guardarDueno(Dueno dueno) {
        return duenos.save(dueno);
    }

    @Override
    public Optional<Dueno> buscarDueno(Long duenoId) {
        return duenos.findById(duenoId);
    }

    @Override
    public Mascota guardarMascota(Mascota mascota) {
        return mascotas.save(mascota);
    }

    @Override
    public Optional<Mascota> buscarMascota(Long mascotaId) {
        return mascotas.findById(mascotaId);
    }

    @Override
    public HistorialMedico guardarHistorial(HistorialMedico historial) {
        return historiales.save(historial);
    }
}