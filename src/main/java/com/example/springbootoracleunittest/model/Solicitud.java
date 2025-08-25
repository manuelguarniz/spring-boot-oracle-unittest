package com.example.springbootoracleunittest.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "SOLICITUDES")
public class Solicitud {
    
    @Id
    private String numeroSolicitud;
    private String dniCliente;
    private LocalDate fechaSolicitud;
    private String asunto;
    private String estado;
    private String descripcion;
    
    // Constructores
    public Solicitud() {}
    
    public Solicitud(String numeroSolicitud, String dniCliente, LocalDate fechaSolicitud, String asunto, String estado, String descripcion) {
        this.numeroSolicitud = numeroSolicitud;
        this.dniCliente = dniCliente;
        this.fechaSolicitud = fechaSolicitud;
        this.asunto = asunto;
        this.estado = estado;
        this.descripcion = descripcion;
    }
    
    // Getters y Setters
    public String getNumeroSolicitud() {
        return numeroSolicitud;
    }
    
    public void setNumeroSolicitud(String numeroSolicitud) {
        this.numeroSolicitud = numeroSolicitud;
    }
    
    public String getDniCliente() {
        return dniCliente;
    }
    
    public void setDniCliente(String dniCliente) {
        this.dniCliente = dniCliente;
    }
    
    public LocalDate getFechaSolicitud() {
        return fechaSolicitud;
    }
    
    public void setFechaSolicitud(LocalDate fechaSolicitud) {
        this.fechaSolicitud = fechaSolicitud;
    }
    
    public String getAsunto() {
        return asunto;
    }
    
    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }
    
    public String getEstado() {
        return estado;
    }
    
    public void setEstado(String estado) {
        this.estado = estado;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
