package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.StatsResponse;
import org.example.repository.DnaRecordRepository;
import org.springframework.stereotype.Service;

/**
 * Servicio para calcular estadísticas de verificaciones de ADN.
 *
 * Calcula:
 * - Cantidad de mutantes
 * - Cantidad de humanos
 * - Ratio (mutantes / humanos)
 *
 * OPTIMIZACIÓN: Usa índices en BD
 * - countByIsMutant() usa idx_is_mutant
 * - Complejidad: O(1) por índice
 * - Sin índice sería O(N) con full table scan
 */
@Service
@RequiredArgsConstructor
public class StatsService {

    private final DnaRecordRepository repository;

    /**
     * Obtiene estadísticas de todas las verificaciones de ADN.
     *
     * @return StatsResponse con contadores y ratio
     */
    public StatsResponse getStats() {
        // Contar mutantes (usa índice idx_is_mutant para O(1))
        long countMutant = repository.countByIsMutant(true);

        // Contar humanos (usa índice idx_is_mutant para O(1))
        long countHuman = repository.countByIsMutant(false);

        // Calcular ratio
        double ratio = calculateRatio(countMutant, countHuman);

        return new StatsResponse(countMutant, countHuman, ratio);
    }

    /**
     * Calcula el ratio de mutantes sobre humanos.
     *
     * Fórmula: ratio = count_mutant_dna / count_human_dna
     *
     * Casos especiales:
     * - Si no hay humanos (división por 0): retorna número de mutantes o 0
     * - Si no hay ninguno: retorna 0.0
     *
     * @param countMutant Cantidad de mutantes
     * @param countHuman Cantidad de humanos
     * @return Ratio calculado
     */
    private double calculateRatio(long countMutant, long countHuman) {
        if (countHuman == 0) {
            // Caso especial: no hay humanos registrados
            return countMutant > 0 ? (double) countMutant : 0.0;
        }

        return (double) countMutant / countHuman;
    }
}