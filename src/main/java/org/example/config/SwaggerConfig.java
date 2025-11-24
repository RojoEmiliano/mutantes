package org.example.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuración de Swagger/OpenAPI para documentación interactiva de la API.
 *
 * Accesible en:
 * - Swagger UI: http://localhost:8080/swagger-ui.html
 * - OpenAPI JSON: http://localhost:8080/api-docs
 */
@Configuration
public class SwaggerConfig {

    /**
     * Configura la documentación OpenAPI con información del proyecto.
     *
     * @return Objeto OpenAPI configurado
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Mutant Detector API")
                        .version("1.0.0")
                        .description(
                                "API REST para detectar mutantes mediante análisis de secuencias de ADN.\n\n" +
                                        "**Proyecto:** Examen Técnico MercadoLibre\n\n" +
                                        "**Funcionalidades:**\n" +
                                        "- Detección de mutantes (POST /mutant)\n" +
                                        "- Estadísticas de verificaciones (GET /stats)\n" +
                                        "- Persistencia con deduplicación por hash SHA-256\n\n" +
                                        "**Criterio de mutante:**\n" +
                                        "Un ADN es mutante si contiene MÁS DE UNA secuencia de 4 letras iguales " +
                                        "en dirección horizontal, vertical o diagonal."
                        )
                        .contact(new Contact()
                                .name("MercadoLibre Backend Team")
                                .email("backend@mercadolibre.com")
                        )
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")
                        )
                )
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Servidor de desarrollo local"),
                        new Server()
                                .url("https://mutantes-api-v2.onrender.com")
                                .description("Servidor de producción (Render)")
                ));
    }
}


