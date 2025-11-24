package org.example.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * PROGRAMA DE ANÁLISIS DE MÉTRICAS DEL ALGORITMO
 *
 * Ejecutar con: gradlew test --tests AlgorithmMetricsTest
 *
 * Mide 5 métricas clave de eficiencia:
 * 1. Tiempos de ejecución por tamaño
 * 2. Escalabilidad (crecimiento vs N)
 * 3. Efectividad del Early Termination
 * 4. Throughput (operaciones/segundo)
 * 5. Análisis estadístico detallado
 */
@DisplayName("⚡ ANÁLISIS DE MÉTRICAS DEL ALGORITMO")
class AlgorithmMetricsTest {

    private final MutantDetector detector = new MutantDetector();
    private final Random random = new Random(42);

    @Test
    @DisplayName("EJECUTAR ANÁLISIS COMPLETO DE MÉTRICAS")
    void ejecutarAnalisisCompleto() {
        imprimirEncabezado();

        // 1. Métricas básicas
        metrica1_TiemposEjecucion();

        // 2. Escalabilidad
        metrica2_Escalabilidad();

        // 3. Early Termination
        metrica3_EarlyTermination();

        // 4. Throughput
        metrica4_Throughput();

        // 5. Análisis estadístico
        metrica5_AnalisisEstadistico();

        imprimirPie();
    }

    private void imprimirEncabezado() {
        System.out.println("\n================================================================");
        System.out.println("    ANALISIS DE METRICAS - ALGORITMO DETECCION MUTANTES");
        System.out.println("================================================================\n");
    }

    private void imprimirPie() {
        System.out.println("\n================================================================");
        System.out.println("    ANALISIS COMPLETADO EXITOSAMENTE");
        System.out.println("================================================================\n");
    }

    /**
     * MÉTRICA 1: Tiempos de ejecución en diferentes tamaños
     */
    private void metrica1_TiemposEjecucion() {
        System.out.println("METRICA 1: Tiempos de Ejecucion por Tamaño");
        System.out.println("----------------------------------------------------------------");
        System.out.printf("%-12s %-18s %-18s %-18s%n",
                "Tamaño", "Tiempo Avg (ms)", "Tiempo Min (ms)", "Tiempo Max (ms)");
        System.out.println("----------------------------------------------------------------");

        int[] sizes = {6, 10, 50, 100, 500, 1000};

        for (int size : sizes) {
            MetricResult result = medirTiempo(size, 100);
            System.out.printf("%-12s %-18.3f %-18.3f %-18.3f%n",
                    size + "x" + size,
                    result.avgMs,
                    result.minMs,
                    result.maxMs);
        }
        System.out.println();
    }

    /**
     * MÉTRICA 2: Análisis de escalabilidad
     */
    private void metrica2_Escalabilidad() {
        System.out.println("METRICA 2: Analisis de Escalabilidad");
        System.out.println("----------------------------------------------------------------");
        System.out.printf("%-12s %-18s %-22s %-18s%n",
                "Tamaño", "Tiempo (ms)", "Factor Crecimiento", "Complejidad");
        System.out.println("----------------------------------------------------------------");

        int[] sizes = {10, 20, 40, 80, 160, 320};
        long previousTime = 0;

        for (int size : sizes) {
            MetricResult result = medirTiempo(size, 50);
            long currentTime = (long) (result.avgMs * 1000);

            String factor = "-";
            String complejidad = "-";

            if (previousTime > 0) {
                double growthFactor = (double) currentTime / previousTime;
                factor = String.format("%.2fx", growthFactor);

                double theoreticalN2 = Math.pow((double) size / (size/2), 2);
                if (growthFactor < theoreticalN2 * 0.7) {
                    complejidad = "< O(N^2)";
                } else if (growthFactor <= theoreticalN2 * 1.3) {
                    complejidad = "~= O(N^2)";
                } else {
                    complejidad = "> O(N^2)";
                }
            }

            System.out.printf("%-12s %-18.3f %-22s %-18s%n",
                    size + "x" + size,
                    result.avgMs,
                    factor,
                    complejidad);

            previousTime = currentTime;
        }
        System.out.println();
    }

    /**
     * MÉTRICA 3: Efectividad del Early Termination
     */
    private void metrica3_EarlyTermination() {
        System.out.println("METRICA 3: Efectividad del Early Termination");
        System.out.println("----------------------------------------------------------------");

        int size = 100;
        int iterations = 1000;

        // DNA Mutante
        String[] mutantDna = generarDnaMutante(size);

        // DNA Humano
        String[] humanDna = generarDnaAleatorio(size);

        // Medir mutante
        long mutantTotal = 0;
        for (int i = 0; i < iterations; i++) {
            long start = System.nanoTime();
            detector.isMutant(mutantDna);
            mutantTotal += System.nanoTime() - start;
        }
        double mutantAvg = mutantTotal / (double) iterations / 1_000_000;

        // Medir humano
        long humanTotal = 0;
        for (int i = 0; i < iterations; i++) {
            long start = System.nanoTime();
            detector.isMutant(humanDna);
            humanTotal += System.nanoTime() - start;
        }
        double humanAvg = humanTotal / (double) iterations / 1_000_000;

        double improvement = ((humanAvg - mutantAvg) / humanAvg) * 100;

        System.out.printf("Matriz: %dx%d (Promedio de %d iteraciones)%n", size, size, iterations);
        System.out.printf("  - DNA Mutante (con early term): %.3f ms%n", mutantAvg);
        System.out.printf("  - DNA Humano (sin early term):  %.3f ms%n", humanAvg);
        System.out.printf("  - Mejora con early termination: %.1f%%%n", improvement);
        System.out.println();
    }

