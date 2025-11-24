package org.example.repository;

import org.example.entity.DnaRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio JPA para operaciones de base de datos sobre DnaRecord.
 * Spring Data JPA genera automáticamente las implementaciones.
 */
@Repository
public interface DnaRecordRepository extends JpaRepository<DnaRecord, Long> {

    /**
     * Busca un registro de ADN por su hash SHA-256.
     * Utiliza el índice idx_dna_hash para búsqueda O(log N).
     *
     * @param dnaHash Hash SHA-256 del ADN
     * @return Optional con el registro si existe, vacío si no
     */
    Optional<DnaRecord> findByDnaHash(String dnaHash);

    /**
     * Cuenta la cantidad de registros según si son mutantes o humanos.
     * Utiliza el índice idx_is_mutant para conteo O(1).
     *
     * @param isMutant true para contar mutantes, false para humanos
     * @return Cantidad de registros que cumplen la condición
     */
    long countByIsMutant(boolean isMutant);
}