/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vetsync.vetsync.citas.infrastructure;

import com.vetsync.vetsync.citas.domain.Veterinario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VeterinarioJpaRepository extends JpaRepository<Veterinario, Long> {
}