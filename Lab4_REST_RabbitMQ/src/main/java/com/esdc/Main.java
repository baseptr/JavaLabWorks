package com.esdc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
        System.out.println("""
                ================================
                Swagger UI: http://localhost:8080/swagger-ui/index.html
                RabbitMQ UI: http://localhost:15672 (guest/guest)
                ================================
                """);
    }
}
