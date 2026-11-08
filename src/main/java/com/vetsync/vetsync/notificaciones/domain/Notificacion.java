/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vetsync.vetsync.notificaciones.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notificaciones")
public class Notificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cita_id", nullable = false)
    private Long citaId;

    @Column(nullable = false, length = 120)
    private String destinatario;

    @Column(nullable = false, length = 150)
    private String asunto;

    @Column(name = "enviada_en", nullable = false)
    private LocalDateTime enviadaEn;

    protected Notificacion() {
    }

    public Notificacion(Long citaId, String destinatario, String asunto, LocalDateTime enviadaEn) {
        this.citaId = citaId;
        this.destinatario = destinatario;
        this.asunto = asunto;
        this.enviadaEn = enviadaEn;
    }

    public Long getId() { return id; }
    public Long getCitaId() { return citaId; }
    public String getDestinatario() { return destinatario; }
    public String getAsunto() { return asunto; }
    public LocalDateTime getEnviadaEn() { return enviadaEn; }
}