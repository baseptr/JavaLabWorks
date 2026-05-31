package com.esdc.controller;

import com.esdc.client.TelegramClient;
import com.esdc.dto.OutgoingMessage;
import com.esdc.dto.TelegramUpdate;
import com.esdc.messaging.MessageProducer;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bot")
public class BotController {

    private final TelegramClient telegramClient;
    private final MessageProducer messageProducer;

    public BotController(TelegramClient telegramClient, MessageProducer messageProducer) {
        this.telegramClient = telegramClient;
        this.messageProducer = messageProducer;
    }

    @GetMapping("/fetch")
    public List<TelegramUpdate> fetchAndPublish(@RequestParam(required = false) Integer offset) {
        var updates = telegramClient.getUpdates(offset);
        updates.forEach(messageProducer::send);
        return updates;
    }

    @PostMapping("/send")
    public String sendMessage(@RequestParam(defaultValue = "600454181") Long chatId, @RequestParam String text) {
        telegramClient.sendMessage(chatId, text);
        messageProducer.sendOutgoing(new OutgoingMessage(chatId, text));
        return "Message sent to chat " + chatId;
    }
}
