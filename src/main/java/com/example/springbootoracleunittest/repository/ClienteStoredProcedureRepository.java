package com.example.springbootoracleunittest.repository;

import com.example.springbootoracleunittest.dto.ClienteDetalleDTO;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ClienteStoredProcedureRepository {

  @PersistenceContext
  private EntityManager entityManager;

  /**
   * Consulta datos del cliente por DNI usando procedimiento almacenado
   * con StoredProcedureQuery y EntityManager
   * 
   * @param dni DNI del cliente
   * @return ClienteDetalleDTO con datos del cliente, cuentas bancarias y
   *         solicitudes
   */
  public ClienteDetalleDTO consultarClientePorDNI(String dni) {
    try {
      // Crear la consulta del procedimiento almacenado usando StoredProcedureQuery
      StoredProcedureQuery query = entityManager
          .createStoredProcedureQuery("CONSULTAR_CLIENTE_POR_DNI")
          .registerStoredProcedureParameter("p_dni", String.class, jakarta.persistence.ParameterMode.IN)
          .registerStoredProcedureParameter("rs1", Class.class, jakarta.persistence.ParameterMode.REF_CURSOR)
          .registerStoredProcedureParameter("rs2", Class.class, jakarta.persistence.ParameterMode.REF_CURSOR)
          .registerStoredProcedureParameter("rs3", Class.class, jakarta.persistence.ParameterMode.REF_CURSOR)
          .setParameter("p_dni", dni);

      // Ejecutar el procedimiento almacenado una sola vez
      query.execute();

      // Obtener cada cursor usando getOutputParameterValue
      ResultSet clienteResult = (ResultSet) query.getOutputParameterValue("rs1");
      ResultSet cuentasResult = (ResultSet) query.getOutputParameterValue("rs2");
      ResultSet solicitudesResult = (ResultSet) query.getOutputParameterValue("rs3");

      // Procesar los resultados
      ClienteDetalleDTO cliente = procesarResultadoCliente(clienteResult);
      List<ClienteDetalleDTO.CuentaBancariaDTO> cuentas = procesarResultadoCuentas(cuentasResult);
      List<ClienteDetalleDTO.SolicitudPendienteDTO> solicitudes = procesarResultadoSolicitudes(solicitudesResult);

      // Asignar las cuentas y solicitudes al cliente
      cliente.setCuentaBancaria(cuentas);
      cliente.setSolicitudesPendientes(solicitudes);

      return cliente;

    } catch (SQLException e) {
      throw new RuntimeException("Error al procesar resultados del procedimiento almacenado: " + e.getMessage(), e);
    } catch (Exception e) {
      throw new RuntimeException("Error al consultar cliente por DNI: " + e.getMessage(), e);
    }
  }

  /**
   * Procesa el resultado del cursor RS1 (datos del cliente)
   */
  private ClienteDetalleDTO procesarResultadoCliente(ResultSet rs) throws SQLException {
    if (rs == null || !rs.next()) {
      throw new RuntimeException("No se encontró el cliente");
    }

    // Asumiendo que el orden de las columnas es: dni, nombre, apellido, email,
    // telefono, direccion
    String dni = rs.getString("dni");
    String nombre = rs.getString("nombre") + " " + rs.getString("apellido");
    String email = rs.getString("email");

    return new ClienteDetalleDTO(dni, nombre, email, new ArrayList<>(), new ArrayList<>());
  }

  /**
   * Procesa el resultado del cursor RS2 (cuentas bancarias)
   */
  private List<ClienteDetalleDTO.CuentaBancariaDTO> procesarResultadoCuentas(ResultSet rs) throws SQLException {
    List<ClienteDetalleDTO.CuentaBancariaDTO> cuentas = new ArrayList<>();

    if (rs != null) {
      while (rs.next()) {
        // Asumiendo que el orden de las columnas es: numero_cuenta, nombre_cliente,
        // tipo_cuenta, saldo
        String numero = rs.getString("numero_cuenta");
        String tipo = rs.getString("tipo_cuenta");
        Double saldo = rs.getDouble("saldo");

        cuentas.add(new ClienteDetalleDTO.CuentaBancariaDTO(numero, tipo, saldo));
      }
    }

    return cuentas;
  }

  /**
   * Procesa el resultado del cursor RS3 (solicitudes pendientes)
   */
  private List<ClienteDetalleDTO.SolicitudPendienteDTO> procesarResultadoSolicitudes(ResultSet rs) throws SQLException {
    List<ClienteDetalleDTO.SolicitudPendienteDTO> solicitudes = new ArrayList<>();

    if (rs != null) {
      while (rs.next()) {
        // Asumiendo que el orden de las columnas es: numero_solicitud, fecha_solicitud,
        // asunto, estado
        String numero = rs.getString("numero_solicitud");
        LocalDate fecha = rs.getDate("fecha_solicitud").toLocalDate();
        String asunto = rs.getString("asunto");

        solicitudes.add(new ClienteDetalleDTO.SolicitudPendienteDTO(numero, fecha, asunto));
      }
    }

    return solicitudes;
  }
}
