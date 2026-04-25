package com.esdc.repository.impl;

import com.esdc.entity.Book;
import com.esdc.entity.Library;
import com.esdc.repository.LibraryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@Profile("jdbctemplate")
@RequiredArgsConstructor
public class LibraryJdbcTemplateRepository implements LibraryRepository {

    private final JdbcTemplate jdbcTemplate;

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
        List<Library> libraries = jdbcTemplate.query("select * from libraries", libraryRowMapper);
        libraries.forEach(lib -> lib.setBooks(findBooksByLibraryId(lib.getId())));
        return libraries;
    }

    @Override
    public Optional<Library> findById(Long id) {
        try {
            Library lib = jdbcTemplate.queryForObject("select * from libraries where id = ?", libraryRowMapper, id);
            if (lib != null) {
                lib.setBooks(findBooksByLibraryId(lib.getId()));
            }
            return Optional.ofNullable(lib);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
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
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    "insert into libraries (name, address, founded_year) values (?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, library.getName());
            ps.setString(2, library.getAddress());
            ps.setInt(3, library.getFoundedYear());
            return ps;
        }, keyHolder);
        if (keyHolder.getKey() != null) {
            library.setId(keyHolder.getKey().longValue());
        }
    }

    @Override
    public void update(Library library) {
        jdbcTemplate.update(
                "update libraries set name = ?, address = ?, founded_year = ? where id = ?",
                library.getName(), library.getAddress(), library.getFoundedYear(), library.getId()
        );
    }

    @Override
    public void delete(Long id) {
        jdbcTemplate.update("delete from libraries where id = ?", id);
    }

    private List<Book> findBooksByLibraryId(Long libraryId) {
        return jdbcTemplate.query("select * from books where library_id = ?", bookRowMapper, libraryId);
    }
}
