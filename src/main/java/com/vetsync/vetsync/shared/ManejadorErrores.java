/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vetsync.vetsync.shared;

import com.vetsync.vetsync.citas.domain.CitaInvalidaException;
import com.vetsync.vetsync.citas.domain.VeterinarioOcupadoException;
import com.vetsync.vetsync.clientes.domain.ClienteInvalidoException;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ManejadorErrores {

    @ExceptionHandler({CitaInvalidaException.class, ClienteInvalidoException.class})
    public ResponseEntity<Map<String, String>> solicitudInvalida(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
    }

    @ExceptionHandler(VeterinarioOcupadoException.class)
    public ResponseEntity<Map<String, String>> conflicto(VeterinarioOcupadoException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", e.getMessage()));
    }
}