/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vetsync.vetsync.notificaciones.infrastructure;

import com.vetsync.vetsync.notificaciones.domain.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificacionJpaRepository extends JpaRepository<Notificacion, Long> {

    boolean existsByCitaId(Long citaId);
}