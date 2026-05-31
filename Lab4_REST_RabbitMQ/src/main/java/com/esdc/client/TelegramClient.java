package com.esdc.client;

import com.esdc.dto.TelegramResponse;
import com.esdc.dto.TelegramUpdate;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class TelegramClient {
    private final RestClient restClient;

    public TelegramClient(String token) {
        this.restClient = RestClient.builder()
                .baseUrl("https://api.telegram.org/bot" + token + "/")
                .build();
    }

    public List<TelegramUpdate> getUpdates(Integer offset) {
        return Optional.ofNullable(
                restClient.get()
                        .uri(offset != null ? "getUpdates?offset={offset}" : "getUpdates", offset)
                        .accept(MediaType.APPLICATION_JSON)
                        .retrieve()
                        .toEntity(TelegramResponse.class)
                        .getBody()
        ).map(TelegramResponse::result).orElse(List.of());
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
