package repository;

import entity.Book;
import entity.Library;

import java.util.List;
import java.util.Optional;

public interface LibraryRepository {
    List<Library> findAll();
    Optional<Library> findById(Long id);
    Library getDefault();
    void create(Library library);
    void update(Library library);
    void delete(Long id);
}
