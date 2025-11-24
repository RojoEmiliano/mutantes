🧬 Mutant Detector API

API REST para detectar si un humano es mutante basándose en su secuencia de ADN.
Java 21 SpringBoot 3.2.0 Gradle 8.8
🌐 Demo en Vivo

Base URL: https://mutantes-api-v2.onrender.com

Swagger UI: [https://mutantes-api-v2.onrender.com/swagger-ui.html](https://mutantes-nhg2.onrender.com/swagger-ui/index.html#/Mutant%20Detector/checkMutant)

⚠️ Puede demorar 30–60 segundos en activarse (plan free de Render).

📝 Descripción

La API identifica si una secuencia de ADN pertenece a un mutante detectando más de una secuencia de cuatro letras iguales consecutivas (A, T, C, G) en sentido:

Horizontal (→)

Vertical (↓)

Diagonal (↘ ↙)

✨ Características

Algoritmo optimizado O(N²)

Validación completa de ADN (NxN, caracteres válidos)

Persistencia con H2 + hash SHA-256

Estadísticas de consultas

Endpoints documentados con Swagger

Tests integrales y reporte de cobertura

Deploy en Render + Dockerfile listo

📦 Requisitos

Java 21

Gradle 8.8

Git (opcional)

🚀 Instalación y Ejecución
git clone https://github.com/RojoEmiliano/mutantes.git
cd mutantes
./gradlew build
./gradlew bootRun


App disponible en: http://localhost:8080

Tests
./gradlew test

🔌 Endpoints
POST /mutant
{
  "dna": ["ATGCGA","CAGTGC","TTATGT","AGAAGG","CCCCTA","TCACTG"]
}


200 OK

{ "result": "mutant" }


403 Forbidden

{ "result": "human" }


400 Bad Request → ADN inválido

GET /stats
{
  "count_mutant_dna": 40,
  "count_human_dna": 100,
  "ratio": 0.4
}

📚 Swagger

Producción: https://mutantes-api-v2.onrender.com/swagger-ui.html

Local: http://localhost:8080/swagger-ui.html

Incluye modelos, ejemplos y botón Try it out.

💾 Base de Datos H2

URL: http://localhost:8080/h2-console

JDBC: jdbc:h2:mem:testdb
Usuario: sa — Password: (vacío)

🧪 Tests

Detección de mutantes en todas direcciones

Casos grandes (1000×1000)

Deduplicación por hash

Cálculo de estadísticas

Validaciones y controladores

Cobertura total aproximada: 58%
(Capa de servicio >90%)

🏗️ Arquitectura
Controller → Service → Repository → H2


Patrones: DTO, Service Layer, Repository, Custom Validators, Exception Handler Global.

📁 Estructura del Proyecto
src/main/java/org/example/
 ├── controller/
 ├── service/
 ├── repository/
 ├── validation/
 ├── exception/
 ├── dto/
 ├── entity/
 └── MutantDetectorApplication.java

🛠️ Tecnologías

Java 21

Spring Boot 3

Spring Web / JPA

H2 Database

Lombok

Swagger OpenAPI

JUnit 5 / Mockito

Docker

🐳 Docker (Deploy en Render)
FROM eclipse-temurin:21-jdk-alpine as build
COPY . .
RUN chmod +x ./gradlew
RUN ./gradlew bootJar --no-daemon

FROM eclipse-temurin:21-jre-alpine
EXPOSE 8080
COPY --from=build ./build/libs/*.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]

👤 Autor

Rojo Emiliano
GitHub: https://github.com/RojoEmiliano

Repositorio: https://github.com/RojoEmiliano/mutantes
