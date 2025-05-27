package com.pickandgo.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();

        // Enregistre le module pour les types Java 8 (LocalDate, etc.)
        mapper.registerModule(new JavaTimeModule());

        // Évite les erreurs liées aux objets vides
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

        // Pour éviter les problèmes de sérialisation de dates (optionnel)
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        return mapper;
    }
}
