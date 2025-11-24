package org.example.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

/**
 * Validador para la anotación @ValidDnaSequence.
 * Implementa la lógica de validación para secuencias de ADN.
 */
public class ValidDnaSequenceValidator implements ConstraintValidator<ValidDnaSequence, String[]> {

    private static final int MIN_SIZE = 4;
    private static final Pattern DNA_PATTERN = Pattern.compile("^[ATCG]+$");

    @Override
    public void initialize(ValidDnaSequence constraintAnnotation) {
        // No requiere inicialización especial
    }

    @Override
    public boolean isValid(String[] dna, ConstraintValidatorContext context) {
        // Validar que no sea null o vacío
        if (dna == null || dna.length == 0) {
            return false;
        }

        final int n = dna.length;

        // Validar tamaño mínimo
        if (n < MIN_SIZE) {
            return false;
        }

        // Validar que sea matriz NxN y contenga solo caracteres válidos
        for (String row : dna) {
            // Validar que la fila no sea null
            if (row == null) {
                return false;
            }

            // Validar que sea cuadrada (cada fila tiene longitud n)
            if (row.length() != n) {
                return false;
            }

            // Validar que solo contenga A, T, C, G
            if (!DNA_PATTERN.matcher(row).matches()) {
                return false;
            }
        }

        return true;
    }
}