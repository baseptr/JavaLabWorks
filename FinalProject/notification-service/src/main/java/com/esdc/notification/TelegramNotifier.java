package com.esdc.notification;

import com.esdc.notification.client.TelegramClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TelegramNotifier {

    private final TelegramClient client;

    public TelegramNotifier(@Value("${telegram.bot-token:}") String token) {
        this.client = token.isBlank() ? null : new TelegramClient(token);
        if (client == null) {
            log.warn("TELEGRAM_BOT_TOKEN is not set — notifications will only be logged");
        }
    }

    public void notify(String chatId, String text) {
        log.info("notification: chat={} text={}", chatId, text);
        if (client == null || chatId == null) {
            return;
        }
        try {
            client.sendMessage(Long.parseLong(chatId), text);
        } catch (Exception e) {
            log.error("notification: failed to send to chat {}: {}", chatId, e.getMessage());
        }
    }
}
