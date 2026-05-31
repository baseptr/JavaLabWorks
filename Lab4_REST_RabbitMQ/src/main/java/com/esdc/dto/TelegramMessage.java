package com.esdc.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TelegramMessage(
        @JsonProperty("message_id") Long messageId,
        TelegramUser from,
        String text,
        Long date
) {}
