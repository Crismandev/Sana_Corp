# 🏥 Sana Corp - Sistema Hospitalario

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.7-brightgreen)
![Java](https://img.shields.io/badge/Java-17-orange)
![MySQL](https://img.shields.io/badge/MySQL-8.0-blue)
![License](https://img.shields.io/badge/License-MIT-yellow)

## 📋 Descripción

**Sana Corp** es un sistema integral de gestión hospitalaria desarrollado con Spring Boot que permite administrar de manera eficiente:

- 👥 **Gestión de Usuarios** (Administradores, Secretarios, Médicos)
- 🏥 **Consultorios y Especialidades Médicas**
- 📅 **Sistema de Citas Médicas**
- ⏰ **Horarios de Médicos**
- 👤 **Registro y Gestión de Pacientes**
- 🔐 **Sistema de Autenticación y Autorización**

## 🚀 Demo en Vivo

- **Aplicación:** [https://sana-corp.railway.app](https://sana-corp.railway.app) *(Próximamente)*
- **Repositorio:** [https://github.com/Crismandev/Sana_Corp](https://github.com/Crismandev/Sana_Corp)

## 🛠️ Tecnologías Utilizadas

### Backend
- **Spring Boot 3.5.7** - Framework principal
- **Spring Security** - Autenticación y autorización
- **Spring Data JPA** - Persistencia de datos
- **Hibernate** - ORM
- **MySQL 8.0** - Base de datos
- **Thymeleaf** - Motor de plantillas
- **Maven** - Gestión de dependencias

### Frontend
- **HTML5 & CSS3**
- **Bootstrap 5** - Framework CSS
- **JavaScript** - Interactividad
- **Thymeleaf** - Renderizado del lado del servidor

## 📦 Instalación y Configuración

### Prerrequisitos

- ☕ **Java 17** o superior
- 🗄️ **MySQL 8.0** o superior
- 🔧 **Maven 3.6** o superior (incluido en el proyecto)

### 1. Clonar el Repositorio

```bash
git clone https://github.com/Crismandev/Sana_Corp.git
cd Sana_Corp
```

### 2. Configurar Base de Datos

```sql
-- Crear base de datos
CREATE DATABASE hospital_virtual;

-- Crear usuario (opcional)
CREATE USER 'sanacorp'@'localhost' IDENTIFIED BY 'password';
GRANT ALL PRIVILEGES ON hospital_virtual.* TO 'sanacorp'@'localhost';
FLUSH PRIVILEGES;
```

### 3. Configurar Variables de Entorno

Crear archivo `.env` en la raíz del proyecto:

```env
DB_HOST=localhost
DB_PORT=3306
DB_NAME=hospital_virtual
DB_USERNAME=root
DB_PASSWORD=tu_password
SERVER_PORT=8082
```

### 4. Ejecutar la Aplicación

#### Opción A: Script Automático
```bash
# Windows
./deploy.bat

# Linux/Mac
./deploy.sh
```

#### Opción B: Maven Manual
```bash
# Compilar y ejecutar
./mvnw clean spring-boot:run

# O generar JAR y ejecutar
./mvnw clean package -DskipTests
java -jar target/sana-corp-secretario-0.0.1-SNAPSHOT.jar
```

### 5. Acceder a la Aplicación

- **URL:** http://localhost:8082
- **Usuario Admin:** `admin` / `admin123`
- **Usuario Secretario:** `secretario` / `secretario123`

## 🐳 Deployment con Docker

### Dockerfile

```dockerfile
FROM openjdk:17-jre-slim

WORKDIR /app

COPY target/sana-corp-secretario-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENV SPRING_PROFILES_ACTIVE=prod

CMD ["java", "-jar", "app.jar"]
```

### Docker Compose

```yaml
version: '3.8'
services:
  app:
    build: .
    ports:
      - "8080:8080"
    environment:
      - SPRING_PROFILES_ACTIVE=prod
      - DB_HOST=mysql
      - DB_USERNAME=sanacorp
      - DB_PASSWORD=password
    depends_on:
      - mysql

  mysql:
    image: mysql:8.0
    environment:
      - MYSQL_ROOT_PASSWORD=rootpassword
      - MYSQL_DATABASE=hospital_virtual
      - MYSQL_USER=sanacorp
      - MYSQL_PASSWORD=password
    ports:
      - "3306:3306"
    volumes:
      - mysql_data:/var/lib/mysql

volumes:
  mysql_data:
```

## ☁️ Deployment en la Nube

### Railway (Recomendado)

1. **Conectar GitHub:**
   - Ir a [Railway.app](https://railway.app)
   - Conectar con GitHub
   - Seleccionar repositorio `Sana_Corp`

2. **Configurar Variables:**
   ```
   SPRING_PROFILES_ACTIVE=prod
   DB_HOST=${{MySQL.MYSQL_HOST}}
   DB_PORT=${{MySQL.MYSQL_PORT}}
   DB_NAME=${{MySQL.MYSQL_DATABASE}}
   DB_USERNAME=${{MySQL.MYSQL_USER}}
   DB_PASSWORD=${{MySQL.MYSQL_PASSWORD}}
   ```

3. **Agregar MySQL:**
   - Agregar servicio MySQL
   - Railway configurará automáticamente las variables

### Render

1. **Crear Web Service:**
   - Conectar repositorio GitHub
   - Build Command: `./mvnw clean package -DskipTests`
   - Start Command: `java -jar target/sana-corp-secretario-0.0.1-SNAPSHOT.jar`

2. **Agregar PostgreSQL:**
   - Crear PostgreSQL database
   - Configurar variables de entorno

### Heroku

```bash
# Instalar Heroku CLI y hacer login
heroku login

# Crear aplicación
heroku create sana-corp-app

# Agregar MySQL
heroku addons:create jawsdb:kitefin

# Configurar variables
heroku config:set SPRING_PROFILES_ACTIVE=prod

# Deploy
git push heroku main
```

## 🗂️ Estructura del Proyecto

```
src/
├── main/
│   ├── java/com/sanacorp/
│   │   ├── controllers/     # Controladores REST
│   │   ├── models/         # Entidades JPA
│   │   ├── repositories/   # Repositorios de datos
│   │   ├── services/       # Lógica de negocio
│   │   ├── security/       # Configuración de seguridad
│   │   └── SanaCorpApplication.java
│   └── resources/
│       ├── templates/      # Plantillas Thymeleaf
│       ├── static/         # CSS, JS, imágenes
│       ├── application.properties
│       └── application-prod.properties
└── test/                   # Tests unitarios
```

## 🔐 Usuarios por Defecto

| Rol | Usuario | Contraseña | Permisos |
|-----|---------|------------|----------|
| Administrador | `admin` | `admin123` | Acceso completo |
| Secretario | `secretario` | `secretario123` | Gestión de citas y pacientes |

## 📊 Funcionalidades

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
- ✅ Reportes básicos

### 👨‍⚕️ Médico
- ✅ Consulta de citas asignadas
- ✅ Gestión de horarios personales
- ✅ Historial de pacientes
- ✅ Actualización de consultas

## 🧪 Testing

```bash
# Ejecutar todos los tests
./mvnw test

# Ejecutar tests con coverage
./mvnw test jacoco:report

# Tests de integración
./mvnw verify
```

## 📈 Monitoreo

### Health Checks
- **Health:** `/actuator/health`
- **Info:** `/actuator/info`
- **Metrics:** `/actuator/metrics`

### Logs
```bash
# Ver logs en tiempo real
tail -f logs/application.log

# Logs por nivel
grep "ERROR" logs/application.log
```

## 🤝 Contribución

1. Fork el proyecto
2. Crear rama feature (`git checkout -b feature/AmazingFeature`)
3. Commit cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abrir Pull Request

## 📝 Changelog

### v0.0.1-SNAPSHOT (Actual)
- ✅ Sistema de autenticación completo
- ✅ Gestión de usuarios y roles
- ✅ CRUD de pacientes y médicos
- ✅ Sistema de citas médicas
- ✅ Gestión de horarios
- ✅ Interfaz web responsive

### Próximas Versiones
- 🔄 API REST completa
- 🔄 Notificaciones por email
- 🔄 Reportes avanzados
- 🔄 Integración con sistemas externos

## 🐛 Problemas Conocidos

- [ ] Optimización de consultas JPA
- [ ] Mejora en validaciones frontend
- [ ] Implementación de cache

## 📞 Soporte

- **Issues:** [GitHub Issues](https://github.com/Crismandev/Sana_Corp/issues)
- **Documentación:** [Wiki del Proyecto](https://github.com/Crismandev/Sana_Corp/wiki)
- **Email:** cristhian.mantilla@example.com

## 📄 Licencia

Este proyecto está bajo la Licencia MIT - ver el archivo [LICENSE](LICENSE) para más detalles.

## 👨‍💻 Autor

**Cristhian Mantilla** - *Desarrollador Full Stack en Proceso*
**Misael Lara**
**Wilder Tudela**
**Kevin **

- GitHub: [@Crismandev](https://github.com/Crismandev)
- LinkedIn: [Cristhian Mantilla](https://www.linkedin.com/in/crismandev/)

---

⭐ **¡Dale una estrella si te gusta el proyecto!** ⭐
