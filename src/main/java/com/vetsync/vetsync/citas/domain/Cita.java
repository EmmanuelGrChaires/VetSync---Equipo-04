/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vetsync.vetsync.citas.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "citas")
public class Cita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Solo el id de la mascota: citas NO importa clases del módulo clientes
    @Column(name = "mascota_id", nullable = false)
    private Long mascotaId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "veterinario_id", nullable = false)
    private Veterinario veterinario;

    @Column(nullable = false)
    private LocalDateTime inicio;

    @Column(nullable = false)
    private LocalDateTime fin;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoCita estado = EstadoCita.PROGRAMADA;

    @Column(length = 255)
    private String motivo;

    protected Cita() {
    }

    public Cita(Long mascotaId, Veterinario veterinario, LocalDateTime inicio, LocalDateTime fin, String motivo) {
        this.mascotaId = mascotaId;
        this.veterinario = veterinario;
        this.inicio = inicio;
        this.fin = fin;
        this.motivo = motivo;
    }

    public Long getId() { return id; }
    public Long getMascotaId() { return mascotaId; }
    public Veterinario getVeterinario() { return veterinario; }
    public LocalDateTime getInicio() { return inicio; }
    public LocalDateTime getFin() { return fin; }
    public EstadoCita getEstado() { return estado; }
    public String getMotivo() { return motivo; }
}