# Etapa 1: Construcción (Build)
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src

# TRUCO DEVOPS: Eliminar el BOM (Byte Order Mark) de todos los archivos .java
# y corregir los saltos de línea de Windows a Linux antes de compilar
RUN apk add --no-cache dos2unix \
    && find src -name "*.java" -type f -exec sed -i '1s/^\xEF\xBB\xBF//' {} + \
    && find src -name "*.java" -type f -exec dos2unix {} +

# Ahora Maven compilará código 100% limpio
RUN mvn clean package -DskipTests

# Etapa 2: Ejecución (Runtime)
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]