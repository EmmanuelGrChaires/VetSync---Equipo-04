/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vetsync.vetsync.notificaciones.domain;

public interface NotificacionRepository {

    boolean yaNotificada(Long citaId);

    void guardar(Notificacion notificacion);
}