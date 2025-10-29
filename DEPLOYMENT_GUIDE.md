# 🏥 SANA CORP - Guía de Despliegue

## 📋 Resumen del Proyecto

**Sana Corp** es un sistema hospitalario desarrollado en Spring Boot que gestiona:
- 👥 Usuarios (Administradores, Secretarios, Médicos)
- 🏥 Consultorios y Especialidades
- 📅 Citas Médicas
- 👨‍⚕️ Horarios de Médicos
- 👤 Gestión de Pacientes

## 🚀 Opciones de Despliegue

### 1. Despliegue Local (Desarrollo)

#### Prerrequisitos:
- ☕ Java 17 o superior
- 🗄️ MySQL Server 8.0+
- 🔧 Maven (incluido con el proyecto)

#### Pasos:
```bash
# 1. Clonar/descargar el proyecto
cd "d:\WS TRAE\PROYECTO LP2\administrador (2)\administrador"

# 2. Ejecutar script de despliegue automático
deploy.bat

# O manualmente:
./mvnw.cmd clean package -DskipTests
java -jar target/sana-corp-secretario-0.0.1-SNAPSHOT.jar
```

**URL de acceso:** http://localhost:8082

### 2. Despliegue en Producción

#### Configuración de Base de Datos:
```bash
# Configurar variables de entorno
set DB_USERNAME=tu_usuario_mysql
set DB_PASSWORD=tu_password_mysql
set SERVER_PORT=8080

# Ejecutar en modo producción
java -jar -Dspring.profiles.active=prod target/sana-corp-secretario-0.0.1-SNAPSHOT.jar
```

#### Configuración MySQL para Producción:
```sql
-- Crear base de datos de producción
CREATE DATABASE hospital_virtual_prod;

-- Crear usuario específico (recomendado)
CREATE USER 'sanacorp_user'@'%' IDENTIFIED BY 'password_seguro';
GRANT ALL PRIVILEGES ON hospital_virtual_prod.* TO 'sanacorp_user'@'%';
FLUSH PRIVILEGES;
```

### 3. Despliegue en Servidor Linux

#### Crear servicio systemd:
```bash
# Crear archivo de servicio
sudo nano /etc/systemd/system/sanacorp.service
```

```ini
[Unit]
Description=Sana Corp Hospital System
After=network.target

[Service]
Type=simple
User=sanacorp
ExecStart=/usr/bin/java -jar -Dspring.profiles.active=prod /opt/sanacorp/sana-corp-secretario-0.0.1-SNAPSHOT.jar
Restart=always
RestartSec=10
Environment=DB_USERNAME=sanacorp_user
Environment=DB_PASSWORD=password_seguro
Environment=SERVER_PORT=8080

[Install]
WantedBy=multi-user.target
```

```bash
# Habilitar y iniciar servicio
sudo systemctl enable sanacorp
sudo systemctl start sanacorp
sudo systemctl status sanacorp
```

### 4. Despliegue con Docker

#### Dockerfile:
```dockerfile
FROM openjdk:17-jre-slim

WORKDIR /app

COPY target/sana-corp-secretario-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENV SPRING_PROFILES_ACTIVE=prod

CMD ["java", "-jar", "app.jar"]
```

#### Docker Compose:
```yaml
version: '3.8'
services:
  sanacorp-app:
    build: .
    ports:
      - "8080:8080"
    environment:
      - SPRING_PROFILES_ACTIVE=prod
      - DB_USERNAME=sanacorp_user
      - DB_PASSWORD=password_seguro
    depends_on:
      - mysql

  mysql:
    image: mysql:8.0
    environment:
      - MYSQL_ROOT_PASSWORD=root_password
      - MYSQL_DATABASE=hospital_virtual_prod
      - MYSQL_USER=sanacorp_user
      - MYSQL_PASSWORD=password_seguro
    ports:
      - "3306:3306"
    volumes:
      - mysql_data:/var/lib/mysql

volumes:
  mysql_data:
```

## 🔧 Configuración por Entornos

### Desarrollo (application.properties)
- Puerto: 8082
- Base de datos: hospital_virtual
- Logs: DEBUG habilitado
- Thymeleaf cache: deshabilitado

### Producción (application-prod.properties)
- Puerto: 8080 (configurable)
- Base de datos: hospital_virtual_prod
- Logs: INFO/WARN únicamente
- Thymeleaf cache: habilitado
- Compresión HTTP: habilitada

## 🔐 Configuración de Seguridad

### Variables de Entorno Recomendadas:
```bash
DB_USERNAME=tu_usuario_mysql
DB_PASSWORD=tu_password_seguro
SERVER_PORT=8080
SPRING_PROFILES_ACTIVE=prod
```

### Usuarios por Defecto:
- **Administrador:** admin / admin123
- **Secretario:** secretario / secretario123

## 📊 Monitoreo y Logs

### Ubicación de Logs:
- Desarrollo: Consola
- Producción: `/var/log/sanacorp/` (Linux) o `logs/` (Windows)

### Endpoints de Salud:
- **Health Check:** http://localhost:8080/actuator/health
- **Info:** http://localhost:8080/actuator/info

## 🚨 Solución de Problemas

### Error de Conexión a Base de Datos:
```bash
# Verificar conectividad
mysql -h localhost -u root -p

# Verificar configuración
java -jar app.jar --debug
```

### Puerto en Uso:
```bash
# Windows
netstat -ano | findstr :8080

# Linux
lsof -i :8080
```

### Memoria Insuficiente:
```bash
# Aumentar memoria heap
java -Xmx2g -jar target/sana-corp-secretario-0.0.1-SNAPSHOT.jar
```

## 📝 Notas Importantes

1. **Base de Datos:** Asegúrate de que MySQL esté ejecutándose antes de iniciar la aplicación
2. **Puertos:** Verifica que los puertos 8080/8082 estén disponibles
3. **Java:** Requiere Java 17 o superior
4. **Memoria:** Mínimo 512MB RAM recomendado
5. **Backup:** Realiza respaldos regulares de la base de datos

## 🆘 Soporte

Para problemas o consultas:
- Revisar logs de la aplicación
- Verificar configuración de base de datos
- Comprobar versión de Java
- Validar puertos disponibles

---
**Desarrollado por:** Equipo Sana Corp  
**Versión:** 0.0.1-SNAPSHOT  
**Fecha:** Octubre 2025