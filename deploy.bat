@echo off
REM ===============================================
REM SCRIPT DE DESPLIEGUE - SANA CORP
REM ===============================================

echo.
echo ===============================================
echo    SANA CORP - SISTEMA HOSPITALARIO
echo    Script de Despliegue Automatizado
echo ===============================================
echo.

REM Verificar si Java está instalado
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ERROR: Java no está instalado o no está en el PATH
    echo Por favor instale Java 17 o superior
    pause
    exit /b 1
)

echo [1/4] Limpiando proyecto anterior...
call mvnw.cmd clean

echo.
echo [2/4] Compilando y empaquetando aplicación...
call mvnw.cmd package -DskipTests

if %errorlevel% neq 0 (
    echo ERROR: Falló la compilación del proyecto
    pause
    exit /b 1
)

echo.
echo [3/4] Verificando JAR generado...
if not exist "target\sana-corp-secretario-0.0.1-SNAPSHOT.jar" (
    echo ERROR: No se encontró el archivo JAR generado
    pause
    exit /b 1
)

echo.
echo [4/4] JAR generado exitosamente!
echo Ubicación: target\sana-corp-secretario-0.0.1-SNAPSHOT.jar
echo.

echo ===============================================
echo OPCIONES DE EJECUCIÓN:
echo ===============================================
echo.
echo 1. Desarrollo (perfil por defecto):
echo    java -jar target\sana-corp-secretario-0.0.1-SNAPSHOT.jar
echo.
echo 2. Producción:
echo    java -jar -Dspring.profiles.active=prod target\sana-corp-secretario-0.0.1-SNAPSHOT.jar
echo.
echo 3. Con variables de entorno personalizadas:
echo    set DB_USERNAME=tu_usuario
echo    set DB_PASSWORD=tu_password
echo    set SERVER_PORT=8080
echo    java -jar -Dspring.profiles.active=prod target\sana-corp-secretario-0.0.1-SNAPSHOT.jar
echo.

echo ¿Desea ejecutar la aplicación ahora? (S/N)
set /p choice=
if /i "%choice%"=="S" (
    echo.
    echo Iniciando aplicación en modo desarrollo...
    java -jar target\sana-corp-secretario-0.0.1-SNAPSHOT.jar
) else (
    echo.
    echo Despliegue completado. Use los comandos anteriores para ejecutar la aplicación.
)

pause