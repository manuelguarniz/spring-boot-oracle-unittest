-- Procedimiento almacenado ficticio para consultar datos del cliente por DNI
-- Este es un ejemplo de cómo podría ser el procedimiento almacenado en Oracle

CREATE OR REPLACE PROCEDURE CONSULTAR_CLIENTE_POR_DNI (
    p_dni IN VARCHAR2,
    rs1 OUT SYS_REFCURSOR,  -- Datos del cliente (un solo registro)
    rs2 OUT SYS_REFCURSOR,  -- Cuentas bancarias asociadas al cliente
    rs3 OUT SYS_REFCURSOR   -- Solicitudes pendientes
) AS
BEGIN
    -- Cursor RS1: Datos del cliente
    OPEN rs1 FOR
        SELECT dni, nombre, apellido, email, telefono, direccion
        FROM CLIENTES
        WHERE dni = p_dni;
    
    -- Cursor RS2: Cuentas bancarias asociadas al cliente
    OPEN rs2 FOR
        SELECT cb.numero_cuenta, cb.nombre_cliente, cb.tipo_cuenta, cb.saldo
        FROM CUENTAS_BANCARIAS cb
        WHERE cb.dni_cliente = p_dni;
    
    -- Cursor RS3: Solicitudes pendientes
    OPEN rs3 FOR
        SELECT s.numero_solicitud, s.fecha_solicitud, s.asunto, s.estado
        FROM SOLICITUDES s
        WHERE s.dni_cliente = p_dni
        AND s.estado IN ('PENDIENTE', 'EN_REVISION')
        ORDER BY s.fecha_solicitud DESC;
        
EXCEPTION
    WHEN OTHERS THEN
        -- Manejo de errores
        RAISE_APPLICATION_ERROR(-20001, 'Error al consultar cliente: ' || SQLERRM);
END CONSULTAR_CLIENTE_POR_DNI;
/

-- Script para crear las tablas de ejemplo (opcional)
/*
CREATE TABLE CLIENTES (
    dni VARCHAR2(20) PRIMARY KEY,
    nombre VARCHAR2(100) NOT NULL,
    apellido VARCHAR2(100) NOT NULL,
    email VARCHAR2(150),
    telefono VARCHAR2(20),
    direccion VARCHAR2(200)
);

CREATE TABLE CUENTAS_BANCARIAS (
    numero_cuenta VARCHAR2(20) PRIMARY KEY,
    dni_cliente VARCHAR2(20) NOT NULL,
    nombre_cliente VARCHAR2(200) NOT NULL,
    tipo_cuenta VARCHAR2(50) NOT NULL,
    saldo NUMBER(15,2) DEFAULT 0,
    FOREIGN KEY (dni_cliente) REFERENCES CLIENTES(dni)
);

CREATE TABLE SOLICITUDES (
    numero_solicitud VARCHAR2(20) PRIMARY KEY,
    dni_cliente VARCHAR2(20) NOT NULL,
    fecha_solicitud DATE DEFAULT SYSDATE,
    asunto VARCHAR2(200) NOT NULL,
    estado VARCHAR2(50) DEFAULT 'PENDIENTE',
    descripcion VARCHAR2(500),
    FOREIGN KEY (dni_cliente) REFERENCES CLIENTES(dni)
);

-- Datos de ejemplo
INSERT INTO CLIENTES (dni, nombre, apellido, email, telefono, direccion) 
VALUES ('12345678', 'Juan', 'Pérez', 'juan.perez@email.com', '123456789', 'Calle Principal 123');

INSERT INTO CUENTAS_BANCARIAS (numero_cuenta, dni_cliente, nombre_cliente, tipo_cuenta, saldo)
VALUES ('1234567890', '12345678', 'Juan Pérez', 'Ahorros', 5000.00);

INSERT INTO CUENTAS_BANCARIAS (numero_cuenta, dni_cliente, nombre_cliente, tipo_cuenta, saldo)
VALUES ('0987654321', '12345678', 'Juan Pérez', 'Corriente', 15000.00);

INSERT INTO SOLICITUDES (numero_solicitud, dni_cliente, fecha_solicitud, asunto, estado, descripcion)
VALUES ('SOL001', '12345678', SYSDATE - 5, 'Solicitud de préstamo', 'PENDIENTE', 'Solicitud de préstamo personal');

INSERT INTO SOLICITUDES (numero_solicitud, dni_cliente, fecha_solicitud, asunto, estado, descripcion)
VALUES ('SOL002', '12345678', SYSDATE - 2, 'Cambio de dirección', 'EN_REVISION', 'Actualización de datos personales');
*/
