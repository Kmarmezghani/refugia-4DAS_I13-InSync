package com.awd.veterinaire;

import com.awd.veterinaire.dto.DossierMedicalResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = "spring.datasource.url=jdbc:h2:mem:veterinaire_dev_test;MODE=PostgreSQL;DATABASE_TO_LOWER=TRUE")
@AutoConfigureMockMvc
@ActiveProfiles({"test", "dev"})
class DossierMedicalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void ouvreLitEtModifieUnDossier() throws Exception {
        String reponse = mockMvc.perform(post("/api/vet/records")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"animalId":12}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.animalId").value(12))
                .andExpect(jsonPath("$.numeroDossier").value("VET-0012"))
                .andExpect(jsonPath("$.dateOuverture").isNotEmpty())
                .andReturn().getResponse().getContentAsString();
        Long id = objectMapper.readValue(reponse, DossierMedicalResponse.class).id();

        mockMvc.perform(get("/api/vet/records/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.animalId").value(12));

        mockMvc.perform(get("/api/vet/records/by-animal/12"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.intValue()));

        mockMvc.perform(patch("/api/vet/records/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"notesGenerales":"Dossier mis à jour"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.notesGenerales").value("Dossier mis à jour"));

        mockMvc.perform(get("/api/vet/records/{id}", id))
                .andExpect(jsonPath("$.notesGenerales").value("Dossier mis à jour"));

        mockMvc.perform(patch("/api/vet/records/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());

        mockMvc.perform(patch("/api/vet/records/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"notesGenerales":null}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.notesGenerales").value(nullValue()));

        mockMvc.perform(post("/api/vet/records")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"animalId":12}
                                """))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.error").value("Conflict"))
                .andExpect(jsonPath("$.message").isNotEmpty())
                .andExpect(jsonPath("$.path").value("/api/vet/records"));
    }

    @Test
    void refuseLesRequetesInvalidesEtLesRessourcesInconnues() throws Exception {
        mockMvc.perform(post("/api/vet/records")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"animalId":0}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));

        mockMvc.perform(post("/api/vet/records")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"animalId":999}
                                """))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));

        mockMvc.perform(get("/api/vet/records/by-animal/999"))
                .andExpect(status().isNotFound());
        mockMvc.perform(get("/api/vet/records/999"))
                .andExpect(status().isNotFound());
        mockMvc.perform(patch("/api/vet/records/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"notesGenerales":"Notes"}
                                """))
                .andExpect(status().isNotFound());
    }
}
