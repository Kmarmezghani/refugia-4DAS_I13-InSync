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

import java.time.LocalDate;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = "spring.datasource.url=jdbc:h2:mem:veterinaire_consultation_test;MODE=PostgreSQL;DATABASE_TO_LOWER=TRUE")
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ConsultationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DossierMedicalRepository dossiers;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void enregistreEtConsulteHistoriqueDuDossier() throws Exception {
        Long dossierId = dossiers.save(new DossierMedical(12L, "VET-0012", LocalDate.of(2026, 11, 20))).getId();

        mockMvc.perform(get("/api/vet/records/{id}/consultations", dossierId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        String reponse = mockMvc.perform(post("/api/vet/consultations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "dossierId": %d,
                                  "dateConsultation": "2026-11-25",
                                  "motif": "Contrôle de routine",
                                  "diagnostic": "Examen normal",
                                  "poids": 8.4,
                                  "temperature": 38.5,
                                  "remarques": "Suivi habituel"
                                }
                                """.formatted(dossierId)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.dossierId").value(dossierId.intValue()))
                .andReturn().getResponse().getContentAsString();
        long consultationId = objectMapper.readTree(reponse).path("id").asLong();

        mockMvc.perform(post("/api/vet/consultations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "dossierId": %d,
                                  "dateConsultation": "2026-11-23",
                                  "motif": "Première visite"
                                }
                                """.formatted(dossierId)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/vet/consultations/{id}", consultationId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dateConsultation").value("2026-11-25"))
                .andExpect(jsonPath("$.motif").value("Contrôle de routine"))
                .andExpect(jsonPath("$.diagnostic").value("Examen normal"))
                .andExpect(jsonPath("$.poids").value(8.4))
                .andExpect(jsonPath("$.temperature").value(38.5))
                .andExpect(jsonPath("$.remarques").value("Suivi habituel"));

        mockMvc.perform(get("/api/vet/records/{id}/consultations", dossierId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].dateConsultation").value("2026-11-25"))
                .andExpect(jsonPath("$[1].dateConsultation").value("2026-11-23"));
    }

    @Test
    void refuseRequeteInvalideEtSignaleRessourcesInconnues() throws Exception {
        mockMvc.perform(post("/api/vet/consultations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"dossierId":1,"motif":" "}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.path").value("/api/vet/consultations"));

        mockMvc.perform(post("/api/vet/consultations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"dossierId":999,"dateConsultation":"2026-11-25","motif":"Contrôle"}
                                """))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));

        mockMvc.perform(get("/api/vet/consultations/999"))
                .andExpect(status().isNotFound());
        mockMvc.perform(get("/api/vet/records/999/consultations"))
                .andExpect(status().isNotFound());
    }
}
