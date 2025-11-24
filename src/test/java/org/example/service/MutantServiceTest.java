package org.example.service;

import org.example.entity.DnaRecord;
import org.example.repository.DnaRecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios para MutantService.
 * Utiliza Mockito para simular dependencias (repository, detector).
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("MutantService - Tests Unitarios")
class MutantServiceTest {

    @Mock
    private MutantDetector mutantDetector;

    @Mock
    private DnaRecordRepository repository;

    @InjectMocks
    private MutantService mutantService;

    private String[] mutantDna;
    private String[] humanDna;

    @BeforeEach
    void setUp() {
        mutantDna = new String[]{
                "ATGCGA",
                "CAGTGC",
                "TTATGT",
                "AGAAGG",
                "CCCCTA",
                "TCACTG"
        };

        humanDna = new String[]{
                "ATGCGA",
                "CAGTGC",
                "TTATTT",
                "AGACGG",
                "GCGTCA",
                "TCACTG"
        };
    }

    @Test
    @DisplayName("Debe analizar DNA mutante y guardarlo en BD")
    void testAnalyzeDna_Mutant_SavesToDatabase() {
        // Given: DNA no existe en BD
        when(repository.findByDnaHash(anyString())).thenReturn(Optional.empty());
        when(mutantDetector.isMutant(mutantDna)).thenReturn(true);
        when(repository.save(any(DnaRecord.class))).thenReturn(new DnaRecord());

        // When: Analizar DNA
        boolean result = mutantService.analyzeDna(mutantDna);

        // Then: Es mutante y se guardó en BD
        assertTrue(result, "Debe retornar true para DNA mutante");
        verify(mutantDetector, times(1)).isMutant(mutantDna);
        verify(repository, times(1)).save(any(DnaRecord.class));
    }

    @Test
    @DisplayName("Debe analizar DNA humano y guardarlo en BD")
    void testAnalyzeDna_Human_SavesToDatabase() {
        // Given: DNA no existe en BD
        when(repository.findByDnaHash(anyString())).thenReturn(Optional.empty());
        when(mutantDetector.isMutant(humanDna)).thenReturn(false);
        when(repository.save(any(DnaRecord.class))).thenReturn(new DnaRecord());

        // When: Analizar DNA
        boolean result = mutantService.analyzeDna(humanDna);

        // Then: Es humano y se guardó en BD
        assertFalse(result, "Debe retornar false para DNA humano");
        verify(mutantDetector, times(1)).isMutant(humanDna);
        verify(repository, times(1)).save(any(DnaRecord.class));
    }

    @Test
    @DisplayName("Debe retornar resultado cacheado cuando DNA ya existe en BD")
    void testAnalyzeDna_CachedResult_DoesNotAnalyzeAgain() {
        // Given: DNA ya existe en BD
        DnaRecord cachedRecord = new DnaRecord("hash123", true);
        when(repository.findByDnaHash(anyString())).thenReturn(Optional.of(cachedRecord));

        // When: Analizar DNA
        boolean result = mutantService.analyzeDna(mutantDna);

        // Then: Retorna resultado cacheado sin llamar al detector
        assertTrue(result, "Debe retornar resultado cacheado");
        verify(mutantDetector, never()).isMutant(any());  // ← No se llamó al detector
        verify(repository, never()).save(any());          // ← No se guardó
    }

    @Test
    @DisplayName("Debe calcular hash correctamente para DNA idéntico")
    void testCalculateDnaHash_IdenticalDna_ProducesSameHash() {
        // Given: Dos DNA idénticos
        when(repository.findByDnaHash(anyString())).thenReturn(Optional.empty());
        when(mutantDetector.isMutant(any())).thenReturn(true);

        // When: Analizar ambos
        mutantService.analyzeDna(mutantDna);
        String[] sameDna = mutantDna.clone();

        // Reset mock para segunda llamada
        reset(repository);
        DnaRecord cachedRecord = new DnaRecord("hash", true);
        when(repository.findByDnaHash(anyString())).thenReturn(Optional.of(cachedRecord));

        boolean result = mutantService.analyzeDna(sameDna);

        // Then: Segunda llamada usa caché (mismo hash)
        assertTrue(result);
        verify(repository, times(1)).findByDnaHash(anyString());
    }

    @Test
    @DisplayName("Debe calcular hashes diferentes para DNA diferente")
    void testCalculateDnaHash_DifferentDna_ProducesDifferentHash() {
        // Given: Dos DNA diferentes
        when(repository.findByDnaHash(anyString())).thenReturn(Optional.empty());
        when(mutantDetector.isMutant(any())).thenReturn(true);
        when(repository.save(any(DnaRecord.class))).thenReturn(new DnaRecord());

        // When: Analizar ambos
        mutantService.analyzeDna(mutantDna);
        mutantService.analyzeDna(humanDna);

        // Then: Se guardaron 2 registros (hashes diferentes)
        verify(repository, times(2)).save(any(DnaRecord.class));
    }
}