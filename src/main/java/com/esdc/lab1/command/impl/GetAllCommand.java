package com.esdc.lab1.command.impl;

import com.esdc.lab1.client.HttpClientWrapper;
import com.esdc.lab1.command.Command;
import com.esdc.lab1.config.AppConfig;

import java.net.http.HttpResponse;
import java.util.Scanner;

public class GetAllCommand implements Command {
    private final HttpClientWrapper client;

    public GetAllCommand(HttpClientWrapper client) {
        this.client = client;
    }

    @Override
    public void execute(Scanner sc) {
        try {
            HttpResponse<String> response = client.get(AppConfig.get("api_1_url"));
            System.out.println("Status: " + response.statusCode());
            System.out.println(response.body());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
