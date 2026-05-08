package com.esdc.entity;

import jakarta.persistence.*;
import lombok.*;


import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "libraries")
@Entity
public class Library {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String address;
    private int foundedYear;
    @ToString.Exclude
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true, mappedBy = "library")
    private List<Book> books;
}


