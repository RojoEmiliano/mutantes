# ========================================
# ETAPA 1: BUILD (Compilación)
# ========================================
# Usar una imagen de Temurin con el JDK completo para compilar
FROM eclipse-temurin:21-jdk-alpine as build

# Copiar TODO el código fuente del proyecto al contenedor
COPY . .

# Dar permisos de ejecución al script gradlew (Gradle Wrapper)
RUN chmod +x ./gradlew

# Ejecutar Gradle para compilar y generar el JAR ejecutable
RUN ./gradlew bootJar --no-daemon

# ========================================
# ETAPA 2: RUNTIME (Ejecución)
# ========================================
# Usar una imagen de Temurin con solo el JRE para ejecutar (más ligera)
FROM eclipse-temurin:21-jre-alpine

# Documentar que la aplicación escucha en el puerto 8080
EXPOSE 8080

# Copiar el JAR generado en la ETAPA 1 (build) a la imagen final
COPY --from=build ./build/libs/inicial1-0.0.1-SNAPSHOT.jar ./app.jar

# Comando que se ejecuta cuando el contenedor inicia
ENTRYPOINT ["java", "-jar", "app.jar"]