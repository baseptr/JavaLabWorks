package service;

import dto.BookRequest;
import entity.Book;
import exception.BookAlreadyExistException;
import exception.BookNotFoundException;
import exception.BookStorageEmptyException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import repository.BookRepository;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class BookService {

    private final BookRepository bookRepository;

    public List<Book> getAll() {
        List<Book> books = bookRepository.findAll();
        if (books.isEmpty()) {
            throw new BookStorageEmptyException("No books in storage");
        }
        return books;
    }

    public Book getById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book not found: " + id));
    }

    public void create(BookRequest request) {
        if (bookRepository.existsByIsbn(request.isbn())) {
            throw new BookAlreadyExistException("Book with ISBN " + request.isbn() + " already exists");
        }
        Book book = Book.builder()
                .title(request.title())
                .author(request.author())
                .isbn(request.isbn())
                .price(request.price())
                .genre(request.genre())
                .publishYear(request.publishYear())
                .build();
        bookRepository.create(book);
        log.info("Created book with ISBN {}", request.isbn());
    }

    public void update(Long id, BookRequest request) {
        bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book not found: " + id));
        Book book = Book.builder()
                .id(id)
                .title(request.title())
                .author(request.author())
                .isbn(request.isbn())
                .price(request.price())
                .genre(request.genre())
                .publishYear(request.publishYear())
                .build();
        bookRepository.update(book);
        log.info("Updated book {}", id);
    }

    public void delete(Long id) {
        bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book not found: " + id));
        bookRepository.delete(id);
        log.info("Deleted book {}", id);
    }
}
