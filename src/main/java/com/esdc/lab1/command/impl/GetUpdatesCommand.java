package com.esdc.lab1.command.impl;

import com.esdc.lab1.client.ApiHttpClient;
import com.esdc.lab1.command.Command;
import com.esdc.lab1.config.AppConfig;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class GetUpdatesCommand implements Command {
    private final ApiHttpClient client;

    public GetUpdatesCommand(ApiHttpClient client) {
        this.client = client;
    }

    @Override
    public void execute(Scanner sc) {
        try {
            System.out.println("Enter offset (0 for all): ");
            String offsetInput = sc.nextLine().trim();

            int offset = offsetInput.isEmpty() ? 0 : Integer.parseInt(offsetInput);
            int lastUpdateId = 0;
            ObjectMapper om = new ObjectMapper();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
            String url = AppConfig.get("api_2_url") + AppConfig.get("api_2_key") + "/getUpdates?offset=" + offset;

            HttpResponse<String> response = client.get(url);
            System.out.println("Status: " + response.statusCode());
            JsonNode root = om.readTree(response.body());
            JsonNode updates = root.get("result");
            if(updates.isEmpty())
            {
                System.out.println("No new updates");
                return;
            }

            for (var update : updates) {
                int updateId = update.get("update_id").asInt();
                lastUpdateId = Math.max(lastUpdateId, updateId);
                JsonNode message = update.get("message");
                if (message != null) {
                    long timestamp = message.get("date").asLong();
                    LocalDateTime dateTime = LocalDateTime.ofEpochSecond(timestamp, 0, ZoneOffset.of("+03:00"));

                    System.out.println("Update ID: " + updateId);
                    System.out.println("From: " + message.get("from").get("first_name").asText());
                    System.out.println("Text: " + message.get("text").asText());
                    System.out.println("Date: " + dateTime.format(formatter));
                    System.out.println("---");

                }
            }
            System.out.println("Next offset: " + (lastUpdateId + 1));
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
