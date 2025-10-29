# ===============================================
# DOCKERFILE - SANA CORP SPRING BOOT
# ===============================================

# Usar imagen base de OpenJDK 17
FROM openjdk:17-jre-slim

# Información del mantenedor
LABEL maintainer="cristhian.mantilla@example.com"
LABEL description="Sana Corp - Sistema Hospitalario"
LABEL version="0.0.1-SNAPSHOT"

# Crear directorio de trabajo
WORKDIR /app

# Crear usuario no-root para seguridad
RUN groupadd -r sanacorp && useradd -r -g sanacorp sanacorp

# Copiar el JAR de la aplicación
COPY target/sana-corp-secretario-0.0.1-SNAPSHOT.jar app.jar

# Cambiar propietario del archivo
RUN chown sanacorp:sanacorp app.jar

# Cambiar al usuario no-root
USER sanacorp

# Exponer el puerto de la aplicación
EXPOSE 8080

# Variables de entorno por defecto
ENV SPRING_PROFILES_ACTIVE=prod
ENV JAVA_OPTS="-Xmx512m -Xms256m"

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

# Comando para ejecutar la aplicación
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]