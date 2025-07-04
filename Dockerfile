# Etapa 1: Compilación
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Copiamos el proyecto completo
COPY . .

# Compilamos el proyecto y generamos el .jar
RUN mvn clean package -DskipTests

# Etapa 2: Ejecución
FROM eclipse-temurin:21-jdk
WORKDIR /app

# Copiamos el .jar desde la etapa de build
COPY --from=build /app/target/*.jar app.jar

# Exponemos el puerto que usará la app (Render define PORT)
ENV PORT=8080
EXPOSE ${PORT}

# Ejecutamos la aplicación
CMD ["java", "-jar", "app.jar"]