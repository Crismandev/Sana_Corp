# 🏥 Sana Corp - Sistema Hospitalario

Sistema integral de gestión hospitalaria desarrollado con Spring Boot que permite administrar de manera eficiente usuarios, citas médicas, pacientes y especialidades.

## 📋 Descripción

Sana Corp es un sistema completo de gestión hospitalaria que incluye:

- 👥 **Gestión de Usuarios** (Administradores, Secretarios, Médicos)
- 🏥 **Consultorios y Especialidades Médicas**
- 📅 **Sistema de Citas Médicas**
- ⏰ **Horarios de Médicos**
- 👤 **Registro y Gestión de Pacientes**
- 📋 **Historiales Médicos**

## 🚀 Demo en Vivo

- **Aplicación**: [Sana Corp en Render](https://sana-corp.onrender.com) (Próximamente)
- **Repositorio**: [GitHub - Sana Corp](https://github.com/Crismandev/Sana_Corp)

## 🛠️ Tecnologías Utilizadas

### Backend
- **Spring Boot 3.5.7** - Framework principal
- **Spring Data JPA** - Persistencia de datos
- **Hibernate** - ORM
- **MySQL 8.0** - Base de datos (desarrollo)
- **PostgreSQL** - Base de datos (producción)
- **Thymeleaf** - Motor de plantillas
- **Maven** - Gestión de dependencias

### Frontend
- **HTML5 & CSS3**
- **Bootstrap 5** - Framework CSS
- **JavaScript** - Interactividad
- **Thymeleaf** - Renderizado del lado del servidor

## 📦 Instalación y Configuración Local

### Prerrequisitos

- ☕ **Java 17** o superior
- 🗄️ **MySQL 8.0** o superior
- 🔧 **Maven 3.6** o superior (incluido en el proyecto)

### 1. Clonar el Repositorio

```bash
git clone https://github.com/Crismandev/Sana_Corp.git
cd Sana_Corp
```

### 2. Configurar Base de Datos Local

```sql
-- Crear base de datos
CREATE DATABASE hospital_virtual;

-- Crear usuario (opcional)
CREATE USER 'sanacorp'@'localhost' IDENTIFIED BY 'password';
GRANT ALL PRIVILEGES ON hospital_virtual.* TO 'sanacorp'@'localhost';
FLUSH PRIVILEGES;
```

### 3. Configurar Variables de Entorno (Opcional)

Crear archivo `.env` en la raíz del proyecto:

```env
DB_URL=jdbc:mysql://localhost:3306/hospital_virtual
DB_USERNAME=root
DB_PASSWORD=tu_password
PORT=8081
```

### 4. Ejecutar la Aplicación

```bash
# Compilar y ejecutar
./mvnw clean spring-boot:run

# O generar JAR y ejecutar
./mvnw clean package -DskipTests
java -jar target/administrador-0.0.1-SNAPSHOT.jar
```

### 5. Acceder a la Aplicación

- **URL**: http://localhost:8081
- **Usuario Admin**: admin / admin123 (configurar en base de datos)

## ☁️ Despliegue en Render

### Paso 1: Preparar el Repositorio

1. Asegúrate de que todos los archivos estén en el repositorio de GitHub
2. Verifica que `application-prod.properties` esté configurado correctamente

### Paso 2: Crear Servicio Web en Render

1. Ve a [Render.com](https://render.com) y crea una cuenta
2. Conecta tu repositorio de GitHub
3. Crea un nuevo **Web Service**
4. Selecciona el repositorio `Sana_Corp`

### Paso 3: Configurar el Servicio

**Build Command:**
```bash
./mvnw clean package -DskipTests
```

**Start Command:**
```bash
java -Dspring.profiles.active=prod -jar target/administrador-0.0.1-SNAPSHOT.jar
```

**Environment:**
- **Runtime**: `Java 17`
- **Build Command**: `./mvnw clean package -DskipTests`
- **Start Command**: `java -Dspring.profiles.active=prod -jar target/administrador-0.0.1-SNAPSHOT.jar`

### Paso 4: Configurar Base de Datos PostgreSQL

1. En Render, crea una nueva **PostgreSQL Database**
2. Anota las credenciales de conexión

### Paso 5: Configurar Variables de Entorno

En la configuración del Web Service, agrega estas variables:

```env
SPRING_PROFILES_ACTIVE=prod
DATABASE_URL=postgresql://usuario:password@host:puerto/database
DB_USERNAME=tu_usuario_postgres
DB_PASSWORD=tu_password_postgres
PORT=10000
```

**Nota**: Render asigna automáticamente el puerto, pero puedes usar la variable `PORT`.

### Paso 6: Desplegar

1. Haz clic en **Deploy**
2. Render construirá y desplegará automáticamente tu aplicación
3. Una vez completado, tendrás una URL pública para tu aplicación

## 🗂️ Estructura del Proyecto

```
src/
├── main/
│   ├── java/com/cibertec/proyecto/
│   │   ├── controller/          # Controladores REST
│   │   │   ├── AdminController.java
│   │   │   ├── AdminDashboardController.java
│   │   │   ├── CitaController.java
│   │   │   ├── MedicoController.java
│   │   │   └── PacienteController.java
│   │   ├── entity/             # Entidades JPA
│   │   │   ├── Usuario.java
│   │   │   ├── Medico.java
│   │   │   ├── Paciente.java
│   │   │   ├── Cita.java
│   │   │   └── HistorialMedico.java
│   │   ├── repository/         # Repositorios de datos
│   │   ├── service/           # Lógica de negocio
│   │   └── AdministradorApplication.java
│   └── resources/
│       ├── templates/         # Plantillas Thymeleaf
│       ├── static/           # CSS, JS, imágenes
│       ├── application.properties
│       └── application-prod.properties
└── test/                     # Tests unitarios
```

## 🔧 Configuración de Entornos

### Desarrollo (application.properties)
- Base de datos: MySQL local
- Puerto: 8081
- Logs: Habilitados
- DevTools: Habilitado

### Producción (application-prod.properties)
- Base de datos: PostgreSQL (Render)
- Puerto: Variable de entorno
- Logs: Optimizados
- DevTools: Deshabilitado
- Cache: Habilitado

## 📊 Funcionalidades Principales

### 👨‍💼 Administrador
- ✅ Gestión completa de usuarios
- ✅ Configuración de consultorios
- ✅ Administración de especialidades
- ✅ Reportes y estadísticas
- ✅ Configuración del sistema

### 👩‍💼 Secretario
- ✅ Registro de pacientes
- ✅ Programación de citas
- ✅ Gestión de horarios
- ✅ Consulta de disponibilidad

### 👨‍⚕️ Médico
- ✅ Gestión de citas asignadas
- ✅ Historiales médicos
- ✅ Registro de consultas
- ✅ Gestión de horarios

## 🚨 Solución de Problemas

### Error de Conexión a Base de Datos
1. Verifica las variables de entorno
2. Asegúrate de que la base de datos PostgreSQL esté activa
3. Revisa los logs de Render para más detalles

### Error de Build en Render
1. Verifica que el `pom.xml` esté correcto
2. Asegúrate de que Java 17 esté configurado
3. Revisa que no haya errores de compilación

### Aplicación no Inicia
1. Verifica el comando de inicio
2. Revisa las variables de entorno
3. Consulta los logs de la aplicación

## 🤝 Contribución

1. Fork el proyecto
2. Crea una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abre un Pull Request

## 📝 Licencia

Este proyecto está bajo la Licencia MIT - ver el archivo [LICENSE.md](LICENSE.md) para detalles.

## 👥 Autores

- **Cristian Mamani** - *Desarrollo Principal* - [@Crismandev](https://github.com/Crismandev)

## 🙏 Agradecimientos

- Spring Boot Community
- Render Platform
- Bootstrap Team
- Thymeleaf Team