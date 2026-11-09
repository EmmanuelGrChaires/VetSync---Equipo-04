/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vetsync.vetsync.clientes.domain;

import java.util.Optional;

public interface ClienteRepository {

    boolean existeEmail(String email);

    Dueno guardarDueno(Dueno dueno);

    Optional<Dueno> buscarDueno(Long duenoId);

    Mascota guardarMascota(Mascota mascota);

    Optional<Mascota> buscarMascota(Long mascotaId);

    HistorialMedico guardarHistorial(HistorialMedico historial);
}