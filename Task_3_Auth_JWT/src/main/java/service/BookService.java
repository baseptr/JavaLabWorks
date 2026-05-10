package service;

import dto.BookRequest;
import entity.Book;
import entity.Library;
import exception.BookAlreadyExistException;
import exception.BookNotFoundException;
import exception.BookStorageEmptyException;
import exception.LibraryNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import repository.BookRepository;
import repository.LibraryRepository;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class BookService {

    private final BookRepository bookRepository;
    private final LibraryRepository libraryRepository;

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
        Library library = request.libraryId() != null
                ? libraryRepository.findById(request.libraryId())
                        .orElseThrow(() -> new LibraryNotFoundException("Library not found: " + request.libraryId()))
                : libraryRepository.getDefault();

        boolean alreadyInLibrary = library.getBooks().stream()
                .anyMatch(b -> b.getIsbn().equals(request.isbn()));
        if (alreadyInLibrary) {
            throw new BookAlreadyExistException("Book with ISBN " + request.isbn() + " already exists in library " + library.getId());
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
        library.getBooks().add(book);
        log.info("Created book with ISBN {} in library {}", request.isbn(), library.getId());
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
