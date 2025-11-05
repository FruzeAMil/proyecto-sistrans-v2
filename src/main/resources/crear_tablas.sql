
-- Tabla CIUDAD
CREATE TABLE CIUDAD (
    id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nombre VARCHAR2(100) NOT NULL
);

-- Tabla USUARIO_CONDUCTOR
CREATE TABLE USUARIO_CONDUCTOR (
    id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    cedula VARCHAR2(20) NOT NULL UNIQUE,
    nombre VARCHAR2(100) NOT NULL,
    correo VARCHAR2(100) NOT NULL UNIQUE,
    celular VARCHAR2(20) NOT NULL,
    calificacion NUMBER DEFAULT 0 CHECK (calificacion >= 0 AND calificacion <= 5)
);

-- Tabla USUARIO_SERVICIO
CREATE TABLE USUARIO_SERVICIO (
    id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    cedula VARCHAR2(20) NOT NULL UNIQUE,
    nombre VARCHAR2(100) NOT NULL,
    correo VARCHAR2(100) NOT NULL UNIQUE,
    celular VARCHAR2(20) NOT NULL,
    calificacion NUMBER DEFAULT 0 CHECK (calificacion >= 0 AND calificacion <= 5),
    numTarjeta VARCHAR2(20),
    nombreTarjeta VARCHAR2(100),
    vencimiento VARCHAR2(10),
    codigoSeguridad NUMBER
);

-- Tabla VEHICULO
CREATE TABLE VEHICULO (
    id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    placa VARCHAR2(10) NOT NULL UNIQUE,
    marca VARCHAR2(50) NOT NULL,
    modelo VARCHAR2(50) NOT NULL,
    capacidadPasajeros NUMBER NOT NULL CHECK (capacidadPasajeros > 0),
    nivel VARCHAR2(20) CHECK (nivel IN ('LARGE', 'COMFORT', 'ESTANDAR')),
    id_usuarioConductor NUMBER NOT NULL,
    CONSTRAINT fk_vehiculo_conductor FOREIGN KEY (id_usuarioConductor) 
        REFERENCES USUARIO_CONDUCTOR(id) ON DELETE CASCADE
);

-- Tabla SERVICIO
CREATE TABLE SERVICIO (
    id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    distanciaKm NUMBER,
    idTarifa NUMBER,
    tipoServicio VARCHAR2(50) NOT NULL,
    fecha DATE NOT NULL,
    costo NUMBER(10,2) NOT NULL,
    id_usuarioConductor NUMBER NOT NULL,
    id_usuarioServicio NUMBER NOT NULL,
    id_vehiculo NUMBER NOT NULL,
    CONSTRAINT fk_servicio_conductor FOREIGN KEY (id_usuarioConductor) 
        REFERENCES USUARIO_CONDUCTOR(id) ON DELETE CASCADE,
    CONSTRAINT fk_servicio_usuario FOREIGN KEY (id_usuarioServicio) 
        REFERENCES USUARIO_SERVICIO(id) ON DELETE CASCADE,
    CONSTRAINT fk_servicio_vehiculo FOREIGN KEY (id_vehiculo) 
        REFERENCES VEHICULO(id) ON DELETE CASCADE
);

-- Tabla PUNTO
CREATE TABLE PUNTO (
    id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    direccion VARCHAR2(200) NOT NULL,
    longitud NUMBER NOT NULL,
    latitud NUMBER NOT NULL,
    orden NUMBER NOT NULL,
    id_servicio NUMBER NOT NULL,
    id_ciudad NUMBER NOT NULL,
    CONSTRAINT fk_punto_servicio FOREIGN KEY (id_servicio) 
        REFERENCES SERVICIO(id) ON DELETE CASCADE,
    CONSTRAINT fk_punto_ciudad FOREIGN KEY (id_ciudad) 
        REFERENCES CIUDAD(id) ON DELETE CASCADE
);

-- Tabla REVISION
CREATE TABLE REVISION (
    id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    calificacion NUMBER NOT NULL CHECK (calificacion >= 1 AND calificacion <= 5),
    comentario VARCHAR2(200) NOT NULL,
    id_servicio NUMBER NOT NULL,
    CONSTRAINT fk_revision_servicio FOREIGN KEY (id_servicio) 
        REFERENCES SERVICIO(id) ON DELETE CASCADE
);

-- Tabla VIAJE
CREATE TABLE VIAJE (
    id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    horaInicio VARCHAR2(10),
    horaFin VARCHAR2(10),
    costo NUMBER(10,2) NOT NULL,
    id_servicio NUMBER NOT NULL,
    CONSTRAINT fk_viaje_servicio FOREIGN KEY (id_servicio) 
        REFERENCES SERVICIO(id) ON DELETE CASCADE
);

-- ====================================
-- ÍNDICES PARA MEJORAR PERFORMANCE
-- ====================================
CREATE INDEX idx_vehiculo_conductor ON VEHICULO(id_usuarioConductor);
CREATE INDEX idx_servicio_conductor ON SERVICIO(id_usuarioConductor);
CREATE INDEX idx_servicio_usuario ON SERVICIO(id_usuarioServicio);
CREATE INDEX idx_servicio_vehiculo ON SERVICIO(id_vehiculo);
CREATE INDEX idx_servicio_fecha ON SERVICIO(fecha);
CREATE INDEX idx_punto_servicio ON PUNTO(id_servicio);
CREATE INDEX idx_punto_ciudad ON PUNTO(id_ciudad);
CREATE INDEX idx_revision_servicio ON REVISION(id_servicio);
CREATE INDEX idx_viaje_servicio ON VIAJE(id_servicio);