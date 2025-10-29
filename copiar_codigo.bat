@echo off
chcp 65001 > nul
echo ========================================
echo    COPIANDO CODIGO SPRING BOOT
echo ========================================
echo.

echo Fecha: %date% %time%
echo.

echo === PROYECTO SPRING BOOT - CODIGO COMPLETO === > proyecto_codigo.txt
echo Fecha: %date% %time% >> proyecto_codigo.txt
echo. >> proyecto_codigo.txt

echo Copiando archivos JAVA...
for /r %%i in (*.java) do (
   echo. >> proyecto_codigo.txt
   echo ================================= >> proyecto_codigo.txt
   echo ARCHIVO: %%i >> proyecto_codigo.txt
   echo ================================= >> proyecto_codigo.txt
   type "%%i" >> proyecto_codigo.txt
   echo. >> proyecto_codigo.txt
)

echo Copiando POM.XML...
echo. >> proyecto_codigo.txt
echo ================================= >> proyecto_codigo.txt
echo ARCHIVO: pom.xml >> proyecto_codigo.txt
echo ================================= >> proyecto_codigo.txt
type pom.xml >> proyecto_codigo.txt
echo. >> proyecto_codigo.txt

echo Copiando APPLICATION PROPERTIES...
for /r %%i in (application*.properties) do (
   echo. >> proyecto_codigo.txt
   echo ================================= >> proyecto_codigo.txt
   echo ARCHIVO: %%i >> proyecto_codigo.txt
   echo ================================= >> proyecto_codigo.txt
   type "%%i" >> proyecto_codigo.txt
   echo. >> proyecto_codigo.txt
)

echo Copiando APPLICATION.YML (si existe)...
for /r %%i in (application*.yml) do (
   echo. >> proyecto_codigo.txt
   echo ================================= >> proyecto_codigo.txt
   echo ARCHIVO: %%i >> proyecto_codigo.txt
   echo ================================= >> proyecto_codigo.txt
   type "%%i" >> proyecto_codigo.txt
   echo. >> proyecto_codigo.txt
)

echo.
echo ========================================
echo    PROCESO COMPLETADO
echo ========================================
echo Se creo el archivo: proyecto_codigo.txt
echo con todo el codigo de tu proyecto.
echo.
pause