package repository;

import entity.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository {
    List<Book> findAll();
    Optional<Book> findById(Long id);
    void create(Book book);
    void update(Book book);
    void delete(Long id);
    boolean existsByIsbn(String isbn);
}
