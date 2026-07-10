# --- Etapa 1: Compilación ---
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
WORKDIR /app

# Copiar el pom y descargar dependencias (Aprovecha la caché de Docker)
COPY pom.xml .
RUN mvn dependency:go-offline

# Copiar el código fuente y compilar el JAR omitiendo los tests
COPY src ./src
RUN mvn clean package -DskipTests

# --- Etapa 2: Imagen de ejecución ---
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copiar SOLO el JAR generado desde la etapa de compilación
COPY --from=build /app/target/*.jar app.jar

# Estandarizamos el puerto interno a 8080 para todos los contenedores
ENV SERVER_PORT=8080
EXPOSE 8080

# Comando de arranque optimizado
ENTRYPOINT ["java", "-jar", "app.jar"]