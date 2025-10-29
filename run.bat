@echo off
echo Iniciando aplicacion Sana Corp...
cd /d "D:\WS TRAE\PROYECTO LP2\administrador (2)\administrador"

REM Intentar ejecutar con Maven Wrapper si existe
if exist "mvnw.cmd" (
    echo Usando Maven Wrapper...
    mvnw.cmd spring-boot:run
) else (
    echo Usando Maven del sistema...
    mvn spring-boot:run
)

pause