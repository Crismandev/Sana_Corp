@echo off
REM ===============================================
REM SCRIPT DE DEPLOYMENT A GITHUB - SANA CORP
REM ===============================================

echo.
echo ===============================================
echo    SANA CORP - DEPLOYMENT A GITHUB
echo    Repositorio: https://github.com/Crismandev/Sana_Corp
echo ===============================================
echo.

REM Verificar si Git está instalado
git --version >nul 2>&1
if %errorlevel% neq 0 (
    echo ERROR: Git no está instalado o no está en el PATH
    echo Por favor instale Git desde: https://git-scm.com/
    pause
    exit /b 1
)

echo [1/6] Verificando estado del repositorio...

REM Verificar si ya es un repositorio Git
if not exist ".git" (
    echo Inicializando repositorio Git...
    git init
    git remote add origin https://github.com/Crismandev/Sana_Corp.git
) else (
    echo Repositorio Git ya existe.
)

echo.
echo [2/6] Compilando proyecto...
call mvnw.cmd clean package -DskipTests

if %errorlevel% neq 0 (
    echo ERROR: Falló la compilación del proyecto
    pause
    exit /b 1
)

echo.
echo [3/6] Preparando archivos para commit...
git add .

echo.
echo [4/6] Verificando cambios...
git status

echo.
echo [5/6] Creando commit...
git commit -m "🚀 Deploy: Sana Corp Hospital System

✅ Spring Boot 3.5.7 application ready for production
✅ MySQL database integration configured
✅ User authentication and authorization system
✅ Complete hospital management features
✅ Docker containerization support
✅ Railway/Render deployment configuration
✅ Comprehensive documentation included

Features:
- 👥 User management (Admin, Secretary, Doctor roles)
- 🏥 Consultorio and specialty management
- 📅 Medical appointment system
- ⏰ Doctor schedule management
- 👤 Patient registration and management
- 🔐 Secure authentication system

Tech Stack:
- Spring Boot 3.5.7
- Spring Security
- Spring Data JPA
- MySQL 8.0
- Thymeleaf
- Bootstrap 5
- Maven

Deployment Ready:
- Railway: nixpacks.toml + railway.json
- Docker: Dockerfile + docker-compose.yml
- Render: Build and start commands configured
- Heroku: Java buildpack compatible

🌐 Ready for cloud deployment!"

if %errorlevel% neq 0 (
    echo No hay cambios para commitear o error en commit.
)

echo.
echo [6/6] Subiendo a GitHub...
echo Pushing to: https://github.com/Crismandev/Sana_Corp.git

git push -u origin main

if %errorlevel% neq 0 (
    echo.
    echo ⚠️  Si es la primera vez, puede que necesites autenticarte con GitHub
    echo.
    echo Opciones:
    echo 1. Usar GitHub CLI: gh auth login
    echo 2. Usar token personal: https://github.com/settings/tokens
    echo 3. Configurar SSH: https://docs.github.com/en/authentication/connecting-to-github-with-ssh
    echo.
    echo Intentando pull primero por si hay conflictos...
    git pull origin main --allow-unrelated-histories
    echo.
    echo Intentando push nuevamente...
    git push origin main
)

echo.
echo ===============================================
echo ✅ DEPLOYMENT COMPLETADO
echo ===============================================
echo.
echo 📁 Repositorio: https://github.com/Crismandev/Sana_Corp
echo 🚀 Listo para deployment en:
echo    • Railway: https://railway.app
echo    • Render: https://render.com  
echo    • Heroku: https://heroku.com
echo.
echo 📋 Próximos pasos:
echo 1. Ir a Railway/Render/Heroku
echo 2. Conectar repositorio GitHub
echo 3. Configurar variables de entorno
echo 4. Agregar base de datos MySQL
echo 5. Deploy automático
echo.
echo 📖 Ver documentación completa en:
echo    • README.md
echo    • DEPLOYMENT_GUIDE.md
echo    • GITHUB_SETUP.md
echo.

pause