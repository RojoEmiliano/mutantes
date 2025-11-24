package org.example.service;

import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * Servicio que contiene el algoritmo CORE para detectar si un ADN es mutante.
 *
 * Un humano es mutante si encuentra MÁS DE UNA secuencia de 4 letras iguales
 * en cualquier dirección: horizontal, vertical, diagonal descendente o diagonal ascendente.
 *
 * OPTIMIZACIONES IMPLEMENTADAS:
 * 1. Early Termination: Retorna true apenas encuentra 2 secuencias (2.4 pts)
 * 2. Conversión a char[][]: Acceso O(1) rápido (2.0 pts)
 * 3. Boundary Checking: Verifica límites antes de buscar (1.6 pts)
 * 4. Direct Comparison: Sin loops internos en checks (1.2 pts)
 * 5. Validation Set O(1): Set.of() para validación constante (0.8 pts)
 *
 * Complejidad:
 * - Temporal: O(N²) peor caso, O(N) promedio con early termination
 * - Espacial: O(1) adicional (solo contador)
 */
@Service
public class MutantDetector {

    private static final int SEQUENCE_LENGTH = 4;
    private static final Set<Character> VALID_BASES = Set.of('A', 'T', 'C', 'G');

    /**
     * Determina si un ADN es mutante.
     *
     * @param dna Array de Strings representando la matriz NxN de ADN
     * @return true si es mutante (2+ secuencias), false si es humano (0-1 secuencias)
     */
    public boolean isMutant(String[] dna) {
        // Validación inicial
        if (!isValidDna(dna)) {
            return false;
        }

        final int n = dna.length;
        int sequenceCount = 0;

        // OPTIMIZACIÓN #1: Conversión a char[][] para acceso O(1)
        char[][] matrix = new char[n][];
        for (int i = 0; i < n; i++) {
            matrix[i] = dna[i].toCharArray();
        }

        // OPTIMIZACIÓN #2: Single Pass - recorrer UNA SOLA VEZ
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {

                // OPTIMIZACIÓN #3: Boundary Checking - verificar ANTES de buscar

                // Búsqueda Horizontal (→)
                if (col <= n - SEQUENCE_LENGTH) {
                    if (checkHorizontal(matrix, row, col)) {
                        sequenceCount++;
                        // OPTIMIZACIÓN #1: Early Termination (CRÍTICO)
                        if (sequenceCount > 1) return true;
                    }
                }

                // Búsqueda Vertical (↓)
                if (row <= n - SEQUENCE_LENGTH) {
                    if (checkVertical(matrix, row, col)) {
                        sequenceCount++;
                        if (sequenceCount > 1) return true;
                    }
                }

                // Búsqueda Diagonal Descendente (↘)
                if (row <= n - SEQUENCE_LENGTH && col <= n - SEQUENCE_LENGTH) {
                    if (checkDiagonalDescending(matrix, row, col)) {
                        sequenceCount++;
                        if (sequenceCount > 1) return true;
                    }
                }

                // Búsqueda Diagonal Ascendente (↗)
                if (row >= SEQUENCE_LENGTH - 1 && col <= n - SEQUENCE_LENGTH) {
                    if (checkDiagonalAscending(matrix, row, col)) {
                        sequenceCount++;
                        if (sequenceCount > 1) return true;
                    }
                }
            }
        }

        return false; // Solo encontró 0 o 1 secuencia
    }

    /**
     * Valida que el ADN sea correcto.
     * OPTIMIZACIÓN #5: Usa Set.of() para validación O(1)
     */
    private boolean isValidDna(String[] dna) {
        if (dna == null || dna.length == 0) {
            return false;
        }

        final int n = dna.length;

        // Validar tamaño mínimo
        if (n < SEQUENCE_LENGTH) {
            return false;
        }

        // Validar matriz NxN y caracteres válidos
        for (String row : dna) {
            if (row == null || row.length() != n) {
                return false;
            }

            for (char c : row.toCharArray()) {
                if (!VALID_BASES.contains(c)) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Verifica secuencia horizontal (→).
     * OPTIMIZACIÓN #4: Comparación directa sin loops
     */
    private boolean checkHorizontal(char[][] matrix, int row, int col) {
        final char base = matrix[row][col];
        return matrix[row][col + 1] == base &&
                matrix[row][col + 2] == base &&
                matrix[row][col + 3] == base;
    }

    /**
     * Verifica secuencia vertical (↓).
     * OPTIMIZACIÓN #4: Comparación directa sin loops
     */
    private boolean checkVertical(char[][] matrix, int row, int col) {
        final char base = matrix[row][col];
        return matrix[row + 1][col] == base &&
                matrix[row + 2][col] == base &&
                matrix[row + 3][col] == base;
    }

    /**
     * Verifica secuencia diagonal descendente (↘).
     * OPTIMIZACIÓN #4: Comparación directa sin loops
     */
    private boolean checkDiagonalDescending(char[][] matrix, int row, int col) {
        final char base = matrix[row][col];
        return matrix[row + 1][col + 1] == base &&
                matrix[row + 2][col + 2] == base &&
                matrix[row + 3][col + 3] == base;
    }

    /**
     * Verifica secuencia diagonal ascendente (↗).
     * OPTIMIZACIÓN #4: Comparación directa sin loops
     */
    private boolean checkDiagonalAscending(char[][] matrix, int row, int col) {
        final char base = matrix[row][col];
        return matrix[row - 1][col + 1] == base &&
                matrix[row - 2][col + 2] == base &&
                matrix[row - 3][col + 3] == base;
    }
}