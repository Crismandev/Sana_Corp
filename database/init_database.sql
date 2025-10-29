-- Script de inicialización de la base de datos hospital_virtual
-- Crear la base de datos si no existe
CREATE DATABASE IF NOT EXISTS hospital_virtual;
USE hospital_virtual;

-- Crear tabla de roles
CREATE TABLE IF NOT EXISTS roles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre_rol VARCHAR(50) NOT NULL UNIQUE,
    descripcion VARCHAR(255),
    activo BOOLEAN DEFAULT TRUE,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Crear tabla de personas
CREATE TABLE IF NOT EXISTS personas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellido_paterno VARCHAR(100) NOT NULL,
    apellido_materno VARCHAR(100),
    dni VARCHAR(8) NOT NULL UNIQUE,
    telefono VARCHAR(15),
    email VARCHAR(100),
    direccion VARCHAR(255),
    fecha_nacimiento DATE,
    genero ENUM('MASCULINO', 'FEMENINO', 'OTRO'),
    activo BOOLEAN DEFAULT TRUE,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Crear tabla de usuarios
CREATE TABLE IF NOT EXISTS usuarios (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    persona_id BIGINT NOT NULL,
    activo BOOLEAN DEFAULT TRUE,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (persona_id) REFERENCES personas(id)
);

-- Crear tabla de relación usuario-rol (muchos a muchos)
CREATE TABLE IF NOT EXISTS usuario_roles (
    usuario_id BIGINT NOT NULL,
    rol_id BIGINT NOT NULL,
    PRIMARY KEY (usuario_id, rol_id),
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id),
    FOREIGN KEY (rol_id) REFERENCES roles(id)
);

-- Crear tabla de especialidades
CREATE TABLE IF NOT EXISTS especialidades (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre_especialidad VARCHAR(100) NOT NULL UNIQUE,
    descripcion TEXT,
    activo BOOLEAN DEFAULT TRUE,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Crear tabla de consultorios
CREATE TABLE IF NOT EXISTS consultorios (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    numero VARCHAR(10) NOT NULL UNIQUE,
    ubicacion VARCHAR(100) NOT NULL,
    descripcion TEXT,
    activo BOOLEAN DEFAULT TRUE,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Crear tabla de pacientes
CREATE TABLE IF NOT EXISTS pacientes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    persona_id BIGINT NOT NULL,
    numero_historia_clinica VARCHAR(20) NOT NULL UNIQUE,
    tipo_sangre VARCHAR(5),
    alergias TEXT,
    enfermedades_cronicas TEXT,
    contacto_emergencia_nombre VARCHAR(100),
    contacto_emergencia_telefono VARCHAR(15),
    activo BOOLEAN DEFAULT TRUE,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (persona_id) REFERENCES personas(id)
);

-- Crear tabla de médicos
CREATE TABLE IF NOT EXISTS medicos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    persona_id BIGINT NOT NULL,
    especialidad_id BIGINT NOT NULL,
    numero_colegiatura VARCHAR(20) NOT NULL UNIQUE,
    activo BOOLEAN DEFAULT TRUE,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (persona_id) REFERENCES personas(id),
    FOREIGN KEY (especialidad_id) REFERENCES especialidades(id)
);

-- Crear tabla de horarios
CREATE TABLE IF NOT EXISTS horarios (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    medico_id BIGINT NOT NULL,
    consultorio_id BIGINT NOT NULL,
    dia_semana ENUM('LUNES', 'MARTES', 'MIERCOLES', 'JUEVES', 'VIERNES', 'SABADO', 'DOMINGO') NOT NULL,
    hora_inicio TIME NOT NULL,
    hora_fin TIME NOT NULL,
    activo BOOLEAN DEFAULT TRUE,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (medico_id) REFERENCES medicos(id),
    FOREIGN KEY (consultorio_id) REFERENCES consultorios(id)
);

-- Crear tabla de citas
CREATE TABLE IF NOT EXISTS citas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    paciente_id BIGINT NOT NULL,
    medico_id BIGINT NOT NULL,
    consultorio_id BIGINT NOT NULL,
    fecha_cita DATE NOT NULL,
    hora_inicio TIME NOT NULL,
    hora_fin TIME NOT NULL,
    estado ENUM('PROGRAMADA', 'CONFIRMADA', 'EN_CURSO', 'COMPLETADA', 'CANCELADA', 'NO_ASISTIO') DEFAULT 'PROGRAMADA',
    motivo_consulta TEXT,
    observaciones TEXT,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (paciente_id) REFERENCES pacientes(id),
    FOREIGN KEY (medico_id) REFERENCES medicos(id),
    FOREIGN KEY (consultorio_id) REFERENCES consultorios(id)
);

