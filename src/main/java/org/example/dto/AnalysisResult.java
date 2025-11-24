package org.example.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Resultado del análisis de ADN")
public class AnalysisResult {
    @Schema(description = "El resultado del análisis", example = "mutant")
    private String result;
}