package config;

import controller.WelcomeController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
import repository.BeverageRepository;
import repository.impl.JSONBeverageRepository;

import java.util.Map;


@ComponentScan(basePackages = {"controller", "service", "repository", "entity"})
@EnableWebMvc
@Configuration
public class WebConfig  {

    @Bean
    public SimpleUrlHandlerMapping urlHandlerMapping(WelcomeController wc){
        return new SimpleUrlHandlerMapping(Map.of("/",wc));
    }

    @Bean
    public BeverageRepository get(){
        return JSONBeverageRepository.getInstance();
    }
}
