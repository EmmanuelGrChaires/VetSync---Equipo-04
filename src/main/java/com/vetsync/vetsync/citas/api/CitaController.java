/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vetsync.vetsync.citas.api;

import com.vetsync.vetsync.citas.application.AgendarCitaService;
import com.vetsync.vetsync.citas.domain.Cita;
import com.vetsync.vetsync.citas.domain.CitaInvalidaException;
import com.vetsync.vetsync.citas.domain.Veterinario;
import com.vetsync.vetsync.citas.infrastructure.VeterinarioJpaRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/citas")
public class CitaController {

    public record CitaRequest(@NotNull Long mascotaId, @NotNull Long veterinarioId,
                              @NotNull LocalDateTime inicio, @NotNull LocalDateTime fin,
                              String motivo) {}
    public record CitaResponse(Long id, Long mascotaId, Long veterinarioId,
                               LocalDateTime inicio, LocalDateTime fin, String estado) {}

    private final AgendarCitaService service;
    private final VeterinarioJpaRepository veterinarios;

    public CitaController(AgendarCitaService service, VeterinarioJpaRepository veterinarios) {
        this.service = service;
        this.veterinarios = veterinarios;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CitaResponse agendar(@Valid @RequestBody CitaRequest req) {
        Veterinario vet = veterinarios.findById(req.veterinarioId())
                .orElseThrow(() -> new CitaInvalidaException("El veterinario no existe"));
        Cita c = service.agendarCita(req.mascotaId(), vet, req.inicio(), req.fin(), req.motivo());
        return new CitaResponse(c.getId(), c.getMascotaId(), c.getVeterinario().getId(),
                c.getInicio(), c.getFin(), c.getEstado().name());
    }
}