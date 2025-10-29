-- =====================================================
-- SCRIPT COMPLETO: BASE DE DATOS HOSPITAL_VIRTUAL
-- Descripción: Sistema integral de gestión hospitalaria
-- Creado: 2025-10-12
-- =====================================================

-- =====================================================
-- SECCIÓN 1: CREACIÓN Y CONFIGURACIÓN INICIAL
-- =====================================================

-- Eliminar base de datos existente para empezar desde cero
DROP DATABASE IF EXISTS hospital_virtual;

-- Crear nueva base de datos
CREATE DATABASE hospital_virtual;

-- Seleccionar la base de datos para uso posterior
USE hospital_virtual;

-- =====================================================
-- SECCIÓN 2: TABLAS PRINCIPALES SIN DEPENDENCIAS
-- Estas tablas no tienen claves foráneas y se crean primero
-- =====================================================

-- Tabla de roles de usuario: Define los tipos de usuarios del sistema
CREATE TABLE roles (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(50) NOT NULL UNIQUE,
    descripcion VARCHAR(100),
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de usuarios: Almacena credenciales de acceso al sistema
CREATE TABLE usuarios (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    email VARCHAR(120) UNIQUE,
    estado TINYINT DEFAULT 1,
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de especialidades médicas: Cataloga las áreas de especialización
CREATE TABLE especialidades (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL UNIQUE,
    descripcion VARCHAR(255),
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de consultorios: Ubicaciones físicas de atención
CREATE TABLE consultorios (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(50) NOT NULL,
    ubicacion VARCHAR(100)
);

-- Tabla de medicamentos: Catálogo de fármacos disponibles
CREATE TABLE medicamentos (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT,
    unidad VARCHAR(50),
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de proveedores: Empresas que suministran medicamentos
CREATE TABLE proveedores (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    ruc VARCHAR(11) UNIQUE,
    contacto VARCHAR(100),
    telefono VARCHAR(15),
    email VARCHAR(100),
    direccion VARCHAR(255),
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- =====================================================
-- SECCIÓN 3: TABLAS CON DEPENDENCIAS
-- Estas tablas tienen claves foráneas y dependen de las anteriores
-- =====================================================

-- Tabla de personas: Información personal vinculada a usuarios
CREATE TABLE personas (
    id INT PRIMARY KEY AUTO_INCREMENT,
    id_usuario INT UNIQUE,
    dni CHAR(8) NOT NULL UNIQUE,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    fecha_nacimiento DATE NOT NULL,
    genero ENUM('M', 'F') NOT NULL,
    telefono VARCHAR(15),
    direccion VARCHAR(255),
    distrito VARCHAR(100),
    provincia VARCHAR(100),
    departamento VARCHAR(100),
    email VARCHAR(120),
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id)
);

-- Tabla de relación usuario-roles: Asigna múltiples roles a usuarios
CREATE TABLE usuario_roles (
    id_usuario INT NOT NULL,
    id_rol INT NOT NULL,
    PRIMARY KEY (id_usuario, id_rol),
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id),
    FOREIGN KEY (id_rol) REFERENCES roles(id)
);

-- Tabla de médicos: Profesionales de la salud con especialidad
CREATE TABLE medicos (
    id INT PRIMARY KEY AUTO_INCREMENT,
    id_persona INT UNIQUE,
    especialidad_id INT NOT NULL,
    FOREIGN KEY (id_persona) REFERENCES personas(id),
    FOREIGN KEY (especialidad_id) REFERENCES especialidades(id)
);

-- Tabla de pacientes: Personas que reciben atención médica
CREATE TABLE pacientes (
    id INT PRIMARY KEY AUTO_INCREMENT,
    id_persona INT UNIQUE,
    seguro_medico VARCHAR(50),
    FOREIGN KEY (id_persona) REFERENCES personas(id)
);

-- Tabla de inventario: Control de stock de medicamentos
CREATE TABLE inventario (
    id INT PRIMARY KEY AUTO_INCREMENT,
    medicamento_id INT NOT NULL,
    cantidad INT NOT NULL,
    stock_minimo INT DEFAULT 10,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (medicamento_id) REFERENCES medicamentos(id)
);

-- =====================================================
-- SECCIÓN 4: TABLAS OPERATIVAS
-- Estas tablas manejan el funcionamiento diario del sistema
-- =====================================================

-- Tabla de horarios médicos: Disponibilidad de los médicos
CREATE TABLE horarios (
    id INT PRIMARY KEY AUTO_INCREMENT,
    id_medico INT NOT NULL,
    dia ENUM('Lunes','Martes','Miércoles','Jueves','Viernes','Sábado'),
    turno ENUM('Mañana','Tarde','Noche'),
    hora_inicio TIME NOT NULL,
    hora_fin TIME NOT NULL,
    FOREIGN KEY (id_medico) REFERENCES medicos(id)
);

-- Tabla de citas: Agenda de consultas médicas
CREATE TABLE citas (
    id INT PRIMARY KEY AUTO_INCREMENT,
    paciente_id INT NOT NULL,
    medico_id INT NOT NULL,
    fecha_hora DATETIME NOT NULL,
    consultorio_id INT,
    estado ENUM('Programada','Cancelada','Completada') DEFAULT 'Programada',
    motivo_consulta VARCHAR(255),
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (paciente_id) REFERENCES pacientes(id),
    FOREIGN KEY (medico_id) REFERENCES medicos(id),
    FOREIGN KEY (consultorio_id) REFERENCES consultorios(id),
    INDEX idx_cita_medico_fecha (medico_id, fecha_hora)
);

-- Tabla de historial médico: Registros clínicos de las consultas
CREATE TABLE historial_medico (
    id INT PRIMARY KEY AUTO_INCREMENT,
    cita_id INT NOT NULL,
    notas TEXT,
    archivos_adjuntos VARCHAR(255),
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (cita_id) REFERENCES citas(id)
);

-- Tabla de recetas: Prescripciones médicas generadas
CREATE TABLE recetas (
    id INT PRIMARY KEY AUTO_INCREMENT,
    historial_id INT NOT NULL,
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (historial_id) REFERENCES historial_medico(id)
);

-- Tabla de relación receta-medicamentos: Detalle de medicamentos en recetas
CREATE TABLE receta_medicamentos (
    receta_id INT NOT NULL,
    medicamento_id INT NOT NULL,
    dosis VARCHAR(100),
    frecuencia VARCHAR(100),
    duracion VARCHAR(100),
    PRIMARY KEY (receta_id, medicamento_id),
    FOREIGN KEY (receta_id) REFERENCES recetas(id),
    FOREIGN KEY (medicamento_id) REFERENCES medicamentos(id)
);

-- Tabla de solicitudes: Pedidos de medicamentos del inventario
CREATE TABLE solicitudes (
    id INT PRIMARY KEY AUTO_INCREMENT,
    solicitante_id INT NOT NULL,
    tipo_solicitante ENUM('Paciente','Medico') NOT NULL,
    medicamento_id INT NOT NULL,
    cantidad INT NOT NULL,
    estado ENUM('Pendiente','Aprobada','Rechazada') DEFAULT 'Pendiente',
    fecha_solicitud TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (medicamento_id) REFERENCES medicamentos(id),
    FOREIGN KEY (solicitante_id) REFERENCES usuarios(id)
);

-- =====================================================
-- SECCIÓN 5: INSERCIÓN DE DATOS BÁSICOS
-- Datos fundamentales para el funcionamiento del sistema
-- =====================================================

-- Insertar roles del sistema
INSERT INTO roles (nombre, descripcion) VALUES
('Administrador', 'Control total del sistema'),
('Cliente', 'Paciente con acceso a servicios'),
('Médico', 'Profesional que atiende pacientes'),
('Secretario', 'Gestiona horarios y citas'),
('Proveedor', 'Maneja inventario de medicamentos');

-- Insertar usuarios iniciales (contraseñas: password123)
INSERT INTO usuarios (username, password_hash, email, estado) VALUES
('admin01', '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9', 'admin@hospital.com', 1),
('medico01', '673ab82a6530ee3bd9b04ee72a4d66afa7fa059aedc685cf44e35d29d90ebafa', 'medico@hospital.com', 1),
('secretario01', '1c392f167af58d8184653ba5f241d00f8e847e3e83211a04be121339ee2744e9', 'secre@hospital.com', 1),
('cliente01', '09a31a7001e261ab1e056182a71d3cf57f582ca9a29cff5eb83be0f0549730a9', 'cliente@hospital.com', 1),
('proveedor01', 'ec84d5c616f419b0a83be49f4b409696db4fb169b1f114ae4d8ae0420129c749', 'proveedor@hospital.com', 1);

-- Asignar roles a usuarios
INSERT INTO usuario_roles (id_usuario, id_rol) VALUES
(1, 1), (4, 2), (2, 3), (3, 4), (5, 5);

-- Insertar especialidades médicas básicas
INSERT INTO especialidades (nombre, descripcion) VALUES
('Pediatría', 'Atención médica infantil'),
('Cardiología', 'Especialidad en enfermedades del corazón'),
('Dermatología', 'Especialidad en enfermedades de la piel'),
('Ginecología', 'Salud femenina y reproductiva'),
('Traumatología', 'Especialidad en huesos y articulaciones'),
('Oftalmología', 'Especialidad en ojos y visión');

-- Insertar consultorios disponibles
INSERT INTO consultorios (nombre, ubicacion) VALUES
('Consultorio 101', 'Primer Piso, Ala A'),
('Consultorio 102', 'Primer Piso, Ala B'),
('Consultorio 201', 'Segundo Piso, Cardiología'),
('Consultorio 202', 'Segundo Piso, Dermatología'),
('Consultorio 301', 'Tercer Piso, Ginecología'),
('Consultorio 302', 'Tercer Piso, Traumatología');

-- Insertar medicamentos básicos
INSERT INTO medicamentos (nombre, descripcion, unidad) VALUES
('Paracetamol 500mg', 'Analgésico y antipirético', 'tabletas'),
('Ibuprofeno 400mg', 'Antiinflamatorio y analgésico', 'tabletas'),
('Amoxicilina 500mg', 'Antibiótico de amplio espectro', 'cápsulas'),
('Loratadina 10mg', 'Antihistamínico para alergias', 'tabletas'),
('Omeprazol 20mg', 'Protector gástrico', 'cápsulas'),
('Metformina 850mg', 'Antidiabético oral', 'tabletas');

-- Insertar proveedores
INSERT INTO proveedores (nombre, ruc, contacto, telefono, email, direccion) VALUES
('PharmaMedic', '12345678901', 'Almacén Central', '900000005', 'proveedor@hospital.com', 'Zona Industrial');

-- =====================================================
-- SECCIÓN 6: DATOS DE PERSONAS Y RELACIONES
-- Información personal y vinculación con usuarios
-- =====================================================

-- Insertar datos personales (vinculados a usuarios existentes)
INSERT INTO personas (id_usuario, dni, nombre, apellido, fecha_nacimiento, genero, telefono, direccion, distrito, provincia, departamento, email) VALUES
(1, '10000001', 'Admin', 'Principal', '1980-01-01', 'M', '900000001', 'Av Central 123', 'Cercado', 'Lima', 'Lima', 'admin@hospital.com'),
(2, '20000002', 'Luis', 'García', '1985-06-15', 'M', '900000002', 'Calle Salud 456', 'San Borja', 'Lima', 'Lima', 'medico@hospital.com'),
(3, '30000003', 'Carla', 'Rojas', '1990-03-12', 'F', '900000003', 'Oficina 789', 'La Molina', 'Lima', 'Lima', 'secre@hospital.com'),
(4, '40000004', 'María', 'López', '1992-08-22', 'F', '900000004', 'Mz A Lt 5', 'San Juan', 'Lima', 'Lima', 'cliente@hospital.com'),
(5, '50000005', 'Almacén', 'Central', '1975-12-01', 'M', '900000005', 'Zona Industrial', 'Villa El Salvador', 'Lima', 'Lima', 'proveedor@hospital.com');

-- Insertar usuarios adicionales para médicos y pacientes
INSERT INTO usuarios (username, password_hash, email, estado) VALUES
('medico02', '673ab82a6530ee3bd9b04ee72a4d66afa7fa059aedc685cf44e35d29d90ebafa', 'cardio@hospital.com', 1),
('medico03', '673ab82a6530ee3bd9b04ee72a4d66afa7fa059aedc685cf44e35d29d90ebafa', 'derma@hospital.com', 1),
('medico04', '673ab82a6530ee3bd9b04ee72a4d66afa7fa059aedc685cf44e35d29d90ebafa', 'gineco@hospital.com', 1),
('medico05', '673ab82a6530ee3bd9b04ee72a4d66afa7fa059aedc685cf44e35d29d90ebafa', 'trauma@hospital.com', 1),
('medico06', '673ab82a6530ee3bd9b04ee72a4d66afa7fa059aedc685cf44e35d29d90ebafa', 'oftalmo@hospital.com', 1),
('paciente02', '09a31a7001e261ab1e056182a71d3cf57f582ca9a29cff5eb83be0f0549730a9', 'paciente2@hospital.com', 1),
('paciente03', '09a31a7001e261ab1e056182a71d3cf57f582ca9a29cff5eb83be0f0549730a9', 'paciente3@hospital.com', 1),
('paciente04', '09a31a7001e261ab1e056182a71d3cf57f582ca9a29cff5eb83be0f0549730a9', 'paciente4@hospital.com', 1),
('paciente05', '09a31a7001e261ab1e056182a71d3cf57f582ca9a29cff5eb83be0f0549730a9', 'paciente5@hospital.com', 1),
('paciente06', '09a31a7001e261ab1e056182a71d3cf57f582ca9a29cff5eb83be0f0549730a9', 'paciente6@hospital.com', 1);

-- Asignar roles a usuarios adicionales
INSERT INTO usuario_roles (id_usuario, id_rol) VALUES
(6, 3), (7, 3), (8, 3), (9, 3), (10, 3),  -- Médicos
(11, 2), (12, 2), (13, 2), (14, 2), (15, 2); -- Pacientes

-- Insertar personas adicionales para médicos y pacientes
INSERT INTO personas (id_usuario, dni, nombre, apellido, fecha_nacimiento, genero, telefono, direccion, distrito, provincia, departamento, email) VALUES
(6, '20000003', 'Carlos', 'Mendoza', '1978-03-20', 'M', '900000006', 'Av Cardiológica 123', 'San Isidro', 'Lima', 'Lima', 'cardio@hospital.com'),
(7, '20000004', 'Ana', 'Silva', '1982-07-15', 'F', '900000007', 'Calle Piel 456', 'Miraflores', 'Lima', 'Lima', 'derma@hospital.com'),
(8, '20000005', 'Patricia', 'Gómez', '1975-11-30', 'F', '900000008', 'Jr Mujer 789', 'San Borja', 'Lima', 'Lima', 'gineco@hospital.com'),
(9, '20000006', 'Roberto', 'Díaz', '1980-05-10', 'M', '900000009', 'Av Huesos 321', 'La Molina', 'Lima', 'Lima', 'trauma@hospital.com'),
(10, '20000007', 'Laura', 'Torres', '1979-09-25', 'F', '900000010', 'Calle Visión 654', 'Surco', 'Lima', 'Lima', 'oftalmo@hospital.com'),
(11, '40000005', 'Juan', 'Pérez', '1990-12-05', 'M', '900000011', 'Mz B Lt 10', 'Comas', 'Lima', 'Lima', 'paciente2@hospital.com'),
(12, '40000006', 'Sofía', 'Castro', '1988-04-18', 'F', '900000012', 'Av Paciente 222', 'Los Olivos', 'Lima', 'Lima', 'paciente3@hospital.com'),
(13, '40000007', 'Miguel', 'Ruiz', '1995-08-22', 'M', '900000013', 'Calle Salud 333', 'Independencia', 'Lima', 'Lima', 'paciente4@hospital.com'),
(14, '40000008', 'Elena', 'Vargas', '1983-01-15', 'F', '900000014', 'Jr Recuperación 444', 'San Martín', 'Lima', 'Lima', 'paciente5@hospital.com'),
(15, '40000009', 'Diego', 'Ramírez', '1992-06-30', 'M', '900000015', 'Av Mejoría 555', 'Villa María', 'Lima', 'Lima', 'paciente6@hospital.com');

-- =====================================================
-- SECCIÓN 7: DATOS OPERATIVOS COMPLETOS
-- Información de médicos, pacientes, citas y registros médicos
-- =====================================================

-- Insertar médicos con sus especialidades
INSERT INTO medicos (id_persona, especialidad_id) VALUES
(2, 1),  -- Luis García - Pediatría
(6, 2),  -- Carlos Mendoza - Cardiología
(7, 3),  -- Ana Silva - Dermatología
(8, 4),  -- Patricia Gómez - Ginecología
(9, 5),  -- Roberto Díaz - Traumatología
(10, 6); -- Laura Torres - Oftalmología

-- Insertar pacientes con información de seguro
INSERT INTO pacientes (id_persona, seguro_medico) VALUES
(4, 'ESSALUD'),           -- María López
(11, 'SIS'),              -- Juan Pérez
(12, 'PACIENTE PRIVADO'), -- Sofía Castro
(13, 'ESSALUD'),          -- Miguel Ruiz
(14, 'SIS'),              -- Elena Vargas
(15, 'PACIENTE PRIVADO'); -- Diego Ramírez

-- Configurar inventario de medicamentos
INSERT INTO inventario (medicamento_id, cantidad, stock_minimo) VALUES
(1, 150, 20),  -- Paracetamol
(2, 200, 30),  -- Ibuprofeno
(3, 150, 25),  -- Amoxicilina
(4, 180, 20),  -- Loratadina
(5, 120, 15),  -- Omeprazol
(6, 100, 10);  -- Metformina

-- =====================================================
-- SECCIÓN 8: DATOS DE OPERACIÓN DIARIA
-- Citas, historiales médicos, recetas y horarios
-- =====================================================

-- Insertar citas médicas programadas
INSERT INTO citas (paciente_id, medico_id, fecha_hora, consultorio_id, estado, motivo_consulta) VALUES
(1, 1, '2025-10-10 09:30:00', 1, 'Programada', 'Dolor de cabeza persistente'),
(2, 2, '2025-10-15 10:00:00', 2, 'Programada', 'Control presión arterial y chequeo cardíaco'),
(3, 3, '2025-10-16 11:30:00', 3, 'Programada', 'Erupción cutánea en brazos y espalda'),
(4, 4, '2025-10-17 09:00:00', 4, 'Programada', 'Control ginecológico anual'),
(5, 5, '2025-10-18 14:00:00', 5, 'Programada', 'Dolor en rodilla izquierda después de caída'),
(6, 6, '2025-10-19 16:30:00', 6, 'Programada', 'Problemas de visión borrosa');

-- Insertar historiales médicos de las consultas
INSERT INTO historial_medico (cita_id, notas, archivos_adjuntos) VALUES
(1, 'Paciente presenta dolor de cabeza desde hace 3 días. Sin fiebre ni vómitos.', NULL),
(2, 'Paciente presenta presión arterial 130/85. Se recomienda monitoreo continuo y reducir consumo de sal.', 'presion_arterial.pdf'),
(3, 'Erupción diagnosticada como dermatitis alérgica. Se indica tratamiento tópico y evitar alérgenos.', 'fotos_erupcion.jpg'),
(4, 'Control ginecológico normal. Resultados de papanicolau dentro de parámetros normales.', 'pap_resultados.pdf'),
(5, 'Esguince grado 1 en rodilla izquierda. Se recomienda reposo, hielo y elevación.', 'radiografia_rodilla.jpg'),
(6, 'Diagnóstico: Miopía progresiva. Se recetan lentes con graduación -2.50 en ambos ojos.', 'examen_oftalmologico.pdf');

-- Insertar recetas médicas generadas
INSERT INTO recetas (historial_id, fecha) VALUES
(1, '2025-10-10 10:00:00'),
(2, '2025-10-15 10:30:00'),
(3, '2025-10-16 12:00:00'),
(4, '2025-10-17 09:45:00'),
(5, '2025-10-18 14:30:00'),
(6, '2025-10-19 17:00:00');

-- Insertar detalles de medicamentos en recetas
INSERT INTO receta_medicamentos (receta_id, medicamento_id, dosis, frecuencia, duracion) VALUES
(1, 1, '1 tableta', 'cada 8 horas', '5 días'),
(2, 2, '1 tableta', 'cada 8 horas si hay dolor', '3 días'),
(3, 3, '1 tableta', 'cada 24 horas', '10 días'),
(4, 4, '1 cápsula', 'cada 24 horas en ayunas', '30 días'),
(5, 2, '1 tableta', 'cada 8 horas', '5 días'),
(6, 5, '1 tableta', 'cada 12 horas', '90 días');

-- Configurar horarios de atención médica
INSERT INTO horarios (id_medico, dia, turno, hora_inicio, hora_fin) VALUES
(1, 'Lunes', 'Mañana', '08:00:00', '12:00:00'),
(2, 'Lunes', 'Mañana', '08:00:00', '12:00:00'),
(2, 'Miércoles', 'Mañana', '08:00:00', '12:00:00'),
(3, 'Martes', 'Tarde', '14:00:00', '18:00:00'),
(4, 'Jueves', 'Mañana', '09:00:00', '13:00:00'),
(5, 'Viernes', 'Mañana', '08:00:00', '12:00:00'),
(6, 'Lunes', 'Tarde', '15:00:00', '19:00:00');

-- =====================================================
-- SECCIÓN 9: CONFIRMACIÓN Y RESUMEN FINAL
-- Verificación de la creación exitosa de la base de datos
-- =====================================================

-- Mensaje de confirmación
SELECT '========================================' as Separador;
SELECT 'BASE DE DATOS HOSPITAL_VIRTUAL CREADA EXITOSAMENTE' as Mensaje;
SELECT '========================================' as Separador;

-- Mostrar resumen completo de datos insertados
SELECT 
    (SELECT COUNT(*) FROM usuarios) as total_usuarios,
    (SELECT COUNT(*) FROM personas) as total_personas,
    (SELECT COUNT(*) FROM medicos) as total_medicos,
    (SELECT COUNT(*) FROM pacientes) as total_pacientes,
    (SELECT COUNT(*) FROM citas) as total_citas,
    (SELECT COUNT(*) FROM recetas) as total_recetas,
    (SELECT COUNT(*) FROM medicamentos) as total_medicamentos,
    (SELECT COUNT(*) FROM especialidades) as total_especialidades;

-- Resumen de relaciones importantes
SELECT '=== RELACIONES PRINCIPALES ===' as Info;
SELECT 
    CONCAT('Médico: ', p.nombre, ' ', p.apellido, ' - ', e.nombre) as medico_especialidad
FROM medicos m
JOIN personas p ON m.id_persona = p.id
JOIN especialidades e ON m.especialidad_id = e.id;

SELECT '=== CITAS PROGRAMADAS ===' as Info;
SELECT 
    c.id as cita_id,
    CONCAT(p_pac.nombre, ' ', p_pac.apellido) as paciente,
    CONCAT(p_med.nombre, ' ', p_med.apellido) as medico,
    c.fecha_hora,
    c.estado
FROM citas c
JOIN pacientes pac ON c.paciente_id = pac.id
JOIN personas p_pac ON pac.id_persona = p_pac.id
JOIN medicos med ON c.medico_id = med.id
JOIN personas p_med ON med.id_persona = p_med.id;