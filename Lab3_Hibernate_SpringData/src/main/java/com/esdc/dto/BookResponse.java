package com.esdc.dto;

public record BookResponse(Long id,
                           String title,
                           String author,
                           String isbn,
                           Double price,
                           String genre,
                           Integer publishYear,
                           Long libraryId) {
}
