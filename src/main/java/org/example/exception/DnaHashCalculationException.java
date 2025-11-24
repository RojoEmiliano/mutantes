package org.example.exception;

/**
 * Excepción lanzada cuando ocurre un error al calcular el hash SHA-256 del ADN.
 * Esto generalmente ocurre si el algoritmo SHA-256 no está disponible,
 * aunque esto es extremadamente raro en JVM modernas.
 */
public class DnaHashCalculationException extends RuntimeException {

    /**
     * Constructor con mensaje y causa.
     *
     * @param message Mensaje descriptivo del error
     * @param cause Causa raíz de la excepción
     */
    public DnaHashCalculationException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor solo con mensaje.
     *
     * @param message Mensaje descriptivo del error
     */
    public DnaHashCalculationException(String message) {
        super(message);
    }
}