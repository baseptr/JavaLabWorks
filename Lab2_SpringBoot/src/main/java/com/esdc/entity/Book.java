package com.esdc.entity;

import lombok.*;

@Getter
@Setter
@Builder
@EqualsAndHashCode
@AllArgsConstructor
public class Book {
    private Long id;
    private String title;
    private String author;
    private String isbn;
    private Double price;
    private String genre;
    private int publishYear;
    private Long libraryId;
}
