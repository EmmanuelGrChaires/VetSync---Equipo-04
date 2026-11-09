/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vetsync.vetsync.notificaciones.domain;

import java.time.LocalDateTime;

public record CitaProxima(Long citaId, String emailDestinatario, String nombreMascota, LocalDateTime inicio) {
}