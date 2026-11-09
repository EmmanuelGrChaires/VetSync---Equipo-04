/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vetsync.vetsync.notificaciones.domain;

import java.time.LocalDateTime;
import java.util.List;

public interface CitasProximasConsulta {

    List<CitaProxima> buscarEntre(LocalDateTime desde, LocalDateTime hasta);
}