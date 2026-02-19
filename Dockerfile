# Usar imagen oficial de Java 17
FROM eclipse-temurin:17-jdk-alpine

# Directorio de trabajo dentro del contenedor
WORKDIR /app


COPY . .

# Dar permisos al wrapper si existe
RUN chmod +x mvnw

# Construir el proyecto
RUN ./mvnw clean package -DskipTests

# Exponer puerto
EXPOSE 8080

# Ejecutar el jar
ENTRYPOINT ["java", "-jar", "target/MediTrackApp-0.0.1-SNAPSHOT.jar"]