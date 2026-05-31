package com.esdc.dto;

import java.util.List;

public record TelegramResponse(boolean ok, List<TelegramUpdate> result) {}
