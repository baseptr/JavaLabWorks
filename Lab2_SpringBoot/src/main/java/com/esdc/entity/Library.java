package com.esdc.entity;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@EqualsAndHashCode
@AllArgsConstructor
public class Library {
    private Long id;
    private String name;
    private String address;
    private int foundedYear;
    private List<Book> books;
}
