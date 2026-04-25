package com.esdc.repository.impl;

import com.esdc.entity.Book;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import com.esdc.repository.BookRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Profile("default")
@Repository
public class BookRepositoryImpl implements BookRepository {
    private final List<Book> books = new ArrayList<>();
    private long nextId = 1;

    @Override
    public List<Book> findAll() {
        return books;
    }

    @Override
    public Optional<Book> findById(Long id) {
        return books.stream()
                .filter(b -> b.getId().equals(id))
                .findFirst();
    }

    @Override
    public void create(Book book) {
        book.setId(nextId++);
        books.add(book);
    }

    @Override
    public void update(Book book) {
        books.stream()
                .filter(b -> b.getId().equals(book.getId()))
                .findFirst()
                .ifPresent(b -> {
                    b.setAuthor(book.getAuthor());
                    b.setIsbn(book.getIsbn());
                    b.setPrice(book.getPrice());
                    b.setGenre(book.getGenre());
                    b.setTitle(book.getTitle());
                    b.setPublishYear(book.getPublishYear());
                });
    }

    @Override
    public void delete(Long id) {
        books.removeIf(b -> b.getId().equals(id));
    }

    @Override
    public boolean existsByIsbn(String isbn) {
        return books.stream()
                .anyMatch(b -> b.getIsbn().equals(isbn));
    }
}
