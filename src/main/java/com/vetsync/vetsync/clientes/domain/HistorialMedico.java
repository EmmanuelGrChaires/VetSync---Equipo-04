/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vetsync.vetsync.clientes.domain;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "historial_medico")
public class HistorialMedico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "mascota_id", nullable = false)
    private Mascota mascota;

    // Solo el id de la cita: clientes NO importa clases del módulo citas
    @Column(name = "cita_id")
    private Long citaId;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String diagnostico;

    @Column(columnDefinition = "TEXT")
    private String tratamiento;

    protected HistorialMedico() {
    }

    public HistorialMedico(Mascota mascota, Long citaId, LocalDate fecha, String diagnostico, String tratamiento) {
        this.mascota = mascota;
        this.citaId = citaId;
        this.fecha = fecha;
        this.diagnostico = diagnostico;
        this.tratamiento = tratamiento;
    }

    public Long getId() { return id; }
    public Mascota getMascota() { return mascota; }
    public Long getCitaId() { return citaId; }
    public LocalDate getFecha() { return fecha; }
    public String getDiagnostico() { return diagnostico; }
    public String getTratamiento() { return tratamiento; }
}