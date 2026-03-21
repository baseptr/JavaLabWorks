package com.esdc.lab1.command.impl;

import com.esdc.lab1.client.ApiHttpClient;
import com.esdc.lab1.command.Command;
import com.esdc.lab1.config.AppConfig;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.http.HttpResponse;
import java.util.Scanner;

public class SendMessageCommand implements Command {
    private final ApiHttpClient client;

    public SendMessageCommand(ApiHttpClient client) {
        this.client = client;
    }

    @Override
    public void execute(Scanner sc) {
        try {
            ObjectMapper om = new ObjectMapper();
            String url = AppConfig.get("api_2_url") + AppConfig.get("api_2_key") + "/sendMessage";
            System.out.println("Enter chat_id in this format(int = 27387287 or String = @channelusername): ");
            String chatId = sc.nextLine().trim();
            System.out.println("Enter message: ");
            String msg = sc.nextLine().trim();

            String body = """
                    {
                    "chat_id": "%s",
                    "text": "%s"
                    }
                    """.formatted(chatId, msg);
            HttpResponse<String> response = client.post(url, body);
            System.out.println("Status: " + response.statusCode());
            JsonNode root = om.readTree(response.body());
            System.out.println(om.writerWithDefaultPrettyPrinter().writeValueAsString(root));
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
