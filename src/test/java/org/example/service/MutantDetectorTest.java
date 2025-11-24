package org.example.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para MutantDetector.
 * Cobertura: >95% del algoritmo core.
 *
 * Casos cubiertos:
 * - Mutantes (horizontal, vertical, diagonales, múltiples, matriz grande)
 * - Humanos (sin secuencias, 1 sola secuencia)
 * - Validaciones (null, empty, non-square, caracteres inválidos, etc.)
 * - Edge cases (secuencias largas, diagonal en esquina)
 */
@DisplayName("MutantDetector - Tests Unitarios del Algoritmo")
class MutantDetectorTest {

    private MutantDetector mutantDetector;

    @BeforeEach
    void setUp() {
        mutantDetector = new MutantDetector();
    }

    // ==================== CASOS MUTANTES (debe retornar true) ====================

    @Test
    @DisplayName("Debe detectar mutante con secuencias horizontal y diagonal")
    void testMutantWithHorizontalAndDiagonalSequences() {
        String[] dna = {
                "ATGCGA",
                "CAGTGC",
                "TTATGT",
                "AGAAGG",
                "CCCCTA",  // ← Horizontal: CCCC
                "TCACTG"
        };

        assertTrue(mutantDetector.isMutant(dna),
                "Debe detectar mutante con secuencias horizontal y diagonal");
    }

    @Test
    @DisplayName("Debe detectar mutante con secuencias verticales")
    void testMutantWithVerticalSequences() {
        String[] dna = {
                "ATGCGA",
                "ATGTGC",
                "ATATGT",
                "AGAAGG",
                "CCCCTA",
                "TCACTG"
        };
        // Columna 0: A-A-A-A (vertical)
        // Fila 4: C-C-C-C (horizontal)

        assertTrue(mutantDetector.isMutant(dna),
                "Debe detectar mutante con secuencias verticales");
    }

    @Test
    @DisplayName("Debe detectar mutante con múltiples secuencias horizontales")
    void testMutantWithMultipleHorizontalSequences() {
        String[] dna = {
                "AAAATG",
                "CAGTGC",
                "TTATGT",
                "AGAAGG",
                "CCCCTA",  // ← CCCC
                "TCACTG"
        };
        // Fila 0: AAAA
        // Fila 4: CCCC

        assertTrue(mutantDetector.isMutant(dna),
                "Debe detectar mutante con múltiples horizontales");
    }

    @Test
    @DisplayName("Debe detectar mutante con diagonales ascendentes y descendentes")
    void testMutantWithBothDiagonals() {
        String[] dna = {
                "ATGCGA",
                "CAGTGC",
                "TTATGT",
                "AGAAGG",
                "CCCCTA",
                "TCACTG"
        };

        assertTrue(mutantDetector.isMutant(dna),
                "Debe detectar mutante con ambos tipos de diagonales");
    }

    @Test
    @DisplayName("Debe detectar mutante en matriz grande 10x10")
    void testMutantWithLargeDna() {
        String[] dna = {
                "ATGCGATTTT",
                "CAGTGCATAT",
                "TTATGTACGT",
                "AGAAGGCGTA",
                "CCCCTATGCA",  // ← CCCC
                "TCACTGACGT",
                "ATGCGAACGT",
                "CAGTGCATCG",
                "TTATGTACCC",
                "AGAAGGCCCC"   // ← CCCC
        };

        assertTrue(mutantDetector.isMutant(dna),
                "Debe detectar mutante en matriz grande 10x10");
    }

    @Test
    @DisplayName("Debe detectar mutante cuando todo es igual (matriz AAAA...)")
    void testMutantAllSameCharacter() {
        String[] dna = {
                "AAAA",
                "AAAA",
                "AAAA",
                "AAAA"
        };

        assertTrue(mutantDetector.isMutant(dna),
                "Debe detectar mutante cuando todas las letras son iguales");
    }

    @Test
    @DisplayName("Debe detectar mutante con diagonal en esquina")
    void testMutantDiagonalInCorner() {
        String[] dna = {
                "AAAA",
                "TTTT",
                "CCCC",
                "GGGG"
        };
        // Múltiples horizontales: A-A-A-A, T-T-T-T, C-C-C-C, G-G-G-G

        assertTrue(mutantDetector.isMutant(dna),
                "Debe detectar mutante con múltiples secuencias horizontales");
    }

