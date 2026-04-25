package com.esdc.repository.impl;

import com.esdc.entity.Book;
import com.esdc.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Profile("jdbcclient")
@RequiredArgsConstructor
public class BookJdbcClientRepository implements BookRepository {

    private final JdbcClient jdbcClient;

    private final RowMapper<Book> rowMapper = (rs, rowNum) -> Book.builder()
            .id(rs.getLong("id"))
            .title(rs.getString("title"))
            .author(rs.getString("author"))
            .isbn(rs.getString("isbn"))
            .price(rs.getDouble("price"))
            .genre(rs.getString("genre"))
            .publishYear(rs.getInt("publish_year"))
            .libraryId(rs.getObject("library_id", Long.class))
            .build();

    @Override
    public List<Book> findAll() {
        return jdbcClient.sql("select * from books")
                .query(rowMapper)
                .list();
    }

    @Override
    public Optional<Book> findById(Long id) {
        return jdbcClient.sql("select * from books where id = :id")
                .param("id", id)
                .query(rowMapper)
                .optional();
    }

    @Override
    public void create(Book book) {
        jdbcClient.sql("insert into books (title, author, isbn, price, genre, publish_year, library_id) values (:title, :author, :isbn, :price, :genre, :publishYear, :libraryId)")
                .param("title", book.getTitle())
                .param("author", book.getAuthor())
                .param("isbn", book.getIsbn())
                .param("price", book.getPrice())
                .param("genre", book.getGenre())
                .param("publishYear", book.getPublishYear())
                .param("libraryId", book.getLibraryId())
                .update();
    }

    @Override
    public void update(Book book) {
        jdbcClient.sql("update books set title = :title, author = :author, isbn = :isbn, price = :price, genre = :genre, publish_year = :publishYear, library_id = :libraryId where id = :id")
                .param("title", book.getTitle())
                .param("author", book.getAuthor())
                .param("isbn", book.getIsbn())
                .param("price", book.getPrice())
                .param("genre", book.getGenre())
                .param("publishYear", book.getPublishYear())
                .param("libraryId", book.getLibraryId())
                .param("id", book.getId())
                .update();
    }

    @Override
    public void delete(Long id) {
        jdbcClient.sql("delete from books where id = :id")
                .param("id", id)
                .update();
    }

    @Override
    public boolean existsByIsbn(String isbn) {
        return jdbcClient.sql("select 1 from books where isbn = :isbn")
                .param("isbn", isbn)
                .query(Integer.class)
                .optional()
                .isPresent();
    }
}
