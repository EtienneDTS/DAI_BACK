package com.pickandgo.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();

        // Désactiver la sérialisation des références circulaires
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

        // Option 1: Ignorer les références circulaires
        objectMapper.configure(SerializationFeature.FAIL_ON_SELF_REFERENCES, false);

        // Option 2: Pour une meilleure solution, utilisez des annotations dans vos entités
        // @JsonManagedReference et @JsonBackReference

        // Support pour les dates Java 8
        objectMapper.registerModule(new JavaTimeModule());

        return objectMapper;
    }
}