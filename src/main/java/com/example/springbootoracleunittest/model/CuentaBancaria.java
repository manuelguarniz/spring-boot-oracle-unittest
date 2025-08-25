package com.example.springbootoracleunittest.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "CUENTAS_BANCARIAS")
public class CuentaBancaria {
    
    @Id
    private String numeroCuenta;
    private String nombreCliente;
    private String dniCliente;
    private String tipoCuenta;
    private Double saldo;
    
    // Constructores
    public CuentaBancaria() {}
    
    public CuentaBancaria(String numeroCuenta, String nombreCliente, String dniCliente, String tipoCuenta, Double saldo) {
        this.numeroCuenta = numeroCuenta;
        this.nombreCliente = nombreCliente;
        this.dniCliente = dniCliente;
        this.tipoCuenta = tipoCuenta;
        this.saldo = saldo;
    }
    
    // Getters y Setters
    public String getNumeroCuenta() {
        return numeroCuenta;
    }
    
    public void setNumeroCuenta(String numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }
    
    public String getNombreCliente() {
        return nombreCliente;
    }
    
    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }
    
    public String getDniCliente() {
        return dniCliente;
    }
    
    public void setDniCliente(String dniCliente) {
        this.dniCliente = dniCliente;
    }
    
    public String getTipoCuenta() {
        return tipoCuenta;
    }
    
    public void setTipoCuenta(String tipoCuenta) {
        this.tipoCuenta = tipoCuenta;
    }
    
    public Double getSaldo() {
        return saldo;
    }
    
    public void setSaldo(Double saldo) {
        this.saldo = saldo;
    }
}