    /**
     * MÉTRICA 4: Throughput
     */
    private void metrica4_Throughput() {
        System.out.println("METRICA 4: Throughput (Operaciones por Segundo)");
        System.out.println("----------------------------------------------------------------");
        System.out.printf("%-12s %-22s %-22s%n",
                "Tamaño", "Ops/seg", "Tiempo/Op (us)");
        System.out.println("----------------------------------------------------------------");

        int[] sizes = {6, 50, 100};

        for (int size : sizes) {
            String[] dna = generarDnaAleatorio(size);

            long endTime = System.currentTimeMillis() + 1000;
            int operations = 0;

            while (System.currentTimeMillis() < endTime) {
                detector.isMutant(dna);
                operations++;
            }

            double timePerOp = 1_000_000.0 / operations;

            System.out.printf("%-12s %-22s %-22.1f%n",
                    size + "x" + size,
                    String.format("%,d", operations),
                    timePerOp);
        }
        System.out.println();
    }

    /**
     * MÉTRICA 5: Análisis estadístico
     */
    private void metrica5_AnalisisEstadistico() {
        System.out.println("METRICA 5: Analisis Estadistico (100x100)");
        System.out.println("----------------------------------------------------------------");

        int size = 100;
        int iterations = 1000;
        List<Double> times = new ArrayList<>();

        for (int i = 0; i < iterations; i++) {
            String[] dna = generarDnaAleatorio(size);
            long start = System.nanoTime();
            detector.isMutant(dna);
            long end = System.nanoTime();
            times.add((end - start) / 1_000_000.0);
        }

        double sum = times.stream().mapToDouble(Double::doubleValue).sum();
        double avg = sum / iterations;
        double min = times.stream().mapToDouble(Double::doubleValue).min().orElse(0);
        double max = times.stream().mapToDouble(Double::doubleValue).max().orElse(0);

        double variance = times.stream()
                .mapToDouble(t -> Math.pow(t - avg, 2))
                .sum() / iterations;
        double stdDev = Math.sqrt(variance);

        times.sort(Double::compareTo);
        double p50 = times.get(iterations / 2);
        double p95 = times.get((int) (iterations * 0.95));
        double p99 = times.get((int) (iterations * 0.99));

        System.out.printf("Iteraciones: %d%n", iterations);
        System.out.printf("  - Media:              %.3f ms%n", avg);
        System.out.printf("  - Mediana (P50):      %.3f ms%n", p50);
        System.out.printf("  - Minimo:             %.3f ms%n", min);
        System.out.printf("  - Maximo:             %.3f ms%n", max);
        System.out.printf("  - Desv. Estandar:     %.3f ms%n", stdDev);
        System.out.printf("  - Percentil 95:       %.3f ms%n", p95);
        System.out.printf("  - Percentil 99:       %.3f ms%n", p99);
        System.out.println();
    }

    // ==================== MÉTODOS AUXILIARES ====================

    private MetricResult medirTiempo(int size, int iterations) {
        double totalTime = 0;
        double minTime = Double.MAX_VALUE;
        double maxTime = 0;

        for (int i = 0; i < iterations; i++) {
            String[] dna = generarDnaAleatorio(size);

            long startTime = System.nanoTime();
            detector.isMutant(dna);
            long endTime = System.nanoTime();

            double timeMs = (endTime - startTime) / 1_000_000.0;
            totalTime += timeMs;
            minTime = Math.min(minTime, timeMs);
            maxTime = Math.max(maxTime, timeMs);
        }

        return new MetricResult(totalTime / iterations, minTime, maxTime);
    }

    private String[] generarDnaAleatorio(int size) {
        char[] bases = {'A', 'T', 'C', 'G'};
        String[] dna = new String[size];

        for (int i = 0; i < size; i++) {
            StringBuilder row = new StringBuilder();
            for (int j = 0; j < size; j++) {
                row.append(bases[random.nextInt(4)]);
            }
            dna[i] = row.toString();
        }

        return dna;
    }

    private String[] generarDnaMutante(int size) {
        String[] dna = new String[size];

        for (int i = 0; i < size; i++) {
            if (i == 0 || i == 1) {
                dna[i] = "A".repeat(size);
            } else {
                char[] bases = {'A', 'T', 'C', 'G'};
                StringBuilder row = new StringBuilder();
                for (int j = 0; j < size; j++) {
                    row.append(bases[random.nextInt(4)]);
                }
                dna[i] = row.toString();
            }
        }

        return dna;
    }

    static class MetricResult {
        double avgMs;
        double minMs;
        double maxMs;

        MetricResult(double avg, double min, double max) {
            this.avgMs = avg;
            this.minMs = min;
            this.maxMs = max;
        }
    }
}