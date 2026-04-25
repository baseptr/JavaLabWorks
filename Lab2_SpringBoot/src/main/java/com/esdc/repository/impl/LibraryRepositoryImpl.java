package com.esdc.repository.impl;

import com.esdc.entity.Book;
import com.esdc.entity.Library;
import com.esdc.repository.LibraryRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@Profile("default")
public class LibraryRepositoryImpl implements LibraryRepository {

    private final List<Library> libraries = new ArrayList<>();
    private long nextId = 1;

    @Override
    public List<Library> findAll() {
        return libraries;
    }

    @Override
    public Optional<Library> findById(Long id) {
        return libraries.stream()
                .filter(l -> l.getId().equals(id))
                .findFirst();
    }

    @Override
    public Library getDefault() {
        if (libraries.isEmpty()) {
            Library def = Library.builder()
                    .name("Default Library")
                    .address("Unknown")
                    .foundedYear(2000)
                    .books(new ArrayList<>())
                    .build();
            create(def);
        }
        return libraries.get(0);
    }

    @Override
    public void create(Library library) {
        library.setId(nextId++);
        libraries.add(library);
    }

    @Override
    public void update(Library library) {
        libraries.stream()
                .filter(l -> l.getId().equals(library.getId()))
                .findFirst()
                .ifPresent(l -> {
                    l.setName(library.getName());
                    l.setAddress(library.getAddress());
                    l.setFoundedYear(library.getFoundedYear());
                });
    }

    @Override
    public void delete(Long id) {
        libraries.removeIf(l -> l.getId().equals(id));
    }
}
