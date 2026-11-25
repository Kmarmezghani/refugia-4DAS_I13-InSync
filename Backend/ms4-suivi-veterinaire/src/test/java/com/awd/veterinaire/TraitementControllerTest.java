package com.awd.veterinaire;

import com.awd.veterinaire.entity.DossierMedical;
import com.awd.veterinaire.repository.DossierMedicalRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = "spring.datasource.url=jdbc:h2:mem:veterinaire_traitement_test;MODE=PostgreSQL;DATABASE_TO_LOWER=TRUE")
@AutoConfigureMockMvc
@ActiveProfiles("test")
class TraitementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DossierMedicalRepository dossiers;

    @Test
    void creeListeEtChangeEtatDuTraitement() throws Exception {
        Long dossierId = dossiers.save(new DossierMedical(20L, "VET-0020", LocalDate.of(2026, 11, 1))).getId();
        String reponse = creer(dossierId, "2026-12-05")
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.dossierId").value(dossierId.intValue()))
                .andExpect(jsonPath("$.statut").value("EN_COURS"))
                .andReturn().getResponse().getContentAsString();
        long id = objectMapper.readTree(reponse).path("id").asLong();

        changerStatut(id, "SUSPENDU").andExpect(status().isOk())
                .andExpect(jsonPath("$.statut").value("SUSPENDU"));
        changerStatut(id, "EN_COURS").andExpect(status().isOk())
                .andExpect(jsonPath("$.statut").value("EN_COURS"));
        changerStatut(id, "SUSPENDU").andExpect(status().isOk());
        changerStatut(id, "TERMINE").andExpect(status().isOk());
        changerStatut(id, "TERMINE").andExpect(status().isOk());
        changerStatut(id, "EN_COURS").andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409));

        mockMvc.perform(get("/api/vet/records/{id}/treatments", dossierId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].statut").value("TERMINE"))
                .andExpect(jsonPath("$[0].dateFin").value("2026-12-05"));
    }

    @Test
    void refuseLesDatesEtLesIdentifiantsInvalides() throws Exception {
        creer(999L, "2026-11-24").andExpect(status().isBadRequest());
        creer(999L, "2026-12-05").andExpect(status().isNotFound());
        changerStatut(999L, "TERMINE").andExpect(status().isNotFound());
        changerStatut(999L, "INCONNU").andExpect(status().isBadRequest());
        mockMvc.perform(get("/api/vet/records/999/treatments")).andExpect(status().isNotFound());
    }

    private ResultActions creer(Long dossierId, String fin) throws Exception {
        return mockMvc.perform(post("/api/vet/treatments")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "dossierId":%d,
                          "medicament":"Médicament prescrit",
                          "posologie":"Selon ordonnance",
                          "dateDebut":"2026-11-25",
                          "dateFin":"%s"
                        }
                        """.formatted(dossierId, fin)));
    }

    private ResultActions changerStatut(long id, String statut) throws Exception {
        return mockMvc.perform(patch("/api/vet/treatments/{id}/status", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"statut":"%s"}
                        """.formatted(statut)));
    }
}
