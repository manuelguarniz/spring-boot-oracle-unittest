package com.example.springbootoracleunittest.repository;

import com.example.springbootoracleunittest.dto.ClienteDetalleDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.StoredProcedureQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("ClienteStoredProcedureRepository Tests")
class ClienteStoredProcedureRepositoryTest {

  @Mock
  private EntityManager entityManager;

  @Mock
  private StoredProcedureQuery storedProcedureQuery;

  @Mock
  private ResultSet clienteResultSet;

  @Mock
  private ResultSet cuentasResultSet;

  @Mock
  private ResultSet solicitudesResultSet;

  @InjectMocks
  private ClienteStoredProcedureRepository repository;

  private static final String DNI_TEST = "12345678";

  @BeforeEach
  void setUp() {
    // Configuración común para todos los tests
    given(entityManager.createStoredProcedureQuery(anyString())).willReturn(storedProcedureQuery);
  }

  @Test
  @DisplayName("Debería consultar cliente por DNI exitosamente")
  void deberiaConsultarClientePorDNIExitosamente() throws SQLException {
    // Arrange
    given(storedProcedureQuery.registerStoredProcedureParameter(anyString(), any(), any()))
        .willReturn(storedProcedureQuery);
    given(storedProcedureQuery.setParameter(anyString(), any())).willReturn(storedProcedureQuery);
    configurarResultSetCliente();
    configurarResultSetCuentas();
    configurarResultSetSolicitudes();

    // Act
    ClienteDetalleDTO resultado = repository.consultarClientePorDNI(DNI_TEST);

    // Assert
    assertNotNull(resultado);
    assertEquals(DNI_TEST, resultado.getDni());
    assertEquals("Juan Pérez", resultado.getNombre());
    assertEquals("juan.perez@email.com", resultado.getEmail());
    assertEquals(2, resultado.getCuentaBancaria().size());
    assertEquals(2, resultado.getSolicitudesPendientes().size());

    // Verificar que se llamó al procedimiento almacenado correctamente
    verify(entityManager).createStoredProcedureQuery("CONSULTAR_CLIENTE_POR_DNI");
    verify(storedProcedureQuery).registerStoredProcedureParameter("p_dni", String.class,
        jakarta.persistence.ParameterMode.IN);
    verify(storedProcedureQuery).registerStoredProcedureParameter("rs1", Class.class,
        jakarta.persistence.ParameterMode.REF_CURSOR);
    verify(storedProcedureQuery).registerStoredProcedureParameter("rs2", Class.class,
        jakarta.persistence.ParameterMode.REF_CURSOR);
    verify(storedProcedureQuery).registerStoredProcedureParameter("rs3", Class.class,
        jakarta.persistence.ParameterMode.REF_CURSOR);
    verify(storedProcedureQuery).setParameter("p_dni", DNI_TEST);
    verify(storedProcedureQuery).execute();
    verify(storedProcedureQuery).getOutputParameterValue("rs1");
    verify(storedProcedureQuery).getOutputParameterValue("rs2");
    verify(storedProcedureQuery).getOutputParameterValue("rs3");
  }

  @Test
  @DisplayName("Debería lanzar excepción cuando no se encuentra el cliente")
  void deberiaLanzarExcepcionCuandoNoSeEncuentraCliente() throws SQLException {
    // Arrange
    given(storedProcedureQuery.registerStoredProcedureParameter(anyString(), any(), any()))
        .willReturn(storedProcedureQuery);
    given(storedProcedureQuery.setParameter(anyString(), any())).willReturn(storedProcedureQuery);
    given(storedProcedureQuery.getOutputParameterValue("rs1")).willReturn(clienteResultSet);
    given(clienteResultSet.next()).willReturn(false); // No hay datos del cliente

    // Act & Assert
    RuntimeException exception = assertThrows(RuntimeException.class, () -> {
      repository.consultarClientePorDNI(DNI_TEST);
    });

    assertTrue(exception.getMessage().contains("No se encontró el cliente"));
  }

  @Test
  @DisplayName("Debería manejar cliente sin cuentas bancarias")
  void deberiaManejarClienteSinCuentasBancarias() throws SQLException {
    // Arrange
    given(storedProcedureQuery.registerStoredProcedureParameter(anyString(), any(), any()))
        .willReturn(storedProcedureQuery);
    given(storedProcedureQuery.setParameter(anyString(), any())).willReturn(storedProcedureQuery);
    configurarResultSetCliente();
    given(storedProcedureQuery.getOutputParameterValue("rs2")).willReturn(cuentasResultSet);
    given(cuentasResultSet.next()).willReturn(false); // No hay cuentas bancarias
    configurarResultSetSolicitudes();

    // Act
    ClienteDetalleDTO resultado = repository.consultarClientePorDNI(DNI_TEST);

    // Assert
    assertNotNull(resultado);
    assertEquals(DNI_TEST, resultado.getDni());
    assertTrue(resultado.getCuentaBancaria().isEmpty());
    assertEquals(2, resultado.getSolicitudesPendientes().size());
  }

