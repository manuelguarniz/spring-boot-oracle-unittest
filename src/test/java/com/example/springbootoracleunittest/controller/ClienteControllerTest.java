package com.example.springbootoracleunittest.controller;

import com.example.springbootoracleunittest.dto.ClienteDetalleDTO;
import com.example.springbootoracleunittest.dto.ClienteResponseDTO;
import com.example.springbootoracleunittest.model.Cliente;
import com.example.springbootoracleunittest.model.CuentaBancaria;
import com.example.springbootoracleunittest.model.Solicitud;
import com.example.springbootoracleunittest.service.ClienteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClienteController.class)
class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClienteService clienteService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testConsultarClientePorDNI() throws Exception {
        // Arrange
        String dni = "12345678";

        List<ClienteDetalleDTO.CuentaBancariaDTO> cuentas = new ArrayList<>();
        cuentas.add(new ClienteDetalleDTO.CuentaBancariaDTO("1234567890", "Ahorros", 5000.0));

        List<ClienteDetalleDTO.SolicitudPendienteDTO> solicitudes = new ArrayList<>();
        solicitudes.add(new ClienteDetalleDTO.SolicitudPendienteDTO("SOL001", LocalDate.now().minusDays(5),
                "Solicitud de préstamo"));

        ClienteDetalleDTO responseDTO = new ClienteDetalleDTO(dni, "Juan Pérez", "juan.perez@email.com", cuentas,
                solicitudes);

        when(clienteService.consultarClientePorDNI(anyString())).thenReturn(responseDTO);

        // Act & Assert
        mockMvc.perform(get("/api/clientes/{dni}", dni)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dni").value(dni))
                .andExpect(jsonPath("$.nombre").value("Juan Pérez"))
                .andExpect(jsonPath("$.email").value("juan.perez@email.com"))
                .andExpect(jsonPath("$.cuentaBancaria").isArray())
                .andExpect(jsonPath("$.cuentaBancaria[0].numero").value("1234567890"))
                .andExpect(jsonPath("$.solicitudesPendientes").isArray())
                .andExpect(jsonPath("$.solicitudesPendientes[0].numero").value("SOL001"));
    }

    @Test
    void testConsultarClientePorDNINoEncontrado() throws Exception {
        // Arrange
        String dni = "99999999";
        when(clienteService.consultarClientePorDNI(anyString()))
                .thenThrow(new RuntimeException("Cliente no encontrado"));

        // Act & Assert
        mockMvc.perform(get("/api/clientes/{dni}", dni)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testHealthEndpoint() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/clientes/health")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Servicio de clientes funcionando correctamente"));
    }
}
