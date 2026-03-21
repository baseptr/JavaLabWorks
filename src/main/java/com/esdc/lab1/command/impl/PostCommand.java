package com.esdc.lab1.command.impl;

import com.esdc.lab1.client.HttpClientWrapper;
import com.esdc.lab1.command.Command;
import com.esdc.lab1.config.AppConfig;

import java.net.http.HttpResponse;
import java.util.Scanner;

public class PostCommand implements Command {
    private final HttpClientWrapper client;

    public PostCommand(HttpClientWrapper client) {
        this.client = client;
    }

    @Override
    public void execute(Scanner sc) {
        System.out.print("Title: ");
        String title = sc.nextLine().trim();
        System.out.print("UserId: ");
        String userId = sc.nextLine().trim();
        System.out.print("Completed (true/false): ");
        String completed = sc.nextLine().trim();

        String body = "{\"title\":\"" + title + "\",\"userId\":" + userId + ",\"completed\":" + completed + "}";

        try {
            HttpResponse<String> response = client.post(AppConfig.get("api_1_url"), body);
            System.out.println("Status: " + response.statusCode());
            System.out.println(response.body());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
