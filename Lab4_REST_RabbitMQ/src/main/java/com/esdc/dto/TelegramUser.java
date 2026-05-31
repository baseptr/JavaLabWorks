package com.esdc.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TelegramUser(
        Long id,
        @JsonProperty("first_name") String firstName,
        String username
) {}
