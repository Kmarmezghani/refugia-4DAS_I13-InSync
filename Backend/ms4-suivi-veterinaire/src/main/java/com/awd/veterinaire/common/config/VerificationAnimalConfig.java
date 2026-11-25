package com.awd.veterinaire.common.config;

import com.awd.veterinaire.service.VerificationAnimal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Set;

@Configuration
public class VerificationAnimalConfig {

    @Bean
    @Profile("dev")
    VerificationAnimal verificationAnimalDev(@Value("${ms4.dev.animal-ids:1,2,12}") Set<Long> animauxConnus) {
        return animalId -> {
            if (!animauxConnus.contains(animalId)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Animal introuvable : " + animalId);
            }
        };
    }

    @Bean
    @Profile("!dev")
    VerificationAnimal verificationAnimalSansMs1() {
        return animalId -> {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                    "La vérification des animaux auprès de MS1 est indisponible.");
        };
    }
}
