/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vetsync.vetsync.clientes.api;

import com.vetsync.vetsync.clientes.application.RegistroClienteService;
import com.vetsync.vetsync.clientes.domain.Dueno;
import com.vetsync.vetsync.clientes.domain.HistorialMedico;
import com.vetsync.vetsync.clientes.domain.Mascota;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ClienteController {

    public record DuenoRequest(@NotBlank String nombre, @NotBlank String email, String telefono) {}
    public record DuenoResponse(Long id, String nombre, String email, String telefono) {}

    public record MascotaRequest(@NotBlank String nombre, @NotBlank String especie,
                                 String raza, LocalDate fechaNacimiento) {}
    public record MascotaResponse(Long id, Long duenoId, String nombre, String especie) {}

    public record HistorialRequest(Long citaId, @NotNull LocalDate fecha,
                                   @NotBlank String diagnostico, String tratamiento) {}
    public record HistorialResponse(Long id, Long mascotaId, LocalDate fecha, String diagnostico) {}

    private final RegistroClienteService service;

    public ClienteController(RegistroClienteService service) {
        this.service = service;
    }

    @PostMapping("/duenos")
    @ResponseStatus(HttpStatus.CREATED)
    public DuenoResponse registrarDueno(@Valid @RequestBody DuenoRequest req) {
        Dueno d = service.registrarDueno(req.nombre(), req.email(), req.telefono());
        return new DuenoResponse(d.getId(), d.getNombre(), d.getEmail(), d.getTelefono());
    }

    @PostMapping("/duenos/{duenoId}/mascotas")
    @ResponseStatus(HttpStatus.CREATED)
    public MascotaResponse registrarMascota(@PathVariable Long duenoId,
                                            @Valid @RequestBody MascotaRequest req) {
        Mascota m = service.registrarMascota(duenoId, req.nombre(), req.especie(),
                req.raza(), req.fechaNacimiento());
        return new MascotaResponse(m.getId(), m.getDueno().getId(), m.getNombre(), m.getEspecie());
    }

    @PostMapping("/mascotas/{mascotaId}/historial")
    @ResponseStatus(HttpStatus.CREATED)
    public HistorialResponse agregarHistorial(@PathVariable Long mascotaId,
                                              @Valid @RequestBody HistorialRequest req) {
        HistorialMedico h = service.agregarHistorial(mascotaId, req.citaId(), req.fecha(),
                req.diagnostico(), req.tratamiento());
        return new HistorialResponse(h.getId(), h.getMascota().getId(), h.getFecha(), h.getDiagnostico());
    }
}