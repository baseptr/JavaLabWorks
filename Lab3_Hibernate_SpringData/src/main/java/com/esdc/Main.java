package com.esdc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {
     void main(String[] args) {
        SpringApplication.run(Main.class, args);
        System.out.println("""
                App started at http://localhost:8080
                Docs json http://localhost:8080/v3/api-docs
                Swagger http://localhost:8080/swagger-ui/index.html
                """);
    }
}