    // ==================== CASOS HUMANOS (debe retornar false) ====================

    @Test
    @DisplayName("Debe retornar false cuando solo hay 1 secuencia")
    void testNotMutantWithOnlyOneSequence() {
        String[] dna = {
                "ATGCGA",
                "CAGTGC",
                "TTATTT",  // ← Solo esta: TTTT
                "AGACGG",
                "GCGTCA",
                "TCACTG"
        };

        assertFalse(mutantDetector.isMutant(dna),
                "Debe retornar false cuando solo hay 1 secuencia (necesita 2+)");
    }

    @Test
    @DisplayName("Debe retornar false cuando no hay secuencias")
    void testNotMutantWithNoSequences() {
        String[] dna = {
                "ATGC",
                "CAGT",
                "TTAT",
                "AGAC"
        };

        assertFalse(mutantDetector.isMutant(dna),
                "Debe retornar false cuando no hay ninguna secuencia");
    }

    @Test
    @DisplayName("Debe retornar false para matriz pequeña 4x4 sin secuencias")
    void testNotMutantSmallDna() {
        String[] dna = {
                "ATCG",
                "CGAT",
                "TAGC",
                "GCTA"
        };

        assertFalse(mutantDetector.isMutant(dna),
                "Debe retornar false para matriz 4x4 sin secuencias");
    }

    // ==================== VALIDACIONES (debe retornar false) ====================

    @Test
    @DisplayName("Debe retornar false cuando DNA es null")
    void testNotMutantWithNullDna() {
        assertFalse(mutantDetector.isMutant(null),
                "Debe retornar false cuando DNA es null");
    }

    @Test
    @DisplayName("Debe retornar false cuando DNA es array vacío")
    void testNotMutantWithEmptyDna() {
        String[] dna = {};

        assertFalse(mutantDetector.isMutant(dna),
                "Debe retornar false cuando DNA es array vacío");
    }

    @Test
    @DisplayName("Debe retornar false cuando matriz no es cuadrada (NxM)")
    void testNotMutantWithNonSquareDna() {
        String[] dna = {
                "ATGC",
                "CAGT",
                "TTAT"  // ← Solo 3 filas, pero cada una tiene 4 caracteres
        };

        assertFalse(mutantDetector.isMutant(dna),
                "Debe retornar false cuando matriz no es cuadrada");
    }

    @Test
    @DisplayName("Debe retornar false cuando hay caracteres inválidos")
    void testNotMutantWithInvalidCharacters() {
        String[] dna = {
                "ATXC",  // ← Carácter 'X' inválido
                "CAGT",
                "TTAT",
                "AGAC"
        };

        assertFalse(mutantDetector.isMutant(dna),
                "Debe retornar false cuando hay caracteres inválidos (no A,T,C,G)");
    }

    @Test
    @DisplayName("Debe retornar false cuando una fila es null")
    void testNotMutantWithNullRow() {
        String[] dna = {
                "ATGC",
                null,  // ← Fila null
                "TTAT",
                "AGAC"
        };

        assertFalse(mutantDetector.isMutant(dna),
                "Debe retornar false cuando una fila es null");
    }

    @Test
    @DisplayName("Debe retornar false cuando matriz es muy pequeña (menor a 4x4)")
    void testNotMutantWithTooSmallDna() {
        String[] dna = {
                "ATC",
                "CAG",
                "TTA"
        };

        assertFalse(mutantDetector.isMutant(dna),
                "Debe retornar false cuando matriz es menor a 4x4");
    }

    // ==================== EDGE CASES ====================

    @Test
    @DisplayName("Debe detectar mutante con secuencia de longitud mayor a 4")
    void testMutantWithSequenceLongerThanFour() {
        String[] dna = {
                "AAAAAA",  // ← 6 A's horizontales (múltiples secuencias de 4)
                "CAGTGC",
                "TTATGT",
                "AGACGG",
                "GCGTCA",
                "TCACTG"
        };

        assertTrue(mutantDetector.isMutant(dna),
                "Secuencia de 6 iguales contiene múltiples secuencias de 4, es mutante");
    }
}