-- Insertar datos de prueba

-- Insertar roles
INSERT INTO roles (nombre_rol, descripcion) VALUES
('ROLE_ADMIN', 'Administrador del sistema'),
('ROLE_SECRETARIO', 'Secretario médico'),
('ROLE_MEDICO', 'Médico'),
('ROLE_PACIENTE', 'Paciente');

-- Insertar especialidades
INSERT INTO especialidades (nombre_especialidad, descripcion) VALUES
('Cardiología', 'Especialidad médica que se encarga del estudio, diagnóstico y tratamiento de las enfermedades del corazón'),
('Neurología', 'Especialidad médica que trata los trastornos del sistema nervioso'),
('Pediatría', 'Especialidad médica que se centra en la salud de bebés, niños y adolescentes'),
('Ginecología', 'Especialidad médica que se ocupa de la salud del aparato reproductor femenino'),
('Traumatología', 'Especialidad médica que se dedica al estudio de las lesiones del aparato locomotor');

-- Insertar consultorios
INSERT INTO consultorios (numero, ubicacion, descripcion) VALUES
('101', 'Primer Piso - Ala Norte', 'Consultorio de Cardiología'),
('102', 'Primer Piso - Ala Norte', 'Consultorio de Neurología'),
('201', 'Segundo Piso - Ala Sur', 'Consultorio de Pediatría'),
('202', 'Segundo Piso - Ala Sur', 'Consultorio de Ginecología'),
('301', 'Tercer Piso - Ala Este', 'Consultorio de Traumatología');

-- Insertar personas (secretarios, médicos y pacientes)
INSERT INTO personas (nombre, apellido_paterno, apellido_materno, dni, telefono, email, direccion, fecha_nacimiento, genero) VALUES
-- Secretarios
('María', 'González', 'López', '12345678', '987654321', 'maria.gonzalez@hospital.com', 'Av. Principal 123', '1985-03-15', 'FEMENINO'),
('Carlos', 'Rodríguez', 'Pérez', '87654321', '987654322', 'carlos.rodriguez@hospital.com', 'Jr. Secundario 456', '1982-07-20', 'MASCULINO'),

-- Médicos
('Dr. Juan', 'Martínez', 'Silva', '11111111', '987111111', 'juan.martinez@hospital.com', 'Av. Médicos 789', '1975-05-10', 'MASCULINO'),
('Dra. Ana', 'López', 'García', '22222222', '987222222', 'ana.lopez@hospital.com', 'Jr. Doctores 321', '1980-09-25', 'FEMENINO'),
('Dr. Pedro', 'Sánchez', 'Morales', '33333333', '987333333', 'pedro.sanchez@hospital.com', 'Av. Salud 654', '1978-12-03', 'MASCULINO'),
('Dra. Laura', 'Torres', 'Vega', '44444444', '987444444', 'laura.torres@hospital.com', 'Jr. Medicina 987', '1983-02-18', 'FEMENINO'),
('Dr. Miguel', 'Herrera', 'Castillo', '55555555', '987555555', 'miguel.herrera@hospital.com', 'Av. Especialistas 147', '1976-11-30', 'MASCULINO'),

-- Pacientes
('Luis', 'Fernández', 'Ruiz', '66666666', '987666666', 'luis.fernandez@email.com', 'Jr. Pacientes 258', '1990-04-12', 'MASCULINO'),
('Carmen', 'Jiménez', 'Mendoza', '77777777', '987777777', 'carmen.jimenez@email.com', 'Av. Ciudadanos 369', '1988-08-07', 'FEMENINO'),
('Roberto', 'Vargas', 'Cruz', '88888888', '987888888', 'roberto.vargas@email.com', 'Jr. Residentes 741', '1995-01-22', 'MASCULINO'),
('Patricia', 'Moreno', 'Díaz', '99999999', '987999999', 'patricia.moreno@email.com', 'Av. Vecinos 852', '1992-06-14', 'FEMENINO'),
('José', 'Ramírez', 'Flores', '10101010', '987101010', 'jose.ramirez@email.com', 'Jr. Comunidad 963', '1987-10-28', 'MASCULINO');

