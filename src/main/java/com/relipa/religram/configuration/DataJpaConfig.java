package com.relipa.religram.configuration;

import com.relipa.religram.entity.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Configuration
@EnableJpaAuditing
public class DataJpaConfig {

    @Bean
    public AuditorAware<String> auditor() {
        return () -> Optional.ofNullable("relipa");
    }
}