package com.esdc.lab3.configuration;

import com.esdc.lab3.repository.BeverageRepository;
import com.esdc.lab3.repository.impl.JSONBeverageRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class RepositoryConfiguration {

    @Bean
    @Primary
    BeverageRepository beverageRepository(){
        return JSONBeverageRepository.getInstance();
    }
}
