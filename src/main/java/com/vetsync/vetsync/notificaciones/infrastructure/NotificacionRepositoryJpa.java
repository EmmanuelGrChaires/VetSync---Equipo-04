/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vetsync.vetsync.notificaciones.infrastructure;

import com.vetsync.vetsync.notificaciones.domain.Notificacion;
import com.vetsync.vetsync.notificaciones.domain.NotificacionRepository;
import org.springframework.stereotype.Repository;

@Repository
public class NotificacionRepositoryJpa implements NotificacionRepository {

    private final NotificacionJpaRepository jpa;

    public NotificacionRepositoryJpa(NotificacionJpaRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public boolean yaNotificada(Long citaId) {
        return jpa.existsByCitaId(citaId);
    }

    @Override
    public void guardar(Notificacion notificacion) {
        jpa.save(notificacion);
    }
}