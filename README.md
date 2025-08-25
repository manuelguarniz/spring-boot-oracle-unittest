# Spring Boot Oracle Unit Test Project

Este es un proyecto base de Spring Boot 3 con Java 17 configurado para trabajar con Oracle Database e incluye un endpoint para consultar datos de clientes a través de procedimientos almacenados.

## Tecnologías utilizadas

- Spring Boot 3.2.0
- Java 17
- Spring Data JPA
- Oracle JDBC Driver (ojdbc11)
- Maven

## Configuración

El proyecto incluye las siguientes dependencias principales:

- `spring-boot-starter-web`: Para crear aplicaciones web REST
- `spring-boot-starter-data-jpa`: Para el acceso a datos con JPA
- `ojdbc11`: Driver de Oracle Database
- `spring-boot-starter-test`: Para testing

## Configuración de la base de datos

La configuración de Oracle se encuentra en `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:oracle:thin:@localhost:1521:XE
spring.datasource.username=system
spring.datasource.password=password
```

## Endpoints disponibles

### Consultar cliente por DNI (Procedimiento almacenado)

**GET** `/api/clientes/{dni}`

Consulta los datos completos de un cliente por DNI usando un procedimiento almacenado, incluyendo:
- Datos del cliente
- Cuentas bancarias asociadas
- Solicitudes pendientes

**Ejemplo de uso:**
```bash
curl -X GET "http://localhost:8080/api/clientes/12345678"
```

**Respuesta:**
```json
{
  "dni": "12345678",
  "nombre": "Juan Pérez",
  "email": "juan.perez@email.com",
  "cuentaBancaria": [
    {
      "numero": "1234567890",
      "tipo": "Ahorros",
      "saldo": 5000.0
    }
  ],
  "solicitudesPendientes": [
    {
      "numero": "SOL001",
      "fecha": "2024-01-20",
      "asunto": "Solicitud de préstamo"
    }
  ]
}
```

### Consultar cliente simulado (Para testing)

**GET** `/api/clientes/simulado/{dni}`

Endpoint para testing que simula la respuesta sin necesidad de base de datos Oracle.

### Endpoint alternativo

**GET** `/api/clientes/consultar?dni={dni}`

### Health check

**GET** `/api/clientes/health`

## Implementación del procedimiento almacenado

El proyecto implementa la lectura de un procedimiento almacenado de Oracle usando `EntityManager` y `ResultSet`:

### Estructura del procedimiento almacenado:
- **Parámetro de entrada:** DNI (VARCHAR2)
- **Cursores de salida:**
  - RS1: Datos del cliente (un solo registro)
  - RS2: Cuentas bancarias asociadas al cliente
  - RS3: Solicitudes pendientes

### Implementación técnica:
- **ClienteStoredProcedureRepository:** Maneja la llamada al procedimiento almacenado
- **EntityManager:** Se usa para crear y ejecutar StoredProcedureQuery
- **StoredProcedureQuery:** Ejecuta el procedimiento almacenado usando JPA
- **ParameterMode.REF_CURSOR:** Maneja los cursores de salida de Oracle
- **ClienteDetalleDTO:** Estructura de respuesta anidada

### Ventajas de usar StoredProcedureQuery:
- **Gestión automática de conexiones:** Spring Boot y JPA manejan automáticamente el pool de conexiones
- **Transaccionalidad:** Se integra automáticamente con el contexto transaccional de Spring
- **Tipado seguro:** Los parámetros se registran con tipos específicos
- **Manejo de errores:** Integración con el sistema de excepciones de JPA
- **Portabilidad:** Funciona con diferentes proveedores de JPA
- **Optimización:** Una sola ejecución del procedimiento almacenado para obtener todos los cursores
- **Acceso eficiente:** Uso de `getOutputParameterValue()` para acceder a cada cursor individualmente

### Optimización implementada:
- **Una sola ejecución:** El procedimiento almacenado se ejecuta una única vez
- **Parámetros de salida:** Uso de `Class.class` para los parámetros REF_CURSOR
- **Acceso a cursores:** `getOutputParameterValue("rs1")`, `getOutputParameterValue("rs2")`, `getOutputParameterValue("rs3")`
- **Reducción de overhead:** Eliminación de múltiples llamadas a la base de datos

