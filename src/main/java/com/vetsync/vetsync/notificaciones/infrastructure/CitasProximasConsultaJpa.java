/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vetsync.vetsync.notificaciones.infrastructure;

import com.vetsync.vetsync.notificaciones.domain.CitaProxima;
import com.vetsync.vetsync.notificaciones.domain.CitasProximasConsulta;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class CitasProximasConsultaJpa implements CitasProximasConsulta {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @SuppressWarnings("unchecked")
    public List<CitaProxima> buscarEntre(LocalDateTime desde, LocalDateTime hasta) {
        List<Object[]> filas = entityManager.createNativeQuery("""
                select c.id, d.email, m.nombre, c.inicio
                from citas c
                join mascotas m on m.id = c.mascota_id
                join duenos d on d.id = m.dueno_id
                where c.estado = 'PROGRAMADA'
                  and c.inicio >= :desde
                  and c.inicio <= :hasta
                """)
                .setParameter("desde", desde)
                .setParameter("hasta", hasta)
                .getResultList();

        return filas.stream()
                .map(f -> new CitaProxima(
                        ((Number) f[0]).longValue(),
                        (String) f[1],
                        (String) f[2],
                        ((Timestamp) f[3]).toLocalDateTime()))
                .toList();
    }
}