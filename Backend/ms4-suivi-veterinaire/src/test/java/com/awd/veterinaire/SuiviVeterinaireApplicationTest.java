package com.awd.veterinaire;

import com.netflix.discovery.EurekaClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SuiviVeterinaireApplicationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ApplicationContext context;

    @Test
    void demarreAvecH2EtExposeSwagger() throws Exception {
        assertThat(context.getBeansOfType(EurekaClient.class)).isEmpty();

        mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.openapi").isNotEmpty());

        mockMvc.perform(get("/swagger-ui.html"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/swagger-ui/index.html"));

        mockMvc.perform(get("/swagger-ui/index.html"))
                .andExpect(status().isOk());
    }

    @Test
    void refuseLouvertureSiMs1EstIndisponible() throws Exception {
        mockMvc.perform(post("/api/vet/records")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"animalId":12}
                                """))
                .andExpect(status().isServiceUnavailable())
                .andExpect(jsonPath("$.status").value(503));

        mockMvc.perform(get("/api/vet/records/by-animal/12"))
                .andExpect(status().isNotFound());
    }
}
