package org.example.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para la respuesta del endpoint GET /stats.
 * Contiene estadísticas de verificaciones de ADN.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Estadísticas de verificaciones de ADN (mutantes vs humanos)")
public class StatsResponse {

    @Schema(
            description = "Cantidad total de ADN mutantes verificados",
            example = "40"
    )
    @JsonProperty("count_mutant_dna")
    private long countMutantDna;

    @Schema(
            description = "Cantidad total de ADN humanos (no mutantes) verificados",
            example = "100"
    )
    @JsonProperty("count_human_dna")
    private long countHumanDna;

    @Schema(
            description = "Ratio de mutantes sobre humanos (count_mutant_dna / count_human_dna). " +
                    "Si no hay humanos, retorna el número de mutantes o 0.",
            example = "0.4"
    )
    @JsonProperty("ratio")
    private double ratio;
}