  @Test
  @DisplayName("Debería manejar cliente sin solicitudes pendientes")
  void deberiaManejarClienteSinSolicitudesPendientes() throws SQLException {
    // Arrange
    given(storedProcedureQuery.registerStoredProcedureParameter(anyString(), any(), any()))
        .willReturn(storedProcedureQuery);
    given(storedProcedureQuery.setParameter(anyString(), any())).willReturn(storedProcedureQuery);
    configurarResultSetCliente();
    configurarResultSetCuentas();
    given(storedProcedureQuery.getOutputParameterValue("rs3")).willReturn(solicitudesResultSet);
    given(solicitudesResultSet.next()).willReturn(false); // No hay solicitudes

    // Act
    ClienteDetalleDTO resultado = repository.consultarClientePorDNI(DNI_TEST);

    // Assert
    assertNotNull(resultado);
    assertEquals(DNI_TEST, resultado.getDni());
    assertEquals(2, resultado.getCuentaBancaria().size());
    assertTrue(resultado.getSolicitudesPendientes().isEmpty());
  }

  @Test
  @DisplayName("Debería manejar SQLException correctamente")
  void deberiaManejarSQLExceptionCorrectamente() throws SQLException {
    // Arrange
    given(storedProcedureQuery.registerStoredProcedureParameter(anyString(), any(), any()))
        .willReturn(storedProcedureQuery);
    given(storedProcedureQuery.setParameter(anyString(), any())).willReturn(storedProcedureQuery);
    given(storedProcedureQuery.getOutputParameterValue("rs1")).willReturn(clienteResultSet);
    given(clienteResultSet.next()).willThrow(new SQLException("Error de base de datos"));

    // Act & Assert
    RuntimeException exception = assertThrows(RuntimeException.class, () -> {
      repository.consultarClientePorDNI(DNI_TEST);
    });

    assertTrue(exception.getMessage().contains("Error al procesar resultados del procedimiento almacenado"));
    assertTrue(exception.getCause() instanceof SQLException);
  }

  @Test
  @DisplayName("Debería manejar excepción general correctamente")
  void deberiaManejarExcepcionGeneralCorrectamente() {
    // Arrange
    given(entityManager.createStoredProcedureQuery(anyString())).willThrow(new RuntimeException("Error general"));

    // Act & Assert
    RuntimeException exception = assertThrows(RuntimeException.class, () -> {
      repository.consultarClientePorDNI(DNI_TEST);
    });

    assertTrue(exception.getMessage().contains("Error al consultar cliente por DNI"));
    assertTrue(exception.getCause() instanceof RuntimeException);
  }

  @Test
  @DisplayName("Debería procesar múltiples cuentas bancarias correctamente")
  void deberiaProcesarMultiplesCuentasBancariasCorrectamente() throws SQLException {
    // Arrange
    given(storedProcedureQuery.registerStoredProcedureParameter(anyString(), any(), any()))
        .willReturn(storedProcedureQuery);
    given(storedProcedureQuery.setParameter(anyString(), any())).willReturn(storedProcedureQuery);
    configurarResultSetCliente();
    configurarResultSetCuentasMultiples();
    configurarResultSetSolicitudes();

    // Act
    ClienteDetalleDTO resultado = repository.consultarClientePorDNI(DNI_TEST);

    // Assert
    assertNotNull(resultado);
    assertEquals(3, resultado.getCuentaBancaria().size());

    // Verificar primera cuenta
    ClienteDetalleDTO.CuentaBancariaDTO primeraCuenta = resultado.getCuentaBancaria().get(0);
    assertEquals("1234567890", primeraCuenta.getNumero());
    assertEquals("Ahorros", primeraCuenta.getTipo());
    assertEquals(5000.0, primeraCuenta.getSaldo());

    // Verificar segunda cuenta
    ClienteDetalleDTO.CuentaBancariaDTO segundaCuenta = resultado.getCuentaBancaria().get(1);
    assertEquals("0987654321", segundaCuenta.getNumero());
    assertEquals("Corriente", segundaCuenta.getTipo());
    assertEquals(15000.0, segundaCuenta.getSaldo());

    // Verificar tercera cuenta
    ClienteDetalleDTO.CuentaBancariaDTO terceraCuenta = resultado.getCuentaBancaria().get(2);
    assertEquals("1122334455", terceraCuenta.getNumero());
    assertEquals("Plazo fijo", terceraCuenta.getTipo());
    assertEquals(25000.0, terceraCuenta.getSaldo());
  }

