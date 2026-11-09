/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vetsync.vetsync.citas.infrastructure;

import com.vetsync.vetsync.citas.domain.MascotaConsulta;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Component;

@Component
public class MascotaConsultaJpa implements MascotaConsulta {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public boolean existe(Long mascotaId) {
        Number total = (Number) entityManager
                .createNativeQuery("select count(*) from mascotas where id = :id")
                .setParameter("id", mascotaId)
                .getSingleResult();
        return total.longValue() > 0;
    }
}