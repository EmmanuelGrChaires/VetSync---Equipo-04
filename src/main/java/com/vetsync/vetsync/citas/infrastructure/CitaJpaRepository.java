/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vetsync.vetsync.citas.infrastructure;

import com.vetsync.vetsync.citas.domain.Cita;
import java.time.LocalDateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CitaJpaRepository extends JpaRepository<Cita, Long> {

    @Query("""
            select count(c) > 0 from Cita c
            where c.veterinario.id = :veterinarioId
              and c.estado <> com.vetsync.vetsync.citas.domain.EstadoCita.CANCELADA
              and c.inicio < :fin
              and c.fin > :inicio
            """)
    boolean existeTraslape(@Param("veterinarioId") Long veterinarioId,
                           @Param("inicio") LocalDateTime inicio,
                           @Param("fin") LocalDateTime fin);
}