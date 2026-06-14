package com.esdc.notification.client;

import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

import java.util.Map;

public class TelegramClient {

    private final RestClient restClient;

    public TelegramClient(String token) {
        this.restClient = RestClient.builder()
                .baseUrl("https://api.telegram.org/bot" + token + "/")
                .build();
    }

    public void sendMessage(Long chatId, String text) {
        restClient.post()
                .uri("sendMessage")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of("chat_id", chatId, "text", text))
                .retrieve()
                .toBodilessEntity();
    }
}
