package com.esdc.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties("book")
@Component
@Data
public class BookStorageConfig {
    private String defaultGenre;
    private String defaultAuthor;
    private String defaultTitle;
    private String  defaultIsbn;
    private Double defaultPrice;
    private int defaultPubYear;
    @Value("#{T(java.lang.String).format('Project: %s', '${spring.application.name}')}")
    private String message;
}
