package org.example.service;

import org.example.dto.StatsResponse;
import org.example.repository.DnaRecordRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios para StatsService.
 * Prueba el cálculo de estadísticas y edge cases del ratio.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("StatsService - Tests Unitarios")
class StatsServiceTest {

    @Mock
    private DnaRecordRepository repository;

    @InjectMocks
    private StatsService statsService;

    @Test
    @DisplayName("Debe retornar estadísticas correctas con mutantes y humanos")
    void testGetStats_WithMutantsAndHumans_ReturnsCorrectStats() {
        // Given: 40 mutantes, 100 humanos
        when(repository.countByIsMutant(true)).thenReturn(40L);
        when(repository.countByIsMutant(false)).thenReturn(100L);

        // When: Obtener stats
        StatsResponse stats = statsService.getStats();

        // Then: Ratio = 40/100 = 0.4
        assertEquals(40L, stats.getCountMutantDna());
        assertEquals(100L, stats.getCountHumanDna());
        assertEquals(0.4, stats.getRatio(), 0.001);

        verify(repository, times(1)).countByIsMutant(true);
        verify(repository, times(1)).countByIsMutant(false);
    }

    @Test
    @DisplayName("Debe retornar ceros cuando no hay registros")
    void testGetStats_NoRecords_ReturnsZeros() {
        // Given: No hay registros
        when(repository.countByIsMutant(true)).thenReturn(0L);
        when(repository.countByIsMutant(false)).thenReturn(0L);

        // When: Obtener stats
        StatsResponse stats = statsService.getStats();

        // Then: Todo en cero
        assertEquals(0L, stats.getCountMutantDna());
        assertEquals(0L, stats.getCountHumanDna());
        assertEquals(0.0, stats.getRatio(), 0.001);
    }

    @Test
    @DisplayName("Debe retornar ratio correcto cuando solo hay mutantes")
    void testGetStats_OnlyMutants_ReturnsCorrectRatio() {
        // Given: 50 mutantes, 0 humanos
        when(repository.countByIsMutant(true)).thenReturn(50L);
        when(repository.countByIsMutant(false)).thenReturn(0L);

        // When: Obtener stats
        StatsResponse stats = statsService.getStats();

        // Then: Ratio = 50 (caso especial: divide by zero)
        assertEquals(50L, stats.getCountMutantDna());
        assertEquals(0L, stats.getCountHumanDna());
        assertEquals(50.0, stats.getRatio(), 0.001);
    }

    @Test
    @DisplayName("Debe retornar ratio correcto cuando solo hay humanos")
    void testGetStats_OnlyHumans_ReturnsZeroRatio() {
        // Given: 0 mutantes, 100 humanos
        when(repository.countByIsMutant(true)).thenReturn(0L);
        when(repository.countByIsMutant(false)).thenReturn(100L);

        // When: Obtener stats
        StatsResponse stats = statsService.getStats();

        // Then: Ratio = 0/100 = 0.0
        assertEquals(0L, stats.getCountMutantDna());
        assertEquals(100L, stats.getCountHumanDna());
        assertEquals(0.0, stats.getRatio(), 0.001);
    }

    @Test
    @DisplayName("Debe calcular ratio correctamente cuando son iguales")
    void testGetStats_EqualCounts_ReturnsRatioOne() {
        // Given: 50 mutantes, 50 humanos
        when(repository.countByIsMutant(true)).thenReturn(50L);
        when(repository.countByIsMutant(false)).thenReturn(50L);

        // When: Obtener stats
        StatsResponse stats = statsService.getStats();

        // Then: Ratio = 50/50 = 1.0
        assertEquals(50L, stats.getCountMutantDna());
        assertEquals(50L, stats.getCountHumanDna());
        assertEquals(1.0, stats.getRatio(), 0.001);
    }

    @Test
    @DisplayName("Debe calcular ratio correctamente para números grandes")
    void testGetStats_LargeNumbers_ReturnsCorrectRatio() {
        // Given: 1000 mutantes, 500 humanos
        when(repository.countByIsMutant(true)).thenReturn(1000L);
        when(repository.countByIsMutant(false)).thenReturn(500L);

        // When: Obtener stats
        StatsResponse stats = statsService.getStats();

        // Then: Ratio = 1000/500 = 2.0
        assertEquals(1000L, stats.getCountMutantDna());
        assertEquals(500L, stats.getCountHumanDna());
        assertEquals(2.0, stats.getRatio(), 0.001);
    }
}