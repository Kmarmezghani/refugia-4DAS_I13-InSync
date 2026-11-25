package com.awd.veterinaire;

import com.awd.veterinaire.entity.DossierMedical;
import com.awd.veterinaire.repository.DossierMedicalRepository;
import com.awd.veterinaire.repository.VaccinationRepository;
import com.awd.veterinaire.service.RappelVaccination;
import com.awd.veterinaire.service.RappelVaccinationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = "spring.datasource.url=jdbc:h2:mem:veterinaire_vaccination_test;MODE=PostgreSQL;DATABASE_TO_LOWER=TRUE")
@AutoConfigureMockMvc
@ActiveProfiles({"test", "dev"})
@RecordApplicationEvents
class VaccinationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DossierMedicalRepository dossiers;

    @Autowired
    private VaccinationRepository vaccinations;

    @Autowired
    private RappelVaccinationService rappels;

    @Autowired
    private ApplicationEvents events;

    @MockitoBean
    private Clock horloge;

    @BeforeEach
    void initialiser() {
        when(horloge.getZone()).thenReturn(ZoneOffset.UTC);
        when(horloge.instant()).thenReturn(Instant.parse("2026-11-25T09:00:00Z"));
        vaccinations.deleteAll();
    }

    @Test
    void enregistreFiltreEtCorrigeLesVaccinations() throws Exception {
        Long dossierId = creerDossier(12L);
        enregistrer(dossierId, "2026-11-24")
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.statut").value("EN_RETARD"));
        String reponse = enregistrer(dossierId, "2026-12-02")
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.statut").value("A_RAPPELER"))
                .andExpect(jsonPath("$.rappelPublie").value(false))
                .andReturn().getResponse().getContentAsString();
        long id = objectMapper.readTree(reponse).path("id").asLong();
        enregistrer(dossierId, "2026-12-03")
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.statut").value("A_JOUR"));

        mockMvc.perform(get("/api/vet/records/{id}/vaccinations", dossierId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));
        mockMvc.perform(get("/api/vet/vaccinations/reminders")
                        .param("beforeDate", "2026-12-02").param("statut", "A_RAPPELER"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
        mockMvc.perform(get("/api/vet/vaccinations/reminders")
                        .param("beforeDate", "2026-12-03").param("statut", "A_RAPPELER"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(id));

        corriger(id, """
                {"dateRappel":null}
                """)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dateRappel").value(nullValue()))
                .andExpect(jsonPath("$.dateVaccination").value("2026-11-01"))
                .andExpect(jsonPath("$.statut").value("A_JOUR"));
        mockMvc.perform(get("/api/vet/vaccinations/reminders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void simuleUnSeulEvenementParDateDeRappel() throws Exception {
        Long dossierId = creerDossier(13L);
        String reponse = enregistrer(dossierId, "2026-11-25")
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        long id = objectMapper.readTree(reponse).path("id").asLong();
        enregistrer(dossierId, "2026-11-24").andExpect(status().isCreated());
        enregistrer(dossierId, "2026-12-03").andExpect(status().isCreated());

        mockMvc.perform(get("/api/vet/vaccinations/reminders")).andExpect(status().isOk());
        assertThat(events.stream(RappelVaccination.class)).isEmpty();
        rappels.verifierRappels();
        rappels.verifierRappels();

        corriger(id, """
                {"vaccin":"Rage corrigé","dateRappel":"2026-11-25"}
                """)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rappelPublie").value(true));
        rappels.verifierRappels();
        assertThat(events.stream(RappelVaccination.class))
                .containsExactly(new RappelVaccination(13L, "Rage", LocalDate.of(2026, 11, 25)));

        corriger(id, """
                {"dateRappel":"2026-11-26"}
                """)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rappelPublie").value(false));
        rappels.verifierRappels();
        rappels.verifierRappels();
        assertThat(events.stream(RappelVaccination.class)).hasSize(2);

        when(horloge.instant()).thenReturn(Instant.parse("2026-11-27T09:00:00Z"));
        mockMvc.perform(get("/api/vet/vaccinations/reminders").param("statut", "EN_RETARD"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void refuseLesDatesEtLesIdentifiantsInvalides() throws Exception {
        long id = creerDossier(14L);
        enregistrer(id, "2026-10-31").andExpect(status().isBadRequest());
        enregistrer(999L, "2026-11-25").andExpect(status().isNotFound());
        corriger(999L, """
                {"vaccin":"Rage"}
                """)
                .andExpect(status().isNotFound());
        corriger(999L, "{}").andExpect(status().isBadRequest());
        corriger(999L, """
                {"vaccin":null,"dateRappel":"2026-11-25"}
                """)
                .andExpect(status().isBadRequest());
        mockMvc.perform(get("/api/vet/records/999/vaccinations")).andExpect(status().isNotFound());
        mockMvc.perform(get("/api/vet/vaccinations/reminders").param("beforeDate", "25/11/2026"))
                .andExpect(status().isBadRequest());
    }

    private Long creerDossier(Long animalId) {
        return dossiers.save(new DossierMedical(animalId, "VET-" + animalId, LocalDate.of(2026, 11, 1))).getId();
    }

    private ResultActions enregistrer(Long dossierId, String dateRappel) throws Exception {
        return mockMvc.perform(post("/api/vet/vaccinations")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"dossierId":%d,"vaccin":"Rage","dateVaccination":"2026-11-01","dateRappel":"%s"}
                        """.formatted(dossierId, dateRappel)));
    }

    private ResultActions corriger(long id, String contenu) throws Exception {
        return mockMvc.perform(patch("/api/vet/vaccinations/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(contenu));
    }
}
