package org.example.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO para respuestas de error estandarizadas.
 * Se utiliza en el GlobalExceptionHandler.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Respuesta de error estándar")
public class ErrorResponse {

    @Schema(description = "Timestamp del error", example = "2025-01-07T15:30:45")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;

    @Schema(description = "Código de estado HTTP", example = "400")
    private int status;

    @Schema(description = "Tipo de error", example = "Bad Request")
    private String error;

    @Schema(description = "Mensaje descriptivo del error", example = "Invalid DNA sequence")
    private String message;

    @Schema(description = "Ruta del endpoint donde ocurrió el error", example = "/mutant")
    private String path;

    /**
     * Constructor simplificado sin path.
     */
    public ErrorResponse(LocalDateTime timestamp, int status, String error, String message) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.message = message;
    }
}