package com.esdc.repository.impl;

import com.esdc.entity.Book;
import com.esdc.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@Profile("jdbc")
@RequiredArgsConstructor
public class BookJdbcRepository implements BookRepository {

    private final DataSource dataSource;

    @Override
    public List<Book> findAll() {
        List<Book> books = new ArrayList<>();
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement("select * from books");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                books.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return books;
    }

    @Override
    public Optional<Book> findById(Long id) {
        Book b = null;
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement("select * from books where id = ?")) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    b = mapRow(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.ofNullable(b);
    }

    @Override
    public void create(Book book) {
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "insert into books (title, author, isbn, price, genre, publish_year, library_id) values (?, ?, ?, ?, ?, ?, ?)")) {
            ps.setString(1, book.getTitle());
            ps.setString(2, book.getAuthor());
            ps.setString(3, book.getIsbn());
            ps.setDouble(4, book.getPrice());
            ps.setString(5, book.getGenre());
            ps.setInt(6, book.getPublishYear());
            ps.setObject(7, book.getLibraryId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Book book) {
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "update books set title = ?, author = ?, isbn = ?, price = ?, genre = ?, publish_year = ?, library_id = ? where id = ?")) {
            ps.setString(1, book.getTitle());
            ps.setString(2, book.getAuthor());
            ps.setString(3, book.getIsbn());
            ps.setDouble(4, book.getPrice());
            ps.setString(5, book.getGenre());
            ps.setInt(6, book.getPublishYear());
            ps.setObject(7, book.getLibraryId());
            ps.setLong(8, book.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Long id) {
        try(Connection con = dataSource.getConnection();
            PreparedStatement ps = con.prepareStatement("delete from books where id = ?"))
        {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean existsByIsbn(String isbn) {
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement("select 1 from books where isbn = ?")) {
            ps.setString(1, isbn);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Book mapRow(ResultSet rs) throws SQLException {
        return Book.builder()
                .id(rs.getLong("id"))
                .title(rs.getString("title"))
                .author(rs.getString("author"))
                .isbn(rs.getString("isbn"))
                .price(rs.getDouble("price"))
                .genre(rs.getString("genre"))
                .publishYear(rs.getInt("publish_year"))
                .libraryId(rs.getObject("library_id", Long.class))
                .build();
    }
}
