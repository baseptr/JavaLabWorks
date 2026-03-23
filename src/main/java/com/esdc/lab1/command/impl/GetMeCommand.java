package com.esdc.lab1.command.impl;

import com.esdc.lab1.client.ApiHttpClient;
import com.esdc.lab1.command.Command;
import com.esdc.lab1.config.AppConfig;
import com.esdc.lab1.entity.TelegramResponse;
import com.esdc.lab1.entity.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.util.Scanner;

@Slf4j
public class GetMeCommand implements Command {
    private final ApiHttpClient client;

    public GetMeCommand(ApiHttpClient client) {
        this.client = client;
    }

    @Override
    public void execute(Scanner sc) {
        try {
            String url = AppConfig.get("api_2_url") + AppConfig.get("api_2_key") + "/getMe";

            var response = client.get(url);
            System.out.println("Status: " + response.statusCode());

            ObjectMapper mapper = new ObjectMapper();
            TelegramResponse<User> bot = mapper.readValue(response.body(), new TypeReference<TelegramResponse<User>>(){});

            if (bot.ok()) {
                System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(bot));
            } else {
                System.out.println("Error: API returned ok=false");
            }

        } catch (Exception e) {
            log.error("Error with req /getMe command\n{}", e.getMessage());
        }
    }
}

