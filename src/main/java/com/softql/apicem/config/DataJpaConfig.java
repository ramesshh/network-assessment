package com.softql.apicem.config;

import com.softql.apicem.domain.User;
import com.softql.apicem.security.SecurityUtil;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = {"com.softql.apicem"})
@EnableJpaAuditing(auditorAwareRef = "auditor")
public class DataJpaConfig {

    @Bean
    public AuditorAware<User> auditor() {
        return () -> SecurityUtil.currentUser();
    }

}
