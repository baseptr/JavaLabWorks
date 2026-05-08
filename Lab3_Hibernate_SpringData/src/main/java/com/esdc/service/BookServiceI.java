package com.esdc.service;

import com.esdc.dto.BookRequest;
import com.esdc.dto.BookResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BookServiceI {
    List<BookResponse> getBooks();
    BookResponse findById(Long id);
    boolean existsByIsbn(String isbn);
    void save(BookRequest request);
    void update(Long id, BookRequest request);
    void delete(Long id);
    Page<BookResponse> getBooks(Pageable pageable);
    List<BookResponse> findBooksWithCostLessThanAndLibraryName(Double cost, String name);
}
