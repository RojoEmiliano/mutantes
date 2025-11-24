package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.AnalysisResult;
import org.example.dto.DnaRequest;
import org.example.dto.ErrorResponse;
import org.example.dto.StatsResponse;
import org.example.service.MutantService;
import org.example.service.StatsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para endpoints de detección de mutantes.
 *
 * Endpoints:
 * - POST /mutant: Verifica si un ADN es mutante
 * - GET /stats: Obtiene estadísticas de verificaciones
 *
 * Documentado con Swagger/OpenAPI para pruebas interactivas.
 */
@RestController
@RequiredArgsConstructor
@Validated
@Tag(name = "Mutant Detector", description = "API para detección de mutantes mediante análisis de ADN")
public class MutantController {

    private final MutantService mutantService;
    private final StatsService statsService;

    /**
     * POST /mutant
     *
     * Verifica si una secuencia de ADN corresponde a un mutante.
     *
     * Request body:
     * {
     *   "dna": ["ATGCGA","CAGTGC","TTATGT","AGAAGG","CCCCTA","TCACTG"]
     * }
     *
     * Respuestas:
     * - 200 OK: Es mutante (con AnalysisResult)
     * - 403 Forbidden: No es mutante (con AnalysisResult)
     * - 400 Bad Request: DNA inválido (con ErrorResponse)
     *
     * @param request DnaRequest con la secuencia de ADN
     * @return ResponseEntity con AnalysisResult
     */
    @PostMapping("/mutant")
    @Operation(
            summary = "Verificar si un ADN es mutante",
            description = "Recibe una secuencia de ADN y determina si corresponde a un mutante. " +
                    "Un mutante tiene más de una secuencia de 4 letras iguales en cualquier dirección " +
                    "(horizontal, vertical o diagonal)."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "El ADN corresponde a un mutante",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AnalysisResult.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "El ADN corresponde a un humano (no mutante)",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AnalysisResult.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Secuencia de ADN inválida",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    public ResponseEntity<AnalysisResult> checkMutant(@Valid @RequestBody DnaRequest request) {
        if (mutantService.analyzeDna(request.getDna())) {
            return ResponseEntity.ok(new AnalysisResult("mutant"));
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new AnalysisResult("human"));
        }
    }

    /**
     * GET /stats
     *
     * Obtiene estadísticas de todas las verificaciones de ADN realizadas.
     *
     * Respuesta:
     * {
     *   "count_mutant_dna": 40,
     *   "count_human_dna": 100,
     *   "ratio": 0.4
     * }
     *
     * @return StatsResponse con las estadísticas
     */
    @GetMapping("/stats")
    @Operation(
            summary = "Obtener estadísticas de verificaciones",
            description = "Retorna estadísticas de todas las verificaciones de ADN: " +
                    "cantidad de mutantes, cantidad de humanos y el ratio entre ambos."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Estadísticas obtenidas exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = StatsResponse.class)
                    )
            )
    })
    public ResponseEntity<StatsResponse> getStats() {
        StatsResponse stats = statsService.getStats();
        return ResponseEntity.ok(stats);
    }
}