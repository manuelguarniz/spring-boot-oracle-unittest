package com.example.springbootoracleunittest.dto;

import com.example.springbootoracleunittest.model.Cliente;
import com.example.springbootoracleunittest.model.CuentaBancaria;
import com.example.springbootoracleunittest.model.Solicitud;
import java.util.List;

public class ClienteResponseDTO {
    
    private Cliente cliente;
    private List<CuentaBancaria> cuentasBancarias;
    private List<Solicitud> solicitudesPendientes;
    
    // Constructores
    public ClienteResponseDTO() {}
    
    public ClienteResponseDTO(Cliente cliente, List<CuentaBancaria> cuentasBancarias, List<Solicitud> solicitudesPendientes) {
        this.cliente = cliente;
        this.cuentasBancarias = cuentasBancarias;
        this.solicitudesPendientes = solicitudesPendientes;
    }
    
    // Getters y Setters
    public Cliente getCliente() {
        return cliente;
    }
    
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
    
    public List<CuentaBancaria> getCuentasBancarias() {
        return cuentasBancarias;
    }
    
    public void setCuentasBancarias(List<CuentaBancaria> cuentasBancarias) {
        this.cuentasBancarias = cuentasBancarias;
    }
    
    public List<Solicitud> getSolicitudesPendientes() {
        return solicitudesPendientes;
    }
    
    public void setSolicitudesPendientes(List<Solicitud> solicitudesPendientes) {
        this.solicitudesPendientes = solicitudesPendientes;
    }
}
