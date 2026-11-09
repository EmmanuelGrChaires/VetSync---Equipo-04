/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vetsync.vetsync.clientes.infrastructure;

import com.vetsync.vetsync.clientes.application.RegistroClienteService;
import com.vetsync.vetsync.clientes.domain.ClienteRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfiguracionClientes {

    @Bean
    public RegistroClienteService registroClienteService(ClienteRepository repository) {
        return new RegistroClienteService(repository);
    }
}