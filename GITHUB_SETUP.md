# 🚀 Guía de Setup GitHub y Deployment

## 📋 Pasos para Subir al Repositorio GitHub

### 1. Inicializar Git en el Proyecto

```bash
# Navegar al directorio del proyecto
cd "d:\WS TRAE\PROYECTO LP2\administrador (2)\administrador"

# Inicializar repositorio Git
git init

# Agregar remote del repositorio Sana_Corp
git remote add origin https://github.com/Crismandev/Sana_Corp.git
```

### 2. Preparar Archivos para Commit

```bash
# Verificar estado
git status

# Agregar todos los archivos
git add .

# Crear commit inicial
git commit -m "🎉 Initial commit: Sana Corp Hospital System

✅ Spring Boot 3.5.7 application
✅ MySQL database integration
✅ User authentication system
✅ Hospital management features
✅ Docker configuration
✅ Railway deployment ready"
```

### 3. Subir al Repositorio

```bash
# Verificar remote
git remote -v

# Subir al repositorio (primera vez)
git push -u origin main

# O si ya existe contenido en GitHub
git pull origin main --allow-unrelated-histories
git push origin main
```

## ☁️ Deployment en Railway

### Paso 1: Conectar GitHub a Railway

1. **Ir a Railway:** https://railway.app
2. **Login/Signup** con tu cuenta GitHub
3. **New Project** → **Deploy from GitHub repo**
4. **Seleccionar:** `Crismandev/Sana_Corp`
5. **Deploy** automáticamente

### Paso 2: Configurar Variables de Entorno

En Railway Dashboard → Variables:

```env
# Perfil de Spring Boot
SPRING_PROFILES_ACTIVE=prod

# Puerto (Railway lo asigna automáticamente)
PORT=${{RAILWAY_PUBLIC_PORT}}

# Base de datos (Railway MySQL Plugin)
DB_HOST=${{MySQL.MYSQL_HOST}}
DB_PORT=${{MySQL.MYSQL_PORT}}
DB_NAME=${{MySQL.MYSQL_DATABASE}}
DB_USERNAME=${{MySQL.MYSQL_USER}}
DB_PASSWORD=${{MySQL.MYSQL_PASSWORD}}

# Configuración adicional
JAVA_TOOL_OPTIONS=-Xmx512m -Xms256m
```

### Paso 3: Agregar MySQL Database

1. **En Railway Dashboard:** Add Plugin
2. **Seleccionar:** MySQL
3. **Railway automáticamente:** Configura las variables de conexión
4. **Verificar:** Las variables `${{MySQL.*}}` están disponibles

### Paso 4: Configurar Dominio (Opcional)

1. **Settings** → **Domains**
2. **Generate Domain** o **Custom Domain**
3. **Ejemplo:** `sana-corp.railway.app`

## 🔧 Deployment en Render

### Paso 1: Crear Web Service

1. **Ir a Render:** https://render.com
2. **New** → **Web Service**
3. **Connect GitHub:** Seleccionar `Sana_Corp`

### Paso 2: Configuración del Servicio

```yaml
# Build Command
./mvnw clean package -DskipTests

# Start Command  
java -jar target/sana-corp-secretario-0.0.1-SNAPSHOT.jar

# Environment
SPRING_PROFILES_ACTIVE=prod
```

### Paso 3: Agregar PostgreSQL

1. **Dashboard** → **New** → **PostgreSQL**
2. **Copiar** Database URL
3. **Configurar** en Web Service:

```env
DATABASE_URL=postgresql://user:password@host:port/database
SPRING_PROFILES_ACTIVE=prod
```

## 🐳 Deployment con Docker

### Opción 1: Docker Hub

```bash
# Build imagen
docker build -t crismandev/sana-corp:latest .

# Push a Docker Hub
docker push crismandev/sana-corp:latest

# Run localmente
docker run -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e DB_HOST=host.docker.internal \
  -e DB_USERNAME=root \
  -e DB_PASSWORD=admin \
  crismandev/sana-corp:latest
```

### Opción 2: Docker Compose

```bash
# Ejecutar con Docker Compose
docker-compose up -d

# Ver logs
docker-compose logs -f

# Parar servicios
docker-compose down
```

## 🌐 Deployment en Heroku

### Paso 1: Preparar Heroku

```bash
# Instalar Heroku CLI
# Windows: https://devcenter.heroku.com/articles/heroku-cli

# Login
heroku login

# Crear aplicación
heroku create sana-corp-hospital
```

### Paso 2: Configurar Buildpack

```bash
# Configurar Java buildpack
heroku buildpacks:set heroku/java

# Verificar
heroku buildpacks
```

### Paso 3: Agregar Base de Datos

```bash
# Agregar JawsDB MySQL
heroku addons:create jawsdb:kitefin

# Ver configuración
heroku config
```

### Paso 4: Configurar Variables

```bash
# Configurar perfil de producción
heroku config:set SPRING_PROFILES_ACTIVE=prod

# Configurar puerto
heroku config:set SERVER_PORT=$PORT
```

### Paso 5: Deploy

```bash
# Agregar remote de Heroku
heroku git:remote -a sana-corp-hospital

# Deploy
git push heroku main

# Ver logs
heroku logs --tail
```

## 📊 Monitoreo Post-Deployment

### Health Checks

```bash
# Verificar salud de la aplicación
curl https://tu-app.railway.app/actuator/health

# Información de la aplicación
curl https://tu-app.railway.app/actuator/info
```

### Logs

```bash
# Railway
railway logs

# Render
# Ver en Dashboard → Logs

# Heroku
heroku logs --tail
```

## 🔒 Configuración de Seguridad

### Variables de Entorno Sensibles

**❌ NUNCA commitear:**
- Contraseñas de base de datos
- API keys
- Tokens de autenticación
- Certificados

**✅ Usar variables de entorno:**
```env
DB_PASSWORD=${{MYSQL_PASSWORD}}
JWT_SECRET=${{JWT_SECRET}}
API_KEY=${{EXTERNAL_API_KEY}}
```

### SSL/HTTPS

La mayoría de plataformas (Railway, Render, Heroku) proporcionan HTTPS automáticamente.

## 🚨 Troubleshooting

### Error: Puerto en Uso
```bash
# Verificar procesos
netstat -ano | findstr :8080
taskkill /PID <PID> /F
```

### Error: Base de Datos
```bash
# Verificar conexión
mysql -h <host> -u <user> -p

# Verificar variables
echo $DB_HOST
echo $DB_USERNAME
```

### Error: Memoria Insuficiente
```bash
# Aumentar memoria heap
export JAVA_OPTS="-Xmx1g -Xms512m"
```

### Error: Build Fallido
```bash
# Limpiar y rebuild
./mvnw clean
./mvnw package -DskipTests
```

## 📞 Soporte

- **GitHub Issues:** https://github.com/Crismandev/Sana_Corp/issues
- **Railway Docs:** https://docs.railway.app
- **Render Docs:** https://render.com/docs
- **Heroku Docs:** https://devcenter.heroku.com

---

🎉 **¡Tu aplicación está lista para el mundo!** 🌍