package com.example.springbootoracleunittest.dto;

import java.time.LocalDate;
import java.util.List;

public class ClienteDetalleDTO {

  private String dni;
  private String nombre;
  private String email;
  private List<CuentaBancariaDTO> cuentaBancaria;
  private List<SolicitudPendienteDTO> solicitudesPendientes;

  // Constructores
  public ClienteDetalleDTO() {
  }

  public ClienteDetalleDTO(String dni, String nombre, String email,
      List<CuentaBancariaDTO> cuentaBancaria,
      List<SolicitudPendienteDTO> solicitudesPendientes) {
    this.dni = dni;
    this.nombre = nombre;
    this.email = email;
    this.cuentaBancaria = cuentaBancaria;
    this.solicitudesPendientes = solicitudesPendientes;
  }

  // Getters y Setters
  public String getDni() {
    return dni;
  }

  public void setDni(String dni) {
    this.dni = dni;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public List<CuentaBancariaDTO> getCuentaBancaria() {
    return cuentaBancaria;
  }

  public void setCuentaBancaria(List<CuentaBancariaDTO> cuentaBancaria) {
    this.cuentaBancaria = cuentaBancaria;
  }

  public List<SolicitudPendienteDTO> getSolicitudesPendientes() {
    return solicitudesPendientes;
  }

  public void setSolicitudesPendientes(List<SolicitudPendienteDTO> solicitudesPendientes) {
    this.solicitudesPendientes = solicitudesPendientes;
  }

  // Clases internas para representar las estructuras anidadas
  public static class CuentaBancariaDTO {
    private String numero;
    private String tipo;
    private Double saldo;

    public CuentaBancariaDTO() {
    }

    public CuentaBancariaDTO(String numero, String tipo, Double saldo) {
      this.numero = numero;
      this.tipo = tipo;
      this.saldo = saldo;
    }

    public String getNumero() {
      return numero;
    }

    public void setNumero(String numero) {
      this.numero = numero;
    }

    public String getTipo() {
      return tipo;
    }

    public void setTipo(String tipo) {
      this.tipo = tipo;
    }

    public Double getSaldo() {
      return saldo;
    }

    public void setSaldo(Double saldo) {
      this.saldo = saldo;
    }
  }

  public static class SolicitudPendienteDTO {
    private String numero;
    private LocalDate fecha;
    private String asunto;

    public SolicitudPendienteDTO() {
    }

    public SolicitudPendienteDTO(String numero, LocalDate fecha, String asunto) {
      this.numero = numero;
      this.fecha = fecha;
      this.asunto = asunto;
    }

    public String getNumero() {
      return numero;
    }

    public void setNumero(String numero) {
      this.numero = numero;
    }

    public LocalDate getFecha() {
      return fecha;
    }

    public void setFecha(LocalDate fecha) {
      this.fecha = fecha;
    }

    public String getAsunto() {
      return asunto;
    }

    public void setAsunto(String asunto) {
      this.asunto = asunto;
    }
  }
}
