package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.DnaRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests de integración para MutantController.
 * Prueba el flujo completo: Controller → Service → Repository → BD.
 *
 * Utiliza MockMvc para simular requests HTTP sin levantar servidor.
 */
@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("MutantController - Tests de Integración")
class MutantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // ==================== POST /mutant - CASOS EXITOSOS ====================

    @Test
    @DisplayName("POST /mutant debe retornar 200 OK cuando es mutante")
    void testCheckMutant_ReturnOk_WhenIsMutant() throws Exception {
        DnaRequest request = new DnaRequest(new String[]{
                "ATGCGA",
                "CAGTGC",
                "TTATGT",
                "AGAAGG",
                "CCCCTA",  // Horizontal: CCCC
                "TCACTG"
        });

        mockMvc.perform(post("/mutant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());  // 200 OK
    }

    @Test
    @DisplayName("POST /mutant debe retornar 403 Forbidden cuando es humano")
    void testCheckMutant_ReturnForbidden_WhenIsHuman() throws Exception {
        DnaRequest request = new DnaRequest(new String[]{
                "ATGCGA",
                "CAGTGC",
                "TTATTT",  // Solo 1 secuencia (no suficiente)
                "AGACGG",
                "GCGTCA",
                "TCACTG"
        });

        mockMvc.perform(post("/mutant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());  // 403 Forbidden
    }

    // ==================== POST /mutant - VALIDACIONES ====================

    @Test
    @DisplayName("POST /mutant debe retornar 400 cuando DNA es inválido (caracteres)")
    void testCheckMutant_ReturnBadRequest_WhenInvalidCharacters() throws Exception {
        DnaRequest request = new DnaRequest(new String[]{
                "ATXC",  // 'X' es inválido
                "CAGT",
                "TTAT",
                "AGAC"
        });

        mockMvc.perform(post("/mutant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())  // 400 Bad Request
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @DisplayName("POST /mutant debe retornar 400 cuando DNA es null")
    void testCheckMutant_ReturnBadRequest_WhenDnaIsNull() throws Exception {
        String jsonRequest = "{\"dna\": null}";

        mockMvc.perform(post("/mutant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @DisplayName("POST /mutant debe retornar 400 cuando DNA es array vacío")
    void testCheckMutant_ReturnBadRequest_WhenDnaIsEmpty() throws Exception {
        DnaRequest request = new DnaRequest(new String[]{});

        mockMvc.perform(post("/mutant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @DisplayName("POST /mutant debe retornar 400 cuando matriz no es cuadrada")
    void testCheckMutant_ReturnBadRequest_WhenNonSquareMatrix() throws Exception {
        DnaRequest request = new DnaRequest(new String[]{
                "ATGC",
                "CAGT",
                "TTAT"  // Solo 3 filas (no es 4x4)
        });

        mockMvc.perform(post("/mutant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());
    }

    // ==================== GET /stats ====================

    @Test
    @DisplayName("GET /stats debe retornar 200 OK con estructura correcta")
    void testGetStats_ReturnOk_WithCorrectStructure() throws Exception {
        mockMvc.perform(get("/stats")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.count_mutant_dna").exists())
                .andExpect(jsonPath("$.count_human_dna").exists())
                .andExpect(jsonPath("$.ratio").exists());
    }

    @Test
    @DisplayName("GET /stats debe retornar números válidos")
    void testGetStats_ReturnValidNumbers() throws Exception {
        // Primero agregar algunos registros
        DnaRequest mutant = new DnaRequest(new String[]{
                "ATGCGA", "CAGTGC", "TTATGT", "AGAAGG", "CCCCTA", "TCACTG"
        });
        DnaRequest human = new DnaRequest(new String[]{
                "ATGCGA", "CAGTGC", "TTATTT", "AGACGG", "GCGTCA", "TCACTG"
        });

        mockMvc.perform(post("/mutant")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mutant)));

        mockMvc.perform(post("/mutant")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(human)));

        // Luego verificar stats
        mockMvc.perform(get("/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count_mutant_dna").isNumber())
                .andExpect(jsonPath("$.count_human_dna").isNumber())
                .andExpect(jsonPath("$.ratio").isNumber());
    }
}