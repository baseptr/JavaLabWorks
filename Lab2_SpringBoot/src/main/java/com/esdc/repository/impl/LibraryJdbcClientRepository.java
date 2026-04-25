package com.esdc.repository.impl;

import com.esdc.entity.Book;
import com.esdc.entity.Library;
import com.esdc.repository.LibraryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@Profile("jdbcclient")
@RequiredArgsConstructor
public class LibraryJdbcClientRepository implements LibraryRepository {

    private final JdbcClient jdbcClient;

    private final RowMapper<Book> bookRowMapper = (rs, rowNum) -> Book.builder()
            .id(rs.getLong("id"))
            .title(rs.getString("title"))
            .author(rs.getString("author"))
            .isbn(rs.getString("isbn"))
            .price(rs.getDouble("price"))
            .genre(rs.getString("genre"))
            .publishYear(rs.getInt("publish_year"))
            .libraryId(rs.getObject("library_id", Long.class))
            .build();

    private final RowMapper<Library> libraryRowMapper = (rs, rowNum) -> Library.builder()
            .id(rs.getLong("id"))
            .name(rs.getString("name"))
            .address(rs.getString("address"))
            .foundedYear(rs.getInt("founded_year"))
            .books(new ArrayList<>())
            .build();

    @Override
    public List<Library> findAll() {
        List<Library> libraries = jdbcClient.sql("select * from libraries")
                .query(libraryRowMapper)
                .list();
        libraries.forEach(lib -> lib.setBooks(findBooksByLibraryId(lib.getId())));
        return libraries;
    }

    @Override
    public Optional<Library> findById(Long id) {
        Optional<Library> library = jdbcClient.sql("select * from libraries where id = :id")
                .param("id", id)
                .query(libraryRowMapper)
                .optional();
        library.ifPresent(lib -> lib.setBooks(findBooksByLibraryId(lib.getId())));
        return library;
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
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcClient.sql("insert into libraries (name, address, founded_year) values (:name, :address, :foundedYear)")
                .param("name", library.getName())
                .param("address", library.getAddress())
                .param("foundedYear", library.getFoundedYear())
                .update(keyHolder);
        if (keyHolder.getKey() != null) {
            library.setId(keyHolder.getKey().longValue());
        }
    }

    @Override
    public void update(Library library) {
        jdbcClient.sql("update libraries set name = :name, address = :address, founded_year = :foundedYear where id = :id")
                .param("name", library.getName())
                .param("address", library.getAddress())
                .param("foundedYear", library.getFoundedYear())
                .param("id", library.getId())
                .update();
    }

    @Override
    public void delete(Long id) {
        jdbcClient.sql("delete from libraries where id = :id")
                .param("id", id)
                .update();
    }

    private List<Book> findBooksByLibraryId(Long libraryId) {
        return jdbcClient.sql("select * from books where library_id = :libraryId")
                .param("libraryId", libraryId)
                .query(bookRowMapper)
                .list();
    }
}
