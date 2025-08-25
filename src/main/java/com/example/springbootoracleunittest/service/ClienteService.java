package com.example.springbootoracleunittest.service;

import com.example.springbootoracleunittest.dto.ClienteDetalleDTO;
import com.example.springbootoracleunittest.dto.ClienteResponseDTO;
import com.example.springbootoracleunittest.model.Cliente;
import com.example.springbootoracleunittest.model.CuentaBancaria;
import com.example.springbootoracleunittest.model.Solicitud;
import com.example.springbootoracleunittest.repository.ClienteRepository;
import com.example.springbootoracleunittest.repository.ClienteStoredProcedureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ClienteStoredProcedureRepository clienteStoredProcedureRepository;

    /**
     * Consulta los datos completos de un cliente por DNI usando el procedimiento
     * almacenado
     * 
     * @param dni DNI del cliente
     * @return ClienteDetalleDTO con datos del cliente, cuentas bancarias y
     *         solicitudes
     */
    public ClienteDetalleDTO consultarClientePorDNI(String dni) {
        return clienteStoredProcedureRepository.consultarClientePorDNI(dni);
    }

    /**
     * Método alternativo que simula la consulta (para testing sin base de datos)
     * 
     * @param dni DNI del cliente
     * @return ClienteResponseDTO con datos simulados
     */
    public ClienteResponseDTO consultarClientePorDNISimulado(String dni) {
        // En un entorno real, esto llamaría al procedimiento almacenado
        // Por ahora, simulamos la respuesta con datos de prueba

        // Buscar cliente por DNI
        Optional<Cliente> clienteOpt = clienteRepository.findById(dni);

        if (clienteOpt.isEmpty()) {
            throw new RuntimeException("Cliente no encontrado con DNI: " + dni);
        }

        Cliente cliente = clienteOpt.get();

        // Simular datos de cuentas bancarias (en un entorno real vendrían del
        // procedimiento)
        List<CuentaBancaria> cuentasBancarias = simularCuentasBancarias(dni);

        // Simular datos de solicitudes pendientes (en un entorno real vendrían del
        // procedimiento)
        List<Solicitud> solicitudesPendientes = simularSolicitudesPendientes(dni);

        return new ClienteResponseDTO(cliente, cuentasBancarias, solicitudesPendientes);
    }

    // Métodos auxiliares para simular datos
    private List<CuentaBancaria> simularCuentasBancarias(String dni) {
        List<CuentaBancaria> cuentas = new ArrayList<>();

        // Simular algunas cuentas bancarias
        cuentas.add(new CuentaBancaria("1234567890", "Juan Pérez", dni, "Ahorros", 5000.0));
        cuentas.add(new CuentaBancaria("0987654321", "Juan Pérez", dni, "Corriente", 15000.0));

        return cuentas;
    }

    private List<Solicitud> simularSolicitudesPendientes(String dni) {
        List<Solicitud> solicitudes = new ArrayList<>();

        // Simular algunas solicitudes pendientes
        solicitudes.add(new Solicitud("SOL001", dni, java.time.LocalDate.now().minusDays(5), "Solicitud de préstamo",
                "Pendiente", "Solicitud de préstamo personal"));
        solicitudes.add(new Solicitud("SOL002", dni, java.time.LocalDate.now().minusDays(2), "Cambio de dirección",
                "En revisión", "Actualización de datos personales"));

        return solicitudes;
    }

}
