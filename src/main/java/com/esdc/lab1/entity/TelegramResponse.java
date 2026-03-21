package com.esdc.lab1.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.*;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = NON_NULL,content = NON_EMPTY)
public record TelegramResponse<T>(boolean ok,
                                  @JsonProperty("description") String desc,
                                  T result,
                                  @JsonProperty("error_code") int errorCode) {
}