### Testing:
- **Tests unitarios completos:** `ClienteStoredProcedureRepositoryTest` con 8 escenarios de prueba
- **Cobertura de casos:** Cliente encontrado, no encontrado, sin cuentas, sin solicitudes, excepciones
- **BDD Mockito:** Uso de `given()` en lugar de `when()` para mejor legibilidad
- **Mocks optimizados:** Uso de Mockito para simular EntityManager y ResultSet
- **Verificación de comportamiento:** Validación de llamadas a métodos y procesamiento de datos

#### Escenarios de prueba implementados:
1. **Consulta exitosa:** Cliente con cuentas y solicitudes
2. **Cliente no encontrado:** Manejo de excepción cuando no existe el DNI
3. **Cliente sin cuentas:** Procesamiento correcto cuando no hay cuentas bancarias
4. **Cliente sin solicitudes:** Procesamiento correcto cuando no hay solicitudes pendientes
5. **Manejo de SQLException:** Captura y transformación de excepciones de base de datos
6. **Excepción general:** Manejo de errores inesperados
7. **Múltiples cuentas:** Procesamiento de más de una cuenta bancaria
8. **Múltiples solicitudes:** Procesamiento de más de una solicitud pendiente

#### BDD Mockito implementado:
- **given() en lugar de when():** Sintaxis más legible y expresiva
- **willReturn() en lugar de thenReturn():** Consistencia con BDD
- **willThrow() en lugar de thenThrow():** Manejo de excepciones más claro
- **Mejor legibilidad:** Los tests son más fáciles de entender y mantener

### Configuración del proyecto:
- **Git ignore optimizado:** Archivo `.gitignore` completo que excluye archivos innecesarios
- **Cobertura de IDEs:** IntelliJ IDEA, Eclipse, NetBeans, VS Code
- **Archivos de build:** Maven, Gradle, archivos compilados
- **Archivos temporales:** Logs, archivos de backup, archivos del sistema operativo
- **Configuraciones sensibles:** Variables de entorno, archivos de configuración específicos

### Estructura de respuesta:
```json
{
  "dni": "12345678",
  "nombre": "Juan Pérez",
  "email": "juan.perez@email.com",
  "cuentaBancaria": [
    {
      "numero": "1234567890",
      "tipo": "Ahorros",
      "saldo": 5000.0
    }
  ],
  "solicitudesPendientes": [
    {
      "numero": "SOL001",
      "fecha": "2024-01-20",
      "asunto": "Solicitud de préstamo"
    }
  ]
}
```

El script del procedimiento almacenado se encuentra en `src/main/resources/sql/procedimiento_almacenado.sql`

## Compilación

Para compilar el proyecto:

```bash
mvn clean compile
```

## Ejecución

Para ejecutar la aplicación:

```bash
mvn spring-boot:run
```

La aplicación se ejecutará en el puerto 8080.

## Testing

Para ejecutar los tests:

```bash
mvn test
```

## Estructura del proyecto

```
src/
├── main/
│   ├── java/
│   │   └── com/example/springbootoracleunittest/
│   │       ├── SpringBootOracleUnittestApplication.java
│   │       ├── controller/
│   │       │   └── ClienteController.java
│   │       ├── service/
│   │       │   └── ClienteService.java
│   │       ├── repository/
│   │       │   ├── ClienteRepository.java
│   │       │   └── ClienteStoredProcedureRepository.java
│   │       ├── model/
│   │       │   ├── Cliente.java
│   │       │   ├── CuentaBancaria.java
│   │       │   └── Solicitud.java
│   │       ├── dto/
│   │       │   ├── ClienteResponseDTO.java
│   │       │   └── ClienteDetalleDTO.java
│   │       └── config/
│   │           └── DatabaseConfig.java
│   └── resources/
│       ├── application.properties
│       └── sql/
│           └── procedimiento_almacenado.sql
└── test/
    └── java/
        └── com/example/springbootoracleunittest/
            ├── SpringBootOracleUnittestApplicationTests.java
            └── controller/
                └── ClienteControllerTest.java
```
