package com.esdc.repository.impl;

import com.esdc.entity.Book;
import com.esdc.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import org.springframework.dao.EmptyResultDataAccessException;

import java.util.List;
import java.util.Optional;

@Repository
@Profile("jdbctemplate")
@RequiredArgsConstructor
public class BookJdbcTemplateRepository implements BookRepository {

    private final JdbcTemplate jdbcTemplate;

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
        return jdbcTemplate.query("select * from books", rowMapper);
    }

    @Override
    public Optional<Book> findById(Long id) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject("select * from books where id = ?", rowMapper, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public void create(Book book) {
        jdbcTemplate.update(
                "insert into books (title, author, isbn, price, genre, publish_year, library_id) values (?, ?, ?, ?, ?, ?, ?)",
                book.getTitle(), book.getAuthor(), book.getIsbn(),
                book.getPrice(), book.getGenre(), book.getPublishYear(), book.getLibraryId()
        );
    }

    @Override
    public void update(Book book) {
        jdbcTemplate.update(
                "update books set title = ?, author = ?, isbn = ?, price = ?, genre = ?, publish_year = ?, library_id = ? where id = ?",
                book.getTitle(), book.getAuthor(), book.getIsbn(),
                book.getPrice(), book.getGenre(), book.getPublishYear(), book.getLibraryId(), book.getId()
        );
    }

    @Override
    public void delete(Long id) {
        jdbcTemplate.update("delete from books where id = ?", id);
    }

    @Override
    public boolean existsByIsbn(String isbn) {
        try {
            jdbcTemplate.queryForObject("select 1 from books where isbn = ?", Integer.class, isbn);
            return true;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }
}
