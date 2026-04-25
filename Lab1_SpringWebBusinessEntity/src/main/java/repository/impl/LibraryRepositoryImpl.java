package repository.impl;

import entity.Library;
import org.springframework.stereotype.Repository;
import repository.LibraryRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class LibraryRepositoryImpl implements LibraryRepository {
    private final List<Library> libraries = new ArrayList<>();
    private long nextId;

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
            Library defaultLibrary = Library.builder()
                    .name("Default Library")
                    .address("Unknown")
                    .foundedYear(2000)
                    .books(new ArrayList<>())
                    .build();
            create(defaultLibrary);
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
                    l.setAddress(library.getAddress());
                    l.setName(library.getName());
                    l.setFoundedYear(library.getFoundedYear());
                    l.setBooks(library.getBooks());
                });
    }

    @Override
    public void delete(Long id) {
        libraries.removeIf(l -> l.getId().equals(id));
    }
}
