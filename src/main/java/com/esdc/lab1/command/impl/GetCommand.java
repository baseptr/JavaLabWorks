package com.esdc.lab1.command.impl;

import com.esdc.lab1.client.ApiHttpClient;
import com.esdc.lab1.command.Command;
import com.esdc.lab1.config.AppConfig;

import java.net.http.HttpResponse;
import java.util.Scanner;

public class GetCommand implements Command {
    private final ApiHttpClient client;

    public GetCommand(ApiHttpClient client) {
        this.client = client;
    }

    @Override
    public void execute(Scanner sc) {
        System.out.print("Enter ID: ");
        String id = sc.nextLine().trim();
        try {
            HttpResponse<String> response = client.get(AppConfig.get("api_1_url") + id);
            System.out.println("Status: " + response.statusCode());
            System.out.println(response.body());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
