/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vetsync.vetsync.clientes.infrastructure;

import com.vetsync.vetsync.clientes.domain.Dueno;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DuenoJpaRepository extends JpaRepository<Dueno, Long> {

    boolean existsByEmail(String email);
}