package com.docente.proyectoejemplo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;

@Configuration
public class SnsConfig {

    @Value("${aws.region:us-east-2}")
    private String region;

    @Bean
    public SnsClient snsClient() {
        // Sin keys hardcodeadas: en EC2 usa el IAM Role de la instancia,
        // en local usa ~/.aws/credentials o variables de entorno.
        return SnsClient.builder()
                .region(Region.of(region))
                .build();
    }
}
