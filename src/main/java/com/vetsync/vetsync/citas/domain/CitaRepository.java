/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vetsync.vetsync.citas.domain;

import java.time.LocalDateTime;

public interface CitaRepository {

    Cita guardar(Cita cita);

    boolean existeTraslape(Long veterinarioId, LocalDateTime inicio, LocalDateTime fin);
}