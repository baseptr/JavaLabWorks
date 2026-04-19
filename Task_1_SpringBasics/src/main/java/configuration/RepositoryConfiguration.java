package configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import repository.BeverageRepository;
import repository.impl.JSONBeverageRepository;

@Configuration
public class RepositoryConfiguration {

    @Bean
    @Primary
    BeverageRepository beverageRepository(){
        return new JSONBeverageRepository();
    }
}
