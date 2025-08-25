package com.example.springbootoracleunittest.controller;

import com.example.springbootoracleunittest.dto.ClienteDetalleDTO;
import com.example.springbootoracleunittest.dto.ClienteResponseDTO;
import com.example.springbootoracleunittest.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/clientes")
@CrossOrigin(origins = "*")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    /**
     * Endpoint para consultar datos completos de un cliente por DNI usando
     * procedimiento almacenado
     * 
     * @param dni DNI del cliente
     * @return ResponseEntity con los datos del cliente, cuentas bancarias y
     *         solicitudes
     */
    @GetMapping("/{dni}")
    public ResponseEntity<ClienteDetalleDTO> consultarClientePorDNI(@PathVariable String dni) {
        try {
            ClienteDetalleDTO response = clienteService.consultarClientePorDNI(dni);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Endpoint alternativo usando query parameter
     * 
     * @param dni DNI del cliente
     * @return ResponseEntity con los datos del cliente, cuentas bancarias y
     *         solicitudes
     */
    @GetMapping("/consultar")
    public ResponseEntity<ClienteDetalleDTO> consultarCliente(@RequestParam String dni) {
        try {
            ClienteDetalleDTO response = clienteService.consultarClientePorDNI(dni);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Endpoint para consultar datos simulados (para testing)
     * 
     * @param dni DNI del cliente
     * @return ResponseEntity con los datos simulados del cliente
     */
    @GetMapping("/simulado/{dni}")
    public ResponseEntity<ClienteResponseDTO> consultarClienteSimulado(@PathVariable String dni) {
        try {
            ClienteResponseDTO response = clienteService.consultarClientePorDNISimulado(dni);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Endpoint de salud para verificar que el servicio está funcionando
     * 
     * @return ResponseEntity con mensaje de estado
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Servicio de clientes funcionando correctamente");
    }
}