-- Insertar usuarios
INSERT INTO usuarios (username, password, persona_id) VALUES
-- Secretarios (password: secretario123)
('secretario1', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBaLFlfeyNBqRm', 1),
('secretario2', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBaLFlfeyNBqRm', 2),

-- Médicos (password: medico123)
('medico1', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 3),
('medico2', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 4),
('medico3', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 5),
('medico4', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 6),
('medico5', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 7);

-- Asignar roles a usuarios
INSERT INTO usuario_roles (usuario_id, rol_id) VALUES
-- Secretarios
(1, 2), -- secretario1 -> ROLE_SECRETARIO
(2, 2), -- secretario2 -> ROLE_SECRETARIO

-- Médicos
(3, 3), -- medico1 -> ROLE_MEDICO
(4, 3), -- medico2 -> ROLE_MEDICO
(5, 3), -- medico3 -> ROLE_MEDICO
(6, 3), -- medico4 -> ROLE_MEDICO
(7, 3); -- medico5 -> ROLE_MEDICO

-- Insertar pacientes
INSERT INTO pacientes (persona_id, numero_historia_clinica, tipo_sangre, contacto_emergencia_nombre, contacto_emergencia_telefono) VALUES
(8, 'HC001', 'O+', 'María Fernández', '987000001'),
(9, 'HC002', 'A+', 'Pedro Jiménez', '987000002'),
(10, 'HC003', 'B+', 'Ana Vargas', '987000003'),
(11, 'HC004', 'AB+', 'Carlos Moreno', '987000004'),
(12, 'HC005', 'O-', 'Lucía Ramírez', '987000005');

-- Insertar médicos
INSERT INTO medicos (persona_id, especialidad_id, numero_colegiatura) VALUES
(3, 1, 'COL001'), -- Dr. Juan Martínez - Cardiología
(4, 2, 'COL002'), -- Dra. Ana López - Neurología
(5, 3, 'COL003'), -- Dr. Pedro Sánchez - Pediatría
(6, 4, 'COL004'), -- Dra. Laura Torres - Ginecología
(7, 5, 'COL005'); -- Dr. Miguel Herrera - Traumatología

-- Insertar horarios de médicos
INSERT INTO horarios (medico_id, consultorio_id, dia_semana, hora_inicio, hora_fin) VALUES
-- Dr. Juan Martínez (Cardiología)
(1, 1, 'LUNES', '08:00:00', '12:00:00'),
(1, 1, 'MIERCOLES', '08:00:00', '12:00:00'),
(1, 1, 'VIERNES', '08:00:00', '12:00:00'),

-- Dra. Ana López (Neurología)
(2, 2, 'MARTES', '09:00:00', '13:00:00'),
(2, 2, 'JUEVES', '09:00:00', '13:00:00'),

-- Dr. Pedro Sánchez (Pediatría)
(3, 3, 'LUNES', '14:00:00', '18:00:00'),
(3, 3, 'MARTES', '14:00:00', '18:00:00'),
(3, 3, 'MIERCOLES', '14:00:00', '18:00:00'),

-- Dra. Laura Torres (Ginecología)
(4, 4, 'JUEVES', '08:00:00', '12:00:00'),
(4, 4, 'VIERNES', '14:00:00', '18:00:00'),

-- Dr. Miguel Herrera (Traumatología)
(5, 5, 'LUNES', '10:00:00', '14:00:00'),
(5, 5, 'MIERCOLES', '10:00:00', '14:00:00'),
(5, 5, 'VIERNES', '10:00:00', '14:00:00');

-- Insertar algunas citas de ejemplo
INSERT INTO citas (paciente_id, medico_id, consultorio_id, fecha_cita, hora_inicio, hora_fin, estado, motivo_consulta) VALUES
(1, 1, 1, '2024-11-01', '08:30:00', '09:00:00', 'PROGRAMADA', 'Control cardiológico rutinario'),
(2, 2, 2, '2024-11-01', '09:30:00', '10:00:00', 'CONFIRMADA', 'Consulta por dolores de cabeza'),
(3, 3, 3, '2024-11-01', '14:30:00', '15:00:00', 'PROGRAMADA', 'Control pediátrico'),
(4, 4, 4, '2024-11-02', '08:30:00', '09:00:00', 'PROGRAMADA', 'Consulta ginecológica'),
(5, 5, 5, '2024-11-02', '10:30:00', '11:00:00', 'CONFIRMADA', 'Evaluación traumatológica');

COMMIT;