package com.esdc.repository.impl;

import com.esdc.entity.Book;
import com.esdc.entity.Library;
import com.esdc.repository.LibraryRepository;
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
public class LibraryJdbcRepository implements LibraryRepository {

    private final DataSource dataSource;

    @Override
    public List<Library> findAll() {
        List<Library> libraries = new ArrayList<>();
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement("select * from libraries");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Library lib = mapRow(rs);
                lib.setBooks(findBooksByLibraryId(con, lib.getId()));
                libraries.add(lib);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return libraries;
    }

    @Override
    public Optional<Library> findById(Long id) {
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement("select * from libraries where id = ?")) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Library lib = mapRow(rs);
                    lib.setBooks(findBooksByLibraryId(con, lib.getId()));
                    return Optional.of(lib);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public Library getDefault() {
        List<Library> all = findAll();
        if (all.isEmpty()) {
            Library def = Library.builder()
                    .name("Default Library")
                    .address("Unknown")
                    .foundedYear(2000)
                    .books(new ArrayList<>())
                    .build();
            create(def);
            return def;
        }
        return all.get(0);
    }

    @Override
    public void create(Library library) {
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "insert into libraries (name, address, founded_year) values (?, ?, ?)",
                     Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, library.getName());
            ps.setString(2, library.getAddress());
            ps.setInt(3, library.getFoundedYear());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    library.setId(keys.getLong(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Library library) {
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "update libraries set name = ?, address = ?, founded_year = ? where id = ?")) {
            ps.setString(1, library.getName());
            ps.setString(2, library.getAddress());
            ps.setInt(3, library.getFoundedYear());
            ps.setLong(4, library.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Long id) {
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement("delete from libraries where id = ?")) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Book> findBooksByLibraryId(Connection con, Long libraryId) throws SQLException {
        List<Book> books = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement("select * from books where library_id = ?")) {
            ps.setLong(1, libraryId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    books.add(mapBookRow(rs));
                }
            }
        }
        return books;
    }

    private Library mapRow(ResultSet rs) throws SQLException {
        return Library.builder()
                .id(rs.getLong("id"))
                .name(rs.getString("name"))
                .address(rs.getString("address"))
                .foundedYear(rs.getInt("founded_year"))
                .books(new ArrayList<>())
                .build();
    }

    private Book mapBookRow(ResultSet rs) throws SQLException {
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