  @Test
  @DisplayName("Debería procesar múltiples solicitudes pendientes correctamente")
  void deberiaProcesarMultiplesSolicitudesPendientesCorrectamente() throws SQLException {
    // Arrange
    given(storedProcedureQuery.registerStoredProcedureParameter(anyString(), any(), any()))
        .willReturn(storedProcedureQuery);
    given(storedProcedureQuery.setParameter(anyString(), any())).willReturn(storedProcedureQuery);
    configurarResultSetCliente();
    configurarResultSetCuentas();
    configurarResultSetSolicitudesMultiples();

    // Act
    ClienteDetalleDTO resultado = repository.consultarClientePorDNI(DNI_TEST);

    // Assert
    assertNotNull(resultado);
    assertEquals(3, resultado.getSolicitudesPendientes().size());

    // Verificar primera solicitud
    ClienteDetalleDTO.SolicitudPendienteDTO primeraSolicitud = resultado.getSolicitudesPendientes().get(0);
    assertEquals("SOL001", primeraSolicitud.getNumero());
    assertEquals(LocalDate.now().minusDays(5), primeraSolicitud.getFecha());
    assertEquals("Solicitud de préstamo", primeraSolicitud.getAsunto());

    // Verificar segunda solicitud
    ClienteDetalleDTO.SolicitudPendienteDTO segundaSolicitud = resultado.getSolicitudesPendientes().get(1);
    assertEquals("SOL002", segundaSolicitud.getNumero());
    assertEquals(LocalDate.now().minusDays(2), segundaSolicitud.getFecha());
    assertEquals("Cambio de dirección", segundaSolicitud.getAsunto());

    // Verificar tercera solicitud
    ClienteDetalleDTO.SolicitudPendienteDTO terceraSolicitud = resultado.getSolicitudesPendientes().get(2);
    assertEquals("SOL003", terceraSolicitud.getNumero());
    assertEquals(LocalDate.now().minusDays(1), terceraSolicitud.getFecha());
    assertEquals("Actualización de datos", terceraSolicitud.getAsunto());
  }

  // Métodos auxiliares para configurar los mocks
  private void configurarResultSetCliente() throws SQLException {
    given(storedProcedureQuery.getOutputParameterValue("rs1")).willReturn(clienteResultSet);
    given(clienteResultSet.next()).willReturn(true);
    given(clienteResultSet.getString("dni")).willReturn(DNI_TEST);
    given(clienteResultSet.getString("nombre")).willReturn("Juan");
    given(clienteResultSet.getString("apellido")).willReturn("Pérez");
    given(clienteResultSet.getString("email")).willReturn("juan.perez@email.com");
  }

  private void configurarResultSetCuentas() throws SQLException {
    given(storedProcedureQuery.getOutputParameterValue("rs2")).willReturn(cuentasResultSet);
    given(cuentasResultSet.next()).willReturn(true, true, false); // Dos cuentas
    given(cuentasResultSet.getString("numero_cuenta")).willReturn("1234567890", "0987654321");
    given(cuentasResultSet.getString("tipo_cuenta")).willReturn("Ahorros", "Corriente");
    given(cuentasResultSet.getDouble("saldo")).willReturn(5000.0, 15000.0);
  }

  private void configurarResultSetCuentasMultiples() throws SQLException {
    given(storedProcedureQuery.getOutputParameterValue("rs2")).willReturn(cuentasResultSet);
    given(cuentasResultSet.next()).willReturn(true, true, true, false); // Tres cuentas
    given(cuentasResultSet.getString("numero_cuenta")).willReturn("1234567890", "0987654321", "1122334455");
    given(cuentasResultSet.getString("tipo_cuenta")).willReturn("Ahorros", "Corriente", "Plazo fijo");
    given(cuentasResultSet.getDouble("saldo")).willReturn(5000.0, 15000.0, 25000.0);
  }

  private void configurarResultSetSolicitudes() throws SQLException {
    given(storedProcedureQuery.getOutputParameterValue("rs3")).willReturn(solicitudesResultSet);
    given(solicitudesResultSet.next()).willReturn(true, true, false); // Dos solicitudes
    given(solicitudesResultSet.getString("numero_solicitud")).willReturn("SOL001", "SOL002");
    given(solicitudesResultSet.getDate("fecha_solicitud")).willReturn(
        Date.valueOf(LocalDate.now().minusDays(5)),
        Date.valueOf(LocalDate.now().minusDays(2)));
    given(solicitudesResultSet.getString("asunto")).willReturn("Solicitud de préstamo", "Cambio de dirección");
  }

  private void configurarResultSetSolicitudesMultiples() throws SQLException {
    given(storedProcedureQuery.getOutputParameterValue("rs3")).willReturn(solicitudesResultSet);
    given(solicitudesResultSet.next()).willReturn(true, true, true, false); // Tres solicitudes
    given(solicitudesResultSet.getString("numero_solicitud")).willReturn("SOL001", "SOL002", "SOL003");
    given(solicitudesResultSet.getDate("fecha_solicitud")).willReturn(
        Date.valueOf(LocalDate.now().minusDays(5)),
        Date.valueOf(LocalDate.now().minusDays(2)),
        Date.valueOf(LocalDate.now().minusDays(1)));
    given(solicitudesResultSet.getString("asunto")).willReturn("Solicitud de préstamo", "Cambio de dirección",
        "Actualización de datos");
  }
}
